Ext.ns("HP");
Ext.ns("Ext.ux");

//数据 grid panel
HP.GridPanel = Ext.extend(Ext.grid.Panel, {
	constructor: function(conf) {
		var def = HP.GridConfig(conf);
		HP.GridPanel.superclass.constructor.call(this, def);
	}
});

//数据  tree panel
HP.TreePanel = Ext.extend(Ext.tree.Panel, {
	constructor: function(conf) {
		var def = HP.TreeConfig(conf);
		HP.TreePanel.superclass.constructor.call(this, def);
	}
});

//form panel
HP.FormPanel = Ext.extend(Ext.form.FormPanel, {
	constructor: function(conf) {
		// Add items
	    conf.items = HP.FromItmesConfig(conf);

	    var def = {
			layout: conf.layout === undefined  ? 'form': conf.layout,
			bodyPadding: 10,
			border: false,
			autoScroll: true,
			buttonAlign: 'center',
			cls: 'form-panel',
			listeners :{
				afterlayout: function(layout, eOpts){
					$_eventAfterrenderForm.call(conf.scope, layout);
					if(conf.afterlayout) conf.afterlayout.call(conf.scope, layout, eOpts);
				},
				fielderrorchange: function( layout, error, eOpts ){
					$_eventAfterrenderForm.call(conf.scope, layout);
					if(conf.fielderrorchange) conf.fielderrorchange.call(conf.scope, layout, error, eOpts);
				},
				afterrender: function(layout, eOpts ){
					$_eventAfterrenderForm.call(conf.scope, layout);
					if(conf.afterrender) conf.afterrender.call(conf.scope, layout, eOpts);
				},
				beforerender: function(layout, eOpts ){
					$_eventAfterrenderForm.call(conf.scope, layout);
					if(conf.beforerender) conf.beforerender.call(conf.scope, layout, eOpts);
				}
			}
			
		};
		
		Ext.apply(def, conf);
		HP.FormPanel.superclass.constructor.call(this, def);
	}
});

//搜索panme
HP.SearchPanel = Ext.extend(Ext.form.FormPanel, {
	constructor: function(conf) {
		
		var search= function(conf){
			$HpSearch({ searchPanel: Ext.getCmp(conf.searchFormPanelId), gridPanel: Ext.getCmp(conf.mainGridPanelId) });
		};
		var reset= function(conf){
			Ext.getCmp(conf.searchFormPanelId).getForm().reset();
		};
		var advanced= function(conf){
			jQuery('#'+ conf.searchFormPanelId + ' .search-advanced').show();
			jQuery('#'+ this.id + ' .btn-advanced').hide();
			jQuery('#'+ this.id + ' input[name="keywords"]').val('');
			jQuery('#'+ this.id + ' input[name="keywords"]').attr('disabled', true);
			Ext.getCmp(conf.searchFormPanelId).setHeight('auto');
		};
		var close= function(conf){
			jQuery('#'+ conf.searchFormPanelId + ' .search-advanced').hide();
			jQuery('#'+ this.id + ' .btn-advanced').show();
			jQuery('#'+ this.id + ' input[name="keywords"]').attr('disabled', false);
			Ext.getCmp(conf.searchFormPanelId).getForm().reset();
			Ext.getCmp(conf.searchFormPanelId).setHeight('auto');
		};
		
		// Add items
		conf.items = [];
	    //add button		
	    var advItems = HP.FromItmesConfig(conf, conf.columnType != undefined ? conf.columnType : 'line');
	    if(! conf.onlyKeywords){
		    advItems.push({xtype:'container', border:false, flex: 1, cls:'search-advanced-button', items:[
		        {text: _lang.TButton.search, iconCls:'fa fa-fw fa-search', scope:this, xtype:'button', handler: function(e){ search.call(this,conf.parentConf); }, cls:'search-button' },
		        {text: _lang.TButton.reset, iconCls:'fa fa-fw fa-undo', scope:this, xtype:'button', handler: function(e){ reset.call(this,conf.parentConf); }, cls:'search-button' },
		        {text: _lang.TButton.close, iconCls:'fa fa-fw fa-close', scope:this, xtype:'button', handler: function(e){ close.call(this,conf.parentConf); }, cls:'search-button btn-close' }
	        ]});
		    
		    conf.items.push({ xtype:'container', border:false, layout:'column', flex: 1, cls:'search-advanced', items:advItems});
	    }
	    
	    var keywordItems = [
			{text:_lang.TText.keywords + ': ', xtype:'label', cls:'label'},
			{hiddenName:'keywords', name:"keywords", text:_lang.TText.keywords, xtype:'textfield', cls:'keywords', width:200},
			{text: _lang.TButton.search, iconCls:'fa fa-fw fa-search', scope:this, xtype:'button', handler: function(e){ search.call(this,conf.parentConf); }, cls:'search-button btn-search-quick'}
	    ];
	    if(! conf.onlyKeywords){
	    	keywordItems.push({text: _lang.TButton.advanced, iconCls:'fa fa-fw fa-search-plus', scope:this, xtype:'button', handler: function(e){ advanced.call(this,conf.parentConf); }, cls:'search-button btn-advanced' });
	    }
	    
		conf.items.unshift({xtype:'container', border:false, layout:'column', flex: 1, cls:'search-quick', items: keywordItems});
		
		var def = {
			layout: conf.layout ? conf.layout: 'column',
			autoHeight: conf.autoHeight ? conf.autoHeight: true,
			border: conf.border ? conf.border: true,
			cls: 'search-panel',
			listeners :{
				scope: conf.scope,
				afterrender: function(layout, eOpts){
					$_eventAfterrenderForm.call(conf.scope, layout);
					if(conf.afterlayout) conf.afterlayout.call(conf.scope, layout, eOpts);
					//enter event
					new Ext.KeyNav({
						target: conf.id, scope: conf.scope,
						enter: function(e){ search(conf.parentConf); },
					});
				}
			}
		};
		
		Ext.apply(def, conf);
		HP.SearchPanel.superclass.constructor.call(this, def);
		
	}	
});

//Window
HP.Window = Ext.extend(Ext.window.Window, {
	constructor: function(conf) {
		var def = {
			layout: conf.layout === undefined  ? 'fit': conf.layout,
			modal: conf.modal === undefined ? true: conf.modal,

			height: conf.height === undefined ? '80%': conf.height,
			minHeight: conf.minHeight === undefined ? 650: conf.minHeight,
			width: conf.width === undefined ? 750: conf.width,
			maximizable: conf.maximizable === undefined ? true: conf.maximizable,
			buttonAlign: 'center'
		};
		
		Ext.apply(def, conf);
		HP.Window.superclass.constructor.call(this, def);
	},
	
	reset : function() {
		this.formPanel.getForm().reset();
	},
	
	cancel : function() {
		this.close();
	},

});


//Init Json Store
HP.JsonStore = Ext.extend(Ext.data.JsonStore, {
	constructor: function(conf) {
		var def = {};
		Ext.applyIf(def, conf);
		HP.JsonStore.superclass.constructor.call(this, def);
	}
});

//Init Tree Store
HP.TreeStore = Ext.extend(Ext.data.TreeStore, {
	constructor: function(conf) {
		var def = {};
		Ext.applyIf(def, conf);
		HP.TreeStore.superclass.constructor.call(this, def);
	}
});


/**
 * grid config
 * lmenubar:  [['key code','name', boolean is commentMenu, handle function, hidden], ...... ]
 */
