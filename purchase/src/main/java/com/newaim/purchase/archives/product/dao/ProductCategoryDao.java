package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryDao extends BaseDao<ProductCategory, String>, ProductCategoryDaoCustom{

	ProductCategory findProductCategoryById(String id);

}
