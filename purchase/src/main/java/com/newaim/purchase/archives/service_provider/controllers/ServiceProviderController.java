package com.newaim.purchase.archives.service_provider.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderService;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderChargeItemsVo;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderPortsVo;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderVo;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.vo.VendorProductCategoryUnionsVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.desktop.email.vo.ContactssVo;

import com.newaim.purchase.desktop.reports.service.ReportsProductService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
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
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archives/service_provider")
public class ServiceProviderController extends ControllerBase {

    @Autowired
    private ServiceProviderService serviceProviderService;
    
    @Autowired
    private ArchivesHistoryService archivesHistoryService;

    @Autowired
    private ReportsProductService reportsProductService;


    @RequiresPermissions("Service:normal:list")
    @PostMapping("/list")
    public RestResult listServiceProvider(ServletRequest request, String sort, String keywords,
                                          @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                          @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("director-S-LK-OR", keywords);
                params.put("address-S-LK-OR", keywords);
                params.put("abn-S-LK-OR", keywords);
                params.put("website-S-LK-OR", keywords);
                params.put("buyerCnName-S-LK-OR", keywords);
                params.put("buyerEnName-S-LK-OR", keywords);
                params.put("cnName-S-LK-OR", keywords);
                params.put("enName-S-LK-OR", keywords);
                params.put("id-S-LK-OR", keywords);
                params.put("code-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
            params = setParams(params, "Service", ":4:3:2:1", false);
            Page<ServiceProviderVo> page = serviceProviderService.listServiceProvider(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"Service:normal:add", "Service:normal:edit"}, logical = Logical.OR)
    public RestResult get(String id, String product, Boolean noReport) {
        RestResult result = new RestResult();
        try {
            ServiceProviderVo rd;
            if (StringUtils.isBlank(product)){
                rd = serviceProviderService.get(id);
            }else {
                rd = serviceProviderService.getWithProducts(id);
            }

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

    @PostMapping("/save")
    @RequiresPermissions(value = {"Service:normal:add", "Service:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") ServiceProvider main,
                           @ModelAttribute("attachments") AttachmentsVo attachments,
                           @ModelAttribute("ports") ServiceProviderPortsVo ports,
                           @ModelAttribute("chargeItems") ServiceProviderChargeItemsVo chargeItems,
                           @ModelAttribute("contactss") ContactssVo contactss){
        RestResult result = new RestResult();
        try {
            if(ACT_ADD.equals(act)){

                serviceProviderService.add(main, attachments.getAttachments(),contactss.getContactss(), ports.getPorts(), chargeItems.getChargeItems());
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                
            }else if(StringUtils.isNotBlank(main.getId())){
                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                	//serviceProvider.setId(null);
                    serviceProviderService.saveAs(main, attachments.getAttachments(),contactss.getContactss(), ports.getPorts(), chargeItems.getChargeItems());
                } else {
                    if (main.getStatus() > 0) {
                        //储审核数据
                        try {
                        	ServiceProvider newMain = BeanMapper.map(main, ServiceProvider.class);
                            archivesHistoryService.clean();
                            ServiceProviderVo newObj = serviceProviderService.getApplyVoToSave(newMain, attachments.getAttachments(),contactss.getContactss(), ports.getPorts(), chargeItems.getChargeItems());
                            archivesHistoryService.save("Service", newMain.getId(), newObj);

                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                        }catch(Exception e){
                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                        }

                    } else {
                        //保存
                    	serviceProviderService.save(main, attachments.getAttachments(),contactss.getContactss(), ports.getPorts(), chargeItems.getChargeItems());
                        result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                    }
                }
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"Service:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            serviceProviderService.deleteServiceProvider(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    
    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"Service:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "Service");
            params.put("businessId-S-EQ-ADD", bid);
            params.put("status-N-LT-ADD", 3);

            Page<ArchivesHistoryVo> rd = archivesHistoryService.list(params, pageNumber, pageSize, getSort(sort));

            result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/historyget")
    @RequiresPermissions(value = {"Service:normal:list", "Vendor:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);

            ServiceProviderVo rd = serviceProviderService.getApplyVoToDisplay((ServiceProviderVo) BeanMapper.toBean(ServiceProviderVo.class, BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()))));

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"Service:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") ServiceProvider main,
            @ModelAttribute("attachments") AttachmentsVo attachments,
            @ModelAttribute("ports") ServiceProviderPortsVo ports,
            @ModelAttribute("chargeItems") ServiceProviderChargeItemsVo chargeItems,
            @ModelAttribute("contactss") ContactssVo contactss){
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){

            	serviceProviderService.save(main, attachments.getAttachments(),contactss.getContactss(), ports.getPorts(), chargeItems.getChargeItems());
                archivesHistoryService.confirm(ahid);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgArchivesConfirmSuccess());
            }else{
                result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure());
            }
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure());
        }

        return result;
    }

    @PostMapping("/historydelete")
    @RequiresPermissions("Service:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("Service" + ":4")){
                //物理删除
                archivesHistoryService.delete(ids);
            }else{
                //删除标记
                archivesHistoryService.setDelete(ids);
            }

            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }
    
    
    
    @ModelAttribute("main")
    public ServiceProvider main(String act, String id) {
    	if (StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)) {
            return serviceProviderService.getServiceProvider(id);
        }
        return new ServiceProvider();
    }

    @InitBinder("bank")
    protected void initBinderBank(WebDataBinder binder){
        binder.setFieldDefaultPrefix("bank.");
    }
}
