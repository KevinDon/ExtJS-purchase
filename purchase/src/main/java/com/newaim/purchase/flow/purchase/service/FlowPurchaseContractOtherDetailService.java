package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractOtherDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractOtherDetail;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractOtherDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mark on 2017/9/18.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowPurchaseContractOtherDetailService extends ServiceBase {

    @Autowired
    private FlowPurchaseContractOtherDetailDao flowPurchaseContractOtherDetailDao;

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowPurchaseContractOtherDetailVo> findOtherDetailsVoByBusinessId(String businessId){
		return BeanMapper.mapList(flowPurchaseContractOtherDetailDao.findOtherDetailsByBusinessId(businessId), FlowPurchaseContractOtherDetail.class, FlowPurchaseContractOtherDetailVo.class);
	}

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowPurchaseContractOtherDetail> findOtherDetailsByBusinessId(String businessId){
		return flowPurchaseContractOtherDetailDao.findOtherDetailsByBusinessId(businessId);
	}
}
