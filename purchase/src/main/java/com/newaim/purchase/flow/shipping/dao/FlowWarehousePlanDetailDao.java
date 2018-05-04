package com.newaim.purchase.flow.shipping.dao;

import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowWarehousePlanDetailDao extends JpaRepository<FlowWarehousePlanDetail, String>, JpaSpecificationExecutor<FlowWarehousePlanDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowWarehousePlanDetail> findDetailsByBusinessId(String businessId);

    List<FlowWarehousePlanDetail> findDetailsByContainerNumber(String containerNumber);
}
