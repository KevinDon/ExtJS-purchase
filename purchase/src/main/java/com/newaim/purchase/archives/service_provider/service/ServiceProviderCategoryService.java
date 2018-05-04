package com.newaim.purchase.archives.service_provider.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderCategoryDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderCategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderCategoryService extends ServiceBase  {

	@Autowired
	private ServiceProviderCategoryDao serviceProviderCategoryDao;

	/**
	 * get list from menu
	 * @param parentId
	 * @return
	 */
	public List<ServiceProviderCategoryVo> list(String parentId, Integer status){
		List<ServiceProviderCategoryVo> result = Lists.newArrayList();
		List<ServiceProviderCategory> rows = serviceProviderCategoryDao.findServiceProviderCategoryByParentId(parentId, status);
		if(rows.size() >0 ){
			result = BeanMapper.mapList(rows, ServiceProviderCategory.class, ServiceProviderCategoryVo.class);
			for (int i = 0; i < result.size(); i++) {
				ServiceProviderCategoryVo vo = result.get(i);
				vo.setChildren(filterProductCategoryVo(vo, status));
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
	private List<ServiceProviderCategoryVo> filterProductCategoryVo(ServiceProviderCategoryVo vo, Integer status){
		List<ServiceProviderCategoryVo> children = vo.getChildren();
		if(children != null && children.size() > 0){
			List<ServiceProviderCategoryVo> cdata = Lists.newArrayList();
			for (int j = 0; j < children.size(); j++) {
				ServiceProviderCategoryVo c = children.get(j);
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
	public List<ServiceProviderCategoryVo> listForAdmin(String parentId){
		List<ServiceProviderCategoryVo> list = null;
		List<ServiceProviderCategory> rows = serviceProviderCategoryDao.findServiceProviderCategoryByParentId(parentId, null);

		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, ServiceProviderCategory.class, ServiceProviderCategoryVo.class);
		}
		return list;

	}

	public ServiceProviderCategoryVo get(String id){
		ServiceProviderCategoryVo o = null;
		ServiceProviderCategory row = serviceProviderCategoryDao.findServiceProviderCategoryById(id);
		return BeanMapper.map(row, ServiceProviderCategoryVo.class);
	}

	public ServiceProviderCategory getServiceProviderCategory(String id){
		ServiceProviderCategory o = serviceProviderCategoryDao.findServiceProviderCategoryById(id);
		return o;
	}

	public ServiceProviderCategory getUp(String id){
		ServiceProviderCategory o = null;
		List<ServiceProviderCategory> rows = serviceProviderCategoryDao.getUp(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;
	}


	public ServiceProviderCategory getDown(String id){
		ServiceProviderCategory o = null;
		List<ServiceProviderCategory> rows = serviceProviderCategoryDao.getDown(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;
	}


	public ServiceProviderCategory getChildrenRow(String id){
		ServiceProviderCategory o = null;
		List<ServiceProviderCategory> rows = serviceProviderCategoryDao.listChildrenRows(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		return o;
	}

	/**
	 *  新增
	 */
	@Transactional(rollbackFor = Exception.class)
	public void add(ServiceProviderCategory category){
		serviceProviderCategoryDao.clear();
		category.setType(Constants.VendorType.SERVICE_PROVIDER.code);
		//设置供应商分类创建人相关信息
		category.setStatus(1);
		setServiceProviderCategoryCreatorInfo(category);
		//设置采购员信息
		setServiceProviderCategoryBuyerInfo(category);
		serviceProviderCategoryDao.save(category);
	}

	@Transactional(rollbackFor = Exception.class)
	public void save(ServiceProviderCategory category){
		category.setType(Constants.VendorType.SERVICE_PROVIDER.code);
		if(StringUtils.isNotBlank(category.getId())){
			//编辑
			setServiceProviderCategoryOperatorInfo(category);
		}else {
			//新增
			setServiceProviderCategoryCreatorInfo(category);
			//设置采购员信息
			setServiceProviderCategoryBuyerInfo(category);
		}
		serviceProviderCategoryDao.save(category);
	}

	/**
	 * 设置服务商分类创建人相关信息
	 */
	private void setServiceProviderCategoryCreatorInfo(ServiceProviderCategory category){
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
	 * 设置服务商分类操作人信息
	 */
	private void setServiceProviderCategoryOperatorInfo(ServiceProviderCategory category){
		category.setUpdatedAt(new Date());
	}

	/**
	 * 设置服务商分类采购员信息
	 * @param category
	 */
	private void setServiceProviderCategoryBuyerInfo(ServiceProviderCategory category){
		UserVo user = SessionUtils.currentUserVo();
		category.setBuyerId(user.getId());
		category.setBuyerCnName(user.getCnName());
		category.setBuyerEnName(user.getEnName());
	}


	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		serviceProviderCategoryDao.delete(id);
	}
	
//	/**
//     * 创建动态查询条件组合.
//     */
//    private Specification<ServiceProviderCategory> buildSpecification(LinkedHashMap<String, Object> searchParams) {
//        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//        Specification<ServiceProviderCategory> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServiceProviderCategory.class);
//        return spec;
//    }
	
}
