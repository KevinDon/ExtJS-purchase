FlowFeePaymentForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.updatePrice = !(this.record.flowStatus  == 2 || this.record.flowStatus == 3) ? true : false;
        this.isCanceled = this.record.status == 4 ? true : false;


        var conf = {
            title : _lang.FlowFeePayment.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowFeePayment',
            winId : 'FlowFeePaymentForm',
            frameId : 'FlowFeePaymentView',
            mainGridPanelId : 'FlowFeePaymentGridPanelID',
            mainFormPanelId : 'FlowFeePaymentFormPanelID',
            processFormPanelId: 'FlowFeePaymentProcessFormPanelID',
            searchFormPanelId: 'FlowFeePaymentSearchFormPanelID',
            mainTabPanelId: 'FlowFeePaymentMainTabsPanelID',
            //subProductGridPanelId : 'FlowFeePaymentSubGridPanelID',
            formGridPanelId : 'FlowFeePaymentFormGridPanelID',
            subFormGridPanelId:'FlowFeePaymentSubGridPanelID',

            urlList: __ctxPath + 'flow/finance/feePayment/list',
			urlSave: __ctxPath + 'flow/finance/feePayment/save',
			urlDelete: __ctxPath + 'flow/finance/feePayment/delete',
			urlGet: __ctxPath + 'flow/finance/feePayment/get',
			urlFlow: __ctxPath + 'flow/finance/feePayment/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowFeePayment&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
			actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy',
            flowAllow: (!this.isAdd) && this.isApprove,
            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1,
            flowCancel: (!this.isAdd) && this.record.flowStatus==1 && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun
        };

        this.initUIComponents(conf);
        FlowFeePaymentForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			cls: 'gb-blank',
			tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });
    },

    initUIComponents: function(conf) {
    	
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd, allowBlank:true, },

                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id, allowBlank:true,  },

               // 费用登记信息
                {xtype: 'section', title:_lang.FlowFeePayment.tabFeeRegisterInfo},
                {xtype: 'container',cls:'row', items:  [
                    { field:'main.feeRegisterId', xtype:'hidden'},
                    { field:'main.feeRegisterBusinessId', xtype:'hidden'},
                    //{ field:'main.orderNumber', xtype:'hidden', allowBlank:true, },
                    { field:'main.orderTitle', xtype:'hidden', allowBlank:true, },
                    { field: 'main.feeRegistrationName', xtype: 'FeeRegistrationDialog', fieldLabel: _lang.FlowFeePayment.fFeeRegisterId, cls:'col-2',
                        hiddenName:'main.feeRegisterId', formId:conf.mainFormPanelId, single: true, formal: true,readOnly:this.readOnly,
                        subcallback: function(rows, selectedData){
                            var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                            var registrationInfomation = rows[0].raw || {};
                            cmpFormPlanId.getCmpByName('main.feeRegisterBusinessId').setValue( rows[0].raw.businessId );

                            cmpFormPlanId.getCmpByName('main.orderId').setValue(registrationInfomation.orderId || '');
                            cmpFormPlanId.getCmpByName('main.orderNumber').setValue(registrationInfomation.orderNumber || '');
                            cmpFormPlanId.getCmpByName('main.orderTitle').setValue(registrationInfomation.orderTitle || '');
                            // cmpFormPlanId.getCmpByName('main.vendorName').setValue(registrationInfomation.vendorName || '');
                            cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(registrationInfomation.beneficiaryCnName || '');
                            cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(registrationInfomation.beneficiaryEnName || '');

                            cmpFormPlanId.getCmpByName('main.bankAccount').setValue(registrationInfomation.bankAccount || '');
                            cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(registrationInfomation.beneficiaryBank || '');
                            cmpFormPlanId.getCmpByName('main.companyEnName').setValue(registrationInfomation.companyEnName || '');
                            cmpFormPlanId.getCmpByName('main.companyCnName').setValue(registrationInfomation.companyCnName || '');
                            cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(registrationInfomation.companyCnAddress || '');
                            cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(registrationInfomation.companyEnAddress || '');
                            cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(registrationInfomation.beneficiaryBankAddress || '');
                            cmpFormPlanId.getCmpByName('main.swiftCode').setValue(registrationInfomation.swiftCode || '');
                            cmpFormPlanId.getCmpByName('main.cnaps').setValue(registrationInfomation.cnaps || '');
                            cmpFormPlanId.getCmpByName('main.currency').setValue(registrationInfomation.currency || 1);


                            if(registrationInfomation != undefined){
                                cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(!!registrationInfomation.guaranteeLetter ? registrationInfomation.guaranteeLetter : '');
                                cmpFormPlanId.getCmpByName('main.guaranteeLetterId').setValue(!!registrationInfomation.guaranteeLetter  ? registrationInfomation.guaranteeLetter : '');
                                cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue(!!registrationInfomation.guaranteeLetterName  ? registrationInfomation.guaranteeLetterName : '');
                                if(!!cmpFormPlanId.contractGuaranteeLetter){

                                    cmpFormPlanId.getCmpByName('main.contractguaranteeLetter').setValue(!!registrationInfomation.contractGuaranteeLetter ? registrationInfomation.contractGuaranteeLetter : '');
                                    cmpFormPlanId.getCmpByName('main.contractguaranteeLetterId').setValue(!!registrationInfomation.contractGuaranteeLetter  ? registrationInfomation.contractGuaranteeLetter : '');
                                    cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue(!!registrationInfomation.contractguaranteeLetterName  ? registrationInfomation.contractguaranteeLetterName : '');

                                }
                            }

                            cmpFormPlanId.getCmpByName('main.totalPriceAud').setValue(registrationInfomation.totalPriceAud || '');
                            cmpFormPlanId.getCmpByName('main.totalPriceRmb').setValue(registrationInfomation.totalPriceRmb || '');
                            cmpFormPlanId.getCmpByName('main.totalPriceUsd').setValue(registrationInfomation.totalPriceUsd || '');
                            if(registrationInfomation.currency == 1){
                                cmpFormPlanId.getCmpByName('main.paymentTotalPriceAud').setValue(registrationInfomation.totalPriceAud || '');
                            }else if(registrationInfomation.currency == 2){
                                cmpFormPlanId.getCmpByName('main.paymentTotalPriceRmb').setValue(registrationInfomation.totalPriceRmb || '');
                            }else{
                                cmpFormPlanId.getCmpByName('main.paymentTotalPriceUsd').setValue(registrationInfomation.totalPriceUsd || '');
                            }
                            cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(curUserInfo.audToUsd|| '');
                            cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(curUserInfo.audToRmb || '');
                            cmpFormPlanId.getCmpByName('main.remark').setValue(registrationInfomation.remark || '');
                            cmpFormPlanId.getCmpByName('main.dueDate').setValue(registrationInfomation.dueDate || '');
                            cmpFormPlanId.getCmpByName('main.title').setValue(registrationInfomation.title || '');
                            cmpFormPlanId.getCmpByName('main.paymentStatus').setValue(!!registrationInfomation.paymentStatus ? registrationInfomation.paymentStatus.toString() : '');
                            cmpFormPlanId.getCmpByName('main.feeType').setValue(!!registrationInfomation.feeType ? registrationInfomation.feeType.toString() : '');
                            //清除附件下的所有信息
                            cmpFormPlanId.getCmpByName('main.attachments').subGridPanel.getStore().removeAll();
                     
                            //attachment init
                            cmpFormPlanId.getCmpByName('main.attachments').setValue(registrationInfomation.attachments);

                            cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().removeAll();
                            cmpFormPlanId.getCmpByName('main.project').formGridPanel.getStore().removeAll();
                            cmpFormPlanId.getCmpByName('main.otherContainer').formGridPanel.getStore().removeAll();
                            if(rows[0].raw.feeType == 3){
                                Ext.getCmp(conf.mainFormPanelId + '-priceContainer').show();
                                Ext.getCmp(conf.mainFormPanelId + '-otherContainer').show();
                                Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                                if(registrationInfomation.details != undefined && registrationInfomation.details.length>0){
                                    for(index in registrationInfomation.details){
                                        var feeRegistrationDetails = {};
                                        Ext.applyIf(feeRegistrationDetails, registrationInfomation.details[index]);
                                        cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().add(feeRegistrationDetails);
                                    }
                                }
                                if(registrationInfomation.otherDetails != undefined && registrationInfomation.otherDetails.length>0){
                                    for(index in registrationInfomation.otherDetails){
                                        var feeRegistrationOtherDetails = {};
                                        Ext.applyIf(feeRegistrationOtherDetails, registrationInfomation.otherDetails[index]);
                                        feeRegistrationOtherDetails.subtotalAud =  (parseFloat(registrationInfomation.otherDetails[index].priceAud) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);
                                        feeRegistrationOtherDetails.subtotalRmb=   (parseFloat(registrationInfomation.otherDetails[index].priceRmb) *  parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);
                                        feeRegistrationOtherDetails.subtotalUsd =   (parseFloat(registrationInfomation.otherDetails[index].priceUsd) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);

                                        cmpFormPlanId.getCmpByName('main.otherContainer').formGridPanel.getStore().add(feeRegistrationOtherDetails);
                                    }
                                }

                                cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().removeAll();

                                //根据Order ID 获取订单明细的产品
                                var id = rows[0].raw.businessId;
                                var tpData = $HpStore({
                                    fields: ['id', 'customClearancePackingDetailVos', 'contractDetails','depositAud','depositRmb','depositUsd'],
                                    url: __ctxPath + 'flow/finance/feeRegister/get?id=' + id,
                                    callback: function (conf, records, eOpts) {

                                        var contractDetails = records[0].raw.customClearancePackingDetailVos || {};
                                        var productDetails =  records[0].raw.contractDetails || {};
                                        if(contractDetails.length > 0){
                                            for(var index in contractDetails){
                                                var product = {};
                                                Ext.applyIf(product, contractDetails[index]);
                                                Ext.apply(product, contractDetails[index].product);
                                                Ext.apply(product, contractDetails[index].product.prop);
                                                product.orderValueAud = parseFloat(contractDetails[index].priceAud) *parseFloat(contractDetails[index].packingQty) ;
                                                product.orderValueRmb = parseFloat(contractDetails[index].priceRmb) * parseFloat(contractDetails[index].packingQty);
                                                product.orderValueUsd = parseFloat(contractDetails[index].priceUsd) * parseFloat(contractDetails[index].packingQty);
                                                product.orderQty =  contractDetails[index].packingQty;
                                                product.orderCartons =   contractDetails[index].packingCartons;
                                                product.sku =  contractDetails[index].product.sku;
                                                product.id =  contractDetails[index].id;
                                                cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().add(product);
                                            }
                                        }else if(productDetails.length > 0){
                                            for(var index in productDetails){
                                                var product = {};
                                                Ext.applyIf(product, productDetails[index]);
                                                Ext.apply(product, productDetails[index].product);
                                                Ext.apply(product, productDetails[index].product.prop);
                                                product.orderCartons =   parseFloat(productDetails[index].orderQty) / parseFloat(productDetails[index].product.prop.pcsPerCarton);
                                                product.sku =  productDetails[index].product.sku;
                                                product.id =  productDetails[index].id;
                                                cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().add(product);
                                            }
                                        };
                                    }
                                })

                            }else if(rows[0].raw.feeType == 4){
                                Ext.getCmp(conf.mainFormPanelId + '-priceContainer').hide();
                                Ext.getCmp(conf.mainFormPanelId + '-otherContainer').hide();
                                Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                            }else{
                                Ext.getCmp(conf.mainFormPanelId + '-priceContainer').hide();
                                Ext.getCmp(conf.mainFormPanelId + '-projectContainer').show();
                                if(registrationInfomation.otherDetails != undefined && registrationInfomation.otherDetails.length>0){
                                    for(index in registrationInfomation.otherDetails){
                                        var feeRegistrationDetails = {};
                                        Ext.applyIf(feeRegistrationDetails, registrationInfomation.otherDetails[index]);
                                        feeRegistrationDetails.subtotalAud =  (parseFloat(registrationInfomation.otherDetails[index].priceAud) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);
                                        feeRegistrationDetails.subtotalRmb=   (parseFloat(registrationInfomation.otherDetails[index].priceRmb) *  parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);
                                        feeRegistrationDetails.subtotalUsd =   (parseFloat(registrationInfomation.otherDetails[index].priceUsd) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(2);
                                        cmpFormPlanId.getCmpByName('main.project').formGridPanel.getStore().add(feeRegistrationDetails);
                                    }
                                }
                            }

                        }
                    },
                    { field: 'main.feeType', xtype: 'dictcombo', fieldLabel: _lang.FlowFeeRegistration.fFeeType, cls:'col-2',
                        code:'service_provider', codeSub:'fee_type', allowBlank: true,  scope: this, readOnly:true,
                    },
                ] },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.title', xtype: 'displayfield', fieldLabel:_lang.FlowFeeRegistration.fTitle, cls:'col-2' , readOnly: true,allowBlank:true,  },
                    { field: 'main.paymentStatus', xtype: 'dictcombo', fieldLabel: _lang.FlowFeeRegistration.fPaymentStatus, cls:'col-2' ,
                        readOnly: true, allowBlank:true,  code:'payment', codeSub:'payment_status',
                    },
                ] },
                { xtype: 'container',cls:'row', items:  [
                        // { field: 'main.companyCnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fVendorAndService,  cls:'col-2' , readOnly: true, allowBlank:false,
                        //     hidden:curUserInfo.lang == 'zh_CN' ? false : true,
                        // },
                        // { field: 'main.companyEnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fVendorAndService, cls:'col-2',  readOnly: true, allowBlank:false,
                        //     hidden:curUserInfo.lang == 'zh_CN' ? true : false
                        // },
                        { field: 'main.orderNumber', xtype: 'displayfield', fieldLabel: _lang.FlowOrderQualityInspection.fOrderNumber, cls:'col-2' , readOnly: true,allowBlank:true,  },
                        { field: 'main.orderId', xtype: 'hidden' },
                ] },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.companyCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2' , readOnly: true, allowBlank:false, },
                    { field: 'main.companyEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2',  readOnly: true, allowBlank:false, },
                    { field: 'main.companyCnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.companyEnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2' , readOnly: true,  allowBlank:false,  },
                    { field: 'main.beneficiaryBank', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.beneficiaryBankAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.beneficiaryCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2',readOnly: true,allowBlank:true, },
                    { field: 'main.beneficiaryEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly: true,allowBlank:true, },
                    { field: 'main.swiftCode', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2' , readOnly: true,  allowBlank:true,  },
                    { field: 'main.cnaps', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2' , readOnly: true,  allowBlank:true, },
                    { field: 'main.bankAccount', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , readOnly: true,  allowBlank:false,  },
                    { field: 'main.currency',  xtype:'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2',
                        readOnly: true, allowBlank: false,
                    }
                ] },
                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetter', xtype: 'hidden'},
                    { field: 'main.guaranteeLetterId', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'guaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-13', },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.guaranteeLetterId',
                            scope: this, cls:'col', iconCls: 'fa fa-fw fa-eye', listeners: {
                            click: function(){
                                var id = Ext.getCmp(conf.mainFormPanelId).getCmpByName(this.hiddenName).getValue()
                                if(!id){
                                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
                                    return;
                                }
                                var def = {
                                    fileId : id,
                                    scope: this.scope
                                };
                                new FilesPreviewDialog(def).show();
                            }
                        }
                        }
                    ] },

                    { field: 'main.contractguaranteeLetter', xtype: 'hidden'},
                    { field: 'main.contractguaranteeLetterId', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'contractGuaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-13', },
                        { field: 'preview',  xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23, hiddenName: 'main.guaranteeLetterId',
                            scope: this,  cls:'col',   iconCls : 'fa fa-fw fa-eye', listeners: {
                            click: function(){
                                var id = Ext.getCmp(conf.mainFormPanelId).getCmpByName(this.hiddenName).getValue()
                                if(!id){
                                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
                                    return;
                                }
                                var def = {
                                    fileId : id,
                                    scope: this.scope
                                };
                                new FilesPreviewDialog(def).show();
                            }
                        }
                        }
                    ]}
                ] },

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.rateAudToRmb', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fRateAudToRmb, cls:'col-2',allowBlank: false , readOnly: true,
                        //value: curUserInfo.audToRmb
                    },
                    { field: 'main.paymentRateAudToRmb', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fNewestRateAudToRmb, cls:'col-2', allowBlank: false, readOnly: true,
                        value: curUserInfo.audToRmb
                    }

                ] },
                //汇率
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.rateAudToUsd', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fRateAudToUsd, cls:'col-2', allowBlank: false, readOnly: true,
                        //value: curUserInfo.audToUsd
                    },
                    { field: 'main.paymentRateAudToUsd', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fNewestRateAudToUsd, cls:'col-2', allowBlank: false, readOnly: true,
                        value: curUserInfo.audToUsd
                    }
                ] },

                {xtype: 'section', title:_lang.FlowFeePayment.tabPaymentInfo},
                { xtype: 'container',cls:'row',  items:  [
                    { field: 'main.dueDate', xtype: 'displayfield', fieldLabel: _lang.FlowDepositContract.fLatestPaymentTime, cls:'col-2', readOnly:true,  allowBlank:true, format: curUserInfo.dateFormat,  },
                    { field: 'main.paymentDate', xtype: 'datetimefield', fieldLabel: _lang.FlowFeePayment.fPaymentDate, cls:'col-2',readOnly:true,  allowBlank:true,
                        format: curUserInfo.dateFormat,  },
                ] },

                // { xtype: 'container',cls:'row', items:  [
                //     { field: 'main.paymentTotalPriceAud', xtype: 'textfield', fieldLabel: _lang.FlowFeeRegistration.fpaymentAud, cls:'col-2', readOnly:true, allowBlank:true,  },
                //     { field: 'main.paymentTotalPriceRmb', xtype: 'textfield', fieldLabel: _lang.FlowFeeRegistration.fpaymentRmb, cls:'col-2', readOnly:true, allowBlank:true,  },
                // ] },
                // { xtype: 'container',cls:'row', items:  [
                //     { field: 'main.paymentTotalPriceUsd', xtype: 'textfield', fieldLabel: _lang.FlowFeeRegistration.fpaymentUsd, cls:'col-2', readOnly:true, allowBlank:true,  },
                // ] },

                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2',items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:true,  allowBlank: true,  updatePriceFlag:1,
                            aud:{field:'main.totalPriceAud', fieldLabel:   _lang.FlowFeeRegistration.fExpectedAud, readOnly:true, },
                            rmb:{field:'main.totalPriceRmb', fieldLabel: _lang.FlowFeeRegistration.fExpectedRmb, readOnly:true,},
                            usd:{field:'main.totalPriceUsd', fieldLabel:  _lang.FlowFeeRegistration.fExpectedUsd, readOnly:true,},
                        }),

                    },
                    { xtype: 'container',cls:'col-2', items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:this.readOnly, allowBlank: true, updatePriceFlag: this.updatePrice ? null : 1,
                            aud:{field:'main.paymentTotalPriceAud', fieldLabel:  _lang.FlowFeeRegistration.fReceivedAud, readOnly:true, },
                            rmb:{field:'main.paymentTotalPriceRmb', fieldLabel:_lang.FlowFeeRegistration.fReceivedRmb, readOnly:true,},
                            usd:{field:'main.paymentTotalPriceUsd', fieldLabel: _lang.FlowFeeRegistration.fReceivedUsd, readOnly:true,},
                        }),
                     },
                ] },




                //费用登记附件信息
                { xtype: 'section', title:_lang.FlowFeePayment.tabFeeRegisterAttachmentInformation},
                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-projectContainer',  items:  [
                    { field: 'main.project', xtype: 'FlowFeeRegistrationProjectFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true, readOnly:true,
                        formGridPanelId:conf.subFormGridPanelId,
                    },
                ] },
                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-priceContainer', hidden:true, items:  [
                    { field: 'main.priceContainer', xtype: 'FlowFeeRegistrationPriceFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, readOnly:true,
                        allowBlank:true, formGridPanelId:conf.subFormGridPanelId,
                    },
                ] },
                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-otherContainer', hidden:true,  items:  [
                    { field: 'main.products', xtype: 'FlowFeeRegistrationPurchaseContractProductFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: true,
                        formGridPanelId:  conf.subFormGridPanelId + '-purchaseContractProduct',
                    },
                    { field: 'main.otherContainer', xtype: 'FlowFeeRegistrationOtherFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                        readOnly: true,  hidden:true, mainFormPanelId: conf.mainFormPanelId,
                        formGridPanelId:conf.subFormGridPanelId,
                    },
                ] },

                { field: 'main_documents', xtype:'hidden', value:this.record.attachment ,allowBlank:true, },
                { field: 'main_documentName', xtype:'hidden', allowBlank:true, },
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId + '-feeRegistration', mainFormPanelId:conf.mainFormPanelId+ '-feeRegistration',
                    valueName:'main_documents',titleName: 'main_documentName',
                    scope:this, readOnly: true,allowBlank:true,
                },


                //费用支付附件信息
                { xtype: 'section', title:_lang.FlowFeePayment.tabFeePaymentAttachmentInformation},
                { field: 'main_FeePaymentDocuments', xtype:'hidden', value:this.record.attachment ,allowBlank:true, },
                { field: 'main_FeePaymentDocumentName', xtype:'hidden', allowBlank:true, },
                { field: 'main.FeePaymentAttachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    valueName:'main_FeePaymentDocuments',titleName: 'main_FeePaymentDocumentName',
                    scope:this,allowBlank:true,
                },


                //备注
                { xtype: 'section', title:_lang.TText.fRemark},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 200,  allowBlank:true, },
                ] },

            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });


        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {

			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
					var json = Ext.JSON.decode(response.responseText);

                    cmpFormPlanId.rateAudToRmb = json.data.rateAudToRmb;
                    cmpFormPlanId.rateAudToUsd = json.data.rateAudToUsd;
                    cmpFormPlanId.getCmpByName('main.feeRegistrationName').setValue(json.data.feeRegisterBusinessId);

                    if(!(json.data.flowStatus  == 2 || json.data.flowStatus == 3)){

                        cmpFormPlanId.getCmpByName('main.paymentRateAudToRmb').setValue(curUserInfo.audToRmb);
                        cmpFormPlanId.getCmpByName('main.paymentRateAudToUsd').setValue(curUserInfo.audToUsd);

                        if(json.data.currency == 1){
					  		cmpFormPlanId.getCmpByName('main.paymentTotalPriceAud').setValue(json.data.paymentTotalPriceAud);
					  	}else if(json.data.currency == 2){
							cmpFormPlanId.getCmpByName('main.paymentTotalPriceRmb').setValue(json.data.paymentTotalPriceRmb);
					  	}else{
					  		cmpFormPlanId.getCmpByName('main.paymentTotalPriceUsd').setValue(json.data.paymentTotalPriceUsd);
					  	}
                    }

       
                    var registrationInfomation = json.data.flowFeeRegisterVo;

                    if(json.data.flowFeeRegisterVo.feeType == 3){
                        Ext.getCmp(conf.mainFormPanelId + '-priceContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-otherContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                        if(registrationInfomation.details != undefined && registrationInfomation.details.length>0){

                            var orderId = registrationInfomation.orderId;
                            cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().removeAll();
                            var contractDetails = registrationInfomation.customClearancePackingDetailVos || {};
                            var productDetails =  registrationInfomation.contractDetails || {};
                            if(contractDetails.length > 0){
                                for(var index in contractDetails){
                                    var product = {};
                                    Ext.applyIf(product, contractDetails[index]);
                                    Ext.apply(product, contractDetails[index].product);
                                    Ext.apply(product, contractDetails[index].product.prop);
                                    product.orderValueAud = parseFloat(contractDetails[index].priceAud) *parseFloat(contractDetails[index].packingQty) ;
                                    product.orderValueRmb = parseFloat(contractDetails[index].priceRmb) * parseFloat(contractDetails[index].packingQty);
                                    product.orderValueUsd = parseFloat(contractDetails[index].priceUsd) * parseFloat(contractDetails[index].packingQty);
                                    product.orderQty =  contractDetails[index].packingQty;
                                    product.orderCartons =   contractDetails[index].packingCartons;
                                    product.sku =  contractDetails[index].product.sku;
                                    product.id =  contractDetails[index].id;
                                    cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().add(product);
                                }
                            }else if(productDetails.length > 0){
                                for(var index in productDetails){
                                    var product = {};
                                    Ext.applyIf(product, productDetails[index]);
                                    Ext.apply(product, productDetails[index].product);
                                    Ext.apply(product, productDetails[index].product.prop);
                                    product.orderCartons =   parseFloat(productDetails[index].orderQty) / parseFloat(productDetails[index].product.prop.pcsPerCarton);
                                    product.sku =  productDetails[index].product.sku;
                                    product.id =  productDetails[index].id;
                                    cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().add(product);
                                }
                            };


                            for(index in registrationInfomation.details){
                                var feeRegistrationDetails = {};
                                Ext.applyIf(feeRegistrationDetails, registrationInfomation.details[index]);
                                cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().add(feeRegistrationDetails);
                            }
                        }
                    }else if(json.data.flowFeeRegisterVo.feeType == 4){
                        Ext.getCmp(conf.mainFormPanelId + '-priceContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-otherContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                    }else{
                        Ext.getCmp(conf.mainFormPanelId + '-priceContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').show();
                        if(registrationInfomation.details != undefined && registrationInfomation.details.length>0){
                            for(index in registrationInfomation.details){
                                var feeRegistrationDetails = {};
                                Ext.applyIf(feeRegistrationDetails, registrationInfomation.details[index]);
                                feeRegistrationDetails.subtotalAud =  (parseFloat(registrationInfomation.details[index].priceAud) * parseFloat(registrationInfomation.details[index].qty)).toFixed(3);
                                feeRegistrationDetails.subtotalRmb=   (parseFloat(registrationInfomation.details[index].priceRmb) *  parseFloat(registrationInfomation.details[index].qty)).toFixed(3);
                                feeRegistrationDetails.subtotalUsd =   (parseFloat(registrationInfomation.details[index].priceUsd) * parseFloat(registrationInfomation.details[index].qty)).toFixed(3);
                                cmpFormPlanId.getCmpByName('main.project').formGridPanel.getStore().add(feeRegistrationDetails);
                            }
                        }
                    }
                    if(!!registrationInfomation && !!registrationInfomation.otherDetails && registrationInfomation.otherDetails.length>0){
                        for(index in registrationInfomation.otherDetails){
                            var otherDetails = {};
                            Ext.applyIf(otherDetails, registrationInfomation.otherDetails[index]);
                            otherDetails.subtotalAud =  (parseFloat(registrationInfomation.otherDetails[index].priceAud) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(3);
                            otherDetails.subtotalRmb=   (parseFloat(registrationInfomation.otherDetails[index].priceRmb) *  parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(3);
                            otherDetails.subtotalUsd =   (parseFloat(registrationInfomation.otherDetails[index].priceUsd) * parseFloat(registrationInfomation.otherDetails[index].qty)).toFixed(3);
                            cmpFormPlanId.getCmpByName('main.otherContainer').formGridPanel.getStore().add(otherDetails);
                        }
                    }
                    if(registrationInfomation != undefined){
                        cmpFormPlanId.getCmpByName('main.orderId').setValue(registrationInfomation.orderId || '');
                        cmpFormPlanId.getCmpByName('main.orderNumber').setValue(registrationInfomation.orderNumber || '');
                        // cmpFormPlanId.getCmpByName('main.vendorName').setValue(registrationInfomation.vendorName || '');
                        cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(registrationInfomation.beneficiaryCnName || '');
                        cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(registrationInfomation.beneficiaryEnName || '');
                        cmpFormPlanId.getCmpByName('main.bankAccount').setValue(registrationInfomation.bankAccount || '');
                        cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(registrationInfomation.beneficiaryBank || '');
                        cmpFormPlanId.getCmpByName('main.companyCnName').setValue(registrationInfomation.companyCnName || '');
                        cmpFormPlanId.getCmpByName('main.companyEnName').setValue(registrationInfomation.companyEnName || '');
                        cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(registrationInfomation.companyCnAddress || '');
                        cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(registrationInfomation.companyEnAddress || '');
                        cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(registrationInfomation.beneficiaryBankAddress || '');
                        cmpFormPlanId.getCmpByName('main.swiftCode').setValue(registrationInfomation.swiftCode || '');
                        cmpFormPlanId.getCmpByName('main.cnaps').setValue(registrationInfomation.cnaps || '');
                        cmpFormPlanId.getCmpByName('main.currency').setValue(registrationInfomation.currency || 1);


                        if(registrationInfomation != undefined){
                            cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(!!registrationInfomation.guaranteeLetter ? registrationInfomation.guaranteeLetter : '');
                            cmpFormPlanId.getCmpByName('main.guaranteeLetterId').setValue(!!registrationInfomation.guaranteeLetter  ? registrationInfomation.guaranteeLetter : '');
                            cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue(!!registrationInfomation.guaranteeLetterName  ? registrationInfomation.guaranteeLetterName : '');
                            if(!!cmpFormPlanId.contractGuaranteeLetter){

                                cmpFormPlanId.getCmpByName('main.contractguaranteeLetter').setValue(!!registrationInfomation.contractGuaranteeLetter ? registrationInfomation.contractGuaranteeLetter : '');
                                cmpFormPlanId.getCmpByName('main.contractguaranteeLetterId').setValue(!!registrationInfomation.contractGuaranteeLetter  ? registrationInfomation.contractGuaranteeLetter : '');
                                cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue(!!registrationInfomation.contractguaranteeLetterName  ? registrationInfomation.contractguaranteeLetterName : '');

                            }
                        }


                        cmpFormPlanId.getCmpByName('main.totalPriceAud').setValue(registrationInfomation.totalPriceAud || '');
                        cmpFormPlanId.getCmpByName('main.totalPriceRmb').setValue(registrationInfomation.totalPriceRmb || '');
                        cmpFormPlanId.getCmpByName('main.totalPriceUsd').setValue(registrationInfomation.totalPriceUsd || '');
                        cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(registrationInfomation.rateAudToUsd || '');
                        cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(registrationInfomation.rateAudToRmb || '');
                        cmpFormPlanId.getCmpByName('main.remark').setValue(registrationInfomation.remark || '');
                        cmpFormPlanId.getCmpByName('main.dueDate').setValue(registrationInfomation.dueDate || '');
                        cmpFormPlanId.getCmpByName('main.title').setValue(registrationInfomation.title || '');
                        cmpFormPlanId.getCmpByName('main.paymentStatus').setValue(!!registrationInfomation.paymentStatus ? registrationInfomation.paymentStatus.toString() : '');
                        cmpFormPlanId.getCmpByName('main.feeType').setValue(!!registrationInfomation.feeType ? registrationInfomation.feeType.toString() : '');

                        if(registrationInfomation.attachments != undefined && registrationInfomation.attachments.length>0){
                            for(index in registrationInfomation.attachments){
                                var feeRegistrationAttachments = {};
                                feeRegistrationAttachments = registrationInfomation.attachments[index];
                                Ext.applyIf(feeRegistrationAttachments, registrationInfomation.attachments[index].document);
                                feeRegistrationAttachments.id= registrationInfomation.attachments[index].documentId;
                                cmpFormPlanId.getCmpByName('main.attachments').subGridPanel.getStore().add(feeRegistrationAttachments);
                            }
                        }
                    }

                  //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.FeePaymentAttachments').setValue(json.data.attachments);
				}
			});
		}

		this.editFormPanel.setReadOnly(this.readOnly, [
            'flowRemark','flowNextHandlerAccount', 'main.totalPriceAud', 'main.totalPriceRmb','main.currency',
            'main.rateAudToRmb','main.totalPriceUsd', 'main.rateAudToUsd', 'main.feeType','main.paymentStatus','main.FeePaymentAttachments'
        ]);
	

    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
        //
         var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        //
        // var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-project')});
        // if(!!rows && rows.length>0){
		// 	for(index in rows){
		// 		params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
		// 		for(key in rows[index].data){
         //            params['details['+index+'].'+key ] = rows[index].data[key];
		// 		}
		// 	}
		// }

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.FeePaymentAttachments').subGridPanel});
        if(!!rows && rows.length>0){
			for(index in rows){
				params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				params['attachments['+index+'].documentId'] = rows[index];
			}
		}
        //查看
        // console.log(Ext.getCmp(this.mainFormPanelId).getForm().getFieldValues());
        // console.log(Ext.getCmp(this.mainFormPanelId).form.findField('remark'));
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();
        if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == ''){
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        }else {
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url: isFlow ? this.urlFlow : this.urlSave,
                params: params,
                callback: function (fp, action, status, grid) {
                    Ext.getCmp(this.winId).close();
                    if(!!grid) {
                        grid.getSelectionModel().clearSelections();
                        grid.getView().refresh();
                    }
                }
            });
        }
    },

});