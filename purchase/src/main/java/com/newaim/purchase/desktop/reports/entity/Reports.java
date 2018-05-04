package com.newaim.purchase.desktop.reports.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_reports")
public class Reports implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "REPO")})
	private String id;
	private String title;
	private String businessId;
	private Integer businessType;   //申请单位置：null 仅一种申请单，1第一种申请单，2第二种申请单；目前仅产品分析、产品汇总、产品趋势分析报告使用
	private String serialNumber;
	private Date reportTime;
	private String vendorId;
	private String vendorCnName;
	private String vendorEnName;
	private String file;
	private String photos;
	private Integer result;
	private Integer status;
	private Date createdAt;
	private Date updatedAt;
	private Date confirmedAt;
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;
	private String confirmedId;
	private String confirmedCnName;
	private String confirmedEnName;
	private String remark;
	private String moduleName;
	private String orderId;
	private String orderNumber;
	private String orderTitle;

	@Transient
	private String reportFile;

	@Transient
	private String images;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    public Integer getBusinessType() {
        return businessType;
    }
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }
    public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorCnName() {
		return vendorCnName;
	}
	public void setVendorCnName(String vendorCnName) {
		this.vendorCnName = vendorCnName;
	}
	public String getVendorEnName() {
		return vendorEnName;
	}
	public void setVendorEnName(String vendorEnName) {
		this.vendorEnName = vendorEnName;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getConfirmedAt() {
		return confirmedAt;
	}
	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorCnName() {
		return creatorCnName;
	}
	public void setCreatorCnName(String creatorCnName) {
		this.creatorCnName = creatorCnName;
	}
	public String getCreatorEnName() {
		return creatorEnName;
	}
	public void setCreatorEnName(String creatorEnName) {
		this.creatorEnName = creatorEnName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentCnName() {
		return departmentCnName;
	}

	public void setDepartmentCnName(String departmentCnName) {
		this.departmentCnName = departmentCnName;
	}

	public String getDepartmentEnName() {
		return departmentEnName;
	}

	public void setDepartmentEnName(String departmentEnName) {
		this.departmentEnName = departmentEnName;
	}

	public String getConfirmedId() {
		return confirmedId;
	}
	public void setConfirmedId(String confirmedId) {
		this.confirmedId = confirmedId;
	}
	public String getConfirmedCnName() {
		return confirmedCnName;
	}
	public void setConfirmedCnName(String confirmedCnName) {
		this.confirmedCnName = confirmedCnName;
	}
	public String getConfirmedEnName() {
		return confirmedEnName;
	}
	public void setConfirmedEnName(String confirmedEnName) {
		this.confirmedEnName = confirmedEnName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}

	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

}
