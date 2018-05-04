ScheduleView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Schedule.tabListTitle,
			moduleName: 'Schedule',
			frameId : 'ScheduleView',
			mainGridPanelId : 'ScheduleGridPanelID',
			searchFormPanelId: 'ScheduleSearchPanelID',
			mainTabPanelId: 'ScheduleTbsPanelId',
			subGridPanelId : 'ScheduleSubGridPanelID',
			urlList: __ctxPath + 'desktop/schedule/list',
			urlSave: __ctxPath + 'desktop/schedule/save',
			urlDelete: __ctxPath + 'desktop/schedule/delete',
			urlGet: __ctxPath + 'desktop/schedule/get',
			refresh: true,
			save: true,
			add: true,
			del: true
		};
		this.initUIComponents(conf);
		ScheduleView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.centerPanel ]
		});
	},
    
	initUIComponents: function(conf) {		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Schedule.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','time','allocationPerson','allocationName'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },

			    { header: _lang.Schedule.fTime, dataIndex: 'time', width: 140 },
			    { header: _lang.Schedule.fAllocationPerson, dataIndex: 'allocationPerson', width: 120 },
			    { header: _lang.Schedule.fAllocationName, dataIndex: 'allocationName', width: 260 }

			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.Schedule.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'main.time', xtype: 'datetimefield', fieldLabel: _lang.Schedule.fTime, allowBlank: false},
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
	},
	
});