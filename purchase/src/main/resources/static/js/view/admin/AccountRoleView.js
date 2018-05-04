AccountRoleView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AccountRole.mTitle,
			moduleName: 'Role',
			frameId : 'AccountRoleView',
			mainGridPanelId : 'AccountRoleGridPanelID',
			mainFormPanelId : 'AccountRoleFormPanelID',
			searchFormPanelId: 'AccountRoleSearchPanelID',
			mainTabPanelId: 'AccountRoleTbsPanelId',
			subGridPanelId : 'AccountRoleSubGridPanelID',
			urlList: __ctxPath + 'admin/role/list',
			urlSave: __ctxPath + 'admin/role/save',
			urlDelete: __ctxPath + 'admin/role/delete',
			urlGet: __ctxPath + 'admin/role/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		AccountRoleView.superclass.constructor.call(this, {
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
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.AccountResource.fCnName},
			    {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.AccountResource.fEnName},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    }
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.AccountRole.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
			fields: [ 'id','cnName','enName', 'code', 'createdAt', 'status', 'sort'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
			    { header: _lang.AccountResource.fCnName, dataIndex: 'cnName', width: 100,hidden:curUserInfo.lang =='zh_CN'? false: true},
			    { header: _lang.AccountResource.fEnName, dataIndex: 'enName', width: 120,hidden:curUserInfo.lang !='zh_CN'? false: true},
			    { header: _lang.AccountRole.fCode, dataIndex: 'code', width: 100},
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.TText.fSort, dataIndex: 'sort', width: 40 }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.AccountRole.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fEnName, allowBlank: false},
			    { field: 'main.code', xtype: 'textfield', fieldLabel: _lang.AccountRole.fCode, allowBlank: false},
			    { field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort, allowBlank: false},
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
                    store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    }
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