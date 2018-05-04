Ext.define('App.AsnDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.AsnDialog',

    constructor : function(conf) {

        this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.AsnDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}

        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new AsnDialogWin({
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
            callback: function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
					this.subcallback.call(this, rows);
				}
            }}, false).show();
    }
});


AsnDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowOrderReceivingNotice.tSelected;
        conf.moduleName = 'FlowOrderReceivingNotice';
        conf.winId = 'AsnDialogWinID';
        conf.mainGridPanelId = 'AsnDialogWinGridPanelID';
        conf.searchFormPanelId= 'AsnDialogWinSearchPanelID';
        conf.selectGridPanelId = 'AsnDialogWinSelectGridPanelID';
        conf.treePanelId = 'AsnDialogWinTreePanelId';
        // console.log(conf.formal);

        var orderId = conf.meForm.getCmpByName(conf.orderId).getValue();
        if(!!orderId){
            if(conf.formal){
                conf.urlList = __ctxPath + 'shipping/asnpackingdetail/listByOrderId?orderId=' + orderId;
            }else{
                conf.urlList = __ctxPath + 'shipping/asnpackingdetail/listByOrderId?orderId=' + orderId;
            }
        }else{
            conf.urlList = '';
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);

        }
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        AsnDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang. FlowOrderReceivingNotice.tSelected,
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
            title:_lang.FlowOrderReceivingNotice.tabNoticeDetail,
            //collapsible: true,
            //split: true,
            header:{cls:'x-panel-header-gray' },
            scope: this,
            forceFit: false,
            border: false,
            autoScroll: true,
            url : url,
            fields: [
                'id','asnId', 'productId', 'sku', 'orderQty','priceAud','priceRmb','priceUsd', 'rateAudToRmb','rateAudToUsd',
                'vendorCnName', 'vendorEnName', 'currency','receivedQty','expectedQty','expectedCartons','receivedCartons','chargebackStatus'
            ],
            columns: [
                { header:'ID', dataIndex: 'id', width: 180, hidden: true,  },
                { header:'ASN ID', dataIndex: 'asnId', width: 180, hidden: true,  },
                { header: _lang.FlowBalanceRefund.fProductId, dataIndex: 'productId', width: 180, hidden: true,  },
                { header: _lang.FlowBalanceRefund.fSku, dataIndex: 'sku', width: 180},

                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width:65,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.FlowProductQuotation.fNewExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //报价
                { header: _lang.FlowBalanceRefund.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowOrderReceivingNotice.fExpected,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'expectedQty', width: 60, },
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'expectedCartons', width: 60,},
                    ]
                },
                { header: _lang.FlowOrderReceivingNotice.fReceived,
                    columns: [
                        { header: _lang.FlowOrderReceivingNotice.fQty, dataIndex: 'receivedQty', width: 60,},
                        { header: _lang.FlowOrderReceivingNotice.fCartons, dataIndex: 'receivedCartons', width: 60, },
                    ]
                },

                {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'chargebackStatus', width: 70,
                    renderer : function(value){
                        value = value || '2';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                }
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e){
                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    selStore.add(record.data);
                }else{
                    this.scope.winOk.call(this.scope);
                };
                if(!!this.scope.selectedData) {
                    this.scope.selectedData.push(record);
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
            title: _lang.FlowOrderReceivingNotice.fSelctedAsn,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            header:{cls:'x-panel-header-gray' },
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
                { header: _lang.FlowOrderReceivingNotice.fSku, dataIndex: 'sku', width: 160, sortable:false},
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
            items: [this.searchPanel, this.gridPanel,]

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