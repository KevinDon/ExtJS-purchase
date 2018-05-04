Ext.define('App.FlowNewProductFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowNewProductFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowNewProduct',
            winId : 'FlowNewProductViewForm',
            frameId : 'FlowNewProductView',
            mainGridPanelId : 'FlowNewProductViewGridPanelID',
            mainFormPanelId : 'FlowNewProductViewFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'FlowNewProductViewSearchFormPanelID',
            mainTabPanelId: 'FlowNewProductViewMainTbsPanelID',
            subGridPanelId : 'FlowNewProductViewSubGridPanelID',
            formGridPanelId : 'FlowNewProductFormGridPanelID'
        };

        this.initUIComponents(conf);

        App.FlowNewProductFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ],
        });
    },

    initUIComponents: function(conf){
        var tools = [{
            type:'collapse', tooltip: _lang.TButton.query, scope: this, hidden:this.readOnly,
                handler: function(event, toolEl, panelHeader) {
                    this.conf = conf;
                    this.onQueryRowAction.call(this);
            }},{
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
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
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
                'id','sku','name','barcode','categoryName','prop','newProduct','productQuotationId','productQuotationBusinessId',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty',
                'competitorSaleRecord','ebayMonthlySales', 'factoryCode',
                'currency','priceAud','priceRmb','priceUsd','creatorId','creatorName','departmentId','departmentName',
                'rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd',
                'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd',
                'competitorPriceAud','competitorPriceRmb','competitorPriceUsd','mandatory',
                'productPredictProfitRmb',
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
                { header:  _lang.ProductDocument.fProductId,  dataIndex: 'id', width: 40, hidden: true },
                { header:  _lang.FlowNewProduct.fProductQuotationId,  dataIndex: 'productQuotationId', width: 40, hidden: true },
                { header:  _lang.FlowNewProduct.fProductQuotationBusinessId,  dataIndex: 'productQuotationBusinessId', width: 40, hidden: true },
                { header:  _lang.FlowNewProduct.fProductQuotationDetailBusinessId,  dataIndex: 'productQuotationDetailBusinessId', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 ,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120, locked: true},
                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //报价
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('orderValueAud', (row.get('priceAud') * row.get('orderQty')).toFixed(3));
                        row.set('orderValueRmb', (row.get('priceRmb') * row.get('orderQty')).toFixed(3));
                        row.set('orderValueUsd', (row.get('priceUsd') * row.get('orderQty')).toFixed(3));
                    },{edit: false, gridId: conf.formGridPanelId})
                },
                //采购数量
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', scope:this, width: 80, cls:'grid-input',
                    editor: {xtype: 'numberfield', minValue: 0, listeners:{
                        change:function(pt, newValue, oldValue, eOpts ){
                            var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                            row.set('orderValueAud', (row.get('priceAud') * newValue).toFixed(3));
                            row.set('orderValueRmb', (row.get('priceRmb') * newValue).toFixed(3));
                            row.set('orderValueUsd', (row.get('priceUsd') * newValue).toFixed(3));
                        }
                    }},
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    }
                },
                //采购货值
                { header: _lang.NewProductDocument.fOrderValue,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null,
                        {edit:false, gridId: conf.formGridPanelId})
                },
                //预计利润
                { header: _lang.NewProductDocument.fPredictProfit,
                    columns: new $groupPriceColumns(this, 'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd', null,
                        {gridId: conf.formGridPanelId})
                },
                //竞争对手报价
                { header: _lang.NewProductDocument.fCompetitorPrice, width: 100,
                    columns: new $groupPriceColumns(this, 'competitorPriceAud','competitorPriceRmb','competitorPriceUsd', null, {gridId: conf.formGridPanelId})
                },

                //对手销量
                { header: _lang.NewProductDocument.fCompetitorSaleRecord, dataIndex: 'competitorSaleRecord',  width:65,
                    editor: {xtype: 'numberfield', minValue: 0, },
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    }
                },
                //ebay月销售额
                { header: _lang.NewProductDocument.fEbayMonthlySales,  width: 80,
                    columns: new $groupPriceColumns(this, 'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd', null,
                        {gridId: conf.formGridPanelId})
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
                // { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                // { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
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
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;

        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new OtherProductDialogWin({
                    single:true,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    productType:'5',
                    selectedId : selectedId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            if(result[0] != undefined && ! $checkGridRowExist(this.meGrid.getStore(),result[0].raw.product.id)) {
                                var product = {};
                                Ext.apply(product, result[0].raw);
                                Ext.apply(product, result[0].raw.product);
                                Ext.applyIf(product, result[0].raw.product.prop);
                                product.productQuotationId = result[0].raw.id;
                                product.productQuotationBusinessId = result[0].raw.businessId;
                                product.productQuotationDetailBusinessId = result[0].raw.detailBusinessId;
                                product.id = result[0].raw.product.id;
                                product.orderQty = result[0].raw.moq;
                                product.orderValueAud =  (result[0].raw.moq * result[0].raw.priceAud).toFixed(2);
                                product.orderValueRmb =  (result[0].raw.moq * result[0].raw.priceRmb).toFixed(2);
                                product.orderValueUsd =  (result[0].raw.moq * result[0].raw.priceUsd).toFixed(2);

                                this.meGrid.getStore().insert(idx, product);
                                this.meGrid.getStore().removeAt(idx + 1);
                            }
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new OtherProductDialogWin({
                    single:false,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    selectedId : '',
                    productType:'5',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(result.data.items.length>0){
                            var items = selectedData;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.product.id)){
                                    var product = {};
                                    Ext.apply(product, items[index].raw);
                                    Ext.apply(product, items[index].raw.product);
                                    Ext.applyIf(product, items[index].raw.product.prop);
                                    product.productQuotationId = items[index].raw.id;
                                    product.productQuotationBusinessId = items[index].raw.businessId;
                                    product.productQuotationDetailBusinessId = items[index].raw.detailBusinessId;
                                    product.orderQty = items[index].raw.moq;
                                    product.orderValueAud =  (items[index].raw.moq *  items[index].raw.priceAud).toFixed(2);
                                    product.orderValueRmb =  (items[index].raw.moq * items[index].raw.priceRmb).toFixed(2);
                                    product.orderValueUsd =  (items[index].raw.moq * items[index].raw.priceUsd).toFixed(2);
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    },
    onQueryRowAction: function(grid, record, action, idx, col, conf) {
        var grid = Ext.getCmp(this.conf.formGridPanelId);
        var form = Ext.getCmp(this.conf.mainFormPanelId);
        var vendorId = form.getCmpByName('main.vendorId').getValue();
        if (grid.getStore().getCount() > 0) {
            Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureReplace, function (button) {
                if (button == 'yes') {
                    grid.getStore().removeAll();
                    Ext.Ajax.request({
                        url:  __ctxPath + 'archives/flow/productquotation/listForImport?type=1&vvendorId=' + vendorId,
                        scope: this, method: 'post',
                        success: function (response, options) {
                            var items = Ext.decode(response.responseText).data;
                            if (items.length > 0) {
                                for (var index in items) {
                                    var product = {};
                                    Ext.apply(product, items[index]);
                                    Ext.apply(product, items[index].product);
                                    Ext.applyIf(product, items[index].product.prop);
                                    product.productQuotationId = items[index].id;
                                    product.productQuotationBusinessId = items[index].businessId;
                                    product.productQuotationDetailBusinessId = items[index].detailBusinessId;
                                    product.orderQty = items[index].moq;
                                    product.orderValueAud = (items[index].moq * items[index].priceAud).toFixed(2);
                                    product.orderValueRmb = (items[index].moq * items[index].priceRmb).toFixed(2);
                                    product.orderValueUsd = (items[index].moq * items[index].priceUsd).toFixed(2);
                                    grid.getStore().add(product);
                                }
                            }
                        },
                        failure: function(){ Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsError) }
                    });
                }
            });
        }else{
            grid.getStore().removeAll();
            Ext.Ajax.request({
                url:  __ctxPath + 'archives/flow/productquotation/listForImport?type=1&vendorId=' + vendorId,
                scope: this, method: 'post',
                success: function (response, options) {
                    var items = Ext.decode(response.responseText).data;
                    if (items.length > 0) {
                        for(var index in items) {
                            var product = {};
                            Ext.apply(product, items[index]);
                            Ext.apply(product, items[index].product);
                            Ext.applyIf(product, items[index].product.prop);
                            product.productQuotationId = items[index].id;
                            product.productQuotationBusinessId = items[index].businessId;
                            product.productQuotationDetailBusinessId = items[index].detailBusinessId;
                            product.orderQty = items[index].moq;
                            product.orderValueAud = (items[index].moq * items[index].priceAud).toFixed(2);
                            product.orderValueRmb = (items[index].moq * items[index].priceRmb).toFixed(2);
                            product.orderValueUsd = (items[index].moq * items[index].priceUsd).toFixed(2);
                            grid.getStore().add(product);
                        }
                    }
                },
                failure: function(){ Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsError) }
            });
        }
    }

});
