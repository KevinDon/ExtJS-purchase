package com.newaim.purchase.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowPurchaseContractDao extends BaseDao<FlowPurchaseContract, String> {


    /**
     * 通过产品id查询相关采购合同
     * @param productId 产品id
     * @return 采购合同集合
     */
    @Query("select t from FlowPurchaseContract t where exists (select 1 from FlowPurchaseContractDetail d where d.businessId = t.id and d.productId = :productId)")
    List<FlowPurchaseContract> findByProductId(@Param("productId") String productId);

    /**
     * 挂起指定产品的采购合同
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_purchase_contract t set hold = 1 where EXISTS\n" +
            " (SELECT 1 from na_flow_purchase_contract_detail d where d.business_id = t.id and d.product_id in ?1)\n" +
            " and Not EXISTS(SELECT 1 from na_purchase_contract o where o.business_id = t.id and o.flag_asn_status = 1)")
    void suspendByProductIds(List<String> productIds);

    @Query("select max(t.orderNumber) from FlowPurchaseContract t where t.orderNumber like concat(:orderNumber, '%')")
    String findMaxOrderNumber(@Param("orderNumber") String orderNumber);

    @Query(nativeQuery = true, value = "select t.* from na_flow_purchase_contract t where (t.status = 0 or t.status = 1) and exists(\n" +
            "  select 1 from na_flow_purchase_contract_detail d where d.business_id = t.id and d.purchase_plan_business_id = :purchasePlanBusinessId\n" +
            ")")
    List<FlowPurchaseContract> findByProductPurchasePlanBusinessId(@Param("purchasePlanBusinessId") String purchasePlanBusinessId);
}
