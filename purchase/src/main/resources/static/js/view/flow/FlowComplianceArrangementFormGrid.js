Ext.define('App.FlowComplianceArrangementFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowComplianceArrangementFormGrid',

    constructor : function(conf){

        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowComplianceArrangement.mTitle,
            moduleName: 'FlowComplianceArrangement',
            winId : 'FlowComplianceArrangementForm',
            frameId : 'FlowComplianceArrangementForm',
            mainGridPanelId : 'FlowComplianceArrangementViewGridPanelID',
            mainFormPanelId : 'FlowComplianceArrangementViewFormPanelID',
            processFormPanelId: 'FlowComplianceArrangementViewProcessFormPanelID',
            searchFormPanelId: 'FlowComplianceArrangementViewSearchFormPanelID',
            mainTabPanelId: 'FlowComplianceArrangementViewMainTbsPanelID',
            subGridPanelId : 'FlowComplianceArrangementViewSubGridPanelID',
            formGridPanelId : 'FlowComplianceArrangementFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowComplianceArrangementFormGrid.superclass.constructor.call(this, {
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
                'id','sku','name','barcode',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords','remark',
                'mandatory', 'creatorName','departmentName', 'vendorName','productLink','newPrevRiskRating','prevRiskRating'
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
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 100},
                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 80 },

                { header: _lang.FlowComplianceArrangement.fRiskRating,
                    columns: [
                        { header: _lang.FlowComplianceArrangement.fNewPrevRiskRating, dataIndex: 'newPrevRiskRating', width: 120,
                            renderer: function(value){
                                var $riskRating = _dict.riskRating;
                                if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                                    return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                                }
                            }
                        },
                        { header: _lang.FlowComplianceArrangement.fPrevRiskRating, dataIndex: 'prevRiskRating', sortable: true, width: 120,
                            renderer: function(value){
                                var $riskRating = _dict.riskRating;
                                if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                                    return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                                }
                            }
                        }]
                },

                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 60},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                // { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                // { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 100 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 160,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 160 },
                { header: _lang.TText.fRemark, dataIndex: 'remark', width: 200,
                    editor : {
                        xtype:'textarea',
                    },
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        return value;
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
                    productType:'2',
                    selectedId : selectedId,
		            meForm: Ext.getCmp(conf.mainFormPanelId),
		            meGrid: Ext.getCmp(conf.formGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0){
                            var product = {};

                            Ext.apply(product, result[0].raw);
                            Ext.applyIf(product, result[0].raw.prop);
                            product.sku = result[0].raw.sku;
                            product.id = result[0].raw.id;
                            product.prevRiskRating = result[0].raw.prop.riskRating;
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
                    productType:'2',
                    selectedId : '',
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
                                    product.sku = items[index].raw.sku;
                                    product.id = items[index].raw.id;
                                    product.prevRiskRating = items[index].raw.prop.riskRating;
		            				this.meGrid.getStore().add(product);
		            			}
		            		}
		            	}
		            }}, false).show();
		        
				break;
		}
	}
});
