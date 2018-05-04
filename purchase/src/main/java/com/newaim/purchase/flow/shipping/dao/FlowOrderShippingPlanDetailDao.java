package com.newaim.purchase.flow.shipping.dao;



import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowOrderShippingPlanDetailDao extends JpaRepository<FlowOrderShippingPlanDetail, String>, JpaSpecificationExecutor<FlowOrderShippingPlanDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowOrderShippingPlanDetail> findDetailsByBusinessId(String businessId);

    /**
     * 通过订单id查询第一条发货计划明细
     * @param orderId 订单id
     * @return 发货计划明细
     */
    FlowOrderShippingPlanDetail findTopByOrderId(String orderId);

}
