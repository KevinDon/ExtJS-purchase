package com.newaim.purchase.archives.service_provider.dao;

import com.newaim.purchase.archives.service_provider.entity.ServiceProviderChargeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceProviderChargeItemDao extends JpaRepository<ServiceProviderChargeItem, String>, JpaSpecificationExecutor<ServiceProviderChargeItem> {

    ServiceProviderChargeItem findServiceProviderChargeItemById(String id);

    /**
     * 删除服务商下所有收费项目
     * @param serviceProviderId 服务商id
     */
    void deleteByServiceProviderId(String serviceProviderId);

    /**
     * 获取服务商下所有的收费项目
     * @param serviceProviderId 服务商id
     * @return
     */
    List<ServiceProviderChargeItem> findChargeItemsByServiceProviderId(String serviceProviderId);
}
