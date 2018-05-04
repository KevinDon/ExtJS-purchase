ProductSpecialityNotifiedView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ProductSpecialityNotified.tabListTitle,
			moduleName: 'MessageSku',
			frameId : 'ProductSpecialityNotifiedView',
			mainGridPanelId : 'ProductSpecialityNotifiedGridPanelID',
			mainFormPanelId : 'ProductSpecialityNotifiedFormPanelID',
			mainViewPanelId : 'ProductSpecialityNotifiedViewPanelID',
			searchFormPanelId: 'ProductSpecialityNotifiedSearchPanelID',
			urlList: __ctxPath + 'messagesku/list',
			urlSave: __ctxPath + 'messagesku/save',
			urlDelete: __ctxPath + 'messagesku/delete',
			urlGet: __ctxPath + 'messagesku/get',
			refresh: true,
			add: true,
			copy: true,
            delSel: true,
			editFun: this.editRow
		};
		this.initUIComponents(conf);
		ProductSpecialityNotifiedView.superclass.constructor.call(this, {
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
			    	store: [['', _lang.TText.vAll], ['1',  _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    }, 
			    {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductSpecialityNotified.fSku},
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.Message.fTitle},
			    {field:'Q-toUserCnName-S-LK', xtype:'textfield', title:_lang.TText.fToUserCnName},
			    {field:'Q-toUserEnName-S-LK', xtype:'textfield', title:_lang.TText.fToUserEnName},
			    {field:'Q-toDepartmentId-S-EQ', xtype:'hidden'},
			    {field:'toDepartmentName', xtype:'DepDialog', title:_lang.Message.fToDepartment,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-toDepartmentId-S-EQ', single: true
			    }, 
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.ProductSpecialityNotified.fCreatorCnName},
			    {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.ProductSpecialityNotified.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title:_lang.Message.fSendDepartment,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-reportTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Reports.fReportTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.ProductSpecialityNotified.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
            multiSelect: true,
			url: conf.urlList,
			fields: [ 'id','status', 'read', 'title', 'sku', 'toUserId', 'toUserCnName', 'toUserEnName', 'toDepartmentId', 'toDepartmentCnName', 'toDepartmentEnName', 
			          'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName', 'createdAt'
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
			    { header: _lang.ProductSpecialityNotified.fNoticeTime, dataIndex: 'createdAt', width: 140 },
			    { header: _lang.ProductSpecialityNotified.fCreatorId, dataIndex: 'creatorId', width: 80, hidden: true},
			    { header: _lang.ProductSpecialityNotified.fCreatorName, dataIndex: 'creatorCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.ProductSpecialityNotified.fCreatorName, dataIndex: 'creatorEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Message.fSendDepartmentId, dataIndex: 'departmentId', width: 80, hidden:true },
			    { header: _lang.Message.fSendDepartment, dataIndex: 'departmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fSendDepartment, dataIndex: 'departmentEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.ProductSpecialityNotified.fToUserId, dataIndex: 'toUserId', width: 80, hidden: true},
			    { header: _lang.ProductSpecialityNotified.fToUserName, dataIndex: 'toUserCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.ProductSpecialityNotified.fToUserName, dataIndex: 'toUserEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Message.fToDepartmentId, dataIndex: 'toDepartmentId', width: 80, hidden:true },
			    { header: _lang.Message.fToDepartment, dataIndex: 'toDepartmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.Message.fToDepartment, dataIndex: 'toDepartmentEnName', width: 120, hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.ProductSpecialityNotified.fTitle, dataIndex: 'title', width: 180 },
			    { header: _lang.ProductSpecialityNotified.fSku, dataIndex: 'sku', width: 180 },
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
		
		//
		this.viewPanel = new HP.FormPanel({
			id: conf.mainViewPanelId,
			title: _lang.ProductSpecialityNotified.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
				{ field: 'id',	xtype: 'hidden'},
			    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.ProductSpecialityNotified.fNoticeTime},
			    { field: 'main.updatedAt', xtype: 'displayfield', fieldLabel: _lang.ProductSpecialityNotified.fReadTime},
			    { field: curUserInfo.lang =='zh_CN'? 'main.creatorCnName':'main.creatorUserEnName', xtype: 'displayfield', fieldLabel: _lang.ProductSpecialityNotified.fCreatorName},
			    { field: curUserInfo.lang =='zh_CN'? 'main.departmentCnName': 'main.departmentEnName', xtype: 'displayfield', fieldLabel: _lang.Message.fSendDepartment},
			    { field: curUserInfo.lang =='zh_CN'? 'main.toUserCnName':'main.toUserEnName', xtype : 'displayfield', fieldLabel : _lang.Message.fToUser },
			    { field: curUserInfo.lang =='zh_CN'? 'main.toDepartmentCnName':'main.toDepartmentEnName', xtype : 'displayfield', fieldLabel : _lang.Message.fToDepartment },
			    { field: 'main.title', xtype: 'displayfield', fieldLabel: _lang.ProductSpecialityNotified.fTitle},
			    { field: 'main.sku', xtype : 'displayfield', fieldLabel : _lang.ProductSpecialityNotified.fSku},
			    { field: 'main.content', xtype: 'displayfield', fieldLabel: _lang.ProductSpecialityNotified.fContent},
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
			url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main',
            success:function(response, options){
                var json = Ext.JSON.decode(response.responseText);
                this.getCmpByName('main.updatedAt').setValue(json.data.updatedAt ? json.data.updatedAt : '-');

                //改变阅读状态
                if(record.data.read == 1 && record.data.toUserId == curUserInfo.id ) {
                    me.gridPanel.getStore().getAt(index).set('read', 2);
                }
            }
		});
	},

	editRow: function(conf){
		new ProductSpecialityNotifiedForm(conf).show();
	}
});