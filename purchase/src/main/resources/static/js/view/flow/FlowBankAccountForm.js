FlowBankAccountForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;
        var conf = {
            title : _lang.FlowBankAccount.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowBankAccount',
            winId : 'FlowBankAccountForm',
            frameId : 'FlowBankAccountView',
            mainGridPanelId : 'FlowBankAccountViewGridPanelID',
            mainFormPanelId : 'FlowBankAccountViewFormPanelID',
            processFormPanelId: 'FlowBankAccountProcessFormPanelID',
            searchFormPanelId: 'FlowBankAccountViewSearchFormPanelID',
            mainTabPanelId: 'FlowBankAccountViewMainTbsPanelID',
            subGridPanelId : 'FlowBankAccountViewSubGridPanelID',
            formGridPanelId: 'FlowBankAccountFormGridPanelID',
            urlList: __ctxPath + 'flow/finance/flowBankAccount/list',
            urlSave: __ctxPath + 'flow/finance/flowBankAccount/save',
            urlDelete: __ctxPath + 'flow/finance/flowBankAccount/delete',
            urlGet: __ctxPath + 'flow/finance/flowBankAccount/get',
            urlFlow: __ctxPath + 'flow/finance/flowBankAccount/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowBankAccount&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
            actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy',
            flowAllow: (!this.isAdd) && this.isApprove && !this.isCanceled,
            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1  && !this.isCanceled,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1  && !this.isCanceled,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1 && !this.isCanceled,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1  && !this.isCanceled,
            flowCancel: (!this.isAdd) &&  this.isApprove && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowBankAccountForm.superclass.constructor.call(this, {
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
                { xtype: 'container',cls:'row', scope: this, items: [
                        { field: 'main.type', xtype: 'combo', fieldLabel: _lang.FlowBankAccount.fType, cls:'col-2',  value:'1',
                            store: [['1', _lang.Email.tabVendor], ['2', _lang.Email.tabServiceProvider]], scope: this,
                            listeners: {
                                change: function (scope, value){
                                    this.scope.changeType.call(this, value, this.scope.editFormPanel.id);
                                }
                            }
                        },
                    ],
                },
                // {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },

                //供应商基础信息
                { xtype: 'container',cls:'row',items:  [
                    { xtype: 'container', id: conf.mainFormPanelId + '-vendorContainer', cls:'row', items: $groupFormVendorFields(this, conf, {hideDetails: this.action == 'add'})},
                    { xtype: 'container',id: conf.mainFormPanelId + '-serviceProviderContainer', cls:'row', items: $groupFormServiceProviderFields(this, conf, {
                        hideDetails: this.action == 'add' , readOnly:this.readOnly
                    }), hidden:true,},
                ] },

				//账号信息
                { xtype: 'section', title:_lang.FlowBankAccount.tabAccountInformation},

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.companyCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },
                    { field: 'main.companyEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },

                    { field: 'main.companyCnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },
                    { field: 'main.companyEnAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2', readOnly:this.readOnly ,allowBlank: false, },

                    { xtype: 'container',cls:'row', id:'depositContainer',	 items:  [
                        { field: 'main.depositType', xtype: 'dictcombo', fieldLabel: _lang.FlowBankAccount.fDepositType, cls:'col-2',  allowBlank: false, code:'purchase', codeSub:'deposit_rate', value:'2',},
                        { field: 'main.depositRate', xtype: 'numberfield', fieldLabel: _lang.FlowBankAccount.fDepositRate, cls:'col-2',  allowBlank: false, value:0, decimalPrecision:2,},
                    ] },

                    { field: 'main.beneficiaryBank', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },
                    { field: 'main.beneficiaryBankAddress', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },
                    { field: 'main.beneficiaryCnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2', readOnly:this.readOnly, },
                    { field: 'main.beneficiaryEnName', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly:this.readOnly, },

                    { field: 'main.swiftCode', xtype: 'textfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2', readOnly:this.readOnly,  },
                    { field: 'main.cnaps', xtype: 'textfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2', readOnly:this.readOnly,  },
                    { field: 'main.bankAccount', xtype: 'textfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2', readOnly:this.readOnly,allowBlank: false, },
                    { field: 'main.currency',  xtype:'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2', allowBlank: false ,readOnly:this.readOnly,allowBlank: false, }
                ] },

                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.guaranteeLetterFile', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'guaranteeLetterName', xtype: 'FilesDialog', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-13',
                            formId: conf.mainFormPanelId, hiddenName: 'main.guaranteeLetterFile', single:true, readOnly:this.readOnly,allowBlank: false,
                        },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.guaranteeLetterFile',
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
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'contractGuaranteeLetterName', xtype: 'FilesDialog', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-13',
                            formId: conf.mainFormPanelId, hiddenName: 'main.contractGuaranteeLetterFile', single:true, readOnly:this.readOnly
                        },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.contractGuaranteeLetterFile',
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
                ] },

            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });

        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);

                    //guarantee letter
                    if(json.data.guaranteeLetterFile) {
                        this.getCmpByName('guaranteeLetterName').setValue(json.data.guaranteeLetterFile.document.name);
                        this.getCmpByName('main.guaranteeLetterFile').setValue(json.data.guaranteeLetterFile.documentId);
                    }

                    //contract guarantee letter
                    if(json.data.contractGuaranteeLetterFile) {
                        this.getCmpByName('contractGuaranteeLetterName').setValue(json.data.contractGuaranteeLetterFile.document.name);
                        this.getCmpByName('main.contractGuaranteeLetterFile').setValue(json.data.contractGuaranteeLetterFile.documentId);
                    }

                    //service provider
                    if(json.data.type == 2){
                        this.getCmpByName('main.serviceProviderId').setValue(json.data.vendorId);
                        this.getCmpByName('main.serviceProviderName').setValue(json.data.vendorName);
                        this.getCmpByName('main.vendorId').setValue('');
                        this.getCmpByName('main.vendorName').setValue('');

                        //service provider details
                        this.getCmpByName('serviceProvider.cnName').setValue(json.data.vendor.cnName);
                        this.getCmpByName('serviceProvider.enName').setValue(json.data.vendor.enName);
                        this.getCmpByName('serviceProvider.address').setValue(json.data.vendor.address);
                        this.getCmpByName('serviceProvider.director').setValue(json.data.vendor.director);
                        this.getCmpByName('serviceProvider.abn').setValue(json.data.vendor.abn);
                        this.getCmpByName('serviceProvider.currency').setValue(json.data.vendor.currency);
                        this.getCmpByName('serviceProvider.website').setValue(json.data.vendor.website);
                        this.getCmpByName('serviceProvider.source').setValue(json.data.vendor.source);
                    }
				}
			});
		}

		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount','main.companyCnName', 'main.companyEnName','main.companyCnAddress', 'main.companyEnAddress','main.beneficiaryBank','main.beneficiaryBankAddress','main.swiftCode','main.cnaps','main.bankAccount', 'main.currency',]);
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	var cmp = Ext.getCmp(this.mainFormPanelId);
    	var type = cmp.getCmpByName('main.type');
        //console.log(cmp.setAllowBlank(false,  ['guaranteeLetterName']));
        //return ;
    	//如果是服务商，将服务商名称等信息保存在供应商字段类，推送到后台
    	if(type.getValue() == 2){
    	    cmp.getCmpByName('main.vendorId').setValue(cmp.getCmpByName('main.serviceProviderId').getValue());
            cmp.getCmpByName('main.vendorName').setValue(cmp.getCmpByName('main.serviceProviderName').getValue());
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
    },

    changeType: function(type, eOpts) {
        var obj = eOpts || this.ownerCt;
        if (type == 1) {
            Ext.getCmp(obj + '-vendorContainer').show();
            Ext.getCmp(obj + '-serviceProviderContainer').hide();
            Ext.getCmp('depositContainer').show();
            Ext.getCmp(obj).getCmpByName('main.depositType').allowBlank = false;
            Ext.getCmp(obj).getCmpByName('main.depositRate').allowBlank = false;
            Ext.getCmp(obj).getCmpByName('main.depositType').setValue('2');
            Ext.getCmp(obj).getCmpByName('main.depositRate').setValue(0);

        } else if (type == 2) {
            Ext.getCmp(obj + '-vendorContainer').hide();
            Ext.getCmp(obj + '-serviceProviderContainer').show();
            Ext.getCmp('depositContainer').hide();
            Ext.getCmp(obj).getCmpByName('main.depositType').allowBlank =true;
            Ext.getCmp(obj).getCmpByName('main.depositRate').allowBlank = true;
            Ext.getCmp(obj).getCmpByName('main.depositType').setValue(null);
            Ext.getCmp(obj).getCmpByName('main.depositRate').setValue(null);

        }
    }
});
