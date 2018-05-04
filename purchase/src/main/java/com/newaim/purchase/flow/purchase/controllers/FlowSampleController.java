package com.newaim.purchase.flow.purchase.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.flow.finance.entity.FlowSamplePayment;
import com.newaim.purchase.flow.finance.service.FlowSamplePaymentService;
import com.newaim.purchase.flow.purchase.entity.FlowSample;
import com.newaim.purchase.flow.purchase.service.FlowSampleService;
import com.newaim.purchase.flow.purchase.vo.FlowSampleDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowSampleOtherDetailsVo;
import com.newaim.purchase.flow.purchase.vo.FlowSampleVo;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/flow/purchase/flowsample")
public class FlowSampleController extends ControllerBase {

    @Autowired
    private FlowSampleService flowSampleService;

    @Autowired
    private FlowSamplePaymentService flowSamplePaymentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    ExportService exportService;

    @Autowired
    private UserService userService;

    @RequiresPermissions("FlowSample:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if (StringUtils.isNotBlank(keywords)) {
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
            } else {
                params = ServletUtils.getParametersStartingWith(request);
                //搜索出指定部门以下的所有记录
                if (params.size() > 0) {
                    if (params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())) {
                        String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
                        params.remove("departmentId-S-EQ");
                        params.put("departmentId-S-IN", depIds);
                    }
                }

            }
            params = setParams(params, "FlowSample", ":4:3:2:1", false);

            //列表数据过滤
            if (null != modal && StringUtils.isNotBlank(modal)) {
                UserVo user = SessionUtils.currentUserVo();

                if ("mine".equals(modal)) {
                    //我发启的
                    params.remove("creatorId-S-EQ-ADD");
                    params.put("creatorId-S-EQ-ADD", user.getId());
                } else if ("involved".equals(modal)) {
                    //我参与的
//					params.put("history.operatorId-S-EQ-ADD", user.getId());
//                  params.put("history.businessId-S-GBY-NON", "");
                }
            }

            Page<FlowSampleVo> page = flowSampleService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("FlowSample:normal:list")
    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            FlowSampleVo rd = flowSampleService.get(id);
            rd.setFlowNextHandler(activitiService.findNextTaskHandler(rd.getProcessInstanceId()));
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions(value = {"FlowSample:normal:add", "FlowSample:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") FlowSample main,
                           @ModelAttribute("details") FlowSampleDetailsVo detailsVo,
                           @ModelAttribute("otherDetails") FlowSampleOtherDetailsVo otherDetailsVo,
                           @ModelAttribute("attachments") AttachmentsVo attachments) {
        RestResult result = new RestResult();
        try {
            if (StringUtils.isNotBlank(main.getId())) {

                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                    flowSampleService.saveAs(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), attachments.getAttachments());
                } else {
                    flowSampleService.update(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), attachments.getAttachments());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            } else {
                flowSampleService.add(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), attachments.getAttachments());

                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }

        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @RequiresPermissions("FlowSample:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            FlowSample flowSample = flowSampleService.getFlowSample(ids);
            //流程未发起
            if (flowSample.getFlowStatus() == null) {
                flowSampleService.delete(flowSample);
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
            } else {
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
    @RequiresPermissions("FlowSample:normal:export")
    @PostMapping("/export")
    public RestResult export(String act, String fileTitle, String fileName, String id, @ModelAttribute("columns") ExportColumnsVo columns,
                             ServletRequest request, String modal, String sort, String keywords,
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

                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<FlowSampleVo>) rl.getData());

                if (null != mydoc) {
                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
                } else {
                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        } else if (ACT_WORD.equals(act) || ACT_EMAIL_PDF.equals(act)) {
            RestResult row = get(id);

            try {
//                MyDocumentVo mydoc = exportService.exportFileForWord(request, act, fileTitle, fileName, id, (FlowSampleVo) row.getData());

//                if (null != mydoc) {
//                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
//                } else {
//                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
//                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        }

        return result;
    }

    @InitBinder("src")
    public void initBinderDetail(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("main.");
    }

    @ModelAttribute("main")
    public FlowSample main(String id) {
        if (StringUtils.isNotBlank(id)) {
            return flowSampleService.getFlowSample(id);
        } else {
            return new FlowSample();
        }
    }

    @PostMapping("/approve")
    public RestResult approve(@RequestParam("id") String businessId, String act, String flowRemark, String flowNextHandlerAccount,
                              @ModelAttribute("main") FlowSample main,
                              @ModelAttribute("details") FlowSampleDetailsVo detailsVo,
                              @ModelAttribute("otherDetails") FlowSampleOtherDetailsVo otherDetailsVo,
                              @ModelAttribute("attachments") AttachmentsVo attachments) {
        if (ACT_START.equals(act)) {
            //保存数据
            if (StringUtils.isNotBlank(main.getId())) {
                flowSampleService.update(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), attachments.getAttachments());
            } else {
                flowSampleService.add(main, detailsVo.getDetails(), otherDetailsVo.getOtherDetails(), attachments.getAttachments());
                businessId = main.getId();
            }
            //启动流程
            return start(businessId);
        }else if(ACT_HOLD.equals(act)){
            //冻结
            flowSampleService.hold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgHoldSuccess(businessId));
        }else if(ACT_UN_HOLD.equals(act)){
            //解除冻结
            flowSampleService.unHold(businessId);
            return new RestResult().setSuccess(true).setMsg(localeMessageSource.getMsgUnHoldSuccess(businessId));
        }else{
            return approveFlow(businessId, act, flowRemark, flowNextHandlerAccount);
        }
    }

    /**
     * 启动流程
     */
    private RestResult start(String businessId) {
        RestResult result = new RestResult();
        try {
            FlowSample flowSample = flowSampleService.getFlowSample(businessId);
            ProcessInstance processInstance = activitiService.startWorkFlow(flowSample);
            result.setSuccess(true).setMsg("流程已启动，流程ID: " + processInstance.getId());
        } catch (ActivitiException e) {
            result.setSuccess(false);
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.warn("没有部署流程", e);
                result.setMsg(e.getMessage());
            } else {
                logger.error("启动流程失败：", e);
                result.setMsg(e.getMessage());
            }
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            logger.error("系统异常", e);
        }
        return result;
    }

    private RestResult approveFlow(String businessId, String act, String flowRemark, String flowNextHandlerAccount) {
        RestResult result = new RestResult();
        UserVo user = SessionUtils.currentUserVo();
        FlowSample flowSample = flowSampleService.getFlowSample(businessId);
        Task task = taskService.createTaskQuery().taskAssignee(user.getAccount()).processInstanceId(flowSample.getProcessInstanceId()).singleResult();
        if (task != null) {
            //提交任务
            activitiService.submitTask(task.getId(), flowSample, getApprovedValue(act), flowRemark, flowNextHandlerAccount);
            if(flowSample.getEndTime() != null){
                FlowSamplePayment flowSamplePayment = flowSamplePaymentService.findBySampleBusinessId(flowSample.getId());
                if(flowSamplePayment != null){
                    //发启样品付款单
                    UserVo startUser = userService.get(flowSamplePayment.getCreatorId());
                    activitiService.startWorkFlow(flowSamplePayment, startUser);
                }
            }
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        } else {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgTaskNotFound());
        }
        return result;
    }

}
