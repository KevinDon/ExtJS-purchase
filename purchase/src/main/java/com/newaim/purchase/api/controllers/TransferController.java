package com.newaim.purchase.api.controllers;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.api.service.FxratesService;
import com.newaim.purchase.api.service.TransferService;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingDetailService;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private FxratesService fxratesService;

    @Autowired
    private WmsApiService wmsApiService;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;

    @Autowired
    private ProductService productService;

    /**
     * OMS接口
     * @param method
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping
    public String transfer(String method, HttpServletRequest request) throws IOException {
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        Enumeration<String>  parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            params.put(name, request.getParameter(name));
        }
        return transferService.transfer(method, params);
    }

    /**
     * OMS接口
     * @param method
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/omsCustom")
    public String transferOmsCustom(String method, HttpServletRequest request) throws IOException {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String>  parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            params.put(name, request.getParameter(name));
        }
        return transferService.transferOmsCustom(method, params);
    }

    /**
     * OMS接口
     * @param method
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/omsCustomRest")
    public RestResult transferOmsCustomForRestResult(String method, HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String>  parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            params.put(name, request.getParameter(name));
        }
        return transferService.transferOmsCustomForRestResult(method, params);
    }

    /**
     * 获取汇率接口
     * @param method
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/fxrates")
    public String getFxrates(String method, HttpServletRequest request) throws IOException {

        return fxratesService.transfer();

    }

    /**
     * WMS接口
     * @param method
     * @param containerNumbers
     * @return
     * @throws IOException
     */
    @PostMapping("/wms")
    public String wmsTransfer(String method, @RequestParam("containerNumbers") List<String> containerNumbers) throws IOException {
        List<CustomClearancePackingDetail> packingDetails = customClearancePackingDetailService.findPackingDetailsByPackingNumbers(containerNumbers);

        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        if(packingDetails != null){
            List<LinkedHashMap<String, Object>> skuList = Lists.newArrayList();
            for (int i = 0; i < packingDetails.size(); i++) {
                CustomClearancePackingDetail detail = packingDetails.get(i);
                Product product = productService.getProduct(detail.getProductId());
                LinkedHashMap<String, Object> sku = Maps.newLinkedHashMap();
                sku.put("sku", product.getSku());
                sku.put("volume", product.getCbm());
                sku.put("weight", product.getCubicWeight());
                sku.put("length", product.getLength());
                sku.put("width", product.getWidth());
                sku.put("height", product.getHeight());
                skuList.add(sku);
            }
            params.put("sku_list", skuList);
        }
        return transferService.transferWarehouse(method, params);

    }

    /**
     * 同步Barcode
     */
    @PostMapping("/syncBarcode")
    public String syncBarcode(String skus) throws IOException {
        return wmsApiService.syncBarcode(skus);
    }
}
