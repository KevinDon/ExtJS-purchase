Ext.define('App.RoleDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.RoleDialog',
    
    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.RoleDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}

    	new RoleDialogWin({
			scope:this,
			single:this.single ? this.single : false,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			selectedId : selectedId,
            isFormField: true,
			meForm: Ext.getCmp(this.formId),
			callback:function(ids, titles) {
				this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
				this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
			}}, false).show();
    }
});


RoleDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.AccountDepartment.mTitle;
		conf.winId = 'RoleDialogWinID';
		conf.mainGridPanelId = 'RoleDialogWinGridPanelID';
		conf.searchFormPanelId= 'RoleDialogWinSearchPanelID',
		conf.selectGridPanelId = 'RoleDialogWinSelectGridPanelID';
		conf.treePanelId = 'RoleDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'admin/role/list',
		conf.refresh = true;
		conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		RoleDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.AccountRole.tSelector,
			width: this.single ? 650 : 750,
			region: 'center',
			layout : 'border',
			tbar: Ext.create("App.toolbar", conf),
			items: [this.centerPanel, this.selectGridPanel]
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
		
		this.gridPanel = new HP.GridPanel({
			region : 'center',
			id : conf.mainGridPanelId,
			title: _lang.AccountRole.tabListTitle,
			scope : this,
			border : false,
			url : conf.urlList,
			fields: [ 'id','cnName','enName', 'createdAt', 'status', 'sort'],
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
			    { header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 120},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120},
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 },
			    { header: _lang.TText.fSort, dataIndex: 'sort', width: 40 }
				
			],// end of columns
			itemdblclick : function(obj, record, item, index, e, eOpts){
				if(! conf.single){
					var selStore = this.scope.selectGridPanel.getStore();
					if(selStore.getCount()){
						for (var i = 0; i < selStore.getCount(); i++) {
							if (selStore.getAt(i).data.id == record.data.id) {
								Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedRecord);
								return;
							}
						}
					}
					selStore.add(record.data);
				}else{
					this.scope.winOk.call(this.scope);
				}
			},
			callback : function(obj, records){
				//初始化选择
				if(this.selectedId && records.length){
					for(var i=0; i<records.length; i++){
						if(records[i].data.id == this.selectedId){
							Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
						}
					}
				};
			}
		});
		
		this.selectGridPanel = new HP.GridPanel({
			region : 'east',
			title: _lang.AccountRole.tSelected,
			id : conf.selectGridPanelId,
			scope : this,
			hidden : this.single ? true : false,
			width: 150,
			minWidth: 150,
			border: false,
			autoLoad: false,
			unbbar : true,
			collapsible : true,
			split : true,
			fields : ['id','cnName','enName'],
			columns : [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
				{ header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 120, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
			],// end of columns
			itemdblclick : function(obj, record, item, index, e, eOpts){
				this.getStore().remove(record);
			}
		});
		
		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.searchPanel, this.gridPanel]
		});
		
		// init value
		if(this.fieldValueName){
			var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
			var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
			if(ids){
				var arrIds = ids.split(',');
				var arrNames = names.split(',');
				var selStore = this.selectGridPanel.getStore();
				for(var i=0; i<arrIds.length; i++){
					if(curUserInfo.lang =='zh_CN'){
						selStore.add({id: arrIds[i], cnName: arrNames[i]});
					}else{
						selStore.add({id: arrIds[i], enName: arrNames[i]});
					}
				}
			}
		}
	},
	
	winOk : function(){
		var ids = '';
		var names = '';
		if(this.single){
			var rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
			for (var i = 0; i < rows.length; i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows[i].data.id;
				names += curUserInfo.lang =='zh_CN' ? rows[i].data.cnName: rows[i].data.enName;
			}
		}else{
			var rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
				names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
			}
		}
		
		if (this.callback) {
			this.callback.call(this, ids, names);
		}
		Ext.getCmp(this.winId).close();
	},
	
	winClean:function(){
		var ids = '';
		var names = '';
		if (this.callback) {
			this.callback.call(this, ids, names);
		}
		Ext.getCmp(this.winId).close();
	}
	
});