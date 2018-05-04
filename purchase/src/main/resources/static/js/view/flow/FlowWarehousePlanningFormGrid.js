Ext.define('App.FlowWarehousePlanningFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowWarehousePlanningFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
            title : _lang.FlowWarehousePlanning.mTitle,
            moduleName: 'FlowWarehousePlanning',
            winId : 'FlowWarehousePlanningForm',
            frameId : 'FlowWarehousePlanningView',
            mainGridPanelId : 'FlowWarehousePlanningGridPanelID',
            mainFormPanelId : 'FlowWarehousePlanningFormPanelID',
            processFormPanelId: 'FlowWarehousePlanningProcessFormPanelID',
            searchFormPanelId: 'FlowWarehousePlanningSearchFormPanelID',
            mainTabPanelId: 'FlowWarehousePlanningTabsPanelID',
            subOrderGridPanelId : 'FlowWarehousePlanningSubOrderGridPanelID',
            formGridPanelId : 'FlowWarehousePlanningFormGridPanelID',

        };
        
        this.initUIComponents(conf);
        
        App.FlowWarehousePlanningFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 500, width: '100%',
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
            title: _lang.FlowWarehousePlanning.fContainer,
            forceFit: false,
            width: 'auto',
            height: 500,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            // edit: !this.readOnly,
            edit: true,
            //multiSelect: true,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','containerType', 'containerNumber', 'orderNo', 'vendorProductCategoryId', 'presale', 'dispatchDate', 'orderId','orderNumber',
                'flagOrderQcStatus', 'eta', 'serviceProviderCnName', 'serviceProviderEnName', 'vendorCnName', 'vendorEnName',
                'group', 'orderTitle','orderIndex','orderShippingPlanId','flowOrderShippingPlanId'
            ],
            columns: [

                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },

                { header:'ID', dataIndex: 'id', width: 120, hidden: true},
                { header: _lang.FlowCustomClearance.fOrderNumber, dataIndex: 'orderNumber', width: 120},
                { header: _lang.FlowCustomClearance.fOrderTitle, dataIndex: 'orderTitle', width: 120},
                { header: _lang.FlowPurchaseContract.fOrderIndex, dataIndex: 'orderIndex', width: 120},
                { header:  _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 180, hidden:true, },
                { header:  _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'flowOrderShippingPlanId', width: 180, },
                { header: _lang.FlowWarehousePlanning.fPresale, dataIndex: 'presale', width: 80 ,
                    renderer: function(value, meta){
                        meta.tdCls = 'grid-input';
                        var dict = _dict.optionsYesNo;
                        if(value  && dict.length > 0 && dict[0].data.options.length > 0){
                            return dict[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                    editor: {xtype: 'dictcombo', code:'options', codeSub:'yesno'  }
                },

                { header: _lang.FlowWarehousePlanning.fContainerNumber, dataIndex: 'containerNumber', width: 180},
                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                            return $containerType[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 120,  hidden:true, },
                { header: _lang.ProductCategory.fCategory, dataIndex: 'vendorProductCategoryId', width: 120, hidden:true, },
                { header: _lang.FlowWarehousePlanning.fInspection, dataIndex: 'flagOrderQcStatus', width: 120 , hidden:true,
                    renderer: function(value){
                        var $orderQcStatus = _dict.orderQcStatus;
                        if(value  && $orderQcStatus.length > 0 && $orderQcStatus[0].data.options.length>0){
                            return $orderQcStatus[0].data.options[parseInt(value) - 1].title;
                        }
                    }
                },
                { header: _lang.FlowWarehousePlanning.fEta, dataIndex: 'eta', width: 140, format: curUserInfo.dateFormat,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                // { header: _lang.FlowWarehousePlanning.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 80, },
                // { header: _lang.FlowWarehousePlanning.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 80, },
                // { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 200},
                // { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 200},
            ],
            afterrender: function(grid){
                // console.log('after render');
                if(!grid.selectedContainers) return;

                var selectedContainers = grid.selectedContainers;
                var store = grid.getStore();
                var data = store.getRange();
                for(var i = 0; i < data.length; i++){
                    if(selectedContainers.includes(data[i].data.containerNumber)) {
                        grid.getSelectionModel().select(store.getAt(i), true);
                    }
                }
            }
        });
    },
    
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new PackingListDialogWin({
                    single:true,
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : selectedId,
                    type:1,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new PackingListDialogWin({
                    single:false,
                    frameId : 'FlowWarehousePlanningView',
                    fieldValueName: 'main_orders',
                    fieldTitleName: 'main_orderName',
                    selectedId : '',
                    type:1,
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result, data) {
                       // console.log('packing list dialog call back');
                        var gridData = [];
                        var orders = [];
                        for(var i = 0; i < data.length; i++){
                            var details = data[i].packingOrders;
                            for(var j = 0; j < details.length; j++){
                                this.meGrid.getStore().add(details[j]);
                                // if(orders.includes(details[j].orderId)) {
                                //     continue;
                                // } else {
                                //     orders.push(details[j].orderId);
                                // }
                                // if( data[i] != undefined &&  !$checkGridRowExist(this.meGrid.getStore(), data[i].id)){
                                //     gridData.push({
                                //         id: data[i].id,
                                //         containerNumber: data[i].containerNumber,
                                //         containerType: data[i].containerType,
                                //         sealsNumber: data[i].sealsNumber,
                                //         eta: data[i].eta,
                                //         orderId:data[i].orderId,
                                //         orderTitle: details[j].orderTitle,
                                //         orderNumber: details[j].orderNumber,
                                //         orderIndex: details[j].orderIndex,
                                //         productCategory: details[j].category,
                                //         vendorProductCategoryId: details[j].category,
                                //         flagOrderQcStatus: details[j].flagOrderQcStatus,
                                //         serviceProviderCnName: details[j].serviceProviderCnName,
                                //         serviceProviderEnName: details[j].serviceProviderEnName,
                                //         vendorCnName: details[j].vendorCnName,
                                //         vendorEnName: details[j].vendorEnName,
                                //         flowOrderShippingPlanId:data[i].flowOrderShippingPlanId,
                                //         orderShippingPlanId:data[i].orderShippingPlanId,
                                //     });
                                // }

                            }
                        }
                        return false;

                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    this.meGrid.getStore().add(items[index].raw);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }

});
