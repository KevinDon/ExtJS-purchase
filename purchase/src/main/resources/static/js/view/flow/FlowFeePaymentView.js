FlowFeePaymentView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowFeePayment.mTitle,
			moduleName: 'FlowFeePayment',
			winId : 'FlowFeePaymentForm',
			frameId : 'FlowFeePaymentView',
			mainGridPanelId : 'FlowFeePaymentGridPanelID',
			mainFormPanelId : 'FlowFeePaymentFormPanelID',
			processFormPanelId: 'FlowFeePaymentProcessFormPanelID',
			searchFormPanelId: 'FlowFeePaymentSearchFormPanelID',
			mainTabPanelId: 'FlowFeePaymentMainTabsPanelID',
			subProductGridPanelId : 'FlowFeePaymentSubGridPanelID',
			formGridPanelId : 'FlowFeePaymentFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/feePayment/list',
			urlSave: __ctxPath + 'flow/finance/feePayment/save',
			urlDelete: __ctxPath + 'flow/finance/feePayment/delete',
			urlGet: __ctxPath + 'flow/finance/feePayment/get',
			urlExport: __ctxPath + 'flow/finance/feePayment/export',
			urlListProduct: __ctxPath + 'flow/finance/feePayment/listproduct',
			urlFlow: __ctxPath + 'flow/finance/feePayment/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowFeePayment&processInstanceId=',
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
		FlowFeePaymentView.superclass.constructor.call(this, {
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
			 	{field:'Q-feeRegisterId-S-LK', xtype:'textfield', title:_lang.FlowFeePayment.fFeeRegisterId},
                {field:'Q-feeType-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'service_provider', codeSub:'fee_type', addAll:true},
                {field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.TText.fOrderNumber},
                {field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowDepositContract.fOrderTitle},
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
			title: _lang.FlowFeePayment.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
            fields: [
                'id', 'feeRegisterId','receivedTotalPriceAud','receivedTotalPriceRmb','receivedTotalPriceUsd','rateAudToRmb','rateAudToUsd','hold','feeRegisterBusinessId',
                'creatorId', 'creatorCnName', 'creatorEnName', 'assigneeId','assigneeCnName','assigneeEnName','departmentId', 'departmentCnName', 'departmentEnName',
				'currency', 'paymentTotalPriceAud','paymentTotalPriceRmb','paymentTotalPriceUsd', 'feeType',
                'totalPriceAud','totalPriceRmb','totalPriceUsd','paymentRateAudToRmb', 'paymentRateAudToUsd',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','orderNumber','orderTitle','vendorId','vendorCnName','vendorEnName'
            ],
            columns: [
                {header: _lang.TText.fId, dataIndex: 'id', width: 180, },
                {header: _lang.FlowFeePayment.fFeeRegisterId, dataIndex: 'feeRegisterId', width: 200 ,hidden:true,},
                {header: _lang.FlowFeePayment.fFeeRegisterId, dataIndex: 'feeRegisterBusinessId', width: 200},
                {header: _lang.FlowFeeRegistration.fFeeType, dataIndex: 'feeType', width: 80,
                    renderer: function (value) {
                        var $feeType = _dict.feeType;
                        if ($feeType.length > 0 && $feeType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $feeType[0].data.options);
                        }
                    }
                },
                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},
                {header: _lang.FlowDepositContract.fOrderTitle, dataIndex: 'orderTitle', width: 120},
                {header: _lang.FlowDepositContract.fVendorId, dataIndex: 'vendorId', width: 180, hidden: true},
                {header: _lang.FlowDepositContract.fVendorCnName, dataIndex: 'vendorCnName', width: 120},
                {header: _lang.FlowDepositContract.fVendorEnName, dataIndex: 'vendorEnName', width: 120},
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },

                { header: _lang.FlowFeePayment.fPayable,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                        },
                        {header: _lang.FlowFeePayment.fTotalPrice,
                            columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null,
                                {edit:false, gridId: conf.mainGridPanelId })
                        },
                    ]
                },
                { header: _lang.FlowFeePayment.fPayment,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'paymentRateAudToRmb', 'paymentRateAudToUsd')
                        },
                        { header: _lang.FlowFeePayment.fTotalPrice,
                            columns: new $groupPriceColumns(this, 'paymentTotalPriceAud','paymentTotalPriceRmb','paymentTotalPriceUsd', null,
                                {edit:false, gridId: conf.mainGridPanelId})
                        },


                    ]
                },



            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow({}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });



        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.attachmentsGridPanel);

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

		var attaList =      Ext.getCmp(conf.mainTabPanelId).items.items[2];
        attaList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                //附件
                attaList.setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
		App.clickTopTab('FlowFeePaymentForm', conf);
	}
});