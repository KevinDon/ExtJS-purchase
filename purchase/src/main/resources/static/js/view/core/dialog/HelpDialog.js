Ext.define('App.HelpDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.HelpDialog',

    onTriggerWrapClick: function() {
    	new HelpDialog({
			scope:this, parent:this.parent,
			single: this.single != null ? this.single : true,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			fileDefType: this.fileDefType,
			selectedId : Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue(),
			form : Ext.getCmp(this.formId),
			eventClose: this.callback,
			callback:function(ids, titles) {
				this.form.getCmpByName(this.fieldTitleName).setValue(titles);
				this.form.getCmpByName(this.fieldValueName).setValue(ids);
				
				this.eventClose ? this.eventClose.call(this, ids, titles) : ''; 
			}}
    	, false).show();
    }
});


HelpDialog = Ext.extend(HP.Window,{
	constructor : function(conf) {
        conf.title = _lang.Help.mTitle;
        conf.moduleName = 'Help';
        conf.winId = 'HelpDialogWinID';
        conf.mainGridPanelId = 'HelpDialogWinGridPanelID';
        conf.mainFormPanelId = 'HelpDialogWinFormPanelID';
        conf.searchFormPanelId= 'HelpDialogWinSearchPanelID';
		conf.selectGridPanelId = 'HelpDialogWinSelectGridPanelID';
        conf.treePanelId = 'HelpDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'admin/help/preview';
		conf.refresh = true;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.single = conf.single != null ? conf.single : false;

        this.initUI(conf);

		HelpDialog.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title ,
			region: 'center', layout : 'border',
			width: 1108,
			height:550,
			items: [this.westPanel, this.centerPanel ]
		});
	},
	
	initUI : function(conf) {
		this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            onlyKeywords: true
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region : 'center',
			scope : this,
			id : conf.mainGridPanelId,
			url : conf.urlList,
			fields : [ 'id','title'],
            sorters: [{property: 'sort', direction: 'ASC'}],
			columns : [
			   { header : 'ID', dataIndex : 'id', hidden : true},
			   { header : _lang.Help.fTitle, dataIndex : 'title'}
			],// end of columns,
			itemclick : function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e);
			},
			callback : function(obj, records){
				//初始化选择
				if(this.selectedId && records.length){
					for(var i=0; i<records.length; i++){
						if(records[i].data.id == this.selectedId){
							Ext.getCmp('HelpDialogGrid').getSelectionModel().select(records[i], true);
						}
					}
				};
			}
		
		});
		
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			region: 'center',
			scope:this,
			style: 'margin:0; padding:0;',
			fieldItems : [
			    { field: 'Help.id', xtype: 'hidden'},
			    { field: 'Help.title', xtype: 'displayfield', fieldLabel:'', fieldStyle:'font-size: 16px;font-weight: bold; line-height: 160%;'},
			    { field: 'Help.content', xtype: 'displayfield', fieldLabel:'', fieldStyle:'font-size: 16px;line-height: 150%;'}
			]
		});

		this.westPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-west',
			title: _lang.Help.fIndex,
			layout: 'border', region: 'west',
			collapsible: true, split: true,
			border: false,
			width:400, scope: this,
			items: [ this.searchPanel, this.gridPanel]
		});
		
		this.centerPanel = new Ext.Panel({
			id: 'HelpDialogSouthPanel',
			layout: 'fit',
			region: 'center',
			border: false,
			scope: this,
			items: [ this.formPanel]
		});
	},
	
	rowClick : function(record, item, index, e) {
		this.formPanel.loadData({
			url: __ctxPath + 'admin/help/info?id=' + record.data.id,
			preName: 'Help', scope: this, loadMask:true, maskTo: this.westPanel.id
		});
	},
	
	winCancel : function() {
		this.close();
	},
	
	winOk : function(){
		this.close();
	}
	
});