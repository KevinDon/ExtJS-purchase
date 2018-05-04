package com.newaim.purchase.archives.service_provider.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderCategoryService;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderCategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/archives/service-provider-category")
public class ServiceProviderCategoryController extends ControllerBase {

	@Autowired
    private ServiceProviderCategoryService serviceProviderCategoryService;


	@PostMapping("/list")
	   @RequiresPermissions("ServiceCategory:normal:list")
	public RestResult list() {
		RestResult result = new RestResult();
		try {

			List<ServiceProviderCategoryVo> rd =  serviceProviderCategoryService.listForAdmin(null);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/listForDialog")
	public RestResult listForDialog() {
		RestResult result = new RestResult();
		try {

			List<ServiceProviderCategoryVo> rd =  serviceProviderCategoryService.list(null, Constants.Status.NORMAL.code);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/get")
	@RequiresPermissions(value = {"ServiceCategory:normal:list"}, logical = Logical.OR)
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ServiceProviderCategoryVo rd =  serviceProviderCategoryService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}

		return result;
	}


	@PostMapping("/save")
	 @RequiresPermissions(value = {"ServiceCategory:normal:add", "ServiceCategory:normal:edit"}, logical = Logical.OR)

	public RestResult save(String act, @ModelAttribute("main") ServiceProviderCategory main) {
		RestResult result = new RestResult();

		try {

			if(ACT_ADD.equals(act) || ACT_INS.equals(act)){
				// created
                ServiceProviderCategory r = BeanMapper.map(main, ServiceProviderCategory.class);

				//新建
				r.setId(null);
				r.setChildren(null);
				r.setCreatedAt(new Date());

				if(ACT_INS.equals(act)){
					//插入子项
					ServiceProviderCategory lastChildren = serviceProviderCategoryService.getChildrenRow(main.getId());
					if(lastChildren!=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf() !=null ? lastChildren.getLeaf(): 1);
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getId());

				}else{
					//新建，在最后
					ServiceProviderCategory lastChildren = serviceProviderCategoryService.getChildrenRow(main.getParentId());
					if(lastChildren !=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf() !=null ? lastChildren.getLeaf(): 1);
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getParentId());
				}

				serviceProviderCategoryService.add(r);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_EDIT.equals(act)){
				//edit
				serviceProviderCategoryService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_UP.equals(act)){
				//上移
				if(StringUtils.isNotBlank(main.getId())){
					ServiceProviderCategory upr = serviceProviderCategoryService.getUp(main.getId());
					if(upr != null){
						Integer oSort = main.getSort();
						main.setSort(upr.getSort());

						serviceProviderCategoryService.save(main);
						upr.setSort(oSort);
						serviceProviderCategoryService.save(upr);

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
					ServiceProviderCategory downr = serviceProviderCategoryService.getDown(main.getId());
					if(downr != null){
						Integer oSort = main.getSort();
						main.setSort(downr.getSort());
						serviceProviderCategoryService.save(main);

						downr.setSort(oSort);
						serviceProviderCategoryService.save(downr);

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
					ServiceProviderCategory r = main;

					if(StringUtils.isNotBlank(main.getParentId())){
						ServiceProviderCategory f = serviceProviderCategoryService.getServiceProviderCategory(r.getParentId());

						ServiceProviderCategory lastChildren = serviceProviderCategoryService.getChildrenRow(f.getParentId());
						if(lastChildren != null && lastChildren.getSort() >0){
							r.setSort(lastChildren.getSort()+1);
						}else{
							r.setSort(1);
						}

						r.setParentId(f.getParentId());
						r.setLevel(f.getLevel());

						serviceProviderCategoryService.save(r);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgUpgradeSuccess());

					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUpgradeFailureTopLevel());
					}

				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_RIGHT.equals(act)){
				//降级
				ServiceProviderCategory r = main;

				ServiceProviderCategory upr = serviceProviderCategoryService.getUp(main.getId());
				if(upr != null && StringUtils.isNotBlank(upr.getId())){
					ServiceProviderCategory lastChildren = serviceProviderCategoryService.getChildrenRow(upr.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
					}else{
						r.setSort(1);
					}
					r.setParentId(upr.getId());

					serviceProviderCategoryService.save(r);

					result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDowngradeSuccess());
				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDowngradeFailureBottomLevel());
				}

			}else{
				result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
			}

		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/delete")
	@RequiresPermissions(value = {"ServiceCategory:normal:del"}, logical = Logical.OR)
	public RestResult delete(String ids) {
		RestResult result = new RestResult();

		try {
			serviceProviderCategoryService.delete(ids);

			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}

	@ModelAttribute("main")
	public ServiceProviderCategory main(String id){
		if(StringUtils.isNotBlank(id)){
			return serviceProviderCategoryService.getServiceProviderCategory(id);
		}
		return new ServiceProviderCategory();
	}
	
}
