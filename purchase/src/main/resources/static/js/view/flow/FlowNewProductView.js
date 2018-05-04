FlowNewProductView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowNewProduct.mTitle,
			moduleName: 'FlowNewProduct',
			winId : 'FlowNewProductViewForm',
			frameId : 'FlowNewProductView',
			mainGridPanelId : 'FlowNewProductViewGridPanelID',
			mainFormPanelId : 'FlowNewProductViewFormPanelID',
			processFormPanelId: 'FlowNewProductProcessFormPanelID',
			searchFormPanelId: 'FlowNewProductViewSearchFormPanelID',
			mainTabPanelId: 'FlowNewProductViewMainTabsPanelID',
			subProductGridPanelId : 'FlowNewProductViewSubGridPanelID',
			formGridPanelId : 'FlowNewProductFormGridPanelID',
			urlList: __ctxPath + 'flow/purchase/newproduct/list',
			urlSave: __ctxPath + 'flow/purchase/newproduct/save',
			urlDelete: __ctxPath + 'flow/purchase/newproduct/delete',
			urlGet: __ctxPath + 'flow/purchase/newproduct/get',
			urlExport: __ctxPath + 'flow/purchase/newproduct/export',
			urlListProduct: __ctxPath + 'flow/purchase/newproduct/listproduct',
			urlFlow: __ctxPath + 'flow/purchase/newproduct/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowNewProduct&processInstanceId=',
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
		FlowNewProductView.superclass.constructor.call(this, {
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
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
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
			title: _lang.FlowNewProduct.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'vendorId','vendorCnName','vendorEnName','currency', 'totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb',
				'rateAudToUsd','totalOrderQty','totalOrderValueAud','totalOrderValueRmb','totalOrderValueUsd','hold',
				'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
				'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','history'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 180  },
				{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency =  _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.NewProductDocument.fPrice,
					columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
				},
				{ header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'totalOrderQty', width: 80 },

                
			],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.productGridPanel = new HP.GridPanel({
            id: conf.subProductGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            bbar: false,
            // url: conf.urlListProduct,
            fields: [
				'id','sku','name','barcode','categoryName','newProduct',
				'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorCnName','vendorEnName',
				'productParameter', 'productDetail', 'productLink', 'keywords', 'orderQty',
				'competitorPrice', 'competitorSaleRecord','ebayMonthlySales','mandatory',
				'currency','priceAud','priceRmb','priceUsd','creatorName','departmentName',
				'rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
				'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd',
				'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd','competitorPriceAud',
				'productPredictProfitRmb','competitorPriceUsd','vendorName','competitorPriceRmb'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 ,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                { header: _lang.NewProductDocument.fPrice, 
                	columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false})
                },
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', width: 80 },

                //预计利润
                { header: _lang.NewProductDocument.fPredictProfit, 
                	columns: new $groupPriceColumns(this, 'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd', null, {edit:false})
                },
                //竞争对手报价
                { header: _lang.NewProductDocument.fCompetitorPrice, width: 100,
                	columns: new $groupPriceColumns(this, 'competitorPriceAud','competitorPriceRmb','competitorPriceUsd', null, {edit:false})
                },
                //对手销量
                { header: _lang.NewProductDocument.fCompetitorSaleRecord, dataIndex: 'competitorSaleRecord', width: 80},
                //ebay月销售额
                { header: _lang.NewProductDocument.fEbayMonthlySales,  width: 100, 
                	columns: new $groupPriceColumns(this, 'ebayMonthlySalesAud','ebayMonthlySalesRmb','ebayMonthlySalesUsd', null, {edit:false})
                },
                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                    renderer: function(value){
                    	if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                    renderer: function(value){
                    	if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                    	if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 80},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                    	if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                /*{ header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorName', width: 60 },*/
        		{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },
                { header: _lang.NewProductDocument.fCreator, dataIndex: 'creatorName', width: 100 },
                { header: _lang.NewProductDocument.fDepartmentId, dataIndex: 'departmentName', width: 100 }
            ]// end of columns
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        this.priceGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-5',
            title: _lang.ProductDocument.fTotalPrice,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            // url: '',
            fields: ['id','rateAudToRmb','rateAudToUsd','currency','totalPriceAud','totalPriceRmb','totalPriceUsd'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },

                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },

            ],// end of columns
        });
        this.reportGridPanel = new App.ReportProductTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.Reports.tabRelatedReports,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.reportGridPanel);
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

		var productList = Ext.getCmp(conf.subProductGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
	            if(!!json.data && !!json.data.details && json.data.details.length>0){
	                for(index in json.data.details){
	                    var product = {};
	                    Ext.applyIf(product, json.data.details[index]);
	                    Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
	                    productList.getStore().add(product);
	                }
	            }

                //相关报告
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
		App.clickTopTab('FlowNewProductForm', conf);
	}
});