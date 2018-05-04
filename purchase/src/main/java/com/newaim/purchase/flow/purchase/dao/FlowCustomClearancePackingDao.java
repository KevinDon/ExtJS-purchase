package com.newaim.purchase.flow.purchase.dao;

import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePacking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowCustomClearancePackingDao extends JpaRepository<FlowCustomClearancePacking, String>, JpaSpecificationExecutor<FlowCustomClearancePacking> {

    /**
     * 通过业务ID删除所有关联的明细
     * @param businessId 业务ID
     */
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowCustomClearancePacking> findPackingsByBusinessId(String businessId);

    /**
     * 挂起指定订单的清关申请
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE FlowCustomClearancePacking set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE FlowCustomClearancePacking set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);
}
