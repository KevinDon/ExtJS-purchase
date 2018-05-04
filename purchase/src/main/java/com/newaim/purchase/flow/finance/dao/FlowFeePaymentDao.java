package com.newaim.purchase.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.finance.entity.FlowFeePayment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowFeePaymentDao extends BaseDao<FlowFeePayment, String> {

    /**
     * 挂起指定订单的费用支付
     * @param orderIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_fee_payment t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_fee_register r where r.id = t.fee_register_id and r.order_id in ?1)")
    void suspendByOrderIds(List<String> orderIds);

    /**
     * 挂起指定订单的费用支付
     * @param orderIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_fee_payment t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_fee_register r where r.id = t.fee_register_id and r.order_id in (select distinct o.id from na_purchase_contract o,na_purchase_contract_detail d where o.id = d.order_id and d.product_id in ?1 and o.flag_asn_status = 2))")
    void suspendByProductIds(List<String> orderIds);
}
