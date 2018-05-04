package com.newaim.purchase.flow.workflow.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDao;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.workflow.entity.FlowObject;
import com.newaim.purchase.flow.workflow.entity.MyAgentSetting;
import com.newaim.purchase.flow.workflow.vo.TaskVo;

/**
 * Created by Mark on 2017/10/4.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ActivitiService extends ServiceBase {

    /**用户节点*/
    private final static String NODE_USER_TASK = "userTask";
    /**网关节点*/
    private final static String NODE_EXCLUSIVE_GATEWAY = "exclusiveGateway";
    /**结束节点*/
    private final static String NODE_END_EVENT= "endEvent";
    /**type属性*/
    private final static String PROPERTY_TYPE = "type";
    /**conditionText属性*/
    private final static String PROPERTY_CONDITION_TEXT= "conditionText";
    private final static String PREFIX_ROLE= "role:";
    private final static String PREFIX_FIND_TYPE= "findType:";

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyAgentSettingService myAgentSettingService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private FlowOperatorHistoryService flowOperatorHistoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ReportsService reportsService;

    /**
     * 分页获取所有待办任务
     */
    public Page<TaskVo> findAllTodoTask(PageRequest pageRequest, String keywords){
        UserVo user = SessionUtils.currentUserVo();
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(user.getAccount());
        if(StringUtils.isNotBlank(keywords)){
            taskQuery.taskNameLike(keywords);
        }
        long total = taskQuery.count();
        List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageRequest.getOffset(), pageRequest.getPageSize());
        Page<Task> p = new PageImpl<>(tasks, pageRequest, total);
        Page<TaskVo> page = p.map(source -> convertToTaskVo(source));
        return page;
    }

    public TaskVo getTaskVo(String taskId){
        return convertToTaskVo(findTaskById(taskId));
    }

    private TaskVo convertToTaskVo(Task task){
        TaskVo taskVo = BeanMapper.map(task, TaskVo.class);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        taskVo.setBusinessId(historicProcessInstance.getBusinessKey());
        if(StringUtils.isNotBlank(historicProcessInstance.getStartUserId())){
            UserVo startUser = userService.findUserVoByAccount(historicProcessInstance.getStartUserId());
            if(startUser != null){
                taskVo.setStartUserId(startUser.getId());
                taskVo.setStartUserName(startUser.getName());
            }
        }
        UserVo assignee = userService.findUserVoByAccount(task.getAssignee());
        taskVo.setAssigneeId(assignee.getId());
        taskVo.setAssigneeName(assignee.getName());
        taskVo.setStartTime(historicProcessInstance.getStartTime());
        taskVo.setEndTime(historicProcessInstance.getEndTime());

        String processDefinitionName= historicProcessInstance.getProcessDefinitionName();

        String taskName = taskVo.getStartUserName() + "[" + processDefinitionName + "]-" + DateFormatUtils.format(taskVo.getStartTime(), "yyyyMMdd");
        taskVo.setTaskName(taskName);
        taskVo.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        return taskVo;
    }

    /**
     * 查找下一节点接收人
     * @param taskId
     * @param variables
     * @param flowNextHandlerAccount
     * @param query
     * @return
     */
    public List<User> nextTaskHandler(String taskId, Map<String, Object> variables, String flowNextHandlerAccount, boolean query, UserVo startUser){

        if("请选择...".equals(flowNextHandlerAccount) || "Please select...".equals(flowNextHandlerAccount)){
            flowNextHandlerAccount = null;
        }
        List<User> users = Lists.newArrayList();
        ActivityImpl activity = nextActivity(taskId);

        if(NODE_USER_TASK.equals(activity.getProperty(PROPERTY_TYPE))){
            //如果multiInstance=parallel  则该节点为多实例userTask节点
            UserTaskActivityBehavior activityBehavior =null;
            if("parallel".equals(activity.getProperty("multiInstance"))) {
            	activityBehavior = (UserTaskActivityBehavior) ((ParallelMultiInstanceBehavior)activity.getActivityBehavior()).getInnerActivityBehavior();
            }else {
                activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
            }
        	
            TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
            Set<Expression> expressions = taskDefinition.getCandidateGroupIdExpressions();
            String roleCode = getRoleCode(expressions);
            //查找接收人方式,默认为向上查找
            String findType = getFindType(expressions);

            users.addAll(findUsers(taskId, roleCode, findType, flowNextHandlerAccount, startUser));
            if(!query){
                Expression e = taskDefinition.getAssigneeExpression();
                if(users.size() == 1){
                    variables.put(StringUtils.substringBetween(e.getExpressionText(), "${", "}"), users.get(0).getAccount());
                }
            }
        }else if(NODE_EXCLUSIVE_GATEWAY.equals(activity.getProperty(PROPERTY_TYPE))){
            //网关节点，继续向下查找节点
            List<PvmTransition> transitions = activity.getOutgoingTransitions();
            String approved = null;
            if(!query){
                Object  varObject = taskService.getVariableLocal(taskId, "approved");
                if(varObject != null){
                    approved = varObject.toString();
                }
            }
            for(PvmTransition t : transitions){
                String conditionText = t.getProperty(PROPERTY_CONDITION_TEXT).toString();
                String conditionKey = StringUtils.trim(StringUtils.substringBetween(conditionText, "${", "="));
                String conditionValue = getConditionValue(conditionText);
                if(!query && containsValue(conditionValue, approved)){
                    variables.put(conditionKey, approved);
                }
                ActivityImpl act = (ActivityImpl) t.getDestination();
                if(NODE_END_EVENT.equals(act.getProperty(PROPERTY_TYPE))){
                    continue;
                }
                UserTaskActivityBehavior activityBehavior =null;
                if("parallel".equals(act.getProperty("multiInstance"))) {
                	activityBehavior = (UserTaskActivityBehavior) ((ParallelMultiInstanceBehavior)act.getActivityBehavior()).getInnerActivityBehavior();
                }else {
                    activityBehavior = (UserTaskActivityBehavior) act.getActivityBehavior();
                }
                TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
                if(NODE_USER_TASK.equals(act.getProperty(PROPERTY_TYPE))){
                    if("0".equals(conditionValue)){
                        //同意时
                        Set<Expression> expressions = taskDefinition.getCandidateGroupIdExpressions();
                        String roleCode = getRoleCode(expressions);
                        //查找接收人方式,默认为向上查找
                        String findType = getFindType(expressions);
                        users = findUsers(taskId, roleCode, findType, flowNextHandlerAccount, startUser);
                        if(!query && containsValue(conditionValue, approved) && users != null && users.size() == 1){
                            Expression e = taskDefinition.getAssigneeExpression();
                            if(users.size() == 1){
                                variables.put(StringUtils.substringBetween(e.getExpressionText(), "${", "}"), users.get(0).getAccount());
                            }
                        }
                    }
                }
            }
        }
        return users;
    }

    /**
     * 查找下一节点接收人
     * @param processInstanceId 流程实例id
     * @return
     */
    public List<UserVo> findNextTaskHandler(String processInstanceId){
        List<UserVo> result = Lists.newArrayList();
        
        if(StringUtils.isNotBlank(processInstanceId)){
        	//如果下一节点是会签节点
        	if(checkNextJoinTask(processInstanceId)) {
        		return result;
        	}
        	//如果当前节点是会签且下一节点是结束
        	if(checkNextEndEvent(processInstanceId)) {
        		return result;
        	}
        	
            UserVo user = SessionUtils.currentUserVo();
            Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(processInstanceId).singleResult();
            if(task != null){
                //  初始流程变量
                Map<String, Object> variables = Maps.newHashMap();
                List<User> users = this.nextTaskHandler(task.getId(), variables, null, true, null);
                result =  BeanMapper.mapList(users, User.class, UserVo.class);
            }
        }
        return result;
    }

    /**
     * 获取人员查找方式
     * @param expressions
     * @return
     */
    private String getFindType(Set<Expression> expressions){

        for(Expression e : expressions){
            if(StringUtils.startsWith(e.getExpressionText(), PREFIX_FIND_TYPE)){
                return StringUtils.substringAfter(e.getExpressionText(), PREFIX_FIND_TYPE);
            }
        }
        return Constants.FlowFindType.UP_ROLE.code;
    }

    /**
     * 获取接收人角色
     * @param expressions
     * @return
     */
    private String getRoleCode(Set<Expression> expressions){
        for(Expression e : expressions){
            if(StringUtils.startsWith(e.getExpressionText(), PREFIX_ROLE)){
                return StringUtils.substringAfter(e.getExpressionText(), PREFIX_ROLE);
            }
        }
        return null;
    }

    /**
     * 通过条件查找接手人列表
     * @param taskId 任务id
     * @param roleCode 接收节点角色
     * @param findType 人员查找方式
     * @param flowNextHandlerAccount 直接指定接收人
     * @return
     */
    private List<User> findUsers(String taskId, String roleCode, String findType, String flowNextHandlerAccount, UserVo startUser){
        List<User> users = Lists.newArrayList();
        if(StringUtils.isNotBlank(flowNextHandlerAccount)){
          User user = userService.findUserByAccount(flowNextHandlerAccount);
          users.add(user);
        }else{
            UserVo user = startUser == null ? SessionUtils.currentUserVo() : startUser;
            if(Constants.FlowFindType.UP_ROLE.code.equals(findType)){
                //向上查找
                users = userService.findUpUserByRoleCodeAndDepartmentId(roleCode, user.getDepartmentId());
            }else if(Constants.FlowFindType.DOWN_ROLE.code.equals(findType)){
                //向下查找
                users = userService.findDownUserByRoleCodeAndDepartmentId(roleCode, user.getDepartmentId());
            }else if(Constants.FlowFindType.ROLE.code.equals(findType)){
                //通过角色查找
                users = userService.findUserByRoleCode(roleCode);
            }else if(Constants.FlowFindType.SAME_ROLE.code.equals(findType)){
                //找之前节点的处理人
                //如果是多个角色时，约定[将"|"替换成"" + Account]组合成参数
                String sameAccount = taskService.getVariable(taskId, StringUtils.replace(roleCode, "|", "") + "Account").toString();
                User sameUser = userService.findUserByAccount(sameAccount);
                if(sameUser != null){
                    users.clear();
                    users.add(sameUser);
                }
            }else if(Constants.FlowFindType.INITIATOR_ROLE.code.equals(findType)){
                //查找发起人账户
                String initiatorAccount = taskService.getVariable(taskId, "applyAccount").toString();
                User initiatorUser = userService.findUserByAccount(initiatorAccount);
                if(initiatorAccount != null){
                    users.clear();
                    users.add(initiatorUser);
                }
            }else if(Constants.FlowFindType.INITIATOR_UP_ROLE.code.equals(findType)){
                //查找发起人账户
                String initiatorAccount = taskService.getVariable(taskId, "applyAccount").toString();
                User initiatorUser = userService.findUserByAccount(initiatorAccount);
                if(initiatorUser != null){
                    users = userService.findUpUserByRoleCodeAndDepartmentId(roleCode, initiatorUser.getDepartmentId());
                }
            }else{
                //默认向上查找
                users = userService.findUpUserByRoleCodeAndDepartmentId(roleCode, user.getDepartmentId());
            }
        }
        //流程代理
        String flowCode = getFlowCodeByTaskId(taskId);
        users = myAgentSettingService.listAgentUsersByFromUsers(users,flowCode);
        return users;
    }

    private boolean containsValue(String conditionValue, String value){
        String[] values = StringUtils.split(conditionValue, ",");
        for (int i = 0; i < values.length; i++) {
            if(value.equals(values[i])){
                return true;
            }
        }
        return false;
    }


    /**
     * 判断下一节点是否为结束
     * @param taskId
     * @return
     */
    private boolean isEndNextNode(String taskId){
        String approved = null;
        Object approvedObj = taskService.getVariableLocal(taskId, "approved");
        if(approvedObj != null){
            approved = approvedObj.toString();
        }
        return isEndNextNode(taskId, approved);
    }

    /**
     * 判断下一节点是否为结束结束
     * @param taskId
     * @return
     */
    public boolean isEndNextNode(String taskId, String approved){
        ActivityImpl activity = nextActivity(taskId);
        if(NODE_END_EVENT.equals(activity.getProperty(PROPERTY_TYPE))){
            return true;
        }else if(NODE_EXCLUSIVE_GATEWAY.equals(activity.getProperty(PROPERTY_TYPE))){
            if(StringUtils.isNotBlank(approved)){
                //网关节点，继续向下查找节点
                List<PvmTransition> transitions = activity.getOutgoingTransitions();
                for(PvmTransition t : transitions){
                    String conditionText = t.getProperty(PROPERTY_CONDITION_TEXT).toString();
                    String conditionValue = getConditionValue(conditionText);
                    ActivityImpl act = (ActivityImpl) t.getDestination();
                    if(NODE_END_EVENT.equals(act.getProperty(PROPERTY_TYPE)) && containsValue(conditionValue, approved)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 下一节点为发起环节
     * @param taskId
     * @return
     */
    private boolean isApplyNextNode(String taskId){
        ActivityImpl activity = nextActivity(taskId);
        if(NODE_USER_TASK.equals(activity.getProperty(PROPERTY_TYPE))){
            UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
            TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
            if("${applyAccount}".equals(taskDefinition.getAssigneeExpression().getExpressionText())){
                return true;
            }
        }else if(NODE_EXCLUSIVE_GATEWAY.equals(activity.getProperty(PROPERTY_TYPE))){
            //网关节点，继续向下查找节点
            List<PvmTransition> transitions = activity.getOutgoingTransitions();
            String approved = taskService.getVariableLocal(taskId, "approved").toString();
            for(PvmTransition t : transitions){
                String conditionText = t.getProperty(PROPERTY_CONDITION_TEXT).toString();
                String conditionValue = getConditionValue(conditionText);
                ActivityImpl act = (ActivityImpl) t.getDestination();
                if(containsValue(conditionValue, approved) && NODE_USER_TASK.equals(act.getProperty(PROPERTY_TYPE))){
                  	UserTaskActivityBehavior activityBehavior=null;
                    if("parallel".equals(act.getProperty("multiInstance"))) {
                    	activityBehavior = (UserTaskActivityBehavior) ((ParallelMultiInstanceBehavior)act.getActivityBehavior()).getInnerActivityBehavior();
                    }else {	
                     activityBehavior = (UserTaskActivityBehavior) act.getActivityBehavior();
                    }
                    TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
                    if("${applyAccount}".equals(taskDefinition.getAssigneeExpression().getExpressionText())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取表达式值
     * @param conditionText 表达式
     * @return
     */
    private String getConditionValue(String conditionText){
        String conditionValue = "";
        if(StringUtils.contains(conditionText, "||")){
            //仅考虑同一参数多个值的情况, 会以“||”分隔
            String[] temps = StringUtils.split(StringUtils.substringBetween(conditionText, "${", "}"), "||");
            for (int i = 0; i < temps.length; i++) {
                conditionValue += "," + StringUtils.trim(StringUtils.substringAfter(temps[i], "=="));
            }
            conditionValue = StringUtils.substring(conditionValue, 1);
        }else{
            conditionValue = StringUtils.trim(StringUtils.substringBetween(conditionText, "==", "}"));
        }
        return conditionValue;
    }

    public boolean isCondition(String el, String key, String value){
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }

    /**
     * 启动流程
     * @param flowObject 申请单对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startWorkFlow(FlowObject flowObject){
        UserVo user = SessionUtils.currentUserVo();
        ProcessInstance processInstance = startWorkFlow(flowObject, user, null);
        //更新相关报告状态为“启用”
        reportsService.saveEnabledByBusinessId(flowObject.getId());
        return processInstance;
    }

    /**
     * 启动流程
     * @param flowObject 申请单对象
     * @param flowNextHandlerAccount 指定接收人
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startWorkFlow(FlowObject flowObject, String flowNextHandlerAccount){
        UserVo user = SessionUtils.currentUserVo();
        return startWorkFlow(flowObject, user, flowNextHandlerAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startWorkFlow(FlowObject flowObject, UserVo startUser){
        return startWorkFlow(flowObject, startUser, null);
    }

    /**
     * 启动流程
     * @param flowObject 申请单对象
     * @param startUser 流程启动用户
     * @param flowNextHandlerAccount 指定接收人
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startWorkFlow(FlowObject flowObject, UserVo startUser, String flowNextHandlerAccount){

        //检查是否冻结
        if(Constants.HoldStatus.HOLD.code.equals(flowObject.getHold())){
            throw new RuntimeException(localeMessageSource.getMsgHoldAlready(flowObject.getId()));
        }

        //1. 设置申请单基础属性
        flowObject.setStartTime(new Date());
        flowObject.setCreatorId(startUser.getId());
        flowObject.setCreatorCnName(startUser.getCnName());
        flowObject.setCreatorEnName(startUser.getEnName());
        flowObject.setDepartmentId(startUser.getDepartment().getId());
        flowObject.setDepartmentCnName(startUser.getDepartment().getCnName());
        flowObject.setDepartmentEnName(startUser.getDepartment().getEnName());
        //  设置申请单流程状态为审批中
        flowObject.setFlowStatus(Constants.FlowStatus.REVIEW.code);
        //  设置申请单状态为正常状态
        flowObject.setStatus(Constants.Status.NORMAL.code);
        String businessKey = flowObject.getId();
        ProcessInstance processInstance;
        try {
            //2. 设置启动流程的用户账号
            identityService.setAuthenticatedUserId(startUser.getAccount());
            //  初始流程变量
            Map<String, Object> variables = Maps.newHashMap();
            //  存储表单变量
            variables.put("data", new JsonMapper().toJson(flowObject));
            //启动流程
            processInstance = runtimeService.startProcessInstanceByKey(flowObject.getClass().getSimpleName(), businessKey, variables);
            //记录发起
            flowOperatorHistoryService.newFlowOperatorHistory(flowObject.getId(), null, null, Constants.FlowAct.START.code, startUser);
            String processInstanceId = processInstance.getProcessInstanceId();
            flowObject.setProcessInstanceId(processInstanceId);
            //发起人为当前处理人
            flowObject.setAssigneeId(startUser.getId());
            flowObject.setAssigneeCnName(startUser.getCnName());
            flowObject.setAssigneeEnName(startUser.getEnName());
            //方法结束后，会自动保存对象
            logger.debug("save entity: {}", flowObject);

            //自动提交到下一个节点
            String taskId = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId();
            List<User> users = this.nextTaskHandler(taskId, variables, flowNextHandlerAccount, false, startUser);
            if(checkNextJoinTask(processInstanceId)) {
                //如果第二个节点是会签，则不用选人，后台直接审批任务
            	String assigneeCnNames =users.get(0).getCnName();
            	String assigneeEnNames =users.get(0).getEnName();
            	String assigneeIds =users.get(0).getId();
            	
            	for(int i=1;i< users.size();i++) {
            		assigneeCnNames= assigneeCnNames+"|"+users.get(i).getCnName();
            		assigneeEnNames= assigneeEnNames+"|"+users.get(i).getEnName();
            	}
                List<String> assigneeAccounts = Lists.newArrayList();
                assigneeAccounts.add(startUser.getAccount());
        		for(User u : users) {
            		if(!assigneeAccounts.contains(u.getAccount())){
            			assigneeAccounts.add(u.getAccount());
            		}
            	}
                taskService.complete(taskId, variables);
                
                //将会签任务人替换为他的代理人
                setAgentUsersForNextTask(flowObject.getProcessInstanceId());
                
                //同意时设置为审批中
                flowObject.setFlowStatus(Constants.FlowStatus.REVIEW.code);
                //设置当前处理人
                flowObject.setAssigneeId(assigneeIds);
                flowObject.setAssigneeCnName(assigneeCnNames);
                flowObject.setAssigneeEnName(assigneeEnNames);
                BaseDao baseDao = (BaseDao) SpringUtil.getBean(StringUtils.uncapitalize(flowObject.getClass().getSimpleName()) + "Dao");
                baseDao.save(flowObject);
                Msg.sendByAccount(assigneeAccounts, localeMessageSource.getMsgTaskMsgTitle(new Object[]{flowObject.getId(), StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName()))}),
                        localeMessageSource.getMsgTaskMsgContent(new Object[]{startUser.getEnName(),
                                StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName())),
                                Constants.FlowStatus.getEnName(flowObject.getFlowStatus()),
                                startUser.getEnName()}));
                //4. 记录历史纪录
                flowOperatorHistoryService.newFlowOperatorHistory(flowObject.getId(), null, null, Constants.FlowAct.ALLOW.code,startUser);
                return processInstance;
            }else{
            	if(isEndNextNode(taskId)){
	                //直接结束
	                taskService.complete(taskId, variables);
	                //结束时设置为通过
	                flowObject.setFlowStatus(Constants.FlowStatus.PASS.code);
	                //设置结束时间
	                flowObject.setEndTime(new Date());
	                //当前处理人为创建人
	                flowObject.setAssigneeId(startUser.getId());
	                flowObject.setAssigneeCnName(startUser.getCnName());
	                flowObject.setAssigneeEnName(startUser.getEnName());
	                //审批中
	                flowOperatorHistoryService.newFlowOperatorHistory(flowObject.getId(), null, null, Constants.FlowAct.ALLOW.code, startUser);
	            }else{
		            if(users != null && users.size() == 1){
		            	User user = users.get(0);
		            	UserVo loginUSer =SessionUtils.currentUserVo();
		            	if(user.getId().equals(loginUSer.getId())){
			                taskService.complete(taskId, variables);
			                //当前处理人为接收人
			                flowObject.setAssigneeId(user.getId());
			                flowObject.setAssigneeCnName(user.getCnName());
			                flowObject.setAssigneeEnName(user.getEnName());
			                List<String> accouts = Lists.newArrayList();
			                accouts.add(user.getAccount());
			                Msg.send(user.getId(), localeMessageSource.getMsgTaskMsgTitle(new Object[]{flowObject.getId(), StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName()))}),
			                        localeMessageSource.getMsgTaskMsgContent(new Object[]{startUser.getEnName(),
			                                StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName())),
			                                Constants.FlowStatus.getEnName(flowObject.getFlowStatus()),
			                                startUser.getEnName()}));
		            	}
		            }
	            }
            }
            logger.debug("start process: {key={}, pid={}， variables={}}", new Object[]{flowObject.getClass().getSimpleName(), businessKey, processInstanceId});
        }finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance;
    }

    public void setAgentUsersForNextTask(String processInstanceId){
		List<Task> taskList = getTasksByProcessInstanceId(processInstanceId);
		List<User> userList = Lists.newArrayList();
		for(int i = 0;i<taskList.size();i++){
			User u = userService.findUserByAccount(taskList.get(i).getAssignee());
			userList.add(u);
			taskList.get(i).setAssignee(u.getId()); //fromUserId
		 }
	    String flowCode = getFlowCodeByProcessInstanceId(processInstanceId);
		Date now = new Date();
		for(int i=0;i<userList.size();i++){
			User userObj=userList.get(i);
			// 1. 获取用户对应的有效的代理设置
			List<MyAgentSetting> list = myAgentSettingService.findAllowedMyAgentSettingByFromUserId(userObj.getId(),flowCode, now);
			if(list != null){
				for(MyAgentSetting myAgentSettingobj : list){
					if(myAgentSettingobj.getFlowCode().equals("ALL")||myAgentSettingobj.getFlowCode().equals(flowCode)){
						User toUser = userService.getUser(myAgentSettingobj.getToUserId());
						for(int j=0;j<taskList.size();j++){
							Task taskObj = taskList.get(j);
							if(myAgentSettingobj.getFromUserId().equals(taskObj.getAssignee())){
							        taskService.setAssignee(taskObj.getId(), toUser.getAccount());  
									break;
							}
						}
						
					}
				}
			}
		}

    }


    /**
     * 提交任务
     * @param taskId 任务id
     * @param flowObject 流程表单对象
     * @param approved 通过、退回、返审
     * @param flowNextHandlerAccount 前台有指定接收人时设置
     * @param flowRemark 审批意见
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitTask(String taskId, FlowObject flowObject, String approved, String flowRemark, String flowNextHandlerAccount){
        try {
            //检查是否冻结
            if(Constants.HoldStatus.HOLD.code.equals(flowObject.getHold())){
                throw new RuntimeException(localeMessageSource.getMsgHoldAlready(flowObject.getId()));
            }
            //当前流程的flowCode
            String flowCode = getFlowCodeByTaskId(taskId);
            //1. 设置当前处理人账号
            UserVo user = SessionUtils.currentUserVo();
            identityService.setAuthenticatedUserId(user.getAccount());
            //2. 设置审批路由参数
            taskService.setVariableLocal(taskId, "approved", approved);
            //获取之前参与人账户
            List<String> assigneeAccounts = getAllAssigneeAccount(taskId);
            //查找发起人账户
            String initiatorAccount = taskService.getVariable(taskId, "applyAccount").toString();
            User initiatorUser = userService.findUserByAccount(initiatorAccount);
            //3. 提交任务
            
            if(Constants.FlowAct.REFUSE.code.equals(approved)){
                //拒绝
                this.endProcess(taskId);
                flowObject.setFlowStatus(Constants.FlowStatus.REJECTED.code);
                flowObject.setEndTime(new Date());
                //当前处理人为发起人
                flowObject.setAssigneeId(initiatorUser.getId());
                flowObject.setAssigneeCnName(initiatorUser.getCnName());
                flowObject.setAssigneeEnName(initiatorUser.getEnName());

                //更新相关报告状态为“禁用”
                reportsService.saveDisabledByBusinessId(flowObject.getId());
                
                
            	//通过之前参与人的accounts获取相关授权人，并给授权人发送消息
            	String userIds = SpringUtil.getBean(UserService.class).getUserIdsByAccounts(assigneeAccounts);
            	List<User> beforeFromUsers= myAgentSettingService.listAgentUsersByToUserIds(userIds, flowCode);
           		for(User u : beforeFromUsers) {
           			if(!assigneeAccounts.contains(u.getAccount())){
           				assigneeAccounts.add(u.getAccount());
           			}
           		}

            }else{
                Map<String, Object> variables = Maps.newHashMap();
                List<User> users = this.nextTaskHandler(taskId, variables, flowNextHandlerAccount, false, null);

            	//通过代理人获取授权人，并给下一节点的授权人发送消息
            	List<User> fromUsers= myAgentSettingService.listAgentUsersByToUsers(users, flowCode);
            	//通过之前参与人的accounts获取相关授权人，并给授权人发送消息
            	String userIds = SpringUtil.getBean(UserService.class).getUserIdsByAccounts(assigneeAccounts);
            	List<User> beforeFromUsers= myAgentSettingService.listAgentUsersByToUserIds(userIds, flowCode);
           		for(User u : fromUsers) {
            		if(!assigneeAccounts.contains(u.getAccount())){
            			assigneeAccounts.add(u.getAccount());
            		}
            	}
           		for(User u : beforeFromUsers) {
           			if(!assigneeAccounts.contains(u.getAccount())){
           				assigneeAccounts.add(u.getAccount());
           			}
           		}
                
                //退回或返审时
                if(Constants.FlowAct.BACK.code.equals(approved) || Constants.FlowAct.REDO.code.equals(approved)){
                    //调整申请
                    if(isApplyNextNode(taskId)){
                        flowObject.setFlowStatus(Constants.FlowStatus.ADJUST_APPLY.code);
                    }
                    taskService.complete(taskId, variables);
                    
                    //将会签任务人替换为他的代理人
                	if(checkJoinTask(flowObject.getProcessInstanceId())) {
                		setAgentUsersForNextTask(flowObject.getProcessInstanceId());
                	}
                    
                    //获取下一节点接收人，设置当前处理人
                    List<Task> tasks = getTasksByProcessInstanceId(flowObject.getProcessInstanceId());
                    
                    
                    if(tasks!=null&&tasks.size()>0) {
                        String nextAccount = tasks.get(0).getAssignee();
                        User u = userService.findUserByAccount(nextAccount);
                        flowObject.setAssigneeId(u.getId());                    	
                        String assigneeCnNames =u.getCnName();
                        String assigneeEnNames =u.getEnName();
                        
                        for(int i=1;i<tasks.size();i++) {
                            nextAccount = tasks.get(i).getAssignee();
                            u = userService.findUserByAccount(nextAccount);
	                		assigneeCnNames= assigneeCnNames+"|"+u.getCnName();
	                		assigneeEnNames= assigneeEnNames+"|"+u.getEnName();
	                	}
                        flowObject.setAssigneeCnName(assigneeCnNames);
                        flowObject.setAssigneeEnName(assigneeEnNames);
                    }
                    
                    
                }else if(Constants.FlowAct.ALLOW.code.equals(approved)){

                    if(isEndNextNode(taskId)){
                        //完成流程
                        taskService.complete(taskId, variables);
                        //结束时设置为通过
                        flowObject.setFlowStatus(Constants.FlowStatus.PASS.code);
                        //当前处理人为发起人
                        flowObject.setAssigneeId(initiatorUser.getId());
                        flowObject.setAssigneeCnName(initiatorUser.getCnName());
                        flowObject.setAssigneeEnName(initiatorUser.getEnName());
                        //设置结束时间
                        flowObject.setEndTime(new Date());

                    }else if(users != null && users.size() >0){
                    	if(checkNextJoinTask(flowObject.getProcessInstanceId())) {
		                        //审批任务
		                    	String assigneeCnNames =users.get(0).getCnName();
		                    	String assigneeEnNames =users.get(0).getEnName();
		                    	String assigneeIds =users.get(0).getId();
		                    	
		                    	for(int i=1;i< users.size();i++) {
		                    		assigneeCnNames= assigneeCnNames+"|"+users.get(i).getCnName();
		                    		assigneeEnNames= assigneeEnNames+"|"+users.get(i).getEnName();
		                    	}
		                    	
	                    		for(User u : users) {
		                    		if(!assigneeAccounts.contains(u.getAccount())){
		                    			assigneeAccounts.add(u.getAccount());
		                    		}
		                    	}
		                        taskService.complete(taskId, variables);
		                        
		                        //将会签任务人替换为他的代理人
		                        setAgentUsersForNextTask(flowObject.getProcessInstanceId());

		                        
		                        //同意时设置为审批中
		                        flowObject.setFlowStatus(Constants.FlowStatus.REVIEW.code);
		                        //设置当前处理人
		                        flowObject.setAssigneeId(assigneeIds);
		                        flowObject.setAssigneeCnName(assigneeCnNames);
		                        flowObject.setAssigneeEnName(assigneeEnNames);
                    	}else {
		                        //审批任务
	                    		if(users.size()!=1) {
	                    			throw new RuntimeException(localeMessageSource.getMsgTaskNoAssignee());
	                    		}
	                    		if(!assigneeAccounts.contains(users.get(0).getAccount())){
	                    			assigneeAccounts.add(users.get(0).getAccount());
	                    		}
		                        taskService.complete(taskId, variables);
		                        //同意时设置为审批中
		                        flowObject.setFlowStatus(Constants.FlowStatus.REVIEW.code);
		                        //设置当前处理人
		                        flowObject.setAssigneeId(users.get(0).getId());
		                        flowObject.setAssigneeCnName(users.get(0).getCnName());
		                        flowObject.setAssigneeEnName(users.get(0).getEnName());
                	}
                    }else{
                        throw new RuntimeException(localeMessageSource.getMsgTaskNoAssignee());
                    }
                }
            }
            // 保存表单数据
            BaseDao baseDao = (BaseDao) SpringUtil.getBean(StringUtils.uncapitalize(flowObject.getClass().getSimpleName()) + "Dao");
            baseDao.save(flowObject);
            Msg.sendByAccount(assigneeAccounts, localeMessageSource.getMsgTaskMsgTitle(new Object[]{flowObject.getId(), StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName()))}),
                    localeMessageSource.getMsgTaskMsgContent(new Object[]{user.getEnName(),
                            StringUtils.joinWith(" ", StringUtils.splitByCharacterTypeCamelCase(flowObject.getClass().getSimpleName())),
                            Constants.FlowStatus.getEnName(flowObject.getFlowStatus()),
                            initiatorUser.getEnName()}));
            //4. 记录历史纪录
            flowOperatorHistoryService.newFlowOperatorHistory(flowObject.getId(), flowRemark, null, approved, null);
        }finally {
            //5. 清空处理人临时数据
            identityService.setAuthenticatedUserId(null);
        }
    }

	private String getFlowCodeByTaskId(String taskId) {
		ActivityImpl activity = currentActivity(taskId);
		ProcessDefinitionEntity processDefinitionEntity =  (ProcessDefinitionEntity)activity.getProcessDefinition();
		String flowCode = processDefinitionEntity.getKey();
		return flowCode;
	}
	
	private String getFlowCodeByProcessInstanceId(String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	 	String flowCode = pi.getProcessDefinitionKey();
		return flowCode;
	}
    
    /**
     * 查找下一节点是否为会签节点
     * @param processInstanceId 流程实例id
     * @return
     */
    public Boolean checkNextJoinTask(String processInstanceId){
    	if(StringUtils.isNotBlank(processInstanceId)){
    		Task task = getTaskByProcessInstanceId(processInstanceId);
    		if(task != null){	
    			//multiInstance=parallel
    			String taskId = task.getId();
    		        ActivityImpl activity = nextActivity(taskId);
    		        if(NODE_USER_TASK.equals(activity.getProperty(PROPERTY_TYPE))){
		                if("parallel".equals(activity.getProperty("multiInstance"))) {
	                		return true;
		                }else {
		                	return false;
			            }
    		        }else if(NODE_EXCLUSIVE_GATEWAY.equals(activity.getProperty(PROPERTY_TYPE))){
    		            //网关节点，继续向下查找节点
    		            List<PvmTransition> transitions = activity.getOutgoingTransitions();
    		            for(PvmTransition t : transitions){
    		                ActivityImpl act = (ActivityImpl) t.getDestination();
    		                if(NODE_END_EVENT.equals(act.getProperty(PROPERTY_TYPE))){
    		                    continue;
    		                }
    		                //multiInstance=parallel  如果是多实例userTask节点
    		                if("parallel".equals(act.getProperty("multiInstance"))) {
    		                		return true;
    		                }else {
    			                if(NODE_USER_TASK.equals(act.getProperty(PROPERTY_TYPE))){
    			                	return false;
			                	}
    			            }
    		            }
    		        }
			}
    	}
    	return false;
    }
    /**
     * 判断当前节点是否为会签节点
     * @param processInstanceId 流程实例id
     * @return
     */
    public Boolean checkJoinTask(String processInstanceId){
    	if(StringUtils.isNotBlank(processInstanceId)){
    		Task task = getTaskByProcessInstanceId(processInstanceId);
    		if(task != null){	
    			String taskId = task.getId();
    			ActivityImpl activity = currentActivity(taskId);
    			//multiInstance=parallel
    			if(NODE_USER_TASK.equals(activity.getProperty(PROPERTY_TYPE))){
    				if("parallel".equals(activity.getProperty("multiInstance"))){
    					return true;
    				}else{
    					return false;
    				}
    			}else{
    					return false;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * 查找下一节点是否为结束
     * @param processInstanceId 流程实例id
     * @return
     */
    public Boolean checkNextEndEvent(String processInstanceId){
    	if(StringUtils.isNotBlank(processInstanceId)){
    		UserVo user = SessionUtils.currentUserVo();
    		Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(processInstanceId).singleResult();
    		if(task != null){	
    			String taskId = task.getId();
    			ActivityImpl activity = nextActivity(taskId);
    			if(NODE_END_EVENT.equals(activity.getProperty(PROPERTY_TYPE))){
    					return true;
    			}else if(NODE_EXCLUSIVE_GATEWAY.equals(activity.getProperty(PROPERTY_TYPE))){
    				//网关节点，继续向下查找节点
    				List<PvmTransition> transitions = activity.getOutgoingTransitions();
    				for(PvmTransition t : transitions){
    					ActivityImpl act = (ActivityImpl) t.getDestination();
    					if(NODE_END_EVENT.equals(act.getProperty(PROPERTY_TYPE))){
    						return true;
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
	/**
	 * 查找流程List<Task>
	 * @param processInstanceId
	 * @return List<Task> org.activiti.engine.task.Task
	 */
	public List<Task> getTasksByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = taskService.createNativeTaskQuery()  
				.sql("SELECT * FROM act_ru_task"  + " T WHERE T.proc_inst_id_ = #{processInstanceId}")  
				.parameter("processInstanceId", processInstanceId)  
				.list(); 
		return tasks;
	}

    /**
     * 获取所有参与人账户
     * @param taskId 任务id
     * @return
     */
    private List<String> getAllAssigneeAccount(String taskId){
        List<String> accounts = Lists.newArrayList();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String, Object> entry: variables.entrySet()){
            if(StringUtils.endsWith(entry.getKey(), "Account") || StringUtils.equals(entry.getKey(), "applyAccount")){
                //过滤重复的
                if(!accounts.contains(entry.getValue())){
                    accounts.add(entry.getValue().toString());
                }
            }
        }
        return accounts;
    }

    /**
     * 终止流程
     */
    @Transactional(rollbackFor = Exception.class)
    public void endProcess(String taskId){
        //当前节点
        ActivityImpl currentActiviti = findActivitiImpl(taskId, null);
        //清空当前流向,并保存当前的流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currentActiviti);

        //创建新的流向
        TransitionImpl newTransition = currentActiviti.createOutgoingTransition();
        //目标节点
        ActivityImpl endActivity = findActivitiImpl(taskId, "end");
        //设置流向的目标节点
        newTransition.setDestination(endActivity);
        //执行任务
        taskService.complete(taskId);
        //删除目标节点新流入
        endActivity.getIncomingTransitions().remove(newTransition);
        //还原以前的流向
        restoreTransition(currentActiviti, oriPvmTransitionList);

    }

    private List<PvmTransition> clearTransition(ActivityImpl activity){
        List<PvmTransition> oriPvmTransitionList = Lists.newArrayList();
        List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList){
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    /**
     * 还原指定的活动节点
     * @param activity
     * @param oriPvmTransition
     */
    private void restoreTransition(ActivityImpl activity, List<PvmTransition> oriPvmTransition){
        List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
        pvmTransitionList.clear();
        for(PvmTransition transition : oriPvmTransition){
            pvmTransitionList.add(transition);
        }
    }

    private ActivityImpl findActivitiImpl(String taskId, String activitiId){
        //取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
        //取得当前活动节点
        if(StringUtils.isEmpty(activitiId)){
            activitiId = findTaskById(taskId).getTaskDefinitionKey();
        }

        //根据流程定义，获取流程实例的结束节点
        if("END".equals(activitiId.toUpperCase())){
            for (ActivityImpl activity: processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                if(pvmTransitionList.isEmpty()){
                    return activity;
                }
            }
        }

        //根据流程节点获取对应的活动节点
        ActivityImpl activity = processDefinition.findActivity(activitiId);
        return activity;
    }

    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId){
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());
        if(processDefinition == null){
            throw new RuntimeException("流程定义未找到");
        }
        return processDefinition;
    }

    public ProcessInstance findProcessInstanceByTaskId(String taskId){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
        if(processInstance == null){
            throw  new RuntimeException("流程实例未找到");
        }
        return processInstance;
    }

    public Task findTaskById(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            throw new RuntimeException("任务实例未找到");
        }
        return task;
    }

    /**
     * 下一节点信息
     * @param taskId
     * @return
     */
    public ActivityImpl nextActivity(String taskId){
        ActivityImpl activity = currentActivity(taskId);
        //获取所有流向线路信息
        List<PvmTransition> outTransitions = activity.getOutgoingTransitions();
        if(outTransitions != null && !outTransitions.isEmpty()){
            return (ActivityImpl) outTransitions.get(0).getDestination();
        }
        return null;
    }


    /**
     * 当前节点信息
     * @param taskId taskId
     * @return
     */
    private ActivityImpl currentActivity(String taskId){

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        //流程定义信息
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(pi.getProcessDefinitionId());

        ExecutionEntity executionEntity = (ExecutionEntity) pi;

        //当前节点
        String activityId = executionEntity.getActivityId();

        //获取所有节点信息
        List<ActivityImpl> activityList = processDefinitionEntity.getActivities();
        //目前已知情况当前节点为会签时，activityId==null
        if(activityId==null) {
        	for (ActivityImpl activity : activityList){
	        		if(task.getTaskDefinitionKey().equals(activity.getId())) {
	                return activity;
	            }
        	}
        }else {
        for (ActivityImpl activity : activityList){
            if(activityId.equals(activity.getId())){
                return activity;
            }
        }
        }
        return null;
    }

    /**
     * 查看流程图
     */
    public void viewImage(String processInstanceId, String processDefinitionKey, HttpServletResponse response) throws IOException {
        InputStream in = null;
        if(StringUtils.isNotBlank(processInstanceId)){
            Task task =getTaskByProcessInstanceId(processInstanceId);
            if(task != null) {
                String processDefinitionId = task.getProcessDefinitionId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
                ProcessDiagramGenerator pdg = new DefaultProcessDiagramGenerator();

                List<String> activeIds = runtimeService.getActiveActivityIds(task.getExecutionId());
                in = pdg.generateDiagram(bpmnModel,"png", activeIds, Collections.emptyList()
                        ,"宋体","宋体","宋体", null, 1.0);

            }else{
                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();

                in = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());

            }
        } else {
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
            in = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
        }
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        for(int b = -1; (b = in.read()) != -1;){
            out.write(b);
        }
        out.close();
        in.close();
    }
    /**
     * 查找流程Task
     * @param processInstanceId
     * @return org.activiti.engine.task.Task
     */
	public Task getTaskByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = taskService.createNativeTaskQuery()  
				  .sql("SELECT * FROM act_ru_task"  + " T WHERE T.proc_inst_id_ = #{processInstanceId}")  
				  .parameter("processInstanceId", processInstanceId)  
				  .list(); 
		Task task = null;
		if(tasks!=null&&tasks.size()>0) {
			task = tasks.get(0);
			UserVo user = SessionUtils.currentUserVo();
			for(Task t : tasks) {
				if(t.getAssignee().equals(user.getAccount())) {
					task = t;
				}
			}
		}
		return task;
	}
	/**
	 * 作废流程
	 * @param processInstanceId
	 * @return org.activiti.engine.task.Task
	 */
	public void cancelProcessByProcessInstanceId(String processInstanceId) {
		String deleteReason ="手工作废"; 
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(pi!=null){
        	runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        }
		List<Task> tasks = taskService.createNativeTaskQuery()  
				.sql("SELECT * FROM act_ru_task"  + " T WHERE T.proc_inst_id_ = #{processInstanceId}")  
				.parameter("processInstanceId", processInstanceId)  
				.list();
		if(tasks!=null&&tasks.size()>0) {
			for(Task t : tasks) {
				taskService.deleteTask(t.getId(), deleteReason);
			}
		}
	}
    /**
     * 指定代理人
     * @param taskId
     * @param agentAccount 代理人账户
     */
    @Transactional(rollbackFor = Exception.class)
    public void setAgent(String taskId, String agentAccount) {
        Task task = findTaskById(taskId);
        if(task != null){
//            ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
            taskService.setAssignee(taskId, agentAccount);
        }
    }

    /**
     * 获取当前用户的 Task 总数
     * @return
     */
    public Integer countTaskNew(){
        Integer result =0;
        UserVo user = SessionUtils.currentUserVo();
        result = Long.valueOf(taskService.createTaskQuery().taskAssignee(user.getAccount()).count()).intValue();
        return result;
    }
}
