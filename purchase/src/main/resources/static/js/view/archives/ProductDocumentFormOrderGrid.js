Ext.define('App.ProductDocumentFormOrderGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductDocumentFormOrderGrid',

    constructor : function(conf){
        //console.log('ProductDocumentViewGrid');
        Ext.applyIf(this, conf);
        var conf = {
            winId: 'ProductDocumentFormOrderGrid',
            title: _lang.AccountDepartment.mTitle,
            moduleName: 'Product',
            frameId : 'ProductDocumentView',
            mainGridPanelId: 'ProductDocumentFormOrderGridGridPanelID',
            mainFormPanelId: 'ProductDocumentFormOrderGridGridFormPanelID',
            searchFormPanelId: 'ProductDocumentFormOrderGridSearchPanelID',
            mainTabPanelId: 'ProductDocumentFormOrderGridTbsPanelId',
            OrderGridPanelId : 'ProductDocumentFormOrderGridPanelID',
            ReportGridPanelId : 'ProductDocumentFormReportGridPanelID',
            CertificateGridPanelId : 'ProductDocumentFormCertificateGridPanelID',
            urlList: __ctxPath + 'archives/product/list',
            urlSave: __ctxPath + 'archives/product/save',
            urlDelete: __ctxPath + 'archives/product/delete',
            urlGet: __ctxPath + 'archives/product/get',

        }
        try{
            this.initUIComponents({
                id: conf.winId,
                width: 'auto',
                height: 304,
                items: [
                    new HP.GridPanel({
                        id: conf.OrderGridPanelId,
                        //collapsible: true,
                        //split: true,
                        scope: this,
                        forceFit: false,
                        autoLoad: false,
                        width: 'auto',
                        minHeight: 200,
                        height: 300,
                        url:  '',
                        fields: [
                            'id','businessId','jobNumber', 'sellerName','sellerAddress','vendorCnName','vendorEnName',
                            'sellerPhone', 'sellerFax', 'sellerContactName','sellerEmail','buyerName','buyerAddress', 'buyerPhone','fBuyerContactName','buyerFax',
                            'buyerEmail', 'currency', 'totalPriceAud', 'totalPriceRmb','totalPriceUsd', 'totalOrderQty','shippingDate',
                            'originPortId','originPortCnName','originPortEnName', 'destinationPortId',
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
                            { header: _lang.ProductDocument.fShippingDate, dataIndex: 'shippingDate', width: 140 },
                            { header: _lang.FlowPurchaseContract.fOriginPort, dataIndex: 'originPortId', width: 100,
                                renderer: function(value){
                                    var $loadingPort = _dict.getValueRow('origin', value);
                                    if(curUserInfo.lang == 'zh_CN'){
                                        return $loadingPort.cnName;
                                    }else{
                                        return $loadingPort.enName;
                                    }
                                }
                            },
                            { header: _lang.FlowPurchaseContract.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                                renderer: function(value){
                                    var $destPort = _dict.getValueRow('destination', value);
                                    if(curUserInfo.lang == 'zh_CN'){
                                        return $destPort.cnName;
                                    }else{
                                        return $destPort.enName;
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
                            //{ header: _lang.ProductDocument.fPaymentTerms, dataIndex: 'paymentTerms', width: 60,  },
                            //{ header: _lang.ProductDocument.fOtherTerms, dataIndex: 'otherTerms', width: 60,  },
                            { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140,  },
                            // { header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
                            //     renderer: function(value){
                            //         var $flowStatus = _dict.flowStatus;
                            //         if($flowStatus.length>0 && $flowStatus[0].data.options.length>0){
                            //             return $dictRenderOutputColor(value, $flowStatus[0].data.options);
                            //         }
                            //     }
                            // },
                        ],//end of columns
                        appendColumns: $groupGridCreatedColumnsForFlow(
                            {
                                //flowStatus:false,
                                assignee:false,
                                creator:false,
                                department:false,
                                createdAt:false,
                                updatedAt:false,
                                flowHold:false,
                                startTime:false,
                                endTime:false,
                                sort:false,
                                status:false
                            }
                        ),
                    })
                ]
            });
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
            App.ProductDocumentFormOrderGrid.superclass.constructor.call(this, conf);
            //Ext.getCmp('ProductDocumentFormOrderGridGridPanelID').getStore().load();
        }
});
