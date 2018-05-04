Ext.define('App.VendorDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.VendorDialog',
    
    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.VendorDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}
    	
    	new VendorDialogWin({
			scope:this,
			single:this.single ? this.single : false,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			selectedId : selectedId,
			subcallback: this.subcallback ? this.subcallback: '',
            isFormField: true,
			meForm: Ext.getCmp(this.formId),
			callback:function(ids, titles, rows, vendorBank) {
				this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
				this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
				if(this.subcallback){
					this.subcallback.call(this, rows, vendorBank);
				}
			}}, false).show();
    }
});


VendorDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.VendorDocument.mTitle;
		conf.winId = 'VendorDialogWinID';
		conf.mainGridPanelId = 'VendorDialogWinGridPanelID';
		conf.searchFormPanelId= 'VendorDialogWinSearchPanelID',
		conf.selectGridPanelId = 'VendorDialogWinSelectGridPanelID';
		conf.treePanelId = 'VendorDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'archives/vendor/list',
		conf.urlGet = __ctxPath + 'archives/vendor/get',
		conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		VendorDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.VendorDocument.tSelector,
			width: this.single ? 980 : 1080,
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
            fieldItems:[
                {field:'Q-categoryId-S-EQ', xtype:'hidden'},
                {field:'categoryName', xtype:'VendorCategoryDialog', title:_lang.VendorDocument.fCategoryName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
                },
                {field:'Q-source-N-EQ', xtype:'dictcombo', value:'', title:_lang.VendorDocument.fSource, code:'vendor', codeSub:'source', addAll:true},
                {field:'Q-director-S-LK', xtype:'textfield', title:_lang.VendorDocument.fDirector},
                {field:'Q-website-S-LK', xtype:'textfield', title:_lang.VendorDocument.fWebsite},
                {field:'Q-abn-S-LK', xtype:'textfield', title:_lang.VendorDocument.fAbn},
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
                {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
                {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
                {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyEnName},
                {field:'Q-address-S-LK', xtype:'textfield', title:_lang.VendorDocument.fAddress},
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
		});// end of searchPanel
		
		this.gridPanel = new HP.GridPanel({
			region : 'center',
			id : conf.mainGridPanelId,
			title: _lang.VendorDocument.tabListTitle,
			scope : this,
			border : false,
			forceFit: false,
			url : conf.urlList,
			fields: [
                'id','categoryName', 'name','cnName','enName', 'buyerName','director',
                'address','abn','website','rating','files', 'sellerPhone', 'sellerFax', 'sellerEmail',
                'source', 'currency', 'orderSerialNumber', 'paymentProvision',
                'dynContent', 'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName',
                'departmentEnName','departmentId','updatedAt',
			],
			columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.VendorDocument.fCategoryName, dataIndex: 'categoryName', width: 120, sortable: false},
                { header: _lang.VendorDocument.fCnName, dataIndex: 'cnName', width: 260},
                { header: _lang.VendorDocument.fEnName, dataIndex: 'enName', width: 260},
                { header: _lang.VendorDocument.fBuyerName, dataIndex: 'buyerName', width: 120, sortable: false},
                { header: _lang.VendorDocument.fDirector, dataIndex: 'director', width: 60},
                { header: _lang.VendorDocument.fAddress, dataIndex: 'address', width: 390 },
                { header: _lang.VendorDocument.fAbn, dataIndex: 'abn', width: 200 },
                { header: _lang.VendorDocument.fWebsite, dataIndex: 'website', width: 140 },
                { header: _lang.VendorDocument.fSource, dataIndex: 'source', width: 80 ,
                    renderer: function(value){
                        var $source = _dict.source;
                        if($source.length>0 && $source[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $source[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60 ,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                }
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
			title: _lang.VendorDocument.tSelected,
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
				{ header: _lang.VendorDocument.fCnName, dataIndex: 'cnName', width: 260, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.VendorDocument.fEnName, dataIndex: 'enName', width: 260, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
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
		if(this.fieldValueName){
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
		var rows = {};
		if(this.single){
			rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
			for (var i = 0; i < rows.length; i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows[i].data.id;
//				names += curUserInfo.lang =='zh_CN' ? rows[i].data.cnName: rows[i].data.enName;
				names += rows[i].data.name;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
//				names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
				names += rows.getAt(i).data.name;
			}
		}

		// if (this.callback) {
		// 	this.callback.call(this, ids, names, rows);
		// }
        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            var me = this;
            var params = {id: ids};
            $postUrl({
                url: this.urlGet, maskTo: this.frameId, params: params, autoMessage:false,
                callback: function (response, eOpts) {
                    var json = Ext.JSON.decode(response.responseText);
                    me.callback.call(me, ids, names, rows, json.data);
                }
            });
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