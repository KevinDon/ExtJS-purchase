package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.purchase.archives.flow.finance.entity.SamplePaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SamplePaymentDetailDao extends JpaRepository<SamplePaymentDetail, String>, JpaSpecificationExecutor<SamplePaymentDetail> {

    /**
     * 根据业务ID查找明细结合
     * @param samplePaymentId 业务id
     * @return 明细集合
     */
    List<SamplePaymentDetail> findDetailsBySamplePaymentId(String samplePaymentId);

}
