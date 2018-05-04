Ext.define('App.WarehousePlanningDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.WarehousePlanningDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.WarehousePlanningDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {

        if (this.onlyRead) {return;}
        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }
        new WarehousePlanningDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            selectedId : selectedId,
            isFormField: true,
            type:this.type? this.type: 1,
            meForm: Ext.getCmp(this.formId),
            callback:function(ids, titles, rows, selected, asnData) {
                // console.log(rows);
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
                    this.subcallback.call(this, rows, asnData);
                }
            }}, false).show();
    }
});


WarehousePlanningDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowOrderShippingPlan.mTitle;
        conf.moduleName = 'FlowOrderQualityInspection';
        conf.winId = 'WarehousePlanningDialogWinID';
        conf.mainGridPanelId = 'WarehousePlanningDialogWinGridPanelID';
        conf.searchFormPanelId= 'WarehousePlanningDialogWinSearchPanelID';
        conf.selectGridPanelId = 'WarehousePlanningDialogWinSelectGridPanelID';;
        conf.treePanelId = 'WarehousePlanningDialogWinTreePanelId';
        conf.refresh = true
        conf.urlList = __ctxPath + 'purchase/customClearancePacking/listForAsn';
        conf.urlGet =  __ctxPath + 'wmsapi/inbound/createAsn';
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        if(!!this.type){
            conf.urlList = conf.urlList + '?type=' + this.type;

        }
        this.initUI(conf);
        WarehousePlanningDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title:  _lang.FlowOrderReceivingNotice.tabWarehousePlanningOrderId,
            width: this.single ? 1080 : 1280,
            region: 'center',
            layout : 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.centerPanel, this.selectGridPanel]
        });
    },

    initUI : function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            onlyKeywords: true,
            bodyCls:'x-panel-body-gray',
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title:  _lang.FlowOrderReceivingNotice.tabWarehousePlanningOrderId,
            //collapsible: true,
            //split: true,
            scope: this,
            header:{cls:'x-panel-header-gray' },
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 980,
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName',
                'departmentEnName','totalContainerQty','vendorName', 'orderNumber', 'orderTitle','containerNumber',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','accessories',
                {name: 'warehousePlanId', mapping: 'warehousePlanDetail.warehousePlanId'},
                {name: 'receiveStartDate', mapping: 'warehousePlanDetail.receiveStartDate'},
                {name: 'receiveEndDate', mapping: 'warehousePlanDetail.receiveEndDate'},
                {name: 'originPlace', mapping: 'warehousePlanDetail.originPlace'},
                {name: 'destinationPlace', mapping: 'warehousePlanDetail.destinationPlace'},
            ],
            columns: [
                { header: _lang.WarehousePlanningDialog.fWarehousePlanningId, dataIndex: 'warehousePlanId', width: 180,  },
                { header: _lang.FlowBalanceRefund.fContainerNumber, dataIndex: 'containerNumber', width: 130,  },
                { header: _lang.ProductDocument.fJobNumber, dataIndex: 'orderNumber', width: 130,  },
                { header: _lang.FlowPurchaseContract.fOrderTitle, dataIndex: 'orderTitle', width: 130,  },
                { header: _lang.FlowCustomClearance.fHasAccessory, dataIndex: 'accessories', width: 130,
                    renderer: function(value){
                        var $optionsYesNo = _dict.optionsYesNo;
                        if($optionsYesNo.length>0 && $optionsYesNo[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $optionsYesNo[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowWarehousePlanning.fReceiveStartDate, dataIndex: 'receiveStartDate', width: 140,  },
                { header: _lang.FlowWarehousePlanning.fReceiveEndDate, dataIndex: 'receiveEndDate', width: 140,  },
                { header: _lang.FlowWarehousePlanning.fOriginAddress, dataIndex: 'originPlace', width: 200,  },
                { header: _lang.FlowWarehousePlanning.fDestAddress, dataIndex: 'destinationPlace', width: 200,  },
            ],
            itemclick : function(obj, record, item, index, e){
                var win = Ext.getCmp(conf.winId);
                win.detailsGridPanel.getStore().removeAll();
                if(!!record.raw && !!record.raw.details) {
                    var detail = {};
                    if(record.raw.details.length > 0){
                        for(index in record.raw.details){
                            Ext.apply(detail, record.raw.details[index]);
                            win.detailsGridPanel.getStore().add(detail);
                        }
                    }
                }
            },
            itemdblclick : function(obj, record, item, index, e, eOpts){
                if(!!this.scope.selectedData) {
                    this.scope.selectedData.push(record.raw);
                }

                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    if(selStore.getCount()){
                        for (var i = 0; i < selStore.getCount(); i++) {
                            if (selStore.getAt(i).data.id == record.data.id) {
                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedRecord);
                                return;
                            }
                        }
                    }

                    selStore.add(record.data);
                }else{
                    this.scope.winOk.call(this.scope);
                }
            },
            callback : function(obj, records){
                //初始化选择
                if(this.selectedId && records.length){
                    for(var i=0; i<records.length; i++){
                        if(records[i].data.id == this.selectedId){
                            // console.log(records);
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                };
            }
        });

        //select records data, with details
        this.selectedData = [];

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title:  _lang.FlowOrderReceivingNotice.tabWarehousePlanningOrderId,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            width: 150,
            minWidth: 150,
            header:{cls:'x-panel-header-gray' },
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            split : true,
            fields : ['id','orderNumber'],
            columns : [
                { header: _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'id', width: 160,  },
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
                var selectedData = [];
                for(var i = 0; i < this.scope.selectedData.length; i++){
                    var data = this.scope.selectedData[i];
                    if(data.id != record.data.id) {
                        selectedData.push(data);
                    }
                }

                this.scope.selectedData = selectedData;
            }
        });

        this.detailsGridPanel = new HP.GridPanel({
            region : 'south',
            title: _lang.FlowWarehousePlanning.tabPackingListOrder,
            id : conf.mainGridPanelId + '-details',
            scope : this,
            height: 300,
            minHeight: 150,
            border: false,
            autoLoad: false,
            header:{cls:'x-panel-header-gray' },
            unbbar : true,
            rsort:false,
            collapsible : true,
            split : true,
            fields : [
                'id', 'productId', 'sku', 'orderQty', 'priceCostAud', 'priceCostRmb', 'priceCostUsd', 'rateAudToRmb','rateAudToUsd',
                'vendorCnName', 'vendorEnName', 'currency',
            ],
            columns : [
                { header:'ID', dataIndex: 'id', width: 80, hidden: true,  },
                { header: _lang.FlowBalanceRefund.fProductId, dataIndex: 'productId', width: 80, hidden: true,  },
                { header: _lang.FlowBalanceRefund.fSku, dataIndex: 'sku', width: 80},
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', width: 80, },

                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width:50,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //成本
                { header: _lang.ProductCost.fPurchaseCost,
                    columns: new $groupPriceColumns(this, 'priceCostAud','priceCostRmb','priceCostUsd', null,{edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowDepositContract.fVendorCnName, dataIndex: 'vendorCnName', width: 120, hidden: curUserInfo.lang == 'zh_CN' ? false : true, },
                { header: _lang.FlowDepositContract.fVendorEnName, dataIndex: 'vendorEnName', width: 120, hidden: curUserInfo.lang == 'zh_CN' ? true : false},


            ],// end of columns
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,

            items: [ this.searchPanel, this.gridPanel, this.detailsGridPanel]
        });

        // init value
        if(this.fieldValueName){
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if(ids){
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for(var i=0; i<arrIds.length; i++){
                    if(curUserInfo.lang =='zh_CN'){
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
                    }else{
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
                    }
                }
            }
        }
    },

    winOk : function(){
        var ids = '';
        var names = '';
        var rows = {};
        var params = {};
        if(this.single){
            rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
            // console.log(Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected)
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows[i].data.warehousePlanId;
                names += rows[i].data.orderNumber;
                params['id'] = rows[i].raw.id;
                params['orderId'] = rows[i].raw.orderId;
                params['orderNumber'] = rows[i].raw.orderNumber;
                params['containerNumber'] = rows[i].raw.containerNumber;
                params['warehousePlanDetail.warehouseId'] = rows[i].raw.warehousePlanDetail.warehouseId;
                params['warehousePlanDetail.receiveStartDate'] = rows[i].raw.warehousePlanDetail.receiveStartDate;
                params['warehousePlanDetail.receiveEndDate'] =  rows[i].raw.warehousePlanDetail.receiveEndDate;
               for(var index = 0; index < rows[i].raw.details.length;  index++){
                   params['details[' + index + '].sku'] =  rows[i].raw.details[index].sku;
                   params['details[' + index + '].orderQty'] =  rows[i].raw.details[index].orderQty;
                   params['details[' + index + '].packingQty'] =  rows[i].raw.details[index].packingQty;
                   params['details[' + index + '].priceCostAud'] =  rows[i].raw.details[index].priceCostAud;
               }
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.warehousePlanId;
                names += rows.getAt(i).data.orderNumber;
            }
        }
        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            var me = this;
            me.callback.call(me, ids, names, rows, win.selectedData, null);
            // $postUrl({
            //     url: this.urlGet, maskTo: this.frameId, params: params, autoMessage:false,
            //     callback: function (response, eOpts) {
            //         var json = Ext.JSON.decode(response.responseText);
            //         if(!!json.data && json.code >= 0 &&  json.code.asnNo != ''){
            //
            //         }else{
            //             Ext.ux.Toast.msg(_lang.TText.titleOperation, json.msg);
            //         }
            //     }
            // });
        }
        Ext.getCmp(this.winId).close();
    },

    winClean:function(){
        var ids = '';
        var names = '';
        if (this.callback) {
            this.callback.call(this, ids, names);
        }
        Ext.getCmp(this.winId).close();
    },
});