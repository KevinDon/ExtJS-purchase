Ext.define('App.ServiceProviderInvoiceDestinationChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ServiceProviderInvoiceDestinationChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ServiceInvoice',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ServiceProviderInvoiceMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ServiceProviderInvoiceMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ServiceProviderInvoiceDestinationChargeGrid.superclass.constructor.call(this, {
            id: conf.subFormPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            items: [ this.subGridPanel ],
            bodyCls:'x-panel-body-gray',
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [
            {
                type: 'refresh', tooltip: _lang.FlowServiceInquiry.fCalculatedAmount, scope: this,
                handler: function (event, toolEl, panelHeader) {
                    this.scope.calculate.call(this.scope.mainForm, conf);
                }
            },
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
            title: _lang.FlowServiceInquiry.fDestinationCharges,
            forceFit: false,
            scope: this,
            width: 'auto',
            height: conf.defHeight-3,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            displayAllPrice: true,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id', 'itemId','itemCnName','itemEnName','unitId','unitCnName','unitEnName',
                'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd' ,'prevPriceGp20Aud', 'prevPriceGp20Rmb', 'prevPriceGp20Usd' ,
                'priceGp40Aud','priceGp40Rmb','priceGp40Usd','prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd',
                'priceHq40Aud','priceHq40Rmb','priceHq40Usd','prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd',
                'priceLclAud','priceLclRmb','priceLclUsd','prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd',
                'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd',
                'gp20Qty','gp40Qty','hq40Qty','lclCbm', 'rowTotalQty',
                'priceOtherAud','priceOtherRmb','priceOtherUsd', 'prevPriceOtherAud','prevPriceOtherRmb','prevPriceOtherUsd',
                'priceTotalAud','priceTotalRmb','priceTotalUsd', 'prevPriceTotalAud','prevPriceTotalRmb','prevPriceTotalUsd'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fChargeItem, dataIndex: 'itemId', width: 120, locked:true,
                    renderer: function(value){
                        var $items = _dict.chargeItem;
                        if($items.length>0 && $items[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $items[0].data.options,[]);
                        }
                    }
                },
                { header: _lang.FlowServiceInquiry.fUnit, dataIndex: 'unitId', width: 100, locked:true,
                    renderer: function(value){
                        var $items = _dict.chargeUnit;
                        if($items.length>0 && $items[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $items[0].data.options,[]);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeWithOldColumns(this,'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd', function (row, value) {
                        this.up().scope.autoCountCharge.call(this, row);
                    }, {gridId: conf.subGridPanelId})
                },

                //其他费用
                { header: _lang.FlowServiceInquiry.fPriceOther, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this, 'priceOtherAud','priceOtherRmb','priceOtherUsd', 'prevPriceOtherAud','prevPriceOtherRmb','prevPriceOtherUsd', function (row, value) {
                        this.up().grid.up().scope.autoCountCharge.call(this, row);
                    }, {gridId: conf.subGridPanelId})}
                ]},

                //价格
                { header: _lang.FlowServiceInquiry.fPriceGp20, columns: [
                        { header: _lang.FlowServiceInquiry.fPrice, columns: $groupPriceWithOldColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd', 'prevPriceGp20Aud','prevPriceGp20Rmb','prevPriceGp20Usd', function (row, value) {
                                this.up().grid.up().scope.autoCountCharge.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty',align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')}
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceGp40, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this,  'priceGp40Aud','priceGp40Rmb','priceGp40Usd',  'prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd', function (row, value) {
                            this.up().grid.up().scope.autoCountCharge.call(this, row);
                        }, {gridId: conf.subGridPanelId})
                    },
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},

                { header: _lang.FlowServiceInquiry.fPriceHq40, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd', 'prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd', function (row, value) {
                            this.up().grid.up().scope.autoCountCharge.call(this, row);
                        }, {gridId: conf.subGridPanelId})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
                { header: _lang.FlowServiceInquiry.fPriceLcl, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd', 'prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd', function (row, value) {
                            this.up().grid.up().scope.autoCountCharge.call(this, row);
                        }, {gridId: conf.subGridPanelId})},
                    { header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0.00')}
                ]},
                { header: _lang.FlowServiceInquiry.fPriceTotal, columns:[
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', 'prevPriceTotalAud','prevPriceTotalRmb','prevPriceTotalUsd', null, {gridId: conf.subGridPanelId, edit:false})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'rowTotalQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
            ]// end of columns
        });
    },

    autoCountCharge: function( row , refreshAll){
        var refreshAll = refreshAll || false;
        var $data = {
            rateAudToAud:  1,
            rateAudToRmb:  row.get('rateAudToRmb') || 0,
            rateAudToUsd:  row.get('rateAudToUsd') || 0,
            gp20Qty:  row.get('gp20Qty') || 0,
            gp40Qty:  row.get('gp40Qty') || 0,
            hq40Qty:  row.get('hq40Qty') || 0,
            lclQty:  row.get('lclCbm') || 0,
        };

        if(refreshAll){
            row.set('priceGp20Rmb', (row.get('priceGp20Aud') || 0 )* $data.rateAudToRmb);
            row.set('priceGp20Usd', (row.get('priceGp20Aud') || 0 )* $data.rateAudToUsd);

            row.set('priceGp40Rmb', (row.get('priceGp40Aud') || 0 )* $data.rateAudToRmb);
            row.set('priceGp40Usd', (row.get('priceGp40Aud') || 0 )* $data.rateAudToUsd);

            row.set('priceHq40Rmb', (row.get('priceHq40Aud') || 0 )* $data.rateAudToRmb);
            row.set('priceHq40Usd', (row.get('priceHq40Aud') || 0 )* $data.rateAudToUsd);

            row.set('priceLclRmb', (row.get('priceLclAud') || 0 )* $data.rateAudToRmb);
            row.set('priceLclUsd', (row.get('priceLclAud') || 0 )* $data.rateAudToUsd);

            row.set('priceOtherRmb', (row.get('priceOtherAud') || 0 )* $data.rateAudToRmb);
            row.set('priceOtherUsd', (row.get('priceOtherAud') || 0 )* $data.rateAudToUsd);
        }

        $data.priceGp20Aud=((row.get('priceGp20Aud')||0)  * $data.gp20Qty) || 0;
        $data.priceGp20Rmb=((row.get('priceGp20Rmb')||0)  * $data.gp20Qty)|| 0;
        $data.priceGp20Usd=((row.get('priceGp20Usd')||0)  * $data.gp20Qty) || 0;

        $data.priceGp40Aud=  (row.get('priceGp40Aud') * $data.gp40Qty) || 0;
        $data.priceGp40Rmb=  (row.get('priceGp40Rmb') * $data.gp40Qty) || 0;
        $data.priceGp40Usd=  (row.get('priceGp40Usd') * $data.gp40Qty) || 0;

        $data.priceHq40Aud=  (row.get('priceHq40Aud') * $data.hq40Qty) || 0;
        $data.priceHq40Rmb=  (row.get('priceHq40Rmb') * $data.hq40Qty)|| 0;
        $data.priceHq40Usd=  (row.get('priceHq40Usd') * $data.hq40Qty)|| 0;

        $data.priceLclAud=  (row.get('priceLclAud') * $data.lclCbm) || 0;
        $data.priceLclRmb=  (row.get('priceLclRmb') * $data.lclCbm) || 0;
        $data.priceLclUsd=  (row.get('priceLclUsd') * $data.lclCbm) || 0;

        $data.priceOtherAud=  row.get('priceOtherAud') || 0;
        $data.priceOtherRmb=  row.get('priceOtherRmb') || 0;
        $data.priceOtherUsd=  row.get('priceOtherUsd') || 0;

        $data.rowTotalQty = parseInt($data.gp20Qty) + parseInt($data.gp40Qty) + parseInt($data.hq40Qty);

        var currency = ['Aud', 'Rmb', 'Usd'];
        for(var i = 0; i < currency.length; i++){
            var suffix = currency[i];
            //total
            if(row.get('unitId') == 1){
                $data['priceTotal' + suffix] = (((row.get('priceGp20' + suffix)||0)  * $data.gp20Qty) || 0) + (((row.get('priceGp20Insurance' + suffix)||0) * $data.gp20Qty) || 0)
                    + (((row.get('priceGp40' + suffix)||0)  * $data.gp40Qty) || 0) + (((row.get('priceGp40Insurance' + suffix)||0) * $data.gp40Qty) || 0)
                    + (((row.get('priceHq40' + suffix)||0)  * $data.hq40Qty) || 0) + (((row.get('priceHq0Insurance' + suffix)||0) * $data.hq40Qty) || 0)
                    + (((row.get('priceLcl' + suffix)||0)  * $data.lclCbm) || 0) + (((row.get('priceLclInsurance' + suffix)||0) * $data.lclCbm) || 0);
            } else {
                $data['priceTotal' + suffix] = $data['priceOther' + suffix];
            }

            //previous total
            if(row.get('unitId') == 1){
                $data['prevPriceTotal' + suffix] = (((row.get('prevPriceGp20' + suffix)||0)  * $data.gp20Qty) || 0) + (((row.get('prevPriceGp20Insurance' + suffix)||0) * $data.gp20Qty) || 0)
                    + (((row.get('prevPriceGp40' + suffix)||0)  * $data.gp40Qty) || 0) + (((row.get('prevPriceGp40Insurance' + suffix)||0) * $data.gp40Qty) || 0)
                    + (((row.get('prevPriceHq40' + suffix)||0)  * $data.hq40Qty) || 0) + (((row.get('prevPriceHq0Insurance' + suffix)||0) * $data.hq40Qty) || 0)
                    + (((row.get('prevPriceLcl' + suffix)||0)  * $data.lclCbm) || 0) + (((row.get('prevPriceLclInsurance' + suffix)||0) * $data.lclCbm) || 0);
            } else {
                $data['prevPriceTotal' + suffix] = $data['priceOther' + suffix];
            }
        }

        row.data['priceTotalAud'] = $data.priceTotalAud;
        row.data['priceTotalRmb'] = $data.priceTotalRmb;
        row.data['priceTotalUsd'] = $data.priceTotalUsd;
        row.data['rowTotalQty'] = $data.rowTotalQty;

        row.data['prevPriceTotalAud'] = $data.prevPriceTotalAud;
        row.data['prevPriceTotalRmb'] = $data.prevPriceTotalRmb;
        row.data['prevPriceTotalUsd'] = $data.prevPriceTotalUsd;
    }
});
