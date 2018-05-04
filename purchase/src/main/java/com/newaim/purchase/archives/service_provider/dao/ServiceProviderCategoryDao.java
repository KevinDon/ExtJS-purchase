package com.newaim.purchase.archives.service_provider.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderCategoryDao extends BaseDao<ServiceProviderCategory, String>, ServiceProviderCategoryDaoCustom{

	ServiceProviderCategory findServiceProviderCategoryById(String id);

}
