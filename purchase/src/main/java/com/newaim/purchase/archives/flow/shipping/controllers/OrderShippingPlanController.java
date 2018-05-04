package com.newaim.purchase.archives.flow.shipping.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.service.OrderShippingPlanService;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanVo;
import com.newaim.purchase.flow.purchase.vo.FlowSampleVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shipping/orderShippingPlan")
public class OrderShippingPlanController extends ControllerBase {

    @Autowired
    private OrderShippingPlanService orderShippingPlanService;
    @Autowired
    private PurchaseContractService purchaseContractService;

   // @RequiresPermissions("OrderShippingPlan:normal:list")
   @PostMapping("/list")
   public RestResult list(ServletRequest request, String sort, String keywords,String type,
                          @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
   ) {
       RestResult result = new RestResult();
       try {
           LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
           if(StringUtils.isNotBlank(keywords)){
               params.put("id-S-LK-OR", keywords);
               params.put("businessId-S-LK-OR", keywords);
               params.put("orderNumbers-S-LK-OR", keywords);
               params.put("creatorCnName-S-LK-OR", keywords);
               params.put("creatorEnName-S-LK-OR", keywords);
               params.put("handlerDepartmentCnName-S-LK-OR", keywords);
               params.put("handlerDepartmentEnName-S-LK-OR", keywords);
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
           if(hasDataType("OrderShippingPlan:4")){
               //带禁用的数据

           }else{
               //非禁用的数据
               //params.put("status-N-EQ-ADD", "1");
           }

           if(hasDataType("OrderShippingPlan:3")){
               //不分部门
//				params.put("status-N-EQ-ADD", "1");
           }else if(hasDataType("User:2")){
               //部门内
               //params.put("status-N-EQ-ADD", "1");
           }else if(hasDataType("OrderShippingPlan:1")){
               //自身
               //params.put("status-N-EQ-ADD", "1");
           }

           if (type!=null){
               if ("1".equals(type)){
                   params.put("flagOrderShippingApplyStatus-S-EQ-ADD", "2");
               }else if("2".equals(type)){
                   //成本计算中选择器
                   params.put("flagCostStatus-N-NEQ-ADD", 1);
                   params.put("flagCustomClearanceStatus-N-EQ-ADD", 1);
               }
           }
           params.put("status-N-EQ-ADD", Constants.Status.NORMAL.code);
           params.put("hold-N-EQ-ADD", Constants.HoldStatus.UN_HOLD.code);
           // params = setParams(params, "OrderShippingPlan", ":4:3:2:1", false);
           Page<OrderShippingPlanVo> page = orderShippingPlanService.list(params, pageNumber, pageSize, getSort(sort));
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
            OrderShippingPlanVo vo =  orderShippingPlanService.get(id);
            result.setSuccess(true).setData(vo).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }


    @PostMapping("/getdetails")
    public RestResult getDetails(String shippingPlanId,String type) {
        RestResult result = new RestResult();
        try {
        	List<OrderShippingPlanDetailVo> vo  = Lists.newArrayList();
            if ("1".equals(type)){
                //发货计划确认选择器
                vo = orderShippingPlanService.findDetailsByOrderShippingPlanId(shippingPlanId);
            }else if("2".equals(type)){
                //成本计算中选择器
                vo = orderShippingPlanService.getDetail(shippingPlanId);
            }
        	result.setSuccess(true).setData(vo).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }
}
