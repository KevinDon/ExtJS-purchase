package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.SampleOtherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SampleOtherDetailDao extends JpaRepository<SampleOtherDetail, String>, JpaSpecificationExecutor<SampleOtherDetail> {

	/**
	 * 根据订单ID查找明细结合
	 * @param sampleId 样品单id
	 * @return 明细集合
	 */
	List<SampleOtherDetail> findOtherDetailsBySampleId(String sampleId);

}
