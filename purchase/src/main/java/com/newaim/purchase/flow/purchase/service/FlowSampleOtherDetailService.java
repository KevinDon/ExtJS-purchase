package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowSampleOtherDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowSampleOtherDetail;
import com.newaim.purchase.flow.purchase.vo.FlowSampleOtherDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSampleOtherDetailService extends ServiceBase {

    @Autowired
    private FlowSampleOtherDetailDao flowSampleOtherDetailDao;

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowSampleOtherDetailVo> findOtherDetailsVoByBusinessId(String businessId){
		return BeanMapper.mapList(flowSampleOtherDetailDao.findOtherDetailsByBusinessId(businessId), FlowSampleOtherDetail.class, FlowSampleOtherDetailVo.class);
	}

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowSampleOtherDetail> findOtherDetailsByBusinessId(String businessId){
		return flowSampleOtherDetailDao.findOtherDetailsByBusinessId(businessId);
	}
}
