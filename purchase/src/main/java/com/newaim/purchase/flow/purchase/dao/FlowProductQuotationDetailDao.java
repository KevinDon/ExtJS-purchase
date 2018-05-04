package com.newaim.purchase.flow.purchase.dao;

import com.newaim.purchase.flow.purchase.entity.FlowProductQuotationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FlowProductQuotationDetailDao extends JpaRepository<FlowProductQuotationDetail, String>, JpaSpecificationExecutor<FlowProductQuotationDetail> {
	
	void deleteByBusinessId(String businessId);

	/**
	 * 根据业务ID查找明细
	 * @param businessId 业务id
	 * @return 明细集合
	 */
	List<FlowProductQuotationDetail> findDetailsByBusinessId(String businessId);

	/**
	 * 通过产品ID查找所有历史报价明细
	 * @param productId
	 * @return
	 */
	List<FlowProductQuotationDetail> findDetailsByProductId(String productId);

	/**
	 * 挂起指定产品的采购询价
	 * @param productIds
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE na_flow_product_quotation_detail t set hold = 1 where t.product_id in ?1 and " +
			"EXISTS (select 1 from na_flow_product_quotation q where q.id = t.business_id and q.effective_date <= current_timestamp and q.valid_until >= current_timestamp)")
	void suspendByProductIds(List<String> productIds);
}
