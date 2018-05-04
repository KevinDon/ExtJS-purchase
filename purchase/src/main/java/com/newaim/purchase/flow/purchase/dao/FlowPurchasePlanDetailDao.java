package com.newaim.purchase.flow.purchase.dao;


import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlowPurchasePlanDetailDao extends JpaRepository<FlowPurchasePlanDetail, String>, JpaSpecificationExecutor<FlowPurchasePlanDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowPurchasePlanDetail> findDetailsByBusinessId(String businessId);

    /**
     * 通过产品ID查找所有历史报价明细
     * @param productId
     * @return
     */
    List<FlowPurchasePlanDetail> findDetailsByProductId(String productId);

    /**
     * 挂起指定产品的采购计划
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_purchase_plan_detail t set hold = 1 where t.product_id in ?1 " +
            "and (EXISTS (select 1 from na_flow_purchase_plan pp where pp.id = t.business_id and pp.flow_status is null or pp.flow_status != 2) " +
            "or (t.order_qty > (select pd.already_order_qty from na_purchase_plan_detail pd,na_purchase_plan np where pd.purchase_plan_id = np.id and np.business_id = t.business_id and pd.product_id = t.product_id) " +
            "or EXISTS (select 1 from na_purchase_contract o, na_purchase_contract_detail d " +
            "where o.id = d.order_id and d.product_id = t.product_id and (o.flag_contract_deposit_status is null or o.flag_contract_deposit_status = 2))))")
    void suspendByProductIds(List<String> productIds);

}
