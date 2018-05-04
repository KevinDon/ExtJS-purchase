Ext.define('App.ProductCombinationFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductCombinationFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductCombination.mTitle,
            moduleName: 'ProductCombination',
            winId : 'ProductCombinationFormGrid',
            frameId : 'ProductCombinationView',
            mainGridPanelId : 'ProductCombinationFormGridGridPanelID',
            mainFormPanelId : 'ProductCombinationFormGridFormPanelID',
            processFormPanelId: 'ProductCombinationFormProcessFormPanelID',
            searchFormPanelId: 'ProductCombinationFormGridSearchFormPanelID',
            mainTabPanelId: 'ProductCombinationFormGridMainTbsPanelID',
            subGridPanelId : 'ProductCombinationFormGridSubGridPanelID',
            formGridPanelId : 'ProductCombinationFormGridPanelID'
        };

        this.initUIComponents(conf);

        App.ProductCombinationFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
            minHeight: 200,
            height: 400, width: 'auto',
            items: [ this.formGridPanel ],
            bodyCls:'x-panel-body-gray'
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
            height: 397,
            url: __ctxPath + 'archives/new-product/list',
            bbar: false,
            tools: tools,
            autoLoad: false,
            // autoScroll: true,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id','sku', 'barcode', 'name', 'creatorId', 'qty' , 'departmentId','productId',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt','ean',
                'priceAud', 'priceRmb', 'priceUsd'
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
                { header: _lang.ProductCombination.fProductId, dataIndex: 'productId', width: 40, hidden: true },

                { header: _lang.ProductCombination.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductCombination.fName, dataIndex: 'name', width: 200},
                { header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 100},
                { header: _lang.ProductCombination.fBarcode, dataIndex: 'barcode', width: 100},
                { header: _lang.ProductCombination.fQty, dataIndex: 'qty', width: 60, editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:0}},
                { header: _lang.ProductCombination.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                        {edit:false, gridId: conf.formGridPanelId})
                },
            ],// end of columns
        });
    },

    btnRowAdd: function(grid, record, action, row, col) {
        console.log(this);
        var selectedId = '';
//    	if(Ext.getCmp(this.conf.mainFormPanelId).getCmpByName('main_products')){
//    		selectedId = Ext.getCmp(this.conf.mainFormPanelId).getCmpByName('main_products').getValue();
//    	}
//
        new ProductDialogWin({
            single:false,
            fieldValueName: 'main_products',
            fieldTitleName: 'main_productsName',
            selectedId : selectedId,
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
                    productType: 2,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.formGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0){
                            result[0].data.productId =  result[0].data.id
                            result[0].data.id =  '';
                            result[0].data.ean =   result[0].raw.ean;

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
                    productType: 2,
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.formGridPanelId),
                    callback:function(ids, titles, result,selectedData) {
                        if(result.data.items.length>0){
                            var items = result.data.items;
                            for(index in items){
                                items[index].raw.productId = items[index].raw.id
                                items[index].raw.id = '';
                                items[index].raw.ean = selectedData[index].raw.ean;

                                this.meGrid.getStore().add(items[index].raw);
                            }
                        }
                    }}, false).show();

                break;
        }
    },
});
