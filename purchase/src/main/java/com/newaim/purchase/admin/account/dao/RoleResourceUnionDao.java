package com.newaim.purchase.admin.account.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.account.entity.RoleResourceUnion;

@Repository
public interface RoleResourceUnionDao extends JpaRepository<RoleResourceUnion, String>, JpaSpecificationExecutor<RoleResourceUnion>{

	RoleResourceUnion findResourceById(String id);
	
	List<RoleResourceUnion> findRoleResourceUnionByRoleId(String roleId);
	
	List<RoleResourceUnion> findRoleResourceUnionByResourceId(String resourceId);

	void deleteRoleResourceUnionByRoleId(String roleId);
	
	void deleteRoleResourceUnionByResourceId(String resourceId);
	
//	@Query ("SELECT main FROM RoleResourceUnion AS main WHERE main.parentId is null ORDER BY sort ")
//	List<RoleResourceUnion> listAllWithRoleId();
//	
//	@Query ("SELECT main FROM RoleResourceUnion AS main LEFT JOIN main.roles t2 ON t2.roleId = :roleId OR t2.roleId is NULL WHERE t2.roleId = :roleId AND main.parentId = :parentId")
//	List<RoleResourceUnion> listAllWithRoleId(@Param("roleId") String roleId, @Param("parentId") String parentId );
	
	@Query ("SELECT t2 FROM RoleResourceUnion t2 WHERE t2.model = :model AND t2.action = :action")
	List<RoleResourceUnion> findRoleResourceUnionByModelAndAction(@Param("model") String model, @Param("action") String action);
}
