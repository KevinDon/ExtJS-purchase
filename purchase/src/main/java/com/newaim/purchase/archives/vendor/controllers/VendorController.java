package com.newaim.purchase.archives.vendor.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/9/18.
 * updated by Lance.hu on 2017/12/8
 */
@RestController
@RequestMapping("/archives/vendor")
public class VendorController extends ControllerBase {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ArchivesHistoryService archivesHistoryService;

    @Autowired
    private ReportsProductService reportsProductService;

    @RequiresPermissions("Vendor:normal:list")
    @PostMapping("/list")
    public RestResult listVendor(ServletRequest request, String sort, String keywords,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if (StringUtils.isNotBlank(keywords)) {
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
            } else {
                params = ServletUtils.getParametersStartingWith(request);
            }

            params = setParams(params, "Vendor", ":4:3:2:1", false);

            Page<VendorVo> page = vendorService.listVendor(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"Vendor:normal:list", "Service:normal:list"}, logical = Logical.OR)
    public RestResult get(String id, String product, Boolean noReport) {
        RestResult result = new RestResult();
        try {
            VendorVo rd;
            if (StringUtils.isBlank(product)) {
                rd = vendorService.get(id);
            } else {
                rd = vendorService.getWithProducts(id);
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

    @PostMapping("/existsVendorCode")
    public RestResult existsVendorCode(String code, String id){
        RestResult result = new RestResult();
        try{
            boolean existsCode = false;
            if(StringUtils.isBlank(id)){
                existsCode = vendorService.existsVendorCode(code);
            }else{
                Vendor vendor = vendorService.getVendor(id);
                if(!StringUtils.equals(code, vendor.getCode())){
                    existsCode = vendorService.existsVendorCode(code);
                }
            }
            result.setSuccess(true);
            if(!existsCode){
                result.setMsg(localeMessageSource.getMsgOperateSuccess()).setData(true);
            }else{
                result.setMsg(localeMessageSource.getMsgProductExistsVendorCode(code)).setData(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @PostMapping("/save")
    @RequiresPermissions(value = {"Vendor:normal:add", "Vendor:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") Vendor main,
                           @ModelAttribute("productCategory") VendorProductCategoryUnionsVo productCategory,
                           @ModelAttribute("attachments") AttachmentsVo attachments,
                           @ModelAttribute("contactss") ContactssVo contactss
    ) {
        RestResult result = new RestResult();
        try {
            if (ACT_ADD.equals(act)) {

                vendorService.add(main, productCategory.getProductCategory(), attachments.getAttachments(), contactss.getContactss());
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

            } else if (StringUtils.isNotBlank(main.getId())) {
                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                    vendorService.saveAs(main, productCategory.getProductCategory(), attachments.getAttachments(), contactss.getContactss());
                } else {
                    if (main.getStatus() > 0) {
                        //储审核数据
                        try {
                            Vendor newMain = BeanMapper.map(main, Vendor.class);
                            archivesHistoryService.clean();
                            VendorVo newObj = vendorService.getApplyVoToSave(newMain, productCategory.getProductCategory(), attachments.getAttachments(), contactss.getContactss());
                            archivesHistoryService.save("Vendor", newMain.getId(), newObj);

                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                        }catch(Exception e){
                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                        }

                    } else {
                        //保存
                        vendorService.save(main, productCategory.getProductCategory(), attachments.getAttachments(), contactss.getContactss());
                        result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                    }
                }
            }
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"Vendor:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            vendorService.deleteVendor(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"Vendor:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "Vendor");
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
    @RequiresPermissions(value = {"Vendor:normal:list", "Service:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);

            VendorVo rd = vendorService.getApplyVoToDisplay((VendorVo) BeanMapper.toBean(VendorVo.class, BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()))));

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"Vendor:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") Vendor main,
                                     @ModelAttribute("productCategory") VendorProductCategoryUnionsVo productCategory,
                                     @ModelAttribute("attachments") AttachmentsVo attachments,
                                     @ModelAttribute("contactss") ContactssVo contactss) {
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){

                vendorService.save(main, productCategory.getProductCategory(), attachments.getAttachments(), contactss.getContactss());
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
    @RequiresPermissions("Vendor:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("Vendor" + ":4")){
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
    public Vendor main(String act, String id) {
        if (StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)) {
            return vendorService.getVendor(id);
        }
        return new Vendor();
    }

    @InitBinder("bank")
    protected void initBinderBank(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("bank.");
    }
}
