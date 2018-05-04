package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationOrderUnion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderQuotationOrderUnionDao extends JpaRepository<ServiceProviderQuotationOrderUnion, String>, JpaSpecificationExecutor<ServiceProviderQuotationOrderUnion>{

}
