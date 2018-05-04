package com.newaim.purchase.archives.product.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.ProductVo;
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
import java.math.BigDecimal;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archives/newproduct")
public class NewProductController extends ControllerBase {

    private final static String MSG_CONFIRM_PRODUCT_SUCCESS = "msg_confirm_product_success";
    private final static String MSG_CONFIRM_PRODUCT_FAILURE = "msg_confirm_product_failure";

    @Autowired
    private ProductService productService;



    @RequiresPermissions("NewProduct:normal:list")
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
                params.put("sku-S-LK-OR", keywords);
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
            if (null!= vendorId && !"".equals(vendorId)){
                params.put("prop.vendorId-S-EQ-ADD", vendorId);
                params.put("status-N-EQ-ADD", 1);
            }
            params.put("newProduct-N-EQ-ADD", Product.NEW_PRODUCT.toString());
            params = setParams(params, "NewProduct", ":4:3:2:1", false);
            Page<ProductVo> page = productService.listProduct(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"NewProduct:normal:list"}, logical = Logical.OR)
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

    @PostMapping("/save")
    @RequiresPermissions(value = {"NewProduct:normal:add", "NewProduct:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") Product product, String srcSku){
        RestResult result = new RestResult();
        try {
            product.setNewProduct(Product.NEW_PRODUCT);
            if(ACT_ADD.equals(act)){
                if(product.getStatus() == null){
                    //草稿
                    product.setStatus(Constants.Status.DRAFT.code);
                }
                product.setNewProduct(1);
                productService.saveProduct(product);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else if(StringUtils.isNotBlank(product.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && act.equals(ACT_COPY)){
                    product.setId(null);
                    ProductVendorProp prop = product.getProp();
                    prop.setId(null);
                    prop.setProduct(product);
                    //清除报价
                    prop.setQuotationPriceAud( new BigDecimal(0));
                    prop.setQuotationPriceRmb(new BigDecimal(0));
                    prop.setQuotationPriceUsd(new BigDecimal(0));
                    prop.setQuotationRateAudToRmb(new BigDecimal(0));
                    prop.setQuotationRateAudToUsd(new BigDecimal(0));
                    if (prop.getTargetBinAud() == null) {
                        prop.setTargetBinAud(BigDecimal.valueOf(0));
                        prop.setTargetBinRmb(BigDecimal.valueOf(0));
                        prop.setTargetBinUsd(BigDecimal.valueOf(0));

                    }
                    //复制时清空风控评级
                    prop.setRiskRating(null);
                    //清空安检、开发，质检标记
                    prop.setFlagComplianceStatus(2);
                    prop.setFlagComplianceId(null);
                    prop.setFlagComplianceTime(null);
                    prop.setFlagDevStatus(2);
                    prop.setFlagDevId(null);
                    prop.setFlagDevTime(null);
                    prop.setFlagQcStatus(2);
                    prop.setFlagQcId(null);
                    prop.setFlagQcTime(null);
                    product.setProp(prop);
                    //草稿
                    if(product.getStatus() == null){
                        //草稿
                        product.setStatus(Constants.Status.DRAFT.code);
                    }
                    product.setNewProduct(1);
                    productService.add(product);
                }else{
                    productService.saveProduct(product);
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(act, e.getMessage()));
        }
        return result;
    }

    @PostMapping("/convertToProduct")
    @RequiresPermissions(value = {"NewProduct:normal:add", "NewProduct:normal:edit"}, logical = Logical.OR)
    public RestResult convertToProduct(String id, String sku){
        RestResult result = new RestResult();
        try {
            Product product = productService.getProduct(id);
            if(!Constants.RiskRating.isPass(product.getProp().getRiskRating())){
                result.setSuccess(false).setMsg(localeMessageSource.getMsgRiskRatingNotPass());
                return result;
            }
            if(!StringUtils.equals(product.getSku(), sku)){
                boolean existsSku = productService.existsSku(sku,0);
                if(existsSku){
                    result.setSuccess(false).setMsg(localeMessageSource.getMsgProductExistsSku(sku));
                    return result;
                }
            }
            productService.convertToProduct(product, sku);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
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
    @RequiresPermissions(value = {"NewProduct:normal:del"}, logical = Logical.OR)
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
