FlowOrderShippingConfirmationView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
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

			urlList: __ctxPath + 'flow/shipping/orderShippingApply/list',
			urlSave: __ctxPath + 'flow/shipping/orderShippingApply/save',
			urlDelete: __ctxPath + 'flow/shipping/orderShippingApply/delete',
			urlGet: __ctxPath + 'flow/shipping/orderShippingApply/get',
			urlExport: __ctxPath + 'flow/shipping/orderShippingApply/export',
			urlListProduct: __ctxPath + 'flow/shipping/orderShippingApply/listproduct',
			urlFlow: __ctxPath + 'flow/shipping/orderShippingApply/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowOrderShippingApply&processInstanceId=',
			refresh: true,
			add: true,
			// copy: true,
			edit: true,
			del: true,
			flowMine:true,
			flowInvolved:true,
			export:true,
			editFun: this.editRow,
		};

		this.initUIComponents(conf);
		FlowOrderShippingConfirmationView.superclass.constructor.call(this, {
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
				//{field:'Q-totalContainerQty-N-EQ', xtype:'textfield', title: _lang.FlowOrderShippingPlan.fTotalContainerQty},
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled],  ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowOrderShippingConfirmation.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			rsort: false,
			fields: [
				'id','orderShippingApplyId','orderNumbers',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold', 'etds', 'etas','orderTitles'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.ProductCertificate.fId, dataIndex: 'id', width: 180  },
                /* {header:  _lang.FlowOrderShippingPlan.fTotalContainerQty, dataIndex: 'totalContainerQty', width: 120},
                 { header: _lang.FlowOrderShippingConfirmation.fOrderShippingApplyId, dataIndex: 'orderShippingApplyId', width: 160  },*/
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumbers', width: 200},
                { header: _lang.TText.fOrderTitle, dataIndex: 'orderTitles', width: 200},
                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etds', width: 140},
                { header: _lang.FlowOrderShippingPlan.fEta, dataIndex: 'etas', width: 140},
			],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.orderGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-2',
            title: _lang.ProductDocument.tabOrderListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
				'id','orderId','orderNumber','orderTitle','originPortId','originPortCnName','originPortEnName',
				'destinationPortId','destinationPortCnName','destinationPortEnName','containerType',
				'readyDate','serviceProviderId', 'containerQty','etd','eta'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 180, hidden:true},
                { header: _lang.TText.fOrderNumber, dataIndex: 'orderNumber', width: 90},
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200,  },
                { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.getValueRow('origin', value);
                        if (curUserInfo.lang == 'zh_CN') { return $destPort.cnName; } else { return $destPort.enName; }
                    }
                },
                { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $destPort = _dict.getValueRow('destination', value);
                        if(curUserInfo.lang == 'zh_CN'){ return $destPort.cnName; }else{ return $destPort.enName; }
                    }
                },
                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if($containerType.length>0 && $containerType[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $containerType[0].data.options);
                        }
                    }
				},
                { header: _lang.FlowOrderShippingPlan.fContainerQty, dataIndex: 'containerQty', width: 80},
                { header: _lang.FlowOrderShippingPlan.fReadyDate, dataIndex: 'readyDate', width: 140},
                { header: _lang.FlowOrderShippingPlan.fEtd, dataIndex: 'etd', width: 140},
                { header: _lang.FlowOrderShippingPlan.fEta, dataIndex: 'eta', width: 140},
                { header: _lang.ServiceProviderDocument.fServiceProviderId, dataIndex: 'serviceProviderId', width: 260},
            ],// end of columns
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

		var productList = Ext.getCmp(conf.mainTabPanelId + '-2');
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(json.data.details.length>0){
                    for(index in json.data.details){
                        var order = {};
                        Ext.applyIf(order, json.data.details[index]);
                        Ext.apply(order, json.data.details[index].order);
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
		App.clickTopTab('FlowOrderShippingConfirmationForm', conf);
	}
});