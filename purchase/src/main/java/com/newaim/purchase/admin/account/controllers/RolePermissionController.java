package com.newaim.purchase.admin.account.controllers;

import com.google.common.collect.Lists;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.service.ResourceService;
import com.newaim.purchase.admin.account.service.RoleResourceUnionService;
import com.newaim.purchase.admin.account.service.RoleService;
import com.newaim.purchase.admin.account.vo.RoleResourceUnionVo;
import com.newaim.purchase.admin.account.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/rolepermission")
public class RolePermissionController extends ControllerBase {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private RoleResourceUnionService roleResourceUnionService;

    @RequiresPermissions("RolePermission:normal:list")
    @PostMapping("/list")
    public RestResult list(String sort, String roleId){
    	RestResult result = new RestResult();
		try {
			if(StringUtils.isNotBlank(roleId)){
				List<RoleResourceUnionVo> rd = roleResourceUnionService.listByRoleId(roleId);
				result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			}else{
				result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure());
			}
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
    }
    
    @RequiresPermissions("RolePermission:normal:list")
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
    
    @RequiresPermissions(value = {"RolePermission:normal:add", "RolePermission:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, String roleId, String[] functions) {
		RestResult result = new RestResult();
		try {
			if(StringUtils.isNotBlank(roleId) && StringUtils.isNoneBlank(functions)){
				
				if(functions.length>0){
					List<RoleResourceUnion> rruList = Lists.newArrayList();

					for(int i=0; i<functions.length; i++){
						RoleResourceUnion rru = new RoleResourceUnion();
						String[] fts = functions[i].split("\\|");
						rru.setRoleId(roleId);
						rru.setResourceId(fts[0]);
						if(fts.length>3){
							rru.setModel(fts[3]);
						}
						if(fts[1].equals("data")){
							rru.setData(fts[2]);
						}else{
							rru.setAction(fts[1] + ":"+fts[2]);
						}
						
						rru.setPath(resourceService.getParentPath(fts[0]));
						rruList.add(rru);
					}
					roleResourceUnionService.save(rruList);
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
