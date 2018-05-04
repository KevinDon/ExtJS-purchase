AccountUserView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AccountUser.mTitle,
			moduleName: 'User',
			frameId : 'AccountUserView',
			mainGridPanelId : 'AccountUserGridPanelID',
			mainFormPanelId : 'AccountUserFormPanelID',
			searchFormPanelId: 'AccountUserSearchPanelID',
			mainTabPanelId: 'AccountUserTbsPanelId',
			subGridPanelId : 'AccountUserSubGridPanelID',
			urlList: __ctxPath + 'admin/user/list',
			urlSave: __ctxPath + 'admin/user/save',
			urlDelete: __ctxPath + 'admin/user/delete',
			urlGet: __ctxPath + 'admin/user/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		AccountUserView.superclass.constructor.call(this, {
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
			    {field:'Q-account-S-LK', xtype:'textfield', title:_lang.TText.fAccount},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title:_lang.AccountUser.fDepartmentName,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.AccountUser.fCnName},
			    {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.AccountUser.fEnName},
			    {field:'Q-gender-N-EQ', xtype:'combo', title:_lang.AccountUser.fGender, value:'',
			    	store: [['', _lang.TText.vAll],['2', _lang.TText.vFemale], ['1', _lang.TText.vMale]]
			    },
			    {field:'Q-email-S-LK', xtype:'textfield', title:_lang.TText.fEmail},
			    {field:'Q-phone-S-LK', xtype:'textfield', title:_lang.AccountUser.fPhone},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    },
                { field: 'Q-joinusAt-D', xtype: 'DateO2TField', fieldLabel: _lang.AccountUser.fJoinusAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.AccountUser.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','account','department.cnName','department.enName', 'departmentId','cnName','enName', 'email', 'qq','skype','extension','phone', 'gender','joinusAt', 'createdAt', 'setListRows', 'lang', 'timezone','dateFormat', 'status'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.TText.fAccount, dataIndex: 'account', width: 120},
				{ header: _lang.AccountUser.fDepartmentName, dataIndex: 'department.cnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.AccountUser.fDepartmentName, dataIndex: 'department.enName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.AccountUser.fDepartmentId, dataIndex: 'departmentId', width: 80, hidden: true },
			    { header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 100,hidden:curUserInfo.lang =='zh_CN'? false: true},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120,hidden:curUserInfo.lang !='zh_CN'? false: true},
			    { header: _lang.AccountUser.fGender, dataIndex: 'gender', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
					}
			    },
			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
			    { header: _lang.AccountUser.fQq, dataIndex: 'qq', width: 140 },
			    { header: _lang.AccountUser.fSkype, dataIndex: 'skype', width: 140 },
			    { header: _lang.AccountUser.fPhone, dataIndex: 'phone', width: 100 },
			    { header: _lang.AccountUser.fExtension, dataIndex: 'extension', width: 100 },
			    { header: _lang.AccountUser.fSetListRows, dataIndex: 'setListRows', width: 40 },
			    { header: _lang.AccountUser.fJoinusAt, dataIndex: 'joinusAt', width: 140 },
			    { header: _lang.AccountUser.fCreatedAt, dataIndex: 'createdAt', width: 140 },
			    { header: _lang.TText.fLang, dataIndex: 'lang', width: 40 },
			    { header: _lang.TText.fTimezone, dataIndex: 'timezone', width: 60 },
			    { header: _lang.TText.fDateFormat, dataIndex: 'dateFormat', width: 80 },
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.AccountUser.tabFormTitle,
			region: 'center',
			scope: this,

			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'main.account', xtype: 'textfield', fieldLabel: _lang.TText.fAccount, allowBlank: false},
			    { field: 'main.departmentId', xtype: 'hidden', allowBlank: false},
			    { field: 'main.department.title', xtype: 'DepDialog', allowBlank: false, fieldLabel: _lang.AccountUser.fDepartmentName, 
			    	formId:conf.mainFormPanelId, hiddenName:'main.departmentId', single: true},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.AccountUser.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.AccountUser.fEnName, allowBlank: false},
			    { field: 'main.gender', xtype: 'combo', fieldLabel: _lang.AccountUser.fGender, allowBlank: false, value:'1',
			    	store: [['1', _lang.TText.vMale],['2', _lang.TText.vFemale]]
			    },
			    { field: 'main.joinusAt', xtype: 'datetimefield', fieldLabel: _lang.AccountUser.fJoinusAt, format: curUserInfo.dateFormat},
			    { xtype: 'section', title:_lang.AccountUser.tabSectionContact},
			    { field: 'main.qq', xtype: 'textfield', fieldLabel: _lang.AccountUser.fQq},
			    { field: 'main.wechat', xtype: 'textfield', fieldLabel: _lang.AccountUser.fWechat},
			    { field: 'main.skype', xtype: 'textfield', fieldLabel: _lang.AccountUser.fSkype},
			    { field: 'main.email', xtype: 'textfield', fieldLabel: _lang.TText.fEmail, allowBlank: false, flex: 20 , vtype:'email',  },
			    { field: 'main.phone', xtype: 'textfield', fieldLabel: _lang.AccountUser.fPhone, allowBlank: false,},
			    { field: 'main.extension', xtype: 'textfield', fieldLabel: _lang.AccountUser.fExtension},
			    { xtype: 'section', title:_lang.AccountUser.tabSectionSetting},
			    { field: 'main.setListRows', xtype: 'textfield', fieldLabel: _lang.AccountUser.fSetListRows, value:'25', allowBlank: false},
			    { field: 'main.lang', xtype: 'combo', fieldLabel: _lang.TText.fLang, allowBlank: false, value:'zh_CN',
			    	store: [['zh_CN', _lang.TText.zh_CN], ['en_AU', _lang.TText.en_AU]]
			    },
			    { field: 'main.timezone', xtype: 'combo', fieldLabel: _lang.TText.fTimezone, allowBlank: false, value:'GMT+8',
			    	store: [['GMT+8', _lang.TText.vTimezone_8], ['GMT+10', _lang.TText.vTimezone_10]]
			    },
			    { field: 'main.dateFormat', xtype: 'combo', fieldLabel: _lang.TText.fDateFormat, allowBlank: false, value:_lang.TText.vDateFormat_c,
			    	store: [[_lang.TText.vDateFormat_c, _lang.TText.vDateFormat_c], [_lang.TText.vDateFormat_e, _lang.TText.vDateFormat_e]]
			    },
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
			    	store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    },
			    { xtype: 'section', title:_lang.AccountUser.tabSectionPassword},
			    { field: 'main.plainPassword', xtype: 'textfield', fieldLabel: _lang.TText.fPassword, maxLength: 30},
			    { field: 'main.password', xtype: 'hidden',  maxLength:30},
			    { field: 'main.salt', xtype: 'hidden'},
			]
		});
		
		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.formPanel]
		});
				
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
        this.formPanel.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main'
		});
	}
});