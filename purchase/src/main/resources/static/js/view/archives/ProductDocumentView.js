ProductDocumentView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductDocument.mTitle,
            frameId : 'ProductDocumentView',
            moduleName:'Product',
            mainGridPanelId : 'ProductDocumentGridPanelID',
            mainFormPanelId : 'ProductDocumentFormPanelID',
            searchFormPanelId: 'ProductDocumentSubFormPanelID',
            mainTabPanelId: 'ProductDocumentTbsPanelId',
            subGridPanelId : 'ProductDocumentSubGridPanelID',
            urlList: __ctxPath + 'archives/product/list',
            urlSave: __ctxPath + 'archives/product/save',
            urlDelete: __ctxPath + 'archives/product/delete',
            urlGet: __ctxPath + 'archives/product/get',
            refresh: true,
            edit: true,
            del: true,
            editFun: this.editRow
        };

        this.initUIComponents(conf);
        ProductDocumentView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.centerPanel ]
        });
    },

    initUIComponents: function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            fieldItems:[
               
                {field:'Q-id-S-LK', xtype:'textfield', title:_lang.VendorDocument.fSystemId,},
                {field:'Q-prop.vendorId-S-LK', xtype:'textfield', title:_lang.VendorDocument.fVendorId, },
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-name-S-LK', xtype:'textfield', title:_lang.ProductDocument.fName},
                {field:'Q-barcode-S-LK', xtype:'textfield', title:_lang.ProductDocument.fBarcode},
                {field:'Q-color-S-LK', xtype:'textfield', title:_lang.ProductDocument.fColor},
                {field:'Q-model-S-LK', xtype:'textfield', title:_lang.ProductDocument.fModel},
                {field:'Q-style-S-LK', xtype:'textfield', title:_lang.ProductDocument.fStyle},
                {field:'Q-categoryId-S-EQ', xtype:'hidden'},
                {field:'CategoryName', xtype:'ProductCategoryDialog', title:_lang.ProductDocument.fCategoryId,
                    formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
                },
                {field:'Q-newProduct-N-EQ', xtype:'combo', title:_lang.ProductDocument.fNewProduct, value:'',
               	 store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
               },
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll],['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
               },
                {field:'Q-combined-N-EQ', xtype:'combo', title:_lang.ProductDocument.fCombined, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.ProductDocument.vDisabled], ['1', _lang.ProductDocument.vEnabled]]
                },
                {field:'Q-mandatory-N-EQ', xtype:'combo', title:_lang.ProductDocument.fMandatory, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.ProductDocument.vDisabled], ['1', _lang.ProductDocument.vEnabled]]
                },
                {field:'Q-electricalProduct-N-EQ', xtype:'combo', title:_lang.ProductDocument.fElectricalProduct, value:'',
                	 store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
                },
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title:_lang.TText.fDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			
            ]
        });// end of searchPanel

        this.ProductDocumentViewTabsPanel = new App.ProductDocumentViewTabs({
            region: 'south',
            split: true,
            maxHeight:'300',
            mainTabPanelId: conf.mainTabPanelId,
            mainGridPanelId: conf.mainGridPanelId,
            OrderGridPanelId: 'tabOrderListTitle',
            HistoricalQuoteGridPanelId: 'tabHistoricalQuoteListTitle',
            frameId : 'ProductDocumentView',
            items: [],
        });

        //grid panel
        this.gridPanel = $productGrid(this, conf);
        this.gridPanel.addListener('itemclick', this.rowClick);

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.ProductDocumentViewTabsPanel]
        });
    },// end of the init

    rowClick : function(dataview, record, item, index, e, conf){
    	var me = this;
        var orderList = Ext.getCmp(this.scope.ProductDocumentViewTabsPanel.OrderGridPanelId);
        var reportList = this.scope.ProductDocumentViewTabsPanel.items.items[1];
        var quotationList = Ext.getCmp(this.scope.ProductDocumentViewTabsPanel.HistoricalQuoteGridPanelId);
        orderList.loadData({
            url : this.conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo:me.conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                //相关订单
                orderList.getStore().removeAll();
                if(!!json.data && !!json.data.orders && json.data.orders.length>0){
                    for(var index in json.data.orders){
                        var order = {};
                        Ext.applyIf(order, json.data.orders[index]);
                        orderList.getStore().add(order);
                    }
                }

                //相关报告
                reportList.setValue(json.data.reports);

                //历史报价
                quotationList.getStore().removeAll();
                if(!!json.data && !!json.data.orders && json.data.quotations.length>0){
                    for(var index in json.data.quotations){
                        var quotation = {};
                        Ext.applyIf(quotation, json.data.quotations[index]);
                        quotationList.getStore().add(quotation);
                    }
                }
                
    			//archivesHistory
                if(record.data.id) {
                    Ext.getCmp(this.scope.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);
                }

                var imagesList = Ext.getCmp(me.conf.mainTabPanelId + '-3');
                imagesList.getStore().removeAll();
                if(!!json.data.prop && !!json.data.prop.imagesDoc && json.data.prop.imagesDoc.length>0){
                    for(var index in json.data.prop.imagesDoc){
                        var images = {};
                        images = json.data.prop.imagesDoc[index];
                        Ext.applyIf(images, json.data.prop.imagesDoc[index].document);
                        images.id= json.data.prop.imagesDoc[index].documentId;
                        imagesList.getStore().add(images);
                    }
                }

            }
        });
    },

    editRow : function(conf){
        new ProductDocumentForm(conf).show();
    }
});
