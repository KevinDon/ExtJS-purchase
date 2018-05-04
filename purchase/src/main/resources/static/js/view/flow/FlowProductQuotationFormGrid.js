Ext.define('App.FlowProductQuotationFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowProductQuotationFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowProductQuotation',
            winId : 'FlowProductQuotationViewForm',
            frameId : 'FlowProductQuotationView',
            mainGridPanelId : 'FlowProductQuotationViewGridPanelID',
            mainFormPanelId : 'FlowProductQuotationViewFormPanelID',
            processFormPanelId: 'FlowProductQuotationProcessFormPanelID',
            searchFormPanelId: 'FlowProductQuotationViewSearchFormPanelID',
            mainTabPanelId: 'FlowProductQuotationViewMainTbsPanelID',
            subGridPanelId : 'FlowProductQuotationViewSubGridPanelID',
            formGridPanelId : 'FlowProductQuotationViewFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowProductQuotationFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 260, width: 'auto',
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
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            header:{
                cls:'x-panel-header-gray'
            },
            edit: !this.readOnly,
            fields: [
                'id','sku','name','priceAud','priceRmb','priceUsd','orderValueAud','orderValueRmb','orderValueUsd','priceAud','moq','priceRmb',
                'priceUsd','rateAudToRmb','rateAudToUsd','netWeight','masterCartonL','currency',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId','destinationPortCnName','destinationPortEnName',
                'innerCartonW','innerCartonH','innerCartonCbm','masterCartonStructure','masterCartonW','masterCartonH',
                'prevPriceUsd','prevPriceAud','prevPriceRmb','prevRateAudToRmb','prevRateAudToUsd',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton',
                'length','width','height','netWeight','cubicWeight','cbm'

            ],
            columns: [
                    { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true,
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
                    { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200 , locked: true},

                    { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },
                    { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency',width:40,
                        renderer: function(value){
                            var $currency = _dict.currency;
                            if($currency.length>0 && $currency[0].data.options.length>0){
                                return $dictRenderOutputColor(value, $currency[0].data.options);
                            }
                        }
                    },
                    //汇率
                    { header: _lang.FlowProductQuotation.fNewExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                    { header: _lang.FlowProductQuotation.fOldExchangeRate,
                        columns: [
                            { header: _lang.TText.fRateAudToRmb, dataIndex: 'prevRateAudToRmb', width: 80,   },
                            { header: _lang.TText.fRateAudToUsd, dataIndex: 'prevRateAudToUsd', width: 80,   },
                        ]
                    },

                    //报价
                    { header: _lang.FlowProductQuotation.fNewPrice,
                        columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                            row.set('orderValueAud', (row.get('priceAud') * row.get('moq')).toFixed(3));
                            row.set('orderValueRmb', (row.get('priceRmb') * row.get('moq')).toFixed(3));
                            row.set('orderValueUsd', (row.get('priceUsd') * row.get('moq')).toFixed(3));
                        },{gridId: conf.formGridPanelId})
                    },
                    { header: _lang.FlowProductQuotation.fOldPrice,
                        columns: [
                            { header: _lang.TText.fAUD, dataIndex: 'prevPriceAud', width: 80,   },
                            { header: _lang.TText.fRMB, dataIndex: 'prevPriceRmb', width: 80,   },
                            { header: _lang.TText.fUSD, dataIndex: 'prevPriceUsd', width: 80,   }
                        ]
                    },

                //采购数量
                    { header: _lang.NewProductDocument.fOrderQqt, dataIndex: 'moq', scope:this, width: 80,
                        renderer: function (value, meta, record) {
                            meta.tdCls = 'grid-input';
                            return value;
                        },
                        editor: {xtype: 'numberfield', minValue: 0, listeners:{
                            change:function(pt, newValue, oldValue, eOpts ){
                                var row = pt.column.scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                row.set('orderValueAud', (row.get('priceAud') * newValue).toFixed(3));
                                row.set('orderValueRmb', (row.get('priceRmb') * newValue).toFixed(3));
                                row.set('orderValueUsd', (row.get('priceUsd') * newValue).toFixed(3));
                            }
                        }
                    }},


                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight',width:60, },
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
		            selectedId : selectedId,
                    productType:'2',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.formGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0){
                            if(result[0] != undefined && ! $checkGridRowExist(this.meGrid.getStore(),result[0].raw.id)) {

                                var product = {};
                                Ext.apply(product, result[0].raw);
                                Ext.applyIf(product, result[0].raw.prop);
                                product.sku = result[0].raw.sku;
                                product.id = result[0].raw.id;
                                product.prevPriceAud = result[0].raw.prop.quotationPriceAud;
                                product.prevPriceRmb = result[0].raw.prop.quotationPriceRmb;
                                product.prevPriceUsd = result[0].raw.prop.quotationPriceUsd;
                                product.prevRateAudToRmb = result[0].raw.prop.quotationRateAudToRmb;
                                product.prevRateAudToUsd = result[0].raw.prop.quotationRateAudToUsd;

                                product.rateAudToRmb = curUserInfo.audToRmb;
                                product.rateAudToUsd = curUserInfo.audToUsd;
                                this.meGrid.getStore().insert(idx, product);
                                this.meGrid.getStore().removeAt(idx + 1);
                            }
		            	}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.formGridPanelId).store.remove(record);
				break;
				
			default :
		        new ProductDialogWin ({
		            single:false,
		            fieldValueName: 'main_products',
		            fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
		            selectedId : '',
                    productType:'2',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.formGridPanelId),
		            callback:function(ids, titles, result, selectedData) {
		            	if(result.data.items.length>0){
		            		var items =selectedData;
		            		for(index in items){
		            			if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    var product = {};
                                    Ext.apply(product, items[index].raw);
                                    Ext.applyIf(product, items[index].raw.prop);
                                    product.sku =  items[index].raw.sku;
                                    product.id =  items[index].raw.id;
                                    product.prevPriceAud =  items[index].raw.prop.quotationPriceAud;
                                    product.prevPriceRmb =  items[index].raw.prop.quotationPriceRmb;
                                    product.prevPriceUsd =  items[index].raw.prop.quotationPriceUsd;
                                    product.prevRateAudToRmb =  items[index].raw.prop.quotationRateAudToRmb;
                                    product.prevRateAudToUsd =  items[index].raw.prop.quotationRateAudToUsd;

                                    product.rateAudToRmb = curUserInfo.audToRmb;
                                    product.rateAudToUsd = curUserInfo.audToUsd;

		            				this.meGrid.getStore().add(product);
		            			}
		            		}
		            	}
		            }}, false).show();
		        
				break;
		}
	}
});
