package com.newaim.purchase.api.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by Mark on 2017/12/12.
 */
@RestController
@RequestMapping("/wmsapi")
public class WmsApiController extends ControllerBase{

    @Autowired
    private WmsApiService wmsApiService;

    /**
     * 创建
     * @return
     */
    @PostMapping("/inbound/createAsn")
    public RestResult createAsn(CustomClearancePackingVo packingVo) throws IOException {
        return wmsApiService.createAsn(packingVo);
    }

    /**
     * 创建
     * @return
     */
    @PostMapping("/inbound/cancelAsn")
    public RestResult cancelAsn(String asnNumber) throws IOException {
        return wmsApiService.cancelAsn(asnNumber);
    }

    /**
     * 获取实收数量
     * @return
     */
    @PostMapping("/inbound/getASNReceivingResult")
    public RestResult getASNReceivingResult(String asnNumber) throws IOException {
        return wmsApiService.getASNReceivingResult(asnNumber);
    }

}
