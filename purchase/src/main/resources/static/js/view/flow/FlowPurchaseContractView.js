FlowPurchaseContractView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowPurchaseContract.mTitle,
			moduleName: 'FlowPurchaseContract',
			winId : 'FlowPurchaseContractForm',
			frameId : 'FlowPurchaseContractView',
			mainGridPanelId : 'FlowPurchaseContractGridPanelID',
			mainFormPanelId : 'FlowPurchaseContractFormPanelID',
			processFormPanelId: 'FlowNewProductProcessFormPanelID',
			searchFormPanelId: 'FlowPurchaseContractSearchFormPanelID',
			mainTabPanelId: 'FlowPurchaseContractMainTbsPanelID',
			subProductGridPanelId : 'FlowPurchaseContractSubProductGridPanelID',
            subVendorGridPanelId:'FlowPurchaseContractSubVendorGridPanelID',
            subPurchasePlanGridPanelId:'FlowPurchaseContractSubPruchasePlanGridPanelID',
            subPriceGridPanelId:'FlowPurchaseContractSubPriceGridPanelID',
            formGridPanelId : 'FlowPurchaseContractFormGridPanelID',

			urlList: __ctxPath + 'flow/purchase/purchasecontract/list',
			urlSave: __ctxPath + 'flow/purchase/purchasecontract/save',
			urlDelete: __ctxPath + 'flow/purchase/purchasecontract/delete',
			urlGet: __ctxPath + 'flow/purchase/purchasecontract/get',
			urlExport: __ctxPath + 'flow/purchase/purchasecontract/export',
			urlListProduct: __ctxPath + 'flow/purchase/purchasecontract/listproduct',
			urlFlow: __ctxPath + 'flow/purchase/purchasecontract/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchaseContract&processInstanceId=',
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
		FlowPurchaseContractView.superclass.constructor.call(this, {
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
                {field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.TText.fOrderNumber},
                {field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.TText.fOrderTitle},
                {field:'Q-sellerEmail-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSellerEmail},
                {field:'Q-buyerEmail-S-LK', xtype:'textfield', title:_lang.ProductDocument.fBuyerEmail},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
				{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['0', _lang.TText.vDraft],  ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			    {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
			    {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
			    { field: 'Q-etd-D', xtype: 'DateO2TField', fieldLabel: _lang.ProductDocument.fShippingDate, format: curUserInfo.dateFormat},
			    { field: 'Q-orderDate-D', xtype: 'DateO2TField', fieldLabel: _lang.ProductDocument.fOrderDate, format: curUserInfo.dateFormat},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowPurchaseContract.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'orderNumber','orderTitle','sellerCnAddress','createdAt','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','isNeededQc',
                'departmentCnName','departmentEnName','updatedAt','status','startTime','endTime','processInstanceId','sellerContactCnName','sellerContactEnName',
                'containerType','containerQty','readyDate',
                'sellerPhone', 'sellerFax', 'sellerContactName','sellerEmail','buyerName','buyerInfoCnAddress','buyerInfoEnAddress', 'buyerInfoPhone',
                'buyerInfoContactCnName','buyerInfoContactEnName','buyerInfoFax','buyerInfoEmail','hold','rateAudToRmb','rateAudToUsd',
                'buyerEmail','currency', 'totalPriceAud', 'totalPriceRmb','totalPriceUsd', 'totalOrderQty','shippingDate','etd','buyerInfoCnName','buyerInfoEnName',
                'originPortId','originPortCnName','originPortEnName','destinationPortId','destinationPortCnName','destinationPortEnName','retd','reta',
                'depositAud','depositRmb','depositUsd','shippingMethod','paymentTerms','otherTerms','orderDate','flowStatus','creatorId','departmentId',
                'totalOtherAud','totalOtherRmb','totalOtherUsd',
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.ProductCertificate.fId, dataIndex: 'id', width: 180  },
				{ header: _lang.TText.fOrderNumber, dataIndex: 'orderNumber', width: 90},
				{ header: _lang.TText.fOrderTitle, dataIndex: 'orderTitle', width: 200},
               /* { header: _lang.ProductDocument.fSellerName, dataIndex: 'sellerName', width: 100, hidden: true },*/
                { header: _lang.FlowPurchasePlan.fIsNeededQc, dataIndex: 'isNeededQc', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        else if(value == '3') return $renderOutputColor('orange', _lang.TText.vExemption);
                        else {return $renderOutputColor('gray', _lang.TText.vNo);}
                    }
                },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                { header: _lang.ProductDocument.fTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.FlowFeeRegistration.tabOtherItem,
                    columns: new $groupPriceColumns(this, 'totalOtherAud','totalOtherRmb','totalOtherUsd', null, {edit:false})
                },
                { header: _lang.FlowPurchaseContract.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },

                // { header: _lang.ProductDocument.fSellerAddress, dataIndex: 'sellerCnAddress', width: 390},
                // { header: _lang.ProductDocument.fSellerPhone, dataIndex: 'sellerPhone', width: 100},
                // { header: _lang.ProductDocument.fSellerFax, dataIndex: 'sellerFax', width: 100,},
                // { header: _lang.ProductDocument.fSellerContactName, dataIndex: 'sellerContactCnName', width: 100 ,hidden:curUserInfo.lang =='zh_CN' ? false: true, },
                // { header: _lang.ProductDocument.fSellerContactName, dataIndex: 'sellerContactEnName', width: 100, hidden:curUserInfo.lang =='zh_CN' ? true: false, },
                // { header: _lang.ProductDocument.fSellerEmail, dataIndex: 'sellerEmail', width: 200 },
                { header: _lang.ProductDocument.fBuyerName, dataIndex: 'buyerInfoCnName', width: 100, hidden:  curUserInfo.lang == 'zh_CN'? false:true,},
                { header: _lang.ProductDocument.fBuyerName, dataIndex: 'buyerInfoEnName', width: 100,hidden:  curUserInfo.lang == 'zh_CN'? true:false,},
                { header: _lang.ProductDocument.fBuyerAddress, dataIndex:'buyerInfoEnAddress', width: 390, hidden:true },
                { header: _lang.ProductDocument.fBuyerAddress, dataIndex:'buyerInfoCnAddress', width: 390, hidden:true },
                { header: _lang.ProductDocument.fBuyerPhone, dataIndex: 'buyerInfoPhone', width: 100, hidden:true },
                { header: _lang.ProductDocument.fBuyerFax, dataIndex: 'buyerInfoFax', width: 100, hidden:true },
                { header: _lang.ProductDocument.fBuyerContactName, dataIndex: 'buyerInfoContactCnName', width: 100, hidden:true },
                { header: _lang.ProductDocument.fBuyerContactName, dataIndex: 'buyerInfoContactEnName', width: 100, hidden:true },
                { header: _lang.ProductDocument.fBuyerEmail, dataIndex:'buyerInfoEmail', width: 200, hidden:true},
                { header: _lang.ProductDocument.fOriginPort, dataIndex: 'originPortId', width: 100,
                    renderer: function(value){
                        var $origin = _dict.origin;
                        if($origin.length>0 && $origin[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $origin[0].data.options);
                        }
                    }
                },

         

                { header: _lang.FlowOrderShippingPlan.fContainerType, dataIndex: 'containerType', width: 80,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if($containerType.length>0 && $containerType[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $containerType[0].data.options);
                        }

                    }
                },
                { header: _lang.FlowOrderShippingPlan.fContainerQty, dataIndex: 'containerQty', width: 80, },


                { header: _lang.ProductDocument.fShippingMethod, dataIndex: 'shippingMethod', width: 60,
                    renderer: function(value){
                        var $shippingMethod = _dict.shippingMethod;
                        if($shippingMethod.length>0 && $shippingMethod[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $shippingMethod[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fOrderDate, dataIndex: 'orderDate', width: 140,  },
                { header: _lang.FlowPurchaseContract.fReadyDate, dataIndex: 'readyDate', width: 140,  },
                { header: _lang.ProductDocument.fShippingDate, dataIndex: 'etd', width: 140 },
                { header: _lang.ProductDocument.fRetd, dataIndex: 'retd', width: 140 },
                { header: _lang.ProductDocument.fReta, dataIndex: 'reta', width: 140 },

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
            bbar: false,
            rsort: false,
            // url: conf.urlListProduct,
            fields: [
                'id','sku','name','barcode','categoryName','factoryCode',
                'orderQty','currency','priceAud','priceRmb','priceUsd','creatorName','departmentName',
                'rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonH',
                'innerCartonL','masterCartonCbm','masterCartonW'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.ProductDocument.fFactoryNumber, dataIndex: 'factoryCode', width: 200},
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
                { header: _lang.FlowPurchasePlan.fOrderValue,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null, {edit:false})
                },

                {header: _lang.ProductDocument.tabInnerCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fInnerCartonL, dataIndex: 'innerCartonL', width: 80,},
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
        this.vendorGridPanel = new HP.FormPanel({
            id: conf.subVendorGridPanelId,
            title: _lang.FlowPurchaseContract.tabVendorListTitle,
            fieldItems : [
                { xtype: 'container',cls:'row', items:  [
                    { field: 'vendor.cnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2'},
                    { field: 'main.sellerAddress', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAddress, cls:'col-2'},
                    { field: 'vendor.enName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2'},
                    { field: 'vendor.bank.beneficiaryCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2', },
                    { field: 'vendor.bank.beneficiaryEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', },
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

        this.purchasePlanPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subPurchasePlanGridPanelId,
            title: _lang.FlowPurchasePlan.mTitle,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            fields: [
                'id', 'vendorId','vendorCnName','vendorEnName','currency', 'priceAud','priceRmb','priceUsd','rateAudToRmb',
                'rateAudToUsd','totalCbm','depositRate','leadTime', 'applicantId',
                'applicantName','departmentId','departmentName','applyTime',
                'createdAt','status','flowStatus','details', 'processInstanceId',
                'flowStatus', 'createdAt','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','endTime','status','updatedAt',
                'departmentCnName','departmentEnName',
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 200, },
//                { header: _lang.FlowNewProduct.fVendorName, dataIndex: 'vendorName', width: 160},
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
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                //报价
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false})
                },
                { header: _lang.FlowPurchasePlan.fTotalCbm, dataIndex: 'totalCbm', width: 60 },
                // { header: _lang.FlowPurchasePlan.fDepositRate, dataIndex: 'depositRate', width: 60 },
                { header: _lang.FlowPurchasePlan.fLeadTime, dataIndex: 'leadTime', width: 60 },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({
                assignee:false,
                sort:false,
            }),
        });
        this.priceGridPanel = new HP.GridPanel({
            id: conf.subPriceGridPanelId,
            title: _lang.ProductDocument.fTotalPrice,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            // url: '',
            fields: [
                'id','businessId', 'itemName', 'priceAud', 'priceRmb', 'priceUsd', 'currency',
                'qty', 'rateAudToUsd', 'rateAudToRmb', 'remark','subtotalAud','subtotalRmb','subtotalUsd'
            ],
            columns: [

                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 180, hidden: true, },

                {
                    header: _lang.FlowFeeRegistration.fAmountName, dataIndex: 'itemName', width: 200,
                    //editor: {xtype: 'textfield',}
                },
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },

                { header: _lang.FlowServiceInquiry.fPriceTotal,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                        {edit:false, gridId: conf.subFormGridPanelId + '-price'})
                },
            ],// end of columns
        });

        this.paymentTermsPanel = Ext.create("Ext.Panel",{
            id : conf.mainTabPanelId+'-paymentTerms',
            title : _lang.ProductDocument.fPaymentTerms,
            layout: 'fit',
            bodyPadding: 5,
            html: ''
        });
        this.otherTermsPanel = Ext.create("Ext.Panel",{
            id : conf.mainTabPanelId+'-otherTerms',
            title : _lang.ProductDocument.fOtherTerms,
            layout: 'fit',
            bodyPadding: 5,
            html: ''
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.vendorGridPanel);
        //viewTabs.add(this.purchasePlanPanel);
        viewTabs.add(this.priceGridPanel);
        viewTabs.add(this.paymentTermsPanel);
        viewTabs.add(this.otherTermsPanel);
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
            loadMask:true,
            maskTo: conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
	            if(!!json.data.details && json.data.details.length>0){
	                for(index in json.data.details){
	                    var product = {};
	                    Ext.applyIf(product, json.data.details[index]);
	                    Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
	                    productList.getStore().add(product);
	                }
	            }

                var PurchaseList = Ext.getCmp(conf.subPurchasePlanGridPanelId);
                PurchaseList.getStore().removeAll();
                if(!!json.data.details && json.data.details.length>0){
                    for(index in json.data.details){
                        var purchase = {};
                        Ext.applyIf(purchase, json.data.details[index]);
                        Ext.apply(purchase, json.data.details[index].product);
                        Ext.applyIf(purchase, json.data.details[index].product.prop);
                        PurchaseList.getStore().add(purchase);
                    }
                }

                //init vendor
                var cmpVendor = Ext.getCmp(conf.subVendorGridPanelId);
                $_setByName(cmpVendor, json.data.vendor, {preName:'vendor', root:'data'});
                $_setByName(cmpVendor, json.data, {preName:'main', root:'data'});


                //
                //应付差额
                var currency = json.data.currency;
                var rateAudToRmb = json.data.rateAudToRmb;
                var rateAudToUsd = json.data.rateAudToUsd;
                var price = [];
                price.push({
                    'itemName' : _lang.FlowFeeRegistration.fTotalPrice,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.totalPriceAud || 0,
                    'priceRmb' : json.data.totalPriceRmb || 0,
                    'priceUsd' : json.data.totalPriceUsd || 0,
                });
                price.push({
                    'itemName' :  _lang.FlowPurchaseContract.fTotalValue,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.totalValueAud || 0,
                    'priceRmb' : json.data.totalValueRmb || 0,
                    'priceUsd' : json.data.totalValueUsd|| 0,
                });
                price.push({
                    'itemName' : _lang.FlowFeeRegistration.fDeposit,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.depositAud || 0,
                    'priceRmb' : json.data.depositRmb || 0,
                    'priceUsd' : json.data.depositUsd || 0,
                });

                price.push({
                    'itemName' : _lang.FlowFeeRegistration.fWriteOffPrice,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.writeOffAud || 0,
                    'priceRmb' : json.data.writeOffRmb || 0,
                    'priceUsd' : json.data.writeOffUsd || 0,
                });
                price.push({
                    'itemName' :  _lang.FlowFeeRegistration.tabOtherItem,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.totalOtherAud || 0,
                    'priceRmb' : json.data.totalOtherRmb || 0,
                    'priceUsd' : json.data.totalOtherUsd|| 0,
                });

                price.push({
                    'itemName' :_lang.FlowFeeRegistration.fBalance,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.balanceAud || 0,
                    'priceRmb' : json.data.balanceRmb || 0,
                    'priceUsd' : json.data.balanceUsd|| 0,
                });

                price.push({
                    'itemName' :  _lang.FlowFeeRegistration.fAdjustBalance,
                    'currency' : currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.adjustBalanceAud || 0,
                    'priceRmb' : json.data.adjustBalanceRmb || 0,
                    'priceUsd' : json.data.adjustBalanceUsd || 0,
                });

                price.push({
                    'itemName' :  _lang.FlowFeeRegistration.fAdjustValueBalance,
                    //'itemEnName' :  _lang.FlowFeeRegistration.fAdjustValueBalance,
                    'currency' :currency,
                    'rateAudToRmb':rateAudToRmb,
                    'rateAudToUsd':rateAudToUsd,
                    'priceAud' : json.data.adjustValueBalanceAud || 0,
                    'priceRmb' : json.data.adjustValueBalanceRmb || 0,
                    'priceUsd' : json.data.adjustValueBalanceUsd || 0,
                });

                Ext.getCmp(conf.subPriceGridPanelId).getStore().removeAll();
                for (var i in price){
                    Ext.getCmp(conf.subPriceGridPanelId).getStore().add(price[i]);
                }

                // var priceGridList = Ext.getCmp(conf.subPriceGridPanelId);
                // priceGridList.getStore().removeAll();
                // if(!!json.data){
                //     var vendor = {};
                //     Ext.apply(vendor, json.data);
                //     priceGridList.getStore().add(vendor);
                // }

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[7].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                //支付条款
                Ext.getCmp(conf.mainTabPanelId+'-paymentTerms').update(!!json.data.paymentTerms ? Ext.util.Format.nl2br(json.data.paymentTerms): '-');
                Ext.getCmp(conf.mainTabPanelId+'-otherTerms').update(!!json.data.otherTerms ? Ext.util.Format.nl2br(json.data.otherTerms): '-');
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
		App.clickTopTab('FlowPurchaseContractForm', conf);
	}
});