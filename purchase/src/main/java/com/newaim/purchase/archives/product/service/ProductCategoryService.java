package com.newaim.purchase.archives.product.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.product.dao.ProductCategoryDao;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import com.newaim.purchase.archives.product.vo.ProductCategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductCategoryService extends ServiceBase  {


	@Autowired
	private ProductCategoryDao productCategoryDao;

	/**
	 * get list from menu
	 * @param parentId
	 * @return
	 */
	public List<ProductCategoryVo> list(String parentId, Integer status){
		List<ProductCategoryVo> result = Lists.newArrayList();

		List<ProductCategory> rows = productCategoryDao.findProductCategoryByParentId(parentId, status);

		if(rows.size() >0 ){
			result = BeanMapper.mapList(rows, ProductCategory.class, ProductCategoryVo.class);
			if(status != null){
				for (int i = 0; i < result.size(); i++) {
					ProductCategoryVo vo = result.get(i);
					vo.setChildren(filterProductCategoryVo(vo, status));
				}
			}
		}
		return result;
	}

	/**
	 * 过滤子集状态，并返回子集
	 * @param vo
	 * @param status
	 * @return
	 */
	private List<ProductCategoryVo> filterProductCategoryVo(ProductCategoryVo vo, Integer status){
		List<ProductCategoryVo> children = vo.getChildren();
		if(children != null && children.size() > 0){
			List<ProductCategoryVo> cdata = Lists.newArrayList();
			for (int j = 0; j < children.size(); j++) {
				ProductCategoryVo c = children.get(j);
				if(status.equals(c.getStatus())){
					if(c.getChildren() != null && c.getChildren().size() > 0){
						c.setChildren(filterProductCategoryVo(c, status));
					}
					cdata.add(c);
				}
			}
			vo.setChildren(cdata);
		}
		return vo.getChildren();
	}

	/**
	 * get list from admin
	 * @param parentId
	 * @return
	 */
	public List<ProductCategoryVo> listForAdmin(String parentId){
		List<ProductCategoryVo> list = null;

		List<ProductCategory> rows = productCategoryDao.findProductCategoryByParentId(parentId, null);

		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, ProductCategory.class, ProductCategoryVo.class);
		}

		return list;

	}

	public ProductCategoryVo get(String id){
		ProductCategory row = productCategoryDao.findProductCategoryById(id);
		ProductCategoryVo o = BeanMapper.map(row, ProductCategoryVo.class);
		return o;

	}

	public ProductCategory getProductCategory(String id){
		ProductCategory o = productCategoryDao.findProductCategoryById(id);
		return o;
	}

	public ProductCategory getUp(String id){
		ProductCategory o = null;
		List<ProductCategory> rows = productCategoryDao.getUp(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}

		return o;
	}


	public ProductCategory getDown(String id){
		ProductCategory o = null;
		List<ProductCategory> rows = productCategoryDao.getDown(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}

		return o;
	}


	public ProductCategory getChildrenRow(String id){
		ProductCategory o = null;
		if(StringUtils.isNotBlank(id)) {
			List<ProductCategory> rows = productCategoryDao.listChildrenRows(id);
			if (rows.size() > 0) {
				o = rows.get(0);
			}
		}
		return o;

	}

	@Transactional
	public void add(ProductCategory category){
		productCategoryDao.clear();

		//设置产品分类创建人相关信息
		setProductCategoryCreatorInfo(category);

		productCategoryDao.save(category);
	}

	@Transactional
	public void save(ProductCategory category){

		if(StringUtils.isNotBlank(category.getId())){ //编辑
			category.setUpdatedAt(new Date());
		}else { //新增
			setProductCategoryCreatorInfo(category);
		}

		productCategoryDao.save(category);
	}

	/**
	 * 设置产品分类创建人相关信息
	 */
	private void setProductCategoryCreatorInfo(ProductCategory category){
		UserVo user = SessionUtils.currentUserVo();
		category.setCreatedAt(new Date());
		category.setCreatorId(user.getId());
		category.setCreatorCnName(user.getCnName());
		category.setCreatorEnName(user.getEnName());
		category.setDepartmentId(user.getDepartmentId());
		category.setDepartmentCnName(user.getDepartment().getCnName());
		category.setDepartmentEnName(user.getDepartment().getCnName());
	}


	@Transactional
	public void delete(String id){
		productCategoryDao.delete(id);
	}
	
}
