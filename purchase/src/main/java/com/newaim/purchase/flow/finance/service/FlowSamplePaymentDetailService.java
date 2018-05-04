package com.newaim.purchase.flow.finance.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;
import com.newaim.purchase.flow.finance.vo.FlowSamplePaymentDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlowSamplePaymentDetailService extends ServiceBase {

    @Autowired
    private FlowSamplePaymentDetailDao flowSamplePaymentDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowSamplePaymentDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(findDetailsByBusinessId(businessId), FlowSamplePaymentDetail.class, FlowSamplePaymentDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowSamplePaymentDetail> findDetailsByBusinessId(String businessId){
        return flowSamplePaymentDetailDao.findAllByBusinessId(businessId);
    }

}
