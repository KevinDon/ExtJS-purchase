package com.newaim.purchase.flow.finance.dao;

import com.newaim.purchase.flow.finance.entity.FlowFeeRegisterDetail;
import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowFeeRegisterDetailDao extends JpaRepository<FlowFeeRegisterDetail, String>, JpaSpecificationExecutor<FlowFeeRegisterDetail> {

    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowFeeRegisterDetail> findDetailsByBusinessId(String businessId);

    List<FlowFeeRegisterDetail> findDetailsByBusinessIdAndType(String businessId, Integer type);
}
