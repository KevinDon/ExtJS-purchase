package com.newaim.purchase.desktop.sta.controllers;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.desktop.sta.entity.StaCost;
import com.newaim.purchase.desktop.sta.entity.ViewStaCost;
import com.newaim.purchase.desktop.sta.service.StaCostService;
import com.newaim.purchase.desktop.sta.service.StaOrderService;
import com.newaim.purchase.desktop.sta.vo.StaCostVo;
import com.newaim.purchase.desktop.sta.vo.StaOrderVo;
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
@RequestMapping("/desktop/stacost")
public class StaCostController extends ControllerBase {

    @Autowired
    private StaCostService staCostService;

//    @RequiresPermissions("StaOrder:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ){
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("id-S-LK-OR", keywords);
                params.put("orderNumber-S-LK-OR", keywords);
                params.put("orderTitle-S-LK-OR", keywords);
                params.put("sellerCnName-S-LK-OR", keywords);
                params.put("sellerEnName-S-LK-OR", keywords);
                params.put("sellerCnAddress-S-LK-OR", keywords);
                params.put("sellerEnAddress-S-LK-OR", keywords);
                params.put("sellerContactCnName-LK-OR", keywords);
                params.put("sellerContactEnName-S-LK-OR", keywords);
                params.put("sellerEmail-S-LK-OR", keywords);
                params.put("buyerCnName-S-LK-OR", keywords);
                params.put("buyerEnName-S-LK-OR", keywords);
                params.put("buyerCnAddress-S-LK-OR", keywords);
                params.put("buyerEnAddress-S-LK-OR", keywords);
                params.put("buyerContactCnName-LK-OR", keywords);
                params.put("buyerContactEnName-S-LK-OR", keywords);
                params.put("buyerEmail-S-LK-OR", keywords);
                params.put("originPortCnName-S-LK-OR", keywords);
                params.put("originPortEnName-S-LK-OR", keywords);
                params.put("destinationPortCnName-S-LK-OR", keywords);
                params.put("destinationPortEnName-S-LK-OR", keywords);
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


            if(hasDataType("StaOrder:4")){
                //带禁用的数据

            }else{
                //非禁用的数据
                //params.put("status-N-EQ-ADD", "1");
            }

            if(hasDataType("StaOrder:3")){
                //不分部门
//				params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("User:2")){
                //部门内
                //params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("StaOrder:1")){
                //自身
                //params.put("status-N-EQ-ADD", "1");
            }
//            params = setParams(params, "StaOrder", ":4:3:2:1", false);
            Page<StaCostVo> page = staCostService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            StaCostVo rd =  staCostService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }

    /**
     *同步视图
     * @return
     */
    @PostMapping("/syncView")
    public RestResult syncView(){
        RestResult result = new RestResult();
        try {
            List<StaCost> staCosts =  staCostService.copyFromView();
            result.setSuccess(true).setData(staCosts).setMsg(localeMessageSource.getMsgUpdateSuccess());
        }catch (Exception e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgUpdateFailure(e.getMessage()));
        }
        return result;
    }


}
