package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.purchase.entity.Sample;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDao extends BaseDao<Sample, String> {
    /**
     * 挂起指定产品的样品申请, 冻结未付款的
     * @param productIds
     */
    @Modifying
    @Query("UPDATE Sample t set hold = 1 where exists (select 1 from SampleDetail d where d.sampleId = t.id and d.productId in ?1) " +
            "and not exists (select 1 from SamplePayment sp where sp.sampleId = t.id)")
    void suspendByProductIds(List<String> productIds);
}
