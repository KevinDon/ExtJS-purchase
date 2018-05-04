package com.newaim.purchase.archives.flow.shipping.dao;


import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationChargeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderQuotationChargeItemDao extends JpaRepository<ServiceProviderQuotationChargeItem, String>, JpaSpecificationExecutor<ServiceProviderQuotationChargeItem> {

    /**
     * 通过报价id查找收费项目报价
     * @param serviceProviderQuotationId 报价id
     * @return 收费项目报价
     */
    List<ServiceProviderQuotationChargeItem> findByServiceProviderQuotationId(String serviceProviderQuotationId);

    /**
     * 通过服务商id和类型查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @return 港口报价
     */
    @Query(nativeQuery = true, value = "SELECT c.id,c.fee_type,c.item_id,c.item_cn_name,c.item_en_name,c.unit_id,c.unit_cn_name,c.unit_en_name,qc.*" +
            " FROM na_service_provider_charge_item c \n" +
            " LEFT JOIN na_service_provider_quotation q on c.service_provider_id = q.service_provider_id and q.type=:type \n" +
            "     and q.end_time = (select max(t.end_time) from na_service_provider_quotation t where t.service_provider_id = q.service_provider_id and t.type=q.type) \n" +
            " LEFT JOIN na_service_provider_quotation_charge_item qc on q.id = qc.service_provider_quotation_id and c.item_id = qc.item_id\n" +
            " WHERE c.service_provider_id = :serviceProviderId")
    List<ServiceProviderQuotationChargeItem> listChargeItems(@Param("serviceProviderId") String serviceProviderId, @Param("type") String type);

    /**
     * 通过服务商id、类型和发货计划id查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @param flowOrderShippingPlanId 发货计划id
     * @return 港口报价
     */
    @Query(nativeQuery = true, value = "SELECT c.id,c.fee_type,c.item_id,c.item_cn_name,c.item_en_name,c.unit_id,c.unit_cn_name,c.unit_en_name,qc.*" +
            " FROM na_service_provider_charge_item c \n" +
            " LEFT JOIN na_service_provider_quotation q on c.service_provider_id = q.flow_order_shipping_plan_id = :flowOrderShippingPlanId and q.type=:type \n" +
            "     and q.end_time = (select max(t.end_time) from na_service_provider_quotation t where t.service_provider_id = p.service_provider_id and t.type=q.type) \n" +
            " LEFT JOIN na_service_provider_quotation_charge_item qc on q.id = qc.service_provider_quotation_id and c.item_id = qc.item_id\n" +
            " WHERE c.service_provider_id = :serviceProviderId")
    List<ServiceProviderQuotationChargeItem> listChargeItems(@Param("serviceProviderId") String serviceProviderId, @Param("type") String type, @Param("flowOrderShippingPlanId") String flowOrderShippingPlanId);

}
