Ext.define('App.ProductCostFormPurchaseContractGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormPurchaseContractGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductCost.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormPurchaseContractMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormPurchaseContractMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormPurchaseContractMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormPurchaseContractMultiFormPanelID';
        conf.defHeight = this.height || 300;
        this.initUIComponents(conf);

        App.ProductCostFormPurchaseContractGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            items: [ this.subGridPanel ],
            bodyCls:'x-panel-body-gray'
        });
    },

    initUIComponents: function(conf){
        var tools = [
            {
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
            id: conf.subFormPanelId,
            title: _lang.ProductCost.tabPurchaseContractProductList,
            forceFit: false,
            // scope: this,
            width: 'auto',
            tools: tools,
            height: conf.defHeight-3,
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [ 'id', 'orderId', 'vendorId', 'orderNumber', 'productId', 'sku', 'hsCode','dutyRate', 'orderQty', 'cartons', 'currency',
                'rateAudToRmb', 'rateAudToUsd', 'priceAud', 'priceRmb', 'priceUsd',
                'orderValueAud', 'orderValueRmb', 'orderValueUsd',
                'masterCartonCbm','unitCbm', 'pcsPerCarton', 'packingCartons','packingQty',
                'salesPriceAud', 'salesPriceRmb', 'salesPriceUsd',
                'salesValueAud', 'salesValueRmb','salesValueUsd'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 40, hidden: true },
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 100},
                { header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 180, hidden: true},
                { header: _lang.ProductDocument.fProductId, dataIndex: 'productId', hidden: true},
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fHsCode, dataIndex: 'hsCode', width: 80},
                { header: _lang.Duty.fRate, dataIndex: 'dutyRate', width: 80},
                { header: _lang.ProductDocument.fMasterCartonCbm, dataIndex: 'masterCartonCbm', width: 80, renderer: Ext.util.Format.numberRenderer('0.0000') },
                { header: _lang.FlowCustomClearance.fUnitCbm, dataIndex: 'unitCbm', width: 80, renderer: Ext.util.Format.numberRenderer('0.0000') },
                { header: _lang.ProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', width: 60},
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'packingQty', width: 80},
                { header: _lang.NewProductDocument.fOrderCartonQty, dataIndex: 'packingCartons', width: 80},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 80,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                { header: _lang.ProductCost.fPurchasePrice, columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit: false})},
                // { header: _lang.ProductCost.fPurchaseValue, columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null, {edit: false})},
                { header: _lang.ProductCost.fSalesPrice, columns: new $groupPriceColumns(this, 'salesPriceAud','salesPriceRmb','salesPriceUsd', null, {edit: false})},
                // { header: _lang.ProductCost.fSalesValue, columns: new $groupPriceColumns(this, 'salesValueAud', 'salesValueRmb','salesValueUsd', null, {edit: false})},
            ],
        });
    }, 
});
