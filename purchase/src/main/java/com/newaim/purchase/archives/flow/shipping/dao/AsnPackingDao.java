package com.newaim.purchase.archives.flow.shipping.dao;


import com.newaim.purchase.archives.flow.shipping.entity.AsnPacking;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnPackingDao extends JpaRepository<AsnPacking, String>, JpaSpecificationExecutor<AsnPacking> {

    /**
     * 通过asnId删除所有关联的明细
     * @param asnId
     */
    void deleteByAsnId(String asnId);

    /**
     * 根据asnId查找明细结合
     * @param asnId
     * @return 明细集合
     */
    List<AsnPacking> findPackingsByAsnId(String asnId);

}
