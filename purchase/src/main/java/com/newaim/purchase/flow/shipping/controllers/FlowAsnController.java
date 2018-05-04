package com.newaim.purchase.flow.shipping.controllers;


import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
import com.newaim.purchase.api.dto.WmsAsnCreateResultDto;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingService;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.service.FlowAsnPackingService;
import com.newaim.purchase.flow.shipping.service.FlowAsnService;
import com.newaim.purchase.flow.shipping.vo.FlowAsnPackingListVo;
import com.newaim.purchase.flow.shipping.vo.FlowAsnVo;
import com.newaim.purchase.flow.workflow.service.ActivitiService;

@RestController
@RequestMapping("/flow/shipping/asn")
public class FlowAsnController extends ControllerBase{

    @Autowired
    private FlowAsnService flowAsnService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;
    
    @Autowired
    private ExportService exportService;

    @Autowired
    private WmsApiService wmsApiService;
    
    @Autowired
    private FlowAsnPackingService flowAsnPackingService;
    
    @Autowired
    private CustomClearancePackingService customClearancePackingService;
    
    @RequiresPermissions("FlowOrderReceivingNotice:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal,String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
        				params.put("asnNumber-S-LK-OR", keywords);
        				params.put("receiveLocation-S-LK-OR", keywords);
        				params.put("creatorCnName-S-LK-OR", keywords);
        				params.put("creatorEnName-S-LK-OR", keywords);
        				params.put("departmentCnName-S-LK-OR", keywords);
        				params.put("departmentEnName-S-LK-OR", keywords);
        				params.put("containerNumber-S-LK-OR", keywords);
        				params.put("sealsNumber-S-LK-OR", keywords);
        				params.put("containerType-S-LK-OR", keywords);
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
            params = setParams(params, "FlowOrderReceivingNotice", ":4:3:2:1", false);
            
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
            
            Page<FlowAsnVo> page = flowAsnService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowOrderReceivingNotice:normal:list")
    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            FlowAsnVo rd =  flowAsnService.get(id);
            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/existsAsnNumber")
    public RestResult existsAsnNumber(String asnNumber, String id){
        RestResult result = new RestResult();
        try{
            boolean existsAsnNumber = false;
            if(StringUtils.isBlank(id)){
                existsAsnNumber = flowAsnService.existsAsnNumber(asnNumber);
            }else{
                FlowAsn flowAsn = flowAsnService.getFlowAsn(id);
                if(!StringUtils.equals(asnNumber, flowAsn.getAsnNumber())){
                    existsAsnNumber = flowAsnService.existsAsnNumber(asnNumber);
                }
            }
            result.setSuccess(true);
            if(!existsAsnNumber){
                result.setMsg(localeMessageSource.getMsgOperateSuccess()).setData(true);
            }else{
                result.setMsg(localeMessageSource.getMsgProductExistsAsnNumber(asnNumber)).setData(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }
    
    @RequiresPermissions(value = {"FlowOrderReceivingNotice:normal:add", "FlowOrderReceivingNotice:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowAsn main, @ModelAttribute("details") FlowAsnPackingListVo details) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    flowAsnService.saveAs(main, details.getDetails());
                }else{
                    flowAsnService.update(main, details.getDetails());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else{
                flowAsnService.add(main, details.getDetails());
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }
        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowOrderReceivingNotice:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowAsn flowAsn = flowAsnService.getFlowAsn(ids);
            //流程未发起
            if(flowAsn.getFlowStatus() == null){
                flowAsnService.delete(flowAsn);
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
    @RequiresPermissions("FlowOrderReceivingNotice:normal:export")
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
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowAsnVo>) rl.getData());

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
        	FlowAsnVo vo = flowAsnService.get(id);

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
    public FlowAsn main(String id){
        if(StringUtils.isNotBlank(id)){
            return flowAsnService.getFlowAsn(id);
        }else{
            return new FlowAsn();
        }
    }

    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowAsn main,
                              @ModelAttribute("details") FlowAsnPackingListVo details){
        if(ACT_START.equals(act)){
            //保存数据
            if(StringUtils.isNotBlank(main.getId())){
                flowAsnService.update(main, details.getDetails());
            }else{
                flowAsnService.add(main, details.getDetails());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowAsnService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowAsnService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
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
            UserVo user = SessionUtils.currentUserVo();
            FlowAsn flowAsn = flowAsnService.getFlowAsn(businessId);
            flowAsn.setFlagCompleteStatus(2);
            flowAsn.setFlagSyncStatus(1);
            RestResult asnCreateResult = wmsApiService.createAsn(flowAsn.getId());
            String asnCreateResultStr ="  ";
            if(asnCreateResult != null && asnCreateResult.getSuccess()){
            	flowAsn.setFlagSyncStatus(3);
            	flowAsn.setFlagSyncDate(new Date());
                WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) asnCreateResult.getData();
            	flowAsn.setAsnNumber(resultDto.getAsnNo());
            }else{
            	WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) asnCreateResult.getData();
            	String skus = StringUtils.join(resultDto.getInvalidSkus(), ",");
            	asnCreateResultStr = ". warning : "+skus +" no exist in wms product" ;
            }
            flowAsnService.save(flowAsn);
            ProcessInstance processInstance = activitiService.startWorkFlow(flowAsn, user);
            
            List<FlowAsnPacking> asnPackings = flowAsnPackingService.findPackingsByBusinessId(flowAsn.getId());
            FlowAsnPacking asnPacking = asnPackings.get(0);
            CustomClearancePacking ccPacking = customClearancePackingService.findOne(asnPacking.getCcPackingId());
            ccPacking.setFlagAsnStatus(1);
            customClearancePackingService.save(ccPacking);
            
            result.setSuccess(true).setMsg("流程已启动，流程ID: " + processInstance.getId()+asnCreateResultStr);
        }catch (ActivitiException e){
            result.setSuccess(false);
            if(e.getMessage().indexOf("no processes deployed with key") != -1){
                logger.warn("没有部署流程", e);
                result.setMsg(e.getMessage());
            }else{
                logger.error("启动流程失败：", e);
                result.setMsg(e.getMessage());
            }
        }catch (IOException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            logger.error("系统异常", e);
        } catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            logger.error("系统异常", e);
        }
        return result;
    }

    private RestResult approveFlow(String businessId, String act, String flowRemark, String flowNextHandlerAccount){
        RestResult result = new RestResult();
        UserVo user = SessionUtils.currentUserVo();
        FlowAsn flowAsn = flowAsnService.getFlowAsn(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowAsn.getProcessInstanceId()).singleResult();
        if(task != null){

            if(activitiService.isEndNextNode(task.getId(), getApprovedValue(act))){
                if(flowAsn.getFlagSyncStatus() == null || flowAsn.getFlagSyncStatus().intValue() != 3){
                    result.setSuccess(false).setMsg("Asn received qty has not been synchronized.");
                    return result;
                }
            }

            //提交任务
            activitiService.submitTask(task.getId(), flowAsn, getApprovedValue(act), flowRemark , flowNextHandlerAccount);

            if(Constants.FlowAct.REFUSE.code.equals(getApprovedValue(act))){
                //终止任务时
                if(StringUtils.isNotBlank(flowAsn.getAsnNumber())){
                    try {
                    	//如果之后加asn作废功能，也要调一次wms的asn取消接口
                        	RestResult asnCreateResult = wmsApiService.cancelAsn(flowAsn.getAsnNumber());
	                        if(asnCreateResult != null && asnCreateResult.getSuccess()){
	                        	flowAsn.setFlagSyncStatus(3);
	                        	flowAsn.setFlagSyncDate(new Date());
	                        	flowAsnService.save(flowAsn);
	                        }else{
	                        	flowAsn.setFlagSyncStatus(2);
	                        	flowAsn.setFlagSyncDate(new Date());
	                        	flowAsnService.save(flowAsn);
	                        	
	                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
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
