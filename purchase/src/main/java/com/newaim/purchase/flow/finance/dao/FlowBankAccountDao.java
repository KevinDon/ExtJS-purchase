package com.newaim.purchase.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.finance.entity.FlowBankAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowBankAccountDao extends  BaseDao<FlowBankAccount, String> {
	
}
