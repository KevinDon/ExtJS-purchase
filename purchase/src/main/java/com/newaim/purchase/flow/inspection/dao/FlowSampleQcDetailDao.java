package com.newaim.purchase.flow.inspection.dao;

import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlowSampleQcDetailDao extends JpaRepository<FlowSampleQcDetail, String>, JpaSpecificationExecutor<FlowSampleQcDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowSampleQcDetail> findDetailsByBusinessId(String businessId);
}
