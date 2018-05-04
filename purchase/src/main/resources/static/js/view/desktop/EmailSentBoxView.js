EmailSentBoxView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Email.tabListSentBoxTitle,
			moduleName: 'Email',
			frameId : 'EmailSentBoxView',
			mainGridPanelId : 'EmailSentBoxGridPanelID',
			mainFormPanelId : 'EmailSentBoxFormPanelID',
			searchFormPanelId: 'EmailSentBoxSearchFormPanelID',
			mainTabPanelId: 'EmailSentBoxTabsPanelId',
            urlList: __ctxPath + 'desktop/email/list?box=out',
            urlDelete: __ctxPath + 'desktop/email/delete?box=out',
            urlGet: __ctxPath + 'desktop/email/get',
			refresh: true,
			add: true,
            forward: true,
			delSel: true,
			editFun: this.editRow
		};
		this.initUIComponents(conf);

		EmailSentBoxView.superclass.constructor.call(this, {
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
			fieldItems:[
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Email.fTitle},
			    {field:'Q-recipientEmail-S-LK', xtype:'textfield', title:_lang.Email.fRecipientPerson},
			    {field:'Q-ccEmail-S-LK', xtype:'textfield', title:_lang.Email.fCopyPerson},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Email.tabListSentBoxTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
            selType: 'cellmodel',
            selMode: 'MULTI',
            multiSelect:true,
            forceFit: false,
            sorters: [{property: 'sendTime', direction: 'DESC'},{property: 'isRead', direction: 'DESC'},{property: 'createdAt', direction: 'DESC'}],
            fields: [ 'id','isHtml','attachmentCount','recipientName', 'recipientEmail', 'ccName', 'ccEmail', 'bccName', 'bccEmail', 'title', 'sendTime',
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
                { header: _lang.Email.fRecipientPerson, dataIndex: 'recipientName', width: 100, hidden:true },
                { header: _lang.Email.fRecipientPerson, dataIndex: 'recipientEmail', width: 200 },
                { header: _lang.Email.fCopyPerson, dataIndex: 'ccName', width: 100, hidden:true },
                { header: _lang.Email.fCopyPerson, dataIndex: 'ccEmail', width: 200 },
                { header: _lang.Email.fSendTime, dataIndex: 'sendTime', width: 140 },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 },
            ],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//内容form
		this.viewPanel = new HP.FormPanel({
			id: conf.mainviewPanelId,
			region: 'center',
			title: _lang.Email.tabFormTitle,
			scope: this,
			fieldItems: [
                { field: 'main.id',	xtype: 'hidden'},
                { field: 'main.recipientEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fRecipientPerson},
                { field: 'main.ccEmail', xtype: 'displayfield', fieldLabel: _lang.Email.fCopyPerson},
                { field: 'main.sendTime', xtype: 'displayfield', fieldLabel: _lang.TText.fSendTime},
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
			root : 'data', preName : 'main', maskTo: conf.mainFormPanelId + '-east',
			success:function(response, options){
				if(me.viewPanel == undefined) return;
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
    }
});