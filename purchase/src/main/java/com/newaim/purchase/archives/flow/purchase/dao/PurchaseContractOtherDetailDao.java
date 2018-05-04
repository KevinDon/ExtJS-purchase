package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractOtherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseContractOtherDetailDao extends JpaRepository<PurchaseContractOtherDetail, String>, JpaSpecificationExecutor<PurchaseContractOtherDetail> {

	/**
	 * 根据订单ID查找明细结合
	 * @param orderId 订单id
	 * @return 明细集合
	 */
	List<PurchaseContractOtherDetail> findOtherDetailsByOrderId(String orderId);


	@Query( nativeQuery = true,value = "SELECT * from na_purchase_contract_other_detail d WHERE d.order_id = ? and d.settlement_type=1")
	List<PurchaseContractOtherDetail> getOtherDeposit(String orderId);
}
