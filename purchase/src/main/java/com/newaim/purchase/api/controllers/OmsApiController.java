package com.newaim.purchase.api.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.api.dto.OmsApiDto;
import com.newaim.purchase.api.dto.OmsOrderDetailDto;
import com.newaim.purchase.api.dto.OmsOrderDto;
import com.newaim.purchase.api.service.OmsApiService;
import com.newaim.purchase.api.service.TransferService;
import com.newaim.purchase.archives.product.service.ProductService;
import com.sun.star.loader.Java;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mark on 2017/12/12.
 */
@RestController
@RequestMapping("/omsapi")
public class OmsApiController extends ControllerBase{

    @Autowired
    private TransferService transferService;

    @Autowired
    private OmsApiService omsApiService;

    @Autowired
    private ProductService productService;

    /**
     * 获取OMS产品分类
     * @return
     */
    @PostMapping("/category/getCateTree")
    public RestResult listOmsCategory(){
        String method = "category/getCateTree";
        Map<String, String> params = Maps.newHashMap();
        return transferService.transferOmsCustomForRestResult(method, params);
    }

    @PostMapping("/order/searchOrders")
    public RestResult searchOrders(String keywords, String orderNo, String platTid, String tid, String userNick,
                                   String email, String startingTime, String endTime, String shopId){
        String method = "order/searchOrders";
        LinkedHashMap<String, String> params = Maps.newLinkedHashMap();
        params.put("keywords", keywords);
        params.put("order_no", orderNo);
        params.put("plat_tid", platTid);
        params.put("tid", tid);
        params.put("user_nick", userNick);
        params.put("email", email);
        params.put("starting_time", startingTime);
        params.put("end_time", endTime);
        params.put("shop_id", shopId);
        RestResult restResult = transferService.transferOmsCustomForRestResult(method, params, OmsOrderDto.class);
        return restResult;
    }

    /**
     * 添加产品
     * @param productId
     * @return
     * @throws Exception
     */
    @PostMapping("/oms/goodsAdd")
    public RestResult goodsAdd(String productId) throws Exception {

        return omsApiService.goodsAdd(productId);
    }

    /**
     * 添加组合产品
     * @param packageId
     * @return
     * @throws Exception
     */
    @PostMapping("/oms/packageAdd")
    public RestResult packageAdd(String packageId) throws Exception {

        return omsApiService.packageAdd(packageId);
    }

    /**
     * 组合产品修改
     * @param packageId
     * @return
     */
    @PostMapping("/order/packageUpdate")
    public RestResult packageUpdate(String packageId) throws Exception {

        return omsApiService.packageUpdate(packageId);

    }

    /**
     * 获取所有店铺
     * @return
     */
    @PostMapping("/base/getAllShop")
    public RestResult getAllShop(){
        String method = "base/getAllShop";
        Map<String, String> params = Maps.newHashMap();
        return transferService.transferOmsCustomForRestResult(method, params);
    }

    public static void main(String[] args) {
        StringBuilder json = new StringBuilder();
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType j3 = jsonMapper.buildCollectionType(ArrayList.class, OmsOrderDto.class);
        JavaType javaType2 = jsonMapper.constructParametricType(OmsApiDto.class, j3);
        OmsApiDto omsApiDto = jsonMapper.fromJson(json.toString(), javaType2);
        System.out.println(omsApiDto);
    }
}
