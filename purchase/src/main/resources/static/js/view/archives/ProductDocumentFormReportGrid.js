Ext.define('App.ProductDocumentFormReportGridId', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductDocumentFormReportGridId',

    constructor : function(conf){
        //console.log('ProductDocumentViewGrid');
        Ext.applyIf(this, conf);
        var conf = {
            winId: 'ProductDocumentFormReportGridId',
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
                id: 'ProductDocumentFormReportGridIdCenterParentId',
                width: 'auto',
                minHeight: 200,
                height: 304,
                items: [
                    new HP.GridPanel({
                        id:conf.ReportGridPanelId,
                        //title: _lang.ProductDocument.fProductCert,
                        //collapsible: true,
                        //split: true,
                        scope: this,
                        forceFit: false,
                        autoLoad: false,
                        width: 'auto',
                        minHeight: 200,
                        height: 300,
                        url:  '',
                        fields: [ 'id','account','sku','departmentName', 'departmentId','cnName'],
                        columns: [
                            { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                            { header: _lang.ProductDocument.fSku, dataIndex: 'account', width: 200 },
                            { header: _lang.ProductDocument.test32, dataIndex: 'sku', width: 200 },
                            { header: _lang.ProductDocument.test33, dataIndex: 'departmentName', width: 140 },
                            { header: _lang.ProductDocument.test34, dataIndex: 'departmentId', width: 140 },
                            { header: _lang.ProductDocument.test35, dataIndex: 'cnName', width: 200},
                        ],//end of columns
                     })
                ]
            });
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
            App.ProductDocumentFormReportGridId.superclass.constructor.call(this, conf);
            //Ext.getCmp('ProductDocumentFormReportGridId').getStore().load();
        }
});
