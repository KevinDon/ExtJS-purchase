package com.newaim.purchase.archives.service_provider.dao;


import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;

import java.util.List;

public interface ServiceProviderCategoryDaoCustom {

    List<ServiceProviderCategory> findServiceProviderCategoryByParentId(String parentId, Integer status);

    List<ServiceProviderCategory> getUp(String id);

    List<ServiceProviderCategory> getDown(String id);

    List<ServiceProviderCategory> listChildrenRows(String id);
}
