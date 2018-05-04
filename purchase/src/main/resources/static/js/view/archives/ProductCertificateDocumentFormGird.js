Ext.define('App.ProductCertificateDocumentFormGird', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCertificateDocumentFormGird',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductCombination.mTitle,
            moduleName: 'ProductCertificate',
            winId : 'ProductCertificateDocumentViewForm',
            frameId : 'ProductCertificateDocumentView',
            mainGridPanelId : 'ProductCertificateDocumentViewGridPanelID',
            mainFormPanelId : 'ProductCertificateDocumentViewFormPanelID',
            processFormPanelId: 'ProductCertificateDocumentProcessFormPanelID',
            searchFormPanelId: 'ProductCertificateDocumentViewSearchFormPanelID',
            mainTabPanelId: 'ProductCertificateDocumentViewMainTbsPanelID',
            subGridPanelId : 'ProductCertificateDocumentViewSubGridPanelID',
            formGridPanelId : 'ProductCertificateDocumentFormGridPanelID'
        };

        this.initUIComponents(conf);

        App.ProductCertificateDocumentFormGird.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 320, width: 'auto',
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

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height: 317,
            url: __ctxPath + 'archives/new-product/list',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            header:{ cls:'x-panel-header-gray' },
            fields: [ 'id','sku','name', 'creatorId', 'qty' , 'categoryId','categoryName',
                'departmentId', 'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:65,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil',
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash',
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200 },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryId', width: 140, hidden: true},
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 140},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({assignee:false, sort: false}),
        });

    },

//     btnRowAdd: function(grid, record, action, row, col) {
//         console.log(this);
//         var selectedId = '';
// //    	if(Ext.getCmp(this.conf.mainFormPanelId).getCmpByName('main_products')){
// //    		selectedId = Ext.getCmp(this.conf.mainFormPanelId).getCmpByName('main_products').getValue();
// //    	}
// //
//         new ProductDialogWin({
//             single:false,
//             fieldValueName: 'main_products',
//             fieldTitleName: 'main_productsName',
//             selectedId : selectedId,
//             meForm: Ext.getCmp(this.conf.mainFormPanelId),
//             meGrid: Ext.getCmp(this.conf.formGridPanelId),
//             callback:function(ids, titles, result) {
//                 if(result.data.items.length>0){
//                     var items = result.data.items;
//                     for(index in items){
//                         this.meGrid.getStore().add(items[index].raw);
//                     }
//                 }
//             }}, false).show();
//
//     },
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
                    productType: '2',
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
                    productType: '2',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                this.meGrid.getStore().add(items[index].raw);
                            }
                        }
                    }}, false).show();

                break;
        }
    },
});
