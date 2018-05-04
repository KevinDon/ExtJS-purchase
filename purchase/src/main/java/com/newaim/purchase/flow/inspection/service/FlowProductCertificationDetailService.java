package com.newaim.purchase.flow.inspection.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.inspection.dao.FlowProductCertificationDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertificationDetail;
import com.newaim.purchase.flow.inspection.vo.FlowProductCertificationDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowProductCertificationDetailService extends ServiceBase {

    @Autowired
    private FlowProductCertificationDetailDao flowProductCertificationDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowProductCertificationDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowProductCertificationDetailDao.findDetailsByBusinessId(businessId), FlowProductCertificationDetail.class, FlowProductCertificationDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowProductCertificationDetail> findDetailsByBusinessId(String businessId){
        return flowProductCertificationDetailDao.findDetailsByBusinessId(businessId);
    }
}
