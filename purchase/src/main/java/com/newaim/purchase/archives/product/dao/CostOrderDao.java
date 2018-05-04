package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostOrder;
import com.newaim.purchase.archives.product.entity.CostProductCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostOrderDao extends JpaRepository<CostOrder, String>, JpaSpecificationExecutor<CostOrder> {

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
    List<CostOrder> findCostOrderByCostId(String costId);
   CostOrder  findCostOrderByOrderId(String orderId);

}
