package com.newaim.purchase.desktop.reports.dao;


import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsProductDaoCustom{

    List<ReportsVo> listReportsByProductId(String productId, Integer status);

    List<ReportsVo> listReportsByProductIds(String[] productIds, Integer status);

    List<ReportsVo> listReportsByBusinessId(String businessId, Integer status);

    List<ReportsVo> listReportsByVendorId(String vendorId, Integer status);

    ReportsProduct findByReportsIdAndProductId(String businessId, String productId);
}
