package com.newaim.purchase.archives.flow.purchase.controller;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractVo;
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
@RequestMapping("/purchase/purchasecontract")
public class PurchaseContractController extends ControllerBase {

    @Autowired
    private PurchaseContractService purchaseContractService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,String type,String vendorId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	/*params.put("id-S-LK-OR", keywords);
            	params.put("orderTitle-S-LK-OR", keywords);*/
            	params.put("orderNumber-S-LK-OR", keywords);
/*            	params.put("originPortCnName-S-LK-OR", keywords);
            	params.put("originPortEnName-S-LK-OR", keywords);
            	params.put("destinationPortCnName-S-LK-OR", keywords);
            	params.put("destinationPortEnName-S-LK-OR", keywords);
			    params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);*/
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

            if (StringUtils.isNotBlank(vendorId)){
                params.put("vendorId-S-EQ-ADD", vendorId);
            }

            if (type != null) {
                /*
                    type:1   （清关申请）过滤条件订单质检通过，未做过清关申请  没有加供应商Id过滤

                    type:2   （差额退款调用）过滤条件ASN通过，未做过差额退款  有加供应商Id过滤

                    type:3    (发货计划调用)  合同定金通过并且未做过发货计划正式的合同订单、没有加供应商Id过滤

                    type:4   （费用登记调用）费用支付没有通过的正式合同订单

                    type:5   （订单质检调用）没做过订单质检、发货确认通过的记录


                    type:6   （合同定金调用）没做过合同定金的正式合同订单

                    type:7    正式的采购合同订单
                 */
                if ("1".equals(type)) {
                    params.put("flagOrderQcStatus-N-EQ-ADD", "1");
                    params.put("flagCustomClearanceStatus-N-EQ-ADD", "2");
                    params.put("flagOrderShippingApplyStatus-N-EQ-ADD", "1");
                    //仅显示本组内的订单
                    String depIds = getMyDepartments();
                    params.put("departmentId-S-IN-ADD", depIds);
                }else if ("2".equals(type)) {
                    params.put("flagAsnStatus-N-EQ-ADD", "1");
                    params.put("flagBalanceRefundStatus-N-EQ-ADD", "2");
                }else if ("3".equals(type)) {
                    params.put("flagContractDepositStatus-N-EQ-ADD", "1");
                    params.put("flagOrderShippingPlanStatus-N-EQ-ADD", "2");
                }else if ("4".equals(type)) {
                    params.put("flagFeePaymentStatus-N-EQ-ADD", "2");
                }else if ("5".equals(type)) {
                    params.put("flagOrderShippingApplyStatus-N-EQ-ADD", "1");
                    //未质检且非免检
                    //params.put("flagOrderQcStatus-N-EQ-ADD", "2");
                    params.put("flagOrderQcId-N-NULL-ADD", "null");
                }else if ("6".equals(type)) {
                    params.put("flagContractDepositStatus-N-EQ-ADD", "2");
                    params.put("depositAud-N-GT-ADD", 0);
                }else{

                }
            }
            params.put("status-N-EQ-ADD", Constants.Status.NORMAL.code);
            params.put("hold-N-EQ-ADD", Constants.HoldStatus.UN_HOLD.code);


            Page<PurchaseContractVo> page = purchaseContractService.list(params, pageNumber, pageSize, getSort(sort));
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
            PurchaseContractVo rd =  purchaseContractService.get(id);

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    /**
     * 费用登记中调用的　合并后的清关明细
     * @param id
     * @return
     */
    @PostMapping("/getForFeeRegister")
    public RestResult getForFeeRegister(String id) {
        RestResult result = new RestResult();
        try {
            PurchaseContractVo rd =  purchaseContractService.getForFeeRegister(id);

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/getForMerge")
    public RestResult getForMerge(String id) {
        RestResult result = new RestResult();
        try {
            PurchaseContractVo rd =  purchaseContractService.getForMerge(id);

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

}
