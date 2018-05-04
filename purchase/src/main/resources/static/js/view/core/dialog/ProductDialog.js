Ext.define('App.ProductDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.ProductDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.ProductDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}

        var selectedId = '';
        var form = Ext.getCmp(this.formId);
        if(form && this.hiddenName && form.getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new ProductDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            isFormField: true,
            productType: this.productType ? this.productType : 2,
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


ProductDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        // this.listeners = {
        //     beforeshow: function( e, eOpts ){
        //         if(!conf.meForm.getCmpByName(conf.fieldVendorIdName).getValue()){
        //             Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsVendorIdError);
        //             return false;
        //         }
        //     }
        // };

        conf.title = _lang.ProductDocument.mTitle;
        conf.moduleName = 'ProductCombination';
        conf.winId = 'ProductDialogWinID';
        conf.mainGridPanelId = 'ProductDialogWinGridPanelID';
        conf.searchFormPanelId= 'ProductDialogWinSearchPanelID';
        conf.selectGridPanelId = 'ProductDialogWinSelectGridPanelID';
        conf.treePanelId = 'ProductDialogWinTreePanelId';
        conf.status = conf.status  || 1;
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.close = true;
        conf.okFun = this.winOk;

        if(conf.productType == '1'){
            //新品
            conf.urlList = __ctxPath + 'archives/newproduct/list?status='+ conf.status;
        }else if(conf.productType == '2'){
            //all product
            conf.urlList = __ctxPath + 'archives/product/list?status=' + conf.status;
        }else if(conf.productType == '3') {
            //采购计划下的产品
            conf.urlList = __ctxPath + 'archives/flow/purchase/plandetail/list?isview=' + conf.isview;
        }else if(conf.productType == '4') {
            //样品申请通过的产品
            conf.urlList = __ctxPath + 'archives/flow/purchase/sample/listfordialog';
        }else if(conf.productType == '5') {
            //询价申请通过的产品
            conf.urlList = __ctxPath + 'archives/flow/productquotation/list';
        }else{
            //正式产品
            conf.urlList = __ctxPath + 'archives/product/listProduct?status=' + conf.status;
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
        }else if(conf.vendorId){
            if(conf.urlList.indexOf("?")>0){
                conf.urlList += '&vendorId=' + conf.vendorId;
            }else {
                conf.urlList += '?vendorId=' + conf.vendorId;
            }
        }


        Ext.applyIf(this, conf);
        this.initUI(conf);

        ProductDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductDocument.mTitle,
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
            fieldItems:[
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-name-S-LK', xtype:'textfield', title:_lang.ProductDocument.fName},
                {field:'Q-barcode-S-LK', xtype:'textfield', title:_lang.ProductDocument.fBarcode},
                {field:'Q-color-S-LK', xtype:'textfield', title:_lang.ProductDocument.fColor},
                {field:'Q-model-S-LK', xtype:'textfield', title:_lang.ProductDocument.fModel},
                {field:'Q-style-S-LK', xtype:'textfield', title:_lang.ProductDocument.fStyle},
                {field:'Q-categoryId-S-EQ', xtype:'hidden'},
                {field:'CategoryName', xtype:'ProductCategoryDialog', title:_lang.ProductDocument.fCategoryId,
                    formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
                },
                {field:'Q-newProduct-N-EQ', xtype:'combo', title:_lang.ProductDocument.fNewProduct, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
                },
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll],['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                },
                {field:'Q-combined-N-EQ', xtype:'combo', title:_lang.ProductDocument.fCombined, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.ProductDocument.vDisabled], ['1', _lang.ProductDocument.vEnabled]]
                },
                {field:'Q-mandatory-N-EQ', xtype:'combo', title:_lang.ProductDocument.fMandatory, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.ProductDocument.vDisabled], ['1', _lang.ProductDocument.vEnabled]]
                },
                {field:'Q-electricalProduct-N-EQ', xtype:'combo', title:_lang.ProductDocument.fElectricalProduct, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
                },
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
            title: _lang.ProductDocument.tabListTitle,
            scope: this,
            forceFit: false,
            border: false,
            multiSelect:  !conf.single ? true : false,
            contextMenu: !conf.single ? $contextMenu : null,
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'id','sku','name','barcode','categoryName','newProduct',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements', 'mandatory',
                'vendorId','product',
                'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty',
                'competitorPrice', 'competitor_sales_volume','ebayMonthlySales',
                'creatorId','departmentId','prop.currency',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt', 'prop.keywords','prop.vendorCnName',
                'prop.vendorEnName','prop.productParameter','prop.productDetail','prop.productLink',
                'prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd', 'prop.riskRating',

                {name: 'priceAud', mapping: 'prop.targetBinAud'},
                {name: 'priceRmb', mapping:'prop.targetBinRmb' },
                {name: 'priceUsd', mapping: 'prop.targetBinUsd'},

            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120,sortable:false},
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 ,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'prop.riskRating',align: 'center', width: 100,
                    renderer: function(value){
                        var $riskRating = _dict.riskRating;
                        if(value  && $riskRating.length > 0 && $riskRating[0].data.options.length>0){
                            return !! $riskRating[0].data.options[parseInt(value) - 1] ? $riskRating[0].data.options[parseInt(value) - 1].title: '';
                        }
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
                { header: _lang.NewProductDocument.fVendorName, dataIndex: 'prop.vendorCnName', width: 200,hidden:curUserInfo.lang == 'zh_CN' ? false: true, },
                { header: _lang.NewProductDocument.fVendorName, dataIndex: 'prop.vendorEnName', width: 200,hidden:curUserInfo.lang == 'zh_CN' ? true: false, },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'prop.productLink', width: 200 },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'prop.keywords', width: 100 },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'prop.currency', width: 80 ,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fAUD, dataIndex: 'priceAud', width: 68,sortable:false },
                { header: _lang.TText.fRMB, dataIndex: 'priceRmb', width: 68,sortable:false },
                { header: _lang.TText.fUSD, dataIndex: 'priceUsd', width: 68,sortable:false },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({assignee:false, sort:false}),
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
            contextMenu: !this.single ? $contextMenu: '',
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
                var selectedData = [];
                for(var i = 0; i < this.scope.selectedData.length; i++){
                    var data = this.scope.selectedData[i];
                    if(data.raw.id != record.data.id) {
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


Ext.define('App.ProductFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductFormMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title: this.fieldLabel,
            moduleName: 'Product',
            fieldValueName: this.valueName || 'main_product',
            fieldTitleName: this.titleName || 'main_productName'
        };
        conf.mainGridPanelId= this.mainGridPanelId;
        conf.mainFormPanelId= this.mainFormPanelId;
        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-ProductFormMultiGrid' + Ext.id();
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-ProductFormMultiForm' + Ext.id();
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 260;
        conf.noTitle = this.noTitle || false;
        conf.productType = this.productType || 1;
        conf.dispalayType = this.dispalayType || 1;
        conf.dataChangeCallback= this.dataChangeCallback || '',
        conf.itemClickCallback= this.itemClickCallback || '',

        this.initUIComponents(conf);

        App.ProductFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ],
        });
    },

    vendorId: '',

    setValue:function (values) {
        var cmpAtta = this.formGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(var i = 0; i < values.length; i++){
                var product = {};
                Ext.applyIf(product, values[i].product);
                Ext.applyIf(product, values[i].product.prop);
                product.prevRiskRating = values[i].product.prop.riskRating;
                cmpAtta.add(product);
            }
        }
    },

    initUIComponents: function(conf){
        var me = this;

        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight-3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id','sku','name','barcode','categoryName',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty','factoryCode',
                'currency','priceAud','priceRmb','priceUsd','creatorId','creatorName','departmentId','departmentName',
                'rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd',
                'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd','ebayMonthlySales',
                'competitorPriceAud','competitorPriceRmb','competitorPriceUsd','competitorSaleRecord','mandatory',
                'productPredictProfitRmb','creatorName','departmentName','riskRating'
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
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 150, locked: true},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 160, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 160, hidden: true },
                { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'riskRating',align: 'center', width: 100, hidden: !conf.dispalayType==1,
                    renderer: function(value){
                        var $riskRating = _dict.riskRating;
                        if(value  && $riskRating.length > 0 && $riskRating[0].data.options.length>0){
                            return !! $riskRating[0].data.options[parseInt(value) - 1] ? $riskRating[0].data.options[parseInt(value) - 1].title: '';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //竞争对手报价
                { header: _lang.NewProductDocument.fCompetitorPrice, width: 100,
                    columns: new $groupPriceColumns(this, 'competitorPriceAud','competitorPriceRmb','competitorPriceUsd', null, {edit:false, gridId: conf.formGridPanelId})
                },
                //对手销量
                { header: _lang.NewProductDocument.fCompetitorSaleRecord, dataIndex: 'competitorSaleRecord', width: 100,},

                //ebay月销售额
                { header: _lang.NewProductDocument.fEbayMonthlySales,  width: 80,
                    columns: new $groupPriceColumns(this, 'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd', null,{edit:false, gridId: conf.formGridPanelId})
                },


                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 70,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 60},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fFactoryCode, dataIndex: 'factoryCode', width: 260},
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data['productLink'];
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },


                { header: _lang.NewProductDocument.fCreator, dataIndex: 'creatorName', width: 100 },
                { header: _lang.NewProductDocument.fDepartmentId, dataIndex: 'departmentName', width: 100 }
            ]// end of columns
        });

        if(!!conf.dataChangeCallback) {
            this.formGridPanel.store.on('dataChanged', function (store) {
                conf.dataChangeCallback.call(me.scope, store);
            });
        }

        if(!!conf.itemClickCallback) {
            this.formGridPanel.on('itemclick', function (grid, rowIndex, columnIndex, e) {
                conf.itemClickCallback.call(me.scope, grid, rowIndex, columnIndex);
            });
        }

    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;

        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    selectedId : selectedId,
                    vendorId: this.vendorId,
                    productType: conf.productType || '1',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.subGridPanelId).store.remove(record);
                break;

            default :
                new ProductDialogWin({
                    single:false,
                    selectedId : '',
                    vendorId: this.vendorId,
                    productType: conf.productType || '1',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(result.data.items.length>0){
                            var items = selectedData;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    var product = {};
                                    Ext.applyIf(product, items[index].raw);
                                    Ext.apply(product, items[index].raw.prop);
                                    product.sku =  items[index].raw.sku;
                                    product.id =  items[index].raw.id;
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});
