package com.newaim.purchase.desktop.reports.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.reports.entity.Reports;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsProductDao extends BaseDao<ReportsProduct, String>, ReportsProductDaoCustom {

    /**
     * 通过报告id删除全部明细
     * @param reportsId
     */
    void deleteDeltailsByReportsId(String reportsId);

    List<ReportsProduct> findDetailsByReportsId(String reportsId);

}
