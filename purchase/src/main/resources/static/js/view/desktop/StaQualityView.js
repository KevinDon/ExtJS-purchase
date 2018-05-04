StaQualityView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.Sta.mQualityTitle,
            frameId : 'StaQualityView',
            moduleName:'StaQuality',
            mainGridPanelId : 'StaQualityGridPanelID',
            searchFormPanelId: 'StaQualitySubFormPanelID',
            urlList: __ctxPath + 'desktop/staquality/list',
            refresh: true,
        };

        this.initUIComponents(conf);
        StaQualityView.superclass.constructor.call(this, {
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
			    { field: 'Q-reportTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Reports.fReportTime, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Sta.tabQualityTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','departmentId','departmentCnName','departmentCnName','buyerCnName','buyerEnName','orderId','orderNumber','orderTitle',
				'sku','reportTime','result','file', 'vendorCnName','vendorEnName',
				'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName',
				'departmentEnName','departmentId','updatedAt',
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,hidden: true},
				{ header: _lang.Sta.fOrderId, dataIndex: 'departmentId', width: 90  },
				{ header: _lang.TText.fDepartmentCnName, dataIndex: 'departmentCnName', width: 90, hidden:curUserInfo.lang =='zh_CN'? false: true   },
				{ header: _lang.TText.fDepartmentEnName, dataIndex: 'departmentCnName', width: 90 ,hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Sta.fBuyerCnName, dataIndex: 'buyerCnName', width: 90 , hidden:curUserInfo.lang =='zh_CN'? false: true},
				{ header: _lang.Sta.fBuyerCnName, dataIndex: 'buyerEnName', width: 90 ,hidden:curUserInfo.lang !='zh_CN'? false: true  },
				{ header: _lang.Sta.fOrderId, dataIndex: 'orderId', width: 90  },
				{ header: _lang.TText.fOrderNumber, dataIndex: 'orderNumber', width: 90  },
				{ header: _lang.TText.fOrderTitle, dataIndex: 'orderTitle', width: 90  },
				{ header: _lang.Sta.fVendorCnName, dataIndex: 'vendorCnName', width: 260 ,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Sta.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Sta.fSku, dataIndex: 'sku', width: 90  },
				{ header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 90  },
				//{ header: _lang.Reports.fResult, dataIndex: 'result', width: 90  },
                { header: _lang.Reports.fResult,
                    columns: [
                        { header: _lang.Sta.fIssueA, dataIndex: 'issueA', width: 90  },
                        { header: _lang.Sta.fIssueB, dataIndex: 'issueB', width: 90  },
                        { header: _lang.Sta.fIssueC, dataIndex: 'issueC', width: 90  },
                        { header: _lang.Sta.fIssueD, dataIndex: 'issueD', width: 90  },
                        { header: _lang.Sta.fIssueE, dataIndex: 'issueE', width: 90  },
                        { header: _lang.Sta.fIssueF, dataIndex: 'issueF', width: 90  },
                        { header: _lang.Sta.fIssueG, dataIndex: 'issueG', width: 90  },
					]
                },
				{ header: _lang.Reports.fFile, dataIndex: 'file', width: 90  },

            ],// end of column
    		//appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
            // itemclick: function(obj, record, item, index, e, eOpts){
				// this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            // }
		});

    },// end of the init

});