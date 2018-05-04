Ext.define('App.FlowPurchaseContractFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowPurchaseContractFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.ProductDocument.mTitle,
            moduleName: 'FlowPurchaseContract',
            winId : 'FlowPurchaseContractForm',
            frameId : 'FlowPurchaseContractView',
            mainGridPanelId : 'FlowPurchaseContractGridPanelID',
            mainFormPanelId : 'FlowPurchaseContractFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'FlowPurchaseContractSearchFormPanelID',
            mainTabPanelId: 'FlowPurchaseContractMainTbsPanelID',
            subProductGridPanelId : 'FlowPurchaseContractSubProductGridPanelID',
            subVendorGridPanelId:'FlowPurchaseContractSubVendorGridPanelID',
            formGridPanelId : 'FlowPurchaseContractFormGridPanelID',
            dataChangeCallback: this.dataChangeCallback,
            getBalanceRefunds: this.getBalanceRefunds,
            scope: this.scope
        };
        
        this.initUIComponents(conf);
        
        App.FlowPurchaseContractFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 260, width: '100%',
            items: [ this.formGridPanel ],
            bodyCls:'x-panel-body-gray',
        });
    },
    
    initUIComponents: function(conf){
    	var tools = [{
            type:'collapse', tooltip: _lang.TButton.query, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onQueryRowAction.call(this);
            }},{
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
            id: conf.formGridPanelId,
            title: _lang.NewProductDocument.tabListTitle,
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
                'id','purchasePlanDetailId','sku','name','barcode','categoryName', 'productId','purchasePlanId', 'purchasePlanBusinessId',
          		'orderQty','availableQty', 'cartons','availableCarton','srcOrderQty',
                'currency','priceAud','priceRmb','priceUsd',
				'rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
				'productPredictProfitAud','productPredictProfitRmb','productPredictProfitUsd',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton','isNeedDeposit'
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
                { header: _lang.FlowPurchaseContract.fIsNeedDeposit, dataIndex: 'isNeedDeposit', width: 80, value: '1',
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        if(value == undefined || value == '') {
                            //console.log(1);
                            //根据定金率设置需要定金默认值
                            if(Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.depositType').getValue() == 1) value = 2;
                            if(Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.depositType').getValue() == 2) value = 1;
                            record.data['isNeedDeposit'] = value;
                        }
                        this.up().isNeedDeposit = value;
                        if(value){
                            var $settlementType = _dict.getValueRow('optionsYesNo', value);
                            if(!!conf.dataChangeCallback) {
                                conf.dataChangeCallback.call(conf.scope);
                            }
                            if(curUserInfo.lang == 'zh_CN'){
                                return $settlementType.cnName;
                            }else{
                                return $settlementType.enName;
                            }
                        }
                    },
                    editor: { xtype:'dictcombo', code:'options', codeSub:'yesno', }
                },
                { header: 'ID', dataIndex: 'id', width: 180, hidden:true, locked: true},
                { header: _lang.FlowPurchasePlan.fFlowOrderShippingPlanDetailId, dataIndex: 'purchasePlanDetailId', width: 180, locked: true,hidden:true,},
                { header: _lang.FlowPurchasePlan.fPurchasePlanId, dataIndex: 'purchasePlanId', width: 180,  locked: true ,hidden:true, },
                { header: _lang.FlowPurchasePlan.fFlowPurchasePlanId, dataIndex: 'purchasePlanBusinessId', width: 180, locked: true},
                { header:_lang.ProductDocument.fProductId, dataIndex: 'productId', width: 180, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 180},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 160},
                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fBarcode, dataIndex: 'barcode', width: 200, hidden: true },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
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
                { header: _lang.NewProductDocument.fPrice,
                	columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                		row.set('orderValueAud', (row.get('priceAud') * row.get('orderQty')).toFixed(2));
            			row.set('orderValueRmb', (row.get('priceRmb') * row.get('orderQty')).toFixed(2));
            			row.set('orderValueUsd', (row.get('priceUsd') * row.get('orderQty')).toFixed(2));
                	},{edit: false, gridId: conf.formGridPanelId})
                },

                //采购数量
                { header: _lang.FlowPurchasePlan.fAvailableQty, dataIndex: 'availableQty', scope:this, width: 80},
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', scope:this, width: 80,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
                    },
                	editor: {xtype: 'numberfield', minValue: 0, enableKeyEvents: true,
                        listeners:{
                            change: function(pt, newValue, oldValue, eOpts) {
                                var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                var value = pt.getValue();
                                if (value >= 0) {
                                    //采购数不能大于可用数量
                                    if (value > row.get('availableQty')) {
                                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowPurchaseContract.rsErrorThanAvailableQty);
                                        row.set('orderQty', row.get('availableQty'));
                                        this.value = row.get('availableQty');
                                        this.setRawValue(row.get('availableQty'));
                                    }
                                    //
                                    row.set('orderValueAud', (row.get('priceAud') * this.value).toFixed(2));
                                    row.set('orderValueRmb', (row.get('priceRmb') * this.value).toFixed(2));
                                    row.set('orderValueUsd', (row.get('priceUsd') * this.value).toFixed(2));

                                    //carton
                                    var pcsPerCarton = parseInt(row.get('pcsPerCarton') || 0);
                                    if (pcsPerCarton > 0) {
                                        var cartons = Math.ceil(this.value / pcsPerCarton);
                                        row.set('cartons', cartons);
                                    }

                                    //更新总金额
                                    if (!!conf.dataChangeCallback) {
                                        conf.dataChangeCallback.call(conf.scope);
                                    }
                                }
                            },
                            specialkey:function(pt, newValue, oldValue, eOpts){
                                //console.log(pt.column.scope.formGridPanel.getStore().getUpdatedRecords());
                                if (event.keyCode == '13') {
                                    var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                    var value = pt.getValue();
                                    if(value >= 0) {
                                        //采购数不能大于可用数量
                                        if (value > row.get('availableQty')) {
                                            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowPurchaseContract.rsErrorThanAvailableQty);
                                            row.set('orderQty', row.get('availableQty'));
                                            this.value = row.get('availableQty');
                                            this.setRawValue(row.get('availableQty'));
                                        }

                                        row.set('orderValueAud', (row.get('priceAud') * this.value).toFixed(2));
                                        row.set('orderValueRmb', (row.get('priceRmb') * this.value).toFixed(2));
                                        row.set('orderValueUsd', (row.get('priceUsd') * this.value).toFixed(2));

                                        //carton
                                        var pcsPerCarton = parseInt(row.get('pcsPerCarton') || 0);
                                        if (pcsPerCarton > 0) {
                                            var cartons = Math.ceil(this.value / pcsPerCarton);
                                            row.set('cartons', cartons);
                                        }

                                        //更新总金额
                                        if (!!conf.dataChangeCallback) {
                                            conf.dataChangeCallback.call(conf.scope);
                                        }
                                    }
                                }
                            }

                	}
                }},
                { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'srcOrderQty', scope:this, width: 80, hidden:true, },
                { header: _lang.NewProductDocument.fOrderCartonQty, dataIndex: 'cartons', scope:this, width: 80, },

                { header: _lang.NewProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', scope:this, width: 80, hidden: true},

                //采购货值
                { header: _lang.NewProductDocument.fOrderValue, 
                	columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null, {edit:false, gridId: conf.formGridPanelId})
                },


                {header: _lang.ProductDocument.tabInnerCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fInnerCartonL, dataIndex: 'innerCartonL', width: 80,},
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
            ],// end of columns
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
				new OtherProductDialogWin({
		            single:true,
		            fieldValueName: 'main_products',
		            fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    me: this,
                    filterVendor: true,
                    productType:'3',
                    isview :1,
                    selectedId : selectedId,
		            meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.formGridPanelId),
		            callback:function(ids, titles, result) {

		            	if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
                            var product = {};
                            Ext.applyIf(product, result[0].raw.product);
                            Ext.apply(product, result[0].raw.product.prop);
                            Ext.applyIf(product, result[0].raw);
                            product.id = result[0].raw.id;
                            product.sku = result[0].raw.sku;
                            product.productId = result[0].raw.productId;
                            product.purchasePlanDetailId = result[0].raw.id;
                            product.purchasePlanId = result[0].raw.purchasePlanId;
                            product.purchasePlanBusinessId = result[0].raw.flowPurchasePlanId;
                            product.orderQty =result[0].raw.availableQty;
                            // var pcsPerCarton = parseInt(result[0].raw.product.prop.pcsPerCarton || 0);
                            // if (pcsPerCarton > 0) {
                            //     product.cartons = Math.ceil(result[0].raw.orderQty / pcsPerCarton);
                            // }
                            product.cartons = result[0].raw.availableCarton;
                            var count = (result[0].raw.availableQty);
                            product.orderValueAud = result[0].raw.priceAud * count;
                            product.orderValueRmb = result[0].raw.priceRmb * count;
                            product.orderValueUsd = result[0].raw.priceUsd * count;

	            			this.meGrid.getStore().insert(idx, product);
	            			this.meGrid.getStore().removeAt(idx+1);
                            if(!!this.me.getBalanceRefunds) {
                                this.me.getBalanceRefunds.call(this.me.scope, conf);
                            }
		            	}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.formGridPanelId).store.remove(record);
                var purchasePlanIds = '';
                    if(this.formGridPanel.getStore().getCount() > 0){
                        for(var i = 0; i < this.formGridPanel.getStore().getCount(); i++){
                            purchasePlanIds = this.formGridPanel.getStore().getAt(i).data.purchasePlanId + ',' + purchasePlanIds;
                        }
                        var purchasePlanIdsProcess = purchasePlanIds.substring(0,purchasePlanIds.length-1);
                    }
                //访问冲销单据链接
                if(!!purchasePlanIds){
                    $HpStore({
                        fields: ['id','businessId', 'purchasePlanId', 'purchasePlanBusinessId', 'orderNumber','orderId', 'vendorCnName','vendorEnName', 'chargebackReason', 'type', 'chargebackStatus', 'currency',
                            'totalFeeAud', 'totalFeeRmb', 'totalFeeUsd', 'rateAudToRmb', 'rateAudToUsd', 'paymentStatus', 'flowStatus', 'remark',
                            'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName',
                            'startTime', 'endTime', 'createdAt', 'updatedAt', 'status', 'flowStatus', 'processInstanceId','vendorId',
                            'assigneeId','assigneeCnName','assigneeEnName'],
                        url: __ctxPath + 'finance/balanceRefund/list?type=2&vendorId=' + Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.vendorId').getValue() + '&purchasePlanIds=' + purchasePlanIdsProcess, loadMask: true, scope: this,
                        callback: function (obj, records, eOpts) {
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffAud').setValue(0);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffRmb').setValue(0);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.writeOffUsd').setValue(0);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.balanceRefunds').setValue(records);
                        }
                    });
                }
                break;
				
			default :
		        new OtherProductDialogWin({
		            single:false,
		            fieldValueName: 'main_products',
		            fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
		            selectedId : '',
                    productType:'3',
                    me: this,
                    isview :1,
		            meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.formGridPanelId),
		            callback:function(ids, titles, result, selectedData) {
		            	if(result.data.items.length>0){
		            		var items = selectedData;
		            		for(var index in items){
		            		    var product = {};
		            			if(items[index] != undefined && !$checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)) {
                                    Ext.applyIf(product, items[index].raw.product);
                                    Ext.apply(product, items[index].raw.product.prop);
                                    Ext.applyIf(product, items[index].raw);
                                    product.id = items[index].raw.id;
                                    product.sku = items[index].raw.sku
                                    product.productId = items[index].raw.productId;
                                    product.purchasePlanDetailId = items[index].raw.id;
                                    product.purchasePlanId = items[index].raw.purchasePlanId;
                                    product.purchasePlanBusinessId = items[index].raw.flowPurchasePlanId;
                                    product.orderQty = items[index].raw.availableQty;
                                    // var pcsPerCarton = parseInt(items[index].raw.product.prop.pcsPerCarton || 0);
                                    // if (pcsPerCarton > 0) {
                                    //     product.cartons = Math.ceil(items[index].raw.orderQty / pcsPerCarton);
                                    // }
                                    product.cartons = items[index].raw.availableCarton;

                                    var count = (items[index].raw.availableQty);
                                    product.orderValueAud = items[index].raw.priceAud * count;
                                    product.orderValueRmb = items[index].raw.priceRmb * count;
                                    product.orderValueUsd = items[index].raw.priceUsd * count;

                                    this.meGrid.getStore().add(product);
                                }
                            };
                            if(!!this.me.getBalanceRefunds) {
                                this.me.getBalanceRefunds.call(this.me.scope, conf);
                            }
		            	}
		            }}, false).show();
		        
				break;
		}
	},

    onQueryRowAction: function(grid, record, action, idx, col, conf) {
        var me = this;
        var grid = Ext.getCmp(this.conf.formGridPanelId);
        var form = Ext.getCmp(this.conf.mainFormPanelId);
        var vendorId = form.getCmpByName('main.vendorId').getValue();
        var isview = 1;
        if (grid.getStore().getCount() > 0) {
            Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureReplace, function (button) {
                if (button == 'yes') {
                    grid.getStore().removeAll();
                    Ext.Ajax.request({
                        url: __ctxPath + 'archives/flow/purchase/plandetail/list?vendorId=' + vendorId + '&isview=' + isview,
                        scope: this, method: 'post',
                        success: function (response, options) {
                            var items = Ext.decode(response.responseText).data;
                            if (items.length > 0) {
                                for(var index in items) {
                                    var product = {};
                                    Ext.applyIf(product, items[index].product);
                                    Ext.apply(product, items[index].product.prop);
                                    Ext.applyIf(product, items[index]);
                                    product.id = items[index].id;
                                    product.sku = items[index].sku
                                    product.productId = items[index].productId;
                                    product.purchasePlanDetailId = items[index].id;
                                    product.purchasePlanId = items[index].purchasePlanId;
                                    product.purchasePlanBusinessId = items[index].flowPurchasePlanId;
                                    product.orderQty = items[index].availableQty;

                                    product.cartons = items[index].availableCarton;

                                    var count = (items[index].availableQty);
                                    product.orderValueAud = items[index].priceAud * count;
                                    product.orderValueRmb = items[index].priceRmb * count;
                                    product.orderValueUsd = items[index].priceUsd * count;

                                    grid.getStore().add(product);
                                }
                            }
                            if(!!me.getBalanceRefunds) {
                                me.getBalanceRefunds.call(me.scope, conf);
                            }
                        },
                        failure: function(){ Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsError) }
                    });
                }
            });
        }else{
            grid.getStore().removeAll();
            Ext.Ajax.request({
                url: __ctxPath + 'archives/flow/purchase/plandetail/listForImport?vendorId=' + vendorId + '&isview=' + isview,
                scope: this, method: 'post',
                success: function (response, options) {
                    var items = Ext.decode(response.responseText).data;
                    if (items.length > 0) {
                        for(var index in items) {
                            var product = {};
                            Ext.applyIf(product, items[index].product);
                            Ext.apply(product, items[index].product.prop);
                            Ext.applyIf(product, items[index]);
                            product.id = items[index].id;
                            product.sku = items[index].sku
                            product.productId = items[index].productId;
                            product.purchasePlanDetailId = items[index].id;
                            product.purchasePlanId = items[index].purchasePlanId;
                            product.purchasePlanBusinessId = items[index].flowPurchasePlanId;
                            product.orderQty = items[index].availableQty;

                            product.cartons = items[index].availableCarton;

                            var count = (items[index].availableQty);
                            product.orderValueAud = items[index].priceAud * count;
                            product.orderValueRmb = items[index].priceRmb * count;
                            product.orderValueUsd = items[index].priceUsd * count;
                            grid.getStore().add(product);
                        }
                    }
                    if(!!me.getBalanceRefunds) {
                        me.getBalanceRefunds.call(me.scope, conf);
                    }
                    //me.getBalanceRefunds();
                },
                failure: function(){ Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsError) }
            });
        }
    }
});

Ext.define('App.FlowPurchaseContractOtherFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowPurchaseContractOtherFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            mainFormPanelId : this.mainFormPanelId ? this.mainFormPanelId : 'FlowPurchaseContractFormPanelID',
            formGridPanelId :  this.formGridPanelId,
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.FlowPurchaseContractOtherFormGrid.superclass.constructor.call(this, {
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
                Ext.getCmp(conf.formGridPanelId).getStore().add({ id: '',itemId:'1', qty:'1', currency: currency });
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
                            var $feeItem = _dict.getValueRow('feeItem', value);
                            record.data.itemCnName = $feeItem.cnName;
                            record.data.itemEnName = $feeItem.enName;
                            if(curUserInfo.lang == 'zh_CN'){
                                return $feeItem.cnName;
                            }else {
                                return $feeItem.enName;
                            }
                        }
                    },
                    editor: { xtype: 'dictcombo',   code:'service_provider', codeSub:'fee_item', }
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
                {header: _lang.FlowPurchaseContract.fSettlementType, dataIndex: 'settlementType', width: 120, scope: this,
                    renderer: function (value, meta, record) {
                        if(value == undefined || value == '') {
                            value = 1;
                            record.data['settlementType'] = value;
                        }
                        this.up().isNeedDeposit = value;
                        meta.tdCls = 'grid-input';
                        if(!!conf.dataChangeCallback) {
                            conf.dataChangeCallback.call(conf.scope);
                        }
                        if(value){
                            var $settlementType = _dict.getValueRow('settlementType', value);
                            if(curUserInfo.lang == 'zh_CN'){
                                return $settlementType.cnName;
                            }else{
                                return $settlementType.enName;
                            }
                        }
                    },
                    editor: { xtype:'dictcombo', code:'purchase', codeSub:'settlement_type',

                    }
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
