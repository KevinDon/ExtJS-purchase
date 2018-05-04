package com.newaim.purchase.admin.system.controllers;

import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;

@RestController
@RequestMapping("/attachment")
public class AttachmentController extends ControllerBase{

	@Autowired
    private AttachmentService attachmentService;
	
	@RequiresPermissions("MyDocument:normal:list")
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
			params = setParams(params, "MyDocument", ":4:3:2:1", false);
    		Page<AttachmentVo> rd = attachmentService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }
	
	@RequiresPermissions("MyDocument:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			AttachmentVo rd =  attachmentService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions(value = {"MyDocument:normal:add", "MyDocument:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") Attachment main) {
		RestResult result = new RestResult();
		try {

			if( StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(ACT_COPY.equals(act)){
					main.setId(null);
					attachmentService.add(main);
				}
				attachmentService.save(main);
			}else{
				attachmentService.add(main);
			}
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgSaveFailure("save", e.getMessage()));
		}
		return result; 
	}
	
	@RequiresPermissions("MyDocument:normal:del")
	@PostMapping("/delete")
	public RestResult delete(ServletRequest request, String ids) {
		RestResult result = new RestResult();
		try {
			attachmentService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	@ModelAttribute("main")
    public Attachment main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return attachmentService.getAttachment(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Attachment();
        }
        return null;
    }
}
