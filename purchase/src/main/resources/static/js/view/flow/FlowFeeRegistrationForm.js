FlowFeeRegistrationForm = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.updatePrice = !(this.record.flowStatus  == 2 || this.record.flowStatus == 3) ? true : false;


        var conf = {
            title : _lang.FlowFeeRegistration.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowFeeRegistration',
            winId : 'FlowFeeRegistrationForm',
            frameId : 'FlowFeeRegistrationView',
            mainGridPanelId : 'FlowFeeRegistrationGridPanelID',
            mainFormPanelId : 'FlowFeeRegistrationFormPanelID',
            processFormPanelId: 'FlowFeeRegistrationProcessFormPanelID',
            searchFormPanelId: 'FlowFeeRegistrationSearchFormPanelID',
            mainTabPanelId: 'FlowFeeRegistrationMainTabsPanelID',
            subFormGridPanelId : 'FlowFeeRegistrationSubGridPanelID',
            subFormOtherGridPanelId : 'FlowFeeRegistrationSubOtherGridPanelID',
            formGridPanelId : 'FlowFeeRegistrationProjectFormGridPanelID',

            urlList: __ctxPath + 'flow/finance/feeRegister/list',
            urlSave: __ctxPath + 'flow/finance/feeRegister/save',
            urlDelete: __ctxPath + 'flow/finance/feeRegister/delete',
            urlGet: __ctxPath + 'flow/finance/feeRegister/get',
            urlFlow: __ctxPath + 'flow/finance/feeRegister/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowFeeRegister&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowFeeRegistrationForm.superclass.constructor.call(this, {
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

                { xtype: 'section', title:_lang.FlowFeeRegistration.tabTitle},
                { xtype: 'container',cls:'row', scope: this, items: [
                    { field: 'main.title', xtype: 'textfield', fieldLabel: _lang.FlowFeeRegistration.fTitle, cls:'col-2' , allowBlank:false,  },
                    { field: 'main.paymentStatus', xtype: 'dictfield', fieldLabel: _lang.FlowFeeRegistration.fPaymentStatus, cls:'col-2' ,
                        code:'payment', codeSub:'payment_status',  value: '1',
                    },
                ]},

                //登记信息
                { xtype: 'section', title:_lang.FlowFeeRegistration.tabRegisterInfomation},
                { xtype: 'container',cls:'row', scope: this, items: [
                    { field: 'main.feeType', xtype: 'dictcombo', fieldLabel: _lang.FlowFeeRegistration.fFeeType, cls:'col-2',  code:'service_provider', codeSub:'fee_type',
                        allowBlank: false,  scope: this,  selectFun: function(records, eOpts){
                        //修改显示的费用类型
                        this.scope.changeType.call(this, records[0].data.id, null, conf.mainFormPanelId);
                        //设置银行账号信息为可填
                    },
                    },
                ],
                },
                // 基础信息
                {xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', id: conf.mainFormPanelId + '-vendorContainer', allowBlank:true,  cls:'row', items: $groupFormVendorFields(this, conf, {
                        readOnly: this.readOnly,
                        hideDetails: this.action == 'add',
                        allowBlank: true,
                        callback: function (eOpts, record) {
                            var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                            cmpFormPlanId.currency =  record.currency;
                            cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue('');
                            cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue('');
                            cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue('');
                            cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue('');
                            cmpFormPlanId.getCmpByName('main.bankAccount').setValue('');
                            cmpFormPlanId.getCmpByName('main.companyCnName').setValue('');
                            cmpFormPlanId.getCmpByName('main.companyEnName').setValue('');
                            cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue('');
                            cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue('');
                            cmpFormPlanId.getCmpByName('main.swiftCode').setValue('');
                            cmpFormPlanId.getCmpByName('main.cnaps').setValue('');
                            cmpFormPlanId.getCmpByName('main.currency').setValue('');
                            cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue( '');
                            cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue('');
                            cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                            cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue('');
                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue('');
                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue('');
                            cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue('');
                            var tpData = $HpStore({
                                fields: ['id', 'bank'],
                                url: __ctxPath + 'archives/vendor/get?id=' + record.id,
                                callback: function (conf, records, eOpts) {
                                    var bankInfomation = records[0].data.bank || {};
                                    cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                                    cmpFormPlanId.getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                                    cmpFormPlanId.getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                                    cmpFormPlanId.getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                                    cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                                    cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                                    cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                                    cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                                    cmpFormPlanId.getCmpByName('main.currency').setValue(!!bankInfomation.currency ? bankInfomation.currency.toString() : '');
                                    cmpFormPlanId.getCmpByName('main.depositType').setValue(bankInfomation.depositType || '');
                                    cmpFormPlanId.getCmpByName('main.depositRate').setValue(bankInfomation.depositRate || '');
                                    if(bankInfomation.guaranteeLetter) {
                                        cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.documentId || '');
                                        cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                    }

                                    //contract guarantee letter
                                    if(bankInfomation.contractGuaranteeLetter) {
                                        cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.documentId || '');
                                        cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                    }
                                }
                            })
                        },
                    }),
                    },

                    { xtype: 'container',id: conf.mainFormPanelId + '-serviceProviderContainer', cls:'row',  hidden:true,
                        items: $groupFormServiceProviderFields(this, conf, {
                            hideDetails: this.action == 'add',
                            readOnly: this.readOnly,
                            allowBlank: true,
                            callback: function (eOpts, record) {

                                var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                cmpFormPlanId.currency =  record.currency;
                                cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue('');
                                cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue('');
                                cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue('');
                                cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue('');
                                cmpFormPlanId.getCmpByName('main.bankAccount').setValue('');
                                cmpFormPlanId.getCmpByName('main.companyCnName').setValue('');
                                cmpFormPlanId.getCmpByName('main.companyEnName').setValue('');
                                cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue('');
                                cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue('');
                                cmpFormPlanId.getCmpByName('main.swiftCode').setValue('');
                                cmpFormPlanId.getCmpByName('main.cnaps').setValue('');
                                cmpFormPlanId.getCmpByName('main.currency').setValue('');
                                cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                                cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue( '');
                                cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue('');
                                cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                                cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue('');
                                cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue('');
                                cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue('');
                                cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue('');
                                var tpData = $HpStore({
                                    fields: ['id', 'bank'],
                                    url: __ctxPath + 'archives/service_provider/get?id=' + record.id,
                                    callback: function (conf, records, eOpts) {
                                        var bankInfomation = records[0].data.bank || {};
                                        cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                                        cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                                        cmpFormPlanId.getCmpByName('main.vendorId').setValue(bankInfomation.vendorId || '');
                                        cmpFormPlanId.getCmpByName('main.vendorName').setValue(bankInfomation.vendorName || '');
                                        cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                                        cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                                        cmpFormPlanId.getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                                        cmpFormPlanId.getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                                        cmpFormPlanId.getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                                        cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                                        cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                                        cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                                        cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                                        cmpFormPlanId.getCmpByName('main.currency').setValue(!!bankInfomation.currency ? bankInfomation.currency.toString() : '');
                                        if(!!bankInfomation.guaranteeLetter) {
                                            cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.documentId || '');
                                            cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                            cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                        }
                                        //contract guarantee letter
                                        if(!!bankInfomation.contractGuaranteeLetter) {
                                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.documentId || '');
                                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                            cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                        }

                                    }
                                })
                            },
                        }),
                    },
                    { field:'main.orderId', xtype:'hidden', allowBlank:true, },
                    { field:'main.orderTitle', xtype:'hidden', allowBlank:true, },
                    { xtype: 'container',id: conf.mainFormPanelId + '-orderContainer', allowBlank:true, cls:'row', hidden:true, items:
                        [
                            {xtype: 'section', title:_lang.FlowFeeRegistration.tabOrder},
                            { field: 'main.orderNumber', xtype: 'OrderDialog', fieldLabel: _lang.FlowOrderQualityInspection.fOrderNumber, cls:'col-2',
                                hiddenName:'main.orderId', formId:conf.mainFormPanelId, single: true, formal: true, type:4,  readOnly: this.readOnly,
                                subcallback: function(rows){
                                    //var orderId =
                                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                    cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().removeAll();
                                    cmpFormPlanId.getCmpByName('main.orderTitle').setValue(rows.data.orderTitle);
                                    cmpFormPlanId.currency =  rows.data.currency;
                                    var  rateAudToRmb =  rows.data.rateAudToRmb;
                                    var  rateAudToUsd =  rows.data.rateAudToUsd;
                                    cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue('');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue('');
                                    cmpFormPlanId.getCmpByName('main.bankAccount').setValue('');
                                    cmpFormPlanId.getCmpByName('main.companyCnName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.companyEnName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue('');
                                    cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue('');
                                    cmpFormPlanId.getCmpByName('main.swiftCode').setValue('');
                                    cmpFormPlanId.getCmpByName('main.cnaps').setValue('');
                                    cmpFormPlanId.getCmpByName('main.currency').setValue('');
                                    cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue( '');
                                    cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue('');
                                    cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue('');
                                    cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue('');
                                    cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue('');
                                    cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue('');
                                    cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue('');

                                    if(cmpFormPlanId.currency == '1'){
                                        cmpFormPlanId.getCmpByName('main.totalPriceAud').setValue(rows.data.adjustBalanceAud || 0);
                                    }else if(cmpFormPlanId.currency == '2'){
                                        cmpFormPlanId.getCmpByName('main.totalPriceRmb').setValue(rows.data.adjustBalanceRmb || 0);
                                    }else if(cmpFormPlanId.currency == '3'){
                                        cmpFormPlanId.getCmpByName('main.totalPriceUsd').setValue(rows.data.adjustBalanceUsd || 0);
                                    };
                                    //应付差额
                                    var price = [];
                                    price.push({
                                        'itemCnName' :_lang.FlowFeeRegistration.fBalance,
                                        'itemEnName' :_lang.FlowFeeRegistration.fBalance,
                                        'currency' :cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.balanceAud || 0,
                                        'priceRmb' : rows.data.balanceRmb || 0,
                                        'priceUsd' : rows.data.balanceUsd || 0,
                                        'applyCost' : 2,
                                        'qty' : 1,
                                    });
                                    price.push({
                                        'itemCnName' : _lang.FlowFeeRegistration.fDeposit,
                                        'itemEnName' : _lang.FlowFeeRegistration.fDeposit,
                                        'currency' : cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.depositAud || 0,
                                        'priceRmb' : rows.data.depositRmb || 0,
                                        'priceUsd' : rows.data.depositUsd || 0,
                                        'qty' : 1,
                                        'applyCost' : 1,
                                    });
                                    price.push({
                                        'itemCnName' : _lang.FlowFeeRegistration.fWriteOffPrice,
                                        'itemEnName' : _lang.FlowFeeRegistration.fWriteOffPrice,
                                        'currency' : cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.writeOffAud || 0,
                                        'priceRmb' : rows.data.writeOffRmb || 0,
                                        'priceUsd' : rows.data.writeOffUsd || 0,
                                        'applyCost' : 2,
                                        'qty' : 1,
                                    });
                                    price.push({
                                        'itemCnName' :  _lang.FlowFeeRegistration.fAdjustBalance,
                                        'itemEnName' :  _lang.FlowFeeRegistration.fAdjustBalance,
                                        'currency' :cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.adjustBalanceAud || 0,
                                        'priceRmb' : rows.data.adjustBalanceRmb || 0,
                                        'priceUsd' : rows.data.adjustBalanceUsd || 0,
                                        'applyCost' : 2,
                                        'qty' : 1,
                                    });
                                    price.push({
                                        'itemCnName' :  _lang.FlowFeeRegistration.fAdjustValueBalance,
                                        'itemEnName' :  _lang.FlowFeeRegistration.fAdjustValueBalance,
                                        'currency' :cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.adjustValueBalanceAud || 0,
                                        'priceRmb' : rows.data.adjustValueBalanceRmb || 0,
                                        'priceUsd' : rows.data.adjustValueBalanceUsd || 0,
                                        'applyCost' : 1,
                                        'qty' : 1,
                                    });
                                    price.push({
                                        'itemCnName' : _lang.FlowFeeRegistration.fTotalPrice,
                                        'itemEnName' : _lang.FlowFeeRegistration.fTotalPrice,
                                        'currency' : cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : rows.data.totalPriceAud || 0,
                                        'priceRmb' : rows.data.totalPriceRmb || 0,
                                        'priceUsd' : rows.data.totalPriceUsd || 0,
                                        'applyCost' : 2,
                                        'qty' : 1,
                                    });
                                    if(rows.data.totalOtherAud != null && rows.data.totalOtherAud != '0.00') {
                                        var tempObj = {
                                            'itemCnName': _lang.FlowFeeRegistration.fOtherBalance,
                                            'itemEnName': _lang.FlowFeeRegistration.fOtherBalance,
                                            'currency': cmpFormPlanId.currency,
                                            'rateAudToRmb': rateAudToRmb,
                                            'rateAudToUsd': rateAudToUsd,
                                            'priceAud':  rows.data.totalOtherAud || 0,
                                            'priceRmb':  rows.data.totalOtherRmb || 0,
                                            'priceUsd':  rows.data.totalOtherUsd || 0,
                                            'applyCost': 1,
                                            'qty': 1,
                                        };
                                    }

                                    for (var i in price){
                                        cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().add(price[i]);
                                    }

                                    Ext.getCmp(conf.subFormGridPanelId).getStore().removeAll();
                                    var otherDetail = rows.data.otherDetails;
                                    var totalBalanceRefundOtherAud = 0, totalBalanceRefundOtherRmb = 0, totalBalanceRefundOtherUsd = 0;
                                    var detailArr = [];
                                    if(!!otherDetail && !!otherDetail &&otherDetail.length>0) {
                                        for (var index in otherDetail) {
                                            var details = {};
                                            if(otherDetail[index].settlementType != 1){
                                                Ext.applyIf(details, otherDetail[index]);
                                                details.subtotalAud =  (parseFloat(otherDetail[index].priceAud) * parseFloat(otherDetail[index].qty)).toFixed(2);
                                                details.subtotalRmb =   (parseFloat(otherDetail[index].priceRmb) *  parseFloat(otherDetail[index].qty)).toFixed(2);
                                                details.subtotalUsd =   (parseFloat(otherDetail[index].priceUsd) * parseFloat(otherDetail[index].qty)).toFixed(2);

                                                //统计其他费用在合同定金下需要支付的总金额
                                                if(otherDetail[index].settlementType == 3 && (rows.data.vendorBank.depositType == 2 && rows.data.vendorBank.depositRate > 0)){
                                                    //按定金率分摊
                                                    totalBalanceRefundOtherAud = totalBalanceRefundOtherAud + (details.subtotalAud * (1-parseFloat(rows.data.depositRate)));
                                                    totalBalanceRefundOtherRmb = totalBalanceRefundOtherRmb + (details.subtotalRmb * (1-parseFloat(rows.data.depositRate)));
                                                    totalBalanceRefundOtherUsd = totalBalanceRefundOtherUsd + (details.subtotalUsd * (1-parseFloat(rows.data.depositRate)));
                                                }else {
                                                    //统计在合同定金下支付
                                                    totalBalanceRefundOtherAud += parseFloat(details.subtotalAud);
                                                    totalBalanceRefundOtherRmb += parseFloat(details.subtotalRmb);
                                                    totalBalanceRefundOtherUsd += parseFloat(details.subtotalUsd);
                                                }

                                                Ext.getCmp(conf.subFormGridPanelId).getStore().add(details);
                                            }
                                        }
                                    };

                                    cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().removeAll();
                                    var contractDetails = rows.data.customClearancePackingDetailVos || {};
                                    var productDetails =  rows.data.details || {};
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

                                    //计算通过实际尾款计算应付款项
                                    var totalAud = parseFloat(rows.data.adjustBalanceAud || 0);
                                    var totalRmb = parseFloat(rows.data.adjustBalanceRmb || 0);
                                    var totalUsd = parseFloat(rows.data.adjustBalanceUsd || 0);

                                    var tempObj = {
                                        'itemCnName' : _lang.FlowFeeRegistration.fOtherBalance,
                                        'itemEnName' : _lang.FlowFeeRegistration.fOtherBalance,
                                        'currency' : cmpFormPlanId.currency,
                                        'rateAudToRmb':rateAudToRmb,
                                        'rateAudToUsd':rateAudToUsd,
                                        'priceAud' : totalBalanceRefundOtherAud || 0,
                                        'priceRmb' : totalBalanceRefundOtherRmb || 0,
                                        'priceUsd' : totalBalanceRefundOtherUsd || 0,
                                        'applyCost' : 2,
                                        'qty' : 1,
                                    };

                                    cmpFormPlanId.totalBalanceRefundOtherAud = totalBalanceRefundOtherAud;
                                    cmpFormPlanId.totalBalanceRefundOtherRmb = totalBalanceRefundOtherRmb;
                                    cmpFormPlanId.totalBalanceRefundOtherUsd = totalBalanceRefundOtherUsd;

                                    cmpFormPlanId.getCmpByName('main.priceContainer').formGridPanel.getStore().add(tempObj);
                                    if(rows.data.currency == 1){
                                        //设置其他费用
                                        cmpFormPlanId.getCmpByName('main.totalPriceAud').setValue(totalAud);
                                    }else if(rows.data.currency == 2){
                                        //设置其他费用
                                        cmpFormPlanId.getCmpByName('main.totalPriceRmb').setValue(totalRmb);
                                    }else{
                                        //设置其他费用
                                        cmpFormPlanId.getCmpByName('main.totalPriceUsd').setValue(totalUsd);
                                    }
                                    cmpFormPlanId.adjustBalanceAud = cmpFormPlanId.getCmpByName('main.totalPriceAud').getValue();
                                    cmpFormPlanId.adjustBalanceRmb = cmpFormPlanId.getCmpByName('main.totalPriceRmb').getValue();
                                    cmpFormPlanId.adjustBalanceUsd = cmpFormPlanId.getCmpByName('main.totalPriceUsd').getValue();

                                    //cmpFormPlanId.getCmpByName('main.vendorId').setValue(records[0].data.vendor.id);
                                    var bankInfomation =  rows.data.vendorBank;
                                    cmpFormPlanId.getCmpByName('main.depositType').setValue(bankInfomation.depositType || '');
                                    cmpFormPlanId.getCmpByName('main.depositRate').setValue(bankInfomation.depositRate || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                                    cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                                    cmpFormPlanId.getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                                    cmpFormPlanId.getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                                    cmpFormPlanId.getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                                    cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                                    cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                                    cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                                    cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                                    cmpFormPlanId.getCmpByName('main.currency').setValue(!!bankInfomation.currency ? bankInfomation.currency.toString() : '0');
                                    if(bankInfomation.guaranteeLetter) {
                                        cmpFormPlanId.getCmpByName('guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('main.guaranteeLetterName').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.documentId || '');
                                    }
                                    //contract guarantee letter
                                    if(bankInfomation.contractGuaranteeLetter) {
                                        cmpFormPlanId.getCmpByName('contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('main.contractGuaranteeLetterName').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                        cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.documentId || '');
                                    }
                                }
                            }
                        ]
                    }

                ] },

                //
                //收款账号信息
                { xtype: 'section', title:_lang.FlowBankAccount.tabAccountInformation},
                { xtype: 'container',cls:'row', items:  [

                    { field: 'main.companyCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2' , readOnly: true, allowBlank:false, },
                    { field: 'main.companyEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2',  readOnly: true, allowBlank:false, },
                    { field: 'main.companyCnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.companyEnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2' , readOnly: true,  allowBlank:false,  },
                    { xtype: 'container',cls:'row', id:'depositContainer', hidden:true,	 items:  [
                        { field: 'main.depositType', xtype: 'dictfield', fieldLabel: _lang.FlowBankAccount.fDepositType, cls:'col-2',  allowBlank: false, code:'purchase', codeSub:'deposit_rate', readOnly:true, },
                        { field: 'main.depositRate', xtype: 'displayfield', fieldLabel: _lang.FlowBankAccount.fDepositRate, cls:'col-2',  allowBlank: false, value:0, decimalPrecision:2,  readOnly:true,},
                    ] },
                    { field: 'main.beneficiaryBank', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.beneficiaryBankAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2' , readOnly: true,  allowBlank:false, },
                    { field: 'main.beneficiaryCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2',readOnly: true,allowBlank:true, },
                    { field: 'main.beneficiaryEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly: true,allowBlank:true, },
                    { field: 'main.swiftCode', xtype: 'textfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2' , readOnly: true,   },
                    { field: 'main.cnaps', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2' , readOnly: true,  },
                    { field: 'main.bankAccount', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , readOnly: true,  allowBlank:false,  },
                    { field: 'main.currency',  xtype:'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2' ,
                        readOnly: true, allowBlank: false,}
                ] },

                //保函&担保函
                // { xtype: 'container', cls:'row', items:  [
                //     { field: 'main.guaranteeLetter', xtype: 'hidden', allowBlank:false, },
                //     { field: 'main.guaranteeLetterName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-2',
                //         formId: conf.mainFormPanelId, hiddenName: 'main.guaranteeLetter', single:true, readOnly: true, allowBlank:false,
                //     },
                //     { field: 'main.contractGuaranteeLetter', xtype: 'hidden', allowBlank:true, },
                //     { field: 'main.contractGuaranteeLetterName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-2',
                //         formId: conf.mainFormPanelId, hiddenName: 'main.contractGuaranteeLetter', single:true, readOnly: true,allowBlank:true,
                //     }
                // ] },


                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetter', xtype: 'hidden'},
                    { field: 'main.guaranteeLetterName', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'guaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-13', },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.guaranteeLetter',
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

                    { field: 'main.contractGuaranteeLetter', xtype: 'hidden'},
                    { field: 'main.contractGuaranteeLetterName', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'contractGuaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-13', },
                        { field: 'preview',  xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23, hiddenName: 'main.contractGuaranteeLetter',
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
                    { xtype: 'container', cls:'col-2',  id: conf.mainFormPanelId + '-totalPriceContainer', items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:this.readOnly,  allowBlank: true, allowNegative:true, updatePriceFlag : this.updatePrice ? 0 : 1,
                            aud:{field:'main.totalPriceAud', fieldLabel: _lang.ProductDocument.fTotalPriceAud},
                            rmb:{field:'main.totalPriceRmb', fieldLabel: _lang.ProductDocument.fTotalPriceRmb},
                            usd:{field:'main.totalPriceUsd', fieldLabel: _lang.ProductDocument.fTotalPriceUsd},
                        }),

                    },
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.rateAudToRmb',fieldLabel:_lang.FlowDepositContract.fRateAudToRmb,  xtype: 'textfield',  value: curUserInfo.audToRmb, allowBlank:true,
                            readOnly:true,  cls:'col-1'
                        },
                        { field: 'main.rateAudToUsd', fieldLabel: _lang.FlowDepositContract.fRateAudToUsd, xtype: 'textfield', value: curUserInfo.audToUsd, allowBlank:true,
                            readOnly:true, cls:'col-1'
                        },
                    ] },
                ] },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.dueDate', xtype: 'datetimefield', fieldLabel: _lang.FlowDepositContract.fLatestPaymentTime, cls:'col-2',readOnly:true,
                        allowBlank:true, format: curUserInfo.dateFormat,
                    },
                ] },


                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-projectContainer', items:  [
                    { field: 'main.project', xtype: 'FlowFeeRegistrationProjectFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                        formGridPanelId:conf.subFormGridPanelId,
                        dataChangeCallback: this.onGridDataChange,
                    },
                ] },

                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-priceContainer',hidden:true,  items:  [
                    { field: 'main.priceContainer', xtype: 'FlowFeeRegistrationPriceFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true, hidden:true,
                        formGridPanelId:conf.subFormGridPanelId,
                    },
                ] },

                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-otherContainer', hidden:true,  items:  [

                    { field: 'main.products', xtype: 'FlowFeeRegistrationPurchaseContractProductFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: true,
                        formGridPanelId:  conf.subFormGridPanelId + '-purchaseContractProduct',
                    },

                    { field: 'main.otherContainer', xtype: 'FlowFeeRegistrationOtherFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                        hidden:true, readOnly: this.readOnly,
                        dataChangeCallback: this.onGridDataChange,
                        formGridPanelId:conf.subFormGridPanelId,
                    },
                ] },

                //备注
                { xtype: 'section', title:_lang.TText.fRemark},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 200,  allowBlank:true, },
                ] },


                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment ,allowBlank:true, },
                { field: 'main_documentName', xtype:'hidden', allowBlank:true, },
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.isApprove, allowBlank:true,
                },

            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });


        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){

                    var json = Ext.JSON.decode(response.responseText);
                    var bank = json.data.vendor ? json.data.vendor.bank : '';
                    var feeType = json.data.feeType;
                    Ext.getCmp(conf.mainFormPanelId).currency = json.data.currency;

                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                    if(!(json.data.flowStatus  == 2 || json.data.flowStatus == 3)){
                        cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(curUserInfo.audToRmb);
                        cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(curUserInfo.audToUsd);

                        if(json.data.currency == 1){
                            cmpFormPlanId.getCmpByName('main.totalPriceAud').setValue(json.data.totalPriceAud);
                        }else if(json.data.currency == 2){
                            cmpFormPlanId.getCmpByName('main.totalPriceRmb').setValue(json.data.totalPriceRmb);
                        }else{
                            cmpFormPlanId.getCmpByName('main.totalPriceUsd').setValue(json.data.totalPriceUsd);
                        }
                    }

                    if(feeType == 1 || feeType == 5){
                        Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-orderContainer').hide();
                    }else if(feeType == 2){
                        Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-orderContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceProviderId').setValue(json.data.vendorId);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceProviderName').setValue(json.data.vendorName);
                        //service provider details
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.cnName').setValue(json.data.vendor.cnName);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.enName').setValue(json.data.vendor.enName);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.address').setValue(json.data.vendor.address);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.director').setValue(json.data.vendor.director);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.abn').setValue(json.data.vendor.abn);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.currency').setValue(json.data.vendor.currency.toString());
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.website').setValue(json.data.vendor.website);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('serviceProvider.source').setValue(json.data.vendor.source);

                    }else if(feeType == 3){
                        Ext.getCmp(conf.mainFormPanelId).type = feeType;
                        Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-orderContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-priceContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-otherContainer').show();
                    }else{
                        Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-orderContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceAud').setReadOnly(false);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceRmb').setReadOnly(false);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceUsd').setReadOnly(false);
                    }

                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();

                    var contractDetails = json.data.contractDetails || {};
                    var packingDetails = json.data.customClearancePackingDetailVos || {};

                    if(!!json.data && !!packingDetails && packingDetails.length > 0){
                        for(var index in packingDetails){
                            var product = {};
                            Ext.applyIf(product, packingDetails[index]);
                            Ext.apply(product, packingDetails[index].product);
                            Ext.apply(product, packingDetails[index].product.prop);
                            product.orderValueAud = parseFloat(packingDetails[index].priceAud) *parseFloat(packingDetails[index].packingQty) ;
                            product.orderValueRmb = parseFloat(packingDetails[index].priceRmb) * parseFloat(packingDetails[index].packingQty);
                            product.orderValueUsd = parseFloat(packingDetails[index].priceUsd) * parseFloat(packingDetails[index].packingQty);
                            product.orderQty =  packingDetails[index].packingQty;
                            product.orderCartons =   parseFloat(packingDetails[index].packingQty) / parseFloat(packingDetails[index].product.prop.pcsPerCarton);
                            product.sku =  packingDetails[index].product.sku;
                            product.id =  packingDetails[index].id;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().add(product);
                        }
                    }else if(!!json.data && !!contractDetails && contractDetails.length > 0){
                        for(var index in contractDetails){
                            var product = {};
                            Ext.applyIf(product, contractDetails[index]);
                            Ext.apply(product, contractDetails[index].product);
                            Ext.apply(product, contractDetails[index].product.prop);
                            product.orderCartons =   parseFloat(contractDetails[index].orderQty) / parseFloat(contractDetails[index].product.prop.pcsPerCarton);
                            product.sku =  contractDetails[index].product.sku;
                            product.id =  contractDetails[index].id;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().add(product);
                        }
                    };

                    //details
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        for(var index in json.data.details){
                            var project = {};
                            Ext.applyIf(project, json.data.details[index]);
                            Ext.apply(project, json.data.details[index].product);
                            project.subtotalAud =  (parseFloat(json.data.details[index].priceAud) * parseFloat(json.data.details[index].qty)).toFixed(2);
                            project.subtotalRmb=   (parseFloat(json.data.details[index].priceRmb) *  parseFloat(json.data.details[index].qty)).toFixed(2);
                            project.subtotalUsd =   (parseFloat(json.data.details[index].priceUsd) * parseFloat(json.data.details[index].qty)).toFixed(2);
                            Ext.getCmp(conf.subFormGridPanelId + '-project').getStore().add(project);
                            Ext.getCmp(conf.subFormGridPanelId + '-price').getStore().add(project);
                        }
                    }

                    if(!!json.data && !!json.data.otherDetails && json.data.otherDetails.length>0){
                        for(var index in json.data.otherDetails){
                            var otherDetails = {};
                            Ext.applyIf(otherDetails, json.data.otherDetails[index]);
                            Ext.apply(otherDetails, json.data.otherDetails[index].product);

                            var totalBalanceRefundOtherAud=0, totalBalanceRefundOtherRmb=0, totalBalanceRefundOtherUsd=0;
                            if(json.data.otherDetails[index].settlementType != 1){
                                otherDetails.subtotalAud =  (parseFloat(json.data.otherDetails[index].priceAud) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                                otherDetails.subtotalRmb =   (parseFloat(json.data.otherDetails[index].priceRmb) *  parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                                otherDetails.subtotalUsd =   (parseFloat(json.data.otherDetails[index].priceUsd) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);

                                //统计其他费用在合同定金下需要支付的总金额
                                if(json.data.otherDetails[index].settlementType == 3 && (json.data.vendorBank.depositType == 2 && json.data.vendorBank.depositRate > 0)){
                                    //按定金率分摊
                                    totalBalanceRefundOtherAud = totalBalanceRefundOtherAud + (otherDetails.subtotalAud * (1-parseFloat(json.data.depositRate)));
                                    totalBalanceRefundOtherRmb = totalBalanceRefundOtherRmb + (otherDetails.subtotalRmb * (1-parseFloat(json.data.depositRate)));
                                    totalBalanceRefundOtherUsd = totalBalanceRefundOtherUsd + (otherDetails.subtotalUsd * (1-parseFloat(json.data.depositRate)));
                                }else {
                                    //统计在合同定金下支付
                                    totalBalanceRefundOtherAud += parseFloat(otherDetails.subtotalAud);
                                    totalBalanceRefundOtherRmb += parseFloat(otherDetails.subtotalRmb);
                                    totalBalanceRefundOtherUsd += parseFloat(otherDetails.subtotalUsd);
                                }
                            }

                            Ext.getCmp(conf.subFormGridPanelId).getStore().add(otherDetails);
                            Ext.getCmp(conf.subFormGridPanelId + '-project').getStore().add(otherDetails);

                            Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherAud = totalBalanceRefundOtherAud;
                            Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherRmb = totalBalanceRefundOtherRmb;
                            Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherUsd = totalBalanceRefundOtherUsd;
                            Ext.getCmp(conf.mainFormPanelId).adjustBalanceAud = json.data.totalPriceAud;
                            Ext.getCmp(conf.mainFormPanelId).adjustBalanceRmb = json.data.totalPriceRmb;
                            Ext.getCmp(conf.mainFormPanelId).adjustBalanceUsd = json.data.totalPriceUsd;
                        }
                    }
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);


                    var form = Ext.getCmp(conf.mainFormPanelId);
                    var priceRows = $getGdItems({grid: Ext.getCmp(conf.subFormGridPanelId + '-price')});
                    Ext.getCmp(conf.mainFormPanelId).adjustBalanceAud = priceRows[3].data['priceAud']
                    Ext.getCmp(conf.mainFormPanelId).adjustBalanceRmb = priceRows[3].data['priceRmb']
                    Ext.getCmp(conf.mainFormPanelId).adjustBalanceUsd = priceRows[3].data['priceUsd']
                    Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherAud =  priceRows[6].data['priceAud'];
                    Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherRmb =  priceRows[6].data['priceRmb'];
                    Ext.getCmp(conf.mainFormPanelId).totalBalanceRefundOtherUsd =  priceRows[6].data['priceUsd'];

                    //console.log()
                     Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceAud').setValue(json.data.totalPriceAud);
                     Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceRmb').setValue(json.data.totalPriceRmb);
                     Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalPriceUsd').setValue(json.data.totalPriceUsd);
                }
            });


        }
        if(this.action == 'add'){
            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.paymentStatus').hide();
        }
        this.editFormPanel.setReadOnly(this.readOnly, [
            'flowRemark','flowNextHandlerAccount',
            'main.beneficiaryBank','main.beneficiaryBankAddress','main.bankAccount','main.totalPriceRmb','main.totalPriceAud','main.totalPriceUsd',
            'main.companyCnName', 'main.companyEnName','main.companyCnAddress','main.companyEnAddress','main.swiftCode','main.cnaps','main.currency',
            'main_contractGuaranteeLetter', 'main_guaranteeLetter','main.rateAudToRmb','main.rateAudToUsd','main.depositType','main.depositRate', 'main.beneficiaryCnName',
            'main.beneficiaryEnName'
        ]);

    },// end of the init



    onGridDataChange: function(store, conf) {
        var totalPriceAud = 0;
        var totalPriceRmb = 0;
        var totalPriceUsd = 0;
        var priceData = this.editFormPanel.getCmpByName('main.priceContainer').formGridPanel.getStore().data.items;
        var data = this.editFormPanel.getCmpByName('main.otherContainer').formGridPanel.getStore().data.items;
        var productDetail = this.editFormPanel.getCmpByName('main.products').formGridPanel.getStore().data.items;
        var depositRate = parseFloat(this.editFormPanel.getCmpByName('main.depositRate').getValue())
        for (var i = 0; i < data.length; i++) {
            if(data[i].data.settlementType == 3){
                //按定金率分摊
                totalPriceAud = totalPriceAud + (data[i].data.subtotalAud * (1 - depositRate));
                totalPriceRmb = totalPriceRmb + (data[i].data.subtotalRmb * (1 - depositRate));
                totalPriceUsd = totalPriceUsd + (data[i].data.subtotalUsd * (1 - depositRate));
            }else {
                //统计在合同定金下支付
                totalPriceAud += parseFloat(data[i].data.subtotalAud || 0.00);
                totalPriceRmb += parseFloat(data[i].data.subtotalRmb || 0.00);
                totalPriceUsd += parseFloat(data[i].data.subtotalUsd|| 0.00);
            }
        }
        //更新不需要订金的产品
        // for (var i = 0; i < productDetail.length; i++) {
        //     if(productDetail[i].data.isNeedDeposit == 2){
        //         totalPriceAud = totalPriceAud +  parseFloat(productDetail[i].data.orderValueAud || 0.00);
        //         totalPriceRmb = totalPriceRmb + parseFloat(productDetail[i].data.orderValueRmb || 0.00);
        //         totalPriceUsd = totalPriceUsd + parseFloat(productDetail[i].data.orderValueUsd|| 0.00);
        //     }
        // };

        var $currency = this.editFormPanel.currency;
        if($currency == '1'){
            console.log(this.editFormPanel.adjustBalanceAud)
            console.log(this.editFormPanel.totalBalanceRefundOtherAud)
            //计算费用登记应付总金额
            var priceAud = totalPriceAud + parseFloat(this.editFormPanel.adjustBalanceAud || 0) - parseFloat(this.editFormPanel.totalBalanceRefundOtherAud || 0);
            this.editFormPanel.getCmpByName('main.totalPriceAud').setValue(priceAud.toFixed(2));
        }else if($currency == '2'){
            //计算费用登记应付总金额
            var priceRmb = totalPriceRmb + parseFloat(this.editFormPanel.adjustBalanceRmb || 0) - parseFloat(this.editFormPanel.totalBalanceRefundOtherRmb || 0);
            this.editFormPanel.getCmpByName('main.totalPriceRmb').setValue(priceRmb.toFixed(2));
        }else if($currency == '3'){
            //计算费用登记应付总金额
            var priceUsd = totalPriceUsd + parseFloat(this.editFormPanel.adjustBalanceUsd || 0) - parseFloat(this.editFormPanel.totalBalanceRefundOtherUsd || 0);
            this.editFormPanel.getCmpByName('main.totalPriceUsd').setValue(priceUsd.toFixed(2));
        }

    },

    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        if(Ext.getCmp(this.mainFormPanelId).type == 3){
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-price')});
            if(!!rows && rows.length>0){
                for(index in rows){
                    params['details['+index+'].type' ] = 1;
                    params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for(key in rows[index].data){
                        params['details['+index+'].'+key ] = rows[index].data[key];
                    }
                }
            }
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId)});
            if(!!rows && rows.length>0){
                for(index in rows){
                    params['otherDetails['+index+'].type'] = 2;
                    params['otherDetails['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for(key in rows[index].data){
                        params['otherDetails['+index+'].'+key ] = rows[index].data[key];
                    }
                }
            }
        }else{
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-project')});
            if(!!rows && rows.length>0){
                for(index in rows){
                    params['details['+index+'].type'] = 2;
                    params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for(key in rows[index].data){
                        params['details['+index+'].'+key ] = rows[index].data[key];
                    }
                }
            }
        }

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

        //查看
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

    changeType: function(type, eOpts, form){

        var obj = eOpts || this.ownerCt;
        var cmp = Ext.getCmp(form);
        cmp.adjustBalanceAud = 0;
        cmp.adjustBalanceRmb = 0;
        cmp.adjustBalanceUsd = 0;
        Ext.getCmp(form + '-projectContainer').show();
        Ext.getCmp(form + '-priceContainer').hide();
        Ext.getCmp(form + '-otherContainer').hide();
        Ext.getCmp('depositContainer').hide();

        cmp.getCmpByName('main.project').formGridPanel.getStore().removeAll();
        cmp.getCmpByName('main.priceContainer').formGridPanel.getStore().removeAll();
        cmp.getCmpByName('main.otherContainer').formGridPanel.getStore().removeAll();
        cmp.getCmpByName('main.products').formGridPanel.getStore().removeAll();

        cmp.getCmpByName('main.beneficiaryCnName').setValue('');
        cmp.getCmpByName('main.beneficiaryEnName').setValue('');
        cmp.getCmpByName('main.depositType').setValue('');
        cmp.getCmpByName('main.depositRate').setValue('');
        cmp.getCmpByName('main.beneficiaryBank').setValue('');
        cmp.getCmpByName('main.beneficiaryBankAddress').setValue('');
        cmp.getCmpByName('main.bankAccount').setValue('');
        cmp.getCmpByName('main.companyEnName').setValue('');
        cmp.getCmpByName('main.companyCnName').setValue('');
        cmp.getCmpByName('main.companyCnAddress').setValue('');
        cmp.getCmpByName('main.companyEnAddress').setValue('');
        cmp.getCmpByName('main.swiftCode').setValue('');
        cmp.getCmpByName('main.cnaps').setValue('');
        cmp.getCmpByName('main.currency').setValue('');
        cmp.getCmpByName('main.guaranteeLetterName').setValue('');
        cmp.getCmpByName('main.contractGuaranteeLetterName').setValue('');

        // console.log(cmp.getCmpByName('main.totalPriceUsd'));
        cmp.getCmpByName('main.totalPriceAud').setReadOnly(false);
        cmp.getCmpByName('main.totalPriceRmb').setReadOnly(false);
        cmp.getCmpByName('main.totalPriceUsd').setReadOnly(false);


        //供应商信息为空
        cmp.getCmpByName('main.vendorName').setValue('');
        cmp.getCmpByName('vendor.cnName').setValue('');
        cmp.getCmpByName('vendor.enName').setValue('');
        cmp.getCmpByName('vendor.address').setValue('');
        cmp.getCmpByName('vendor.director').setValue('');
        cmp.getCmpByName('vendor.abn').setValue('');
        cmp.getCmpByName('vendor.currency').setValue('');
        cmp.getCmpByName('vendor.website').setValue('');
        cmp.getCmpByName('vendor.source').setValue('');
        Ext.getCmp(form + '-vendor').hide();

        //服务商信息
        cmp.getCmpByName('main.serviceProviderName').setValue('');
        cmp.getCmpByName('serviceProvider.cnName').setValue('');
        cmp.getCmpByName('serviceProvider.enName').setValue('');
        cmp.getCmpByName('serviceProvider.address').setValue('');
        cmp.getCmpByName('serviceProvider.director').setValue('');
        cmp.getCmpByName('serviceProvider.abn').setValue('');
        cmp.getCmpByName('serviceProvider.currency').setValue('');
        cmp.getCmpByName('serviceProvider.website').setValue('');
        cmp.getCmpByName('serviceProvider.source').setValue('');
        Ext.getCmp(form + '-serviceProvider').hide();


        //订单清空
        cmp.getCmpByName('main.orderId').setValue('');
        cmp.getCmpByName('main.orderNumber').setValue('');


        if(type == 1 || type == 5){
            Ext.getCmp(form + '-vendorContainer').show();
            Ext.getCmp(form + '-serviceProviderContainer').hide();
            Ext.getCmp(form + '-orderContainer').hide();
        }else if(type == 2){
            Ext.getCmp(form + '-vendorContainer').hide();
            Ext.getCmp(form + '-serviceProviderContainer').show();
            Ext.getCmp(form + '-orderContainer').hide();
        }else if(type == 3){
            Ext.getCmp(form).type = type;
            Ext.getCmp(form + '-vendorContainer').hide();
            Ext.getCmp(form + '-serviceProviderContainer').hide();
            Ext.getCmp(form + '-orderContainer').show();
            Ext.getCmp(form + '-priceContainer').show();
            Ext.getCmp(form + '-projectContainer').hide();
            Ext.getCmp(form + '-otherContainer').show();
            Ext.getCmp('depositContainer').show();

        }else{

            Ext.getCmp(form + '-vendorContainer').show();
            Ext.getCmp(form + '-serviceProviderContainer').hide();
            Ext.getCmp(form + '-orderContainer').hide();
            Ext.getCmp(form + '-projectContainer').hide();
            Ext.getCmp('depositContainer').show();
        }
        cmp.getCmpByName('main.totalPriceAud').setValue(0);
    }
});