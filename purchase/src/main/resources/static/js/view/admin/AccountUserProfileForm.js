Ext.ns('App.AccountUserProfileForm');
AccountUserProfileForm = Ext.extend(Ext.Panel, {
	constructor : function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			winId : 'AccountUserProfileForm',
			moduleName: 'User',
			title : _lang.AccountUser.tabProfileTitle,
			mainFormPanelId : 'AccountUserProfileFormMainFormPanelId',
			urlSave: __ctxPath + 'admin/user/setting',
			urlGet: __ctxPath + 'admin/user/get',
			save: true,
            saveAbs: true,
			close: true,
			saveFun: this.saveFun
		};
	
		this.initUIComponents(conf);
		AccountUserProfileForm.superclass.constructor.call(this, {
			id: conf.winId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
			items: [ this.formPanel]
		});

	},// end of the constructor

	initUIComponents : function(conf) {
		this.formPanel = new HP.FormPanel({
			id : conf.mainFormPanelId,
			region: 'center',
			scope: this,
			fieldItems: [
			    { xtype: 'section', title:_lang.AccountUser.tabSectionAccount},
			    { field: 'id',	xtype: 'hidden'},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.account', xtype: 'displayfield', fieldLabel: _lang.TText.fAccount, cls:'col-2'},
				    { field: 'main.department.title', xtype: 'displayfield', fieldLabel: _lang.AccountUser.fDepartmentName, cls:'col-2'}
			    ]},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.AccountUser.fCnName, allowBlank: false, cls:'col-2'},
				    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.AccountUser.fEnName, allowBlank: false, cls:'col-2'}
			    ]},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.gender', xtype: 'combo', fieldLabel: _lang.AccountUser.fGender, allowBlank: false, value:'1', cls:'col-2',
				    	store: [['1', _lang.TText.vMale],['2', _lang.TText.vFemale]]
				    },
				    { field: 'main.joinusAt', xtype: 'displayfield', fieldLabel: _lang.AccountUser.fJoinusAt, format: curUserInfo.dateFormat, cls:'col-2'}
			    ]},
			    
			    { xtype: 'section', title:_lang.AccountUser.tabSectionContact},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.qq', xtype: 'textfield', fieldLabel: _lang.AccountUser.fQq, cls:'col-2'},
				    { field: 'main.wechat', xtype: 'textfield', fieldLabel: _lang.AccountUser.fWechat, cls:'col-2'}
			    ]},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.skype', xtype: 'textfield', fieldLabel: _lang.AccountUser.fSkype, cls:'col-2'},
				    { field: 'main.email', xtype: 'textfield', vtype:'email', fieldLabel: _lang.TText.fEmail, allowBlank: false, cls:'col-2'}
			    ]},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.phone', xtype: 'textfield', fieldLabel: _lang.AccountUser.fPhone, allowBlank: false, cls:'col-2'},
				    { field: 'main.extension', xtype: 'textfield', fieldLabel: _lang.AccountUser.fExtension, cls:'col-2'}
			    ]},
			    
			    { xtype: 'section', title:_lang.AccountUser.tabSectionSetting},
			    { xtype: 'container', cls: 'row', items: [
				    { field: 'main.setListRows', xtype: 'combo', fieldLabel: _lang.AccountUser.fSetListRows, value:50, cls:'col-2',
						store:[[30,30],[50,50],[80,80],[100,100],[150,150],[200,200]]
					},
				    { field: 'main.lang', xtype: 'combo', fieldLabel: _lang.TText.fLang, allowBlank: false, value:'zh_CN', cls:'col-2',
				    	store: [['zh_CN', _lang.TText.zh_CN], ['en_AU', _lang.TText.en_AU]]
				    }
				]},
				{ xtype: 'container', cls: 'row', items: [
				    { field: 'main.timezone', xtype: 'combo', fieldLabel: _lang.TText.fTimezone, allowBlank: false, value:'GMT+8', cls:'col-2',
				    	store: [['GMT+8', _lang.TText.vTimezone_8], ['GMT+10', _lang.TText.vTimezone_10]]
				    },
				    { field: 'main.dateFormat', xtype: 'combo', fieldLabel: _lang.TText.fDateFormat, allowBlank: false, value:_lang.TText.vDateFormat_c, cls:'col-2',
				    	store: [[_lang.TText.vDateFormat_c, _lang.TText.vDateFormat_c], [_lang.TText.vDateFormat_e, _lang.TText.vDateFormat_e]]
				    }
			    ]},
			    
			    { xtype: 'section', title:_lang.AccountUser.tabSectionPassword},
			    { field: 'main.password', xtype: 'hidden'},
			    { field: 'main.salt', xtype: 'hidden'},
			    { xtype: 'container', cls: 'row', items: [
			        { field: 'main.plainPassword', xtype: 'textfield', fieldLabel: _lang.TText.fPassword, cls:'col-2'}
			    ]}
			]
		});

		// 加载表单对应的数据
		if (!Ext.isEmpty(curUserInfo.id)) {
			this.formPanel.loadData({
				url : conf.urlGet + '?id=' + curUserInfo.id, root : 'data',preName : 'main'
			});
		}
	},// end of the initcomponents

	saveFun : function() {
		$postForm({
			formPanel : Ext.getCmp(this.mainFormPanelId),
			scope : this,
			url : this.urlSave,
			params: {act:'save'},
			callback : function(fp, action) {
				if (this.callback) {
					this.callback.call(this.scope);
				}
			}
		});
	}// end of save
    
});