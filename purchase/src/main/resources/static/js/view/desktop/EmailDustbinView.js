EmailDustbinView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Email.tabListDustbinTitle,
			moduleName: 'Email',
			frameId : 'EmailDustbinView',
			mainGridPanelId : 'EmailDustbinGridPanelID',
			mainFormPanelId : 'EmailDustbinFormPanelID',
			mainViewPanelId : 'EmailDustbinViewPanelID',
			searchFormPanelId: 'EmailDustbinSearchFormPanelID',
			mainTabPanelId: 'EmailDustbinTabsPanelId',
            urlList: __ctxPath + 'desktop/email/list?box=dust',
            urlDelete: __ctxPath + 'desktop/email/delete?box=dust',
			urlGet: __ctxPath + 'desktop/email/get',
			refresh: true,
			delSel: true
		};
		this.initUIComponents(conf);

        EmailDustbinView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.searchPanel, this.centerPanel ]
		});
	},
	
	initUIComponents: function(conf) {
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			//onlyKeywords:true,
			fieldItems:[
				{field:'Q-isRead-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vUnread], ['1', _lang.TText.vRead]]
			    },
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Email.fTitle},
			    {field:'Q-sendName-S-LK', xtype:'textfield', title:_lang.Email.fSendPerson},
			    {field:'Q-sendEmail-S-LK', xtype:'textfield', title:_lang.Email.fSendEmail},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Email.tabListDustbinTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
            multiSelect:true,
            forceFit: false,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
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
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//内容form
		this.viewPanel = new HP.FormPanel({
			id: conf.mainViewPanelId,
			region: 'center',
			title: _lang.Email.tabFormTitle,
			scope: this,
			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.recipientEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fRecipientPerson},
			    { field: 'main.ccEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fCopyPerson},
			    { field: 'main.sendEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fSendPerson},
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
	}
	
});