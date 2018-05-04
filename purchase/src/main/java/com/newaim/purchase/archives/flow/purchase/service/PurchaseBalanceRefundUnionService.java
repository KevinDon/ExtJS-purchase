package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.finance.service.BalanceRefundService;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseBalanceRefundUnionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchaseBalanceRefundUnionService extends ServiceBase {

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Autowired
    private BalanceRefundService balanceRefundService;

    public PurchaseBalanceRefundUnion getPurchaseBalanceRefundUnion(String id){
        return purchaseBalanceRefundUnionDao.findOne(id);
    }

    public PurchaseBalanceRefundUnionVo get(String id){
        return BeanMapper.map(getPurchaseBalanceRefundUnion(id), PurchaseBalanceRefundUnionVo.class);
    }

    public List<PurchaseBalanceRefundUnion> findByPurchasePlanBusinessId(String purchasePlanBusinessId){
        return purchaseBalanceRefundUnionDao.findByPurchasePlanBusinessId(purchasePlanBusinessId);
    }

    public List<PurchaseBalanceRefundUnionVo> findVoByPurchasePlanBusinessId(String purchasePlanBusinessId){
        List<PurchaseBalanceRefundUnionVo> data =  BeanMapper.mapList(findByPurchasePlanBusinessId(purchasePlanBusinessId), PurchaseBalanceRefundUnion.class, PurchaseBalanceRefundUnionVo.class);
        setBalanceRefundInfo(data);
        return data;
    }

    public List<PurchaseBalanceRefundUnion> findByPurchasePlanId(String purchasePlanId){
        return purchaseBalanceRefundUnionDao.findByPurchasePlanId(purchasePlanId);
    }

    public List<PurchaseBalanceRefundUnionVo> findVoByPurchasePlanId(String purchasePlanId){
        List<PurchaseBalanceRefundUnionVo> data = BeanMapper.mapList(findByPurchasePlanId(purchasePlanId), PurchaseBalanceRefundUnion.class, PurchaseBalanceRefundUnionVo.class);
        setBalanceRefundInfo(data);
        return data;
    }

    public List<PurchaseBalanceRefundUnion> findByPurchasePlanIdIn(List<String> purchasePlanIds){
        return purchaseBalanceRefundUnionDao.findByPurchasePlanIdIn(purchasePlanIds);
    }

    public List<PurchaseBalanceRefundUnionVo> findVoByPurchasePlanIdIn(List<String> purchasePlanIds){
        List<PurchaseBalanceRefundUnionVo> data = BeanMapper.mapList(findByPurchasePlanIdIn(purchasePlanIds), PurchaseBalanceRefundUnion.class, PurchaseBalanceRefundUnionVo.class);
        setBalanceRefundInfo(data);
        return data;
    }

    public List<PurchaseBalanceRefundUnion> findByPurchaseContractBusinessId(String purchaseContractBusinessId){
        return purchaseBalanceRefundUnionDao.findByPurchaseContractBusinessId(purchaseContractBusinessId);
    }

    public List<PurchaseBalanceRefundUnionVo> findVoByPurchaseContractBusinessId(String purchaseContractBusinessId){
        List<PurchaseBalanceRefundUnionVo> data = BeanMapper.mapList(findByPurchaseContractBusinessId(purchaseContractBusinessId), PurchaseBalanceRefundUnion.class, PurchaseBalanceRefundUnionVo.class);
        setBalanceRefundInfo(data);
        return data;
    }

    public List<PurchaseBalanceRefundUnion> findByPurchaseContractId(String purchaseContractId){
        return purchaseBalanceRefundUnionDao.findByPurchaseContractId(purchaseContractId);
    }

    public List<PurchaseBalanceRefundUnionVo> findVoByPurchaseContractId(String purchaseContractId){
        List<PurchaseBalanceRefundUnionVo> data = BeanMapper.mapList(findByPurchaseContractId(purchaseContractId), PurchaseBalanceRefundUnion.class, PurchaseBalanceRefundUnionVo.class);
        setBalanceRefundInfo(data);
        return data;
    }

    private void setBalanceRefundInfo(List<PurchaseBalanceRefundUnionVo> data){
        if(data != null && data.size() > 0){
            for (int i = 0; i < data.size(); i++) {
                PurchaseBalanceRefundUnionVo vo = data.get(i);
                if(StringUtils.isNotBlank(vo.getBalanceRefundId())){
                    vo.setBalanceRefund(balanceRefundService.get(vo.getBalanceRefundId()));
                }
            }
        }
    }
}
