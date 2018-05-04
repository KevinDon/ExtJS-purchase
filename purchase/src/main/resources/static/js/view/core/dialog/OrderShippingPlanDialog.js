Ext.define('App.OrderShippingPlanDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.OrderShippingPlanDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.OrderShippingPlanDialog.superclass.constructor.call(this, conf);
    },

    onTriggerWrapClick: function(conf) {

        if (this.onlyRead) {return;}
        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }
        new OrderShippingPlanDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            selectedId : selectedId,
            isFormField: true,
            getFlow: this.getFlow || false,
            serviceId: this.serviceId || '',
            type: this.type || '',
            meForm: Ext.getCmp(this.formId),
            callback:function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
                    this.subcallback.call(this, rows);
                }
            }}, false).show();
    }
});


OrderShippingPlanDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowOrderShippingPlan.mTitle;
        conf.moduleName = 'FlowOrderQualityInspection';
        conf.winId = 'OrderShippingPlanDialogWinID';
        conf.mainGridPanelId = 'OrderShippingPlanDialogWinGridPanelID';
        conf.searchFormPanelId= 'OrderShippingPlanDialogWinSearchPanelID';
        conf.selectGridPanelId = 'OrderShippingPlanDialogWinSelectGridPanelID';
        conf.treePanelId = 'OrderShippingPlanDialogWinTreePanelId';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);

        if(this.getFlow){
            conf.urlList = __ctxPath + 'flow/shipping/orderShippingPlan/listForDialog';
        }else{
            conf.urlList = __ctxPath + 'shipping/orderShippingPlan/list';
        }

        if(!!this.type){
            conf.urlList = conf.urlList + '?type=' + this.type;
        }

        if(!!this.serviceId){
            if(conf.urlList.indexOf("?")>0){
                conf.urlList += '&serviceProviderId=' + conf.serviceId;
            }else {
                conf.urlList += '?serviceProviderId=' + conf.serviceId;
            }
        }

        this.initUI(conf);
        OrderShippingPlanDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.FlowOrderShippingPlan.mTitle,
            width: this.single ? 980 : 1080,
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
            onlyKeywords: true
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title:_lang.FlowOrderShippingPlan.mTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            //width: '85%',
            //minWidth: 1080,
            autoScroll: true,
            collapsible : true,
            split : true,
            url : conf.urlList,
            rsort: false,
            // url: '',
            fields: [
                'id','businessId','orderShippingPlanId','orderNumbers','handlerCnName','handlerEnName','handlerId',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName','totalContainerQty','vendorName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','handlerCnName','handlerId','handlerEnName'
            ],
            columns: [
                { header: _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'id', width: 180, hidden: !this.getFlow,  },
                { header: _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'businessId', width: 180, hidden: this.getFlow },
                { header: _lang.FlowOrderShippingPlan.fOrderNumber, dataIndex: 'orderNumbers', width: 180},
                { header: _lang.FlowOrderShippingPlan.fTotalContainerQty, dataIndex: 'totalContainerQty', width: 80},

            ],
            appendColumns: $groupGridCreatedColumnsForFlow({
                assignee:false,
                flowStatus:false,
                flowHold:false,
            }),
            itemclick : function(obj, record, item, index, e){
                var win = Ext.getCmp(conf.winId);
                win.detailsGridPanel.getStore().removeAll();

                if(!!record.raw && !!record.raw.id) {
                    var url = __ctxPath + 'shipping/orderShippingPlan/getdetails?shippingPlanId=' + record.raw.id;

                    if(!!conf.type){
                        url = url + '&type=' + conf.type;
                    }

                    $postUrl({
                        url: url, maskTo: this.frameId, params: [], autoMessage:false,
                        callback: function (response, eOpts) {
                            var json = Ext.JSON.decode(response.responseText);
                            if(!!json.data){
                                win.detailsGridPanel.getStore().add(json.data);
                            }
                        }
                    });
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
            title: _lang.FlowOrderShippingPlan.tSelected,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            width: 150,
            minWidth: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            split : true,
            fields : ['businessId','orderNumber'],
            columns : [
                { header: _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'businessId', width: 200,  },
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
            title:  _lang.FlowOrderShippingPlan.tOrderDetail,
            id : conf.mainGridPanelId + '-details',
            scope : this,
            height: 300,
            minHeight: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            split : true,
			forceFit: false,
            rsort: false,
            fields : [
                'orderNumber', 'orderTitle', 'originPortId', 'destinationPortId', 'containerType',
                'containerQty', 'readyDate', 'etd', 'serviceProviderCnName', 'serviceProviderEnName','serviceProviderQuotationId'
            ],
            columns : [
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },
                { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100 ,
                    renderer: function(value){
                        var $loadingPort = _dict.getValueRow('origin', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $loadingPort.cnName;
                        }else{
                            return $loadingPort.enName;
                        }
                    },
                },
                { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.getValueRow('destination', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $destPort.cnName;
                        }else{
                            return $destPort.enName;
                        }
                    },
                },
                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                            return $containerType[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                },
                { header: _lang.FlowOrderShippingPlan.fContainerQty, dataIndex: 'containerQty', width: 80,
                    editor: {xtype: 'textfield',   }
                },
                { header: _lang.FlowOrderShippingPlan.fReadyDate, dataIndex: 'readyDate', width: 140, format: curUserInfo.dateFormat,
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat},
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etd', width: 140, format: curUserInfo.dateFormat,
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat},
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowWarehousePlanning.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 260},
                { header: _lang.FlowWarehousePlanning.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 260},
                { header: _lang.FlowServiceInquiry.fQuotationId, dataIndex: 'serviceProviderQuotationId', width: 160},

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
        if(this.single){
            rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows[i].data.id;
                names += rows[i].data.id;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.id;
            }
        }

        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            this.callback.call(this, ids, names, rows, win.selectedData);
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
    }
});