HP.GridConfig = function(conf) {

    //页大小
    conf.baseParams = !!conf.baseParams ? conf.baseParams : {limit:(curUserInfo.pageSize || 30)};
    if(!!conf.baseParams){
        conf.baseParams.limit = conf.baseParams.limit || (curUserInfo.pageSize || 30);
    }else{
        conf.baseParams = {limit:(curUserInfo.pageSize || 30)};
    }
    conf.pageSize= conf.baseParams.limit;
    
	// store
	if(conf.autoload || conf.autoload === undefined) conf.store = $HpStore(conf);
	// col tip
	var append = conf.appendColumns || [];
	for (var i = 0; i < append.length; i++) {
		conf.columns.push(append[i]);
	}
	
	for (var i = 0; i < conf.columns.length; i++) {
		conf.columns[i].tooltip = conf.columns[i].tooltip ? conf.columns[i].tooltip : conf.columns[i].width<65? conf.columns[i].header: '';
		
		if (!conf.columns[i].renderer) {
			conf.columns[i].renderer = function(data, metadata, record,	rowIndex, columnIndex, store) {
                // if (!Ext.isEmpty(data) && data.toString().length * 5 >=  parseInt(this.columns[columnIndex].width)) { metadata.tdAttr = 'data-qtip="' + data + '"'; }
				return data;
			};
		}
		//col header
		if (conf.headers) { conf.columns[i].header = conf.headers[i]; }
	}
	//line number
	if (conf.columns) conf.columns.unshift({xtype: 'rownumberer', text:'#', width:35 });
	
	//复选框
	if(conf.multiSelect) conf.selModel = Ext.create('Ext.selection.CheckboxModel',{mode: !!conf.selMode? conf.selMode:"SIMPLE"});
    conf.selType = !!conf.selType? conf.selType:  'rowmodel';


	//left menu
	var contextMenuItems = [];
	var action = [];
	if(conf.lmenubar && conf.lmenubar.length>0){
		toolbar = [];
		var confLBar = conf.lmenubar;
		for(var i=0; i<conf.lmenubar.length; i++ ){
			action[confLBar[i][1]] = Ext.create('Ext.Action', {
				scope: confLBar[i][0],
			    iconCls: 'btn-'+ confLBar[i][1],
			    text: confLBar[i][2],
			    handler: confLBar[i][4],
			    disabled: confLBar[i][5]
			});
			
			toolbar.push( action[confLBar[i][1]]);
			if(confLBar[i][3]) contextMenuItems.push( action[confLBar[i][1]] );
		}
		
		if(conf.barDirection && conf.barDirection=='top'){
			conf.tbar = toolbar;
		}else if(conf.barDirection=='left'){
			conf.lbar = toolbar;
		}else if(conf.barDirection=='right'){
			conf.rbar = toolbar;
		}else if(conf.barDirection=='footer'){
			conf.bbar = toolbar;
		}else{
			conf.lbar = toolbar;
		}
	}
	// context menu
	var contextMenu = conf.contextMenu || null;
	if(contextMenuItems.length>0){
		contextMenu = Ext.create('Ext.menu.Menu', { plain: true, items: contextMenuItems });
	}else if(!!contextMenu && contextMenu.length>0){
        contextMenu = Ext.create('Ext.menu.Menu', { plain: true, items: contextMenu });
    }

	// page bar
	var pageBar = {xtype: 'pagingtoolbar', store: conf.store, displayInfo: true, border:false, width:'100%', height:25};

	// 加上插件 expander
	if (conf.expander) {
		if (! conf.plugins) conf.plugins = [];		
		conf.plugins.push(conf.expander);
	}
	
	//设置可编辑列
	if(conf.edit){
		var rowEditor = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit: 1,
			autoCancel: false,
            triggerEvent: 'cellclick'
		});
		
		if (! conf.plugins) conf.plugins = [];		
		conf.plugins.push(rowEditor);
	}

    var def = {
		shim: true,
		trackMouseOver: true,
		disableSelection: false,
		loadMask: true,
		columnLines: true,
		stripeRows: true,
		forceFit: conf.forceFit? conf.forceFit : true,
		border: false,
		viewConfig: { autoHeight:true,	enableRowBody: false, showPreview:false, enableTextSelection:true},
		bbar: conf.unbbar? '': (conf.bbar ? conf.bbar: pageBar),
		action: action,
		emptyText: _lang.TText.noData,
		cls: 'grid-panel',
		listeners: {
			itemcontextmenu: function(view, record, node, index, e) {
				if(conf.itemcontextmenu) conf.itemcontextmenu.call(this, view, record, node, index, e);
				if(contextMenu != null){ e.stopEvent(); contextMenu.showAt(e.getXY()); }
		        return false;
		    },
		    select: function(record, item, index, e, eOpts ){
		    	if(conf.select) conf.select.call(this, record, item, index, e, eOpts);
		    },
		    deselect: function(record, item, index, e, eOpts ){
		    	if(conf.deselect) conf.deselect.call(this, record, item, index, e, eOpts);
		    },
		    itemclick: function(record, item, index, e, eOpts ){
		    	if(conf.itemclick) conf.itemclick.call(this, record, item, index, e, eOpts);
		    },
            cellclick: function(record, item, index, e, eOpts ){
                if(conf.cellclick) conf.cellclick.call(this, record, item, index, e, eOpts);
            },
		    itemdblclick: function(record, item, index, e, eOpts ){
		    	if(conf.itemdblclick) conf.itemdblclick.call(this, record, item, index, e, eOpts);
		    },
		    itemmouseenter: function(e, eOpts ){
		    	if(conf.itemmouseenter) conf.itemmouseenter.call(this, e, eOpts );
		    },
		    itemmouseleave: function(e, eOpts ){
		    	if(conf.itemmouseleave) conf.itemmouseleave.call(this, e, eOpts );
		    },
            afterrender: function(e, eOpts){
                if(conf.afterrender) conf.afterrender.call(this, e, eOpts);
            },
		}
	};
	
	Ext.apply(def, conf);
	
	return def;
};

HP.TreeConfig = function(conf) {
	conf.store = $HpTreeStore(conf);

	//复选框
	if(conf.multiSelect) 
	    conf.selModel = Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"});
	
	var def = {
		shim: true,
		border: false,
		title: conf.title != undefined ? conf.title: '',
		autoScroll: conf.autoScroll !=undefined? conf.autoScroll: true,
		loadMask: false,
		useArrows: conf.useArrows !=undefined? conf.useArrows: false,
	    rootVisible: conf.rootVisible !=undefined? conf.rootVisible: false,
	    multiSelect: conf.multiSelect !=undefined? conf.multiSelect: true,
	    singleExpand: conf.singleExpand !=undefined? conf.singleExpand: false,
	    forceFit: conf.forceFit !=undefined? conf.forceFit: true,
	    xtype: 'tree-grid',
	    cls: 'tree-panel',
		listeners: {
			itemcontextmenu: function(view, record, node, index, e) {
				if(conf.itemcontextmenu) conf.itemcontextmenu.call(this, view, record, node, index, e);
				if(contextMenu != null){ e.stopEvent(); contextMenu.showAt(e.getXY()); }
		        return false;
		    },
		    select: function(record, item, index, e, eOpts ){
		    	if(conf.select) conf.select.call(this, record, item, index, e, eOpts);
		    },
		    deselect: function(record, item, index, e, eOpts ){
		    	if(conf.deselect) conf.deselect.call(this, record, item, index, e, eOpts);
		    },
		    itemclick: function(record, item, index, e, eOpts ){
		    	if(conf.itemclick) conf.itemclick.call(this, record, item, index, e, eOpts);
		    },
		    itemdblclick: function(record, item, index, e, eOpts ){
		    	if(conf.itemdblclick) conf.itemdblclick.call(this, record, item, index, e, eOpts);
		    },
		    afterlayout: function(layout, eOpts ){
		    	if(conf.afterlayout) conf.afterlayout.call(this, layout, eOpts);
			},
			afterrender: function(eOpts ){
		    	if(conf.afterrender) conf.afterrender.call(this, eOpts);
			},
			beforerender: function(eOpts){
				if(conf.beforerender) conf.beforerender.call(this, eOpts);
			}
		}
	};
	Ext.apply(def, conf);

	return def;
};

//Init form items
HP.FromItmesConfig = function(conf, type){
	var fieldItems = [];
	if(conf.fieldItems !== undefined && conf.fieldItems.length>0){
	  	for(var i=0; i<conf.fieldItems.length; i++){
	  		if(type == 'line'){
	  			var header = {xtype:'container', border:false, layout:'column', cls:'form-container', flex: 1};
				var item = conf.fieldItems[i];
				if(item.xtype == 'hidden') {
                    header = HP.createFormItem(item);
                }else if(item.xtype == 'DateO2TField'){
                    header = HP.createFormItem(item, conf.fieldItems[i].type != undefined? conf.fieldItems[i].type: 'line');
				}else{
					header.items = HP.createFormItem(item, conf.fieldItems[i].type != undefined? conf.fieldItems[i].type: 'line');
				}
	    		fieldItems.push(header);
	  		}else{
				var item = conf.fieldItems[i];
				fieldItems.push(HP.createFormItem(item));
	  		}
	  	}
	  	
	  	var append = conf.appendItems || [];
		for (var i = 0; i < append.length; i++) {
			fieldItems.push(HP.createFormItem(append[i]));
		}
	  		  	
	  	return fieldItems;
    }
    return;
};


