package com.newaim.purchase.admin.account.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.Resource;
import com.newaim.purchase.admin.account.service.ResourceService;
import com.newaim.purchase.admin.account.vo.ResourceMenuVo;
import com.newaim.purchase.admin.account.vo.ResourceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ResourceController extends ControllerBase {

	@Autowired
	private ResourceService resourceService;

	@ResponseBody
	@PostMapping("/menu")
	public RestResult leftMenu(String alias){
		RestResult result = new RestResult();
		try {
			
			List<String> userIds = SessionUtils.currentUserVo().getRoleIds();
			
			List<ResourceMenuVo> rd =  resourceService.list(alias, userIds);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		return result;
	}

	@RequiresPermissions("Modules:normal:list")
	@PostMapping("/list")
	public RestResult list() {
		RestResult result = new RestResult();
		try {

			List<ResourceVo> rd =  resourceService.listForAdmin(null);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions("Modules:normal:list")
	
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ResourceVo rd =  resourceService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}



	@RequiresPermissions(value = {"Modules:normal:add", "Modules:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") Resource main) {
		RestResult result = new RestResult();

		try {

			if(ACT_ADD.equals(act) || ACT_INS.equals(act)){
				// created
				Resource r = new Resource(); 
				BeanMapper.copyProperties(main, r, true);
				//新建
				r.setId(null);
				r.setChildren(null);
				r.setCreatedAt(new Date());
				
				if(ACT_INS.equals(act)){
					//插入子项
					Resource lastChildren = resourceService.getChildrenRow(main.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf());
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getId());
					
				}else{
					//新建，在最后
					Resource lastChildren = resourceService.getChildrenRow(main.getParentId());
					if(lastChildren!=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf());
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getParentId());
				}

				resourceService.add(r);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_EDIT.equals(act)){
				//edit
				main.setFunctions(main.getFunctions());
				resourceService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_UP.equals(act)){
				//上移
				if(StringUtils.isNotBlank(main.getId())){
					Resource upr = resourceService.getUp(main.getId());
					if(upr != null){
						Integer oSort = main.getSort();
						main.setSort(upr.getSort());
						
						resourceService.save(main);
						upr.setSort(oSort);
						resourceService.save(upr);

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
					Resource downr = resourceService.getDown(main.getId());
					if(downr != null){
						Integer oSort = main.getSort();
						main.setSort(downr.getSort());
						resourceService.save(main);

						downr.setSort(oSort);
						resourceService.save(downr);

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
					Resource r = main;

					if(StringUtils.isNotBlank(main.getParentId())){
						Resource f = resourceService.getResource(r.getParentId());

						Resource lastChildren = resourceService.getChildrenRow(f.getParentId());
						if(lastChildren != null && lastChildren.getSort() >0){
							r.setSort(lastChildren.getSort()+1);
						}else{
							r.setSort(1);
						}

						r.setParentId(f.getParentId());
						r.setLevel(f.getLevel());

						resourceService.save(r);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgUpSuccess());

					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUpgradeFailureTopLevel());
					}

				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_RIGHT.equals(act)){
				//降级
				Resource r = main;

				Resource upr = resourceService.getUp(main.getId());
				if(StringUtils.isNotBlank(upr.getId())){
					Resource lastChildren = resourceService.getChildrenRow(upr.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
					}else{
						r.setSort(1);
					}
					r.setParentId(upr.getId());

					resourceService.save(r);

					result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDownSuccess());
				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDownFailureBottom());
				}

			}else{
				result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
	
	@RequiresPermissions("Modules:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();

		try {
			resourceService.delete(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
	
	@ModelAttribute("main")
    public Resource main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return resourceService.getResource(id);
        }
        return null;
    }
}
