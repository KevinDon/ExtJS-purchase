package com.newaim.purchase.desktop.reports.entity;

import com.google.common.collect.Lists;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="na_reports_product")
public class ReportsProduct implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "REPP")})
    private String id;

    private String reportsId;

    private String productId;

    //以下仅安检报告使用
    private Integer cplRiskRating = 0;           //新风险级别
    private Float cplScore = 0F;                  //风险评估总分
    private Integer cplHasAuthentication = 0;   //需要认证
    private String cplAuthenticationNames;      //认证名称，以“,“间隔
    private String cplReference;                  //参考链接
    //============

    @Transient
    private List<ReportsProductTroubleDetail> troubleDetails = Lists.newArrayList();

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

    public List<ReportsProductTroubleDetail> getTroubleDetails() {
        return troubleDetails;
    }

    public void setTroubleDetails(List<ReportsProductTroubleDetail> troubleDetails) {
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
