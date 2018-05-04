Ext.define('App.UserDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.UserDialog',

    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.UserDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}

    	new UserDialogWin({
			scope:this,
			single:this.single ? this.single : false,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			fieldDepId: this.depId,
			fieldDepName: this.depName,
			selectedId : selectedId,
			isFormField: true,
			inDep: this.inDep ? true : false,
			initValue: this.initValue || true,
			subcallback: this.subcallback ? this.subcallback: '',
			meForm: Ext.getCmp(this.formId),
			callback:function(ids, titles, rows) {
				this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
				this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
				if(this.fieldDepId != undefined && this.single){
					this.meForm.getCmpByName(this.fieldDepId).setValue(rows[0].data.departmentId);
					this.meForm.getCmpByName(this.fieldDepName).setValue(curUserInfo.lang =='zh_CN'? rows[0].data['department.cnName']: rows[0].data['department.enName']);
				}
				if(this.subcallback){
					this.subcallback.call(this, rows);
				}
			}}, false).show();
    }
});


UserDialogWin = Ext.extend(HP.Window, {
	constructor : function(conf) {
		conf.title = _lang.AccountDepartment.mTitle;
		conf.winId = 'UserDialogWinID';
		conf.mainGridPanelId = 'UserDialogWinGridPanelID';
		conf.mainTreePanelId = 'UserDialogWinTreePanelID';
		conf.searchFormPanelId= 'UserDialogWinSearchPanelID',
		conf.selectGridPanelId = 'UserDialogWinSelectGridPanelID';
		conf.treePanelId = 'UserDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'dep/choseList',
		conf.urlSubList = __ctxPath + 'admin/user/listbydepartmentid',
		conf.treeRefresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.treeExpand = true;
		conf.ok = true;
		conf.okFun = this.winOk;


		if(!!conf.inDep){
            conf.urlList += '?inDep=1';
            conf.urlSubList += '?inDep=1';
		}
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		// 默认为多用户
		UserDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.AccountUser.tSelector,
			width: this.single ? 650 : 750,
			region: 'center',
			layout : 'border',
			tbar: Ext.create("App.toolbar", conf),
			items: [
				this.searchPanel,
			    new Ext.panel.Panel({
			    	title: _lang.AccountDepartment.tabListTitle,
			    	id : conf.mainTreePanelId,
			    	region: 'west',
			    	layout: 'fit',
			    	width: 180,
			    	maxWidth:250,
			    	collapsible : true,
                    hidden : this.inDep ? true : false,
                    split : true,
			    	border : false,
			    	autoScroll : true,
			    	scroll: 'horizontal',
			    })
			    ,this.selectGridPanel
			    ,this.userGridPanel
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
    				var cmpPanel = Ext.getCmp(conf.mainTreePanelId);
    				cmpPanel.removeAll();
    				var UserDialogTreePanel = new Ext.tree.TreePanel({
						id : conf.treePanelId,
						layout : 'fit',
						border : false,
                        //hidden : this.inDep ? true : false,
                        hideHeaders : true,
					    singleExpand: false,
					    forceFit : true,
					    rootVisible : false,
					    lines:true,
					    columns: [
					        { text: curUserInfo.lang =='zh_CN'? _lang.AccountResource.fCnName: _lang.AccountResource.fEnName, dataIndex: 'title', xtype:'treecolumn', width:250},
					        { text: 'ID', dataIndex: 'id', width:50, hidden:true}
						],
						store : Ext.create('Ext.data.TreeStore', {
						    root: { children: arr },
						    fields: ['id','parentId','title','leaf','expanded'],
						}),
						listeners: {
							scope : this,
							'itemclick' : function(obj, record, item, index, e, eOpts){

                                this.userGridPanel.getStore().reload({params:{'depId': record.data.id, 'page' : 1}});
                            }
						}
					});

                        cmpPanel.add(UserDialogTreePanel);
                        UserDialogTreePanel.expandAll();
    			}else{
    				Ext.ux.Toast.msg(_lang.TText.titleClew, obj.msg);
    			}
    		}
    	});
	},
	
	initUI : function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            fieldItems:[
                {field:'Q-account-S-LK', xtype:'textfield', title:_lang.TText.fAccount},
                {field:'Q-phone-S-LK', xtype:'textfield', title:_lang.AccountUser.fPhone},
                {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.AccountUser.fCnName},
                {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.AccountUser.fEnName},
                {field:'Q-gender-N-EQ', xtype:'combo', title:_lang.Contacts.fGender, value:'',
                    store: [['', _lang.TText.vAll],['2', _lang.TText.vFemale], ['1', _lang.TText.vMale]]
                },
            ]
        });// end of searchPanel
		//grid panel
		this.userGridPanel = new HP.GridPanel({
			region : 'center',
			id : conf.mainGridPanelId,
			title: _lang.AccountUser.tSelectable,
			scope : this,
			forceFit: false,
			url : conf.urlSubList,
//			autoLoad: false,
			baseParams: {depId: this.depId ? this.depId : curUserInfo.depId},
			fields: [ 'id','account','department.cnName','department.enName', 'departmentId','cnName','enName', 'email', 'qq','skype','extension','phone', 'gender', 'status'],
			columnLines: true,
			columns : [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.TText.fAccount, dataIndex: 'account', width: 100},
				{ header: _lang.AccountUser.fDepartmentName, dataIndex: 'department.cnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.AccountUser.fDepartmentName, dataIndex: 'department.enName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.AccountUser.fDepartmentId, dataIndex: 'departmentId', width: 60, hidden: true },
			    { header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 100},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120},
			    { header: _lang.AccountUser.fGender, dataIndex: 'gender', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
					}
			    },
			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
			    { header: _lang.AccountUser.fQq, dataIndex: 'qq', width: 140 },
			    { header: _lang.AccountUser.fSkype, dataIndex: 'skype', width: 140 },
			    { header: _lang.AccountUser.fPhone, dataIndex: 'phone', width: 100 },
			    { header: _lang.AccountUser.fExtension, dataIndex: 'extension', width: 100 },
                $renderOutputStatusColumns()
			],// end of columns
			itemdblclick : function(obj, record, item, index, e, eOpts){
				if(!conf.single){
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
			title: _lang.AccountUser.tSelected,
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
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true}
			],// end of columns
			itemdblclick : function(obj, record, item, index, e, eOpts){
				this.getStore().remove(record);
			}
		});
		
		// init value
		if(this.initValue && this.fieldValueName){
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
		var rows = [];
		if(this.single){
			rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
			for (var i = 0; i < rows.length; i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows[i].data.id;
				names += curUserInfo.lang =='zh_CN' ? rows[i].data.cnName: rows[i].data.enName;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
				names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
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