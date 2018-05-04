ServiceProviderInvoiceForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove = this.record.flowStatus > 0 && this.action != 'add' && this.action != 'copy' ? true: false;
        var conf = {
            title : _lang.ServiceProviderInvoice.mTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
            moduleName: 'ServiceInvoice',
            winId : 'ServiceProviderInvoiceForm',
			frameId : 'ServiceProviderInvoiceView',
			mainGridPanelId : 'ServiceProviderInvoiceViewGridPanelID',
			mainFormPanelId : 'ServiceProviderInvoiceViewFormPanelID',
			processFormPanelId: 'ServiceProviderInvoiceProcessFormPanelID',
            urlList: __ctxPath + 'flow/shipping/serviceinquiry/list',
            urlSave: __ctxPath + 'flow/shipping/serviceinquiry/save',
            urlDelete: __ctxPath + 'flow/shipping/serviceinquiry/delete',
            urlGet: __ctxPath + 'flow/shipping/serviceinquiry/get',
            urlFlow: __ctxPath + 'flow/shipping/serviceinquiry/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processInstanceId=' + this.record.processInstanceId,
			actionName: this.action,
            flowStart: !this.isApprove && this.record.flowStatus != 0,
            save: !this.isApprove || this.record.flowStatus<1 && this.record.status>0,
            close: true,
            flowAllow: this.isApprove || this.record.flowStatus==0 && this.record.status>0,
            flowRefuse: this.isApprove,
            flowRedo: this.isApprove,
            flowBack: this.isApprove,
            processRemark: this.isApprove,
            processHistory: this.isApprove,
            processDirection: true,
            saveFun: this.saveFun
        };

        Ext.applyIf(this, conf);
        this.initUIComponents(conf);
        ServiceProviderInvoiceForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			cls: 'gb-blank',
			tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });

        this.containerTypes = ['Gp20', 'Gp40', 'Hq40', 'Lcl'];
        this.gstRate = 0.1;
        this.rateAudToUsd = 0.72;
        this.rateAudToRmb = 5.01;
        this.sideLoaderItemId = 12;
        this.fuelSurchargeItemId = 13;
        this.perContainerUnitId = 1;
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

                { xtype: 'section', title:_lang.ServiceProviderInvoice.tabOrderShippingPlan},
                { xtype: 'container',cls:'row', items:  [
                    {  field: 'section', xtype: 'container', cls: 'row', items: [
                        { field: 'orderShippingPlanId', xtype: 'hidden'},
                        { field: 'orderShippingPlan', xtype: 'OrderShippingPlanDialog', fieldLabel: _lang.FlowOrderShippingPlan.fOrderShippingPlanId, cls:'col-2',
                            formId: conf.mainFormPanelId, hiddenName: 'orderShippingPlanId', single:true, readOnly: this.isApprove,
                            subcallback: function(rows){
                                //console.log('invoice form, sub callback');
                                //console.log(rows);
                                if(!!rows && rows.length > 0){
                                    var data = rows[0].raw;
                                    scope.editFormPanel.getCmpByName('orderShippingPlanId').setValue(data.id);
                                    scope.editFormPanel.getCmpByName('serviceProviderQuotationId').setValue(data.serviceProviderQuotationId);
                                    scope.editFormPanel.getCmpByName('serviceProviderQuotation').setValue(data.serviceProviderQuotationId);

                                    scope.editFormPanel.getCmpByName('serviceProviderId').setValue(data.serviceProviderId);
                                    scope.editFormPanel.getCmpByName('serviceProviderCnName').setValue(data.serviceProviderCnName);
                                    scope.editFormPanel.getCmpByName('serviceProviderEnName').setValue(data.serviceProviderEnName);
                                    // scope.loadOrderShippingPlanData.call(scope, data.id);
                                }
                            }
                        },
                    ]},
                    { field: 'serviceProviderId', xtype: 'hidden'},
                    { field: 'serviceProviderCnName',  xtype:'displayfield', fieldLabel:_lang.TText.fServiceProviderCnName, cls:'col-2',},
                    { field: 'serviceProviderEnName',  xtype:'displayfield', fieldLabel:_lang.TText.fServiceProviderEnName, cls:'col-2',},
                ] },

				//invoice
                { xtype: 'section', title:_lang.ServiceProviderInvoice.tabInvoice},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'type', xtype:'dictfield', code:'service_inquiry', codeSub:'quotation_type', fieldLabel: _lang.FlowServiceInquiry.fQuotationType, cls:'col-2'},
                    { field: 'serviceProviderQuotationId', xtype: 'hidden'},
                    { field: 'serviceProviderQuotation',  xtype:'displayfield', fieldLabel:_lang.FlowServiceInquiry.fQuotationId, cls:'col-2',},
                    { field: 'destinationPortId', xtype:'dictfield', code:'service_provider', codeSub:'destination_port', fieldLabel: _lang.FlowServiceInquiry.fDestination, cls:'col-2'},
                    { field: 'currencyAdjustment', xtype: 'displayfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fCurrencyAdjustment, decimalPrecision:4, cls:'col-2'},
                    { field: 'effectiveDate', xtype: 'displayfield', fieldLabel: _lang.FlowServiceInquiry.fEffectiveDate, format: curUserInfo.dateFormat, cls: 'col-2'},
                    { field: 'rateAudToRmb', xtype: 'displayfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fExchangeRateAudToRmb, cls:'col-2',
                        decimalPrecision:4
                    },
                    { field: 'validUntil', xtype: 'displayfield', fieldLabel: _lang.FlowServiceInquiry.fValidUntil, format: curUserInfo.dateFormat, cls: 'col-2', },
                    { field: 'rateAudToUsd', xtype: 'displayfield', minValue: 0, fieldLabel: _lang.FlowServiceInquiry.fExchangeRateAudToUsd, cls:'col-2',
                        decimalPrecision:4
                    },
                ]},


                //海运费用
                { field: 'freightCharges', xtype: 'ServiceProviderInvoiceFreightChargeGrid', fieldLabel: _lang.ServiceProviderInvoice.fFreightCharges,
                    frameId: conf.mainFormPanelId, scope:this, readOnly: this.isApprove, height:350
                },

                //本地费用明细
                { field: 'destinationCharges', xtype: 'ServiceProviderInvoiceDestinationChargeGrid', fieldLabel: _lang.FlowServiceInquiry.fDestinationCharges,
                    frameId: conf.mainFormPanelId, scope:this, readOnly: this.isApprove, height:350,
                    mainFormPanelId: conf.mainFormPanelId,
                    mainForm: this,
                    calculate: this.calculate
                },

                //本地费用明细
                { field: 'dutyCharges', xtype: 'ServiceProviderInvoiceDutyChargeGrid', fieldLabel: _lang.ServiceProviderInvoice.fDutyCharges,
                    frameId: conf.mainFormPanelId, scope:this, readOnly: this.isApprove, height:350,
                    mainFormPanelId: conf.mainFormPanelId,
                    mainForm: this,
                    calculate: this.calculate
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
		    if(this.record.type == 3){
                var cmp = Ext.getCmp(conf.mainFormPanelId);
                var cmpShippingPlanId = cmp.getCmpByName('main_shippingPlanName');
                cmpShippingPlanId.show();
            }
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);
					var cmp = Ext.getCmp(conf.mainFormPanelId);
                    if (!!json.data.ports) {
                        var grid = cmp.getCmpByName('freightCharges').subGridPanel;
                        var gridStore = grid.getStore();
                        var ports = json.data.ports;
                        gridStore.removeAll();
                        for(var index in ports){
                            ports[index].rateAudToRmb = grid.audToRmb;
                            ports[index].rateAudToUsd = grid.audToUsd;
                            gridStore.add(ports[index] || {});
                        }
                    }


                    if (!!json.data.chargeItems) {
                        var grid = cmp.getCmpByName('destinationCharges').subGridPanel;
                        var gridStore = grid.getStore();
                        var chargeItems = json.data.chargeItems;
                        gridStore.removeAll();
                        for(var index in chargeItems){
                            chargeItems[index].rateAudToRmb = grid.audToRmb;
                            chargeItems[index].rateAudToUsd = grid.audToUsd;
                            gridStore.add(chargeItems[index] || {});
                        }
                    }
				}
			});
		}

		this.editFormPanel.setReadOnly(this.isApprove, ['flowRemark']);
		this.loadOrderShippingPlanData();
        // this.startCalculate();
    },// end of the init
    
    saveFun: function(){
        var params = {act: this.actionName ? this.actionName: 'save'};
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        //freight charges
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('freightCharges').subGridPanel});
        if(!!rows && rows.length > 0){
            for(var index in rows){
                for(key in rows[index].data){
                    params['ports['+index+'].'+key ] = rows[index].data[key];
                }
                params['ports['+index+'].businessId'] = businessId;
            }
        }

        //destination charges
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('destinationCharges').subGridPanel});
        if(!!rows && rows.length > 0){
            for(var index in rows){
                for(key in rows[index].data){
                    params['chargeItems['+index+'].'+key ] = rows[index].data[key];
                }
                params['chargeItems['+index+'].businessId'] = businessId;
            }
        }

        $postForm({
			formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
			scope: this,
			url: this.urlSave,
			params: params,
            callback: function (fp, action, status, grid) {
				Ext.getCmp(this.winId).close();
                if(!!grid) {
                    grid.getSelectionModel().clearSelections();
                    grid.getView().refresh();
                }
			}
		});
    },

    extractProductDetailsFromOrder: function(){
        var products = {};
        for(var orderId in this.orders){
            var order = this.orders[orderId];
            for(var i = 0; i < order.details.length; i++){
                var detail = order.details[i];
                var product = order.details[i].product;
                if(!products[product.sku]){
                    var pcsPerCarton = product.prop.pcsPerCarton || 1;
                    products[product.sku] = {
                        //todo
                        tariffItem: product.prop.tariffItem || 1,
                        unitCbm: Ext.util.Format.round(parseFloat(product.prop.masterCartonCbm)/pcsPerCarton, 4),
                        priceAud: parseFloat(detail.priceAud),
                        priceRmb: parseFloat(detail.priceRmb),
                        priceUsd: parseFloat(detail.priceUsd)
                    }
                }
            }
        }

        this.products = products;
        this.orderQty = this.orders.length;
    },

    updateSkuPercent: function(){
        var planTotalCbm = 0;
        for(var orderId in this.orders){
            var order = this.orders[orderId];
            var orderTotalCbm = 0;
            for(var i = 0; i < order.details.length; i++){
                var product = order.details[i].product;
                if(!!order.details[i].orderQty && !! this.products[product.sku].unitCbm) {
                    orderTotalCbm += order.details[i].orderQty * this.products[product.sku].unitCbm;
                }
            }
            order.orderTotalCbm = orderTotalCbm;
            planTotalCbm += orderTotalCbm;

            // console.log('update sku percent');
            // console.log(order.orderTotalCbm);
            // console.log(planTotalCbm);
        }

        for(var orderId in this.orders){
            var order = this.orders[orderId];
            for(var i = 0; i < order.details.length; i++){
                var sku = order.details[i].product.sku;
                var tp = this.products[sku];

                if(!!order.orderTotalCbm && !!this.orderQty) {
                    tp['orderCbmPercent'] = tp.unitCbm / order.orderTotalCbm / this.orderQty;
                } else {
                    tp['orderCbmPercent'] = 0;
                }

                if(!!planTotalCbm) {
                    tp['planCbmPercent'] = tp.unitCbm / planTotalCbm;
                } else {
                    tp['planCbmPercent'] = 0;
                }
            }
        }
    },

    updateTotal1: function(aud, rmb, usd){
        this.total1Aud += aud;
        this.total1Rmb += rmb;
        this.total1Usd += usd;
    },

    updateTotal2: function(aud, rmb, usd){
        this.total2Aud += aud;
        this.total2Rmb += rmb;
        this.total2Usd += usd;
    },

    addFreightCost: function(){
        var grid = Ext.getCmp(this.mainFormPanelId).getCmpByName('freightCharges').subGridPanel;
        var range = grid.getStore().getRange();

        for(var i = 0; i < range.length; i++){
            var data = range[i].data;
            this.updateTotal1(data.priceTotalAud, data.priceTotalRmb, data.priceTotalUsd);
        }
    },

    addDestinationCost: function(){
        var grid = Ext.getCmp(this.mainFormPanelId).getCmpByName('destinationCharges').subGridPanel;
        var range = grid.getStore().getRange();

        for(var i = 0; i < range.length; i++){
            var chargeItem = range[i].data;
            var priceTotalAud = chargeItem.priceTotalAud;
            var priceTotalRmb = chargeItem.priceTotalRmb;
            var priceTotalUsd = chargeItem.priceTotalUsd;

            if(chargeItem.itemId == this.fuelSurchargeItemId) continue;
            if(chargeItem.itemId == this.sideLoaderItemId) {
                var fuelCharge = 0;
                for (var i = 0; i < this.destinationCharges.length; i++) {
                    if (this.destinationCharges[i].itemId != this.fuelSurchargeItemId) continue;
                    priceTotalAud += this.destinationCharges[i].priceOtherAud;
                    priceTotalRmb += this.destinationCharges[i].priceTotalRmb;
                    priceTotalUsd += this.destinationCharges[i].priceTotalUsd;
                }
            }

            for(var containerNumber in this.containers){
                var container = this.containers[containerNumber];
                if(parseInt(chargeItem.unitId) == this.perContainerUnitId){
                    this.updateTotal1(priceTotalAud, priceTotalRmb, priceTotalUsd);
                } else {
                    this.updateTotal2(priceTotalAud, priceTotalRmb, priceTotalUsd);
                }
            }
        }
    },

    loadFormData:function(data){
        var form = Ext.getCmp(this.mainFormPanelId);
        var freightGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('freightCharges').subGridPanel;
        var destinationGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('destinationCharges').subGridPanel;
        var dutyGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('dutyCharges').subGridPanel;
        var mainData = {
            orderShippingPlanId: data.flowOrderShippingPlanId,
            orderShippingPlan: data.flowOrderShippingPlanId,
            serviceProviderQuotationId: data.quotationId,
            serviceProviderQuotation: data.quotationId,
            destinationPortId: '',
            currencyAdjustment: data.currencyAdjustment,
            effectiveDate: data.effectiveDate,
            validUntil: data.validUntil,
            rateAudToRmb: data.rateAudToRmb,
            rateAudToUsd: data.rateAudToUsd
        };

        form.getForm().setValues(mainData);

        //reset port and freight charges container qty
        var types = ['gp20', 'gp40', 'hq40', 'lcl'];
        if(!!data.ports){
            for(var i = 0; i < data.ports.length; i++) {
                for(var j = 0; j < types.length; j++){
                    var type = types[j];
                    data.ports[i][type + 'Qty'] = 0;
                }
            }
        }

        if(!!data.chargeItems){
            for(var i = 0; i < data.chargeItems.length; i++) {
                for(var j = 0; j < types.length; j++){
                    var type = types[j];
                    data.chargeItems[i][type + 'Qty'] = 0;
                }
            }
        }

        //update container quantity
        if(!!data.packings) {
            var containers = [];
            var containerNumbers = [];
            for(var i = 0; i < data.packings.length; i++){
                var packing = data.packings[i];
                var index = containerNumbers.indexOf(packing.containerNumber);
                if(index > 0) continue;
                else {
                    containers.push({
                        originPortId : 1,
                        destinationPortId: 1,
                        containerNumber: packing.containerNumber,
                        containerType: parseInt(packing.containerType)
                    })
                }
            }

            var gp20Qty = 0;
            var gp40Qty = 0;
            var hq40Qty = 0;
            var lclQty = 0;
            for(var i = 0; i < containers.length; i++){
                var container = containers[i];
                var type = types[container.containerType - 1];
                console.log(type);
                if(container.containerType == 1) gp20Qty += 1;
                else if(container.containerType == 2) gp40Qty += 1;
                else if(container.containerType == 3) hq40Qty += 1;

                //freight
                for(var j = 0; j < data.ports.length; j++){
                    var port = data.ports[j];
                    if(port.originPortId == container.originPortId && port.destinationPortId == container.destinationPortId){
                        port[type + 'Qty'] += 1;
                        break;
                    }
                }
            }

            //destination
            for(var i = 0; i < data.chargeItems.length; i++){
                var chargeItem = data.chargeItems[i];
                if(chargeItem.unitId != this.perContainerUnitId) continue;
                else {
                    chargeItem['gp20Qty']  = gp20Qty;
                    chargeItem['gp40Qty']  = gp40Qty;
                    chargeItem['hq40Qty']  = hq40Qty;
                }
            }
        }

        if(!!data.ports){
            freightGrid.getStore().add(data.ports);
        }

        if(!!data.chargeItems){
            destinationGrid.getStore().add(data.chargeItems);
        }

        if(!!data.tariffs) {
            dutyGrid.getStore().add(data.tariffs);
        }
    },

    calculateCost: function(data){
        // if(!this.containers || !this.freightCharges || !this.orders) return;
        this.freightCharges = data.ports;
        this.destinationCharges = data.chargeItems;
        this.orders = data.orders;

        this.extractProductDetailsFromOrder();
        this.updateSkuPercent();
        this.addFreightCost();
        this.addDestinationCost();

        var currency = ['Aud', 'Rmb', 'Usd'];
        for(var sku in this.products){
            var product = this.products[sku];
            for(var i = 0; i < currency.length; i++){
                var c = currency[i];
                product['price' + c] = this['total1' + c] * product.planCbmPercent + this['total1' + c] * product.orderCbmPercent;
            }
        }

        // console.log('finish');
        // console.log(this.products);
    },

    loadOrderShippingPlanData: function(orderShippingPlanId){
        var scope = this;
        var url = __ctxPath + 'archives/service_provider_invoice/getQuotation?orderShippingPlanId=' + orderShippingPlanId;
        // var url = 'http://127.0.0.1:8080/purchase/archives/service_provider_invoice/getQuotation?orderShippingPlanId=FOSP20171025014138466619';
        Ext.Ajax.request({
            url: url,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var data = response.data;
                console.log(data);
                if(!!data) {
                    scope.loadFormData.call(scope, data);
                    scope.calculateCost.call(scope, data);
                }
            }
        });
    }
});
