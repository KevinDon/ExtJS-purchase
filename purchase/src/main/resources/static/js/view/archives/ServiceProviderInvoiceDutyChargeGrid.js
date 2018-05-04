Ext.define('App.ServiceProviderInvoiceDutyChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ServiceProviderInvoiceDutyChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ServiceInvoice',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ServiceProviderInvoiceDutyMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ServiceProviderInvoiceDutyMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceDutyMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceDutyMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ServiceProviderInvoiceDutyChargeGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [
            {
                type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
                handler: function (event, toolEl, panelHeader) {
                    var grid = this.subGridPanel;
                    grid.getStore().insert(grid.getStore().count(), {});
                }
            }
        ];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.ServiceProviderInvoiceDuty.fDuty,
            forceFit: false,
            scope: this,
            width: 'auto',
            height:conf.defHeight -3 ,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,            
            displayAllPrice: true,
            bbar: false,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id', 'tariffItem', 'description', 'rateAudToRmb', 'rateAudToUsd', 'priceAud', 'priceRmb', 'priceUsd',
                'qty', 'dutyRate', 'priceTotalAud','priceTotalRmb','priceTotalUsd'
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
                { header: _lang.ServiceProviderInvoiceDuty.fTariffItem, dataIndex: 'tariffItem', width: 150, editor: {xtype: 'textfield'}},
                { header: _lang.ServiceProviderInvoiceDuty.fDescription, dataIndex: 'description', width: 150, editor: {xtype: 'textfield'}},
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                { header: _lang.ServiceProviderInvoiceDuty.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {gridId: conf.subGridPanelId})
                },

                { header: _lang.ServiceProviderInvoiceDuty.fQty, dataIndex: 'qty',align: 'right', width: 60,
                    editor: {xtype: 'numberfield', minValue: 0,
                        listeners:{
                            change:function(pt, newValue, oldValue, eOpts ) {
                                var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                row.set('qty', newValue);
                                this.up().grid.scope.autoCountDuty.call(this, row, newValue);
                            }
                        }
                    },renderer: Ext.util.Format.numberRenderer('0')
                },
                { header: _lang.ServiceProviderInvoiceDuty.fRate, dataIndex: 'dutyRate',align: 'right', width: 60,
                    editor: {xtype: 'numberfield', minValue: 0.00,
                        listeners:{
                            change:function(pt, newValue, oldValue, eOpts ) {
                                var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                row.set('dutyRate', newValue);
                                this.up().grid.scope.autoCountDuty.call(this, row, newValue);
                            }
                        }
                    },renderer: Ext.util.Format.numberRenderer('0.00')
                },
                { header: _lang.ServiceProviderInvoiceDuty.fDuty,
                    columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', null, {edit:false})
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

    autoCountDuty: function(row , refreshAll){
        var refreshAll = refreshAll || false;
        var $data = {
            rateAudToAud:  1,
            rateAudToRmb:  row.get('rateAudToRmb') || 0,
            rateAudToUsd:  row.get('rateAudToUsd') || 0,
            qty: row.get('qty') || 0,
            dutyRate: row.get('dutyRate') || 0
        };

        if(refreshAll){
            row.set('priceRmb', (row.get('priceAud') || 0 )* $data.rateAudToRmb);
            row.set('priceUsd', (row.get('priceAud') || 0 )* $data.rateAudToUsd);
        }

        $data.priceTotalAud =((row.get('priceAud')||0)  * $data.qty * $data.dutyRate) || 0;
        $data.priceTotalRmb =((row.get('priceRmb')||0)  * $data.qty * $data.dutyRate)|| 0;
        $data.priceTotalUsd =((row.get('priceUsd')||0)  * $data.qty * $data.dutyRate) || 0;

        row.set('priceTotalAud', $data.priceTotalAud);
        row.set('priceTotalRmb', $data.priceTotalRmb);
        row.set('priceTotalUsd', $data.priceTotalUsd);
    }
});
