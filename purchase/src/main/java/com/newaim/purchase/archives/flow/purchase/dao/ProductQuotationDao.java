package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.purchase.entity.ProductQuotation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductQuotationDao extends  BaseDao<ProductQuotation, String> {

    /**
     * 通过产品ID查找所有历史报价明细
     * @param productId
     * @return
     */
    List<ProductQuotation> findProductQuotationsByProductId(String productId);

    @Query("select t from ProductQuotation t where t.vendorId = :vendorId and t.creatorId = :creatorId and t.effectiveDate <= current_timestamp and t.validUntil >= current_timestamp and t.status = 1 and t.hold = 2 and exists (select 1 from Product p where t.productId = p.id and p.status = 1)")
    List<ProductQuotation> findAllByVendor(@Param("vendorId") String vendorId, @Param("creatorId") String creatorId);



    @Query("select t from ProductQuotation t where t.vendorId = :vendorId and t.creatorId = :creatorId " +
            "and t.effectiveDate <= current_timestamp and t.validUntil >= current_timestamp and t.status = 1 and t.hold = 2 " +
            "and exists (select 1 from Product p where t.productId = p.id and p.status = 1) " +
            "and t.id = (select max(q.id) from ProductQuotation q where q.vendorId = :vendorId and q.creatorId = :creatorId " +
            "and q.effectiveDate <= current_timestamp and q.validUntil >= current_timestamp  and q.status = 1 and q.hold = 2 " +
            "and q.productId = t.productId)")
    List<ProductQuotation> findAllLatestByVendor(@Param("vendorId") String vendorId, @Param("creatorId") String creatorId);

    /**
     * 挂起指定产品的新品开发
     * @param productIds
     */
    @Modifying
    @Query("UPDATE ProductQuotation t set hold = 1 where productId in ?1 and t.effectiveDate <= current_timestamp and t.validUntil >= current_timestamp")
    void suspendByProductIds(List<String> productIds);

    List<ProductQuotation> findByBusinessId(String businessId);
}
