FlowWarehousePlanningView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
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
			mainTabPanelId: 'FlowWarehousePlanningMainTabsPanelID',
			subOrderGridPanelId : 'FlowWarehousePlanningPlanSubOrderGridPanelID',
			formGridPanelId : 'FlowWarehousePlanningFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/warehousePlan/list',
			urlSave: __ctxPath + 'flow/shipping/warehousePlan/save',
			urlDelete: __ctxPath + 'flow/shipping/warehousePlan/delete',
			urlGet: __ctxPath + 'flow/shipping/warehousePlan/get',
			urlExport: __ctxPath + 'flow/shipping/warehousePlan/export',
			urlListProduct: __ctxPath + 'flow/shipping/warehousePlan/listproduct',
			urlFlow: __ctxPath + 'flow/shipping/warehousePlan/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowWarehousePlan&processInstanceId=',
			refresh: true,
			add: true,
			copy: true,
			edit: true,
			del: true,
			flowMine:true,
			flowInvolved:true,
			export:true,
			editFun: this.editRow,
		};

		this.initUIComponents(conf);
		FlowWarehousePlanningView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel, this.centerPanel ]
		});
	},
    
	initUIComponents: function(conf) {
	
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
				{field:'Q-id-S-LK', xtype:'textfield', title:_lang.TText.fId},
				 {field:'Q-originPlace-S-LK', xtype:'textfield', title:_lang.FlowWarehousePlanning.fOriginAddress},
				    {field:'Q-destinationPlace-S-LK', xtype:'textfield', title:_lang.FlowWarehousePlanning.fDestAddress},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['0', _lang.TText.vDraft]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			   
			    {field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fOrderNumber,},
			    {field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fOrderTitle,},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-receiveStartDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowWarehousePlanning.fReceiveStartDate, format: curUserInfo.dateFormat},
			    { field: 'Q-receiveEndDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowWarehousePlanning.fReceiveEndDate, format: curUserInfo.dateFormat},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			  
			]
		});// end of searchPanel

        //grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowWarehousePlanning.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
            //groupField: 'remark',
            //features: [{ftype:'grouping', groupHeaderTpl: '{name}'}],
            // features: [{ftype:'grouping', groupHeaderTpl: '{group}'}],
            // features: [{ftype:'grouping', groupHeaderTpl: 'Group: {group} ({rows.length})'}],
			fields: [
				'id', 'remark', 'dispatchDate','eta','containerNumber','containerType','presale','inspection','serviceProviderId','serviceProviderCnName',
				'serviceProviderEnName','receiveDate','receiveStartDate','receiveEndDate',
                'containerType', 'containerNumber', 'orderNo', 'productCategory', 'presale',
				'dispatchDate', 'originPlace', 'destinationPlace', 'receiveStartDate', 'receiveEndDate',
                'inspection', 'eta', 'serviceProvider','hold',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName',
				'departmentId','departmentCnName','departmentEnName','originPlace','destinationPlace',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','orderNumber','orderTitle'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowWarehousePlanning.fId, dataIndex: 'id', width: 180  },
                { header: _lang.FlowWarehousePlanning.fDispatchDate, dataIndex: 'dispatchDate', width: 140, },
                { header: _lang.FlowCustomClearance.fOrderNumber, dataIndex: 'orderNumber', width: 160,  },
                { header: _lang.FlowCustomClearance.fOrderTitle, dataIndex: 'orderTitle', width: 160,  },
                { header: _lang.FlowWarehousePlanning.fOriginAddress, dataIndex: 'originPlace', width: 100,  },
                { header: _lang.FlowWarehousePlanning.fDestAddress, dataIndex: 'destinationPlace', width: 100,  },
                { header: _lang.FlowWarehousePlanning.fReceiveStartDate, dataIndex: 'receiveStartDate', width: 140,  },
                { header: _lang.FlowWarehousePlanning.fReceiveEndDate, dataIndex: 'receiveEndDate', width: 140,  },
            ],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.orderGridPanel = new HP.GridPanel({
            id: conf.subOrderGridPanelId,
            title: _lang.FlowWarehousePlanning.tabWarehouse,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'containerType', 'containerNumber', 'orderNo', 'vendorProductCategoryId', 'presale', 'dispatchDate','orderNumber',
                'flagOrderQcStatus', 'eta', 'serviceProviderCnName', 'serviceProviderEnName', 'vendorCnName', 'vendorEnName',
                'group', 'orderTitle','orderIndex','orderShippingPlanId','flowOrderShippingPlanId'
            ],
            columns: [
                { header: _lang.FlowCustomClearance.fOrderNumber, dataIndex: 'orderNumber', width: 120},
                { header: _lang.FlowCustomClearance.fOrderTitle, dataIndex: 'orderTitle', width: 120},
                { header: _lang.FlowPurchaseContract.fOrderIndex, dataIndex: 'orderIndex', width: 120},
                { header:  _lang.FlowOrderShippingPlan.fOrderShippingPlanId, dataIndex: 'orderShippingPlanId', width: 180, hidden:true, },
                { header:  _lang.FlowOrderShippingPlan.fFlowOrderShippingPlanId, dataIndex: 'flowOrderShippingPlanId', width: 180, },
                { header: _lang.FlowWarehousePlanning.fContainerNumber, dataIndex: 'containerNumber', width: 180},
                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                            return $containerType[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                },
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
            ]// end of columns
        });
        
        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.orderGridPanel);

		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, viewTabs ]
		});
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
		var productList = Ext.getCmp(conf.subOrderGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(json.data.details.length > 0){
                    for(index in json.data.details){
                        var order = {};
                        Ext.applyIf(order, json.data.details[index]);
                        productList.getStore().add(order);
                    }
                }
            }
        });
		
        var cmpDirection = Ext.getCmp(conf.mainTabPanelId + '-view-direction');
        cmpDirection.clean();
        cmpDirection.load(conf.urlDirection + record.data.processInstanceId);
		
		var cmpHistory = Ext.getCmp(conf.mainTabPanelId + '-view-history');
		cmpHistory.getStore().removeAll();
		if(record.data.processInstanceId){
			cmpHistory.getStore().url = conf.urlHistoryList;
			$HpSearch({
				searchQuery: {"Q-businessId-S-EQ": record.data.id},
				gridPanel: cmpHistory
			});
		};
		
	},
	editRow : function(conf){
		App.clickTopTab('FlowWarehousePlanningForm', conf);
	}
});