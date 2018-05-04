FlowSamplePaymentView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowSamplePayment.mTitle,
			moduleName: 'FlowSamplePayment',
			winId : 'FlowSamplePaymentViewForm',
			frameId : 'FlowSamplePaymentView',
			mainGridPanelId : 'FlowSamplePaymentGridPanelID',
			mainFormPanelId : 'FlowSamplePaymentFormPanelID',
			processFormPanelId: 'FlowSamplePaymentProcessFormPanelID',
			searchFormPanelId: 'FlowSamplePaymentSearchFormPanelID',
			mainTabPanelId: 'FlowSamplePaymentMainTbsPanelID',
			subProductGridPanelId : 'FlowSamplePaymentSubGridPanelID',
			formGridPanelId : 'FlowSamplePaymentFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/flowSamplePayment/list',
			urlSave: __ctxPath + 'flow/finance/flowSamplePayment/save',
			urlDelete: __ctxPath + 'flow/finance/flowSamplePayment/delete',
			urlGet: __ctxPath + 'flow/finance/flowSamplePayment/get',
			urlExport: __ctxPath + 'flow/finance/flowSamplePayment/export',
			urlListProduct: __ctxPath + 'flow/finance/flowSamplePayment/listproduct',
			urlFlow: __ctxPath + 'flow/finance/flowSamplePayment/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowSamplePayment&processInstanceId=',
			refresh: true,
			//add: true,
			//copy: true,
			edit: true,
			del: true,
			flowMine:true,
			flowInvolved:true,
			export:true,
			editFun: this.editRow,
		};

		this.initUIComponents(conf);
		FlowSamplePaymentView.superclass.constructor.call(this, {
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
			 	{field:'Q-beneficiaryBank-S-LK', xtype:'textfield', title:_lang.FlowSamplePayment.fBeneficiaryBank},
			 	{field:'Q-bankAccount-S-LK', xtype:'textfield', title:_lang.FlowSamplePayment.fBankAccount},
			 	{field:'Q-beneficiary-S-LK', xtype:'textfield', title:_lang.FlowSamplePayment.fBeneficiary},
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

        //var $currency = new $HpDictStore({code:'transaction', codeSub:'currency'});
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowSamplePayment.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'sampleBusinessId','vendorId','vendorCnName','vendorEnName', 'beneficiaryBank','bankAccount','processInstanceId',
				'beneficiary','currency','totalSampleFeeAud','totalSampleFeeRmb','totalSampleFeeUsd','hold',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,},
				{ header:  _lang.FlowSamplePayment.tabSampleID, dataIndex: 'sampleBusinessId', width: 185},
				{ header: _lang.ProductDocument.fVendorId, dataIndex: 'vendorId', width: 160,hidden:true },
				{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260, hidden: curUserInfo.lang == 'zh_CN' ? false: true, },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,  hidden: curUserInfo.lang != 'zh_CN' ? false: true,},
                { header: _lang.FlowSamplePayment.fBeneficiary, dataIndex: 'beneficiary', width: 100 },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowFeePayment.fTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalSampleFeeAud','totalSampleFeeRmb','totalSampleFeeUsd', null, {edit:false})
                },

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
            fields: [
                'id','sku','vendorCnName','vendorEnName','productCategoryId', 'categoryName','name',
                'sampleName','sampleFeeAud','sampleFeeRmb','sampleFeeUsd','currency',
                'sampleFeeRefund','sampleReceiver','rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'qty',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton',
                'length','width','height','netWeight','cubicWeight','cbm'            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120, locked: true},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                { header: _lang.FlowSample.fSampleFee,
                    columns: new $groupPriceColumns(this, 'sampleFeeAud','sampleFeeRmb','sampleFeeUsd', function(row, value){
                        row.set('orderValueAud', (row.get('sampleFeeAud') * row.get('qty')).toFixed(3));
                        row.set('orderValueRmb', (row.get('sampleFeeRmb') * row.get('qty')).toFixed(3));
                        row.set('orderValueUsd', (row.get('sampleFeeUsd') * row.get('qty')).toFixed(3));
                    },{edit:false, gridId: conf.formGridPanelId})
                },
                //采购数量
                { header: _lang.FlowSample.fSampleQty, dataIndex: 'qty', scope:this, width: 80,},
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', function(row, value){
                    },{edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowSample.fSampleFeeRefund, dataIndex: 'sampleFeeRefund', width: 100,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    },
                },
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
            ],// end of columns
        });
        this.bankAccountFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainTabPanelId + '-2',
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

        var productList = Ext.getCmp(conf.subProductGridPanelId);

        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
			success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
	            if(!!json.data && !!json.data.details && json.data.details.length>0){
	                for(index in json.data.details){
	                    var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
                        product.orderValueAud = (json.data.details[index].sampleFeeAud *json.data.details[index].qty).toFixed(3);
                        product.orderValueRmb = (json.data.details[index].sampleFeeRmb *json.data.details[index].qty).toFixed(3);
                        product.orderValueUsd = (json.data.details[index].sampleFeeUsd *json.data.details[index].qty).toFixed(3);
  	                    productList.getStore().add(product);
	                }
	            }

                var bankAccountInfo = Ext.getCmp(conf.mainTabPanelId + '-2');
	            if(!!json.data && !!json.data.vendor) {
                    var bankInfomation = json.data.vendor.bank;
                    //init vendor
                    $_setByName(bankAccountInfo, json.data.vendor.bank, {preName: 'bank', root: 'data'});
                    $_setByName(bankAccountInfo, json.data.vendor, {preName: 'main', root: 'data'});

                    if (!!bankInfomation.guaranteeLetterFile) {
                        bankAccountInfo.getCmpByName('main_guaranteeLetter').setValue(bankInfomation.guaranteeLetterFile.document.name || '');
                    }
                    //contract guarantee letter
                    if (!!bankInfomation.contractGuaranteeLetterFile) {
                        bankAccountInfo.getCmpByName('main_contractGuaranteeLetter').setValue(bankInfomation.contractGuaranteeLetterFile.document.name || '');
                    }
                }

                //attachment init
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
		App.clickTopTab('FlowSamplePaymentForm', conf);
	}
});