Ext.define('App.ProductCostFormOtherChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormOtherChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ProductCost',
        };

        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormChargeMultiGrid';
        conf.frameId = this.frameId;
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormChargeMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormChargeMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormChargeMultiFormPanelID';
        conf.defHeight = this.height || 300;
        this.initUIComponents(conf);
        
        App.ProductCostFormOtherChargeGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            items: [ this.subGridPanel ],
            bodyCls:'x-panel-body-gray',
        });
    },
    
    initUIComponents: function(conf){
        var scope = this;
        var tools = [
            {
                type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
                handler: function (event, toolEl, panelHeader) {
                    var grid = this.subGridPanel;
                    grid.getStore().insert(grid.getStore().count(), {});
                }
            },{
                type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
                handler: function(event, toolEl, panelHeader) {
                    this.setHeight(conf.defHeight);
                    this.subGridPanel.setHeight(conf.defHeight-3);
                }
            },{
                type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
                handler: function(event, toolEl, panelHeader) {
                    this.setHeight(700);
                    this.subGridPanel.setHeight(693);
                }
            }
        ];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.ProductCost.tabOtherCharge,
            forceFit: false,
            scope: this,
            width: 'auto',
            height: conf.defHeight-3,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            bbar: false,
            edit: !this.readOnly,
            displayAllPrice: true,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id', 'itemId','itemCnName','itemEnName','unitId','unitCnName','unitEnName',
                'rateAudToRmb','rateAudToUsd','qty',
                'priceAud','priceRmb','priceUsd','subtotalAud','subtotalRmb','subtotalUsd',
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
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fChargeItem, dataIndex: 'itemId', width: 120,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.chargeItem, [])},
                    editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'charge_item'}
                },
                // { header: _lang.FlowServiceInquiry.fUnit, dataIndex: 'unitId', width: 100,
                //     renderer: function(value) { return $renderGridDictColumn(value, _dict.chargeUnit, [])},
                //     editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'unit_of_operation'}
                // },

                {header: _lang.ProductCombination.fQty, dataIndex: 'qty', width: 60, scope:this,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'numberfield', minValue: 0,
                        listeners: {
                            change: function (pt, newValue, oldValue, eOpts) {
                                var gridPanel = pt.column.scope.subGridPanel;
                                var row = pt.column.scope.subGridPanel.getSelectionModel().selected.getAt(0);
                                var value = pt.getValue();
                                row.set('subtotalAud', (row.get('priceAud') * this.value).toFixed(2));
                                row.set('subtotalRmb', (row.get('priceRmb') * this.value).toFixed(2));
                                row.set('subtotalUsd', (row.get('priceUsd') * this.value).toFixed(2));
                                //ͳ���ܽ��
                                var totalPriceAud = 0,totalPriceRmb = 0,totalPriceUsd = 0, qty = 0;
                                for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                                    totalPriceAud = parseFloat(totalPriceAud) + parseFloat(gridPanel.getStore().getAt(i).data.subtotalAud || 0);
                                    totalPriceRmb = parseFloat(totalPriceRmb) + parseFloat(gridPanel.getStore().getAt(i).data.subtotalRmb || 0);
                                    totalPriceUsd = parseFloat(totalPriceUsd) + parseFloat(gridPanel.getStore().getAt(i).data.subtotalUsd || 0);
                                }
                                //Ext.getCmp(pt.column.scope.frameId).getCmpByName('qty5').setValue(qty.toFixed(2));
                                Ext.getCmp(pt.column.scope.frameId).getCmpByName('otherChargeSumAud').setValue(totalPriceAud.toFixed(2));
                                Ext.getCmp(pt.column.scope.frameId).getCmpByName('otherChargeSumRmb').setValue(totalPriceRmb.toFixed(2));
                                Ext.getCmp(pt.column.scope.frameId).getCmpByName('otherChargeSumUsd').setValue(totalPriceUsd.toFixed(2));
                            },
                        }
                    }
                },


                { header: _lang.NewProductDocument.fExchangeRate, columns: new $groupExchangeColumns(this, 'rateAudToRmb','rateAudToUsd')},
                { header: _lang.ProductCost.fDestinationChargePrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('subtotalAud', (row.get('priceAud') * row.get('qty')).toFixed(2));
                        row.set('subtotalRmb', (row.get('priceRmb') * row.get('qty')).toFixed(2));
                        row.set('subtotalUsd', (row.get('priceUsd') * row.get('qty')).toFixed(2));
                        var gridPanel = Ext.getCmp(conf.subGridPanelId);
                        var totalPriceAud = 0,totalPriceRmb = 0,totalPriceUsd = 0;
                        for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                            totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.subtotalAud || 0);
                            totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.subtotalRmb || 0);
                            totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.subtotalUsd || 0);
                        }

                        Ext.getCmp(conf.frameId).getCmpByName('otherChargeSumAud').setValue(totalPriceAud.toFixed(2));
                        Ext.getCmp(conf.frameId).getCmpByName('otherChargeSumRmb').setValue(totalPriceRmb.toFixed(2));
                        Ext.getCmp(conf.frameId).getCmpByName('otherChargeSumUsd').setValue(totalPriceUsd.toFixed(2));
                    }, {gridId: conf.subGridPanelId}),
                },
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'subtotalAud','subtotalRmb','subtotalUsd', null,
                        { edit:false, gridId: conf.formGridPanelId})
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
});
