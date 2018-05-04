package com.newaim.purchase.archives.flow.shipping.dao;


import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderQuotationPortDao extends JpaRepository<ServiceProviderQuotationPort, String>, JpaSpecificationExecutor<ServiceProviderQuotationPort>, ServiceProviderQuotationPortDaoCustom {


    /**
     * 通过报价查找服务商港口报价
     * @param serviceProviderQuotationId 报价id
     * @return 港口报价
     */
    List<ServiceProviderQuotationPort> findByServiceProviderQuotationId(String serviceProviderQuotationId);

    /**
     * 通过服务商id和类型查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @return 港口报价
     */
    @Query(nativeQuery = true, value = "SELECT p.id,p.origin_port_id,p.origin_port_cn_name,p.origin_port_en_name,p.destination_port_id,p.destination_port_cn_name,p.destination_port_en_name,qp.*" +
            " FROM na_service_provider_port p\n" +
            " LEFT JOIN na_service_provider_quotation q on p.service_provider_id = q.service_provider_id and q.type=:type \n" +
            "     and q.end_time = (select max(t.end_time) from na_service_provider_quotation t where t.service_provider_id = q.service_provider_id and t.type=q.type) \n" +
            " LEFT JOIN na_service_provider_quotation_port qp on  q.id = qp.service_provider_quotation_id and p.origin_port_id = qp.origin_port_id\n" +
            " WHERE p.service_provider_id = :serviceProviderId")
    List<ServiceProviderQuotationPort> listPorts(@Param("serviceProviderId") String serviceProviderId, @Param("type") String type);


    /**
     * 通过服务商id、类型和发货计划id查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @param flowOrderShippingPlanId 发货计划id
     * @return 港口报价
     */
    @Query(nativeQuery = true, value = "SELECT p.id,p.origin_port_id,p.origin_port_cn_name,p.origin_port_en_name,p.destination_port_id,p.destination_port_cn_name,p.destination_port_en_name,qp.*" +
            " FROM na_service_provider_port p\n" +
            " LEFT JOIN na_service_provider_quotation q on p.service_provider_id = q.service_provider_id and and q.flow_order_shipping_plan_id = :flowOrderShippingPlanId q.type=:type \n" +
            "     and q.end_time = (select max(t.end_time) from na_service_provider_quotation t where t.service_provider_id = q.service_provider_id and t.type=q.type) \n" +
            " LEFT JOIN na_service_provider_quotation_port qp on  q.id = qp.service_provider_quotation_id and p.origin_port_id = qp.origin_port_id\n" +
            " where p.service_provider_id = :serviceProviderId")
    List<ServiceProviderQuotationPort> listPorts(@Param("serviceProviderId") String serviceProviderId, @Param("type") String type, @Param("flowOrderShippingPlanId") String flowOrderShippingPlanId);

    @Query(nativeQuery = true, value = "SELECT pp.* from na_order_shipping_plan_detail pd inner join  na_service_provider_quotation_port pp  on pd.origin_port_id = pp.origin_port_id \n" +
            "and pd.service_provider_quotation_id = pp.service_provider_quotation_id \n" +
            "WHERE pd.order_shipping_plan_id = ? ")
    List<ServiceProviderQuotationPort> listports( String shippingPlanId);
}
