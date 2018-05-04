package com.newaim.purchase.archives.flow.finance.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.finance.dao.PurchaseContractDepositDao;
import com.newaim.purchase.archives.flow.finance.entity.PurchaseContractDeposit;
import com.newaim.purchase.archives.flow.finance.vo.PurchaseContractDepositVo;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchaseContractDepositService extends ServiceBase {

    @Autowired
    private PurchaseContractDepositDao purchaseContractDepositDao;
    @Autowired
    private PurchaseContractDao contractDao;
    /**
     * 分页查询正式费用登记数据
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<PurchaseContractDepositVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<PurchaseContractDeposit> spec = buildSpecification((LinkedHashMap<String, Object>) params);
        Page<PurchaseContractDeposit> p = purchaseContractDepositDao.findAll(spec, pageRequest);
        Page<PurchaseContractDepositVo> page = p.map(new Converter<PurchaseContractDeposit, PurchaseContractDepositVo>() {
            @Override
            public PurchaseContractDepositVo convert(PurchaseContractDeposit purchaseContractDeposit) {
                PurchaseContractDepositVo vo = convertToPurchaseContractDepositVo(purchaseContractDeposit);
                return vo;
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param purchaseContractDeposit
     * @return
     */
    private PurchaseContractDepositVo convertToPurchaseContractDepositVo(PurchaseContractDeposit purchaseContractDeposit){
        PurchaseContractDepositVo vo = BeanMapper.map(purchaseContractDeposit, PurchaseContractDepositVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<PurchaseContractDeposit> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchaseContractDeposit> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchaseContractDeposit.class);
        return spec;
    }

    /**
     * 根据ID获取费用支付
     * @param id
     * @return
     */
    public PurchaseContractDeposit getPurchaseContractDeposit(String id){
        return purchaseContractDepositDao.findOne(id);
    }

    /**
     * 根据ID获取费用支付
     * @param id
     * @return
     */
    public PurchaseContractDepositVo get(String id){
        PurchaseContractDepositVo vo =convertToPurchaseContractDepositVo(getPurchaseContractDeposit(id));
        return vo;
    }

    public List<PurchaseContractDeposit> findDepositsByOrderIds(List<String> orderIds){
        return purchaseContractDepositDao.findByOrderIdIn(orderIds);
    }

    public List<PurchaseContractDepositVo> findDepositsVoByOrderIds(List<String> orderIds){

        return BeanMapper.mapList(findDepositsByOrderIds(orderIds), PurchaseContractDeposit.class, PurchaseContractDepositVo.class);
    }

    public List<PurchaseContractDepositVo> findDepositvoByOrderId(String orderId) {
        List<PurchaseContractDeposit> deposits = purchaseContractDepositDao.findByOrderId(orderId);
        List<PurchaseContractDepositVo> vo = BeanMapper.mapList(deposits, PurchaseContractDeposit.class, PurchaseContractDepositVo.class);
        BigDecimal totalValueAud = contractDao.findOne(orderId).getTotalValueAud();
        BigDecimal totalValueRmb = contractDao.findOne(orderId).getTotalValueRmb();
        BigDecimal totalValueUsd = contractDao.findOne(orderId).getTotalValueUsd();
        for (PurchaseContractDepositVo purchaseContractDepositVo : vo) {
            purchaseContractDepositVo.setTotalOtherAud(totalValueAud);
            purchaseContractDepositVo.setTotalvalueRmb(totalValueRmb);
            purchaseContractDepositVo.setTotalvalueUsd(totalValueUsd);
        }
        return vo;

    }

}
