package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehousePlanDetailDao extends JpaRepository<WarehousePlanDetail, String>, JpaSpecificationExecutor<WarehousePlanDetail> {

    void deleteByWarehousePlanId(String warehousePlanId);

    /**
     * 根据业务ID查找明细结合
     * @param warehousePlanId 业务id
     * @return 明细集合
     */
    List<WarehousePlanDetail> findDetailsByWarehousePlanId(String warehousePlanId);

}
