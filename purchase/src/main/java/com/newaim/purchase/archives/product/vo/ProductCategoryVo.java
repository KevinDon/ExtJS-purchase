package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;


public class ProductCategoryVo implements Serializable {
    private String id;

    private String cnName;

    private String enName;

    private String name;

    private String omsId;

    private String omsCode;

    private String omsName;

    private String parentId;

    private Integer leaf;

    private Integer level;

    private Integer status;

    private Integer sort;

    private String path;
    
    private String creatorId;
    private String creatorName;
    private String creatorCnName;
    private String creatorEnName;

    private List<ProductCategoryVo> children = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOmsId() {
        return omsId;
    }

    public void setOmsId(String omsId) {
        this.omsId = omsId;
    }

    public String getOmsCode() {
        return omsCode;
    }

    public void setOmsCode(String omsCode) {
        this.omsCode = omsCode;
    }

    public String getOmsName() {
        return omsName;
    }

    public void setOmsName(String omsName) {
        this.omsName = omsName;
    }
    
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ProductCategoryVo> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategoryVo> children) {
        this.children = children;
    }

}
