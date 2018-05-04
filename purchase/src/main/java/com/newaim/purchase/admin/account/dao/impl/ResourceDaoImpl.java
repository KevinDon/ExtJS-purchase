package com.newaim.purchase.admin.account.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.admin.account.dao.ResourceDaoCustom;
import com.newaim.purchase.admin.account.entity.Resource;


public class ResourceDaoImpl extends BaseDaoCustomImpl implements ResourceDaoCustom{
	
	@Override
	public List<Resource> findResourceByParentIdOrderBySortAsc(String parentId){
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Resource AS main WHERE 1 = 1 ");
		
		if(parentId == null || parentId.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("parentId", parentId);
			hql.append("AND main.parentId=:parentId ");
		}
		
		hql.append("ORDER BY main.sort ASC");
		
		return list(hql.toString(), params);
	}

	@Override
	public List<Resource> findResourceByParentIdOrderBySortAsc(String parentId, List<String> roleIds){
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		List<Resource> list = Lists.newArrayList();
		StringBuilder hql = new StringBuilder();
		
		hql = new StringBuilder("SELECT DISTINCT main.* FROM na_account_resource main ");
		
		hql.append("LEFT JOIN na_account_role_resource AS T1 ON position(main.id in t1.path)>0 ");
		hql.append("WHERE main.status=1 ");
		if(roleIds!=null && roleIds.size()>0){
			hql.append(" AND t1.role_id in (:roleIds) ");
			params.put("roleIds", roleIds);
		}
		
		if(parentId == null || parentId.isEmpty()){
			hql.append("AND (main.parent_id is null OR main.parent_id='') ");
		}else{
			params.put("parentId", parentId);
			hql.append("AND main.parent_id=:parentId ");
		}
		hql.append("ORDER BY main.sort ASC");
		
		List<Object> rows = listBySql(hql.toString(), params);
		for(int i=0;i<rows.size();i++){
			Object[] objects = (Object[])rows.get(i);
			Resource rs = new Resource();
			rs.setId(objects[0].toString());
			if(objects[1] == null) {
				rs.setParentId(null);
			} else {
				rs.setParentId(objects[1].toString());
			}
			rs.setCnName(objects[2].toString());
			rs.setEnName(objects[3].toString());
			rs.setLeaf(Integer.parseInt(objects[4].toString()));
			rs.setLevel(Integer.parseInt(objects[5].toString()));
			rs.setType(Integer.parseInt(objects[6].toString()));
			if(objects[7] == null) {
				rs.setUrl(null);
			} else {
				rs.setUrl(objects[7].toString());
			}
			rs.setStatus(Integer.parseInt(objects[8].toString()));
			rs.setSort(Integer.parseInt(objects[9].toString()));
			if(objects[10] == null) {
				rs.setItemIcon(null);
			}
			rs.setItemIcon(objects[10].toString());
			if(objects[12] == null) {
				rs.setCode(null);
			} else {
				rs.setCode(objects[12].toString());
			}
			rs.setChildren(findChildrenResourceByParentId(rs.getId(), roleIds));
			list.add(rs);
		}
				
		return list;
	}
	
	public List<Resource> findChildrenResourceByParentId(String parentId, List<String> roleIds){
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		List<Resource> list = Lists.newArrayList();
		StringBuilder hql = new StringBuilder();

		if(StringUtils.isBlank(parentId) || StringUtils.isEmpty(parentId) ){ return list;}
		
		hql = new StringBuilder("SELECT DISTINCT main.* FROM na_account_resource main ");
		
		hql.append("LEFT JOIN na_account_role_resource AS T1 ON position(main.id in t1.path)>0 ");
		hql.append("WHERE main.status=1 ");
		
		hql.append(" AND t1.role_id in (:roleIds) ");
		params.put("roleIds", roleIds);
		
		params.put("parentId", parentId);
		hql.append("AND main.parent_id=:parentId ");
		
		hql.append("ORDER BY main.sort ASC");
				
		List<Object> rows = listBySql(hql.toString(), params);
		for(int i=0;i<rows.size();i++){
			Object[] objects = (Object[])rows.get(i);
			Resource rs = new Resource();
			rs.setId(objects[0].toString());
			if(objects[1] == null) {
				rs.setParentId(null);
			} else {
				rs.setParentId(objects[1].toString());
			}
			rs.setCnName(objects[2].toString());
			rs.setEnName(objects[3].toString());
			rs.setLeaf(Integer.parseInt(objects[4].toString()));
			rs.setLevel(Integer.parseInt(objects[5].toString()));
			rs.setType(Integer.parseInt(objects[6].toString()));
			if(objects[7] == null) {
				rs.setUrl(null);
			} else {
				rs.setUrl(objects[7].toString());
			}
			rs.setStatus(Integer.parseInt(objects[8].toString()));
			rs.setSort(Integer.parseInt(objects[9].toString()));
//			if(StringUtils.isAnyBlank((String) objects[10])) rs.setItemIcon(null); rs.setItemIcon(objects[10].toString());
			if(objects[12] == null) {
				rs.setCode(null);
			} else {
				rs.setCode(objects[12].toString());
			}
			rs.setChildren(findChildrenResourceByParentId(rs.getId(), roleIds));
			list.add(rs);
		}
		
		return list;
	}
	
	@Override
	public List<Resource> getUp(String id) {
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Resource AS main, Resource AS t2 WHERE 1 = 1 ");
		
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
	public List<Resource> getDown(String id) {
		StringBuilder hql = new StringBuilder("SELECT main FROM Resource AS main, Resource AS t2 WHERE 1 = 1 ");
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
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
	public List<Resource> listChildrenRows(String id) {
		
		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Resource AS main where 1 = 1 ");
		
		if(id == null || id.isEmpty()){
			hql.append("AND (main.parentId is null OR main.parentId='') ");
		}else{
			params.put("id", id);
			hql.append("AND main.parentId=:id ");
		}
		
		hql.append("ORDER BY main.sort DESC");

		return list(hql.toString(), params);
	}

	@Override
	public List<Resource> getParentPath(String id) {

		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
		StringBuilder hql = new StringBuilder("SELECT main FROM Resource AS main where 1 = 1 ");
		
		if(StringUtils.isNotBlank(id)){
			params.put("id", id);
			hql.append("AND main.id=:id ");
		}
		
		return list(hql.toString(), params);
	}

}
