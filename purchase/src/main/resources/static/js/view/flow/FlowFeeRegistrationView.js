FlowFeeRegistrationView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);

        var conf = {
			title : _lang.FlowFeeRegistration.mTitle,
			moduleName: 'FlowFeeRegistration',
			winId : 'FlowFeeRegistrationForm',
			frameId : 'FlowFeeRegistrationView',
			mainGridPanelId : 'FlowFeeRegistrationGridPanelID',
			mainFormPanelId : 'FlowFeeRegistrationFormPanelID',
			processFormPanelId: 'FlowFeeRegistrationProcessFormPanelID',
			searchFormPanelId: 'FlowFeeRegistrationSearchFormPanelID',
			mainTabPanelId: 'FlowFeeRegistrationMainTabsPanelID',
            subFormGridPanelId : 'FlowFeeRegistrationSubGridPanelID',
            formGridPanelId : 'FlowFeeRegistrationProjectFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/feeRegister/list',
			urlSave: __ctxPath + 'flow/finance/feeRegister/save',
			urlDelete: __ctxPath + 'flow/finance/feeRegister/delete',
			urlGet: __ctxPath + 'flow/finance/feeRegister/get',
			urlExport: __ctxPath + 'flow/finance/feeRegister/export',
			urlListProduct: __ctxPath + 'flow/finance/feeRegister/listproduct',
			urlFlow: __ctxPath + 'flow/finance/feeRegister/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowFeeRegister&processInstanceId=',
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
		FlowFeeRegistrationView.superclass.constructor.call(this, {
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
			 	{field:'Q-feeType-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'service_provider', codeSub:'fee_type', addAll:true},
			 	{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.TText.fOrderNumber},
			 	{field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowDepositContract.fOrderTitle},
			 	{field:'Q-title-S-LK', xtype:'textfield', title:_lang.FlowFeeRegistration.fTitle},
			 	{field:'Q-beneficiaryBank-S-LK', xtype:'textfield', title:_lang.BankAccount.fBeneficiaryBank},
			 	{field:'Q-bankAccount-S-LK', xtype:'textfield', title:_lang.BankAccount.fBankAccount},
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
			    {field:'Q-companyCnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-companyEnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyEnName},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowFeeRegistration.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
            fields: [
                'id', 'title','paymentStatus', 'feeType', 'orderNumber', 'vendorCnName', 'vendorEnName', 'beneficiaryBank', 'bankAccount', 'companyName', 'currency',
                'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark','hold',
                'creatorId', 'creatorCnName', 'creatorEnName','assigneeId','assigneeCnName','assigneeEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','companyEnName','companyCnName','orderTitle'
            ],
            columns: [
                {header: _lang.FlowBankAccount.fId, dataIndex: 'id', width: 180,  },
                {header: _lang.FlowFeeRegistration.fFeeType, dataIndex: 'feeType', width: 60,
                    renderer: function (value) {
                        var $feeType = _dict.feeType;
                        if ($feeType.length > 0 && $feeType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $feeType[0].data.options);
                        }
                    }
                },
                {header: _lang.FlowFeeRegistration.fTitle, dataIndex: 'title', width: 200,  },
                {header: _lang.FlowFeeRegistration.fPaymentStatus, dataIndex: 'paymentStatus', width: 80,
                    renderer: function(value){
                        var $paymentStatus = _dict.paymentStatus;
                        if ($paymentStatus.length > 0 && $paymentStatus[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $paymentStatus[0].data.options);
                        }
                    }
                },

                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},
                {header: _lang.FlowDepositContract.fOrderTitle, dataIndex: 'orderTitle', width: 120},
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'companyCnName', width:260, hidden: curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'companyEnName', width: 260, hidden: curUserInfo.lang !='zh_CN'? false: true },
                {header: _lang.BankAccount.fBeneficiaryBank, dataIndex: 'beneficiaryBank', width: 200},
                {header: _lang.BankAccount.fBankAccount, dataIndex: 'bankAccount', width: 200},
                // {header: _lang.BankAccount.fCnCompanyName, dataIndex: 'companyCnName', width: 200},
                // {header: _lang.BankAccount.fEnCompanyName, dataIndex: 'companyEnName', width: 200},
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
                    header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', null, {edit: false})
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow({}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.projectGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-2',
            title: _lang.FlowFeeRegistration.tabItemList,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','businessId', 'itemId','itemCnName','itemEnName','priceAud', 'priceRmb', 'priceUsd', 'currency',
                'qty', 'rateAudToUsd', 'rateAudToRmb', 'remark','subtotalAud','subtotalRmb','subtotalUsd'
            ],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 200, hidden: true, },

                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'itemId', width: 180, hidden: true, },
                {header:_lang.FlowFeeRegistration.fProject, dataIndex: 'itemCnName', width: 180, hidden: curUserInfo.lang == 'zh_CN'? false : true,
                },
                {header: _lang.FlowFeeRegistration.fProject, dataIndex: 'itemEnName', width: 180, hidden: curUserInfo.lang == 'zh_CN'? true : false,
                },

                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                    editor: {xtype: 'dictcombo', code:'transaction', codeSub:'currency'}
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                //报价
                { header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false, gridId: conf.mainTabPanelId + '-2' }),

                },
                // {header: _lang.ProductCombination.fQty, id : conf.mainTabPanelId + '-qtyContainer', dataIndex: 'qty', width: 60, scope:this, },
                //
                //
                // { header: _lang.FlowFeeRegistration.fSubtotal,  id:  conf.mainTabPanelId + '-priceContainer',
                //     columns: new $groupPriceColumns(this, 'subtotalAud','subtotalRmb','subtotalUsd', null,
                //         {edit:false, gridId: conf.mainTabPanelId + '-2' }),
                // },

                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200,
                    editor: {xtype: 'textarea', }
                },
            ]// end of columns
    	})

        this.productGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-3',
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','sku','name','barcode',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords',
                'mandatory', 'creatorName','departmentName',  'vendorName','productLink','newPrevRiskRating','prevRiskRating'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },

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
            ]// end of columns
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });
        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.projectGridPanel);
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

		var projectList = Ext.getCmp(conf.mainTabPanelId+'-2');
        projectList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                projectList.getStore().removeAll();
	            if(!!json.data && !!json.data.details &&  json.data.details.length>0){
	                for(index in json.data.details){
	                    var project = {};
	                    Ext.applyIf(project, json.data.details[index]);
                        project.subtotalAud =  (parseFloat(json.data.details[index].priceAud) * parseFloat(json.data.details[index].qty)).toFixed(2);
                        project.subtotalRmb=   (parseFloat(json.data.details[index].priceRmb) *  parseFloat(json.data.details[index].qty)).toFixed(2);
                        project.subtotalUsd =   (parseFloat(json.data.details[index].priceUsd) * parseFloat(json.data.details[index].qty)).toFixed(2);
                        projectList.getStore().add(project);
	                }
                }
                //'priceAud','priceRmb','priceUsd'
                if(!!json.data && !!json.data.otherDetails &&  json.data.otherDetails.length>0){
                    for(index in json.data.otherDetails){
                        var otherProject = {};
                        Ext.applyIf(otherProject, json.data.otherDetails[index]);
                        otherProject.priceAud =  (parseFloat(json.data.otherDetails[index].priceAud) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                        otherProject.priceRmb=   (parseFloat(json.data.otherDetails[index].priceRmb) *  parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                        otherProject.priceUsd =   (parseFloat(json.data.otherDetails[index].priceUsd) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                        projectList.getStore().add(otherProject);
                    }
                }
                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                var productList = Ext.getCmp(conf.mainTabPanelId+'-3');
                productList.getStore().removeAll();
                if(!!json.data && !!json.data.customClearancePackingDetailVos &&  json.data.customClearancePackingDetailVos.length>0){
                    for(index in json.data.customClearancePackingDetailVos){
                        var product = {};
                        Ext.apply(product, json.data.customClearancePackingDetailVos[index]);
                        Ext.applyIf(product, json.data.customClearancePackingDetailVos[index].product);
                        Ext.applyIf(product, json.data.customClearancePackingDetailVos[index].product.prop);
                        product.sku =  json.data.customClearancePackingDetailVos[index].product.sku;
                        productList.getStore().add(product);
                    }
                }else if(!!json.data && !!json.data.contractDetails &&  json.data.contractDetails.length>0){
                    for(index in json.data.contractDetails){
                        var product = {};
                        Ext.apply(product, json.data.contractDetails[index]);
                        Ext.applyIf(product, json.data.contractDetails[index].product);
                        Ext.applyIf(product, json.data.contractDetails[index].product.prop);
                        product.sku =  json.data.contractDetails[index].product.sku;
                        productList.getStore().add(product);
                    }
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
		App.clickTopTab('FlowFeeRegistrationForm', conf);
	}
});