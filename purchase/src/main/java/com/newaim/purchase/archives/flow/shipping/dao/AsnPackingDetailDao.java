package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.AsnPackingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnPackingDetailDao extends JpaRepository<AsnPackingDetail, String>, JpaSpecificationExecutor<AsnPackingDetail> {

    void deleteByAsnPackingId(String asnPackingId);

    /**
     * 根据业务ID查找明细结合
     * @param asnPackingId
     * @return 明细集合
     */
    List<AsnPackingDetail> findPackingDetailsByAsnPackingId(String asnPackingId);

    List<AsnPackingDetail> findPackDetailsByAsnId(String asnId);

    @Query("select t from AsnPackingDetail t where t.orderId = :orderId")
    List<AsnPackingDetail> findPackDetailsByOrderId(@Param("orderId") String orderId);
}
