package com.newaim.purchase.archives.flow.inspection.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.inspection.entity.SampleQc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleQcDao extends BaseDao<SampleQc, String> {

    /**
     * 挂起指定产品的样品质检
     * @param productIds
     */
    @Modifying
    @Query("UPDATE SampleQc set hold = 1 where  productId in ?1")
    void suspendByProductIds(List<String> productIds);
}