HP.createFormItem = function(item, type, level, fieldWidth, labelWidth){
	var subTitle = new Array();
	var subItem = new Array();
	
	for(var subkey in item){

		if(type == 'line' && subkey == 'title'){
			subTitle['text'] = item[subkey] + ': '; 
			subTitle['xtype'] = 'label'; 
			subTitle['cls'] = 'label';
		}else if(subkey == 'xtype'){
			if(item[subkey] == 'combo'){
				subItem['hiddenName'] = item['field'];
				subItem['name'] = item['field'];
				subItem['model'] = 'local';
				subItem['triggerAction'] = 'all';
				subItem['emptyText'] = _lang.TText.pleaseSelect;
				subItem['allowBlank'] = false;
				subItem['editable'] = false;
			}else if(item[subkey] == 'container'){
				subItem['items'] = new Array();
				for(var j=0; j<item['items'].length; j++){
					subItem['items'][j]= HP.createFormItem(item['items'][j], '', 2);
					item['items'][j] = subItem['items'][j];
				}
			}else if(item[subkey] == 'radiogroup'){
				if(item['data'] != undefined && item['data'].length){
					var subRgItems = [];
					for(var rgItem in item['data']){
						subRgItems.push({
							name: item['field'],
							inputValue: item['data'][rgItem][0],
							boxLabel: item['data'][rgItem][1],
							id: item['field'] + '-' + item['data'][rgItem][0]
						});
					}
					subItem['items'] = subRgItems;
				}
			}else if(item[subkey] == 'hidden'){
				subItem['hiddenName'] = item['field'];
				subItem['name'] = item['field'];
				subItem['cls'] = 'field-hidden';
			}else{
				subItem['name'] = item['field'];
			}
			subItem[subkey] = item[subkey];
			
		}else if(subkey != 'field' || level <= 2 && subkey != 'items'){
			subItem[subkey] = item[subkey];
		}
		
		subItem['width'] = item['width']? item['width']: fieldWidth? fieldWidth :150;
		subItem['labelWidth'] = item['labelWidth']? item['labelWidth']: labelWidth? labelWidth :130;
	}
	if(type == 'line'){
		if(item[subkey] == 'hidden' || subItem.xtype == 'DateO2TField'){
            return subItem;
			// return [[] , subItem];
		}else{
			return [subTitle , subItem];
		}
		
	}else{
		return subItem;
	}
};


/* ======================  */
Ext.define("App.DateO2TField", {
    extend: 'Ext.container.Container',
    alias: 'widget.DateO2TField',
    constructor : function(conf) {
        Ext.applyIf(this, conf);
        App.DateO2TField.superclass.constructor.call(this, {
            width: 478, layout: 'hbox',
            items: [
                {xtype: 'label', html: '<b>'+conf.fieldLabel+ ': </b>', labelWidth: 0,margin: '0', hideLabel: true, style:'padding-top: 3px;', width: 78},
                {xtype: conf.type ||  'datetimefield',name: conf.name + '-GTE', labelWidth: 45, width: 200, fieldLabel: _lang.TText.fDateFrom, format: conf.format || curUserInfo.dateFormat},
                {xtype: conf.type ||  'datetimefield',name: conf.name + '-LTE', labelWidth: 46, width: 200, fieldLabel: _lang.TText.fDateTo, format: conf.format ||  curUserInfo.dateFormat}
            ]
        });
    }
});

Ext.define("App.DictField", {
    extend: 'Ext.form.field.Display',
    alias: 'widget.dictfield',
    constructor : function(conf) {
    	Ext.applyIf(this, conf);
    	if(this.fields == undefined ){ this.fields =  ['id','title','codeMain','codeSub','options']; }
    	this.callback = function(op, records, eOpts, parent){
    		op.setValue(op.value);
    	};
    	this.url = __ctxPath + 'dict/getkey?code='+conf.code+'&codeSub='+conf.codeSub+'';

    	App.DictField.superclass.constructor.call(this, {
    		store: new $HpStore(this),
    		setValue: function(v){
    			var va = v;
    			if(this.store != undefined && this.store.data.items.length>0){
    				var options = this.store.data.items[0].data.options;
    				if(this.labelColors != undefined)
    					va = $dictRenderOutputColor(v, options, this.labelColors);
    				else
    					va = $dictRenderOutputColor(v, options);
    			}
    			this.value = v;
        		this.lastValue = va;
        		this.setRawValue(va);
        	}
    	});
    }
});

Ext.define("App.DictCombo", {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.dictcombo',
    
    constructor : function(conf) {
    	Ext.apply(this, conf);
    	this.dataType = conf.dataType || 'str';

    	if(this.fields == undefined ){ this.fields =  ['id','title','codeMain','codeSub','options']; }
        this.callback = function (op, records, eOpts, parent) {
            var options = op.store.data.items[0].data.options;
            op.store.removeAll();
            for (var i = 0; i < options.length; i++) {
                op.store.add({id: op.dataType=='int' ? parseInt(options[i].value): options[i].value.toString(), title: options[i].title});
            }
            if (op.addAll) {
                op.store.insert(0, {id: "", title: _lang.TText.vAll});
            }
            // if (op.allowBlank) {
            //     op.store.insert(0, {id: "", title: _lang.TText.pleaseSelect});
            // }
			if(!!op.value) {
                op.select(op.dataType == 'int' ? parseInt(op.value) : op.value.toString(), true);
            }
        };
    	
        var myStore = null;
    	var localData = _dict.getItemsByCode(conf.code, conf.codeSub);
		if(!! localData && localData.length>0){
			var items = localData[0].data;
            var $data = [];
            if (conf.addAll) {
                $data.push(['', _lang.TText.vAll]);
            }
            // if (conf.allowBlank) {
            //     $data.push(['', _lang.TText.pleaseSelect]);
            // }
            for(var index in items.options) {
                $data.push([this.dataType=='int' ? parseInt(items.options[index].value): items.options[index].value.toString(),items.options[index].title, items.codeMain, items.codeSub]);
            }
            myStore = new Ext.data.SimpleStore({ fields: this.fields, data: $data });
		}else {
            this.url = __ctxPath + 'dict/getkey?code=' + conf.code + '&codeSub=' + conf.codeSub + '';
            myStore = new $HpStore(this);
        }

    	var cmp = App.DictCombo.superclass.constructor.call(this, {
    	    valueField: this.fields[0], 
    	    displayField:this.fields[1],
    	    emptyText: _lang.TText.pleaseSelect,
    	    queryMode:'local',
    	    triggerAction:'all',
    	    editable:false,
			readOnly: this.readOnly || false,
    	    store: myStore,
            dataType: this.dataType || 'int',
    		listeners:{
    	         scope: this,
    	         select: function(combo, records, eOpts){
                     try{
    	        		 if (this.selectFun) {this.selectFun.call(combo, records, eOpts);}
    	        	 }catch(e){}
    	         },
    	         change: function(combo, newValue, oldValue, eOpts ){
    	        	 try{
    	        		 if(oldValue != newValue) this.select(newValue, true);
    	        	 }catch(e){}
    	         },
    	         beforeBlur:function(combo, records, eOpts){if (this.beforeBlurFun) {this.beforeBlurFun.call(combo, records, eOpts);}},
                 afterrender: function (combo, records, eOpts) {if (this.afterrenderFun) {this.afterrenderFun.call(combo, records, eOpts);}}
    	    },
    		displayTpl: Ext.create('Ext.XTemplate',
		        '<tpl for=".">',
		            '{title}',
		        '</tpl>'
		    )
    	});
    }
});

