Ext.define('App.FlowProductQuotationDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.FlowProductQuotationDialog',

    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.FlowProductQuotationDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}
    	
    	var selectedId = '';

    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}

        new FlowProductQuotationDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback:function(ids, titles) {
                 this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                 this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
            }}, false).show();
    }
});


FlowProductQuotationDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.ProductDocument.tabListTitle;
        conf.moduleName = 'FlowProductQuotation';
        conf.winId = 'FlowProductQuotationDialogWinID';
        conf.mainGridPanelId = 'FlowProductQuotationDialogWinGridPanelID';
        conf.searchFormPanelId= 'FlowProductQuotationDialogWinSearchPanelID',
        conf.selectGridPanelId = 'FlowProductQuotationDialogWinSelectGridPanelID';
        conf.treePanelId = 'FlowProductQuotationDialogWinTreePanelId';
        conf.urlList = '',
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);
        FlowProductQuotationDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductDocument.tabListTitle,
            width: this.single ? 880 : 1080,
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
            title: _lang.ProductDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 1080,
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'id','sku','name','priceAud','priceRmb','priceUsd', 'orderQty', 'rateAudToRmb','rateAudToUsd',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId','destinationPortCnName','destinationPortEnName',
                'netWeight','masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonStructure','masterCartonWeight'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200,  },

                //报价
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('orderValueAud', (row.get('priceAud') * row.get('orderQty')).toFixed(2));
                        row.set('orderValueRmb', (row.get('priceRmb') * row.get('orderQty')).toFixed(2));
                        row.set('orderValueUsd', (row.get('priceUsd') * row.get('orderQty')).toFixed(2));
                    },{gridId: conf.formGridPanelId})
                },
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},


                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', },

                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight',},

                { header: _lang.ProductDocument.fMasterCartonL, dataIndex: 'masterCartonL',  },

                { header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonW', },
                { header: _lang.ProductDocument.fInnerCartonH, dataIndex: 'innerCartonH', },
                { header: _lang.ProductDocument.fInnerCartonCbm, dataIndex: 'innerCartonCbm',  },
                { header: _lang.ProductDocument.fMasterCartonStructure, dataIndex: 'masterCartonStructure', width: 60 },
                { header: _lang.ProductDocument.fMasterCartonWeight, dataIndex: 'masterCartonWeight', width: 60 },

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
            title: _lang.ProductDocument.tSelected,
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
            fields : ['id','sku'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sampleSku', width: 200, sortable:false,},
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
                names += rows[i].data.sku;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.sku;
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