Ext.define('App.ProductProblemFormProductGird', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductProblemFormProductGird',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductProblem.mTitle,
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormProductWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            dataChangeCallback: this.dataChangeCallback,
            itemClickCallback: this.itemClickCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.ProductProblemFormProductGird.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-product',
            minHeight: 200,
            height: 250, width: 'auto',
            bodyCls:'x-panel-body-gray',
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
            id: conf.subGridPanelId + '-product',
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            url: __ctxPath + 'archives/new-product/list',
            bbar: false,
            // tools: tools,
            autoLoad: false,
            header:{ cls:'x-panel-header-gray' },
            rsort: false,
            fields: [
                'orderId','orderNumber','orderNumber', 'productId', 'sku' , 'combined','orderIndex','jobNo',
                'productName', 'categoryId', 'categoryName', 'sellQty','riskRating', 'quotationCurrency',
                'rateAudToRmb', 'rateAudToUsd', 'quotationPriceAud','quotationPriceRmb','quotationPriceUsd',
                'length','width','height','netWeight','cubicWeight','cbm'
            ],
            columns: [
                { header: 'ID', dataIndex: 'orderId', width: 40, hidden: true },
                { header: _lang.FlowDepositContract.fOrderNumber, dataIndex: 'jobNo', width: 90, },
                { header: _lang.ProductCertificate.fProductId, dataIndex: 'productId', width: 90, hidden: true, },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductProblem.fSellQty, dataIndex: 'sellQty', width: 80},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80},
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryId', width: 80, hidden: true, },
                { header: _lang.ProductDocument.fCategoryName, dataIndex: 'categoryName', width: 120},
                // { header: _lang.FlowPurchaseContract.fOrderIndex, dataIndex: 'orderIndex', width: 120},
                { header: _lang.FlowCustomClearance.fProductName, dataIndex: 'productName', width: 120},
                { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'riskRating',align: 'center', width: 100,
                    renderer: function(value){
                        var $riskRating = _dict.riskRating;
                        if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'quotationCurrency',
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'quotationRateAudToRmb', 'quotationRateAudToUsd')},


                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'quotationPriceAud','quotationPriceRmb','quotationPriceUsd', null,{edit:false, gridId:conf.mainTabPanelId+'-0',})
                },
                {header: _lang.ProductDocument.tabProductSize,
                    columns: [
                        {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 80,},
                        {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 80,},
                        {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 80,},
                        { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm' ,width: 80,  },
                        { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 80,  },
                        { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight' ,width: 80, },
                    ],
                },
            ],// end of columns
        });

        this.formGridPanel.store.on('dataChanged', function (store) {
            if(!!conf.dataChangeCallback) {
                conf.dataChangeCallback.call(conf.scope, store, conf);
            }
        });

        this.formGridPanel.on('itemclick', function(grid, rowIndex, columnIndex, e) {
            if(!!conf.itemClickCallback) {
                conf.itemClickCallback.call(conf.scope, grid, rowIndex, columnIndex);
            }
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
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
                new ProductDialogWin({
                    single:false,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    selectedId : '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(!!result.data && !!result.data.items && result.data.items.length>0){
                            var items = result.data.items;
                            for(var index in items){
                                this.meGrid.getStore().add(items[index].raw);
                            }
                        }
                    }}, false).show();

                break;
        }
    },
});

Ext.define('App.ProductProblemFormASNGird', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductProblemFormASNGird',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            //title : _lang.ProductProblem.mTitle,
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
        };

        this.initUIComponents(conf);

        App.ProductProblemFormASNGird.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-ASN',
            minHeight: 200,
            height: 250, width: 'auto',
            bodyCls:'x-panel-body-gray',
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
            id: conf.subGridPanelId + '-ASN',
            //title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            url: __ctxPath + 'archives/new-product/list',
            bbar: false,
            // tools: tools,
            autoLoad: false,
            rsort: false,
            fields: [ 'orderId','orderNumber','orderNumber', 'productId', 'sku' , 'combined',
                'productName', 'categoryId', 'sellQty',
            ],
            columns: [
                { header: 'ID', dataIndex: 'orderId', width: 40, hidden: true },
                { header: _lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 90},
                { header: _lang.ProductCertificate.fProductId, dataIndex: 'productId', width: 90, hidden: true, },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 40},
                { header: _lang.FlowCustomClearance.fProductName, dataIndex: 'productName', width: 200},
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryId', width: 80, hidden: true, },
                { header: _lang.ProductProblem.fCategoryCnName, dataIndex: 'categoryCnName', width: 100},
                { header: _lang.ProductProblem.fCategoryEnName, dataIndex: 'categoryEnName', width: 120},
                { header: _lang.ProductProblem.fSellQty, dataIndex: 'sellQty', width: 60},
            ],// end of columns
        });

    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
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
                new ProductDialogWin({
                    single:false,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    selectedId : '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                this.meGrid.getStore().add(items[index].raw);
                            }
                        }
                    }}, false).show();

                break;
        }
    },
});

