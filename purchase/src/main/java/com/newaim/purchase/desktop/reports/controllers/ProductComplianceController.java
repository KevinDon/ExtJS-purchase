package com.newaim.purchase.desktop.reports.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.reports.entity.Reports;
import com.newaim.purchase.desktop.reports.entity.ReportsCompliance;
import com.newaim.purchase.desktop.reports.service.ReportsComplianceService;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.desktop.reports.vo.ReportsComplianceVo;
import com.newaim.purchase.desktop.reports.vo.ReportsProductsVo;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/reportproductcompliance")
public class ProductComplianceController extends ControllerBase{

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private ReportsComplianceService reportsComplianceService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("ReportProductCompliance:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
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
				params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
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
			
			params = setParams(params, "ReportProductCompliance", ":4:3:2:1", false);

			params.put("moduleName-S-EQ-ADD", "ReportProductCompliance");
			
    		Page<ReportsVo> rd = reportsService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("ReportProductCompliance:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			String[] params = new String[]{"vendor","file","attachment","photo"};
			ReportsVo rd =  reportsService.get(id, params);
			rd.setCompliance(reportsComplianceService.getByReportsId(rd.getId()));
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"ReportProductCompliance:normal:add","ReportProductCompliance:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act,
                           @ModelAttribute("main") Reports main, ReportsProductsVo products,
                           @ModelAttribute("compliance") ReportsCompliance compliance
    ) {
		RestResult result = new RestResult();
		try {

			main.setModuleName("ReportProductCompliance");
			if( main.getId() != null && StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    Reports o = reportsService.saveAs(main, products.getProducts());
                    reportsComplianceService.add(o.getId(), compliance);

				}else{
                    UserVo user = SessionUtils.currentUserVo();
                    if(main.getCreatorId() != null && !main.getCreatorId().equals(user.getId())){
                        throw new Exception(localeMessageSource.getMsgUnauthorized());
                    }

					Reports o = reportsService.save(main, products.getProducts());
                    reportsComplianceService.save(o.getId(), compliance);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				
			}else{

                Reports o = reportsService.add(main, products.getProducts());
                reportsComplianceService.add(o.getId(), compliance);

				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
				
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("ReportProductCompliance:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			ReportsVo o = reportsService.get(ids);
			if(o.getStatus()>0){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if(hasDataType("ReportProductCompliance" + ":4")){
				//物理删除
                reportsComplianceService.deleteAllByReportsId(ids);
				reportsService.delete(ids);
			}else{
				//删除标记
				reportsService.setDelete(ids);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public Reports main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return reportsService.getReports(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Reports();
        }
        return null;
    }

    @InitBinder("compliance")
    protected void initBinderCompliance(WebDataBinder binder){
        binder.setFieldDefaultPrefix("compliance.");
    }

}
