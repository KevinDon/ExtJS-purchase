Ext.define('App.ServiceProviderInvoiceFreightChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ServiceProviderInvoiceFreightChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ServiceProviderInvoice.mTitle,
            moduleName: 'ServiceInvoice',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ServiceProviderInvoiceFreightMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ServiceProviderInvoiceFreightMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceFreightMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ServiceProviderInvoiceFreightMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ServiceProviderInvoiceFreightChargeGrid.superclass.constructor.call(this, {
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
            title: _lang.FlowServiceInquiry.fFreightCharges,
            forceFit: false,
            scope: this,
            width: 'auto',
            height:conf.defHeight -3 ,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            displayAllPrice: true,
            fields: [
                'id', 'originPortId','originPortCnName','originPortEnName', 'destinationPortId', 'destinationPortCnName','destinationPortEnName',
                'sailingDays','frequency','priceBaseOceanUsd','priceInsuranceAud','priceInsuranceRmb','priceInsuranceUsd',
                'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd' ,'priceGp20InsuranceAud', 'priceGp20InsuranceRmb', 'priceGp20InsuranceUsd',
                'prevPriceGp20Aud', 'prevPriceGp20Rmb', 'prevPriceGp20Usd' ,'prevPriceGp20InsuranceAud', 'prevPriceGp20InsuranceRmb', 'prevPriceGp20InsuranceUsd',
                'priceGp40Aud','priceGp40Rmb','priceGp40Usd', 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd',
                'prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd', 'prevPriceGp40InsuranceAud','prevPriceGp40InsuranceRmb','prevPriceGp40InsuranceUsd',
                'priceHq40Aud','priceHq40Rmb','priceHq40Usd', 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd',
                'prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd', 'prevPriceHq40InsuranceAud','prevPriceHq40InsuranceRmb','prevPriceHq40InsuranceUsd',
                'priceLclAud','priceLclRmb','priceLclUsd', 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',
                'prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd', 'prevPriceLclInsuranceAud','prevPriceLclInsuranceRmb','prevPriceLclInsuranceUsd',
                'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd',
                'gp20Qty','gp40Qty','hq40Qty','lclCbm',
                'prevSailingDays', 'prevFrequency',
                'priceTotalAud','priceTotalRmb','priceTotalUsd', 'prevPriceTotalAud','prevPriceTotalRmb','prevPriceTotalUsd', 'rowTotalQty'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 100, locked:true,
                    renderer: function(value){
                        var $origin = _dict.origin;
                        if($origin.length>0 && $origin[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $origin[0].data.options, []);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeWithOldColumns(this,'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd', function (row, value) {
                        this.up().scope.autoCountFreight.call(this, row);
                    }, {gridId: conf.subGridPanelId})
                },

                { header: _lang.FlowServiceInquiry.fSailingDays, columns: [
                    { header: _lang.TText.fNew, dataIndex: 'sailingDays', width: 50, editor: {xtype: 'numberfield', minValue: 0,} },
                    { header: _lang.TText.fOld, dataIndex: 'prevSailingDays', width: 50}
                ]},
                { header: _lang.FlowServiceInquiry.fFrequency, columns: [
                    {header: _lang.TText.fNew, dataIndex: 'frequency', width: 50, editor:{xtype: 'numberfield', minValue: 0,} },
                    {header: _lang.TText.fOld, dataIndex: 'prevFrequency', width: 50},
                ]},
                { header: _lang.FlowServiceInquiry.fPriceGp20,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceWithOldColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd', 'prevPriceGp20Aud','prevPriceGp20Rmb','prevPriceGp20Usd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceWithOldColumns(this, 'priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd', 'prevPriceGp20InsuranceAud','prevPriceGp20InsuranceRmb','prevPriceGp20InsuranceUsd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty', align: 'right', width: 60,
                            editor: {xtype: 'numberfield', minValue: 0,
                                listeners:{
                                    change:function(pt, newValue, oldValue, eOpts ) {
                                        var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                        row.set('gp20Qty', newValue);
                                        this.up().grid.up().scope.autoCountFreight.call(this, row, newValue);
                                    }
                                }
                            },renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceGp40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceWithOldColumns(this,  'priceGp40Aud','priceGp40Rmb','priceGp40Usd',  'prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceWithOldColumns(this, 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd', 'prevPriceGp40InsuranceAud','prevPriceGp40InsuranceRmb','prevPriceGp40InsuranceUsd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty',align: 'right', width: 60,
                            editor: {xtype: 'numberfield', minValue: 0,
                                listeners:{
                                    change:function(pt, newValue, oldValue, eOpts ) {
                                        var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                        row.set('gp40Qty', newValue);
                                        this.up().grid.up().scope.autoCountFreight.call(this, row, newValue);
                                    }
                                }
                            },renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceHq40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceWithOldColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd', 'prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceWithOldColumns(this, 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd', 'prevPriceHq40InsuranceAud','prevPriceHq40InsuranceRmb','prevPriceHq40InsuranceUsd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty',align: 'right', width: 60,
                            editor: {xtype: 'numberfield', minValue: 0,
                                listeners:{
                                    change:function(pt, newValue, oldValue, eOpts ) {
                                        var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                        row.set('hq40Qty', newValue);
                                        this.up().grid.up().scope.autoCountFreight.call(this, row, newValue);
                                    }
                                }
                            },renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceLcl,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceWithOldColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd', 'prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceWithOldColumns(this, 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd', 'prevPriceLclInsuranceAud','prevPriceLclInsuranceRmb','prevPriceLclInsuranceUsd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, row);
                            }, {gridId: conf.subGridPanelId})
                        },
                        { header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm',align: 'right', width: 60,
                            editor: {xtype: 'numberfield', minValue: 0,
                                listeners:{
                                    change:function(pt, newValue, oldValue, eOpts ) {

                                        var totalQty = 0;

                                        var row = this.up().grid.getSelectionModel().selected.getAt(0);
                                        row.set('lclCbm', newValue);
                                        this.up().grid.up().scope.autoCountFreight.call(this, row, newValue);
                                    }
                                }
                            },renderer: Ext.util.Format.numberRenderer('0.00')
                        }
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceTotal, columns:[
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceWithOldColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', 'prevPriceTotalAud','prevPriceTotalRmb','prevPriceTotalUsd', null, {gridId: conf.subGridPanelId, edit:false})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'rowTotalQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
            ]// end of columns
        });
	},

    autoCountFreight: function( row , refreshAll){
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
            row.set('priceGp20InsuranceRmb', (row.get('priceGp20InsuranceAud') || 0 )* $data.rateAudToRmb);
            row.set('priceGp20InsuranceUsd', (row.get('priceGp20InsuranceAud') || 0 )* $data.rateAudToUsd);

            row.set('priceGp40Rmb', (row.get('priceGp40Aud') || 0 )* $data.rateAudToRmb);
            row.set('priceGp40Usd', (row.get('priceGp40Aud') || 0 )* $data.rateAudToUsd);
            row.set('priceGp40InsuranceRmb', (row.get('priceGp40InsuranceAud') || 0 )* $data.rateAudToRmb);
            row.set('priceGp40InsuranceUsd', (row.get('priceGp40InsuranceAud') || 0 )* $data.rateAudToUsd);

            row.set('priceHq40Rmb', (row.get('priceHq40Aud') || 0 )* $data.rateAudToRmb);
            row.set('priceHq40Usd', (row.get('priceHq40Aud') || 0 )* $data.rateAudToUsd);
            row.set('priceHq40InsuranceRmb', (row.get('priceHq40InsuranceAud') || 0 )* $data.rateAudToRmb);
            row.set('priceHq40InsuranceUsd', (row.get('priceHq40InsuranceAud') || 0 )* $data.rateAudToUsd);

            row.set('priceLclRmb', (row.get('priceLclAud') || 0 )* $data.rateAudToRmb);
            row.set('priceLclUsd', (row.get('priceLclAud') || 0 )* $data.rateAudToUsd);
            row.set('priceLclInsuranceRmb', (row.get('priceLclInsuranceAud') || 0 )* $data.rateAudToRmb);
            row.set('priceLclInsuranceUsd', (row.get('priceLclInsuranceAud') || 0 )* $data.rateAudToUsd);
        }

        $data.priceGp20Aud=((row.get('priceGp20Aud')||0)  * $data.gp20Qty) || 0;
        $data.priceGp20Rmb=((row.get('priceGp20Rmb')||0)  * $data.gp20Qty)|| 0;
        $data.priceGp20Usd=((row.get('priceGp20Usd')||0)  * $data.gp20Qty) || 0;
        $data.priceGp20InsuranceAud=((row.get('priceGp20InsuranceAud')||0) * $data.gp20Qty) || 0;
        $data.priceGp20InsuranceRmb=((row.get('priceGp20InsuranceRmb')||0) * $data.gp20Qty)|| 0;
        $data.priceGp20InsuranceUsd=((row.get('priceGp20InsuranceUsd')||0) * $data.gp20Qty) || 0;

        $data.priceGp40Aud=  (row.get('priceGp40Aud') * $data.gp40Qty) || 0;
        $data.priceGp40Rmb=  (row.get('priceGp40Rmb') * $data.gp40Qty) || 0;
        $data.priceGp40Usd=  (row.get('priceGp40Usd') * $data.gp40Qty) || 0;
        $data.priceGp40InsuranceAud=  (row.get('priceGp40InsuranceAud') * $data.gp40Qty) || 0;
        $data.priceGp40InsuranceRmb=  (row.get('priceGp40InsuranceRmb') * $data.gp40Qty) || 0;
        $data.priceGp40InsuranceUsd=  (row.get('priceGp40InsuranceUsd') * $data.gp40Qty) || 0;

        $data.priceHq40Aud=  (row.get('priceHq40Aud') * $data.hq40Qty) || 0;
        $data.priceHq40Rmb=  (row.get('priceHq40Rmb') * $data.hq40Qty)|| 0;
        $data.priceHq40Usd=  (row.get('priceHq40Usd') * $data.hq40Qty)|| 0;
        $data.priceHq40InsuranceAud=  (row.get('priceHq40InsuranceAud') * $data.hq40Qty) || 0;
        $data.priceHq40InsuranceRmb=  (row.get('priceHq40InsuranceRmb') * $data.hq40Qty)|| 0;
        $data.priceHq40InsuranceUsd=  (row.get('priceHq40InsuranceUsd') * $data.hq40Qty)|| 0;

        $data.priceLclAud=  (row.get('priceLclAud') * $data.lclCbm) || 0;
        $data.priceLclRmb=  (row.get('priceLclRmb') * $data.lclCbm) || 0;
        $data.priceLclUsd=  (row.get('priceLclUsd') * $data.lclCbm) || 0;
        $data.priceLclInsuranceAud=  (row.get('priceLclInsuranceAud') * $data.lclCbm) || 0;
        $data.priceLclInsuranceRmb=  (row.get('priceLclInsuranceRmb') * $data.lclCbm) || 0;
        $data.priceLclInsuranceUsd=  (row.get('priceLclInsuranceUsd') * $data.lclCbm) || 0;

        $data.priceTotalAud = $data.priceGp20Aud +$data.priceGp20InsuranceAud + $data.priceGp40Aud  + $data.priceGp40InsuranceAud + $data.priceHq40Aud + $data.priceHq40InsuranceAud + $data.priceLclAud + $data.priceLclInsuranceAud;
        $data.priceTotalRmb = $data.priceGp20Rmb +$data.priceGp20InsuranceRmb + $data.priceGp40Rmb  + $data.priceGp40InsuranceRmb + $data.priceHq40Rmb + $data.priceHq40InsuranceRmb + $data.priceLclRmb + $data.priceLclInsuranceRmb;
        $data.priceTotalUsd = $data.priceGp20Usd +$data.priceGp20InsuranceUsd + $data.priceGp40Usd  + $data.priceGp40InsuranceUsd + $data.priceHq40Usd + $data.priceHq40InsuranceUsd + $data.priceLclUsd + $data.priceLclInsuranceUsd;

        $data.rowTotalQty = parseInt($data.gp20Qty) + parseInt($data.gp40Qty) + parseInt($data.hq40Qty);

        row.data['priceTotalAud'] = $data.priceTotalAud;
        row.data['priceTotalRmb'] = $data.priceTotalRmb;
        row.data['priceTotalUsd'] = $data.priceTotalUsd;
        row.data['rowTotalQty'] = $data.rowTotalQty;

        //previous total
        var currency = ['Aud', 'Rmb', 'Usd'];
        for(var i = 0; i < currency.length; i++){
            var suffix = currency[i];
            $data['prevPriceTotal' + suffix] = (((row.get('prevPriceGp20' + suffix)||0)  * $data.gp20Qty) || 0) + (((row.get('prevPriceGp20Insurance' + suffix)||0) * $data.gp20Qty) || 0)
                + (((row.get('prevPriceGp40' + suffix)||0)  * $data.gp40Qty) || 0) + (((row.get('prevPriceGp40Insurance' + suffix)||0) * $data.gp40Qty) || 0)
                + (((row.get('prevPriceHq40' + suffix)||0)  * $data.hq40Qty) || 0) + (((row.get('prevPriceHq0Insurance' + suffix)||0) * $data.hq40Qty) || 0)
                + (((row.get('prevPriceLcl' + suffix)||0)  * $data.lclCbm) || 0) + (((row.get('prevPriceLclInsurance' + suffix)||0) * $data.lclCbm) || 0);
        }

        row.data['prevPriceTotalAud'] = $data.prevPriceTotalAud;
        row.data['prevPriceTotalRmb'] = $data.prevPriceTotalRmb;
        row.data['prevPriceTotalUsd'] = $data.prevPriceTotalUsd;
    }
});
