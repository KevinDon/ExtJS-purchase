package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostProductDao extends JpaRepository<CostProduct, String>, JpaSpecificationExecutor<CostProduct> {

    /**
     * 删除成本計算下所有產品
     * @param costId 成本id
     */
    void deleteByCostId(String costId);

    /**
     * 获取该成本下所有产品
     * @param costId 成本id
     * @return
     */
    List<CostProduct> findCostProductsByCostId(String costId);
    
    List<CostProduct> findCostProductsByCostIdAndOrderId(String costId,String orderId);

    CostProduct findCostProductByOrderId(String orderId);
}
