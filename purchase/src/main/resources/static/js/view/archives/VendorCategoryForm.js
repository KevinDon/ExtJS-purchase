VendorCategoryForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.applyIf(this, _cfg);
		var conf = {
			winId : 'VendorCategoryFormWinID',
			moduleName: 'VendorCategory',
			title : _lang.VendorCategory.mTitle,
			viewPanelId : 'VendorCategoryViewPanelID',
			treePanelId : 'VendorCategoryViewTreePanelId',
			formPanelId : 'VendorCategoryViewFormPanelId',
			urlList: __ctxPath + 'archives/vendor-category/list',
			urlSave: __ctxPath + 'archives/vendor-category/save',
			urlDelete: __ctxPath + 'archives/vendor-category/delete',
			urlGet: __ctxPath + 'archives/vendor-category/get',
			actionName: this.action,
			treeRefresh: true,
			treeSave: true,
			cancel: true
		};
		
		this.initUIComponents(conf);
		VendorCategoryForm.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.VendorCategory.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
			tbar: Ext.create("App.toolbar", conf),
			width : 500, height : 350,
			items : this.formPanel
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.formPanel = new HP.FormPanel({
			id : conf.formPanelId,
			fieldItems : [
			    { field: 'id',	xtype: 'hidden', value : this.id == null ? '' : this.id},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.TText.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.TText.fEnName, allowBlank: false},
				{ field: 'main.status', xtype: 'radiogroup', fieldLabel: _lang.TText.fStatus, value: '2',
					data: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
				},
				{ field: 'main.leaf', xtype: 'radiogroup', fieldLabel : _lang.TText.fLeaf, value: '1',
					data: [['0', _lang.TText.vNo], ['1',  _lang.TText.vYes]]
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