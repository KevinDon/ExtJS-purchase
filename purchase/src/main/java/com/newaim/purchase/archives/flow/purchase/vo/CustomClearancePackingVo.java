package com.newaim.purchase.archives.flow.purchase.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearance;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlanDetail;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanDetailVo;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingDetailVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CustomClearancePackingVo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -322604134552229896L;
	private String id;
    private String customClearanceId;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    /**批次号*/
    private String orderIndex;
    /**有配件*/
    private Integer accessories;


    /**预计发货、到岸、完货、装柜时间*/
    private Date etd;
    private Date eta;
    private Date readyDate;
    private Date packingDate;

    /**集装箱编号*/
    private String containerNumber;
    /**封印编号*/
    private String sealsNumber;
    /**柜号*/
    private String containerOrder;
    /**柜型*/
    private String containerType;
    /**对方订单号*/
    private String ciNumber;

    private String flowOrderShippingPlanId;
    private String orderShippingPlanId;

    private Integer flagWarehousePlanStatus;

    private Integer flagAsnStatus;
    /**挂起状态*/
    private Integer hold;
    private  Integer flagCostStatus;//成本计算完成标记
    private  String flagCostId;//成本id
    private Date flagCostTime;//成本计算完成时间

    /**关联详细表信息*/
    private List<CustomClearancePackingDetailVo> details = Lists.newArrayList();

    private WarehousePlanDetailVo warehousePlanDetail;

    private List<CustomClearancePackingVo> packingOrders = Lists.newArrayList();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CustomClearancePackingVo> getPackingOrders() {
		return packingOrders;
	}

	public void setPackingOrders(List<CustomClearancePackingVo> packingOrders) {
		this.packingOrders = packingOrders;
	}

	public Integer getAccessories() {
        return accessories;
    }

    public void setAccessories(Integer accessories) {
        this.accessories = accessories;
    }

    public String getCustomClearanceId() {
        return customClearanceId;
    }

    public void setCustomClearanceId(String customClearanceId) {
        this.customClearanceId = customClearanceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CustomClearancePackingDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<CustomClearancePackingDetailVo> details) {
        this.details = details;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getPackingDate() {
        return packingDate;
    }

    public void setPackingDate(Date packingDate) {
        this.packingDate = packingDate;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getSealsNumber() {
        return sealsNumber;
    }

    public void setSealsNumber(String sealsNumber) {
        this.sealsNumber = sealsNumber;
    }

    public String getContainerOrder() {
        return containerOrder;
    }

    public void setContainerOrder(String containerOrder) {
        this.containerOrder = containerOrder;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getCiNumber() {
        return ciNumber;
    }

    public void setCiNumber(String ciNumber) {
        this.ciNumber = ciNumber;
    }

    public String getOrderShippingPlanId() {
        return orderShippingPlanId;
    }

    public void setOrderShippingPlanId(String orderShippingPlanId) {
        this.orderShippingPlanId = orderShippingPlanId;
    }

    public WarehousePlanDetailVo getWarehousePlanDetail() {
        return warehousePlanDetail;
    }

    public void setWarehousePlanDetail(WarehousePlanDetailVo warehousePlanDetail) {
        this.warehousePlanDetail = warehousePlanDetail;
    }

    public Integer getFlagWarehousePlanStatus() {
        return flagWarehousePlanStatus;
    }

    public void setFlagWarehousePlanStatus(Integer flagWarehousePlanStatus) {
        this.flagWarehousePlanStatus = flagWarehousePlanStatus;
    }

    public Integer getFlagAsnStatus() {
        return flagAsnStatus;
    }

    public void setFlagAsnStatus(Integer flagAsnStatus) {
        this.flagAsnStatus = flagAsnStatus;
    }


    public String getFlowOrderShippingPlanId() {
        return flowOrderShippingPlanId;
    }

    public void setFlowOrderShippingPlanId(String flowOrderShippingPlanId) {
        this.flowOrderShippingPlanId = flowOrderShippingPlanId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getFlagCostStatus() {
        return flagCostStatus;
    }

    public void setFlagCostStatus(Integer flagCostStatus) {
        this.flagCostStatus = flagCostStatus;
    }

    public String getFlagCostId() {
        return flagCostId;
    }

    public void setFlagCostId(String flagCostId) {
        this.flagCostId = flagCostId;
    }

    public Date getFlagCostTime() {
        return flagCostTime;
    }

    public void setFlagCostTime(Date flagCostTime) {
        this.flagCostTime = flagCostTime;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }
}
