ProductCertificateDocumentForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;

        var conf = {
            title: _lang.ProductCertificateDocument.mTitle+ ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
            moduleName: 'ProductCertificate',
            winId : 'ProductCertificateDocumentForm',
            frameId : 'ProductCertificateDocumentView',
            mainGridPanelId : 'ProductCertificateDocumentViewGridPanelID',
            mainFormPanelId : 'ProductCertificateDocumentViewFormPanelID',
            processFormPanelId: 'ProductCertificateDocumentProcessFormPanelID',
            searchFormPanelId: 'ProductCertificateDocumentViewSearchFormPanelID',
            mainTabPanelId: 'ProductCertificateDocumentViewMainTbsPanelID',
            subGridPanelId : 'ProductCertificateDocumentViewSubGridPanelID',
            formGridPanelId: 'ProductCertificateDocumentFormGridPanelID',

            urlList: __ctxPath + 'archives/productCertificate/list',
            urlSave:  _cfg.urlSave || __ctxPath + 'archives/productCertificate/save',
            urlDelete:  _cfg.urlDelete || __ctxPath + 'archives/productCertificate/delete',
            urlGet: _cfg.urlGet || __ctxPath + 'archives/productCertificate/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            actionName: this.action,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            reset: true,
            confirm: this.isConfirm,
            saveFun: this.saveFun
        };

        this.initUIComponents(conf);
        ProductCertificateDocumentForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductCertificateDocument.mTitle + $getTitleSuffix(this.action),
            tbar: Ext.create("App.toolbar", conf),
            width : 1024, height : 700,
            items :[ this.formPanel ]
        });
    },// end of the constructor

    initUIComponents: function(conf) {
        this.formPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            fieldItems : [
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                { xtype: 'container', cls: 'row',
                    items:[
                        {field: 'main.businessId', xtype: 'hidden'},
                        { field: 'main_businessId',  xtype: 'FlowOtherDialog', fieldLabel: _lang.Reports.fBusinessId, hiddenName: 'main.businessId', cls: 'col-2', allowBlank: false,
                            frameId: conf.mainFormPanelId,single: true,readOnly: this.isReadOnly, displayType: 4,
                            subcallback: function (ids, titles, redords) {
                                //申请单选择并且初始化
                                if (this.selectedId != ids) {
                                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                                    $_setByName(cmpVendor, redords.data, {preName: 'main', root: 'data'});
                                    this.meForm.getCmpByName('main.vendorId').setValue(redords.data.vendorId);
                                    this.meForm.getCmpByName('main.vendorName').setValue(redords.data.vendorName);
                                    cmpVendor.show();
                                }
                                if (!!redords.data.details) {
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').setValue(redords.data.details);
                                } else {
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').setValue('');
                                }
                            }
                        },
                    ]

                },
                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    callback: function (eOpts) {
                        //产品选择器关联设置
                        var cmp = eOpts.getCmpByName('main.products');
                        cmp.vendorId = eOpts.getCmpByName('main.vendorId').getValue();
                        cmp.formGridPanel.getStore().removeAll();
                    },
                    readOnly: this.isApprove
                })},

                //products
                { field: 'main.products', xtype: 'ProductFormMultiGrid',fieldLabel:_lang.ProductDocument.tabProductList, height: 150, productType:2,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: true, allowBlank: false
                },

                //证书信息
                { xtype: 'section', title:_lang.ProductCertificateDocument.tabCertificateInfo},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'effectiveDate', xtype: 'datetimefield', fieldLabel: _lang.ProductCertificate.fEffectiveDate, format: curUserInfo.dateFormat, cls: 'col-2', allowBlank: false,},
                    {field: 'validUntil', xtype: 'datetimefield', fieldLabel: _lang.ProductCertificate.fValidUntil, format: curUserInfo.dateFormat, cls: 'col-2', allowBlank: false,},
                ]},

                {xtype: 'container', cls: 'row', items: [
                    {field: 'certificateNumber', xtype: 'textfield', fieldLabel: _lang.ProductCertificate.fCertificateNumber, cls: 'col-2', allowBlank: false,},
                    { field: 'main.fileId', xtype: 'hidden'},
                    { field: 'main.fileName', xtype: 'FilesDialog', fieldLabel: _lang.ProductCertificate.fCertificateFile, cls:'col-2', allowBlank: false,
                        formId: conf.mainFormPanelId, hiddenName: 'main.fileId', readOnly: this.readOnly, single:true,
                    },
                ]},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'description', xtype: 'textarea', fieldLabel: _lang.ProductCertificate.fDesc, cls: 'col-2', allowBlank: false, height: 200,},
                    {field: 'relevantStandard', xtype: 'textarea', fieldLabel: _lang.ProductCertificate.fRelevantStandard, cls: 'col-2', allowBlank: false, height: 200,},
                ]},
            ].concat($creatorInfo(this.isApprove))
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    var cmp = this;

                    //init vendor
                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                    $_setByName(cmpVendor, json.data, {preName:'vendor', root:'data'});
                    cmpVendor.show();

                    //init porducts
                    cmp.getCmpByName('main.products').vendorId = cmp.getCmpByName('main.vendorId').getValue();
                    cmp.getCmpByName('main.products').setValue(json.data.details);

                    cmp.getCmpByName('main.fileName').setValue(json.data.certificateAttach.document.name);
                }
            });
        }
        this.formPanel.setReadOnly(this.isApprove || this.isConfirm, []);
    },// end of the init

    saveFun : function (action){
        var params = {act: action? action: this.actionName ? this.actionName: 'save'};
        if(action == 'confirm') params.ahid = this.ahid;

        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.products').formGridPanel});
        if(rows.length>0){
            for(index in rows){
                params['details[' + index + '].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                for(key in rows[index].data){
                    if(key == 'id')
                        params['details['+index+'].productId'] = rows[index].data.id;
                    else
                        params['details['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
            	 Ext.getCmp(this.mainGridPanelId + '-ArchivesHistoryTabGrid').refresh();
                Ext.getCmp(this.winId).close();
            }
        });
    }
});