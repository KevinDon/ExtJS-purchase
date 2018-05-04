package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mark on 2017/12/12.
 */
public class OmsCategoryDto implements Serializable {

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("parent_code")
    private String parentCode;

    private List<OmsCategoryDto> children = Lists.newArrayList();

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<OmsCategoryDto> getChildren() {
        return children;
    }

    public void setChildren(List<OmsCategoryDto> children) {
        this.children = children;
    }
}
