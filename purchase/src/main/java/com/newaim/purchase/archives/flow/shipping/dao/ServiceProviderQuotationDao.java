package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderQuotationDao extends BaseDao<ServiceProviderQuotation, String>, ServiceProviderQuotationDaoCustom {

    /**
     * 查找最近一次有效报价
     * @param serviceProviderId 服务商id
     * @param type 报价类型
     * @return 报价
     */
    @Query(nativeQuery = true, value = "select t.* from na_service_provider_quotation t where t.service_provider_id = :serviceProviderId and t.type = :type and t.status = 1 and t.valid_until >= now() order by t.valid_until desc")
    List<ServiceProviderQuotation> findLatestServiceProviderQuotation(@Param("serviceProviderId") String serviceProviderId, @Param("type") String type);


    /**
     * 查询所有与指定发货计划相关的报价
     * @param flowOrderShippingPlanId 流程发货计划id
     * @return
     */
    List<ServiceProviderQuotation> findQuotationsByFlowOrderShippingPlanId(String flowOrderShippingPlanId);

    List<ServiceProviderQuotation> findByBusinessId(String businessId);

}
