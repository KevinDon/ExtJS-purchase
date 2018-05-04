Ext.define('App.ProductProblemViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ProductProblemViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        //console.log(conf);
        conf.items = [];
        conf.items[0] = new HP.GridPanel({
            region: 'center',
            id: conf.mainTabPanelId+'-0',
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:250,
            //url: __ctxPath + 'archives/new-product/list',
            bbar: false,
            // tools: tools,
            autoLoad: false,
            header:{ cls:'x-panel-header-gray' },
            rsort: false,
            fields: [
                'orderId','orderNumber','orderNumber', 'productId', 'sku' , 'combined','orderIndex','jobNo',
                'productName', 'categoryId', 'categoryName', 'sellQty','riskRating', 'currency',
                'rateAudToRmb', 'rateAudToUsd','priceAud','priceRmb','priceUsd',
                'length','width','height','netWeight','cubicWeight','cbm'
            ],
            columns: [
                { header: 'ID', dataIndex: 'orderId', width: 40, hidden: true },
               // { header: _lang.FlowDepositContract.fOrderNumber, dataIndex: 'jobNo', width: 90, },
                { header: _lang.ProductCertificate.fProductId, dataIndex: 'productId', width: 90, hidden: true, },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductProblem.fSellQty, dataIndex: 'sellQty', width: 80},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80},
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryId', width: 80, hidden: true, },
                { header: _lang.ProductDocument.fCategoryName, dataIndex: 'categoryName', width: 120},
                { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'riskRating',align: 'center', width: 100,
                    renderer: function(value){
                        var $riskRating = _dict.riskRating;
                        if(value  && $riskRating.length > 0 && $riskRating[0].data.options.length>0){
                            return !! $riskRating[0].data.options[parseInt(value) - 1] ? $riskRating[0].data.options[parseInt(value) - 1].title: '';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency',
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},


                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null,{edit:false, gridId:conf.mainTabPanelId+'-0',})
                },
                {header: _lang.ProductDocument.tabProductSize,
                    columns: [
                        {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 80,},
                        {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 80,},
                        {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 80,},
                        { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm' ,width: 80,  },
                        { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 80,  },
                        { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight' ,width: 80, },
                    ],
                },
            ],// end of columns
        });
        conf.items[1] = new HP.GridPanel({
            id: conf.mainTabPanelId+'-1',
            title : _lang.ProductProblem.fCommentList,
            //url: conf.urlList,
            bbar: false,
            autoLoad: false,
            rsort: false,
            forceFit : false,
            fields: ['id', 'remark','createdAt','createdAt'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fRemarkTitle, dataIndex: 'remark', width: 300, },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140, },
                { header: _lang.TText.fUpdatedAt, dataIndex: 'createdAt', width: 140, },
            ],// end of columns
        });

        conf.items[2] = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });



        try{
            App.ProductProblemViewTabs.superclass.constructor.call(this, {
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
