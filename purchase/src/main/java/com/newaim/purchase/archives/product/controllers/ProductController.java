package com.newaim.purchase.archives.product.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.product.vo.ProductVo;

/**
 * Created by Mark on 2017/9/18.
 */
@RestController
@RequestMapping("/archives/product")
public class ProductController extends ControllerBase {

    private final static String MSG_CONFIRM_PRODUCT_SUCCESS = "msg_confirm_product_success";
    private final static String MSG_CONFIRM_PRODUCT_FAILURE = "msg_confirm_product_failure";

    @Autowired
    private ProductService productService;

    @Autowired
    private ArchivesHistoryService archivesHistoryService;
    
    @RequiresPermissions("Product:normal:list")
    @PostMapping("/list")
    public RestResult listProduct(ServletRequest request, String sort, String keywords,String vendorId,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("sku-S-LK-OR", keywords);
                params.put("name-S-LK-OR", keywords);
                params.put("packageName-S-LK-OR", keywords);
                params.put("barcode-S-LK-OR", keywords);
                params.put("ean-S-LK-OR", keywords);
                params.put("color-S-LK-OR", keywords);
                params.put("model-S-LK-OR", keywords);
                params.put("style-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
                params.put("prop.vendorCnName-S-LK-OR", keywords);
                params.put("prop.vendorEnName-S-LK-OR", keywords);
                params.put("id-S-LK-OR", keywords);
                params.put("prop.vendorId-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            	
            }
            if ( null!= vendorId && !"".equals(vendorId)){
                params.put("prop.vendorId-S-EQ-ADD", vendorId);
                params.put("status-N-EQ-ADD", 1);
            }

            params = setParams(params, "Product", ":4:3:2:1", false);
            Page<ProductVo> page = productService.listProduct(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listProduct")
    public RestResult list(ServletRequest request, String sort, String keywords,String vendorId,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("sku-S-LK-OR", keywords);
                params.put("name-S-LK-OR", keywords);
                params.put("packageName-S-LK-OR", keywords);
                params.put("barcode-S-LK-OR", keywords);
                params.put("ean-S-LK-OR", keywords);
                params.put("color-S-LK-OR", keywords);
                params.put("model-S-LK-OR", keywords);
                params.put("style-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
                params.put("prop.vendorCnName-S-LK-OR", keywords);
                params.put("prop.vendorEnName-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);

            }
            if (null!= vendorId && !"".equals(vendorId)){
                params.put("prop.vendorId-S-EQ-ADD", vendorId);
                params.put("status-S-EQ-ADD", 1);
            }
            params.put("newProduct-S-EQ", Product.NORMAL_PRODUCT.toString());
            params = setParams(params, "Product", ":4:3:2:1", false);
            Page<ProductVo> page = productService.listProduct(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/existsSku")
    public RestResult existsSku(String sku, String id){
        RestResult result = new RestResult();
        try{
            boolean existsSku = false;
            if(StringUtils.isBlank(id)){
                existsSku = productService.existsSku(sku,0);
            }else{
                Product product = productService.getProduct(id);
                if(product.getStatus()==3){
                	existsSku = productService.existsSku(sku,0);
                }else{
                	if(StringUtils.equals(sku, product.getSku())){
                		existsSku = productService.existsSku(sku,1);
                	}else{
                		existsSku = productService.existsSku(sku,0);
                	}
                }
            }
            if(!existsSku){
                result.setMsg(localeMessageSource.getMsgOperateSuccess()).setData(true);
            }else{
                result.setMsg(localeMessageSource.getMsgProductExistsSku(sku)).setData(false);
            }
            result.setSuccess(true);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @PostMapping("/existsBarcode")
    public RestResult existsBarcode(String barcode, String id){
        RestResult result = new RestResult();
        try{
            boolean existsBarcode = false;
            if(StringUtils.isBlank(id)){
                existsBarcode = productService.existsBarcode(barcode);
            }else{
                Product product = productService.getProduct(id);
                if(!StringUtils.equals(barcode, product.getBarcode())){
                    existsBarcode = productService.existsBarcode(barcode);
                }
            }
            result.setSuccess(true);
            if(!existsBarcode){
                result.setMsg(localeMessageSource.getMsgOperateSuccess()).setData(true);
            }else{
                result.setMsg(localeMessageSource.getMsgProductExistsBarcode()).setData(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @PostMapping("/existsEan")
    public RestResult existsEan(String ean, String id){
        RestResult result = new RestResult();
        try{
            boolean existsEan = false;
            if(StringUtils.isBlank(id)){
                existsEan = productService.existsEan(ean);
            }else{
                Product product = productService.getProduct(id);
                if(!StringUtils.equals(ean, product.getEan())){
                    existsEan = productService.existsEan(ean);
                }
            }
            result.setSuccess(true);
            if(!existsEan){
                result.setMsg(localeMessageSource.getMsgOperateSuccess()).setData(true);
            }else{
                result.setMsg(localeMessageSource.getMsgProductExistsEan(ean)).setData(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }


    @PostMapping("/get")
    @RequiresPermissions(value = {"Product:normal:list"}, logical = Logical.OR)
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {

            ProductVo rd =  productService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/getproductforcertificate")
    public RestResult getProductCertificateByProductId(@RequestParam("productIds") String[] productIds) {
        RestResult result = new RestResult();
        try {
            List<ProductCertificateVo> vo =  productService.getProductCertificateByProductId(Lists.newArrayList(productIds));
            result.setSuccess(true).setData(vo).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }


    @PostMapping("/save")
    @RequiresPermissions(value = {"Product:normal:add", "Product:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") Product product, String srcSku){
        RestResult result = new RestResult();
        try {
            product.setNewProduct(Product.NORMAL_PRODUCT);
            if(ACT_ADD.equals(act)){
                productService.saveProduct(product);
                //草稿
                product.setStatus(Constants.Status.DRAFT.code);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else if(StringUtils.isNotBlank(product.getId())){
            	   //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                	 product.setId(null);
                     ProductVendorProp prop = product.getProp();
                     prop.setId(null);
                     prop.setProduct(product);
                     product.setProp(prop);
                     //草稿
                     product.setStatus(Constants.Status.DRAFT.code);
                     productService.add(product);
                } else {
                    if (product.getStatus() > -1) {
                        //储审核数据
                        try {
                        	Product newMain = BeanMapper.map(product, Product.class);
                            ProductVendorProp prop = BeanMapper.map(product.getProp(), ProductVendorProp.class);
                            newMain.setProp(prop);
							archivesHistoryService.clean();
							ProductVo newObj = productService.getApplyVoToSave(newMain);
                            archivesHistoryService.save("Product", newMain.getId(), newObj);

                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                        }catch(Exception e){
                            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                        }

                    } else {
                        //保存
                        product.setIsSync(Constants.ProductSyncFlag.UPDATE.code);
                    	 productService.saveProduct(product);
                        result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                    }
                }
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(act, e.getMessage()));
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
            productService.confirmProduct(id);
            result.setSuccess(true).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_PRODUCT_SUCCESS));
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_PRODUCT_FAILURE, e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"Product:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            productService.deleteProduct(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"Product:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "Product");
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
    @RequiresPermissions(value = {"Product:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);
            Map<String, ?> o = BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()));

            ProductVo rd = productService.getApplyVoToDisplay((ProductVo) BeanMapper.toBean(ProductVo.class, o), o);

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"Product:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") Product product) {
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){
                product.setIsSync(Constants.ProductSyncFlag.UPDATE.code);
                productService.save(product);
                archivesHistoryService.confirm(ahid);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgArchivesConfirmSuccess());
            }else{
                result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure());
            }
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure() + ": " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/historydelete")
    @RequiresPermissions("Product:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("Product" + ":4")){
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
    public Product product(String id){
        if(StringUtils.isNotBlank(id)){
            return productService.getProduct(id);
        }
        return new Product();
    }

    @InitBinder("prop")
    public void initBinderProp(WebDataBinder binder){
        binder.setFieldDefaultPrefix("prop.");
    }
}
