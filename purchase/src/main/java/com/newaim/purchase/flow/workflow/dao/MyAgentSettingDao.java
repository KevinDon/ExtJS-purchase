package com.newaim.purchase.flow.workflow.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.workflow.entity.MyAgentResource;
import com.newaim.purchase.flow.workflow.entity.MyAgentSetting;
import com.newaim.purchase.flow.workflow.vo.MyAgentSettingVo;

@Repository
public interface MyAgentSettingDao extends BaseDao<MyAgentSetting, String> {

	MyAgentSetting findMyAgentSettingById(String id);

	List<MyAgentSetting> findMyAgentSettingByFromUserId(String fromUserId);
	
	List<MyAgentSetting> findMyAgentSettingByToUserId(String toUserId);


	
	@Query("select t from MyAgentSetting t where t.status=1 and t.fromUserId = :fromUserId and t.fromTime <= :date and t.toTime >= :date order by t.createdAt desc")
	List<MyAgentSetting> findAllowedMyAgentSettingByFromUserId(@Param("fromUserId") String fromUserId, @Param("date") Date date);
	
	@Query("select t from MyAgentSetting t where t.status=1 and t.toUserId = :toUserId and t.fromTime <= :date and t.toTime >= :date order by t.createdAt desc")
	List<MyAgentSetting> findAllowedMyAgentSettingByToUserId(@Param("toUserId") String toUserId, @Param("date") Date date);
	
	@Query("select t from MyAgentSetting t where t.status=1 and ( t.flowCode = :flowCode or t.flowCode='ALL') and t.fromUserId = :fromUserId and t.fromTime <= :date and t.toTime >= :date order by t.createdAt desc")
	List<MyAgentSetting> findAllowedMyAgentSettingByFromUserId(@Param("fromUserId") String fromUserId,@Param("flowCode") String flowCode, @Param("date") Date date);
	
	@Query("select t from MyAgentSetting t where t.status=1 and ( t.flowCode = :flowCode or t.flowCode='ALL') and t.toUserId = :toUserId and t.fromTime <= :date and t.toTime >= :date order by t.createdAt desc")
	List<MyAgentSetting> findAllowedMyAgentSettingByToUserId(@Param("toUserId") String toUserId,@Param("flowCode") String flowCode, @Param("date") Date date);
	
	@Query("select t1 from MyAgentSetting t1,MyAgentSetting t2  where t1.fromUserId = t2.fromUserId and t1.toUserId = t2.toUserId and t1.status = t2.status "
+ "and  t1.fromTime = t2.fromTime and t1.toTime = t2.toTime and t1.creatorId = t2.creatorId and t2.id = :id ")
	List<MyAgentSetting> findSameMyAgentSettingById(@Param("id") String id);
	
	
	/**
	 * 获取去重后的所有记录的MyAgentSettingId
	 * @param 
	 * @return List<MyAgentSetting>
	 */
	@Query("select min(t.id) as id from MyAgentSetting t group by t.toUserId,t.fromUserId,t.fromTime,t.toTime,t.creatorId")
	List<String> finDistinctMyAgentSettingIds();
	
}
