package com.newaim.purchase.flow.purchase.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseBalanceRefundUnionsVo;
import com.newaim.purchase.desktop.reports.service.ReportsProductService;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.service.FlowPurchaseContractService;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanService;
import com.newaim.purchase.flow.purchase.vo.FlowPurchasePlanDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowPurchasePlanVo;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
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
@RequestMapping("/flow/purchase/plan")
public class FlowPurchasePlanController extends ControllerBase {

    @Autowired
    private FlowPurchasePlanService flowPurchasePlanService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;
    
    @Autowired
    private ExportService exportService;

    @Autowired
    private ReportsProductService reportsProductService;

    @Autowired
    private FlowPurchaseContractService flowPurchaseContractService;
    
    @RequiresPermissions("FlowPurchasePlan:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request,String modal, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                           Integer type
    ){
        RestResult result = new RestResult();
        try {

            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
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

            params = setParams(params, "FlowPurchasePlan", ":4:3:2:1", false);
            
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
            if (null != type) {
                if (type == 1) {
                    //1. 产品分析报告中的采购计划选择器
                    params.put("status-S-EQ-OR", Constants.Status.DRAFT.code);
                    params.put("flowStatus-S-EQ-OR", Constants.FlowStatus.ADJUST_APPLY.code);
                }
            }
            Page<FlowPurchasePlanVo> page = flowPurchasePlanService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowPurchasePlan:normal:list")
    @PostMapping("/get")
    public RestResult get(String id, Boolean noReport) {
        RestResult result = new RestResult();
        try {
            FlowPurchasePlanVo rd =  flowPurchasePlanService.get(id);

            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));

            //加载报告
            if(noReport == null || BooleanUtils.isFalse(noReport)) {
                rd.setReports(reportsProductService.listByBusinessId(id, -1));
            }

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions(value = {"FlowPurchasePlan:normal:add", "FlowPurchasePlan:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowPurchasePlan main,
                           @ModelAttribute("details") FlowPurchasePlanDetailsVo detailsVo,
                           @ModelAttribute("purchaseBalanceRefundUnions") PurchaseBalanceRefundUnionsVo purchaseBalanceRefundUnionsVo,
                           @ModelAttribute("attachments") AttachmentsVo attachments) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){

                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    flowPurchasePlanService.saveAs(main,detailsVo.getDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                }else{
                    flowPurchasePlanService.update(main, detailsVo.getDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else{
                flowPurchasePlanService.add(main,detailsVo.getDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }
        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions("FlowPurchasePlan:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowPurchasePlan flowPurchasePlan = flowPurchasePlanService.getFlowPurchasePlan(ids);
            //流程未发起
            if(flowPurchasePlan.getFlowStatus() == null){
                flowPurchasePlanService.delete(flowPurchasePlan);
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
    @RequiresPermissions("FlowPurchasePlan:normal:export")
    @PostMapping("/export")
    public RestResult export(String act, String fileTitle, String fileName, String id, @ModelAttribute("columns") ExportColumnsVo columns,
                             HttpServletRequest request, String modal, String sort, String keywords,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                             Integer type
    ) {
        RestResult result = new RestResult();

        if (ACT_PDF.equals(act) || ACT_EXCEL.equals(act) || ACT_CSV.equals(act)) {
            RestResult rl = list(request, modal, sort, keywords, pageNumber, pageSize, type);
            if (rl.getTc() < 1) {
                result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
                return result;
            }

            try {
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowPurchasePlanVo>) rl.getData());

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
        	FlowPurchasePlanVo vo = flowPurchasePlanService.get(id);

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
    public FlowPurchasePlan main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowPurchasePlanService.getFlowPurchasePlan(id);
        }else{
            return new FlowPurchasePlan();
        }
    }


    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowPurchasePlan main,
                              @ModelAttribute("details") FlowPurchasePlanDetailsVo detailsVo,
                              @ModelAttribute("purchaseBalanceRefundUnions") PurchaseBalanceRefundUnionsVo purchaseBalanceRefundUnionsVo,
                              @ModelAttribute("attachments") AttachmentsVo attachments){
        if(ACT_START.equals(act)){
            //保存数据
            if(StringUtils.isNotBlank(main.getId())){
                flowPurchasePlanService.update(main,detailsVo.getDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
            }else{
                flowPurchasePlanService.add(main,detailsVo.getDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowPurchasePlanService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowPurchasePlanService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
        }else if(ACT_CANCEL.equals(act)){
            //作废
            RestResult result = new RestResult();
            try {
                List<FlowPurchaseContract> flowOrders = flowPurchaseContractService.findByProductPurchasePlanBusinessId(businessId);
                if(flowOrders != null && flowOrders.size() > 0){
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < flowOrders.size(); i++) {
                        if(i > 0) {
                            sb.append(",");
                        }
                        sb.append(flowOrders.get(i).getId());
                    }
                    return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_purchase_plan_already_used", businessId, sb.toString()));
                }
                flowPurchasePlanService.cancel(businessId);
                FlowPurchasePlan flowPurchasePlan = flowPurchasePlanService.getFlowPurchasePlan(businessId);
                activitiService.cancelProcessByProcessInstanceId(flowPurchasePlan.getProcessInstanceId());
                return result.setSuccess(true).setMsg(localeMessageSource.getMsgCancelSuccess(businessId));
            } catch (Exception e) {
                return result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }
        }else{
            return approveFlow(businessId, act, flowRemark, flowNextHandlerAccount);
        }
    }

    /**
     * 启动流程
     */
    private RestResult start(String businessId){
        RestResult result = new RestResult();
        try{
            FlowPurchasePlan flowPurchasePlan = flowPurchasePlanService.getFlowPurchasePlan(businessId);
            ProcessInstance processInstance = activitiService.startWorkFlow(flowPurchasePlan);
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
        FlowPurchasePlan flowPurchasePlan = flowPurchasePlanService.getFlowPurchasePlan(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowPurchasePlan.getProcessInstanceId()).singleResult();
        if(task != null){
            //提交任务
            activitiService.submitTask(task.getId(), flowPurchasePlan, getApprovedValue(act), flowRemark , flowNextHandlerAccount);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }else {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
        }
        return result;
    }

}
