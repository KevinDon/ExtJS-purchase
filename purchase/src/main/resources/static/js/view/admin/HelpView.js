HelpView = Ext.extend(Ext.Panel, {
	constructor : function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.Help.mTitle,
            moduleName: 'User',
            frameId : 'HelpView',
            mainGridPanelId : 'HelpGridPanelID',
            mainFormPanelId : 'HelpFormPanelID',
            searchFormPanelId: 'HelpSearchPanelID',
            mainTabPanelId: 'HelpTbsPanelId',
            subGridPanelId : 'HelpSubGridPanelID',
            urlList: __ctxPath + 'admin/help/list',
            urlSave: __ctxPath + 'admin/help/save',
            urlDelete: __ctxPath + 'admin/help/delete',
            urlGet: __ctxPath + 'admin/help/get',
            refresh: true,
            save: true,
            add: true,
            saveAs: true,
            del: true
        };
		this.initUIComponents(conf);
		HelpView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.formPanel, this.eastPanel ]
		});
	},
	
	initUIComponents : function(conf) {
		this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
			fieldItems:[
			    {title:_lang.Help.fTitle,field:'Q-title-S-LK', xtype:'textfield', width: 80, labelWidth:40},
			    {title:_lang.Help.fContent,field:'Q-content-S-LK', xtype:'textfield', width: 100, labelWidth:40},
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
                },
			]
		});// end of searchPanel
		
		this.gridPanel = new HP.GridPanel({
			region : 'center',
            id: conf.mainGridPanelId,
            title: _lang.Help.tabListTitle,
			scope : this,
            url: conf.urlList,
			fields : [ 'id', 'title', 'status'],
            sorters: [{property: 'sort', direction: 'ASC'}],
			columns : [
			   { header: 'ID', dataIndex : 'id', width: 40, hidden: true},
			   { header: _lang.Help.fTitle, dataIndex : 'title'},
			   { header: _lang.TText.fStatus, dataIndex : 'status', width : 40,
                   renderer: function(value){
                       if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                       if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                   }
			   },
			],// end of columns
			
			itemclick : function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});

		//内容区域
		this.formPanel = new HP.FormPanel({
            id: conf.mainFormPanelId,
			title : _lang.Help.fContent,
			region : 'center',
			scope: this,
			border:true,
			bodyStyle: 'border-top:0 none; border-left:0 none; border-bottom:0 none;',
			fieldItems : [
			    { field: 'main.id', xtype : 'hidden'},
                { xtype: 'container',cls:'row', items: [
			        { field: 'main.title', xtype: 'textfield', maxLength: 130, fieldLabel: _lang.Help.fTitle, allowBlank: false, cls:'col-1'},
                ]},
                { xtype: 'container',cls:'row', items: [
                    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1', cls:'col-2',
                        store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
                    },
                    {
                      field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort, value:'0', cls:'col-2',
                    }
                ]},
//			    { field: 'main.relation', xtype: 'textfield', fieldLabel: '关联帮助', labelWidth:55},
                { field: 'main.content', id: 'main_content', xtype: 'ckeditor', fieldLabel: _lang.Help.fContent, width:'100%', height:450}
			]
		});
		
		//布局
		this.eastPanel = new Ext.Panel({
			id : conf.mainFormPanelId + '-f',
			title :_lang.Help.tabListTitle,
			split : true,
			width: '25%', labelWidth: 60,
			region : 'east',
			layout : 'border',
			bodyStyle: 'border-top:0 none; border-right:0 none; border-bottom:0 none;',
			items: [ this.searchPanel, this.gridPanel ]
		});
				
	},// end of the init

    rowClick: function(record, item, index, e, conf) {
        this.formPanel.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            root : 'data', preName : 'main'
        });
    }
	
});