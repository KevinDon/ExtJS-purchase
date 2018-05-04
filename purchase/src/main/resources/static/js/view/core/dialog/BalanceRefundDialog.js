Ext.define('App.BalanceRefundFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.BalanceRefundFormMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title: this.fieldLabel,
            moduleName: 'Product',
            fieldValueName: this.valueName || 'main_balanceRefundIds',
            fieldTitleName: this.titleName || 'main_balanceRefundName',
            callback: this.callback,
            scope: this.scope
        };
        conf.mainGridPanelId= this.mainGridPanelId;
        conf.mainFormPanelId= this.mainFormPanelId;
        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-BalanceRefundFormMultiGrid' + Ext.id();
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-BalanceRefundFormMultiForm' + Ext.id();
        conf.readOnly = this.readOnly;
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 220;
        conf.noTitle = this.noTitle || false;
        this.initUIComponents(conf);

        App.BalanceRefundFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ],
        });
    },

    vendorId: '',

    setValue:function (values, initValues) {
        var cmpAtta = this.formGridPanel.getStore();
        cmpAtta.removeAll();

        if(this.readOnly){
                if(!!initValues && initValues.length>0){
                        for (var j=0; j < initValues.length; j++){
                            initValues[j].balanceRefund.active = true;
                            cmpAtta.add(initValues[j].balanceRefund);
                        }
                }
        }else{
            if(!!values && values.length>0){
                if(!!initValues && initValues.length>0){
                    for(var i = 0; i < values.length; i++){
                        for (var j=0; j < initValues.length; j++){
                            if(values[i].data.businessId == initValues[j].balanceRefundBusinessId && values[i].data.id == initValues[j].balanceRefundId){
                                values[i].data.active = true;
                            }
                        }
                    }
                }
                cmpAtta.add(values);
            }
        }
    },

    initUIComponents: function(conf){
        var me = this;

        var tools = [{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight-3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.FlowBalanceRefund.tabRefundList,
            forceFit: false,
            width: 'auto',
            height:conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray'},
            fields:[
                'active', 'id','businessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                'assigneeId','assigneeCnName','assigneeEnName',
            ],
            columns:[
                {header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false, scope:this, hidden: this.readOnly,
                    renderer:function(value, meta){
                        var totalAud = 0;
                        var totalRmb = 0;
                        var totalUsd = 0;
                        if(value !=undefined){
                            //获取Form表单下的冲销总金额 都加上
                            for(var i = 0; i < this.formGridPanel.getStore().getCount(); i ++){
                                if(this.formGridPanel.getStore().getAt(i).data.active == true){
                                    var priceAud = this.formGridPanel.getStore().getAt(i).data.totalFeeAud;
                                    var priceRmb = this.formGridPanel.getStore().getAt(i).data.totalFeeRmb;
                                    var priceUsd = this.formGridPanel.getStore().getAt(i).data.totalFeeUsd;
                                    totalAud = totalAud + parseFloat(priceAud);
                                    totalRmb = totalRmb + parseFloat(priceRmb);
                                    totalUsd = totalUsd + parseFloat(priceUsd);
                                }
                            }
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffAud').setValue(totalAud.toFixed(2));
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffRmb').setValue(totalRmb.toFixed(2));
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffUsd').setValue(totalUsd.toFixed(2));
                        }

                        try {
                            if (this.callback) this.callback.call(this.scope);
                        }catch (e){console.log(e)}

                        return $renderOutputCheckColumns(value, meta);

                    },
                },
                {header: _lang.TText.fId, dataIndex: 'id', width: 180, hidden:true},
                {header: _lang.FlowBalanceRefund.fBusinessId, dataIndex: 'businessId', width: 180, hidden:true },
                {header: _lang.FlowBalanceRefund.fOrderId, dataIndex: 'orderId', width: 180, hidden:true },
                {header: _lang.FlowBalanceRefund.fOrderNumber, dataIndex: 'orderNumber', width: 150},
                {header: _lang.FlowBalanceRefund.fRefundType, dataIndex: 'type', width: 70,
                    renderer: function (value) {
                        var $refundType = _dict.refundType;
                        if ($refundType.length > 0 && $refundType[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $refundType[0].data.options);
                        }
                    }
                },
                {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'chargebackStatus', width: 70,
                    renderer : function(value){
                        value = value || '2';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                {header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                {header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                {header: _lang.FlowBalanceRefund.fTotalFeePrice,columns: new $groupPriceColumns(this, 'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', null, {edit: false}) },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({assignee:false,sort:false}),
            itemclick: function (obj, record, item, index, e, eOpts) {
            }
        });
        this.formGridPanel.store.on('dataChanged', function (store) {
            if (conf.callback) conf.callback.call(conf.scope, store, conf);
        });
    }
});