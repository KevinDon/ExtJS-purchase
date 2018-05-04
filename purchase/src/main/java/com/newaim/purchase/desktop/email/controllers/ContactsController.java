package com.newaim.purchase.desktop.email.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.email.entity.Contacts;
import com.newaim.purchase.desktop.email.service.ContactsService;
import com.newaim.purchase.desktop.email.vo.ContactsVo;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/contacts")
public class ContactsController extends ControllerBase{

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("Contacts:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords, String vendorId,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("cnName-S-LK-OR", keywords);
				params.put("enName-S-LK-OR", keywords);
				params.put("skype-S-LK-OR", keywords);
				params.put("qq-S-LK-OR", keywords);
				params.put("phone-S-LK-OR", keywords);
				params.put("extension-S-LK-OR", keywords);
				params.put("wechat-S-LK-OR", keywords);
				params.put("mobile-S-LK-OR", keywords);
				params.put("email-S-LK-OR", keywords);
				params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
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
			params = setParams(params, "Contacts", ":4:3:2:1", false);

			if(null != vendorId && StringUtils.isNotEmpty(vendorId)){
			    params.put("vendorId-S-EQ-ADD", vendorId);
            }

    		Page<ContactsVo> rd = contactsService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

	@RequiresPermissions("Contacts:normal:list")
	@PostMapping("/listForDialog")
	public RestResult listForDialog(String sort, String vendorId, String type,
						   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
						   @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
	){
		RestResult result = new RestResult();
		try {
			Page<ContactsVo> rd = new PageImpl<>(new ArrayList<>(), new PageRequest(pageNumber, pageSize), 0);
			LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();

			params = setParams(params, "Contacts", ":4:3:2:1", false);

			if(StringUtils.equals(type, "1")){
				//采购合同中联系人选择
				params.put("vendorId-S-EQ-ADD", vendorId);

				rd = contactsService.list(params, pageNumber, pageSize, getSort(sort));
			}
			result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		return result;
	}

    @RequiresPermissions("Contacts:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ContactsVo rd =  contactsService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"Contacts:normal:add","Contacts:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") Contacts main) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if( main.getId() != null && StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					contactsService.saveAs(main);
				}else{
					contactsService.save(main);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				
			}else{
					
				contactsService.add(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
				
			}
			
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("Contacts:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!contactsService.get(ids).getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if(hasDataType("Contacts" + ":4")){
				//物理删除
				contactsService.delete(ids);
			}else{
				//删除标记
				contactsService.setDelete(ids);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public Contacts main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return contactsService.getContacts(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Contacts();
        }
        return null;
    }
}
