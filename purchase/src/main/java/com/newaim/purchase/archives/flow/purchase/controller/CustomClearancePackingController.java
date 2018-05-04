package com.newaim.purchase.archives.flow.purchase.controller;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingService;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
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

/**
 * 装柜单选择器
 */
@RestController
@RequestMapping("/purchase/customClearancePacking")
public class CustomClearancePackingController extends ControllerBase {

    @Autowired
    private CustomClearancePackingService customClearancePackingService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,String type,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("containerNumber-S-LK-OR", keywords);
                params.put("orderNumber-S-LK-OR", keywords);
            	/*params.put("sealsNumber-S-LK-OR", keywords);
                 params.put("flowOrderShippingPlanId-S-LK-OR", keywords);*/
              
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

            if (type!=null){
                if (type.equals("1")){
                    params.put("flagWarehousePlanStatus-S-EQ-ADD", "2");
                }
            }

            params.put("hold-N-EQ-ADD", Constants.HoldStatus.UN_HOLD.code);

            Page<CustomClearancePackingVo> page = customClearancePackingService.listPacking(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }


    @PostMapping("/listForAsn")
    public RestResult listForAsn(ServletRequest request, String sort, String keywords,String type,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("asnNumber-S-LK-OR", keywords);
                params.put("warehouseId-S-LK-OR", keywords);
                params.put("orderNumber-S-LK-OR", keywords);
                params.put("receiveLocation-S-LK-OR", keywords);
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
            if(hasDataType("CustomClearancePacking:4")){
                //带禁用的数据

            }else{
                //非禁用的数据
                //params.put("status-N-EQ-ADD", "1");
            }

            if(hasDataType("CustomClearancePacking:3")){
                //不分部门
//				params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("User:2")){
                //部门内
                //params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("CustomClearancePacking:1")){
                //自身
                //params.put("status-N-EQ-ADD", "1");
            }

            if (type!=null){
                if (type.equals("1")){
                    params.put("flagAsnStatus-S-EQ-ADD", "2");
                }
            }

            params.put("hold-N-EQ-ADD", Constants.HoldStatus.UN_HOLD.code);
            //grady
            params.put("flagAsnStatus-N-NEQ-ADD", 1);
            params.put("flagCostStatus-N-EQ-ADD", 1);

            Page<CustomClearancePackingVo> page = customClearancePackingService.listPackingForAsn(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }



}
