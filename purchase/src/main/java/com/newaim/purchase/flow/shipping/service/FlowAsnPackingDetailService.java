package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingDetailService;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import com.newaim.purchase.flow.shipping.vo.FlowAsnPackingDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowAsnPackingDetailService extends ServiceBase {

    @Autowired
    private FlowAsnPackingDetailDao flowAsnPackingDetailDao;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;

    public List<FlowAsnPackingDetailVo> findPackingDetailsVoByAsnPackingId(String asnPackingId){
        List<FlowAsnPackingDetail> list = findPackingDetailsByAsnPackingId(asnPackingId);
        List<FlowAsnPackingDetailVo> asnPackingDetails = BeanMapper.mapList(list, FlowAsnPackingDetail.class, FlowAsnPackingDetailVo.class);
        if(asnPackingDetails != null && asnPackingDetails.size() > 0){
            for (int i = 0; i < asnPackingDetails.size(); i++) {
                FlowAsnPackingDetailVo detail = asnPackingDetails.get(i);
                if(StringUtils.isNotBlank(detail.getCcPackingDetailId())){
                    CustomClearancePackingDetailVo ccPackingDetail = customClearancePackingDetailService.get(detail.getCcPackingDetailId());
                    detail.setCcPackingDetail(ccPackingDetail);
                }
            }
        }
        return asnPackingDetails;
    }

    /**
     * 根据业务id返回所有明细
     * @param asnPackingId 业务ID
     * @return detail集合
     */
    public List<FlowAsnPackingDetail> findPackingDetailsByAsnPackingId(String asnPackingId){
        return flowAsnPackingDetailDao.findPackingDetailsByAsnPackingId(asnPackingId);
    }
    /**
     * 根据订单id返回所有明细
     * @param orderId 订单ID
     * @return detail集合
     */
    public List<FlowAsnPackingDetail> findFlowAsnPackingDetailsByOrderId(String orderId){
    	return flowAsnPackingDetailDao.findFlowAsnPackingDetailsByOrderId(orderId);
    }

}
