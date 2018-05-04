EmailInboxView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Email.tabListInboxTitle,
			moduleName: 'Email',
			frameId : 'EmailInboxView',
			mainGridPanelId : 'EmailInboxGridPanelID',
			mainFormPanelId : 'EmailInboxFormPanelID',
			searchFormPanelId: 'EmailInboxSearchFormPanelID',
			mainTabPanelId: 'EmailInboxTabsPanelId',
            urlList: __ctxPath + 'desktop/email/list?box=in',
            urlDelete: __ctxPath + 'desktop/email/delete?box=in',
            urlGet: __ctxPath + 'desktop/email/get',
			refresh: true,
            receive: true,
            copy: true,
            reply: true,
            replyall: true,
            forward: true,
            delSel: true,
            editFun: this.editRow,
            receiveFun: this.receiveFun
		};
		this.initUIComponents(conf);
		
		EmailInboxView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: this.toolbars,
			items: [ this.searchPanel, this.centerPanel ],
            listeners:{
                afterrender: function( eOpts ){
                    var me = this;
                    var config = conf;
                    //接收邮箱链接
                    var store = $HpStore({
                        url:__ctxPath + 'desktop/emailsetting/list?mini=1',
                        fields: ['id','title', 'isDefault'], scope: this,
                        callback: function (conf, records, eOpts) {
                            var toolbar = Ext.getCmp(config.mainGridPanelId + '-toolbar');
                            if(!!records && records.length>0) {
                                var menuFile = new Ext.menu.Menu();
                                for(var index in records) {
                                    menuFile.add({
                                        text: records[index].data.title, iconCls: 'fa fa-circle', sid:records[index].data.id,
                                        handler: function (){conf.scope.receiveForBox.call(config, this);}
                                    });
                                }
                                menuFile.add({text: _lang.TText.vAll + _lang.TText.fEmail, iconCls: 'fa fa-circle', handler: function (){conf.scope.receiveForBox.call(config, this);}});
                                for (var index in toolbar.items.items) {
                                    var item = toolbar.items.items[index];
                                    if (item.iconCls == 'fa fa-fw fa-cloud-download') {
                                        item.menu = menuFile;
                                        break;
                                    }
                                }
                            }
                        }
                    })
                }
            }
		});
	},
	
	initUIComponents: function(conf) {
	    //接收按钮
        conf.toolbarId = conf.mainGridPanelId + '-toolbar';
        this.toolbars = Ext.create("App.toolbar", conf);

        this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
				{field:'Q-isRead-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vUnread], ['1', _lang.TText.vRead]]
			    },
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Email.fTitle},
			    {field:'Q-sendName-S-LK', xtype:'textfield', title:_lang.Email.fSendPerson},
			    {field:'Q-sendEmail-S-LK', xtype:'textfield', title:_lang.Email.fSendEmail},
		/*		{field:'Q-sendEmailId-N-EQ', xtype:'hidden'},
			    {field:'sendEmail', xtype:'ContactsDialog', title:_lang.Email.fSendEmail,
                    formId:conf.searchFormPanelId, hiddenName:'Q-sendEmailId-S-EQ', single: true
				},
				{field:'Q-recipientPerson-S-LK', xtype:'textfield', title:_lang.Email.fRecipientPerson},*/
				{field:'Q-recipientEmail-S-LK', xtype:'textfield', title:_lang.Email.fRecipientEmail},
				 { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Email.tabListInboxTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
            selType: 'cellmodel',
			selMode: 'MULTI',
            multiSelect:true,
            forceFit: false,
            sorters: [{property: 'sendTime', direction: 'DESC'},{property: 'isRead', direction: 'DESC'},{property: 'createdAt', direction: 'DESC'}],
            fields: [ 'id','isRead','isReplied','isHtml','attachmentCount','sendName','sendEmail','sendTime','recipientName', 'recipientEmail', 'ccName', 'ccEmail', 'title',
                'createdAt', 'updatedAt', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'
            ],
			width: '45%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.Email.fAttachmentCount, dataIndex: 'attachmentCount', width: 30,
                    renderer: function(value, rowObj, row, rowIndex, colIndex) {
                        if (value != undefined && value >0) {
                            return $renderOutputColor('blue', '<i class="fa fa-paperclip" aria-hidden="true">' + value + '</i>');
                        } else {
                            return $renderOutputColor('gary', _lang.TText.vNo );
                        }
                    }
				},
			    { header: _lang.TText.fRead, dataIndex: 'isRead', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vRead);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vUnread);
					}
			    },
                { header: _lang.Email.fTitle, dataIndex: 'title', width: 350,
                    renderer: function(value, rowObj, row, rowIndex, colIndex) {
                        if (!!value) {
                            if(row.get('isRead') == '2') return $renderOutputColor('blue', value);
                            else return value;
                        }else{
                            return $renderOutputColor('gray', _lang.Email.vNoTitle);
                        }
                    }
				},
			    { header: _lang.Email.fSendPerson, dataIndex: 'sendName', width: 100 },
			    { header: _lang.Email.fSendEmail, dataIndex: 'sendEmail', width: 200 },
                { header: _lang.Email.fSendTime, dataIndex: 'sendTime', width: 140 },
                { header: _lang.Email.fRecipientPerson, dataIndex: 'recipientName', width: 100, hidden:true },
                { header: _lang.Email.fRecipientEmail, dataIndex: 'recipientEmail', width: 200 },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 },
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//内容form
		this.viewPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			region: 'center',
			title: _lang.Email.tabFormTitle,
			scope: this,
			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.sendEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fSendPerson},
                { field: 'main.recipientEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fRecipientPerson},
			    { field: 'main.ccEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fCopyPerson},
	            { field: 'main.sendTime', xtype: 'displayfield', fieldLabel: _lang.Email.fSendTime},
                { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt},
                { field: 'main.title', xtype: 'displayfield', fieldLabel: _lang.Email.fTitle},
                { xtype: 'section'},
                { field: 'main.content', xtype: 'displayfield'},
			]
		});

        var emailRelatedTabs = Ext.create ('App.EmailRelatedTabs', { conf: conf });

        this.eastPanel = new Ext.Panel({
            id: conf.mainFormPanelId + '-east',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.viewPanel, emailRelatedTabs]
        });

		//主体内容
		this.centerPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-top',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.eastPanel]
		});

	},// end of the init
		
	rowClick: function(record, item, index, e, conf) {
		var me = this;
        this.viewPanel.form.reset();
		this.viewPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main',  maskTo: conf.mainFormPanelId+ '-east',
			success:function(response, options){
				if(me.viewPanel == undefined) return;
				//改变阅读状态
                if(record.data.isRead == '2') {
                    me.gridPanel.getStore().getAt(index).set('isRead', '1');
                    me.gridPanel.getStore().getAt(index).set('title', record.data.title);
                    curUserInfo.countEmailNew = curUserInfo.countEmailNew-1 ;
                    App.iniDesktop.call(this, curUserInfo, true, true)
                }

                if(response.responseText){
                    var json = Ext.JSON.decode(response.responseText);
                    if(json.success == true){
                        //attachment init
                        Ext.getCmp(conf.mainTabPanelId + '-attachments-f').setValue(json.data.attachments);
                    }
                }
			}
		});
	},
    editRow: function(conf){
        new EmailSendDialogWin(conf).show();
    },
    receiveFun: function (conf) {
        return;
    },
    receiveForBox: function (eOpts) {
        var me = this;
        var tpData = $postUrl({
            url: __ctxPath + 'desktop/email/receive',
            params: !!eOpts.sid? {sid:eOpts.sid}: {},
            scope:this,
            maskTo: this.frameId,
            grid: Ext.getCmp(this.mainGridPanelId)
        })
    }
});