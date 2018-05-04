Ext.define('App.OrderShippingConfirmationDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.OrderShippingConfirmationDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.OrderShippingConfirmationDialog.superclass.constructor.call(this, conf);
    },

    onTriggerWrapClick: function(conf) {
        var scope = this;
        if (this.onlyRead) {return;}
        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new OrderShippingConfirmationDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            initValue: this.initValue || true,
            isFormField: true,
            formId: this.formId  || 'OrderShippingConfirmationDialogWinID',
            meForm: Ext.getCmp(this.formId),
            callback:function(title, records) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(title);
                if(scope.callback) {
                    scope.callback.call(scope, title, records);
                }

                this.eventClose ? this.eventClose.call(this, title, records) : '';
            }}, false).show();
    }
});

OrderShippingConfirmationDialogWin = Ext.extend(HP.Window,{
    scope: this,
    constructor : function(conf) {
        conf.title = _lang.MyDocument.mOrderShippingConfirmationDialogTitle;
        conf.moduleName = 'PackingList';
        conf.winId = conf.formId + '-Pop';
        conf.urlList= __ctxPath + 'flow/purchase/purchasecontract/list';

        conf.mainGridPanelId = conf.mainGridPanelId || conf.formId + '-PackingListGrid';
        conf.mainFormPanelId = conf.formId != undefined? conf.formId + '-PackingListForm' : conf.mainGridPanelId + '-PackingListForm';
        conf.searchFormPanelId= conf.mainGridPanelId != undefined ? conf.formId + '-PackingListSearch' : conf.mainGridPanelId + '-PackingListSearch';
        conf.subGridPanelId =  conf.subGridPanelId ? conf.subGridPanelId +'-PackingListMultiGridPanelID' : conf.mainGridPanelId +'-PackingListMultiGridPanelID' ;
        conf.subFormPanelId = conf.subFormPanelId ?  conf.subFormPanelId  +'-PackingListMultiFormPanelID' : conf.mainGridPanelId +'-PackingListMultiFormPanelID';
        conf.selectGridPanelId = conf.selectGridPanelId ?  conf.selectGridPanelId  +'-PackingListMultiFormSelectPanelID' : conf.mainGridPanelId +'-PackingListMultiFormSelectPanelID';

        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        conf.defHeight = this.height || 600;

        // console.log(conf);
        Ext.applyIf(this, conf);
        this.initUI(conf);
        OrderShippingConfirmationDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title,
            width: this.single ? 880 : 1080,
            height: conf.defHeight,
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

        //grid panel
        this.gridPanel = new HP.GridPanel({
            region : 'center',
            scope : this,
            id : conf.subGridPanelId,
            forceFit: false,
            // url: conf.urlList,
            url: '',
            fields: [
                'businessId', 'orderId', 'orderNumber','orderTitle','originPortId','originPortCnName','originPortEnName',
                'destinationPortId','destinationPortCnName','destinationPortEnName','containerType','readyDate','etd', 'eta',
                'serviceProviderId','serviceProviderName','serviceProviderCnName','serviceProviderEnName',
                'containerQty',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
                { header: _lang.FlowOrderShippingConfirmation.fOrderShippingConfirmationId, dataIndex: 'businessId', width: 180,  },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 100, hidden: true},
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},
                { header: _lang.FlowPurchaseContract.fSubject, dataIndex: 'orderTitle', width: 200, },
                { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100 ,
                    renderer: function(value){
                        var $loadingPort = _dict.origin;
                        if(value  && $loadingPort.length > 0 && $loadingPort[0].data.options.length>0){
                            return $loadingPort[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                },
                { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.destination;
                        if(value  && $destPort.length > 0 && $destPort[0].data.options.length>0){
                            return $destPort[0].data.options[parseInt(value) - 1].title;
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
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etd', width: 140, format: curUserInfo.dateFormat,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowOrderShippingConfirmation.fEta, dataIndex: 'eta', width: 140, dateFormat: curUserInfo.dateFormat,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    },
                },
                // { header: _lang.FlowOrderShippingPlan.fDepositStatus, dataIndex: 'depositStatus', width: 80},
                { header: _lang.ServiceProviderDocument.fServiceProviderId, dataIndex: 'serviceProviderId', width: 200},
                { header: _lang.VendorDocument.fCnName, dataIndex: 'serviceProviderCnName', width: 260},
                { header: _lang.VendorDocument.fEnName, dataIndex: 'serviceProviderEnName', width: 260},
            ],// end of columns
            // appendColumns: $groupGridCreatedColumns({sort:false}),
            itemdblclick : function(obj, record, item, index, e, eOpts){
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
                // //初始化选择
                // if(this.selectedId && records.length){
                //     for(var i=0; i<records.length; i++){
                //         if(records[i].data.id == this.selectedId){
                //             Ext.getCmp(conf.subGridPanelId).getSelectionModel().select(records[i], true);
                //         }
                //     }
                // };
            }

        });

        this.loadTestData();

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.PurchaseContractDialog.tSelected,
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
            fields : ['id','orderNumber'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.PurchaseContractDialog.fOrderNo, dataIndex: 'orderNumber', width:90, sortable:false,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
            }
        });

        this.centerPanel = new Ext.Panel({
            id: conf.winId + '-center',
            layout: 'border',
            region: 'center',
            border: false,
            scope: this,
            items: [ this.searchPanel, this.gridPanel]
        });

        // init value
        if(this.initValue && this.fieldValueName){
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if(ids){
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for(var i=0; i<arrIds.length; i++){
                    selStore.add({id: arrIds[i], name: arrNames[i]});
                }
            }
        }
    },

    winOk: function(){
        var formFieldValue = null;
        var records = null;

        if(true || this.single){
            records = Ext.getCmp(this.subGridPanelId).getSelectionModel().selected.items;
            // console.log(records);
        }else{
            var store = Ext.getCmp(this.selectGridPanelId).getStore();
            records = store.getRange();
        }

        if(!!records && records.length > 0){
            var orderNumbers = [];
            for(var i = 0; i < records.length; i++){
                orderNumbers.push(records[i].orderNumber);
            }
            formFieldValue = orderNumbers.join(',');
        }

        if (this.callback) {
            this.callback.call(this, formFieldValue, records);
        }

        Ext.getCmp(this.winId).close();
    },

    winClean: function(){
        var ids = '';
        var names = '';
        if (this.callback) {
            this.callback.call(this, ids, names);
        }
        Ext.getCmp(this.winId).close();
    },

    loadTestData: function(){
        var listUrl = __ctxPath + 'flow/shipping/orderShippingApply/list';
        var detailsUrl = __ctxPath + 'flow/shipping/orderShippingApply/get?id=';
        this.data = [];
        var scope = this;
        Ext.Ajax.request({
            url : listUrl,
            method : 'post',
            success : function(response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var ids = [];
                for(var i= 0; i < response.data.length; i++){
                    ids.push(response.data[i].id);
                }

                for(var i = 0; i < ids.length; i++){
                    var id = ids[i];
                    Ext.Ajax.request({
                        url : detailsUrl + id,
                        method : 'post',
                        success : function(response, options) {
                            var response = Ext.JSON.decode(response.responseText);
                            scope.gridPanel.getStore().add(response.data.details);
                            scope.data.push(response.data.details);
                        },
                        failure : function() {
                        }
                    });
                }
            },
            failure : function() {
            }
        });
    },
});