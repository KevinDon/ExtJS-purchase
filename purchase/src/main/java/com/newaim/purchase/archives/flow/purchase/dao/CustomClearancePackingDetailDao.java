package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomClearancePackingDetailDao extends JpaRepository<CustomClearancePackingDetail, String>, JpaSpecificationExecutor<CustomClearancePackingDetail> {

    /**
     * 通过装柜单ID删除所有关联的明细
     * @param packingId 业务ID
     */
    void deleteByPackingId(String packingId);

    /**
     * 根据装柜单ID查找明细结合
     * @param packingId
     * @return
     */
    List<CustomClearancePackingDetail> findPackingDetailsByPackingId(String packingId);

    /**
     * 根据装柜单ID查找明细结合
     * @param packingIds
     * @return
     */
    List<CustomClearancePackingDetail> findPackingDetailsByPackingIdIn(List<String> packingIds);

    List<CustomClearancePackingDetail> findPackingDetailsByProductId(String productId);

    CustomClearancePackingDetail findPackingDetailByPackingIdAndProductId(String packingId, String productId);

}
