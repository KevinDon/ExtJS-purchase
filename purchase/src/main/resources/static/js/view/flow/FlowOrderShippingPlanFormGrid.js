Ext.define('App.FlowOrderShippingPlanFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowOrderShippingPlanFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowOrderShippingPlan.mTitle,
            moduleName: 'FlowOrderShippingPlan',
            winId : 'FlowOrderShippingPlanForm',
            frameId : 'FlowOrderShippingPlanView',
            mainGridPanelId : 'FlowOrderShippingPlanGridPanelID',
            mainFormPanelId : 'FlowOrderShippingPlanFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'FlowOrderShippingPlanSearchFormPanelID',
            mainTabPanelId: 'FlowOrderShippingPlaMainTabsPanelID',
            subOrderGridPanelId : 'FlowOrderShippingPlanSubOrderGridPanelID',
            formGridPanelId : 'FlowOrderShippingPlanFormGridPanelID',
            clearServiceProviderQuotation: this.clearServiceProviderQuotation,
            scope: this.scope,
        };
        conf.defHeight = this.height || 300;

        this.initUIComponents(conf);

        App.FlowOrderShippingPlanFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },

    initUIComponents: function(conf){
        var scope = this;
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight -3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(600);
                this.formGridPanel.setHeight(597);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowOrderShippingPlan.tabPlanDetail,
            forceFit: false,
            width: '100%',
            height: conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            sort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'orderId','orderNumber','orderTitle','originPortId','orderBusinessId','id',
                'destinationPortId','containerType','readyDate','etd',
                'serviceProviderId','serviceProviderName','serviceProviderCnName','serviceProviderEnName','containerQty',
                'serviceProviderQuotationId'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'orderId', width: 180,  hidden:true,  },
                { header: _lang.FlowOrderShippingPlan.fOrderBusinessId, dataIndex: 'orderBusinessId', width: 180,  },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},

                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },

                { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100 ,
                    renderer: function(value){
                        var $loadingPort = _dict.getValueRow('origin', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $loadingPort.cnName;
                        }else{
                            return $loadingPort.enName;
                        }
                    },
                },

                { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.getValueRow('destination', value);
                        if(curUserInfo.lang == 'zh_CN'){
                            return $destPort.cnName;
                        }else{
                            return $destPort.enName;
                        }
                    },
                },
                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value,meta){
                        meta.tdCls = 'grid-input';
                        var $containerType = _dict.containerType;
                        if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                            return $containerType[0].data.options[parseInt(value) - 1].title;
                        }
                    },

                    editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'container_type',
                        scope:this, selectFun: function(records, eOpts){
                            this.scope.clearServiceProviderQuotation.call(conf.scope)
                        }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fContainerQty, dataIndex: 'containerQty', width: 80,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'textfield',   }
                },


                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etd', width: 140, format: curUserInfo.dateFormat,
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat},
                    renderer: function(value,meta){
                        meta.tdCls = 'grid-input';
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fReadyDate, dataIndex: 'readyDate', width: 140, format: curUserInfo.dateFormat,
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat},
                    renderer: function(value, meta){
                        meta.tdCls = 'grid-input';
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },

                // { header: _lang.ServiceProviderDocument.fServiceProviderId, dataIndex: 'serviceProviderId', width: 160,
                //     editor: { xtype: 'ServiceProviderQuotationDialog', formId: conf.formGridPanelId,mainFormPanelId:conf.mainFormPanelId,
                //         valueType:'grid', scope:this, readOnly: this.isApprove, single:true,
                //         subcallback: function (rows) {
                //             // console.log(rows);
                //             var data = rows[0].raw;
                //             // console.log(data);
                //             var gridRow = scope.formGridPanel.getSelectionModel().selected.getAt(0);
                //             gridRow.set('serviceProviderId', data.serviceProviderId);
                //             gridRow.set('serviceProviderCnName', data.serviceProviderCnName);
                //             gridRow.set('serviceProviderEnName', data.serviceProviderEnName);
                //             gridRow.set('serviceProviderQuotationId', data.quotationId);
                //         }
                //     }
                // },
                // { header: _lang.FlowWarehousePlanning.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 160},
                // { header: _lang.FlowWarehousePlanning.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 160},
                // { header: _lang.FlowServiceInquiry.fQuotationId, dataIndex: 'serviceProviderQuotationId', width: 160},
            ]// end of columns
        });


    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new OrderDialogWin({
                    single:true,
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : selectedId,
                    formal:true,
                    type: 3,
                    scope: this,
                    checkDestinationPort: this.checkDestinationPort,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(selectedData.length>0){
                            var data = selectedData;
                            var order = {};
                            Ext.apply(order, data[0].raw);
                            Ext.applyIf(order, data[0].raw.prop);
                            order.orderBusinessId = data[0].raw.businessId
                            order.orderId = data[0].raw.id
                            this.meGrid.getStore().insert(idx, order);
                            this.meGrid.getStore().removeAt(idx+1);

                            if(!!conf.clearServiceProviderQuotation) {
                                conf.clearServiceProviderQuotation.call(conf.scope);
                            }
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                // if(!!conf.clearServiceProviderQuotation) {
                //     conf.clearServiceProviderQuotation.call(conf.scope);
                // }
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new OrderDialogWin({
                    single:false,
                    frameId : 'FlowOrderShippingPlanView',
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : '',
                    type: 3,
                    scope: this,
                    checkDestinationPort: this.checkDestinationPort,
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(selectedData.length > 0){
                            var items = selectedData;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    var data = items[index].raw;
                                    data.orderBusinessId = items[index].raw.businessId
                                    data.orderId = items[index].raw.id
                                    this.meGrid.getStore().add(data);
                                }
                            }

                            if(!!this.scope.clearServiceProviderQuotation) {
                                this.scope.clearServiceProviderQuotation.call(this.scope.scope);
                            }
                        }
                    }}, false).show();
                break;
        }
    },
    checkDestinationPort: function(selectReord){
        console.log(1);
        if(this.formGridPanel.getStore().getCount() > 0){
            if(selectReord != this.formGridPanel.getStore().getAt(0).data.destinationPortId){
                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedDestinationPortRecord);
                return;
            }

        }
    }
});
