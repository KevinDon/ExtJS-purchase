package com.newaim.purchase.archives.vendor.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.vendor.entity.VendorCategory;
import com.newaim.purchase.archives.vendor.service.VendorCategoryService;
import com.newaim.purchase.archives.vendor.vo.VendorCategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/archives/vendor-category")
public class VendorCategoryController extends ControllerBase {

	@Autowired
    private VendorCategoryService vendorCategoryService;


	@PostMapping("/list")
	@RequiresPermissions("VendorCategory:normal:list")
	public RestResult list() {
		RestResult result = new RestResult();
		try {

			List<VendorCategoryVo> rd =  vendorCategoryService.listForAdmin(null);

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

			List<VendorCategoryVo> rd =  vendorCategoryService.list(null, Constants.Status.NORMAL.code);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {

			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/get")
	@RequiresPermissions(value = {"VendorCategory:normal:list"}, logical = Logical.OR)
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			VendorCategoryVo rd =  vendorCategoryService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}

		return result;
	}


	@PostMapping("/save")
	@RequiresPermissions(value = {"VendorCategory:normal:add", "VendorCategory:normal:edit"}, logical = Logical.OR)
	public RestResult save(String act, @ModelAttribute("main") VendorCategory main) {
		RestResult result = new RestResult();

		try {

			if(ACT_ADD.equals(act) || ACT_INS.equals(act)){
				// created
				VendorCategory r = BeanMapper.map(main, VendorCategory.class);
				//新增
				r.setId(null);
				r.setChildren(null);

				if(ACT_INS.equals(act)){
					//插入子项
					VendorCategory lastChildren = vendorCategoryService.getChildrenRow(main.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf() !=null ? lastChildren.getLeaf(): 1);
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getId());

				}else{
					//新建，在最后
					VendorCategory lastChildren = vendorCategoryService.getChildrenRow(main.getParentId());
					if(lastChildren !=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf() !=null ? lastChildren.getLeaf(): 1);
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getParentId());
				}

				vendorCategoryService.add(r);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_EDIT.equals(act)){
				//edit
				vendorCategoryService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_UP.equals(act)){
				//上移
				if(StringUtils.isNotBlank(main.getId())){
					VendorCategory upr = vendorCategoryService.getUp(main.getId());
					if(upr != null){
						Integer oSort = main.getSort();
						main.setSort(upr.getSort());

						vendorCategoryService.save(main);
						upr.setSort(oSort);
						vendorCategoryService.save(upr);

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
					VendorCategory downr = vendorCategoryService.getDown(main.getId());
					if(downr != null){
						Integer oSort = main.getSort();
						main.setSort(downr.getSort());
						vendorCategoryService.save(main);

						downr.setSort(oSort);
						vendorCategoryService.save(downr);

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
					VendorCategory r = main;

					if(StringUtils.isNotBlank(main.getParentId())){
						VendorCategory f = vendorCategoryService.getVendorCategory(r.getParentId());

						VendorCategory lastChildren = vendorCategoryService.getChildrenRow(f.getParentId());
						if(lastChildren != null && lastChildren.getSort() >0){
							r.setSort(lastChildren.getSort()+1);
						}else{
							r.setSort(1);
						}

						r.setParentId(f.getParentId());
						r.setLevel(f.getLevel());

						vendorCategoryService.save(r);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgUpgradeSuccess());

					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUpgradeFailureTopLevel());
					}

				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_RIGHT.equals(act)){
				//降级
				VendorCategory r = main;

				VendorCategory upr = vendorCategoryService.getUp(main.getId());
				if(upr != null && StringUtils.isNotBlank(upr.getId())){
					VendorCategory lastChildren = vendorCategoryService.getChildrenRow(upr.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
					}else{
						r.setSort(1);
					}
					r.setParentId(upr.getId());

					vendorCategoryService.save(r);

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
	@RequiresPermissions(value = {"VendorCategory:normal:del"}, logical = Logical.OR)
	public RestResult delete(String ids) {
		RestResult result = new RestResult();

		try {
			vendorCategoryService.delete(ids);

			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}

	@ModelAttribute("main")
	public VendorCategory main(String act, String id){
		if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
			return vendorCategoryService.getVendorCategory(id);
		}
		return new VendorCategory();
	}
	
}
