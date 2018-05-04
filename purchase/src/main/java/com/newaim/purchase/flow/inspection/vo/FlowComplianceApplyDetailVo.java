package com.newaim.purchase.flow.inspection.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.archives.product.vo.ProductVo;

public class FlowComplianceApplyDetailVo implements Serializable{

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String businessId;
    private String productId;
    private ProductVo product;

    private String sku;//产品编码
    private Integer newPrevRiskRating;//最新风险评级
    private Integer prevRiskRating;//上次风险评级
    private String remark;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNewPrevRiskRating() {
        return newPrevRiskRating;
    }

    public void setNewPrevRiskRating(Integer newPrevRiskRating) {
        this.newPrevRiskRating = newPrevRiskRating;
    }

    public Integer getPrevRiskRating() {
        return prevRiskRating;
    }

    public void setPrevRiskRating(Integer prevRiskRating) {
        this.prevRiskRating = prevRiskRating;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
