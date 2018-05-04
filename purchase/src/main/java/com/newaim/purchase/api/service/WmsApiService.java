package com.newaim.purchase.api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.api.dto.OmsApiDto;
import com.newaim.purchase.api.dto.WmsAsnCreateDto;
import com.newaim.purchase.api.dto.WmsAsnCreateResultDto;
import com.newaim.purchase.api.dto.WmsAsnGetAsnReceivingResultDto;
import com.newaim.purchase.api.dto.WmsAsnSkuDto;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanDetailVo;
import com.newaim.purchase.archives.product.dao.CostOrderDao;
import com.newaim.purchase.archives.product.dao.CostProductCostDao;
import com.newaim.purchase.archives.product.dao.CostProductDao;
import com.newaim.purchase.archives.product.entity.CostOrder;
import com.newaim.purchase.archives.product.entity.CostProduct;
import com.newaim.purchase.archives.product.entity.CostProductCost;
import com.newaim.purchase.flow.shipping.dao.FlowAsnDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;

/**
 * Created by Mark on 2017/12/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsApiService extends ServiceBase {


    @Autowired
    private TransferService transferService;

    @Autowired
    private FlowAsnDao flowAsnDao;

    @Autowired
    private FlowAsnPackingDao flowAsnPackingDao;

    @Autowired
    private FlowAsnPackingDetailDao flowAsnPackingDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;
    
    @Autowired
    private CostOrderDao costOrderDao;
    
    @Autowired
    private CostProductDao costProductDao;
    
    @Autowired
    private CostProductCostDao costProductCostDao;

    /**
     * 创建ASN
     * @return
     */
    public RestResult createAsn(String asnId) throws IOException {
        RestResult result = new RestResult();
        String method = "createASN";
        WmsAsnCreateDto wmsAsnCreateDto = new WmsAsnCreateDto();
        FlowAsn asn = flowAsnDao.findOne(asnId);
        if(asn != null){
            List<FlowAsnPacking> asnPackings = flowAsnPackingDao.findPackingsByBusinessId(asnId);
            FlowAsnPacking asnPacking = asnPackings.get(0);
            wmsAsnCreateDto.setPurchaseOrderNo(asnPacking.getOrderNumber());
            wmsAsnCreateDto.setWarehouse(asn.getWarehouseId());
            if(asn.getReceiveStartDate() != null){
                wmsAsnCreateDto.setExpectedArriveTimeFrom(DateFormatUtil.format(asn.getReceiveStartDate(), "yyyy-MM-dd HH:mm:ss"));
            }
            if(asn.getReceiveEndDate() != null){
                wmsAsnCreateDto.setExpectedArriveTimeTo(DateFormatUtil.format(asn.getReceiveEndDate(), "yyyy-MM-dd HH:mm:ss"));
            }
            PurchaseContract order = purchaseContractDao.findOne(asnPacking.getOrderId());
            wmsAsnCreateDto.setSupplierId(order.getVendorId());
            wmsAsnCreateDto.setSupplierName(order.getVendorEnName());
            wmsAsnCreateDto.setSupplierAddress1(order.getSellerEnAddress());
            wmsAsnCreateDto.setSupplierAddress2(null);
            wmsAsnCreateDto.setSupplierCity(null);
            wmsAsnCreateDto.setSupplierProvince(null);
            wmsAsnCreateDto.setSupplierCountry(null);
            wmsAsnCreateDto.setSupplierPostcode(null);
            wmsAsnCreateDto.setSupplierContact(order.getSellerContactEnName());
            wmsAsnCreateDto.setSupplierTel1(order.getSellerPhone());
            wmsAsnCreateDto.setSupplierTel2(null);
            wmsAsnCreateDto.setSupplierEmail(order.getSellerEmail());
            wmsAsnCreateDto.setSupplierFax(order.getSellerFax());
            CustomClearancePacking ccPacking = customClearancePackingDao.findOne(asnPacking.getCcPackingId());
            if(ccPacking != null){
                //TODO 可以在asnPacking中添加字段
                wmsAsnCreateDto.setContainerNo(ccPacking.getContainerNumber());
                customClearancePackingDao.save(ccPacking);
            }
            
            //grady
            List<FlowAsnPackingDetail> flowAsnPackingDetailList = flowAsnPackingDetailDao.findByBusinessId(asnId);
            List<String> asnProductIdList = Lists.newArrayList();
            Map<String,Integer> expectedQtyMap = new HashMap<String,Integer>();
            for(FlowAsnPackingDetail obj :flowAsnPackingDetailList){
            	asnProductIdList.add(obj.getProductId());
            	expectedQtyMap.put(obj.getProductId(), obj.getExpectedQty());
            }
            //grady costOrder
            CostOrder costOrder = costOrderDao.findCostOrderByOrderId(ccPacking.getOrderId());
            List<CostProduct> costProductList = costProductDao.findCostProductsByCostIdAndOrderId(costOrder.getCostId(),costOrder.getOrderId());
            if(costProductList != null && costProductList.size() > 0){
                List<WmsAsnSkuDto> dtoDetails = Lists.newArrayList();
                Map<String,CostProduct> costProductMap = new HashMap<String,CostProduct>();
                for (int i = 0; i < costProductList.size(); i++) {
                	CostProduct costProductObj = costProductList.get(i);
                	costProductMap.put(costProductObj.getProductId(), costProductObj);
                }
                for (int i = 0; i < asnProductIdList.size(); i++) {
                	String asnProductId = asnProductIdList.get(i);
                    WmsAsnSkuDto skuDto = new WmsAsnSkuDto();
                    CostProduct costProductObj = costProductMap.get(asnProductId);
                    if(costProductObj!=null){
	                    skuDto.setSku(costProductObj.getSku());
	                    skuDto.setExpectedQty(expectedQtyMap.get(costProductObj.getProductId()));
	                    CostProductCost costProductCost = costProductCostDao.findCostProductCostByCostIdAndProductId(costProductObj.getCostId(), costProductObj.getProductId());
	                    skuDto.setCost(costProductCost.getTotalCostAud()+"");
	                    dtoDetails.add(skuDto);
                    }
                }
                wmsAsnCreateDto.setDetail(dtoDetails);
            }
        }
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params.put("asns", wmsAsnCreateDto);
            String resultStr = transferService.transferInbound(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                OmsApiDto dto = jsonMapper.fromJson(resultStr, new TypeReference<OmsApiDto<WmsAsnCreateResultDto>>(){});
                if(dto != null){
                    if(Long.valueOf(dto.getStatus()) > 0){
                        result.setSuccess(true);
                        WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) dto.getData();
                        result.setSuccess(true).setData(resultDto.getAsnNo());
                    }else{
                        if(dto.getData() != null){
                            WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) dto.getData();
                            result.setSuccess(true).setData(resultDto.getAsnNo());
                        }
                        result.setSuccess(false);
                    }
                    result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 创建ASN
     * @return
     */
    public RestResult createAsn(CustomClearancePackingVo packingVo) throws IOException {
        RestResult result = new RestResult();
        String method = "createASN";
        WmsAsnCreateDto wmsAsnCreateDto = new WmsAsnCreateDto();
        if(packingVo != null){
            wmsAsnCreateDto.setPurchaseOrderNo(packingVo.getOrderNumber());
            WarehousePlanDetailVo warehousePlanDetailVo = packingVo.getWarehousePlanDetail();
            wmsAsnCreateDto.setWarehouse(warehousePlanDetailVo.getWarehouseId());
            if(warehousePlanDetailVo.getReceiveStartDate() != null){
                wmsAsnCreateDto.setExpectedArriveTimeFrom(DateFormatUtil.format(warehousePlanDetailVo.getReceiveStartDate(), "yyyy-MM-dd HH:mm:ss"));
            }
            if(warehousePlanDetailVo.getReceiveEndDate() != null){
                wmsAsnCreateDto.setExpectedArriveTimeTo(DateFormatUtil.format(warehousePlanDetailVo.getReceiveEndDate(), "yyyy-MM-dd HH:mm:ss"));
            }
            PurchaseContract order = purchaseContractDao.findOne(packingVo.getOrderId());
            wmsAsnCreateDto.setSupplierId(order.getVendorId());
            wmsAsnCreateDto.setSupplierName(order.getVendorEnName());
            wmsAsnCreateDto.setSupplierAddress1(order.getSellerEnAddress());
            wmsAsnCreateDto.setSupplierAddress2(null);
            wmsAsnCreateDto.setSupplierCity(null);
            wmsAsnCreateDto.setSupplierProvince(null);
            wmsAsnCreateDto.setSupplierCountry(null);
            wmsAsnCreateDto.setSupplierPostcode(null);
            wmsAsnCreateDto.setSupplierContact(order.getSellerContactEnName());
            wmsAsnCreateDto.setSupplierTel1(order.getSellerPhone());
            wmsAsnCreateDto.setSupplierTel2(null);
            wmsAsnCreateDto.setSupplierEmail(order.getSellerEmail());
            wmsAsnCreateDto.setSupplierFax(order.getSellerFax());
            wmsAsnCreateDto.setContainerNo(packingVo.getContainerNumber());

            List<CustomClearancePackingDetailVo> details = packingVo.getDetails();
            if(details != null && details.size() > 0){
                List<WmsAsnSkuDto> dtoDetails = Lists.newArrayList();
                for (int i = 0; i < details.size(); i++) {
                    CustomClearancePackingDetailVo detail = details.get(i);
                    WmsAsnSkuDto skuDto = new WmsAsnSkuDto();
                    skuDto.setSku(detail.getSku());
                    skuDto.setExpectedQty(detail.getPackingQty());
                    //grady
                    skuDto.setCost(detail.getPriceCostAud()+"");
                    dtoDetails.add(skuDto);
                }
                wmsAsnCreateDto.setDetail(dtoDetails);
            }
        }
        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put("asns", wmsAsnCreateDto);
            String resultStr = transferService.transferInbound(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                OmsApiDto dto = jsonMapper.fromJson(resultStr, new TypeReference<OmsApiDto<WmsAsnCreateResultDto>>(){});
                if(dto != null){
                    if(Long.valueOf(dto.getStatus()) > 0){
                        WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) dto.getData();
                        result.setSuccess(true).setData(resultDto.getAsnNo());
                    }else{
                        if(dto.getData() != null){
                            WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) dto.getData();
                            if(StringUtils.isNotBlank(resultDto.getAsnNo())){
                                result.setSuccess(true).setData(resultDto.getAsnNo());
                            }
                        }
                        result.setSuccess(false);
                    }
                    result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 取消ASN
     * @param asnNumber
     * @return
     * @throws IOException
     */
    public RestResult cancelAsn(String asnNumber) throws IOException {
        RestResult result = new RestResult();
        String method = "cancelASN";
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        params.put("asn_no", asnNumber);
        try {
            String resultStr = transferService.transferInbound(method, params);
            JsonMapper jsonMapper = new JsonMapper();
            OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class);
            if(dto != null){
                if(Long.valueOf(dto.getStatus()) > 0){
                	
                	//grady
                	FlowAsn asnObj = flowAsnDao.getByAsnNumber(asnNumber);
                    List<FlowAsnPacking> asnPackings = flowAsnPackingDao.findPackingsByBusinessId(asnObj.getId());
                    FlowAsnPacking asnPacking = asnPackings.get(0);
                    CustomClearancePacking ccPacking = customClearancePackingDao.findOne(asnPacking.getCcPackingId());
                    ccPacking.setFlagAsnStatus(2);
                    customClearancePackingDao.save(ccPacking);
                    result.setSuccess(true);
                }else{
                    result.setSuccess(false);
                }
                result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
            }else{
                result.setSuccess(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(e.getMessage());
        }

        return result;
    }

    /**
     * 通过asn获取实收数量
     * @param asnNumber
     * @return
     * @throws IOException
     */
    public RestResult getASNReceivingResult(String asnNumber) throws IOException {
        RestResult result = new RestResult();
        String method = "getASNReceivingResult";
        Map<String, Object> params = Maps.newHashMap();
        params.put("asn_no", StringUtils.split(asnNumber, ","));
        try {
            String resultStr = transferService.transferInbound(method, params);
            JsonMapper jsonMapper = new JsonMapper();
            OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class, WmsAsnGetAsnReceivingResultDto.class);
            if(dto != null){
                if(Long.valueOf(dto.getStatus()) > 0){
                    result.setSuccess(true);

                }else{
                    result.setSuccess(false);
                }
                result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
            }else{
                result.setSuccess(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(e.getMessage());
        }

        return result;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public String syncBarcode(String skus) throws IOException {
        String method = "syncBarcode";
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        params.put("skus", skus);
        return transferService.transferInbound(method, params);
    }
}
