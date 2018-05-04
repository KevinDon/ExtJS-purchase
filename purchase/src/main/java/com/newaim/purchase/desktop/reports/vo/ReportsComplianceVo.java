package com.newaim.purchase.desktop.reports.vo;

import java.io.Serializable;

/**
 * @author lance
 */

public class ReportsComplianceVo implements Serializable{

    private String id;

    private String reportsId;
    private String productProblem;
    private String standard;
    private String evaluationSafetyRecommendations;
    private String essentialStandard;

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

    public String getProductProblem() {
        return productProblem;
    }

    public void setProductProblem(String productProblem) {
        this.productProblem = productProblem;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getEvaluationSafetyRecommendations() {
        return evaluationSafetyRecommendations;
    }

    public void setEvaluationSafetyRecommendations(String evaluationSafetyRecommendations) {
        this.evaluationSafetyRecommendations = evaluationSafetyRecommendations;
    }

    public String getEssentialStandard() {
        return essentialStandard;
    }

    public void setEssentialStandard(String essentialStandard) {
        this.essentialStandard = essentialStandard;
    }
}
