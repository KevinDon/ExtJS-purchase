package com.newaim.purchase.archives.vendor.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Mark on 2017/9/18.
 */
@Repository
public interface VendorDao extends BaseDao<Vendor, String>,VendorDaoCustom {

    Vendor findVendorById(String id);

    List<Vendor> findByCode(String code);
}
