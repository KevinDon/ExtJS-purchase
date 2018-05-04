FlowProductCertificationView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.FlowProductCertification.mTitle,
            moduleName: 'FlowProductCertification',
            winId : 'FlowProductCertificationForm',
            frameId : 'FlowProductCertificationView',
            mainGridPanelId : 'FlowProductCertificationGridPanelID',
            mainFormPanelId : 'FlowProductCertificationFormPanelID',
            processFormPanelId: 'FlowProductCertificationProcessFormPanelID',
            searchFormPanelId: 'FlowProductCertificationSearchFormPanelID',
            mainTabPanelId: 'FlowProductCertificationMainTbsPanelID',
            productCertificationGridPanel : 'FlowProductCertificationSubGridPanelID',
            formGridPanelId : 'FlowProductCertificationFormGridPanelID',

            urlList: 'flow/inspection/productCertification/list',
            urlSave: __ctxPath + 'flow/inspection/productCertification/save',
            urlDelete: __ctxPath + 'flow/inspection/productCertification/delete',
            urlGet: __ctxPath + 'flow/inspection/productCertification/get',
            urlExport: __ctxPath + 'flow/inspection/productCertification/export',
            urlListProduct: __ctxPath + 'flow/inspection/productCertification/listproduct',
            urlFlow: __ctxPath + 'flow/inspection/productCertification/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowProductCertification&processInstanceId=',
            refresh: true,
			add: true,
			copy: true,
			edit: true,
			del: true,
			flowMine:true,
			flowInvolved:true,
			export:true,
			editFun: this.editRow,
        };

        this.initUIComponents(conf);
        FlowProductCertificationView.superclass.constructor.call(this, {
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
                {field:'Q-id-S-LK', xtype:'textfield', title:_lang.TText.fId},
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
                {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
                {field:'Q-departmentId-S-EQ', xtype:'hidden'},
                {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
                },
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                    store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                },
                {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
                {field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
                {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
                { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel


        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.FlowProductCertification.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id','sku','vendorCnName','vendorEnName','name','categoryName','productCertificateId',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold'

            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header:_lang.TText.fId, dataIndex: 'id', width:180 },
//                { header: _lang.FlowProductCertification.fVenderName, dataIndex: 'vendorName', width: 100,},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
            ],// end of column
            appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.productCertificationGridPanel = new HP.GridPanel({
            id: conf.productCertificationGridPanel,
            title: _lang.FlowProductCertification.tabCertificateList,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','sku','vendorCnName','vendorEnName','certificateNumber','description','effectiveDate','validUntil'
            ],
            columns: [
//            { header: _lang.ProductCertificate.fVendorName, dataIndex: 'vendorName', width: 100,},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.FlowProductCertification.fProductCertificateId, dataIndex: 'id', width: 200,},
                { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200,},
                { header: _lang.ProductCertificate.fDesc, dataIndex: 'description', width: 100,},
                { header: _lang.ProductCertificate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140,},
                { header: _lang.ProductCertificate.fValidUntil, dataIndex: 'validUntil', width: 140,},
            ]// en d of columns
        });

        this.productListGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainTabPanelId + 'product',
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            fields: [
                'id','sku','combined', 'name',
                'barcode','categoryName',
                'packageName','color','model','style','length',
                'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
                'seasonal', 'indoorOutdoor','electricalProduct',
                'powerRequirements','mandatory','operatedAt',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt'
            ],
            columns: [

                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 40 ,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 200 },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 200 },
                { header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 40 },
                { header: _lang.ProductDocument.fModel, dataIndex: 'model', width: 40 },

                { header: _lang.ProductDocument.fProductInformation, columns:[
                    { header: _lang.ProductDocument.fStyle, dataIndex: 'style', width: 40 ,sortable: true, },
                    { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
                ]},

                { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 40,

                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }

                },
                { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 40},
                { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
            ]// end of columns
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productCertificationGridPanel);
        viewTabs.add(this.productListGridPanel);
        viewTabs.add(this.attachmentsGridPanel);

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, viewTabs ]
        });
    },// end of the init

    rowClick: function(record, item, index, e, conf) {

        var certificationList = Ext.getCmp(conf.productCertificationGridPanel);
        certificationList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,maskTo: conf.mainTabPanelId, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    //attachment init
                certificationList.getStore().removeAll();
                if(json.data.certificates.length>0){
                        for(index in json.data.certificates){
                            var attachments = {};
                            Ext.applyIf(attachments, json.data.certificates[index]);
                            //Ext.apply(attachments, json.data.certificates[index].certificateAttach.document);

                            certificationList.getStore().add(attachments);
                        }
                    }

                    var productList = Ext.getCmp( conf.mainTabPanelId + 'product');
                    productList.getStore().removeAll();
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        productList.getStore().add(product);
                    }

                    //附件
                     Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json. data.attachments: '');
                }
        });

        var cmpDirection = Ext.getCmp(conf.mainTabPanelId + '-view-direction');
        cmpDirection.clean();
        cmpDirection.load(conf.urlDirection + record.data.processInstanceId);

        var cmpHistory = Ext.getCmp(conf.mainTabPanelId + '-view-history');
        cmpHistory.getStore().removeAll();
        if(record.data.processInstanceId){
            cmpHistory.getStore().url = conf.urlHistoryList;
            $HpSearch({
                searchQuery: {"Q-businessId-S-EQ": record.data.id},
                gridPanel: cmpHistory
            });
        };

    },
    editRow : function(conf){
        App.clickTopTab('FlowProductCertificationForm', conf);
    }
});