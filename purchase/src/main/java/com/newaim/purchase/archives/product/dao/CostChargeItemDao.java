package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostChargeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostChargeItemDao extends JpaRepository<CostChargeItem, String>, JpaSpecificationExecutor<CostChargeItem> {

    /**
     * 删除成本計算下所有本地费用
     * @param costId 成本id
     */
    void deleteByCostId(String costId);

    /**
     * 获取该成本下所有本地费用
     * @param costId 成本id
     * @return
     */
    List<CostChargeItem> findCostChargeItemsByCostId(String costId);
}
