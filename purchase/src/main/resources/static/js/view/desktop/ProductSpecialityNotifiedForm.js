ProductSpecialityNotifiedForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.applyIf(this, _cfg);

        this.isApprove = this.action != 'add' && this.action != 'copy' ? true: false;
        
		var conf = {
			title : _lang.Message.mTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
			winId : 'ProductSpecialityNotifiedFormWinID',
			moduleName: 'MessageSku',
			frameId : 'ProductSpecialityNotifiedView',
			mainGridPanelId : 'ProductSpecialityNotifiedGridPanelID',
			mainFormPanelId : 'ProductSpecialityNotifiedFormPanelID',
			mainViewPanelId : 'ProductSpecialityNotifiedViewPanelID',
			searchFormPanelId: 'ProductSpecialityNotifiedSearchPanelID',
			subFormPanelId : 'ProductSpecialityNotifiedViewSubFormPanelID',
			urlSave: __ctxPath + 'messagesku/save',
			urlGet: __ctxPath + 'messagesku/get',
			actionName: this.action,
            sendTo: true,
            close: true
		};
		
		this.initUIComponents(conf);
		ProductSpecialityNotifiedForm.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : conf.title,
			tbar: Ext.create("App.toolbar", conf),
			cls: 'gb-blank',
			width : 800, height : 600,
			items : this.editFormPanel
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.editFormPanel = new HP.FormPanel({
			id : conf.mainFormPanelId,
			region: 'center',
			closeWin: conf.winId,
			fieldItems : [
				{ field: 'id',	xtype: 'hidden'},
				{ field: 'main_sku',	xtype: 'hidden'},
				{ field: 'main.sku', xtype: 'ProductDialog', fieldLabel: _lang.ProductSpecialityNotified.fSku, hiddenName:'main_sku', formId:conf.mainFormPanelId, allowBlank : false},
				{ field: 'main.title', xtype: 'textfield', fieldLabel: _lang.ProductSpecialityNotified.fTitle, allowBlank: false},
				{ field: 'main.content', xtype: 'htmleditor', fieldLabel: _lang.ProductSpecialityNotified.fContent, allowBlank: false, height: 420},
				{ field: 'main.toDepartmentId', xtype: 'hidden', value: curUserInfo.depId},
				{ field: 'main.creatorId', xtype: 'hidden', value: curUserInfo.id}
			]
		});
		
		// 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			this.editFormPanel.loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true
			});
        }

	}// end of the initcomponents
    
});