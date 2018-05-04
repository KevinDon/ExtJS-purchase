package com.newaim.purchase.admin.system.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.DictionaryDescService;
import com.newaim.purchase.admin.system.service.DictionaryService;
import com.newaim.purchase.admin.system.service.DictionaryValueService;
import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
import com.newaim.purchase.admin.system.vo.DictionaryVo;

@RestController
@RequestMapping("/dict")
public class DictionaryController extends ControllerBase {

	@Autowired
    private DictionaryService dictionaryService;
	
	@Autowired
    private DictionaryDescService dictionaryDescService;
	
	@Autowired
	private DictionaryValueService dictionaryValueService;


	@RequiresPermissions("Dictionary:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("codeMain-S-LK-OR", keywords);
				params.put("codeSub-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}
			params = setParams(params, "Dictionary", ":4:3:2:1", false);

			//调整排序字段
            Map<String, String> sortMap = Maps.newHashMap();
            sortMap.put("title","desc.name");
            sortMap.put("categoryName","categoryId");

    		Page<DictionaryVo> rd = dictionaryService.list(params, pageNumber, pageSize, getSort(sort, sortMap));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("Dictionary:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			DictionaryVo rd =  dictionaryService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

//	@RequiresPermissions("Dictionary:normal:list")
	@PostMapping("/getkey")
	public RestResult getKeyList(String code, String codeSub) {
		RestResult result = new RestResult();
		try {
			List<DictionaryCallVo> rd = Lists.newArrayList();
			
			if(StringUtils.isEmpty(codeSub)){
				rd =  dictionaryService.getByCodemain(code);
			}else{
				rd =  dictionaryService.getByCodemainAndCodeSub(code, codeSub);
			}
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

	@RequiresPermissions(value = {"Dictionary:normal:add","Dictionary:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") DictionaryVo main) {
		RestResult result = new RestResult();
		try {
			if( main.getId() != null && ! main.getId().isEmpty() ){
				
				//复制另存
				if(act != null && ACT_COPY.equals(act)){
					main.setId(null);
				}else{
					dictionaryDescService.deleteByDictId(main.getId());
				}
				
				dictionaryService.save(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				
				dictionaryService.save(main);

				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
		}
		
		return result; 
	}
	
	@RequiresPermissions("Dictionary:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			dictionaryDescService.deleteByDictId(ids);
			dictionaryValueService.deleteByDictId(ids);
			dictionaryService.delete(ids);

			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
}
