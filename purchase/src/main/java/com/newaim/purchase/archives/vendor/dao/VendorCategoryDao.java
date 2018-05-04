package com.newaim.purchase.archives.vendor.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.vendor.entity.VendorCategory;
import org.springframework.stereotype.Repository;

/**
 * @author Mark
 */
@Repository
public interface VendorCategoryDao extends BaseDao<VendorCategory, String>, VendorCategoryDaoCustom{

    /**
     * 通过id查找供应商分类
     * @param id id
     * @return 供应商分类
     */
    VendorCategory findVendorCategoryById(String id);
}
