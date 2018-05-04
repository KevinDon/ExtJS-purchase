package com.newaim.purchase.flow.inspection.entity;

import com.newaim.purchase.archives.product.entity.Product;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "na_flow_compliance_apply_detail")
public class FlowComplianceApplyDetail implements Serializable{
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FCAD")})
    private String id;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "product_id")
    private String productId;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    private String sku;
    /**最新风险评级*/
    private Integer newPrevRiskRating;
    /**上次风险评级*/
    private Integer prevRiskRating;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