Ext.define('App.ProductProblemFormTicketTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ProductProblemFormTicketTabs',

    resourceData : [],
    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
        };


        App.ProductProblemFormTicketTabs.superclass.constructor.call(this, {
            id: this.mainTabPanelId + '-form',
            title: _lang.ReportProductCompliance.fCheckDetails,
            region: 'south',
            plain: this.plain != undefined ? this.plain: false,
            split: this.split != undefined? this.split: true,
            scope: this,
            height: this.height !=undefined? this.height: '32%',
            minHeight: 150,
            autoHeight: true,
            autoScroll : false,
            autoRender:true,
            width: 'auto',
            bodyCls:'x-panel-body-gray',
            cls:'sub-tabs x-panel-tabs-gray',
            listeners : {
                'tabchange' : function(tab, newc, oldc) {
                    if(!tab.mainFormPanelId || !tab.retaleGridName) return;
                    var cmp = Ext.getCmp(tab.mainFormPanelId).getCmpByName(tab.retaleGridName).formGridPanel;
                    if(cmp.getStore().data.getCount()>0){
                        var records = cmp.getStore().data.items;
                        for(var i=0; i< cmp.getStore().data.getCount(); i++){
                            if(records[i].data.sku == newc.title){
                                cmp.getSelectionModel().select(records[i], true);
                            }
                        }
                    }
                }
            }
        });
    },

    addTab: function(sku, productId, initData, index){
        var grid = new HP.GridPanel({
            id: sku + index,
            // height: this.defHeight-3,
            height: 500,
            title: sku,
            url: __ctxPath + 'dict/getkey?code=' + this.code,
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: true,
            groupField: 'title',
            forceFit : true,
            features: [{ftype:'grouping'}],
            fields: ['id', 'codeMain', 'codeSub', 'troubleTicketId', 'troubleDetailId', 'active', 'options', 'title', 'subTitle','qty'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fTroubleTicketId, dataIndex: 'troubleTicketId', width: 80, hidden: true, sortable:false},
                { header: _lang.ProductProblem.fTroubleTicketId, dataIndex: 'troubleDetailId', width: 80, hidden: true, sortable:false,},
                { header: _lang.Dictionary.fCodeMain, dataIndex: 'codeMain', width: 120, hidden: true, sortable:false },
                { header: _lang.Dictionary.fCodeSub, dataIndex: 'codeSub', width: 140, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fTroubleCategoryId, dataIndex: 'title', width: 100, sortable:false},
                { header: _lang.ProductProblem.fTroubleDetailId, dataIndex: 'subTitle', width: 300, flex : 3, sortable:false,},
                { header: _lang.ProductProblem.fTroubleQty, dataIndex: 'qty', width: 80, sortable:false, scope:this,
                    editor: { xtype: 'numberfield', minValue: 1, value: 1, },
                },
                { header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false,
                    listeners: {
                        checkchange: function(me, recordIndex, checked){
                            var row = Ext.getCmp(sku + index).getStore().getAt(recordIndex);
                            if(checked){
                                if(!row.get('qty')){
                                    row.set('qty', 1);
                                }
                            }else{
                                row.set('qty', '');
                            }
                        }
                    },
                    renderer:function(value, meta, rec){
                        return $renderOutputCheckColumns(value, meta)
                    }
                }
            ], // end of columns
        });

        $HpStore({
            fields: ['id', 'codeMain', 'codeSub', 'troubleTicketId', 'troubleDetailId', 'troubleQty', 'active', 'options', 'title', 'subTitle'],
            url: __ctxPath + 'dict/getkey?code=' + this.code, loadMask: true, scope: this,
            callback: function (conf, records, eOpts) {
                grid.getStore().removeAll();
                for (var index in records) {
                    var row = {};

                    row.codeMain = records[index].data.codeMain;
                    row.codeSub = records[index].data.codeSub;
                    row.title = records[index].data.title;

                    if (records[index].data.options.length > 0) {
                        var options = records[index].data.options;
                        for (var i in options) {
                            row.troubleDetailId = options[i].value;
                            row.subTitle = options[i].title;
                            if(!!initData){
                                    var active = false;
                                    var qty = '';
                                     for(var j in initData){//初始化时后台返回的数据，二维数组
                                         for(var k in initData[j]){
                                             if(initData[j][k].sku == grid.title){
                                                 if(initData[j][k].codeSub == row.codeSub){
                                                     if(initData[j][k].troubleDetailId == row.troubleDetailId){
                                                         qty = initData[j][k].qty;
                                                         active = true;
                                                     }
                                                 }
                                             }
                                         }
                                     }
                            }
                            row.active = active;
                            row.qty = qty;
                            grid.getStore().add(row);
                        }
                    }

                }
            }
        });

        //add tab
        var cmpPanel = Ext.getCmp(this.mainTabPanelId + '-form');
        cmpPanel.add(grid);
    },

    updateTabs: function(skus, productIds, data){
        var cmpPanel = Ext.getCmp(this.mainTabPanelId + '-form');
        var tabsToDelete = [];
        var tabs = cmpPanel.items.items;
        for(var i = 0; i < tabs.length; i++){
            var tab = tabs[i];
            var index = skus.indexOf(tab.title);
            if(index < 0) tabsToDelete.push(tab);
            else{
                skus.splice(index, 1);
                productIds.splice(index, 1);
            }
        }
        for(var i = 0; i < tabsToDelete.length; i++) cmpPanel.remove(tabsToDelete[i], true);
        for(var i = 0; i < skus.length; i++){
            this.addTab(skus[i], productIds[i], data, i);
        }
        this.setActiveTab(0);
    },


    setActiveTabBySku: function(sku){
        var cmpPanel = Ext.getCmp(this.mainTabPanelId + '-form');
        var tabsToDelete = [];
        var tabs = cmpPanel.items.items;
        for(var i = 0; i < tabs.length; i++){
            var tab = tabs[i];
            if(tab.title == sku) {
                cmpPanel.setActiveTab(tab);
                break;
            }
        }
    }
});

