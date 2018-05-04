package com.newaim.purchase.flow.workflow.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.flow.workflow.entity.MyAgentResource;

@Repository
public interface MyAgentResourceDao extends JpaRepository<MyAgentResource, String>, JpaSpecificationExecutor<MyAgentResource> {

	void deleteMyAgentResourceByBusinessId(String businessId);
	
	/**
	 * 通过代理人id查找所有有效的授权
	 * @param userId 代理人id
	 * @param  date 时间节点
	 * @return 代理权限列表
	 */
	@Query("select t from MyAgentResource t where t.userId = :userId and t.fromTime <= :date and t.toTime >= :date ")
	List<MyAgentResource> findMyAgentResourcesByUserId(@Param("userId") String userId, @Param("date") Date date);
}
