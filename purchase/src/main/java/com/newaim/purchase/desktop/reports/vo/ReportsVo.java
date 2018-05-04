package com.newaim.purchase.desktop.reports.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;

public class ReportsVo implements Serializable{

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
	private String orderId;
	private String orderNumber;
	private String orderTitle;
	private String moduleName;
	
	//其它附件
	private List<AttachmentVo> attachments = Lists.newArrayList();
	//图片原型
    private List<AttachmentVo> imagesDoc = Lists.newArrayList();
    //图片ID
    private String images;
    //报告文件
    private AttachmentVo reportFile;
    //供应商
	private VendorVo vendor;
	private String vendorName;

	//相关产品
	private List<ReportsProductVo> details = Lists.newArrayList();

	//相关标准信息（仅安检报告中使用）
	private ReportsComplianceVo compliance = null;


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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<AttachmentVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentVo> attachments) {
		this.attachments = attachments;
	}

	public List<AttachmentVo> getImagesDoc() {
		return imagesDoc;
	}

	public void setImagesDoc(List<AttachmentVo> imagesDoc) {
		this.imagesDoc = imagesDoc;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public AttachmentVo getReportFile() {
		return reportFile;
	}

	public void setReportFile(AttachmentVo reportFile) {
		this.reportFile = reportFile;
	}

	public VendorVo getVendor() {
		return vendor;
	}

	public void setVendor(VendorVo vendor) {
		this.vendor = vendor;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<ReportsProductVo> getDetails() {
		return details;
	}

	public void setDetails(List<ReportsProductVo> details) {
		this.details = details;
	}

    public ReportsComplianceVo getCompliance() {
        return compliance;
    }

    public void setCompliance(ReportsComplianceVo compliance) {
        this.compliance = compliance;
    }
}
