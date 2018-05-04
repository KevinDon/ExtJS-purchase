package com.newaim.purchase.desktop.reports.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.entity.ReportsProductTroubleDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsProductTroubleDetailDao extends BaseDao<ReportsProductTroubleDetail, String> {

    /**
     * 通过报告id删除数据
     * @param reportsId
     */
    void deleteDeltailsByReportsId(String reportsId);

    List<ReportsProductTroubleDetail> findAllByReportsProductId(String reportsProductId);

    List<ReportsProductTroubleDetail> findAllByProductId(String productId);

    List<ReportsProductTroubleDetail> findAllByReportsIdAndProductId(String reportsId, String productId);

}
