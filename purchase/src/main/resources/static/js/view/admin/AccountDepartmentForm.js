AccountDepartmentForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.applyIf(this, _cfg);
		var conf = {
			winId : 'AccountDepartmentFormWinID',
			moduleName: 'Department',
			title : _lang.AccountDepartment.mTitle,
			viewPanelId : 'AccountDepartmentViewPanelID',
			treePanelId : 'AccountDepartmentViewTreePanelId',
			formPanelId : 'AccountDepartmentViewFormPanelId',
			urlList: __ctxPath + 'dep/list',
			urlSave: __ctxPath + 'dep/save',
			urlDelete: __ctxPath + 'dep/delete',
			urlGet: __ctxPath + 'dep/get',
			treeRefresh: true,
			treeSave: true,
			cancel: true,
			reset: true
		};
		
		this.initUIComponents(conf);
		AccountDepartmentForm.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.AccountDepartment.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
			tbar: Ext.create("App.toolbar", conf),
			width : 500, height : 350,
			items : this.formPanel
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.formPanel = new HP.FormPanel({
			id : conf.formPanelId,
			fieldItems : [
			    { field: 'act',	xtype: 'hidden', value : this.action},
			    { field: 'id',	xtype: 'hidden', value : this.id == null ? '' : this.id},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fEnName, allowBlank: false},
				{ field: 'main.status', xtype: 'radiogroup', fieldLabel: _lang.TText.fStatus, value: '2', 
					data: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
				},
				{ field: 'main.leaf', xtype: 'radiogroup', fieldLabel : _lang.AccountResource.fLeaf, value: '1',
					data: [['0', _lang.TText.vNo], ['1', _lang.TText.vYes]]
				}
			]
		});
		// 加载表单对应的数据
		if (!Ext.isEmpty(this.record.id)) {
			this.formPanel.loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, scope: this
			});
		}

	}// end of the initcomponents
    
});