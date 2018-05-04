package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.TroubleTicketAsn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TroubleTicketAsnDao extends JpaRepository<TroubleTicketAsn, String>, JpaSpecificationExecutor<TroubleTicketAsn> {

    /**
     * 通过troubleTicketId删除
     * @param troubleTicketId 成本id
     */
    void deleteByTroubleTicketId(String troubleTicketId);

    /**
     * 获取该troubleTicked下所有TroubleTicketAsn
     * @param troubleTicketId
     * @return
     */
    List<TroubleTicketAsn> findTroubleTicketAsnsByTroubleTicketId(String troubleTicketId);
}
