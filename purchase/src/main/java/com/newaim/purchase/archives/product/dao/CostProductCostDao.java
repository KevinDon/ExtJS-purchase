package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostProductCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostProductCostDao extends JpaRepository<CostProductCost, String>, JpaSpecificationExecutor<CostProductCost> {

    /**
     * 删除成本計算下所有產品成本
     * @param costId 成本id
     */
    void deleteByCostId(String costId);

    /**
     * 获取该成本下所有产品成本
     * @param costId 成本id
     * @return
     */
    List<CostProductCost> findCostProductCostsByCostId(String costId);
    CostProductCost findCostProductCostByCostIdAndProductId(String costId,String productId);
}
