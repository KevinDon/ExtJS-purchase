package com.newaim.purchase.archives.flow.purchase.controller;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.flow.purchase.entity.ProductQuotation;
import com.newaim.purchase.archives.flow.purchase.service.ProductQuotationService;
import com.newaim.purchase.archives.flow.purchase.vo.ProductQuotationVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archives/flow/productquotation")
public class ProductQuotationController extends ControllerBase{

    @Autowired
    private ProductQuotationService productQuotationService;
	
//	@RequiresPermissions("FlowProductQuotation:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String modal,String sort, String keywords,String vendorId, String type,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
		try {

            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("id-S-LK-OR", keywords);
				params.put("sku-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
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
			
			params = setParams(params, "FlowProductQuotation", ":4:3:2:1", false);
			
            //列表数据过滤
            if(null != modal && StringUtils.isNotBlank(modal)){
                UserVo user = SessionUtils.currentUserVo();

                if("mine".equals(modal)){
                    //我发启的
                    params.remove("creatorId-S-EQ-ADD");
                    params.put("creatorId-S-EQ-ADD", user.getId());
                }else if("involved".equals(modal)){
                    //我参与的
//					params.put("history.operatorId-S-EQ-ADD", user.getId());
//                  params.put("history.businessId-S-GBY-NON", "");
                }
            }

			if (StringUtils.isNotBlank(vendorId)){
				params.put("vendorId-S-EQ-ADD", vendorId);
			}
			params.put("productStatus-N-EQ-ADD", Constants.Status.NORMAL.code);
			params.put("status-N-EQ-ADD", Constants.Status.NORMAL.code);

			//有效期内的询价
			params.put("effectiveDate-D-LTE-ADD", DateFormatUtil.formatTimeWithTimeZone(new Date()));
			params.put("validUntil-D-GTE-ADD", DateFormatUtil.formatTimeWithTimeZone(new Date()));

			Page<ProductQuotationVo> page = productQuotationService.list(params, pageNumber, pageSize, getSort(sort));
			result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
        return result;
    }

	@PostMapping("/listForImport")
	public RestResult listForImport(String vendorId, String type){
		RestResult result = new RestResult();
		try {
			List<ProductQuotationVo> data = productQuotationService.listAllByVendor(vendorId, type);
			result.setSuccess(true).setData(data).setMsg(localeMessageSource.getMsgListSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		return result;
	}
    
//	@RequiresPermissions("FlowProductQuotation:normal:list")
    @PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ProductQuotationVo rd =  productQuotationService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}


    
    @ModelAttribute("main")
    public ProductQuotation main(String id){
        if(StringUtils.isNotBlank(id)){
            return productQuotationService.getProductQuotation(id);
        }else{
        	return new ProductQuotation();
        }
    }








}
