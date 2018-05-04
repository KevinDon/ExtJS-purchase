package com.newaim.purchase.archives.product.controllers;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletRequest;

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

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.archives.product.service.ProductCertificateService;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionsVo;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.vendor.service.VendorService;

@RestController
@RequestMapping("/archives/productCertificate")
public class ProductCertificateController extends ControllerBase {

    private final static String MSG_CONFIRM_PRODUCT_SUCCESS = "msg_confirm_product_success";
    private final static String MSG_CONFIRM_PRODUCT_FAILURE = "msg_confirm_product_failure";

    @Autowired
    private ProductCertificateService productCertificateService;
    @Autowired
    private VendorService vendorService;
    
    @Autowired
    private ArchivesHistoryService archivesHistoryService;

    @PostMapping("/list")
    @RequiresPermissions("ProductCertificate:normal:list")
    public RestResult listProduct(ServletRequest request, String sort, String keywords,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			params.put("status-N-EQ-ADD", "3");
            if(StringUtils.isNotBlank(keywords)){
                params.put("sku-S-LK-OR", keywords);
                params.put("vendorCnName-S-LK-OR", keywords);
                params.put("vendorEnName-S-LK-OR", keywords);
                params.put("relevantStandard-S-LK-OR", keywords);
                params.put("certificateNumber-S-LK-OR", keywords);
                params.put("description-S-LK-OR", keywords);
                params.put("id-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }

//            params.put("newProduct-S-EQ", Product.NORMAL_PRODUCT.toString());
            params = setParams(params, "ProductCertificate", ":4:3:2:1", false);
            Page<ProductCertificateVo> page = productCertificateService.listProduct(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions("ProductCertificate:normal:list")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            ProductCertificateVo rd =  productCertificateService.get(id);
//            if(StringUtils.isNotBlank(rd.getVendorId()) && !StringUtils.isEmpty(rd.getVendorId())){
//                rd.setVendor(vendorService.get(rd.getVendorId()));
//            }
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/save")
    @RequiresPermissions(value = {"ProductCertificate:normal:add", "ProductCertificate:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") ProductCertificate main, @ModelAttribute("details") ProductCertificateUnionsVo details){
        RestResult result = new RestResult();
        try {
            if (ACT_ADD.equals(act)) {
                productCertificateService.add(main,details.getDetails());
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));

            }else {
                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                    productCertificateService.saveAs(main, details.getDetails());
                }else{
                    if (main.getStatus() > 0) {
                        //储审核数据
                        try {
                            ProductCertificate newMain = BeanMapper.map(main, ProductCertificate.class);
                            archivesHistoryService.clean();
                            ProductCertificateVo newObj = productCertificateService.getApplyVoToSave(newMain, details.getDetails());
                            archivesHistoryService.save("ProductCertificate", newMain.getId(), newObj);

                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                        }catch(Exception e){
                            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                        }
                    } else {
                        //保存
                        productCertificateService.update(main, details.getDetails());
                        result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                    }
                }

            }
        }catch (RuntimeException e){
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }

    /**
     * 确认产品档案
     * @param id
     */
    @PostMapping("/confirm")
    public RestResult confirm(String id){
        RestResult result = new RestResult();
        try{
            productCertificateService.confirmProductCertificate(id);
            result.setSuccess(true).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_PRODUCT_SUCCESS));
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_PRODUCT_FAILURE, e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"ProductCertificate:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            productCertificateService.deleteProductCertificate(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    @ModelAttribute("main")
    public ProductCertificate productCertificate(String id){
        if(StringUtils.isNotBlank(id)){
            return productCertificateService.getProductCertificate(id);
        }
        return new ProductCertificate();
    }

    @InitBinder("prop")
    public void initBinderProp(WebDataBinder binder){
        binder.setFieldDefaultPrefix("prop.");
    }

    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"ProductCertificate:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "ProductCertificate");
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
    @RequiresPermissions(value = {"ProductCertificate:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);

            ProductCertificateVo rd = productCertificateService.getApplyVoToDisplay((ProductCertificateVo) BeanMapper.toBean(ProductCertificateVo.class, BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()))));

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/getproductforcertificate")
    public RestResult getProductCertificateByBusinessId(@RequestParam("id") String id) {
        RestResult result = new RestResult();
        try {
            List<ProductCertificateVo> vo =  productCertificateService.findByBusinessId(id);
            result.setSuccess(true).setData(vo).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }
    
    
    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"ProductCertificate:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") ProductCertificate main, @ModelAttribute("details") ProductCertificateUnionsVo detailsVo) {
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){

            	productCertificateService.update(main, detailsVo.getDetails());
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
    @RequiresPermissions("ProductCertificate:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("ProductCertificate" + ":4")){
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

}
