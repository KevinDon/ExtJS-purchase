package com.newaim.purchase.flow.purchase.dao;

import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowCustomClearancePackingDetailDao extends JpaRepository<FlowCustomClearancePackingDetail, String>, JpaSpecificationExecutor<FlowCustomClearancePackingDetail> {

    /**
     * 通过业务ID删除所有关联的明细
     * @param packingId 业务ID
     */
    void deleteByPackingId(String packingId);

    /**
     * 根据业务ID查找明细结合
     * @param packingId 业务id
     * @return 明细集合
     */
    List<FlowCustomClearancePackingDetail> findPackingDetailsByPackingId(String packingId);

}
