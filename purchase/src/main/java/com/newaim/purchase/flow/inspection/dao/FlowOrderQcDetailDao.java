package com.newaim.purchase.flow.inspection.dao;

import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowOrderQcDetailDao extends JpaRepository<FlowOrderQcDetail, String>, FlowOrderQcDetailDaoCustom {
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowOrderQcDetail> findDetailsByBusinessId(String businessId);

}
