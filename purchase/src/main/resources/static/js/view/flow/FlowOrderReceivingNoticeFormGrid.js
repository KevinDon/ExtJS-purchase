Ext.define('App.FlowOrderReceivingNoticeFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowOrderReceivingNoticeFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowOrderReceivingNotice',
            winId : 'FlowOrderReceivingNoticeViewForm',
            frameId : 'FlowOrderReceivingNoticeView',
            mainGridPanelId : 'FlowOrderReceivingNoticeViewGridPanelID',
            mainFormPanelId : 'FlowOrderReceivingNoticeViewFormPanelID',
            processFormPanelId: 'FlowOrderReceivingNoticeProcessFormPanelID',
            searchFormPanelId: 'FlowOrderReceivingNoticeViewSearchFormPanelID',
            mainTabPanelId: 'FlowOrderReceivingNoticeViewMainTbsPanelID',
            subGridPanelId : 'FlowOrderReceivingNoticeViewSubGridPanelID',
            formGridPanelId : 'FlowOrderReceivingNoticeViewFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowOrderReceivingNoticeFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 260, width: 'auto',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
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
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
				'id','productId','sku','expectedQty','receivedQty','priceAud','priceRmb','priceUsd','priceCostAud','priceCostRmb','priceCostUsd', 'rateAudToRmb','rateAudToUsd',
				'netWeight','masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonStructure','masterCartonWeight','currency',
                'innerCartonL','masterCartonCbm','masterCartonW'
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
                { header: 'ProductId', dataIndex: 'productId', width: 40, hidden: true },
                { header: _lang.FlowOrderReceivingNotice.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                //报价
                { header: _lang.FlowOrderReceivingNotice.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', function(row, value){
                        row.set('orderValueAud', (row.get('priceAud') * row.get('moq')).toFixed(3));
                        row.set('orderValueRmb', (row.get('priceRmb') * row.get('moq')).toFixed(3));
                        row.set('orderValueUsd', (row.get('priceUsd') * row.get('moq')).toFixed(3));
                    },{gridId: conf.formGridPanelId})
                },

                //汇率
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
                        {header: _lang.ProductDocument.fMasterCartonH, dataIndex: 'masterCartonWeight', width: 80,},
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
		            selectedId : selectedId,
                    productType:1,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.formGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0){
	            			this.meGrid.getStore().insert(idx, result[0].data);
	            			this.meGrid.getStore().removeAt(idx+1);
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
		            selectedId : '',
                    productType:1,
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
