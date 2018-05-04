package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostTariffDao extends JpaRepository<CostTariff, String>, JpaSpecificationExecutor<CostTariff> {

    /**
     * 删除成本計算下所有成本关税
     * @param costId 成本id
     */
    void deleteByCostId(String costId);

    /**
     * 获取该成本下所有成本关税
     * @param costId 成本id
     * @return
     */
    List<CostTariff> findCostTariffByCostId(String costId);
}
