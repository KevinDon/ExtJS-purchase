package com.newaim.purchase.archives.flow.shipping.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.archives.flow.shipping.service.WarehousePlanService;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/shipping/warehousePlan")
public class WarehousePlanController extends ControllerBase {

    @Autowired
    private WarehousePlanService warehousePlanService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
             	params.put("id-S-LK-OR", keywords);
				params.put("originPlace-S-LK-OR", keywords);
				params.put("destinationPlace-S-LK-OR", keywords);
				params.put("assigneeCnName-S-LK-OR", keywords);
				params.put("assigneeEnName-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
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

            Page<WarehousePlanVo> page = warehousePlanService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

//    @PostMapping("/listPackings")
//    public RestResult listPackings(String id) {
//        RestResult result = new RestResult();
//        try {
//            List<CustomClearancePackingVo> rd =  warehousePlanService.getCustomClearancePackingDetail(id);
//            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
//
//        } catch (Exception e) {
//            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
//        }
//
//        return result;
//    }

}
