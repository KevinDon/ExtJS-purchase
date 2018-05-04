Ext.define('App.AsnPackingListPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.AsnPackingListPanel',
    title: _lang.FlowCustomClearance.fPackingSheet,
    width: '100%',

    tools: [ {
        type: 'minus', tooltip: _lang.TButton.del, scope: this,
        handler: function (event, toolEl, panelHeader) {
            var mainEditForm = panelHeader.ownerCt.mainForm;
            panelHeader.ownerCt.deletePackingListPanel.call(mainEditForm, panelHeader.ownerCt);
        }
    }],

    initComponent: function(){
        var conf = {
            frameId : 'FlowCustomClearanceView',
            mainFormPanelId : 'FlowCustomClearanceViewFormPanelID',
            formId: 'PackingListForm'
        };

        // conf.mainGridPanelId = this.mainGridPanelId || this.formId + '-PackingListMultiGrid';
        conf.panelId = this.panelId || 'AsnPackingListPanel';
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
                    { field: 'containerNumber', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fContainerNumber, cls: 'col-2', allowBlank: true, readOnly:true,},
                    { field: 'ccPackingId', xtype: 'textfield', fieldLabel: 'PackingId', cls: 'col-2', allowBlank: true, readOnly:true, hidden: true, },
                    { field: 'sealsNumber', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fSealsNumber, cls: 'col-2', allowBlank: true,readOnly:true,},
                    { field: 'containerType', xtype: 'hidden'},
                    { field: 'containerTypeName', xtype: 'dictfield', code:'service_provider', codeSub:'container_type',
                        fieldLabel: _lang.FlowCustomClearance.fContainerType, cls: 'col-2', allowBlank: true,
                    },
                ]},
            ]
        });
    },

    initGridPanel: function(conf){
        var tools = [{
            type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                var grid = this.formGridPanel;
                grid.getStore().insert(grid.getStore().count(), {});
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
            height: conf.defHeight-3,
            url: '',
            header:{cls:'x-panel-header-gray' },
            autoLoad: false,
            rsort: false,
            bbar: false,
            edit: !this.readOnly,
            fields: [
                'productId', 'sku', 'barcode', 'factoryCode', 'color', 'size', 'style','priceAud','priceRmb','priceUsd','rateAudToRmb', 'rateAudToUsd',
                'orderQty', 'cartons','expectedQty','expectedCartons', 'receivedQty', 'receivedCartons','currency',
                'unitCbm', 'totalCbm', 'totalNw', 'totalGw','priceCostAud','priceCostRmb','priceCostUsd'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [
                        {
                            iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                            callback: function(grid, record, action, idx, col, e, target) {
                                this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                            }
                        }]
                },
                { header: 'ID', dataIndex: 'productId', width: 180, hidden: true },
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200, },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //成本价
                { header: _lang.FlowOrderReceivingNotice.fPrice,
                    columns: new $groupPriceColumns(this, 'priceCostAud','priceCostRmb','priceCostUsd', null,{edit: false, gridId: conf.formGridPanelId})
                },
                //报价
                { header: _lang.FlowOrderReceivingNotice.fCostPrice,
                        columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit: false, gridId: conf.formGridPanelId, hidden: true })
                },

                { header: _lang.FlowOrderReceivingNotice.fExpected,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'expectedQty', width: 80,
                            renderer: function (value, meta, record) {
                                meta.tdCls = 'grid-input';
                                return value;
                            },
                            editor: {xtype: 'numberfield'}},
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'expectedCartons', width: 80,
                            renderer: function (value, meta, record) {
                                meta.tdCls = 'grid-input';
                                return value;
                            },
                            editor: {xtype: 'numberfield'}},
                    ]
                },
                { header: _lang.FlowOrderReceivingNotice.fReceived,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'receivedQty', width: 80,
                            renderer: function (value, meta, record) {
                                meta.tdCls = 'grid-input';
                                return value;
                            },
                            editor: {xtype: 'numberfield'}},
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'receivedCartons', width: 80,
                            renderer: function (value, meta, record) {
                                meta.tdCls = 'grid-input';
                                return value;
                            },
                            editor: {xtype: 'numberfield'}},
                    ]
                },
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'barcode', width: 200, },
                { header: _lang.FlowCustomClearance.fColor, dataIndex: 'color', width: 160, },
                { header: _lang.FlowCustomClearance.fSize, dataIndex: 'size', width: 160, },
                { header: _lang.FlowCustomClearance.fStyle, dataIndex: 'style', width: 160, },
                { header: _lang.FlowCustomClearance.fTotalCbm, dataIndex: 'totalCbm', width: 60, },
                { header: _lang.FlowCustomClearance.fTotalGw, dataIndex: 'totalGw', width: 60, },
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
        var  product = this.orders;

        console.log(data)
        if(this.orders.length > 0){
            for(var index in this.orders){
                product[index].expectedQty = this.orders[index].packingQty;
                product[index].expectedCartons = this.orders[index].packingCartons;
                if(!!this.orders[index].product){
                    Ext.apply(this.orders[index], this.orders[index].product);

                    this.formGridPanel.getStore().add(this.orders[index]);
                }else{
                    // console.log(this.orders[index]);
                }
            }
        }
        
        //container type
        if(!!data.containerType) data.containerTypeName = data.containerType;

        this.formGridPanel.getStore().add(product);
        this.editFormPanel.getForm().setValues(data);
        this.editFormPanel.getCmpByName('ccPackingId').setValue(data.ccPackingId || data.id);
    }
});