AutoCodeView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AutoCode.mTitle,
			moduleName: 'AutoCode',
			frameId : 'AutoCodeView',
			mainGridPanelId : 'AutoCodeGridPanelID',
			mainFormPanelId : 'AutoCodeFormPanelID',
			searchFormPanelId: 'AutoCodeSearchPanelID',
			mainTabPanelId: 'AutoCodeTbsPanelId',
			subGridPanelId : 'AutoCodeSubGridPanelID',
			urlList: __ctxPath + 'admin/autocode/list',
			urlSave: __ctxPath + 'admin/autocode/save',
			urlDelete: __ctxPath + 'admin/autocode/delete',
			urlGet: __ctxPath + 'admin/autocode/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		AutoCodeView.superclass.constructor.call(this, {
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
			    {field:'Q-title-S-LK', xtype:'textfield', title:_lang.TText.fTitle},
			    {field:'Q-format-S-LK', xtype:'textfield', title:_lang.TText.fFormat},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    },
                { field: 'Q-updatedAt-D', xtype: 'DateO2TField', fieldLabel: _lang.AutoCode.fUpdateAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.AutoCode.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','title','departmentId', 'departmentCnName', 'departmentEnName','format','mainValue','mainValueCleanRule', 'subValue','subValueCleanRule','updatedAt','groupNo', 'lastValue','sort', 'code','status'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.AutoCode.fTitle, dataIndex: 'title', width: 120},
				{ header: _lang.AutoCode.fCode, dataIndex: 'code', width: 120},
				{ header: _lang.AutoCode.fDepartmentName, dataIndex: 'departmentCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.AutoCode.fDepartmentName, dataIndex: 'departmentEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.AutoCode.fDepartmentId, dataIndex: 'departmentId', width: 80, hidden: true },
			    { header: _lang.AutoCode.fMainValue, dataIndex: 'mainValue', width: 80},
			    { header: _lang.AutoCode.fSubValue, dataIndex: 'subValue', width: 80},
			    { header: _lang.AutoCode.fLastValue, dataIndex: 'lastValue', width: 80},
			    { header: _lang.TText.fSort, dataIndex: 'sort', width: 100 },
			    { header: _lang.AutoCode.fGroupNo, dataIndex: 'groupNo', width: 100 },
			    { header: _lang.TText.fUpdatedAt, dataIndex: 'updatedAt', width: 140 },
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.AutoCode.tabFormTitle,
			region: 'center',
			scope: this,

			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'main.title', xtype: 'textfield', fieldLabel: _lang.AutoCode.fTitle, allowBlank: false},
			    { field: 'main.departmentId', xtype: 'hidden', allowBlank: true},
			    { field:  curUserInfo.lang =='zh_CN'? 'main.departmentCnName': 'main.departmentEnName', xtype: 'DepDialog', allowBlank: false, fieldLabel: _lang.AutoCode.fDepartmentName,
			    	formId:conf.mainFormPanelId, hiddenName:'main.departmentId', single: true, allowBlank:true},
			    { field: 'main.code', xtype: 'textfield', fieldLabel: _lang.AutoCode.fCode, allowBlank: false},
			    { field: 'main.format', xtype: 'textareafield', fieldLabel: _lang.AutoCode.fFormat, allowBlank: false},
                { field: 'main.description', xtype: 'htmleditor', fieldLabel: _lang.AutoCode.fDescription, allowBlank: false},
			    { field: 'main.mainValue', xtype: 'numberfield', fieldLabel: _lang.AutoCode.fMainValue, allowBlank: true},
			    { field: 'main.mainValueCleanRule', xtype: 'textfield', fieldLabel: _lang.AutoCode.fMainValueCleanRule, allowBlank: true},
			    { field: 'main.subValue', xtype: 'numberfield', fieldLabel: _lang.AutoCode.fSubValue, allowBlank: true},
			    { field: 'main.subValueCleanRule', xtype: 'textfield', fieldLabel: _lang.AutoCode.fSubValueCleanRule, allowBlank: true},
			    { field: 'main.lastValue', xtype: 'textfield', fieldLabel: _lang.AutoCode.fLastValue},
			    { field: 'main.updatedAt', xtype: 'displayfield', fieldLabel: _lang.TText.fUpdatedAt, format: curUserInfo.dateFormat},
			    { field: 'main.groupNo', xtype: 'textfield', fieldLabel: _lang.AutoCode.fGroupNo, allowBlank:false},
			    { field: 'main.sort', xtype: 'numberfield', fieldLabel: _lang.TText.fSort, allowBlank:false},
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: true, value:'1',
			    	store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    }
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
            url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main'
		});
	}
});