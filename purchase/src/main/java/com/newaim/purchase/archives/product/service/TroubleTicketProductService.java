package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.TroubleTicketProductDao;
import com.newaim.purchase.archives.product.entity.TroubleTicketProduct;
import com.newaim.purchase.archives.product.vo.TroubleTicketProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TroubleTicketProductService extends ServiceBase{

    @Autowired
    private TroubleTicketProductDao troubleTicketDetailDao;

    @Autowired
    private TroubleTicketProductDetailService troubleTicketProductDetailService;

    public List<TroubleTicketProduct> findDetailsByTroubleTicketId(String troubleTicketId){
        return troubleTicketDetailDao.findDetailsByTroubleTicketId(troubleTicketId);
    }

    public List<TroubleTicketProductVo> findDetailsVoByTroubleTicketId(String troubleTicketId){
        List<TroubleTicketProductVo> data = BeanMapper.mapList(troubleTicketDetailDao.findDetailsByTroubleTicketId(troubleTicketId), TroubleTicketProduct.class, TroubleTicketProductVo.class);
        if(data != null && data.size() > 0){
            for (int i = 0; i < data.size(); i++) {
                TroubleTicketProductVo vo = data.get(i);
                vo.setTroubleDetails(troubleTicketProductDetailService.findDetailsVoByTroubleTicketProductId(vo.getId()));
            }
        }
        return data;
    }

}
