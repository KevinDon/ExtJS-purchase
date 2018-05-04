Ext.ns("App");

App.importJs = {
    AppHome: [
        "js/view/App.home.modules.js",
	    "js/view/App.home.js"
    ],
    ModulesView: [
        "js/view/admin/ModulesView.js",
        "js/view/admin/ModulesForm.js"
    ],
    FlowProcessesView: [
        "js/view/admin/FlowProcessesView.js",
        "js/view/admin/FlowProcessesForm.js"
    ],
    AccountDepartmentView: [
        "js/view/admin/AccountDepartmentForm.js",
	    "js/view/admin/AccountDepartmentView.js"
	],
    AccountUserView: [
        "js/view/admin/AccountUserView.js"
    ],
    AccountUserRoleView: [
        "js/view/admin/AccountUserRoleView.js"
    ],
    AccountPermissionView:[
	    "js/view/admin/AccountPermissionView.js"
	],
    AccountRoleView: [
        "js/view/admin/AccountRoleView.js"
    ],
    AccountRoleAssignView: [
	    "js/view/admin/AccountRoleAssignView.js"
    ],
    AccountUserProfileForm: [
	    "js/view/admin/AccountUserProfileForm.js"
	],
    DataDictionaryManageView: [
       "js/view/admin/DataDictionaryManageView.js",
       "js/view/admin/DataDictionaryManageTabs.js"
    ],
    DataDictionaryValueView: [
       "js/view/admin/DataDictionaryValueView.js",
       "js/view/admin/DataDictionaryValueTabs.js"
    ],
    DataDictionaryCategoryView: [
       "js/view/admin/DataDictionaryCategoryTabs.js",
       "js/view/admin/DataDictionaryCategoryView.js"
    ],
    HelpView:[
        "js/view/admin/HelpView.js",
    ],
    AutoCodeView: [
        "js/view/admin/AutoCodeView.js",
    ],
    CommonConfigurationItemsView:[
	   "js/view/admin/CommonConfigurationItemsView.js"
	],
	MyDocumentCategoryView: [
       "js/view/admin/MyDocumentCategoryTabs.js",
       "js/view/admin/MyDocumentCategoryView.js"
    ],
    MyDocumentView: [
	  "js/view/admin/MyDocumentView.js"
	],
    MyTemplateView: [
        "js/view/admin/MyTemplateView.js"
    ],
    SystemToolsView:[
        "js/view/admin/SystemToolsView.js"
    ],
    AlertSettingView: [
        "js/view/admin/AlertSettingView.js"
    ],
    //关于系统
    AboutView:[
        "js/view/admin/AboutView.js"
	],

    ProductCategoryView: [
        "js/view/core/dialog/OMSCategoryDialog.js",
        "js/view/archives/ProductCategoryForm.js",
        "js/view/archives/ProductCategoryView.js"
    ],

    ProductDocumentView: [
        "js/view/core/dialog/TariffDialog.js",
        "js/view/archives/ProductDocumentViewTabs.js",
        "js/view/archives/ProductDocumentView.js",
        "js/view/archives/ProductDocumentForm.js",
        "js/view/archives/ProductDocumentFormOrderGrid.js",
        "js/view/archives/ProductDocumentFormReportGrid.js",
        "js/view/archives/ProductDocumentFormCertificateGrid.js"
    ],

    NewProductDocumentView: [
        "js/view/core/dialog/TariffDialog.js",
        "js/view/archives/NewProductDocumentForm.js",
        "js/view/archives/NewProductDocumentView.js",
        "js/view/archives/NewProductDocumentViewTabs.js",
        "js/view/archives/NewProductDocumentFormCheck.js",
    ],

    ProductCombinationView: [
        "js/view/archives/ProductCombinationForm.js",
        "js/view/archives/ProductCombinationFormGrid.js",
        "js/view/archives/ProductCombinationView.js",
        "js/view/archives/ProductCombinationViewTabs.js"
    ],

    TariffView: [
        "js/view/archives/TariffForm.js",
        "js/view/archives/TariffView.js",
        "js/view/archives/TariffViewTabs.js"
    ],

	VendorCategoryView: [
	    "js/view/archives/VendorCategoryForm.js",
	    "js/view/archives/VendorCategoryView.js"
	],
	VendorDocumentView: [
	    "js/view/archives/VendorDocumentViewTabs.js",
	    "js/view/archives/VendorDocumentForm.js",
	    "js/view/archives/VendorDocumentView.js",
	    "js/view/archives/VendorDocumentFormReportGrid.js"
	],
	ServiceProviderCategoryView: [
	    "js/view/archives/ServiceProviderCategoryForm.js",
	    "js/view/archives/ServiceProviderCategoryView.js"
	],
	ServiceProviderDocumentView: [
        "js/view/archives/ServiceProviderDocumentViewTabs.js",
        "js/view/archives/ServiceProviderDocumentForm.js",
        "js/view/archives/ServiceProviderDocumentView.js",
        "js/view/archives/ServiceProviderOriginPortGrid.js",
        "js/view/archives/ServiceProviderChargeItemGrid.js"
	],

    ServiceProviderInvoiceView: [
        "js/view/archives/ServiceProviderInvoiceDestinationChargeGrid.js",
        "js/view/archives/ServiceProviderInvoiceFreightChargeGrid.js",
        "js/view/archives/ServiceProviderInvoiceDutyChargeGrid.js",
        "js/view/archives/ServiceProviderInvoiceForm.js",
        "js/view/archives/ServiceProviderInvoiceView.js",
    ],

    ProductCostView: [
        "js/view/archives/ProductCostViewTabs.js",
        "js/view/core/dialog/OrderShippingPlanDialog.js",
        "js/view/archives/ProductCostFormPaymentGrid.js",
        "js/view/archives/ProductCostFormOrderCbmGrid.js",
        "js/view/archives/ProductCostFormPurchaseContractGrid.js",
        "js/view/archives/ProductCostFormFreightChargeGrid.js",
        "js/view/archives/ProductCostFormDestinationChargeGrid.js",
        "js/view/archives/ProductCostFormDutyChargeGrid.js",
        "js/view/archives/ProductCostFormCostGrid.js",
        "js/view/archives/ProductCostForm.js",
        "js/view/archives/ProductCostView.js",
        "js/view/archives/ProductCostFormOtherChargeGrid.js",
    ],

	MyAgentSettingView: [
        "js/view/flow/MyAgentSettingView.js",
        "js/view/flow/MyAgentSettingViewFormGrid.js",
        "js/view/flow/MyAgentSettingForm.js",
    ],

    MessageView: [
        "js/view/desktop/MessageForm.js",
        "js/view/desktop/MessageView.js"
    ],

    EventsView: [
        "js/view/desktop/EventsView.js"
    ],
    
    ScheduleView: [
        "js/view/desktop/ScheduleView.js"
    ],
    
    ProductSpecialityNotifiedView: [
        "js/view/desktop/ProductSpecialityNotifiedForm.js",
        "js/view/desktop/ProductSpecialityNotifiedView.js"
    ],
    
    EmailInboxView: [
        "js/view/desktop/EmailInboxView.js",
        "js/view/desktop/EmailRelatedTabs.js",
    ],
    
    EmailDraftsView: [
        "js/view/desktop/EmailDraftsView.js",
        "js/view/desktop/EmailRelatedTabs.js",
    ],

    EmailSentBoxView: [
        "js/view/desktop/EmailSentBoxView.js",
        "js/view/desktop/EmailRelatedTabs.js"
    ],
    
    EmailDustbinView: [
        "js/view/desktop/EmailDustbinView.js",
        "js/view/desktop/EmailRelatedTabs.js"
    ],
    
    EmailSettingView: [
        "js/view/desktop/EmailSettingView.js",
    ],

    ContactsView: [
       "js/view/desktop/ContactsView.js",
    ],

    ProductCertificateDocumentView: [
        "js/view/archives/ProductCertificateDocumentView.js",
        "js/view/archives/ProductCertificateDocumentTabs.js",
        "js/view/archives/ProductCertificateDocumentForm.js",
        "js/view/archives/ProductCertificateDocumentFormGird.js",
    ],

    FlowNewProductView: [
        "js/view/flow/FlowNewProductView.js",
    ],

    FlowNewProductForm: [
        "js/view/flow/FlowNewProductForm.js",
        "js/view/flow/FlowNewProductFormGrid.js",
    ],

    FlowBankAccountView: [
        "js/view/flow/FlowBankAccountView.js"
    ],

    FlowBankAccountForm: [
        "js/view/flow/FlowBankAccountForm.js"
    ],

    FlowSampleView: [
        "js/view/flow/FlowSampleView.js",
        "js/view/flow/FlowSampleForm.js",
        "js/view/flow/FlowSampleFormGrid.js",
    ],

    FlowSampleForm: [
        "js/view/flow/FlowSampleForm.js",
        "js/view/flow/FlowSampleFormGrid.js"
    ],

    FlowServiceInquiryView: [
        "js/view/flow/FlowServiceInquiryView.js"
    ],

    FlowServiceInquiryForm: [
        "js/view/flow/FlowServiceInquiryForm.js",
        "js/view/flow/FlowServiceInquiryFreightChargeGrid.js",
        "js/view/flow/FlowServiceInquiryDestinationChargeGrid.js"
    ],

    FlowCustomClearanceView: [
        "js/view/core/dialog/PackingListPanel.js",
        "js/view/flow/FlowCustomClearanceView.js",
    ],

    FlowCustomClearanceForm: [
        "js/view/core/dialog/PurchaseOrderDialog.js",
        "js/view/core/dialog/OrderShippingConfirmationDialog.js",
        "js/view/core/dialog/PackingListPanel.js",
        "js/view/flow/FlowCustomClearanceForm.js",
    ],

    FlowProductQuotationView: [
        "js/view/flow/FlowProductQuotationView.js"
    ],

    FlowProductQuotationForm: [
        "js/view/flow/FlowProductQuotationForm.js",
        "js/view/flow/FlowProductQuotationFormGrid.js",
    ],

    FlowPurchasePlanView: [
        "js/view/flow/FlowPurchasePlanView.js",
        "js/view/flow/FlowPurchasePlanFormGrid.js"
    ],

    FlowPurchasePlanForm: [
        "js/view/flow/FlowPurchasePlanForm.js",
        "js/view/flow/FlowPurchasePlanFormGrid.js",
    ],

    FlowComplianceArrangementView: [
        "js/view/flow/FlowComplianceArrangementView.js"
    ],

    FlowComplianceArrangementForm: [
        "js/view/flow/FlowComplianceArrangementForm.js",
        "js/view/flow/FlowComplianceArrangementFormGrid.js",
    ],

    FlowSampleQcView: [
        "js/view/flow/FlowSampleQcView.js",
        "js/view/flow/FlowSampleQcFormGrid.js"
    ],

    FlowSampleQcForm: [
        "js/view/flow/FlowSampleQcForm.js",
        "js/view/flow/FlowSampleQcFormGrid.js"
    ],

    FlowProductCertificationView:[
        "js/view/flow/FlowProductCertificationView.js"
    ],

    FlowProductCertificationForm:[
        "js/view/flow/FlowProductCertificationForm.js",
        "js/view/flow/FlowProductCertificationFormGrid.js",
    ],

    FlowSamplePaymentView: [
        "js/view/flow/FlowSamplePaymentView.js"
    ],

    FlowSamplePaymentForm: [
        "js/view/flow/FlowSamplePaymentForm.js",
        "js/view/flow/FlowSamplePaymentFormGrid.js",
        "js/view/flow/FlowSampleFormGrid.js",
    ],

    FlowPurchaseContractView: [
        "js/view/flow/FlowPurchaseContractView.js"
    ],
    FlowPurchaseContractForm: [
        "js/view/flow/FlowPurchaseContractForm.js",
        "js/view/flow/FlowPurchaseContractFormGrid.js",
    ],

    FlowOrderQualityInspectionView: [
        "js/view/flow/FlowOrderQualityInspectionView.js"
    ],

    FlowOrderQualityInspectionForm: [
        "js/view/flow/FlowOrderQualityInspectionForm.js",
        "js/view/flow/FlowOrderQualityInspectionFormGrid.js",
    ],

    FlowWarehousePlanningView: [
        "js/view/flow/FlowWarehousePlanningView.js"
    ],

    FlowWarehousePlanningForm: [
        "js/view/core/dialog/WarehouseDialog.js",
        "js/view/flow/FlowWarehousePlanningForm.js",
        "js/view/flow/FlowWarehousePlanningFormGrid.js"
    ],

    
    ExchangeRateView: [
        "js/view/desktop/ExchangeRateView.js",
        "js/view/desktop/ExchangeRateForm.js",
    ],

    FlowOrderShippingPlanView:[
        "js/view/flow/FlowOrderShippingPlanFormGrid.js",
        "js/view/core/dialog/ServiceProviderQuotationDialog.js",
        "js/view/flow/FlowOrderShippingPlanView.js",
    ],

    FlowOrderShippingPlanForm:[
        "js/view/flow/FlowOrderShippingPlanFormGrid.js",
        "js/view/core/dialog/ServiceProviderQuotationDialog.js",
        "js/view/flow/FlowOrderShippingPlanForm.js",
    ],

    FlowDepositContractView:[
        "js/view/flow/FlowDepositContractView.js"
    ],

    FlowDepositContractForm:[
        "js/view/flow/FlowDepositContractForm.js",
        "js/view/flow/FlowPurchaseContractFormGrid.js",
        "js/view/flow/FlowDepositContractFormGrid.js",
    ],

    FlowOrderShippingConfirmationView:[
        "js/view/flow/FlowOrderShippingConfirmationView.js"
    ],

    FlowOrderShippingConfirmationForm:[
        "js/view/flow/FlowOrderShippingConfirmationForm.js",
        "js/view/flow/FlowOrderShippingConfirmationFormGrid.js",
    ],

    FlowFeeRegistrationView:[
        "js/view/flow/FlowFeeRegistrationView.js"
    ],

    FlowFeeRegistrationForm:[
        "js/view/flow/FlowFeeRegistrationForm.js",
        "js/view/flow/FlowFeeRegistrationFormGrid.js",
    ],

    FlowOrderReceivingNoticeView:[
        "js/view/core/dialog/AsnPackingListPanel.js",
        "js/view/flow/FlowOrderReceivingNoticeView.js",
        "js/view/flow/FlowOrderReceivingNoticeFormGrid.js",
    ],

    FlowOrderReceivingNoticeForm:[
        "js/view/core/dialog/WarehouseDialog.js",
        "js/view/core/dialog/AsnPackingListPanel.js",
        "js/view/core/dialog/WarehousePlanningDialog.js",
        "js/view/flow/FlowOrderReceivingNoticeForm.js",
        "js/view/flow/FlowOrderReceivingNoticeFormGrid.js",
    ],

    FlowBalanceRefundView:[
        "js/view/flow/FlowBalanceRefundView.js"
    ],

    FlowBalanceRefundForm:[
        "js/view/flow/FlowBalanceRefundForm.js",
        "js/view/flow/FlowBalanceRefundFormGrid.js",
        "js/view/flow/FlowPurchaseContractFormGrid.js",
    ],

    FlowFeePaymentView:[
        "js/view/flow/FlowFeePaymentView.js"
    ],

    FlowFeePaymentForm:[
        "js/view/flow/FlowFeePaymentForm.js",
        "js/view/flow/FlowFeeRegistrationFormGrid.js",
    ],

    ReportSupplierInvestigationView:[
        "js/view/desktop/ReportSupplierInvestigationView.js",
        "js/view/desktop/ReportSupplierInvestigationForm.js"
    ],
    ReportProductAnalysisView: [
        "js/view/desktop/ReportProductAnalysisForm.js",
        "js/view/desktop/ReportProductAnalysisView.js"
    ],
    ReportProductForecastView:[
        "js/view/desktop/ReportProductForecastView.js",
        "js/view/desktop/ReportProductForecastForm.js"
    ],

    ReportProductSummaryView:[
        "js/view/desktop/ReportProductSummaryView.js",
        "js/view/desktop/ReportProductSummaryForm.js"
    ],
    ReportSampleInspectionView:[
        "js/view/desktop/ReportSampleInspectionFormGrid.js",
        "js/view/desktop/ReportSampleInspectionView.js",
        "js/view/desktop/ReportSampleInspectionForm.js"
    ],
    ReportProductComplianceView:[
        "js/view/desktop/ReportSampleInspectionFormGrid.js",
        "js/view/desktop/ReportProductComplianceView.js",
        "js/view/desktop/ReportProductComplianceForm.js",
        "js/view/desktop/ReportProductComplianceFormGrid.js"
    ],

    ReportOrderInspectionView:[
        "js/view/desktop/ReportOrderInspectionView.js",
        "js/view/desktop/ReportOrderInspectionForm.js",
        "js/view/desktop/ReportOrderInspectionFormGrid.js"
    ],

    ProductProblemView:[
        "js/view/core/dialog/OMSOrderDialog.js",
        "js/view/archives/ProductProblemView.js",
        "js/view/archives/ProductProblemForm.js",
        "js/view/archives/ProductProblemFormGird.js",
        "js/view/archives/ProductProblemViewTabs.js",
        "js/view/archives/ProductProblemFormRemarkForm.js",
    ],
    
    StaOrderView:[
        "js/view/desktop/StaOrderView.js",
    ],
    
    StaProblemView:[
        "js/view/desktop/StaProblemView.js",
    ],
    
    StaCostView:[
        "js/view/desktop/StaCostView.js",
    ],
    
    StaQualityView:[
        "js/view/desktop/StaQualityView.js",
    ],
    
    StaOrdersCycleView:[
        "js/view/desktop/StaOrdersCycleView.js",
    ],
};