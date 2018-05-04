package com.newaim.purchase.constants;

/**
 * 定义通用常量
 */
public class ConstantsAttachment {

    /**
     * 附件类型
     */
    public enum Status{
        Cost("Cost", "成本计算"),
        Vendor("Vendor", "供应商其它附件"),
        Vendor_Photo("Vendor_Photo", "供应商图片"),
        Product_Photo("Product_Photo", "产品图片"),
        ProductProblem("Product_Problem", "问题记录"),
        FlowPurchaseContract("FlowPurchaseContract", "工作流－采购合同"),
        FlowSamplePayment("FlowSamplePayment", "工作流－样品付款"),
        FlowComplianceApply("FlowComplianceApply", "工作流－安全检验"),
        ProductCertificate("ProductCertificate", "工作流－产品证书"),
        FlowSample("FlowSample", "工作流－样品申请"),
        FlowProductQuotation("FlowProductQuotation", "工作流－采购询价"),
        FlowProductCertification("FlowProductCertification", "工作流－产品认证"),
        FlowNewProduct("FlowNewProduct", "工作流－新品申请"),
        FlowCustomClearance("FlowCustomClearance", "工作流－清关申请"),
        FlowCustomClearance_Photo("FlowCustomClearance_Photo", "工作流－清关申请图片"),
        FlowFeeRegister("FlowFeeRegister","工作流－费用登记"),
        FlowFeePayment("FlowFeePayment","工作流－费用支付"),
        FlowPurchaseContractDeposit("FlowPurchaseContractDeposit", "工作流－合同定金"),
        FlowOrderQc("FlowOrderQc", "工作流－订单质检"),
        FlowSampleQc("FlowSampleQc", "工作流－样品质检"),
        FlowPurchasePlan("FlowPurchasePlan", "工作流－采购计划"),
        FlowBankAccount("FlowBankAccount", "工作流－收款账号信息"),
        Reports("Reprots", "报告文件"),
        Reports_Photo("Reprots_Photo", "报告图片"),
        Reports_File("Reports_File", "报告图片"),
        BankAccount_File("BankAccount_File", "保函文件"),
        BankAccount_ContractFile("BankAccount_ContractFile","合同履约担保函"),
        MyTemplate("MyTemplate", "模板附件"),
        Email("Email","邮件附件"),
        Email_Inner("Email_inner","邮件内的图片"),
        FlowServiceProviderQuotation("FlowServiceProviderQuotation", "工作流-采购询价")
        ;

        Status(String code, String name){
            this.code = code;
            this.name = name;
        }
        public final String code;

        public final String name;
    }
}
