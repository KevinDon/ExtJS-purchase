package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractOtherDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractOtherDetail;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractOtherDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchaseContractOtherDetailService {

    @Autowired
    private PurchaseContractOtherDetailDao purchaseContractOtherDetailDao;

    /**
     * 查找合同其它费用明细
     * @param orderId
     * @return
     */
    public List<PurchaseContractOtherDetail> findOtherDetailsByOrderId(String orderId){
        return purchaseContractOtherDetailDao.findOtherDetailsByOrderId(orderId);
    }

    /**
     * 查找合同其它费用明细
     * @param orderId
     * @return
     */
    public List<PurchaseContractOtherDetailVo> findOtherDetailVosByOrderId(String orderId){
        List<PurchaseContractOtherDetail> data = findOtherDetailsByOrderId(orderId);
        List<PurchaseContractOtherDetailVo> result = BeanMapper.mapList(data, PurchaseContractOtherDetail.class, PurchaseContractOtherDetailVo.class);
        return result;
    }

    public List<PurchaseContractOtherDetailVo> findOtherDepositByOrderId(String orderId) {
        List<PurchaseContractOtherDetail> otherDeposit = purchaseContractOtherDetailDao.getOtherDeposit(orderId);
        return BeanMapper.mapList(otherDeposit, PurchaseContractOtherDetail.class, PurchaseContractOtherDetailVo.class);
    }


}
