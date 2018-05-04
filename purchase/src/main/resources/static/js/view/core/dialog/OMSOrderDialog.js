Ext.define('App.OmsOrderDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.OmsOrderDialog',

    constructor : function(conf) {

        this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.OmsOrderDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
        new OmsOrderDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            type: !! this.type ? this.type : '',
            // selectedId : selectedId,
            formal : this.formal ? this.formal: false,
            vendorId : Ext.getCmp(this.formId).vendorId ?  Ext.getCmp(this.formId).vendorId : '',
            filter: this.filter,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback: function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
					this.subcallback.call(this, rows);
				}
            }}, false).show();
    }
});


OmsOrderDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowOrderQualityInspection.tabOrderDetail;
        conf.moduleName = 'FlowOrderQualityInspection';
        conf.winId = 'OmsOrderDialogWinID';
        conf.mainGridPanelId = 'OmsOrderDialogWinGridPanelID';
        conf.searchFormPanelId= 'OmsOrderDialogWinSearchPanelID';
        conf.selectGridPanelId = 'OmsOrderDialogWinSelectGridPanelID';
        conf.treePanelId = 'OmsOrderDialogWinTreePanelId';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        OmsOrderDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.FlowOrderQualityInspection.tabOrderDetail,
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
            fieldItems:[
                {field:'orderNo', xtype:'textfield', title: _lang.ProductProblem.fOmsOrderId},
                {field:'tid', xtype:'textfield', title: _lang.ProductProblem.fTransactionNumber},
                {field:'shopId', xtype:'textfield', title: _lang.ProductProblem.fSellChannel},
                {field:'userNick', xtype:'textfield', title: _lang.ProductProblem.fMemberNickname},
                {field:'email', xtype:'textfield', title: _lang.ProductProblem.fEmail},
            ]
        });

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title:_lang.FlowOrderQualityInspection.tabOrderDetail,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 980,
            autoScroll: true,
            autoLoad: false,
            url : __ctxPath + 'omsapi/order/searchOrders',
            fields: [
                'order_no', 'shop_id', 'shop_code', 'email', 'user_nick', 'tid', 'email', 'order_time',
                {name: 'id', mapping: 'order_no'}
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 100, hidden: true },
                { header:_lang.ProductProblem.fOmsOrderId, dataIndex: 'order_no', width: 200, },
                { header:_lang.ProductProblem.fTransactionNumber, dataIndex: 'tid', width: 90, },
                { header:_lang.ProductProblem.fSellChannel, dataIndex: 'shop_id', width: 90, hidden: true},
                { header:_lang.ProductProblem.fSellChannel, dataIndex: 'shop_code', width: 90, },
                { header:_lang.ProductProblem.fMemberNickname, dataIndex: 'user_nick', width: 150, },
                { header:_lang.ProductProblem.fEmail, dataIndex: 'email', width: 200, },
                { header:_lang.ProductProblem.fOrderAt, dataIndex: 'order_time', width: 140, format: curUserInfo.dateFormat,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.scope.winOk.call(this.scope);
            },
            callback : function(obj, records){
                //初始化选择
                if(this.selectedId && records.length){
                    for(var i=0; i<records.length; i++){
                        if(records[i].data.id == this.selectedId){
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                }
            }
        });

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.PurchaseContractDialog.tSelected,
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
            fields : ['id','orderNumber'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.PurchaseContractDialog.fOrderNo, dataIndex: 'orderNumber', width:90, sortable:false,},
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
                names += rows[i].data.id;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.id;
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
    },
});