MessageView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Message.mTitle,
			moduleName: 'Message',
			frameId : 'MessageView',
			mainGridPanelId : 'MessageGridPanelID',
			mainFormPanelId : 'MessageFormPanelID',
			mainViewPanelId : 'MessageViewPanelID',
			searchFormPanelId: 'MessageSearchPanelID',
			urlList: __ctxPath + 'message/list',
			urlSave: __ctxPath + 'message/save',
			urlDelete: __ctxPath + 'message/delete',
			urlGet: __ctxPath + 'message/get',
			refresh: true,
			add: true,
			copy: true,
            delSel: true,
			editFun: this.editRow,
            delSelFun: this.delSelFun
		};
		this.initUIComponents(conf);
		MessageView.superclass.constructor.call(this, {
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
				{field:'Q-read-N-EQ', xtype:'combo', title:_lang.Message.fRead, value:'',
			    	store: [['', _lang.TText.vAll], ['1',  _lang.TText.vUnread], ['2', _lang.TText.vRead]]
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['1',  _lang.TText.vEnabled], ['2', _lang.TText.vDisabled], ['3', _lang.TText.vDeleted]]
			    },
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Message.fTitle},
			    {field:'Q-toUserCnName-S-LK', xtype:'textfield', title:_lang.TText.fToUserCnName},
			    {field:'Q-toUserEnName-S-LK', xtype:'textfield', title:_lang.TText.fToUserEnName},
			    {field:'Q-toDepartmentId-S-EQ', xtype:'hidden'},
			    {field:'toDepartmentName', xtype:'DepDialog', title:_lang.Message.fToDepartment,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-toDepartmentId-S-EQ', single: true
			    }, 
			    {field:'Q-fromUserCnName-S-LK', xtype:'textfield', title:_lang.TText.fFromUserCnName},
			    {field:'Q-fromUserEnName-S-LK', xtype:'textfield', title:_lang.TText.fFromUserEnName},
			    {field:'Q-fromDepartmentId-S-EQ', xtype:'hidden'},
			    {field:'fromDepartmentName', xtype:'DepDialog', title:_lang.Message.fSendDepartment,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-fromDepartmentId-S-EQ', single: true
			    },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.Message.fSendTime, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Message.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
            multiSelect: true,
			url: conf.urlList,
			fields: [ 'id','status', 'read', 'title', 'toUserId', 'toUserCnName', 'toUserEnName', 'fromUserId', 'fromUserCnName', 'fromUserEnName',
			          'toDepartmentId', 'toDepartmentCnName', 'toDepartmentEnName', 'fromDepartmentId', 'fromDepartmentCnName', 'fromDepartmentEnName', 'createdAt'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
			    { header: _lang.Message.fRead, dataIndex: 'read', width: 40,
					renderer: function(value){
						if(value == 1) return $renderOutputColor('gray', _lang.TText.vUnread);
						if(value == 2) return $renderOutputColor('green', _lang.TText.vRead);
					}
			    },
			    { header: _lang.Message.fSendTime, dataIndex: 'createdAt', width: 140 },
			    { header: _lang.Message.fTitle, dataIndex: 'title', width: 200 },
			    { header: _lang.Message.fSendUserId, dataIndex: 'fromUserId', width: 80, hidden:true },
			    { header: _lang.Message.fSendUser, dataIndex: 'fromUserCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fSendUser, dataIndex: 'fromUserEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Message.fSendDepartmentId, dataIndex: 'fromDepartmentId', width: 80, hidden:true },
			    { header: _lang.Message.fSendDepartment, dataIndex: 'fromDepartmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fSendDepartment, dataIndex: 'fromDepartmentEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Message.fToUserId, dataIndex: 'toUserId', width: 80, hidden:true },
			    { header: _lang.Message.fToUser, dataIndex: 'toUserCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fToUser, dataIndex: 'toUserEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Message.fToDepartmentId, dataIndex: 'toDepartmentId', width: 80, hidden:true },
			    { header: _lang.Message.fToDepartment, dataIndex: 'toDepartmentCnName', width:100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fToDepartment, dataIndex: 'toDepartmentEnName', width:120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('red', _lang.TText.vDisabled);
						if(value == '3') return $renderOutputColor('gray', _lang.TText.vDeleted);
					}
			    }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		this.viewPanel = new HP.FormPanel({
			id: conf.mainViewPanelId,
			title: _lang.Message.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'main.title', xtype: 'displayfield', fieldLabel: _lang.Message.fTitle},
			    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.Message.fSendTime},
			    { field: curUserInfo.lang =='zh_CN'? 'main.fromUserCnName':'main.fromUserEnName', xtype: 'displayfield', fieldLabel: _lang.Message.fSendUser},
			    { field: curUserInfo.lang =='zh_CN'? 'main.fromDepartmentCnName': 'main.fromDepartmentEnName', xtype: 'displayfield', fieldLabel: _lang.Message.fSendDepartment},
			    { field : curUserInfo.lang =='zh_CN'? 'main.toUserCnName':'main.toUserEnName', xtype : 'displayfield', fieldLabel : _lang.Message.fToUser },
			    { field : curUserInfo.lang =='zh_CN'? 'main.toDepartmentCnName':'main.toDepartmentEnName', xtype : 'displayfield', fieldLabel : _lang.Message.fToDepartment },
				{ field: 'main.content', xtype: 'displayfield', fieldLabel: _lang.Message.fContent},
			]
		});
		
		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.viewPanel]
		});
				
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
	    var me = this;
        this.viewPanel.form.reset();
		this.viewPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id, root: 'data', preName : 'main',
            success:function(response, options){
                //改变阅读状态
                if(record.data.read == 1 && record.data.toUserId == curUserInfo.id ) {
                    me.gridPanel.getStore().getAt(index).set('read', 2);
                    curUserInfo.countMessageNew>0 ? curUserInfo.countMessageNew = curUserInfo.countMessageNew-1: 0 ;
                    App.iniDesktop.call(this, curUserInfo, true, true);
                }
            }
		});
	},
	
	editRow: function(conf){
		new MessageForm(conf).show();
	},

    delSelFun: function (conf) {
        var cmpPanel = Ext.getCmp(this.mainGridPanelId);
        $delGridRs({
            url: this.urlDelete, scope: this, grid: cmpPanel, idName: 'id',
            callback : function(fp, action) {
                this.refreshRs.call(this);
                App.timerRun(true);
            }
        });
    }
});