package com.newaim.purchase.flow.inspection.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractDetailService;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import com.newaim.purchase.flow.inspection.vo.FlowOrderQcDetailVo;
import com.newaim.purchase.flow.workflow.entity.FlowOperatorHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowOrderQcDetailService extends ServiceBase {

    @Autowired
    private FlowOrderQcDetailDao flowOrderQcDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderQcDetailVo> findDetailsVoByBusinessId(String businessId){
        List<FlowOrderQcDetailVo> foqdvs = Lists.newArrayList();

        foqdvs = BeanMapper.mapList(flowOrderQcDetailDao.findDetailsByBusinessId(businessId), FlowOrderQcDetail.class, FlowOrderQcDetailVo.class);

        if(null != foqdvs && foqdvs.size()>0){
            for (FlowOrderQcDetailVo foqdv: foqdvs){
                List<ProductVo> pv = flowOrderQcDetailDao.getProductsByOrderId(foqdv.getOrderId());
                foqdv.setProduct(pv);
            }
        }

        return foqdvs;
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderQcDetail> findDetailsByBusinessId(String businessId){
        return flowOrderQcDetailDao.findDetailsByBusinessId(businessId);
    }
}
