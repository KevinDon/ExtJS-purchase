package com.newaim.purchase.archives.flow.purchase.service;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearance;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlan;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlanDetail;
import com.newaim.purchase.archives.flow.shipping.service.OrderShippingPlanDetailService;
import com.newaim.purchase.archives.flow.shipping.service.OrderShippingPlanService;
import com.newaim.purchase.archives.flow.shipping.service.WarehousePlanDetailService;
import com.newaim.purchase.archives.flow.shipping.service.WarehousePlanService;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanDetailVo;
import com.newaim.purchase.archives.product.entity.CostProductCost;
import com.newaim.purchase.archives.product.service.CostProductCostService;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingPlanDetailService;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CustomClearancePackingService extends ServiceBase {


    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;

    @Autowired
    private CustomClearanceService customClearanceService;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private OrderShippingPlanService orderShippingPlanService;

    @Autowired
    private FlowOrderShippingPlanDetailService flowOrderShippingPlanDetailService;

    @Autowired
    private OrderShippingPlanDetailService orderShippingPlanDetailService;

    @Autowired
    private WarehousePlanDetailService warehousePlanDetailService;

    @Autowired
    private WarehousePlanService warehousePlanService;

    @Autowired
    private CostProductCostService costProductCostService;
    /**
     * 查询所有装柜单信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<CustomClearancePackingVo> listPacking(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<CustomClearancePacking> spec = buildSpecification(params);
        Page<CustomClearancePacking> p = customClearancePackingDao.findAll(spec, pageRequest);
        Page<CustomClearancePackingVo> page = p.map(customClearancePacking ->{
            CustomClearancePackingVo vo = convertToCustomClearancePackingVo(customClearancePacking);
            //根据订单ID获取订单信息
            PurchaseContract purchaseContract = purchaseContractService.getPurchaseContract(vo.getOrderId());
            //根据订单ID获得发货计划流程ID和正式业务的发货ID
            OrderShippingPlanDetail orderShippingPlanDetail = orderShippingPlanDetailService.findTopOrderShippingPlanDetailByOrderId(purchaseContract.getId());
            if(orderShippingPlanDetail != null){
                vo.setOrderShippingPlanId(orderShippingPlanDetail.getOrderShippingPlanId());
            }
            FlowOrderShippingPlanDetail flowOrderShippingPlanDetail = flowOrderShippingPlanDetailService.findTopFlowOrderShippingPlanDetailByOrderId(purchaseContract.getId());
            if (flowOrderShippingPlanDetail!=null){
                vo.setFlowOrderShippingPlanId(flowOrderShippingPlanDetail.getBusinessId());
            }
            //获取清关时间信息
            CustomClearance customClearance = customClearanceService.getCustomClearance(vo.getCustomClearanceId());
            vo.setEta(customClearance.getEta());
            vo.setEtd(customClearance.getEtd());
            vo.setReadyDate(customClearance.getReadyDate());
            vo.setPackingDate(customClearance.getPackingDate());
            //获得装柜单详细信息
            vo.setDetails(customClearancePackingDetailService.findPackingDetailsVoByPackingId(vo.getId()));
            List<CustomClearancePackingDetailVo> detail = vo.getDetails();
            if (detail!=null){
                for (int i = 0;i<detail.size();i++){
                    CustomClearancePackingDetailVo detailVo = detail.get(i);
                    detailVo.setVendorEnName(purchaseContract.getVendorEnName());
                    detailVo.setVendorCnName(purchaseContract.getVendorCnName());
                    detailVo.setFlagOrderQcStatus(purchaseContract.getFlagOrderQcStatus());
                    detailVo.setOrderIndex(purchaseContract.getOrderIndex());
                    detailVo.setOrderTitle(purchaseContract.getOrderTitle());
                    detailVo.setOrderNumber(purchaseContract.getOrderNumber());
                }
            }
            List<CustomClearancePackingVo> packingOrders = Lists.newArrayList();
            List<CustomClearancePacking> customClearancePackings = customClearancePackingDao.findPackingsByContainerNumber(vo.getContainerNumber());
            for (int i = 0; i < customClearancePackings.size(); i++) {
                CustomClearancePackingVo packing = convertToCustomClearancePackingVo(customClearancePackings.get(i));
                //根据订单ID获取订单信息
                PurchaseContract order = purchaseContractService.getPurchaseContract(packing.getOrderId());
                //根据订单ID获得发货计划流程ID和正式业务的发货ID
                OrderShippingPlanDetail planDetail = orderShippingPlanDetailService.findTopOrderShippingPlanDetailByOrderId(order.getId());
                if(planDetail != null){
                    packing.setOrderShippingPlanId(planDetail.getOrderShippingPlanId());
                }
                FlowOrderShippingPlanDetail flowPlanDetail = flowOrderShippingPlanDetailService.findTopFlowOrderShippingPlanDetailByOrderId(order.getId());
                if (flowPlanDetail!=null){
                    packing.setFlowOrderShippingPlanId(flowPlanDetail.getBusinessId());
                }
                //获取清关时间信息
                CustomClearance cc = customClearanceService.getCustomClearance(packing.getCustomClearanceId());
                packing.setEta(cc.getEta());
                packing.setEtd(cc.getEtd());
                packing.setReadyDate(cc.getReadyDate());
                packing.setPackingDate(cc.getPackingDate());
                packing.setOrderIndex(order.getOrderIndex());
                packingOrders.add(packing);
            }
            vo.setPackingOrders(packingOrders);
            return vo;
        });
        return page;
    }
    /**
     * 查询所有装柜单信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<CustomClearancePackingVo> listPackingForAsn(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);

        List<WarehousePlanDetail> warehousePlanDetails = warehousePlanDetailService.findWarehousePlanDetails();
        String containerNumbersStr = "";
        Map<String, WarehousePlanDetailVo> containerNumberMap = Maps.newHashMap();
        if(warehousePlanDetails != null && warehousePlanDetails.size() > 0){
            String[] containerNumbers = new String[warehousePlanDetails.size()];
            for (int i = 0; i < warehousePlanDetails.size(); i++) {
                WarehousePlanDetail detail = warehousePlanDetails.get(i);
                containerNumbers[i] = detail.getContainerNumber();
                WarehousePlanDetailVo warehousePlanDetailVo = BeanMapper.map(detail, WarehousePlanDetailVo.class);
                WarehousePlan warehousePlan = warehousePlanService.getWarehousePlan(detail.getWarehousePlanId());
                warehousePlanDetailVo.setWarehouseId(warehousePlan.getWarehouseId());
                warehousePlanDetailVo.setFlowWarehousePlanId(warehousePlan.getBusinessId());
                warehousePlanDetailVo.setOriginPlace(warehousePlan.getOriginPlace());
                warehousePlanDetailVo.setDestinationPlace(warehousePlan.getDestinationPlace());
                warehousePlanDetailVo.setReceiveDate(warehousePlan.getReceiveDate());
                warehousePlanDetailVo.setReceiveStartDate(warehousePlan.getReceiveStartDate());
                warehousePlanDetailVo.setReceiveEndDate(warehousePlan.getReceiveEndDate());
                containerNumberMap.put(detail.getContainerNumber(), warehousePlanDetailVo);
            }
            containerNumbersStr = StringUtils.joinWith(",", containerNumbers);
        }
        params.put("containerNumber-S-IN", containerNumbersStr);
        Specification<CustomClearancePacking> spec = buildSpecification(params);
        Page<CustomClearancePacking> p = customClearancePackingDao.findAll(spec, pageRequest);
        Page<CustomClearancePackingVo> page = p.map(customClearancePacking ->{
            CustomClearancePackingVo vo = convertToCustomClearancePackingVo(customClearancePacking);
            //根据订单ID获取订单信息
            PurchaseContract purchaseContract = purchaseContractService.getPurchaseContract(vo.getOrderId());
            //根据订单ID获得发货计划流程ID和正式业务的发货ID
            OrderShippingPlanDetail orderShippingPlanDetail = orderShippingPlanDetailService.findTopOrderShippingPlanDetailByOrderId(purchaseContract.getId());
            if(orderShippingPlanDetail != null){
                vo.setOrderShippingPlanId(orderShippingPlanDetail.getOrderShippingPlanId());
            }
            FlowOrderShippingPlanDetail flowOrderShippingPlanDetail = flowOrderShippingPlanDetailService.findTopFlowOrderShippingPlanDetailByOrderId(purchaseContract.getId());
            if (flowOrderShippingPlanDetail!=null){
                vo.setFlowOrderShippingPlanId(flowOrderShippingPlanDetail.getBusinessId());
            }
            //获取清关时间信息
            CustomClearance customClearance = customClearanceService.getCustomClearance(vo.getCustomClearanceId());
            vo.setEta(customClearance.getEta());
            vo.setEtd(customClearance.getEtd());
            vo.setReadyDate(customClearance.getReadyDate());
            vo.setPackingDate(customClearance.getPackingDate());
            vo.setAccessories(customClearance.getAccessories());
            vo.setWarehousePlanDetail(containerNumberMap.get(vo.getContainerNumber()));
            //获得装柜单详细信息
            vo.setDetails(customClearancePackingDetailService.findPackingDetailsVoByPackingId(vo.getId()));
            List<CustomClearancePackingDetailVo> details = vo.getDetails();
            if (details!=null){
                for (int i = 0;i<details.size();i++){
                    CustomClearancePackingDetailVo detail = details.get(i);
                    detail.setVendorEnName(purchaseContract.getVendorEnName());
                    detail.setVendorCnName(purchaseContract.getVendorCnName());
                    detail.setFlagOrderQcStatus(purchaseContract.getFlagOrderQcStatus());
                    detail.setOrderIndex(purchaseContract.getOrderIndex());
                    detail.setOrderTitle(purchaseContract.getOrderTitle());
                    if(StringUtils.isNotBlank(vo.getOrderShippingPlanId())){
                        CostProductCost costProductCost = costProductCostService.findByOrderShippingPlanIdAndProductId(vo.getOrderShippingPlanId(), detail.getProductId());
                        if(costProductCost != null){
                            detail.setPriceCostAud(costProductCost.getTotalCostAud());
                            detail.setPriceCostRmb(costProductCost.getTotalCostRmb());
                            detail.setPriceCostUsd(costProductCost.getTotalCostUsd());

                        }
                    }
                    detail.setRateAudToRmb(purchaseContract.getRateAudToRmb());
                    detail.setRateAudToUsd(purchaseContract.getRateAudToUsd());
                    detail.setCurrency(purchaseContract.getCurrency());
                }
            }
            return vo;
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param customClearancePacking
     * @return
     */
    private  CustomClearancePackingVo convertToCustomClearancePackingVo( CustomClearancePacking customClearancePacking){
        CustomClearancePackingVo vo = BeanMapper.map(customClearancePacking,CustomClearancePackingVo.class);
        return vo;
    }

    /**
     * 动态建立关联
     * @param searchParams
     * @return
     */
    private Specification<CustomClearancePacking> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<CustomClearancePacking> spec = DynamicSpecifications.bySearchFilter(filters.values(),  CustomClearancePacking.class);
        return spec;
    }


    /**
     * 根据清关ID获取请关单
     * @param customClearanceId
     * @return detail集合
     */
    public List<CustomClearancePacking> findPackingsByCustomClearanceId(String customClearanceId){
        return customClearancePackingDao.findPackingsByCustomClearanceId(customClearanceId);
    }

    /**
     * 根据清关ID获取请关单
     * @param customClearanceId
     * @return
     */
    public List<CustomClearancePackingVo> findPackingsVoByCustomClearanceId(String customClearanceId){
        List<CustomClearancePacking> list = findPackingsByCustomClearanceId(customClearanceId);
        List<CustomClearancePackingVo> voList = new ArrayList<CustomClearancePackingVo>();
        for(int i = 0; i < list.size(); i++){
            CustomClearancePackingVo vo = convertToCustomClearancePackingVo(list.get(i));
            vo.setDetails(customClearancePackingDetailService.findPackingDetailsVoByPackingId(vo.getCustomClearanceId()));
            voList.add(vo);
        }

        return  voList;
    }

    /**
     * 根据清关ID获取柜明细
     * @param customClearanceId
     * @return
     */
    public CustomClearancePacking getCustomClearancePacking(String customClearanceId){
        return customClearancePackingDao.findOne(customClearanceId);
    }

    public List<CustomClearancePacking> findPackingsByOrderIds(List<String> orderIds){
        return customClearancePackingDao.findPackingsByOrderIdIn(orderIds);
    }


    /**
     * 根据柜编号获取柜信息
     * @param containerNumber
     * @return
     */
    public List<CustomClearancePacking> getPackingByContainerNumber(String containerNumber){
        return customClearancePackingDao.findPackingsByContainerNumber(containerNumber);
    }


    /**
     * 通过订单id查找装柜单
     * @param orderIds
     * @return
     */
    public List<CustomClearancePackingVo> findPackingsVoByOrderIds(List<String> orderIds){
        List<CustomClearancePackingVo> result = BeanMapper.mapList(findPackingsByOrderIds(orderIds), CustomClearancePacking.class, CustomClearancePackingVo.class);
        if(result != null){
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setDetails(customClearancePackingDetailService.findPackingDetailsVoByPackingId(result.get(i).getId()));
            }
        }
        return result;
    }

    /**
     * 根据清关ID获取请关单明细
     * @param id
     * @return
     */
    public CustomClearancePackingVo get(String id){
        CustomClearancePackingVo vo = convertToCustomClearancePackingVo(getCustomClearancePacking(id));
        vo.setDetails(customClearancePackingDetailService.findPackingDetailsVoByPackingId(vo.getId()));
        return vo;
    }


    public CustomClearancePacking findOne(String id) {
        return customClearancePackingDao.findOne(id);
    }

    public void save(CustomClearancePacking customClearancePacking) {

        customClearancePackingDao.save(customClearancePacking);
    }
}
