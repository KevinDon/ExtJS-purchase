ProductCertificateDocumentView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductCertificateDocument.mTitle,
            moduleName: 'ProductCertificate',
            winId : 'ProductCertificateDocumentViewForm',
            frameId : 'ProductCertificateDocumentView',
            mainGridPanelId : 'ProductCertificateDocumentViewGridPanelID',
            mainFormPanelId : 'ProductCertificateDocumentViewFormPanelID',
            processFormPanelId: 'ProductCertificateDocumentProcessFormPanelID',
            searchFormPanelId: 'ProductCertificateDocumentViewSearchFormPanelID',
            mainTabPanelId: 'ProductCertificateDocumentViewMainTbsPanelID',
            subProductGridPanelId : 'ProductCertificateDocumentViewSubGridPanelID',
            formGridPanelId : 'ProductCertificateDocumentFormGridPanelID',

            urlList: 'archives/productCertificate/list',
            urlSave: __ctxPath + 'archives/productCertificate/save',
            urlDelete: __ctxPath + 'archives/productCertificate/delete',
            urlGet: __ctxPath + 'archives/productCertificate/get',
            urlListProduct: __ctxPath + 'archives/productCertificate/list',
            refresh: true,
            edit: true,
            add: true,
            copy: true,
            del: true,
            editFun: this.editRow
        };

        this.initUIComponents(conf);
        ProductCertificateDocumentView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.centerPanel ]
        });
    },

    initUIComponents: function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            fieldItems:[
                {field:'Q-id-S-LK', xtype:'textfield', title:_lang.VendorDocument.fSystemId,},

                {field:'Q-departmentId-S-EQ', xtype:'hidden'},
                {field:'departmentName', xtype:'DepDialog', title:_lang.TText.fDepartmentName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
                },
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-certificateNumber-S-LK', xtype:'textfield', title:_lang.ProductCertificate.fCertificateNumber},
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                },
                {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
                {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
                {field:'Q-relevantStandard-S-LK', xtype:'textfield', title:_lang.ProductCertificate.fRelevantStandard},
                {field:'Q-description-S-LK', xtype:'textfield', title:_lang.ProductCertificate.fDesc},
                { field: 'Q-effectiveDate-D', xtype: 'DateO2TField', fieldLabel: _lang.ProductCertificate.fEffectiveDate, format: curUserInfo.dateFormat},
                { field: 'Q-validUntil-D', xtype: 'DateO2TField', fieldLabel: _lang.ProductCertificate.fValidUntil, format: curUserInfo.dateFormat},
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
                

            ]
        });// end of searchPanel


        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id','vendorId','vendorCnName','vendorEnName','productId', 'relevantStandard','description','certificateNumber','effectiveDate','validUntil',
                'certificateFile', 'status', 'creatorId','creatorCnName','creatorEnName','createdAt',
                'updatedAt','departmentId','departmentEnName','departmentCnName'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductCertificate.fId, dataIndex: 'id', width: 180,},
                { header: _lang.ProductCertificate.fProductId, dataIndex: 'productId', width: 100, hidden:true},
                { header: _lang.ProductCertificate.fVendorId, dataIndex: 'vendorId', width: 100, hidden:true },
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.ProductCertificate.fRelevantStandard, dataIndex: 'relevantStandard', width: 140 , },
                { header: _lang.ProductCertificate.fDesc, dataIndex: 'description', width: 200 , },
                { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200 , },
                { header: _lang.ProductCertificate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140 , },
                { header: _lang.ProductCertificate.fValidUntil, dataIndex: 'validUntil', width: 140 , }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            },
        });

        this.ProductCertificateDocumentTabsPanel = new App.ProductCertificateDocumentTabs({
            region: 'south',
            //collapsible: true,
            split: true,
            mainTabPanelId: conf.mainTabPanelId,
    		mainGridPanelId: conf.mainGridPanelId,
            gridPanelId: conf.subProductGridPanelId,
            productCertificateGridPanelId: conf.productCertificateList,
            items: [],
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.ProductCertificateDocumentTabsPanel ]
        });
    },// end of the init

    rowClick: function(record, item, index, e, conf) {
        var productList = Ext.getCmp(conf.subProductGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        productList.getStore().add(product);
                    }
                }
              //archivesHistory
                if(record.data.id) {
                    Ext.getCmp(conf.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);
                }
            }
        });

    },
    editRow : function(conf){
        new ProductCertificateDocumentForm(conf).show();
    }
});