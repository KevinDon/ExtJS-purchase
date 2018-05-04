package com.newaim.purchase.flow.purchase.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearance;
import com.newaim.purchase.flow.purchase.service.FlowCustomClearanceService;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingsListVo;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearanceVo;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;


@RestController
@RequestMapping("/flow/purchase/customClearance")
public class FlowCustomClearanceController extends ControllerBase {

    @Autowired
    private FlowCustomClearanceService flowCustomClearanceService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;
    
    @Autowired
    private ExportService exportService;
    
    @RequiresPermissions("FlowCustomClearance:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal ,String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ){
        RestResult result = new RestResult();
        try {

            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("id-S-LK-OR", keywords);
                params.put("orderNumber-S-LK-OR", keywords);
                params.put("orderTitle-S-LK-OR", keywords);
               /* params.put("serviceProviderCnName-S-LK-OR", keywords);
                params.put("serviceProviderEnName-S-LK-OR", keywords);
                params.put("newUsedDeclaration-S-LK-OR", keywords);
                params.put("commercialInvoice-S-LK-OR", keywords);
                params.put("packingDeclaration-S-LK-OR", keywords);
                params.put("deliveryTerms-S-LK-OR", keywords);
                params.put("containerNumber-S-LK-OR", keywords);
                params.put("sealsNumber-S-LK-OR", keywords);
                params.put("ciNumber-S-LK-OR", keywords);
                params.put("remark-S-LK-OR", keywords);
                params.put("tradeTerm-S-LK-OR", keywords);
                params.put("vessel-S-LK-OR", keywords);
                params.put("voy-S-LK-OR", keywords);*/
            	params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("assigneeCnName-S-LK-OR", keywords);
				params.put("assigneeEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);

            }else{
                params = ServletUtils.getParametersStartingWith(request);
                //搜索出指定部门以下的所有记录
                if(params.size()>0){
                    if(params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())){
                        String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
                        params.remove("departmentId-S-EQ");
                        params.put("departmentId-S-IN", depIds);
                    }
                }

            }

            params = setParams(params, "FlowCustomClearance", ":4:3:2:1", false);
            
            //列表数据过滤
            if(null != modal && StringUtils.isNotBlank(modal)){
                UserVo user = SessionUtils.currentUserVo();

                if("mine".equals(modal)){
                    //我发启的
                    params.remove("creatorId-S-EQ-ADD");
                    params.put("creatorId-S-EQ-ADD", user.getId());
                }else if("involved".equals(modal)){
                    //我参与的
//					params.put("history.operatorId-S-EQ-ADD", user.getId());
//                  params.put("history.businessId-S-GBY-NON", "");
                }
            }
            
            Page<FlowCustomClearanceVo> page = flowCustomClearanceService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowCustomClearance:normal:list")
    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            FlowCustomClearanceVo rd =  flowCustomClearanceService.get(id);
            //设置流程备选人
            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions(value = {"FlowCustomClearance:normal:add", "FlowCustomClearance:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowCustomClearance main,
                           @ModelAttribute("details")FlowCustomClearancePackingsListVo details,
                           @ModelAttribute("attachments") AttachmentsVo attachments) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){

                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    flowCustomClearanceService.saveAs(main, details.getDetails(), attachments.getAttachments());
                }else{
                    flowCustomClearanceService.update(main, details.getDetails(), attachments.getAttachments());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else{
                flowCustomClearanceService.add(main, details.getDetails(), attachments.getAttachments());

                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }

        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions("FlowCustomClearance:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowCustomClearance flowCustomClearance = flowCustomClearanceService.getFlowCustomClearance(ids);
            //流程未发起
            if(flowCustomClearance.getFlowStatus() == null){
                flowCustomClearanceService.delete(flowCustomClearance);
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
            }else{
                result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskCannotDelete());
            }
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    /**
     * 导出Grid数据
     *
     * @param act
     * @param request
     * @param modal
     * @param sort
     * @param keywords
     * @param pageNumber
     * @param pageSize
     */
    @RequiresPermissions("FlowCustomClearance:normal:export")
    @PostMapping("/export")
    public RestResult export(String act, String fileTitle, String fileName, String id, @ModelAttribute("columns") ExportColumnsVo columns,
                             HttpServletRequest request, String modal, String sort, String keywords,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();

        if (ACT_PDF.equals(act) || ACT_EXCEL.equals(act) || ACT_CSV.equals(act)) {
            RestResult rl = list(request, modal, sort, keywords, pageNumber, pageSize);
            if (rl.getTc() < 1) {
                result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
                return result;
            }

            try {
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowCustomClearanceVo>) rl.getData());

                if (null != mydoc) {
                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
                } else {
                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        } else if (ACT_WORD.equals(act) || ACT_EMAIL_WORD.equals(act)) {
        	FlowCustomClearanceVo vo = flowCustomClearanceService.get(id);

            try {
                MyDocumentVo mydoc = exportService.exportFileForWordByXml(request, act, fileTitle, fileName, vo);

                if (null != mydoc) {
                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
                } else {
                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        }

        return result;
    }

    @ModelAttribute("main")
    public FlowCustomClearance main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowCustomClearanceService.getFlowCustomClearance(id);
        }else{
            return new FlowCustomClearance();
        }
    }

    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String nextHandlerId, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowCustomClearance main,
                              @ModelAttribute("details")FlowCustomClearancePackingsListVo details,
                              @ModelAttribute("attachments") AttachmentsVo attachments){

        if(ACT_START.equals(act)){
            String nextHandlerAccount = null;
            if(StringUtils.isNotBlank(nextHandlerId)){
                UserVo nextUser = userService.get(nextHandlerId);
                if(nextUser != null){
                    nextHandlerAccount = nextUser.getAccount();
                }
            }
            //保存数据
            if(StringUtils.isNotBlank(main.getId())){
                flowCustomClearanceService.update(main, details.getDetails(), attachments.getAttachments());
            }else{
                flowCustomClearanceService.add(main, details.getDetails(), attachments.getAttachments());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId, nextHandlerAccount);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowCustomClearanceService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowCustomClearanceService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
        }else{
            //审批
            if(main.getFlowStatus() == Constants.FlowStatus.ADJUST_APPLY.code.intValue()){
                flowCustomClearanceService.update(main, details.getDetails(), attachments.getAttachments());
            }
            return approveFlow(businessId, act, flowRemark, nextHandlerId, flowNextHandlerAccount);
        }
    }


    /**
     * 启动流程
     */
    private RestResult start(String businessId, String flowNextHandlerAccount){
        RestResult result = new RestResult();
        try{
            FlowCustomClearance flowCustomClearance = flowCustomClearanceService.getFlowCustomClearance(businessId);
            ProcessInstance processInstance;
                processInstance = activitiService.startWorkFlow(flowCustomClearance, flowNextHandlerAccount);
            result.setSuccess(true).setMsg("流程已启动，流程ID: " + processInstance.getId());
        }catch (ActivitiException e){
            result.setSuccess(false);
            if(e.getMessage().indexOf("no processes deployed with key") != -1){
                logger.warn("没有部署流程", e);
                result.setMsg(e.getMessage());
            }else{
                logger.error("启动流程失败：", e);
                result.setMsg(e.getMessage());
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            logger.error("系统异常", e);
        }
        return result;
    }

    /**
     * 审批
     */
    private RestResult approveFlow(String businessId, String act, String flowRemark, String nextHandlerId, String flowNextHandlerAccount){
        RestResult result = new RestResult();
        UserVo user = SessionUtils.currentUserVo();
        FlowCustomClearance flowCustomClearance = flowCustomClearanceService.getFlowCustomClearance(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowCustomClearance.getProcessInstanceId()).singleResult();
        if(task != null){
            //提交任务
            if(Constants.FlowStatus.ADJUST_APPLY.code.equals(flowCustomClearance.getFlowStatus())){
                //调整申请时直接提交给订单发起人
                UserVo nextUser = userService.get(nextHandlerId);
                flowNextHandlerAccount = nextUser.getAccount();
            }
            activitiService.submitTask(task.getId(), flowCustomClearance, getApprovedValue(act), flowRemark , flowNextHandlerAccount);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }else {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
        }
        return result;
    }

}
