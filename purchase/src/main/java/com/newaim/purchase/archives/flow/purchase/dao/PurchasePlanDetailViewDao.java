package com.newaim.purchase.archives.flow.purchase.dao;


import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchasePlanDetailViewDao extends JpaRepository<PurchasePlanDetailView, String>, JpaSpecificationExecutor<PurchasePlanDetailView> {


    @Query("select t from PurchasePlanDetailView t where t.vendorId = :vendorId and t.creatorId = :creatorId")
    List<PurchasePlanDetailView> findAllByVendor(@Param("vendorId") String vendorId, @Param("creatorId") String creatorId);

}
