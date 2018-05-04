Ext.define('App.FlowBalanceRefundProjectFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowBalanceRefundProjectFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowBalanceRefund.mTitle,
            moduleName: 'FlowBalanceRefund',
            winId : 'FlowBalanceRefundForm',
            frameId : 'FlowBalanceRefundView',
            mainGridPanelId : 'FlowBalanceRefundGridPanelID',
            mainFormPanelId : 'FlowBalanceRefundFormPanelID',
            processFormPanelId: 'FlowBalanceRefundProcessFormPanelID',
            searchFormPanelId: 'FlowBalanceRefundSearchFormPanelID',
            mainTabPanelId: 'FlowBalanceRefundMainTabsPanelID',
            subFormGridPanelId : 'FlowBalanceRefundSubGridPanelID',
            formGridPanelId : 'FlowBalanceRefundProjectFormGridPanelID',
        };

        this.initUIComponents(conf);

        App.FlowBalanceRefundProjectFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-project',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },

    initUIComponents: function(conf){
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                var currency = Ext.getCmp(conf.mainFormPanelId).currency;
                Ext.getCmp(conf.subFormGridPanelId + '-project').getStore().add({id: '', preditQty:'1', confirmQty:'1', diffQty:'1', currency: currency});

            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(260);
                this.formGridPanel.setHeight(258);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(698);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subFormGridPanelId + '-project',
            title: _lang.FlowFeeRegistration.tabChargeItem,
            forceFit: false,
            width: 'auto',
            height: 250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','businessId', 'payProject', 'priceAud', 'priceRmb', 'priceUsd', 'currency',
                'preditQty','confirmQty','diffQty', 'rateAudToUsd', 'rateAudToRmb', 'remark','diffAud','diffRmb','diffUsd',
                'receivedPriceAud','receivedPriceRmb','receivedPriceUsd'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 200, hidden: true, },

                {header: _lang.FlowFeeRegistration.fProject, dataIndex: 'payProject', width: 120,
                    editor: {xtype: 'textfield',}
                },
                {header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 65,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd') },
                //应付价格
                { header: _lang.FlowBalanceRefund.fExpectedPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('diffAud', Math.abs(row.get('priceAud') - row.get('receivedPriceAud')).toFixed(3));
                        row.set('diffRmb', Math.abs(row.get('priceRmb') - row.get('receivedPriceRmb')).toFixed(3));
                        row.set('diffUsd', Math.abs(row.get('priceUsd') - row.get('receivedPriceUsd')).toFixed(3));

                        var totalPriceAud = 0;
                        var totalPriceRmb= 0;
                        var totalPriceUsd = 0;
                        var formPanel = Ext.getCmp(conf.mainFormPanelId);
                        var gridPanel = Ext.getCmp(conf.subFormGridPanelId + '-project');

                        //统计总金额
                        for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                            totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.diffAud);
                            totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.diffRmb);
                            totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.diffUsd);
                        }

                        formPanel.getCmpByName('main.totalFeeAud').setValue(totalPriceAud.toFixed(3));
                        formPanel.getCmpByName('main.totalFeeRmb').setValue(totalPriceRmb.toFixed(3));
                        formPanel.getCmpByName('main.totalFeeUsd').setValue(totalPriceUsd.toFixed(3));

                    },{gridId: conf.subFormGridPanelId + '-project',})
                },

                //实付价格
                { header: _lang.FlowBalanceRefund.fReceivedPrice,
                    columns: new $groupPriceColumns(this, 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd', function(row, value){
                        row.set('diffAud', Math.abs(row.get('priceAud') - row.get('receivedPriceAud')).toFixed(3));
                        row.set('diffRmb', Math.abs(row.get('priceRmb') - row.get('receivedPriceRmb')).toFixed(3));
                        row.set('diffUsd', Math.abs(row.get('priceUsd') - row.get('receivedPriceUsd')).toFixed(3));

                        var totalPriceAud = 0;
                        var totalPriceRmb= 0;
                        var totalPriceUsd = 0;
                        var formPanel = Ext.getCmp(conf.mainFormPanelId);
                        var gridPanel = Ext.getCmp(conf.subFormGridPanelId + '-project');

                        //统计总金额
                        for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                            totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.diffAud);
                            totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.diffRmb);
                            totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.diffUsd);
                        }

                        formPanel.getCmpByName('main.totalFeeAud').setValue(totalPriceAud.toFixed(3));
                        formPanel.getCmpByName('main.totalFeeRmb').setValue(totalPriceRmb.toFixed(3));
                        formPanel.getCmpByName('main.totalFeeUsd').setValue(totalPriceUsd.toFixed(3));

                    },{gridId: conf.subFormGridPanelId + '-project',})
                },

                //发运数量
                {header: _lang.FlowBalanceRefund.fPreditQty, dataIndex: 'preditQty', width: 60, scope:this, value:'1' , hidden:true,  },
                //发运数量
                {header: _lang.FlowBalanceRefund.fConfirmQty, dataIndex: 'confirmQty', width: 60, scope:this, value:'1', hidden:true, },
                //发运数量
                {header: _lang.FlowBalanceRefund.fDiffQty, dataIndex: 'diffQty', width: 60, scope:this, value:'1', hidden:true, },


                { header: _lang.FlowBalanceRefund.fDiff,
                    columns: new $groupPriceColumns(this, 'diffAud','diffRmb','diffUsd', null,
                        {edit:false, gridId: conf.subFormGridPanelId + '-project'}
                    )
                },

                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'textarea'}
                },
            ]// end of columns
        });
    }
});


