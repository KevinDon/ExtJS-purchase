package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Help;
import com.newaim.purchase.admin.system.service.HelpService;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.admin.system.vo.HelpVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/admin/help")
public class HelpController extends ControllerBase{

    @Autowired
    private HelpService helpService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("Help:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String box, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
    		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
                params.put("content-S-LK-OR", keywords);
				params.put("title-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}

    		Page<HelpVo> rd = helpService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("Help:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			HelpVo rd =  helpService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

	@PostMapping("/preview")
	public RestResult preview(ServletRequest request, String box, String sort, String keywords,
						   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
						   @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
	){
		RestResult result = new RestResult();
		try {
			LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("content-S-LK-OR", keywords);
				params.put("title-S-LK-OR", keywords);
			}

			Page<HelpVo> rd = helpService.list(params, pageNumber, pageSize, getSort(sort));

			result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		return result;
	}

	@PostMapping("/info")
	public RestResult info(String id) {
		RestResult result = new RestResult();
		try {
			HelpVo rd =  helpService.get(id);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}

		return result;
	}
    
    @RequiresPermissions(value = {"Help:normal:add","Help:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") Help main, @ModelAttribute("attachments") AttachmentsVo attachments) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			
			if( main.getId() != null && StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					main.setId(null);
					helpService.add(main);
				}else{
					helpService.save(main);
				}
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				
			}else{
				helpService.add(main);
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}



    @RequiresPermissions("Help:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			if(hasDataType("Help" + ":4")){
				//物理删除
				helpService.delete(ids);
			}else{
				//删除标记
				helpService.setDelete(ids);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public Help main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return helpService.getHelp(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Help();
        }
        return null;
    }
}
