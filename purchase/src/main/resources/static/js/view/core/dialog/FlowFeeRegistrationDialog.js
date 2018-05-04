Ext.define('App.FlowFeeRegistrationDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.FlowFeeRegistrationDialog',
    
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
    	
    	new FlowFeeRegistrationDialogWin({
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


FlowFeeRegistrationDialogWin = Ext.extend(HP.Window,{
	constructor : function(conf) {
		conf.title = _lang.FlowFeeRegistration.mTitle;
		conf.winId = 'FlowFeeRegistrationDialogWinID';
		conf.mainGridPanelId = 'FlowFeeRegistrationDialogWinGridPanelID';
		conf.searchFormPanelId= 'FlowFeeRegistrationDialogWinSearchPanelID',
		conf.selectGridPanelId = 'FlowFeeRegistrationDialogWinSelectGridPanelID';
		conf.treePanelId = 'FlowFeeRegistrationDialogWinTreePanelId';
		conf.urlList = __ctxPath + 'finance/feeRegister/list',
		conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
		conf.ok = true;
		conf.okFun = this.winOk;
		
		Ext.applyIf(this, conf);
		this.initUI(conf);
		
		FlowFeeRegistrationDialogWin.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : _lang.FlowFeeRegistration.tSelector,
			width: this.single ? 650 : 750,
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
			title: _lang.FlowFeeRegistration.tabItemList,
			scope : this,
			border : false,
			forceFit: false,
			url : conf.urlList,
			fields: [
                'id', 'feeType', 'orderId','vendorId', 'vendorName', 'beneficiaryBank', 'bankAccount', 'companyName', 'currency',
                'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId'
            ],
			columns: [
				{header: _lang.FlowBankAccount.fId, dataIndex: 'id', width: 180,  },
                {header: _lang.FlowFeeRegistration.fFeeType, dataIndex: 'feeType', width: 60,
                    renderer: function (value) {
                        var $feeType = _dict.feeType;
                        if ($feeType.length > 0 && $feeType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $feeType[0].data.options);
                        }
                    }
                },

                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 90},

                {header: _lang.FlowFeeRegistration.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true},
                {header: _lang.FlowFeeRegistration.fVendorAndService, dataIndex: 'vendorName', width: 260,},
                {header: _lang.BankAccount.fBeneficiaryBank, dataIndex: 'beneficiaryBank', width: 200},
                {header: _lang.BankAccount.fBankAccount, dataIndex: 'bankAccount', width: 200},
                {header: _lang.FlowFeeRegistration.fCompanyName, dataIndex: 'companyName', width: 200},
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                {
                    header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', null, {edit: false})
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
			],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false}),
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
			title: _lang.FlowFeeRegistration.tSelected,
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
			fields : ['id','orderId'],
			columns : [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
				{ header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderId', width: 90},
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
				names += rows[i].data.orderId;
			}
		}else{
			rows = Ext.getCmp(this.selectGridPanelId).getStore();
			for (var i = 0; i < rows.getCount(); i++) {
				if (i > 0) {ids += ',';names += ',';}
				ids += rows.getAt(i).data.id;
//				names += curUserInfo.lang =='zh_CN' ? rows.getAt(i).data.cnName: rows.getAt(i).data.enName;
				names += rows.getAt(i).data.orderId;
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