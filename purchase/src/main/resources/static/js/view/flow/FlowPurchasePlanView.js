FlowPurchasePlanView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
        Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowPurchasePlan.mTitle,
			moduleName: 'FlowPurchasePlan',
            winId : 'FlowPurchasePlanForm',
            frameId : 'FlowPurchasePlanView',
            mainGridPanelId : 'FlowPurchasePlanGridPanelID',
            mainFormPanelId : 'FlowPurchasePlanFormPanelID',
            processFormPanelId: 'FlowPurchasePlanProcessFormPanelID',
            searchFormPanelId: 'FlowPurchasePlanFormGridSearchFormPanelID',
            mainTabPanelId: 'FlowPurchasePlanFormGridMainTbsPanelID',
            ProductGridPanelId : 'FlowPurchasePlanProductGridPanelID',
            subVendorGridPanelId:'FlowPurchasePlanSubVendorGridPanelID',
            subAttachmentsGridPanelId:'FlowPurchasePlanSubAttachmentsGridPanelID',
            subPriceGridPanelId:'FlowPurchasePlanSubPriceGridPanelID',
            subPurchaseContractGridPanelId:'FlowPurchasePlanSubPurchaseContractGridPanelID',
            formGridPanelId : 'FlowPurchasePlanFormGridPanelID',

			urlList: __ctxPath + 'flow/purchase/plan/list',
			urlSave: __ctxPath + 'flow/purchase/plan/save',
			urlDelete: __ctxPath + 'flow/purchase/plan/delete',
			urlGet: __ctxPath + 'flow/purchase/plan/get',
			urlExport: __ctxPath + 'flow/purchase/plan/export',
			urlListProduct: __ctxPath + 'flow/purchase/plan/listproduct',
			urlFlow: __ctxPath + 'flow/purchase/plan/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchasePlan&processInstanceId=',
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
		FlowPurchasePlanView.superclass.constructor.call(this, {
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
			    	store: [['', _lang.TText.vAll],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['0', _lang.TText.vDraft], ['4',  _lang.TText.vCancel]]
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
			title: _lang.FlowPurchasePlan.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
                'id', 'vendorId','vendorCnName','vendorEnName','currency', 'totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb',
                'rateAudToUsd','totalCbm','depositRate','leadTime', 'creatorCnName','creatorEnName','startTime','endTime',
				'createdAt','status','flowStatus','details', 'processInstanceId','hold',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName','updatedAt',
            ],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 182, },
				{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },

				{ header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
					renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
			    		    return $dictRenderOutputColor(value, $currency[0].data.options);
			    		}
			    	}
				},
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                //报价
				{ header: _lang.NewProductDocument.fPrice,
					columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
				},
				//汇率
				{ header: _lang.FlowPurchasePlan.fTotalCbm, dataIndex: 'totalCbm', width: 60 },
				// { header: _lang.FlowPurchasePlan.fDepositRate, dataIndex: 'depositRate', width: 60 },
				{ header: _lang.FlowPurchasePlan.fLeadTime, dataIndex: 'leadTime', width: 60 },

			],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow(),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){

			}
		});

        this.productGridPanel = new App.FlowPurchasePlanFormGrid({
            mainTabPanelId: conf.mainTabPanelId,
            mainGridPanelId: conf.mainGridPanelId,
            title:  _lang.ProductDocument.tabListTitle,
            scope: this, readOnly: true,
            defHeight: 215,
            noTitle: true
        });

        this.vendorGridPanel = new HP.FormPanel({
            id: conf.subVendorGridPanelId,
            title: _lang.FlowPurchaseContract.tabVendorListTitle,
            fieldItems : [
                { xtype: 'container',cls:'row', items:  [
                    { field: 'vendor.cnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2'},
                    { field: 'main.sellerAddress', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAddress, cls:'col-2'},
                    { field: 'vendor.enName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2'},
                    { field: 'main.sellerEnAddress', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnAddress, cls:'col-2'},
                    { field: 'main.sellerPhone', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fSellerPhone, cls:'col-2', },
                    { field: 'main.sellerFax', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fSellerFax, cls:'col-2', },
                    { field: 'main.sellerEmail', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fSellerEmail, cls:'col-2', },
                    { field: 'vendor.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
                    { field: 'main.sellerContactCnName', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fSellerContactCnName, cls:'col-2', },
                    { field: 'main.sellerContactEnName', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fSellerContactEnName, cls:'col-2', },
                    { field: 'vendor.website', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fWebsite, cls:'col-2', },
                    { field: 'vendor.source', xtype: 'dictfield', code:'vendor', codeSub:'source', fieldLabel: _lang.VendorDocument.fSource, cls:'col-2'},
                    { field: 'vendor.buyerName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fBuyerName, cls:'col-2'},
                    { field: 'vendor.departmentName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, cls:'col-2'},
                    { field: 'vendor.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2'},
                    { field: 'vendor.updatedAt', xtype: 'displayfield', fieldLabel: _lang.TText.fUpdatedAt, cls:'col-2'}
                ]}
            ]
        });



        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        // this.priceGridPanel = new HP.GridPanel({
        //     id: conf.mainTabPanelId + '-5',
        //     title: _lang.ProductDocument.fTotalPrice,
        //     forceFit: false,
        //     autoLoad: false,
        //     bbar: false,
        //     rsort: false,
        //     // url: '',
        //     fields: ['id','rateAudToRmb','rateAudToUsd','currency','totalPriceAud','totalPriceRmb','totalPriceUsd'],
        //     columns: [
        //         { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
        //
        //         //汇率
        //         { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
        //         { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
        //             renderer: function(value){
        //                 var $currency = _dict.currency;
        //                 if($currency.length>0 && $currency[0].data.options.length>0){
        //                     return $dictRenderOutputColor(value, $currency[0].data.options);
        //                 }
        //             }
        //         },
        //         { header: _lang.ProductDocument.fTotalPrice,
        //             columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
        //         },
        //
        //     ],// end of columns
        // });

        this.purchaseContractGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainTabPanelId + '-6',
            title: _lang.FlowPurchasePlan.tabPurchaseContract,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id', 'orderNumber','orderTitle','sellerCnName','sellerEnName','sellerCnAddress',
                'sellerPhone', 'sellerFax', 'sellerContactCnName','sellerContactEnName',',sellerEmail','buyerInfoCnName','buyerInfoEnName',
                'buyerInfoPhone','buyerInfoContactCnName','buyerInfoContactEnName','buyerInfoFax','sellerEmail',
                'buyerInfoEmail','currency', 'totalPriceAud', 'totalPriceRmb','totalPriceUsd', 'totalOrderQty','etd','buyerInfoCnAddress','buyerInfoEnAddress',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId','destinationPortCnName','destinationPortEnName',
                'depositAud','depositRmb','depositUsd','shippingMethod','paymentTerms','otherTerms','orderDate','startTime','flowStatus',
                 'createdAt','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','endTime','status','updatedAt','departmentCnName','departmentEnName',
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: _lang.ProductCertificate.fId, dataIndex: 'id', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 100},
                { header: _lang.FlowPurchaseContract.fOrderTitle, dataIndex: 'orderTitle', width: 150},
                { header: _lang.ProductDocument.fSellerName, dataIndex: 'sellerCnName', width: 100, hidden: curUserInfo.lang == 'zh_CN'? false: true },
                { header: _lang.ProductDocument.fSellerName, dataIndex: 'sellerEnName', width: 100, hidden: curUserInfo.lang == 'zh_CN'? true: false },
                { header: _lang.ProductDocument.fSellerAddress, dataIndex: 'sellerCnAddress', width: 300},
                { header: _lang.ProductDocument.fSellerPhone, dataIndex: 'sellerPhone', width: 100},
                { header: _lang.ProductDocument.fSellerFax, dataIndex: 'sellerFax', width: 100,},
                { header: _lang.ProductDocument.fSellerContactName, dataIndex: 'sellerContactCnName', width: 100,hidden: curUserInfo.lang == 'zh_CN'? false: true  },
                { header: _lang.ProductDocument.fSellerContactName, dataIndex: 'sellerContactEnName', width: 100,hidden: curUserInfo.lang == 'zh_CN'? true: false  },
                { header: _lang.ProductDocument.fSellerEmail, dataIndex: 'sellerEmail', width: 200 },
                { header: _lang.ProductDocument.fBuyerName, dataIndex: 'buyerInfoCnName', width: 100 ,hidden: curUserInfo.lang == 'zh_CN'? false: true  },
                { header: _lang.ProductDocument.fBuyerName, dataIndex: 'buyerInfoEnName', width: 100 ,hidden: curUserInfo.lang == 'zh_CN'? true: false   },
                { header: _lang.ProductDocument.fBuyerAddress, dataIndex: 'buyerInfoCnAddress', width:390,hidden: curUserInfo.lang == 'zh_CN'? false: true},
                { header: _lang.ProductDocument.fBuyerAddress, dataIndex: 'buyerInfoEnAddress', width:390,hidden: curUserInfo.lang == 'zh_CN'? true: false},
                { header: _lang.ProductDocument.fBuyerPhone, dataIndex: 'buyerInfoPhone', width: 100},
                { header: _lang.ProductDocument.fBuyerFax, dataIndex: 'buyerInfoFax', width: 100 },
                { header: _lang.ProductDocument.fBuyerContactName, dataIndex: 'buyerInfoContactCnName', width: 100,hidden: curUserInfo.lang == 'zh_CN'? false: true },
                { header: _lang.ProductDocument.fBuyerContactName, dataIndex: 'buyerInfoContactEnName', width: 100,hidden: curUserInfo.lang == 'zh_CN'? true: false },
                { header: _lang.ProductDocument.fBuyerEmail, dataIndex: 'buyerInfoEmail', width: 200 },

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

                { header: _lang.ProductDocument.fShippingDate, dataIndex: 'etd', width: 140 },
                { header: _lang.ProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.origin, []);}

                },

                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },

                { header: _lang.ProductDocument.fShippingMethod, dataIndex: 'shippingMethod', width: 60,},
                { header: _lang.ProductDocument.fPaymentTerms, dataIndex: 'paymentTerms', width: 60,},
                { header: _lang.ProductDocument.fOtherTerms, dataIndex: 'otherTerms', width: 60,},
                { header: _lang.ProductDocument.fOrderDate, dataIndex: 'orderDate', width: 140,},
                { header: _lang.ProductDocument.fStartTime, dataIndex: 'startTime', width: 140,},
            ],// end of column
            appendColumns: $groupGridCreatedColumnsForFlow({
                assignee:false,
            }),
        });

        this.reportGridPanel = new App.ReportProductTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.Reports.tabRelatedReports,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.vendorGridPanel);
        viewTabs.add(this.attachmentsGridPanel);
       // viewTabs.add(this.priceGridPanel);
        viewTabs.add(this.reportGridPanel);
        viewTabs.add(this.purchaseContractGridPanel);

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

        var productList = Ext.getCmp(conf.mainTabPanelId + '-products');
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo:conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
                        product.orderValueAud = parseFloat(json.data.details[index].priceAud || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);
                        product.orderValueRmb = parseFloat(json.data.details[index].priceRmb || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);
                        product.orderValueUsd = parseFloat(json.data.details[index].priceUsd || 0) * parseFloat(json.data.details[index].orderQty || 0).toFixed(3);

                        productList.getStore().add(product);
                    }
                }
                var ordersList = Ext.getCmp(conf.mainTabPanelId + '-6');
                ordersList.getStore().removeAll();
                if(json.data.orders.length>0){
                    for(index in json.data.orders){
                        ordersList.getStore().add(json.data.orders[index]);
                    }
                }

                //init vendor
                var cmpVendor = Ext.getCmp(conf.subVendorGridPanelId);
                $_setByName(cmpVendor, json.data.vendor, {preName:'vendor', root:'data'});
                $_setByName(cmpVendor, json.data, {preName:'main', root:'data'});

                //attachment init
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                //total Price
                // var totalPriceList = Ext.getCmp(conf.mainTabPanelId + '-5');
                // totalPriceList.getStore().removeAll();
                // if (json.data != undefined) {
                //
                //     var totalPrice = {};
                //     Ext.applyIf(totalPrice, json.data);
                //     //Ext.apply(totalPrice, json.data.details[index].product);
                //     //Ext.applyIf(totalPrice, json.data.details[index].product.prop);
                //     totalPriceList.getStore().add(totalPrice);
                // }

                //相关报告
                Ext.getCmp(conf.mainTabPanelId).items.items[5].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');
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
		App.clickTopTab('FlowPurchasePlanForm', conf);
	}
});