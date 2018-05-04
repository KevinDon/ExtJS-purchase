package com.newaim.purchase.flow.purchase.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractDetailVo;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractDetail;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mark on 2017/9/18.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowPurchaseContractDetailService extends ServiceBase {

    @Autowired
    private FlowPurchaseContractDetailDao flowPurchaseContractDetailDao;

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowPurchaseContractDetailVo> findDetailsVoByBusinessId(String businessId){
		return BeanMapper.mapList(flowPurchaseContractDetailDao.findDetailsByBusinessId(businessId), FlowPurchaseContractDetail.class, FlowPurchaseContractDetailVo.class);
	}

	/**
	 * 根据合同id查找合并后的合同明细(相同sku会做价格和数量合并)
	 * @param orderId
	 * @return
	 */
	public List<FlowPurchaseContractDetailVo> findMergeDetailsVoByOrderId(String orderId){

		List<FlowPurchaseContractDetailVo> destDetails = Lists.newArrayList();
		Map<String, List<FlowPurchaseContractDetailVo>> temp = Maps.newHashMap();
		List<FlowPurchaseContractDetailVo> srcDetails = findDetailsVoByBusinessId(orderId);
		for (int i = 0; i < srcDetails.size(); i++) {
			FlowPurchaseContractDetailVo detail = srcDetails.get(i);
			String productId = detail.getProductId();
			if(temp.containsKey(productId)){
				temp.get(productId).add(detail);
			}else{
				List<FlowPurchaseContractDetailVo> newDetails = Lists.newArrayList();
				newDetails.add(detail);
				temp.put(productId, newDetails);
				destDetails.add(detail);
			}
		}

		//对有重复SKU做价格合并
		Map<String,FlowPurchaseContractDetailVo> priceMap = Maps.newHashMap();
		for (String productId : temp.keySet()) {
			List<FlowPurchaseContractDetailVo> details = temp.get(productId);
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
					FlowPurchaseContractDetailVo detail = details.get(i);
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
				FlowPurchaseContractDetailVo priceDetail = new FlowPurchaseContractDetailVo();
				priceDetail.setPriceAud(totalPriceAud.divide(totalOrderQty, 4));
				priceDetail.setPriceRmb(totalPriceRmb.divide(totalOrderQty, 4));
				priceDetail.setPriceUsd(totalPriceUsd.divide(totalOrderQty, 4));
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
			FlowPurchaseContractDetailVo detail = srcDetails.get(i);
			if(priceMap.containsKey(detail.getProductId())){
				FlowPurchaseContractDetailVo priceDetail = priceMap.get(detail.getProductId());
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

	/**
	 * 根据业务id返回所有明细
	 * @param businessId 业务ID
	 * @return detailVo集合
	 */
	public List<FlowPurchaseContractDetail> findDetailsByBusinessId(String businessId){
		return flowPurchaseContractDetailDao.findDetailsByBusinessId(businessId);
	}
}
