package com.newaim.purchase.flow.workflow.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.workflow.entity.FlowProcesses;
import com.newaim.purchase.flow.workflow.entity.MyAgentResource;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowProcessesDao extends BaseDao<FlowProcesses, String> {

	FlowProcesses findFlowProcessesById(String id);

	/**
	 * 查找所有已发布的最新版本的流程
	 * @return 流程
	 */
	@Query("select t1 from FlowProcesses t1 where t1.isPublish ='1' and (t1.code,t1.ver) in (select t2.code, max(t2.ver) as ver from FlowProcesses t2  where t2.isPublish ='1'  group by t2.code) ")
	List<FlowProcesses> lastestList();
	
}
