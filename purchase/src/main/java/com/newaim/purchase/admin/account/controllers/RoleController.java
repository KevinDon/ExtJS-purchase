package com.newaim.purchase.admin.account.controllers;

import org.apache.commons.lang3.StringUtils;
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
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.Role;
import com.newaim.purchase.admin.account.service.RoleService;
import com.newaim.purchase.admin.account.vo.RoleVo;
import com.newaim.purchase.admin.account.vo.UserVo;

import javax.servlet.ServletRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
public class RoleController extends ControllerBase {

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("Role:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
		try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("cnName-S-LK-OR", keywords);
				params.put("enName-S-LK-OR", keywords);
				params.put("code-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}
			params = setParams(params, "Role", ":4:3:2:1", false);
		/*	if(!hasDataType("ReportOrderInspection" + ":3") && !hasDataType("ReportOrderInspection" + ":2") && hasDataType("ReportOrderInspection" + ":1")){
				//自身
				UserVo user = SessionUtils.currentUserVo();
				params.put("creatorId-S-EQ-ADD", user.getId());
			}*/
			Page<RoleVo> page = roleService.list(params, pageNumber, pageSize, getSort(sort));
			result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
        return result;
    }
    
    @RequiresPermissions("Role:normal:list")
    @PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			RoleVo rd =  roleService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"Role:normal:add", "Role:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") Role main) {
		RestResult result = new RestResult();
		try {
			if( StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					
					main.setCreatedAt(new Date());
					main.setId(null);
					
					roleService.add(main);
				}else{
					roleService.save(main);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				
			}else{
				
				main.setCreatedAt(new Date());				
				roleService.add(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
				
			}
			
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
		}
		
		return result; 
	}
    
    @RequiresPermissions("Role:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			roleService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	@ModelAttribute("main")
    public Role main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return roleService.getRole(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Role();
        }
        return null;
    }
}
