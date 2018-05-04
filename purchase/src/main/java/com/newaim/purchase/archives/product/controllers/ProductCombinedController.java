package com.newaim.purchase.archives.product.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import com.newaim.purchase.archives.product.service.ProductCombinedService;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionsVo;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.product.vo.ProductCombinedDetailsVo;
import com.newaim.purchase.archives.product.vo.ProductCombinedVo;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/9/25.
 */
@RestController
@RequestMapping("/archives/productCombined")
public class ProductCombinedController extends ControllerBase {

    private final static String MSG_CONFIRM_PRODUCT_SUCCESS = "msg_confirm_productCombined_success";
    private final static String MSG_CONFIRM_PRODUCT_FAILURE = "msg_confirm_productCombined_failure";

    @Autowired
    private ProductCombinedService productCombinedService;
    @Autowired
    private ArchivesHistoryService archivesHistoryService;

    @RequiresPermissions("ProductCombination:normal:list")
    @PostMapping("/list")
    public RestResult listProduct(ServletRequest request, String sort,String keywords,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("combinedSku-S-LK-OR", keywords);
                params.put("combinedName-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
                params.put("ean-S-LK-OR", keywords);
                params.put("barcode-S-LK-OR", keywords);
                params.put("id-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
            params = setParams(params, "ProductCombination", ":4:3:2:1", false);
            Page<ProductCombinedVo> page = productCombinedService.listProductCombined(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"ProductCombination:normal:list"}, logical = Logical.OR)

    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            ProductCombinedVo rd =  productCombinedService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }


    @PostMapping("/save")
    @RequiresPermissions(value = {"ProductCombination:normal:add", "ProductCombination:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") ProductCombined main, @ModelAttribute("details") ProductCombinedDetailsVo detailsVo) {
        RestResult result = new RestResult();
        try {
        	
        	 if (ACT_ADD.equals(act)) {
        		 productCombinedService.add(main, detailsVo.getDetails());
        		 result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));

             }else {
            	 //复制另存
                 if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                	 productCombinedService.saveAs(main, detailsVo.getDetails());
                 }else{
                     if (main.getStatus() > -1) {
                         //储审核数据
                         try {
                        	 ProductCombined newMain = BeanMapper.map(main, ProductCombined.class);
                             archivesHistoryService.clean();
                             ProductCombinedVo newObj = productCombinedService.getApplyVoToSave(newMain, detailsVo.getDetails());
                             archivesHistoryService.save("ProductCombination", newMain.getId(), newObj);

                             result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                         }catch(Exception e){
                             result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                         }
                     } else {
                         //保存
                    	 productCombinedService.update(main, detailsVo.getDetails());
                         result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                     }
                	 
                 }
            	 
            	 
             }
       
        } catch (Exception e) {
            result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @ModelAttribute("main")
    public ProductCombined main(String id){
        if(StringUtils.isNotBlank(id)){
            return productCombinedService.getProductCombined(id);
        }else{
            return new ProductCombined();
        }
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"ProductCombination:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            productCombinedService.deleteProductCombined(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }
    
    
    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"ProductCombination:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "ProductCombination");
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
    @RequiresPermissions(value = {"ProductCombination:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);

            ProductCombinedVo rd = productCombinedService.getApplyVoToDisplay((ProductCombinedVo) BeanMapper.toBean(ProductCombinedVo.class, BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()))));

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"ProductCombination:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") ProductCombined main, @ModelAttribute("details") ProductCombinedDetailsVo detailsVo) {
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){
            	productCombinedService.update(main, detailsVo.getDetails());
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
    @RequiresPermissions("ProductCombination:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("ProductCombination" + ":4")){
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
