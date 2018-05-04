MessageForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.applyIf(this, _cfg);

        this.isApprove = this.action != 'add' && this.action != 'copy' ? true: false;
        
		var conf = {
			title : _lang.Message.mTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
			winId : 'MessageFormWinID',
			moduleName: 'Message',
			frameId : 'MessageView',
			mainGridPanelId : 'MessageGridPanelID',
			mainFormPanelId : 'MessageFormPanelID',
			mainViewPanelId : 'MessageViewPanelID',
			searchFormPanelId: 'MessageSearchPanelID',
			subFormPanelId : 'MessageViewSubFormPanelID',
			urlSave: __ctxPath + 'message/save',
			urlGet: __ctxPath + 'message/get',
			actionName: this.action,
			sendTo: true,
            close: true
		};
		
		this.initUIComponents(conf);
		MessageForm.superclass.constructor.call(this, {
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
			    { field: 'main.toUserId', xtype: 'hidden', allowBlank: false},
			    { field : curUserInfo.lang =='zh_CN'? 'main.toUserCnName':'main.toUserEnName', xtype : 'UserDialog', fieldLabel : _lang.Message.fToUser,
					formId : conf.mainFormPanelId, hiddenName : 'main.toUserId', allowBlank : false
				},
				{ field: 'main.title', xtype: 'textfield', fieldLabel: _lang.Message.fTitle, allowBlank : false},
				{ field: 'main.content', xtype: 'htmleditor', fieldLabel: _lang.Message.fContent, allowBlank : false, height: 420},
				{ field: 'main.fromUserId', xtype: 'hidden', value: curUserInfo.id}
			]
		});
		
		// 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			this.editFormPanel.loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true
			})
        }

	}// end of the initcomponents
    
});