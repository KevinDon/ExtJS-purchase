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
import com.newaim.purchase.admin.system.service.DictionaryCategoryDescService;
import com.newaim.purchase.admin.system.service.DictionaryCategoryService;
import com.newaim.purchase.admin.system.vo.DictionaryCategoryVo;

@RestController
@RequestMapping("/dictcate")
public class DictionaryCategoryController extends ControllerBase{

	@Autowired
    private DictionaryCategoryService dictionaryCategoryService;
	
	@Autowired
    private DictionaryCategoryDescService dictionaryCategoryDescService;

	@RequiresPermissions("DictionaryCategory:normal:list")
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
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}
			params = setParams(params, "DictionaryCategory", ":4:3:2:1", false);
            //调整排序字段
            Map<String, String> sortMap = Maps.newHashMap();
            sortMap.put("title","categoryId");

    		Page<DictionaryCategoryVo> rd = dictionaryCategoryService.list(params, pageNumber, pageSize, getSort(sort, sortMap));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("DictionaryCategory:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			DictionaryCategoryVo rd =  dictionaryCategoryService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions(value = {"DictionaryCategory:normal:add", "DictionaryCategory:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") DictionaryCategoryVo main) {
		RestResult result = new RestResult();
		try {
			
			//复制另存
			if(act != null && ACT_COPY.equals(act)){
				main.setId(null);
			}else{
				dictionaryCategoryDescService.deleteByCategoryId(main.getId());
			}
			
			dictionaryCategoryService.save(main);
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
		} catch (Exception e) {
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
		}
		return result; 
	}
	
	@RequiresPermissions("DictionaryCategory:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {

			dictionaryCategoryDescService.deleteByCategoryId(ids);
			
			dictionaryCategoryService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	
	
}
