package com.newaim.purchase.flow.purchase.dao;

import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.entity.FlowSampleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlowSampleDetailDao extends JpaRepository<FlowSampleDetail, String>, JpaSpecificationExecutor<FlowSampleDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowSampleDetail> findDetailsByBusinessId(String businessId);

}
