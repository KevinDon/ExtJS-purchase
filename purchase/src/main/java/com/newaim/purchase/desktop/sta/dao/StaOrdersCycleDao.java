package com.newaim.purchase.desktop.sta.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.sta.entity.StaOrdersCycle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaOrdersCycleDao extends BaseDao<StaOrdersCycle, String> {
    /**
     * 删除未走完流程的订单记录
     */
    @Modifying
    @Query( nativeQuery = true,value = "DELETE from na_sta_orders_cycle  WHERE  flag_asn_time is  null or (flag_asn_time + '7 days') > now() ")
    void deleteFlowData();

    /**
     * 插入未走完流程的订单记录
     */
    @Modifying
    @Query( nativeQuery = true,value = "INSERT into  na_sta_orders_cycle (SELECT vso.* from na_view_sta_orders_cycle vso WHERE ( (vso.flag_asn_time + '7 days') > now() or vso.flag_asn_time is null) and vso.status=1 )")
    void updateFlowData();
}
