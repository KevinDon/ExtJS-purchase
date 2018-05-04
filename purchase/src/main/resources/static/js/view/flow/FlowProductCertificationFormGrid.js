Ext.define('App.FlowProductCertificationFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowProductCertificationFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.FlowNewProduct.mTitle,
            winId : 'FlowProductCertificationForm',
            moduleName: 'FlowProductCertification',
            frameId : 'FlowProductCertificationView',
            mainGridPanelId : 'FlowProductCertificationGridPanelID',
            mainFormPanelId : 'FlowProductCertificationFormPanelID',
            processFormPanelId: 'FlowProductCertificationProcessFormPanelID',
            searchFormPanelId: 'FlowProductCertificationSearchFormPanelID',
            mainTabPanelId: 'FlowProductCertificationMainTbsPanelID',
            productCertificationGridPanel : 'FlowProductCertificationGridPanelID',
            formGridPanelId : 'FlowProductCertificationFormGridPanelID',
            dataChangeCallback: this.dataChangeCallback,
            scope: this.scope
        };

        this.initUIComponents(conf);

        App.FlowProductCertificationFormGrid.superclass.constructor.call(this, {
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
                'id','sku','combined', 'name',
                'barcode','categoryName',
                'packageName','color','model','style','length',
                'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
                'seasonal', 'indoorOutdoor','electricalProduct',
                'powerRequirements','mandatory','operatedAt',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton','isNeedDeposit'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
                        callback: function(grid, record, action, idx, col, e, target){
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
        this.formGridPanel.store.on('dataChanged', function (store) {
            if(!!conf.dataChangeCallback) {
                conf.dataChangeCallback.call(conf.scope, store, conf);
            }
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
                    scope: this,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    selectedId : selectedId,
                    productType: '2',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            var product = {};
                            Ext.apply(product, result[0].raw)
                            Ext.applyIf(product, result[0].raw.prop)
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
                    scope: this,
                    fieldValueName: 'main_products',
                    fieldTitleName: 'main_productsName',
                    fieldVendorIdName: 'main.vendorId',
                    filterVendor: true,
                    selectedId : '',
                    productType: '2',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(result.data.items.length>0){
                            var items = selectedData;
                            for(index in items){
                                var product = {};
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    Ext.apply(product, items[index].raw)
                                    Ext.applyIf(product, items[index].raw.prop)
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
                    }}, false).show();
                break;
        }
    },

});

Ext.define('App.FlowProductCertificationAttachmentsGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.FlowProductCertificationAttachmentsGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.FlowNewProduct.mTitle,
            winId : 'FlowProductCertificationForm',
            moduleName: 'FlowProductCertification',
            frameId : 'FlowProductCertificationView',
            mainGridPanelId : 'FlowProductCertificationGridPanelID',
            mainFormPanelId : 'FlowProductCertificationFormPanelID',
            processFormPanelId: 'FlowProductCertificationProcessFormPanelID',
            searchFormPanelId: 'FlowProductCertificationSearchFormPanelID',
            mainTabPanelId: 'FlowProductCertificationMainTbsPanelID',
            productCertificationGridPanel : 'FlowProductCertificationGridPanelID',
            formGridPanelId : 'FlowProductCertificationFormAttachmentsGridPanelID',
        };
        this.initUIComponents(conf);
        App.FlowProductCertificationFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 260, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ]
        });
    },

    initUIComponents: function(conf){
        // var tools = [{
        //     type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
        //     handler: function(event, toolEl, panelHeader) {
        //         this.conf = conf;
        //         this.onRowAction.call(this);
        //     }},{
        //     type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
        //     handler: function(event, toolEl, panelHeader) {
        //         this.setHeight(260);
        //         this.formGridPanel.setHeight(258);
        //     }},{
        //     type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
        //     handler: function(event, toolEl, panelHeader) {
        //         this.setHeight(700);
        //         this.formGridPanel.setHeight(698);
        //     }}
        // ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowProductCertification.tabCertificateList,
            forceFit: false,
            width: 'auto',
            height:250,
            url: '',
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{ cls:'x-panel-header-gray' },
            fields: [
                'id','sku','vendorCnName','vendorEnName','certificateNumber','description','effectiveDate','validUntil'
            ],
            columns: [
                //{ header: _lang.ProductCertificate.fVendorName, dataIndex: 'vendorName', width: 100,},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.FlowProductCertification.fProductCertificateId, dataIndex: 'id', width: 200,},
                { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200,},
                { header: _lang.ProductCertificate.fDesc, dataIndex: 'description', width: 100,},
                { header: _lang.ProductCertificate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140,},
                { header: _lang.ProductCertificate.fValidUntil, dataIndex: 'validUntil', width: 140,},
            ]// end of columns
        });
    },

    // btnRowEdit: function(){},
    // btnRowRemove: function() {},
    // onRowAction: function(grid, record, action, idx, col, conf) {
    //     switch (action) {
    //         case 'btnRowEdit' :
    //             var selectedId = record.data.id;
    //             new ProductDialogWin({
    //                 single:true,
    //                 scope: this,
    //                 fieldValueName: 'main_products',
    //                 fieldTitleName: 'main_productsName',
    //                 fieldVendorIdName: 'main.vendorId',
    //                 filterVendor: true,
    //                 selectedId : selectedId,
    //                 productType: '2',
    //                 meForm: Ext.getCmp(conf.mainFormPanelId),
    //                 meGrid: Ext.getCmp(conf.formGridPanelId),
    //                 getRowProductID: this.getRowProductID,
    //                 callback:function(ids, titles, result) {
    //                     if(result.length>0){
    //                         this.meGrid.getStore().insert(idx, result[0].data);
    //                         this.meGrid.getStore().removeAt(idx+1);
    //                     }
    //                     this.getRowProductID(this.meGrid, this.meForm)
    //                 }}, false).show();
    //             break;
    //
    //         case 'btnRowRemove' :
    //             Ext.getCmp(conf.formGridPanelId).store.remove(record);
    //             break;
    //
    //         default :
    //             new ProductDialogWin({
    //                 single:false,
    //                 scope: this,
    //                 fieldValueName: 'main_products',
    //                 fieldTitleName: 'main_productsName',
    //                 fieldVendorIdName: 'main.vendorId',
    //                 filterVendor: true,
    //                 selectedId : '',
    //                 productType: '2',
    //                 getRowProductID: this.getRowProductID,
    //                 meForm: Ext.getCmp(this.conf.mainFormPanelId),
    //                 meGrid: Ext.getCmp(this.conf.formGridPanelId),
    //                 callback:function(ids, titles, result, selectedData) {
    //                     if(result.data.items.length>0){
    //                         var items = selectedData;
    //                         for(index in items){
    //                             var product = {};
    //                             if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
    //                                 Ext.apply(product, items[index].raw)
    //                                 Ext.applyIf(product, items[index].raw.prop)
    //                                 this.meGrid.getStore().add(product);
    //                             }
    //                         }
    //                     }
    //                     this.getRowProductID(this.meGrid, this.meForm);
    //                 }}, false).show();
    //             break;
    //     }
    // },

});
