Ext.define('App.FlowSampleQcFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowSampleQcFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        this.noTitle = this.noTitle || false;

        var conf = {
    		title :  _lang.NewProductDocument.tabListTitle,
            moduleName: 'FlowSampleQc',
            winId : 'FlowSampleQcForm',
            frameId : 'FlowSampleQcView',
            mainGridPanelId : 'FlowSampleQcGridPanelID',
            mainFormPanelId : 'FlowSampleQcFormPanelID',
            processFormPanelId: 'FlowSampleQcProcessFormPanelID',
            searchFormPanelId: 'FlowSampleQcSearchFormPanelID',
            mainTabPanelId: 'FlowSampleQcMainTbsPanelID',
            subGridPanelId : 'FlowSampleQcSubGridPanelID',
            formGridPanelId : this.formGridPanelId || 'FlowSampleQcFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowSampleQcFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            title :  this.noTitle? _lang.NewProductDocument.tabListTitle: '',
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
                Ext.applyIf(product, values[i].product);
                Ext.applyIf(product, values[i].product.prop);
                product.prevRiskRating = values[i].product.prop.riskRating;
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
            id: conf.formGridPanelId,
            title: this.noTitle? '' : _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            url: '',
            bbar: false,
            tools: this.noTitle? '' :  tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header: this.noTitle? false : { cls:'x-panel-header-gray'},
            fields: [
                'id','sku','combined', 'name',
                'barcode','categoryName',
                'packageName','color','model','style','length',
                'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
                'seasonal', 'indoorOutdoor','electricalProduct',
                'powerRequirements','mandatory','operatedAt',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.noTitle? true: this.readOnly,
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
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 40 ,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 200 },
                { header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 40 },
                { header: _lang.ProductDocument.fModel, dataIndex: 'model', width: 40 },

                { header: _lang.ProductDocument.fProductInformation, columns:[
                    { header: _lang.ProductDocument.fStyle, dataIndex: 'style', width: 40 ,sortable: true, },
                    { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
                ]},

                { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 40,

                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }

                },
                { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 40},
                { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
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
                            var product = {};
                            Ext.apply(product, result[0].raw);
                            Ext.applyIf(product, result[0].raw.prop);
	            			this.meGrid.getStore().insert(idx, product);
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
		            callback:function(ids, titles, result,selectedData) {
		            	if(result.data.items.length>0){
		            		var items = selectedData;
		            		for(index in items){
		            			if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
		            			    var product = {};
		            			    Ext.apply(product, items[index].raw);
		            			    Ext.applyIf(product, items[index].raw.prop);
		            				this.meGrid.getStore().add(product);
		            			}
		            		}
		            	}
		            }}, false).show();
		        
				break;
		}
	}
});
