package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnDao extends BaseDao<Asn, String> {
    /**
     * 挂起指定订单的ASN
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE Asn set hold = 1 where  orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE Asn set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    @Query("select t from Asn t where t.asnNumber = ?1 and t.hold = 2 and t.status = 1")
    List<Asn> findByAsnNumber(String asnNumber);
}
