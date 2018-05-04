package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearancePackingDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowCustomClearancePackingDetailService extends ServiceBase {

    @Autowired
    private FlowCustomClearancePackingDetailDao flowCustomClearancePackingDetailDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 根据业务id返回所有明细
     * @param packingId 业务ID
     * @return detailVo集合
     */
    public List<FlowCustomClearancePackingDetailVo> findPackingDetailsVoByPackingId(String packingId){
        List<FlowCustomClearancePackingDetail> data = flowCustomClearancePackingDetailDao.findPackingDetailsByPackingId(packingId);
        List<FlowCustomClearancePackingDetailVo> result = BeanMapper.mapList(data, FlowCustomClearancePackingDetail.class, FlowCustomClearancePackingDetailVo.class);
        if(result != null && result.size() > 0){
            for (int i = 0; i < result.size(); i++) {
                FlowCustomClearancePackingDetailVo vo = result.get(i);
                Product product = productDao.findOne(vo.getProductId());
                ProductVendorProp prop = product.getProp();
                vo.setMasterCartonCbm(prop.getMasterCartonCbm());
                vo.setMasterCartonGrossWeight(prop.getMasterCartonGrossWeight());
                vo.setPcsPerCarton(prop.getPcsPerCarton());
            }
        }
        return result;
    }

    /**
     * 根据业务id返回所有明细
     * @param packingId 业务ID
     * @return detail集合
     */
    public List<FlowCustomClearancePackingDetail> findPackingDetailsByPackingId(String packingId){
        return flowCustomClearancePackingDetailDao.findPackingDetailsByPackingId(packingId);
    }

}
