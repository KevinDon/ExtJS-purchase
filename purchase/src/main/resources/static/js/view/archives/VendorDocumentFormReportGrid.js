Ext.define('App.VendorDocumentFormReportGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.VendorDocumentFormReportGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        try{
            this.initUIComponents({
                id: 'VendorDocumentFormReportGridCenterParentId',
                width: 'auto',
                minHeight: 200,
                height: 300,
                items: [
                    new HP.GridPanel({
                        id: 'VendorDocumentFormReportGridId',
                        title: _lang.VendorDocument.fProductCategory,
                        //collapsible: true,
                        //split: true,
                        scope: this,
                        forceFit: false,
                        autoLoad: false,
                        width: 'auto',
                        minHeight: 200,
                        height: 300,
                        header:{
                            cls:'x-panel-header-gray'
                        },
                        bodyCls:'x-panel-body-gray',
                        //url:  __ctxPath + 'admin/user/list',
                        fields: [ 'id','vendorId','contactsName', 'companyPosition','phone','mobile','fax','skype','qq','email','type'],
                        columns: [
                            { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                            { header: 'VendorId', dataIndex: 'vendorId', width: 40, hidden: true },
            				{ header: _lang.VendorDocument.fContactsName, dataIndex: 'contactsName', width: 120},
            				{ header: _lang.VendorDocument.fCompanyPosition, dataIndex: 'companyPosition', width: 120 },
            			    { header: _lang.VendorDocument.fPhone, dataIndex: 'phone', width: 100 },
            			    { header: _lang.VendorDocument.fMobile, dataIndex: 'mobile', width: 100},
            				{ header: _lang.VendorDocument.fFax, dataIndex: 'fax', width: 100 },
            			    { header: _lang.VendorDocument.fSkype, dataIndex: 'skype', width: 140 },
            			    { header: _lang.VendorDocument.fQq, dataIndex: 'qq', width: 140},
            				{ header: _lang.VendorDocument.fEmail, dataIndex: 'email', width: 140 },
            			    { header: _lang.VendorDocument.fType, dataIndex: 'type', width: 60 },
                        ]//end of columns
                    })
                ]
            });
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
            App.VendorDocumentFormReportGrid.superclass.constructor.call(this, conf);
            //Ext.getCmp('VendorDocumentFormCategoryGridId').getStore().load();
        }
});
