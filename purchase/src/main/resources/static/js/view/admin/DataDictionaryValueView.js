DataDictionaryValueView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Dictionary.mValueTitle,
			moduleName: 'DictionaryValue',
			frameId : 'DataDictionaryValueView',
			mainGridPanelId : 'DataDictionaryValueGridPanelID',
			mainFormPanelId : 'DataDictionaryValueFormPanelID',
			searchFormPanelId: 'DataDictionaryValueSearchPanelID',
			mainTabPanelId: 'DataDictionaryValueTbsPanelId',
			subGridPanelId : 'DataDictionaryValueSubGridPanelID',
			urlList: __ctxPath + 'dict/list',
			urlSave: __ctxPath + 'dictvalue/save',
			urlDelete: __ctxPath + 'dictvalue/delete',
			urlGet: __ctxPath + 'dictvalue/get',
			urlSubList: __ctxPath + 'dictvalue/list',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true,
			addFun: this.addRow,
			delFun: this.deleteRow
		};
		this.initUIComponents(conf);
		DataDictionaryValueView.superclass.constructor.call(this, {
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
				{title:_lang.Dictionary.fDictTitle, field:'Q-title-S-LK', xtype:'textfield'},
			    {title:_lang.Dictionary.fCodeMain, field:'Q-codeMain-S-LK', xtype:'textfield'},
			    {title:_lang.Dictionary.fCodeSub, field:'Q-codeSub-S-LK', xtype:'textfield'},
			    {title:_lang.Dictionary.mCateTitle, field:'Q-categoryId-S-EQ', xtype:'comboremote', addAll: true,
			    	url:__ctxPath + 'dictcate/list'
			    },
			    {title:_lang.TText.fType, field:'Q-type-N-EQ', xtype:'combo', value:'',
			    	store: [['', _lang.TText.vAll], ['1', _lang.Dictionary.vSingleOption], ['2', _lang.Dictionary.vMultipleOption]]
			    },
			    {title:_lang.Dictionary.fCustom,field:'Q-custom-N-EQ', xtype:'combo', value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    }
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			scope: this,
			border: false,
            forceFit: false,
			url: conf.urlList,
			fields: [ 'id','title','codeMain','codeSub','categoryId', 'categoryName', 'type','sort','status','custom'],
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.Dictionary.fDictTitle, dataIndex: 'title', width: 160, sortable: false},
                { header: _lang.Dictionary.mCateTitle, dataIndex: 'categoryName', width: 140},
                { header: _lang.Dictionary.fCateId, dataIndex: 'categoryId', width: 40, hidden: true },
                { header: _lang.Dictionary.fCodeMain, dataIndex: 'codeMain', width: 120},
                { header: _lang.Dictionary.fCodeSub, dataIndex: 'codeSub', width: 160 },
                { header: _lang.TText.fType, dataIndex: 'type', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.Dictionary.vSingleOption);
                        if(value == '2') return $renderOutputColor('blue', _lang.Dictionary.vMultipleOption);
                    }
                },
                { header: _lang.Dictionary.fCustom, dataIndex: 'custom', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                    }
                },
                { header: _lang.TText.fSort, dataIndex: 'sort', width:40 },
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});
		
		
		//字典可选值
		this.centerGridPanel = new HP.GridPanel({
			title: _lang.Dictionary.tabDictValueTitle,
			region: 'south',
			height: '30%',
			minHeight: 250,
			autoLoad: false,
			collapsible: true,
			split: true,
			scope: this,
            bbar: false,
			id: conf.subGridPanelId,
			url: conf.urlSubList,

			fields: [ 'id','title','dictId','value','sort','status','isDefault','custom'],
			columns: [
			    { header: 'ID', dataIndex: 'id', hidden: true, width: 50},
			    { header: _lang.Dictionary.fDictId, dataIndex: 'dictId', hidden: true, width: 50},
			    { header: _lang.Dictionary.fTitle, dataIndex: 'title'},
			    { header:_lang.Dictionary.fValue, dataIndex: 'value', width: 80 },
			    { header: _lang.TText.fSort, dataIndex: 'sort', width: 50 },
			    { header: _lang.Dictionary.fIsDefault, dataIndex: 'isDefault', width: 50,
			    	renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
					}
			    },
			    { header: _lang.Dictionary.fCustom, dataIndex: 'custom', width: 80,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
					}
			    },
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 60,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    }
		    ],
		    itemclick: function(obj, record, item, index, e, eOpts){
				//加载内容
		    	var me = this;
				this.scope.centerFormPanel.loadData({
					url: conf.urlGet +'?id=' + record.data.id,
					root: 'data', preName: 'main', scope: this, loadMask:true, maskTo: this.scope.centerFormPanel.id,
					success:function(response, options){
						if(conf.mainFormPanelId == undefined) return; else formPanel = Ext.getCmp(conf.mainFormPanelId);
						var obj = Ext.JSON.decode(response.responseText);
		    			if(obj.success == true){
		    				if(obj.data.desc.length){
			    				for(var i=0; i< obj.data.desc.length; i++){
			    					if(obj.data.desc[i].lang == 'zh_CN'){
				    					formPanel.getCmpByName('main.desc[0].lang').setValue(obj.data.desc[i].lang);
				    					formPanel.getCmpByName('main.desc[0].text').setValue(obj.data.desc[i].text);
				    				}else{
				    					formPanel.getCmpByName('main.desc[1].lang').setValue(obj.data.desc[i].lang);
				    					formPanel.getCmpByName('main.desc[1].text').setValue(obj.data.desc[i].text);
				    				}
			    				}
		    				}
		    			}
					}
				});
			},
			itemcontextmenu: function(view, record, node, index, e){
				this.scope.btnSubDisabled.call(this, record.data.isCustomer);
			},
		});
		
		//选项编辑卡
		this.centerFormPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.Dictionary.tabDictValueDescTitle,
			region: 'center',
			scope: this,
			width: '100%',

			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.dictId', xtype: 'hidden', fieldLabel: _lang.Dictionary.fDictId, allowBlank: false, value: this.id,},
			    { field: 'main.value', xtype: 'textfield', fieldLabel: _lang.Dictionary.fValue, allowBlank: false},
			    { field: 'main.isDefault', xtype: 'combo', fieldLabel: _lang.Dictionary.fIsDefault, allowBlank: false, value:'1',
			    	store: [['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    },
			    { field: 'main.custom', xtype: 'combo', fieldLabel: _lang.Dictionary.fCustom, allowBlank: false, value:'1',
			    	store: [['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    },
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
			    	store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    },
			    { field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort, allowBlank: false},
			    { field: 'main.param1', xtype: 'textareafield', fieldLabel: _lang.Dictionary.fParam1, height:120},
			    { field: 'main.param2', xtype: 'textareafield', fieldLabel: _lang.Dictionary.fParam2, height:120},
			    { field: 'main.tabs', xtype: 'DataDictionaryValueTabs', conf: conf, width:'100%', height: 280}
			]
		});
		
		//布局
		this.westPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-west',
			title: _lang.Dictionary.tabDictTitle,
			region: 'west',
			layout: 'border',
			collapsible: true,
			split: true,
			width: '55%',
			minWidth: 400,
			border: false,
			items: [ this.gridPanel, this.centerGridPanel ]
		});
		
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.westPanel, this.centerFormPanel]
		});
	},// end of the init
	
	rowClick: function(record, item, index, e) {
		var me=this;
		$HpSearch({
			searchPanel: this.centerSearchPanel,
			searchQuery: {"Q-dictId-S-EQ": record.data.id},
			gridPanel: this.centerGridPanel,
			callback: function(conf){
				me.centerFormPanel.form.reset();
				me.centerFormPanel.getCmpByName('main.dictId').setValue(record.data.id);
			}
		});
		
	},
	
	addRow : function(){
		var cmpPanel = Ext.getCmp(this.mainGridPanelId);
		var selRs = cmpPanel.getSelectionModel().selected.items;
		
		if (selRs && selRs.length == 1) {
			Ext.getCmp(this.mainFormPanelId).form.reset();
			Ext.getCmp(this.mainFormPanelId).getCmpByName('main.dictId').setValue(selRs[0].data.id);
		}else if (selRs.length == 0) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		}else{
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
		}
	},
	
	deleteRow : function(){
		var cmpPanel = Ext.getCmp(this.subGridPanelId);
		var selRs = cmpPanel.getSelectionModel().selected.items;
		
		if (selRs && selRs.length == 1) {
			$delGridRs({
				url : this.urlDelete,
				scope : this,
				grid : cmpPanel,
				idName : 'id',
				callback : function(fp, action) {
					Ext.getCmp(this.subGridPanelId).getStore().reload();
				}
			});
		}else if (selRs.length == 0) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		}else{
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
		}
    	
	},
});