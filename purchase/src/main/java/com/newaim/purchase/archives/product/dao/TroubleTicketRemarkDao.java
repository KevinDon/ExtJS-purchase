package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.TroubleTicketRemark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TroubleTicketRemarkDao extends JpaRepository<TroubleTicketRemark, String>, JpaSpecificationExecutor<TroubleTicketRemark> {

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
    @Query("select t from TroubleTicketRemark t where t.troubleTicketId = ?1 order by t.createdAt")
    List<TroubleTicketRemark> findRemarksByTroubleTicketId(String troubleTicketId);
}
