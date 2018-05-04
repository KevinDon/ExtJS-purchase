package com.newaim.purchase.archives.flow.purchase.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearance;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchaseContractService extends ServiceBase {

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private PurchaseContractDetailService purchaseContractDetailService;

    @Autowired
    private PurchaseContractOtherDetailService purchaseContractOtherDetailService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomClearanceService customClearanceService;

    @Autowired
    private CustomClearancePackingService customClearancePackingService;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;


    /**
     * 根据产品id获取对应采购合同
     * @param productId 产品id
     */
    public List<PurchaseContractVo> listByProductId(String productId){
        List<PurchaseContract> p = purchaseContractDao.findByProductId(productId);
        return BeanMapper.mapList(p, PurchaseContract.class, PurchaseContractVo.class);
    }

    /**
     * 根据产品id获取对应采购合同
     * @param productId 产品id
     */
    public List<PurchaseContractVo> listByProductId(String productId, int pageSize){
        Page<PurchaseContract> p = purchaseContractDao.findByProductId(productId, new PageRequest(0, pageSize));
        return BeanMapper.mapList(p, PurchaseContract.class, PurchaseContractVo.class);
    }

    /**
     * 获得采购合同（正式）List数据
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<PurchaseContractVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<PurchaseContract> spec = buildSpecification(params);
        Page<PurchaseContract> p = purchaseContractDao.findAll(spec, pageRequest);
        Page<PurchaseContractVo> page = p.map(purchaseContract -> {
            PurchaseContractVo vo = convertToPurchaseContractVo(purchaseContract);
            return vo;
        });
        return page;
    }

    private PurchaseContractVo convertToPurchaseContractVo(PurchaseContract purchaseContract){
        PurchaseContractVo vo = BeanMapper.map(purchaseContract, PurchaseContractVo.class);
        return vo;
    }

    /**
     * 根据id获取采购合同
     * @param id
     * @return 采购合同
     */
    public PurchaseContract getPurchaseContract(String id){
        return purchaseContractDao.findOne(id);
    }

    /**
     * 根据id获取采购合同
     * @param id
     * @return 采购合同
     */
    public PurchaseContractVo get(String id){
        PurchaseContractVo vo =convertToPurchaseContractVo(getPurchaseContract(id));
        vo.setDetails(purchaseContractDetailService.findDetailVosByOrderId(id));
        vo.setOtherDetails(purchaseContractOtherDetailService.findOtherDetailVosByOrderId(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendorBank(bankAccountService.getByVendorId(vo.getVendorId()));
        }
        return vo;
    }

    /**
     *费用登记调用
     * @param id
     * @return
     */
    public PurchaseContractVo getForFeeRegister(String id){
        PurchaseContractVo vo =convertToPurchaseContractVo(getPurchaseContract(id));
        //如果清关标记=1，调用清关的明细
        if ("1".equals(vo.getFlagCustomClearanceStatus().toString())){
            List<CustomClearancePackingDetailVo> detailVos = new ArrayList<>();
            CustomClearanceVo customClearanceVo = customClearanceService.getCustomClearanceVo(vo.getFlagCustomClearanceId());
            if (customClearanceVo != null) {
                List<CustomClearancePackingVo> packingVos = customClearancePackingService.findPackingsVoByCustomClearanceId(customClearanceVo.getId());
                for (int i = 0;i < packingVos.size();i++ ){
                    //通过装柜ID查找装柜明细
                    List<CustomClearancePackingDetailVo> vos = customClearancePackingDetailService.findPackingDetailsVoByPackingId(packingVos.get(i).getId());
                    for (CustomClearancePackingDetailVo detailVo : vos ){
                        detailVos.add(detailVo);
                    }
                }
            }
            vo.setCustomClearancePackingDetailVos(customClearancePackingDetailService.findMergeDetailVosByPackingId(detailVos));
        }else {
            //否则，调用采购合同明细
            vo.setDetails(purchaseContractDetailService.findDetailVosByOrderId(id));
        }
        vo.setOtherDetails(purchaseContractOtherDetailService.findOtherDetailVosByOrderId(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendorBank(bankAccountService.getByVendorId(vo.getVendorId()));
        }
        return vo;
    }

    /**
     * 获取采购订单，并对订单中sku相同，报价不同的做合并，并计算合并后的价格
     * @param id
     * @return
     */
    public PurchaseContractVo getForMerge(String id){
        PurchaseContractVo vo = convertToPurchaseContractVo(getPurchaseContract(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendorBank(bankAccountService.getByVendorId(vo.getVendorId()));
        }
        vo.setDetails(purchaseContractDetailService.findMergeDetailVosByOrderId(id));
        return vo;
    }

    public List<PurchaseContract> findByPurchasePlanBusinessId(String purchasePlanBusinessId){
        return purchaseContractDao.findByPurchasePlanBusinessId(purchasePlanBusinessId);
    }

    public List<PurchaseContractVo> findVoByPurchasePlanBusinessId(String purchasePlanBusinessId){
        return BeanMapper.mapList(findByPurchasePlanBusinessId(purchasePlanBusinessId), PurchaseContract.class, PurchaseContractVo.class);
    }

    public List<PurchaseContract> findByVendorId(String vendorId){
        return purchaseContractDao.findByVendorId(vendorId);
    }

    public List<PurchaseContractVo> findVoByVendorId(String vendorId){
        return BeanMapper.mapList(findByVendorId(vendorId), PurchaseContract.class, PurchaseContractVo.class);
    }

    /**
     * 动态建立关联
     * @param searchParams
     * @return
     */
    private Specification<PurchaseContract> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchaseContract> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchaseContract.class);
        return spec;
    }

}
