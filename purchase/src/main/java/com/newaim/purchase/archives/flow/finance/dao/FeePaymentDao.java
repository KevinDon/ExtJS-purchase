package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.finance.entity.FeePayment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeePaymentDao extends BaseDao<FeePayment, String> {


    /**
     * 挂起指定订单的费用支付
     * @param orderIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_fee_payment t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_fee_register r where r.id = t.fee_register_id and r.order_id in ?1)")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_fee_payment t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_fee_register r where r.id = t.fee_register_id and r.order_id in (select distinct o.id from na_purchase_contract o,na_purchase_contract_detail d where o.id = d.order_id and d.product_id in ?1 and o.flag_asn_status = 2))")
    void suspendByProductIds(List<String> orderIds);

    List<FeePayment> findByBusinessId(String businessId);

    @Query("select t from FeePayment t where exists (select 1 from FeeRegister r where r.id = t.feeRegisterId and r.status = 1 and r.orderId in(:orderIds))")
    List<FeePayment> findBalancesByOrderIds(@Param("orderIds") List<String> orderIds);
}
