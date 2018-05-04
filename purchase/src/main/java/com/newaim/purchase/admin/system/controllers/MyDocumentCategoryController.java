package com.newaim.purchase.admin.system.controllers;

import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.MyDocumentCategoryDescService;
import com.newaim.purchase.admin.system.service.MyDocumentCategoryService;
import com.newaim.purchase.admin.system.vo.MyDocumentCategoryVo;

@RestController
@RequestMapping("/mydoccate")
public class MyDocumentCategoryController extends ControllerBase{

	@Autowired
    private MyDocumentCategoryService myDocumentCategoryService;
	
	@Autowired
    private MyDocumentCategoryDescService myDocumentCategoryDescService;

	@RequiresPermissions("MyDocumentCategory:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("path-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}
			params = setParams(params, "MyDocumentCategory", ":4:3:2:1", false);
            //调整排序字段
            Map<String, String> sortMap = Maps.newHashMap();
            sortMap.put("title","sort");

    		Page<MyDocumentCategoryVo> rd = myDocumentCategoryService.list(params, pageNumber, pageSize, getSort(sort, sortMap));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("MyDocumentCategory:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			MyDocumentCategoryVo rd =  myDocumentCategoryService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions(value = {"MyDocumentCategory:normal:add","MyDocumentCategory:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") MyDocumentCategoryVo main) {
		RestResult result = new RestResult();
		try {
			
			//复制另存
			if(act != null && ACT_COPY.equals(act)){
				main.setId(null);
			}else{
				myDocumentCategoryDescService.deleteByCategoryId(main.getId());
			}
			
			myDocumentCategoryService.save(main);
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
		} catch (Exception e) {
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
		}
		return result; 
	}
	
	@RequiresPermissions("MyDocumentCategory:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {

			myDocumentCategoryDescService.deleteByCategoryId(ids);
			
			myDocumentCategoryService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	
	
}
