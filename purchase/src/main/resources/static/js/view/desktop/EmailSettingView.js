EmailSettingView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Email.tabListSettingTitle,
			moduleName: 'EmailSetting',
			frameId : 'EmailSettingView',
			mainGridPanelId : 'EmailSettingGridPanelID',
			mainFormPanelId : 'EmailSettingFormPanelID',
			searchFormPanelId: 'EmailSettingSearchPanelID',
			mainTabPanelId: 'EmailSettingTbsPanelId',
			subGridPanelId : 'EmailSettingSubGridPanelID',
			urlList: __ctxPath + 'desktop/emailsetting/list',
			urlSave: __ctxPath + 'desktop/emailsetting/save',
			urlDelete: __ctxPath + 'desktop/emailsetting/delete',
			urlGet: __ctxPath + 'desktop/emailsetting/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		EmailSettingView.superclass.constructor.call(this, {
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
				{field:'Q-shared-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fShared, code:'document', codeSub:'shared', addAll:true},
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    },
			    {field:'Q-type-N-EQ', xtype:'combo', title:_lang.Email.fAccountType, value:'', 
			    	store: [['', _lang.TText.vAll], ['2',  _lang.Email.vEmailImap], ['1', _lang.Email.vEmailPop]]
			    },
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Email.fTitle},
			    {field:'Q-sendName-S-LK', xtype:'textfield', title:_lang.Email.fSendName},
			    {field:'Q-email-S-LK', xtype:'textfield', title:_lang.TText.fEmail},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Email.tabListSettingTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
			url: conf.urlList,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
			fields: [ 'id','title','email','shared','type','status','sendName',
	           'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
			],
			width: '45%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.Email.fTitle, dataIndex: 'title', width: 150 },
				{ header: _lang.TText.fEmail, dataIndex: 'email', width: 200},
				{ header: _lang.Email.fSendName, dataIndex: 'sendName', width: 100 },
			    { header: _lang.Email.fAccountType, dataIndex: 'type', width: 60,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.Email.vEmailPop);
						if(value == '2') return $renderOutputColor('gray', _lang.Email.vEmailImap);
					}
			    },
			    { header: _lang.TText.fShared, dataIndex: 'shared', width: 60 ,
			    	renderer: function(value){
                    	var $shared = _dict.shared;
                        if($shared.length>0 && $shared[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $shared[0].data.options);
                        }
                    }
			    }
			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//
        this.formPanel = new HP.FormPanel({
            id: conf.mainFormPanelId,
			title: _lang.Email.tabListSettingTitle,
            region: 'center',
            scope: this,
            fieldItems: [
                { field: 'id',	xtype: 'hidden'},
                { xtype: 'container', cls:'row', items:  [
					{ xtype: 'section', title:_lang.Email.tabSectionBase},
					{ field: 'main.title', xtype: 'textfield', fieldLabel: _lang.Email.fTitle,allowBlank: false, cls:'col-1'},
                    { field: 'main.email', xtype: 'textfield', fieldLabel: _lang.TText.fEmail, vtype: 'email',allowBlank: false, cls: 'col-2',
                        listeners: {
                            change: function (pt, newValue, oldValue, eOpts) {
                            	if(newValue && newValue.indexOf('@')>0) {
                            		var mail = newValue.substr(newValue.indexOf('@') + 1);
                                    this.up().up().getCmpByName('main.servicePop').setValue('pop.'+ mail);
                                    this.up().up().getCmpByName('main.serviceSmtp').setValue('smtp.'+ mail);
                                    this.up().up().getCmpByName('main.serviceImap').setValue('imap.'+ mail);
                                }else{
                                    this.up().up().getCmpByName('main.servicePop').setValue('');
                                    this.up().up().getCmpByName('main.serviceSmtp').setValue('');
                                    this.up().up().getCmpByName('main.serviceImap').setValue('');
								}
                            }
                        }
                    },
					{ field: 'main.sendName', xtype: 'textfield', fieldLabel: _lang.Email.fSendName, allowBlank: false, cls:'col-2'},
					{ field: 'main.type', xtype: 'combo', fieldLabel: _lang.Email.fAccountType, value: '1', cls:'col-2', hidden:true,
						store: [ ['1', _lang.Email.vEmailPop], ['2', _lang.Email.vEmailImap]],
						listeners:{
							change: function (pt, newValue, oldValue, eOpts ) {
								if(newValue == 2){
									Ext.getCmp('section-pop').hide();
									Ext.getCmp('section-smtp').hide();
									Ext.getCmp('section-imap').show();
								}else {
									Ext.getCmp('section-pop').show();
									Ext.getCmp('section-smtp').show();
									Ext.getCmp('section-imap').hide();
								}
							}
						}
					},
                    { field: 'main.isDefault', xtype: 'combo', fieldLabel: _lang.Email.fIsDefaultMail, value:'2', cls:'col-2',
                        store: [['1', _lang.TText.vYes], ['2', _lang.TText.vNo]]
                    },
				]},

                { xtype: 'container', id: 'section-pop', cls:'row', items:  [
                    { xtype: 'section', title:_lang.Email.tabSectionPop3},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.servicePop', xtype: 'textfield', fieldLabel: _lang.Email.fServicePop, cls:'col-1'},
						{ field: 'main.servicePopPort', xtype: 'textfield', fieldLabel: _lang.Email.fServicePopPort, cls:'col-1', value:'110'},
						{ field: 'main.servicePopSsl', xtype: 'combo', fieldLabel: _lang.Email.fNeedEncryptedCommunication, value:'2', cls:'col-1',
							store: [['1', _lang.TText.vYes], ['2', _lang.TText.vNo]],
                            listeners:{
                                change: function (pt, newValue, oldValue, eOpts ) {
                                	if(newValue == '2'){
                                        this.up().getCmpByName('main.servicePopPort').setValue('110');
									}else if(newValue == '1'){
                                        this.up().getCmpByName('main.servicePopPort').setValue('995');
									}
                                }
                            }
						},
                        {xtype:'button',id:'checkPopId', text: _lang.Email.tCheckServerConn,
                            handler: function() {
                                var tpData = $postUrl({
                                    fields: ['id', 'ports', 'chargeItems', 'bank'],
                                    url: __ctxPath + 'desktop/emailsetting/checkconn?act=pop',
                                    params:{
                                        'main.type': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.type').getValue(),
                                        'main.servicePop': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.servicePop').getValue(),
                                        'main.servicePopPort': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.servicePopPort').getValue(),
                                        'main.servicePopSsl': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.servicePopSsl').getValue(),
                                        'main.popAccount': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.popAccount').getValue(),
                                        'main.popPassword': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.popPassword').getValue(),
                                    },
                                    maskTo: 'checkPopId'
                                })
                            }}
					]},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.popAccount', id: 'main_popAccount', xtype: 'textfield', fieldLabel: _lang.Email.fPopAccount, cls:'col-1'},
						{ field: 'main.popPassword', id: 'main_popPassword', xtype: 'textfield', fieldLabel: _lang.Email.fPopPassword, inputType: 'password', cls:'col-1'},
					]}
				]},

                { xtype: 'container', id: 'section-smtp', cls:'row', items:  [
                    { xtype: 'section', title:_lang.Email.tabSectionSmtp},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.serviceSmtp', xtype: 'textfield', fieldLabel: _lang.Email.fServiceSmtp, cls:'col-1'},
						{ field: 'main.serviceSmtpPort', xtype: 'textfield', fieldLabel: _lang.Email.fServiceSmtpPort, cls:'col-1', value:'25'},
						{ field: 'main.serviceSmtpSsl', xtype: 'combo', fieldLabel: _lang.Email.fNeedEncryptedCommunication, value:'2', cls:'col-1',
							store: [['1', _lang.TText.vYes], ['2', _lang.TText.vNo]],
                            listeners:{
                                change: function (pt, newValue, oldValue, eOpts ) {
                                    if(newValue == '2'){
                                        this.up().getCmpByName('main.serviceSmtpPort').setValue('25');
                                    }else if(newValue == '1'){
                                        this.up().getCmpByName('main.serviceSmtpPort').setValue('465');
                                    }
                                }
                            }
						},
						{xtype:'button',id:'checkSmtpId', text: _lang.Email.tCheckServerConn,
                            handler: function() {
                                var tpData = $postUrl({
                                    fields: ['id', 'ports', 'chargeItems', 'bank'],
                                    url: __ctxPath + 'desktop/emailsetting/checkconn?act=smtp',
                                    params:{
										'main.type': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.type').getValue(),
										'main.serviceSmtp': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceSmtp').getValue(),
										'main.serviceSmtpPort': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceSmtpPort').getValue(),
										'main.serviceSmtpSsl': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceSmtpSsl').getValue(),
										'main.smtpAccount': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.smtpAccount').getValue(),
										'main.smtpPassword': Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.smtpPassword').getValue(),
									},
                                    maskTo: 'checkSmtpId'
                                })
                            }}
                    ]},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.smtpAccount', xtype: 'textfield', fieldLabel: _lang.Email.fSmtpAccount, cls:'col-1'},
						{ field: 'main.smtpPassword', xtype: 'textfield', fieldLabel: _lang.Email.fSmtpPassword, inputType: 'password', cls:'col-1'},
						{ field: 'main_alike', xtype:'checkboxfield', fieldLabel: _lang.Email.fAlike, cls:'col-1',
							listeners:{
                                change: function(pt, value){
									if(value){
                                        this.up().getCmpByName('main.smtpAccount').setValue(Ext.getCmp('main_popAccount').getValue());
                                        this.up().getCmpByName('main.smtpPassword').setValue(Ext.getCmp('main_popPassword').getValue());
                                    }
								}
							}
						}
					]}
                ]},

                { xtype: 'container', id: 'section-imap', cls:'row', hidden:true, items:  [
                    { xtype: 'section', title:_lang.Email.tabSectionImap},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.serviceImap', xtype: 'textfield', fieldLabel: _lang.Email.fServiceImap, cls:'col-1'},
						{ field: 'main.serviceImapPort', xtype: 'textfield', fieldLabel: _lang.Email.fServiceImapPort, cls:'col-1', value:'143'},
						{ field: 'main.serviceImapSsl', xtype: 'combo', fieldLabel: _lang.Email.fNeedEncryptedCommunication, value:'2', cls:'col-1',
							store: [['1', _lang.TText.vYes], ['2', _lang.TText.vNo]],
                            listeners:{
                                change: function (pt, newValue, oldValue, eOpts ) {
                                    if(newValue == '2'){
                                        this.up().getCmpByName('main.serviceImapPort').setValue('143');
                                    }else if(newValue == '1'){
                                        this.up().getCmpByName('main.serviceImapPort').setValue('993');
                                    }
                                }
                            }
						}
                    ]},
                    { xtype: 'container', cls:'col-2', items:  [
						{ field: 'main.imapAccount', xtype: 'textfield', fieldLabel: _lang.Email.fImapAccount, cls:'col-1'},
						{ field: 'main.imapPassword', xtype: 'textfield', fieldLabel: _lang.Email.fImapPassword, inputType: 'password', cls:'col-1'},
                    ]}
                ]},

                { xtype: 'section', title:_lang.Email.tabSectionAdvancedSettings},
                { xtype: 'container', cls:'row', items:  [
					{ field: 'main.shared', xtype: 'dictcombo', fieldLabel: _lang.TText.fShared, allowBlank: false, value:'1', code:'document', codeSub:'shared', cls:'col-2'},
                    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, cls:'col-2', value:'1',
                        store: [['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                    },
					{ field : 'main.signature',xtype : 'htmleditor',fieldLabel : _lang.Email.fSignature, height : 230, cls:'col-1', width:'100%' }
                ]}
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
			url : conf.urlGet + '?id=' + record.data.id, root : 'data', preName : 'main', maskTo:conf.mainFormPanelId,
			success:function (response) {
                var json = Ext.JSON.decode(response.responseText);
                this.scope.changeType(json.data.type, this);
            }
		});
	},

	changeType: function(type, eOpts){
        if(type == 1){
            Ext.getCmp('section-pop').show();
            Ext.getCmp('section-smtp').show();
            Ext.getCmp('section-imap').hide();
        }else if(type == 2){
            Ext.getCmp('section-pop').hide();
            Ext.getCmp('section-smtp').hide();
            Ext.getCmp('section-imap').show();
        }
    }
});