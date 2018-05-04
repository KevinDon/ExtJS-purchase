package com.newaim.purchase.admin.account.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.Department;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.DepartmentService;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.DepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;

@RestController
@RequestMapping("/dep")
public class DepartmentController extends ControllerBase {
	
	@Autowired
    private DepartmentService departmentService;

	@Autowired
	private UserService userService;
	
	
	@RequiresPermissions("Department:normal:list")
	@PostMapping("/list")
	public RestResult list() {
    	RestResult result = new RestResult();
		try {
    		
			List<DepartmentVo> rd =  departmentService.list(null);
    		
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		
		return result; 
	}
	@RequiresPermissions("Department:normal:list")
	@PostMapping("/choseList")
	public RestResult choseList(String inDep) {
		RestResult result = new RestResult();
		try {
			if(inDep.equals("1")){
                UserVo user = SessionUtils.currentUserVo();
                List<DepartmentVo> rd =  departmentService.list(user.getDepartmentId());
                result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			}else{
				List<DepartmentVo> rd =  departmentService.list(null);
				result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			}
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions("Department:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			DepartmentVo rd =  departmentService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions(value = {"Department:normal:add", "Department:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") Department main) {
		RestResult result = new RestResult();

		try {

			if(ACT_ADD.equals(act) || ACT_INS.equals(act)){
				// created
				Department r = new Department(); 
				BeanMapper.copyProperties(main, r, true);

				r.setId(null);
				r.setChildren(null);
				r.setCreatedAt(new Date());
				
				if(ACT_INS.equals(act)){
					//插入子项
					Department lastChildren = departmentService.getChildrenRow(main.getId());
					if(lastChildren!=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf());
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getId());
					
				}else{
					//新建，在最后
					Department lastChildren = departmentService.getChildrenRow(main.getParentId());
					if(lastChildren!=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf());
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getParentId());
				}

				departmentService.add(r);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_EDIT.equals(act)){
				//edit
				departmentService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_UP.equals(act)){
				//上移
				if(StringUtils.isNotBlank(main.getId())){
					Department upr = departmentService.getUp(main.getId());
					if(upr != null){
						Integer oSort = main.getSort();
						main.setSort(upr.getSort());
						
						departmentService.save(main);
						upr.setSort(oSort);
						departmentService.save(upr);

						result.setSuccess(true).setMsg(localeMessageSource.getMsgUpSuccess());
					}else{
						result.setSuccess(false).setMsg(localeMessageSource.getMsgUpFailureTop());
					}
				}else{
					result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_DOWN.equals(act)){
				//下移
				if(StringUtils.isNotBlank(main.getId())){
					Department downr = departmentService.getDown(main.getId());
					if(downr != null){
						Integer oSort = main.getSort();
						main.setSort(downr.getSort());
						departmentService.save(main);

						downr.setSort(oSort);
						departmentService.save(downr);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDownSuccess());
					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDownFailureBottom());
					}
				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_LEFT.equals(act)){
				//升级
				if(StringUtils.isNotBlank(main.getId())){
					Department r = main;

					if(StringUtils.isNotBlank(main.getParentId())){
						Department f = departmentService.getDepartment(r.getParentId());
						Department lastChildren = departmentService.getChildrenRow(f.getParentId());
						if(lastChildren != null && lastChildren.getSort() >0){
							r.setSort(lastChildren.getSort()+1);
						}else{
							r.setSort(1);
						}

						r.setParentId(f.getParentId());
						r.setLevel(f.getLevel());

						departmentService.save(r);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgUpSuccess());

					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUpgradeFailureTopLevel());
					}

				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_RIGHT.equals(act)){
				//降级
				Department r = main;

				Department upr = departmentService.getUp(main.getId());
				if(StringUtils.isNotBlank(upr.getId())){
					Department lastChildren = departmentService.getChildrenRow(upr.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
					}else{
						r.setSort(1);
					}
					r.setParentId(upr.getId());

					departmentService.save(r);

					result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDownSuccess());
				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDownFailureBottom());
				}

			}else{
				result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
			}

			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions("Department:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();

		try {
			List<User> users = userService.findByDepartmentId(ids);
			if(users != null && users.size() > 0){
				result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure("There are some users under the departmnet!"));
				return result;
			}
			departmentService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	@ModelAttribute("main")
    public Department main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return departmentService.getDepartment(id);
        }
        return null;
    }
}
