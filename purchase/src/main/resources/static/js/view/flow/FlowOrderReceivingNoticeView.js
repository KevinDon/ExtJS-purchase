FlowOrderReceivingNoticeView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowOrderReceivingNotice.mTitle,
			moduleName: 'FlowOrderReceivingNotice',
			winId : 'FlowOrderReceivingNoticeForm',
			frameId : 'FlowOrderReceivingNoticeView',
			mainGridPanelId : 'FlowOrderReceivingNoticeGridPanelID',
			mainFormPanelId : 'FlowOrderReceivingNoticeFormPanelID',
			processFormPanelId: 'FlowOrderReceivingNoticeProcessFormPanelID',
			searchFormPanelId: 'FlowOrderReceivingNoticeSearchFormPanelID',
			mainTabPanelId: 'FlowOrderReceivingNoticeMainTabsPanelID',
			subOrderGridPanelId : 'FlowOrderReceivingNoticePlanSubOrderGridPanelID',
			formGridPanelId : 'FlowOrderReceivingNoticeFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/asn/list',
			urlSave: __ctxPath + 'flow/shipping/asn/save',
			urlDelete: __ctxPath + 'flow/shipping/asn/delete',
			urlGet: __ctxPath + 'flow/shipping/asn/get',
			urlExport: __ctxPath + 'flow/shipping/asn/export',
			urlListProduct: __ctxPath + 'flow/shipping/asn/listproduct',
			urlFlow: __ctxPath + 'flow/shipping/asn/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowAsn&processInstanceId=',
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
		FlowOrderReceivingNoticeView.superclass.constructor.call(this, {
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
				{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fOrderNumber},
				{field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fOrderTitle},
				{field:'Q-asnNumber-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fAsnNumber},
				{field:'Q-receiveLocation-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fReceiveLocation},
				{field:'Q-containerNumber-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fContainerNumber,} ,
				{field:'Q-containerType-S-LK', xtype:'dictcombo', title:_lang.FlowCustomClearance.fContainerType, code:'service_provider', codeSub:'container_type',},
				{field:'Q-sealsNumber-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fSealsNumber, },
				{field:'Q-warehouseId-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fWarehouseId},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['0', _lang.TText.vDraft]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-receiveStartDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowOrderReceivingNotice.fReceiveStartDate, format: curUserInfo.dateFormat},
			    { field: 'Q-receiveEndDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowOrderReceivingNotice.fReceiveEndDate, format: curUserInfo.dateFormat},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		


		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowOrderReceivingNotice.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','orderId','orderNumber','orderTitle','receiveDate','asnNumber','accessories','receiveLocation','warehouseId',
				'receiveStartDate','receiveEndDate','remark','creatorId','creatorCnName','creatorEnName','assigneeId',
				'assigneeCnName','assigneeEnName', 'departmentId','departmentCnName','departmentEnName','flagCompleteStatus','flagCompleteTime','sealsNumber',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold','flagSyncStatus','flagSyncDate','containerType','containerNumber'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowOrderReceivingNotice.fId, dataIndex: 'id', width: 180 },
				{ header: _lang.FlowOrderReceivingNotice.fOrderId, dataIndex: 'orderId', width: 200, hidden:true },
				{ header: _lang.FlowOrderReceivingNotice.fOrderNumber, dataIndex: 'orderNumber', width: 90  },
				{ header: _lang.FlowOrderReceivingNotice.fOrderTitle, dataIndex: 'orderTitle', width: 200  },
				{ header: _lang.FlowOrderReceivingNotice.fReceiveDate, dataIndex: 'receiveDate', width: 140 ,hidden:true, },
				{ header: _lang.FlowOrderReceivingNotice.fAsnNumber, dataIndex: 'asnNumber', width: 140},
				{ header: _lang.FlowCustomClearance.fContainerNumber, dataIndex: 'containerNumber', width: 80},
				{ header: _lang.FlowCustomClearance.fContainerType, dataIndex: 'containerType', width: 70,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if($containerType.length>0 && $containerType[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $containerType[0].data.options);
                        }
                    }
				},
				{ header: _lang.FlowCustomClearance.fSealsNumber, dataIndex: 'sealsNumber', width: 70},
				{ header: _lang.FlowOrderReceivingNotice.fAccessories, dataIndex: 'accessories', width: 40, hidden: true, },
				{ header: _lang.FlowOrderReceivingNotice.fReceiveLocation, dataIndex: 'receiveLocation', width: 200, hidden:true,},
				{ header: _lang.FlowOrderReceivingNotice.fWarehouseId, dataIndex: 'warehouseId', width: 100},
				{ header: _lang.FlowOrderReceivingNotice.fReceiveStartDate, dataIndex: 'receiveStartDate', width: 140},
				{ header: _lang.FlowOrderReceivingNotice.fReceiveEndDate, dataIndex: 'receiveEndDate', width: 140},

                { header:  _lang.ProductDocument.fIsSync, dataIndex: 'flagSyncStatus', width: 60,
						renderer: function(value){
                            var $sync = _dict.sync;
                            if($sync.length>0 && $sync[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $sync[0].data.options);
                            }
						}
				},
                { header: _lang.ProductDocument.fIsSyncDate, dataIndex: 'flagSyncDate', width: 150},
                { header: _lang.FlowOrderReceivingNotice.fFlagCompleteStatus, dataIndex: 'flagCompleteStatus', width: 50,
                    renderer: function(value){
                        var $item = _dict.syncStatus;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowOrderReceivingNotice.fFlagCompleteTime, dataIndex: 'flagCompleteTime', width: 140},


            ],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

		this.productGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-product',
            title: _lang.FlowOrderReceivingNotice.tabNoticeDetail,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','productId', 'sku', 'barcode', 'factoryCode', 'color', 'size', 'style',
                'orderQty', 'cartons', 'expectedQty', 'expectedCartons', 'receivedQty', 'receivedCartons',
                'unitCbm', 'totalCbm', 'totalNw', 'totalGw'
            ],
            columns: [
                { header: 'ID', dataIndex: 'productId', width: 180, hidden: true },
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200, },
                { header: _lang.FlowOrderReceivingNotice.fExpected,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'expectedQty', width: 80, },
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'expectedCartons', width: 80, },
                    ]
                },
                { header: _lang.FlowOrderReceivingNotice.fReceived,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'receivedQty', width: 80},
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'receivedCartons', width: 80},
                    ]
                },
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'barcode', width: 200, },
                { header: _lang.FlowCustomClearance.fColor, dataIndex: 'color', width: 160, },
                { header: _lang.FlowCustomClearance.fSize, dataIndex: 'size', width: 160, },
                { header: _lang.FlowCustomClearance.fStyle, dataIndex: 'style', width: 160, },
                { header: _lang.FlowCustomClearance.fTotalCbm, dataIndex: 'totalCbm', width: 60, },
                { header: _lang.FlowCustomClearance.fTotalGw, dataIndex: 'totalGw', width: 60, },

            ]// end of columns
        });
        
        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);

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
		var productList = Ext.getCmp(conf.mainTabPanelId + '-product');
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                    if(!!json.data && !!json.data.details && json.data.details.length > 0){
                    if(json.data.details[0].asnPackingDetails.length>0){
                        var items = [];
                        for (var index in json.data.details[0].asnPackingDetails ) {
                            var product = {}
                            Ext.apply(product, json.data.details[0].asnPackingDetails[index]);
                            Ext.applyIf(product, json.data.details[0].asnPackingDetails[index].ccPackingDetail);
                            items.push(product)
                        }
                        productList.getStore().add(items);
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
		App.clickTopTab('FlowOrderReceivingNoticeForm', conf);
	}
});