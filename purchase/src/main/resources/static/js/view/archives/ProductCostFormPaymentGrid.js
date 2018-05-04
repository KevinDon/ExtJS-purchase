Ext.define('App.ProductCostFormPaymentGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormPaymentGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ProductCost.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormPaymentMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormPaymentMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormPaymentMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormPaymentMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ProductCostFormPaymentGrid.superclass.constructor.call(this, {
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
            title: _lang.ProductCost.fPayment,
            forceFit: false,
            scope: this,
            width: 'auto',
            height:conf.defHeight -3 ,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            bbar: false,
            header:{
                cls:'x-panel-header-gray'
            },
            displayAllPrice: true,
            fields: [
                'id','businessId', 'orderNumber',  'itemId','itemCnName','itemEnName', 'priceAud', 'priceRmb', 'priceUsd', 'currency',
                'qty', 'rateAudToUsd', 'rateAudToRmb', 'remark','subtotalAud','subtotalRmb','subtotalUsd','settlementType','paymentRateAudToRmb', 'paymentRateAudToUsd'
            ],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 180, hidden: true, },
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'itemId', width: 180, hidden: true, },
                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', 100: 180},
                {header:_lang.FlowFeeRegistration.fProject, dataIndex: 'itemCnName', width: 100, hidden: curUserInfo.lang == 'zh_CN'? false : true,
                    renderer: function (value, meta, record) {
                        // meta.tdCls = 'grid-input';
                        return value;
                    },
                    // editor: {xtype: 'textfield',}
                },
                {header: _lang.FlowFeeRegistration.fProject, dataIndex: 'itemEnName', width: 100, hidden: curUserInfo.lang == 'zh_CN'? true : false,
                    renderer: function (value, meta, record) {
                        // meta.tdCls = 'grid-input';
                        return value;
                    },
                    // editor: {xtype: 'textfield',}
                },
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 80,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                //单价
                { header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        // row.set('subtotalAud', (row.get('priceAud') * row.get('qty')).toFixed(3));
                        // row.set('subtotalRmb', (row.get('priceRmb') * row.get('qty')).toFixed(3));
                        // row.set('subtotalUsd', (row.get('priceUsd') * row.get('qty')).toFixed(3));
                    },{gridId: conf.formGridPanelId, edit: false})
                },
                { header: _lang.Duty.fQty,dataIndex: 'qty', width: 55,},
                {header: _lang.FlowFeePayment.fPaymentExchange, columns: $groupExchangeColumns(this, 'paymentRateAudToRmb', 'paymentRateAudToUsd')},
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'subtotalAud','subtotalRmb','subtotalUsd', null,
                        { edit:false, gridId: conf.formGridPanelId})
                },
                {header: _lang.FlowPurchaseContract.fSettlementType, dataIndex: 'settlementType', width: 120, scope: this, hidden:true,
                    renderer: function (value, meta, record) {
                        if(value){
                            var $settlementType = _dict.getValueRow('settlementType', value);
                            if(curUserInfo.lang == 'zh_CN'){
                                return $settlementType.cnName;
                            }else{
                                return $settlementType.enName;
                            }
                        }
                    },
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 150,
                    renderer: function (value, meta, record) {
                        // meta.tdCls = 'grid-input';
                        return value;
                    },
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
    updateSummary: function(form){
        if(!form) form = Ext.getCmp(this.mainFormPanelId);
        var range = this.subGridPanel.getStore().getRange();
        var sumAud = 0, sumRmb = 0, sumUsd = 0;
        for(var i = 0; i < range.length; i++){
            var row = range[i].data;
            sumAud += parseFloat(row.subtotalAud || 0);
            sumRmb += parseFloat(row.subtotalRmb || 0);
            sumUsd += parseFloat(row.subtotalUsd || 0);
        }

        form.getCmpByName('paymentSumAud').setValue(sumAud.toFixed(2));
        form.getCmpByName('paymentSumRmb').setValue(sumRmb.toFixed(2));
        form.getCmpByName('paymentSumUsd').setValue(sumUsd.toFixed(2));
    }
});
