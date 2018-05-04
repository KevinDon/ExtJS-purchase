NewProductDocumentView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.NewProductDocument.mTitle,
            frameId : 'NewProductDocumentView',
            moduleName:'NewProduct',
            mainGridPanelId : 'NewProductDocumentGridPanelID',
            mainFormPanelId : 'NewProductDocumentFormPanelID',
            searchFormPanelId: 'NewProductDocumentSubFormPanelID',
            mainTabPanelId: 'NewProductDocumentTbsPanelId',
            subGridPanelId : 'NewProductDocumentSubGridPanelID',
            urlList: __ctxPath + 'archives/newproduct/list',
            urlSave: __ctxPath + 'archives/newproduct/save',
            urlDelete: __ctxPath + 'archives/newproduct/delete',
            urlGet: __ctxPath + 'archives/newproduct/get',
            actionName: this.action,
            refresh: true,
            edit: true,
            add: true,
            copy: true,
            del: true,
            editFun: this.editRow
        };

        this.initUIComponents(conf);
        NewProductDocumentView.superclass.constructor.call(this, {
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
                {field:'Q-departmentId-S-EQ', xtype:'hidden'},
                {field:'departmentName', xtype:'DepDialog', title:_lang.TText.fDepartmentName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
                },
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-name-S-LK', xtype:'textfield', title:_lang.ProductDocument.fName},
                {field:'Q-barcode-S-LK', xtype:'textfield', title:_lang.ProductDocument.fBarcode},
                {field:'Q-packageName-S-LK', xtype:'textfield', title:_lang.ProductDocument.fPackageName},
                {field:'Q-categoryId-S-EQ', xtype:'hidden'},
                {field:'CategoryName', xtype:'ProductCategoryDialog', title:_lang.ProductDocument.fCategoryId,
                    formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
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
                {field:'Q-color-S-LK', xtype:'textfield', title:_lang.ProductDocument.fColor},
                {field:'Q-model-S-LK', xtype:'textfield', title:_lang.ProductDocument.fModel},
                {field:'Q-style-S-LK', xtype:'textfield', title:_lang.ProductDocument.fStyle},
                {field:'Q-electricalProduct-N-EQ', xtype:'combo', title:_lang.ProductDocument.fElectricalProduct, value:'',
                	 store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
                },
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel


        this.gridPanel = $newProductGrid(this, conf);
        this.gridPanel.addListener('itemclick', this.rowClick);

        this.NewProductDocumentViewTabsPanel = new App.NewProductDocumentViewTabs({
            region: 'south',
            mainTabPanelId: conf.mainTabPanelId,
            mainGridPanelId: conf.mainGridPanelId,
            // frameId : conf.frameId,
            split: true,
            height: '40%',
            items: []
        });


        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.NewProductDocumentViewTabsPanel ]
        });
    },// end of the init

    rowClick : function(dataview, record, item, index, e, conf){
        var me = this;
        var reportList = this.scope.NewProductDocumentViewTabsPanel.items.items[0];

        $postUrl({
            url :  __ctxPath + 'archives/product/get' + '?id=' + record.data.id, autoMessage:false,
            loadMask:true, maskTo:me.conf.mainTabPanelId,
            callback:function(response){
                var json = Ext.JSON.decode(response.responseText);

                //attachment init
                // var attachmentsList = Ext.getCmp(me.conf.mainTabPanelId + '-1');
                // attachmentsList.getStore().removeAll();
                // if(json.data.attachments != undefined && json.data.attachments.length>0){
                //     for(index in json.data.attachments){
                //         var attachments = {};
                //         attachments = json.data.attachments[index];
                //         Ext.applyIf(attachments, json.data.attachments[index].document);
                //         attachments.id= json.data.attachments[index].documentId;
                //         attachmentsList.getStore().add(attachments);
                //     }
                // }

                var imagesList = Ext.getCmp(me.conf.mainTabPanelId + '-2');
                imagesList.getStore().removeAll();
                if(json.data.prop.imagesDoc != undefined && json.data.prop.imagesDoc.length>0){
                    for(index in json.data.prop.imagesDoc){
                        var images = {};
                        images = json.data.prop.imagesDoc[index];
                        Ext.applyIf(images, json.data.prop.imagesDoc[index].document);
                        images.id= json.data.prop.imagesDoc[index].documentId;
                        imagesList.getStore().add(images);
                    }
                }

                //相关报告
                reportList.setValue(json.data.reports);
            }
        });
    },

    editRow : function(conf){
        new NewProductDocumentForm(conf).show();
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        if(record.data['prop.flagDevStatus'] != 1 || record.data['prop.flagComplianceStatus'] != 1 || record.data['prop.flagQcStatus']  != 1){
            return Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorRecord)
        }
        //console.log(record);
        new NewProductDocumentFormCheck(record.raw).show();
    }
});