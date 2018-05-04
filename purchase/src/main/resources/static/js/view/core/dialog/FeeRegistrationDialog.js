Ext.define('App.FeeRegistrationDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.FeeRegistrationDialog',

    constructor : function(conf) {

        this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.FeeRegistrationDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}

        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new FeeRegistrationDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            selectedId : selectedId,
            formal : this.formal ? this.formal: false,
            filter: this.filter,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback: function(ids, titles, rows, selectedData) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
					this.subcallback.call(this, rows, selectedData);
				}
            }}, false).show();
    }
});


FeeRegistrationDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowFeePayment.tabFeeRegisterInfo;
        conf.moduleName = 'FlowFeePayment';
        conf.winId = 'FeeRegistrationDialogWinID';
        conf.mainGridPanelId = 'FeeRegistrationDialogWinGridPanelID';
        conf.searchFormPanelId= 'FeeRegistrationDialogWinSearchPanelID';
        conf.selectGridPanelId = 'FeeRegistrationDialogWinSelectGridPanelID';
        conf.treePanelId = 'FeeRegistrationDialogWinTreePanelId';
        if(conf.formal){
           conf.urlList = __ctxPath + 'finance/feeRegister/listForDialog';
        }else{
            conf.urlList = __ctxPath + 'flow/finance/feeRegister/list';
        }

        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        FeeRegistrationDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.FlowFeePayment.tabFeeRegisterInfo,
            width: this.single ? 1100 : 1280,
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

        //filter
        var url = conf.urlList;
        if(!!this.filter){
            url += "?" + Ext.urlEncode(this.filter);
        }
        this.selectedData = [];

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title:_lang.FlowFeePayment.tabFeeRegisterInfo,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            width: '100%',
            minWidth: 1080,
            autoScroll: true,
            url : url,
            fields: [
                'id','businessId', 'feeType', 'orderNumber', 'vendorName', 'beneficiaryBank', 'bankAccount', 'companyName', 'currency','vendorCnName','vendorEnName','companyCnName',
                'companyEnName',  'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark','title',
                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName','paymentStatus',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','companyEnName','companyCnName','dueDate', 'attachments'
            ],
            columns: [
                {header: _lang.FlowBankAccount.fId, dataIndex: 'id', width: 180, hidden:true, },
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 180, },
                {header: _lang.FlowFeeRegistration.fFeeType, dataIndex: 'feeType', width: 80,
                    renderer: function (value) {
                        var $feeType = _dict.feeType;
                        if ($feeType.length > 0 && $feeType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $feeType[0].data.options);
                        }
                    }
                },
                {header: _lang.FlowFeeRegistration.fTitle, dataIndex: 'title', width: 120,  },
                {header: _lang.FlowFeeRegistration.fPaymentStatus, dataIndex: 'paymentStatus', width: 60,
                    renderer: function(value){
                        var $paymentStatus = _dict.paymentStatus;
                        if ($paymentStatus.length > 0 && $paymentStatus[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $paymentStatus[0].data.options);
                        }
                    }
                },

                {header: _lang.FlowOrderShippingPlan.fOrderId, dataIndex: 'orderNumber', width: 90},

                {header: _lang.FlowFeeRegistration.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true},
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'companyCnName', width:260, hidden: curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'companyEnName', width: 260, hidden: curUserInfo.lang !='zh_CN'? false: true },
                {header: _lang.BankAccount.fBeneficiaryBank, dataIndex: 'beneficiaryBank', width: 200},
                {header: _lang.BankAccount.fBankAccount, dataIndex: 'bankAccount', width: 200},
                {header: _lang.BankAccount.fCnCompanyName, dataIndex: 'companyCnName', width: 200},
                {header: _lang.BankAccount.fEnCompanyName, dataIndex: 'companyEnName', width: 200},
                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                {
                    header: _lang.FlowFeeRegistration.fFeeRegistrationTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', null, {edit: false})
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
                {header: _lang.FlowDepositContract.fLatestPaymentTime, dataIndex: 'dueDate', width: 140},
                {header: _lang.FlowDepositContract.fLatestPaymentTime, dataIndex: 'attachments', width: 120, hidden: true},
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
                    //console.log(record.data);
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
            title: _lang.FlowOrderQualityInspection.tSelected,
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
            fields : ['id','orderNumber', 'businessId'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header:  _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 40, hidden: true, sortable:false },
                { header: _lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 90, sortable:false,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
                var selectedData = [];
                for(var i = 0; i < this.scope.selectedData.length; i++){
                    var data = this.scope.selectedData[i];
                    if(data.id != record.data.id) {
                        selectedData.push(data);
                    }
                }

                this.scope.selectedData = selectedData;
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
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
                    }else{
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
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
                names += rows[i].data.businessId;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.businessId;
            }
        }

        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            this.callback.call(this, ids, names, rows, win.selectedData);
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