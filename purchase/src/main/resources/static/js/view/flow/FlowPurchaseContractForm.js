FlowPurchaseContractForm = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;
        //console.log(this.action != 'copy' || this.isAdd || this.record.flowStatus === null);
        var conf = {
            title : _lang.FlowPurchaseContract.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowPurchaseContract',
            winId : 'FlowPurchaseContractForm',
            frameId : 'FlowPurchaseContractView',
            mainGridPanelId : 'FlowPurchaseContractGridPanelID',
            mainFormPanelId : 'FlowPurchaseContractFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'FlowPurchaseContractSearchFormPanelID',
            mainTabPanelId: 'FlowPurchaseContractMainTbsPanelID',
            subProductGridPanelId : 'FlowPurchaseContractSubProductGridPanelID',
            subVendorGridPanelId:'FlowPurchaseContractSubVendorGridPanelID',
            subFormGridPanelId:'FlowPurchaseContractSubOtherGridPanelID',
            formGridPanelId : 'FlowPurchaseContractFormGridPanelID',

            urlList: __ctxPath + 'flow/purchase/purchasecontract/list',
            urlSave: __ctxPath + 'flow/purchase/purchasecontract/save',
            urlDelete: __ctxPath + 'flow/purchase/purchasecontract/delete',
            urlGet: __ctxPath + 'flow/purchase/purchasecontract/get',
            urlExport: __ctxPath + 'flow/purchase/purchasecontract/export',
            urlFlow: __ctxPath + 'flow/purchase/purchasecontract/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchaseContract&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
            flowCancel: (!this.isAdd) && (this.record.flowStatus==1 || this.record.flowStatus==2) && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
            expdoc:!this.isAdd,
            tomail:!this.isAdd
        };

        this.initUIComponents(conf);
        FlowPurchaseContractForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            cls: 'gb-blank',
            tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });
    },

    initUIComponents: function(conf) {
        var scope = this;
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                // {field: 'main.rateAudToRmb', xtype: 'hidden', value: curUserInfo.audToRmb},
                // {field: 'main.rateAudToUsd', xtype: 'hidden', value: curUserInfo.audToUsd},

                { xtype: 'container',cls:'row', hidden: this.isAdd, items: [
                    { xtype: 'section', title: _lang.FlowPurchasePlan.fIsNeededQc},
                    {field: 'main.isNeededQc',xtype: 'displayfield',fieldLabel: _lang.FlowPurchasePlan.fIsNeededQc,cls: 'col-2',
                        renderer: function (value) {
                            if (value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                            else if (value == '3') return $renderOutputColor('orange', _lang.TText.vExemption);
                            else return $renderOutputColor('gray', _lang.TText.vNo);
                        }
                    }
                ]},

                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf, {
                    hideDetails: this.action == 'add',
                    callback: function(cmp, row, result, bankInfo){
                        scope.editFormPanel.getCmpByName('main.guaranteeLetterFile').setValue('');
                        scope.editFormPanel.getCmpByName('main.guaranteeLetterFileId').setValue('');
                        scope.editFormPanel.getCmpByName('guaranteeLetterName').setValue('');
                        scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFile').setValue('');
                        scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFileId').setValue('');
                        scope.editFormPanel.getCmpByName('contractGuaranteeLetterName').setValue('');
                        scope.editFormPanel.getCmpByName('main.currency').setValue('');

                        if(!row || !row.id) return;
                        scope.editFormPanel.currency = bankInfo.bank.currency;

                        if(bankInfo.bank != undefined){
                            scope.editFormPanel.getCmpByName('main.guaranteeLetterFile').setValue(!!bankInfo.bank.guaranteeLetter ? bankInfo.bank.guaranteeLetter : '');
                            scope.editFormPanel.getCmpByName('main.guaranteeLetterFileId').setValue(!!bankInfo.bank.guaranteeLetterFile &&  !!bankInfo.bank.guaranteeLetterFile.document ? bankInfo.bank.guaranteeLetterFile.document.id : '');
                            scope.editFormPanel.getCmpByName('guaranteeLetterName').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.guaranteeLetterFile.document.name ? bankInfo.bank.guaranteeLetterFile.document.name : '');
                            if(!!bankInfo.bank.contractGuaranteeLetter){
                                scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFile').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetter ? bankInfo.bank.contractGuaranteeLetter : '');
                                scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFileId').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile.document ? bankInfo.bank.contractGuaranteeLetterFile.document.id : '');
                                scope.editFormPanel.getCmpByName('contractGuaranteeLetterName').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile.document.name ? bankInfo.bank.contractGuaranteeLetterFile.document.name : '');
                            }
                        }

                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.otherContainer').formGridPanel.getStore().removeAll();
                        scope.editFormPanel.getCmpByName('main.sellerEmail').setValue(row.sellerEmail);
                        scope.editFormPanel.getCmpByName('main.sellerPhone').setValue(row.sellerPhone);
                        scope.editFormPanel.getCmpByName('main.sellerFax').setValue(row.sellerFax);
                        scope.editFormPanel.getCmpByName('main.currency').setValue(bankInfo.bank.currency);

                        if(!!result[0].raw.paymentProvision){
                            var msg = result[0].raw.paymentProvision.replace(/<\/?[^>]*>/g, ''); //去除HTML标签
                            msg = msg.replace(/[|]*\n/, '') //去除行尾空格
                            msg = msg.replace(/&npsp;/ig, ''); //去掉空格占位符
                            msg = msg.replace(/<([a-zA-Z]+)\s*[^><]*>/g,"<$1>");//去掉标签内的属性
                            scope.editFormPanel.getCmpByName('main.otherTerms').setValue(msg);
                        }
                        scope.editFormPanel.getCmpByName('main.balancePaymentTerm').setValue(!!result[0].raw.balancePaymentTerm ? result[0].raw.balancePaymentTerm.toString() : '');
                        scope.editFormPanel.getCmpByName('main.balanceCreditTerm').setValue(result[0].raw.balanceCreditTerm);
                        scope.editFormPanel.getCmpByName('main.tradeTerm').setValue(result[0].raw.tradeTerm);
                        // console.log(result[0].raw);
                        scope.editFormPanel.getCmpByName('main.depositType').setValue(!!result[0].raw.depositType ? result[0].raw.depositType.toString() : '');
                        scope.editFormPanel.getCmpByName('main.depositRate').setValue(result[0].raw.depositRate);
                        Ext.getCmp(conf.mainFormPanelId).currency = result[0].raw.currency;

                        var productGrid = scope.editFormPanel.getCmpByName('main.products').formGridPanel;
                        productGrid.getStore().removeAll();
                        //供应商产品分类
                        Ext.Ajax.request({
                            url: __ctxPath + 'archives/vendor-product-category/listForDialog?vendorId=' + row.id,
                            scope: this, method: 'post',
                            success: function (response, options) {
                                var obj = Ext.decode(response.responseText);
                                if (obj.success == true) {
                                    var data = [];
                                    for(var i = 0; i < obj.data.length; i++){
                                        var category = obj.data[i].productCategory;
                                        data.push({id: obj.data[i].id, title: obj.data[i].alias});
                                    }
                                    scope.editFormPanel.getCmpByName('main.vendorProductCategoryAlias').setLocalSource(data, true);
                                }else{
                                    scope.editFormPanel.getCmpByName('main.vendorProductCategoryAlias').setLocalSource({});
                                }
                            }
                        });

                        //供应商联系人
                        Ext.Ajax.request({
                            url: __ctxPath + 'contacts/listForDialog?type=1&vendorId=' + row.id,
                            scope: this, method: 'post',
                            success: function (response, options) {
                                var obj = Ext.decode(response.responseText);
                                if (obj.success == true) {
                                    var data = [];
                                    for(var i = 0; i < obj.data.length; i++){
                                        var category = obj.data[i].productCategory;
                                        data.push({id: obj.data[i].id, title: obj.data[i].cnName});
                                    }
                                    scope.editFormPanel.getCmpByName('main.sellerContactCnName').setLocalSource(data, true);
                                }else{
                                    scope.editFormPanel.getCmpByName('main.sellerContactCnName').setLocalSource({});
                                }
                            }
                        });
                    }
                })},

                {xtype: 'container', cls: 'row', items: [
                    { field: 'main.currency',  xtype:'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2' , hidden:true, },
                    { field: 'main.vendorProductCategoryId', xtype: 'hidden', },
                    { field: 'main.vendorProductCategoryAlias', xtype: 'combodny', fieldLabel: _lang.FlowPurchaseContract.fVendorProductCategory, cls: 'col-2', allowBlank: false },
                    { field: 'main.sellerEmail', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerEmail, cls:'col-2'  },
                    { field: 'main.sellerPhone', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerPhone, cls:'col-2' ,allowBlank:false,  },
                    { field: 'main.sellerFax', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerFax, cls:'col-2'  },
                    { field: 'main.sellerContactId', xtype: 'hidden', },
                    { field: 'main.sellerContactCnName', xtype: 'combodny', fieldLabel: _lang.VendorDocument.fContactsName, cls:'col-2', allowBlank:false,  },
                ]},
                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetterFile', xtype: 'hidden'},
                    { field: 'main.guaranteeLetterFileId', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'guaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-13', },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.guaranteeLetterFileId',
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

                    { field: 'main.contractGuaranteeLetterFile', xtype: 'hidden'},
                    { field: 'main.contractGuaranteeLetterFileId', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'contractGuaranteeLetterName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-13', },
                        { field: 'preview',  xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23, hiddenName: 'main.guaranteeLetterFileId',
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


                //买方基础信息
                { xtype: 'section', title:_lang.FlowPurchaseContract.tabSeller},
                {field:'main.buyerInfoId', xtype:'hidden'},
                {xtype: 'container', cls: 'row', items: [
                    { field: 'main.buyerInfoName', xtype: 'BuyerDialog', fieldLabel: _lang.FlowPurchaseContract.fBuyer, cls: 'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.buyerInfoId', single: true, readOnly: this.readOnly, allowBlank:false,
                        subcallback: function(result){
                            var cmp = Ext.getCmp(conf.mainFormPanelId);
                            if(result == undefined || result.length<1){
                                cmp.getCmpByName('main.buyerInfoEmail').setValue('');
                                cmp.getCmpByName('main.buyerInfoPhone').setValue('');
                                cmp.getCmpByName('main.buyerInfoFax').setValue('');
                                cmp.getCmpByName('main.buyerInfoCnAddress').setValue('');
                                cmp.getCmpByName('main.buyerInfoEnAddress').setValue('');
                                cmp.getCmpByName('main.buyerInfoContactCnName').setValue('');
                                cmp.getCmpByName('main.buyerInfoContactEnName').setValue('');
                                cmp.getCmpByName('main.cnName').setValue('');
                                cmp.getCmpByName('main.enName').setValue('');
                                Ext.getCmp(conf.mainFormPanelId + '-buyer').hide();
                                if(options.callback) options.callback.call(this, cmp, null);
                                return;
                            }

                            var row = result[0].data;
                            cmp.getCmpByName('main.buyerInfoEmail').setValue(row.email);
                            cmp.getCmpByName('main.buyerInfoPhone').setValue(row.phone);
                            cmp.getCmpByName('main.buyerInfoFax').setValue(row.fax);
                            cmp.getCmpByName('main.buyerInfoCnAddress').setValue(row.cnAddress);
                            cmp.getCmpByName('main.buyerInfoEnAddress').setValue(row.enAddress);
                            cmp.getCmpByName('main.buyerInfoContactCnName').setValue(row.contactCnName);
                            cmp.getCmpByName('main.buyerInfoContactEnName').setValue(row.contactEnName);
                            cmp.getCmpByName('main.buyerInfoCnName').setValue(row.cnName);
                            cmp.getCmpByName('main.buyerInfoEnName').setValue(row.enName);

                            Ext.getCmp(conf.mainFormPanelId + '-buyer').show();
                        }
                    },
                ]},

                { xtype: 'container',id:conf.mainFormPanelId + '-buyer', cls:'row', items:  [
                    { field: 'main.buyerInfoCnName', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerName, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? false: true,  allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoEnName', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerName, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? true: false ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoFax', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fBuyerFax, cls:'col-2', allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoEmail', xtype: 'textfield', fieldLabel:_lang.TText.fEmail, cls:'col-2' ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoPhone', xtype: 'textfield', fieldLabel:_lang.AccountUser.fPhone, cls:'col-2' ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoCnAddress', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerAddress, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? false: true ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoEnAddress', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerAddress, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? true: false ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoContactCnName', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerContactName, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? false: true ,allowBlank:false, readOnly: this.readOnly, },
                    { field: 'main.buyerInfoContactEnName', xtype: 'textfield', fieldLabel:_lang.ProductDocument.fBuyerContactName, cls:'col-2', hidden: curUserInfo.lang =='zh_CN' ? true: false ,allowBlank:false, readOnly: this.readOnly, },
                ] },

                //合同信息
                { xtype: 'section', title:_lang.FlowPurchaseContract.tabContractInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.orderNumber', xtype: 'textfield',  fieldLabel: _lang.ProductDocument.fJobNumber, cls:'col-2', readOnly:true},
                    { field: 'main.orderTitle', xtype: 'textfield', fieldLabel: _lang.FlowPurchaseContract.fSubject, cls:'col-2', readOnly: this.readOnly, },
                    { field: 'main.factoryContractNumber', xtype: 'textfield', fieldLabel: _lang.FlowPurchaseContract.fFactoryContractNumber, cls:'col-2', readOnly: this.readOnly, },

                    {xtype: 'container',cls:'row', items:  [
                        { field: 'main.depositType', xtype: 'dictcombo', fieldLabel: _lang.FlowBankAccount.fDepositType, cls:'col-2',  allowBlank: false, code:'purchase', codeSub:'deposit_rate', readOnly:true,   },
                        { field: 'main.depositRate', xtype: 'textfield',  fieldLabel: _lang.FlowBankAccount.fDepositRate, cls:'col-2', readOnly:true,allowBlank:false},
                        { xtype: 'container',cls:'row', items:  [
                            {field: 'main.rateAudToRmb', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToRmb, cls: 'col-2', value: curUserInfo.audToRmb, readOnly: true,  },
                            {field: 'main.rateAudToUsd', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToUsd, cls: 'col-2', value: curUserInfo.audToUsd, readOnly: true,  },
                        ] },
                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId,
                            aud:{field:'main.totalValueAud', fieldLabel:_lang.FlowPurchaseContract.fTotalValueAud, value:0},
                            rmb:{field:'main.totalValueRmb', fieldLabel:_lang.FlowPurchaseContract.fTotalValueRmb, value:0},
                            usd:{field:'main.totalValueUsd', fieldLabel:_lang.FlowPurchaseContract.fTotalValueUsd, value:0}
                        })},
                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                            aud:{field:'main.depositAud', fieldLabel:_lang.ProductDocument.fDepositAud, value:0},
                            rmb:{field:'main.depositRmb', fieldLabel:_lang.ProductDocument.fDepositRmb, value:0},
                            usd:{field:'main.depositUsd', fieldLabel:_lang.ProductDocument.fDepositUsd, value:0}
                        })},
                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:true, allowBlank: true, updatePriceFlag : 1,
                            aud:{field:'main.writeOffAud', fieldLabel:_lang.FlowPurchaseContract.fWriteOffAud, value:0},
                            rmb:{field:'main.writeOffRmb', fieldLabel:_lang.FlowPurchaseContract.fWriteOffRmb, value:0},
                            usd:{field:'main.writeOffUsd', fieldLabel:_lang.FlowPurchaseContract.fWriteOffUsd, value:0}
                        })},

                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:this.readOnly, allowBlank: false, updatePriceFlag: this.readOnly ? 1 : null,
                            aud:{field:'main.balanceAud', fieldLabel:_lang.FlowPurchaseContract.fBalanceAud, value:0},
                            rmb:{field:'main.balanceRmb', fieldLabel:_lang.FlowPurchaseContract.fBalanceRmb, value:0},
                            usd:{field:'main.balanceUsd', fieldLabel:_lang.FlowPurchaseContract.fBalanceUsd, value:0}
                        })},
                    ]},
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                        aud:{field:'main.totalOtherAud', fieldLabel:_lang.FlowPurchaseContract.fOtherItemAud, value:0},
                        rmb:{field:'main.totalOtherRmb', fieldLabel:_lang.FlowPurchaseContract.fOtherItemRmb, value:0},
                        usd:{field:'main.totalOtherUsd', fieldLabel:_lang.FlowPurchaseContract.fOtherItemUsd, value:0}
                    })},

                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                        aud:{field:'main.totalOtherDepositAud', fieldLabel:_lang.FlowPurchaseContract.fOtherItemDepositAud, value:0},
                        rmb:{field:'main.totalOtherDepositRmb', fieldLabel:_lang.FlowPurchaseContract.fOtherItemDepositRmb, value:0},
                        usd:{field:'main.totalOtherDepositUsd', fieldLabel:_lang.FlowPurchaseContract.fOtherItemDepositUsd, value:0}
                    })},


                ] },

                { xtype: 'container',cls:'row', items:  [
                    {field: 'main.readyDate', xtype: 'datetimefield', fieldLabel: _lang.FlowPurchaseContract.fReadyDate, format: curUserInfo.dateFormat,allowBlank:false, cls:'col-2'},
                    {field: 'main.etd', xtype: 'datetimefield', fieldLabel: _lang.ProductDocument.fShippingDate, format: curUserInfo.dateFormat, allowBlank:false, cls:'col-2'},
                    {field: 'main.orderDate', xtype: 'datetimefield', fieldLabel: _lang.ProductDocument.fOrderDate, format: curUserInfo.dateFormat,allowBlank:false, cls:'col-2'},
                ] },



                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.originPortId',  xtype:'dictcombo', code:'service_provider', codeSub:'origin_port', fieldLabel:_lang.ProductDocument.fOriginPort, cls:'col-2',allowBlank: false },
                    { field: 'main.destinationPortId',  xtype:'dictcombo', code:'service_provider', codeSub:'destination_port', fieldLabel:_lang.FlowPurchasePlan.fDestinationPort, cls:'col-2', value: '1',allowBlank: false },
                    { field: 'main.shippingMethod',  xtype:'dictcombo', code:'service_provider', codeSub:'shipping_method', fieldLabel:_lang.ProductDocument.fShippingMethod, cls:'col-2',allowBlank: false, value: '1', },
                    { field: 'main.tradeTerm', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fTradeTerm, cls: 'col-2',allowBlank: false, value:'FOB'},
                    { field: 'main.containerType', xtype: 'dictcombo', code:'service_provider', codeSub:'container_type',
                        fieldLabel: _lang.FlowOrderShippingPlan.fContainerType, cls:'col-2', allowBlank:false,
                    },
                    { field: 'main.containerQty', xtype: 'numberfield', fieldLabel:_lang.FlowOrderShippingPlan.fContainerQty, cls:'col-2', allowBlank:false, value:1, allowDecimals: false, allowNegative  : false, minValue:1,},
                ] },

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.balancePaymentTerm', xtype: 'dictcombo', code:'purchase', codeSub:'balance_payment_term', fieldLabel: _lang.FlowPurchaseContract.fBalancePaymentTerm, cls:'col-2', value:'1', allowBlank:false,
                        // selectFun: function(records){
                        //     if(records[0].data.id == '3'){
                        //         Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.balanceCreditTerm').show();
                        //     }else{
                        //         Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.balanceCreditTerm').hide();
                        //         Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.balanceCreditTerm').setValue('0');
                        //     }
                        // }
                    },
                    { field: 'main.balanceCreditTerm', xtype: 'numberfield', fieldLabel: _lang.FlowPurchaseContract.fBalanceCreditTerm, cls:'col-2',value:0},
                ] },

                //产品报价明细
                { xtype: 'section', title:_lang.FlowPurchaseContract.tabProductsQuotationInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'totalPriceDisplayAud', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fTotalPriceAud, cls:'col-3', readOnly: true,},
                    { field: 'totalPriceDisplayRmb', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fTotalPriceRmb, cls:'col-3',  readOnly: true,},
                    { field: 'totalPriceDisplayUsd', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fTotalPriceUsd, cls:'col-3',  readOnly: true,},

                    { field: 'main.totalPriceAud', xtype: 'hidden', },
                    { field: 'main.totalPriceRmb', xtype: 'hidden', },
                    { field: 'main.totalPriceUsd', xtype: 'hidden', },

                ] },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'totalCbm', xtype: 'displayfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCbm, cls:'col-3',decimalPrecision: 2,},
                    { field: 'main.totalCbm', xtype: 'hidden', fieldLabel: _lang.FlowPurchasePlan.fTotalCbm, cls:'col-3',decimalPrecision: 2,},
                    { field: 'main.totalCases', xtype: 'displayfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCases, cls:'col-3'},
                    { field: 'main.totalCartons', xtype: 'displayfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCartons, cls:'col-3'},
                    { field: 'main.reta', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fReta, cls:'col-3', },
                    { field: 'main.retd', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fRetd, cls:'col-3', },
                ] },


                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowPurchaseContractFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly,
                    dataChangeCallback: this.onGridDataChange,
                    getBalanceRefunds: this.getBalanceRefunds
                },


                //其他费用
                { xtype: 'section', title: _lang.FlowFeeRegistration.tabOtherItem},
                { field: 'main.otherContainer', xtype: 'FlowPurchaseContractOtherFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                        hidden:true,readOnly: this.readOnly,
                        dataChangeCallback: this.onGridDataChange,
                        formGridPanelId:conf.subFormGridPanelId,
                        mainFormPanelId: conf.mainFormPanelId
                },

                //可冲销单据
                { xtype: 'section', title:_lang.FlowPurchasePlan.tabWriteOffDocument},
                { field: 'main.balanceRefunds', xtype: 'BalanceRefundFormMultiGrid', fieldLabel: _lang.FlowBalanceRefund.tabRefundList, scope:this, readOnly: this.readOnly,
                    mainFormPanelId: conf.mainFormPanelId, callback: this.onGridDataChange
                },

                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: false,
                },

                //付款条款
                { xtype: 'section', title:_lang.ProductDocument.fPaymentTerms},
                { field: 'main.paymentTerms', xtype: 'textarea', fieldLabel: '', width:'100%', height:300},

                { xtype: 'section', title:_lang.ProductDocument.fOtherTerms},
                { field: 'main.otherTerms', xtype: 'textarea', fieldLabel: '', width:'100%', height:300},

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });


        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            var me = this;
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);

                    var meForm = Ext.getCmp(conf.mainFormPanelId);
                    meForm.vendorProductCategoryId = json.data.vendorProductCategoryId;
                    meForm.sellerContactId = json.data.sellerContactId;

                    meForm.rateAudToRmb = json.data.rateAudToRmb;
                    meForm.rateAudToUsd = json.data.rateAudToUsd;

                    meForm.currency = json.data.currency;
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        var purchasePlanIds = '';
                        for(var index in json.data.details){
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            Ext.applyIf(product, json.data.details[index].product.prop);
                            Ext.apply(product, json.data.details[index].product);
                            product.id = json.data.details[index].purchasePlanDetailId;

                            if(me.action == 'copy') {
                                product.availableQty =  json.data.details[index].planAvailableQty;
                                product.orderQty = json.data.details[index].planAvailableQty;
                            }
                            if(json.data.details[index].availableQty <= 0) {
                                product.availableQty = 0;
                                product.orderQty = 0;
                            }

                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                            purchasePlanIds += ','+ json.data.details[index].purchasePlanId;
                        }

                        //访问冲销单据链接
                        var meConf = conf;
                        if(this.readOnly){
                            Ext.getCmp(meConf.mainFormPanelId).getCmpByName('main.balanceRefunds').setValue(null, json.data.purchaseBalanceRefundUnions);
                        }else{
                            if(!!purchasePlanIds){
                                var purchasePlanIdsProcess = purchasePlanIds.substring(1);
                                $HpStore({
                                    fields: [
                                        'id','businessId', 'purchasePlanId', 'purchasePlanBusinessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type',
                                        'chargebackStatus', 'currency',  'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                                        'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                                        'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                                        'assigneeId','assigneeCnName','assigneeEnName'],
                                    url: __ctxPath + 'finance/balanceRefund/list?type=2&vendorId=' + meForm.getCmpByName('main.vendorId').getValue() + '&purchasePlanIds=' + purchasePlanIdsProcess, loadMask: true, scope: this,
                                    callback: function (obj, records, eOpts) {
                                        meForm.getCmpByName('main.writeOffAud').setValue(0);
                                        meForm.getCmpByName('main.writeOffRmb').setValue(0);
                                        meForm.getCmpByName('main.writeOffUsd').setValue(0);

                                        meForm.getCmpByName('main.balanceRefunds').setValue(records, !!json.data && !!json.data.purchaseBalanceRefundUnions? json.data.purchaseBalanceRefundUnions: '' );
                                    }
                                });
                            }
                        }

                    }
                    //attachment init
                    if(!!json.data && !!json.data.attachments) Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);

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
                        Ext.Ajax.request({
                        url: __ctxPath + 'archives/vendor-product-category/listForDialog?vendorId=' + json.data.vendorId,
                        scope: this, method: 'post',
                        success: function (response, options) {

                            var obj = Ext.decode(response.responseText);
                            if (obj.success == true) {
                                var data = [];
                                for(var i = 0; i < obj.data.length; i++){
                                    var category = obj.data[i].productCategory;
                                    data.push({id: obj.data[i].id, title: obj.data[i].alias});
                                }
                                Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.vendorProductCategoryAlias').setLocalSource(data);
                                Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.vendorProductCategoryAlias').setValue(json.data.vendorProductCategoryAlias);
                                // Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.vendorProductCategoryId').setValue(json.data.vendorProductCategoryAlias);
                            }else{
                                Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.vendorProductCategoryAlias').setLocalSource({});
                            }
                        }
                    });

                    //供应商联系人
                    Ext.Ajax.request({
                        url: __ctxPath + 'contacts/list?vendorId=' + json.data.vendorId,
                        scope: this, method: 'post',
                        success: function (response, options) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success == true) {
                                var data = [];
                                for(var i = 0; i < obj.data.length; i++){
                                    var category = obj.data[i].productCategory;
                                    data.push({id: obj.data[i].id, title: obj.data[i].cnName});
                                }
                                scope.editFormPanel.getCmpByName('main.sellerContactCnName').setLocalSource(data);
                                scope.editFormPanel.getCmpByName('main.sellerContactCnName').setValue(json.data.sellerContactCnName);
                            }else{
                                scope.editFormPanel.getCmpByName('main.sellerContactCnName').setLocalSource({});
                                //scope.editFormPanel.getCmpByName('main.sellerContactEnName').setLocalSource({});
                            }
                        }
                    });

                    //供应商保函
                    Ext.Ajax.request({
                        url: conf.urlGet = __ctxPath + 'archives/vendor/get?id='+ json.data.vendorId,
                        scope: this, method: 'post',
                        success: function (response, options) {
                            var bankInfo = Ext.decode(response.responseText).data;
                            scope.editFormPanel.getCmpByName('main.guaranteeLetterFile').setValue('');
                            scope.editFormPanel.getCmpByName('main.guaranteeLetterFileId').setValue('');
                            scope.editFormPanel.getCmpByName('guaranteeLetterName').setValue('');
                            scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFile').setValue('');
                            scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFileId').setValue('');
                            scope.editFormPanel.getCmpByName('contractGuaranteeLetterName').setValue('');

                            if(!bankInfo) return;
                            if(bankInfo.bank != undefined){
                                scope.editFormPanel.getCmpByName('main.guaranteeLetterFile').setValue(!!bankInfo.bank.guaranteeLetter ? bankInfo.bank.guaranteeLetter : '');
                                scope.editFormPanel.getCmpByName('main.guaranteeLetterFileId').setValue(!!bankInfo.bank.guaranteeLetterFile &&  !!bankInfo.bank.guaranteeLetterFile.document ? bankInfo.bank.guaranteeLetterFile.document.id : '');
                                scope.editFormPanel.getCmpByName('guaranteeLetterName').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.guaranteeLetterFile.document.name ? bankInfo.bank.guaranteeLetterFile.document.name : '');
                                if(!!bankInfo.bank.contractGuaranteeLetter){
                                    scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFile').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetter ? bankInfo.bank.contractGuaranteeLetter : '');
                                    scope.editFormPanel.getCmpByName('main.contractGuaranteeLetterFileId').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile.document ? bankInfo.bank.contractGuaranteeLetterFile.document.id : '');
                                    scope.editFormPanel.getCmpByName('contractGuaranteeLetterName').setValue(!!bankInfo.bank.guaranteeLetterFile && !!bankInfo.bank.contractGuaranteeLetterFile.document.name ? bankInfo.bank.contractGuaranteeLetterFile.document.name : '');
                                }
                            }
                        }
                    });

                }
            });
        }else{
            this.editFormPanel.getCmpByName('main.paymentTerms').setValue( _dict.getValueRow('paymentTerms', 1).cnName);
        }
        //if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
        this.editFormPanel.setReadOnly(this.readOnly, [
            'flowRemark','flowNextHandlerAccount','main.depositAud','main.depositRmb', 'main.depositUsd',
            'main.depositRate','main.writeOffAud','main.writeOffRmb','main.writeOffUsd','main.depositType',
            'main.rateAudToRmb','main.rateAudToUsd','main.orderNumber','main.totalPriceAud','main.totalPriceRmb','main.totalPriceUsd'
        ]);

    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },

    onGridDataChange: function(store, conf){
        var totalPriceAud = 0, totalPriceRmb = 0, totalPriceUsd = 0;

        var totalOtherAud = 0, totalOtherRmb = 0,  totalOtherUsd = 0;

        var totalValueAud = 0, totalValueRmb = 0, totalValueUsd = 0;
        var totalValueDepositAud = 0, totalValueDepositRmb = 0, totalValueDepositUsd = 0;
        var totalOtherDepositAud = 0, totalOtherDepositRmb = 0, totalOtherDepositUsd = 0;
        var totalCbm = 0, totalCases=0, totalCartons=0;
        var audToRmb = this.editFormPanel.getCmpByName('main.rateAudToRmb').getValue();
        var audToUsd = this.editFormPanel.getCmpByName('main.rateAudToUsd').getValue();

        //设置定金
        var depositType = this.editFormPanel.getCmpByName('main.depositType').getValue()
        var depositRate = this.editFormPanel.getCmpByName('main.depositRate').getValue();
        var $currency = this.editFormPanel.currency;

        // var data = store.getRange();
        var data = this.editFormPanel.getCmpByName('main.products').formGridPanel.getStore().data.items;
        var otherData = this.editFormPanel.getCmpByName('main.otherContainer').formGridPanel.getStore().data.items;

        //总体积、货值总金额
        if(!!data && data.length >0) {
            for (var i = 0; i < data.length; i++) {
                var rowTotalCbm = 0;
                totalValueAud = totalValueAud + parseFloat(data[i].data.orderValueAud);
                totalValueRmb = totalValueRmb + parseFloat(data[i].data.orderValueRmb);
                totalValueUsd = totalValueUsd + parseFloat(data[i].data.orderValueUsd);
                // //计算不需要定金的货值总金额
                if(data[i].data.isNeedDeposit == 1){
                    totalValueDepositAud = totalValueDepositAud + parseFloat(data[i].data.orderValueAud);
                    totalValueDepositRmb = totalValueDepositRmb + parseFloat(data[i].data.orderValueRmb);
                    totalValueDepositUsd = totalValueDepositUsd + parseFloat(data[i].data.orderValueUsd);
                }
                var rowTotalCbm = 0;
                var orderQtyCarton = parseFloat(data[i].data.cartons) || 0;
                totalCases += parseInt(data[i].data.orderQty) || 0;
                totalCartons += orderQtyCarton;

                var cartonCbm = data[i].raw.masterCartonCbm || data[i].raw.innerCartonCbm || 0

                rowTotalCbm = parseFloat(cartonCbm) * orderQtyCarton;
                totalCbm = parseFloat(totalCbm) + parseFloat(rowTotalCbm);
                //console.log(data[i].raw.cbm);
            }
        }

        //其他费用的总金额
        if(!!otherData && otherData.length >0){
            for (var i = 0; i < otherData.length; i++) {
                if(otherData[i].data.settlementType == 1){
                    totalOtherDepositAud = totalOtherDepositAud + parseFloat(otherData[i].data.subtotalAud || 0.00);
                    totalOtherDepositRmb = totalOtherDepositRmb + parseFloat(otherData[i].data.subtotalRmb || 0.00);
                    totalOtherDepositUsd = totalOtherDepositUsd + parseFloat(otherData[i].data.subtotalUsd || 0.00);
                }
                if(otherData[i].data.settlementType == 3){
                    totalOtherDepositAud = totalOtherDepositAud + parseFloat(otherData[i].data.subtotalAud || 0.00) * depositRate;
                    totalOtherDepositRmb = totalOtherDepositRmb + parseFloat(otherData[i].data.subtotalRmb || 0.00) * depositRate;
                    totalOtherDepositUsd = totalOtherDepositUsd + parseFloat(otherData[i].data.subtotalUsd || 0.00) * depositRate;
                }
                totalOtherAud = totalOtherAud +  parseFloat(otherData[i].data.subtotalAud || 0.00);
                totalOtherRmb = totalOtherRmb + parseFloat(otherData[i].data.subtotalRmb || 0.00);
                totalOtherUsd = totalOtherUsd + parseFloat(otherData[i].data.subtotalUsd|| 0.00);
            }
        }

        //合同总金额
        totalPriceAud = totalValueAud + totalOtherAud;
        totalPriceRmb = totalValueRmb + totalOtherRmb;
        totalPriceUsd = totalValueUsd +  totalOtherUsd;

        this.editFormPanel.getCmpByName('main.totalCbm').setValue(totalCbm.toFixed(2));
        this.editFormPanel.getCmpByName('totalCbm').setValue(totalCbm.toFixed(2));
        this.editFormPanel.getCmpByName('main.totalCases').setValue(totalCases);
        this.editFormPanel.getCmpByName('main.totalCartons').setValue(totalCartons);


        if($currency == '1'){
            // this.editFormPanel.getCmpByName('main.totalPriceAud').setValue(totalPriceAud.toFixed(2));
            //计算总金额
            totalPriceAud = totalPriceAud.toFixed(2);
            totalPriceRmb = (totalPriceAud * audToRmb).toFixed(2);
            totalPriceUsd = (totalPriceAud * audToUsd).toFixed(2);

            var depositAud = depositType == 1 ? depositRate : depositType == 2 ? (totalValueDepositAud * depositRate).toFixed(2): 0;

            this.editFormPanel.getCmpByName('main.depositAud').setValue(depositAud);

            //设置货值总金额
            this.editFormPanel.getCmpByName('main.totalValueAud').setValue(totalValueAud.toFixed(2));
            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherAud').setValue(totalOtherAud.toFixed(2));

            //获取差额退款
            var writeOffAud = this.editFormPanel.getCmpByName('main.writeOffAud').getValue() || 0;
            //设置尾款
            this.editFormPanel.getCmpByName('main.balanceAud').setValue(totalValueAud>0 ? (totalValueAud - depositAud - writeOffAud).toFixed(2): 0);

            //设置其他订金
            this.editFormPanel.getCmpByName('main.totalOtherDepositAud').setValue(totalOtherDepositAud.toFixed(2));

        }else if($currency == '2'){
            //计算总金额
            totalPriceAud = (totalPriceRmb / audToRmb).toFixed(2);
            totalPriceRmb = totalPriceRmb.toFixed(2);
            totalPriceUsd = (totalPriceRmb / audToRmb * audToUsd).toFixed(2);
            // this.editFormPanel.getCmpByName('main.totalPriceRmb').setValue(totalPriceRmb.toFixed(2));

            var depositRmb = depositType == 1 ? depositRate : depositType == 2 ? (totalValueDepositRmb * depositRate).toFixed(2) : 0;

            this.editFormPanel.getCmpByName('main.depositRmb').setValue(depositRmb);
            //设置货值总金额
            this.editFormPanel.getCmpByName('main.totalValueRmb').setValue(totalValueRmb.toFixed(2));
            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherRmb').setValue(totalOtherRmb.toFixed(2));

            //获取差额退款
            var writeOffRmb = this.editFormPanel.getCmpByName('main.writeOffRmb').getValue() || 0;
            //设置尾款
            this.editFormPanel.getCmpByName('main.balanceRmb').setValue(totalValueRmb>0 ? (totalValueRmb - depositRmb - writeOffRmb).toFixed(2) : 0);

            //设置其他订金
            this.editFormPanel.getCmpByName('main.totalOtherDepositRmb').setValue(totalOtherDepositRmb.toFixed(2));
        }else if($currency == '3'){
            //计算总金额
            totalPriceAud = (totalPriceUsd / audToUsd).toFixed(2);
            totalPriceRmb = (totalPriceUsd * audToRmb / audToUsd).toFixed(2);
            totalPriceUsd = totalPriceUsd.toFixed(2);
            // this.editFormPanel.getCmpByName('main.totalPriceUsd').setValue(totalPriceUsd.toFixed(2))

            var depositUsd = depositType == 1 ? depositRate : depositType == 2 ? (totalValueDepositUsd * depositRate).toFixed(2): 0;

            this.editFormPanel.getCmpByName('main.depositUsd').setValue(depositUsd);
            //设置货值总金额
            this.editFormPanel.getCmpByName('main.totalValueUsd').setValue(totalValueUsd.toFixed(2));
            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherUsd').setValue(totalOtherUsd.toFixed(2));
            //获取差额退款
            var writeOffUsd = this.editFormPanel.getCmpByName('main.writeOffUsd').getValue() || 0;
            //设置尾款
            this.editFormPanel.getCmpByName('main.balanceUsd').setValue(totalValueUsd>0 ? (totalValueUsd - depositUsd - writeOffUsd).toFixed(2): 0);

            //设置其他订金
            this.editFormPanel.getCmpByName('main.totalOtherDepositUsd').setValue(totalOtherDepositUsd.toFixed(2));
        }

        //设置总价格
        this.editFormPanel.getCmpByName('main.totalPriceAud').setValue(totalPriceAud);
        this.editFormPanel.getCmpByName('main.totalPriceRmb').setValue(totalPriceRmb);
        this.editFormPanel.getCmpByName('main.totalPriceUsd').setValue(totalPriceUsd);
        this.editFormPanel.getCmpByName('totalPriceDisplayAud').setValue(totalPriceAud);
        this.editFormPanel.getCmpByName('totalPriceDisplayRmb').setValue(totalPriceRmb);
        this.editFormPanel.getCmpByName('totalPriceDisplayUsd').setValue(totalPriceUsd);
    },

    saveFun: function(action, isFlow){
        if(this.actionName != 'add'){
            Ext.getCmp(this.mainFormPanelId).getCmpByName('main.vendorProductCategoryId').setValue(Ext.getCmp(this.mainFormPanelId).vendorProductCategoryId);
            Ext.getCmp(this.mainFormPanelId).getCmpByName('main.sellerContactId').setValue(Ext.getCmp(this.mainFormPanelId).sellerContactId);
        }else{
            Ext.getCmp(this.mainFormPanelId).getCmpByName('main.vendorProductCategoryId').setValue(Ext.getCmp(this.mainFormPanelId).getCmpByName('main.vendorProductCategoryAlias').getValue());
            Ext.getCmp(this.mainFormPanelId).getCmpByName('main.sellerContactId').setValue(Ext.getCmp(this.mainFormPanelId).getCmpByName('main.sellerContactCnName').getValue());
        }

        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var flag = true;
        var errorProduct = false;
        var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
        if(!! rows && rows.length>0){
            errorProduct = true;
            for(index in rows){
                if(!rows[index].data['orderQty'] || rows[index].data['orderQty']  <= 0 || rows[index].data['orderQty'] == ''){
                    //数量为空时不能保存
                    flag = false;
                }
                params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                for(key in rows[index].data){
                    if(key != 'id') params['details['+index+'].'+key ] = rows[index].data[key];
                }
                params['details['+index+'].productId'] = rows[index].raw.product.id;
            }
        }

        var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId)});
        if(!!rows && rows.length>0){
            for(index in rows){
                for(key in rows[index].data){
                    params['otherDetails['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }
        
        //差额退款单关联
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.balanceRefunds').formGridPanel});
        if(!!rows && rows.length>0){
            for (var index in rows){
                if(rows[index].data.active == true) {
                    params['purchaseBalanceRefundUnions[' + index + '].balanceRefundBusinessId'] = rows[index].data.businessId;
                    params['purchaseBalanceRefundUnions[' + index + '].balanceRefundId'] = rows[index].data.id;
                    params['purchaseBalanceRefundUnions[' + index + '].purchasePlanId'] = rows[index].data.purchasePlanId;
                    params['purchaseBalanceRefundUnions[' + index + '].purchasePlanBusinessId'] = rows[index].data.purchasePlanBusinessId;
                    params['purchaseBalanceRefundUnions[' + index + '].purchaseContractBusinessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
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
        }else if(!flag) {
            //价格为空或采购数量为空时不能保存
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSamplePriceError);
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

    //获取冲销金额链接
    getBalanceRefunds : function(store, conf){
        var me = this;
        var purchasePlanIds = '';
        var gird = me.editFormPanel.getCmpByName('main.products').formGridPanel;
        if(gird.getStore().getCount() > 0){
            for(var i = 0; i < gird.getStore().getCount(); i++){
                purchasePlanIds =gird.getStore().getAt(i).data.purchasePlanId + ',' + purchasePlanIds;
            }
            var purchasePlanIdsProcess = purchasePlanIds.substring(0, purchasePlanIds.length-1);
        }

        //访问冲销单据链接
        if(!!purchasePlanIdsProcess){
            $HpStore({
                fields: ['id','businessId', 'purchasePlanId', 'purchasePlanBusinessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                    'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                    'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                    'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                    'assigneeId','assigneeCnName','assigneeEnName'],
                url: __ctxPath + 'finance/balanceRefund/list?type=2&vendorId=' + me.getCmpByName('main.vendorId').getValue() + '&purchasePlanIds=' + purchasePlanIdsProcess, loadMask: true, scope: this,
                callback: function (obj, records, eOpts) {
                    me.getCmpByName('main.writeOffAud').setValue(0);
                    me.getCmpByName('main.writeOffRmb').setValue(0);
                    me.getCmpByName('main.writeOffUsd').setValue(0);
                    me.getCmpByName('main.balanceRefunds').setValue(records);
                }
            });
        }
    },
});