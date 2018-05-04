package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.TroubleTicketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TroubleTicketProductDao extends JpaRepository<TroubleTicketProduct, String>, JpaSpecificationExecutor<TroubleTicketProduct> {

    /**
     * 通过troubleTicketId删除
     * @param troubleTicketId 成本id
     */
    void deleteByTroubleTicketId(String troubleTicketId);

    /**
     * 获取该troubleTicked下所有TroubleTicketDetail
     * @param troubleTicketId
     * @return
     */
    List<TroubleTicketProduct> findDetailsByTroubleTicketId(String troubleTicketId);
}
