package com.newaim.purchase.archives.product.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import com.newaim.purchase.archives.product.service.ProductCategoryService;
import com.newaim.purchase.archives.product.vo.ProductCategoryVo;
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
@RequestMapping("/archives/product-category")
public class ProductCategoryController extends ControllerBase {

	@Autowired
    private ProductCategoryService productCategoryService;
	 @RequiresPermissions("ProductCategory:normal:list")
	@PostMapping("/list")
	public RestResult list() {
		RestResult result = new RestResult();
		try {

			List<ProductCategoryVo> rd =  productCategoryService.listForAdmin(null);

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

			List<ProductCategoryVo> rd =  productCategoryService.list(null, Constants.Status.NORMAL.code);

			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/get")
	@RequiresPermissions(value = {"ProductCategory:normal:list"}, logical = Logical.OR)
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			ProductCategoryVo rd =  productCategoryService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}

		return result;
	}


	@PostMapping("/save")
	@RequiresPermissions(value = {"ProductCategory:normal:add", "ProductCategory:normal:edit"}, logical = Logical.OR)
	public RestResult save(String act, @ModelAttribute("main") ProductCategory main) {
		RestResult result = new RestResult();

		try {

			if(ACT_ADD.equals(act) || ACT_INS.equals(act)){
				// created
				ProductCategory r = BeanMapper.map(main, ProductCategory.class);

				//新建
				r.setId(null);
				r.setChildren(null);

				if(ACT_INS.equals(act)){
					//插入子项
					ProductCategory lastChildren = productCategoryService.getChildrenRow(main.getId());
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
					ProductCategory lastChildren = productCategoryService.getChildrenRow(main.getParentId());
					if(lastChildren!=null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
						r.setLevel(lastChildren.getLeaf());
					}else{
						r.setSort(1);
						r.setLevel(1);
					}
					r.setParentId(main.getParentId());
				}

				productCategoryService.add(r);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_EDIT.equals(act)){
				//edit
				productCategoryService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

			}else if(ACT_UP.equals(act)){
				//上移
				if(StringUtils.isNotBlank(main.getId())){
					ProductCategory upr = productCategoryService.getUp(main.getId());
					if(upr != null){
						Integer oSort = main.getSort();
						main.setSort(upr.getSort());

						productCategoryService.save(main);
						upr.setSort(oSort);
						productCategoryService.save(upr);

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
					ProductCategory downr = productCategoryService.getDown(main.getId());
					if(downr != null){
						Integer oSort = main.getSort();
						main.setSort(downr.getSort());
						productCategoryService.save(main);

						downr.setSort(oSort);
						productCategoryService.save(downr);

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
					ProductCategory r = main;

					if(StringUtils.isNotBlank(main.getParentId())){
						ProductCategory f = productCategoryService.getProductCategory(r.getParentId());

						ProductCategory lastChildren = productCategoryService.getChildrenRow(f.getParentId());
						if(lastChildren != null && lastChildren.getSort() >0){
							r.setSort(lastChildren.getSort()+1);
						}else{
							r.setSort(1);
						}

						r.setParentId(f.getParentId());
						r.setLevel(f.getLevel());

						productCategoryService.save(r);

						result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgUpgradeSuccess());

					}else{
						result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUpgradeFailureTopLevel());
					}

				}else{
					result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
				}

			}else if(ACT_RIGHT.equals(act)){
				//降级
				ProductCategory r = main;

				ProductCategory upr = productCategoryService.getUp(main.getId());
				if(upr != null && StringUtils.isNotBlank(upr.getId())){
					ProductCategory lastChildren = productCategoryService.getChildrenRow(upr.getId());
					if(lastChildren != null && lastChildren.getSort() >0){
						r.setSort(lastChildren.getSort()+1);
					}else{
						r.setSort(1);
					}
					r.setParentId(upr.getId());

					productCategoryService.save(r);

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
	@RequiresPermissions(value = {"ProductCategory:normal:del"}, logical = Logical.OR)
	public RestResult delete(String ids) {
		RestResult result = new RestResult();

		try {
			productCategoryService.delete(ids);
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}

	@ModelAttribute("main")
	public ProductCategory main(String act, String id){
		if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
			return productCategoryService.getProductCategory(id);
		}
		return new ProductCategory();
	}
	
}
