package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseBalanceRefundUnionDao extends JpaRepository<PurchaseBalanceRefundUnion, String>, JpaSpecificationExecutor<PurchaseBalanceRefundUnion> {

    void deleteByPurchasePlanBusinessId(String purchasePlanBusinessId);

    List<PurchaseBalanceRefundUnion> findByPurchasePlanBusinessId(String purchasePlanBusinessId);

    List<PurchaseBalanceRefundUnion> findByPurchasePlanId(String purchasePlanId);

    List<PurchaseBalanceRefundUnion> findByPurchasePlanIdIn(List<String> purchasePlanIds);

    List<PurchaseBalanceRefundUnion> findByPurchaseContractBusinessId(String purchaseContractBusinessId);

    List<PurchaseBalanceRefundUnion> findByPurchaseContractId(String purchaseContractId);
}
