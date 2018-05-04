Ext.define('App.PackingListPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.PackingListPanel',
    title: _lang.FlowCustomClearance.fPackingSheet,
    width: '100%',
    header:{
        cls:'x-panel-header-gray'
    },
    scope: this,
    bodyCls:'x-panel-body-gray',

    initComponent: function(){
        this. tools = [{
            type: 'plus', tooltip: _lang.TButton.add, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                var mainEditForm = panelHeader.ownerCt.scope;
                panelHeader.ownerCt.insertPackingListPanel.call(mainEditForm);
                var value =  parseInt(mainEditForm.getCmpByName('main.containerQty').getValue()) + 1;
                mainEditForm.getCmpByName('main.containerQty').setValue(value);
            }
        }, {
            type: 'minus', tooltip: _lang.TButton.del, scope: this,
            handler: function (event, toolEl, panelHeader) {
                var mainEditForm = panelHeader.ownerCt.mainForm;
                panelHeader.ownerCt.deletePackingListPanel.call(mainEditForm, panelHeader.ownerCt);
            }
        }];

        var conf = {
            frameId : 'FlowCustomClearanceView',
            mainFormPanelId : 'FlowCustomClearanceViewFormPanelID',
            formId: 'PackingListForm',
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        // conf.mainGridPanelId = this.mainGridPanelId || this.formId + '-PackingListMultiGrid';
        conf.panelId = this.panelId || 'PackingListPanel';
        conf.mainFormPanelId = this.mainFormPanelId || this.formId + '-PackingListMultiForm';
        conf.subFormPanelId = this.subFormPanelId || conf.panelId +'-FormPanelID';
        conf.subGridPanelId = this.subGridPanelId || conf.panelId +'-GridPanelID';
        conf.defHeight = this.height || 260;

        this.initEditFormPanel(conf);
        this.initGridPanel(conf);
        this.loadData();
        Ext.apply(this, {
            items: [this.editFormPanel, this.formGridPanel]
        });

        this.superclass.initComponent.call(this);
    },

    initEditFormPanel: function(conf){
        this.editFormPanel = new HP.FormPanel({
            region: 'north',
            id: conf.subFormPanelId,
            fieldItems: [
                { xtype: 'container',cls:'row', items:  [
                    { field: 'containerNumber', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fContainerNumber, cls: 'col-2', allowBlank: false,},
                    { field: 'sealsNumber', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fSealsNumber, cls: 'col-2', allowBlank: false,},
                    { field: 'containerType', xtype: 'dictcombo', code:'service_provider', codeSub:'container_type', fieldLabel: _lang.FlowCustomClearance.fContainerType, cls: 'col-2', allowBlank: false, value: '1'},
                ]},
            ]
        });
    },

    initGridPanel: function(conf){
        var scope = this;
        var tools = [{
            type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                var grid = this.formGridPanel;
                var mainEditForm = panelHeader.ownerCt.ownerCt.mainForm;
                var rateAudToRmb = mainEditForm.rate.rateAudToRmb;
                var rateAudToUsd =  mainEditForm.rate.rateAudToUsd;

                grid.getStore().insert(grid.getStore().count(), {
                    rateAudToRmb : rateAudToRmb,
                    rateAudToUsd: rateAudToUsd,
                });
            }
        }, {
            type: 'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight-3);
            }
        }, {
            type: 'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }
        }];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.FlowCustomClearance.fDetails,
            forceFit: false,
            width: 'auto',
            height: conf.defHeight-3,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            bbar: false,
            edit: !this.readOnly,
            header: {cls:'x-panel-header-gray'  },
            fields: [
                'id', 'sku', 'barcode', 'factoryCode', 'color', 'size', 'style','productId','factoryCode','packingQty','totalGw','masterCartonCbm',
                'orderQty', 'cartons', 'packingQty', 'packingCartons','pcsPerCarton','rateAudToRmb','rateAudToUsd','currency','srcPackingCartons','srcPackingQty',
                'unitCbm', 'totalCbm', 'totalNw', 'totalGw','priceAud','priceRmb','priceUsd','unitGw'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, hidden: this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [
                        {
                            iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                            callback: function(grid, record, action, idx, col, e, target) {
                                this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                            }
                        }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: 'ProductId', dataIndex: 'productId', width: 40, hidden: true },
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200, scope:this,
                    editor: {
                        xtype: 'OtherProductDialog',
                        productType : 5,
                        filterVendor: true,
                        fieldVendorIdName : 'main.vendorId',
                        formId: conf.mainFormPanelId,
                        single: true,
                        subcallback: function (rows) {
                            if(rows && rows.length > 0) {
                                var data = rows[0].raw;
                                var gridRow = scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                gridRow.set('productId', data.product.id);
                                gridRow.set('sku', data.sku);
                                gridRow.set('barcode', data.product.barcode);
                                gridRow.set('currency', data.currency);
                                gridRow.set('color', data.product.color);
                                gridRow.set('size', data.product.model);
                                gridRow.set('style', data.product.style);
                                gridRow.set('factoryCode', data.product.prop.factoryCode);
                                //gridRow.set('currency', data.product.prop.quotationCurrency);
                                gridRow.set('orderQty', 0);
                                gridRow.set('cartons', 0);
                                console.log(1);
                                var audToRmb = gridRow.data.rateAudToRmb;
                                var audToUsd = gridRow.data.rateAudToUsd;
                                var totalGw = (parseFloat(data.product.prop.masterCartonWeight) || 0);
                                gridRow.set('unitCbm', data.product.prop.masterCartonCbm);
                                gridRow.set('unitGw', data.product.prop.masterCartonWeight);
                                gridRow.set('pcsPerCarton', data.product.prop.pcsPerCarton);
                                //gridRow.set('totalGw', totalGw.toFixed(2));

                                if( data.currency == 1){
                                    gridRow.set('priceAud', parseFloat( data.priceAud).toFixed(2));
                                    gridRow.set('priceRmb', parseFloat(audToRmb *  data.priceAud).toFixed(2));
                                    gridRow.set('priceUsd', parseFloat(audToUsd *  data.priceAud).toFixed(2));
                                }else if( data.currency == 2){
                                    gridRow.set('priceRmb',  parseFloat( data.priceRmb).toFixed(2));
                                    gridRow.set('priceAud', parseFloat( data.priceRmb / audToRmb).toFixed(2));
                                    gridRow.set('priceUsd', parseFloat( data.priceRmb / audToRmb * audToUsd).toFixed(2));
                                }else if( data.currency == 3){
                                    gridRow.set('priceUsd', parseFloat( data.priceUsd).toFixed(2));
                                    gridRow.set('priceAud', parseFloat( data.priceUsd / audToUsd).toFixed(2));
                                    gridRow.set('priceRmb', parseFloat(audToRmb *  data.priceUsd /audToUsd).toFixed(2));
                                }else{
                                    gridRow.set('priceRmb', '');
                                    gridRow.set('priceAud', '');
                                    gridRow.set('priceUsd', '');
                                }}
                        }
                    }
                },
                { header: _lang.FlowCustomClearance.fFactoryCode, dataIndex: 'factoryCode', width: 80, hidden:true, },
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'barcode', width: 200, editor: {xtype: 'textfield'}},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },

                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd', null, {defaultRate: {
                    audToRmb: '',
                    audToUsd: '',
                }}), },

                {
                    header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud', 'priceRmb', 'priceUsd', null, {edit: true})
                },
                { header: _lang.FlowCustomClearance.fOrderQty, dataIndex: 'orderQty', width: 80,
                    renderer: function (value, meta, record) {
                        return value;
                    },
                    //editor: {xtype: 'numberfield' , }
                    },
                { header: _lang.FlowCustomClearance.fCartons, dataIndex: 'cartons', width: 80,
                    renderer: function (value, meta, record) {
                        return value;
                    },
                    //editor: {xtype: 'numberfield'}
                    },
                { header: _lang.FlowCustomClearance.fPackingQty, dataIndex: 'packingQty', width: 80, scope:this,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'numberfield',   listeners : {
                        change : function(pt, newValue, oldValue, eOpts){
                            var gridRow = scope.formGridPanel.getSelectionModel().selected.getAt(0);
                            var  packingCartons = Math.ceil(newValue / gridRow.data.pcsPerCarton)
                            gridRow.set('packingCartons', packingCartons);
                            console.log(1);
                            var totalGw = parseFloat(packingCartons * gridRow.data.unitGw);
                            var totalCbm = parseFloat(packingCartons * gridRow.data.unitCbm);
                            gridRow.set('totalGw', totalGw.toFixed(2));
                            gridRow.set('totalCbm', totalCbm.toFixed(2));
                            //更新总金额
                            if (!!conf.dataChangeCallback) {
                                conf.dataChangeCallback.call(conf.scope);
                            }
                        }
                    }}
                },

                { header: _lang.FlowCustomClearance.fPackingCartons, dataIndex: 'packingCartons', width: 80,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor :{xtype: 'numberfield',  listeners : {
                            change : function(pt, newValue, oldValue, eOpts){
                                var gridRow = scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                var  packingQty = Math.ceil(newValue * gridRow.data.pcsPerCarton)
                                gridRow.set('packingQty', packingQty);
                                var totalGw = parseFloat(newValue * gridRow.data.unitGw);
                                var totalCbm = parseFloat(newValue * gridRow.data.unitCbm);
                                gridRow.set('totalGw', totalGw.toFixed(2));
                                gridRow.set('totalCbm', totalCbm.toFixed(2));
                                //更新总金额
                                if (!!conf.dataChangeCallback) {
                                    conf.dataChangeCallback.call(conf.scope);
                                }
                            }

                        }
                    }
                    },
                { header: _lang.ProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', width: 80, hidden:true, },
                { header: _lang.FlowCustomClearance.fSrcPackingQty, dataIndex: 'srcPackingQty', width: 80, hidden:true, },
                { header: _lang.FlowCustomClearance.fSrcPackingCartons, dataIndex: 'srcPackingCartons', width: 80, hidden:true, },
                { header: _lang.FlowCustomClearance.fUnitCbm, dataIndex: 'unitCbm', width: 120, hidden:true,},
                { header: _lang.FlowCustomClearance.fUnitGw, dataIndex: 'unitGw', width: 120, hidden:true,},
                { header: _lang.FlowCustomClearance.fColor, dataIndex: 'color', width: 120, },
                { header: _lang.FlowCustomClearance.fSize, dataIndex: 'size', width: 120,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'textfield'}
                },
                { header: _lang.FlowCustomClearance.fStyle, dataIndex: 'style', width: 120,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'textfield'}
                },
                // { header: _lang.FlowCustomClearance.fUnitCbm, dataIndex: 'unitCbm', width: 70,
                //     renderer: function (value, meta, record) {
                //         meta.tdCls = 'grid-input';
                //         return value;
                //     },
                //     editor: {xtype: 'textfield'}
                // },
                { header: _lang.FlowCustomClearance.fTotalCbm, dataIndex: 'totalCbm', width: 70,
                    renderer: function (value, meta, record) {
                        //meta.tdCls = 'grid-input';
                        return value;
                    },
                    //editor: {xtype: 'textfield'}
                },
                // { header: _lang.FlowCustomClearance.fTotalNw, dataIndex: 'totalNw', width: 70,
                //
                //     renderer: function (value, meta, record) {
                //         meta.tdCls = 'grid-input';
                //         return value;
                //     },
                //     editor: {xtype: 'textfield'}
                // },
                { header: _lang.FlowCustomClearance.fTotalGw, dataIndex: 'totalGw', width: 70,
                    renderer: function (value, meta, record) {
                    //meta.tdCls = 'grid-input';
                    return value;
                },
                    //editor: {xtype: 'textfield'}
                },
            ]// end of columns
        });


    },
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowRemove' :
                Ext.getCmp(conf.subGridPanelId).store.remove(record);
                break;
            default :
                break;
        }
    },

    loadData: function(){
        var data = this.data;
        if(!data) return;
        if(!!data.packingList){
            this.formGridPanel.getStore().add(data.packingList);
        }
        this.editFormPanel.getForm().setValues(data);

        if(!!this.mainForm.containerType) {
            this.editFormPanel.getCmpByName('containerType').setValue(''+ this.mainForm.containerType);
        }
    }
});