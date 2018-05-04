package com.newaim.purchase.archives.flow.purchase.controller;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.archives.flow.purchase.service.PurchasePlanDetailService;
import com.newaim.purchase.archives.flow.purchase.service.PurchasePlanDetailViewService;
import com.newaim.purchase.archives.flow.purchase.vo.PurchasePlanDetailViewVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchasePlanDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archives/flow/purchase/plandetail")
public class PurchasePlanDetailController extends ControllerBase {

    @Autowired
    private PurchasePlanDetailService purchasePlanDetailService;

    @Autowired
    private PurchasePlanDetailViewService purchasePlanDetailViewService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords, Integer isview,String vendorId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ){
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("id-S-LK-OR", keywords);
                params.put("sku-S-LK-OR", keywords);
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
            if (null!= vendorId && !"".equals(vendorId)){
                params.put("vendorId-S-EQ-ADD", vendorId);
            }

            if(isview != null &&isview ==1){
                Page<PurchasePlanDetailViewVo> page = purchasePlanDetailViewService.list(params, pageNumber, pageSize, getSort(sort));
                result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
            }else {
                Page<PurchasePlanDetailVo> page = purchasePlanDetailService.list(params, pageNumber, pageSize, getSort(sort));
                result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
            }

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listForImport")
    public RestResult listForImport(String vendorId){
        RestResult result = new RestResult();
        try {
            List<PurchasePlanDetailViewVo> data = purchasePlanDetailViewService.listAllByVendor(vendorId);
            result.setSuccess(true).setData(data).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            PurchasePlanDetailVo rd =  purchasePlanDetailService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }


}
