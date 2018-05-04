/**
 * ContactsDialog 字段触发
 * ContactsDialogWin　　弹出窗口
 * ContactsFormMultiGrid　多选列表
 */
Ext.define('App.ContactsDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.ContactsDialog',

    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.ContactsDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new ContactsDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            initValue: this.initValue || true,
            type: this.type || 0,
            isFormField: true,
            subcallback: this.subcallback ? this.subcallback: '',
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


ContactsDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.Contacts.tSelector;
        conf.moduleName = 'Contacts';
        conf.winId = 'ContactsDialogWinID';
        conf.mainGridPanelId = 'ContactsDialogWinGridPanelID';
        conf.searchFormPanelId= 'ContactsDialogWinSearchPanelID',
        conf.selectGridPanelId = 'ContactsDialogWinSelectGridPanelID';
        conf.treePanelId = 'ContactsDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'contacts/list' + (this.type>0 ? '?type=' +this.type: ''),
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;
        conf.type = this.type;

        Ext.applyIf(this, conf);
  
        this.initUI(conf);

        ContactsDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.Contacts.mTitle,
            width: this.single ? 880 : 1080,
            height:600,
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
            // onlyKeywords: true,
            fieldItems:[
            	 {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                     store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
                 },
                {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.Contacts.fCnName},
                {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.Contacts.fEnName},
                {field:'Q-gender-N-EQ', xtype:'combo', title:_lang.Contacts.fGender, value:'',
                    store: [['', _lang.TText.vAll],['2', _lang.TText.vFemale], ['1', _lang.TText.vMale]]
                },
                {field:'Q-email-S-LK', xtype:'textfield', title:_lang.TText.fEmail},
                {field:'Q-qq-S-LK', xtype:'textfield', title:_lang.Contacts.fQq},
                {field:'Q-mobile-S-LK', xtype:'textfield', title:_lang.Contacts.fMobile},
                {field:'Q-phone-S-LK', xtype:'textfield', title:_lang.Contacts.fPhone},
                {field:'Q-skype-S-LK', xtype:'textfield', title:_lang.Contacts.fSkype},
                {field:'Q-wechat-S-LK', xtype:'textfield', title:_lang.Contacts.fWechat},
                {field:'Q-vendorId-S-EQ', xtype:'hidden'},
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title: _lang.Contacts.tabListTitle,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 1080,
            autoScroll: true,
            url : conf.urlList,
            fields: [ 'id','vendorId','vendorCnName','vendorEnName','cnName','enName', 'gender','title', 'email','department', 'qq','skype','extension','phone','wechat','mobile','shared','remark','status',
		           'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
				],
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 120, hidden: true },
				{ header:_lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true },
			    { header: _lang.Contacts.fCnName, dataIndex: 'cnName', width: 100},
			    { header: _lang.Contacts.fEnName, dataIndex: 'enName', width: 120},
			    { header: _lang.Contacts.fGender, dataIndex: 'gender', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
					}
			    },
			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
			    { header: _lang.Contacts.fQq, dataIndex: 'qq', width: 140 },
			    { header: _lang.Contacts.fSkype, dataIndex: 'skype', width: 140 },
			    { header: _lang.Contacts.fPhone, dataIndex: 'phone', width: 100 },
			    { header: _lang.Contacts.fExtension, dataIndex: 'extension', width: 100 },
			    { header: _lang.Contacts.fWechat, dataIndex: 'wechat', width: 140 },
			    { header: _lang.Contacts.fMobile, dataIndex: 'mobile', width: 100 },
			    { header: _lang.Contacts.fRemark, dataIndex: 'remark', width: 200 },
			    
				
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
            title: _lang.Contacts.tSelected,
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
            fields : ['id','cnName','enName', 'email'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 130, hidden: true, sortable:false },
				{ header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 100, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200, sortable:false, hidden:true},

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
        if(this.initValue && this.fieldValueName){
			var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
			var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
			if(ids){
				var arrIds = ids.split(',');
				var arrNames = names.split(',');
				var selStore = this.selectGridPanel.getStore();
				for(var i=0; i<arrIds.length; i++){
                    var arrTitles = arrNames[i].split(':');
					if(curUserInfo.lang =='zh_CN'){
						selStore.add({id: arrIds[i], cnName: arrTitles[0], email: arrTitles[1]});
					}else{
						selStore.add({id: arrIds[i], enName: arrTitles[0], email: arrTitles[1]});
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
				names += ':' + rows[i].data.email;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
				names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
                names += ':' + rows.getAt(i).data.email;
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


Ext.define('App.ContactsFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ContactsFormMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
    		title : this.fieldLabel,
            moduleName: 'Contacts',
            fieldValueName: this.valueName || 'main_contactss',
            fieldTitleName: this.titleName || 'main_contactssName'
        };
        conf.mainGridPanelId= this.mainGridPanelId || this.formId + '-ContactsMultiGrid';
        conf.mainFormPanelId= this.mainFormPanelId || this.formId + '-ContactsMultiFrom';
        conf.subGridPanelId= this.subGridPanelId || conf.mainGridPanelId +'-ContactsMultiGridPanelID';
        conf.subFormPanelId= this.subFormPanelId || conf.mainGridPanelId +'-ContactsMultiFormPanelID';
        conf.defHeight = this.height || 200;
        //TODO 后期调整
        this.readOnly = true;
        
        this.initUIComponents(conf);
        
        App.ContactsFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
			minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [{
                type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
                handler: function(event, toolEl, panelHeader) {
                	this.conf = conf;
                	this.onRowAction.call(this);
                }
    		},{
            	type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            	handler: function(event, toolEl, panelHeader) {
            		this.setHeight(conf.defHeight);
        			this.subGridPanel.setHeight(conf.defHeight-3);
            	}
    		},{
        		type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
        		handler: function(event, toolEl, panelHeader) {
        			this.setHeight(400);
        			this.subGridPanel.setHeight(397);
           	}}
        ];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.VendorDocument.tabContactsInformation,
            width: '100%',
            height: conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
    		fields: [ 'id','vendorCnName','vendorEnName','vendorId','cnName','enName', 'gender','title', 'email','department', 'qq',
                'port','agentCompany', 'skype','extension','phone','wechat','mobile','shared','remark','status',
 	           'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
 			],
 			columns: [
 				{ header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
					keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
					actions: [{ 
						iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
						callback: function(grid, record, action, idx, col, e, target) {
							this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
						}
					},{
						iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
						callback: function(grid, record, action, idx, col, e, target) {
							this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
						} 
					}]				    
				},
 				{ header: 'ID', dataIndex: 'id', width: 120, hidden: true },
 				{ header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorCnName', width: 260, hidden: true },
 				{ header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendorEnName', width: 260, hidden: true },
 				{ header:_lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true },
 			    { header: _lang.Contacts.fCnName, dataIndex: 'cnName', width: 100},
 			    { header: _lang.Contacts.fEnName, dataIndex: 'enName', width: 120},
 			    { header: _lang.Contacts.fGender, dataIndex: 'gender', width: 40,
 					renderer: function(value){
 						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
 						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
 					}
 			    },
 			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
                { header: _lang.Contacts.fPort, dataIndex: 'port', width: 100,
                    renderer: function(value){
                        var $ports = _dict.origin;
                        if($ports.length>0 && $ports[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $ports[0].data.options);
                        }
                    }
                },
                { header: _lang.Contacts.fAgentCompany, dataIndex: 'agentCompany', width: 100 },
 			    { header: _lang.Contacts.fQq, dataIndex: 'qq', width: 140 },
 			    { header: _lang.Contacts.fSkype, dataIndex: 'skype', width: 140 },
 			    { header: _lang.Contacts.fPhone, dataIndex: 'phone', width: 100 },
 			    { header: _lang.Contacts.fExtension, dataIndex: 'extension', width: 100 },
 			    { header: _lang.Contacts.fWechat, dataIndex: 'wechat', width:140 },
 			    { header: _lang.Contacts.fMobile, dataIndex: 'mobile', width: 100 },
 			    { header: _lang.Contacts.fRemark, dataIndex: 'remark', width: 200 },
 				
 			],// end of columns
        	appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
        });
    },
    
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
		switch (action) {
			case 'btnRowEdit' :
				var selectedId = record.data.id;
				new ContactsDialogWin({
					scope:this,
		            single:true,
		            fieldValueName: conf.fieldValueName,
		            fieldTitleName: conf.fieldTitleName,
		            selectedId : selectedId,
		            meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
	            			this.meGrid.getStore().insert(idx, result[0].data);
	            			this.meGrid.getStore().removeAt(idx+1);
	            		}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.subGridPanelId).store.remove(record);
				break;
				
			default :
		        new ContactsDialogWin({
		        	scope:this,
		            single:false,
		            fieldValueName: this.conf.fieldValueName,
		            fieldTitleName: this.conf.fieldTitleName,
		            selectedId : '',
		            meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.data.items.length>0){
		            		var items = result.data.items;
		            		for(var index=0; index<items.length; index++){
		            			if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
		            				this.meGrid.getStore().add(items[index].raw);
		            			}
		            		}
		            	}
		            }}, false).show();
		        
				break;
		}
	}
});
