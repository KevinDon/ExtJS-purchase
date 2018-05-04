package com.newaim.purchase.desktop.reports.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.reports.entity.ReportsCompliance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsComplianceDao extends BaseDao<ReportsCompliance, String> {

    ReportsCompliance findReportsComplianceById(String id);

    void deleteAllByReportsId(String reportsId);

    ReportsCompliance findReportsComplianceByReportsId(String reportsId);

}
