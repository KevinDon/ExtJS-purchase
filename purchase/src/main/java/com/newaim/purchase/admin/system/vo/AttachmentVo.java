package com.newaim.purchase.admin.system.vo;

import com.newaim.purchase.admin.system.entity.MyDocument;

import java.io.Serializable;

public class AttachmentVo implements Serializable {

    private String id;
    private String businessId;
    private String documentId;
    private String moduleName;
    private String categoryId;

    private MyDocumentVo document = null;

    public AttachmentVo() {}
    public AttachmentVo(MyDocumentVo mydoc) {
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

    public MyDocumentVo getDocument() {
        return document;
    }

    public void setDocument(MyDocumentVo document) {
        this.document = document;
    }

}
