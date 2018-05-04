ExchangeRateView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ExchangeRate.mTitle,
			moduleName: 'ExchangeRate',
			frameId : 'ExchangeRateView',
			mainGridPanelId : 'ExchangeRateGridPanelID',
			mainFormPanelId : 'ExchangeRateFormPanelID',
			mainViewPanelId : 'ExchangeRateViewPanelID',
			searchFormPanelId: 'ExchangeRateSearchPanelID',
            urlList: __ctxPath + 'desktop/rateControl/list',
            urlSave: __ctxPath + 'desktop/rateControl/save',
            urlDelete: __ctxPath + 'desktop/rateControl/delete',
            urlGet: __ctxPath + 'desktop/rateControl/get',
	        refresh: true,
            edit: true,
            add: true,
            // copy: true,
            editFun: this.editRow
		};
		
		this.initUIComponents(conf);
		ExchangeRateView.superclass.constructor.call(this, {
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
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-effectiveDate-D', xtype: 'DateO2TField', fieldLabel: _lang.ExchangeRate.fEffectiveDate, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.ExchangeRate.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','effectiveDate','aud', 'rateAudToRmb', 'rateAudToUsd','creatorCnName','creatorEnName',
				'departmentCnName','departmentEnName','status','createdAt','updatedAt','publishStatus'
			],

			width: '55%',
			minWidth: 400,
			columns: [	
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
			    { header: _lang.ExchangeRate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140 },
			    { header: _lang.ExchangeRate.fAud, dataIndex: 'aud', width: 80 },
			    { header: _lang.ExchangeRate.fRateAudToRmb, dataIndex: 'rateAudToRmb', width: 80, xtype: 'numbercolumn', format:'0,0000'},
			    { header: _lang.ExchangeRate.fRateAudToUsd, dataIndex: 'rateAudToUsd', width: 80, xtype: 'numbercolumn', format:'0,0000' },
			],// end of columns
			appendColumns: $groupGridCreatedColumnsForFlow({
                flowStatus:false,
				startTime: false,
                endTime:false,
                assignee:false,
			}),
			itemclick: function(obj, record, item, index, e, eOpts){
				//this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
				
	},// end of the init

	editRow: function(conf){
		new ExchangeRateForm(conf).show();
	}
});