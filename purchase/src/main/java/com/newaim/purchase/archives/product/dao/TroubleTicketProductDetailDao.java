package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.TroubleTicketProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TroubleTicketProductDetailDao extends JpaRepository<TroubleTicketProductDetail, String>, JpaSpecificationExecutor<TroubleTicketProductDetail> {

    /**
     * 通过troubleTicketId删除
     * @param troubleTicketId 成本id
     */
    void deleteByTroubleTicketId(String troubleTicketId);

    /**
     * 通过troubleTicketProductId删除
     * @param troubleTicketProductId 成本id
     */
    void deleteByTroubleTicketProductId(String troubleTicketProductId);

    /**
     * 获取该troubleTicked下所有TroubleTicketDetail
     * @param troubleTicketProductId
     * @return
     */
    List<TroubleTicketProductDetail> findDetailsByTroubleTicketProductId(String troubleTicketProductId);
}
