Ext.define('App.FlowOrderQualityInspectionFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowOrderQualityInspectionFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowNewProduct.mTitle,
            moduleName: 'FlowOrderQualityInspection',
            winId : 'FlowOrderQualityInspectionForm',
            frameId : 'FlowOrderQualityInspectionView',
            mainGridPanelId : 'FlowOrderQualityInspectionGridPanelID',
            mainFormPanelId : 'FlowOrderQualityInspectionFormPanelID',
            processFormPanelId: 'FlowOrderQualityInspectionProcessFormPanelID',
            searchFormPanelId: 'FlowOrderQualityInspectionSearchFormPanelID',
            mainTabPanelId: 'FlowOrderQualityInspectionMainTbsPanelID',
            subOrderGridPanelId : 'FlowOrderQualityInspectionSubOrderGridPanelID',
            formGridPanelId : 'FlowOrderQualityInspectionFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowOrderQualityInspectionFormGrid.superclass.constructor.call(this, {
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
            title: _lang.FlowOrderQualityInspection.tabOrderDetail,
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
                'orderId','orderNumber','orderTitle','currency','totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb','rateAudToUsd',
                'depositAud','depositRmb','depositUsd'
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
                { header: 'ID', dataIndex: 'orderId', width: 40, hidden: true },
                { header:_lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 200, },
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 120, },

                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
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
				new OrderDialogWin({
                    vendorId: Ext.getCmp(this.conf.mainFormPanelId).vendorId,
		            single:true,
                    formal:true,
                    type:5,
                    fieldValueName: 'main_order',
		            fieldTitleName: 'main_OrderName',
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
		        new OrderDialogWin({
		            single:false,
                    vendorId: Ext.getCmp(this.conf.mainFormPanelId).vendorId,
                    frameId :this.conf.frameId,
                    formal:true,
                    type:5,
                    fieldValueName: 'main_order',
		            fieldTitleName: 'main_OrderName',
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
