package com.newaim.purchase.admin.account.dao;

import com.newaim.purchase.admin.account.entity.Department;

import java.util.List;

public interface DepartmentDaoCustom {
	
	List<Department> findDepartmentByParentIdOrderBySort(String parentId);

	List<Department> getUp(String id);
	
	List<Department> getDown(String id);
	
	List<Department> listChildrenRows(String id);
}
