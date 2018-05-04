FlowPurchasePlanForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowPurchasePlan.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowPurchasePlan',
            winId : 'FlowPurchasePlanForm',
            frameId : 'FlowPurchasePlanView',
            mainGridPanelId : 'FlowPurchasePlanGridPanelID',
            mainFormPanelId : 'FlowPurchasePlanFormPanelID',
            processFormPanelId: 'FlowPurchasePlanProcessFormPanelID',
            searchFormPanelId: 'FlowPurchasePlanFormGridSearchFormPanelID',
            mainTabPanelId: 'FlowPurchasePlanFormGridMainTbsPanelID',
            ProductGridPanelId : 'FlowPurchasePlanProductGridPanelID',
            formGridPanelId : 'FlowPurchasePlanFormGridPanelID',

			urlList: __ctxPath + 'flow/purchase/plan/list',
			urlSave: __ctxPath + 'flow/purchase/plan/save',
			urlDelete: __ctxPath + 'flow/purchase/plan/delete',
			urlGet: __ctxPath + 'flow/purchase/plan/get',
			urlFlow: __ctxPath + 'flow/purchase/plan/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchasePlan&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        FlowPurchasePlanForm.superclass.constructor.call(this, {
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
                // {field: 'main.rateAudToRmb', xtype: 'hidden', value: curUserInfo.audToRmb},
                // {field: 'main.rateAudToUsd', xtype: 'hidden', value: curUserInfo.audToUsd},
                
                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    hideDetails: this.action == 'add',
                    callback: function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).currency = record.currency;
                        var me = conf;
                        $HpStore({
                            fields: ['id','businessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                                'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                                'assigneeId','assigneeCnName','assigneeEnName'],
                            url: __ctxPath + 'finance/balanceRefund/list?vendorId=' + record.id + '&type=1', loadMask: true, scope: this,
                            callback: function (obj, records, eOpts) {
                                Ext.getCmp(me.mainFormPanelId).getCmpByName('main.balanceRefunds').setValue(records);
                            }
                        });

                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    }

                })},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.tradeTerm', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fTradeTerm, cls: 'col-2',allowBlank: false, value:'FOB'},
                ]},
                //可冲销单据
                { xtype: 'section', title:_lang.FlowPurchasePlan.tabWriteOffDocument},
                { field: 'main.balanceRefunds', xtype: 'BalanceRefundFormMultiGrid', fieldLabel: _lang.FlowBalanceRefund.tabRefundList, scope:this,  readOnly: this.readOnly,
                    mainFormPanelId: conf.mainFormPanelId, callback: this.onGridDataChange
                },

                //报价明细
                { xtype: 'section', title:_lang.FlowPurchaseContract.tabProductsQuotationInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.leadTime', xtype: 'numberfield', fieldLabel: _lang.NewProductDocument.fLeadTime, cls:'col-2', allowBlank: false },
                    { field: 'main.totalCbm', xtype: 'numberfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCbm, cls:'col-2', decimalPrecision: 3, allowBlank: false }
                ]},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.totalCases', xtype: 'numberfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCases, cls:'col-2', allowBlank: false },
                    { field: 'main.totalCartons', xtype: 'numberfield', fieldLabel: _lang.FlowPurchasePlan.fTotalCartons, cls:'col-2', allowBlank: false },
                ]},
                { xtype: 'container',cls:'row', items:  [
                    {field: 'main.rateAudToRmb', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToRmb, cls: 'col-2', value: curUserInfo.audToRmb, readOnly:true, },
                    {field: 'main.rateAudToUsd', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToUsd, cls: 'col-2', value: curUserInfo.audToUsd,  readOnly:true,},
                ]},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'displayfield', allowBlank: true, updatePriceFlag : 1,
                        aud:{field:'totalPriceAud', fieldLabel:_lang.ProductDocument.fTotalPriceAud},
                        rmb:{field:'totalPriceRmb', fieldLabel:_lang.ProductDocument.fTotalPriceRmb},
                        usd:{field:'totalPriceUsd', fieldLabel:_lang.ProductDocument.fTotalPriceUsd}
                    })},
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'hidden', allowBlank: true, updatePriceFlag : 1,
                        aud:{field:'main.totalPriceAud', fieldLabel:_lang.ProductDocument.fTotalPriceAud},
                        rmb:{field:'main.totalPriceRmb', fieldLabel:_lang.ProductDocument.fTotalPriceRmb},
                        usd:{field:'main.totalPriceUsd', fieldLabel:_lang.ProductDocument.fTotalPriceUsd}
                    })},


                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'displayfield', allowBlank: true,  updatePriceFlag : 1,
                        aud:{field:'main.writeOffAud', fieldLabel:_lang.FlowPurchaseContract.fWriteOffAud},
                        rmb:{field:'main.writeOffRmb', fieldLabel:_lang.FlowPurchaseContract.fWriteOffRmb},
                        usd:{field:'main.writeOffUsd', fieldLabel:_lang.FlowPurchaseContract.fWriteOffUsd}
                    })}
                ]},

                { xtype: 'section', title:_lang.FlowPurchasePlan.tabPurchasePlanList},
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowPurchasePlanFormGrid', fieldLabel: _lang.FlowProductQuotation.fQuoteDetail, scope:this, readOnly: this.readOnly,
                    dataChangeCallback: this.onGridDataChange,
                },

                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: this.readOnly},

                //相关报告
                { xtype: 'section', title: _lang.Reports.tabRelatedReports},
                { field: 'main.reports', xtype: 'ReportsFormMultiGrid',fieldLabel: _lang.Reports.tabRelatedReports,
                    farmeId: conf.mainFormPanelId, scope:this, readOnly: true,
                },

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var me= this;
                    var json = Ext.JSON.decode(response.responseText);
                    Ext.getCmp(conf.mainFormPanelId).currency = json.data.vendor['currency'];
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        for(index in json.data.details){
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            Ext.applyIf(product, json.data.details[index].product.prop);
                            Ext.apply(product, json.data.details[index].product)

                            product.orderValueAud = parseFloat(json.data.details[index].priceAud || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);
                            product.orderValueRmb = parseFloat(json.data.details[index].priceRmb || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);
                            product.orderValueUsd = parseFloat(json.data.details[index].priceUsd || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);

                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                        }
                    }

                    var meConf = conf;
                    if(this.readOnly){
                        Ext.getCmp(meConf.mainFormPanelId).getCmpByName('main.balanceRefunds').setValue(null, json.data.purchaseBalanceRefundUnions);
                    }else{
                        $HpStore({
                            fields: ['id','businessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                                'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                                'assigneeId','assigneeCnName','assigneeEnName'],
                            url: __ctxPath + 'finance/balanceRefund/list?type=1&vendorId=' + json.data.vendorId, loadMask: true, scope: this,
                            callback: function (obj, records, eOpts) {
                                Ext.getCmp(meConf.mainFormPanelId).getCmpByName('main.balanceRefunds').setValue(records, json.data.purchaseBalanceRefundUnions);
                            }
                        });
                    }

                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                    //相关报告
                    !!json.data && !!json.data.reports && me.action != 'copy' ? Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(json.data.reports) : '';

                }
            });
        }
        this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount','main.totalCbm','main.rateAudToRmb','main.rateAudToUsd']);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    onGridDataChange: function(store, conf) {
        var totalCbm = 0, totalCases=0, totalCartons=0;
        var totalPriceAud = 0, totalPriceRmb = 0,totalPriceUsd = 0;
        var totalBalanceRefundsAud = 0, totalBalanceRefundsRmb = 0,totalBalanceRefundsUsd = 0;
        var audToRmb = this.editFormPanel.getCmpByName('main.rateAudToRmb').getValue();
        var audToUsd = this.editFormPanel.getCmpByName('main.rateAudToUsd').getValue();
       
        var data = this.editFormPanel.getCmpByName('main.products').formGridPanel.getStore().data.items;
        var balanceRefundsData = this.editFormPanel.getCmpByName('main.balanceRefunds').formGridPanel.getStore().data.items;
        for(var i = 0; i < balanceRefundsData.length; i++) {
            if(balanceRefundsData[i].data.active == true){
                totalBalanceRefundsAud = totalBalanceRefundsAud + parseFloat(balanceRefundsData[i].data.totalFeeAud);
                totalBalanceRefundsRmb = totalBalanceRefundsRmb + parseFloat(balanceRefundsData[i].data.totalFeeRmb);
                totalBalanceRefundsUsd = totalBalanceRefundsUsd + parseFloat(balanceRefundsData[i].data.totalFeeUsd);
            }
        }

        for (var i = 0; i < data.length; i++) {
            var rowTotalCbm = 0;
            totalPriceAud = totalPriceAud +  parseFloat(data[i].data.orderValueAud || 0.00);
            totalPriceRmb = totalPriceRmb + parseFloat(data[i].data.orderValueRmb || 0.00);
            totalPriceUsd = totalPriceUsd + parseFloat(data[i].data.orderValueUsd || 0.00);
            var orderQtyCarton = parseFloat(data[i].data.orderQtyCarton) || 0;
            totalCases += parseInt(data[i].data.orderQty) || 0;
            totalCartons += parseInt(data[i].data.orderQtyCarton) || 0;

            var cartonCbm = data[i].raw.masterCartonCbm || data[i].raw.innerCartonCbm || 0

            rowTotalCbm = parseFloat(cartonCbm) * orderQtyCarton;
            totalCbm = parseFloat(totalCbm) + parseFloat(rowTotalCbm);
            //console.log(data[i].raw.cbm);
        };
        var $currency = this.editFormPanel.currency;

        if($currency == '1'){
            totalPriceRmb = (totalPriceAud * audToRmb)
            totalPriceUsd = (totalPriceAud * audToUsd)
        }else if($currency == '2'){
            totalPriceAud = (totalPriceRmb / audToRmb)
            totalPriceUsd = (totalPriceRmb / audToRmb * audToUsd)
        }else if($currency == '3'){
            totalPriceAud = (totalPriceUsd / audToUsd)
            totalPriceRmb = (totalPriceUsd * audToRmb / audToUsd)
        }

        this.editFormPanel.getCmpByName('totalPriceAud').setValue(totalPriceAud.toFixed(2));
        this.editFormPanel.getCmpByName('totalPriceRmb').setValue(totalPriceRmb.toFixed(2));
        this.editFormPanel.getCmpByName('totalPriceUsd').setValue(totalPriceUsd.toFixed(2));
        this.editFormPanel.getCmpByName('main.totalPriceAud').setValue(totalPriceAud.toFixed(2));
        this.editFormPanel.getCmpByName('main.totalPriceRmb').setValue(totalPriceRmb.toFixed(2));
        this.editFormPanel.getCmpByName('main.totalPriceUsd').setValue(totalPriceUsd.toFixed(2));
        this.editFormPanel.getCmpByName('main.writeOffAud').setValue(totalBalanceRefundsAud.toFixed(2));
        this.editFormPanel.getCmpByName('main.writeOffRmb').setValue(totalBalanceRefundsRmb.toFixed(2));
        this.editFormPanel.getCmpByName('main.writeOffUsd').setValue(totalBalanceRefundsUsd.toFixed(2));

        this.editFormPanel.getCmpByName('main.totalCbm').setValue(totalCbm.toFixed(2));
        this.editFormPanel.getCmpByName('main.totalCases').setValue(totalCases);
        this.editFormPanel.getCmpByName('main.totalCartons').setValue(totalCartons);

    },

    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var flag = false;
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
    	if(!!rows && rows.length>0){
			for(index in rows){
				params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				for(key in rows[index].data){
				    if(rows[index].data.orderQty < rows[index].data.moq || rows[index].data.orderQty < 1 || rows[index].data.orderQty == null || rows[index].data.orderQtyCarton < 1 ||  rows[index].data.orderQtyCarton ==null){
                        flag = true;
                    }
					if(key == 'id') {
                        params['details['+index+'].productId'] = rows[index].data.id;
                    }else
						params['details['+index+'].'+key ] = rows[index].data[key];
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
                    params['purchaseBalanceRefundUnions[' + index + '].purchasePlanBusinessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
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

        //reports
        var reportsCount = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.reports').subGridPanel});
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if(params['details[0].productId'] == undefined || params.act == 'start' && (reportsCount==undefined || reportsCount<1)) {
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorReportData);
        }else if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == ''){
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        }else if(flag){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowPurchaseContract.rsErrorThanMoqQty);
        }else{
            $postForm({
            	formPanel: Ext.getCmp(this.mainFormPanelId),
            	grid: Ext.getCmp(this.mainGridPanelId),
            	scope: this,
            	url: isFlow ? this.urlFlow: this.urlSave,
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