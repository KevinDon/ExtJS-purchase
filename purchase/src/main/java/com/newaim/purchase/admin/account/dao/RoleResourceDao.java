package com.newaim.purchase.admin.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.account.entity.RoleResource;

@Repository
public interface RoleResourceDao extends JpaRepository<RoleResource, String>, JpaSpecificationExecutor<RoleResource> {

	void deleteRoleResourceByRoleId(String roleId);
}
