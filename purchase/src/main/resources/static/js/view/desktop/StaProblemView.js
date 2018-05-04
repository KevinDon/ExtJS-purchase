StaProblemView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.Sta.mProblemTitle,
            frameId : 'StaProblemView',
            moduleName:'StaProblem',
            mainGridPanelId : 'StaProblemGridPanelID',
            searchFormPanelId: 'StaProblemSubFormPanelID',
            urlList: __ctxPath + 'desktop/staproblem/list',
            refresh: true,
        };

        this.initUIComponents(conf);
        StaProblemView.superclass.constructor.call(this, {
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
			    { field: 'Q-orderAt-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fOrderDate, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Sta.tabProblemTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','ticketNo','categoryCnName','categoryEnName','departmentCnName','departmentEnName','vendorCnName','vendorEnName','sku','orderAt','orderSent',
				'sellChannel','refundValue','refundAt','categoryId','subCategoryId','title','description','currency','approverCnName','approverEnName','codeMain','codeSub',
				'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName','refundAmountUsd','refundAmountRmb','refundAmountUsd',
				'departmentEnName','departmentId','updatedAt',
			],
            groupField:'sellChannel',
            features: [{ftype:'grouping'}],
            width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,hidden: true},
				{ header: _lang.Sta.fTicketNo, dataIndex: 'ticketNo', width: 90  },
				{ header: _lang.Sta.fCategory, dataIndex: 'categoryCnName', width: 90 ,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Sta.fCategory, dataIndex: 'categoryEnName', width: 90  ,hidden:curUserInfo.lang !='zh_CN'? false: true},
				{ header: _lang.Sta.fTeam, dataIndex: 'departmentCnName', width: 90  ,hidden:curUserInfo.lang =='zh_CN'? false: true},
				{ header: _lang.Sta.fTeam, dataIndex: 'departmentEnName', width: 90  ,hidden:curUserInfo.lang !='zh_CN'? false: true},
				{ header: _lang.Sta.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Sta.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Sta.fSku, dataIndex: 'sku', width: 90  },
				{ header: _lang.Sta.fOrderDate, dataIndex: 'orderAt', width: 140  },
				{ header: _lang.Sta.fOrderSent, dataIndex: 'orderSent', width: 90  },
                { header: _lang.Sta.fOrderType, dataIndex: 'sellChannel', width: 90  },
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
                { header: _lang.Sta.fRefundValue,
                    columns: new $groupPriceColumns(this, 'refundAmountUsd','refundAmountRmb','refundAmountUsd', null, {edit:false})
                },

				{ header: _lang.Sta.fRefundDate, dataIndex: 'refundAt', width: 90  },
                { header: _lang.Reports.fConfirmed, dataIndex: 'approverCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.Reports.fConfirmed, dataIndex: 'approverEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },

                { header: _lang.Sta.fCategoryId, dataIndex: 'codeMain', width: 90  },
				{ header: _lang.Sta.fSubCategoryId, dataIndex: 'codeSub', width: 90  },
				{ header: _lang.Sta.fTitle, dataIndex: 'title', width: 90  },
                { header: _lang.Sta.fDescription, dataIndex: 'fDescription', width: 90  },
				
            ],// end of column
    		//appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
            // itemclick: function(obj, record, item, index, e, eOpts){
				// this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            // }
		});

    },// end of the init

});