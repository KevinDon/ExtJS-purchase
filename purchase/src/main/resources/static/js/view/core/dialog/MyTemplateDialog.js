Ext.define('App.MyTemplateDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.MyTemplateDialog',
    
    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.MyTemplateDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}
    	
    	new MyTemplateDialogWin({
			scope:this,
			single:this.single ? this.single : true,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			selectedId : selectedId,
			subcallback: this.subcallback ? this.subcallback: '',
            isFormField: true,
			meForm: Ext.getCmp(this.formId),
			callback:function(ids, titles, rows) {
				this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
				this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
				if(this.subcallback){
					this.subcallback.call(this, rows);
				}
			}}, false).show();
    }
});


MyTemplateDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.MyTemplate.mMyTemplateSelector;
        conf.moduleName = 'MyTemplate';
		conf.winId = 'MyTemplateDialogWinID';
		conf.mainGridPanelId = 'MyTemplateDialogWinGridPanelID';
		conf.searchFormPanelId= 'MyTemplateDialogWinSearchPanelID',
		conf.selectGridPanelId = 'MyTemplateDialogWinSelectGridPanelID';
		conf.treePanelId = 'MyTemplateDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'mytemplate/list',
		conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		MyTemplateDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : conf.title ,
            width: 900,
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
			title: _lang.MyTemplate.tabBuyerList,
			scope : this,
			border : false,
			forceFit: false,
			url : conf.urlList,
            fields: ['id','name','type','context','status','shared','createdAt','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 80, hidden: true },
                { header: _lang.MyTemplate.fName, dataIndex: 'name', width:260 },
                { header: _lang.MyTemplate.fContext, dataIndex: 'context', width:200 },
                { header: _lang.MyTemplate.fType, dataIndex: 'type', width:60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.MyTemplate.vTypeOnlyFile);
                        else if(value == '2') return $renderOutputColor('blue', _lang.MyTemplate.vTypeEmail);
                    }
                },
                { header: _lang.TText.fShared, dataIndex: 'shared', width:60,
                    renderer: function(value){
                        $shared = _dict.shared;
                        if($shared.length>0 && $shared[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $shared[0].data.options);
                        }
                    }
                },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
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
			title: _lang.MyTemplate.tSelected,
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
				{ header: _lang.MyTemplate.fCnName, dataIndex: 'name', width: 260, sortable:false},
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
                    selStore.add({id: arrIds[i], name: arrNames[i]});
				}
			}
		}
	},
	
	winOk : function(){
		var ids = '';
		var names = '';
		var rows = {};
		if(this.single){
			rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
			for (var i = 0; i < rows.length; i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows[i].data.id;
				names += rows[i].data.name;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
				names += rows.getAt(i).data.name;
			}
		}
		
		if (this.callback) {
			this.callback.call(this, ids, names, rows);
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