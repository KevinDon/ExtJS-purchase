package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApplyDetail;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShippingApplyDetailDao extends JpaRepository<OrderShippingApplyDetail, String>, JpaSpecificationExecutor<OrderShippingApplyDetail> {

    /**
     * 根据orderShippingApplyId查找明细结合
     * @param orderShippingApplyId
     * @return 明细集合
     */
    List<OrderShippingApplyDetail> findDetailsByOrderShippingApplyId(String orderShippingApplyId);

}
