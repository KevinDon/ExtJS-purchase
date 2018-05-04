package com.newaim.purchase.archives.flow.purchase.vo;


import com.newaim.purchase.archives.flow.finance.vo.BalanceRefundVo;

import java.io.Serializable;


public class PurchaseBalanceRefundUnionVo implements Serializable {

    private String id;

    private String balanceRefundBusinessId;

    private String balanceRefundId;

    private String purchasePlanBusinessId;

    private String purchasePlanId;

    private String purchaseContractBusinessId;

    private String purchaseContractId;

    private BalanceRefundVo balanceRefund;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalanceRefundBusinessId() {
        return balanceRefundBusinessId;
    }

    public void setBalanceRefundBusinessId(String balanceRefundBusinessId) {
        this.balanceRefundBusinessId = balanceRefundBusinessId;
    }

    public String getBalanceRefundId() {
        return balanceRefundId;
    }

    public void setBalanceRefundId(String balanceRefundId) {
        this.balanceRefundId = balanceRefundId;
    }

    public String getPurchasePlanBusinessId() {
        return purchasePlanBusinessId;
    }

    public void setPurchasePlanBusinessId(String purchasePlanBusinessId) {
        this.purchasePlanBusinessId = purchasePlanBusinessId;
    }

    public String getPurchasePlanId() {
        return purchasePlanId;
    }

    public void setPurchasePlanId(String purchasePlanId) {
        this.purchasePlanId = purchasePlanId;
    }

    public String getPurchaseContractBusinessId() {
        return purchaseContractBusinessId;
    }

    public void setPurchaseContractBusinessId(String purchaseContractBusinessId) {
        this.purchaseContractBusinessId = purchaseContractBusinessId;
    }

    public String getPurchaseContractId() {
        return purchaseContractId;
    }

    public void setPurchaseContractId(String purchaseContractId) {
        this.purchaseContractId = purchaseContractId;
    }

    public BalanceRefundVo getBalanceRefund() {
        return balanceRefund;
    }

    public void setBalanceRefund(BalanceRefundVo balanceRefund) {
        this.balanceRefund = balanceRefund;
    }
}
