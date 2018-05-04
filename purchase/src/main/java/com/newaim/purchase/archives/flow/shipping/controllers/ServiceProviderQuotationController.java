package com.newaim.purchase.archives.flow.shipping.controllers;

import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationService;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationVo;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlanVo;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlansVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipping/serviceinquiry")
public class ServiceProviderQuotationController {

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private ServiceProviderQuotationService serviceProviderQuotationService;

    @PostMapping("/listQuotations")
    public RestResult listQuotations(ServiceProviderQuotationVo params){
        RestResult result = new RestResult();
        try {
            List<ServiceProviderQuotationVo> rd =  serviceProviderQuotationService.findQuotationVosByParams(params);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/plan/listQuotations")
    public RestResult listQuotationsForPlan(SpQuotationForOrderPlansVo queryParams){
        RestResult result = new RestResult();
        try {
            List<SpQuotationForOrderPlanVo> rd =  serviceProviderQuotationService.findQuotationsVoForOrderPlanByParams(queryParams.getQueryParams());
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }
}
