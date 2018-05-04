Ext.define('App.ProductCostFormOrderCbmGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormOrderCbmGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ProductCost.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormOrderCbmMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormOrderCbmMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormOrderCbmMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormOrderCbmMultiFormPanelID';
        conf.defHeight = this.height || 180;

        this.initUIComponents(conf);
        
        App.ProductCostFormOrderCbmGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [
            new $toolsPlusPriceColumnsDisplay({gridId: conf.subGridPanelId}),
            {
                type: 'minimize', tooltip: _lang.TButton.minimize, scope: this,
                handler: function (event, toolEl, panelHeader) {
                    this.setHeight(conf.defHeight);
                    this.subGridPanel.setHeight(conf.defHeight-3);
                }
            }, {
                type: 'maximize', tooltip: _lang.TButton.maximize, scope: this,
                handler: function (event, toolEl, panelHeader) {
                    this.setHeight(700);
                    this.subGridPanel.setHeight(697);
            }
        }];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.ProductCost.fOrderCbm,
            forceFit: false,
            scope: this,
            width: 'auto',
            height:conf.defHeight -3 ,
            url: '',
            // tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            bbar: false,
            header:{
                cls:'x-panel-header-gray'
            },
            displayAllPrice: true,
            fields: [
                'id','orderNumber','totalCbm', 'totalShippingCbm','totalPackingCbm',
                'gstAud','gstRmb','gstUsd',
                'valueGstAud','valueGstRmb','valueGstUsd',
                'localGstAud','localGstRmb','localGstUsd',
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width: 60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 180},
                {header: _lang.ProductCost.fCbm,
                    columns:[
                        {header: _lang.ProductCost.fOrderCbm, dataIndex: 'totalCbm', width: 100},
                        {header:_lang.ProductCost.fActualCbm, dataIndex: 'totalPackingCbm', width: 100},
                        {header: _lang.Sta.fTotalShippingCbm, dataIndex: 'totalShippingCbm', width: 100,
                            renderer: function (value, meta, record) {
                                meta.tdCls = 'grid-input';
                                return value;
                            },
                            editor: {xtype: 'textfield'}
                        }
                ]},
                {header: _lang.ProductCost.fValueGst, hidden: true,
                    columns:[
                        {header: _lang.TText.fAUD, dataIndex: 'valueGstAud', width: 100, hidden: true},
                        {header:_lang.TText.fRMB, dataIndex: 'valueGstRmb', width: 100, hidden: true},
                        {header: _lang.TText.fUSD, dataIndex: 'valueGstRmb', width: 100, hidden: true}
                    ]
                },
                {header: _lang.ProductCost.fLocalGst, hidden: true,
                    columns:[
                        {header: _lang.TText.fAUD, dataIndex: 'localGstAud', width: 100, hidden: true},
                        {header:_lang.TText.fRMB, dataIndex: 'localGstRmb', width: 100, hidden: true},
                        {header: _lang.TText.fUSD, dataIndex: 'localGstRmb', width: 100, hidden: true}
                    ]
                },
                {header: _lang.ProductCost.fGST, hidden: true,
                    columns:[
                        {header: _lang.TText.fAUD, dataIndex: 'gstAud', width: 100, hidden: true},
                        {header:_lang.TText.fRMB, dataIndex: 'gstRmb', width: 100, hidden: true},
                        {header: _lang.TText.fUSD, dataIndex: 'gstRmb', width: 100, hidden: true}
                    ]
                },

    ]// end of columns
        });
	},
 btnRowEdit: function(){},
btnRowRemove: function() {},
onRowAction: function(grid, record, action, idx, col, conf) {
    switch (action) {

        case 'btnRowRemove' :
            Ext.getCmp(conf.subGridPanelId).store.remove(record);
            break;

    }
},
});
