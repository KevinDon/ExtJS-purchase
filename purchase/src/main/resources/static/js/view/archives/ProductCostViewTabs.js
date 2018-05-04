Ext.define('App.ProductCostViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ProductCostViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        conf.items = [];
        conf.items[0] = new HP.GridPanel({
            id: conf.mainTabPanelId + '-0',
            title: _lang.ProductCost.tabPurchaseContractProductList,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [ 'id', 'orderId', 'vendorId', 'orderNumber', 'productId', 'sku', 'hsCode', 'orderQty', 'cartons', 'currency',
                'rateAudToRmb', 'rateAudToUsd', 'priceAud', 'priceRmb', 'priceUsd',
                'orderValueAud', 'orderValueRmb', 'orderValueUsd',
                'masterCartonCbm', 'pcsPerCarton', 'packingCartons','packingQty',
                'salesPriceAud', 'salesPriceRmb', 'salesPriceUsd',
                'salesValueAud', 'salesValueRmb','salesValueUsd'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.FlowPurchaseContract.fOrderId, dataIndex: 'orderId', width: 200},
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 100},
                { header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', hidden: true},
                { header: _lang.ProductDocument.fProductId, dataIndex: 'productId', hidden: true},
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fHsCode, dataIndex: 'hsCode', width: 100},
                { header: _lang.ProductDocument.fMasterCartonCbm, dataIndex: 'masterCartonCbm', width: 80, renderer: Ext.util.Format.numberRenderer('0.00') },
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

        conf.items[1] = new HP.GridPanel({
            id: conf.mainTabPanelId + '-1',
            title: _lang.FlowServiceInquiry.fFreightCharges,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id', 'originPortId', 'destinationPortId', 'rateAudToRmb','rateAudToUsd',
                'priceGp20Aud','priceGp20Rmb','priceGp20Usd','priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd',
                'priceGp40Aud','priceGp40Rmb','priceGp40Usd','priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd',
                'priceHq40Aud','priceHq40Rmb','priceHq40Usd','priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd',
                'priceLclAud','priceLclRmb','priceLclUsd','priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',
                'priceTotalAud','priceTotalRmb','priceTotalUsd',
                'gp20Qty', 'gp40Qty', 'hq40Qty', 'lclCbm', 'rowTotalQty'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 100, locked:true,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.origin, [])}
                },
                { header: _lang.FlowServiceInquiry.fDestination, dataIndex: 'destinationPortId', width: 100, locked:true,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.destination, [])}
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: new $groupExchangeColumns(this, 'rateAudToRmb','rateAudToUsd')},
                { header: _lang.FlowServiceInquiry.fPriceGp20,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd')},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd')},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceGp40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceGp40Aud','priceGp40Rmb','priceGp40Usd')},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd')},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceHq40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd')},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd')},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceLcl,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd')},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd')},
                        { header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0.00')},
                    ]
                },
            ]
        });

        conf.items[2] = new HP.GridPanel({
            id: conf.mainTabPanelId + '-2',
            title: _lang.FlowServiceInquiry.fDestinationCharges,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id', 'itemId','itemCnName','itemEnName','unitId','unitCnName','unitEnName',
                'rateAudToRmb','rateAudToUsd',
                'priceAud','priceRmb','priceUsd'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fChargeItem, dataIndex: 'itemId', width: 120,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.chargeItem, [])},
                    editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'charge_item'}
                },
                { header: _lang.FlowServiceInquiry.fUnit, dataIndex: 'unitId', width: 100,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.chargeUnit, [])},
                    editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'unit_of_operation'}
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: new $groupExchangeColumns(this, 'rateAudToRmb','rateAudToUsd')},
                { header: _lang.ProductCost.fDestinationChargePrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {gridId: conf.subGridPanelId}),
                },
            ]
        });

        conf.items[3] = new HP.GridPanel({
            id: conf.mainTabPanelId + '-3',
            title: _lang.Duty.fDuty,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            bbar: false,
            fields: [
                'id', 'hsCode', 'description', 'rateAudToRmb', 'rateAudToUsd', 'salesValueAud','salesValueRmb','salesValueUsd',
                'salesQty', 'dutyRate', 'tariffAud','tariffRmb','tariffUsd'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.Duty.fHsCode, dataIndex: 'hsCode', width: 150, editor: {xtype: 'textfield'}},
                { header: _lang.Duty.fDescription, dataIndex: 'description', width: 150, editor: {xtype: 'textfield'}, hidden: true},
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                { header: _lang.Duty.fQty, dataIndex: 'salesQty',align: 'right', width: 60, editor: {xtype: 'numberfield', minValue: 0}, renderer: Ext.util.Format.numberRenderer('0')},
                { header: _lang.Duty.fPrice,
                    columns: new $groupPriceColumns(this, 'salesValueAud','salesValueRmb','salesValueUsd', function(row, value){
                        this.up().grid.scope.autoCountDuty.call(this, row, value);
                    },{gridId: conf.subGridPanelId})
                },
                { header: _lang.Duty.fRate, dataIndex: 'dutyRate',align: 'right', width: 60,
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
                { header: _lang.Duty.fDuty,
                    columns: new $groupPriceColumns(this, 'tariffAud','tariffRmb','tariffUsd', null, {edit:false})
                },
            ]
        });

        conf.items[4] = new HP.GridPanel({
            id: conf.mainTabPanelId + '-4',
            title: _lang.ProductCost.tabProductCost,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            bbar: false,
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
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: 'HasError', dataIndex: 'hasError', width: 50, hidden: true },
                // { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 200},
                { header: _lang.ProductDocument.fProductId, dataIndex: 'productId', width: 180, hidden:true},
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 180},
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
            ]
        });

        conf.items[5] = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        conf.items[5].doLayout();

        try{
            App.ProductCostViewTabs.superclass.constructor.call(this, {
                id: this.mainTabPanelId,
                title: conf.fieldLabel,
                autoWidth: true,
                autoScroll : true,
                minHeight: 300,
                cls:'tabs'
            });
            this.initUIComponents(conf);
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
        var cmpPanel = Ext.getCmp(this.mainTabPanelId);
        for(var i = 0; i < conf.items.length; i++){
            cmpPanel.add(conf.items[i]);
        }
        cmpPanel.setActiveTab(conf.items[0]);
    }
});
