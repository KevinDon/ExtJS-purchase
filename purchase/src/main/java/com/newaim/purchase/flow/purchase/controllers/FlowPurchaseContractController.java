package com.newaim.purchase.flow.purchase.controllers;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseBalanceRefundUnionsVo;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import com.newaim.purchase.flow.finance.entity.FlowSamplePayment;
import com.newaim.purchase.flow.finance.service.FlowPurchaseContractDepositService;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.inspection.service.FlowOrderQcService;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.purchase.service.FlowPurchaseContractService;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractOtherDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractVo;
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
@RequestMapping("/flow/purchase/purchasecontract")
public class FlowPurchaseContractController extends ControllerBase {

    @Autowired
    private FlowPurchaseContractService flowPurchaseContractService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;
    
    @Autowired
    private ExportService exportService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowOrderQcService flowOrderQcService;

    @Autowired
    private FlowPurchaseContractDepositService flowPurchaseContractDepositService;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;
    
    @RequiresPermissions("FlowPurchaseContract:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal,String sort, String keywords, String type, String vendorId,
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
    /*        	params.put("sellerCnName-S-LK-OR", keywords);
            	params.put("sellerEnName-S-LK-OR", keywords);
            	params.put("sellerCnAddress-S-LK-OR", keywords);
            	params.put("sellerEnAddress-S-LK-OR", keywords);
            	params.put("sellerContactCnName-LK-OR", keywords);
            	params.put("sellerContactEnName-S-LK-OR", keywords);
            	params.put("sellerEmail-S-LK-OR", keywords);
            	params.put("originPortCnName-S-LK-OR", keywords);
            	params.put("originPortEnName-S-LK-OR", keywords);*/
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

            if (type != null) {
                if (type.equals("2")) {
                    //如果供应商ID不为空，则根据供应商ID查找
                    if (StringUtils.isNotBlank(vendorId)){
                        params.put("vendorId-S-EQ-ADD", vendorId);
                    }
                }
            }

            params = setParams(params, "FlowPurchaseContract", ":4:3:2:1", false);
            
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
            
            Page<FlowPurchaseContractVo> page = flowPurchaseContractService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowPurchaseContract:normal:list")
    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            FlowPurchaseContractVo rd =  flowPurchaseContractService.get(id);
            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions(value = {"FlowPurchaseContract:normal:add", "FlowPurchaseContract:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowPurchaseContract main,
                           @ModelAttribute("detail") FlowPurchaseContractDetailsVo detailsVo,
                           @ModelAttribute("otherDetail") FlowPurchaseContractOtherDetailsVo otherDetailsVo,
                           @ModelAttribute("purchaseBalanceRefundUnions") PurchaseBalanceRefundUnionsVo purchaseBalanceRefundUnionsVo,
                           @ModelAttribute("attachments") AttachmentsVo attachments) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){

                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    flowPurchaseContractService.saveAs(main,detailsVo.getDetails(), otherDetailsVo.getOtherDetails() ,purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                }else{
                    flowPurchaseContractService.update(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails() ,purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else{
                flowPurchaseContractService.add(main,detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }
        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions("FlowPurchaseContract:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowPurchaseContract flowPurchaseContract = flowPurchaseContractService.getFlowPurchaseContract(ids);
            //流程未发起
            if(flowPurchaseContract.getFlowStatus() == null){
                flowPurchaseContractService.delete(flowPurchaseContract);
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
    @RequiresPermissions("FlowPurchaseContract:normal:export")
    @PostMapping("/export")
    public RestResult export(String act, String fileTitle, String fileName, String id, @ModelAttribute("columns") ExportColumnsVo columns,
                             HttpServletRequest request, String modal, String sort, String keywords,String type, String vendorId,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();

        if (ACT_PDF.equals(act) || ACT_EXCEL.equals(act) || ACT_CSV.equals(act)) {
            RestResult rl = list(request, modal, sort, keywords, type, vendorId, pageNumber, pageSize);
            if (rl.getTc() < 1) {
                result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
                return result;
            }

            try {
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowPurchaseContractVo>) rl.getData());

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
        	FlowPurchaseContractVo vo = flowPurchaseContractService.getForMerge(id);

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
    public FlowPurchaseContract main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowPurchaseContractService.getFlowPurchaseContract(id);
        }else{
            return new FlowPurchaseContract();
        }
    }


    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowPurchaseContract main,
                              @ModelAttribute("detail") FlowPurchaseContractDetailsVo detailsVo,
                              @ModelAttribute("otherDetail") FlowPurchaseContractOtherDetailsVo otherDetailsVo,
                              @ModelAttribute("purchaseBalanceRefundUnions") PurchaseBalanceRefundUnionsVo purchaseBalanceRefundUnionsVo,
                              @ModelAttribute("attachments") AttachmentsVo attachments){
        if(ACT_START.equals(act)){
            //保存数据
            if(StringUtils.isNotBlank(main.getId())){
                flowPurchaseContractService.update(main,detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
            }else{
                flowPurchaseContractService.add(main,detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowPurchaseContractService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowPurchaseContractService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
        }else if(ACT_CANCEL.equals(act)){
            //作废
            RestResult result = new RestResult();
            FlowPurchaseContract flowOrder = flowPurchaseContractService.getFlowPurchaseContract(businessId);
            PurchaseContract order = null;
            if(flowOrder.getFlowStatus() != null && flowOrder.getFlowStatus() == Constants.FlowStatus.PASS.code.intValue()){
                order = purchaseContractDao.findLastByBusinessId(businessId);
                if(order != null){
                    if(order.getFlagContractDepositStatus() == 1 && order.getDepositAud().add(order.getTotalOtherDepositAud()).doubleValue() > 0){
                        return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_order_already_used_deposit", businessId, order.getFlagContractDepositId()));
                    }
                    if(order.getFlagOrderShippingApplyStatus() == 1){
                        return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_order_already_used_shipping_apply", businessId, order.getFlagOrderShippingApplyId()));
                    }
                }
            }
            flowPurchaseContractService.cancel(flowOrder, order);
            FlowPurchaseContract flowPurchaseContract = flowPurchaseContractService.getFlowPurchaseContract(businessId);
            activitiService.cancelProcessByProcessInstanceId(flowPurchaseContract.getProcessInstanceId());
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgCancelSuccess(businessId));
        }else{
            if(ACT_ALLOW.equals(act) && Constants.FlowStatus.ADJUST_APPLY.code.equals(main.getFlowStatus())){
                //调整申请提交时保存表单数据
                flowPurchaseContractService.update(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), purchaseBalanceRefundUnionsVo.getPurchaseBalanceRefundUnions(), attachments.getAttachments());
            }
            return approveFlow(businessId, act, flowRemark, flowNextHandlerAccount, attachments.getAttachments());
        }
    }

    /**
     * 启动流程
     */
    private RestResult start(String businessId){
        RestResult result = new RestResult();
        try{
            FlowPurchaseContract flowPurchaseContract = flowPurchaseContractService.getFlowPurchaseContract(businessId);
            ProcessInstance processInstance = activitiService.startWorkFlow(flowPurchaseContract);
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

    private RestResult approveFlow(String businessId, String act, String flowRemark, String flowNextHandlerAccount, List<Attachment> attachments){
        RestResult result = new RestResult();
        UserVo user = SessionUtils.currentUserVo();
        String approved = getApprovedValue(act);
        FlowPurchaseContract flowPurchaseContract = flowPurchaseContractService.getFlowPurchaseContract(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowPurchaseContract.getProcessInstanceId()).singleResult();
        if(task != null){
            if(activitiService.isEndNextNode(task.getId(), approved)){
                //最后一个环节时，可以保存附件
                attachmentService.save(attachments, FlowPurchaseContractService.FILE_MODULE_NAME, businessId);
            }
            //提交任务
            activitiService.submitTask(task.getId(), flowPurchaseContract, approved, flowRemark , flowNextHandlerAccount);

            if(Constants.FlowAct.REFUSE.code.equals(approved)){
                //拒绝时还原采购计划中sku数量
                flowPurchaseContractService.restorePurchasePlan(businessId);
            }

            if(flowPurchaseContract.getEndTime() != null){
                //任务结束后
                if(flowPurchaseContract.getIsNeededQc() != null && flowPurchaseContract.getIsNeededQc() == 2){
                    //需要质检的，发起质检流程
                    PurchaseContract order = purchaseContractDao.findLastByBusinessId(businessId);
                    if(order != null){
                        FlowOrderQc flowOrderQc = flowOrderQcService.findByOrderId(order.getId());
                        UserVo startUser = userService.get(flowOrderQc.getCreatorId());
                        String nextHandlerAccount = userService.getUser(flowPurchaseContract.getCreatorId()).getAccount();
                        activitiService.startWorkFlow(flowOrderQc, startUser, nextHandlerAccount);
                    }
                }
                //采购合同正常通过后，自动创建的合同定金单发启流程
                PurchaseContract order = purchaseContractDao.findLastByBusinessId(businessId);
                if (order != null && order.getDepositAud().intValue() > 0){
                    FlowPurchaseContractDeposit flowPurchaseContractDeposit = flowPurchaseContractDepositService.findByOrderId(order.getId());
                    if(flowPurchaseContractDeposit != null){
                        //发启合同定金流程
                        UserVo startUser = userService.get(flowPurchaseContractDeposit.getCreatorId());
                        activitiService.startWorkFlow(flowPurchaseContractDeposit, startUser);
                    }
                }
            }
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }else {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
        }
        return result;
    }

}
