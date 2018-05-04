package com.newaim.purchase.archives.flow.purchase.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "na_purchase_balance_refund_union")
public class PurchaseBalanceRefundUnion implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "PBRU")})
    private String id;

    private String balanceRefundBusinessId;

    private String balanceRefundId;

    private String purchasePlanBusinessId;

    private String purchasePlanId;

    private String purchaseContractBusinessId;

    private String purchaseContractId;

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
}
