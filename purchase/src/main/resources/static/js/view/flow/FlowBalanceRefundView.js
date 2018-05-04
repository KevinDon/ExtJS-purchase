FlowBalanceRefundView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);

        var conf = {
			title : _lang.FlowBalanceRefund.mTitle,
			moduleName: 'FlowBalanceRefund',
			winId : 'FlowBalanceRefundForm',
			frameId : 'FlowBalanceRefundView',
			mainGridPanelId : 'FlowBalanceRefundGridPanelID',
			mainFormPanelId : 'FlowBalanceRefundFormPanelID',
			processFormPanelId: 'FlowBalanceRefundProcessFormPanelID',
			searchFormPanelId: 'FlowBalanceRefundSearchFormPanelID',
			mainTabPanelId: 'FlowBalanceRefundMainTabsPanelID',
			subProductGridPanelId : 'FlowBalanceRefundSubGridPanelID',
			formGridPanelId : 'FlowBalanceRefundFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/balanceRefund/list',
			urlSave: __ctxPath + 'flow/finance/balanceRefund/save',
			urlDelete: __ctxPath + 'flow/finance/balanceRefund/delete',
			urlGet: __ctxPath + 'flow/finance/balanceRefund/get',
			urlExport: __ctxPath + 'flow/finance/balanceRefund/export',
			urlListorder: __ctxPath + 'flow/finance/balanceRefund/listorder',
			urlFlow: __ctxPath + 'flow/finance/balanceRefund/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowBalanceRefund&processInstanceId=',
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
		FlowBalanceRefundView.superclass.constructor.call(this, {
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
			 	{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowBalanceRefund.fOrderNumber},
			 	{field:'Q-type-N-EQ', xtype:'dictcombo', value:'', title:_lang.FlowBalanceRefund.fRefundType, code:'purchase', codeSub:'refund_type', addAll:true},
			 	 {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			 	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
			    {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowBalanceRefund.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
            fields: [
                'id', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName','hold',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                'assigneeId','assigneeCnName','assigneeEnName',
            ],
            columns: [
                {header: _lang.TText.fId, dataIndex: 'id', width: 180},
                {header: _lang.FlowBalanceRefund.fOrderId, dataIndex: 'orderId', width: 180, hidden:true },
                {header: _lang.FlowBalanceRefund.fOrderNumber, dataIndex: 'orderNumber', width: 150},
//                {header: _lang.FlowBalanceRefund.fOrderName, dataIndex: 'orderName', width: 120,hidden:true},
                {header: _lang.FlowBalanceRefund.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true},
                {header: _lang.VendorDocument.fVendorAndService, dataIndex: 'vendorCnName', width: 260,hidden: curUserInfo.lang == 'zh_CN' ? false: true},
                {header: _lang.VendorDocument.fVendorAndService, dataIndex: 'vendorEnName', width: 260,hidden: curUserInfo.lang != 'zh_CN' ? false: true},
                //{header: _lang.FlowBalanceRefund.fChargebackReason, dataIndex: 'chargebackReason', width: 200},
                {header: _lang.FlowBalanceRefund.fRefundType, dataIndex: 'type', width: 70,
                    renderer: function (value) {
                        var $refundType = _dict.refundType;
                        if ($refundType.length > 0 && $refundType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $refundType[0].data.options);
                        }
                    }
				},
                {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'chargebackStatus', width: 70,
                    renderer : function(value){
                        value = value || '2';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                {
                    header: _lang.FlowBalanceRefund.fTotalFeePrice,
                    columns: new $groupPriceColumns(this, 'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', null, {edit: false})
                },

                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow({}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.productsGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-2',
            title: _lang.FlowBalanceRefund.tabRefundProductList,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','sku','productId','currency',
                'expectedQty', 'receivedQty', 'diffQty', 'priceAud', 'priceRmb', 'priceUsd', 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd',
                'rateAudToUsd', 'rateAudToRmb','diffAud','diffRmb','diffUsd','chargebackStatus','remark'
            ],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                {header: _lang.FlowOrderReceivingNotice.fSku, dataIndex: 'sku', width: 200},
                {header: _lang.FlowOrderReceivingNotice.fProductId, dataIndex: 'productId', width: 200, hidden : true, },

                {header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 65,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //报价
                { header: _lang.FlowBalanceRefund.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                        {edit: true, gridId: conf.formGridPanelId})
                },
                {header: _lang.FlowBalanceRefund.fDiffQty, dataIndex: 'diffQty', width: 80,},
                { header: _lang.FlowBalanceRefund.fDiff,
                    columns: new $groupPriceColumns(this, 'diffAud','diffRmb','diffUsd', null,
                        { gridId: conf.subFormGridPanelId + '-sample'})
                },
                {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'chargebackStatus', width: 70,
                    renderer : function(value){
                        value = value || '2';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
            ]// end of columns
    	});

        this.projectGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-3',
            title: _lang.FlowFeeRegistration.tabChargeItem,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','businessId', 'payProject', 'priceAud', 'priceRmb', 'priceUsd', 'currency',
                'preditQty','confirmQty','diffQty', 'rateAudToUsd', 'rateAudToRmb', 'remark','diffAud','diffRmb','diffUsd',
                'receivedPriceAud','receivedPriceRmb','receivedPriceUsd'
            ],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 185, hidden: true, },
                {header: _lang.FlowFeeRegistration.fProject, dataIndex: 'payProject', width: 200,},
                {header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 65,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                //应付价格
                { header: _lang.FlowBalanceRefund.fExpectedPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd',null,{gridId: conf.subFormGridPanelId + '-project',})
                },

                //实付价格
                { header: _lang.FlowBalanceRefund.fReceivedPrice,
                    columns: new $groupPriceColumns(this, 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd', null,{gridId: conf.subFormGridPanelId + '-project',})
                },

                //发运数量
                {header: _lang.FlowBalanceRefund.fPreditQty, dataIndex: 'preditQty', width: 60, scope:this, value:'1' , hidden:true,  },
                //发运数量
                {header: _lang.FlowBalanceRefund.fConfirmQty, dataIndex: 'confirmQty', width: 60, scope:this, value:'1', hidden:true, },
                //发运数量
                {header: _lang.FlowBalanceRefund.fDiffQty, dataIndex: 'diffQty', width: 60, scope:this, value:'1', hidden:true, },

                { header: _lang.FlowBalanceRefund.fDiff,
                    columns: new $groupPriceColumns(this, 'diffAud','diffRmb','diffUsd', null,
                        {edit:false, gridId: conf.subFormGridPanelId + '-project'}
                    )
                },

                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
            ]
        });

        this.chargebackReasonFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainTabPanelId + '-4',
            scope: this,
            title:  _lang.FlowBalanceRefund.fChargebackReason,
            autoScroll: true,
            fieldItems : [
                //收款账号信息
                {xtype: 'container', cls: 'row',
                    items: [
                        {field: 'main.chargebackReason',xtype: 'displayfield',readOnly: true},
                    ],
                }
            ]
        });


        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productsGridPanel);
        viewTabs.add(this.projectGridPanel);
        viewTabs.add(this.chargebackReasonFormPanel);

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

		var productList = Ext.getCmp(conf.mainTabPanelId+'-2');
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                Ext.getCmp(conf.mainTabPanelId+'-3').getStore().removeAll();
	            if(json.data.details.length>0){
                    if(json.data.type == 1) {
                        //供应商
                        for (var index in json.data.details) {
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            productList.getStore().add(product);
                        }
                    }else if(json.data.type == 2){
                        //服务商
                        var items = {};
                        for (var index in json.data.details) {
                            Ext.applyIf(items, json.data.details[index]);
                            Ext.getCmp(conf.mainTabPanelId+'-3').getStore().add(items);
                        }
                    }else if(json.data.type == 3){
                        //样品支付
                        for (var index in json.data.details) {
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            productList.getStore().add(product);
                        }
                    }
	            }

                var chargebackReason = Ext.getCmp(conf.mainTabPanelId + '-4');
                chargebackReason.getForm().reset();
                if(!!json.data && !!json.data.chargebackReason){
                    chargebackReason.getCmpByName('main.chargebackReason').setValue(json.data.chargebackReason || '');
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
		App.clickTopTab('FlowBalanceRefundForm', conf);
	}
});