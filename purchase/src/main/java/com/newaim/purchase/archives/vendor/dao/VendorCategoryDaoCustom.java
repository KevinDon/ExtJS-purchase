package com.newaim.purchase.archives.vendor.dao;


import com.newaim.purchase.archives.vendor.entity.VendorCategory;

import java.util.List;

/**
 * @author Mark
 */
public interface VendorCategoryDaoCustom {

    /**
     * 通过上级ID查找供应商分类列表，并排序
     * @param parentId 上级ID
     * @return 分类列表
     */
    List<VendorCategory> findVendorCategoryByParentId(String parentId, Integer status);

    /**
     * 获取上一分类信息
     * @param id 上一分类id
     * @return 上一个分类信息
     */
    List<VendorCategory> getUp(String id);


    /** 获取下一个分类信息
     * @param id 下一分类id
     * @return 下一个分类信息
     */
    List<VendorCategory> getDown(String id);

    /**
     * 获取所有子分类信息
     * @param id 当前分类信息id
     * @return 子分类列表
     */
    List<VendorCategory> listChildrenRows(String id);
}
