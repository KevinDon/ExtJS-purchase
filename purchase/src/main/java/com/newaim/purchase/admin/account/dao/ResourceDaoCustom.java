package com.newaim.purchase.admin.account.dao;

import com.newaim.purchase.admin.account.entity.Resource;

import java.util.List;

public interface ResourceDaoCustom {
	
	List<Resource> findResourceByParentIdOrderBySortAsc(String parentId);

	List<Resource> findResourceByParentIdOrderBySortAsc(String parentId, List<String> roleIds);
	
	List<Resource> getUp(String id);
	
	List<Resource> getDown(String id);
	
	List<Resource> listChildrenRows(String id);
	
	List<Resource> getParentPath(String id);
}
