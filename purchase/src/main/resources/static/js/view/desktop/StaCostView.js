StaCostView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.Sta.mCostTitle,
            frameId : 'StaCostView',
            moduleName:'StaCost',
            mainGridPanelId : 'StaCostGridPanelID',
            searchFormPanelId: 'StaCostSubFormPanelID',
            urlList: __ctxPath + 'desktop/stacost/list',
            refresh: true,
        };

        this.initUIComponents(conf);
        StaCostView.superclass.constructor.call(this, {
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
			    
            ]
        });// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Sta.tabCostTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','orderId','productType','sku', 'orderIndex','orderQty', 'vendorCnName','vendorEnName','origin','containerType','containerQty','currency',
                'rateAudToRmb', 'rateAudToUsd','portFeeAud','portFeeRmb','portFeeUsd', 'chargeItemFeeUsd','chargeItemFeeRmb','chargeItemFeeUsd',
                'tariffAud','tariffRmb','tariffUsd','customProcessingFeeAud','customProcessingFeeRmb','customProcessingFeeUsd',
                'otherFeeAud','otherFeeRmb','otherFeeUsd', 'totalCostTaxAud','totalCostTaxRmb','totalCostTaxUsd',  'totalCostAud','totalCostRmb','totalCostUsd',
                'gstAud','gstRmb','gstUsd','totalOrderCostAud','totalOrderCostRmb','totalOrderCostUsd','addedCostAud','addedCostRmb','addedCostUsd',
                'electronicProcessingFeeUsd','electronicProcessingFeeRmb','electronicProcessingFeeUsd',
                'cbm','totalItemCbm','totalCbm',
				'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName',
				'departmentEnName','departmentId','updatedAt',
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,hidden: true},
				{ header: _lang.Sta.fSku, dataIndex: 'productId', width: 180 ,hidden: true},
				{ header: _lang.Sta.fOrderId, dataIndex: 'orderId', width: 90  },
				{ header: _lang.FlowPurchasePlan.fOrderQty, dataIndex: 'orderQty', width: 90  },
				// { header: _lang.Sta.fProductType, dataIndex: 'productType', width: 90  },
				// { header: _lang.Sta.fOrderIndex, dataIndex: 'orderIndex', width: 90  },
				{ header: _lang.Sta.fSku, dataIndex: 'sku', width: 90  },
				// { header: _lang.Sta.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				// { header: _lang.Sta.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency =  _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },

                { header: _lang.Sta.fSeaFreight,
                    columns: new $groupPriceColumns(this, 'portFeeAud','portFeeRmb','portFeeUsd', null, {edit:false})
                },
                { header: _lang.Sta.fDomesticFreight,
                    columns: new $groupPriceColumns(this, 'chargeItemFeeUsd','chargeItemFeeRmb','chargeItemFeeUsd', null, {edit:false})
                },

                { header: _lang.Sta.fDuty,
                    columns: new $groupPriceColumns(this, 'tariffAud','tariffRmb','tariffUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fDuty,
                    columns: new $groupPriceColumns(this, 'customProcessingFeeAud','customProcessingFeeRmb','customProcessingFeeUsd', null, {edit:false})
                },
				{ header: _lang.FlowServiceInquiry.fPriceOther,
                    columns: new $groupPriceColumns(this, 'otherFeeAud','otherFeeRmb','otherFeeUsd', null, {edit:false})
                },
                { header: _lang.Sta.fGst,
                    columns: new $groupPriceColumns(this, 'gstAud','gstRmb','gstUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fFinalCost,
                    columns: new $groupPriceColumns(this, 'totalCostAud','totalCostRmb','totalCostUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fFinalCostGst,
                    columns: new $groupPriceColumns(this, 'totalCostTaxAud','totalCostTaxRmb','totalCostTaxUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fFinalCostGst,
                    columns: new $groupPriceColumns(this, 'totalOrderCostAud','totalOrderCostRmb','totalOrderCostUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fFinalCostGst,
                    columns: new $groupPriceColumns(this, 'addedCostAud','addedCostRmb','addedCostUsd', null, {edit:false})
                },
				{ header: _lang.Sta.fFinalCostGst,
                    columns: new $groupPriceColumns(this, 'electronicProcessingFeeUsd','electronicProcessingFeeRmb','electronicProcessingFeeUsd', null, {edit:false})
                },

                { header: _lang.Sta.fTotalItemCbm, dataIndex: 'totalItemCbm', width: 90  },
                { header: _lang.Sta.fTotalCbm, dataIndex: 'totalCbm', width: 90  },
                { header: _lang.Sta.fCbm, dataIndex: 'cbm', width: 90  },

            ],// end of column
    	    //appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
			// itemclick: function(obj, record, item, index, e, eOpts){
			// 	this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			// }
		});

    },// end of the init

});