package com.newaim.purchase.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.AsnPackingDetail;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlowAsnPackingDetailDao extends JpaRepository<FlowAsnPackingDetail, String>, JpaSpecificationExecutor<FlowAsnPackingDetail> {

    void deleteByAsnPackingId(String asnPackingId);

    /**
     * 根据业务ID查找明细结合
     * @param asnPackingId
     * @return 明细集合
     */
    List<FlowAsnPackingDetail> findPackingDetailsByAsnPackingId(String asnPackingId);

    List<FlowAsnPackingDetail> findByBusinessId(String businessId);

    /**
     * 获取asn明细
     * @param asnNumber
     * @param orderNumber
     * @param warehouseId
     * @param sku
     * @return
     */
    @Query("select t from FlowAsnPackingDetail t where exists (select 1 from FlowAsn a where a.asnNumber = :asnNumber and a.orderNumber = :orderNumber and a.warehouseId = :warehouseId and a.id = t.businessId and a.status=1 and (a.flowStatus = 0 or a.flowStatus = 1)) and t.sku = :sku")
    FlowAsnPackingDetail getFlowAsnPackingDetail(@Param("asnNumber") String asnNumber, @Param("orderNumber") String orderNumber, @Param("warehouseId") String warehouseId, @Param("sku") String sku);
    
    @Query("select t from FlowAsnPackingDetail t where t.businessId=:businessId and t.sku=:sku ")
    FlowAsnPackingDetail getFlowAsnPackingDetail(@Param("businessId") String businessId, @Param("sku") String sku);
    

    @Query("select t from FlowAsnPackingDetail t where t.orderId = :orderId")
    List<FlowAsnPackingDetail> findFlowAsnPackingDetailsByOrderId(@Param("orderId") String orderId);
}
