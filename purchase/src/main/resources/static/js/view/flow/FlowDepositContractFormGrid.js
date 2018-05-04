Ext.define('App.FlowDepositContractFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowDepositContractFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
    		title : _lang.FlowSamplePayment.mTitle,
            moduleName: 'FlowDepositContract',
            winId : 'FlowDepositContractViewForm',
            frameId : 'FlowDepositContractView',
            mainGridPanelId : 'FlowDepositContractGridPanelID',
            mainFormPanelId : 'FlowDepositContractFormPanelID',
            processFormPanelId: 'FlowDepositContractProcessFormPanelID',
            searchFormPanelId: 'FlowDepositContractSearchFormPanelID',
            mainTabPanelId: 'FlowDepositContractMainTbsPanelID',
            subProductGridPanelId : 'FlowDepositContractSubGridPanelID',
            formGridPanelId : 'FlowDepositContractFormGridPanelID',
        };
        
        this.initUIComponents(conf);
        
        App.FlowDepositContractFormGrid.superclass.constructor.call(this, {
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
            //tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','sku','name','barcode','priceAud','priceRmb','priceUsd','orderQty','categoryName',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords',
                'mandatory', 'creatorName','departmentName',  'vendorName','productLink','newPrevRiskRating','prevRiskRating','isNeedDeposit'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.FlowPurchaseContract.fIsNeedDeposit, dataIndex: 'isNeedDeposit',width: 80,
                    renderer: function (value, meta, record) {
                        meta.tdCls = 'grid-input';
                        if (value == undefined || value == '') {
                            //根据定金率设置需要定金默认值
                            if (Ext.getCmp(conf.mainFormPanelId).depositType == 1) value = 2;
                            if (Ext.getCmp(conf.mainFormPanelId).depositType == 2) value = 1;
                            record.data['isNeedDeposit'] = value;
                        }
                        this.up().isNeedDeposit = value;
                        if (value) {
                            var $settlementType = _dict.getValueRow('optionsYesNo', value);
                            // if(!!conf.dataChangeCallback) {
                            //     conf.dataChangeCallback.call(conf.scope);
                            // }
                            if (curUserInfo.lang == 'zh_CN') {
                                return $settlementType.cnName;
                            } else {
                                return $settlementType.enName;
                            }
                        }
                    },
                },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 200, },
                { header: _lang.FlowFeeRegistration.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,{edit:false, gridId: conf.formGridPanelId,})
                },
                {header: _lang.ProductCombination.fQty, dataIndex: 'orderQty', width: 60, scope:this, },
                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 40},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                // { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 60 },
                // { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },

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
		        new ProductDialogWin({
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
