package com.newaim.purchase.archives.finance.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.finance.entity.BankAccount;

@Repository
public interface BankAccountDao extends BaseDao<BankAccount, String> {

	BankAccount findBankAccountById(String id);

	/**
	 * 获取供应商下所有银行信息
	 * @param vendorId 供应商id
	 * @return
	 */
	List<BankAccount> findBankAccountByVendorId(String vendorId);

	/**
	 * 获取供应商下所有指定状态的银行信息
	 * @param vendorId 供应商id
	 * @param status 状态
	 * @return
	 */
	List<BankAccount> findBankAccountByVendorIdAndStatus(String vendorId, Integer status);
	
	void deleteByVendorId(String vendorId);
	
	void deleteByVendorIdAndStatus(String vendorId, Integer status);

	List<BankAccount> findByBusinessId(String businessId);

}
