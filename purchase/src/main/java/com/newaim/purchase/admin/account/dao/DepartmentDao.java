package com.newaim.purchase.admin.account.dao;

import com.newaim.core.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.account.entity.Department;

@Repository
public interface DepartmentDao extends BaseDao<Department, String>, DepartmentDaoCustom{
	
	Department findDepartmentById(String id);
	
}
