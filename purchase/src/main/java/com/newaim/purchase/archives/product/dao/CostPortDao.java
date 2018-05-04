package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.CostPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostPortDao extends JpaRepository<CostPort, String>, JpaSpecificationExecutor<CostPort> {

    /**
     * 删除成本計算下所有海运费
     * @param costId 成本id
     */
    void deleteByCostId(String costId);

    /**
     * 获取该成本下所有海运费
     * @param costId 成本id
     * @return
     */
    List<CostPort> findCostPortsByCostId(String costId);
}
