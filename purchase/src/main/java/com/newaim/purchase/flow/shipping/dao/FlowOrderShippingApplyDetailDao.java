package com.newaim.purchase.flow.shipping.dao;

import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApplyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowOrderShippingApplyDetailDao extends JpaRepository<FlowOrderShippingApplyDetail, String>, JpaSpecificationExecutor<FlowOrderShippingApplyDetail> {

    /**
     * 根据业务id删除数据
     * @param businessId
     */
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowOrderShippingApplyDetail> findDetailsByBusinessId(String businessId);

}
