
App.PortletView = Ext.extend(Ext.app.Portlet, {
    constructor : function(conf) {
        Ext.applyIf(this, conf);
        App.PortletView.superclass.constructor.call(this,{
            closable: true
        });
    },
    cls:'x-portlet x-panel-body-gray',
});


PermissionPortalView = function(height) {
    return new App.PortletView({
        id : 'PermissionPortalView',
        title : _lang.AccountPermission.tabMyPermission,
        collapsible: true,
        layout: 'fit',
        region: 'center',
        resizable: true,
        height: height || 400,
        header:{ cls:'x-panel-header-gray' },
        items: [{ xtype: 'uxiframe'}],
        url: __ctxPath + "pub/role",
        afterrender: function(layout, eOpts){
            layout.items.items[0].load(	__ctxPath + "pub/role");
        }
    });
};

MessagePortalView = function(height) {
    return new App.PortletView({
        id : 'MessagePortalView',
        title : _lang.Message.tabListTitle,
        collapsible: true,
        resizable: true,
        height: height || 170,
        header:{ cls:'x-panel-header-gray' },
        items: new HP.GridPanel({
            region: 'center',
            id: 'MessagePortalViewGrid',
            url: __ctxPath + "message/list",
            fields: ['id', 'read', 'title', 'toUserId', 'toUserCnName', 'toUserEnName', 'fromUserId', 'fromUserCnName', 'fromUserEnName','createdAt'],
            baseParams:{limit:5},
            bbar: null,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.Message.fRead, dataIndex: 'read', width: 50,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('gray', _lang.TText.vUnread);
                        if(value == '2') return $renderOutputColor('green', _lang.TText.vRead);
                    }
                },
                { header: _lang.Message.fSendTime, dataIndex: 'createdAt', width: 130 },
                { header: _lang.Message.fTitle, dataIndex: 'title', width: 200 },
                { header: _lang.Message.fSendUserId, dataIndex: 'fromUserId', width: 80, hidden:true },
                { header: _lang.Message.fSendUser, dataIndex: 'fromUserCnName', width: 80, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.Message.fSendUser, dataIndex: 'fromUserEnName', width: 90, hidden:curUserInfo.lang !='zh_CN'? false: true },
            ],// end of columns
            itemclick: function(obj, record, item, index, e, eOpts){
                App.clickTopTab('MessageView',{mid: record.data.id});
            }
        })
    });
};

TasksPortalView = function(height) {
    return new App.PortletView({
        id : 'TasksPortalView',
        title : _lang.Events.tabListTitle,
        collapsible: true,
        resizable: true,
        height: height || 172,
        header:{ cls:'x-panel-header-gray' },
        items: new HP.GridPanel({
            scope: this,
            url: __ctxPath + 'desktop/events/list',
            fields: ['id', 'businessId','taskName','startUserName', 'startTime', 'createTime', 'formKey'],
            baseParams:{limit:5},
            bbar: null,
            sorters: [{property: 'createTime', direction: 'DESC'}],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 80, hidden: true },
                { header: _lang.Events.fBusinessId, dataIndex: 'businessId', width: 150, hidden: true },
                { header: _lang.Events.fTaskName, dataIndex: 'taskName', width: 300 },
                { header: _lang.Events.fStartUser, dataIndex: 'startUserName', width: 80 },
                { header: _lang.Events.fStartTime, dataIndex: 'startTime', width: 130 },
                { header: _lang.Events.fArrivedTime, dataIndex: 'createTime', width: 130 }

            ],// end of columns
            itemclick: function(obj, record, item, index, e, eOpts){
                conf={};
                conf.record = {
                    id: record.data.businessId,
                    flowStatus: 1,
                    status: 1,
                };
                App.clickTopTab(record.data.formKey, conf);
            }
        })
    });
};

RatePortalView = function(height){
    return new App.PortletView({
        id : 'RatePortalView',
        title : _lang.ExchangeRate.tabPulishRate,
        collapsible: true,
        layout: 'fit',
        region: 'center',
        resizable: true,
        height: height || 110,
        header:{ cls:'x-panel-header-gray' },
        items: new HP.FormPanel({
            id: 'RatePortalViewTodayForm',
            fieldItems: [
                { field: 'rate_audToRmb', xtype: 'displayfield', fieldLabel: _lang.ExchangeRate.fRateAudToRmb, value: curUserInfo.audToRmb },
                { field: 'rate_audToUsd', xtype: 'displayfield', fieldLabel: _lang.ExchangeRate.fRateAudToUsd, value: curUserInfo.audToUsd },
            ]
        })
    });
}

