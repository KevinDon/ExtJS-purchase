Ext.define('App.BuyerDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.BuyerDialog',
    
    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.BuyerDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';
    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}
    	
    	new BuyerDialogWin({
			scope:this,
			single:this.single ? this.single : false,
			fieldValueName: this.hiddenName,
			fieldTitleName: this.name,
			selectedId : selectedId,
			subcallback: this.subcallback ? this.subcallback: '',
            isFormField: true,
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


BuyerDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.FlowPurchaseContract.fBuyer;
        conf.moduleName = 'Buyer';
		conf.winId = 'BuyerDialogWinID';
		conf.mainGridPanelId = 'BuyerDialogWinGridPanelID';
		conf.searchFormPanelId= 'BuyerDialogWinSearchPanelID',
		conf.selectGridPanelId = 'BuyerDialogWinSelectGridPanelID';
		conf.treePanelId = 'BuyerDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'system/buyerInfo/list',
		conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		BuyerDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.FlowPurchaseContract.tSelector,
			width: this.single ? 1080 : 1280,
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
			onlyKeywords: true
		});// end of searchPanel
		
		this.gridPanel = new HP.GridPanel({
			region : 'center',
			id : conf.mainGridPanelId,
			title: _lang.FlowPurchaseContract.tabBuyerList,
			scope : this,
			border : false,
			forceFit: false,
			url : conf.urlList,
			fields: [
                'id','contactCnName', 'contactEnName','cnName','enName', 'departmentId','email','departmentCnName','departmentEnName',
				'cnAddress','enAddress','phone','fax'
			],
			columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.FlowPurchaseContract.fCnName, dataIndex: 'cnName', width: 100 },
                { header: _lang.FlowPurchaseContract.fEnName, dataIndex: 'enName', width: 120 },

                { header: _lang.AccountUser.fDepartmentId, dataIndex: 'departmentId', width: 60, hidden: true},
                { header: _lang.FlowPurchaseContract.fCnAddress, dataIndex: 'cnAddress', width: 390 },
                { header: _lang.FlowPurchaseContract.fEnAddress, dataIndex: 'enAddress', width: 390 },
                { header: _lang.FlowPurchaseContract.fContactCnName, dataIndex: 'contactCnName', width: 100},
                { header: _lang.FlowPurchaseContract.fContactEnName, dataIndex: 'contactEnName', width: 120},
                { header: _lang.FlowPurchaseContract.fDepartmentCnName, dataIndex: 'departmentCnName', width: 100},
                { header: _lang.FlowPurchaseContract.fDepartmentEnName, dataIndex: 'departmentEnName', width: 120},
                { header: _lang.TText.fEmail, dataIndex: 'email', width: 200},
                { header: _lang.VendorDocument.fFax, dataIndex: 'fax', width: 140},
                { header: _lang.AccountUser.fPhone, dataIndex: 'phone', width: 100 },
			],// end of columns
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
			title: _lang.FlowPurchaseContract.tSelected,
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
				{ header: _lang.FlowPurchaseContract.fCnName, dataIndex: 'cnName', width: 100, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.FlowPurchaseContract.fEnName, dataIndex: 'enName', width: 120, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
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
				names += curUserInfo.lang =='zh_CN' ? rows[i].data.cnName: rows[i].data.enName;
				//names += rows[i].data.name;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
				//names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
				names += rows.getAt(i).data.name;
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