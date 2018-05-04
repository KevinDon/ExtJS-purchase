package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.purchase.entity.NewProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NewProductDao extends  BaseDao<NewProduct, String> {
    /**
     * 挂起指定产品的新品开发
     * @param productIds
     */
    @Modifying
    @Query("UPDATE NewProduct set hold = 1 where productId in ?1")
    void suspendByProductIds(List<String> productIds);
}
