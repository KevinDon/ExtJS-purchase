package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomClearancePackingDao extends JpaRepository<CustomClearancePacking, String>, JpaSpecificationExecutor<CustomClearancePacking> {

    /**
     * 通过customClearanceId删除所有关联的明细
     * @param customClearanceId 业务ID
     */
    void deleteByCustomClearanceId(String customClearanceId);

    /**
     * 根据customClearanceId查找明细结合
     * @param customClearanceId 业务id
     * @return 明细集合
     */
    List<CustomClearancePacking> findPackingsByCustomClearanceId(String customClearanceId);

    /**
     * 通过订单id查找装柜单
     * @param orderIds
     * @return
     */
    List<CustomClearancePacking> findPackingsByOrderIdIn(List<String> orderIds);

    /**
     * 通过订单id查找装柜单
     * @param orderId
     * @return
     */
    List<CustomClearancePacking> findPackingsByOrderId(String orderId);

    /**
     * 通过订单id查找查找所有未做ASN的装柜单
     * @param orderId
     * @return
     */
    @Query("select t from CustomClearancePacking t where t.orderId = ?1 and (t.flagAsnStatus = 2 or t.flagAsnStatus is null)")
    List<CustomClearancePacking> findNotAsnPackingsByOrderId(String orderId);

    /**
     * 通过订单id查找查找所有未做送仓计划的装柜单
     * @param orderId
     * @return
     */
    @Query("select t from CustomClearancePacking t where t.orderId = ?1 and (t.flagWarehousePlanStatus = 2 or t.flagWarehousePlanStatus is null)")
    List<CustomClearancePacking> findNotWarehousePlanPackingsByOrderId(String orderId);

    /**
     * 通过containerNumber找装柜单
     * @param containerNumber
     * @return
     */
    List<CustomClearancePacking> findPackingsByContainerNumber(String containerNumber);

    /**
     * 通过多个containerNumber找装柜单
     * @param containerNumbers
     * @return
     */
    List<CustomClearancePacking> findPackingsByContainerNumberIn(List<String> containerNumbers);

    /**
     * 挂起指定订单的清关申请
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE CustomClearancePacking set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE CustomClearancePacking set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

}
