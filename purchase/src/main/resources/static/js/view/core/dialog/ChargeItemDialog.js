/**
 * ChargeItemDialog 字段触发
 * ChargeItemDialogWin　　弹出窗口
 * FlowFeeRegistrationMultiGrid  多选列表
 */
Ext.define('App.ChargeItemDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.ChargeItemDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.ChargeItemDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}

        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new ChargeItemDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            meForm: Ext.getCmp(this.formId),
            isFormField: true,
            subcallback: this.subcallback ? this.subcallback: '',
            callback:function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);

                if(this.subcallback){
                    this.subcallback.call(this, rows);
                }
            }}, false).show();
    }
});


ChargeItemDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowFeeRegistration.fFeeType;
        conf.moduleName = 'FlowFeeRegistration';
        conf.winId = 'ChargeItemDialogWinID';
        conf.mainGridPanelId = 'ChargeItemDialogWinGridPanelID';
        conf.searchFormPanelId= 'ChargeItemDialogWinSearchPanelID',
        conf.selectGridPanelId = 'ChargeItemDialogWinSelectGridPanelID';
        conf.treePanelId = 'ChargeItemDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'archives/product/list',
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        ChargeItemDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.FlowFeeRegistration.fFeeType,
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
            title: _lang.FlowFeeRegistration.tabChargeItem,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 1080,
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'id','sku','name',
                'barcode','categoryName',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty',
                'competitorPrice', 'competitor_sales_volume','ebayMonthlySales',
                'currency','priceAud','priceRmb', 'priceUsd',
                'creatorId','departmentId',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt'

            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},

                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 40,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 40,
                    renderer: function(value){
                        if(value == '1') return  _lang.NewProductDocument.vIndoor;
                        if(value == '2') return _lang.NewProductDocument.vOutdoor;
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 40,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 40},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                        if(value == '1') return  _lang.TText.vYes;
                        if(value == '2') return _lang.TText.vNo;
                    }
                },
                { header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 260 },
                { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200 },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', width: 80 },
                { header: _lang.NewProductDocument.fCompetitorPrice, dataIndex: 'competitorPrice', width: 60 },
                { header: _lang.NewProductDocument.fCompetitorSaleRecord, dataIndex: 'competitorSaleRecord', width: 60 },
                { header: _lang.NewProductDocument.fEbayMonthlySales, dataIndex: 'ebayMonthlySales', width: 60 },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60 },
                { header: _lang.TText.fAUD, dataIndex: 'priceAud', width: 60 },
                { header: _lang.TText.fRMB, dataIndex: 'priceRmb', width: 60 },
                { header: _lang.TText.fUSD, dataIndex: 'priceUsd', width: 60 },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns(),
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
                    //console.log(record.data);
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
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                };
            }
        });

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.FlowFeeRegistration.tSelected,
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
            fields : ['id','sku'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, sortable:false,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
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
            this.callback.call(this, ids, names, rows);
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

Ext.define('App.FlowFeeRegistrationMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowFeeRegistrationMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowFeeRegistration.mTitle,
            moduleName: 'FlowFeeRegistration',
            winId : 'FlowFeeRegistrationForm',
            frameId : 'FlowFeeRegistrationView',
            mainGridPanelId : 'FlowFeeRegistrationGridPanelID',
            mainFormPanelId : 'FlowFeeRegistrationFormPanelID',
            processFormPanelId: 'FlowFeeRegistrationProcessFormPanelID',
            searchFormPanelId: 'FlowFeeRegistrationSearchFormPanelID',
            mainTabPanelId: 'FlowFeeRegistrationMainTabsPanelID',
            subProductGridPanelId : 'FlowFeeRegistrationSubGridPanelID',
            formGridPanelId : 'FlowFeeRegistrationMultiGridPanelID',

        };

        this.initUIComponents(conf);

        App.FlowFeeRegistrationMultiGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            items: [ this.formGridPanel ]
        });
    },

    initUIComponents: function(conf){
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(260);
                this.formGridPanel.setHeight(258);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(698);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowFeeRegistration.tabChargeItem,
            forceFit: false,
            width: 'auto',
            height:250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            fields: [
                'id', 'businessId', 'project', 'receivedAud', 'expectedAud', 'currency', 'currency',
                'receivedRmb', 'expectedRmb', 'receivedUsd', 'expectedUsd', 'rateAudToUsd', 'rateAudToRmb', 'remark',
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 180, hidden: true, },

                {header: _lang.FlowFeeRegistration.fProject, dataIndex: 'project', width: 80, },

                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },

                {
                    header: _lang.FlowFeeRegistration.fPriceAUD,
                    columns: [
                        {header: _lang.FlowFeeRegistration.fReceivedAud, dataIndex: 'receivedAud', width: 80,},
                        {header: _lang.FlowFeeRegistration.fExpectedAud, dataIndex: 'expectedAud', width: 80},

                    ]
                },
                {
                    header: _lang.FlowFeeRegistration.fPriceRMB,
                    columns: [
                        {header: _lang.FlowFeeRegistration.fReceivedRmb, dataIndex: 'receivedRmb', width: 80,},
                        {header: _lang.FlowFeeRegistration.fExpectedRmb, dataIndex: 'expectedRmb', width: 80},

                    ]
                },
                {
                    header: _lang.FlowFeeRegistration.fPriceUSD,
                    columns: [
                        {header: _lang.FlowFeeRegistration.fReceivedUsd, dataIndex: 'receivedUsd', width: 80,},
                        {header: _lang.FlowFeeRegistration.fExpectedUsd, dataIndex: 'expectedUsd', width: 80},

                    ]
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 120},
            ]// end of columns
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ChargeItemDialogWin({
                    single:true,
                    fieldValueName: 'main_chargeItem',
                    fieldTitleName: 'main_chargeItemName',
                    selectedId : selectedId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new ChargeItemDialogWin({
                    single:false,
                    fieldValueName: 'main_chargeItem',
                    fieldTitleName: 'main_chargeItemName',
                    selectedId : '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    this.meGrid.getStore().add(items[index].raw);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});
