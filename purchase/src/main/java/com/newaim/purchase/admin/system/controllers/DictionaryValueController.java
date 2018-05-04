package com.newaim.purchase.admin.system.controllers;

import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.DictionaryValueDescService;
import com.newaim.purchase.admin.system.service.DictionaryValueService;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;

@RestController
@RequestMapping("/dictvalue")
public class DictionaryValueController extends ControllerBase {

	@Autowired
    private DictionaryValueService dictionaryValueService;
	
	@Autowired
    private DictionaryValueDescService dictionaryValueDescService;

	@RequiresPermissions("DictionaryValue:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
        ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = ServletUtils.getParametersStartingWith(request);
    		params = setParams(params, "DictionaryValue", ":4:3:2:1", false);
    		Page<DictionaryValueVo> rd = dictionaryValueService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("DictionaryValue:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			DictionaryValueVo rd =  dictionaryValueService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions(value = {"DictionaryValue:normal:add", "DictionaryValue:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") DictionaryValueVo main) {
		RestResult result = new RestResult();
		try {
			if( main.getId() != null && ! main.getId().isEmpty() ){
				
				//复制另存
				if(act != null && ACT_COPY.equals(act)){
					main.setId(null);
				}else{
					dictionaryValueDescService.deleteByDictId(main.getId());
				}
				
				dictionaryValueService.save(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				dictionaryValueService.save(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
		}
		
		return result; 
	}
	
	@RequiresPermissions("DictionaryValue:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			dictionaryValueDescService.deleteByDictId(ids);
			
			dictionaryValueService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
}
