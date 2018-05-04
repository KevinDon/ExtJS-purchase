package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.finance.vo.FeePaymentVo;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterDetailVo;
import com.newaim.purchase.archives.flow.finance.vo.PurchaseContractDepositVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractOtherDetailVo;

import java.util.List;

public class CostFeeDetailVo {
    private  List<FeePaymentVo> balances = Lists.newArrayList();
    private List<FeeRegisterDetailVo> FeeRegisterDetails = Lists.newArrayList();
    private List<PurchaseContractDepositVo> deposits = Lists.newArrayList();
    private List<PurchaseContractOtherDetailVo> orderOtherDetail = Lists.newArrayList();

    public List<FeePaymentVo> getBalances() {
        return balances;
    }

    public void setBalances(List<FeePaymentVo> balances) {
        this.balances = balances;
    }

    public List<FeeRegisterDetailVo> getFeeRegisterDetails() {
        return FeeRegisterDetails;
    }

    public void setFeeRegisterDetails(List<FeeRegisterDetailVo> feeRegisterDetails) {
        FeeRegisterDetails = feeRegisterDetails;
    }

    public List<PurchaseContractDepositVo> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<PurchaseContractDepositVo> deposits) {
        this.deposits = deposits;
    }

    public List<PurchaseContractOtherDetailVo> getOrderOtherDetail() {
        return orderOtherDetail;
    }

    public void setOrderOtherDetail(List<PurchaseContractOtherDetailVo> orderOtherDetail) {
        this.orderOtherDetail = orderOtherDetail;
    }
}
