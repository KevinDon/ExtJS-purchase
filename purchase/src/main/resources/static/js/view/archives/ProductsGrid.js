ProductsGrid = Ext.extend(HP.Window, {
    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            winId: 'ProductsGridWinID',
            title: _lang.ProductDocument.mTitle,
            mainGridPanelId: 'ProductsGridPanelID',
            urlList: __ctxPath + 'archives/product/list',
            urlSave: __ctxPath + 'archives/product/save',
            urlDelete: __ctxPath + 'archives/product/delete',
            urlGet: __ctxPath + 'archives/product/get',
            refresh: true,
            save: true,
            cancel: true,
            reset: true
        }
        this.initUIComponents(conf);
        ProductsGrid.superclass.constructor.call(this, {
            id: conf.winId,
            scope: this,
            title: _lang.ProductDocument.mTitle,
            tbar: Ext.create("App.toolbar", conf),
            width: 900, height: 500,
            items: this.gridPanel
        });
    },//end of the constructor

    initUIComponents: function (conf) {
        this.gridPanel = new HP.GridPanel({
            id: conf.mainGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            url: __ctxPath + 'archives/product/list',
            fields: [
                'id', 'sku', 'combined', 'name',
                'barcode', 'categoryName', 'subCategoryId',
                'packageName', 'color', 'model', 'style', 'length',
                'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
                'seasonal', 'indoorOutdoor', 'electricalProduct',
                'powerRequirements', 'mandatory', 'status',
                'creatorId', 'createdAt', 'operatedAt', 'departmentId'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                { xtype: 'checkcolumn', header: 'Select?', width: 60,
                    editor: { xtype: 'checkbox',
                        cls: 'x-grid-checkheader-editor'
                    }
                },
                {header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                {header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 40},
                {header: _lang.ProductDocument.fName, dataIndex: 'name', width: 100, hidden: true},
                {header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 200},
                {header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 200},
                {header: _lang.ProductDocument.fSubCategoryId, dataIndex: 'subCategoryId', width: 120,},
                {header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 200},
                {header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 50},
                {header: _lang.ProductDocument.fModel, dataIndex: 'model', width: 40},
                {header: _lang.ProductDocument.fStyle, dataIndex: 'style', width: 40},
                {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60},
                {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60},
                {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60},
                {header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60},
                {header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60},
                {header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60},
                {header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 60},
                {header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 40},
                {header: _lang.ProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 40,},
                {header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 40},
                {header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 40,},
                {header: _lang.ProductDocument.fStatus, dataIndex: 'status', width: 40},
                {header: _lang.ProductDocument.fCreatorId, dataIndex: 'creatorId', width: 60},
                {header: _lang.ProductDocument.fCreatedAt, dataIndex: 'createdAt', width: 140},
                {header: _lang.ProductDocument.fOperatedAt, dataIndex: 'operatedAt', width: 140},
                {header: _lang.ProductDocument.fDepartmentId, dataIndex: 'departmentId', width: 60},
            ],// end of columns

            itemclick: function (obj, record, item, index, e, eOpts) {
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            },
            itemcontextmenu: function (view, record, node, index, e) {

            }
        });
    }
})