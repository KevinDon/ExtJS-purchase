package com.newaim.purchase.admin.account.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;
import com.newaim.purchase.admin.account.service.RoleService;
import com.newaim.purchase.admin.account.service.UserRoleUnionService;
import com.newaim.purchase.admin.account.vo.RoleVo;
import com.newaim.purchase.admin.account.vo.UserRoleUnionVo;

import java.util.List;

@RestController
@RequestMapping("/admin/roleassign")
public class RoleAssignController extends ControllerBase {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleUnionService userRoleUnionService;
    

    @RequiresPermissions("RoleAssign:normal:list")
    @PostMapping("/listuser")
    public RestResult listUser(String sort, String roleId){
    	RestResult result = new RestResult();
		try {
			
			if(StringUtils.isNotBlank(roleId)){
				List<UserRoleUnionVo> rd = userRoleUnionService.listByRoleId(roleId);
				result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			}else{
				result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure());
			}

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
    }
    
    @RequiresPermissions("RoleAssign:normal:list")
    @PostMapping("/listrole")
    public RestResult listRole(String sort, String userId){
    	RestResult result = new RestResult();
		try {
			
			List<UserRoleUnionVo> rd = Lists.newArrayList();
			rd = userRoleUnionService.listByUserId(userId);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
    }
    
    @RequiresPermissions("RoleAssign:normal:list")
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
    
    @RequiresPermissions(value = {"RoleAssign:normal:add", "RoleAssign:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String roleId, String[] userIds, String userId, String[] roleIds) {
		RestResult result = new RestResult();
		try {
			
			if(StringUtils.isNotBlank(roleId)){
				userRoleUnionService.deleteUserRoleByRoleId(roleId);
				if(userIds != null){
					for(int i=0; i<userIds.length; i++){
						UserRoleUnion o = new UserRoleUnion();
						o.setRoleId(roleId);
						o.setUserId(userIds[i]);
						userRoleUnionService.save(o);
					}
				}
				
				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
			}else if(StringUtils.isNotBlank(userId)){
				userRoleUnionService.deleteUserRoleByUserId(userId);
				
				if(roleIds != null){
					for(int i=0; i<roleIds.length; i++){
						UserRoleUnion o = new UserRoleUnion();
						o.setRoleId(roleIds[i]);
						o.setUserId(userId);
						userRoleUnionService.save(o);
					}
				}
				
				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
			}else{
				
				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveFailure("edit"));
			}
			
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
}
