package com.newaim.purchase.archives.flow.purchase.vo;


import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;

import java.io.Serializable;
import java.util.List;


public class PurchaseBalanceRefundUnionsVo implements Serializable {

    private List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions = Lists.newArrayList();

    public List<PurchaseBalanceRefundUnion> getPurchaseBalanceRefundUnions() {
        return purchaseBalanceRefundUnions;
    }

    public void setPurchaseBalanceRefundUnions(List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions) {
        this.purchaseBalanceRefundUnions = purchaseBalanceRefundUnions;
    }
}
