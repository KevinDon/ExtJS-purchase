FlowServiceInquiryForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowServiceInquiry.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowServiceInquiry',
            winId : 'FlowServiceInquiryForm',
			frameId : 'FlowServiceInquiryView',
			mainGridPanelId : 'FlowServiceInquiryViewGridPanelID',
			mainFormPanelId : 'FlowServiceInquiryViewFormPanelID',
			processFormPanelId: 'FlowServiceInquiryProcessFormPanelID',
            urlList: __ctxPath + 'flow/shipping/serviceinquiry/list',
            urlSave: __ctxPath + 'flow/shipping/serviceinquiry/save',
            urlDelete: __ctxPath + 'flow/shipping/serviceinquiry/delete',
            urlGet: __ctxPath + 'flow/shipping/serviceinquiry/get',
            urlFlow: __ctxPath + 'flow/shipping/serviceinquiry/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowServiceProviderQuotation&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        };

        this.initUIComponents(conf);
        FlowServiceInquiryForm.superclass.constructor.call(this, {
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

                //服务商基础信息
                { xtype: 'container',cls:'row', items: $groupFormServiceProviderFields(this, conf,{
                    selectType:2,
                    callback: function (eOpts, record) {
                        var cmp = Ext.getCmp(conf.mainFormPanelId);
                        cmp.getCmpByName('main.type').setValue('');

                        if(record != null ){

                            var currency = _dict.getValueRow('currency', record.currency);
                            if(!!currency) {
                                var mainCurrency = $ucFirst(currency.cnName);
                                cmp.getCmpByName('main.freightCharges').subGridPanel.mainCurrency = mainCurrency;
                                cmp.getCmpByName('main.destinationCharges').subGridPanel.mainCurrency = mainCurrency;
                            }

                            var tpData = $HpStore({
                                fields: ['id','ports','chargeItems','bank'],
                                url:__ctxPath + 'archives/service_provider/get?id='+ record.id,
                                callback: function(eOpts, records){
                                    if(records  && records[0].data ){
                                        cmp.templateData = records[0].data;
                                    }else{
                                        cmp.templateData = null;
                                    }
                                }
                            });
                            cmp.getCmpByName('main.type').setReadOnly(false);
                            cmp.getCmpByName('main_shippingPlanName').serviceId = record.id ;
                        }else{
                            cmp.getCmpByName('main.type').setReadOnly(true);
                            cmp.getCmpByName('main.shippingPlanId').setValue('');
                            cmp.getCmpByName('main_shippingPlanName').setValue('');
                        }

                        cmp.getCmpByName('main.freightCharges').subGridPanel.getStore().removeAll();
                        cmp.getCmpByName('main.destinationCharges').subGridPanel.getStore().removeAll();
                    }
                })},

				//报价信息
                { xtype: 'section', title:_lang.FlowServiceInquiry.tabQuoteDetails},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-1', items:  [
                        { field: 'main.name', xtype:'textfield',  fieldLabel: _lang.FlowServiceInquiry.fName, cls:'col-2',  allowBlank:false},
                        { field: 'main.type', xtype:'dictcombo', code:'service_inquiry', codeSub:'quotation_type', fieldLabel: _lang.FlowServiceInquiry.fQuotationType, cls:'col-2',
                            scope: this, selectFun: function (record, eOpts) {
                                var cmp = Ext.getCmp(conf.mainFormPanelId);
                                cmp.getCmpByName('main.freightCharges').subGridPanel.getStore().removeAll();
                                cmp.getCmpByName('main.destinationCharges').subGridPanel.getStore().removeAll();

                                var cmpShippingPlanId = cmp.getCmpByName('main_shippingPlanName');

                                if(record[0].data.id == 3){
                                    cmpShippingPlanId.show();
                                    cmp.getCmpByName('main.destinationPortId').setReadOnly(true);
                                }else{
                                    cmp.getCmpByName('main.destinationPortId').setReadOnly(false);
                                    cmpShippingPlanId.hide();
                                    cmpShippingPlanId.setValue('');

                                    var serviceId = cmp.getCmpByName('main.serviceProviderId').getValue();
                                    var tpData = $HpStore({
                                        fields: ['id','ports','chargeItems','bank'],
                                        url:__ctxPath + 'flow/shipping/serviceinquiry/getpricetemplate?serviceProviderId='+ serviceId + '&type='+ record[0].data.id, loadMask:true, maskTo:cmp.getCmpByName('main.freightCharges').subGridPanel.id,
                                        callback: function(eOpts, records){
                                            if(records  && records[0].data ){
                                                cmp.templateData = records[0].data;
                                            }else{
                                                cmp.templateData = null;
                                            }
                                            if(cmp.templateData){
                                                //加载模板港口
                                                var grid = cmp.getCmpByName('main.freightCharges').subGridPanel;
                                                var gridStore = grid.getStore();
                                                var ports = cmp.templateData.ports;
                                                gridStore.removeAll();
                                                for(var index in ports){
                                                    ports[index].rateAudToRmb = grid.audToRmb;
                                                    ports[index].rateAudToUsd = grid.audToUsd;
                                                    gridStore.add(ports[index] || {});
                                                }

                                                //加载收费项
                                                var grid = cmp.getCmpByName('main.destinationCharges').subGridPanel;
                                                var gridStore = grid.getStore();
                                                var chargeItems = cmp.templateData.chargeItems;
                                                gridStore.removeAll();
                                                for(index in chargeItems){
                                                    chargeItems[index].rateAudToRmb = grid.audToRmb;
                                                    chargeItems[index].rateAudToUsd = grid.audToUsd;
                                                    gridStore.add(chargeItems[index] || {});
                                                }
                                            }
                                        }
                                    });
                                }
                            },
                           allowBlank:false, readOnly:true,
                        },
                    ],},
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.flowOrderShippingPlanId', xtype:'hidden'},
                        { field: 'main_shippingPlanName', xtype: 'OrderShippingPlanDialog', fieldLabel: _lang.FlowServiceInquiry.fShippingPlanId, cls:'col-1',
                            formId: conf.mainFormPanelId, hiddenName: 'main.flowOrderShippingPlanId', hidden:true, single: true, primaryTable:true, type:1, getFlow:true,
                            subcallback: function(records){
                                var planId = records[0].data.id;
                                // Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.destinationPortId').setValue(!!records && !!records[0].raw.details && !!records[0].raw.details[0].destinationPortId ? records[0].raw.details[0].destinationPortId: '');

                                var serviceProviderId = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceProviderId').getValue();
                                var type = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.type').getValue();

                                var url = __ctxPath + 'flow/shipping/serviceinquiry/getpricetemplate?flowOrderShippingPlanId=' + planId + '&serviceProviderId=' + serviceProviderId + '&type='+ type;
                                var cmp = Ext.getCmp(conf.mainFormPanelId);
                                cmp.getCmpByName('main.freightCharges').subGridPanel.getStore().removeAll();
                                cmp.getCmpByName('main.destinationCharges').subGridPanel.getStore().removeAll();

                                var tpData = $HpStore({
                                    fields: ['id','ports','chargeItems'],
                                    url: url,
                                    callback: function(eOpts, records){
                                        if(records  && records[0].data ){
                                            cmp.templateData = records[0].data;
                                        }else{
                                            cmp.templateData = null;
                                        }
                                        if(cmp.templateData){
                                            //加载模板港口
                                            var grid = cmp.getCmpByName('main.freightCharges').subGridPanel;
                                            var gridStore = grid.getStore();
                                            var ports = cmp.templateData.ports;
                                            gridStore.removeAll();
                                            for(var index in ports){
                                                ports[index].rateAudToRmb = grid.audToRmb;
                                                ports[index].rateAudToUsd = grid.audToUsd;
                                                gridStore.add(ports[index] || {});
                                            }

                                            //加载收费项
                                            var grid = cmp.getCmpByName('main.destinationCharges').subGridPanel;
                                            var gridStore = grid.getStore();
                                            var chargeItems = cmp.templateData.chargeItems;
                                            gridStore.removeAll();
                                            for(index in chargeItems){
                                                chargeItems[index].rateAudToRmb = grid.audToRmb;
                                                chargeItems[index].rateAudToUsd = grid.audToUsd;
                                                gridStore.add(chargeItems[index] || {});
                                            }
                                        }

                                    }
                                });

                            }
                        },
                    ]}
                ]},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.destinationPortId', xtype:'dictcombo', code:'service_provider', codeSub:'destination_port', fieldLabel: _lang.FlowServiceInquiry.fDestination, cls:'col-1', value:'1', allowBlank:false},
                        { field: 'main.effectiveDate', xtype: 'datetimefield', fieldLabel: _lang.FlowServiceInquiry.fEffectiveDate, format: curUserInfo.dateFormat, cls: 'col-1', allowBlank: false,},
                        { field: 'main.validUntil', xtype: 'datetimefield', fieldLabel: _lang.FlowServiceInquiry.fValidUntil, format: curUserInfo.dateFormat, cls: 'col-1', allowBlank: false,},
                    ]},
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.currencyAdjustment', xtype: 'numberfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fCurrencyAdjustment, decimalPrecision:4, cls:'col-1', allowBlank:false},
                        { field: 'main.rateAudToRmb', xtype: 'numberfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fExchangeRateAudToRmb, cls:'col-1', allowBlank:false,
                            decimalPrecision:4, value:curUserInfo.audToRmb, listeners:{
                                blur:function(pt, newValue, oldValue, eOpts ) {
                                    var value = this.value;
                                    if(value) {
                                         $postUrl({ url:__ctxPath  + 'pub/getnull', method: 'get', maskTo:conf.mainFormPanelId, autoMessage: false,
                                            callback: function (req) {
                                                var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.freightCharges').subGridPanel;
                                                cmp.audToRmb = value;
                                                $setColumnValue(cmp, 'rateAudToRmb', value);
                                                for (var index in cmp.getStore().data.items) {
                                                    cmp.scope.autoCountFreight.call(this, cmp.getStore().data.items[index], true);
                                                }
                                                var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.destinationCharges').subGridPanel;
                                                cmp.audToRmb = value;
                                                $setColumnValue(cmp, 'rateAudToRmb', value);
                                                for (var index in cmp.getStore().data.items) {
                                                    cmp.scope.autoCountCharge.call(this, cmp.getStore().data.items[index], true);
                                                }
                                            }
                                        })
                                    }
                                }
                            }
                        },
                        {field: 'main.rateAudToUsd', xtype: 'numberfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fExchangeRateAudToUsd, cls: 'col-1', allowBlank: false,
                            decimalPrecision: 4, value: curUserInfo.audToUsd, listeners: {
                                blur: function (pt, newValue, oldValue, eOpts) {

                                    // value = pt.getValue();
                                    var value = this.value;
                                    if (value) {
                                        $postUrl({ url: __ctxPath + 'pub/getnull', method: 'get', maskTo:conf.mainFormPanelId, autoMessage: false,
                                            callback: function (req) {
                                                var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.freightCharges').subGridPanel;
                                                cmp.audToUsd = value;
                                                $setColumnValue(cmp, 'rateAudToUsd', value);
                                                for (var index in cmp.getStore().data.items) {
                                                    cmp.scope.autoCountFreight.call(this, cmp.getStore().data.items[index], true);
                                                }
                                                var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.destinationCharges').subGridPanel;
                                                cmp.audToUsd = value;
                                                $setColumnValue(cmp, 'rateAudToUsd', value);
                                                for (var index in cmp.getStore().data.items) {
                                                    cmp.scope.autoCountCharge.call(this, cmp.getStore().data.items[index], true);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    ]}
                ]},


                //海运费用
                { field: 'main.freightCharges', xtype: 'FlowServiceInquiryFreightChargeGrid', fieldLabel: _lang.FlowServiceInquiry.fFreightCharges,
                    frameId: conf.mainFormPanelId, scope:this, readOnly: this.readOnly, height:400,
                    mainFormPanelId: conf.mainFormPanelId, mainForm: this
                },

                //本地费用明细
                { field: 'main.destinationCharges', xtype: 'FlowServiceInquiryDestinationChargeGrid', fieldLabel: _lang.FlowServiceInquiry.fDestinationCharges,
                    frameId: conf.mainFormPanelId, scope:this, readOnly: this.readOnly, height:400,
                    mainFormPanelId: conf.mainFormPanelId, mainForm: this, calculate: this.calculate
                },



                //   附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: false
                },


            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
		    if(this.record.type == 3){
                var cmp = Ext.getCmp(conf.mainFormPanelId);
                var cmpShippingPlanId = cmp.getCmpByName('main_shippingPlanName');
                cmpShippingPlanId.show();
            }

            var me = this;
            var action = this.action;
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);
					var cmp = Ext.getCmp(conf.mainFormPanelId);
                    if (!!json.data && !!json.data.ports && json.data.ports.length>0) {
                        var grid = cmp.getCmpByName('main.freightCharges').subGridPanel;
                        var gridStore = grid.getStore();
                        var ports = json.data.ports;
                        grid.audToRmb = json.data.rateAudToRmb;
                        grid.audToUsd = json.data.rateAudToUsd;
                        gridStore.removeAll();
                        for(var index in ports){
                            ports[index].rateAudToRmb = grid.audToRmb;
                            ports[index].rateAudToUsd = grid.audToUsd;
                            gridStore.add(ports[index] || {});
                        }

                        cmp.getCmpByName('main_shippingPlanName').setValue(json.data.flowOrderShippingPlanId || '');
                    }


                    if (!!json.data && !!json.data.chargeItems && json.data.chargeItems.length>0) {
                        var grid = cmp.getCmpByName('main.destinationCharges').subGridPanel;
                        var gridStore = grid.getStore();
                        var chargeItems = json.data.chargeItems;
                        grid.audToRmb = json.data.rateAudToRmb;
                        grid.audToUsd = json.data.rateAudToUsd;
                        gridStore.removeAll();
                        for(var index in chargeItems){
                            chargeItems[index].rateAudToRmb = json.data.rateAudToRmb|| grid.audToRmb;
                            chargeItems[index].rateAudToUsd = json.data.rateAudToUsd|| grid.audToUsd;
                            gridStore.add(chargeItems[index] || {});
                        }
                    }

                    //update exchange rate on copy event
                    if(action == 'copy'){
                        var cmp = Ext.getCmp(conf.mainFormPanelId);
                        var audToRmb = cmp.getCmpByName('main.rateAudToRmb');
                        audToRmb.setValue(curUserInfo.audToRmb);
                        audToRmb.fireEvent('blur', audToRmb);

                        var audToUsd = cmp.getCmpByName('main.rateAudToUsd');
                        audToUsd.setValue(curUserInfo.audToUsd);
                        audToUsd.fireEvent('blur', audToUsd);
                    }

                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments? json.data.attachments : '');

                }
			});
		}

		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount',]);
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        //freight charges
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.freightCharges').subGridPanel});
        if(!!rows && rows.length > 0){
            for(var index in rows){
                for(key in rows[index].data){
                    params['ports['+index+'].'+key ] = rows[index].data[key];
                }
                params['ports['+index+'].businessId'] = businessId;
            }
        }

        //destination charges
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.destinationCharges').subGridPanel});
        if(!!rows && rows.length > 0){
            for(var index in rows){
                for(key in rows[index].data){
                    params['chargeItems['+index+'].'+key ] = rows[index].data[key];
                }
                params['chargeItems['+index+'].businessId'] = businessId;
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
    },

    refershFun: function(conf){
        $postUrl({
            url: __ctxPath + 'pub/getnull', method: 'get', maskTo: conf.mainFormPanelId, autoMessage: false,
            callback: function (req) {
                var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.freightCharges').subGridPanel;
                // cmp.audToRmb = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.rateAudToRmb').getValue();
                // cmp.audToUsd = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.rateAudToUsd').getValue();
                // $setColumnValue(cmp, 'rateAudToUsd', cmp.audToUsd);
                for (var index=0; index< cmp.getStore().data.items.length; index++) {
                    cmp.scope.autoCountFreight.call(cmp, cmp.getStore().data.items[index], true);
                }
                // var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.destinationCharges').subGridPanel;
                // cmp.audToRmb = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.rateAudToRmb').getValue();
                // cmp.audToUsd = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.rateAudToUsd').getValue();
                // $setColumnValue(cmp, 'rateAudToUsd', cmp.audToUsd);
                // for (var index in cmp.getStore().data.items) {
                //     cmp.scope.autoCountCharge.call(cmp, cmp.getStore().data.items[index], true);
                // }
            }
        });
    },

    calculate: function(conf){

        var form = Ext.getCmp(conf.mainFormPanelId);
        var freightChargesGrid = form.getCmpByName('main.freightCharges').subGridPanel;
        var destinationChargesGrid = form.getCmpByName('main.destinationCharges').subGridPanel;

        //calculate total qty
        var totalGp20Qty = 0;
        var totalGp40Qty = 0;
        var totalHq40Qty = 0;
        var totalLclCbm = 0;
        var range = freightChargesGrid.getStore().getRange();
        for(var i = 0; i < range.length; i++){
            var data = range[i].data;
            totalGp20Qty += !!data.gp20Qty ? parseInt(data.gp20Qty) : 0;
            totalGp40Qty += !!data.gp40Qty ? parseInt(data.gp40Qty) : 0;
            totalHq40Qty += !!data.hq40Qty ? parseInt(data.hq40Qty) : 0;
            totalLclCbm += !!data.lclCbm ? parseFloat(data.lclCbm) : 0;
        }

        //update destination charges grid
        $postUrl({ url:__ctxPath  + 'pub/getnull', method: 'get', maskTo:conf.mainFormPanelId, autoMessage: false,
            callback: function (req) {
                range = destinationChargesGrid.getStore().getRange();
                for(var i = 0; i < range.length; i++){
                    var data = range[i].data;
                    // console.log(data);
                    if(data.unitId == 1) {
                        //per container
                        range[i].set('gp20Qty', totalGp20Qty);
                        range[i].set('gp40Qty', totalGp40Qty);
                        range[i].set('hq40Qty', totalHq40Qty);
                        range[i].set('lclCbm', totalLclCbm);
                    } else {
                        range[i].set('gp20Qty', '');
                        range[i].set('gp40Qty', '');
                        range[i].set('hq40Qty', '');
                        range[i].set('lclCbm', '');
                    }
                }
            }
        });
    }
});
