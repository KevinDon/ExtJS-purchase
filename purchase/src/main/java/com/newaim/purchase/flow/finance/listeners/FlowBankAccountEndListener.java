package com.newaim.purchase.flow.finance.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.finance.dao.BankAccountDao;
import com.newaim.purchase.archives.finance.entity.BankAccount;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.vendor.dao.VendorDao;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.flow.finance.entity.FlowBankAccount;
import com.newaim.purchase.flow.finance.service.FlowBankAccountService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收款信息变更正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowBankAccountEndListener extends CommonEndListener {

    @Autowired
    private FlowBankAccountService flowBankAccountService;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private VendorDao vendorDao;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 获取申请单对象
        FlowBankAccount flowBankAccount = flowBankAccountService.getFlowBankAccount(businessId);
        //2. 禁用之前的帐号信息
        List<BankAccount> bankAccounts = bankAccountDao.findBankAccountByVendorId(flowBankAccount.getVendorId());
        for (BankAccount bankAccount: bankAccounts) {
            bankAccount.setStatus(Constants.Status.DISABLED.code);
        }
        bankAccountDao.save(bankAccounts);
        //3. 写入正式数据表并关联申请单
        BankAccount bankAccount = BeanMapper.map(flowBankAccount, BankAccount.class);
        bankAccount.setId(null);
        bankAccount.setBusinessId(businessId);
        bankAccountDao.save(bankAccount);
        Vendor vendor = vendorDao.getOne(flowBankAccount.getVendorId());
        vendor.setCurrency(bankAccount.getCurrency());
        vendorDao.save(vendor);
        productVendorPropDao.batchUpdateCurrency(bankAccount.getVendorId(), bankAccount.getCurrency());

    }
}
