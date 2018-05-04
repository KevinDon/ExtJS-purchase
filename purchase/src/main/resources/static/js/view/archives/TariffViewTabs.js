Ext.define('App.TariffViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.TariffViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        //console.log(conf);
        conf.items = [];
        conf.items[0] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-0',
            title : _lang.ProductDocument.tabListTitle,
            scope : this,
            forceFit : false,
            rsort: false,
            autoLoad : false,
            bbar: false,
            fields : [
                'id','sku','combined', 'name', 'barcode','categoryName','packageName','color','model','style','length',
                'width', 'height', 'cbm', 'cubicWeight', 'netWeight', 'seasonal', 'indoorOutdoor','electricalProduct',
                'powerRequirements','mandatory','operatedAt','status', 'sort',
                'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId', 'departmentCnName','departmentEnName','updatedAt'
            ],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80 ,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 200 },
                { header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 40 },
                { header: _lang.ProductDocument.fModel, dataIndex: 'model', width: 40 },
                { header: _lang.ProductDocument.fStyle, dataIndex: 'style', width: 40},
                { header: _lang.ProductDocument.fProductInformation, columns:[
                    { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true},
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true },
                ]},
                { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 80},
                { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                }
            ]
        });



        try {
            conf.items[1] = new App.ArchivesHistoryTabGrid({
                historyListUrl: 'archives/tariff/history',
                historyGetUrl: 'archives/tariff/historyget',
                historyConfirmUrl: 'archives/tariff/historyconfirm',
                historyDelUrl: 'archives/tariff/historydelete',
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.ArchivesHistory.mTitle,
                winForm: TariffForm,
                noTitle: true
            });
        }catch (e){
            console.log(e);
        }
        
        try{
            App.TariffViewTabs.superclass.constructor.call(this, {
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
