ProductDocumentForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;
        this.isReadOnly = this.isApprove || this.isConfirm;
        var conf = {
            winId: 'ProductDocumentFormWinID',
            title: _lang.ProductDocument.mTitle,
            moduleName:'Product',
            actionName: this.action,
            mainGridPanelId : 'ProductDocumentGridPanelID',
            mainFormPanelId : 'ProductDocumentFormPanelID',
            searchFormPanelId: 'ProductDocumentSubFormPanelID',
            mainTabPanelId: 'ProductDocumentTbsPanelID',
            OrderGridPanelId : 'ProductDocumentFormOrderGridPanelID',
            ReportGridPanelId : 'ProductDocumentFormReportGridPanelID',
            CertificateGridPanelId : 'ProductDocumentFormCertificateGridPanelID',

            urlList: __ctxPath + 'archives/product/list',
            urlSave:_cfg.urlSave || __ctxPath + 'archives/product/save',
            urlDelete:_cfg.urlDelete || __ctxPath + 'archives/product/delete',
            urlGet:_cfg.urlGet || __ctxPath + 'archives/product/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            reset: true,
            confirm: this.isConfirm,
            saveFun: this.saveRow
        };

        this.initUIComponents(conf);
        ProductDocumentForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductDocument.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
            tbar: Ext.create("App.toolbar", conf),
            width : 1096, height : 800,
            items : this.formPanel
        });
    },// end of the constructor

	initUIComponents : function(conf) {
	    var scope = this;
	    var updatePriceFlag = false;
        this.formPanel = new HP.FormPanel({
            id: conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems: [

                //基础资料
                {xtype: 'section', title: _lang.ProductDocument.tabInitialValue},
                {field: 'id', xtype: 'hidden', value: this.action == 'add' ? '': this.record.id},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.sku', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fSku, formId: conf.mainFormPanelId, cls: 'col-2', allowBlank: false,
                        listeners: {
                            change: function(field, newValue, oldValue){
                                this.setValue(newValue.toUpperCase());
                            }
                        }
                    },
                    {field: 'main.combined', xtype:'dictfield', code:'options', codeSub:'yesno', fieldLabel: _lang.ProductDocument.fCombined, cls:'col-2'},
                    {field: 'main.name', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fName, cls: 'col-2'},

                    { xtype: 'container', cls:'row',  items:[
                        {field: 'main.barcode', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fBarcode, cls: 'col-2', scope : this, allowBlank: false

                        },
                        { xtype:'button', id:'main_button_barcode', text: _lang.TText.fGenerate, width:80, formId: conf.mainFormPanelId, scope:this, hidden: this.isReadOnly,
                            handler: function(e) { this.scope.getAutoBarCode.call(e);}
                        },
                    ]},
                    { xtype: 'container', cls:'row',  items:[
                        {field: 'main.ean', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fEan, cls: 'col-2', scope: this, allowBlank: false,
                            validator : function(value){
                                var id = this.scope.scope.action == 'copy' ? '' : this.scope.scope.formPanel.getCmpByName('id').getValue();
                                var isExist = true;
                                if(value != ''){
                                    Ext.Ajax.request({
                                        url: __ctxPath + 'archives/product/existsEan?ean=' + value + '&id=' +id ,
                                        scope: this, method: 'post',
                                        async:false,
                                        success: function (response, options) {
                                            //return true;
                                            var obj = Ext.decode(response.responseText);
                                            if(obj.success == true){
                                                if(obj.data == true){

                                                }else{
                                                    isExist = false;
                                                    Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg, value);
                                                }
                                            }
                                        }
                                    });
                                }
                                return isExist;
                            }//end_validator
                        },
                        { xtype:'button', id:'main_button_ean', text: _lang.TText.fGenerate, width:80, formId: conf.mainFormPanelId, scope:this, hidden: this.isReadOnly,
                            handler: function(e) { this.scope.getAutoEanCode.call(e);}
                        },
                    ]},
                    { xtype: 'container', cls:'row',  items:[
                        {field: 'main.purchaseType', xtype:'dictcombo', code:'product', codeSub:'purchase_type', fieldLabel: _lang.ProductDocument.fPurchaseType, cls: 'col-2', value: '2', },
                        {field: 'main.categoryId', xtype: 'hidden'},
                        {field: 'main.categoryName', xtype: 'ProductCategoryDialog', title: _lang.ProductDocument.fCategoryId, fieldLabel: _lang.ProductDocument.fCategoryId,
                            formId: conf.mainFormPanelId, hiddenName: 'main.categoryId', single: true, cls: 'col-2', readOnly: this.isReadOnly
                        },
                        {field: 'main.color', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fColor, cls: 'col-2',  maxLength:50,},
                        {field: 'main.model', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fModel, cls: 'col-2'},
                        {field: 'main.style', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fStyle, cls: 'col-2',},
                        {field: 'hsCodeDescription', xtype: 'hidden'},
                        {field: 'main.prop.hsCode', xtype: 'TariffDialog', fieldLabel: _lang.ProductDocument.fHsCode, cls: 'col-2', readOnly: this.isReadOnly,
                            formId: conf.mainFormPanelId, hiddenName: 'hsCodeDescription', readOnly: this.isReadOnly, single:true
                        },
                    ]},

                ]},

                {xtype: 'section', title: _lang.NewProductDocument.tabPrice},
                {field: 'main.prop.rateAudToRmb', xtype:'displayfield', fieldLabel: _lang.ExchangeRate.fRateAudToRmb, cls: 'col-2', value: curUserInfo.audToRmb },
                {field: 'main.prop.rateAudToUsd', xtype:'displayfield', fieldLabel: _lang.ExchangeRate.fRateAudToUsd, cls: 'col-2', value: curUserInfo.audToUsd},
                {xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        allowBlank: true, cls: 'col-1',
                        aud: { field: 'main.prop.targetBinAud', fieldLabel: _lang.ProductDocument.fTargetBinAud, },
                        rmb: { field: 'main.prop.targetBinRmb', fieldLabel: _lang.ProductDocument.fTargetBinRmb, },
                        usd: { field: 'main.prop.targetBinUsd', fieldLabel: _lang.ProductDocument.fTargetBinUsd,}
                    })},
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        allowBlank: true, cls: 'col-1',
                        aud: { field: 'main.prop.competitorPriceAud', fieldLabel: _lang.NewProductDocument.fCompetitorPriceAud, },
                        rmb: { field: 'main.prop.competitorPriceRmb', fieldLabel: _lang.NewProductDocument.fCompetitorPriceRmb, },
                        usd: { field: 'main.prop.competitorPriceUsd', fieldLabel: _lang.NewProductDocument.fCompetitorPriceUsd, }
                    })},
                    { xtype: 'container', cls:'col-2', items: $groupPriceFields(scope, {
                        allowBlank: true, cls: 'col-1',
                        aud: { field: 'main.prop.ebayMonthlySalesAud', fieldLabel: _lang.NewProductDocument.fEbayMonthlySalesAud, },
                        rmb: { field: 'main.prop.ebayMonthlySalesRmb', fieldLabel: _lang.NewProductDocument.fEbayMonthlySalesRmb, },
                        usd: { field: 'main.prop.ebayMonthlySalesUsd', fieldLabel: _lang.NewProductDocument.fEbayMonthlySalesUsd,}
                    })},
                    {field: 'main.prop.competitorSaleRecord', xtype:'textfield', fieldLabel: _lang.NewProductDocument.fCompetitorSaleRecord, cls: 'col-2', },
                ]},

                {xtype: 'section', title: _lang.ProductDocument.tabCompliance},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.prop.riskRating', xtype: 'dictfield', readOnly: true, fieldLabel: _lang.ProductDocument.fRiskRating, cls: 'col-2',code:'check', codeSub:'product'},
                    {field: 'main.ageLimit', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fAgeRestriction, cls: 'col-2'},
                    {field: 'main.mandatory', xtype:'dictcombo', code:'options', codeSub:'yesno', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fMandatory, cls: 'col-2', },
                    {field: 'main.seasonal', xtype:'dictcombo', code:'options', codeSub:'yesno', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fSeasonal, cls: 'col-2', },
                    {field: 'main.indoorOutdoor', xtype:'dictcombo', code:'options', codeSub:'yesno', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.findoorOutdoor, cls: 'col-2', },
                    {field: 'main.electricalProduct', xtype:'dictcombo', code:'options', codeSub:'yesno', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fElectricalProduct, cls: 'col-2', },
                    {field: 'main.powerRequirements', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fPowerRequirements, cls: 'col-2'},
                    {field: 'main.packageName', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fPackageName, cls: 'col-2',},
                    {field: 'main.prop.productLink', xtype: 'textfield', title: _lang.ProductDocument.fProductLink, fieldLabel: _lang.ProductDocument.fProductLink,  cls: 'col-2',},
                    {field: 'main.prop.keywords', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fKeywords, cls: 'col-2',}
                ]},

                //供应商产品属性
                {xtype: 'section', title: _lang.ProductDocument.tabVendorProductsValue},
                {field: 'main.prop.vendorId', xtype: 'hidden'},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.prop.vendorName', xtype: 'VendorDialog', title: _lang.ProductDocument.fVendorId, fieldLabel: _lang.ProductDocument.fVendorId,  cls: 'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.prop.vendorId', single: true, readOnly: this.isReadOnly,
                        subcallback: function(rows){
                            if(!!rows && rows.length > 0){
                                var data = rows[0].data;
                                scope.getCmpByName('displayCurrency').setValue(data.currency);
                                scope.getCmpByName('main.prop.currency').setValue(data.currency);
                            }
                        }
                    },
                    {field: 'main.prop.currency', xtype: 'hidden'},
                    {field: 'displayCurrency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.ProductDocument.fCurrency, cls: 'col-2',},
                    {field: 'main.prop.factoryCode', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fFactoryCode, cls: 'col-2',},
                    {field: 'main.prop.moq', xtype: 'numberfield', decimalPrecision: 0, minValue: 0, fieldLabel: _lang.ProductDocument.fMoq, cls: 'col-2', allowBlank:false, },
                    {field: 'main.prop.originPortId', xtype: 'dictcombo', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fOriginPort, cls: 'col-2', code:'service_provider', codeSub:'origin_port'},
                    {field: 'main.prop.leadTime', xtype: 'numberfield', decimalPrecision: 0, minValue: 0, fieldLabel: _lang.ProductDocument.fLeadTime, cls: 'col-2',},
                    {field: 'main.prop.pcsPerCarton', xtype: 'numberfield', decimalPrecision: 0, minValue: 0, fieldLabel: _lang.ProductDocument.fPcsPerCarton, cls: 'col-2', allowBlank:false, },
                    {field: 'main.prop.pcsPerPallets', xtype: 'numberfield', decimalPrecision: 0, minValue: 0, fieldLabel: _lang.ProductDocument.fPcsPerPallets, cls: 'col-2',},
                    {field: 'main.prop.productParameter', xtype: 'htmleditor', title: _lang.ProductDocument.fProductParameter, fieldLabel: _lang.ProductDocument.fProductParameter, cls: 'col-2',},
                    {field: 'main.prop.productDetail', xtype: 'htmleditor', fieldLabel: _lang.ProductDocument.fProductDetail, cls: 'col-2', },
                ]},

                //  产品图片
                {xtype: 'section', title: _lang.ProductDocument.tabProductImages},
                { field: 'main.prop.images', xtype: 'hidden'},
                { field: 'main.prop.imagesName', xtype: 'hidden'},
                { field: 'main_images', xtype: 'ImagesDialog', fieldLabel: _lang.VendorDocument.tabImages,
                    formId: conf.mainFormPanelId, hiddenName: 'main.prop.images', titleName:'main.prop.imagesName', fileDefType: 1, readOnly: this.isReadOnly,
                },

                //  产品尺寸
                {xtype: 'section', title: _lang.ProductDocument.tabProductSize},
                {xtype: 'container', cls: 'row', items: [
                    {xtype: 'container', cls: 'col-2', items:
                        $groupvVlumeFields(this.mainFormPanelId, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:false, type:'textfield', allowBlank: true,
                            length:{field:'main.length', fieldLabel: _lang.ProductDocument.fLength, },
                            width:{field:'main.width', fieldLabel: _lang.ProductDocument.fWidth},
                            height:{field:'main.height', fieldLabel: _lang.ProductDocument.fHeight},
                            cmb:{field: 'main.cbm', },
                            cubicWeight: {field: 'main.cubicWeight', }
                        })
                    },

                    {xtype: 'container', cls: 'col-2', items: [
                        {field: 'main.cbm', xtype: 'numberfield', decimalPrecision: 4, minValue: 0, fieldLabel: _lang.ProductDocument.fCbm, cls: 'col-1',},
                        {field: 'main.netWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fNetWeight, cls: 'col-1',},
                        {field: 'main.cubicWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fCubicWeight, cls: 'col-1',},
                    ]}
                ]},

                //   内箱尺寸
                {xtype: 'section', title: _lang.ProductDocument.tabInnerCartonSize},
                {field: 'main.prop.id', xtype: 'hidden'},
                {xtype: 'container', cls: 'row', items: [
                    {xtype: 'container', cls: 'col-2', items:
                        $groupvVlumeFields(this.mainFormPanelId, {
                            decimalPrecision: 2, cls: 'col-1', readOnly:false, type:'textfield', allowBlank: false,
                            length:{field:'main.prop.innerCartonL', fieldLabel: _lang.ProductDocument.fInnerCartonL, },
                            width:{field:'main.prop.innerCartonW', fieldLabel: _lang.ProductDocument.fInnerCartonW},
                            height:{field:'main.prop.innerCartonH', fieldLabel: _lang.ProductDocument.fInnerCartonH},
                            cmb:{field: 'main.prop.innerCartonCbm', },
                            cubicWeight: {field: 'main.prop.innerCartonCubicWeight', }
                        }).concat([
                            {field: 'main.prop.innerCartonGrossWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fInnerCartonGrossWeight, cls: 'col-1', allowBlank: false,},
                        ])
                    },

                    {xtype: 'container', cls: 'col-2', items: [
                        {field: 'main.prop.innerCartonCbm', xtype: 'numberfield', decimalPrecision: 4, minValue: 0, fieldLabel: _lang.ProductDocument.fInnerCartonCbm, cls: 'col-1', allowBlank: false,},
                        {field: 'main.prop.innerCartonNetWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fInnerCartonNetWeight, cls: 'col-1', allowBlank: false,},
                        {field: 'main.prop.innerCartonCubicWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fInnerCartonCubicWeight, cls: 'col-1', allowBlank: false,},
                    ]}
                ]},

                //   外箱尺寸
                {xtype: 'section', title: _lang.ProductDocument.tabMasterCartonSize},
                {xtype: 'container', cls: 'row', items: [
                    {xtype: 'container', cls: 'col-2', items:
                        // {field: 'main.prop.masterCartonL', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonL, cls: 'col-1',},
                        // {field: 'main.prop.masterCartonW', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonW, cls: 'col-1',},
                            // {field: 'main.prop.masterCartonH', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonH, cls: 'col-1',},
                            $groupvVlumeFields(this.mainFormPanelId, {
                                decimalPrecision: 2, cls: 'col-1', readOnly:false, type:'textfield', allowBlank: false,
                                length:{field:'main.prop.masterCartonL', fieldLabel: _lang.ProductDocument.fMasterCartonL, },
                                width:{field:'main.prop.masterCartonW', fieldLabel: _lang.ProductDocument.fMasterCartonW},
                                height:{field:'main.prop.masterCartonH', fieldLabel: _lang.ProductDocument.fMasterCartonH},
                                cmb:{field: 'main.prop.masterCartonCbm', },
                                cubicWeight: {field: 'main.prop.masterCartonCubicWeight', }
                            }).concat([
                                {field: 'main.prop.masterCartonStructure', xtype: 'dictcombo', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fMasterCartonStructure, cls: 'col-1', value: '', code:'product', codeSub:'master_carton_structure', allowBlank: false},
                            ])
                    },
                    {xtype: 'container', cls: 'col-2', items: [
                        {field: 'main.prop.masterCartonCbm', xtype: 'numberfield', decimalPrecision: 4, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonCbm, cls: 'col-1', allowBlank: false},
                        {field: 'main.prop.masterCartonGrossWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonGrossWeight, cls: 'col-1',allowBlank: false},
                        {field: 'main.prop.masterCartonCubicWeight', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.ProductDocument.fMasterCartonCubicWeight, cls: 'col-1',allowBlank: false},
                        {field: 'main.prop.masterCartonWeight', xtype: 'dictcombo', fieldLabel: _lang.ProductDocument.fMasterCartonWeight, cls: 'col-2', value: '150g', code:'product', codeSub:'master_carton_weight', allowBlank: false}
                    ]}
                ]},

                //物流信息
                {xtype: 'section', title: _lang.ProductDocument.tabShippingInformation},
                {field: 'main.prop.serviceProviderId', xtype: 'hidden'},
                {xtype: 'container', cls: 'row',
                    items: [
                        {field: 'main.prop.recommendedCarrier', xtype: 'dictcombo', readOnly: this.isReadOnly, fieldLabel: _lang.ProductDocument.fRecommendedCarrier, code:'product', codeSub:'carrier', cls: 'col-2',value:'1',},
                    ].concat($groupPriceFields(scope, {
                        allowBlank: true,
                        aud: {
                            field: 'main.prop.estimatedAvgPostageAud',
                            fieldLabel: _lang.ProductDocument.fEstimatedAvgPostageAud,
                        },
                        rmb: {
                            field: 'main.prop.estimatedAvgPostageRmb',
                            fieldLabel: _lang.ProductDocument.fEstimatedAvgPostageRmb,
                        },
                        usd: {
                            field: 'main.prop.estimatedAvgPostageUsd',
                            fieldLabel: _lang.ProductDocument.fEstimatedAvgPostageUsd,
                        }
                    }))
                },

                // //附件信息
                // { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                // { field: 'main_documents', xtype:'hidden', value:this.record.attachment ,allowBlank:true, },
                // { field: 'main_documentName', xtype:'hidden', allowBlank:true, },
                // { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                //     mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                //     scope:this, readOnly: this.isApprove,allowBlank:true,
                // },

                //相关证书
                {xtype: 'section', cls: 'row', title: _lang.ProductDocument.tabCertificateTitle},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.prop.certificateCert', xtype: 'ProductDocumentFormCertificateGrid', fieldLabel: _lang.ProductDocument.fCertificateCert,}
                ]},

                //产品订单
                {xtype: 'section', title: _lang.ProductDocument.tabOrderListTitle},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'productCert', xtype: 'ProductDocumentFormOrderGrid', fieldLabel: _lang.ProductDocument.fProductCert,},
                ]},

                //相关报告
                { xtype: 'section', title: _lang.Reports.tabRelatedReports},
                { field: 'main.reports', xtype: 'ReportsFormMultiGrid',fieldLabel: _lang.Reports.tabRelatedReports,
                    farmeId: conf.mainFormPanelId, scope:this, readOnly: true,
                },

                { xtype: 'section', title: _lang.ProductDocument.tabIsSync},
                { xtype: 'container', cls: 'row',  items:  [
                    { field: 'main.isSync', xtype: 'dictfield',fieldLabel: _lang.ProductDocument.fIsSync,cls:'col-2', code:'product', codeSub:'sync' , readOnly: true,

                    },

                    { field: 'main.syncTime', xtype: 'displayfield',fieldLabel: _lang.ProductDocument.fIsSyncDate, cls:'col-2',},
                ]}
            ].concat($creatorInfo(this.isReadOnly, {notArchives: false}))
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url: conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName: 'main', loadMask: true, success: function (response) {
                    var json = Ext.JSON.decode(response.responseText);
                    //相关证书
                    if (!!json.data && !! json.data.certificates && json.data.certificates.length > 0) {
                        for (var index in json.data.certificates) {
                            Ext.getCmp(conf.CertificateGridPanelId).getStore().add(json.data.certificates[index]);
                        }
                    }

                    //相关订单
                    if (!!json.data && !! json.data.orders && json.data.orders.length > 0) {
                        for (var index in json.data.orders) {
                            Ext.getCmp(conf.OrderGridPanelId).getStore().add(json.data.orders[index]);
                        }
                    }

                    //reports init
                    if(this.action != 'copy') Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(!!json.data && !!json.data.reports ? json.data.reports: '');
                    //attachment init
                    // Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachment);

                    //barcode ean
                    if (json.data.barcode != null) {
                        Ext.getCmp('main_button_barcode').setDisabled(true);
                    }
                    if (json.data.ean != null) {
                        Ext.getCmp('main_button_ean').setDisabled(true);
                    }

                    // if(json.data.purchaseType != ''){
                    //     Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.purchaseType').setReadOnly(true);
                    // }
                    //
                    if(this.action == 'copy'){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.barcode').setValue('')
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.ean').setValue('')
                    }


                    //images init
                    if(!!json.data && !!json.data.prop && json.data.prop.imagesDoc != undefined && json.data.prop.imagesDoc.length>0){
                        for(index in json.data.prop.imagesDoc){
                            var images = {};
                            Ext.applyIf(images, json.data.prop.imagesDoc[index].document);
                            images.id= json.data.prop.imagesDoc[index].documentId;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_images').dataview.getStore().add(images);
                        }
                    }

                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('displayCurrency').setValue(Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.prop.currency').getValue());
                }
            });
        }
        this.formPanel.setReadOnly(this.isApprove || this.isConfirm, ['main.isSync']);

    },// end of the initcomponents

    saveRow : function (action){
        var params = {act: action? action: this.actionName ? this.actionName: 'save'};

        // //attachments
        // var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        // if(rows.length>0){
        //     for(index in rows){
        //         params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        //         params['attachments['+index+'].documentId'] = rows[index];
        //     }
        // }
        if(action == 'confirm') params.ahid = this.ahid;
        var sku = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.sku').getValue();
        var id = this.actionName == 'copy' ? '' : Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        var barcode = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.barcode').getValue();
        var ean = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.ean').getValue();




        Ext.Ajax.request({
            url: __ctxPath + 'archives/product/existsSku?sku=' + sku + '&id=' + id,
            scope: this, method: 'post',
            success: function (response, options) {
                var obj = Ext.decode(response.responseText);
                if (obj.success == true) {
                    if(obj.data == true){
                        var isExists = false;
                        if(barcode != ''){
                            Ext.Ajax.request({
                                url: __ctxPath + 'archives/product/existsBarcode?barcode=' + barcode + '&id=' +id ,
                                scope: this, method: 'post',
                                async:false,
                                success: function (response, options) {
                                    //return true;
                                    var obj = Ext.decode(response.responseText);
                                    if(obj.success == true){
                                        if(obj.data != true) {
                                            Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg, barcode);
                                            isExists = true;
                                        }
                                    }
                                }
                            });
                        }
                        if(ean != ''){
                            Ext.Ajax.request({
                                url: __ctxPath + 'archives/product/existsEan?ean=' + ean + '&id=' +id ,
                                scope: this, method: 'post',
                                async:false,
                                success: function (response, options) {
                                    //return true;
                                    var obj = Ext.decode(response.responseText);
                                    if(obj.success == true){
                                        if(obj.data != true) {
                                            Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg, ean);
                                            isExists = true;
                                        }
                                    }
                                }
                            });
                        }
                        if(!isExists){
                            $postForm({
                                formPanel: Ext.getCmp(this.mainFormPanelId),
                                grid: Ext.getCmp(this.mainGridPanelId),
                                scope: this,
                                url: this.urlSave,
                                params: params,
                                callback: function (fp, action, status, grid) {
                                    Ext.getCmp(this.mainGridPanelId + '-ArchivesHistoryTabGrid').refresh();
                                    Ext.getCmp(this.winId).close();
                                    if(!!grid) {
                                        grid.getSelectionModel().clearSelections();
                                        grid.getView().refresh();
                                    }
                                }
                            });
                        }
                    }else{
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg);
                    }
                }
            }
        });

    },


    getAutoBarCode: function () {
        var me = this;
        console.log( this);

        Ext.Ajax.request({
            url: __ctxPath + 'admin/autocode/generate?code=barcode',
            scope: this, method: 'post', success: function (response, options) {
                var obj = Ext.decode(response.responseText);
                if (obj.success == true) {
                    if (!!obj.data) {
                        me.scope.formPanel.getCmpByName('main.barcode').setValue(obj.data);
                        me.setDisabled(true);
                    } else {
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
                    }
                }
            }
        });
    },
    getAutoEanCode: function () {
        var me = this;
        console.log(this);
        Ext.Ajax.request({
            url: __ctxPath + 'admin/autocode/generate?code=ean',
            scope: this, method: 'post', success: function (response, options) {
                var obj = Ext.decode(response.responseText);
                if (obj.success == true) {
                    if (!!obj.data) {
                        me.scope.formPanel.getCmpByName('main.ean').setValue(obj.data);
                        me.setDisabled(true);
                    } else {
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
                    }
                }
            }
        });
    },
});