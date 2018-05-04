package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseContractDao extends BaseDao<PurchaseContract, String> {


    /**
     * 通过产品id查询相关(正式)采购合同
     * @param productId 产品id
     * @return 采购合同集合
     */
    @Query(value = "select t from PurchaseContract t where exists (select 1 from PurchaseContractDetail d where d.orderId = t.id and d.productId = :productId)")
    List<PurchaseContract> findByProductId(@Param("productId") String productId);

    /**
     * 通过产品id查询相关(正式)采购合同
     * @param productId 产品id
     * @return 采购合同集合
     */
    @Query(value = "select t from PurchaseContract t where exists (select 1 from PurchaseContractDetail d where d.orderId = t.id and d.productId = :productId)")
    Page<PurchaseContract> findByProductId(@Param("productId") String productId, Pageable pageable);

    /**
     * 查找所有未支付的有效订单
     * @return
     */
    @Query("select t from PurchaseContract t where t.status = 1 and t.hold = 2 and t.flagFeePaymentStatus = 2")
    List<PurchaseContract> findAllNotPaymentOrders();

    /**
     * 根据产品id获取所有未冻结的订单
     * @param productIds
     * @return
     */
    @Query(nativeQuery = true, value = "select t.* from na_purchase_contract t where (hold = 2 or hold is null) and EXISTS \n" +
            "(SELECT 1 from na_purchase_contract_detail d where d.order_id = t.id and d.product_id in ?1)")
    List<PurchaseContract> findAllUnsuspendByProductIds(List<String> productIds);

    /**
     * 挂起指定产品的采购合同
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_purchase_contract t set hold = 1 where t.flag_asn_status = 2 and EXISTS\n" +
            "(SELECT 1 from na_purchase_contract_detail d where d.order_id = t.id and d.product_id in ?1)")
    void suspendByProductIds(List<String> productIds);


    /**
     * 通过流程业务id查找订单
     * @param businessId
     * @return
     */
    PurchaseContract findLastByBusinessId(String businessId);

    @Query("select t from PurchaseContract t where exists (select 1 from PurchaseContractDetail d " +
            "where d.orderId = t.id and d.purchasePlanBusinessId = ?1)")
    List<PurchaseContract> findByPurchasePlanBusinessId(String purchasePlanBusinessId);


    List<PurchaseContract> findByVendorId(String vendorId);
}
