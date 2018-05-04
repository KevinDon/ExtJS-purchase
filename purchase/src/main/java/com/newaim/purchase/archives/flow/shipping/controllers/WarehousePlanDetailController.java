package com.newaim.purchase.archives.flow.shipping.controllers;


import com.newaim.core.contoller.ControllerBase;
import com.newaim.purchase.archives.flow.shipping.service.WarehousePlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping/warehouseplandetail")
public class WarehousePlanDetailController extends ControllerBase {

    @Autowired
    private WarehousePlanDetailService warehousePlanDetailService;

}
