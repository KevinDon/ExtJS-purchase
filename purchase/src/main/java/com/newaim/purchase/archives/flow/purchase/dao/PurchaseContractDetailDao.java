package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseContractDetailDao extends JpaRepository<PurchaseContractDetail, String>, JpaSpecificationExecutor<PurchaseContractDetail> {

	/**
	 * 根据订单ID查找明细结合
	 * @param orderId 订单id
	 * @return 明细集合
	 */
	List<PurchaseContractDetail> findDetailsByOrderId(String orderId);

	List<PurchaseContractDetail> findDetailsByProductId(String productId);

}
