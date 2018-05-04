Ext.define('App.DepDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.DepDialog',
    
    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.DepDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}

    	new DepDialogWin({
			scope:this,
			single:this.single ? this.single : false,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			selectedId : selectedId,
	        fieldUserId: this.userId,
			fieldUserName: this.userName,
			isFormField: true,
			meForm: Ext.getCmp(this.formId),
			callback:function(ids, titles) {
				this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
				this.meForm.getCmpByName(this.fieldValueName).setValue(ids);

	            if(this.fieldUserId != undefined){
	            	this.meForm.getCmpByName(this.fieldUserId).setValue('');
	            	this.meForm.getCmpByName(this.fieldUserName).setValue('');
                }
			}}, false).show();
    }
});


DepDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.AccountDepartment.mTitle;
		conf.winId = 'DepDialogWinID';
		conf.mainGridPanelID = 'DepDialogWinGridPanelID';
		conf.selectGridPanelId = 'DepDialogWinSelectGridPanelID';
		conf.treePanelId = 'DepDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'dep/list',
		conf.treeRefresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.treeExpand = true;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI();
		
		DepDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.AccountDepartment.tSelector,
			width: this.single ? 450 : 600,
			height: 450,
			region: 'center',
			layout : 'border',
			tbar: Ext.create("App.toolbar", conf),
			items: [
			    new Ext.panel.Panel({
			    	id: conf.mainGridPanelID,
		    		border : true,
		    		region: 'center',
			    })
			    ,this.selectGridPanel
			]
		});
		
		Ext.Ajax.request({
    		url : conf.urlList,
    		scope : this,
    		method: 'post',
    		success : function(response, options) {
    			var obj = Ext.decode(response.responseText);
    			
    			if(obj.success == true){
    				var arr = obj.data;
    				var cmpPanel = Ext.getCmp(conf.mainGridPanelID);
    				
    				var DepDialogTreePanel = new Ext.tree.TreePanel({
						id : conf.treePanelId,
						layout : 'fit',
						border : false,
						autoScroll : true,
						autoHeight : true,
						multiSelect: ! this.single,
					    singleExpand: false,
					    forceFit : true,
					    rootVisible : false,
					    border:false,
					    lines:true,
						columns: [
					        { text: curUserInfo.lang =='zh_CN'? _lang.AccountResource.fCnName: _lang.AccountResource.fEnName, dataIndex: 'title', xtype:'treecolumn', width:250},
					        { text: 'ID', dataIndex: 'id', width:50, hidden:true}
						],
						store : Ext.create('Ext.data.TreeStore', {
						    root: { children: arr },
						    fields: ['id','parentId','title','leaf','expanded'],
						}),
						listeners : {
							scope : this,
		    				'itemdblclick' : function(obj, record, item, index, e, eOpts){
		    					if(!this.single) {
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
		    						this.winOk.call(this);
		    					}
		    				}
						}
					});
					cmpPanel.add(DepDialogTreePanel);
					DepDialogTreePanel.expandAll();
					
					//初始化选择
					this.selecteNodes(DepDialogTreePanel.getRootNode().childNodes, this.selectedId);
    			}else{
    				Ext.ux.Toast.msg(_lang.TText.titleClew, obj.msg);
    			}
    		}
    	});
	},
	
	initUI : function() {
		//grid panel
		this.selectGridPanel = new HP.GridPanel({
			region : 'east',
			title: _lang.AccountUser.tSelected,
			id : this.selectGridPanelId,
			scope : this,
			width: '45%',
			hidden : this.single != undefined ? this.single : false,
			border: true,
			autoLoad: false,
			collapsible : true,
			split : true,
			fields : ['id','title'],
			columns : [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
				{ header: _lang.AccountDepartment.tDepartment, dataIndex: 'title'},
			],// end of columns
			itemdblclick : function(obj, record, item, index, e, eOpts){
				this.getStore().remove(record);
			}
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
					selStore.add({id: arrIds[i], title: arrNames[i]});
				}
			}
		}
	},
	//遍历选择
	selecteNodes :function (nodes, selectedId){
		for(var i=0; i<nodes.length; i++){
	        if(nodes[i].data.id == selectedId){
	    	    Ext.getCmp(this.treePanelId).getSelectionModel().select(nodes[i], true);
	        }
	        if(nodes[i].hasChildNodes()){
	    	    this.selecteNodes(nodes[i].childNodes, selectedId);
	        }
		}
	},
	
	winOk : function(){
		var ids = '';
		var names = '';
		
		if(this.single){
			var rows = Ext.getCmp(this.treePanelId).getSelectionModel().selected.items;
			for (var i = 0; i < rows.length; i++) {
				if (i > 0) {
					ids += ',';
					names += ',';
				}
				ids += rows[i].data.id;
				names += rows[i].data.title;
			}
		}else{
			var rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {
					ids += ',';
					names += ',';
				}
				ids += rows.getAt(i).data.id;
				names += rows.getAt(i).data.title;
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