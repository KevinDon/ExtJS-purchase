package com.newaim.purchase.admin.system.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name="na_attachment")
public class Attachment implements Serializable {
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FA")})
	private String id;
	private String businessId;
	
	@Column(name = "document_id")
	private String documentId;
	private String moduleName;
	private String categoryId;
	
	@OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "document_id", insertable=false, updatable=false)
    private MyDocument document = null;

	public Attachment() {}
	public Attachment(MyDocument mydoc) {
	    this.setDocument(mydoc);
	    this.setDocumentId(mydoc.getId());
    }

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public MyDocument getDocument() {
		return document;
	}
	public void setDocument(MyDocument document) {
		this.document = document;
	}
}
