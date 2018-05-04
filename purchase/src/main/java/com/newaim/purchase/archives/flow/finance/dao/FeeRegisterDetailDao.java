package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.purchase.archives.flow.finance.entity.FeeRegisterDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRegisterDetailDao extends JpaRepository<FeeRegisterDetail, String>, JpaSpecificationExecutor<FeeRegisterDetail> {

    void deleteByFeeRegisterId(String feeRegisterId);

    /**
     * 根据业务ID查找明细结合
     * @param feeRegisterId 业务id
     * @return 明细集合
     */
    List<FeeRegisterDetail> findDetailsByFeeRegisterId(String feeRegisterId);


    List<FeeRegisterDetail> findDetailsByFeeRegisterIdAndApplyCost(String feeRegisterId, Integer applyCost);


    List<FeeRegisterDetail> findDetailsByFeeRegisterIdAndType(String feeRegisterId, Integer type);
}
