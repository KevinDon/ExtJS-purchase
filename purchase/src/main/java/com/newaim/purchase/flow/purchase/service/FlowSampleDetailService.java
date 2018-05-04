package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowSampleDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowSampleDetail;
import com.newaim.purchase.flow.purchase.vo.FlowSampleDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bryan 2017/10/18.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSampleDetailService extends ServiceBase {

    @Autowired
    private FlowSampleDetailDao flowSampleDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowSampleDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowSampleDetailDao.findDetailsByBusinessId(businessId), FlowSampleDetail.class, FlowSampleDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowSampleDetail> findDetailsByBusinessId(String businessId){
        return flowSampleDetailDao.findDetailsByBusinessId(businessId);
    }

}