PurchaseContractPortalView = function(height) {
    return new App.PortletView({
        id : 'PurchaseContractPortalView',
        title : _lang.FlowPurchasePlan.tabPurchaseContractList,
        collapsible: true,
        resizable: true,
        height: height || 210,
        header:{ cls:'x-panel-header-gray' },
        items: new HP.GridPanel({
            region: 'center',
            id: 'PurchaseContractPortalViewGrid',
            url:  __ctxPath + 'flow/purchase/purchasecontract/list',
            fields: [
                'id','orderNumber','orderTitle',
                'originPortId', 'destinationPortId', 'readyDate','shippingDate','status','flowStatus','processInstanceId',
                'currency','totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb','rateAudToUsd','updatedAt'
            ],
            baseParams:{limit:5},
            bbar: null,
            forceFit: false,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            columns: [
                { header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 182 },
                { header:_lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 90, },
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },
                { header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 70,
                    renderer: function(value){
                        var $port = _dict.origin;
                        if($port.length>0 && $port[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $port[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowServiceInquiry.fDestination, dataIndex: 'destinationPortId', width: 70,
                    renderer: function(value){
                        var $port = _dict.destination;
                        if($port.length>0 && $port[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $port[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowPurchaseContract.fReadyDate, dataIndex: 'readyDate', width: 80, format: curUserInfo.dateFormat},
                { header: _lang.ProductDocument.fShippingDate, dataIndex: 'shippingDate', width: 80,  format: curUserInfo.dateFormat },

                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
                    renderer: function(value){
                        if(value == null) return $renderOutputColor('gray', _lang.TText.vNotSubmit);
                        var $flowStatus = _dict.flowStatus;
                        if($flowStatus.length>0 && $flowStatus[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $flowStatus[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fUpdatedAt, dataIndex: 'updatedAt', width: 140 }
            ],// end of columns
            itemclick: function(obj, record, item, index, e, eOpts){
                conf={action: 'edit'};
                conf.record = {
                    id: record.raw.id,
                    flowStatus: record.raw.flowStatus,
                    status: record.raw.status,
                    processInstanceId: record.raw.processInstanceId
                };
                App.clickTopTab('FlowPurchaseContractForm', conf);
            }
        })
    });
};
PurchasePlanPortalView = function(height) {
    return new App.PortletView({
        id : 'PurchasePlanPortalView',
        title : _lang.FlowPurchasePlan.tabPurchasePlanList,
        collapsible: true,
        resizable: true,
        height: height || 210,
        header:{ cls:'x-panel-header-gray' },
        items: new HP.GridPanel({
            region: 'center',
            id: 'PurchasePlanPortalViewGrid',
            url:  __ctxPath + 'flow/purchase/plan/list',
            fields: [
                'id', 'vendorId','vendorCnName','vendorEnName','currency', 'totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb',
                'rateAudToUsd','totalCbm','depositRate','leadTime', 'creatorCnName','creatorEnName','startTime','endTime',
                'createdAt','status','flowStatus','processInstanceId',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName',
                'departmentEnName','updatedAt',
            ],
            baseParams:{limit:5},
            bbar: null,
            forceFit: false,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            columns: [
                { header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 182, },
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //报价
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                { header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
                    renderer: function(value){
                        if(value == null) return $renderOutputColor('gray', _lang.TText.vNotSubmit);
                        var $flowStatus = _dict.flowStatus;
                        if($flowStatus.length>0 && $flowStatus[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $flowStatus[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fUpdatedAt, dataIndex: 'updatedAt', width: 140 }
            ],// end of columns
            itemclick: function(obj, record, item, index, e, eOpts){
                conf={action: 'edit'};
                conf.record = {
                    id: record.raw.id,
                    flowStatus: record.raw.flowStatus,
                    status: record.raw.status,
                    processInstanceId: record.raw.processInstanceId
                };
                App.clickTopTab('FlowPurchasePlanForm', conf);
            }
        })
    });
};

