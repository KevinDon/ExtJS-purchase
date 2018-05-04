package com.newaim.purchase.admin.account.dao;

import com.newaim.core.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.account.entity.Resource;

@Repository
public interface ResourceDao extends BaseDao<Resource, String>, ResourceDaoCustom{
	
	Resource findResourceById(String id);
	
}
