package com.newaim.purchase.archives.service_provider.dao;

import com.newaim.purchase.archives.service_provider.entity.ServiceProviderPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceProviderPortDao extends JpaRepository<ServiceProviderPort, String>, JpaSpecificationExecutor<ServiceProviderPort> {


    ServiceProviderPort findServiceProviderPortById(String id);

    /**
     * 通过供应商id删除
     * @param serviceProviderId 供应商id
     */
    void deletePortByServiceProviderId(String serviceProviderId);

    /**
     * 获取服务商下所有的港口
     * @param serviceProviderId 服务商id
     * @return
     */
    List<ServiceProviderPort> findPortsByServiceProviderId(String serviceProviderId);

}