Ext.define('App.FlowBalanceRefundFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowBalanceRefundFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowBalanceRefund.tabRefundInfomation,
            moduleName: 'FlowBalanceRefund',
            winId : 'FlowBalanceRefundForm',
            frameId : 'FlowBalanceRefundView',
            mainGridPanelId : 'FlowBalanceRefundGridPanelID',
            mainFormPanelId : 'FlowBalanceRefundFormPanelID',
            processFormPanelId: 'FlowBalanceRefundProcessFormPanelID',
            searchFormPanelId: 'FlowBalanceRefundSearchFormPanelID',
            mainTabPanelId: 'FlowBalanceRefundMainTabsPanelID',
            subFormGridPanelId : 'FlowBalanceRefundSubGridPanelID',
            formGridPanelId : 'FlowBalanceRefundFormGridPanelID',
        };

        this.initUIComponents(conf);

        App.FlowBalanceRefundFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },

    setValue:function (values) {
        var cmpAtta = this.formGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(var i = 0; i < values.length; i++){
                var product = {};
                Ext.applyIf(product, values[i]);
                Ext.applyIf(product, values[i].product.prop);
                Ext.applyIf(product, values[i].product);
                console.log(product);
                cmpAtta.add(product);
            }
        }
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
                this.setHeight(260);
                this.formGridPanel.setHeight(258);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(698);
            }}
        ];

        

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subFormGridPanelId + '-product',
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height: 250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','sku','productId','currency', 'asnId',
                'expectedQty', 'receivedQty', 'diffQty', 'priceAud', 'priceRmb', 'priceUsd', 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd',
                'rateAudToUsd', 'rateAudToRmb','diffAud','diffRmb','diffUsd','chargebackStatus',
                {name: 'subRemark', mapping: 'remark'},
            ],
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
                    {header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                    {header: _lang.FlowOrderReceivingNotice.fASNId, dataIndex: 'asnId', width: 100, hidden:true},
                    {header: _lang.FlowOrderReceivingNotice.fSku, dataIndex: 'sku', width: 200},
                    {header: _lang.FlowOrderReceivingNotice.fProductId, dataIndex: 'productId', width: 200, hidden : true, },

                    {header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 65,
                        renderer: function (value) {
                            var $currency = _dict.currency;
                            if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                                return $dictRenderOutputColor(value, $currency[0].data.options);
                            }
                        },
                    },

                    //汇率
                    {header: _lang.NewProductDocument.fExchangeRate,
                        columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                    },

                    //报价
                    {header: _lang.FlowBalanceRefund.fPrice,
                        columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                            {edit: true, gridId: conf.formGridPanelId})
                    },
                    //实付
                    // {header: _lang.FlowBalanceRefund.fReceivedPrice,
                    //     columns: new $groupPriceColumns(this, 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd',
                    //         null,{ gridId: conf.subFormGridPanelId + '-product'})
                    // },
                    //发运数量
                    {header: _lang.FlowBalanceRefund.fPreditQty, dataIndex: 'expectedQty', width: 65, scope:this},
                    {header: _lang.FlowBalanceRefund.fConfirmQty, dataIndex: 'receivedQty', width: 65, scope:this},
                    {header: _lang.FlowBalanceRefund.fDiffQty, dataIndex: 'diffQty', width: 65,},

                    {header: _lang.FlowBalanceRefund.fDiff,
                        columns: new $groupPriceColumns(this, 'diffAud','diffRmb','diffUsd', null,
                            { gridId: conf.subFormGridPanelId + '-product'})
                    },
                    {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'chargebackStatus', width: 70,
                        renderer : function(value){
                            value = value || '2';
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                            if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                        }
                    },
                    {header: _lang.TText.fRemark, dataIndex: 'subRemark', width: 200,
                         editor: {xtype: 'textarea'},
                         renderer: function(value, meta){
                             meta.tdCls = 'grid-input';
                            return value;
                         },
                    },
                ]// end of columns
        });

    },
    setTotals: function(formPanel, gridPanel){
        var totalPriceAud = 0, totalPriceRmb= 0, totalPriceUsd = 0;

        //统计总金额
        for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
            if(gridPanel.getStore().getAt(i).data.chargebackStatus != '1') {
                totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.diffAud);
                totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.diffRmb);
                totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.diffUsd);
            }
        }
        formPanel.getCmpByName('main.totalFeeAud').setValue(totalPriceAud.toFixed(3));
        formPanel.getCmpByName('main.totalFeeRmb').setValue(totalPriceRmb.toFixed(3));
        formPanel.getCmpByName('main.totalFeeUsd').setValue(totalPriceUsd.toFixed(3));
    },
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        var me = this;
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new AsnDialogWin({
                    single:true,
                    orderId: 'main.orderId',
                    fieldValueName: 'main.orders',
                    fieldTitleName: 'main.orderName',
                    selectedId : selectedId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subFormGridPanelId + '-product'),
                    callback:function(ids, titles, result, selectedData) {
                        if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(),  result[0].data.id)){
                            var item =  result
                            var data = {};
                            Ext.applyIf(data, item[0].raw);

                            data.diffQty = item[0].data.expectedQty - item[0].data.receivedQty;

                            data.currency = item[0].data.currency || this.meForm.currency;

                            data.receivedPriceAud = item[0].data.priceAud * item[0].data.receivedQty;
                            data.receivedPriceRmb = item[0].data.priceRmb * item[0].data.receivedQty;
                            data.receivedPriceUsd = item[0].data.priceUsd * item[0].data.receivedQty;

                            data.diffAud = parseFloat(item[0].data.priceAud || 0) *  parseFloat(data.diffQty);
                            data.diffRmb =  parseFloat(item[0].data.priceRmb || 0) *  parseFloat(data.diffQty);
                            data.diffUsd =  parseFloat(item[0].data.priceUsd || 0) *  parseFloat(data.diffQty);
                            data.id = item[0].raw.id;
                            this.meGrid.getStore().insert(idx, data);
                            this.meGrid.getStore().removeAt(idx + 1);

                            me.setTotals.call(this, this.meForm, this.meGrid);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.subFormGridPanelId + '-product').store.remove(record);
                break;

            default :
                new AsnDialogWin({
                    single:false,
                    orderId: 'main.orderId',
                    fieldValueName: 'main.orders',
                    fieldTitleName: 'main.orderName',
                    selectedId : '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.subFormGridPanelId + '-product'),
                    callback:function(ids, titles, result, selectDetail) {
                        this.meGrid.getStore().removeAll();
                        if(result.data.items.length > 0){
                            var items = selectDetail;
                            for(var index in items){
                                if(items[index] != undefined && !$checkGridRowExist(this.meGrid.getStore(), items[index].data.id)){
                                    items[index].data.diffQty = items[index].data.expectedQty - items[index].data.receivedQty;

                                    items[index].data.currency = items[index].data.currency || this.meForm.currency;

                                    items[index].data.receivedPriceAud = items[index].data.priceAud * items[index].data.receivedQty;
                                    items[index].data.receivedPriceRmb = items[index].data.priceRmb * items[index].data.receivedQty;
                                    items[index].data.receivedPriceUsd = items[index].data.priceUsd * items[index].data.receivedQty;

                                    items[index].data.diffAud = items[index].data.priceAud * items[index].data.diffQty;
                                    items[index].data.diffRmb = items[index].data.priceRmb * items[index].data.diffQty;
                                    items[index].data.diffUsd = items[index].data.priceUsd * items[index].data.diffQty;

                                    items[index].id = selectDetail[index].raw.id;
                                    this.meGrid.getStore().add(items[index].data);
                                }
                            }

                            me.setTotals.call(this, this.meForm, this.meGrid);
                        }
                    }}, false).show();

                break;
        }
    }
});



Ext.define('App.FlowBalanceRefundSampleFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowBalanceRefundSampleFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowSample.tabGridTitle,
            moduleName: 'FlowBalanceRefund',
            winId : 'FlowBalanceRefundForm',
            frameId : 'FlowBalanceRefundView',
            mainGridPanelId : 'FlowBalanceRefundGridPanelID',
            mainFormPanelId : 'FlowBalanceRefundFormPanelID',
            processFormPanelId: 'FlowBalanceRefundProcessFormPanelID',
            searchFormPanelId: 'FlowBalanceRefundSearchFormPanelID',
            mainTabPanelId: 'FlowBalanceRefundMainTabsPanelID',
            subFormGridPanelId : 'FlowBalanceRefundSubGridPanelID',
            formGridPanelId : 'FlowBalanceRefundFormGridSamplePanelID',
        };

        this.initUIComponents(conf);

        App.FlowBalanceRefundSampleFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },

    vendorId: '___',

    initUIComponents: function(conf){
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(260);
                this.formGridPanel.setHeight(258);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(698);
            }}
        ];



        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subFormGridPanelId + '-sample',
            title : _lang.FlowSample.tabGridTitle,
            forceFit: false,
            width: 'auto',
            height: 250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','sku','productId','currency',
                'expectedQty', 'receivedQty', 'diffQty', 'priceAud', 'priceRmb', 'priceUsd', 'receivedPriceAud','receivedPriceRmb','receivedPriceUsd',
                'rateAudToUsd', 'rateAudToRmb','diffAud','diffRmb','diffUsd','feeRefunded'
            ],
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
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                {header: _lang.FlowOrderReceivingNotice.fSku, dataIndex: 'sku', width: 200},
                {header: _lang.FlowOrderReceivingNotice.fProductId, dataIndex: 'productId', width: 200, hidden : true, },

                {
                    header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function (value) {
                        var $currency = _dict.currency;
                        if ($currency.length > 0 && $currency[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    },
                },
                //汇率
                {
                    header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },

                //报价
                { header: _lang.FlowBalanceRefund.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                        {edit: true, gridId: conf.formGridPanelId})
                },
                {header: _lang.FlowBalanceRefund.fDiffQty, dataIndex: 'diffQty', width: 80,},
                { header: _lang.FlowBalanceRefund.fDiff,
                    columns: new $groupPriceColumns(this, 'diffAud','diffRmb','diffUsd', null,
                        { gridId: conf.subFormGridPanelId + '-sample'})
                },
                {header: _lang.FlowBalanceRefund.fChargebackStatus, dataIndex: 'feeRefunded', width: 70,
                    renderer : function(value){
                        value = value || '2';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                }
            ]// end of columns
        });

    },
    setValue:function (values) {
        var cmpAtta = this.formGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(var i = 0; i < values.length; i++){
                cmpAtta.add(values[i]);
            }
        }
    },
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    orderId: 'main.orderId',
                    fieldValueName: 'main.samples',
                    fieldTitleName: 'main.sampleName',
                    selectedId : selectedId,
                    vendorId: this.vendorId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subFormGridPanelId + '-sample'),
                    callback:function(ids, titles, result) {
                        if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.subFormGridPanelId + '-sample').store.remove(record);
                break;

            default :
                new ProductDialogWin({
                    single:false,
                    orderId: 'main.orderId',
                    fieldValueName: 'main.samples',
                    fieldTitleName: 'main.sampleName',
                    selectedId : '',
                    vendorId: this.vendorId,
                    productType: 4,
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.subFormGridPanelId + '-sample'),
                    callback:function(ids, titles, result, selectDeatil) {
                        // this.meGrid.getStore().removeAll();
                        if(result.data.items.length > 0){
                            var items = selectDeatil;
                            for(index in items){
                                if(items[index] != undefined && !$checkGridRowExist(this.meGrid.getStore(), items[index].data.id)){
                                    items[index].data.diffQty = Math.abs(items[index].data.expectedQty - items[index].data.receivedQty);
                                    this.meGrid.getStore().add(items[index].data);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});