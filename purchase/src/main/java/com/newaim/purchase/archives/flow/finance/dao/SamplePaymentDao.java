package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.finance.entity.SamplePayment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SamplePaymentDao extends BaseDao<SamplePayment, String> {

    /**
     * 挂起指定订单的费用支付
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_sample_payment t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_sample_payment_detail d where d.sample_payment_id = t.id and d.product_id in ?1)")
    void suspendByProductIds(List<String> productIds);

    List<SamplePayment> findByBusinessId(String businessId);
}
