Ext.define('App.ProductCostFormFreightChargeGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCostFormFreightChargeGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ProductCost.mTitle,
            moduleName: 'ProductCost',
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.frameId + '-ProductCostFormFreightMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.frameId + '-ProductCostFormFreightMultiForm';
        conf.subGridPanelId = this.subGridPanelId || conf.mainGridPanelId +'-ProductCostFormFreightMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || conf.mainGridPanelId +'-ProductCostFormFreightMultiFormPanelID';
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);
        
        App.ProductCostFormFreightChargeGrid.superclass.constructor.call(this, {
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
            bbar: false,
            header:{
                cls:'x-panel-header-gray'
            },
            displayAllPrice: true,
            fields: [
                'id', 'originPortId', 'destinationPortId', 'rateAudToRmb','rateAudToUsd',
                'priceGp20Aud','priceGp20Rmb','priceGp20Usd','priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd',
                'priceGp40Aud','priceGp40Rmb','priceGp40Usd','priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd',
                'priceHq40Aud','priceHq40Rmb','priceHq40Usd','priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd',
                'priceLclAud','priceLclRmb','priceLclUsd','priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',
                'priceTotalAud','priceTotalRmb','priceTotalUsd',
                'gp20Qty', 'gp40Qty', 'hq40Qty', 'lclCbm', 'rowTotalQty'
            ],
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
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd', function (row, value) {
                                this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                            },
                            {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd', function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        },  {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceGp40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceGp40Aud','priceGp40Rmb','priceGp40Usd',  function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        }, {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd' , function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        }, {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceHq40,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd', function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        },  {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd',  function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        },   {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')},
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceLcl,
                    columns: [
                        { header: _lang.FlowServiceInquiry.fPriceBaseOcean, columns: new $groupPriceColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd',  function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        },    {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceInsurance, columns: new $groupPriceColumns(this, 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',  function (row, value) {
                            this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                        },  {gridId : conf.subGridPanelId,})},
                        { header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0.00')},
                    ]
                },
                { header: _lang.FlowFeeRegistration.fSubtotal, columns:[
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd',  function (row, value) {
                        this.up().grid.up().scope.autoCountFreight.call(this, scope, row);
                    },  {edit: false, gridId : conf.subGridPanelId,})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'rowTotalQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
            ]// end of columns
        });
	},

    autoCountFreight: function(scope, row){
        var data = {
            rateAudToAud:  1,
            rateAudToRmb:  row.get('rateAudToRmb') || 0,
            rateAudToUsd:  row.get('rateAudToUsd') || 0,
            gp20Qty:  row.get('gp20Qty') || 0,
            gp40Qty:  row.get('gp40Qty') || 0,
            hq40Qty:  row.get('hq40Qty') || 0,
            lclCbm:  row.get('lclCbm') || 0,
        };

        data.priceGp20Aud=((row.get('priceGp20Aud')||0)  * data.gp20Qty) || 0;
        data.priceGp20Rmb=((row.get('priceGp20Rmb')||0)  * data.gp20Qty)|| 0;
        data.priceGp20Usd=((row.get('priceGp20Usd')||0)  * data.gp20Qty) || 0;
        data.priceGp20InsuranceAud=((row.get('priceGp20InsuranceAud')||0) * data.gp20Qty) || 0;
        data.priceGp20InsuranceRmb=((row.get('priceGp20InsuranceRmb')||0) * data.gp20Qty)|| 0;
        data.priceGp20InsuranceUsd=((row.get('priceGp20InsuranceUsd')||0) * data.gp20Qty) || 0;

        data.priceGp40Aud=  (row.get('priceGp40Aud') * data.gp40Qty) || 0;
        data.priceGp40Rmb=  (row.get('priceGp40Rmb') * data.gp40Qty) || 0;
        data.priceGp40Usd=  (row.get('priceGp40Usd') * data.gp40Qty) || 0;
        data.priceGp40InsuranceAud=  (row.get('priceGp40InsuranceAud') * data.gp40Qty) || 0;
        data.priceGp40InsuranceRmb=  (row.get('priceGp40InsuranceRmb') * data.gp40Qty) || 0;
        data.priceGp40InsuranceUsd=  (row.get('priceGp40InsuranceUsd') * data.gp40Qty) || 0;

        data.priceHq40Aud=  (row.get('priceHq40Aud') * data.hq40Qty) || 0;
        data.priceHq40Rmb=  (row.get('priceHq40Rmb') * data.hq40Qty)|| 0;
        data.priceHq40Usd=  (row.get('priceHq40Usd') * data.hq40Qty)|| 0;
        data.priceHq40InsuranceAud=  (row.get('priceHq40InsuranceAud') * data.hq40Qty) || 0;
        data.priceHq40InsuranceRmb=  (row.get('priceHq40InsuranceRmb') * data.hq40Qty)|| 0;
        data.priceHq40InsuranceUsd=  (row.get('priceHq40InsuranceUsd') * data.hq40Qty)|| 0;

        data.priceLclAud=  (row.get('priceLclAud') * data.lclCbm) || 0;
        data.priceLclRmb=  (row.get('priceLclRmb') * data.lclCbm) || 0;
        data.priceLclUsd=  (row.get('priceLclUsd') * data.lclCbm) || 0;
        data.priceLclInsuranceAud=  (row.get('priceLclInsuranceAud') * data.lclCbm) || 0;
        data.priceLclInsuranceRmb=  (row.get('priceLclInsuranceRmb') * data.lclCbm) || 0;
        data.priceLclInsuranceUsd=  (row.get('priceLclInsuranceUsd') * data.lclCbm) || 0;

        data.priceTotalAud = data.priceGp20Aud +data.priceGp20InsuranceAud + data.priceGp40Aud  + data.priceGp40InsuranceAud + data.priceHq40Aud + data.priceHq40InsuranceAud + data.priceLclAud + data.priceLclInsuranceAud;
        data.priceTotalRmb = data.priceGp20Rmb +data.priceGp20InsuranceRmb + data.priceGp40Rmb  + data.priceGp40InsuranceRmb + data.priceHq40Rmb + data.priceHq40InsuranceRmb + data.priceLclRmb + data.priceLclInsuranceRmb;
        data.priceTotalUsd = data.priceGp20Usd +data.priceGp20InsuranceUsd + data.priceGp40Usd  + data.priceGp40InsuranceUsd + data.priceHq40Usd + data.priceHq40InsuranceUsd + data.priceLclUsd + data.priceLclInsuranceUsd;

        data.rowTotalQty = parseInt(data.gp20Qty) + parseInt(data.gp40Qty) + parseInt(data.hq40Qty);

        row.data['priceTotalAud'] = data.priceTotalAud;
        row.data['priceTotalRmb'] = data.priceTotalRmb;
        row.data['priceTotalUsd'] = data.priceTotalUsd;
        row.data['rowTotalQty'] = data.rowTotalQty;

        scope.updateSummary.call(scope);
    },

    updateSummary: function(form){
        if(!form) form = Ext.getCmp(this.mainFormPanelId);
        var range = this.subGridPanel.getStore().getRange();
        var sumAud = 0, sumUsd = 0, sumRmb = 0;
        var containerQty = 0;
        var lclCbm = 0;
        var vendorNumber = 0;
        var orderQty = 0;
        for(var i = 0; i < range.length; i++){
            var row = range[i].data;
            sumAud += parseFloat(row.priceTotalAud || 0);
            sumUsd += parseFloat(row.priceTotalUsd || 0);
            sumRmb += parseFloat(row.priceTotalRmb || 0);

            containerQty += parseInt(row.gp20Qty || 0) + parseInt(row.gp40Qty || 0) + parseInt(row.hq40Qty || 0);
            lclCbm += parseFloat(row.lclCbm || 0);
        }

        var form = Ext.getCmp(this.mainFormPanelId);
        form.getCmpByName('calFreightSumAud').setValue(sumAud.toFixed(2));
        form.getCmpByName('calFreightSumRmb').setValue(sumRmb.toFixed(2));
        form.getCmpByName('calFreightSumUsd').setValue(sumUsd.toFixed(2));
        form.getCmpByName('calContainerQty').setValue(containerQty);
        form.getCmpByName('calLclCbm').setValue(lclCbm);
    }
});
