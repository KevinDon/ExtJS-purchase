package com.newaim.purchase.flow.shipping.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowAsnDao extends BaseDao<FlowAsn, String> {

    /**
     * 挂起指定订单的ASN
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE FlowAsn set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE FlowAsn set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    @Query("select t from FlowAsn t where t.flagSyncStatus = 2")
    List<FlowAsn> findAllCancelAsns();

    @Query("select t from FlowAsn t where t.flagSyncStatus = 1")
    List<FlowAsn> findAllCreateAsns();
    
    @Query("select t from FlowAsn t where t.hold = 2 and t.status = 1 and t.flowStatus = 1 and  t.flagCompleteStatus= 2")
    List<FlowAsn> findAllReceiveAsns();

    
    @Query("select t from FlowAsn t where t.asnNumber = ?1 and t.hold = 2 and t.status = 1 and (t.flowStatus is null or t.flowStatus = 0 or t.flowStatus = 1 or t.flowStatus = 2)")
    List<FlowAsn> findByAsnNumber(String asnNumber);
    
    @Query("select t from FlowAsn t where t.asnNumber = :asnNumber ")
    FlowAsn getByAsnNumber(@Param("asnNumber")String asnNumber);
    
    @Query("select t from FlowAsn t where t.orderId = :orderId and t.flowStatus = 2 ")
    List<FlowAsn> findPassFlowAsnByOrderId(@Param("orderId")String orderId);
}
