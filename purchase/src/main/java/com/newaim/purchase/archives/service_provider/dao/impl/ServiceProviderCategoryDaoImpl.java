package com.newaim.purchase.archives.service_provider.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderCategoryDaoCustom;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;


public class ServiceProviderCategoryDaoImpl extends BaseDaoCustomImpl implements ServiceProviderCategoryDaoCustom {
	
	@Override
	public List<ServiceProviderCategory> findServiceProviderCategoryByParentId(String parentId, Integer status){
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ServiceProviderCategory AS main WHERE 1 = 1 ");
		
		if(parentId == null || parentId.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("parentId", parentId);
			hql.append("AND main.parentId=:parentId ");
		}
		if(status != null){
			params.put("status", status);
			hql.append("and main.status = :status ");
		}
		params.put("type", Constants.VendorType.SERVICE_PROVIDER.code);
		hql.append("and main.type = :type ");
		hql.append("ORDER BY main.sort ASC");
		
		return list(hql.toString(), params);
	}
	
	@Override
	public List<ServiceProviderCategory> getUp(String id) {
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ServiceProviderCategory AS main, ServiceProviderCategory AS t2 WHERE 1 = 1 ");
		
		if(id == null || id.isEmpty()){
			hql.append("AND (t2.id is null OR t2.id='') ");			
		}else{
			params.put("id", id);
			hql.append("AND t2.id=:id ");
		}
		params.put("type", Constants.VendorType.SERVICE_PROVIDER.code);
		hql.append("and main.type = :type ");
		hql.append("AND (t2.parentId= main.parentId OR main.parentId is null AND t2.parentId is null) AND main.sort <= (t2.sort-1) ");
		hql.append("ORDER BY main.sort DESC");
		
		return list(hql.toString(), params);
	}

	@Override
	public List<ServiceProviderCategory> getDown(String id) {
		StringBuilder hql = new StringBuilder("SELECT main FROM ServiceProviderCategory AS main, ServiceProviderCategory AS t2 WHERE 1 = 1 ");
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		if(id == null || id.isEmpty()){
			hql.append("AND (t2.id is null OR t2.id='') ");
		}else{
			params.put("id", id);
			hql.append("AND t2.id=:id ");
		}
		params.put("type", Constants.VendorType.SERVICE_PROVIDER.code);
		hql.append("and main.type = :type ");
		hql.append("AND (main.parentId is null AND t2.parentId is null OR t2.parentId = main.parentId) AND main.sort >= (t2.sort+1) ");
		hql.append("ORDER BY main.sort ASC");

		return list(hql.toString(), params);
	}
	
	
	@Override
	public List<ServiceProviderCategory> listChildrenRows(String id) {
		
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ServiceProviderCategory AS main where 1 = 1 ");
		
		if(id == null || id.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("id", id);
			hql.append("AND main.parentId=:id ");
		}
		params.put("type", Constants.VendorType.SERVICE_PROVIDER.code);
		hql.append("and main.type = :type ");
		hql.append("ORDER BY main.sort DESC");

		return list(hql.toString(), params);
	}

}
