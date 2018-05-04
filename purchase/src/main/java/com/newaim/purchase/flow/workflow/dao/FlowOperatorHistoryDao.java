package com.newaim.purchase.flow.workflow.dao;

import com.newaim.purchase.flow.workflow.entity.FlowOperatorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mark on 2017/10/10.
 */
@Repository
public interface FlowOperatorHistoryDao extends JpaRepository<FlowOperatorHistory, String>, JpaSpecificationExecutor<FlowOperatorHistory> {

    List<FlowOperatorHistory> findByBusinessId(String businessId);
}
