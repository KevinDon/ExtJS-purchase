package com.newaim.purchase.admin.account.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/admin/user")
public class UserController extends ControllerBase {

    @Autowired
    private UserService userService;

    @RequiresPermissions("User:normal:list")
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
				params.put("account-S-LK-OR", keywords);
				params.put("email-S-LK-OR", keywords);
				params.put("qq-S-LK-OR", keywords);
				params.put("skype-S-LK-OR", keywords);
				params.put("wechat-S-LK-OR", keywords);
				params.put("phone-S-LK-OR", keywords);
				params.put("extension-S-LK-OR", keywords);
				params.put("department.cnName-S-LK-OR", keywords);
				params.put("department.enName-S-LK-OR", keywords);
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

            params = setParams(params, "User", ":4:3:2:1", false);
			
			if(hasDataType("User:4")){
				//带禁用的数据
				
			}else{
				//非禁用的数据
				params.put("status-N-EQ-ADD", "1");
			}
			
			if(hasDataType("User:3")){
				//不分部门
//				params.put("status-N-EQ-ADD", "1");
			}else if(hasDataType("User:2")){
				//部门内
				//params.put("status-N-EQ-ADD", "1");
			}else if(hasDataType("User:1")){
				//自身
				//params.put("status-N-EQ-ADD", "1");
			}
			
			Page<UserVo> page = userService.list(params, pageNumber, pageSize, getSort(sort));
			result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
        return result;
    }
    
//    @RequiresPermissions("User:normal:list")
    @PostMapping("/listbydepartmentid")
    public RestResult listByDepartmentId(ServletRequest request, String sort, String keywords, String depId,String inDep,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
		try {
			
			if(inDep.equals("1")){
	                LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
	                UserVo user = SessionUtils.currentUserVo();
	                String depIds = user.getDepartmentId();
	                params.put("departmentId-S-IN", depIds);
					
					if(hasDataType("User:4")){
						//带禁用的数据
						
					}else{
						//非禁用的数据
						params.put("status-N-EQ-ADD", "1");
					}
					
					
					Page<UserVo> page = userService.list(params, pageNumber, pageSize, getSort(sort));
					result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
			}else{
				if(StringUtils.isNotBlank(depId)){
	                LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
	                
	                String depIds = getDepartmentsByDepId(depId);
	                params.put("departmentId-S-IN", depIds);
					
					if(hasDataType("User:4")){
						//带禁用的数据
						
					}else{
						//非禁用的数据
						params.put("status-N-EQ-ADD", "1");
					}
					
					
					Page<UserVo> page = userService.list(params, pageNumber, pageSize, getSort(sort));
					result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
				}else{
	                LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
	                if (StringUtils.isNotBlank(keywords)) {
	                    params.put("account-S-LK-OR", keywords);
	                    params.put("skype-S-LK-OR", keywords);
	                    params.put("email-S-LK-OR", keywords);
	                    params.put("qq-S-LK-OR", keywords);
	                    params.put("wechat-S-LK-OR", keywords);
	                    params.put("cnName-S-LK-OR", keywords);
	                    params.put("enName-S-LK-OR", keywords);
	                    params.put("phone-S-LK-OR", keywords);
	                } else {
	                    params = ServletUtils.getParametersStartingWith(request);
	                }
					
					if(hasDataType("User:4")){
						//带禁用的数据
						
					}else{
						//非禁用的数据
						params.put("status-N-EQ-ADD", "1");
					}
					
					Page<UserVo> page = userService.list(params, pageNumber, pageSize, getSort(sort));
					result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
				}
			}

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
        return result;
    }
    
    @RequiresPermissions("User:normal:list")
    @PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			UserVo rd =  userService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"User:normal:add","User:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") User main) {
		RestResult result = new RestResult();
		try {
			if( StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					main.setCreatedAt(new Date());
					main.setId(null);
					userService.add(main);
				}else{
					userService.save(main);
				}
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				main.setDepartment(null);
				main.setCreatedAt(new Date());
				userService.add(main);

				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @PostMapping("/setting")
	public RestResult setting(String act, @ModelAttribute("main") User main) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			
			if( StringUtils.isNotBlank(main.getId()) && main.getId().equals(user.getId())){
				
				userService.save(main);
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
				
			}else{
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgOperateException(act));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("User:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			userService.delete(ids);
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public User main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return userService.getUser(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new User();
        }
        return null;
    }
}
