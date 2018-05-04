/**
 * ProductCategoryDialog 字段触发
 * ProductCategoryDialogWin　　弹出窗口
 * ProductCategoryMultiGrid　多选列表
 */
Ext.define('App.ProductCategoryDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.ProductCategoryDialog',

    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.ProductCategoryDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new ProductCategoryDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            isFormField: true,
            initValue: this.initValue || true,
            subcallback: this.subcallback ? this.subcallback: '',
            meForm: Ext.getCmp(this.formId),
            callback: function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                
                if(this.subcallback){
					this.subcallback.call(this, rows);
				}
            }}, false).show();
    }
});


ProductCategoryDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.AccountDepartment.mTitle;
        conf.moduleName = 'ProductCategory';
        conf.winId = 'ProductCategoryDialogWinID';
        conf.mainGridPanelID = 'ProductCategoryDialogWinGridPanelID';
        conf.mainTreePanelId = 'ProductCategoryDialogWinTreePanelID';
        conf.selectGridPanelId = 'ProductCategoryDialogWinSelectGridPanelID';
        conf.treePanelId = 'ProductCategoryDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'archives/product-category/listForDialog',
        conf.treeRefresh = true;
        conf.cancel = true;
        // conf.treeExpand = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI();

        ProductCategoryDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductCategory.mProductCategorySelector,
            width: this.single ? 450 : 600,
            height: 450,
            region: 'center',
            layout : 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [
                new Ext.panel.Panel({
                    id: conf.mainTreePanelId,
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
                    var cmpPanel = Ext.getCmp(conf.mainTreePanelId);
                    var ProductCategoryDialogTreePanel = new Ext.tree.TreePanel({
                        id : conf.treePanelId,
                        layout : 'fit',
                        autoScroll : true,
                        autoHeight : true,
                        multiSelect: ! this.single,
                        // singleExpand: true,
                        forceFit : true,
                        rootVisible : false,
                        height: 590,
                        border:false,
                        lines:true,
                        columns: [
                            { text: curUserInfo.lang =='zh_CN'? _lang.ProductCategory.fCnName: _lang.ProductCategory.fEnName, dataIndex: 'name', xtype:'treecolumn', width:250},
                            { text: 'ID', dataIndex: 'id', width:50, hidden:true}
                        ],
                        store : Ext.create('Ext.data.TreeStore', {
                            root: { children: arr },
                            fields: ['id','fid','name','cnName','enName','leaf','expanded'],
                        }),
                        listeners : {
                            scope : this,
                            'itemdblclick' : function(obj, record, item, index, e, eOpts){
                                if(!this.single) {
                                    var selStore = this.scope.selectGridPanel.getStore();
                                    if(selStore.getCount()){
                                        for (var i = 0; i < selStore.getCount(); i++) {
                                            if (selStore.getAt(i).data.id == record.data.id) {
                                                Ext.ux.Toast.msg('操作信息', '选项已被选中！');
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
                    cmpPanel.add(ProductCategoryDialogTreePanel);
                    // ProductCategoryDialogTreePanel.expandAll();

                    //初始化选择
                    this.selecteNodes(ProductCategoryDialogTreePanel.getRootNode().childNodes, this.selectedId);
                }else{
                    Ext.ux.Toast.msg(_lang.ProductCategory.mProductCategorySelector, obj.msg);
                }
            }
        });
    },

    initUI : function() {
        //grid panel
        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.ProductCategory.fSelctedCategory,
            id : this.selectGridPanelId,
            scope : this,
            width: '45%',
            hidden : this.single != undefined ? this.single : false,
            border: true,
            autoLoad: false,
            collapsible : true,
            split : true,
            bbar: false,
            fields : ['id','name','cnName','enName'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 130, hidden: true, sortable:false },
                { header: _lang.ProductCategory.fCnName, dataIndex: 'cnName', width: 120, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.ProductCategory.fEnName, dataIndex: 'enName', width: 120, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
			    
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

    //遍历选择
    selecteNodes :function (nodes, selectedId){
        for(var i=0; i<nodes.length; i++){
            if(nodes[i].data.id == selectedId){
                Ext.getCmp(this.treePanelId).getSelectionModel().select(nodes[i], true);
                this.expandParentNode(nodes[i]);
            }
            if(nodes[i].hasChildNodes()){
                this.selecteNodes(nodes[i].childNodes, selectedId);
            }
        }
    },

    expandParentNode: function(node){
        if(!!node.parentNode) {
            node.parentNode.expand();
            this.expandParentNode(node.parentNode);
        }
    },

    winOk : function(){
        var ids = '';
        var names = '';
        var rows = {};
        if(this.single){
            rows = Ext.getCmp(this.treePanelId).getSelectionModel().selected.items;
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {ids += ','; names += ',';}
                ids += rows[i].data.id;
                names += rows[i].data.name;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ','; names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.name;
            }
        }

        if (this.callback) {
            this.callback.call(this, ids, names, rows);
        }
        Ext.getCmp(this.winId).close();
    }

});


Ext.define('App.ProductCategoryMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCategoryMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : this.fieldLabel,
            moduleName : 'ProductCategory'
        };
        conf.mainGridPanelId= this.mainGridPanelId || this.formId + '-MultiGridPanelID';
        conf.mainFormPanelId= this.mainFormPanelId || this.formId + '-MultiFormPanelID';
        conf.subGridPanelId= this.subGridPanelId || conf.mainGridPanelId +'-VendorMultiGridPanelID';
        conf.subFormPanelId= this.subFormPanelId || conf.mainGridPanelId +'-VendorMultiFormPanelID';
        conf.defHeight = this.height || 200;
        
        this.initUIComponents(conf);

        App.ProductCategoryMultiGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: 'auto',

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
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.subGridPanel.setHeight(conf.defHeight-3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.subGridPanel.setHeight(697);
            }}
        ];

        this.subGridPanel = new HP.GridPanel({
        	region: 'center',
            id: conf.subGridPanelId,
            title: _lang.VendorDocument.tabProductCategory,
            width: '100%',
            height:conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },

            fields: [ 'id','productCategoryId', 'alias','orderIndex','cnName','enName'],
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
                { header: 'ID', dataIndex: 'id', width: 100, hidden: true },
				{ header: _lang.VendorDocument.fProductCategoryId, dataIndex: 'productCategoryId', width: 120, hidden: true},
				{ header: _lang.ProductCategory.fCnName, dataIndex: 'cnName', width: 230},
			    { header: _lang.ProductCategory.fEnName, dataIndex: 'enName', width: 230},
			    { header: _lang.VendorDocument.fAlias, dataIndex: 'alias', width: 230, editor:{xtype:'textfield'} },
			    { header: _lang.VendorDocument.fOrderIndex, dataIndex: 'orderIndex', width: 80 },
            ],// end of columns
          //  appendColumns: $groupGridCreatedColumns(),
        });

    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
		switch (action) {
			case 'btnRowEdit' :
				var selectedId = record.data.id;
				new ProductCategoryDialogWin({
		            single:true,
		            fieldValueName: 'main_productCategory',
		            fieldTitleName: 'main_productCategoryName',
		            selectedId : selectedId,
		            meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
		            	    console.log(result[0].data);
                            result[0].data.alias = result[0].data.enName;
	            			this.meGrid.getStore().insert(idx, result[0].data);
	            			this.meGrid.getStore().removeAt(idx+1);
		            	}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.subGridPanelId).store.remove(record);
				break;
				
			default :
		        new ProductCategoryDialogWin({
		            single:false,
		            fieldValueName: 'main_productCategory',
		            fieldTitleName: 'main_productCategoryName',
		            selectedId : '',
		            initValue: false,
		            meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.data.items.length>0){
		            		var items = result.data.items;
		            		console.log(items);
		            		for(var index=0; index<items.length; index++){
		            			if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
		            				var row = {
		            					productCategoryId : items[index].raw.id,
                                        alias : items[index].raw.enName,
		            				};
		            				Ext.apply(row, items[index].raw);
		            				this.meGrid.getStore().add(row);
		            			}
		            		}
		            	}
		            }}, false).show();
		        
				break;
		}
	}
});
