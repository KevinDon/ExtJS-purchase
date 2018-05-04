package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.entity.ArchivesHistory;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archiveshistory")
public class ArchivesHistoryController extends ControllerBase{

	@Autowired
    private ArchivesHistoryService archivesHistoryService;
	
//	@RequiresPermissions("ArchivesHistory:normal:list")
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
			params = setParams(params, "ArchivesHistory", ":4:3:2:1", false);
    		Page<ArchivesHistoryVo> rd = archivesHistoryService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("ArchivesHistory:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ArchivesHistoryVo rd =  archivesHistoryService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

//	@RequiresPermissions(value = {"ArchivesHistory:normal:add","ArchivesHistory:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") ArchivesHistory main) {
		RestResult result = new RestResult();
		try {

			if( StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(ACT_COPY.equals(act)){
					main.setId(null);
					archivesHistoryService.add(main);
				}
				archivesHistoryService.save(main);
			}else{
				archivesHistoryService.add(main);
			}
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgSaveFailure("save", e.getMessage()));
		}
		return result; 
	}
	
	@RequiresPermissions("ArchivesHistory:normal:del")
	@PostMapping("/delete")
	public RestResult delete(ServletRequest request, String ids) {
		RestResult result = new RestResult();
		try {
            if(hasDataType("MyTemplate" + ":4")){
                //物理删除
                archivesHistoryService.delete(ids);
            }else{
                //删除标记
                archivesHistoryService.setDelete(ids);
            }

			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	@ModelAttribute("main")
    public ArchivesHistory main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return archivesHistoryService.getArchivesHistory(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new ArchivesHistory();
        }
        return null;
    }
}
