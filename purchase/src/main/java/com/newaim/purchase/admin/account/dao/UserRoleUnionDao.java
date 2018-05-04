package com.newaim.purchase.admin.account.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;

@Repository
public interface UserRoleUnionDao extends BaseDao<UserRoleUnion, String>, UserRoleUnionDaoCustom{

	UserRoleUnion findUserRoleUnionById(String id);
	
	List<UserRoleUnion> findUserRoleUnionByRoleId(String roleId);
	
	List<UserRoleUnion> findUserRoleUnionByUserId(String userId);

	void deleteUserRoleUnionByRoleId(String roleId);
	
	void deleteUserRoleUnionByUserId(String userId);

}
