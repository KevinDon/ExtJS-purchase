package com.newaim.purchase.flow.purchase.dao;

import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FlowPurchaseContractDetailDao extends JpaRepository<FlowPurchaseContractDetail, String>, JpaSpecificationExecutor<FlowPurchaseContractDetail> {

	/**
	 * 通过业务ID删除所有关联的明细
	 * @param businessId 业务ID
	 */
	void deleteByBusinessId(String businessId);

	/**
	 * 根据业务ID查找明细结合
	 * @param businessId 业务id
	 * @return 明细集合
	 */
	List<FlowPurchaseContractDetail> findDetailsByBusinessId(String businessId);
}
