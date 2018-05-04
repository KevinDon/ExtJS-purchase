ModulesView = Ext.extend(Ext.Panel, {
	constructor : function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			frameId : 'ModulesView',
			moduleName: 'Modules',
			title : _lang.AccountResource.mTitle,
			viewPanelId : 'ModulesViewPanelID',
			treePanelId : 'ModulesViewTreePanelId',
			urlList: __ctxPath + 'modules/list',
			urlSave: __ctxPath + 'modules/save',
			urlDelete: __ctxPath + 'modules/delete',
			treeRefresh: true,
			treeAdd: true,
			treeInsert: true,
			treeEdit: true,
			treeUp: true,
			treeDown: true,
			treeLeft: true,
			treeRight: true,
			treeDel: true,
			treeExpand: true,
			editFun: this.editRow
		};
		
		this.initUIComponents(conf);
		ModulesView.superclass.constructor.call(this, {
			id: conf.frameId, 
			title: conf.title,
			region: 'center', layout : 'border',
			closable: true, autoScroll: false,
			items: [ new Ext.panel.Panel({
				id: conf.viewPanelId,
				border : false,
				autoScroll: true,
				layout: 'fit',
				region: 'center',
				tbar: Ext.create("App.toolbar", conf),
			}) ]
		});

	},
	
	initUIComponents : function(conf) {
		Ext.Ajax.request({
    		url : conf.urlList,
    		scope : this,
    		method: 'post',
    		success : function(response, options) {
    			var obj = Ext.decode(response.responseText);
    			
    			if(obj.success == true){
    				var arr = obj.data;
    				var cmpPanel = Ext.getCmp(conf.viewPanelId);
    				
    				var ModulesViewTreePanel = new Ext.tree.TreePanel({
						id : conf.treePanelId,
					    rootVisible : false,
					    border: false,
						multiSelect: false,
						defaults: {flex: true},
						columns: [
							{ header: _lang.AccountResource.fCnName, dataIndex: 'cnName', width:'20%', xtype:'treecolumn'},
							{ header: 'ID', dataIndex: 'id', width:'10%', hidden : true },
							{ header: _lang.AccountResource.fEnName, dataIndex: 'enName', width:'25%'},
							{ header: _lang.AccountResource.fCode, dataIndex: 'code', width:180},
							{ header: _lang.AccountResource.fItemIcon, dataIndex: 'itemIcon', width:60,
								renderer : function(value){
									if(value != undefined)	return '<i class="fa-line '+value+'"></i>';
									else return $renderOutputColor('gray', _lang.TText.noValue);;
								}
							},
							{ header: _lang.AccountResource.fType, dataIndex: 'type', width:50,
								renderer : function(value){
									if(value == '0') return $renderOutputColor('green', _lang.AccountResource.vNode);
								    if(value == '1') return $renderOutputColor('gray', _lang.AccountResource.vLink);
								    if(value == '2') return $renderOutputColor('blue', _lang.AccountResource.vModule);
								}
							},
							{ header: _lang.AccountResource.fUrl, dataIndex: 'url', width:220},
							{ header: _lang.TText.fLeaf, dataIndex: 'leaf', width: 40,
								renderer : function(value){
									if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
									if(value == '0') return $renderOutputColor('gray', _lang.TText.vNo);
								}
							},
							{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
								renderer : function(value){
									if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
									if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
								}
							},
							{ header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 },
							{ header: _lang.TText.fSort, dataIndex: 'sort', width : 40}
						],
						store : Ext.create('Ext.data.TreeStore', {
						    root: { children: arr },
						    sorters: [{property: 'sort', direction: 'ASC'}],
						    fields: ['id','parentId', 'cnName','enName', 'type', 'url', 'code', 'status', 'sort', 'leaf', 'itemIcon', 'expanded', 'createdAt'],
						}),
						listeners : {
							scope : this,
		    				'itemclick' : function(obj, record, item, index, e, eOpts){
		    				}
						}
					});
					cmpPanel.add(ModulesViewTreePanel);
					ModulesViewTreePanel.expandAll();
    			}else{
    				Ext.ux.Toast.msg(_lang.TText.titleClew, obj.msg);
    			}
    		}
    	});
	},// end of the init
	
	editRow : function(conf){
		new ModulesForm(conf).show();
	}
});