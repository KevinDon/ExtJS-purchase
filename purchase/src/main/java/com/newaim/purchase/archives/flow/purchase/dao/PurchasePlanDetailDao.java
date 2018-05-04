package com.newaim.purchase.archives.flow.purchase.dao;


import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasePlanDetailDao extends JpaRepository<PurchasePlanDetail, String>, JpaSpecificationExecutor<PurchasePlanDetail> {

    @Query("select t from PurchasePlanDetail t where exists (select 1 from PurchaseContractDetail od " +
            " where od.purchasePlanDetailId = t.id and od.orderId = :orderId and od.productId = :productId) " +
            " order by t.priceAud asc ")
    List<PurchasePlanDetail> findByOrderIdAndProductId(@Param("orderId") String orderId, @Param("productId") String productId);

    /**
     * 挂起指定产品的采购计划
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_purchase_plan_detail t set hold = 1 where t.product_id in ?1 " +
            "and (t.order_qty > t.already_order_qty or EXISTS (select 1 from na_purchase_contract o, na_purchase_contract_detail d " +
            "where o.id = d.order_id and d.product_id = t.product_id and (o.flag_contract_deposit_status is null or o.flag_contract_deposit_status = 2)))")
    void suspendByProductIds(List<String> productIds);
}
