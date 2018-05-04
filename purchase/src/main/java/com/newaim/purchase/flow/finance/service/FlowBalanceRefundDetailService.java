package com.newaim.purchase.flow.finance.service;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import com.newaim.purchase.flow.finance.vo.FlowBalanceRefundDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowBalanceRefundDetailService extends ServiceBase {

    @Autowired
    private FlowBalanceRefundDetailDao flowBalanceRefundDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowBalanceRefundDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowBalanceRefundDetailDao.findDetailsByBusinessId(businessId), FlowBalanceRefundDetail.class, FlowBalanceRefundDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowBalanceRefundDetail> findDetailsByBusinessId(String businessId){
        return flowBalanceRefundDetailDao.findDetailsByBusinessId(businessId);
    }

}
