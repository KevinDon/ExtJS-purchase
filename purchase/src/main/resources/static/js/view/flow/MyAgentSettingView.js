MyAgentSettingView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.MyAgentSetting.mTitle,
			moduleName: 'MyAgentSetting',
			frameId : 'MyAgentSettingView',
			mainGridPanelId : 'MyAgentSettingGridPanelID',
			mainFormPanelId : 'MyAgentSettingFormPanelID',
			searchFormPanelId: 'MyAgentSettingSearchPanelID',
			mainTabPanelId: 'MyAgentSettingTbsPanelId',
			subGridPanelId : 'MyAgentSettingSubGridPanelID',
			urlList: __ctxPath + 'flow/myagentsetting/list',
			urlSave: __ctxPath + 'flow/myagentsetting/save',
			urlDelete: __ctxPath + 'flow/myagentsetting/delete',
			urlGet: __ctxPath + 'flow/myagentsetting/get',
			refresh: true,
			edit: true,
			add: true,
			//saveAs: true,
			del: true,
            //addFun: this.addRow,
            editFun: this.editRow,
            saveFun: this.saveFun,
        };
		this.initUIComponents(conf);
		MyAgentSettingView.superclass.constructor.call(this, {
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
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-toUserCnName-S-LK', xtype:'textfield', title:_lang.MyAgentSetting.fToUserCnName},
			    {field:'Q-toUserEnName-S-LK', xtype:'textfield', title:_lang.MyAgentSetting.fToUserEnName},
			    {field:'Q-fromUserCnName-S-LK', xtype:'textfield', title:_lang.MyAgentSetting.fFromUserCnName},
			    {field:'Q-fromUserEnName-S-LK', xtype:'textfield', title:_lang.MyAgentSetting.fFromUserEnName},
			    { field: 'Q-fromTime-D', xtype: 'DateO2TField', fieldLabel: _lang.MyAgentSetting.fFromTime, format: curUserInfo.dateFormat},
			    { field: 'Q-toTime-D', xtype: 'DateO2TField', fieldLabel: _lang.MyAgentSetting.fToTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.MyAgentSetting.mTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
            rsort: false,
			url: conf.urlList,
			fields: [
				'id','toUserId','toUserCnName','toUserEnName', 'toDepartmentId','toDepartmentCnName','toDepartmentEnName',
			          'fromUserId','fromUserCnName','fromUserEnName', 'fromDepartmentId','fromDepartmentCnName','fromDepartmentEnName',
			          'creatorId','creatorCnName','creatorEnName', 'assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
			          'fromTime', 'toTime', 'status', 'createdAt', 'updatedAt','flowCode','cnName','enName','days'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.MyAgentSetting.fFromTime, dataIndex: 'fromTime', width: 140},
				{ header: _lang.MyAgentSetting.fToTime, dataIndex: 'toTime', width: 140 },
				{ header: _lang.MyAgentSetting.fFlowCode, dataIndex: 'flowCode', width: 150, },
				{ header: _lang.MyAgentSetting.fName, dataIndex: 'cnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fName, dataIndex: 'enName', width: 100 , hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fDays, dataIndex: 'days', width: 40, },
				{ header: _lang.MyAgentSetting.fToUserId, dataIndex: 'toUserId', width: 80, hidden: true },
				{ header: _lang.MyAgentSetting.fToUserName, dataIndex: 'toUserCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fToUserName, dataIndex: 'toUserEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fToDepartmentId, dataIndex: 'toDepartmentId', width: 80, hidden: true },
				{ header: _lang.MyAgentSetting.fToDepartmentName, dataIndex: 'toDepartmentCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fToDepartmentName, dataIndex: 'toDepartmentEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fFromUserId, dataIndex: 'fromUserId', width: 80, hidden: true },
				{ header: _lang.MyAgentSetting.fFromUserName, dataIndex: 'fromUserCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fFromUserName, dataIndex: 'fromUserEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fFromDepartmentId, dataIndex: 'fromDepartmentId', width: 80, hidden: true },
				{ header: _lang.MyAgentSetting.fFromDepartmentName, dataIndex: 'fromDepartmentCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.MyAgentSetting.fFromDepartmentName, dataIndex: 'fromDepartmentEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
		});


		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel]
		});
	},// end of the init


    editRow : function(conf){
        new MyAgentSettingForm(conf).show();
    }

});