Ext.define('App.ProductCostFormDutyChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormDutyChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormDutyMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormDutyMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormDutyMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormDutyMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ProductCostFormDutyChargeGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
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
            }
        ];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.Duty.fDuty,
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
                'id', 'hsCode', 'description', 'rateAudToRmb', 'rateAudToUsd', 'salesValueAud','salesValueRmb','salesValueUsd',
                'salesQty', 'dutyRate', 'tariffAud','tariffRmb','tariffUsd','itemCode'
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
                { header: _lang.Duty.fHsCode, dataIndex: 'hsCode', width: 150,
                    editor:  { xtype: 'TariffDialog', single: true, formId : conf.mainFormPanelId,scope:this,
                        subcallback: function (rows) {
                            console.log(rows);
                            var data = rows[0].raw;
                            var gridRow = scope.subGridPanel.getSelectionModel().selected.getAt(0);
                            gridRow.set('hsCode', data.itemCode);
                            gridRow.set('dutyRate', data.dutyRate);
                        }
                    },
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        return value
                    }
                },
                { header: _lang.Duty.fDescription, dataIndex: 'description', width: 150, editor: {xtype: 'textfield'}, hidden: true},
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                { header: _lang.Duty.fQty, dataIndex: 'salesQty',align: 'right', width: 60, editor: {xtype: 'numberfield', minValue: 0},
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        return Ext.util.Format.number(value, "0");
                    }
                },
                { header: _lang.Duty.fPrice,
                    columns: new $groupPriceColumns(this, 'salesValueAud','salesValueRmb','salesValueUsd', function(row, value){
                        this.up().grid.scope.autoCountDuty.call(this.up().grid.scope, row, value);
                    },{gridId: conf.subGridPanelId})
                },
                { header: _lang.Duty.fRate, dataIndex: 'dutyRate',align: 'right', width: 60,
                    editor: {xtype: 'numberfield', minValue: 0.00,
                        listeners:{
                            change:function(pt, newValue, oldValue, eOpts ) {
                                var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                row.set('dutyRate', newValue);
                                this.up().grid.scope.autoCountDuty.call(this.up().grid.scope, row, newValue);
                            }
                        }
                    },
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        return Ext.util.Format.number(value, "0.00");
                    }
                },
                { header: _lang.Duty.fDuty,
                    columns: new $groupPriceColumns(this, 'tariffAud','tariffRmb','tariffUsd', null, {edit:false})
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
    autoCountDuty: function(row){
        var $data = {
            rateAudToAud:  1,
            rateAudToRmb:  row.get('rateAudToRmb') || 0,
            rateAudToUsd:  row.get('rateAudToUsd') || 0,
            qty: row.get('salesQty') || 0,
            dutyRate: row.get('dutyRate') || 0
        };

        $data.priceTotalAud =((row.get('salesValueAud')||0)  * $data.dutyRate / 100) || 0;
        $data.priceTotalRmb =((row.get('salesValueRmb')||0)  * $data.dutyRate / 100)|| 0;
        $data.priceTotalUsd =((row.get('salesValueUsd')||0)  * $data.dutyRate / 100) || 0;

        row.set('tariffAud', $data.priceTotalAud);
        row.set('tariffRmb', $data.priceTotalRmb);
        row.set('tariffUsd', $data.priceTotalUsd);

        this.updateSummary();
    },
    updateSummary: function(form){
        if(!form) form = Ext.getCmp(this.mainFormPanelId);
        var range = this.subGridPanel.getStore().getRange();
        var sumAud = 0, sumRmb = 0, sumUsd = 0;
        for(var i = 0; i < range.length; i++){
            var row = range[i].data;
            sumAud += parseFloat(row.tariffAud || 0);
            sumRmb += parseFloat(row.tariffRmb || 0);
            sumUsd += parseFloat(row.tariffUsd || 0);
        }

        form.getCmpByName('calTariffSumAud').setValue(sumAud.toFixed(2));
        form.getCmpByName('calTariffSumRmb').setValue(sumRmb.toFixed(2));
        form.getCmpByName('calTariffSumUsd').setValue(sumUsd.toFixed(2));
    }
});
