Ext.define('App.ProductDocumentFormCertificateGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductDocumentFormCertificateGrid',

    constructor : function(conf){
        //console.log('ProductDocumentViewGrid');
        Ext.applyIf(this, conf);

        var conf = {
            winId : 'ProductDocumentFormCertificateGrid',
            title : _lang.AccountDepartment.mTitle,
            moduleName:'Product',
            frameId : 'ProductDocumentView',
            mainGridPanelId : 'ProductDocumentFormCertificateGridPanelID',
            mainFormPanelId : 'ProductDocumentFormCertificateGridFormPanelID',
            searchFormPanelId: 'ProductDocumentFormCertificateGridSubFormPanelID',
            mainTabPanelId: 'ProductDocumentFormCertificateGridTbsPanelId',
            formGridPanelId:'ProductDocumentFormCertificateFormGridPanelID',
            OrderGridPanelId : 'ProductDocumentFormOrderGridPanelID',
            ReportGridPanelId : 'ProductDocumentFormReportGridPanelID',
            CertificateGridPanelId : 'ProductDocumentFormCertificateGridPanelID',
            urlList: __ctxPath + 'archives/product/list',
            urlSave: __ctxPath + 'archives/product/save',
            urlDelete: __ctxPath + 'archives/product/delete',
            urlGet: __ctxPath + 'archives/product/get',
        };
        this.initUIComponents({
            id: conf.winId,
            width: 'auto',
            minHeight: 200,
            height: 304,
            items: [
                new HP.GridPanel({
                    id: conf.CertificateGridPanelId,                     
                    scope: this,
                    forceFit: false,
                    autoLoad: false,
                    width: 'auto',
                    minHeight: 200,
                    height: 300,
                    bbar: false,                        
                    url:  '',
                    fields: [ 'id','vendorName','relevantStandard', 'description','certificateNumber','effectiveDate', 'validUntil'],
                    columns: [                            
                        { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                        { header: _lang.ProductCertificate.fVendorName, dataIndex: 'vendorName', width: 200 },
                        { header: _lang.ProductCertificate.fRelevantStandard, dataIndex: 'relevantStandard', width: 140 },
                        { header: _lang.ProductCertificate.fDesc, dataIndex: 'description', width: 140 },
                        { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200 },
                        { header: _lang.ProductCertificate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140},
                        { header: _lang.ProductCertificate.fValidUntil, dataIndex: 'validUntil', width: 140},
                    ],//end of columns
                 })
            ]
        });
    },

    initUIComponents: function(conf) {
            App.ProductDocumentFormCertificateGrid.superclass.constructor.call(this, conf);
            //console.log(Ext.getCmp(this.conf));
            //Ext.getCmp('ProductDocumentFormCertificateFormGridPanelID').getStore().load();
    }  
});
