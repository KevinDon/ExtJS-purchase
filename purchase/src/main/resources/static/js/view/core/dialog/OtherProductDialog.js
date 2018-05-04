Ext.define('App.OtherProductDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.OtherProductDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.OtherProductDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}

        var selectedId = '';
        var form = Ext.getCmp(this.formId);
        if(form && this.hiddenName && form.getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new OtherProductDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            productType: this.productType ? this.productType : '',
            vendorId: this.vendorId ? this.vendorId : '',
            filterVendor: this.filterVendor ? this.filterVendor : false,
            fieldVendorIdName: this.fieldVendorIdName ? this.fieldVendorIdName : '',
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            subcallback: this.subcallback ? this.subcallback: '',
            callback:function(ids, titles, rows) {
                if(this.fieldValueName) {
                    this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                    this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                }

                if(this.subcallback){
                    this.subcallback.call(this, rows);
                }
            }}, false).show();
    }
});


OtherProductDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.ProductDocument.tSelector;
        conf.moduleName = 'ProductCombination';
        conf.winId = 'OtherProductDialogWinID';
        conf.mainGridPanelId = 'OtherProductDialogWinGridPanelID';
        conf.searchFormPanelId= 'OtherProductDialogWinSearchPanelID';
        conf.selectGridPanelId = 'OtherProductDialogWinSelectGridPanelID';
        conf.treePanelId = 'OtherProductDialogWinTreePanelId';

        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        if(conf.productType == '3'){
            //采购计划下的产品
            conf.urlList = __ctxPath + 'archives/flow/purchase/plandetail/list?isview=' + conf.isview;
        }else if(conf.productType == '5'){
            //询价申请通过的产品
            conf.urlList = __ctxPath + 'archives/flow/productquotation/list';
        }else{
            //产品
            conf.urlList = __ctxPath + 'archives/product/listProduct';
        }

        //filter product by vendorId
        if(!!conf.filterVendor){
            var cmp = conf.meForm.getCmpByName(conf.fieldVendorIdName);
            if(!!cmp){
                if(conf.urlList.indexOf("?")>0){
                    conf.urlList += '&vendorId=' + cmp.getValue();
                }else {
                    conf.urlList += '?vendorId=' + cmp.getValue();
                }
            }
        }
        if(!!conf.vendorId){
            if(conf.urlList.indexOf("?")>0){
                conf.urlList += '&vendorId=' + conf.vendorId;
            }else {
                conf.urlList += '?vendorId=' + conf.vendorId;
            }
        }

        Ext.applyIf(this, conf);
        this.initUI(conf);

        OtherProductDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title,
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
            onlyKeywords: conf.productType == '5'? false : true,
            fieldItems:[
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
                {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
                {field:'Q-departmentId-S-EQ', xtype:'hidden'},
                {field:'departmentName', xtype:'DepDialog', title:_lang.TText.fDepartmentName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
                },
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},

            ]

        });// end of searchPanel

        var $contextMenu = [
            Ext.create('Ext.Action', {
                text: _lang.TText.tSelectAll,
                scope: this,
                handler: function(e, pt){
                    var selectItems = this.gridPanel.getSelectionModel().selected.items;
                    if(!conf.single && selectItems.length>0){
                        var selStore = this.selectGridPanel.getStore();
                        for(var j= 0; j<selectItems.length; j++ ) {
                            if (selStore.getCount()) {
                                var wantAdd = true;
                                for (var i = 0; i < selStore.getCount(); i++) {
                                    if (selStore.getAt(i).data.id == selectItems[j].data.id) {
                                        wantAdd =  false;
                                        break;
                                    }
                                }

                                if(wantAdd){
                                    selStore.add(selectItems[j].data);
                                    if(!!this.selectedData) {
                                        this.selectedData.push(selectItems[j]);
                                    }
                                }

                            }else{
                                selStore.add(selectItems[j].data);
                                if(!!this.selectedData) {
                                    this.selectedData.push(selectItems[j]);
                                }
                            }
                        }
                    }
                },
                cls: 'fa fa-fw fa-check',
                itemId: 'ActProduct5-SelectAll'
            })
        ];

        this.selectedData = [];
        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title: conf.title,
            scope: this,
            forceFit: false,
            rsort:false,
            border: false,
            autoScroll: true,
            multiSelect:  !conf.single ? true : false,
            contextMenu: !conf.single ? $contextMenu : null,
            url : conf.urlList,
            fields: [
                'sku', 'vendorId','product','currency','flowPurchasePlanId', 'purchasePlanId',
                'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty',
                'competitorPrice', 'competitor_sales_volume','ebayMonthlySales',
                'availableQty','alreadyOrderQty','orderQty','totalOrderQty','priceAud','priceRmb','priceUsd',
                {name: 'barcode', mapping: 'product.barcode'},
                {name: 'newProduct', mapping: 'product.newProduct'},
                {name: 'name', mapping: 'product.name'},
                {name: 'categoryName', mapping: 'product.categoryName'},
                {name: 'seasonal', mapping: 'product.seasonal'},
                {name: 'indoorOutdoor', mapping: 'product.indoorOutdoor'},
                {name: 'powerRequirements', mapping: 'product.powerRequirements'},
                {name: 'mandatory', mapping: 'product.mandatory'},
                {name: 'status', mapping: 'product.status'},
                {name: 'sort', mapping: 'product.sort'},
                {name: 'creatorId', mapping: 'product.creatorId'},
                {name: 'creatorCnName', mapping: 'product.creatorCnName'},
                {name: 'creatorEnName', mapping: 'product.creatorEnName'},
                {name: 'createdAt', mapping: conf.productType == '2' ||  conf.productType == '5'?  'createdAt': 'product.createdAt'},
                {name: 'updatedAt', mapping: conf.productType == '2' ||  conf.productType == '5'?  'updatedAt': 'product.updatedAt'},
                {name: 'departmentId', mapping: 'product.departmentId'},
                {name: 'departmentCnName', mapping: 'product.departmentCnName'},
                {name: 'departmentEnName', mapping: 'product.departmentEnName'},
                {name: 'electricalProduct', mapping: 'product.electricalProduct'},
                {name: 'vendorCnName', mapping: 'product.prop.vendorCnName'},
                {name: 'vendorEnName', mapping: 'product.prop.vendorEnName'},
                {name: 'productParameter', mapping: '.product.prop.productParameter'},
                {name: 'productDetail', mapping: 'product.prop.productDetail'},
                {name: 'productLink', mapping: 'product.prop.productLink'},
                {name: 'keywords', mapping: 'product.prop.keywords'},
            ],
            columns: [
                { header: _lang.FlowPurchasePlan.fFlowPurchasePlanId, dataIndex: 'flowPurchasePlanId', width: 180,  hidden: conf.productType == '3' ? false: true},
                { header: _lang.FlowPurchasePlan.fPurchasePlanId, dataIndex: 'purchasePlanId', width: 180,  hidden: conf.productType == '3' ? false: true},
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 180},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 180, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 180 },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 160},
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 , 
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 80 ,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowPurchasePlan.fOrderQty, dataIndex: 'orderQty', width: 80, hidden: conf.productType == '3' ? false: true,},
                { header: _lang.FlowPurchasePlan.fAlreadyOrderQty, dataIndex: 'alreadyOrderQty', width: 80,  hidden: conf.productType == '3' ? false: true,
                    rerender: function (value) {
                        value = value == null ? 0: value;
                        return value;
                    }
                },

                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 80},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fVendorName, dataIndex: 'vendorCnName', width: 200,hidden:curUserInfo.lang == 'zh_CN' ? false: true, },
                { header: _lang.NewProductDocument.fVendorName, dataIndex: 'vendorEnName', width: 200,hidden:curUserInfo.lang == 'zh_CN' ? true: false, },

                { header: _lang.TText.fAUD, dataIndex: 'priceAud', width: 68 },
                { header: _lang.TText.fRMB, dataIndex: 'priceRmb', width: 68 },
                { header: _lang.TText.fUSD, dataIndex: 'priceUsd', width: 68 },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({assignee:false,sort:false,}),
            itemdblclick : function(obj, record, item, index, e, eOpts){
                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    if(selStore.getCount()){
                        for (var i = 0; i < selStore.getCount(); i++) {
                            //采购计划ID
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
                if(!!this.scope.selectedData) {
                    this.scope.selectedData.push(record);
                }
            },
            callback : function(obj, records){
                //初始化选择
                if(this.selectedId && records.length){
                    for(var i=0; i<records.length; i++){
                        if(records[i].data.id == this.selectedId){
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                };
            }
        });


        var $contextMenu = [
            Ext.create('Ext.Action', {
                text: _lang.TText.tSelectCancel,
                scope: this,
                handler: function(){ this.selectGridPanel.getStore().removeAll(); this.selectedData=[] },
                cls: 'fa fa-fw fa-eraser',
                itemId: 'ActProduct5-SelectCancel'
            })
        ];

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.ProductDocument.tSelected,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            width: 150,
            minWidth: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            contextMenu: !this.single ? $contextMenu: '',
            split : true,
            fields : ['id','sku'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, sortable:false,},
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

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,

            items: [ this.searchPanel, this.gridPanel]
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
                names += rows[i].data.sku;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
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