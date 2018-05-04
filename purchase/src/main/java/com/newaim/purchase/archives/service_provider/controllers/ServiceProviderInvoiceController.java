package com.newaim.purchase.archives.service_provider.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderInvoiceService;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderInvoiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/archives/service_provider_invoice")
public class ServiceProviderInvoiceController extends ControllerBase {

    @Autowired
    private ServiceProviderInvoiceService serviceProviderInvoiceService;

    @PostMapping("/getQuotation")
    public RestResult getQuotation(String orderShippingPlanId,String orderShippingPlanBusinessId ) {
        RestResult result = new RestResult();
        try {
            ServiceProviderInvoiceVo rd = serviceProviderInvoiceService.getByOrderShippingPlanId(orderShippingPlanId,orderShippingPlanBusinessId);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }
}
