package com.newaim.purchase.archives.product.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.archives.product.dao.ProductCategoryDaoCustom;
import com.newaim.purchase.archives.product.entity.ProductCategory;

import java.util.List;
import java.util.Map;


public class ProductCategoryDaoImpl extends BaseDaoCustomImpl implements ProductCategoryDaoCustom {
	
	@Override
	public List<ProductCategory> findProductCategoryByParentId(String parentId, Integer status){
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ProductCategory AS main WHERE 1 = 1 ");
		
		if(parentId == null || parentId.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("parentId", parentId);
			hql.append("AND main.parentId=:parentId ");
		}
		if(status != null){
			params.put("status", status);
			hql.append(" and main.status = :status ");
		}
		hql.append("ORDER BY main.sort ASC");

		return list(hql.toString(), params);
	}

	@Override
	public List<ProductCategory> getUp(String id) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ProductCategory AS main, ProductCategory AS t2 WHERE 1 = 1 ");
		
		if(id == null || id.isEmpty()){
			hql.append("AND (t2.id is null OR t2.id='') ");			
		}else{
			params.put("id", id);
			hql.append("AND t2.id=:id ");
		}
		
		hql.append("AND (t2.parentId= main.parentId OR main.parentId is null AND t2.parentId is null) AND main.sort <= (t2.sort-1) ");
		hql.append("ORDER BY main.sort DESC");
		
		return list(hql.toString(), params);
	}

	@Override
	public List<ProductCategory> getDown(String id) {
		StringBuilder hql = new StringBuilder("SELECT main FROM ProductCategory AS main, ProductCategory AS t2 WHERE 1 = 1 ");
		Map<String, Object> params = Maps.newHashMap();
			if(id == null || id.isEmpty()){
				hql.append("AND (t2.id is null OR t2.id='') ");
			}else{
				params.put("id", id);
				hql.append("AND t2.id=:id ");
			}
			
			hql.append("AND (main.parentId is null AND t2.parentId is null OR t2.parentId = main.parentId) AND main.sort >= (t2.sort+1) ");
			hql.append("ORDER BY main.sort ASC");
			
			return list(hql.toString(), params);
	}
	
	
	@Override
	public List<ProductCategory> listChildrenRows(String id) {
		
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM ProductCategory AS main where 1 = 1 ");
		
		if(id == null || id.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("id", id);
			hql.append("AND main.parentId=:id ");
		}
		
		hql.append("ORDER BY main.sort DESC");

		return list(hql.toString(), params);
	}

}
