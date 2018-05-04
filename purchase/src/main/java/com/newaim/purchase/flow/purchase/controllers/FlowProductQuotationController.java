package com.newaim.purchase.flow.purchase.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotation;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.service.FlowNewProductService;
import com.newaim.purchase.flow.purchase.service.FlowProductQuotationService;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanService;
import com.newaim.purchase.flow.purchase.vo.FlowProductQuotationDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowProductQuotationVo;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotation;
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
import java.util.Map;

@RestController
@RequestMapping("/flow/purchase/quotation")
public class FlowProductQuotationController extends ControllerBase{

    @Autowired
    private FlowProductQuotationService flowProductQuotationService;

    @Autowired
    private FlowPurchasePlanService flowPurchasePlanService;

    @Autowired
    private FlowNewProductService flowNewProductService;

    @Autowired
    private TaskService taskService;

	@Autowired
	private ActivitiService activitiService;
	
    @Autowired
    private ExportService exportService;
	
	@RequiresPermissions("FlowProductQuotation:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal,String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
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
				params.put("assigneeCnName-S-LK-OR", keywords);
				params.put("assigneeEnName-S-LK-OR", keywords);
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
			
			params = setParams(params, "FlowProductQuotation", ":4:3:2:1", false);
			
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
            
			Page<FlowProductQuotationVo> page = flowProductQuotationService.list(params, pageNumber, pageSize, getSort(sort));
			result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
        return result;
    }
    
	@RequiresPermissions("FlowProductQuotation:normal:list")
    @PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			FlowProductQuotationVo rd =  flowProductQuotationService.get(id);
			rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
	@RequiresPermissions(value = {"FlowProductQuotation:normal:add", "FlowProductQuotation:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") FlowProductQuotation main
			, @ModelAttribute("detail") FlowProductQuotationDetailsVo detailsVo
			,@ModelAttribute("attachments") AttachmentsVo attachments) {
		RestResult result = new RestResult();
		try {
			if( StringUtils.isNotBlank(main.getId())){

				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    flowProductQuotationService.saveAs(main, detailsVo.getDetails(),attachments.getAttachments());
				}else{
                    flowProductQuotationService.update(main, detailsVo.getDetails(),attachments.getAttachments());
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
                flowProductQuotationService.add(main, detailsVo.getDetails(),attachments.getAttachments());

				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("FlowProductQuotation:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			FlowProductQuotation flowProductQuotation = flowProductQuotationService.getFlowProductQuotation(ids);
            //流程未发起
			if(flowProductQuotation.getFlowStatus() == null){
				flowProductQuotationService.delete(flowProductQuotation);
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
    @RequiresPermissions("FlowProductQuotation:normal:export")
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
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowProductQuotationVo>) rl.getData());

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
        	FlowProductQuotationVo vo = flowProductQuotationService.get(id);

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
    public FlowProductQuotation main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowProductQuotationService.getFlowProductQuotation(id);
        }else{
        	return new FlowProductQuotation();
        }
    }

    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
							  @ModelAttribute("main") FlowProductQuotation main,
							  @ModelAttribute("detail") FlowProductQuotationDetailsVo detailsVo,
							  @ModelAttribute("attachments") AttachmentsVo attachments){
		if(ACT_START.equals(act)){
			//保存数据
			if(StringUtils.isNotBlank(main.getId())){
				flowProductQuotationService.update(main, detailsVo.getDetails(),attachments.getAttachments());
			}else{
				flowProductQuotationService.add(main, detailsVo.getDetails(),attachments.getAttachments());
				businessId = main.getId();
			}
			//启动流程
			return start(businessId);
		}else if(ACT_HOLD.equals(act)){
			//冻结
			flowProductQuotationService.hold(businessId);
			return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
		}else if(ACT_UN_HOLD.equals(act)){
			//解除冻结
			flowProductQuotationService.unHold(businessId);
			return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
		}else if(ACT_CANCEL.equals(act)){
			//作废
			RestResult result = new RestResult();
			try {
				List<FlowPurchasePlan> flowPurchasePlans = flowPurchasePlanService.findByProductQuotationBusinessId(businessId);
				if(flowPurchasePlans != null && flowPurchasePlans.size() > 0){
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < flowPurchasePlans.size(); i++) {
						if(i > 0) {
							sb.append(",");
						}
						sb.append(flowPurchasePlans.get(i).getId());
					}
					return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_product_quotation_already_used_plan", businessId, sb.toString()));
				}
				List<FlowNewProduct> flowNewProducts = flowNewProductService.findByProductQuotationBusinessId(businessId);
				if(flowNewProducts != null && flowNewProducts.size() > 0){
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < flowNewProducts.size(); i++) {
						if(i > 0) {
							sb.append(",");
						}
						sb.append(flowNewProducts.get(i).getId());
					}
					return result.setSuccess(false).setMsg(localeMessageSource.getMessage("msg_product_quotation_already_used_new", businessId, sb.toString()));
				}
				flowProductQuotationService.cancel(businessId);
	            FlowProductQuotation quotation = flowProductQuotationService.getFlowProductQuotation(businessId);
				activitiService.cancelProcessByProcessInstanceId(quotation.getProcessInstanceId());
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
            FlowProductQuotation quotation = flowProductQuotationService.getFlowProductQuotation(businessId);
            ProcessInstance processInstance = activitiService.startWorkFlow(quotation);
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
    private RestResult approveFlow(String businessId, String act, String flowRemark, String flowNextHandlerAccount){
    	RestResult result = new RestResult();
		UserVo user = SessionUtils.currentUserVo();
    	FlowProductQuotation flowProductQuotation = flowProductQuotationService.getFlowProductQuotation(businessId);
		Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowProductQuotation.getProcessInstanceId()).singleResult();
		if(task != null){
			//提交任务
			activitiService.submitTask(task.getId(), flowProductQuotation, getApprovedValue(act), flowRemark , flowNextHandlerAccount);
			result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
		}else {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
		}
		return result;
	}

}
