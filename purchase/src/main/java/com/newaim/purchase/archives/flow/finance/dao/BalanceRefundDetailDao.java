package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.purchase.archives.flow.finance.entity.BalanceRefundDetail;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRefundDetailDao extends JpaRepository<BalanceRefundDetail, String>, JpaSpecificationExecutor<BalanceRefundDetail> {

    void deleteByBalanceRefundId(String balanceRefundId);

    /**
     * 根据业务ID查找明细结合
     * @param balanceRefundId 业务id
     * @return 明细集合
     */
    List<BalanceRefundDetail> findDetailsByBalanceRefundId(String balanceRefundId);

}
