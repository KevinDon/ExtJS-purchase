package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowNewProductDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.vo.FlowNewProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mark on 2017/9/18.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowNewProductDetailService extends ServiceBase {

    @Autowired
    private FlowNewProductDetailDao flowNewProductDetailDao;

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowNewProductDetailVo> findDetailsVoByBusinessId(String businessId){
		return BeanMapper.mapList(flowNewProductDetailDao.findDetailsByBusinessId(businessId), FlowNewProductDetail.class, FlowNewProductDetailVo.class);
	}

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detail集合
	 */
	public List<FlowNewProductDetail> findDetailsByBusinessId(String businessId){
		return flowNewProductDetailDao.findDetailsByBusinessId(businessId);
	}
}
