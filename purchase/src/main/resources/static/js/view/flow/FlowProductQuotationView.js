FlowProductQuotationView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
			title : _lang.FlowProductQuotation.mTitle,
			moduleName: 'FlowProductQuotation',
			winId : 'FlowProductQuotationForm',
			frameId : 'FlowProductQuotationView',
			mainGridPanelId : 'FlowProductQuotationViewGridPanelID',
			mainFormPanelId : 'FlowProductQuotationViewFormPanelID',
			processFormPanelId: 'FlowProductQuotationProcessFormPanelID',
			searchFormPanelId: 'FlowProductQuotationViewSearchFormPanelID',
			mainTabPanelId: 'FlowProductQuotationViewMainTbsPanelID',
			ProductGridPanelId : 'FlowProductQuotationViewProductGridPanelId',
			formGridPanelId : 'FlowProductQuotationViewFormGridPanelID',
            urlList: __ctxPath + 'flow/purchase/quotation/list',
            urlSave: __ctxPath + 'flow/purchase/quotation/save',
            urlDelete: __ctxPath + 'flow/purchase/quotation/delete',
            urlGet: __ctxPath + 'flow/purchase/quotation/get',
            urlExport: __ctxPath + 'flow/purchase/quotation/export',
            urlListProduct: __ctxPath + 'flow/purchase/quotation/listproduct',
            urlFlow: __ctxPath + 'flow/purchase/quotation/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowProductQuotation&processInstanceId=',
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
		FlowProductQuotationView.superclass.constructor.call(this, {
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
				{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled] , ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			    {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
			    {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
			    { field: 'Q-effectiveDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowProductQuotation.fEffectiveDate, format: curUserInfo.dateFormat},
			    { field: 'Q-validUntil-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowProductQuotation.fValidUntil, format: curUserInfo.dateFormat},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.FlowProductQuotation.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id', 'vendorId','vendorCnName','vendorEnName','currency', 'totalPriceAud','totalPriceRmb','effectiveDate',
				'validUntil','totalPriceUsd','rateAudToRmb', 'rateAudToUsd','creatorId', 'creatorName','endTime',
				'departmentId','departmentName', 'startTime', 'createdAt','flowStatus','processInstanceId','hold',
				 'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'assigneeId','assigneeCnName','assigneeEnName','departmentId',
                'departmentCnName','departmentEnName','updatedAt'
            ],
            columns: [
                { header: _lang.FlowNewProduct.fId, dataIndex: 'id',width: 180, },
         		{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency',width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                { header: _lang.FlowProductQuotation.fEffectiveDate, dataIndex: 'effectiveDate' ,width: 140},
                { header: _lang.FlowProductQuotation.fValidUntil,  readOnly:true, dataIndex: 'validUntil',width: 140, },
            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.productGridPanel = new HP.GridPanel({
            id: conf.ProductGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            // url: conf.urlListProduct,
            fields: [
				'id','sku','name','priceAud','priceRmb','priceUsd', 'moq', 'rateAudToRmb','rateAudToUsd','currency',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId','destinationPortCnName','destinationPortEnName',
				'netWeight','masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonStructure','masterCartonWeight',
                'prevPriceUsd','prevPriceAud','prevPriceRmb','prevRateAudToRmb','prevRateAudToUsd',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton',
                'length','width','height','netWeight','cubicWeight','cbm'

            ],
            columns: [
                { header: 'ID', dataIndex: 'productId', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200 , locked: true},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency',width:40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.FlowProductQuotation.fNewExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                { header: _lang.FlowProductQuotation.fOldExchangeRate,
                    columns: [
                        { header: _lang.TText.fRateAudToRmb, dataIndex: 'prevRateAudToRmb', width: 80,   },
                        { header: _lang.TText.fRateAudToUsd, dataIndex: 'prevRateAudToUsd', width: 80,   },
                    ]
                },
                //报价
                { header: _lang.FlowProductQuotation.fNewPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('orderValueAud', (row.get('priceAud') * row.get('moq')).toFixed(3));
                        row.set('orderValueRmb', (row.get('priceRmb') * row.get('moq')).toFixed(3));
                        row.set('orderValueUsd', (row.get('priceUsd') * row.get('moq')).toFixed(3));
                    },{gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowProductQuotation.fOldPrice,
                    columns: [
                        { header: _lang.TText.fAUD, dataIndex: 'prevPriceAud', width: 80,   },
                        { header: _lang.TText.fRMB, dataIndex: 'prevPriceRmb', width: 80,   },
                        { header: _lang.TText.fUSD, dataIndex: 'prevPriceUsd', width: 80,   }
                    ]
                },

                //采购数量
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'moq', scope:this, width: 80,
                    editor: {xtype: 'numberfield', minValue: 0, listeners:{
                        change:function(pt, newValue, oldValue, eOpts ){
                            var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                            row.set('orderValueAud', (row.get('priceAud') * newValue).toFixed(3));
                            row.set('orderValueRmb', (row.get('priceRmb') * newValue).toFixed(3));
                            row.set('orderValueUsd', (row.get('priceUsd') * newValue).toFixed(3));
                        }
                    }
                    }},


                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight',width:60, },
                {header: _lang.ProductDocument.tabProductSize,
                    columns: [
                        {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 80,},
                        {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 80,},
                        {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 80,},
                        { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight'  },
                        { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight'  },
                        { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabInnerCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonL', width: 80,},
                        { header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonW' },
                        { header: _lang.ProductDocument.fInnerCartonH, dataIndex: 'innerCartonH' },
                        { header: _lang.ProductDocument.fInnerCartonCbm, dataIndex: 'innerCartonCbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabMasterCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fMasterCartonL, dataIndex: 'masterCartonL', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonW, dataIndex: 'masterCartonW', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonH, dataIndex: 'masterCartonH', width: 80,},
                        { header: _lang.ProductDocument.fMasterCartonCbm, dataIndex: 'masterCartonCbm'  },
                    ],
                },

            ]// end of columns
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
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
        var productList = Ext.getCmp(conf.ProductGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
                        productList.getStore().add(product);
                    }
                }

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
		App.clickTopTab('FlowProductQuotationForm', conf);
	}
});