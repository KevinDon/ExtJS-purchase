Ext.define('App.FlowSampleFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowSampleFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowSample',
            winId : 'FlowSampleViewForm',
            frameId : 'FlowSampleView',
            mainGridPanelId : 'FlowSampleViewGridPanelID',
            mainFormPanelId : 'FlowSampleViewFormPanelID',
            processFormPanelId: 'FlowSampleViewProcessFormPanelID',
            searchFormPanelId: 'FlowSampleViewSearchFormPanelID',
            mainTabPanelId: 'FlowSampleViewMainTbsPanelID',
            subGridPanelId : 'FlowSampleViewSubGridPanelID',
            formGridPanelId: 'FlowSampleFormGridPanelID',
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.FlowSampleFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 250, width: '100%',
            items: [ this.formGridPanel ],
            bodyCls:'x-panel-body-gray',
        });
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

        var $currency = new $HpDictStore({code:'transaction', codeSub:'currency'});
        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowSample.tabGridTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id','sku','vendorCnName','vendorEnName','productCategoryId', 'categoryName','name',
                'sampleName','sampleFeeAud','sampleFeeRmb','sampleFeeUsd','currency',
                'sampleFeeRefund','sampleReceiver','rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'qty',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton',
                'length','width','height','netWeight','cubicWeight','cbm'

            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true,  hidden: this.readOnly,
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
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 160,},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                { header: _lang.FlowSample.fSampleFee,
                    columns: new $groupPriceColumns(this, 'sampleFeeAud','sampleFeeRmb','sampleFeeUsd', function(row, value){
                        row.set('orderValueAud', (row.get('sampleFeeAud') * row.get('qty')).toFixed(3));
                        row.set('orderValueRmb', (row.get('sampleFeeRmb') * row.get('qty')).toFixed(3));
                        row.set('orderValueUsd', (row.get('sampleFeeUsd') * row.get('qty')).toFixed(3));
                        if (!!conf.dataChangeCallback) {
                            conf.dataChangeCallback.call(conf.scope);
                        }
                    },{gridId: conf.formGridPanelId})
                },
                //采购数量
                { header: _lang.FlowSample.fSampleQty, dataIndex: 'qty', scope:this, width: 80,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'numberfield', minValue: 1 , listeners:{
                        change:function(pt, newValue, oldValue, eOpts ){
                            var gridPanel = pt.column.scope.formGridPanel;

                            var row = gridPanel.getSelectionModel().selected.getAt(0);

                            //统计总金额
                            var rowAud = (row.get('sampleFeeAud') * newValue).toFixed(3);
                            var rowRmb = (row.get('sampleFeeRmb') * newValue).toFixed(3);
                            var rowUsd = (row.get('sampleFeeUsd') * newValue).toFixed(3);
                            row.set('orderValueAud',rowAud);
                            row.set('orderValueRmb',rowRmb);
                            row.set('orderValueUsd',rowUsd);
                            //更新总金额
                            if (!!conf.dataChangeCallback) {
                                conf.dataChangeCallback.call(conf.scope);
                            }
                            // for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                            //     totalPriceAud = totalPriceAud + parseFloat(rowAud);
                            //     totalPriceRmb = totalPriceRmb + parseFloat(rowRmb);
                            //     totalPriceUsd = totalPriceUsd + parseFloat(rowUsd);
                            // }

                            //Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeAud').setValue(totalPriceAud.toFixed(3));
                            //Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeRmb').setValue(totalPriceRmb.toFixed(3));
                            //Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeUsd').setValue(totalPriceUsd.toFixed(3));
                        }
                    },

                }},
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', function(row, value){

                    },{edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowSample.fSampleFeeRefund, dataIndex: 'sampleFeeRefund', width: 100,
                    renderer: function(value, meta){
                        meta.tdCls = 'grid-input';
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    },
                    editor: {xtype: 'combo', store: [['2', _lang.TText.vNo], ['1', _lang.TText.vYes]], value:'2', },

                },
                //{ header: _lang.FlowSample.fVendorName, dataIndex: 'vendorCnName', width: 260},
                { header: _lang.FlowSample.fSampleName, dataIndex: 'name', width: 200},
                {header: _lang.ProductDocument.tabProductSize,
                    columns: [
                        {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 80,},
                        {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 80,},
                        {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 80,},
                        { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight'  },
                        { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight'  },
                        { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabInnerCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonL', width: 80,},
                        { header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonW' },
                        { header: _lang.ProductDocument.fInnerCartonH, dataIndex: 'innerCartonH' },
                        { header: _lang.ProductDocument.fInnerCartonCbm, dataIndex: 'innerCartonCbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabMasterCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fMasterCartonL, dataIndex: 'masterCartonL', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonW, dataIndex: 'masterCartonW', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonH, dataIndex: 'masterCartonH', width: 80,},
                        { header: _lang.ProductDocument.fMasterCartonCbm, dataIndex: 'masterCartonCbm'  },
                    ],
                },


            ]// end of columns
        });
        this.formGridPanel.store.on('dataChanged', function (store) {
            if(!!conf.dataChangeCallback) {
                conf.dataChangeCallback.call(conf.scope, store, conf);
            }
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ProductDialogWin({
                    single:true,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    productType:'2',
                    selectedId : selectedId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            if(result[0].raw != undefined && ! $checkGridRowExist(this.meGrid.getStore(), result[0].raw.id)){
                                var product = {};
                                Ext.apply(product, result[0].raw);
                                Ext.applyIf(product, result[0].raw.prop);
                                product.id =  result[0].raw.id
                                product.sampleFeeAud = 0;
                                product.sampleFeeRmb = 0;
                                product.sampleFeeUsd = 0;
                                product.orderValueAud = 0;
                                product.orderValueRmb = 0;
                                product.orderValueUsd = 0;
                                product.sampleFeeRefund = 2;
                                this.meGrid.getStore().insert(idx, product);
                                this.meGrid.getStore().removeAt(idx+1);
                            }
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new ProductDialogWin({
                    single:false,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    selectedId : '',
                    productType:'2',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result,selectedData) {
                        if(result.data.items.length>0){
                            var items = selectedData;
                            for(index in items){
                                var product = {};
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    Ext.apply(product, items[index].raw);
                                    Ext.applyIf(product, items[index].raw.prop);
                                    product.id =items[index].raw.id;
                                    //'sampleFeeAud','sampleFeeRmb','sampleFeeUsd',
                                    product.sampleFeeAud = 0;
                                    product.sampleFeeRmb = 0;
                                    product.sampleFeeUsd = 0;
                                    //'orderValueAud','orderValueRmb','orderValueUsd',
                                    product.orderValueAud = 0;
                                    product.orderValueRmb = 0;
                                    product.orderValueUsd = 0;
                                    product.sampleFeeRefund = 2;
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});

Ext.define('App.FlowSampleOtherFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowSampleOtherFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            mainFormPanelId : this.mainFormPanelId ? this.mainFormPanelId : 'FlowSampleFormPanelID',
            formGridPanelId :  this.formGridPanelId,
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.FlowSampleOtherFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            bodyCls:'x-panel-body-gray',
            height: 260, width: '100%',
            items: [ this.formGridPanel ]
        });
    },

    initUIComponents: function(conf){
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                var currency = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.currency').getValue();
                Ext.getCmp(conf.formGridPanelId).getStore().add({ id: '',itemId:'1', qty: '1', currency: currency });
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
            id: conf.formGridPanelId,
            title: _lang.FlowFeeRegistration.tabOtherItem,
            forceFit: false,
            width: 'auto',
            height: 250,
            url: '',
            bbar: false,
            tools: tools,
            header:{ cls:'x-panel-header-gray' },
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            fields: [
                'id','businessId', 'itemId','itemCnName','itemEnName', 'priceAud', 'priceRmb', 'priceUsd', 'currency',
                'qty', 'rateAudToUsd', 'rateAudToRmb', 'remark','subtotalAud','subtotalRmb','subtotalUsd', 'settlementType',
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width: 60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'businessId', width: 180, hidden: true, },
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'itemCnName', width: 180, hidden: true, },
                {header: _lang.ProductDocument.fBusinessId, dataIndex: 'itemEnName', width: 180, hidden: true, },
                {header:_lang.FlowFeeRegistration.fProject, dataIndex: 'itemId', width: 180,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        if(value){
                            var $feeItem = _dict.getValueRow('feeOtherItem', value);
                            record.data.itemCnName = $feeItem.cnName;
                            record.data.itemEnName = $feeItem.enName;
                            if(curUserInfo.lang == 'zh_CN'){
                                return $feeItem.cnName;
                            }else {
                                return $feeItem.enName;
                            }
                        }
                    },
                    editor: { xtype: 'dictcombo',   code:'product', codeSub:'fee_other_item', }
                },

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
                { header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('subtotalAud', (row.get('priceAud') * row.get('qty')).toFixed(2));
                        row.set('subtotalRmb', (row.get('priceRmb') * row.get('qty')).toFixed(2));
                        row.set('subtotalUsd', (row.get('priceUsd') * row.get('qty')).toFixed(2));

                        if(!!conf.dataChangeCallback){
                            conf.dataChangeCallback.call(conf.scope);
                        }
                    },{gridId: conf.formGridPanelId})
                },
                {header: _lang.ProductCombination.fQty, dataIndex: 'qty', width: 60, scope:this,
                    renderer: function (value,meta) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                    editor: {xtype: 'numberfield', minValue: 1 , listeners:{
                        change:function(pt, newValue, oldValue, eOpts ){
                            var gridPanel = pt.column.scope.formGridPanel;
                            var totalPriceAud = 0;
                            var totalPriceRmb= 0;
                            var totalPriceUsd = 0;
                            var row = gridPanel.getSelectionModel().selected.getAt(0);

                            row.set('subtotalAud', (row.get('priceAud') * newValue).toFixed(2));
                            row.set('subtotalRmb', (row.get('priceRmb') * newValue).toFixed(2));
                            row.set('subtotalUsd', (row.get('priceUsd') * newValue).toFixed(2));

                            //统计总金额
                            for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                                totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.subtotalAud);
                                totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.subtotalRmb);
                                totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.subtotalUsd);
                            }
                            if(!!conf.dataChangeCallback){
                                conf.dataChangeCallback.call(conf.scope);
                            }
                        }
                    },
                    },
                },

                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'subtotalAud','subtotalRmb','subtotalUsd', null,
                        { edit:false, gridId: conf.formGridPanelId})
                },
                {header: _lang.TText.fRemark, dataIndex: 'remark', width: 200,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value
                    },
                    editor: {xtype: 'textarea'}
                },
            ]// end of columns
        });

        this.formGridPanel.store.on('dataChanged', function (store) {
            if(!!conf.dataChangeCallback) {
                conf.dataChangeCallback.call(conf.scope, store, conf);
            }
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowEdit' :
                // var selectedId = record.data.id;
                // new ChargeItemDialogWin({
                //     single:true,
                //     fieldValueName: 'main_chargeItem',
                //     fieldTitleName: 'main_chargeItemName',
                //     selectedId : selectedId,
                //     meForm: Ext.getCmp(conf.mainFormPanelId),
                //     meGrid: Ext.getCmp(conf.formGridPanelId),
                //     callback:function(ids, titles, result) {
                //         if(result.length>0){
                //             console.log( result[0].data);
                //             this.meGrid.getStore().insert(idx, result[0].data);
                //             this.meGrid.getStore().removeAt(idx+1);
                //         }
                //     }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new ChargeItemDialogWin({
                    single:false,
                    fieldValueName: 'main_chargeItem',
                    fieldTitleName: 'main_chargeItemName',
                    selectedId : '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    this.meGrid.getStore().add(items[index].raw);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});
