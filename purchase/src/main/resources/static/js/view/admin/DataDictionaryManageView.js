DataDictionaryManageView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Dictionary.mTitle,
			moduleName: 'Dictionary',
			frameId : 'DataDictionaryManageView',
			mainGridPanelId : 'DataDictionaryManageGridPanelID',
			mainFormPanelId : 'DataDictionaryManageFormPanelID',
			searchFormPanelId: 'DataDictionaryManageSearchPanelID',
			mainTabPanelId: 'DataDictionaryManageTbsPanelId',
			subGridPanelId : 'DataDictionaryManageSubGridPanelID',
			urlList: __ctxPath + 'dict/list',
			urlSave: __ctxPath + 'dict/save',
			urlDelete: __ctxPath + 'dict/delete',
			urlGet: __ctxPath + 'dict/get',
			urlSubList: __ctxPath + 'dictdesc/list',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		DataDictionaryManageView.superclass.constructor.call(this, {
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
			    	store: [['', _lang.TText.vAll], ['1', _lang.Dictionary.vSingleOption], ['2', _lang.Dictionary.vMultipleOption], ['3', _lang.Dictionary.vLineTextOption]]
			    },
			    {title:_lang.Dictionary.fCustom,field:'Q-custom-N-EQ', xtype:'combo', value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    }
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Dictionary.tabDictTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','title','codeMain','codeSub','categoryId', 'categoryName', 'type','sort','status','custom'],
			width: '55%',
			minWidth: 400,
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
						if(value == '3') return $renderOutputColor('yellow', _lang.Dictionary.vLineTextOption);
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
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});
		
		//选项编辑卡
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.Dictionary.tabDictDescTitle,
			region: 'center',
			scope: this,

			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.categoryId', xtype: 'comboremote', fieldLabel: _lang.Dictionary.mCateTitle,
			    	url:__ctxPath + 'dictcate/list'
			    },
			    { field: 'main.codeMain', xtype: 'textfield', fieldLabel: _lang.Dictionary.fCodeMain, allowBlank: false },
			    { field: 'main.codeSub', xtype: 'textfield', fieldLabel: _lang.Dictionary.fCodeSub },
			    { field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort, allowBlank: false},
			    { field: 'main.type', xtype: 'combo', fieldLabel: _lang.TText.fType, allowBlank: false, value:'1',
			    	store: [['1', _lang.Dictionary.vSingleOption], ['2', _lang.Dictionary.vMultipleOption], ['3', _lang.Dictionary.vLineTextOption]]
			    },
			    { field: 'main.custom', xtype: 'combo', fieldLabel: _lang.Dictionary.fCustom, allowBlank: false, value:'1',
			    	store: [['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    },
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
                    store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    },
			    { field: 'main.tabs', xtype: 'DataDictionaryManageTabs', conf: conf, width:'100%', height: 'auto'}
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
		var me = this;
		this.formPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main', 
			success:function(response, options){
				if(me.formPanel == undefined) return;
				if(response.responseText){
					var obj = Ext.JSON.decode(response.responseText);
	    			if(obj.success == true){
	    				for(var i=0; i< obj.data.desc.length; i++){
	    					if(obj.data.desc[i].lang == 'zh_CN'){
		    					me.formPanel.getCmpByName('main.desc[0].lang').setValue(obj.data.desc[i].lang);
		    					me.formPanel.getCmpByName('main.desc[0].name').setValue(obj.data.desc[i].name);
		    					me.formPanel.getCmpByName('main.desc[0].context').setValue(obj.data.desc[i].context);
		    				}else{
		    					me.formPanel.getCmpByName('main.desc[1].lang').setValue(obj.data.desc[i].lang);
		    					me.formPanel.getCmpByName('main.desc[1].name').setValue(obj.data.desc[i].name);
		    					me.formPanel.getCmpByName('main.desc[1].context').setValue(obj.data.desc[i].context);
		    				}
	    				}
	    			}
				
    			}
			}
		});
	}
});