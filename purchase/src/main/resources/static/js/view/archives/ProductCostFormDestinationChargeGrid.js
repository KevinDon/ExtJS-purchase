Ext.define('App.ProductCostFormDestinationChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormDestinationChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ProductCost',
        };

        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormDestinationMultiGrid';
        conf.frameId = this.frameId;
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormDestinationMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormDestinationMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormDestinationMultiFormPanelID';
        conf.defHeight = this.height || 300;
        this.initUIComponents(conf);
        
        App.ProductCostFormDestinationChargeGrid.superclass.constructor.call(this, {
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
            title: _lang.FlowServiceInquiry.fDestinationCharges,
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
                'rateAudToRmb','rateAudToUsd','containerType', 'containerQty',
                'priceAud','priceRmb','priceUsd','subtotalAud','subtotalRmb','subtotalUsd',
                'receivedPriceAud','receivedPriceRmb','receivedPriceUsd'
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
                { header: _lang.FlowServiceInquiry.fUnit, dataIndex: 'unitId', width: 100,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.chargeUnit, [])},
                    editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'unit_of_operation'}
                },
                { header: _lang.ProductCost.fContainerType, dataIndex: 'containerType', width: 100,
                    renderer: function(value) {
                    if(!value) return _lang.TText.vAll;
                        return $renderGridDictColumn(value, _dict.containerType, [])
                    },
                    //editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'container_type'}
                },
                {header: _lang.ProductCombination.fQty, dataIndex: 'containerQty', width: 60, scope:this,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'numberfield', minValue: 0,
                        listeners: {
                            change:function(pt, newValue, oldValue, eOpts ) {
                                var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                row.set('containerQty', newValue);
                                this.up().grid.scope.updateSubtotal.call(this.up().grid.scope, row, newValue);
                            }
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: new $groupExchangeColumns(this, 'rateAudToRmb','rateAudToUsd')},
                { header: _lang.ProductCost.fDestinationChargePrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        this.up().grid.scope.updateSubtotal.call(this.up().grid.scope, row, value);
                    }, {gridId: conf.subGridPanelId})
                },
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'subtotalAud','subtotalRmb','subtotalUsd', null,
                        { edit:false, gridId: conf.subGridPanelId})
                },
                { header: _lang.ProductCost.fActualSum,
                    columns: new $groupPriceColumns(this, 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd', function(row, value){
                            this.up().grid.scope.updateSummary.call(this.up().grid.scope);
                        },{ gridId: conf.subGridPanelId})
                }
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
    updateSubtotal: function(row){
        var qty = row.get('containerQty') || 0;
        var priceAud = row.get('priceAud') || 0;
        var priceRmb = row.get('priceRmb') || 0;
        var priceUsd = row.get('priceUsd') || 0;

        row.set('subtotalAud', priceAud * qty);
        row.set('subtotalRmb', priceRmb * qty);
        row.set('subtotalUsd', priceUsd * qty);

        row.set('receivedPriceAud', priceAud * qty);
        row.set('receivedPriceRmb', priceRmb * qty);
        row.set('receivedPriceUsd', priceUsd * qty);

        this.updateSummary();
    },
    updateSummary: function(form){
        if(!form) form = Ext.getCmp(this.mainFormPanelId);
        var range = this.subGridPanel.getStore().getRange();
        var sumAud = 0, sumRmb = 0, sumUsd = 0;
        for(var i = 0; i < range.length; i++){
            var row = range[i].data;
            sumAud += parseFloat(row.receivedPriceAud || 0);
            sumRmb += parseFloat(row.receivedPriceRmb || 0);
            sumUsd += parseFloat(row.receivedPriceUsd || 0);
        }

        form.getCmpByName('calChargeSumAud').setValue(sumAud.toFixed(2));
        form.getCmpByName('calChargeSumRmb').setValue(sumRmb.toFixed(2));
        form.getCmpByName('calChargeSumUsd').setValue(sumUsd.toFixed(2));
        form.getCmpByName('chargeSumAud').setValue(sumAud.toFixed(2));
    }
});
