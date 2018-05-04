Ext.define('App.FlowOrderShippingConfirmationFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowOrderShippingConfirmationFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
            title : _lang.FlowOrderShippingConfirmation.mTitle,
            moduleName: 'FlowOrderShippingConfirmation',
            winId : 'FlowOrderShippingConfirmationForm',
            frameId : 'FlowOrderShippingConfirmationView',
            mainGridPanelId : 'FlowOrderShippingConfirmationGridPanelID',
            mainFormPanelId : 'FlowOrderShippingConfirmationFormPanelID',
            processFormPanelId: 'FlowOrderShippingConfirmationProcessFormPanelID',
            searchFormPanelId: 'FlowOrderShippingConfirmationSearchFormPanelID',
            mainTabPanelId: 'FlowOrderShippingConfirmationMainTabsPanelID',
            subOrderGridPanelId : 'FlowOrderShippingConfirmationSubOrderGridPanelID',
            formGridPanelId : 'FlowOrderShippingConfirmationFormGridPanelID',

        };
        
        this.initUIComponents(conf);
        
        App.FlowOrderShippingConfirmationFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 600, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [{
                type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
                handler: function(event, toolEl, panelHeader) {
                	this.conf = conf;
                	this.onRowAction.call(this);
            }},{
            	type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            	handler: function(event, toolEl, panelHeader) {
            		this.setHeight(260);
        			this.formGridPanel.setHeight(258);
            }},{
        		type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
        		handler: function(event, toolEl, panelHeader) {
        			this.setHeight(700);
        			this.formGridPanel.setHeight(698);
           	}}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowOrderShippingConfirmation.tabOrderDetail,
            forceFit: false,
            width: 'auto',
            height: 600,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'ID','orderShippingPlanId','orderShippingPlanBusinessId', 'orderId', 'orderNumber','orderTitle','originPortId','originPortCnName','originPortEnName',
                'destinationPortId','destinationPortCnName','destinationPortEnName','containerType','readyDate','etd', 'eta',
                'containerQty', 'serviceProviderId','serviceProviderName','serviceProviderCnName','serviceProviderEnName', 'serviceProviderQuotationId',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
				{ header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
					keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
					actions: [
                        // {
                        //     iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
                        //     callback: function(grid, record, action, idx, col, e, target) {
                        //         this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        //     }
                        // },
                        {
                            iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                            callback: function(grid, record, action, idx, col, e, target) {
                                this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                            }
                        }
					]
				},

                { header: 'ID', dataIndex: 'id', width: 200, hidden: true },
                { header: _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 200, hidden: true },
                { header: _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'orderShippingPlanBusinessId', width: 200, hidden: true },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 200, hidden: true},
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 200},
                { header: _lang.FlowPurchaseContract.fSubject, dataIndex: 'orderTitle', width: 120, },
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
                        renderer: function(value){
                            var $containerType = _dict.containerType;
                            if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                               return $containerType[0].data.options[parseInt(value) - 1].title;
                            }
                        },
                },
                { header: _lang.FlowOrderShippingPlan.fContainerQty, dataIndex: 'containerQty', width: 80,
                    editor: {xtype: 'textfield',   },
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fReadyDate, dataIndex: 'readyDate', width: 140, format: curUserInfo.dateFormat,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etd', width: 140, format: curUserInfo.dateFormat,
                    renderer: function(value, meta){
                        meta.tdCls = 'grid-input';
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    },
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat, }
                },
                { header: _lang.FlowOrderShippingConfirmation.fEta, dataIndex: 'eta', width: 140, dateFormat: curUserInfo.dateFormat,
                    renderer: function(value, meta){
                        meta.tdCls = 'grid-input';
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    },
                    editor: {xtype: 'datetimefield', format: curUserInfo.dateFormat, }
                },
                { header: _lang.FlowWarehousePlanning.fServiceProviderId, dataIndex: 'serviceProviderId', width: 260},
                { header: _lang.FlowWarehousePlanning.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 260},
                { header: _lang.FlowWarehousePlanning.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 260},
                { header: _lang.FlowServiceInquiry.fQuotationId, dataIndex: 'serviceProviderQuotationId', width: 160},
            ],// end of columns
        });
    },
    
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new OrderShippingPlanDialogWin({
                    single:true,
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : selectedId,
                    type:1,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, names, rows, selectedData) {
                        if(!!selectedData){
                            var data = selectedData[0].details;
                            // console.log(data);
                            this.meGrid.getStore().insert(idx, data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new OrderShippingPlanDialogWin({
                    single:false,
                    frameId : 'FlowOrderShippingConfirmationView',
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : '',
                    type:1,
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, names, rows, selectedData) {
                        if(!!selectedData){
                            var data = [];
                            //storeData['id'] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[id].raw.id))

                            for(var i = 0; i < selectedData.length; i++){
                                    for(var index in  selectedData[i].details){
                                        selectedData[i].details[index].orderShippingPlanBusinessId = selectedData[i].businessId;
                                        selectedData[i].details[index].id = selectedData[i].details[index].orderId
                                    }
                                    data.push(selectedData[i].details);
                                    if(selectedData[i].id != undefined && !$checkGridRowExist(this.meGrid.getStore(), selectedData[i].details[index].orderId )  ){
                                        this.meGrid.getStore().add(selectedData[i].details);
                                    }
                            }
                             console.log(1);

                        }
                    }}, false).show();

                break;
        }
    }

});
