Ext.define('App.FlowSamplePaymentFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowSamplePaymentFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowSamplePayment.mTitle,
            moduleName: 'FlowSamplePayment',
            winId : 'FlowSamplePaymentViewForm',
            frameId : 'FlowSamplePaymentView',
            mainGridPanelId : 'FlowSamplePaymentGridPanelID',
            mainFormPanelId : 'FlowSamplePaymentFormPanelID',
            processFormPanelId: 'FlowSamplePaymentProcessFormPanelID',
            searchFormPanelId: 'FlowSamplePaymentSearchFormPanelID',
            mainTabPanelId: 'FlowSamplePaymentMainTbsPanelID',
            subProductGridPanelId : 'FlowSamplePaymentSubGridPanelID',
            formGridPanelId : 'FlowSamplePaymentFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowSamplePaymentFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
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
            header:{ cls:'x-panel-header-gray' },
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
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 160},
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 50,
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
                       
                        var gridPanel = Ext.getCmp(conf.formGridPanelId);
                        var totalPriceAud = 0;
                        var totalPriceRmb = 0;
                        var totalPriceUsd = 0;

                        for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                            totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.orderValueAud);
                            totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.orderValueRmb);
                            totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.orderValueUsd);
                        }

                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalSampleFeeAud').setValue(totalPriceAud.toFixed(3));
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalSampleFeeRmb').setValue(totalPriceRmb.toFixed(3));
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.totalSampleFeeUsd').setValue(totalPriceUsd.toFixed(3));

                    },{gridId: conf.formGridPanelId})
                },
                //采购数量
                { header: _lang.FlowSample.fSampleQty, dataIndex: 'qty', scope:this, width: 80,
                    editor: {xtype: 'numberfield', minValue: 1 , listeners:{
                        change:function(pt, newValue, oldValue, eOpts ){
                            var gridPanel = pt.column.scope.formGridPanel;

                            var row = gridPanel.getSelectionModel().selected.getAt(0);

                            var rowAud = (row.get('sampleFeeAud') * newValue).toFixed(3);
                            var rowRmb = (row.get('sampleFeeRmb') * newValue).toFixed(3);
                            var rowUsd = (row.get('sampleFeeUsd') * newValue).toFixed(3);
                            row.set('orderValueAud',rowAud);
                            row.set('orderValueRmb',rowRmb);
                            row.set('orderValueUsd',rowUsd);

                            //统计总金额
                            var totalPriceAud = 0;
                            var totalPriceRmb = 0;
                            var totalPriceUsd = 0;
                            for(var i = 0; i < gridPanel.getStore().getCount(); i++) {
                                console.log(typeof gridPanel.getStore().getAt(i).data.orderValueAud);
                                totalPriceAud = totalPriceAud + parseFloat(gridPanel.getStore().getAt(i).data.orderValueAud);
                                totalPriceRmb = totalPriceRmb + parseFloat(gridPanel.getStore().getAt(i).data.orderValueRmb);
                                totalPriceUsd = totalPriceUsd + parseFloat(gridPanel.getStore().getAt(i).data.orderValueUsd);
                            }
                            Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeAud').setValue(totalPriceAud.toFixed(3));
                            Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeRmb').setValue(totalPriceRmb.toFixed(3));
                            Ext.getCmp(pt.column.scope.scope.scope.mainFormPanelId).getCmpByName('main.totalSampleFeeUsd').setValue(totalPriceUsd.toFixed(3));
                        }
                    },
                    }},
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', function(row, value){

                    },{edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowSample.fSampleFeeRefund, dataIndex: 'sampleFeeRefund', width: 100,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    },
                    editor: {xtype: 'combo', store: [['', ''],['2', _lang.TText.vNo], ['1', _lang.TText.vYes]] }
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
                    productType:'4',
                    selectedId : selectedId,
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
		        new ProductDialogWin({
		            single:false,
		            fieldValueName: 'main_products',
		            fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
		            selectedId : '',
                    productType:'4',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
		            meGrid: Ext.getCmp(this.conf.formGridPanelId),
		            callback:function(ids, titles, result, selectedData) {
		            	if(result.data.items.length>0) {
                            var items = selectedData;
                            for (index in items) {
                                var product = {};
                                if (items[index] != undefined && !$checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)) {
                                    Ext.apply(product, items[index].raw);
                                    Ext.applyIf(product, items[index].raw.prop);
                                    product.id = items[index].raw.id
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
		            }}, false).show();
		        
				break;
		}
	}
});
