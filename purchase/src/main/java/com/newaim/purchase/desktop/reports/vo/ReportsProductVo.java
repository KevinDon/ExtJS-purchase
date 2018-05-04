package com.newaim.purchase.desktop.reports.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.vo.ProductVo;

import java.io.Serializable;
import java.util.List;

public class ReportsProductVo implements Serializable{

    private String id;

    private String reportsId;

    private String productId;

    private ProductVo product;

    //以下仅安检报告使用
    private Integer cplRiskRating = 0;           //新风险级别
    private Float cplScore = 0F;                  //风险评估总分
    private Integer cplHasAuthentication = 0;   //需要认证
    private String cplAuthenticationNames;      //认证名称，以“,“间隔
    private String cplReference;                  //参考链接
    //============

    //产品问题
    private List<ReportsProductTroubleDetailVo> troubleDetails = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportsId() {
        return reportsId;
    }

    public void setReportsId(String reportsId) {
        this.reportsId = reportsId;
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

    public List<ReportsProductTroubleDetailVo> getTroubleDetails() {
        return troubleDetails;
    }

    public void setTroubleDetails(List<ReportsProductTroubleDetailVo> troubleDetails) {
        this.troubleDetails = troubleDetails;
    }

    public Integer getCplRiskRating() {
        return cplRiskRating;
    }

    public void setCplRiskRating(Integer cplRiskRating) {
        this.cplRiskRating = cplRiskRating;
    }

    public Float getCplScore() {
        return cplScore;
    }

    public void setCplScore(Float cplScore) {
        this.cplScore = cplScore;
    }

    public Integer getCplHasAuthentication() {
        return cplHasAuthentication;
    }

    public void setCplHasAuthentication(Integer cplHasAuthentication) {
        this.cplHasAuthentication = cplHasAuthentication;
    }

    public String getCplAuthenticationNames() {
        return cplAuthenticationNames;
    }

    public void setCplAuthenticationNames(String cplAuthenticationNames) {
        this.cplAuthenticationNames = cplAuthenticationNames;
    }

    public String getCplReference() {
        return cplReference;
    }

    public void setCplReference(String cplReference) {
        this.cplReference = cplReference;
    }
}
