package com.newaim.purchase.archives.product.vo;

import java.io.Serializable;
import java.util.Date;

public class TroubleTicketAsnVo implements Serializable {

    private String id;

    private String troubleTicketId;

    private String asnId;
    private Date receiveDate;
    private String asnNumber;
    private String receiveLocation;
    private String warehouseId;
    private String asnCreatedAt;
    private String asnCreatorId;
    private String asnCreatorCnName;
    private String asnCreatorEnName;
    private Integer asnStatus;
    private Integer asnFlowStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTroubleTicketId() {
        return troubleTicketId;
    }

    public void setTroubleTicketId(String troubleTicketId) {
        this.troubleTicketId = troubleTicketId;
    }

    public String getAsnId() {
        return asnId;
    }

    public void setAsnId(String asnId) {
        this.asnId = asnId;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getAsnCreatedAt() {
        return asnCreatedAt;
    }

    public void setAsnCreatedAt(String asnCreatedAt) {
        this.asnCreatedAt = asnCreatedAt;
    }

    public String getAsnCreatorId() {
        return asnCreatorId;
    }

    public void setAsnCreatorId(String asnCreatorId) {
        this.asnCreatorId = asnCreatorId;
    }

    public String getAsnCreatorCnName() {
        return asnCreatorCnName;
    }

    public void setAsnCreatorCnName(String asnCreatorCnName) {
        this.asnCreatorCnName = asnCreatorCnName;
    }

    public String getAsnCreatorEnName() {
        return asnCreatorEnName;
    }

    public void setAsnCreatorEnName(String asnCreatorEnName) {
        this.asnCreatorEnName = asnCreatorEnName;
    }

    public Integer getAsnStatus() {
        return asnStatus;
    }

    public void setAsnStatus(Integer asnStatus) {
        this.asnStatus = asnStatus;
    }

    public Integer getAsnFlowStatus() {
        return asnFlowStatus;
    }

    public void setAsnFlowStatus(Integer asnFlowStatus) {
        this.asnFlowStatus = asnFlowStatus;
    }
}
