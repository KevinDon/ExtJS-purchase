FlowDepositContractView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowDepositContract.mTitle,
			moduleName: 'FlowDepositContract',
			winId : 'FlowDepositContractViewForm',
			frameId : 'FlowDepositContractView',
			mainGridPanelId : 'FlowDepositContractGridPanelID',
			mainFormPanelId : 'FlowDepositContractFormPanelID',
			processFormPanelId: 'FlowDepositContractProcessFormPanelID',
			searchFormPanelId: 'FlowDepositContractSearchFormPanelID',
			mainTabPanelId: 'FlowDepositContractMainTbsPanelID',
			subGridPanelId : 'FlowDepositContractSubGridPanelID',
			formGridPanelId : 'FlowDepositContractFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/purchaseContractDeposit/list',
			urlSave: __ctxPath + 'flow/finance/purchaseContractDeposit/save',
			urlDelete: __ctxPath + 'flow/finance/purchaseContractDeposit/delete',
			urlGet: __ctxPath + 'flow/finance/purchaseContractDeposit/get',
			urlExport: __ctxPath + 'flow/finance/purchaseContractDeposit/export',
			urlFlow: __ctxPath + 'flow/finance/purchaseContractDeposit/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowPurchaseContractDeposit&processInstanceId=',
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
		FlowDepositContractView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel, this.centerPanel ]
		});
	},
    
	initUIComponents: function(conf) {
        var $sampleFeeRefund = new $HpDictStore({code:'samplefee', codeSub:'samplefee_return'});
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			 	{field:'Q-id-S-LK', xtype:'textfield', title:_lang.TText.fId},
			 	{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.TText.fOrderNumber},
			 	{field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.TText.fOrderTitle},
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
			    {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
			    {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel

        var $currency = new $HpDictStore({code:'transaction', codeSub:'currency'});
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowDepositContract.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'vendorId','orderId','orderNumber','vendorCnName','vendorEnName','processInstanceId','orderTitle',
				'currency','remark','totalDepositAud','totalDepositRmb','totalDepositUsd','rateAudToRmb',
				'rateAudToUsd','latestPaymentTime','status', 'createdAt', 'updatedAt', 'creatorCnName', 'creatorEnName',
				'flowStatus','startTime', 'endTime','departmentName','departmentCnName','departmentEnName',
                'assigneeId','assigneeCnName','assigneeEnName','hold','payableAud','payableRmb','payableUsd','paymentAud','paymentRmb','paymentUsd',
                'paymentTotalDepositAud','paymentTotalDepositRmb','paymentTotalDepositUsd','depositRate',
                 'paymentRateAudToRmb', 'paymentRateAudToUsd'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowDepositContract.fId, dataIndex: 'id', width: 180  },
				{ header: _lang.ProductDocument.fVendorId, dataIndex: 'vendorId', width: 160,hidden:true },
//				{ header: _lang.FlowNewProduct.fVendorName, dataIndex: 'vendorName', width: 160},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.FlowDepositContract.fOrderNumber, dataIndex: 'orderNumber', width: 90},
				{ header: _lang.TText.fOrderTitle, dataIndex: 'orderTitle', width: 180},

                { header: _lang.FlowPurchasePlan.fDepositRate, dataIndex: 'depositRate', width:60 },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率



                { header: _lang.FlowDepositContract.fTotal,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                        },
                        { header: _lang.FlowDepositContract.fTotalDeposit,
                            columns: new $groupPriceColumns(this,'payableAud', 'payableRmb', 'payableUsd', null, {edit:false}),
                        },
                    ]
                },
                { header: _lang.FlowDepositContract.fReceivedTotal,
                    columns: [
                       {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'paymentRateAudToRmb', 'paymentRateAudToUsd')
                        },
                        { header: _lang.FlowDepositContract.fReceivedTotalDeposit,
                            columns: new $groupPriceColumns(this, 'paymentAud','paymentRmb','paymentUsd', null,
                                {edit:false, gridId: conf.mainGridPanelId})
                        },
                    ],
                },




                { header: _lang.FlowDepositContract.fLatestPaymentTime, dataIndex: 'latestPaymentTime', width: 80, hidden:true, },
                // { header: _lang.TText.fRemark, dataIndex: 'remark', width: 200 },

            ],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.productGridPanel = new HP.GridPanel({
            id: conf.subGridPanelId + '-product',
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','sku','name','barcode','priceAud','priceRmb','priceUsd','orderQty','categoryName',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords',
                'mandatory', 'creatorName','departmentName',  'vendorName','productLink','newPrevRiskRating','prevRiskRating'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 200, },

                { header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,{gridId: conf.formGridPanelId,})
                },

                {header: _lang.ProductCombination.fQty, dataIndex: 'orderQty', width: 60, scope:this, },
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
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                // { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                // { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },

            ],// end of columns
        });
        this.bankAccountFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainTabPanelId + '-3',
            scope: this,
            title: _lang.FlowBankAccount.tabAccountInformation,

            autoScroll: true,
            fieldItems : [
                //收款账号信息
                { xtype: 'container',cls:'row', items:  [

                    { field: 'bank.companyCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2' , readOnly: true,allowBlank:true,  },
                    { field: 'bank.companyEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2',  readOnly: true, allowBlank:true, },
                    { field: 'bank.companyCnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.companyEnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2' , readOnly: true, allowBlank:true,  },
                    { field: 'bank.beneficiaryBank', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.beneficiary', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , hidden:true, },
                    { field: 'bank.beneficiaryBankAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.beneficiaryCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2',readOnly: true,allowBlank:true, },
                    { field: 'bank.beneficiaryEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', readOnly: true,allowBlank:true, },
                    { field: 'bank.swiftCode', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.cnaps', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.bankAccount', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2' , readOnly: true, allowBlank:true, },
                    { field: 'bank.currency',  xtype:'displayfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.TText.fCurrency, cls:'col-2' ,
                        readOnly: true, allowBlank: false,
                        renderer: function(value){
                            var $currency = _dict.currency;
                            if($currency.length>0 && $currency[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $currency[0].data.options);
                            }
                        }
                    }
                ] },
                //保函&担保函
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main_guaranteeLetter', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.guaranteeLetter', single:true, readOnly: true, allowBlank:true,
                    },
                    { field: 'main_contractGuaranteeLetter', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.contractGuaranteeLetter', single:true, readOnly: true,allowBlank:true,
                    }
                ] },
            ]
        });


        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.bankAccountFormPanel);
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
        var productList = Ext.getCmp(conf.subGridPanelId + '-product');
        productList.loadData({
            url :  conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
			success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                var contractDetails = json.data.purchaseContractDetail || {};
                if(contractDetails.length > 0){
                    productList.getStore().removeAll();
                    for(index in contractDetails){
                        var product = {};
                        Ext.applyIf(product, contractDetails[index]);
                        Ext.apply(product, contractDetails[index].product);
                        Ext.apply(product, contractDetails[index].product.prop);
                        product.sku =  contractDetails[index].product.sku
                        product.id =  contractDetails[index].id
                        productList.getStore().add(product);
                    }
                }
                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                //init vendor
                var bankAccountInfo = Ext.getCmp(conf.mainTabPanelId + '-3');
                var  bankInfomation = json.data.vendor.bank;
                //init vendor
                $_setByName(bankAccountInfo, json.data.vendor.bank, {preName:'bank', root:'data'});
                $_setByName(bankAccountInfo,  json.data.vendor, {preName:'main', root:'data'});
                if (!!bankInfomation.guaranteeLetterFile) {
                    bankAccountInfo.getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                }
                //contract guarantee letter
                if (!!bankInfomation.contractGuaranteeLetterFile) {
                    bankAccountInfo.getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
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
		App.clickTopTab('FlowDepositContractForm', conf);
	}
});