Ext.define('App.ProductProblemFormRemarkGird', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductProblemFormRemarkGird',

    resourceData : [],
    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormRemarkGirdWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            urlList: __ctxPath + 'archives/troubleTicket/remark/list',
            urlSave: __ctxPath + 'archives/troubleTicket/remark/update',
            urlDelete: __ctxPath + 'archives/troubleTicket/remark/delete',
            urlGet: __ctxPath + 'archives/troubleTicket/remark/get',
        };

        conf.defHeight = this.defHeight ? this.defHeight: 300;
        this.initUIComponents(conf);

        App.ProductProblemFormRemarkGird.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-Remark',
            minHeight: 250,
            height: conf.defHeight, width: 'auto',
            bodyCls:'x-panel-body-gray',
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
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight -3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            id: conf.subGridPanelId + '-Remark',
            height:conf.defHeight-3,
            //url: conf.urlList,
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: true,
            forceFit : true,
            fields: ['id', 'remark','createdAt','createdAt','creatorId','creatorCnName', 'creatorEnName'],
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
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fRemarkTitle, dataIndex: 'remark', width: 500, sortable:false},
                { header: _lang.TText.fCreatorId, dataIndex: 'creatorId', width: 60, hidden:true },
                { header: _lang.Flow.fOperator, dataIndex: 'creatorCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.Flow.fOperator, dataIndex: 'creatorEnName', width: 100, hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 130, sortable:false},
                { header: _lang.TText.fUpdatedAt, dataIndex: 'createdAt', width: 130, sortable:false},
            ],// end of columns
        });


    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                conf.selectedId = record.data.id;
                new ProductProblemFormRemarkForm(conf).show();
                break;

            case 'btnRowRemove' :
                var tpData = $HpStore({
                    fields: ['id'],
                    url: conf.urlDelete + '?id=' + record.data.id,
                    callback: function (conf, records, eOpts) {
                        //console.log('success');
                    }
                });
                Ext.getCmp(conf.subGridPanelId + '-Remark').store.remove(record);
                break;

            default :
                break;
        }
    },
});


Ext.define('App.ProductProblemFormCustomerRemarkGird', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductProblemFormCustomerRemarkGird',

    resourceData : [],
    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormCustomerRemarkGirdWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            urlList: __ctxPath + 'archives/troubleTicket/remark/list',
            urlSave: __ctxPath + 'archives/troubleTicket/remark/update',
            urlDelete: __ctxPath + 'archives/troubleTicket/remark/delete',
            urlGet: __ctxPath + 'archives/troubleTicket/remark/get',
        };

        conf.defHeight = this.defHeight ? this.defHeight: 300;
        this.initUIComponents(conf);

        App.ProductProblemFormRemarkGird.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-customerRemark',
            minHeight: 250,
            height: conf.defHeight, width: 'auto',
            bodyCls:'x-panel-body-gray',
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
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight -3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            id: conf.subGridPanelId + '-customerRemark',
            height:conf.defHeight-3,
            //url: conf.urlList,
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: true,
            forceFit : true,
            fields: ['id', 'remark','createdAt','createdAt','creatorId','creatorCnName', 'creatorEnName'],
            columns: [

                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fRemarkTitle, dataIndex: 'remark', width: 500, sortable:false},
                { header: _lang.TText.fCreatorId, dataIndex: 'creatorId', width: 60, hidden:true },
                { header: _lang.TText.fCreatorName, dataIndex: 'creatorCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fCreatorName, dataIndex: 'creatorEnName', width: 100, hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 130, sortable:false},
                { header: _lang.TText.fUpdatedAt, dataIndex: 'createdAt', width: 130, sortable:false},
            ],// end of columns
        });


    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                conf.selectedId = record.data.id;
                new ProductProblemFormRemarkForm(conf).show();
                break;

            case 'btnRowRemove' :
                var tpData = $HpStore({
                    fields: ['id'],
                    url: conf.urlDelete + '?id=' + record.data.id,
                    callback: function (conf, records, eOpts) {
                        //console.log('success');
                    }
                });
                Ext.getCmp(conf.subGridPanelId + '-Remark').store.remove(record);
                break;

            default :
                break;
        }
    },
});
