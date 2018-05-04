Ext.define('App.PackingListDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.PackingListDialog',

    constructor : function(conf) {

        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.PackingListDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}

        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new PackingListDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            selectedId : selectedId,
            isFormField: true,
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


PackingListDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {

        conf.title = _lang.FlowWarehousePlanning.tabPackingListSelector;
        conf.moduleName = 'FlowWarehousePlanning';
        conf.winId = 'PackingListDialogWinID';
        conf.mainGridPanelId = 'PackingListDialogWinGridPanelID';
        conf.searchFormPanelId= 'PackingListDialogWinSearchPanelID';
        conf.selectGridPanelId = 'PackingListDialogWinSelectGridPanelID';
        conf.treePanelId = 'PackingListDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'purchase/customClearancePacking/list';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        if(!!this.type){
            conf.urlList =  conf.urlList + '?type=' + this.type;
        }
        this.initUI(conf);

        PackingListDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.FlowWarehousePlanning.tabPackingListSelector,
            width: this.single ? 980 : 1080,
            height: 800,
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
            title:_lang.FlowWarehousePlanning.tabPackingListOrder,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            autoScroll: true,
            url : conf.urlList,
            rsort: false,
            fields: [
                'id','containerNumber', 'sealsNumber', 'orderNo', 'flowOrderShippingPlanId', 'containerType', 'subject', 'orderShippingPlanId',
                'originPort', 'destinationPort','orderNumber','orderTitle',
                'readyDate', 'etd', 'eta', 'packingDate'
            ],
            columns: [
                { header:'ID', dataIndex: 'id', width: 180, hidden:true, },
                { header:  _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'flowOrderShippingPlanId', width: 180, },
                { header:  _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 180, hidden:true, },
                { header: _lang.FlowCustomClearance.fOrderNumber, dataIndex: 'orderNumber', width: 80,  },
                { header:  _lang.FlowCustomClearance.fOrderTitle, dataIndex: 'orderTitle', width: 180, },
                { header: _lang.FlowCustomClearance.fContainerNumber, dataIndex: 'containerNumber', width: 200, },
                { header: _lang.FlowCustomClearance.fSealsNumber, dataIndex: 'sealsNumber', width: 200, },
                {
                    header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function (value) {
                        var $type = _dict.containerType;
                        if (value && $type.length > 0 && $type[0].data.options.length > 0) {
                            return $type[0].data.options[parseInt(value) - 1].title;
                        }
                    }
                },
                { header: _lang.FlowCustomClearance.fReadyDate, dataIndex: 'readyDate', width: 140, },
                { header: _lang.FlowCustomClearance.fEtd, dataIndex: 'etd', width: 140, },
                { header: _lang.FlowCustomClearance.fEta, dataIndex: 'eta', width: 140, },
               // { header: _lang.FlowCustomClearance.fPackingDate, dataIndex: 'packingDate', width: 140, },
                // { header: _lang.FlowCustomClearance.fOrderNo, dataIndex: 'orderNo', width: 180, },
                // { header: _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 180, },
                // { header: _lang.FlowPurchaseContract.fSubject, dataIndex: 'subject', width: 180, },
                // { header: _lang.FlowOrderShippingPlan.fOriginPort, dataIndex: 'originPort', width: 120 ,
                //     renderer: function(value){
                //         var $loadingPort = _dict.origin;
                //         if(value  && $loadingPort.length > 0 && $loadingPort[0].data.options.length>0){
                //             return $loadingPort[0].data.options[parseInt(value) - 1].title;
                //         }
                //     }
                // },
                // { header: _lang.FlowOrderShippingPlan.fDestinationPort, dataIndex: 'destinationPort', width: 120,
                //     renderer: function(value){
                //         var $port = _dict.destination;
                //         if(value  && $port.length > 0 && $port[0].data.options.length>0){
                //             return $port[0].data.options[parseInt(value) - 1].title;
                //         }
                //     }
                // },
            ],// end of columns
            itemclick : function(obj, record, item, index, e){
                var win = Ext.getCmp(conf.winId);
                win.detailsGridPanel.getStore().removeAll();
                if(!!record.raw && !!record.raw) {
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

                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    if(conf.meGrid.getStore().getCount() > 0 ){
                        if(conf.meGrid.getStore().getAt(0).data.orderShippingPlanId != record.data.orderShippingPlanId){
                            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowWarehousePlanning.selectedSamePlanId);
                            return;
                        }
                    }

                    if(selStore.getCount()){
                        if (selStore.getAt(0).data.orderShippingPlanId != record.data.orderShippingPlanId) {
                            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowWarehousePlanning.selectedSamePlanId);
                            return;
                        }
                        for (var i = 0; i < selStore.getCount(); i++) {
                            if (selStore.getAt(i).data.containerNumber == record.data.containerNumber) {
                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedRecord);
                                return;
                            }
                        }
                    }
                    selStore.add(record.data);
                }else{
                    this.scope.winOk.call(this.scope);
                }
                if(!!this.scope.selectedData) {
                    this.scope.selectedData.push(record.raw);
                }
            },
            callback: function(obj, records){
                //初始化选择
                if(this.selectedId && records.length){
                    for(var i=0; i<records.length; i++){
                        if(records[i].data.containerNumber == this.selectedId){
                            console.log(records);
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
            title: _lang.FlowWarehousePlanning.tabPackingListOrder,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            width: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            rsort:false,
            collapsible : true,
            split : true,
            fields : ['id','containerNumber', 'orderShippingPlanId'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.FlowCustomClearance.fContainerNumber, dataIndex: 'containerNumber', width: 200, },
                { header: _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 200, hidden: true, },
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
            title: _lang.FlowWarehousePlanning.tabListOrder,
            id : conf.selectGridPanelId + '-details',
            scope : this,
            hidden : this.single ? true : false,
            height: 300,
            minHeight: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            split : true,
            forceFit: false,

            autoScroll: true,
            rsort: false,
            fields : ['id', 'sku', 'barcode', 'factoryCode', 'color', 'size', 'style','orderNumber',
                'orderQty', 'cartons', 'unitCbm', 'totalCbm', 'totalNw', 'totalGw',
                'orderTitle', 'orderIndex', 'category', 'inspection', 'serviceProviderCnName', 'serviceProviderEnName',
                'vendorCnName', 'vendorEnName','flagOrderQcStatus','receivedCartons','packingQty','packingCartons'
            ],
            columns : [
                { header: _lang.FlowCustomClearance.fOrderNumber, dataIndex: 'orderNumber', width: 120},
                { header: _lang.FlowCustomClearance.fOrderTitle, dataIndex: 'orderTitle', width: 200},
                { header: _lang.FlowPurchaseContract.fOrderIndex, dataIndex: 'orderIndex', width: 200},
                { header: _lang.ProductCategory.fCategory, dataIndex: 'category', width: 120, hidden: true, },
                { header: _lang.FlowWarehousePlanning.fInspection, dataIndex: 'flagOrderQcStatus', width: 200,
                    renderer: function(value){
                        var $orderQcStatus = _dict.orderQcStatus;
                        if(value  && $orderQcStatus.length > 0 && $orderQcStatus[0].data.options.length>0){
                            return $orderQcStatus[0].data.options[parseInt(value) - 1].title;
                        }
                    }
                },
                { header: _lang.FlowWarehousePlanning.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 260, hidden: true,},
                { header: _lang.FlowWarehousePlanning.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 260, hidden: true,},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260},
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260},
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'barcode', width: 200},
                { header: _lang.FlowCustomClearance.fFactoryCode, dataIndex: 'factoryCode', width: 200},
                { header: _lang.FlowCustomClearance.fColor, dataIndex: 'color', width: 40},
                { header: _lang.FlowCustomClearance.fSize, dataIndex: 'size', width: 40},
                { header: _lang.FlowCustomClearance.fStyle, dataIndex: 'style', width: 40},
                { header: _lang.FlowCustomClearance.fOrderQty, dataIndex: 'packingQty', width: 80},
                { header: _lang.FlowCustomClearance.fCartons, dataIndex: 'packingCartons', width: 80},
                //{ header:_lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'cartons', width: 80},
                { header: _lang.FlowCustomClearance.fUnitCbm, dataIndex: 'unitCbm', width: 60, hidden: true},
                { header: _lang.FlowCustomClearance.fTotalCbm, dataIndex: 'totalCbm', width: 60},
                { header: _lang.FlowCustomClearance.fTotalNw, dataIndex: 'totalNw', width: 60, hidden: true},
                { header: _lang.FlowCustomClearance.fTotalGw, dataIndex: 'totalGw', width: 60},
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
        // if(this.fieldValueName){
        //     var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
        //     var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
        //     if(ids){
        //         var arrIds = ids.split(',');
        //         var arrNames = names.split(',');
        //         var selStore = this.selectGridPanel.getStore();
        //         for(var i=0; i<arrIds.length; i++){
        //             if(curUserInfo.lang =='zh_CN'){
        //                 selStore.add({id: arrIds[i], sku: arrNames[i]});
        //             }else{
        //                 selStore.add({id: arrIds[i], sku: arrNames[i]});
        //             }
        //         }
        //     }
        // }
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
                names += rows[i].data.sku;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.containerNumber;
                names += rows.getAt(i).data.sku;
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