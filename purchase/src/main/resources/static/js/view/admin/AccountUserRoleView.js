AccountUserRoleView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AccountUserRole.mTitle,
			moduleName: 'UserRole',
			frameId : 'AccountUserRoleView',
			mainGridPanelId : 'AccountUserRoleGridPanelID',
			mainFormPanelId : 'AccountUserRoleFormPanelID',
			searchFormPanelId: 'AccountUserRoleSearchPanelID',
			mainTabPanelId: 'AccountUserRoleTbsPanelId',
			subGridPanelId : 'AccountUserRoleSubGridPanelID',
			urlList: __ctxPath + 'admin/user/list',
			urlSave: __ctxPath + 'admin/roleassign/save',
			urlSubList: __ctxPath + 'admin/roleassign/listrole',
			urlRoleList: __ctxPath + 'admin/role/list',
			urlGet: __ctxPath + 'admin/roleassign/get',
			refresh: true,
			save: true,
			saveFun: this.saveFun
		};
		this.initUIComponents(conf);
		AccountUserRoleView.superclass.constructor.call(this, {
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
			title: _lang.AccountUserRole.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
			fields: [ 'id','account','department.cnName','department.enName', 'departmentId','cnName','enName', 'email', 'gender','joinusAt', 'createdAt', 'status'],
			width: '75%',
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
			    { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.AccountUser.fJoinusAt, dataIndex: 'joinusAt', width: 140 }
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});
		
		//
		this.formPanel = new HP.GridPanel({
			id: conf.mainFormPanelId,
			title: _lang.AccountUserRole.tabFormTitle,
			region: 'center',
			scope: this,
			multiSelect:true,
			url: conf.urlRoleList,
			fields: [ 'id','cnName','enName', 'createdAt', 'status', 'sort'],
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
				{ header: _lang.AccountResource.fCnName, dataIndex: 'cnName', hidden:curUserInfo.lang!='zh_CN' ? true: false},
				{ header: _lang.AccountResource.fEnName, dataIndex: 'enName', hidden:curUserInfo.lang!='en_AU' ? true: false},
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
				},
				{ header: _lang.TText.fSort, dataIndex: 'sort', width: 40, hidden:true }
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
		$HpStore({
			url : conf.urlSubList,
			fields: ['id', 'roleId'],
			baseParams : { userId : record.data.id, limit:0},
			callback: function(pt, records, eOpts){
				var cmpPanel = Ext.getCmp(conf.mainFormPanelId);
				var seModel = cmpPanel.getSelectionModel();
				var orows = cmpPanel.getStore().data.items;
				for(var i=0; i<orows.length; i++){
					seModel.deselect(orows[i]);
					if(records.length>0){
						for(var j=0; j<records.length; j++){
							if(orows[i].data.id == records[j].data.roleId){
								seModel.select(orows[i], true);
							}
						}
					}
				}
				
			}
		});
	},
	saveFun: function(){
		var userIds = $getGdSelectedIds({grid: Ext.getCmp(this.mainGridPanelId)});
		var roleIds = $getGdSelectedIds({grid: Ext.getCmp(this.mainFormPanelId)});
		
		if(userIds.length<1) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
			return;
		};
		
		$postUrl({
			url : this.urlSave,
			params: {'userId':userIds, 'roleIds': roleIds},
			scope: this, grid: Ext.getCmp(this.mainFormPanelId)
		});
	}
});