Ext.define('App.ProductCombinationViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ProductCombinationViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        //console.log(conf);
        conf.items = [];
        conf.items[0] = new HP.GridPanel({
                id: conf.mainTabPanelId + '-0',
                title: _lang.NewProductDocument.tabListTitle,
                scope : this,
                forceFit : false,
                rsort: false,
                autoLoad : false,
                bbar: false,
                header:{
                    cls:'x-panel-header-gray'
                },
                fields: [ 'id','sku', 'barcode', 'name', 'creatorId', 'qty' , 'departmentId','ean',
                    'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                    'departmentCnName','departmentEnName','updatedAt',
                    'priceAud', 'priceRmb', 'priceUsd'
                ],
                columns: [
                    { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                    { header: _lang.ProductCombination.fSku, dataIndex: 'sku', width: 200},
                    { header: _lang.ProductCombination.fName, dataIndex: 'name', width: 200},
                    { header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 200},
                    { header: _lang.ProductCombination.fBarcode, dataIndex: 'barcode', width: 200},
                    { header: _lang.ProductCombination.fQty, dataIndex: 'qty', width: 60,},
                    { header: _lang.ProductCombination.fPrice,
                        columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,
                            {edit:false, gridId: conf.formGridPanelId})
                    },
                ],// end of columns
            });

        try {
            conf.items[1] = new App.ArchivesHistoryTabGrid({
                historyListUrl: 'archives/productCombined/history',
                historyGetUrl: 'archives/productCombined/historyget',
                historyConfirmUrl: 'archives/productCombined/historyconfirm',
                historyDelUrl: 'archives/productCombined/historydelete',
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.ArchivesHistory.mTitle,
                winForm: ProductCombinationForm,
                noTitle: true
            });
        }catch (e){
            console.log(e);
        }
        
        try{
            App.ProductCombinationViewTabs.superclass.constructor.call(this, {
                id: this.mainTabPanelId,
                title: conf.fieldLabel,
                autoWidth: true,
                autoScroll : true,
                minHeight: 300,
                cls:'tabs'
            });
            this.initUIComponents(conf);
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
        var cmpPanel = Ext.getCmp(this.mainTabPanelId);
        for(var i = 0; i < conf.items.length; i++){
            cmpPanel.add(conf.items[i]);
        }
        cmpPanel.setActiveTab(conf.items[0]);
    }
});
