Ext.define('App.ProductDocumentViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ProductDocumentViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        //console.log(conf);
        conf.items = [];
        conf.items[0] = new HP.GridPanel({
            id: conf.OrderGridPanelId,
            title: _lang.ProductDocument.tabOrderListTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            bbar: false,
            limit: 20,
            width: '55%',
            minWidth: 400,
            fields: [
                'id','businessId','jobNumber', 'sellerName','sellerAddress','vendorCnName','vendorEnName',
                'sellerPhone', 'sellerFax', 'sellerContactName','sellerEmail','buyerName','buyerAddress', 'buyerPhone','fBuyerContactName','buyerFax',
                'buyerEmail', 'currency', 'totalPriceAud', 'totalPriceRmb','totalPriceUsd', 'totalOrderQty','shippingDate',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId','etd',
                'depositAud','depositRmb','depositUsd','shippingMethod','paymentTerms','otherTerms','orderDate','startTime','flowStatus',
                'orderTitle', 'orderNumber', 'vendorName', 'createdAt'
            ],

            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fBusinessId, dataIndex: 'id', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderTitle, dataIndex: 'orderTitle', width: 180 },
                { header: _lang.ProductDocument.fVendorName, dataIndex: 'vendorCnName', width: 200 ,hidden:curUserInfo.lang == 'zh_CN' ? false: true, },
                { header: _lang.ProductDocument.fVendorName, dataIndex: 'vendorEnName', width: 200, hidden:curUserInfo.lang == 'zh_CN' ? true: false,  },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 80,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fTotalOrderQty, dataIndex: 'totalOrderQty', width: 60 },
                { header: _lang.ProductDocument.fShippingDate, dataIndex: 'etd', width: 140 },
                { header: _lang.FlowPurchaseContract.fOriginPort, dataIndex: 'originPortId', width: 100,
                    renderer: function(value){
                        if(value){
                            var $loadingPort = _dict.getValueRow('origin', value);
                            if(curUserInfo.lang == 'zh_CN'){
                                return $loadingPort.cnName;
                            }else{
                                return $loadingPort.enName;
                            }
                        }
                    }
                },
                { header: _lang.FlowPurchaseContract.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        if(value){
                            var $destPort = _dict.getValueRow('destination', value);
                            if(curUserInfo.lang == 'zh_CN'){
                                return $destPort.cnName;
                            }else{
                                return $destPort.enName;
                            }
                        }
                    }
                },
                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fShippingMethod, dataIndex: 'shippingMethod', width: 60,
                    renderer: function(value){
                        var $item = _dict.shippingMethod;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140,  },

            ],//end of columns
        });

        conf.items[1] = new App.ReportProductTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.Reports.tabRelatedReports,
            noTitle: true
        });

        conf.items[2] = new HP.GridPanel({
            id: conf.HistoricalQuoteGridPanelId,
            title: _lang.ProductDocument.tabHistoricalQuoteListTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            fields: [
                'id', 'vendorCnName', 'vendorEnName', 'currency', 'rateAudToRmb','rateAudToUsd',
                'priceAud','priceRmb','priceUsd', 'moq', 'originPortId', 'destinationPortId',
                'effectiveDate', 'validUntil', 'createdAt'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 150},
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 150},

                { header: _lang.ProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fMoq, dataIndex: 'moq', width: 60},
                { header: _lang.FlowProductQuotation.fOriginPort, dataIndex: 'originPortId', width: 60,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.origin, [])}
                },
                { header: _lang.FlowProductQuotation.fDestinationPort, dataIndex: 'destinationPortId', width: 60,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.destination, [])}
                },
                { header: _lang.FlowProductQuotation.fEffectiveDate, dataIndex: 'effectiveDate', width: 140,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.FlowProductQuotation.fValidUntil, dataIndex: 'validUntil', width: 140,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140,
                    renderer: function(value){
                        if(typeof(value) == "string") {
                            return value;
                        }else {
                            return Ext.Date.format(value, curUserInfo.dateFormat);
                        }
                    }
                },
            ],//end of columns
        });

        conf.items[3] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-3',
            title : _lang.TText.fImageList,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: [
                'id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName',
                'departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width:35, readOnly:true, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.MyDocument.fName, dataIndex: 'name', width:260 },
                { header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
                { header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
                { header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
            // end of columns
        });

        try {
            conf.items[4] = new App.ArchivesHistoryTabGrid({
                historyListUrl: 'archives/product/history',
                historyGetUrl: 'archives/product/historyget',
                historyConfirmUrl: 'archives/product/historyconfirm',
                historyDelUrl: 'archives/product/historydelete',
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.ArchivesHistory.mTitle,
                winForm: ProductDocumentForm,
                noTitle: true
            });
        }catch (e){
            console.log(e);
        }
        
        
        try{
            App.ProductDocumentViewTabs.superclass.constructor.call(this, {
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
    },

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
        }
    }
});
