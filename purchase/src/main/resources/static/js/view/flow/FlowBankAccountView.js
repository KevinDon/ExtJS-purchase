FlowBankAccountView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowBankAccount.mTitle,
			moduleName: 'FlowBankAccount',
			formName:'FlowBankAccountForm',
			winId : 'FlowBankAccountViewForm',
			frameId : 'FlowBankAccountView',
			mainGridPanelId : 'FlowBankAccountViewGridPanelID',
			mainFormPanelId : 'FlowBankAccountViewFormPanelID',
			processFormPanelId: 'FlowBankAccountProcessFormPanelID',
			searchFormPanelId: 'FlowBankAccountViewSearchFormPanelID',
			mainTabPanelId: 'FlowBankAccountViewMainTbsPanelID',
			subGridPanelId : 'FlowBankAccountViewSubGridPanelID',
			formGridPanelId : 'FlowBankAccountFormGridPanelID',

			urlList: __ctxPath + 'flow/finance/flowBankAccount/list',
			urlSave: __ctxPath + 'flow/finance/flowBankAccount/save',
			urlDelete: __ctxPath + 'flow/finance/flowBankAccount/delete',
			urlGet: __ctxPath + 'flow/finance/flowBankAccount/get',
			urlExport: __ctxPath + 'flow/finance/flowBankAccount/export',
			urlFlow: __ctxPath + 'flow/finance/flowBankAccount/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowBankAccount&processInstanceId=',
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
		FlowBankAccountView.superclass.constructor.call(this, {
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
			 	{field:'Q-beneficiaryBank-S-LK', xtype:'textfield', title:_lang.BankAccount.fBeneficiaryBank},
			 	 {field:'Q-beneficiaryBankAddress-S-LK', xtype:'textfield', title:_lang.BankAccount.fBeneficiaryBankAddress},
			 	{field:'Q-bankAccount-S-LK', xtype:'textfield', title:_lang.BankAccount.fBankAccount},
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
			title: _lang.FlowBankAccount.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'vendorId','vendorCnName', 'vendorEnName', 'companyCnAddress', 'companyEnAddress','beneficiaryName',
				'beneficiaryBank', 'swiftCode', 'beneficiaryBankAddress', 'bankAccount', 'currency',
				'status', 'createdAt', 'updatedAt', 'creatorName', 'flowStatus','hold','beneficiaryCnName','beneficiaryEnName',
				'startTime', 'endTime', 'creatorId', 'creatorName', 'creatorCnName', 'creatorEnName','assigneeId','assigneeCnName','assigneeEnName',
				'departmentName', 'departmentCnName', 'departmentEnName', 'processInstanceId'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowBankAccount.fId, dataIndex: 'id', width: 180  },

				{ header: _lang.BankAccount.fCnCompanyName, dataIndex: 'vendorCnName', width: 260},
                { header: _lang.BankAccount.fEnCompanyName, dataIndex: 'vendorEnName', width: 260},
                { header: _lang.BankAccount.fCnCompanyAddress, dataIndex: 'companyCnAddress', width: 390},
                { header: _lang.BankAccount.fEnCompanyAddress, dataIndex: 'companyEnAddress', width: 390},

                { header: _lang.BankAccount.fBeneficiaryBank, dataIndex: 'beneficiaryBank', width: 200},
                { header: _lang.BankAccount.fSwiftCode, dataIndex: 'swiftCode', width: 120},
                { header: _lang.BankAccount.fBeneficiaryBankAddress, dataIndex: 'beneficiaryBankAddress', width: 390},
                { header:  _lang.BankAccount.fBeneficiaryCnName, dataIndex: 'beneficiaryCnName', width: 120},
                { header:  _lang.BankAccount.fBeneficiaryEnName, dataIndex: 'beneficiaryEnName', width: 120},
                { header: _lang.BankAccount.fBankAccount, dataIndex: 'bankAccount', width: 200},

                { header: _lang.TText.fCurrency, dataIndex: 'currency', width:60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                }
			],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});
        
        var viewTabs = Ext.create('widget.FlowViewTabs', conf);

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
		}
	},

	editRow : function(conf){
		App.clickTopTab('FlowBankAccountForm', conf);
	}
});