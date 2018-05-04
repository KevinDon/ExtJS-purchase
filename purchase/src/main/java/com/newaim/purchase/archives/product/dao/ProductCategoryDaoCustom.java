package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDaoCustom {

    List<ProductCategory> findProductCategoryByParentId(String parentId, Integer status);

    List<ProductCategory> getUp(String id);

    List<ProductCategory> getDown(String id);

    List<ProductCategory> listChildrenRows(String id);
}
