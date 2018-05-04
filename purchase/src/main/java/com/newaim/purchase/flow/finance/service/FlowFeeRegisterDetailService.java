package com.newaim.purchase.flow.finance.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegister;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegisterDetail;
import com.newaim.purchase.flow.finance.vo.FlowFeeRegisterDetailVo;
import com.newaim.purchase.flow.finance.vo.FlowFeeRegisterVo;
import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.vo.FlowNewProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowFeeRegisterDetailService extends ServiceBase {

    @Autowired
    private FlowFeeRegisterDetailDao flowFeeRegisterDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowFeeRegisterDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowFeeRegisterDetailDao.findDetailsByBusinessId(businessId), FlowFeeRegisterDetail.class, FlowFeeRegisterDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowFeeRegisterDetail> findDetailsByBusinessId(String businessId){
        return flowFeeRegisterDetailDao.findDetailsByBusinessId(businessId);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @param type 类型
     * @return detail集合
     */
    public List<FlowFeeRegisterDetailVo> findDetailsVoByBusinessIdAndType(String businessId, Integer type){
        return BeanMapper.mapList(flowFeeRegisterDetailDao.findDetailsByBusinessIdAndType(businessId, type), FlowFeeRegisterDetail.class, FlowFeeRegisterDetailVo.class);
    }

    /**
     * 根据业务id和类型返回所有明细
     * @param businessId 业务ID
     * @param type 类型
     * @return detail集合
     */
    public List<FlowFeeRegisterDetail> findDetailsByBusinessIdAndType(String businessId, Integer type){
        return flowFeeRegisterDetailDao.findDetailsByBusinessIdAndType(businessId, type);
    }

}
