Ext.define('App.ServiceProviderQuotationDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.ServiceProviderQuotationDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.ServiceProviderQuotationDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}
        var selectedId = '';

        new ServiceProviderQuotationDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            meFormGrid: Ext.getCmp(this.formGridPanelId),
            subcallback: this.subcallback ? this.subcallback : null,
            callback:function(ids, titles, rows) {
                if(this.subcallback){
                    this.subcallback.call(this.scope, rows);
                }
            }}, false).show();
    }
});


ServiceProviderQuotationDialogWin = Ext.extend(HP.Window, {
    constructor: function (conf) {
        conf.title = _lang.FlowServiceInquiry.mTitle;
        conf.moduleName = 'FlowOrderShippingPlan';
        conf.winId = 'ServiceProviderQuotationDialogWinID';
        conf.mainGridPanelId = 'ServiceProviderQuotationDialogWinGridPanelID';
        conf.searchFormPanelId = 'ServiceProviderQuotationDialogWinSearchPanelID';
        conf.selectGridPanelId = 'ServiceProviderQuotationDialogWinSelectGridPanelID';
        conf.treePanelId = 'ServiceProviderQuotationDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'shipping/ServiceProviderQuotationDetail/list';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField : false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        ServiceProviderQuotationDialogWin.superclass.constructor.call(this, {
            id: conf.winId,
            scope: this,
            title: _lang.FlowServiceInquiry.mTitle,
            width: this.single ? 1080 : 1280,
            region: 'center',
            layout: 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.centerPanel]
        });
    },

    prepareParams: function(conf){
        var queryParams = {};
        var rows = $getGdItems({grid: this.meFormGrid});
        if(rows != undefined && rows.length>0){
            for(index in rows){
                queryParams['queryParams['+index+'].'+ 'originPortId' ] = rows[index].data.originPortId;
                queryParams['queryParams['+index+'].'+ 'destinationPortId' ] = rows[index].data.destinationPortId;
                queryParams['queryParams['+index+'].'+ 'containerType' ] = rows[index].data.containerType;
                queryParams['queryParams['+index+'].'+ 'containerQty' ] = rows[index].data.containerQty;
                queryParams['queryParams['+index+'].'+ 'orderNumber' ] = rows[index].data.orderNumber;

                if(typeof(rows[index].data.etd) != 'string') {
                    queryParams['queryParams[' + index + '].' + 'etd'] = Ext.Date.format(rows[index].data.etd, curUserInfo.dateFormat);
                } else {
                    queryParams['queryParams[' + index + '].' + 'etd'] = rows[index].data.etd;
                }
            }
        }

        return queryParams;
    },

    initUI: function (conf) {
        var params = this.prepareParams(conf);
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            onlyKeywords: true
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.FlowServiceInquiry.mTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            width: '100%',
            // minWidth: 1180,
            autoScroll: true,
            baseParams: params,
            url: __ctxPath + 'shipping/serviceinquiry/plan/listQuotations',
            fields: [
                'id', 'serviceProviderId', 'serviceProviderCnName', 'serviceProviderCnName', 'serviceProviderQuotationId',
                'effectiveFrom', 'validUntil', 'freight', 'insurance',
                'destinationCharges', 'priceTotal',
                'orderNumber', 'originPortId', 'destinationPortId','sailingDays',
                'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd', 'priceGp20InsuranceAud', 'priceGp20InsuranceRmb', 'priceGp20InsuranceUsd',
                'priceGp40Aud', 'priceGp40Rmb', 'priceGp40Usd', 'priceGp40InsuranceAud', 'priceGp40InsuranceRmb', 'priceGp40InsuranceUsd',
                'priceHq40Aud', 'priceHq40Rmb', 'priceHq40Usd', 'priceHq40InsuranceAud', 'priceHq40InsuranceRmb', 'priceHq40InsuranceUsd',
                'priceLclAud', 'priceLclRmb', 'priceLclUsd', 'priceLclInsuranceAud', 'priceLclInsuranceRmb', 'priceLclInsuranceUsd',
                'gp20Qty', 'gp40Qty', 'hq40Qty', 'lclCbm',
                'totalPriceChargeItemAud', 'totalPriceChargeItemRmb', 'totalPriceChargeItemUsd',
                'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd','eta','logisticsTime',
                'etd', 'effectiveDate', 'validUntil'
            ],
            columns: [
                {header: _lang.FlowServiceInquiry.fId, dataIndex: 'id', width: 180, hidden: true},
                {
                    header: _lang.FlowWarehousePlanning.fServiceProviderId,
                    dataIndex: 'serviceProviderId',
                    width: 180,
                    hidden: true
                },
                {
                    header: _lang.FlowWarehousePlanning.fServiceProviderName,
                    dataIndex: 'serviceProviderCnName',
                    width: 260,
                },
                {header: _lang.FlowServiceInquiry.fQuotationId, dataIndex: 'serviceProviderQuotationId', width: 180},
                {header: _lang.FlowServiceInquiry.fSailingDays, dataIndex: 'sailingDays', width: 180},
                //{header: _lang.FlowOrderShippingPlan.fLogisticsTime, dataIndex: 'logisticsTime', width: 180},

                { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100 ,
                    renderer: function(value){
                        var $loadingPort = _dict.getValueRow('origin', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $loadingPort.cnName;
                        }else{
                            return $loadingPort.enName;
                        }
                    }
                },
                { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.getValueRow('destination', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $destPort.cnName;
                        }else{
                            return $destPort.enName;
                        }
                    }
                },
                {
                    header: _lang.FlowServiceInquiry.fEffectiveDate,
                    dataIndex: 'effectiveDate',
                    width: 140,
                    format: curUserInfo.dateFormat,
                    renderer: function (value) {
                        if (typeof(value) == "string") {
                            return value;
                        } else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                {
                    header: _lang.FlowServiceInquiry.fValidUntil,
                    dataIndex: 'validUntil',
                    width: 140,
                    format: curUserInfo.dateFormat,
                    renderer: function (value) {
                        if (typeof(value) == "string") {
                            return value;
                        } else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },

                {
                    header: _lang.FlowServiceInquiry.fDestinationCharges,
                    columns: new $groupPriceColumns(this, 'totalPriceChargeItemAud', 'totalPriceChargeItemRmb', 'totalPriceChargeItemUsd', null, {edit: false}),
                },
                {
                    header: _lang.FlowServiceInquiry.fPriceGp20,
                    columns: [
                        {
                            header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceColumns(this, 'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceColumns(this, 'priceGp20InsuranceAud', 'priceGp20InsuranceRmb', 'priceGp20InsuranceUsd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceQty,
                            dataIndex: 'gp20Qty',
                            align: 'right',
                            width: 60,
                            renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                {
                    header: _lang.FlowServiceInquiry.fPriceGp40,
                    columns: [
                        {
                            header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceColumns(this, 'priceGp40Aud', 'priceGp40Rmb', 'priceGp40Usd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceColumns(this, 'priceGp40InsuranceAud', 'priceGp40InsuranceRmb', 'priceGp40InsuranceUsd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceQty,
                            dataIndex: 'gp40Qty',
                            align: 'right',
                            width: 60,
                            renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                {
                    header: _lang.FlowServiceInquiry.fPriceHq40,
                    columns: [
                        {
                            header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceColumns(this, 'priceHq40Aud', 'priceHq40Rmb', 'priceHq40Usd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceColumns(this, 'priceHq40InsuranceAud', 'priceHq40InsuranceRmb', 'priceHq40InsuranceUsd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceQty,
                            dataIndex: 'hq40Qty',
                            align: 'right',
                            width: 60,
                            renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                {
                    header: _lang.FlowServiceInquiry.fPriceLcl,
                    columns: [
                        {
                            header: _lang.FlowServiceInquiry.fPriceBaseOcean,
                            columns: new $groupPriceColumns(this, 'priceLclAud', 'priceLclRmb', 'priceLclUsd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceInsurance,
                            columns: new $groupPriceColumns(this, 'priceLclInsuranceAud', 'priceLclInsuranceRmb', 'priceLclInsuranceUsd', null, {edit: false}),
                        },
                        {
                            header: _lang.FlowServiceInquiry.fPriceCbm,
                            dataIndex: 'lclCbm',
                            align: 'right',
                            width: 60,
                            renderer: Ext.util.Format.numberRenderer('0.00')
                        }
                    ]
                },
                // {
                //     header: _lang.FlowServiceInquiry.fPriceTotal,
                //     columns: [
                //         new $groupPriceColumns(this, 'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', null, {edit: false}),
                //
                //         {
                //             header: _lang.FlowServiceInquiry.fPriceCbm,
                //             dataIndex: 'lclCbm',
                //             align: 'right',
                //             width: 60,
                //             renderer: Ext.util.Format.numberRenderer('0.00')
                //         }
                //     ]
                // },
            ],

            itemdblclick: function (obj, record, item, index, e, eOpts) {
                if (!!this.scope.selectedData) {
                    this.scope.selectedData.push(record.raw);
                }

                this.scope.winOk.call(this.scope);
            },
            callback: function (obj, records) {
                //初始化选择
                if (this.selectedId && records.length) {
                    for (var i = 0; i < records.length; i++) {
                        if (records[i].data.id == this.selectedId) {
                            // console.log(records);
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                }
                ;
            }
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [this.gridPanel]
            // items: [this.searchPanel, this.gridPanel]
        });

        //select records data, with details
        this.selectedData = [];
        // this.loadData();
    },

    winOk: function () {
        var ids = '';
        var names = '';
        var rows = {};
        if (this.single) {
            rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {
                    ids += ',';
                    names += ',';
                }
                ids += rows[i].data.id;
                names += rows[i].data.id;
            }
        } else {
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {
                    ids += ',';
                    names += ',';
                }
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.id;
            }
        }

        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            this.callback.call(this, ids, names, rows, win.selectedData);
        }
        Ext.getCmp(this.winId).close();
    },

    winClean: function () {
        var ids = '';
        var names = '';
        if (this.callback) {
            this.callback.call(this, ids, names);
        }
        Ext.getCmp(this.winId).close();
    },

    loadData: function () {
        var readyDate = this.getReadyDate();
        var listUrl = __ctxPath + 'shipping/serviceinquiry/listQuotations?readyDate=' + encodeURIComponent(readyDate);
        this.data = [];
        var scope = this;
        Ext.Ajax.request({
            url: listUrl,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                scope.quotes = response.data;
                scope.calculate();
            },
            failure: function () {
            }
        });
    },

    getReadyDate: function(){
        var grid = null;
        if (!!this.meFormGrid) grid = this.meFormGrid;
        else return null;

        var data = grid.getStore().getRange();
        var date = null;
        for (var i = 0; i < data.length; i++) {
            var row = data[i].data;
            if(!row.readyDate) continue;
            if(!date || row.readyDate < date) date = row.readyDate;
        }

        if(!!date){
            var tmp = date.split(' ');
            date = tmp[0];
        }
        return date;
    },

    loadOrders: function () {
        var grid = null;
        if (!!this.meFormGrid) grid = this.meFormGrid;
        else return null;

        var data = grid.getStore().getRange();
        var ret = [];
        for (var i = 0; i < data.length; i++) {
            ret.push(data[i].data);
        }

        return ret;
    },

    groupOrders: function(){
        var orders = this.loadOrders();
        if (!orders) return null;
        // console.log(orders);

        //group by origin port, count container qty
        var containerType = ['Gp20', 'Gp40', 'Hq40', 'Lcl'];
        var orderGroups = [];
        var orderGroupsOriginPorts = [];
        for (var i = 0; i < orders.length; i++) {
            var order = orders[i];
            var index = orderGroupsOriginPorts.indexOf(order.originPortId);
            if (index < 0) {
                orderGroupsOriginPorts.push(order.originPortId);
                var item = {
                    order: order.orderId,
                    readyDate: order.readyDate,
                    originPortId: order.originPortId,
                    destinationPortId: order.destinationPortId
                };

                var type = containerType[parseInt(order.containerType) - 1];
                item[type] = parseFloat(order.containerQty);
                orderGroups.push(item);
            } else {
                item = orderGroups[index];
                item.order += ', ' + order.orderId;

                var type = containerType[parseInt(order.containerType) - 1];
                if (!!item[type]) {
                    item[type] += parseFloat(order.containerQty);
                } else {
                    item[type] = parseFloat(order.containerQty);
                }
            }
        }

        return orderGroups;
    },

    calculate: function () {
        var orderGroups = this.groupOrders();
        if(!orderGroups || orderGroups.length == 0) return;

        // console.log('quote');
        // console.log(this.quotes);
        //
        // console.log('order groups');
        // console.log(orderGroups);

        //calculate
        var quotes = this.quotes;
        var data = [];
        for (var i = 0; i < quotes.length; i++) {
            var quote = quotes[i];
            for (var j = 0; j < orderGroups.length; j++) {
                var order = orderGroups[j];
                var port = null;
                for (var k = 0; k < quote.ports.length; k++) {
                    if (quote.ports[k].originPortId == order.originPortId && quote.ports[k].destinationPortId == order.destinationPortId) {
                        port = quote.ports[k];
                    }
                }
                if (!port) continue;

                var gridDataRow = {
                    originPortId: order.originPortId,
                    destinationPortId: order.destinationPortId,
                    order: order.order,
                    serviceProviderId: quote.serviceProviderId,
                    serviceProviderCnName: quote.serviceProviderCnName,
                    serviceProviderEnName: quote.serviceProviderEnName,
                    quotationId: quote.id,
                    readyDate: order.readyDate,
                    effectiveDate: quote.effectiveDate,
                    validUntil: quote.validUntil,
                    priceGp20Aud: port.priceGp20Aud,
                    priceGp20Usd: port.priceGp20Usd,
                    priceGp20Rmb: port.priceGp20Rmb,
                    priceGp40Aud: port.priceGp40Aud,
                    priceGp40Usd: port.priceGp40Usd,
                    priceGp40Rmb: port.priceGp40Rmb,
                    priceHq40Aud: port.priceHq40Aud,
                    priceHq40Usd: port.priceHq40Usd,
                    priceHq40Rmb: port.priceHq40Rmb,
                    priceLclAud: port.priceLclAud,
                    priceLclUsd: port.priceLclUsd,
                    priceLclRmb: port.priceLclRmb,
                    priceGp20InsuranceAud: port.priceGp20InsuranceAud,
                    priceGp20InsuranceUsd: port.priceGp20InsuranceUsd,
                    priceGp20InsuranceRmb: port.priceGp20InsuranceRmb,
                    priceGp40InsuranceAud: port.priceGp40InsuranceAud,
                    priceGp40InsuranceUsd: port.priceGp40InsuranceUsd,
                    priceGp40InsuranceRmb: port.priceGp40InsuranceRmb,
                    priceHq40InsuranceAud: port.priceHq40InsuranceAud,
                    priceHq40InsuranceUsd: port.priceHq40InsuranceUsd,
                    priceHq40InsuranceRmb: port.priceHq40InsuranceRmb,
                    priceLclInsuranceAud: port.priceLclInsuranceAud,
                    priceLclInsuranceUsd: port.priceLclInsuranceUsd,
                    priceLclInsuranceRmb: port.priceLclInsuranceRmb
                };

                var containerType = ['Gp20', 'Gp40', 'Hq40', 'Lcl'];
                var currency = ['Aud', 'Rmb', 'Usd'];
                var prefix = containerType[order.containerType - 1];

                //calculate freight charges
                for (var c = 0; c < currency.length; c++) {
                    var suffix1 = currency[c];
                    var freightCharges = 0;
                    var destinationCharges = 0;
                    for (var t = 0; t < containerType.length; t++) {
                        var suffix2 = containerType[t];
                        if(!order[suffix2]) continue;

                        //freight charges
                        freightCharges += (gridDataRow['price' + suffix2 + suffix1] || 0) * order[suffix2]
                            + (gridDataRow['price' + prefix + 'Insurance' + suffix1] || 0) * order[suffix2];

                        //destination charges
                        for (var k = 0; k < quote.chargeItems.length; k++) {
                            var chargeItem = quote.chargeItems[k];
                            if(chargeItem.unitId == 1){
                                var price = chargeItem['price' + suffix2 + suffix1];
                                if(!!price) {
                                    price = parseFloat(price);
                                    destinationCharges += price * order[suffix2];
                                }
                            } else {
                                var price = chargeItem['priceOther' + suffix2];
                                if(!!price) {
                                    destinationCharges += parseFloat(price);
                                }
                            }
                        }

                        gridDataRow[suffix2.toLocaleLowerCase() + 'Qty'] = order[suffix2];
                    }

                    gridDataRow['freightCharges' + suffix1] = freightCharges;
                    gridDataRow['destinationCharges' + suffix1] = destinationCharges;
                    gridDataRow['priceTotal' + suffix1] = freightCharges + destinationCharges;
                }

                data.push(gridDataRow);
            }
        }

        // console.log('grid data');
        // console.log(data);
        this.gridPanel.getStore().loadData(data);
    },
});