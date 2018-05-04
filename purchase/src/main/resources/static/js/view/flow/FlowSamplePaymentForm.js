FlowSamplePaymentForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.updatePrice = !(this.record.flowStatus  == 2 || this.record.flowStatus == 3) ? true : false;
        var conf = {
            title : _lang.FlowSamplePayment.mTitle + $getTitleSuffix(this.action),

            moduleName: 'FlowSamplePayment',
            winId : 'FlowSamplePaymentForm',
            frameId : 'FlowSamplePaymentView',
            mainGridPanelId : 'FlowSamplePaymentGridPanelID',
            mainFormPanelId : 'FlowSamplePaymentFormPanelID',
            processFormPanelId: 'FlowSamplePaymentProcessFormPanelID',
            searchFormPanelId: 'FlowSamplePaymentSearchFormPanelID',
            mainTabPanelId: 'FlowSamplePaymentMainTbsPanelID',
            subProductGridPanelId : 'FlowSamplePaymentSubGridPanelID',
            formGridPanelId : 'FlowSamplePaymentFormGridPanelID',
            subFormGridPanelId:'FlowSamplePaymentFormOtherGridPanelID',

			urlList: __ctxPath + 'flow/finance/flowSamplePayment/list',
			urlSave: __ctxPath + 'flow/finance/flowSamplePayment/save',
			urlDelete: __ctxPath + 'flow/finance/flowSamplePayment/delete',
			urlGet: __ctxPath + 'flow/finance/flowSamplePayment/get',
			urlFlow: __ctxPath + 'flow/finance/flowSamplePayment/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowSamplePayment&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        FlowSamplePaymentForm.superclass.constructor.call(this, {
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
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },

                //样品申请单号
                { xtype: 'section', title: _lang.FlowSamplePayment.tabSampleID},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.sampleBusinessId', xtype: 'displayfield', fieldLabel:_lang.FlowSamplePayment.tabSampleID,  cls:'col-2'},
                ] },
                //供应商基础信息
                { xtype: 'container',
                    id: conf.mainFormPanelId + '-vendorContainer',
                    allowBlank: true,
                    cls: 'row',
                    items: $groupFormVendorFields(this, conf, {
                        hideDetails: this.action == 'add',
                        readOnly: true,
                        callback: function (eOpts, record) {
                            var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
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
                            cmpFormPlanId.getCmpByName('main_guaranteeLetter').setValue('');
                            cmpFormPlanId.getCmpByName('main_contractGuaranteeLetter').setValue('');
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
                                    cmpFormPlanId.getCmpByName('main.beneficiary').setValue(bankInfomation.vendorName || '');
                                    cmpFormPlanId.getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                                    cmpFormPlanId.getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                                    cmpFormPlanId.getCmpByName('main.currency').setValue(bankInfomation.currency || '');
                                    if (bankInfomation.guaranteeLetterFile) {
                                        cmpFormPlanId.getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                                    }
                                    //contract guarantee letter
                                    if (bankInfomation.contractGuaranteeLetterFile) {
                                        cmpFormPlanId.getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                                    }
                                }
                            })
                        },
                    }),
                },

				//收款账号信息
                { xtype: 'section', title:_lang.FlowBankAccount.tabAccountInformation},
                { xtype: 'container',cls:'row', items:  [

                    { field: 'main.companyCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2' , readOnly: true,allowBlank:true,  },
                    { field: 'main.companyEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2',  readOnly: true, allowBlank:true, },
                    { field: 'main.companyCnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.companyEnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true,  },
                    { field: 'main.beneficiaryBank', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.beneficiary', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , hidden:true, },
                    { field: 'main.beneficiaryBankAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.beneficiaryCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2',readOnly: true,allowBlank:true, },
                    { field: 'main.beneficiaryEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly: true,allowBlank:true, },
                    { field: 'main.swiftCode', xtype: 'textfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.cnaps', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.bankAccount', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'main.currency',  xtype:'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2' ,
                        readOnly: true, allowBlank: false, value:''}
                ] },

                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetter', xtype: 'hidden', allowBlank:true, },
                    { field: 'main_guaranteeLetter', xtype: 'FilesDialog', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.guaranteeLetter', single:true, readOnly: true, allowBlank:true,
                    },
                    { field: 'main.contractGuaranteeLetter', xtype: 'hidden', allowBlank:true, },
                    { field: 'main_contractGuaranteeLetter', xtype: 'FilesDialog', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.contractGuaranteeLetter', single:true, readOnly: true,allowBlank:true,
                    }
                ] },

                //付款明细
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
                { xtype: 'container', cls:'col-1', items:
                    [
                        { xtype: 'container', cls:'col-2', items:
                            $groupPriceFields(this.editFormPanel, {
                                decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: 1,
                                aud: { field: 'main.totalSampleFeeAud', xtype: 'displayfield', fieldLabel: _lang.FlowSamplePayment.fPayableTotalSampleFeeAud,  cls:'col-2'},
                                rmb: { field: 'main.totalSampleFeeRmb', xtype: 'displayfield',  fieldLabel: _lang.FlowSamplePayment.fPayableTotalSampleFeeRmb, cls:'col-2'},
                                usd: { field: 'main.totalSampleFeeUsd', xtype: 'displayfield', fieldLabel: _lang.FlowSamplePayment.fPayableTotalSampleFeeUsd,  cls:'col-2'},
                            }),
                        },
                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly: this.updatePrice ? false : true, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.updatePrice ? null : 1,
                            aud: { field: 'main.paymentTotalSampleFeeAud', xtype: 'textfield', fieldLabel: _lang.FlowSamplePayment.fPaymentTotalSampleFeeAud,  cls:'col-2'},
                            rmb: { field: 'main.paymentTotalSampleFeeRmb', xtype: 'textfield',  fieldLabel: _lang.FlowSamplePayment.fPaymentTotalSampleFeeRmb, cls:'col-2'},
                            usd: { field: 'main.paymentTotalSampleFeeUsd', xtype: 'textfield', fieldLabel: _lang.FlowSamplePayment.fPaymentTotalSampleFeeUsd,  cls:'col-2'},
                        })},


                    ]
                },

                //增加样品
                { xtype: 'section', title:_lang.ProductDocument.tabListTitle},
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowSamplePaymentFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: true,},

                //其他费用
                { xtype: 'section', title: _lang.FlowFeeRegistration.tabOtherItem},
                { field: 'main.otherContainer', xtype: 'FlowSampleOtherFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                    hidden:true,readOnly: this.readOnly,
                    dataChangeCallback: this.onGridDataChange,
                    formGridPanelId:conf.subFormGridPanelId,
                    mainFormPanelId: conf.mainFormPanelId
                },
                // 备注
                {xtype: 'section', title: _lang.FlowCustomClearance.tabRemark},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 300,  allowBlank:true,  readOnly: this.readOnly},
                ] },
                //   附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: false
                },



            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);

                    var json = Ext.JSON.decode(response.responseText);
                    if(!(json.data.flowStatus  == 2 || json.data.flowStatus == 3)){
                        cmpFormPlanId.getCmpByName('main.paymentRateAudToRmb').setValue(curUserInfo.audToRmb);
                        cmpFormPlanId.getCmpByName('main.paymentRateAudToUsd').setValue(curUserInfo.audToUsd);

                        if(json.data.currency == 1){
                            cmpFormPlanId.getCmpByName('main.paymentTotalSampleFeeAud').setValue(json.data.paymentTotalSampleFeeAud);
                        }else if(json.data.currency == 2){
                            cmpFormPlanId.getCmpByName('main.paymentTotalSampleFeeRmb').setValue(json.data.paymentTotalSampleFeeRmb);
                        }else{
                            cmpFormPlanId.getCmpByName('main.paymentTotalSampleFeeUsd').setValue(json.data.paymentTotalSampleFeeUsd);
                        }
                    }
					if(!!json.data && !!json.data.details && json.data.details.length>0){
						for(index in json.data.details){
							var product = {};
							Ext.applyIf(product, json.data.details[index]);
							Ext.apply(product, json.data.details[index].product);
							Ext.applyIf(product, json.data.details[index].product.prop);
                            product.orderValueAud = (json.data.details[index].sampleFeeAud *json.data.details[index].qty).toFixed(3);
                            product.orderValueRmb = (json.data.details[index].sampleFeeRmb *json.data.details[index].qty).toFixed(3);
                            product.orderValueUsd = (json.data.details[index].sampleFeeUsd *json.data.details[index].qty).toFixed(3);
                            product.sku =  json.data.details[index].product.sku;
							Ext.getCmp(conf.formGridPanelId).getStore().add(product);
						}
					}

                    if(!!json.data && !!json.data.vendor) {
                        var bankInfomation = json.data.vendor.bank || {};
                    }else{
                        var bankInfomation = {};
                    }
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.beneficiaryCnName').setValue(bankInfomation.beneficiaryCnName || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.beneficiaryEnName').setValue(bankInfomation.beneficiaryEnName || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.beneficiaryBank').setValue(bankInfomation.beneficiaryBank || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.beneficiaryBankAddress').setValue(bankInfomation.beneficiaryBankAddress || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.bankAccount').setValue(bankInfomation.bankAccount || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.companyCnName').setValue(bankInfomation.companyCnName || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.companyEnName').setValue(bankInfomation.companyEnName || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.beneficiary').setValue(bankInfomation.vendorName || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.companyCnAddress').setValue(bankInfomation.companyCnAddress || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.companyEnAddress').setValue(bankInfomation.companyEnAddress || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.swiftCode').setValue(bankInfomation.swiftCode || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.cnaps').setValue(bankInfomation.cnaps || '');
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.currency').setValue(!!bankInfomation.currency ? bankInfomation.currency.toString() : '');
                    if (bankInfomation.guaranteeLetterFile) {
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                    }
                    //contract guarantee letter
                    if (bankInfomation.contractGuaranteeLetterFile) {
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                    }

                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments? json.data.attachments : '');

                    if(!!json.data && !!json.data.otherDetails && json.data.otherDetails.length>0) {
                        for (var index in json.data.otherDetails) {
                            var otherDetails = {};
                            Ext.applyIf(otherDetails, json.data.otherDetails[index]);
                            otherDetails.subtotalAud =  (parseFloat(json.data.otherDetails[index].priceAud) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            otherDetails.subtotalRmb=   (parseFloat(json.data.otherDetails[index].priceRmb) *  parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            otherDetails.subtotalUsd =   (parseFloat(json.data.otherDetails[index].priceUsd) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            Ext.getCmp(conf.subFormGridPanelId).getStore().add(otherDetails);
                        }
                    }
				}
			});	
		}
        //if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount', 'main.paymentTotalSampleFeeAud', 'main.paymentTotalSampleFeeRmb', 'main.paymentTotalSampleFeeUsd']);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
    	if(rows.length>0){
			for(index in rows){
				params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				for(key in rows[index].data){
					if(key == 'id') 
						params['details['+index+'].productId'] = rows[index].data.id;
					else
						params['details['+index+'].'+key ] = rows[index].data[key];
				}
			}
		}
        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
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
    }
});