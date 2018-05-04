package com.newaim.purchase.archives.vendor.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.vendor.dao.VendorCategoryDao;
import com.newaim.purchase.archives.vendor.entity.VendorCategory;
import com.newaim.purchase.archives.vendor.vo.VendorCategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VendorCategoryService extends ServiceBase  {

	@Autowired
	private VendorCategoryDao vendorCategoryDao;

	/**
	 * get list from menu
	 * @param parentId
	 * @return
	 */
	public List<VendorCategoryVo> list(String parentId, Integer status){
		List<VendorCategoryVo> list = null;
		List<VendorCategory> rows = vendorCategoryDao.findVendorCategoryByParentId(parentId, status);

		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, VendorCategory.class, VendorCategoryVo.class);
			for (int i = 0; i < list.size(); i++) {
				VendorCategoryVo vo = list.get(i);
				vo.setChildren(filterProductCategoryVo(vo, status));
			}
		}

		return list;

	}

	/**
	 * 过滤子集状态，并返回子集
	 * @param vo
	 * @param status
	 * @return
	 */
	private List<VendorCategoryVo> filterProductCategoryVo(VendorCategoryVo vo, Integer status){
		List<VendorCategoryVo> children = vo.getChildren();
		if(children != null && children.size() > 0){
			List<VendorCategoryVo> cdata = Lists.newArrayList();
			for (int j = 0; j < children.size(); j++) {
				VendorCategoryVo c = children.get(j);
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
	public List<VendorCategoryVo> listForAdmin(String parentId){
		List<VendorCategoryVo> list = null;
		List<VendorCategory> rows = vendorCategoryDao.findVendorCategoryByParentId(parentId, null);

		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, VendorCategory.class, VendorCategoryVo.class);
		}
		return list;

	}

	public VendorCategoryVo get(String id){
		VendorCategory row = vendorCategoryDao.findVendorCategoryById(id);
		return BeanMapper.map(row, VendorCategoryVo.class);
	}

	public VendorCategory getVendorCategory(String id){
		return vendorCategoryDao.findVendorCategoryById(id);
	}

	public VendorCategory getUp(String id){
		VendorCategory o = null;
		List<VendorCategory> rows = vendorCategoryDao.getUp(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;
	}

	public VendorCategory getDown(String id){
		VendorCategory o = null;
		List<VendorCategory> rows = vendorCategoryDao.getDown(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;
	}


	public VendorCategory getChildrenRow(String id){
		VendorCategory o = null;
		List<VendorCategory> rows = vendorCategoryDao.listChildrenRows(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;

	}

	/**
	 *  新增
	 */
	@Transactional(rollbackFor = Exception.class)
	public void add(VendorCategory category){
		vendorCategoryDao.clear();
		category.setType(Constants.VendorType.VENDOR.code);
		//设置供应商分类创建人相关信息
		setVendorCategoryCreatorInfo(category);
		//设置供应商分类采购员信息
		setVendorCategoryBuyerInfo(category);
		vendorCategoryDao.save(category);
	}

	/**
	 * 保存，包含新增和修改
	 * @param category
	 */
	@Transactional(rollbackFor = Exception.class)
	public void save(VendorCategory category){
		category.setType(Constants.VendorType.VENDOR.code);
		if(StringUtils.isNotBlank(category.getId())){
			//编辑
			setVendorCategoryOperatorInfo(category);
		}else { //新增
			setVendorCategoryCreatorInfo(category);
			//设置供应商分类采购员信息
			setVendorCategoryBuyerInfo(category);
		}
		vendorCategoryDao.save(category);
	}

	/**
	 * 设置供应商分类创建人相关信息
	 */
	private void setVendorCategoryCreatorInfo(VendorCategory category){
		UserVo user = SessionUtils.currentUserVo();
		category.setCreatedAt(new Date());
		category.setCreatorId(user.getId());
		category.setCreatorCnName(user.getCnName());
		category.setCreatorEnName(user.getEnName());
		category.setDepartmentId(user.getDepartmentId());
		category.setDepartmentCnName(user.getDepartment().getCnName());
		category.setDepartmentEnName(user.getDepartment().getCnName());
	}

	/**
	 * 设置供应商分类操作人信息
	 */
	private void setVendorCategoryOperatorInfo(VendorCategory category){
		category.setUpdatedAt(new Date());
	}

	/**
	 * 设置供应商分类采购员信息
	 * @param category
	 */
	private void setVendorCategoryBuyerInfo(VendorCategory category){
		UserVo user = SessionUtils.currentUserVo();
		category.setBuyerId(user.getId());
		category.setBuyerCnName(user.getCnName());
		category.setBuyerEnName(user.getEnName());
	}

	/**
	 * 删除供应商
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		vendorCategoryDao.delete(id);
	}
	
}
