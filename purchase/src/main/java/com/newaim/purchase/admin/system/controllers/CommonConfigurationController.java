package com.newaim.purchase.admin.system.controllers;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.admin.system.service.DictionaryDescService;
import com.newaim.purchase.admin.system.service.DictionaryService;
import com.newaim.purchase.admin.system.service.DictionaryValueService;
import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;

@RestController
@RequestMapping("/common")
public class CommonConfigurationController extends ControllerBase {

	@Autowired
    private DictionaryService dictionaryService;
	
	@Autowired
    private DictionaryDescService dictionaryDescService;
	
	@Autowired
	private DictionaryValueService dictionaryValueService;


	@RequiresPermissions("CommonConfiguration:normal:list")
	@PostMapping("/list")
	public RestResult list(String categoryId) {
		RestResult result = new RestResult();
		try {
			List<DictionaryCallVo> rd =  dictionaryService.getByCategoryId("DDC20170814062954183");
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

	@RequiresPermissions({"CommonConfiguration:normal:edit"})
	@PostMapping("/save")
	public RestResult save(String act, String rows) {
		RestResult result = new RestResult();
		try {
			JSONArray arrRows = JSONArray.fromObject(rows);
			if(arrRows.size()>0){
				for (int i=0; i<arrRows.size(); i++){
					JSONObject objRow = JSONObject.fromObject(arrRows.get(i));
					
					dictionaryValueService.deleteByDictId(objRow.getString("vid"));
					
					DictionaryValueVo dv = new DictionaryValueVo();
					dv.setDictId(objRow.getString("vid"));
					dv.setValue(objRow.getString("value"));
					dv.setIsDefault(1);
					dv.setCustom(1);
					dv.setSort(1);
					dv.setStatus(1);
					dictionaryValueService.save(dv);
				}
			}
		
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgSaveFailure("edit", e.getMessage()));
		}
		
		return result; 
	}
	
}
