package com.newaim.purchase.desktop.reports.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.reports.entity.Reports;

@Repository
public interface ReportsDao extends BaseDao<Reports, String> {

	Reports findReportsById(String id);

	List<Reports> findReportsByVendorId(String vendorId);

	List<Reports> findReportsByBusinessIdAndStatus(String businessId, Integer status);

    List<Reports> findReportsByBusinessIdAndStatusIn(String businessId, List<Integer> statusList);

}
