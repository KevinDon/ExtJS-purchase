MyDocumentCategoryView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.MyDocument.mTitleCate,
			moduleName: 'MyDocumentCategory',
			frameId : 'MyDocumentCategoryView',
			mainGridPanelId : 'MyDocumentCategoryGridPanelID',
			mainFormPanelId : 'MyDocumentCategoryFormPanelID',
			searchFormPanelId: 'MyDocumentCategorySearchFormPanelID',
			mainTabPanelId: 'MyDocumentCategoryTbsPanelId',
			urlList: __ctxPath + 'mydoccate/list',
			urlSave: __ctxPath + 'mydoccate/save',
			urlDelete: __ctxPath + 'mydoccate/delete',
			urlGet: __ctxPath + 'mydoccate/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		
		MyDocumentCategoryView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.searchPanel, this.centerPanel ]
		});
	},
	
	initUIComponents: function(conf) {
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
				{field:'Q-path-S-LK', xtype:'textfield', title: _lang.MyDocument.fCatePath},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			 
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.MyDocument.tabDictCateTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
            sorters: [{property: 'sort', direction: 'ASC'}],
			fields: ['id','title','path','cnDcd', 'enDcd','sort','status'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: curUserInfo.lang =='zh_CN'? _lang.Dictionary.fCnCateTitle: _lang.Dictionary.fEnCateTitle, dataIndex: 'title', width:200},
				{ header: _lang.MyDocument.fCatePath, dataIndex: 'path', width:120 },
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.TText.fSort, dataIndex: 'sort', width:40 }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//内容form
		this.centerFormPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			region: 'center',
			title: _lang.MyDocument.tabDictCateDescTitle,
			scope: this,
			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
                    store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    },
			    { field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort, allowBlank: false},
			    { field: 'main.path', xtype: 'textfield', fieldLabel: _lang.MyDocument.fPath, allowBlank: false},
			    { field: 'main.desc', xtype: 'MyDocumentCategoryTabs', conf: conf, width:'100%' ,height: 'auto'}
			]
		});
		
		
		//主体内容
		this.centerPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-top',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.centerFormPanel]
		});
				
	},// end of the init
		
	rowClick: function(record, item, index, e, conf) {
		var me = this;
		this.centerFormPanel.loadData({
			url: conf.urlGet + '?id=' + record.data.id,
			root: 'data', preName : 'main', 
			success:function(response, options){
				if(response.responseText){
					var obj = Ext.JSON.decode(response.responseText);
	    			if(obj.success == true){
	    				for(var i=0; i< obj.data.desc.length; i++){
	    					if(obj.data.desc[i].lang == 'zh_CN'){
		    					me.centerFormPanel.getCmpByName('main.desc[0].lang').setValue(obj.data.desc[i].lang);
		    					me.centerFormPanel.getCmpByName('main.desc[0].title').setValue(obj.data.desc[i].title);
		    					me.centerFormPanel.getCmpByName('main.desc[0].context').setValue(obj.data.desc[i].context);
		    				}else{
		    					me.centerFormPanel.getCmpByName('main.desc[1].lang').setValue(obj.data.desc[i].lang);
		    					me.centerFormPanel.getCmpByName('main.desc[1].title').setValue(obj.data.desc[i].title);
		    					me.centerFormPanel.getCmpByName('main.desc[1].context').setValue(obj.data.desc[i].context);
		    				}
	    				}
	    			}
				
    			}
			}
		});
	}
});