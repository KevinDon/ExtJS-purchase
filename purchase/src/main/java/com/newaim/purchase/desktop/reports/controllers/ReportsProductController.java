package com.newaim.purchase.desktop.reports.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.service.ReportsProductService;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.desktop.reports.vo.ReportsProductVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/reportsproduct")
public class ReportsProductController extends ControllerBase{

    @Autowired
    private ReportsProductService reportProductService;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, Integer type, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("title-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("confirmedCnName-S-LK-OR", keywords);
				params.put("confirmedEnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
				params.put("serialNumber-S-LK-OR", keywords);
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
			
			params = setParams(params, "ReportProductAnalysis", ":4:3:2:1", false);
		
			params.put("moduleName-S-EQ-ADD", "ReportProductAnalysis");

    		Page<ReportsProductVo> rd = reportProductService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			String[] params = new String[]{"vendor","file","attachment","photo"};
			ReportsProductVo rd =  reportProductService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

    @ModelAttribute("main")
	public ReportsProduct main(String act, String id){
		if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
			return reportProductService.getReportsProduct(id);
		}else if(StringUtils.isNotBlank(act)){
			return new ReportsProduct();
		}
		return null;
	}
}
