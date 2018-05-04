ModulesForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.applyIf(this, _cfg);
		var conf = {
			winId : 'ModulesFormWinID',
			moduleName: 'Modules',
			title : _lang.AccountResource.mTitle,
			viewPanelId : 'ModulesViewPanelID',
			treePanelId : 'ModulesViewTreePanelId',
			formPanelId : 'ModulesViewFormPanelId',
			urlList: __ctxPath + 'modules/list',
			urlSave: __ctxPath + 'modules/save',
			urlDelete: __ctxPath + 'modules/delete',
			treeRefresh: true,
			treeSave: true,
			cancel: true,
			reset: true
		};
		
		this.initUIComponents(conf);
		ModulesForm.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this, autoScroll: false,
			title : _lang.AccountResource.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
			tbar: Ext.create("App.toolbar", conf),
			width : 550, height : 520,
			items : this.formPanel
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.formPanel = new HP.FormPanel({
			id : conf.formPanelId,
			scope: this,
			autoScroll: false,
			fieldItems : [
			    { field: 'act',	xtype: 'hidden', value : this.action},
			    { field: 'id',	xtype: 'hidden', value : this.id == null ? '' : this.id},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.AccountResource.fEnName, allowBlank: false},
			    { field: 'main.itemIcon', xtype: 'textfield', fieldLabel : _lang.AccountResource.fItemIcon},
			    { field: 'main.url', xtype: 'textfield', fieldLabel : _lang.AccountResource.fUrl},
			    { field: 'main.type', xtype: 'radiogroup', fieldLabel: _lang.AccountResource.fType,
					data:[['0', _lang.TText.vNull], ['1',  _lang.AccountResource.vLocalUrl], ['2', _lang.AccountResource.vModuleName]]
				},
				{ field: 'main.status', xtype: 'radiogroup', fieldLabel: _lang.TText.fStatus, value: '2', 
					data: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
				},
				{ field: 'main.leaf', xtype: 'radiogroup', fieldLabel : _lang.AccountResource.fLeaf, value: '1',
					data: [['0', _lang.TText.vNo], ['1', _lang.TText.vYes]]
				},
				{ xtype: 'section', title:_lang.AccountResource.tPermissionFunction},
				{ field: 'main.code', xtype: 'textfield', fieldLabel: _lang.AccountResource.fCode},
				{ field: 'main.function', xtype: 'checkboxremote', fieldLabel: _lang.AccountResource.fFunction, width:'100%', height:250,
					fields: ['id','title','codeMain','codeSub','options', 'type'], conf:conf,
					url:__ctxPath + 'dict/getkey?code=module_action',
				}
			]
		});
		// 加载表单对应的数据
		if (!Ext.isEmpty(this.record.id)) {
		    var me = this;
			this.formPanel.loadData({
				url : __ctxPath + 'modules/get?id=' + this.record.id,
				preName : 'main', loadMask:true, scope: this,
				success:function(response, options){
					if(me.formPanel == undefined) return;
					if(response.responseText){
						var obj = Ext.JSON.decode(response.responseText);
		    			if(obj.success == true){
		    				conf.functions = obj.data.functions;
		    			}
	    			}
				}
			});
		}

	}// end of the initcomponents
    
});