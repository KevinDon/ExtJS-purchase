package com.newaim.purchase.archives.flow.purchase.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractDetail;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractDetailVo;
import com.newaim.purchase.archives.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchaseContractDetailService {

    @Autowired
    private PurchaseContractDetailDao purchaseContractDetailDao;

    @Autowired
    private ProductService productService;

    /**
     * 查找合同明细
     * @param orderId
     * @return
     */
    public List<PurchaseContractDetail> findDetailsByOrderId(String orderId){
        return purchaseContractDetailDao.findDetailsByOrderId(orderId);
    }

    /**
     * 查找合同明细
     * @param orderId
     * @return
     */
    public List<PurchaseContractDetailVo> findDetailVosByOrderId(String orderId){
        List<PurchaseContractDetail> data = findDetailsByOrderId(orderId);
        List<PurchaseContractDetailVo> result = BeanMapper.mapList(data, PurchaseContractDetail.class, PurchaseContractDetailVo.class);
        if(data != null){
            for (int i = 0; i < data.size(); i++) {
                PurchaseContractDetailVo vo = result.get(i);
                vo.setProduct(productService.get(vo.getProductId()));
            }
        }
        return result;
    }

    /**
     * 根据合同id查找合并后的合同明细(相同sku会做价格和数量合并)
     * @param orderId
     * @return
     */
    public List<PurchaseContractDetailVo> findMergeDetailVosByOrderId(String orderId){

        List<PurchaseContractDetailVo> destDetails = Lists.newArrayList();
        Map<String, List<PurchaseContractDetailVo>> temp = Maps.newHashMap();
        List<PurchaseContractDetailVo> srcDetails = findDetailVosByOrderId(orderId);
        for (int i = 0; i < srcDetails.size(); i++) {
            PurchaseContractDetailVo detail = srcDetails.get(i);
            String productId = detail.getProductId();
            if(temp.containsKey(productId)){
                temp.get(productId).add(detail);
            }else{
                List<PurchaseContractDetailVo> newDetails = Lists.newArrayList();
                newDetails.add(detail);
                temp.put(productId, newDetails);
                destDetails.add(detail);
            }
        }

        //对有重复SKU做价格合并
        Map<String,PurchaseContractDetailVo> priceMap = Maps.newHashMap();
        for (String productId : temp.keySet()) {
            List<PurchaseContractDetailVo> details = temp.get(productId);
            if(details.size() > 1){
                BigDecimal totalPriceAud = BigDecimal.ZERO;
                BigDecimal totalPriceRmb = BigDecimal.ZERO;
                BigDecimal totalPriceUsd = BigDecimal.ZERO;
                BigDecimal totalOrderQty = BigDecimal.ZERO;
                BigDecimal totalCartons = BigDecimal.ZERO;
                BigDecimal totalOrderValueAud = BigDecimal.ZERO;
                BigDecimal totalOrderValueRmb = BigDecimal.ZERO;
                BigDecimal totalOrderValueUsd = BigDecimal.ZERO;
                for (int i = 0; i < details.size(); i++) {
                    PurchaseContractDetailVo detail = details.get(i);
                    BigDecimal orderQty = BigDecimal.valueOf(detail.getOrderQty());
                    totalPriceAud = totalPriceAud.add(detail.getPriceAud().multiply(orderQty));
                    totalPriceRmb = totalPriceRmb.add(detail.getPriceRmb().multiply(orderQty));
                    totalPriceUsd = totalPriceUsd.add(detail.getPriceUsd().multiply(orderQty));
                    totalOrderQty = totalOrderQty.add(BigDecimal.valueOf(detail.getOrderQty()));
                    totalCartons = totalCartons.add(BigDecimal.valueOf(detail.getCartons()));
                    totalOrderValueAud = totalOrderValueAud.add(detail.getPriceAud().multiply(orderQty));
                    totalOrderValueRmb = totalOrderValueRmb.add(detail.getPriceRmb().multiply(orderQty));
                    totalOrderValueUsd = totalOrderValueUsd.add(detail.getPriceUsd().multiply(orderQty));
                }
                PurchaseContractDetailVo priceDetail = new PurchaseContractDetailVo();
                priceDetail.setPriceAud(totalPriceAud.divide(totalOrderQty, 4));
                priceDetail.setPriceRmb(totalPriceRmb.divide(totalOrderQty,4));
                priceDetail.setPriceUsd(totalPriceUsd.divide(totalOrderQty,4));
                priceDetail.setOrderQty(totalOrderQty.intValue());
                priceDetail.setCartons(totalCartons.intValue());
                priceDetail.setOrderValueAud(totalOrderValueAud);
                priceDetail.setOrderValueRmb(totalOrderValueRmb);
                priceDetail.setOrderValueUsd(totalOrderValueUsd);
                priceMap.put(productId, priceDetail);
            }
        }

        //回写合并后的价格和数量
        for (int i = 0; i < destDetails.size(); i++) {
            PurchaseContractDetailVo detail = destDetails.get(i);
            if(priceMap.containsKey(detail.getProductId())){
                PurchaseContractDetailVo priceDetail = priceMap.get(detail.getProductId());
                detail.setPriceAud(priceDetail.getPriceAud());
                detail.setPriceRmb(priceDetail.getPriceRmb());
                detail.setPriceUsd(priceDetail.getPriceUsd());
                detail.setOrderQty(priceDetail.getOrderQty());
                detail.setCartons(priceDetail.getCartons());
                detail.setOrderValueAud(priceDetail.getOrderValueAud());
                detail.setOrderValueRmb(priceDetail.getOrderValueRmb());
                detail.setOrderValueUsd(priceDetail.getOrderValueUsd());
            }
        }
        return destDetails;
    }

    public List<PurchaseContractDetail> findDetailsByProductId(String productId){

        return purchaseContractDetailDao.findDetailsByProductId(productId);
    }


}
