package com.newaim.purchase.archives.vendor.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.archives.vendor.service.VendorProductCategoryUnionService;
import com.newaim.purchase.archives.vendor.vo.VendorProductCategoryUnionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/archives/vendor-product-category")
public class VendorProductCategoryUnionController extends ControllerBase {

	@Autowired
    private VendorProductCategoryUnionService vendorProductCategoryUnionService;


	@PostMapping("/listForDialog")
	public RestResult listForDialog(String vendorId) {
		RestResult result = new RestResult();
		try {

			List<VendorProductCategoryUnionVo> rd =  vendorProductCategoryUnionService.listByVendorId(vendorId);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {

			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
	}

	
}
