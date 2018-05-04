package com.newaim.purchase.archives.flow.shipping.service;


import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationDao;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationPortDao;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationChargeItem;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationVo;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlanVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderQuotationService {

    @Autowired
    private ServiceProviderQuotationDao serviceProviderQuotationDao;

    @Autowired
    private ServiceProviderQuotationPortDao serviceProviderQuotationPortDao;

    @Autowired
    private ServiceProviderQuotationPortService serviceProviderQuotationPortService;

    @Autowired
    private ServiceProviderQuotationChargeItemService serviceProviderQuotationChargeItemService;

    /**
     * 查询供应商最新的报价
     * @param serviceProviderId
     * @param type
     * @return
     */
    public ServiceProviderQuotation findLatestServiceProviderQuotation(String serviceProviderId, String type){
        List<ServiceProviderQuotation> data = serviceProviderQuotationDao.findLatestServiceProviderQuotation(serviceProviderId, type);
        return data != null && data.size() > 0 ? data.get(0) : null;
    }

    /**
     * 通过id查找
     * @param id
     * @return
     */
    public ServiceProviderQuotation getServiceProviderQuotation(String id){
        return serviceProviderQuotationDao.findOne(id);
    }

    public ServiceProviderQuotationVo get(String id){
        ServiceProviderQuotation quotation = serviceProviderQuotationDao.findOne(id);
        ServiceProviderQuotationVo vo = BeanMapper.map(quotation, ServiceProviderQuotationVo.class);
        vo.setPorts(serviceProviderQuotationPortService.listPortVosByServiceProviderQuotationId(vo.getId()));
        vo.setChargeItems(serviceProviderQuotationChargeItemService.listChargeItemsVosByServiceProviderQuotationId(vo.getId()));
        return vo;
    }


    /**
     * 查询指定发货计划相关的报价
     * @param flowOrderShippingPlanId 发货计划id
     * @return
     */
    public List<ServiceProviderQuotation> findQuotationsByFlowOrderShippingPlanId(String flowOrderShippingPlanId){
        return serviceProviderQuotationDao.findQuotationsByFlowOrderShippingPlanId(flowOrderShippingPlanId);
    }

    /**
     * 查询指定发货计划相关的报价
     * @param flowOrderShippingPlanId 发货计划id
     * @return
     */
    public List<ServiceProviderQuotationVo> findQuotationVosByFlowOrderShippingPlanId(String flowOrderShippingPlanId){
        return convertToServiceProviderQuotationListVo(findQuotationsByFlowOrderShippingPlanId(flowOrderShippingPlanId));
    }

    /**
     *  根据条件查询相关的报价
     * @param params 参数对象
     * @return
     */
    public List<ServiceProviderQuotationVo> findQuotationVosByParams(ServiceProviderQuotationVo params){
        List<ServiceProviderQuotation> data = serviceProviderQuotationDao.findQuotationsByParams(params);
        return convertToServiceProviderQuotationListVo(data);
    }

    private List<ServiceProviderQuotationVo> convertToServiceProviderQuotationListVo(List<ServiceProviderQuotation> data){
        List<ServiceProviderQuotationVo> result = BeanMapper.mapList(data, ServiceProviderQuotation.class, ServiceProviderQuotationVo.class);
        if(result != null && result.size() > 0){
            for (int i = 0; i < result.size(); i++) {
                ServiceProviderQuotationVo vo = result.get(i);
                vo.setPorts(serviceProviderQuotationPortService.listPortVosByServiceProviderQuotationId(vo.getId()));
                vo.setChargeItems(serviceProviderQuotationChargeItemService.listChargeItemsVosByServiceProviderQuotationId(vo.getId()));
            }
        }
        return result;
    }


    /**
     *  根据条件查询相关的报价
     * @param queryParams 参数对象
     * @return
     */
    public List<SpQuotationForOrderPlanVo> findQuotationsVoForOrderPlanByParams(List<SpQuotationForOrderPlanVo> queryParams){
        List<ServiceProviderQuotationPort> data = serviceProviderQuotationPortDao.findQuotationPortsForOrderPlan(queryParams);
        List<SpQuotationForOrderPlanVo> result = Lists.newArrayList();
        if(data != null){
            for (int i = 0; i < data.size(); i++) {
                ServiceProviderQuotationPort port = data.get(i);
                SpQuotationForOrderPlanVo vo = new SpQuotationForOrderPlanVo();
                ServiceProviderQuotation quotation = serviceProviderQuotationDao.findOne(port.getServiceProviderQuotationId());
                vo.setServiceProviderQuotationId(port.getServiceProviderQuotationId());
                vo.setCurrency(port.getCurrency());
                vo.setRateAudToRmb(port.getRateAudToRmb());
                vo.setRateAudToUsd(port.getRateAudToUsd());
                vo.setOriginPortId(port.getOriginPortId());
                vo.setSailingDays(port.getSailingDays());
                vo.setOriginPortCnName(port.getOriginPortCnName());
                vo.setOriginPortEnName(port.getOriginPortEnName());
                vo.setDestinationPortId(port.getDestinationPortId());
                vo.setDestinationPortCnName(port.getDestinationPortCnName());
                vo.setDestinationPortEnName(port.getDestinationPortEnName());
                vo.setServiceProviderId(quotation.getServiceProviderId());
                vo.setServiceProviderCnName(quotation.getServiceProviderCnName());
                vo.setServiceProviderEnName(quotation.getServiceProviderEnName());
                vo.setEffectiveDate(quotation.getEffectiveDate());
                vo.setValidUntil(quotation.getValidUntil());
                for (int j = 0; j < queryParams.size(); j++) {
                    SpQuotationForOrderPlanVo param = queryParams.get(j);
                    if(StringUtils.equals(vo.getOriginPortId(), param.getOriginPortId())
                            && StringUtils.equals(vo.getDestinationPortId(), param.getDestinationPortId())
                            && param.getEtd().getTime() >= vo.getEffectiveDate().getTime()
                            && param.getEtd().getTime() <= vo.getValidUntil().getTime()){
                        if(StringUtils.equals(param.getContainerType(), "1")){
                            vo.setPriceGp20Aud(port.getPriceGp20Aud());
                            vo.setPriceGp20Rmb(port.getPriceGp20Rmb());
                            vo.setPriceGp20Usd(port.getPriceGp20Usd());
                            vo.setPriceGp20InsuranceAud(port.getPriceGp20InsuranceAud());
                            vo.setPriceGp20InsuranceRmb(port.getPriceGp20InsuranceRmb());
                            vo.setPriceGp20InsuranceUsd(port.getPriceGp20InsuranceUsd());
                            vo.setGp20Qty(param.getContainerQty());
                            vo.setContainerType("1");
                        }else if(StringUtils.equals(param.getContainerType(), "2")){
                            vo.setPriceGp40Aud(port.getPriceGp40Aud());
                            vo.setPriceGp40Rmb(port.getPriceGp40Rmb());
                            vo.setPriceGp40Usd(port.getPriceGp40Usd());
                            vo.setPriceGp40InsuranceAud(port.getPriceGp40InsuranceAud());
                            vo.setPriceGp40InsuranceRmb(port.getPriceGp40InsuranceRmb());
                            vo.setPriceGp40InsuranceUsd(port.getPriceGp40InsuranceUsd());
                            vo.setGp40Qty(param.getContainerQty());
                            vo.setContainerType("2");
                        }else if(StringUtils.equals(param.getContainerType(), "3")){
                            vo.setPriceHq40Aud(port.getPriceHq40Aud());
                            vo.setPriceHq40Rmb(port.getPriceHq40Rmb());
                            vo.setPriceHq40Usd(port.getPriceHq40Usd());
                            vo.setPriceHq40InsuranceAud(port.getPriceHq40InsuranceAud());
                            vo.setPriceHq40InsuranceRmb(port.getPriceHq40InsuranceRmb());
                            vo.setPriceHq40InsuranceUsd(port.getPriceHq40InsuranceUsd());
                            vo.setHq40Qty(param.getContainerQty());
                            vo.setContainerType("3");
                        }else if(StringUtils.equals(param.getContainerType(), "4")){
                            vo.setPriceLclAud(port.getPriceLclAud());
                            vo.setPriceLclRmb(port.getPriceLclRmb());
                            vo.setPriceLclUsd(port.getPriceLclUsd());
                            vo.setPriceLclInsuranceAud(port.getPriceLclInsuranceAud());
                            vo.setPriceLclInsuranceRmb(port.getPriceLclInsuranceRmb());
                            vo.setPriceLclInsuranceUsd(port.getPriceLclInsuranceUsd());
                            vo.setLclCbm(param.getContainerQty());
                            vo.setContainerType("4");
                        }
                        if(StringUtils.isNotBlank(vo.getOrderNumber())){
                            vo.setOrderNumber(vo.getOrderNumber() + "," + param.getOrderNumber());
                        }else{
                            vo.setOrderNumber(param.getOrderNumber());
                        }
                    }
                }
                if ("1".equals(vo.getContainerType()) && (vo.getPriceGp20Aud() == null || vo.getPriceGp20Aud().doubleValue() == 0) ){
                    continue;
                }
                if ("2".equals(vo.getContainerType()) && (vo.getPriceGp40Aud() == null || vo.getPriceGp40Aud().doubleValue() == 0) ){
                    continue;
                }
                if ("3".equals(vo.getContainerType()) &&( vo.getPriceHq40Aud() == null || vo.getPriceHq40Aud().doubleValue() == 0 ) ){
                    continue;
                }
                if ("4".equals(vo.getContainerType()) && (vo.getPriceLclAud() == null || vo.getPriceLclAud().doubleValue() == 0 ) ){
                    continue;
                }
                BigDecimal totalPriceChargeItemAud = BigDecimal.ZERO;
                BigDecimal totalPriceChargeItemRmb = BigDecimal.ZERO;
                BigDecimal totalPriceChargeItemUsd = BigDecimal.ZERO;
                BigDecimal totalPriceGp20Aud = BigDecimal.ZERO;
                BigDecimal totalPriceGp20Rmb = BigDecimal.ZERO;
                BigDecimal totalPriceGp20Usd = BigDecimal.ZERO;
                BigDecimal totalPriceGp40Aud = BigDecimal.ZERO;
                BigDecimal totalPriceGp40Rmb = BigDecimal.ZERO;
                BigDecimal totalPriceGp40Usd = BigDecimal.ZERO;
                BigDecimal totalPriceHq40Aud = BigDecimal.ZERO;
                BigDecimal totalPriceHq40Rmb = BigDecimal.ZERO;
                BigDecimal totalPriceHq40Usd = BigDecimal.ZERO;
                BigDecimal totalPriceLclAud = BigDecimal.ZERO;
                BigDecimal totalPriceLclRmb = BigDecimal.ZERO;
                BigDecimal totalPriceLclUsd = BigDecimal.ZERO;
                BigDecimal totalPriceOtherAud = BigDecimal.ZERO;
                BigDecimal totalPriceOtherRmb = BigDecimal.ZERO;
                BigDecimal totalPriceOtherUsd = BigDecimal.ZERO;
                List<ServiceProviderQuotationChargeItem> chargeItems = serviceProviderQuotationChargeItemService.listChargeItemsByServiceProviderQuotationId(port.getServiceProviderQuotationId());
                if(chargeItems != null){
                    for (int j = 0; j < chargeItems.size(); j++) {
                        ServiceProviderQuotationChargeItem chargeItem = chargeItems.get(j);
                        if(StringUtils.equals(chargeItem.getUnitId(), "1")){
                            //每柜
                            if(vo.getPriceGp20Aud() != null && vo.getGp20Qty() != null){
                                totalPriceGp20Aud = totalPriceGp20Aud.add(vo.getPriceGp20Aud().multiply(vo.getGp20Qty()));
                                totalPriceGp20Rmb = totalPriceGp20Rmb.add(vo.getPriceGp20Rmb().multiply(vo.getGp20Qty()));
                                totalPriceGp20Usd = totalPriceGp20Usd.add(vo.getPriceGp20Usd().multiply(vo.getGp20Qty()));
                            }
                            if(vo.getPriceGp40Aud() != null && vo.getGp40Qty() != null){
                                totalPriceGp40Aud = totalPriceGp40Aud.add(vo.getPriceGp40Aud().multiply(vo.getGp40Qty()));
                                totalPriceGp40Rmb = totalPriceGp40Rmb.add(vo.getPriceGp40Rmb().multiply(vo.getGp40Qty()));
                                totalPriceGp40Usd = totalPriceGp40Usd.add(vo.getPriceGp40Usd().multiply(vo.getGp40Qty()));
                            }
                            if(vo.getPriceHq40Aud() != null && vo.getHq40Qty() != null){
                                totalPriceHq40Aud = totalPriceHq40Aud.add(vo.getPriceHq40Aud().multiply(vo.getHq40Qty()));
                                totalPriceHq40Rmb = totalPriceHq40Rmb.add(vo.getPriceHq40Rmb().multiply(vo.getHq40Qty()));
                                totalPriceHq40Usd = totalPriceHq40Usd.add(vo.getPriceHq40Usd().multiply(vo.getHq40Qty()));
                            }
                            if(vo.getPriceLclAud() != null && vo.getHq40Qty() != null){
                                totalPriceLclAud = totalPriceLclAud.add(vo.getPriceLclAud().multiply(vo.getHq40Qty()));
                                totalPriceLclRmb = totalPriceLclRmb.add(vo.getPriceLclRmb().multiply(vo.getHq40Qty()));
                                totalPriceLclUsd = totalPriceLclUsd.add(vo.getPriceLclUsd().multiply(vo.getHq40Qty()));
                            }
                        }else{
                            totalPriceOtherAud = totalPriceOtherAud.add(vo.getPriceOtherAud());
                            totalPriceOtherRmb = totalPriceOtherRmb.add(vo.getPriceOtherRmb());
                            totalPriceOtherUsd = totalPriceOtherUsd.add(vo.getPriceOtherUsd());
                        }
                    }
                    totalPriceChargeItemAud = totalPriceGp20Aud.add(totalPriceGp40Aud).add(totalPriceHq40Aud).add(totalPriceLclAud).add(totalPriceOtherAud);
                    totalPriceChargeItemRmb = totalPriceGp20Rmb.add(totalPriceGp40Rmb).add(totalPriceHq40Rmb).add(totalPriceLclRmb).add(totalPriceOtherRmb);
                    totalPriceChargeItemUsd = totalPriceGp20Usd.add(totalPriceGp40Usd).add(totalPriceHq40Usd).add(totalPriceLclUsd).add(totalPriceOtherUsd);
                    vo.setTotalPriceChargeItemAud(totalPriceChargeItemAud);
                    vo.setTotalPriceChargeItemRmb(totalPriceChargeItemRmb);
                    vo.setTotalPriceChargeItemUsd(totalPriceChargeItemUsd);
                    BigDecimal totalPriceAud = totalPriceChargeItemAud;
                    BigDecimal totalPriceRmb = totalPriceChargeItemRmb;
                    BigDecimal totalPriceUsd = totalPriceChargeItemUsd;
                    if(vo.getPriceGp20Aud() != null && vo.getGp20Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceGp20Aud().multiply(vo.getGp20Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceGp20Rmb().multiply(vo.getGp20Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceGp20Usd().multiply(vo.getGp20Qty()));
                    }
                    if(vo.getPriceGp20InsuranceAud() != null && vo.getGp20Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceGp20InsuranceAud().multiply(vo.getGp20Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceGp20InsuranceRmb().multiply(vo.getGp20Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceGp20InsuranceUsd().multiply(vo.getGp20Qty()));
                    }
                    if(vo.getPriceGp40Aud() != null && vo.getGp40Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceGp40Aud().multiply(vo.getGp40Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceGp40Rmb().multiply(vo.getGp40Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceGp40Usd().multiply(vo.getGp40Qty()));
                    }
                    if(vo.getPriceGp40InsuranceAud() != null && vo.getGp40Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceGp40InsuranceAud().multiply(vo.getGp40Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceGp40InsuranceRmb().multiply(vo.getGp40Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceGp40InsuranceUsd().multiply(vo.getGp40Qty()));
                    }
                    if(vo.getPriceHq40Aud() != null && vo.getHq40Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceHq40Aud().multiply(vo.getHq40Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceHq40Rmb().multiply(vo.getHq40Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceHq40Usd().multiply(vo.getHq40Qty()));
                    }
                    if(vo.getPriceHq40InsuranceAud() != null && vo.getHq40Qty() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceHq40InsuranceAud().multiply(vo.getHq40Qty()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceHq40InsuranceRmb().multiply(vo.getHq40Qty()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceHq40InsuranceUsd().multiply(vo.getHq40Qty()));
                    }
                    if(vo.getPriceLclAud() != null && vo.getLclCbm() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceLclAud().multiply(vo.getLclCbm()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceLclRmb().multiply(vo.getLclCbm()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceLclUsd().multiply(vo.getLclCbm()));
                    }
                    if(vo.getPriceLclInsuranceAud() != null && vo.getLclCbm() != null){
                        totalPriceAud = totalPriceAud.add(vo.getPriceLclInsuranceAud().multiply(vo.getLclCbm()));
                        totalPriceRmb = totalPriceRmb.add(vo.getPriceLclInsuranceRmb().multiply(vo.getLclCbm()));
                        totalPriceUsd = totalPriceUsd.add(vo.getPriceLclInsuranceUsd().multiply(vo.getLclCbm()));
                    }
                    vo.setTotalPriceAud(totalPriceAud);
                    vo.setTotalPriceRmb(totalPriceRmb);
                    vo.setTotalPriceUsd(totalPriceUsd);

                }
                result.add(vo);
            }
        }
        return result;
    }
}
