package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasePlanDao extends BaseDao<PurchasePlan, String> {

    List<PurchasePlan> findByBusinessId(String businessId);
}
