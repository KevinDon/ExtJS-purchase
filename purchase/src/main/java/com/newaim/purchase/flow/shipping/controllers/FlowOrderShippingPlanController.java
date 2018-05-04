package com.newaim.purchase.flow.shipping.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApply;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingApplyService;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingPlanService;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingPlanDetailsVo;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingPlanVo;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/flow/shipping/orderShippingPlan")
public class FlowOrderShippingPlanController extends ControllerBase {

    @Autowired
    private FlowOrderShippingPlanService flowOrderShippingPlanService;

    @Autowired
    private FlowOrderShippingApplyService flowOrderShippingApplyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;
    
    @Autowired
    private ExportService exportService;
    
    @RequiresPermissions("FlowOrderShippingPlan:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request,String modal, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
                params.put("orderNumbers-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("handlerDepartmentCnName-S-LK-OR", keywords);
				params.put("handlerDepartmentEnName-S-LK-OR", keywords);
				params.put("reviewerCnName-S-LK-OR", keywords);
				params.put("reviewerEnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentCnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentEnName-S-LK-OR", keywords);
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

            params = setParams(params, "FlowOrderShippingPlan", ":4:3:2:1", false);
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
            Page<FlowOrderShippingPlanVo> page = flowOrderShippingPlanService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowOrderShippingPlan:normal:list")
    @PostMapping("/listForDialog")
    public RestResult listForDialog(ServletRequest request,String modal, String sort,String type, String serviceProviderId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            //搜索出指定部门以下的所有记录
            if(params.size()>0){
                if(params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())){
                    String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
                    params.remove("departmentId-S-EQ");
                    params.put("departmentId-S-IN", depIds);
                }
            }
            params = setParams(params, "FlowOrderShippingPlan", ":4:3:2:1", false);
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
            if(type != null && "1".equals(type)){
                if(StringUtils.isNotBlank(serviceProviderId)){
                    params.put("serviceProviderId-S-EQ-ADD", serviceProviderId);
                }
                params.put("flowStatus-N-IN-ADD", Constants.FlowStatus.ADJUST_APPLY.code + "," + Constants.FlowStatus.REVIEW.code);
            }
            Page<FlowOrderShippingPlanVo> page = flowOrderShippingPlanService.listForDialog(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowOrderShippingPlan:normal:list")
    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            FlowOrderShippingPlanVo rd =  flowOrderShippingPlanService.get(id);
            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions(value = {"FlowOrderShippingPlan:normal:add", "FlowOrderShippingPlan:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowOrderShippingPlan main, @ModelAttribute("details") FlowOrderShippingPlanDetailsVo detailsVo) {
        RestResult result = new RestResult();
        try {
            if (StringUtils.isNotBlank(main.getId())) {
                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                    flowOrderShippingPlanService.saveAs(main, detailsVo.getDetails());
                } else {
                    flowOrderShippingPlanService.update(main, detailsVo.getDetails());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            } else {
                flowOrderShippingPlanService.add(main, detailsVo.getDetails());
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }
        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowOrderShippingPlan:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(ids);
            //流程未发起
            if(flowOrderShippingPlan.getFlowStatus() == null){
                flowOrderShippingPlanService.delete(flowOrderShippingPlan);
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
            }else{
                result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskCannotDelete());
            }
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @InitBinder("src")
    public void initBinderDetail(WebDataBinder binder){
        binder.setFieldDefaultPrefix("main.");
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
    @RequiresPermissions("FlowOrderShippingPlan:normal:export")
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
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowOrderShippingPlanVo>) rl.getData());

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
        	FlowOrderShippingPlanVo vo = flowOrderShippingPlanService.get(id);

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
    public FlowOrderShippingPlan main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowOrderShippingPlanService.getFlowOrderShippingPlan(id);
        }else{
            return new FlowOrderShippingPlan();
        }
    }

    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowOrderShippingPlan main,
                              @ModelAttribute("details") FlowOrderShippingPlanDetailsVo detailsVo){
        if(ACT_START.equals(act)){
            //保存数据
            if(StringUtils.isNotBlank(main.getId())){
                flowOrderShippingPlanService.update(main, detailsVo.getDetails());
            }else{
                flowOrderShippingPlanService.add(main, detailsVo.getDetails());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowOrderShippingPlanService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowOrderShippingPlanService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
        }else if(ACT_CANCEL.equals(act)){
            //作废
            RestResult result = new RestResult();
            try {
                List<FlowOrderShippingApply> flowOrders = flowOrderShippingApplyService.findByOrderShippingPlanBusinessId(businessId);
                if(flowOrders != null && flowOrders.size() > 0){
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < flowOrders.size(); i++) {
                        if(i > 0) {
                            sb.append(",");
                        }
                        sb.append(flowOrders.get(i).getId());
                    }
                    return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_order_shipping_plan_already_apply", businessId, sb.toString()));
                }
                flowOrderShippingPlanService.cancel(businessId);
                FlowOrderShippingPlan shippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(businessId);
                activitiService.cancelProcessByProcessInstanceId(shippingPlan.getProcessInstanceId());
                return result.setSuccess(true).setMsg(localeMessageSource.getMsgCancelSuccess(businessId));
            } catch (Exception e) {
                return result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }
        }else{
            if(main.getFlowStatus() == Constants.FlowStatus.ADJUST_APPLY.code.intValue()){
                flowOrderShippingPlanService.update(main, detailsVo.getDetails());
            }
            return approveFlow(businessId, act, flowRemark, flowNextHandlerAccount);
        }
    }

    /**
     * 启动流程
     */
    private RestResult start(String businessId){
        RestResult result = new RestResult();
        try{
            FlowOrderShippingPlan shippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(businessId);
            ProcessInstance processInstance = activitiService.startWorkFlow(shippingPlan);
            flowOrderShippingPlanService.updateOrderShippingStatusByBusinessId(businessId, 1);
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

    private RestResult approveFlow(String businessId, String act, String flowRemark, String flowNextHandlerAccount){
        RestResult result = new RestResult();
        UserVo user = SessionUtils.currentUserVo();
        FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowOrderShippingPlan.getProcessInstanceId()).singleResult();
        if(task != null){
            //提交任务
            activitiService.submitTask(task.getId(), flowOrderShippingPlan, getApprovedValue(act), flowRemark , flowNextHandlerAccount);
            if(Constants.FlowAct.REFUSE.code.equals(getApprovedValue(act))){
                //终止任务时
                flowOrderShippingPlanService.updateOrderShippingStatusByBusinessId(businessId, 2);
            }
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }else {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
        }
        return result;
    }
}
