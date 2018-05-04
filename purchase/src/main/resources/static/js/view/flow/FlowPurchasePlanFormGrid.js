Ext.define('App.FlowPurchasePlanFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowPurchasePlanFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
    		title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowPurchasePlan',
            winId : 'FlowPurchasePlanViewForm',
            frameId : 'FlowPurchasePlanView',
            mainGridPanelId : 'FlowPurchasePlanGridPanelID',
            mainFormPanelId : 'FlowPurchasePlanFormPanelID',
            processFormPanelId: 'FlowPurchasePlanProcessFormPanelID',
            searchFormPanelId: 'FlowPurchasePlanFormGridSearchFormPanelID',
            mainTabPanelId: 'FlowPurchasePlanFormGridMainTbsPanelID',
            ProductGridPanelId : 'FlowPurchasePlanProductGridPanelID',
            formGridPanelId : this.mainTabPanelId != undefined? this.mainTabPanelId + '-products' : 'FlowPurchasePlanFormGridPanelID',
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 260;
        conf.noTitle = this.noTitle || false;

        this.initUIComponents(conf);
        
        App.FlowPurchasePlanFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
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
            		this.setHeight(conf.defHeight);
        			this.formGridPanel.setHeight(conf.defHeight-3);
            }},{
        		type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
        		handler: function(event, toolEl, panelHeader) {
        			this.setHeight(700);
        			this.formGridPanel.setHeight(693);
           	}}
        ];

        this.formGridPanel = new HP.GridPanel({
                region: 'center',
                id: conf.formGridPanelId,
                title: conf.noTitle ?  conf.title : _lang.NewProductDocument.tabListTitle,
                forceFit: false,
                width: 'auto',
                height: conf.defHeight-3,
                url: '',
                bbar: false,
                tools: tools,
                autoLoad: false,
                rsort: false,
                edit: !this.readOnly,
                header: conf.noTitle ? false : {cls: 'x-panel-header-gray'},
                fields: [
                    'id','sku','categoryId','categoryName', 'name','riskRating','orderQty',
                    'priceAud','priceRmb', 'priceUsd','orderValueAud','orderValueRmb','orderValueUsd','rateAudToRmb','rateAudToUsd','originPortId','originPortCnName',
                    'originPortEnName','destinationPortId','destinationPortCnName','destinationPortEnName','netWeight','masterCartonL','currency','pcsPerCarton','orderQtyCarton',
                    'innerCartonW','innerCartonH','innerCartonCbm','masterCartonStructure','masterCartonWeight',
                    'masterCartonW','masterCartonH','quotationPriceAud','quotationPriceRmb','quotationPriceUsd','quotationRateAudToRmb','quotationRateAudToUsd',
                    'productParameter', 'moq', 'orderQtyCarton','productDetail','masterCartonCbm','innerCartonL',
                    'length','width','height','netWeight','cubicWeight','cbm',
                    'prevPriceUsd','prevPriceAud','prevPriceRmb','prevRateAudToRmb','prevRateAudToUsd',
                    'orderValueAud','orderValueRmb','orderValueUsd','productQuotationId','productQuotationBusinessId','productQuotationDetailBusinessId'
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
                    { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                    { header:  _lang.FlowProductQuotation.fProductQuotationId, dataIndex: 'productQuotationId', width: 40, hidden: true },
                    { header:  _lang.FlowProductQuotation.fProductQuotationBusinessId, dataIndex: 'productQuotationBusinessId', width: 40, hidden: true },
                    { header:  _lang.FlowProductQuotation.fProductQuotationDetailBusinessId, dataIndex: 'productQuotationDetailBusinessId', width: 40, hidden: true },
                    { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200 , locked: true},
                    { header: _lang.ProductCategory.mTitle, dataIndex: 'categoryId', width: 180, hidden: true},
                    { header: _lang.ProductCategory.mTitle, dataIndex: 'categoryName', width: 160},
                    { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },
                    { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'riskRating',align: 'center', width: 100,
                        renderer: function(value){
                            var $riskRating = _dict.riskRating;
                            if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                            }
                        }
                    },
                    { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency',
                        renderer: function(value){
                            var $currency = _dict.currency;
                            if($currency.length>0 && $currency[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $currency[0].data.options);
                            }
                        }
                    },

                    //汇率
                    { header: _lang.FlowProductQuotation.fNewExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                    { header: _lang.FlowProductQuotation.fOldExchangeRate, columns: $groupExchangeColumns(this, 'prevRateAudToRmb', 'prevRateAudToUsd')},

                    //报价
                    { header: _lang.FlowProductQuotation.fNewPrice,
                        columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                            row.set('orderValueAud', (row.get('priceAud') * row.get('orderQty')).toFixed(3));
                            row.set('orderValueRmb', (row.get('priceRmb') * row.get('orderQty')).toFixed(3));
                            row.set('orderValueUsd', (row.get('priceUsd') * row.get('orderQty')).toFixed(3));
                        },{edit:false, gridId: conf.formGridPanelId})
                    },
                    { header: _lang.FlowProductQuotation.fOldPrice,
                        columns: [
                            { header: _lang.TText.fAUD, dataIndex: 'prevPriceAud', width: 80,   },
                            { header: _lang.TText.fRMB, dataIndex: 'prevPriceRmb', width: 80,   },
                            { header: _lang.TText.fUSD, dataIndex: 'prevPriceUsd', width: 80,   }
                        ]
                    },

                    { header: _lang.ProductDocument.fMoq, dataIndex: 'moq' },

                    //采购数量
                    { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'orderQty', scope:this, width: 80, allowNegative : false,
                        renderer: function (value, meta, record) {
                            meta.tdCls = 'grid-input';
                            return value;
                        },
                        editor: {xtype: 'numberfield', minValue: 0 , enableKeyEvents: true,
                            renderer : function(value){
                                return Math.abs(value);
                            },
                            listeners:{
                                    change: function(pt, newValue, oldValue, eOpts){

                                        //console.log(pt.column.ownerCt.grid.getSelectionModel().getSelection()[0].data);
                                        var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                        var value =pt.getValue();
                                        //采购数不能少于起订量 //暂时不做判断
                                        // if(value >= 0){
                                        //     if(value < row.get('moq')){
                                        //         Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowPurchaseContract.rsErrorThanMoqQty);
                                        //         row.set('orderQty', row.get('moq'));
                                        //         this.value = row.get('orderQty');
                                        //         this.setRawValue(row.get('orderQty'));
                                        //     }
                                        // }

                                        row.set('orderValueAud', parseFloat(row.get('priceAud') * value).toFixed(2));
                                        row.set('orderValueRmb', parseFloat(row.get('priceRmb') * value).toFixed(2));
                                        row.set('orderValueUsd', parseFloat(row.get('priceUsd') * value).toFixed(2));
                                        //carton
                                        var pcsPerCarton = parseInt(row.get('pcsPerCarton') || 0);
                                        if(pcsPerCarton > 0){
                                            var cartons = Math.ceil(value / pcsPerCarton);
                                            row.set('orderQtyCarton', cartons);
                                        }
                                        //更新总金额
                                        if(!!conf.dataChangeCallback) {
                                            conf.dataChangeCallback.call(conf.scope);
                                        }
                                    },
                                specialkey: function (pt, newValue, oldValue, eOpts) {
                                    //console.log(pt.column.scope.formGridPanel.getStore().getUpdatedRecords());
                                    if (event.keyCode == '13') {
                                        //console.log(pt.column.ownerCt.grid.getSelectionModel().getSelection()[0].data);
                                        var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                        var value = pt.getValue();
                                        //采购数不能少于起订量 //暂时不做判断
                                        if(value >= 0){
                                            if(value < row.get('moq')){
                                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.FlowPurchaseContract.rsErrorThanMoqQty);
                                                row.set('orderQty', row.get('moq'));
                                                this.value = row.get('orderQty');
                                                this.setRawValue(row.get('orderQty'));
                                            }
                                        }
                                        row.set('orderValueAud', parseFloat(row.get('priceAud') * value).toFixed(2));
                                        row.set('orderValueRmb', parseFloat(row.get('priceRmb') * value).toFixed(2));
                                        row.set('orderValueUsd', parseFloat(row.get('priceUsd') * value).toFixed(2));
                                        //carton
                                        var pcsPerCarton = parseInt(row.get('pcsPerCarton') || 0);
                                        if(pcsPerCarton > 0){
                                            var cartons = Math.ceil(value / pcsPerCarton);
                                            row.set('orderQtyCarton', cartons);
                                        }
                                        //更新总金额
                                        if(!!conf.dataChangeCallback) {
                                            conf.dataChangeCallback.call(conf.scope);
                                        }
                                    }
                                }
                        }
                    }},

                    { header: _lang.FlowPurchasePlan.fOrderQtyCarton, dataIndex: 'orderQtyCarton', allowNegative : false },
                    { header: _lang.NewProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', scope:this, width: 80, hidden: true},
                    //采购货值
                    { header: _lang.NewProductDocument.fOrderValue,
                        columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null, {edit:false, gridId: conf.formGridPanelId})
                    },

                    { header: _lang.ProductDocument.fOriginPort, dataIndex: 'originPortId',
                        renderer: function(value){
                            var $origin = _dict.origin;
                            if($origin.length>0 && $origin[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $origin[0].data.options);
                            }
                        }
                    },
                    { header: _lang.FlowPurchasePlan.fDestinationPort, dataIndex: 'destinationPortId',
                        renderer: function(value){
                            var $destination = _dict.destination;
                            value = value==null?  1: value;
                            if($destination.length>0 && $destination[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $destination[0].data.options);
                            }
                        },
                        editor: {xtype: 'dictcombo', code:'service_provider', codeSub:'destination_port'}
                    },

                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight' },
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
                            { header: _lang.ProductDocument.fInnerCartonL, dataIndex: 'innerCartonL' },
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
        //console.log(Ext.getCmpByName('orderQty'));

        // console.log(this.formGridPanel.getColumnModel());
        // var task = new Ext.util.DelayedTask(function(){
        //     alert(Ext.getDom('orderQty').value.length);
        // });

        // // Wait 500ms before calling our function. If the user presses another key
        // // during that 500ms, it will be cancelled and we'll wait another 500ms.
        // Ext.getDom('orderQty').on('keypress', function() {
        //     task.delay(500);
        // });
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
                    filterVendor: true,
                    productType: '5',
                    selectedId : selectedId,
		            meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.formGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].raw.product.id)){
                            var product = {};
                            Ext.apply(product, result[0].raw.product);
                            Ext.apply(product, result[0].raw.product.prop);
                            Ext.applyIf(product, result[0].raw);
                            product.sku =  result[0].raw.sku;
                            product.id =  result[0].raw.product.id;
                            product.prevPriceAud =   result[0].raw.product.prop.quotationPriceAud;
                            product.prevPriceRmb =  result[0].raw.product.prop.quotationPriceRmb;
                            product.prevPriceUsd =    result[0].raw.product.prop.quotationPriceUsd;
                            product.prevRateAudToRmb =    result[0].raw.product.prop.quotationRateAudToRmb;
                            product.prevRateAudToUsd =    result[0].raw.product.prop.quotationRateAudToUsd;
                            product.rateAudToRmb =  result[0].raw.rateAudToRmb;
                            product.rateAudToUsd =  result[0].raw.rateAudToUsd;
                            //'productQuotationId','productQuotationBusinessId'
                            product.productQuotationId =result[0].raw.id;
                            product.productQuotationBusinessId =result[0].raw.businessId;
                            product.productQuotationDetailBusinessId =result[0].raw.detailBusinessId;

                            product.orderQty =result[0].raw.product.prop.moq;
                            product.orderValueAud =  (result[0].raw.moq *  result[0].raw.priceAud).toFixed(2);
                            product.orderValueRmb =  (result[0].raw.moq * result[0].raw.priceRmb).toFixed(2);
                            product.orderValueUsd =  (result[0].raw.moq * result[0].raw.priceUsd).toFixed(2);
                            product.orderQtyCarton = Math.ceil((parseInt(result[0].raw.moq) || 0) / (parseInt(result[0].raw.product.prop.pcsPerCarton) || 1));
	            			this.meGrid.getStore().insert(idx, product);
	            			this.meGrid.getStore().removeAt(idx+1);
		            	}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.formGridPanelId).store.remove(record);
				break;
				
			default :
		        new OtherProductDialogWin ({
		            single:false,
		            fieldValueName: 'main_products',
		            fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
		            selectedId : '',
                    productType: '5',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.formGridPanelId),
		            callback:function(ids, titles, result, selectedData) {
		            	if(result.data.items.length>0){
		            		var items = selectedData;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.product.id)){
                                    var product = {};
                                    Ext.apply(product, items[index].raw);
                                    Ext.applyIf(product, items[index].raw.product);
                                    Ext.applyIf(product, items[index].raw.product.prop);
                                    product.sku =  items[index].raw.sku;
                                    product.id =  items[index].raw.product.id;

                                    //起订量如果为null
                                    if(!!items[index].raw.product.prop.moq && items[index].raw.product.prop.moq > 0) product.moq = items[index].raw.product.prop.moq;
                                    else product.moq = 0;

                                    product.orderQty = items[index].raw.moq;
                                    product.orderValueAud =  (items[index].raw.moq *  items[index].raw.priceAud).toFixed(2);
                                    product.orderValueRmb =  (items[index].raw.moq * items[index].raw.priceRmb).toFixed(2);
                                    product.orderValueUsd =  (items[index].raw.moq * items[index].raw.priceUsd).toFixed(2);
                                    product.orderQtyCarton = Math.ceil((parseInt(items[index].raw.moq) || 0) / (parseInt(items[index].raw.product.prop.pcsPerCarton) || 1));

                                    product.productQuotationId = items[index].raw.id;
                                    product.productQuotationBusinessId = items[index].raw.businessId;
                                    product.productQuotationDetailBusinessId = items[index].raw.detailBusinessId;
                                    this.meGrid.getStore().add(product);
                                }
                            }
		            	}
		            }}, false).show();
				break;
		}
	},

    onQueryRowAction: function(grid, record, action, idx, col, conf) {
        var grid = Ext.getCmp(this.conf.formGridPanelId);
        var form = Ext.getCmp(this.conf.mainFormPanelId);
        var vendorId = form.getCmpByName('main.vendorId').getValue();
        if (grid.getStore().getCount() > 0) {
            Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureReplace, function (button) {
                if (button == 'yes') {
                        grid.getStore().removeAll();
                        Ext.Ajax.request({
                            url:  __ctxPath + 'archives/flow/productquotation/listForImport?type=1&vendorId=' + vendorId,
                            scope: this, method: 'post',
                            success: function (response, options) {
                                var items = Ext.decode(response.responseText).data;
                                if (items.length > 0) {
                                    for(var index in items) {
                                        var product = {};
                                        Ext.apply(product, items[index]);
                                        Ext.applyIf(product, items[index].product);
                                        Ext.applyIf(product, items[index].product.prop);
                                        product.sku = items[index].sku;
                                        product.id = items[index].product.id;

                                        //起订量如果为null
                                        if (!!items[index].product.prop.moq && items[index].product.prop.moq > 0) product.moq = items[index].product.prop.moq;
                                        else product.moq = 0;

                                        product.orderQty = items[index].moq;
                                        product.orderValueAud = (items[index].moq * items[index].priceAud).toFixed(2);
                                        product.orderValueRmb = (items[index].moq * items[index].priceRmb).toFixed(2);
                                        product.orderValueUsd = (items[index].moq * items[index].priceUsd).toFixed(2);
                                        product.orderQtyCarton = Math.ceil(parseInt(items[index].moq ? items[index].moq : 0) / parseInt(items[index].product.prop.pcsPerCarton ? items[index].product.prop.pcsPerCarton : 1));

                                        product.productQuotationId = items[index].raw.businessId;
                                        product.productQuotationBusinessId = items[index].raw.detailBusinessId;
                                        grid.getStore().add(product);
                                    }
                                }
                        },
                        failure: function(){Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsError) }
                    });
                }
            });
        }else{
                grid.getStore().removeAll();
                Ext.Ajax.request({
                    url:  __ctxPath + 'archives/flow/productquotation/listForImport?type=1&vendorId=' + vendorId,
                    scope: this, method: 'post',
                    success: function (response, options) {
                        var items = Ext.decode(response.responseText).data;
                        if (items.length > 0) {
                            for(var index in items) {
                                var product = {};
                                Ext.apply(product, items[index]);
                                Ext.applyIf(product, items[index].product);
                                Ext.applyIf(product, items[index].product.prop);
                                product.sku = items[index].sku;
                                product.id = items[index].product.id;

                                //起订量如果为null
                                if (!!items[index].product.prop.moq && items[index].product.prop.moq > 0) product.moq = items[index].product.prop.moq;
                                else product.moq = 0;

                                product.orderQty = items[index].moq;
                                product.orderValueAud = (items[index].moq * items[index].priceAud).toFixed(2);
                                product.orderValueRmb = (items[index].moq * items[index].priceRmb).toFixed(2);
                                product.orderValueUsd = (items[index].moq * items[index].priceUsd).toFixed(2);
                                product.orderQtyCarton = Math.ceil(parseInt(items[index].moq ? items[index].moq : 0) / parseInt(items[index].product.prop.pcsPerCarton ? items[index].product.prop.pcsPerCarton : 1));

                                product.productQuotationId = items[index].raw.businessId;
                                product.productQuotationBusinessId = items[index].raw.detailBusinessId;
                                grid.getStore().add(product);
                            }
                        }
                    },
                    failure: function(){ Ext.ux.Toast.msg(_lang.TText.titleOperation,  _lang.TText.rsError) }
                });
        }
    }
});