Ext.define("App.Comboremote", {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.comboremote',
    
    constructor : function(conf) {
    	Ext.applyIf(this, conf);
    	if(this.fields == undefined ){ this.fields =  ['id','title', 'isDefault']; }
    	this.callback = function(op, records, eOpts, parent){
    		if(op.addAll){op.store.insert(0, {id:"",title:_lang.TText.vAll});}
    		if(op.useDefault || false)
    		for(var index in records){
    		    if(!!records[index].data.isDefault && records[index].data.isDefault == 1){
                    op.setValue(records[index].data.id);
                    break;
                }
            }
            if(!op.getValue() && op.useDefault) op.setValue(records[0].data.id);
            if(!!op.subcallback) op.subcallback.call(op, records);
    	};
        this.baseParams={limit:100};
    	this.sorters= [{property: 'sort', direction: 'ASC'}];

    	App.Comboremote.superclass.constructor.call(this, {
    	    valueField: this.fields[0], 
    	    displayField:this.fields[1],
    	    emptyText: _lang.TText.pleaseSelect,
    	    queryMode:'local',
    	    triggerAction:'all',
    	    editable:false,
            // autoLoad: true,
            maskTo: this.id,
            fieldLabel: !!this.fieldLabel ?  this.fieldLabel  + ' <font color=red>*</font>' : '',
            readOnly: this.readOnly || false,
    	    store: new $HpStore(this),
            allowBlank: this.allowBlank != undefined ? this.allowBlank: false,
    		listeners:{
    	         scope: this,
    	         select: function(combo, records, eOpts){
    	        	 try{
    	        		 if (this.selectFun) {this.selectFun.call(combo, records, eOpts);}
    	        	 }catch(e){}
    	         },
    	        beforeBlur:function(combo, records, eOpts){if (this.beforeBlurFun) {this.beforeBlurFun.call(combo, records, eOpts);}}
    	    },

    		displayTpl: Ext.create('Ext.XTemplate',
		        '<tpl for=".">',
		            '{title}',
		        '</tpl>'
		    )
    	});
    },
});

Ext.define("App.ComboDny", {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.combodny',

    constructor : function(conf) {
        Ext.applyIf(this, conf);
        if(this.fields == undefined ){ this.fields =  ['id','title', 'isDefault']; }
        if(conf.allowBlank !== undefined ){ this.allowBlank =  conf.allowBlank; }

        try {
            App.Comboremote.superclass.constructor.call(this, {
                valueField: this.fields[0] || 'id',
                displayField: this.fields[1] || 'text',
                emptyText: _lang.TText.pleaseSelect,
                queryMode: 'local',
                triggerAction: 'all',
                editable:false,
            })
        }catch(e){alert(e)};
    },
    setLocalSource: function (value, initSelected) {
        this.store.removeAll();
        this.setValue('');
        this.setRawValue('');
        if (!!value && value.length) {
            var $data = [];
            for (var index in value) {
                this.store.add({id:value[index].id, title:value[index].title});
            }
            if(initSelected) {
                this.setValue(value[0].id);
                this.setRawValue(value[0].title);
            }
            this.show();
        }
    },
    initComponent: function () {
        this.store = new Ext.data.SimpleStore({mode:'local', fields: this.fields});
        this.callParent(arguments);
    }

});

Ext.define("App.core.Extends.Section", {
	extend: 'Ext.Panel',
    alias: 'widget.section',
    requires: ['Ext.XTemplate'],
    cls: 'section-panel',
    constructor : function(conf) {    	
    	var tpl = new Ext.XTemplate(
    		'<div class="field-section"><span>{title}</span>',
			'</div>'
		);
    	var data = { title: conf.title };
    	tpl.overwrite(this, data);
    	
    	App.core.Extends.Section.superclass.constructor.call(this, {
    		height: 25,
    		widthLabel:0,
    		border:false,
    		htmlEncode: false,
    		html: this.innerHTML
    	});
    }
});

Ext.define("App.core.Extends.Checkboxgroup", {
    extend: 'Ext.Panel',
    alias: 'widget.checkboxremote',
    
    constructor : function(conf) {
    	Ext.applyIf(this, conf);
    	if(this.fields == undefined){ this.fields =  ['id','title','codeMain','codeSub','options']; }
		this.callback = function(op, records, eOpts, parent){
        	if(records == undefined || records.length <1) return;
        	
        	var functionsO = conf.conf.functions;
        	var functionsRows = [];
        	if(functionsO != undefined && functionsO.length>0){
        		for(i in functionsO){
        			for(j in functionsO[i]){
        				if(functionsO[i][j] == undefined && !functionsO[i][j]) break;
        				var map =functionsO[i][j].split('|');
        				if(j==0) functionsRows[map[0]]=[];
        				functionsRows[map[0]][map[1]]=true;
        			}
        		}
        	}
        	
    		for(i in records){
    			var row = records[i].getData();
    			var subItems=[];
    			subItems.push({id:'model-'+i, boxLabel: _lang.TText.vAll, name: 'main.functions['+i+'][0]', inputValue: row.codeSub + '|' + row.codeSub,
    				checked: functionsRows[row.codeSub]!=undefined && functionsRows[row.codeSub][row.codeSub]!=undefined ? functionsRows[row.codeSub][row.codeSub] & true : false,
    				listeners:{
    					change: function(eOpts, newValue, oldValue){
    						var objs = Ext.getCmp(conf.conf.formPanelId).getCmpByName(this.name).ownerCt.items.items;
    						var z = 1;
    						while(z<objs.length){
    							var obj = jQuery('#'+objs[z].id);
    							if(newValue){
    								obj.addClass("x-form-cb-checked");
    								objs[z].checked = true;
    							}else{
    								obj.removeClass("x-form-cb-checked");
    								objs[z].checked = false;
    							}
    							z++;
    						}
    					}
    				}
    			});
    			
    			if(row.options.length>0){
    				for(var y=0; y<row.options.length; y++){
    					var subrow = row.options[y];
    					subItems.push({id:'model-'+i+'-'+y, boxLabel: subrow.title, name: 'main.functions['+i+']['+(y+1)+']', inputValue: row.codeSub + '|' +subrow.value,
    						checked: functionsRows[row.codeSub]!=undefined && functionsRows[row.codeSub][subrow.value]!=undefined ? functionsRows[row.codeSub][subrow.value] & true : false,
    						listeners:{
    	    					change: function(eOpts, newValue, oldValue){
    	    						var z=1;
    	    						var mainId = this.id.substr(0,this.id.lastIndexOf('-'));
    	    						var action = true;
    	    						var objs = Ext.getCmp(conf.conf.formPanelId).getCmpByName(this.name).ownerCt.items.items;
    								while(z < objs.length){
    									if(objs[z].checked == false){ action = false; }
    									z++;
    								}
    								
    								if(action){
    									jQuery('#'+mainId).addClass("x-form-cb-checked");
    									objs[0].checked = true;
    								}else{
    									jQuery('#'+mainId).removeClass("x-form-cb-checked");
    									objs[0].checked = false;
    								}
    	    					}
    	    				}    						
    					});
    				}
    			}

    			op.add(new Ext.form.CheckboxGroup(
    				{ xtype:'fieldcontainer', fieldLabel:row.title, columns:5, defaultType: 'checkboxfield', items:subItems}
    			));
    			op.doLayout();
    		}
		};
    	
		App.core.Extends.Checkboxgroup.superclass.constructor.call(this, {
			fieldLabel: this.fieldLabel,
			layout:'form',
			border:false,
    		store: new $HpStore(this)
    	});
    }
});

