package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowProductQuotationDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotationDetail;
import com.newaim.purchase.flow.purchase.vo.FlowProductQuotationDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowProductQuotationDetailService extends ServiceBase {

    @Autowired
    private FlowProductQuotationDetailDao flowProductQuotationDetailDao;

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowProductQuotationDetailVo> findDetailsVoByBusinessId(String businessId){
		return BeanMapper.mapList(flowProductQuotationDetailDao.findDetailsByBusinessId(businessId), FlowProductQuotationDetail.class, FlowProductQuotationDetailVo.class);
	}

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detail集合
	 */
	public List<FlowProductQuotationDetail> findDetailsByBusinessId(String businessId){
		return flowProductQuotationDetailDao.findDetailsByBusinessId(businessId);
	}

	/**
	 * 通过产品id查找历史报价
	 * @param productId 产品id
	 */
	public List<FlowProductQuotationDetailVo> findDetailsVoByProductId(String productId){
		return BeanMapper.mapList(flowProductQuotationDetailDao.findDetailsByProductId(productId), FlowProductQuotationDetail.class, FlowProductQuotationDetailVo.class);
	}
}
