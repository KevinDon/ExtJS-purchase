ServiceProvidersGrid = Ext.extend(HP.Window, {
    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            winId: 'ServiceProvidersGridWinID',
            title: _lang.ServiceProviderDocument.mTitle,
            mainGridPanelId: 'ServiceProvidersGridPanelID',
            urlList: __ctxPath + 'archives/service_provider/list',
            urlSave: __ctxPath + 'archives/service_provider/save',
            urlDelete: __ctxPath + 'archives/service_provider/delete',
            urlGet: __ctxPath + 'archives/service_provider/get',
            refresh: true,
            save: true,
            cancel: true,
            reset: true
        }
        this.initUIComponents(conf);
        ServiceProvidersGrid.superclass.constructor.call(this, {
            id: conf.winId,
            scope: this,
            title: _lang.ServiceProviderDocument.mTitle,
            tbar: Ext.create("App.toolbar", conf),
            width: 900, height: 500,
            items: this.gridPanel
        });
    },//end of the constructor

    initUIComponents: function (conf) {
        this.gridPanel = new HP.GridPanel({
            id: conf.mainGridPanelId,
            title: _lang.ServiceProviderDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            url: __ctxPath + 'archives/service_provider/list',
			fields: [
				'id','categoryName','name', 'buyerName','director',
				'address','abn','website','rating','files',
				'source', 'currency', 'paymentProvision',
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
				{ header: _lang.ServiceProviderDocument.fCategoryName, dataIndex: 'categoryName', width: 120},
				{ header: _lang.ServiceProviderDocument.fName, dataIndex: 'name', width: 200 },
			    { header: _lang.ServiceProviderDocument.fBuyerName, dataIndex: 'buyerName', width: 120},
			    { header: _lang.ServiceProviderDocument.fDirector, dataIndex: 'director', width: 60},
			    { header: _lang.ServiceProviderDocument.fAddress, dataIndex: 'address', width: 390},
			    { header: _lang.ServiceProviderDocument.fAbn, dataIndex: 'abn', width: 200 },
			    { header: _lang.ServiceProviderDocument.fWebsite, dataIndex: 'website', width: 200 },
			    { header: _lang.ServiceProviderDocument.fRating, dataIndex: 'rating', width: 70 },
			    { header: _lang.ServiceProviderDocument.fFiles, dataIndex: 'files', width: 60 },
			    { header: _lang.ServiceProviderDocument.fSource, dataIndex: 'source', width: 80 },
			    { header: _lang.ServiceProviderDocument.fCurrency, dataIndex: 'currency', width: 60 },
			    { header: _lang.ServiceProviderDocument.fPaymentProvision, dataIndex: 'paymentProvision', width: 80 },
				{ header: _lang.ServiceProviderDocument.fDynContent, dataIndex: 'dynContent', width: 80 },
				{ header: _lang.ServiceProviderDocument.fCreatedAt, dataIndex: 'createdAt', width: 140 }
				
			],// end of columns

            itemclick: function (obj, record, item, index, e, eOpts) {
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            },
            itemcontextmenu: function (view, record, node, index, e) {

            }
        });
    }
})