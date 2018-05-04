package com.newaim.purchase.admin.account.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.admin.account.dao.DepartmentDaoCustom;
import com.newaim.purchase.admin.account.entity.Department;


public class DepartmentDaoImpl extends BaseDaoCustomImpl implements DepartmentDaoCustom{
	
	@Override
	public List<Department> findDepartmentByParentIdOrderBySort(String parentId){
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Department AS main WHERE 1 = 1 ");
		
		if(StringUtils.isBlank(parentId)){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("parentId", parentId);
			hql.append("AND main.parentId=:parentId ");
		}
		
		hql.append("ORDER BY main.sort");
		
		return list(hql.toString(), params);
	}
	
	@Override
	public List<Department> getUp(String id) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Department AS main, Department AS t2 WHERE 1 = 1 ");
		
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
	public List<Department> getDown(String id) {
		StringBuilder hql = new StringBuilder("SELECT main FROM Department AS main, Department AS t2 WHERE 1 = 1 ");
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
	public List<Department> listChildrenRows(String id) {
		
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Department AS main where 1 = 1 ");
		
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
