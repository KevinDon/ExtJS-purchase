package com.newaim.purchase.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FlowNewProductDao extends  BaseDao<FlowNewProduct, String> {
    /**
     * 挂起指定产品的新品开发
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_new_product t set hold = 1 where (t.flow_status is null or t.flow_status != 2) and EXISTS\n" +
            "(SELECT 1 from na_flow_new_product_detail d where d.business_id = t.id and d.product_id in ?1)")
    void suspendByProductIds(List<String> productIds);

    @Query(nativeQuery = true, value = "select t.* from na_flow_new_product t where (t.status = 0 or t.status = 1) and exists\n" +
            " (select 1 from na_flow_new_product_detail d where d.business_id = t.id and d.product_quotation_business_id = :productQuotationBusinessId )")
    List<FlowNewProduct> findByProductQuotationBusinessId(@Param("productQuotationBusinessId") String productQuotationBusinessId);

}
