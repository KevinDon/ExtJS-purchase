package com.newaim.purchase.archives.service_provider.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceProviderDao extends BaseDao<ServiceProvider, String>, ServiceProviderDaoCustom {


}
