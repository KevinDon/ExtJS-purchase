Ext.define('App.ReportOrderInspectionFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ReportOrderInspectionFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowNewProduct.mTitle,
            moduleName: 'ReportOrderInspection',
            winId : 'ReportOrderInspectionViewForm',
            frameId : 'ReportOrderInspectionView',
            mainGridPanelId : this.mainGridPanelId,
            mainFormPanelId : this.mainFormPanelId,
            mainViewPanelId : 'ReportOrderInspectionViewPanelID',
            formGridPanelId : 'ReportOrderInspectionFormGridPanelID' + Ext.id(),
            dataChangeCallback: this.dataChangeCallback,
            itemClickCallback: this.itemClickCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.ReportOrderInspectionFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ],
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
                'id','sku','name','barcode','categoryName',
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
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120, locked: true},
                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200, hidden: true },

                //采购数量
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', scope:this, width: 80,},
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
                { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200 },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },

                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fCreator, dataIndex: 'creatorName', width: 100 },
                { header: _lang.NewProductDocument.fDepartmentId, dataIndex: 'departmentName', width: 100 }
            ]// end of columns
        });

        this.formGridPanel.store.on('dataChanged', function (store) {
            if(!!conf.dataChangeCallback) {
                conf.dataChangeCallback.call(conf.scope, store, conf);
            }
        });

        this.formGridPanel.on('itemclick', function(grid, rowIndex, columnIndex, e) {
            if(!!conf.itemClickCallback) {
                conf.itemClickCallback.call(conf.scope, grid, rowIndex, columnIndex, conf);
            }
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;
        var vendorId = '';
        if(!!conf && !!conf.mainFormPanelId) {
            var form = Ext.getCmp(conf.mainFormPanelId);
            vendorId = form.getCmpByName('main.vendorId').getValue();
        }

        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    selectedId : selectedId,
                    vendorId: vendorId,
                    productType:'1',
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
                    selectedId : '',
                    vendorId: vendorId,
                    productType:'1',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
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