Ext.define("Ext.ux.form.field.CKEditor", {
    extend: 'Ext.form.field.TextArea',
    alias: 'widget.ckeditor',
    
    constructor : function() {
    	this.callParent(arguments);
    	this.addEvents("instanceReady");
    },
    
    initComponent: function () {
        this.callParent(arguments);
        this.on("afterrender", function(){
        	if(!this.CKConfig) this.CKConfig={};
        	this.CKConfig.width = this.width? this.width: '100%';
        	this.CKConfig.height = this.height? this.height : '100%';
        	
        	var SmallMenu = [
        	        { name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
             		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
             		{ name: 'insert', groups: [ 'insert' ] },
             		{ name: 'tools', groups: [ 'tools' ] },
					{ name: 'others', groups: [ 'others' ] }
            ];
        	var BigMenu = [
        	        { name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
					{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
					{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
					{ name: 'forms', groups: [ 'forms' ] },
					{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
					{ name: 'colors', groups: [ 'colors' ] },
					{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
					{ name: 'styles', groups: [ 'styles' ] },
					{ name: 'links', groups: [ 'links' ] },
					{ name: 'insert', groups: [ 'insert' ] },
					{ name: 'tools', groups: [ 'tools' ] },
					{ name: 'others', groups: [ 'others' ] },
					{ name: 'about', groups: [ 'about' ] }
        	];
        	
            var config ={
                toolbarGroups: this.menu == 'small'? SmallMenu: BigMenu,
                enterMode: CKEDITOR.ENTER_BR,
              	shiftEnterMode: CKEDITOR.ENTER_P,
                toolbarCanCollapse: true,
                zIndex: 19000,
                baseFloatZIndex: 19001,
                toolbarStartupExpanded: this.expanded === undefined? true : this.expanded,
                contentsCss: __ctxPath +'js/lib/ckeditor/contents.css',
                removeButtons: this.menu == 'small'?
                		 'Templates,Save,NewPage,Preview,Undo,Redo,Cut,Copy,SelectAll,Scayt,Form,Radio,Checkbox,TextField,Textarea,Select,Button,ImageButton,HiddenField,BidiLtr,BidiRtl,Language,Anchor,Flash,Table,PageBreak,Iframe,Styles,About,CreateDiv,Blockquote,Find,Replace,Bold,Italic,Underline,Strike,Subscript,Superscript,RemoveFormat,NumberedList,BulletedList,Outdent,Indent,JustifyLeft,JustifyCenter,JustifyRight,JustifyBlock,Link,Unlink,HorizontalRule,Smiley,SpecialChar,Format,Font,FontSize,ShowBlocks,TextColor,BGColor,Maximize'
                		:'Templates,Save,NewPage,Preview,Undo,Redo,Cut,Copy,SelectAll,Scayt,Form,Radio,Checkbox,TextField,Textarea,Select,Button,ImageButton,HiddenField,BidiLtr,BidiRtl,Language,Anchor,Flash,Table,PageBreak,Iframe,Styles,About,CreateDiv,Blockquote,Maximize'

            };
            Ext.applyIf(config, this.CKConfig);
             
            this.editor = CKEDITOR.replace(this.inputEl.id, config);
            this.editor.name = this.name;//把配置中的name属性赋值给CKEditor的name属性
            this.editor.on("instanceReady", function(){
                this.fireEvent("instanceReady", this, this.editor);//触发instanceReady事件
            }, this);
        }, this);
        
    },
    onRender: function (ct, position) {
        if (!this.el) {
            this.defaultAutoCreate = {
                tag: 'textarea',
                autocomplete: 'off'
            };
        }
        this.callParent(arguments);
    },
    setValue: function (value) {
        this.callParent(arguments);
        if (this.editor) {
            this.editor.setData(value);
        }
    },
    getRawValue: function () {//要覆盖getRawValue方法，否则会取不到值
        if (this.editor) {
            return this.editor.getData();
        } else {
            return '';
        }
    },
    getValue: function () {
        return this.getRawValue();
    }
});

Ext.define("App.core.Extends.Toolbar", {
    alias: 'App.toolbar',
    	
    constructor : function(conf) {
    	Ext.applyIf(this, conf);

    	var toolbars = [];
    	if(this.treeRefresh)
    		toolbars.push({text: _lang.TButton.refresh, iconCls:'fa fa-fw fa-refresh', scope: this, handler:function(){ this.treeRefreshRs.call(this); }});
    	
    	//functions
    	if(this.refresh)
    		toolbars.push({text: _lang.TButton.refresh, iconCls:'fa fa-fw fa-refresh', scope: this, handler:function(){ this.refreshRs.call(this); }});
    	if(this.close)
    		toolbars.push({text: _lang.TButton.close, iconCls:'fa fa-fw fa-close', scope: this, handler:function(){ this.closeRs.call(this); }});
    	if(this.reset)
    		toolbars.push({text: _lang.TButton.reset, iconCls:'fa fa-fw fa-undo', scope: this, handler:function(){ this.resetRs.call(this); }});
    	if(this.ok)
    		toolbars.push({text: _lang.TButton.ok, iconCls:'fa fa-fw fa-check', scope: this, handler:function(){ this.okRs.call(this); }});
    	if(this.clean)
    		toolbars.push({text: _lang.TButton.clean, iconCls:'fa fa-fw fa-eraser', scope: this, handler:function(){ this.cleanRs.call(this); }});
    	if(this.cancel)
    		toolbars.push({text: _lang.TButton.cancel, iconCls:'fa fa-fw fa-close', scope: this, handler:function(){ this.cancelRs.call(this); }});
        if(this.receive)
            toolbars.push({text: _lang.TButton.receive, iconCls:'fa fa-fw fa-cloud-download', scope: this, handler:function(){ this.receiveRs.call(this); }});
    	if((this.saveAbs || $permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":normal:edit") || $permissionCheck(this.moduleName + ":flow:start")) && this.save)
    		toolbars.push({text: _lang.TButton.save, iconCls:'fa fa-fw fa-save', scope: this, handler:function(){ this.saveRs.call(this); }});
    	if(($permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":normal:edit") || $permissionCheck(this.moduleName + ":flow:start")) && this.send)
    		toolbars.push({text: _lang.TButton.send, iconCls:'fa fa-fw fa-send', scope: this, handler:function(){ this.sendRs.call(this); }});
        if(($permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":normal:edit") || $permissionCheck(this.moduleName + ":flow:start")) && this.sendTo)
            toolbars.push({text: _lang.TButton.send, iconCls:'fa fa-fw fa-send', scope: this, handler:function(){ this.sendToRs.call(this); }});
    	if(($permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":normal:edit")) && this.saveAs)
    		toolbars.push({text: _lang.TButton.saveAs, iconCls:'fa fa-fw fa-clipboard', scope: this, handler:function(){ this.saveAsRs.call(this); }});
    	
    	if(($permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":flow:start"))&& this.add)
    		toolbars.push({text: _lang.TButton.add, iconCls:'fa fa-fw fa-plus', scope: this, handler:function(){ this.addRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":flow:start") && this.flowStart)
    		toolbars.push({text: _lang.TButton.start, iconCls:'fa fa-fw fa-play', scope: this, handler:function(){ this.flowStartRs.call(this); }});

    	if(($permissionCheck(this.moduleName + ":normal:add") || $permissionCheck(this.moduleName + ":flow:start")) && this.copy)
    		toolbars.push({text: _lang.TButton.copy, iconCls:'fa fa-fw fa-copy', scope: this, handler:function(){ this.copyRs.call(this); }});
    	if(($permissionCheck(this.moduleName + ":normal:edit") || $permissionCheck(this.moduleName + ":flow:start")) && this.edit)
    		toolbars.push({text: _lang.TButton.edit, iconCls:'fa fa-fw fa-pencil-square-o', scope: this, handler:function(){ this.editRs.call(this);}});
        if($permissionCheck(this.moduleName + ":normal:confirm") && this.confirm)
            toolbars.push({text: _lang.TButton.confirm, iconCls:'fa fa-fw fa-check', scope: this, handler:function(){ this.confirmRs.call(this); }});
        if(this.reply)
            toolbars.push({text: _lang.TButton.reply, iconCls:'fa fa-fw fa-reply', scope: this, handler:function(){ this.replyRs.call(this); }});
        if(this.replyall)
            toolbars.push({text: _lang.TButton.replyall, iconCls:'fa fa-fw fa-reply-all', scope: this, handler:function(){ this.replyAllRs.call(this); }});
        if(this.forward)
            toolbars.push({text: _lang.TButton.forward, iconCls:'fa fa-fw fa-share', scope: this, handler:function(){ this.forwardRs.call(this); }});
        if(($permissionCheck(this.moduleName + ":normal:del") || $permissionCheck(this.moduleName + ":flow:start")) && this.del)
    		toolbars.push({text: _lang.TButton.del, iconCls:'fa fa-fw fa-trash', scope: this, handler:function(){ this.delRs.call(this);}});
        if(($permissionCheck(this.moduleName + ":normal:del") || $permissionCheck(this.moduleName + ":flow:start")) && this.delSel)
            toolbars.push({text: _lang.TButton.del, iconCls:'fa fa-fw fa-trash', scope: this, handler:function(){ this.delSelRs.call(this);}});

        if(this.preview)
            toolbars.push({text: _lang.TButton.preview, iconCls:'fa fa-fw fa-eye', scope: this, handler:function(){ this.previewRs.call(this); }});

    	// tree functions
    	if($permissionCheck(this.moduleName + ":normal:add") && this.treeAdd)
    		toolbars.push({text: _lang.TButton.add, iconCls:'fa fa-fw fa-plus', scope: this, handler:function(){ this.treeAddRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":normal:add") && this.treeInsert)
    		toolbars.push({text: _lang.TButton.sub, iconCls:'fa fa-fw fa-indent', scope: this, handler:function(){ this.treeInsertRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeEdit)
    		toolbars.push({text: _lang.TButton.edit, iconCls:'fa fa-fw fa-pencil-square-o', scope: this, handler:function(){ this.treeEditRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeSave)
    		toolbars.push({text: _lang.TButton.save, iconCls:'fa fa-fw fa-save', scope: this, handler:function(){ this.treeSaveRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeUp)
    		toolbars.push({text: _lang.TButton.up, iconCls:'fa fa-fw fa-chevron-up', scope: this, handler:function(){ this.treeUpRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeDown)
    		toolbars.push({text: _lang.TButton.down, iconCls:'fa fa-fw fa-chevron-down', scope: this, handler:function(){ this.treeDownRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeLeft)
    		toolbars.push({text: _lang.TButton.left, iconCls:'fa fa-fw fa-chevron-left', scope: this, handler:function(){ this.treeLeftRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:edit") && this.treeRight)
    		toolbars.push({text: _lang.TButton.right, iconCls:'fa fa-fw fa-chevron-right', scope: this, handler:function(){ this.treeRightRs.call(this);}});
    	if($permissionCheck(this.moduleName + ":normal:del") && this.treeDel)
    		toolbars.push({text: _lang.TButton.del, iconCls:'fa fa-fw fa-trash', scope: this, handler:function(){ this.treeDelRs.call(this);}});

    	// flow
    	if($permissionCheck(this.moduleName + ":flow:allow") && this.flowAllow)
    		toolbars.push({text: _lang.TButton.allow, iconCls:'fa fa-fw fa-check-square', scope: this, handler:function(){ this.flowAllowRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":flow:refuse") && this.flowRefuse)
    		toolbars.push({text: _lang.TButton.refuse, iconCls:'fa fa-fw fa-window-close', scope: this, handler:function(){ this.flowRefuseRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":flow:back") && this.flowBack)
    		toolbars.push({text: _lang.TButton.back, iconCls:'fa fa-fw fa-reply', scope: this, handler:function(){ this.flowBackRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":flow:redo") && this.flowRedo)
    		toolbars.push({text: _lang.TButton.redo, iconCls:'fa fa-fw fa-step-backward', scope: this, handler:function(){ this.flowRedoRs.call(this); }});
    	if($permissionCheck(this.moduleName + ":flow:hold") && this.flowHold)
    		toolbars.push({text: _lang.TButton.hold, iconCls:'fa fa-fw fa-lock', scope: this, handler:function(){ this.flowHoldRs.call(this); }});
        if($permissionCheck(this.moduleName + ":flow:unhold") && this.flowUnhold)
            toolbars.push({text: _lang.TButton.unhold, iconCls:'fa fa-fw fa-unlock', scope: this, handler:function(){ this.flowUnholdRs.call(this); }});
        if($permissionCheck(this.moduleName + ":flow:cancel") && this.flowCancel)
            toolbars.push({text: _lang.TButton.flowCancel, iconCls:'fa fa-fw fa-close', scope: this, handler:function(){ this.flowCancelRs.call(this); }});

        if($permissionCheck(this.moduleName + ":normal:export") && this.export) {
            toolbars.push('-');
            toolbars.push({ text: _lang.TButton.export, iconCls: 'fa fa-fw fa-sticky-note-o', id: (this.preId ? this.preId: this.moduleName) + '-bt-export', scope: this,
				menu: [
                    {text: _lang.TText.exportToPdf, iconCls: 'fa fa-file-pdf-o', scope: this, hidden: !!this.exportConf? this.exportConf.pdf != undefined ? this.exportConf.pdf: false: false, handler: function (){this.exportRs.call(this, 'pdf');}},
                    {text: _lang.TText.exportToXls, iconCls: 'fa fa-file-excel-o', scope: this, hidden: !!this.exportConf? this.exportConf.xls != undefined ? this.exportConf.xls: false: false, handler: function (){this.exportRs.call(this, 'xls');}},
				]
            });
        }
        if($permissionCheck(this.moduleName + ":normal:export") && this.expdoc) {
            toolbars.push('-');
            toolbars.push({ text: _lang.TButton.expdoc, iconCls: 'fa fa-file-word-o', scope: this, handler: function () { this.exportRs.call(this, 'doc');}})
        }

        if($permissionCheck(this.moduleName + ":normal:email") && this.email)
            toolbars.push({text: _lang.TButton.mail, iconCls:'fa fa-fw fa-envelope-o', id:this.moduleName+'-bt-email', scope: this, handler:function(){ this.emailRs.call(this); }});

        if($permissionCheck(this.moduleName + ":normal:email") && this.tomail)
            toolbars.push({text: _lang.TButton.mail, iconCls:'fa fa-fw fa-envelope-o', id:this.moduleName+'-bt-email', scope: this, handler:function(){ this.emailRs.call(this, 'tomail'); }});


        if(this.treeExpand){
            toolbars.push('->');
            toolbars.push({text: _lang.TButton.expand, iconCls: 'fa fa-fw fa-expand', scope: this, handler:function(){ this.myExpandAll.call(this); }});
            toolbars.push('-');
            toolbars.push({text: _lang.TButton.collapse, iconCls: 'fa fa-fw fa-compress', scope: this, handler:function(){ this.myCollapseAll.call(this);}});
        }

    	if(this.flowMine){
    		toolbars.push('->');
	    	toolbars.push({text: _lang.TButton.mine, iconCls:'fa fa-fw fa-male', scope: this, handler:function(){ this.flowMineRs.call(this); }});
	    	// if(this.flowInvolved)
	    	// 	toolbars.push({text: _lang.TButton.involved, iconCls:'fa fa-fw fa-users', scope: this, handler:function(){ this.flowInvolvedRs.call(this); }});
            toolbars.push({text: _lang.TButton.normal, iconCls:'fa fa-fw fa-th', scope: this, handler:function(){ this.flowNormalRs.call(this); }});
    	}
    	
    	if(this.processRemark || this.processHistory || this.processDirection){
    		toolbars.push('->');
	    	if(this.processRemark)
	    		toolbars.push({text: _lang.TButton.processRemark, iconCls:'fa fa-fw fa-male', scope: this, handler:function(){ this.processRemarkRs.call(this); }});
	    	if(this.processHistory)
	    		toolbars.push({text: _lang.TButton.processHistory, iconCls:'fa fa-fw fa-users', scope: this, handler:function(){ this.processHistoryRs.call(this); }});
	    	if(this.processDirection)
	    		toolbars.push({text: _lang.TButton.processDirection, iconCls:'fa fa-fw fa-users', scope: this, handler:function(){ this.processDirectionRs.call(this); }});
    	}

    	var def= {cls:'my-toolbar', items: toolbars};
        this.toolbarId ? def.id=this.toolbarId: '';
    	return Ext.create("Ext.toolbar.Toolbar", def);
    },
    
    //
    refreshRs : function() {
    	if(this.refreshFun) this.refreshFun.call(this); 
    	else {
    		if(this.mainGridPanelId){Ext.getCmp(this.mainGridPanelId).getStore().reload();}
    	}
    },
    okRs : function() {if(this.okFun) this.okFun.call(this); else this.cancelRs();},
    addRs : function(){ if(this.addFun) this.addFun.call(this); else this.doEditRs('add');},
    receiveRs : function(){ if(this.receiveFun) this.receiveFun.call(this); else Ext.emptyFn;},
    editRs : function(){ if(this.doEditFun) this.doEditFun.call(this); else this.doEditRs('edit');},
    sendRs : function(){ if(this.sendFun) this.sendFun.call(this); else this.doEditRs('send');},
    sendToRs : function(){ if(this.sendToFun) this.sendToFun.call(this); else this.saveRs('send');},
    copyRs : function(){ if(this.copyFun) this.copyFun.call(this); else this.doEditRs('copy');},
    replyRs : function(){ if(this.replyFun) this.replyFun.call(this); else this.doEditRs('reply');},
    replyAllRs : function(){ if(this.replyallFun) this.replyallFun.call(this); else this.doEditRs('replyall');},
    forwardRs : function(){ if(this.forwardFun) this.forwardFun.call(this); else this.doEditRs('forward');},
    previewRs : function(){ if(this.previewFun) this.previewFun.call(this); else this.doEditRs('preview');},
    resetRs : function(){ if(this.resetFun) this.resetFun.call(this); else Ext.getCmp(this.mainFormPanelId).form.reset();},
    cleanRs : function(){ 
    	if(this.cleanFun) this.cleanFun.call(this); 
    	else{
    		if(Ext.getCmp(this.winId)){
    			Ext.getCmp(this.winId).close();
    			if (this.callback) {
    				this.callback.call(this, '', '');
    			}
    		}
    		else if(Ext.getCmp(this.frameId)) Ext.getCmp(this.frameId).close();
    	}
    },
    closeRs : function(){ 
    	if(this.closeFun) this.closeFun.call(this); 
    	else{
    		if(Ext.getCmp(this.winId)) Ext.getCmp(this.winId).close();
    		else if(Ext.getCmp(this.frameId)) Ext.getCmp(this.frameId).close();
    	}
    },
    cancelRs : function(){ 
    	if(this.cancelFun) this.cancelFun.call(this); 
    	else{
    		if(Ext.getCmp(this.winId)) Ext.getCmp(this.winId).close();
    		else if(Ext.getCmp(this.frameId)) Ext.getCmp(this.frameId).close();
    	}
    },
    
    saveAsRs : function(){
    	var me = this;
    	Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureCopy, function(btn) {
    		if (btn == 'yes') {
		    	if(me.copyFun)
		    		me.copyFun.call(me);
		    	else{
		    		$postForm({
						formPanel: Ext.getCmp(me.mainFormPanelId),
						grid: me.subGridPanelId != undefined ? Ext.getCmp(me.subGridPanelId) : Ext.getCmp(me.mainGridPanelId),
						scope: me,
						url: me.urlSave,
						params : {act: this.actionName ? this.actionName: 'copy'},
						callback: function(fp, action) {
							me.refreshRs.call(me);
							Ext.getCmp(me.mainFormPanelId).form.reset();
						}
					});
		    	}
    		}
    	});
    },
    
    saveRs : function(action) {
    	if(this.saveFun) 
    		this.saveFun.call(this, action);
    	else{
			$postForm({
				formPanel: Ext.getCmp(this.mainFormPanelId),
				grid: this.subGridPanelId != undefined ? Ext.getCmp(this.subGridPanelId) : Ext.getCmp(this.mainGridPanelId),
				scope: this,
				url: this.urlSave,
				params : {act: action? action: this.actionName ? this.actionName: 'save'},
				callback: function(fp, action) {
					this.refreshRs.call(this);
					var formPanel = Ext.getCmp(this.mainFormPanelId);
					formPanel.form.reset();
					if(formPanel.closeWin != undefined && formPanel.closeWin){
						Ext.getCmp(this.winId).close();
					}
					
				}
			});
    	}
    },

    confirmRs : function(){ if(this.confirmFun) this.confirmFun.call(this); else this.saveRs('confirm');},

    doEditRs : function(action){
    	if (this.doEditFun){
    		this.doEditFun.call(this);
    	}else{
			var cmpPanel = Ext.getCmp(this.mainGridPanelId);
			var selRs = cmpPanel.getSelectionModel().selected.items;
			var me = this;
			if ((action == 'add' || selRs && selRs.length == 1)) {
				if(this.editFun != undefined ){
					this.editFun.call(me, {
						scope : this,
						action: action?action:'edit',
						grid:cmpPanel,
						record: action != 'add' && selRs.length? selRs[0].data : '',
						callback: function(fp, action){
							me.refreshRs.call(me);
						}
					});
				}else{
					Ext.getCmp(this.mainFormPanelId).form.reset();
				}
				
			}else if (selRs.length == 0) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
			}else{
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
			}
	    }
	},

    delRs : function(){
    	if (this.delFun){
    		this.delFun.call(this);
    	}else{
			var cmpPanel = Ext.getCmp(this.mainGridPanelId);
			var selRs = cmpPanel.getSelectionModel().selected.items;
			
			if (selRs && selRs.length == 1) {
				$delGridRs({
					url: this.urlDelete, scope: this, grid: cmpPanel, idName: 'id',
					callback : function(fp, action) {
						this.refreshRs.call(this);
					}
				});
			}else if (selRs.length == 0) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
			}else{
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
			}
    	}
	},

    delSelRs : function(){
        if (this.delSelFun){
            this.delSelFun.call(this);
        }else{
            var cmpPanel = Ext.getCmp(this.mainGridPanelId);
            $delGridRs({
                url: this.urlDelete, scope: this, grid: cmpPanel, idName: 'id',
                callback : function(fp, action) {
                    this.refreshRs.call(this);
                }
            });

        }
    },

    exportRs: function(action){
        if (this.exportFun){
            this.exportFun.call(this, action);
        }else{
            if(action == 'pdf' || action == 'xls' || action == 'csv') {
                var cmp = Ext.getCmp(this.mainGridPanelId);
                var params = cmp.getStore().proxy.extraParams;
                $getExportFieldsFromGrid(cmp, params);
                params['act'] = action;
                params['fileTitle'] = cmp.title;
                params['fileName'] = cmp.id;
                $postUrl({
                    grid: cmp, url: this.urlExport, maskTo: this.frameId, params: params,
                    callback: function (response, eOpts) {
                        var json = Ext.JSON.decode(response.responseText)
                        if (json.success) new FilesPreviewDialog({fileId: json.data.id}).show();
                    }
                });
            }else {
                var cmp = Ext.getCmp(this.mainFormPanelId);
                var params = {act: action, id: cmp.getCmpByName('id').getValue(), fileTitle:Ext.getCmp(this.winId).title, fileName:cmp.id};
                $postUrl({
                    url: this.urlExport, maskTo: this.winId, params: params,
                    callback: function (response, eOpts) {
                        var json = Ext.JSON.decode(response.responseText)
                        if (json.success && json.data && action=='doc') new FilesPreviewDialog({fileId: json.data.id}).show();
                    }
                })
            }
        }
	},

    emailRs : function(action, fileId){
        if (this.emailFun) {
            this.emailFun.call(this);
        }else if(action =='tomail'){
            if(this.tomailFun){
                this.tomailFun.call(this, function(fileId){
                    new EmailSendDialogWin({
                        scope : this, fileId: fileId, action: action,
                        callback: function(fp, action){
                            me.refreshRs.call(me);
                        }
                    }).show();
                });
            }else{
                var cmp = Ext.getCmp(this.mainFormPanelId);
                var params = {act: 'edoc', id: cmp.getCmpByName('id').getValue(), fileTitle:this.title, fileName:this.mainFormPanelId};
                $postUrl({
                    url: this.urlExport, maskTo: this.winId, params: params,
                    callback: function (response, eOpts) {
                        var json = Ext.JSON.decode(response.responseText)
                        if (json.success && json.data){
                            new EmailSendDialogWin({
                                scope : this, fileId: json.data.id, action: action,
                                callback: function(fp, action){
                                    me.refreshRs.call(me);
                                }
                            }).show();
                        }
                    }
                })
            }

        }else{
            var cmpPanel = Ext.getCmp(this.mainGridPanelId);
            var selRs = cmpPanel.getSelectionModel().selected.items;
            var me = this;
            if ((action == 'add' || selRs && selRs.length == 1)) {
                new EmailSendDialogWin({
                    scope : this,
                    grid:cmpPanel,
                    record: selRs.length? selRs[0].data : '',
                    callback: function(fp, action){
                        me.refreshRs.call(me);
                    }
				}).show();
            }else if (selRs.length == 0) {
                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
            }else{
                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
            }
        }
    },
	
	//
    treeAddRs : function(){ this.treeDoEditRs('add');},
    treeInsertRs : function(){this.treeDoEditRs('ins');},
    treeEditRs : function(){this.treeDoEditRs('edit');},
    treeUpRs : function(){this.treeMoveRs('up');},
	treeDownRs : function(){this.treeMoveRs('down');},
	treeLeftRs : function(){this.treeMoveRs('left');},
	treeRightRs : function(){this.treeMoveRs('right');},
	myExpandAll : function(){if (this.expandAllFun){this.expandAllFun.call(this);}else{Ext.getCmp(this.treePanelId).expandAll();}},
	myCollapseAll : function(){if (this.collapseAllFun){this.collapseAllFun.call(this);}else{Ext.getCmp(this.treePanelId).collapseAll();}},
	
	
	treeRefreshRs : function(parentNode){
		Ext.Ajax.request({
			url : this.urlList,scope : this,method: 'post',
			success : function(response, options) {
    			var obj = Ext.JSON.decode(response.responseText);

    			if(obj.success == true){
    				var arr = obj.data;
    				var cmpPanel = Ext.getCmp(this.treePanelId);
    				var currentNode = cmpPanel.getSelectionModel().getLastSelected();
    				cmpPanel.setRootNode({ text: 'Root', expanded: true, children: arr });
    				
    				if(currentNode == null){
    					if(parentNode != null) {
    						currentNode = parentNode;
    					}else{
    						currentNode = cmpPanel.getRootNode();
    					}
    				}
    				var path = currentNode.getPath('id');
    				
    				cmpPanel.expandPath(path);
    				cmpPanel.selectPath(path);
    				
    			}else{
    				Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg);
    			}    			
    		}
		});
	},
		
	treeDelRs : function(){
		var cmpPanel = Ext.getCmp(this.treePanelId);
		var selRs = cmpPanel.getSelectionModel().selected.items;
		
		if (selRs && selRs.length == 1) {
			var currentNode = cmpPanel.getSelectionModel().getLastSelected();
			var parentNode = currentNode.parentNode;
			
			$delGridRs({
				url : this.urlDelete,
				scope : this,
				grid : cmpPanel,
				idName : 'id',
				callback : function(fp, action) {
					this.treeRefreshRs.call(this, parentNode);
				}
			});
		}else if (selRs.length == 0) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		}else{
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
		}
	},
	
	treeDoEditRs : function(action){
		var cmpPanel = Ext.getCmp(this.treePanelId);
		var selRs = cmpPanel.getSelectionModel().selected.items;
		var me = this;
		
		if (selRs && selRs.length == 1) {
			var currentNode = cmpPanel.getSelectionModel().getLastSelected();
			var parentNode = currentNode.parentNode;
			
			this.editFun.call(me, {
				scope : me,
				action: action,
				grid: cmpPanel,
				parentNode: parentNode,
				record: selRs[0].data,
				callback: function(fp, action){
					me.treeRefreshRs.call(me, parentNode);
				}
			});
		}else if (selRs.length == 0) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		}else{
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
		}
	},
	
	treeSaveRs: function(){
		if (this.treeSaveFun){
    		this.treeSaveFun.call(this);
    	}else{
    		var me = this;
    		$postForm({
    			formPanel : Ext.getCmp(this.formPanelId),
    			grid :  this.grid,
    			scope : this,
    			url : this.urlSave,
    			params : {act: this.actionName ? this.actionName: ''},
    			callback : function(fp, action) {
    				me.treeRefreshRs.call(me, this.parentNode);
    				if(Ext.getCmp(this.winId)) Ext.getCmp(this.winId).close();
    			}
    		});
    	}
	},
	
	treeMoveRs : function(action){
		var cmpPanel = Ext.getCmp(this.treePanelId);
		var ids = $editGridRs({grid: cmpPanel, idName: 'id'});
		
		if (ids && ids.length > 0) {
			var currentNode = cmpPanel.getSelectionModel().getLastSelected();
			var parentNode = currentNode.parentNode;
			
			$postUrl({
				scope : this,
				url : this.urlSave,
				params : {act: action, 'id': ids},
				callback : function(fp, action) {
					this.treeRefreshRs.call(this, parentNode);
				}
			});
		}
	},
	
	flowStartRs: function(){ if(this.flowStartFun) this.flowStartFun.call(this); else this.flowSaveRs('start'); },
	flowAllowRs: function(){if(this.flowAllowFun) this.flowAllowFun.call(this); else this.flowSaveRs('allow'); },
	flowRefuseRs: function(){ if(this.flowRefuseFun) this.flowRefuseFun.call(this); else this.flowSaveRs('refuse');},
	flowRedoRs: function(){ if(this.flowRedoFun) this.flowRedoFun.call(this); else this.flowSaveRs('redo');},
	flowBackRs: function(){ if(this.flowBackFun) this.flowBackFun.call(this); else this.flowSaveRs('back');},
    flowHoldRs: function(){ if(this.flowHoldFun) this.flowHoldFun.call(this); else this.flowSaveRs('hold');},
    flowUnholdRs: function(){ if(this.flowUnholdFun) this.flowUnholdFun.call(this); else this.flowSaveRs('unhold');},
    flowCancelRs: function(){ if(this.flowCancelFun) this.flowCancelFun.call(this); else{
        var me = this;
        Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureCancellation, function(btn) {
            if (btn == 'yes') { me.flowSaveRs('cancel');}
        });
    } },
	flowSaveRs : function(action) {
        var me = this;
        if(action == 'refuse'){
            Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureRefuse, function(btn) {
                if (btn == 'yes') {
                    if(me.flowSaveFun)
                        me.flowSaveFun.call(me, !!action? action: this.actionName);
                    else{
                        $postForm({
                            formPanel: Ext.getCmp(me.mainFormPanelId),
                            grid: me.subGridPanelId != undefined ? Ext.getCmp(me.subGridPanelId) : Ext.getCmp(me.mainGridPanelId),
                            scope: me,
                            url: me.urlFlow,
                            params : {act: action? action:'save'},
                            callback: function(fp, action) {
                                me.refreshRs.call(me);
                                var formPanel = Ext.getCmp(this.mainFormPanelId);
                                formPanel.form.reset();
                                if(formPanel.closeWin != undefined && formPanel.closeWin && Ext.getCmp(this.winId)){
                                    Ext.getCmp(this.winId).close();
                                }
                            }
                        });
                    }
                }
            })
        }else{
            if(this.flowSaveFun)
                this.flowSaveFun.call(this, !!action? action: this.actionName);
            else{
                $postForm({
                    formPanel: Ext.getCmp(this.mainFormPanelId),
                    grid: this.subGridPanelId != undefined ? Ext.getCmp(this.subGridPanelId) : Ext.getCmp(this.mainGridPanelId),
                    scope: this,
                    url: this.urlFlow,
                    params : {act: action? action:'save'},
                    callback: function(fp, action) {
                        me.refreshRs.call(me);
                        var formPanel = Ext.getCmp(this.mainFormPanelId);
                        formPanel.form.reset();
                        if(formPanel.closeWin != undefined && formPanel.closeWin && Ext.getCmp(this.winId)){
                            Ext.getCmp(this.winId).close();
                        }
                    }
                });
            }
        }
    },
	flowMineRs: function(){
        var me = this;
	    if(this.flowMineFun) this.flowMineFun.call(this); else {
	        var cmp = Ext.getCmp(me.mainGridPanelId);
            cmp.getStore().proxy.url = cmp.url + '?modal=mine';
	        cmp.getStore().reload({callback: function(records, operation, success) {
                if(records != undefined && records.length<1){
                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
                }
            }});
        };
	},
	flowInvolvedRs: function(){
        var me = this;
	    if(this.flowInvolvedFun) this.flowInvolvedFun.call(this); else {
            var cmp = Ext.getCmp(me.mainGridPanelId);
            cmp.getStore().proxy.url = cmp.url + '?modal=involved';
            cmp.getStore().reload({callback: function(records, operation, success) {
                if(records != undefined && records.length<1){
                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
                }
            }});
        };
    },
    flowNormalRs: function(){
        var me = this;
        if(this.flowNormalFun) this.flowNormalFun.call(this); else {
            var cmp = Ext.getCmp(me.mainGridPanelId);
            cmp.getStore().proxy.url = cmp.url;
            cmp.getStore().reload();
        };
    },

	processRemarkRs: function(){ if(this.processRemarkFun) this.processRemarkFun.call(this); else {
		var cmpPanel = Ext.getCmp(this.mainTabPanelId);
		cmpPanel.setActiveTab(this.mainTabPanelId + '-0');
		};
	},
	processHistoryRs: function(){ if(this.processHistoryFun) this.processHistoryFun.call(this); else {
		var cmpPanel = Ext.getCmp(this.mainTabPanelId);
		cmpPanel.setActiveTab(this.mainTabPanelId + '-1');
		};
	},
	processDirectionRs: function(){ if(this.processDirectionFun) this.processDirectionFun.call(this); else {
		var cmpPanel = Ext.getCmp(this.mainTabPanelId);
		cmpPanel.show();
		cmpPanel.setActiveTab(this.mainTabPanelId + '-2');
		};
	}
});