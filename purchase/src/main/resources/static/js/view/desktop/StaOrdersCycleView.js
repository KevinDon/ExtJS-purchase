StaOrdersCycleView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.Sta.mOrdersCycleTitle,
            frameId : 'StaOrdersCycleView',
            moduleName:'StaOrdersCycle',
            mainGridPanelId : 'StaOrdersCycleGridPanelID',
            searchFormPanelId: 'StaOrdersCycleSubFormPanelID',
            urlList: __ctxPath + 'desktop/staorderscycle/list',
            urlExport: __ctxPath + 'desktop/staorderscycle/export',
            refresh: true,
            export:true
        };

        this.initUIComponents(conf);
        StaOrdersCycleView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.gridPanel ]
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
				{field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fOrderNumber},
				{field:'Q-asnNumber-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fAsnNumber},
				{field:'Q-receiveLocation-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fReceiveLocation},
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
                { field: 'Q-orderConfirmedDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fOrderConfirmedDate, format: curUserInfo.dateFormat},
                { field: 'Q-depositDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fDepositDate, format: curUserInfo.dateFormat},
                { field: 'Q-originalReadyDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fOriginalReadyDate, format: curUserInfo.dateFormat},
                { field: 'Q-adjustReadyDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fAdjustReadyDate, format: curUserInfo.dateFormat},
                { field: 'Q-originalEtd-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fEtd, format: curUserInfo.dateFormat},
                //{ field: 'Q-justEta-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fEta, format: curUserInfo.dateFormat},
                { field: 'Q-agentNotificationDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fAgentNotificationDate, format: curUserInfo.dateFormat},
                { field: 'Q-balanceDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fBalanceDate, format: curUserInfo.dateFormat},
                { field: 'Q-qcTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fQcTime, format: curUserInfo.dateFormat},
                { field: 'Q-shippingDocReceivedDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fShippingDocReceived, format: curUserInfo.dateFormat},
                { field: 'Q-shippingDocForwarded-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fShippingDocForwarded, format: curUserInfo.dateFormat},
                { field: 'Q-containerArrivingTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fContainerArrivingTime, format: curUserInfo.dateFormat},
                { field: 'Q-putAwayTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fPutAwayTime, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Sta.tabOrdersCycleTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','orderNumber','orderTitle','orderIndex','vendorId','vendorCnName','vendorEnName','orderConfirmedDate', 'depositDate','originalReadyDate',
				'adjustReadyDate','originalEtd','justEta','agentNotificationDate','balanceDate','qcTime','shippingDocReceivedDate','shippingDocForwardedDate',
				'containerArrivingTime','putAwayTime','orderConfirmedCycle','leadTime','containerPortCycle','customClearCycle','containerArrivingCycle','unloadingCycle',
				'orderCycle','createdTime','eta','status',
				'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName',
				'departmentEnName','departmentId','updatedAt','createdTime'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,hidden: true},
				{ header: _lang.Sta.fOrderId, dataIndex: 'orderNumber', width: 90  },
				{ header: _lang.Sta.fOrderTitle, dataIndex: 'orderTitle', width: 180  },

				{ header: _lang.Sta.fVendorId, dataIndex: 'vendorId', width: 90,hidden: true  },
				{ header: _lang.Sta.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Sta.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.Sta.fCreatedTime, dataIndex: 'createdTime', width: 140  },
				{ header: _lang.Sta.fOrderConfirmedDate, dataIndex: 'orderConfirmedDate', width: 140  },
                { header: _lang.Sta.fOriginalReadyDate, dataIndex: 'originalReadyDate', width: 140  },
                { header: _lang.Sta.fAdjustReadyDate, dataIndex: 'adjustReadyDate', width: 140  },
				{ header: _lang.Sta.fDepositDate, dataIndex: 'depositDate', width: 140  },

                { header: _lang.Sta.fEtd, dataIndex: 'originalEtd', width: 140  },
                //{ header: _lang.Sta.fEta, dataIndex: 'justEta', width: 140  },
                { header: _lang.Sta.fAgentNotificationDate, dataIndex: 'agentNotificationDate', width: 140  },
                { header: _lang.Sta.fBalanceDate, dataIndex: 'balanceDate', width: 140  },
                { header: _lang.Sta.fQcTime, dataIndex: 'qcTime', width: 140  },
                { header: _lang.Sta.fShippingDocReceived, dataIndex: 'shippingDocReceivedDate', width: 140  },
                { header: _lang.Sta.fShippingDocForwarded, dataIndex: 'shippingDocForwardedDate', width: 140  },
                { header: _lang.Sta.fEta, dataIndex: 'eta', width: 140  },
                { header: _lang.Sta.fContainerArrivingTime, dataIndex: 'containerArrivingTime', width: 140  },
                { header: _lang.Sta.fPutAwayTime, dataIndex: 'putAwayTime', width: 140  },
                { header: _lang.Sta.fOrderConfirmedCycle, dataIndex: 'orderConfirmedCycle', width: 70  },
                { header: _lang.Sta.fLeadTime, dataIndex: 'leadTime', width: 70  },
                { header: _lang.Sta.fContainerPortCycle, dataIndex: 'containerPortCycle', width: 70  },
                { header: _lang.Sta.fCustomClearCycle, dataIndex: 'customClearCycle', width: 70  },
                { header: _lang.Sta.fContainerArrivingCycle, dataIndex: 'containerArrivingCycle', width: 70  },
                { header: _lang.Sta.fUnloadingCycle, dataIndex: 'unloadingCycle', width: 70  },
                { header: _lang.Sta.fOrderCycle, dataIndex: 'orderCycle', width: 70  },
                { header: _lang.TText.fStatus, dataIndex: 'status', width: 50 ,hidden:true, },

            ],// end of column
    		//appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
		// 	itemclick: function(obj, record, item, index, e, eOpts){
		// 		this.scope.rowClick.call(this.scope, record, item, index, e, conf);
		// 	}
		 });

    },// end of the init

});