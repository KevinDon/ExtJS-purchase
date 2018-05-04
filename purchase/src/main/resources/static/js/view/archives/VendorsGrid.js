VendorsGrid = Ext.extend(HP.Window, {
    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            winId: 'VendorsGridWinID',
            title: _lang.VendorDocument.mTitle,
            mainGridPanelId: 'VendorsGridPanelID',
            urlList: __ctxPath + 'archives/vendor/list',
            urlSave: __ctxPath + 'archives/vendor/save',
            urlDelete: __ctxPath + 'archives/vendor/delete',
            urlGet: __ctxPath + 'archives/vendor/get',
            refresh: true,
            save: true,
            cancel: true,
            reset: true
        }
        this.initUIComponents(conf);
        VendorsGrid.superclass.constructor.call(this, {
            id: conf.winId,
            scope: this,
            title: _lang.VendorDocument.mTitle,
            tbar: Ext.create("App.toolbar", conf),
            width: 900, height: 500,
            items: this.gridPanel
        });
    },//end of the constructor

    initUIComponents: function (conf) {
        this.gridPanel = new HP.GridPanel({
            id: conf.mainGridPanelId,
            title: _lang.VendorDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            url: __ctxPath + 'archives/vendor/list',
            fields: [
				'id','categoryName','name', 'buyerName','director',
				'address','abn','website','rating','files',
				'source', 'currency', 'orderSerialNumber', 'paymentProvision',
				'dynContent', 'status', 'createdAt'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { xtype: 'checkcolumn', header: 'Select?', width: 60, editor: {
                        xtype: 'checkbox',
                        cls: 'x-grid-checkheader-editor'
                    }
                },
				{ header: _lang.VendorDocument.fCategoryName, dataIndex: 'categoryName', width: 100},
				{ header: _lang.VendorDocument.fName, dataIndex: 'name', width: 90 },
			    { header: _lang.VendorDocument.fBuyerName, dataIndex: 'buyerName', width: 90},
			    { header: _lang.VendorDocument.fDirector, dataIndex: 'director', width: 80},
			    { header: _lang.VendorDocument.fAddress, dataIndex: 'address', width: 150 },
			    { header: _lang.VendorDocument.fAbn, dataIndex: 'abn', width: 150 },
			    { header: _lang.VendorDocument.fWebsite, dataIndex: 'website', width: 80 },
			    { header: _lang.VendorDocument.fRating, dataIndex: 'rating', width: 70 },
			    { header: _lang.VendorDocument.fFiles, dataIndex: 'files', width: 60 },
			    { header: _lang.VendorDocument.fSource, dataIndex: 'source', width: 100 },
			    { header: _lang.VendorDocument.fCurrency, dataIndex: 'currency', width: 100 },
			    { header: _lang.VendorDocument.fOrderSerialNumber, dataIndex: 'orderSerialNumber', width: 100 },
			    { header: _lang.VendorDocument.fPaymentProvision, dataIndex: 'paymentProvision', width: 80 },
				{ header: _lang.VendorDocument.fDynContent, dataIndex: 'dynContent', width: 80 },
				{ header: _lang.VendorDocument.fCreatedAt, dataIndex: 'createdAt', width: 60 }
				
			],// end of columns

            itemclick: function (obj, record, item, index, e, eOpts) {
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            },
            itemcontextmenu: function (view, record, node, index, e) {

            }
        });
    }
})