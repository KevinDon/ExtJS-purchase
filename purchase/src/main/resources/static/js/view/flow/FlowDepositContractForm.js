FlowDepositContractForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowDepositContract.mTitle + $getTitleSuffix(this.action),

            moduleName: 'FlowDepositContract',
            winId : 'FlowDepositContractForm',
            frameId : 'FlowDepositContractView',
            mainGridPanelId : 'FlowDepositContractGridPanelID',
            mainFormPanelId : 'FlowDepositContractFormPanelID',
            processFormPanelId: 'FlowDepositContractProcessFormPanelID',
            searchFormPanelId: 'FlowDepositContractSearchFormPanelID',
            mainTabPanelId: 'FlowDepositContractMainTbsPanelID',
            subProductGridPanelId : 'FlowDepositContractSubGridPanelID',
            formGridPanelId : 'FlowDepositContractFormGridPanelID',
            subFormGridPanelId: 'FlowwDepositContractSubOtherGridPanelID',
            urlList: __ctxPath + 'flow/finance/purchaseContractDeposit/list',
            urlSave: __ctxPath + 'flow/finance/purchaseContractDeposit/save',
            urlDelete: __ctxPath + 'flow/finance/purchaseContractDeposit/delete',
            urlGet: __ctxPath + 'flow/finance/purchaseContractDeposit/get',
            urlFlow: __ctxPath + 'flow/finance/purchaseContractDeposit/approve',

			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchaseContractDeposit&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
			actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy' && !this.isCanceled,
            flowAllow: (!this.isAdd) && this.isApprove && !this.isCanceled,
            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1 && !this.isCanceled,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1 && !this.isCanceled,
            flowCancel: (!this.isAdd) && this.record.flowStatus==1  && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowDepositContractForm.superclass.constructor.call(this, {
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
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd },
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                { field:'main.orderId', xtype:'hidden'},
                { xtype: 'container',id: conf.mainFormPanelId + '-orderContainer', allowBlank:true, cls:'row',  items:
                    [
                        {xtype: 'section', title:_lang.FlowFeeRegistration.tabOrder},
                        { field: 'main.orderNumber', xtype: 'OrderDialog', fieldLabel: _lang.FlowOrderQualityInspection.fOrderNumber, cls:'col-2',
                            hiddenName:'main.orderId', formId:conf.mainFormPanelId, single: true, formal: true, readOnly: this.readOnly, type: 6,
                            subcallback: function(rows){
                                var orderId = rows.data.id;
                                var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                cmpFormPlanId.getCmpByName('main.orderId').setValue(rows.data.id);
                                cmpFormPlanId.getCmpByName('main.orderTitle').setValue(rows.data.orderTitle);
                                var totalDepositOtherAud = 0, totalDepositOtherRmb = 0, totalDepositOtherUsd = 0;
                                var otherDetail = rows.data.otherDetails;
                               // console.log('-------------------');
                                Ext.getCmp(conf.subFormGridPanelId).getStore().removeAll();
                                if(!!otherDetail && !!otherDetail &&otherDetail.length>0) {
                                    for (var index in otherDetail) {
                                        var details = {};
                                        if(otherDetail[index].settlementType != 2){
                                            Ext.applyIf(details, otherDetail[index]);
                                            details.subtotalAud =  (parseFloat(otherDetail[index].priceAud) * parseFloat(otherDetail[index].qty)).toFixed(2);
                                            details.subtotalRmb =   (parseFloat(otherDetail[index].priceRmb) *  parseFloat(otherDetail[index].qty)).toFixed(2);
                                            details.subtotalUsd =   (parseFloat(otherDetail[index].priceUsd) * parseFloat(otherDetail[index].qty)).toFixed(2);

                                            //统计其他费用在合同定金下需要支付的总金额
                                            if(otherDetail[index].settlementType == 1){
                                                //统计在合同定金下支付
                                                totalDepositOtherAud += parseFloat(details.subtotalAud);
                                                totalDepositOtherRmb += parseFloat(details.subtotalRmb);
                                                totalDepositOtherUsd += parseFloat(details.subtotalUsd);
                                            }else if(otherDetail[index].settlementType == 3 && !((rows.data.vendorBank.depositType == 2 && rows.data.vendorBank.depositRate == 0) || rows.data.vendorBank.depositType == 1) ){
                                                //按定金率分摊
                                                totalDepositOtherAud = totalDepositOtherAud + (details.subtotalAud * parseFloat(rows.data.depositRate));
                                                totalDepositOtherRmb = totalDepositOtherRmb + (details.subtotalRmb * parseFloat(rows.data.depositRate));
                                                totalDepositOtherUsd = totalDepositOtherUsd + (details.subtotalUsd * parseFloat(rows.data.depositRate));
                                            }

                                            Ext.getCmp(conf.subFormGridPanelId).getStore().add(details);
                                        }
                                    }
                                };
                               

                                //计算应付款项
                                if(rows.data.vendorBank.depositType == 2 && rows.data.vendorBank.depositRate > 0){
                                    var totalAud = parseFloat(rows.data.depositAud) + parseFloat(totalDepositOtherAud);
                                    var totalRmb = parseFloat(rows.data.depositRmb) + parseFloat(totalDepositOtherRmb);
                                    var totalUsd = parseFloat(rows.data.depositUsd) +parseFloat(totalDepositOtherUsd);
                                }else if((rows.data.vendorBank.depositType == 2 && rows.data.vendorBank.depositRate == 0) || rows.data.vendorBank.depositType == 1){
                                    var totalAud = parseFloat(totalDepositOtherAud);
                                    var totalRmb = parseFloat(totalDepositOtherRmb);
                                    var totalUsd = parseFloat(totalDepositOtherUsd);
                                }

                                cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(rows.data.rateAudToRmb);
                                cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(rows.data.rateAudToUsd);
                                //设置货值定金
                                cmpFormPlanId.getCmpByName('main.totalValueDepositAud').setValue(rows.data.depositAud);
                                cmpFormPlanId.getCmpByName('main.totalValueDepositRmb').setValue(rows.data.depositRmb);
                                cmpFormPlanId.getCmpByName('main.totalValueDepositUsd').setValue(rows.data.depositUsd);
                                //设置应付款项
                                cmpFormPlanId.getCmpByName('main.payableAud').setValue(totalAud);
                                cmpFormPlanId.getCmpByName('main.payableRmb').setValue(totalRmb);
                                cmpFormPlanId.getCmpByName('main.payableUsd').setValue(totalUsd);

                                //更具汇率设置相应币种
                                if(rows.data.currency == 1){
                                    //设置实付款项
                                    cmpFormPlanId.getCmpByName('main.paymentAud').setValue(0);
                                    //设置其他费用
                                    cmpFormPlanId.getCmpByName('main.totalOtherAud').setValue(totalDepositOtherAud);
                               
                                }else if(rows.data.currency == 2){
                                    //设置实付款项
                                    cmpFormPlanId.getCmpByName('main.paymentRmb').setValue(0);
                                    //设置其他费用
                                    cmpFormPlanId.getCmpByName('main.totalOtherRmb').setValue(totalDepositOtherRmb);
                                }else{
                                    //设置实付款项
                                    cmpFormPlanId.getCmpByName('main.paymentUsd').setValue(0);
                                    //设置其他费用
                                    cmpFormPlanId.getCmpByName('main.totalOtherUsd').setValue(totalDepositOtherUsd);
                                }


                                var tpData = $HpStore({
                                    fields: ['id', 'contractDetails', 'vendor','depositAud','depositRmb','depositUsd'],
                                    url: __ctxPath + 'flow/finance/feeRegister/getOrderInfo?orderId=' + orderId,
                                    callback: function (conf, records, eOpts) {
                                        cmpFormPlanId.getCmpByName('main.vendorId').setValue(records[0].data.vendor.id);
                                        cmpFormPlanId.getCmpByName('main.vendorName').setValue(records[0].data.vendor.cnName);
                                        cmpFormPlanId.getCmpByName('vendor.cnName').setValue(records[0].data.vendor.cnName);
                                        cmpFormPlanId.getCmpByName('vendor.enName').setValue(records[0].data.vendor.enName);
                                        cmpFormPlanId.getCmpByName('vendor.address').setValue(records[0].data.vendor.address);
                                        cmpFormPlanId.getCmpByName('vendor.director').setValue(records[0].data.vendor.director);
                                        cmpFormPlanId.getCmpByName('vendor.abn').setValue(records[0].data.vendor.abn);
                                        cmpFormPlanId.getCmpByName('vendor.currency').setValue(records[0].data.vendor.currency);
                                        cmpFormPlanId.getCmpByName('vendor.website').setValue(records[0].data.vendor.website);
                                        cmpFormPlanId.getCmpByName('vendor.source').setValue(records[0].data.vendor.source);

                                        var bankInfomation = records[0].data.vendor.bank || {};
                                        cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                                        cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                                        cmpFormPlanId.getCmpByName('main.depositType').setValue(bankInfomation.depositType.toString() || 0);
                                        cmpFormPlanId.getCmpByName('main.depositRate').setValue(bankInfomation.depositRate || 0);
                                        cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                                        cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                                        cmpFormPlanId.getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                                        cmpFormPlanId.getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                                        cmpFormPlanId.getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                                        cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                                        cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                                        cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                                        cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                                        cmpFormPlanId.getCmpByName('main.currency').setValue(bankInfomation.currency.toString() || 0);
                                        if(bankInfomation.guaranteeLetterFile) {
                                            cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(!!bankInfomation.guaranteeLetter ? bankInfomation.guaranteeLetterFile.document.id : '');
                                            cmpFormPlanId.getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                        }

                                        //contract guarantee letter
                                        if(bankInfomation.contractGuaranteeLetterFile) {
                                            cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue(!!bankInfomation.contractGuaranteeLetter ? bankInfomation.contractGuaranteeLetterFile.document.id : '');
                                            cmpFormPlanId.getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                        }
                                        if(records[0].data.vendor.currency == 1){
                                            //更新实收价格
                                            cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(false);
                                            cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentRmb').clearListeners();
                                            cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentUsd').clearListeners();
                                        }else if(records[0].data.vendor.currency == 2){
                                            //更新实收价格
                                            cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentAud').clearListeners();
                                            cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(false);
                                            cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentUsd').clearListeners();
                                        }else{
                                            //更新实收价格
                                            cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentAud').clearListeners();
                                            cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(true);
                                            cmpFormPlanId.getCmpByName('main.paymentRmb').clearListeners();
                                            cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(false);
                                        }


                                        //
                                        // cmpFormPlanId.getCmpByName('main.totalDepositAud').setValue(records[0].data.totalValueAud);
                                        // cmpFormPlanId.getCmpByName('main.totalDepositRmb').setValue(records[0].data.totalValueRmb);
                                        // cmpFormPlanId.getCmpByName('main.totalDepositUsd').setValue(records[0].data.totalValueUsd);
                                        //
                                        // cmpFormPlanId.getCmpByName('main.paymentTotalDepositAud').setValue(records[0].data.depositAud);
                                        // cmpFormPlanId.getCmpByName('main.paymentTotalDepositRmb').setValue(records[0].data.depositRmb);
                                        // cmpFormPlanId.getCmpByName('main.paymentTotalDepositUsd').setValue(records[0].data.depositUsd);

                                       var contractDetails = records[0].data.contractDetails || {};
                                        if(contractDetails.length > 0){
                                            cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().removeAll();
                                            for(index in contractDetails){
                                                var totalPriceAud = 0;
                                                var totalPriceRmb = 0;
                                                var totalPriceUsd = 0;
                                                totalPriceAud += parseFloat(contractDetails[index].orderValueAud) || 0;
                                                totalPriceRmb += parseFloat(contractDetails[index].orderValueRmb) || 0;
                                                totalPriceUsd += parseFloat(contractDetails[index].orderValueUsd) || 0;
                                                var product = {};
                                                Ext.applyIf(product, contractDetails[index]);
                                                Ext.apply(product, contractDetails[index].product);
                                                Ext.apply(product, contractDetails[index].product.prop);
                                                product.sku =  contractDetails[index].product.sku;
                                                product.id =  contractDetails[index].id;
                                                cmpFormPlanId.getCmpByName('main.products').formGridPanel.getStore().add(product);
                                            }
                                        }
                                    }

                                })


                            }
                        },
                        { field: "main.orderTitle", xtype: 'textfield', fieldLabel: _lang.TText.fOrderTitle, cls:'col-2', },
                    ]
                },

                //供应商信息
                {xtype: 'section', title:  _lang.VendorDocument.tabVendorInformation},
                {field:'main.vendorId', xtype:'hidden'},
                { xtype: 'container',cls:'row', items:  [
                    { field: "main.vendorName", xtype: 'textfield', fieldLabel: _lang.VendorDocument.fVendor, cls:'col-2', readOnly: true, },
                ] },
                { xtype: 'container',cls:'row',	 id: conf.mainFormPanelId + '-vendor',  items:  [
                    { field: 'vendor.cnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2'},
                    { field: 'vendor.address', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAddress, cls:'col-2'},
                    { field: 'vendor.enName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2'},
                    { field: 'vendor.director', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fDirector, cls:'col-2', },
                    { field: 'vendor.abn', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAbn, cls:'col-2', },
                    { field: 'vendor.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
                    { field: 'vendor.website', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fWebsite, cls:'col-2', },
                    { field: 'vendor.source', xtype: 'dictfield', code:'vendor', codeSub:'source', fieldLabel: _lang.VendorDocument.fSource, cls:'col-2'}
                ] },

                //收款账号信息
                { xtype: 'section', title:_lang.FlowBankAccount.tabAccountInformation},
                { xtype: 'container',cls:'row',	 items:  [
                    { field: 'main.depositType', xtype: 'dictfield', fieldLabel: _lang.FlowBankAccount.fDepositType, cls:'col-2',  allowBlank: false,
                        code:'purchase', codeSub:'deposit_rate',  readOnly:true,
                     },
                    { field: 'main.depositRate', xtype: 'displayfield', fieldLabel: _lang.FlowBankAccount.fDepositRate, cls:'col-2',  readOnly:true,},
                ] },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.companyCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2' , readOnly: true,allowBlank:true,  },
                    { field: 'main.companyEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2',  readOnly: true, allowBlank:true, },
                    { field: 'main.companyCnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.companyEnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true,  },
                    { field: 'main.beneficiaryBank', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.beneficiaryBankAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.beneficiaryCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2',readOnly: true,allowBlank:true, },
                    { field: 'main.beneficiaryEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly: true,allowBlank:true, },

                    { field: 'main.swiftCode', xtype: 'textfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.cnaps', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.bankAccount', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.currency',  xtype:'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2' ,
                        readOnly: true, allowBlank: false,
                        afterrender: function(combo, records, eOpts){
                                console.log(1);
                        }
                    }
                ] },

                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetter', xtype: 'hidden', allowBlank:true, },
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'main_guaranteeLetter', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-13',
                            formId: conf.mainFormPanelId, hiddenName: 'main.guaranteeLetter', single:true, readOnly: true,allowBlank:true,
                        },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.guaranteeLetter',
                            scope: this, cls:'col', iconCls: 'fa fa-fw fa-eye', listeners: {
                            click: function(){
                                console.log(1);
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
                    { field: 'main.contractGuaranteeLetter', xtype: 'hidden', allowBlank:true, },
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'main_contractGuaranteeLetter', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-13',
                            formId: conf.mainFormPanelId, hiddenName: 'main.contractGuaranteeLetter', single:true, readOnly: true,allowBlank:true,
                        },
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
                            },
                        }
                    ]}
                ] },

                //合同定金
                { xtype: 'section', title:_lang.FlowDepositContract.tabDepositInfomation},
                //汇率
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
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2',  id: conf.mainFormPanelId + '-totalPriceContainer', items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly: true,  allowBlank: false, updatePriceFlag: 1, formId: conf.mainFormPanelId,
                            aud:{field:'main.totalValueDepositAud', fieldLabel:  _lang.FlowDepositContract.fTotalDepositAUD,value:0},
                            rmb:{field:'main.totalValueDepositRmb', fieldLabel:_lang.FlowDepositContract.fTotalDepositRMB,value:0},
                            usd:{field:'main.totalValueDepositUsd', fieldLabel:  _lang.FlowDepositContract.fTotalDepositUSD,value:0},
                        }),
                    },
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly: true, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                        aud:{field:'main.totalOtherAud', fieldLabel:_lang.FlowPurchaseContract.fOtherItemAud, value:0},
                        rmb:{field:'main.totalOtherRmb', fieldLabel:_lang.FlowPurchaseContract.fOtherItemRmb, value:0},
                        usd:{field:'main.totalOtherUsd', fieldLabel:_lang.FlowPurchaseContract.fOtherItemUsd, value:0}
                    })},
                ]},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-2', items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:true,  allowBlank: true, updatePriceFlag: 1,
                            aud:{field:'main.payableAud', fieldLabel:  _lang.FlowDepositContract.fTotalAUD,value:0 },
                            rmb:{field:'main.payableRmb', fieldLabel:_lang.FlowDepositContract.fTotalRMB, value:0 },
                            usd:{field:'main.payableUsd', fieldLabel: _lang.FlowDepositContract.fTotalUSD, value:0},
                        }),
                    },
                    { xtype: 'container',cls:'col-2', items:
                        $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:false,  allowBlank: true, 
                            aud:{field:'main.paymentAud', fieldLabel:  _lang.FlowDepositContract.fReceivedTotalAUD, value:0},
                            rmb:{field:'main.paymentRmb', fieldLabel:_lang.FlowDepositContract.fReceivedTotalRMB, value:0 },
                            usd:{field:'main.paymentUsd', fieldLabel: _lang.FlowDepositContract.fReceivedTotalUSD, value:0 },
                        }),
                     },
                ] },

                // { xtype: 'container',cls:'row', items:  [
                //     { field: 'main.latestPaymentTime', xtype: 'datetimefield', fieldLabel: _lang.FlowDepositContract.fLatestPaymentTime, cls:'col-2', format: curUserInfo.dateFormat, },
                // ] },

                //产品明细
                { xtype: 'section', title:_lang.ProductDocument.tabListTitle},
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowDepositContractFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.isApprove},

                //备注
                // { xtype: 'section', title:_lang.TText.fRemark},
                // { xtype: 'container',cls:'row', items:  [
                //     { field: 'main.remark', xtype: 'textarea', width: '100%', height: 200,  allowBlank:true, },
                // ] },

                //其他费用
                { xtype: 'section', title: _lang.FlowFeeRegistration.tabOtherItem},
                { field: 'main.otherContainer', xtype: 'FlowPurchaseContractOtherFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                    hidden:true,readOnly: true,
                    //dataChangeCallback: this.onGridDataChange,
                    formGridPanelId:conf.subFormGridPanelId,
                    mainFormPanelId: conf.mainFormPanelId
                },
                //   附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                	mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                	scope:this, readOnly: false
                },

                //创建人信息
                { xtype: 'section', title:_lang.ProductDocument.fBusinessIdInfo},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.id', xtype: 'displayfield',  fieldLabel: _lang.FlowDepositContract.fId, cls:'col-2', readOnly:true },
                ] },

            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });

        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);

                    var bankInfomation = json.data.vendor.bank || {};
                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                    if(!(json.data.flowStatus  == 2 || json.data.flowStatus == 3)){
                        cmpFormPlanId.getCmpByName('main.paymentRateAudToRmb').setValue(curUserInfo.audToRmb);
                        cmpFormPlanId.getCmpByName('main.paymentRateAudToUsd').setValue(curUserInfo.audToUsd);
                    }
                    cmpFormPlanId.getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                    cmpFormPlanId.getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                    cmpFormPlanId.getCmpByName('main.depositType').setValue(bankInfomation.depositType || '');
                    cmpFormPlanId.getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                    cmpFormPlanId.getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                    cmpFormPlanId.getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                    cmpFormPlanId.getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                    cmpFormPlanId.getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                    cmpFormPlanId.getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                    cmpFormPlanId.getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                    cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                    cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                    if(bankInfomation.guaranteeLetterFile) {
                        cmpFormPlanId.getCmpByName('main.guaranteeLetter').setValue(!!bankInfomation.guaranteeLetter ? bankInfomation.guaranteeLetterFile.document.id : '');
                        cmpFormPlanId.getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                    }
                    //contract guarantee letter
                    if(bankInfomation.contractGuaranteeLetterFile) {
                        cmpFormPlanId.getCmpByName('main.contractGuaranteeLetter').setValue(!!bankInfomation.contractGuaranteeLetter ? bankInfomation.contractGuaranteeLetterFile.document.id : '');
                        cmpFormPlanId.getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                    }

                    if(json.data.vendor.currency == 1){
                        //更新实收价格
                        cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(false);
                        cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentRmb').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentUsd').clearListeners();

                    }else if(json.data.vendor.currency == 2){
                        //更新实收价格
                        cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentAud').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(false);
                        cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentUsd').clearListeners();
                    }else{
                        //更新实收价格
                        cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentAud').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentRmb').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(false);
                    }
                    
                    if(json.data.flowStatus  == 2 || json.data.flowStatus == 3){
                        //更新实收价格
                        cmpFormPlanId.getCmpByName('main.paymentAud').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentAud').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentRmb').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentRmb').clearListeners();
                        cmpFormPlanId.getCmpByName('main.paymentUsd').setReadOnly(true);
                        cmpFormPlanId.getCmpByName('main.paymentUsd').clearListeners();
                        //设置实收款项
                        cmpFormPlanId.getCmpByName('main.paymentAud').setValue(json.data.paymentAud);
                        cmpFormPlanId.getCmpByName('main.paymentRmb').setValue(json.data.paymentRmb);
                        cmpFormPlanId.getCmpByName('main.paymentUsd').setValue(json.data.paymentUsd);
                    }
                    //product
                    if(!!json.data && !!json.data.purchaseContractDetail && json.data.purchaseContractDetail.length>0){
                        for(index in json.data.purchaseContractDetail){
                            var product = {};
                            Ext.applyIf(product, json.data.purchaseContractDetail[index]);
                            Ext.apply(product, json.data.purchaseContractDetail[index].product);

                            product.id = json.data.purchaseContractDetail[index].id;
                            product.productDetail = json.data.purchaseContractDetail[index].product.prop.productDetail;
                            product.productLink = json.data.purchaseContractDetail[index].product.prop.productLink;
                            product.productParameter = json.data.purchaseContractDetail[index].product.prop.productParameter;
                            product.keywords = json.data.purchaseContractDetail[index].product.prop.keywords;

                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                        }
                    }

                    if(!!json.data && !!json.data.purchaseContractOtherDetails && json.data.purchaseContractOtherDetails.length>0){
                        for(index in json.data.purchaseContractOtherDetails){
                            var details = {};

                            if(json.data.purchaseContractOtherDetails[index].settlementType != 2){
                                Ext.applyIf(details, json.data.purchaseContractOtherDetails[index]);
                                details.subtotalAud =  (parseFloat(json.data.purchaseContractOtherDetails[index].priceAud) * parseFloat(json.data.purchaseContractOtherDetails[index].qty)).toFixed(2);
                                details.subtotalRmb =   (parseFloat(json.data.purchaseContractOtherDetails[index].priceRmb) *  parseFloat(json.data.purchaseContractOtherDetails[index].qty)).toFixed(2);
                                details.subtotalUsd =   (parseFloat(json.data.purchaseContractOtherDetails[index].priceUsd) * parseFloat(json.data.purchaseContractOtherDetails[index].qty)).toFixed(2);
                                Ext.getCmp(conf.subFormGridPanelId).getStore().add(details);
                            }
                        }
                    }
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments? json.data.attachments : '');
				}
			});
		}

		this.editFormPanel.setReadOnly(this.readOnly, [
		    'flowRemark','flowNextHandlerAccount', 'main.vendorName', 'main.beneficiaryBank','main.beneficiaryBankAddress','main.bankAccount','main.totalPriceRmb',
            'main.totalPriceAud','main.totalPriceUsd', 'main.companyCnName', 'main.companyEnName','main.companyCnAddress',
            'main.companyEnAddress','main.swiftCode','main.cnaps','main.currency', 'main_contractGuaranteeLetter', 'main_guaranteeLetter',
            'main.rateAudToUsd', 'main.rateAudToRmb','main.totalDepositAud','main.totalDepositRmb','main.totalDepositUsd',
            'main.paymentTotalDepositAud','main.paymentTotalDepositRmb','main.paymentTotalDepositUsd','main.depositRate','main.NewestRateAudToRmb',
            'main.NewestRateAudToUsd','main.paymentTotalDepositAud','main.paymentRateAudToRmb','main.paymentRateAudToUsd',
            'main.depositType','main.totalOtherAud','main.totalOtherRmb','main.totalOtherUsd','main.totalAud','main.totalRmb','main.totalUsd',
            'main.totalValueDepositAud','main.totalValueDepositRmb','main.totalValueDepositUsd','main.payableAud', 'main.payableRmb', 'main.payableUsd'
        ]);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	var me = this;

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == '') {
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        /*}else if(params.act == 'cancel'){
            Ext.Msg.confirm(_lang.TText.titleConfirm,  _lang.TText.sureCancellation, function(btn) {
                if (btn == 'yes') {
                    $postForm({
                        formPanel: Ext.getCmp(me.mainFormPanelId),
                        grid: Ext.getCmp(me.mainGridPanelId),
                        scope: this,
                        url: isFlow ? me.urlFlow : me.urlSave,
                        params: params,
                        callback: function (fp, action, status, grid) {
                            Ext.getCmp(me.winId).close();
                            if(!!grid) {
                                grid.getSelectionModel().clearSelections();
                                grid.getView().refresh();
                            }
                        }
                    });
                }
            });*/
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
    }
});