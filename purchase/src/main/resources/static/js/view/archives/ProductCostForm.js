ProductCostForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove = this.action != 'add' && this.action != 'copy' ? true: false;
        //调试用
        // this.isApprove = false;

        var conf = {
            title : _lang.ProductCost.mTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
            moduleName: 'ProductCost',
            winId : 'ProductCostForm',
            frameId : 'ProductCostView',
            mainGridPanelId : 'ProductCostViewGridPanelID',
            mainFormPanelId : 'ProductCostViewFormPanelID',
            processFormPanelId: 'ProductCostProcessFormPanelID',
            urlList: __ctxPath + 'archives/cost/list',
            urlSave: __ctxPath + 'archives/cost/save',
            urlDelete: __ctxPath + 'archives/cost/delete',
            urlGet: __ctxPath + 'archives/cost/get',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            actionName: this.action,
            flowStart: !this.isApprove && this.record.flowStatus != 0,
            save: true,
            close: true,
            saveFun: this.saveFun,
            scope: this
        };

        Ext.applyIf(this, conf);
        this.initUIComponents(conf);
        ProductCostForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            cls: 'gb-blank',
            tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });

        this.containerTypes = ['Gp20', 'Gp40', 'Hq40', 'Lcl'];
        this.currency = ['Aud', 'Rmb', 'Usd'];
        this.perContainerUnitId = 1;
        this.perJobUnitId = 2;
        this.perVendorUnitId = 5;
        this.priceDecimalPrecision = 2;
        this.costOrder = {};        //每个订单成本汇总
        this.orderCbm = {};         //每个订单的订单总体积
        this.actualOrderCbm = {};   //每个订单的实际船运体积
    },

    initUIComponents: function(conf) {
        var scope = this;
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                {field:'main.orderShippingPlanBusinessId', xtype:'hidden'},
                { xtype: 'container',cls:'col-2', items:  [
                    { field: 'main.orderShippingPlanId', xtype:'hidden'},
                    { field: 'main_shippingPlanName', xtype: 'OrderShippingPlanDialog', fieldLabel: _lang.FlowServiceInquiry.fShippingPlanId, cls:'col-1',
                        formId: conf.mainFormPanelId, hiddenName: 'main.orderShippingPlanId', single: true, primaryTable:true, allowBlank: false, type:2, readOnly: this.isApprove,
                        subcallback: function(records){
                            if(!records) return;
                            scope.editFormPanel.getCmpByName('main.orderShippingPlanBusinessId').setValue(records[0].raw.businessId);
                            scope.editFormPanel.getCmpByName('main_shippingPlanName').setValue(records[0].raw.businessId);
                            scope.loadOrderShippingPlanData.call(scope, records[0].raw.id, records[0].raw.businessId);
                        }
                    },
                ]},
                {xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.retd', xtype: 'datetimefield', fieldLabel: _lang.ProductDocument.fRetd, cls:'col-2', format: curUserInfo.dateFormat, allowBlank:false,},
                        { field: 'main.reta', xtype: 'datetimefield', fieldLabel: _lang.ProductDocument.fReta, cls:'col-2', format: curUserInfo.dateFormat, allowBlank:false,},
                    ]},
                    { xtype: 'container',cls:'col-2', items:  [
                        {field: 'main.rateAudToRmb', xtype: 'textfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb,cls:'col-2', allowBlank:false,
                            mainFormPanelId: conf.mainFormPanelId,
                            listeners :{change: function( newValue, oldValue, eOpts ) {scope.changeExchange.call(this, newValue, oldValue, eOpts);}}
                        },
                        {field: 'main.rateAudToUsd', xtype: 'textfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb,cls:'col-2', allowBlank:false,
                            mainFormPanelId: conf.mainFormPanelId,
                            listeners :{change: function( newValue, oldValue, eOpts ) {scope.changeExchange.call(this, newValue, oldValue, eOpts);}}
                        },
                    ]},
                ]},


                { field: 'orderCbm', xtype: 'ProductCostFormOrderCbmGrid', fieldLabel: _lang.ProductCost.fOrderCbm, readOnly: this.isApprove, },
                { field: 'payment', xtype: 'ProductCostFormPaymentGrid', fieldLabel: _lang.ProductCost.tabPayment, mainFormPanelId: conf.mainFormPanelId},
                { xtype: 'section', title:_lang.ProductCost.tabPaymentSummary},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-2', items:  [
                        { xtype: 'container',cls:'col-2', items:  [
                            { field: 'paymentSumAudToRmb', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb, cls: 'col-1', readOnly:true},
                            { field: 'paymentSumAudToUsd', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-1', readOnly:true}
                        ]},
                        { xtype: 'container',cls:'col-2', items:  [
                            { field: 'paymentSumAud', xtype: 'displayfield', value: 0, fieldLabel: _lang.ProductDocument.fTotalPriceAud, cls: 'col-1', readOnly:true},
                            { field: 'paymentSumRmb', xtype: 'displayfield', value: 0, fieldLabel: _lang.ProductDocument.fTotalPriceRmb, cls: 'col-1', readOnly:true },
                            { field: 'paymentSumUsd', xtype: 'displayfield', value: 0, fieldLabel: _lang.ProductDocument.fTotalPriceUsd, cls: 'col-1',  readOnly:true}
                        ]},
                    ]},
                    { xtype: 'container',cls:'col-2', items:  [
                        { xtype: 'container',cls:'col-2', items:  [
                            { field: 'paymentGstAudToRmb', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb, cls: 'col-1', readOnly:true},
                            { field: 'paymentGstAudToUsd', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-1', readOnly:true}
                        ]},
                        {xtype: 'container', cls: 'col-2', items:$groupPriceFields(scope,
                                {decimalPrecision: 2,cls: 'col-1',readOnly: true,allowBlank: true,updatePriceFlag: 0,
                                    aud: {field: 'valueGstAud', fieldLabel: _lang.ProductCost.fValueGstAud},
                                    rmb: {field: 'valueGstRmb', fieldLabel: _lang.ProductCost.fValueGstRmb},
                                    usd: {field: 'valueGstUsd', fieldLabel: _lang.ProductCost.fValueGstUsd}
                                })
                        }
                    ]}
                ]},

                // { xtype: 'section', title:_lang.ProductCost.tabFreight},
                { field: 'freightCharge', xtype: 'ProductCostFormFreightChargeGrid', readOnly: this.isApprove,
                    frameId: conf.mainFormPanelId,
                    mainFormPanelId: conf.mainFormPanelId,
                    fieldLabel: _lang.FlowServiceInquiry.fFreightCharges,
                },

                { xtype: 'section', title:_lang.ProductCost.tabFreightSummary},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'displayfield', allowBlank: true, updatePriceFlag : 1,
                        aud:{field:'calFreightSumAud', fieldLabel:_lang.ProductDocument.fTotalPriceAud, },
                        rmb:{field:'calFreightSumRmb', fieldLabel:_lang.ProductDocument.fTotalPriceRmb,},
                        usd:{field:'calFreightSumUsd', fieldLabel:_lang.ProductDocument.fTotalPriceUsd, }
                    }).concat([
                        { field: 'calContainerQty', xtype: 'displayfield',  fieldLabel: _lang.ProductCost.fContainerQty, cls: 'col-1', readOnly:true},
                        { field: 'calLclCbm', xtype: 'displayfield', fieldLabel: _lang.ProductCost.fLclCbm, cls: 'col-1', readOnly:true},
                    ]).concat([
                        { field: 'vendorNumber', xtype: 'displayfield', fieldLabel: _lang.ProductCost.fVendorQty, cls: 'col-1', readOnly:true},
                        { field: 'orderQty', xtype: 'displayfield',  fieldLabel: _lang.ProductCost.fOrderQty, cls: 'col-1', readOnly:true},
                    ])},
                    { xtype: 'container', cls:'col-2', items: [
                        { xtype: 'container', cls:'col-2', items: [
                            { field: 'freightSumAudToRmb', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb, cls: 'col-1', readOnly:true},
                            { field: 'freightSumAudToUsd', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-1', readOnly:true}
                        ]},
                        {xtype: 'container', cls: 'col-2', items:$groupPriceFields(scope,
                            {decimalPrecision: 2, cls: 'col-1', readOnly: true, allowBlank: true, updatePriceFlag: 0,
                                aud: {field: 'freightSumAud', fieldLabel: _lang.ProductCost.fActualFreightSumAud},
                                rmb: {field: 'freightSumRmb', fieldLabel: _lang.ProductCost.fActualFreightSumRmb},
                                usd: {field: 'freightSumUsd', fieldLabel: _lang.ProductCost.fActualFreightSumUsd}
                            })
                        }
                    ]}
                ]},

                // { xtype: 'section', title:_lang.ProductCost.tabDestination},
                { field: 'destinationCharge', xtype: 'ProductCostFormDestinationChargeGrid', readOnly: this.isApprove,
                    frameId: conf.mainFormPanelId,
                    mainFormPanelId: conf.mainFormPanelId,
                    fieldLabel: _lang.FlowServiceInquiry.tabDestination,
                },

                { xtype: 'section', title:_lang.ProductCost.tabDestinationSummary},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'displayfield', allowBlank: true, updatePriceFlag : 1,
                        aud:{field:'calChargeSumAud', fieldLabel:_lang.ProductDocument.fTotalPriceAud, },
                        rmb:{field:'calChargeSumRmb', fieldLabel:_lang.ProductDocument.fTotalPriceRmb, },
                        usd:{field:'calChargeSumUsd', fieldLabel:_lang.ProductDocument.fTotalPriceUsd, }
                    })},
                    { xtype: 'container', cls:'col-2', items: [
                        { field: 'chargeSumAudToRmb', xtype: 'displayfield', fieldLabel: _lang.NewProductDocument.fExchangeRateAudToRmb, cls: 'col-2', readOnly:true},
                        { field: 'chargeSumAudToUsd', xtype: 'displayfield', fieldLabel:_lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-2', readOnly:true}
                        ].concat(
                            { xtype: 'container', cls:'col-2', items:
                                $groupPriceFields(scope, {
                                    decimalPrecision: 2, cls: 'col-1', readOnly:true, allowBlank: true,  updatePriceFlag : 0,
                                    aud:{field:'chargeSumAud', fieldLabel:_lang.ProductCost.fActualDestinationSumAud},
                                    rmb:{field:'chargeSumRmb', fieldLabel:_lang.ProductCost.fActualDestinationSumRmb},
                                    usd:{field:'chargeSumUsd', fieldLabel:_lang.ProductCost.fActualDestinationSumUsd}
                                })
                            }
                        ).concat(
                            { xtype: 'container', cls:'col-2', items:
                                $groupPriceFields(scope, {
                                    decimalPrecision: 2, cls: 'col-1', readOnly:true, allowBlank: true,  updatePriceFlag : 0,
                                    aud:{field:'localGstAud', fieldLabel:_lang.ProductCost.fLocalGstAud},
                                    rmb:{field:'localGstRmb', fieldLabel:_lang.ProductCost.fLocalGstRmb},
                                    usd:{field:'localGstUsd', fieldLabel:_lang.ProductCost.fLocalGstUsd}
                                })
                            }
                        )
                    }
                ]},


                { field: 'dutyCharge', xtype: 'ProductCostFormDutyChargeGrid', fieldLabel: _lang.ProductCost.tabDuty, readOnly: this.isApprove, mainFormPanelId: conf.mainFormPanelId },
                { xtype: 'section', title:_lang.ProductCost.tabDutySummary},
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        decimalPrecision: 2, cls: 'col-1', readOnly:true, type:'displayfield', allowBlank: true, updatePriceFlag : 1,
                        aud:{field:'calTariffSumAud', fieldLabel:_lang.ProductDocument.fTotalPriceAud, },
                        rmb:{field:'calTariffSumRmb', fieldLabel:_lang.ProductDocument.fTotalPriceRmb, },
                        usd:{field:'calTariffSumUsd', fieldLabel:_lang.ProductDocument.fTotalPriceUsd, }
                    }).concat([
                        //{ field: 'qty3', xtype: 'displayfield', fieldLabel: _lang.ProductCost.fContainerQty, cls: 'col-1', readOnly:true},
                       // { field: 'qty4', xtype: 'displayfield', fieldLabel: _lang.ProductCost.fLclCbm, cls: 'col-1', readOnly:true},
                    ])},
                    { xtype: 'container', cls:'col-2', items: [
                        { xtype: 'container', cls:'col-2', items: [
                            { field: 'tariffSumAudToRmb', xtype: 'displayfield', fieldLabel:  _lang.NewProductDocument.fExchangeRateAudToRmb,  cls: 'col-1', readOnly:true},
                            { field: 'tariffSumAudToUsd', xtype: 'displayfield', fieldLabel:  _lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-1', readOnly:true}
                        ]},
                        {xtype: 'container', cls: 'col-2', items:$groupPriceFields(scope,
                            {decimalPrecision: 2, cls: 'col-1', readOnly:true, allowBlank: true,  updatePriceFlag : 0,
                                aud:{field:'tariffSumAud', fieldLabel:_lang.ProductCost.fActualTariffSumAud},
                                rmb:{field:'tariffSumRmb', fieldLabel:_lang.ProductCost.fActualTariffSumRmb},
                                usd:{field:'tariffSumUsd', fieldLabel:_lang.ProductCost.fActualTariffSumUsd}
                            })
                        }
                    ]}
                ]},

                { xtype: 'section', title:_lang.Duty.fCustomsProcessing},
                {  field: 'section', xtype: 'container', cls: 'row', items: [
                    { field: 'customeProcessingFeeNote', xtype: 'displayfield', value: _lang.ProductCost.fCustomProcessingNote, cls: 'col-2',  readOnly:true}
                ]},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'customProcessingAudToRmb', xtype: 'displayfield', fieldLabel:   _lang.NewProductDocument.fExchangeRateAudToRmb, cls: 'col-3', readOnly:true},
                    { field: 'customProcessingAudToUsd', xtype: 'displayfield', fieldLabel:  _lang.NewProductDocument.fExchangeRateAudToUsd, cls: 'col-3', readOnly:true}
                ] },
                { xtype: 'container', cls:'row', items: $groupPriceFields(scope, {
                    decimalPrecision: 2, readOnly:true, allowBlank: true,  updatePriceFlag : 0, cls: 'col-3',
                    aud:{field:'customProcessingFeeAud', fieldLabel:_lang.ProductCost.fCustomProcessingFeeAud, readOnly: this.isApprove},
                    rmb:{field:'customProcessingFeeRmb', fieldLabel:_lang.ProductCost.fCustomProcessingFeeRmb, readOnly: this.isApprove},
                    usd:{field:'customProcessingFeeUsd', fieldLabel:_lang.ProductCost.fCustomProcessingFeeUsd, readOnly: this.isApprove}
                })},

                { xtype: 'section', title:_lang.ProductCost.tabProductCost},
                {  field: 'section', xtype: 'container', cls: 'row', items: [
                    { field: 'productCostNote', xtype: 'displayfield', value: _lang.ProductCost.fProductCostNote, cls: 'col-2',  readOnly:true}
                ]},
                { field: 'productCost', xtype: 'ProductCostFormCostGrid', fieldLabel: _lang.ProductCost.tabDuty,
                    frameId: conf.mainFormPanelId, scope:this, height:350,
                    mainFormPanelId: conf.mainFormPanelId,
                    calculate: this.calculate
                },

                // { xtype: 'section', title:_lang.ProductCost.tabPurchaseContract},
                { field: 'purchaseContract', xtype: 'ProductCostFormPurchaseContractGrid', fieldLabel: _lang.ProductCost.tabPurchaseContractProductList, readOnly: this.isApprove, },

                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.isApprove
                },

                //创建人信息
                { xtype: 'section', title:_lang.NewProductDocument.tabCreatorInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'applicantName', xtype: 'displayfield', value:curUserInfo.loginname, fieldLabel: _lang.TText.fApplicantName, cls:'col-2', readOnly:true },
                    { field: 'departmentName', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.TText.fAppDepartmentName, cls:'col-2', readOnly:true}
                ] },

                { xtype: 'container',cls:'row', items: [
                    { field: 'createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', readOnly:true},
                    { field: 'status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls:'col-2', readOnly:true,
                        renderer: function(value){
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                            if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                        }
                    }
                ], hidden: !this.isApprove },
            ]
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            //payment
            this.getFee();

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    var cmp = Ext.getCmp(conf.mainFormPanelId);
                    //console.log('ajax');
                    // console.log(json.data);

                    //order shipping plan id
                    cmp.getCmpByName('main_shippingPlanName').setValue(json.data.orderShippingPlanBusinessId);

                    //purchase contract items
                    if(!!json.data.costProducts){
                        var grid = cmp.getCmpByName('purchaseContract').subGridPanel;
                        grid.getStore().add(json.data.costProducts);
                    }
                    if(!!json.data.costOrders){
                        var grid = cmp.getCmpByName('orderCbm').subGridPanel;
                        grid.getStore().add(json.data.costOrders);
                    }
                    //freight charge
                    if(!!json.data.costPorts){
                        var grid = cmp.getCmpByName('freightCharge').subGridPanel;
                        for(var i = 0; i < json.data.costPorts.length; i++){
                            var port = json.data.costPorts[i];
                            for(var j = 0; j < scope.currency.length; j++){
                                var suffix = scope.currency[j];
                                port['priceTotal' + suffix] = parseFloat(port['priceGp20' + suffix] || 0) * parseInt(port['gp20Qty'] || 0)
                                    + parseFloat(port['priceGp40' + suffix] || 0) * parseInt(port['gp40Qty'] || 0)
                                    + parseFloat(port['priceHq40' + suffix] || 0) * parseInt(port['hq40Qty'] || 0)
                                    + parseFloat(port['priceLcl' + suffix] || 0) * parseInt(port['lclCbm'] || 0)
                                    + parseFloat(port['priceGp20Insurance' + suffix] || 0) * parseInt(port['gp20Qty'] || 0)
                                    + parseFloat(port['priceGp40Insurance' + suffix] || 0) * parseInt(port['gp40Qty'] || 0)
                                    + parseFloat(port['priceHq40Insurance' + suffix] || 0) * parseInt(port['hq40Qty'] || 0)
                                    + parseFloat(port['priceLclInsurance' + suffix] || 0) * parseInt(port['lclCbm'] || 0);
                            }

                            port['rowTotalQty'] = parseInt(port['gp20Qty'] || 0) + parseInt(port['gp40Qty'] || 0) + parseInt(port['hq40Qty'] || 0);
                        }

                        grid.getStore().add(json.data.costPorts);
                        cmp.getCmpByName('freightCharge').updateSummary();
                    }

                    //destination charge
                    if(!!json.data.costChargeItems){
                        var grid = cmp.getCmpByName('destinationCharge').subGridPanel;
                        for(var i = 0; i < json.data.costChargeItems.length; i++){
                            var item = json.data.costChargeItems[i];
                            var qty = parseInt(item.containerQty || 0);
                            for(var j = 0; j < scope.currency.length; j++){
                                var suffix = scope.currency[j];
                                item['subtotal' + suffix] = parseFloat(item['price' + suffix] || 0) * qty;
                                item['receivedPrice' + suffix] = parseFloat(item['price' + suffix] || 0) * qty;
                            }
                        }
                        grid.getStore().add(json.data.costChargeItems);
                        cmp.getCmpByName('destinationCharge').updateSummary();
                    }

                    //duty
                    if(!!json.data.costTariffs){
                        var grid = cmp.getCmpByName('dutyCharge').subGridPanel;
                        grid.getStore().add(json.data.costTariffs);
                        cmp.getCmpByName('dutyCharge').updateSummary();
                    }

                    //product costs
                    if(!!json.data.costProductCosts){
                        var grid = cmp.getCmpByName('productCost').subGridPanel;
                        // for(var i = 0; i < json.data.costProductCosts.length; i++){
                        //     var row = json.data.costProductCosts[i];
                        //     var qty = parseInt(row.qty || 0);
                        //     for(var j = 0; j < scope.currency.length; j++){
                        //         var suffix = scope.currency[j];
                        //         row['subTotalCost' + suffix] = parseFloat(row['totalCost' + suffix] || 0) * qty;
                        //     }
                        // }
                        grid.getStore().add(json.data.costProductCosts);
                    }

                    //attachments
                    cmp.getCmpByName('attachments').setValue(json.data.attachments);
                }
            });
        } 

        this.editFormPanel.setReadOnly(this.isApprove, ['flowRemark']);

        // this.startCalculate();
    },// end of the init

    round: function(value, precision){
        if(typeof precision === 'undefined') precision = this.priceDecimalPrecision;
        return Ext.util.Format.round(value, precision);
    },

    loadOrderShippingPlanData: function(orderShippingPlanId, orderShippingPlanBusinessId){
        var scope = this;
        var form = Ext.getCmp(this.mainFormPanelId);
        scope.clearData();

        //设置当前最新汇率
        form.getCmpByName('main.rateAudToRmb').setValue(curUserInfo.audToRmb);
        form.getCmpByName('main.rateAudToUsd').setValue(curUserInfo.audToUsd);
        form.rateAudToRmb = curUserInfo.audToRmb;
        form.rateAudToUsd = curUserInfo.audToUsd;

        var url = __ctxPath + 'archives/service_provider_invoice/getQuotation?orderShippingPlanId=' + orderShippingPlanId + '&orderShippingPlanBusinessId=' + orderShippingPlanBusinessId;
        Ext.Ajax.request({
            url: url,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var data = response.data;
                if(!!data) {
                    scope.loadData.call(scope, data);
                }
            }
        });
    },

    getFee: function(){
        var url = url = __ctxPath + 'archives/cost/getFee?costId=' + this.record.id;
        var scope = this;
        Ext.Ajax.request({
            url: url,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var data = response.data;
                if(!!data) {
                    scope.loadPayment(data);
                }
            }
        });
    },

    clearData: function(){
        var form = Ext.getCmp(this.mainFormPanelId);
        var ordersGrid = form.getCmpByName('purchaseContract').subGridPanel;
        var freightGrid = form.getCmpByName('freightCharge').subGridPanel;
        var destGrid = form.getCmpByName('destinationCharge').subGridPanel;
        var dutyGrid = form.getCmpByName('dutyCharge').subGridPanel;
        var costGrid = form.getCmpByName('productCost').subGridPanel;
        var paymentGrid = form.getCmpByName('payment').subGridPanel;
        var orderCbmGrid = form.getCmpByName('orderCbm').subGridPanel;

        paymentGrid.getStore().removeAll();
        orderCbmGrid.getStore().removeAll();
        ordersGrid.getStore().removeAll();
        freightGrid.getStore().removeAll();
        destGrid.getStore().removeAll();
        dutyGrid.getStore().removeAll();
        costGrid.getStore().removeAll();

        form.getCmpByName('paymentSumAud').setValue(0);
        form.getCmpByName('paymentSumRmb').setValue(0);
        form.getCmpByName('paymentSumUsd').setValue(0);

        form.getCmpByName('calFreightSumAud').setValue(0);
        form.getCmpByName('calFreightSumRmb').setValue(0);
        form.getCmpByName('calFreightSumUsd').setValue(0);
        form.getCmpByName('calContainerQty').setValue(0);
        form.getCmpByName('calLclCbm').setValue(0);
        form.getCmpByName('vendorNumber').setValue(0);
        form.getCmpByName('orderQty').setValue(0);
        form.getCmpByName('freightSumAud').setValue(0);
        form.getCmpByName('freightSumRmb').setValue(0);
        form.getCmpByName('freightSumUsd').setValue(0);

        form.getCmpByName('calChargeSumAud').setValue(0);
        form.getCmpByName('calChargeSumRmb').setValue(0);
        form.getCmpByName('calChargeSumUsd').setValue(0);
        form.getCmpByName('chargeSumAud').setValue(0);
        form.getCmpByName('chargeSumRmb').setValue(0);
        form.getCmpByName('chargeSumUsd').setValue(0);

        form.getCmpByName('calTariffSumAud').setValue(0);
        form.getCmpByName('calTariffSumRmb').setValue(0);
        form.getCmpByName('calTariffSumUsd').setValue(0);
        form.getCmpByName('tariffSumAud').setValue(0);
        form.getCmpByName('tariffSumRmb').setValue(0);
        form.getCmpByName('tariffSumUsd').setValue(0);

        form.getCmpByName('customProcessingFeeAud').setValue(0);
    },

    loadData: function(data){
        this.rebuildData(data);
        this.loadPayment(data);
        this.loadOrder(data.orders, data.packings);
        this.loadFreight(data);
        this.loadDestination(data);
        this.loadDuty(data);
        //加载订单体积及明细
        this.initializeOrderCbm(data.orders, data.costProducts);
        this.initializeCustomProcessing();
    },
    
    initializeOrderCbm: function(orders, costdatas){
        var purchaseContractData = this.getGridData('purchaseContract');
        var data = {};
        for(var i = 0; i < purchaseContractData.length; i++){
            var purchaseContract = purchaseContractData[i];
            var qty = parseInt(purchaseContract.packingQty);
            var masterCartonCbm = parseFloat(purchaseContract.masterCartonCbm);
            var unitCbm = masterCartonCbm/parseFloat(purchaseContract.pcsPerCarton);
            if(!data[purchaseContract.orderNumber]) data[purchaseContract.orderNumber] = {
                orderNumber: purchaseContract.orderNumber,
                totalCbm: 0,
                totalPackingCbm: 0,
                totalShippingCbm: 0
            };

            data[purchaseContract.orderNumber].totalCbm += unitCbm * qty;
            data[purchaseContract.orderNumber].totalPackingCbm += unitCbm * qty;

            //packing cbm & shipping cbm
            for(var j = 0; j < orders.length; j++){
                if(orders[j].orderNumber == purchaseContract.orderNumber){
                    !!orders[j].totalCbm? data[purchaseContract.orderNumber].totalCbm = orders[j].totalCbm : '';
                    // data[purchaseContract.orderNumber].totalPackingCbm = orders[j].totalPackingCbm;
                    data[purchaseContract.orderNumber].totalShippingCbm = orders[j].totalShippingCbm || data[purchaseContract.orderNumber].totalPackingCbm;
                    break;
                }
            }
        }

        //组成订单体积表数据
        var gridData = [];
        for(var orderNumber in data){
            gridData.push(data[orderNumber]);
        }

        //回写装运体积
        var form = Ext.getCmp(this.mainFormPanelId);
        var grid = form.getCmpByName('orderCbm').subGridPanel;
        grid.getStore().add(gridData);
    },

    //计算总的电子行政费
    initializeCustomProcessing: function(){
        var threshold = _dict.customProcessing;
        if(!threshold || threshold.length == 0 || threshold[0].data.options.length == 0) return;
        var options = threshold[0].data.options;
        var customProcessingFee = parseFloat(options[0].param2);
        var form = Ext.getCmp(this.mainFormPanelId);
        var totalValueAud = form.getCmpByName('paymentSumAud').getValue();
        for(index in options) {
            if (eval(totalValueAud + options[index].param1)){
                customProcessingFee = parseFloat(options[index].param2);
                break;
            }
        }
        form.getCmpByName('customProcessingFeeAud').setValue(customProcessingFee);
    },

    loadPayment: function(data){
        var form = Ext.getCmp(this.mainFormPanelId);
        var grid = form.getCmpByName('payment').subGridPanel;
        var paymentPriceAud=0,paymentPriceRmb = 0, paymentPriceUsd = 0;
        var totalPaymentPriceAud=0,totalPaymentPriceRmb = 0, totalPaymentPriceUsd = 0;
        var paymentAudToRmb=0,paymentAudToUsd=0;

        if(!!data.feeRegisterDetails && data.feeRegisterDetails.length > 0){
            for(var i = 0; i < data.feeRegisterDetails.length; i++){
                if(data.feeRegisterDetails[i].applyCost == 1) {
                    var row = data.feeRegisterDetails[i];
                    if(row.currency == 1){
                        paymentPriceAud = (row.priceAud * row.qty).toFixed(2);
                        paymentPriceRmb = (paymentPriceAud  * row.paymentRateAudToRmb).toFixed(2);
                        paymentPriceUsd = (paymentPriceAud  * row.paymentRateAudToUsd).toFixed(2);
                    }else if(row.currency == 2){
                        paymentPriceRmb = (row.priceRmb * row.qty).toFixed(2);
                        paymentPriceAud = (paymentPriceRmb / row.paymentRateAudToRmb).toFixed(2);
                        paymentPriceUsd = (paymentPriceRmb / row.paymentRateAudToRmb * row.paymentRateAudToUsd).toFixed(2);
                    }else{
                        paymentPriceUsd = (row.priceUsd * row.qty).toFixed(2);
                        paymentPriceAud = (paymentPriceUsd / row.paymentRateAudToUsd).toFixed(2);
                        paymentPriceRmb = (paymentPriceUsd / row.paymentRateAudToUsd * row.paymentRateAudToRmb).toFixed(2);
                    }

                    totalPaymentPriceAud += parseFloat(paymentPriceAud);
                    totalPaymentPriceRmb += parseFloat(paymentPriceRmb);
                    totalPaymentPriceUsd += parseFloat(paymentPriceUsd);
                    row.subtotalAud = paymentPriceAud;
                    row.subtotalRmb = paymentPriceRmb;
                    row.subtotalUsd = paymentPriceUsd;
                    grid.getStore().add(row);
                }
            }
            //设置支付的平均汇率
            paymentAudToRmb = (totalPaymentPriceRmb /totalPaymentPriceAud).toFixed(4);
            paymentAudToUsd = (totalPaymentPriceUsd /totalPaymentPriceAud).toFixed(4);

            form.getCmpByName('paymentSumAudToRmb').setValue(paymentAudToRmb);
            form.getCmpByName('paymentSumAudToUsd').setValue(paymentAudToUsd);

            form.paymentAudToRmb = parseFloat(paymentAudToRmb);
            form.paymentAudToUsd = parseFloat(paymentAudToUsd);
        }

        if(!!data.otherDetails && data.otherDetails.length > 0){
            for(var i = 0; i < data.otherDetails.length; i++){
                grid.getStore().add(data.otherDetails[i]);
            }
        }

        form.getCmpByName('payment').updateSummary(form);
    },

    loadOrder: function(orders, packings){
        if(!orders || !packings) return;
        var packedOrders = [];
        for(var i = 0; i < orders.length; i++){
            var packing = orders[i];
            if(packedOrders.indexOf(orders.orderId) >= 0) continue;
            packedOrders.push(orders.orderId);
        }
        var data = [];
        for(var i = 0; i < packings.length; i++){
            var order = packings[i];
            // console.log(order)
            //check if order has packings
            for(var j = 0; j < order.details.length; j++){
                var obj = {};
                var details = order.details[j];
                Ext.applyIf(obj, details);
                Ext.applyIf(obj, details.product);
                Ext.applyIf(obj, details.product.prop);

                obj['salesPriceAud'] = parseFloat(obj['targetBinAud'] || 0);
                obj['salesPriceRmb'] = parseFloat(obj['targetBinRmb'] || 0);
                obj['salesPriceUsd'] = parseFloat(obj['targetBinUsd'] || 0);
                obj['salesValueAud'] = parseFloat(obj['targetBinAud'] || 0) * parseInt(obj.orderQty || 0);
                obj['salesValueRmb'] = parseFloat(obj['targetBinRmb'] || 0) * parseInt(obj.orderQty || 0);
                obj['salesValueUsd'] = parseFloat(obj['targetBinUsd'] || 0) * parseInt(obj.orderQty || 0);

                obj['unitCbm'] = (obj.masterCartonCbm/obj.pcsPerCarton).toFixed(4);

                if(!!obj.product) obj['productId'] = obj.product.id;
                obj.orderId = order.orderId;
                obj.orderNumber = order.orderNumber;
                data.push(obj);
            }
        }
        var form = Ext.getCmp(this.mainFormPanelId);
        var purchaseContractGrid = form.getCmpByName('purchaseContract').subGridPanel;
        purchaseContractGrid.getStore().add(data);
    },

    encodeOriginDestPorts: function(originPortId, destPortId){
        return 'S' + originPortId + '-D' + destPortId;
    },

    rebuildData: function(data){
        //orders
        var orders = {};
        for(var i = 0; i < data.orders.length; i++){
            var order = data.orders[i];
            if(!orders[order.id]) orders[order.id] = order;
        }
        this.orders = orders;

        //create a dictionary for ports, use origin port id as index
        var ports = {};
        for(var i = 0; i < data.ports.length; i++){
            var port = data.ports[i];
            var portKey = this.encodeOriginDestPorts(port.originPortId, port.destinationPortId);
            if(!ports[portKey]) ports[portKey] = port;

            for(var j = 0; j < this.containerTypes.length; j++){
                var typeName = this.containerTypes[j].toLowerCase();
                port[typeName + 'Qty'] = 0;
            }
            port['lclCbm'] = 0;
        }
        this.ports = ports;
    },

    getContainerTypeName: function(type){
        type = parseInt(type);
        return this.containerTypes[type - 1];
    },

    loadFreight: function(data){
        var freight = [];
        var selectedPorts = [];
        var totalPriceAud=0,  totalPriceRmb=0,  totalPriceUsd=0;

        for(var i = 0; i < data.packings.length; i++){
            var packing = data.packings[i];
            var order = this.orders[packing.orderId];
            var portKey = this.encodeOriginDestPorts(order.originPortId, order.destinationPortId);
            var port = this.ports[portKey];
            if(!port) continue;

            var containerTypeName = this.getContainerTypeName(packing.containerType).toLowerCase();
            port[containerTypeName + 'Qty'] = port[containerTypeName + 'Qty'] + 1;
            if(selectedPorts.indexOf(portKey) < 0) selectedPorts.push(portKey);

            //lcl cbm
            if(containerTypeName.toUpperCase() == "LCL") {
                for(var j = 0; j < packing.details.length; j++){
                    port['lclCbm'] += parseFloat(packing.details[j].totalCbm);
                }
            }
        }

        for(var i = 0; i < selectedPorts.length; i++){
            var port = this.ports[selectedPorts[i]];
            for(var j = 0; j < this.currency.length; j++){
                var suffix = this.currency[j];
                port['priceTotal' + suffix] = parseFloat(port['priceGp20' + suffix] || 0) * parseInt(port['gp20Qty'] || 0)
                    + parseFloat(port['priceGp40' + suffix] || 0) * parseInt(port['gp40Qty'] || 0)
                    + parseFloat(port['priceHq40' + suffix] || 0) * parseInt(port['hq40Qty'] || 0)
                    + parseFloat(port['priceLcl' + suffix] || 0) * parseInt(port['lclCbm'] || 0)
                    + parseFloat(port['priceGp20Insurance' + suffix] || 0) * parseInt(port['gp20Qty'] || 0)
                    + parseFloat(port['priceGp40Insurance' + suffix] || 0) * parseInt(port['gp40Qty'] || 0)
                    + parseFloat(port['priceHq40Insurance' + suffix] || 0) * parseInt(port['hq40Qty'] || 0)
                    + parseFloat(port['priceLclInsurance' + suffix] || 0) * parseInt(port['lclCbm'] || 0);
            }

            port['rowTotalQty'] = parseInt(port['gp20Qty'] || 0) + parseInt(port['gp40Qty'] || 0) + parseInt(port['hq40Qty'] || 0);
            freight.push(port);
        }

        var form = Ext.getCmp(this.mainFormPanelId);
        var freightGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('freightCharge').subGridPanel;
        freightGrid.getStore().add(freight);

        form.getCmpByName('freightCharge').updateSummary(form);
        form.getCmpByName('vendorNumber').setValue(data.vendorNumber);
        form.getCmpByName('orderQty').setValue(data.orders.length);
    },

    loadDestination: function(data){
        if(!data.chargeItems || !data.packings) return;

        //count containers
        var containerNumbers = [];
        var containerQty = {};
        for(var i = 0; i < data.packings.length; i++){
            var packing= data.packings[i];
            if(containerNumbers.indexOf(packing.containerNumber) >= 0) continue;
            containerNumbers.push(packing.containerNumber);

            var containerType = parseInt(packing.containerType);
            if(!containerQty[containerType]) containerQty[containerType] = 0;
            containerQty[containerType] += 1;
        }

        //count number of jobs and number of vendors
        var numberOfJobs = 0;
        var numberOfVendors = 0;
        var vendorIds = [];
        for(var orderId in this.orders){
            var order = this.orders[orderId];
            numberOfJobs++;
            if(vendorIds.indexOf(order.vendorId) < 0) vendorIds.push(order.vendorId);
        }

        var gridData = [];
        for(var i = 0; i < data.costChargeItems.length; i++){
            var item = data.costChargeItems[i];
            var unitId = parseInt(item.unitId);
            var containerType = parseInt(item.containerType);
            if(unitId == this.perContainerUnitId) {
                if(!!containerQty[containerType]) item.containerQty = containerQty[containerType];
                else continue;
            } else {
                item.containerQty = 1;
            }

            item.subtotalAud = parseFloat(item.priceAud || 0) * item.containerQty;
            item.subtotalRmb = parseFloat(item.priceRmb || 0) * item.containerQty;
            item.subtotalUsd = parseFloat(item.priceUsd || 0) * item.containerQty;
            item.receivedPriceAud = item.subtotalAud;
            item.receivedPriceRmb = item.subtotalRmb;
            item.receivedPriceUsd = item.subtotalUsd;
            gridData.push(item);
        }

        var form = Ext.getCmp(this.mainFormPanelId);
        var destGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('destinationCharge').subGridPanel;
        destGrid.getStore().add(gridData);

        //update summary
        form.getCmpByName('destinationCharge').updateSummary(form);
    },

    loadDuty: function(data){
        var form = Ext.getCmp(this.mainFormPanelId);
        var orderGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('purchaseContract').subGridPanel;
        var orderData = orderGrid.getStore().getRange();

        var duty = {};
        for(var i = 0; i < orderData.length; i++){
            var data = orderData[i].data;
            if(!data.hsCode) continue;
            if(!duty[data.hsCode]) {
                duty[data.hsCode] = {
                    hsCode: data.hsCode,
                    itemCode: data.hsCode,
                    dutyRate: data.dutyRate || 0,
                    salesQty: 0,
                    salesValueAud: 0,
                    salesValueRmb: 0,
                    salesValueUsd: 0
                };
            }

            duty[data.hsCode].salesQty += parseInt(data.orderQty || 0);
            duty[data.hsCode].salesValueAud += parseFloat(data.salesValueAud || 0);
            duty[data.hsCode].salesValueRmb += parseFloat(data.salesValueRmb || 0);
            duty[data.hsCode].salesValueUsd += parseFloat(data.salesValueUsd || 0);
        }

        //duty
        for(var hsCode in duty){
            var item = duty[hsCode];
            item['tariffAud'] = (item.salesValueAud || 0) * (item.dutyRate || 0) / 100;
            item['tariffRmb'] = (item.salesValueRmb || 0) * (item.dutyRate || 0) / 100;
            item['tariffUsd'] = (item.salesValueUsd || 0) * (item.dutyRate || 0) / 100;
        }

        var data = [];
        for(var hsCode in duty) data.push(duty[hsCode]);
        var form = Ext.getCmp(this.mainFormPanelId);
        var dutyGrid = form.getCmpByName('dutyCharge').subGridPanel;
        dutyGrid.getStore().add(data);

        //update summary
        form.getCmpByName('dutyCharge').updateSummary(form);
    },

    getGridData: function(name){
        var data = [];
        var form = Ext.getCmp(this.mainFormPanelId);
        var store = form.getCmpByName(name).subGridPanel.getStore().getRange();
        for(var i = 0; i < store.length; i++){
            data.push(store[i].data);
        }

        return data;
    },

    encodeOrderSKU: function(orderNumber, sku){
        return 'Job' + orderNumber + '-SKU' + sku;
    },

    calculateOrderCbm: function(){
        var orderData = this.getGridData('purchaseContract');  //采购合同表格数据
        var orderCbmData = this.getGridData('orderCbm');    //订单体积表格数据
        var orderCbm = {};  //每个订单的订单总体积
        var actualOrderCbm = {}; //每个订单的实际船运体积

        //采购合同的订单体积
        for(var i = 0; i < orderData.length; i++) {
            var order = orderData[i];
            var qty = parseInt(order.packingQty); //订单采购的sku件数
            var masterCartonCbm = parseFloat(order.masterCartonCbm);   //单个SKU的外箱体积
            var unitCbm = masterCartonCbm / parseFloat(order.pcsPerCarton); //单个SKU体积
            if (!orderCbm[order.orderNumber]) orderCbm[order.orderNumber] = unitCbm * qty;   //这个SKU所属订单的总体积
            else orderCbm[order.orderNumber] += unitCbm * qty;
        }

        //实际船运体积
        for(var i = 0; i < orderCbmData.length; i++){
            var actual = orderCbmData[i];
            actualOrderCbm[actual.orderNumber] = parseFloat(actual.totalShippingCbm || 0);
        }

        this.orderCbm = orderCbm;
        this.actualOrderCbm = actualOrderCbm;
    },

    calculatePayment: function(productCosts){
        var payment = this.getGridData('payment');
        var orderPayment = {};  //每个订单的费用（采购合同中的费用）
        var orderData = this.getGridData('purchaseContract');
        var actualOrderPayment = {}; //每个订单的实际支付费用
        var form = Ext.getCmp(this.mainFormPanelId);

        //订单费用
        for(var i = 0; i < orderData.length; i++) {
            var order = orderData[i];
            var qty = parseInt(order.packingQty); //订单采购的sku件数
            if (!orderPayment[order.orderNumber]) orderPayment[order.orderNumber] = parseFloat(order.priceAud || 0) * parseInt(qty || 0);
            else orderPayment[order.orderNumber] += parseFloat(order.priceAud || 0) * parseInt(qty || 0);
        }

        //实际支付的费用
        for(var i = 0; i < payment.length; i++){
            var p = payment[i];
            if(!actualOrderPayment[p.orderNumber]) actualOrderPayment[p.orderNumber] = parseFloat(p.subtotalAud || 0);
            else actualOrderPayment[p.orderNumber] += parseFloat(p.subtotalAud || 0);
        }

        for(var code in productCosts){
            var product = productCosts[code];
            if(product.qty <= 0)continue;
            //单个SKU所在订单的总体积
            var cbm1 = this.orderCbm[product.orderNumber];
            //这个订单的船务体积
            var cbm2 = this.actualOrderCbm[product.orderNumber];
            if(!cbm1 || !cbm2) continue;

            //按照订单体积和船务体积的比例对unit cbm进行缩放
            product.unitCbm = product.unitCbm /cbm1 * cbm2;
            product.totalCbm = product.unitCbm * product.qty;

            var payment1 = orderPayment[product.orderNumber];
            var payment2 = actualOrderPayment[product.orderNumber];

            if(!payment1 || !payment2) continue;
            //单价
            var allSkuPricePercent = product.priceUsd / parseFloat(form.totalOrderValueUsd);
            if(product.currency == 1){
                allSkuPricePercent = product.priceAud / parseFloat(form.totalOrderValueAud);
            }else if(product.currency == 2){
                allSkuPricePercent = product.priceRmb / parseFloat(form.totalOrderValueRmb);
            }

            var paymentSumAud = this.round(form.getCmpByName('paymentSumAud').getValue());
            var paymentSumRmb = this.round(form.getCmpByName('paymentSumRmb').getValue());
            var paymentSumUsd = this.round(form.getCmpByName('paymentSumUsd').getValue());

            product.purchaseCostAud = this.round(allSkuPricePercent * paymentSumAud, 4);
            product.purchaseCostRmb = this.round(allSkuPricePercent * paymentSumRmb, 4);
            product.purchaseCostUsd = this.round(allSkuPricePercent * paymentSumUsd, 4);
            product.allSkuPricePercent = allSkuPricePercent;
        }
    },


    calculateFreight: function(productCosts){
        var orderCbmData = this.getGridData('orderCbm');    //订单体积表格数据
        var freight = this.getGridData('freightCharge');    //海运费用表格数据

        //所有SKU总体积
        var allSkuCbm = 0;
        for(var i = 0; i < orderCbmData.length; i++){
            allSkuCbm = allSkuCbm + parseFloat(orderCbmData[i].totalShippingCbm || 0);
        }

        if(!allSkuCbm) {
            Ext.Msg.alert(_lang.TText.titleClew, _lang.ProductCost.magInvalidCbm);
            return false;
        }

        //计算每个SKU在总体积中的比例
        for(var code in productCosts){
            productCosts[code]['allSkuCbmPercent'] = this.round(productCosts[code].unitCbm/allSkuCbm, 8);
        }

        //计算总海运费用
        var totalFreightCost = Ext.getCmp(this.mainFormPanelId).getCmpByName('freightSumAud').getValue();
        if(!totalFreightCost) {
            //如果手动输入不填写，则用报价中的海运费
            for (var i = 0; i < freight.length; i++) {
                totalFreightCost += parseFloat(freight[i].priceTotalAud);
            }
        }

        //计算海运成本
        for(var code in productCosts){
            var percent = productCosts[code].allSkuCbmPercent;
            var product = productCosts[code];
            product['freightCost'] = this.round(totalFreightCost * percent, 4);
        }

        return true;
    },

    calculateDestination: function(productCosts){
        var orderData = this.getGridData('purchaseContract');
        var dest = this.getGridData('destinationCharge');
        var chargeSumAud = parseFloat(Ext.getCmp(this.mainFormPanelId).getCmpByName('chargeSumAud').getValue()||0);

        //计算本地费成本
        for(var code in productCosts){
            var percent = productCosts[code].allSkuCbmPercent;
            var product = productCosts[code];
            product['destinationCost'] = this.round(chargeSumAud * percent, 4);
        }
    },

    calculateDuty: function(productCosts){
        var form = Ext.getCmp(this.mainFormPanelId);
        //总货值
        var totalValueAud = parseFloat(form.getCmpByName('paymentSumAud').getValue()||0);
        //手输总关税
        var tariffSumAud = parseFloat(form.getCmpByName('tariffSumAud').getValue()||0);
        //关税
        var duty = this.getGridData('dutyCharge');
        var tariffItemDutyCharges = {};
        if(!!tariffSumAud) {
            for (var code in productCosts) {
                var product = productCosts[code];
                product.dutyCost = this.round(product.allSkuPricePercent * tariffSumAud, 4);
            }
        }else{
            for (var i = 0; i < duty.length; i++) {
                tariffItemDutyCharges[duty[i].hsCode] = {
                    totalSalesValueAud: 0,                                  //total sales value for a product category (hscode)
                    tariffAud: parseFloat(duty[i].tariffAud || 0)           //total value charged for a product category (hscode)
                }
            }

            for (var code in productCosts) {
                var product = productCosts[code];
                if (!product.hsCode) continue;
                tariffItemDutyCharges[product.hsCode].totalSalesValueAud += product.salesValueAud;
            }

            for (var code in productCosts) {
                var product = productCosts[code];
                var charge = tariffItemDutyCharges[product.hsCode];
                if (!product.hsCode || !charge.totalSalesValueAud || !product.qty) continue;
                product.dutyCost = this.round(product.salesValueAud / charge.totalSalesValueAud * charge.tariffAud / product.qty, 4);
            }
        }

        //行政费用
        var customProcessingFee = parseFloat(form.getCmpByName('customProcessingFeeAud').getValue()||0);
        if(!!totalValueAud) {
            for (var code in productCosts) {
                var product = productCosts[code];
                var cost = this.round(customProcessingFee * product.allSkuPricePercent, 4);
                product['customProcessingCost'] += cost;
            }
        }
    },

    //GST计算
    calculateGst: function(productCosts){
        var form = Ext.getCmp(this.mainFormPanelId);
        var localGstAud = parseFloat(form.getCmpByName('localGstAud').getValue()||0);
        var valueGstAud = parseFloat(form.getCmpByName('valueGstAud').getValue()||0);

        if(!!localGstAud && !!valueGstAud) {
            for (var code in productCosts) {
                var product = productCosts[code];
                product['localGst'] += this.round(localGstAud * product.allSkuCbmPercent, 4);
                product['valueGst'] += this.round(valueGstAud * product.allSkuPricePercent, 4);
            }
        }
    },

    calculate: function() {
        var productCosts = {};  //产品成本
        var orderData = this.getGridData('purchaseContract');
        var form = Ext.getCmp(this.mainFormPanelId);
        var totalOrderValueAud=0, totalOrderValueRmb=0, totalOrderValueUsd=0;
        var totalOrderCbm=0;

        form.rateAudToRmb = parseFloat(form.getCmpByName('main.rateAudToRmb').getValue());
        form.rateAudToUsd = parseFloat(form.getCmpByName('main.rateAudToUsd').getValue());
        form.getCmpByName('productCost').subGridPanel.audToRmb=form.rateAudToRmb;
        form.getCmpByName('productCost').subGridPanel.audToUsd=form.rateAudToUsd;

        for(var i = 0; i < orderData.length; i++){
            var order = orderData[i];
            var qty = parseInt(order.packingQty); //订单采购的sku件数
            var masterCartonCbm  = parseFloat(order.masterCartonCbm);   //单个SKU的外箱体积
            // var unitCbm = masterCartonCbm / parseFloat(order.pcsPerCarton); //单个SKU体积
            var unitCbm = parseFloat(order.unitCbm); //单个SKU体积

            var code = this.encodeOrderSKU(order.orderNumber, order.sku);
            if(!productCosts[code]){
                var totalCbm = unitCbm * qty;
                productCosts[code] = {
                    orderId: order.orderId,
                    orderNumber: order.orderNumber,
                    sku: order.sku,
                    productId: order.productId,
                    vendorId: order.vendorId,
                    hsCode: order.hsCode,
                    dutyRate: order.dutyRate || 0,
                    currency: order.currency,
                    priceAud: parseFloat(order.priceAud ||0),
                    priceRmb: parseFloat(order.priceRmb ||0),
                    priceUsd: parseFloat(order.priceUsd ||0),
                    paymentAudToRmb: form.paymentAudToRmb,
                    paymentAudToUsd: form.paymentAudToUsd,
                    rateAudToRmb: form.rateAudToRmb,
                    rateAudToUsd: form.rateAudToUsd,
                    qty: qty,
                    unitCbm: unitCbm,
                    totalCbm: totalCbm,  //cbm of a SKU in all order
                    salesPriceAud: parseFloat(order.priceAud || 0),
                    salesPriceRmb: parseFloat(order.priceRmb || 0),
                    salesPriceUsd: parseFloat(order.priceUsd || 0),
                    salesValueAud: parseInt(qty || 0) * parseFloat(order.priceAud || 0),
                    salesValueRmb: parseInt(qty || 0) * parseFloat(order.priceRmb || 0),
                    salesValueUsd: parseInt(qty || 0) * parseFloat(order.priceUsd || 0),
                    purchaseCostAud: 0,
                    purchaseCostRmb: 0,
                    purchaseCostUsd: 0,
                    totalCostAud: 0,
                    totalCostRmb: 0,
                    totalCostUsd: 0,
                    subTotalCostAud: 0,
                    subTotalCostRmb: 0,
                    subTotalCostUsd: 0,
                    totalPurchaseValueAud: parseFloat(order.priceAud || 0) * parseInt(qty || 0),
                    totalPurchaseValueRmb: parseFloat(order.priceRmb || 0) * parseInt(qty || 0),
                    totalPurchaseValueUsd: parseFloat(order.priceUsd || 0) * parseInt(qty || 0),
                    freightCost: 0,
                    destinationCost: 0,
                    dutyCost: 0,
                    valueGst: 0,
                    localGst: 0,
                    customProcessingCost: 0,
                    otherCost: 0,
                    totalCost: 0,
                    hasError: masterCartonCbm <= 0 || unitCbm<=0
                   // totalCost: parseFloat(order.priceAud)
                };
            } else {
                // console.log(order.sku + '累加')
                var totalCbm = productCosts[code].unitCbm * qty;
                productCosts[code].totalCbm  = parseFloat(productCosts[code].totalCbm || 0) + totalCbm;
                productCosts[code].qty += qty;
                productCosts[code].salesValueAud += productCosts[code].salesPriceAud * qty;
                productCosts[code].salesValueRmb += productCosts[code].salesValueRmb * qty;
                productCosts[code].salesValueUsd += productCosts[code].salesValueUsd * qty;
                productCosts[code].totalPurchaseValueAud += parseFloat(order.priceAud || 0) * qty;
                productCosts[code].totalPurchaseValueRmb += parseFloat(order.priceRmb || 0) * qty;
                productCosts[code].totalPurchaseValueUsd += parseFloat(order.priceUsd || 0) * qty;
            }
            totalOrderValueAud += productCosts[code].totalPurchaseValueAud;
            totalOrderValueRmb += productCosts[code].totalPurchaseValueRmb;
            totalOrderValueUsd += productCosts[code].totalPurchaseValueUsd;

            totalOrderCbm += productCosts[code].totalCbm;
        }
        form.totalOrderValueAud = totalOrderValueAud;
        form.totalOrderValueRmb = totalOrderValueRmb;
        form.totalOrderValueUsd = totalOrderValueUsd;

        form.totalOrderCbm = totalOrderCbm;

        //计算成本
        this.calculateOrderCbm();
        this.calculatePayment(productCosts);                //采购成本
        if(!this.calculateFreight(productCosts)) return;    //海运
        this.calculateDestination(productCosts);             //本地费用
        this.calculateDuty(productCosts);                    //关税和行政费用
        this.calculateGst(productCosts);                    //计算货值GST和本地费GST

        //写入数据到界面成本表格
        var productGridData = [];

        for(var code in productCosts){
            var product = productCosts[code];

            var totalSubCost = product.freightCost + product.destinationCost + product.dutyCost + product.customProcessingCost + product.otherCost;

            //总成本
            var totalCostUsd = product.purchaseCostUsd + totalSubCost * product.paymentAudToUsd  ;
            var totalCostAud = this.round(totalCostUsd / product.paymentAudToUsd, 4);
            var totalCostRmb = this.round(totalCostUsd / product.paymentAudToUsd * product.paymentAudToRmb, 4);

            //单价
            var subtotalUsd = product.priceUsd;
            var subtotalAud = this.round(subtotalUsd / product.paymentAudToUsd, 4);
            var subtotalRmb = this.round(subtotalUsd / product.paymentAudToUsd * product.paymentAudToRmb, 4);
            //采购成本
            var purchaseCostUsd = product.purchaseCostUsd;
            var purchaseCostAud = this.round(purchaseCostUsd / product.paymentAudToUsd, 4);
            var purchaseCostRmb = this.round(purchaseCostUsd / product.paymentAudToUsd * product.paymentAudToRmb, 4);

            if(product.currency == 1){
                totalCostAud = product.purchaseCostAud + totalSubCost;
                totalCostRmb = this.round(totalCostAud  * product.paymentAudToRmb, 4);
                totalCostUsd = this.round(totalCostAud  * product.paymentAudToUsd, 4);

                subtotalAud = product.priceAud;
                subtotalRmb = this.round(subtotalAud  * product.paymentAudToRmb, 4);
                subtotalUsd = this.round(subtotalAud  * product.paymentAudToUsd, 4);

                purchaseCostAud = product.purchaseCostAud;
                purchaseCostRmb = this.round(purchaseCostAud  * product.paymentAudToRmb, 4);
                purchaseCostUsd = this.round(purchaseCostAud  * product.paymentAudToUsd, 4);

            }else if(product.currency == 2){
                totalCostRmb = product.purchaseCostRmb + totalSubCost * product.paymentAudToRmb;
                totalCostAud = this.round(totalCostRmb / product.paymentAudToRmb, 4);
                totalCostUsd = this.round(totalCostRmb / product.paymentAudToRmb * product.paymentAudToUsd, 4);

                subtotalRmb = product.priceRmb;
                subtotalAud = this.round(subtotalRmb / product.paymentAudToRmb, 4);
                subtotalUsd = this.round(subtotalRmb / product.paymentAudToRmb * product.paymentAudToUsd, 4);

                purchaseCostRmb = product.purchaseCostRmb;
                purchaseCostAud = this.round(purchaseCostRmb / product.paymentAudToRmb, 4);
                purchaseCostUsd = this.round(purchaseCostRmb / product.paymentAudToRmb * product.paymentAudToUsd, 4);
            }

            productGridData.push({
                orderId: product.orderId,
                orderNumber: product.orderNumber,
                sku: product.sku,
                orderQty: product.qty,
                currency: product.currency,
                productId: product.productId,
                priceAud: subtotalAud,
                priceRmb: subtotalRmb,
                priceUsd: subtotalUsd,
                priceCostAud: purchaseCostAud,
                priceCostRmb: purchaseCostRmb,
                priceCostUsd: purchaseCostUsd,
                portFeeAud: product.freightCost,
                portFeeRmb: product.freightCost * product.rateAudToRmb,
                portFeeUsd: product.freightCost * product.rateAudToUsd,
                chargeItemFeeAud: product.destinationCost,
                chargeItemFeeRmb: product.destinationCost * product.rateAudToRmb,
                chargeItemFeeUsd: product.destinationCost * product.rateAudToUsd,
                tariffAud: product.dutyCost,
                tariffRmb: product.dutyCost * product.rateAudToRmb,
                tariffUsd: product.dutyCost * product.rateAudToUsd,
                customProcessingFeeAud: product.customProcessingCost,
                customProcessingFeeRmb: product.customProcessingCost * product.rateAudToRmb,
                customProcessingFeeUsd: product.customProcessingCost * product.rateAudToUsd,
                otherFeeAud: product.otherCost,
                otherFeeRmb: product.otherCost * product.rateAudToRmb,
                otherFeeUsd: product.otherCost * product.rateAudToUsd,
                totalCostAud: totalCostAud,
                totalCostRmb: totalCostRmb,
                totalCostUsd: totalCostUsd,
                subTotalCostAud: this.round(totalCostAud *  product.qty, 4),
                subTotalCostRmb: this.round(totalCostRmb *  product.qty, 4),
                subTotalCostUsd: this.round(totalCostUsd *  product.qty, 4),
                valueGstAud: product.valueGst,
                valueGstRmb: product.valueGst * product.rateAudToRmb,
                valueGstUsd: product.valueGst * product.rateAudToUsd,
                localGstAud: product.localGst,
                localGstRmb: product.localGst * product.rateAudToRmb,
                localGstUsd: product.localGst * product.rateAudToUsd,
                gstAud: product.valueGst + product.localGst,
                gstRmb: (product.valueGst + product.localGst) * product.rateAudToRmb,
                gstUsd: (product.valueGst + product.localGst) * product.rateAudToUsd,
                hasError: product.hasError
            });

            // productCostSumAud += this.round(totalCostAud) *  product.qty;
        }

        var costGrid = form.getCmpByName('productCost').subGridPanel;
        costGrid.getStore().removeAll();
        costGrid.getStore().add(productGridData);

        //按订单编号计算每个订单的成本汇总
        this.updateCostOrder();
    },

    updateCostOrder: function(){
        var orderCbmData = this.getGridData('orderCbm');
        var productCostData = this.getGridData('productCost');
        var form = Ext.getCmp(this.mainFormPanelId);

        var costOrder = {};
        for(var i = 0; i < orderCbmData.length; i++){
            var orderCbm = orderCbmData[i];
            costOrder[orderCbm.orderNumber] = {
                orderId: '',
                orderNumber: orderCbm.orderNumber,
                currency: '1',
                rateAudToRmb: form.paymentAudToRmb,
                rateAudToUsd: form.paymentAudToUsd,
                priceCostAud: 0,
                priceCostRmb: 0,
                priceCostUsd: 0,
                portFeeAud: 0,
                portFeeRmb: 0,
                portFeeUsd: 0,
                chargeItemFeeAud: 0,
                chargeItemFeeRmb: 0,
                chargeItemFeeUsd: 0,
                tariffAud: 0,
                tariffRmb: 0,
                tariffUsd: 0,
                customProcessingFeeAud: 0,
                customProcessingFeeRmb: 0,
                customProcessingFeeUsd: 0,
                otherFeeAud: 0,
                otherFeeRmb: 0,
                otherFeeUsd: 0,
                totalCostAud: 0,
                totalCostRmb: 0,
                totalCostUsd: 0,
                localGstAud: 0,
                localGstRmb: 0,
                localGstUsd: 0,
                valueGstAud: 0,
                valueGstRmb: 0,
                valueGstUsd: 0,
                gstAud: 0,
                gstRmb: 0,
                gstUsd: 0,
                orderTitle: '',
                totalCbm: orderCbm.totalCbm,
                totalPackingCbm: orderCbm.totalPackingCbm,
                totalShippingCbm: orderCbm.totalShippingCbm
            };
        }
        console.log('==========刷order')
        //calculate order cost
        var attributes = ['priceCostAud', 'priceCostRmb', 'priceCostUsd', 'portFeeAud', 'portFeeRmb', 'portFeeUsd',
            'chargeItemFeeAud', 'chargeItemFeeRmb', 'chargeItemFeeUsd', 'tariffAud', 'tariffRmb', 'tariffUsd',
            'customProcessingFeeAud', 'customProcessingFeeRmb', 'customProcessingFeeUsd',
            'otherFeeAud', 'otherFeeRmb', 'otherFeeUsd', 'localGstAud', 'localGstRmb', 'localGstUsd', 'valueGstAud', 'valueGstRmb', 'valueGstUsd', 'gstAud', 'gstRmb', 'gstUsd'];
        for(var i = 0; i < productCostData.length; i++){
            var productCost = productCostData[i];
            var order = costOrder[productCost.orderNumber];
            order.orderId = productCost.orderId;
            order['totalCostAud'] += parseFloat(productCost['totalCostAud']);
            order['totalCostRmb'] += parseFloat(productCost['totalCostRmb']);
            order['totalCostUsd'] += parseFloat(productCost['totalCostUsd']);

            var orderQty = parseInt(productCost['orderQty']);
            for(var j = 0; j < attributes.length; j++){
                var attribute = attributes[j];
                order[attribute] += parseFloat(productCost[attribute] || 0) * orderQty;
            }
        }

        this.costOrder = costOrder;
    },

    //计算汇率修改
    changeExchange: function(e, newValue, oldValue){
        var form = Ext.getCmp(e.mainFormPanelId);
        if(!!e && e.name == 'main.rateAudToRmb'){
            form.getCmpByName('paymentGstAudToRmb').setValue(newValue);
            form.getCmpByName('freightSumAudToRmb').setValue(newValue);
            form.getCmpByName('chargeSumAudToRmb').setValue(newValue);
            form.getCmpByName('tariffSumAudToRmb').setValue(newValue);
            form.getCmpByName('customProcessingAudToRmb').setValue(newValue);
            form.rateAudToRmb = newValue;
            form.getCmpByName('productCost').subGridPanel.audToRmb=newValue;
        }else if(!!e && e.name == 'main.rateAudToUsd'){
            form.getCmpByName('paymentGstAudToUsd').setValue(newValue);
            form.getCmpByName('freightSumAudToUsd').setValue(newValue);
            form.getCmpByName('chargeSumAudToUsd').setValue(newValue);
            form.getCmpByName('tariffSumAudToUsd').setValue(newValue);
            form.getCmpByName('customProcessingAudToUsd').setValue(newValue);
            form.rateAudToUsd = newValue;
            form.getCmpByName('productCost').subGridPanel.audToUsd=newValue;
        }
    },

    saveFun: function(){
        var params = {act: this.actionName ? this.actionName: 'save'};
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        //order cbm
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('orderCbm').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costProducts['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['orderCbm['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //purchase contract
        rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('purchaseContract').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costProducts['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['costProducts['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //freight
        rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('freightCharge').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costPorts['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['costPorts['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //destination
        rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('destinationCharge').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costChargeItems['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['costChargeItems['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //duty
        rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('dutyCharge').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costTariffs['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['costTariffs['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //product cost
        var hasError = false;
        rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('productCost').subGridPanel});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['costProductCosts['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    params['costProductCosts['+index+'].'+key ] = rows[index].data[key];
                }
                if(rows[index].data['hasError']){
                    hasError = true;
                }
            }
        }

        //cost order
        var index = 0;
        for(var orderId in this.scope.costOrder){
            var data = this.scope.costOrder[orderId];
            params['costOrders['+index+'].businessId'] = businessId;
            for(var key in data){
                params['costOrders['+index+'].'+key ] = data[key];
            }
            index++;
        }

        //attachment
        rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('attachments').subGridPanel});
        if(rows.length>0){
            for(var index in rows){
                params['attachments['+index+'].businessId'] = businessId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
        
        if(hasError){
            Ext.Msg.alert(_lang.TText.titleClew, _lang.ProductCost.msgInvalidCost);
            return;
        }

        // console.log(params);

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.mainGridPanelId).getStore().reload();
                Ext.getCmp(this.winId).close();
            }
        });
    },
});
