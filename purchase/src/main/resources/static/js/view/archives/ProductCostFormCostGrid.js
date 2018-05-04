Ext.define('App.ProductCostFormCostGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormCostGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-CostMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-CostMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-CostMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-CostMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ProductCostFormCostGrid.superclass.constructor.call(this, {
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
                type: 'refresh', tooltip: _lang.ProductCost.fCalculateCost, scope: this, hidden: this.readOnly,
                handler: function (event, toolEl, panelHeader) {
                    this.scope.calculate.call(this.scope, conf);
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
            title: _lang.ProductCost.tabProductCost,
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
                'id', 'orderId', 'orderNumber', 'productId', 'currency', 'sku',
                'priceAud', 'priceRmb', 'priceUsd','priceCostAud', 'priceCostRmb', 'priceCostUsd',
                'portFeeAud', 'portFeeRmb', 'portFeeUsd', 'chargeItemFeeAud', 'chargeItemFeeRmb', 'chargeItemFeeUsd',
                'tariffAud', 'tariffRmb', 'tariffUsd', 'customProcessingFeeAud', 'customProcessingFeeRmb', 'customProcessingFeeUsd',
                'otherFeeAud', 'otherFeeRmb', 'otherFeeUsd', 'totalCostAud', 'totalCostRmb', 'totalCostUsd',
                'valueGstAud', 'valueGstRmb', 'valueGstUsd','localGstAud', 'localGstRmb', 'localGstUsd','gstAud', 'gstRmb', 'gstUsd',
                'hasError','orderQty','rateAudToRmb','rateAudToUsd',
                'subTotalCostAud','subTotalCostRmb','subTotalCostUsd'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, hidden:true,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: 'HasError', dataIndex: 'hasError', width: 50, hidden: true },
                // { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 200},
                { header: _lang.ProductDocument.fProductId, dataIndex: 'productId', width: 180, hidden:true},
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 120},
                { header: _lang.ProductDocument.fCurrency, dataIndex: 'currency', width: 100, hidden:true,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.currency);}
                },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fTotalOrderQty, dataIndex: 'orderQty', width: 50},
                { header: _lang.OrderDocument.fPrice, columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit: false, decimalPrecision:4, audHidden:true, rmbHidden:true, usdHidden:true}), hidden:true},
                { header: _lang.ProductCost.fPurchaseCost, columns: new $groupPriceColumns(this, 'priceCostAud','priceCostRmb','priceCostUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fFreightCost, columns: new $groupPriceColumns(this, 'portFeeAud','portFeeRmb','portFeeUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fDestinationCost, columns: new $groupPriceColumns(this, 'chargeItemFeeAud','chargeItemFeeRmb','chargeItemFeeUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fDutyCost, columns: new $groupPriceColumns(this, 'tariffAud','tariffRmb','tariffUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fCustomProcessingCost, columns: new $groupPriceColumns(this, 'customProcessingFeeAud','customProcessingFeeRmb','customProcessingFeeUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fOtherCost, columns: new $groupPriceColumns(this, 'otherFeeAud','otherFeeRmb','otherFeeUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fValueGst, columns: new $groupPriceColumns(this, 'valueGstAud','valueGstRmb','valueGstUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fLocalGst, columns: new $groupPriceColumns(this, 'localGstAud','localGstRmb','localGstUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fGST, columns: new $groupPriceColumns(this, 'gstAud','gstRmb','gstUsd', null, {edit: false, decimalPrecision:4, rmbHidden:true, usdHidden:true})},
                { header: _lang.ProductCost.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd', null, {gridId: conf.subGridPanelId})},
                { header: _lang.ProductCost.fTotalCost, columns: new $groupPriceColumns(this, 'totalCostAud','totalCostRmb','totalCostUsd', null, {edit: false})},
                { header: _lang.FlowFeeRegistration.fSubtotal, columns: new $groupPriceColumns(this, 'subTotalCostAud','subTotalCostRmb','subTotalCostUsd', null, {edit: false})},
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

    updateTotalCost: function(type, newValue){
        var grid = this.subGridPanel;
        var row = grid.getSelectionModel().selected.items[0];
        var total = 0;
        if(type == 'priceCost') total += newValue;
        else total += parseFloat(row.data.priceCostAud || 0);

        if(type == 'portFee') total += newValue;
        else total += parseFloat(row.data.portFeeAud || 0);

        if(type == 'chargeItemFee') total += newValue;
        else total += parseFloat(row.data.chargeItemFeeAud || 0);

        if(type == 'tariff') total += newValue;
        else total += parseFloat(row.data.tariffAud || 0);

        if(type == 'customProcessingFee') total += newValue;
        else total += parseFloat(row.data.customProcessingFeeAud || 0);

        if(type == 'other') total += newValue;
        else total += parseFloat(row.data.otherFeeAud || 0);

        total = Ext.util.Format.round(total, 4);
        row.set('totalCostAud', total);
        row.set('totalCostRmb', total * row.data.rateAudToRmb);
        row.set('totalCostUsd', total * row.data.rateAudToUsd);
        row.set('subTotalCostAud', total * row.data.orderQty);
        row.set('subTotalCostRmb', total * row.data.orderQty * row.data.rateAudToRmb);
        row.set('subTotalCostUsd', total * row.data.orderQty * row.data.rateAudToUsd);
        row.commit();
    }
});
