FlowCustomClearanceView = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.FlowCustomClearance.mTitle,
            moduleName: 'FlowCustomClearance',
            formName:'FlowCustomClearanceForm',
            winId : 'FlowCustomClearanceViewForm',
            frameId : 'FlowCustomClearanceView',
            mainGridPanelId : 'FlowCustomClearanceViewGridPanelID',
            mainFormPanelId : 'FlowCustomClearanceViewFormPanelID',
            processFormPanelId: 'FlowCustomClearanceProcessFormPanelID',
            searchFormPanelId: 'FlowCustomClearanceViewSearchFormPanelID',
            mainTabPanelId: 'FlowCustomClearanceViewMainTbsPanelID',
            subGridPanelId : 'FlowCustomClearanceViewSubGridPanelID',
            formGridPanelId : 'FlowCustomClearanceFormGridPanelID',

            urlList: __ctxPath + 'flow/purchase/customClearance/list',
            urlSave: __ctxPath + 'flow/purchase/customClearance/save',
            urlDelete: __ctxPath + 'flow/purchase/customClearance/delete',
            urlGet: __ctxPath + 'flow/purchase/customClearance/get',
            urlExport: __ctxPath + 'flow/purchase/customClearance/export',
            urlFlow: __ctxPath + 'flow/purchase/customClearance/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowCustomClearance&processInstanceId=',
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
        FlowCustomClearanceView.superclass.constructor.call(this, {
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
                {field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fPurchaseOrderId},
                {field:'Q-ciNumber-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fCiNumber},
                {field:'Q-vessel-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fVessel},
                {field:'Q-voy-S-LK', xtype:'textfield', title:_lang.FlowCustomClearance.fVoy},
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
                { field: 'Q-readyDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowCustomClearance.fReadyDate, format: curUserInfo.dateFormat},
			    { field: 'Q-etd-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowCustomClearance.fEtd, format: curUserInfo.dateFormat},
			    { field: 'Q-eta-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowCustomClearance.fEta, format: curUserInfo.dateFormat},
                { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},

            ]
        });// end of searchPanel

        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.FlowCustomClearance.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id', 'orderNumber','serviceProvider', 'effectiveDate', 'validUntil', 'sailingDays', 'sailingFrequency','tradeTerm',
                'exchangeRateAudToUsd', 'currencyAdjustment', 'currency','eta','etd','readyDate','packingDate','containerQty','deliveryTerms', 'totalPackingCbm','totalShippingCbm',
                'vessel','voy','ciNumber','accessories','creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold',
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: _lang.FlowCustomClearance.fId, dataIndex: 'id', width: 180 },
                // { header: _lang.FlowCustomClearance.fServiceProvider, dataIndex: 'serviceProvider', width: 160},
                { header: _lang.FlowCustomClearance.fPurchaseOrderId, dataIndex: 'orderNumber', width: 90},
                { header: _lang.FlowCustomClearance.fContainerQty, dataIndex: 'containerQty', width: 60},
                { header: _lang.FlowCustomClearance.fTotalPackingCbm, dataIndex: 'totalPackingCbm', width: 80},
                { header: _lang.FlowCustomClearance.fTotalShippingCbm, dataIndex: 'totalShippingCbm', width: 80},
                { header: _lang.FlowCustomClearance.fReadyDate, dataIndex: 'readyDate', width: 140},
                { header: _lang.FlowCustomClearance.fEtd, dataIndex: 'etd', width: 140},
                { header: _lang.FlowCustomClearance.fEta, dataIndex: 'eta', width: 140},
                // { header: _lang.FlowCustomClearance.fPackingDate, dataIndex: 'packingDate', width: 140},
                { header: _lang.FlowCustomClearance.fTradeTerm, dataIndex: 'tradeTerm', width: 60},
                { header: _lang.FlowCustomClearance.fVessel, dataIndex: 'vessel', width: 120},
                { header: _lang.FlowCustomClearance.fVoy, dataIndex: 'voy', width: 80},
                { header: _lang.FlowCustomClearance.fCiNo, dataIndex: 'ciNumber', width: 200},
                { header: _lang.FlowCustomClearance.fHasAccessory, dataIndex: 'accessories', width:60,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo, [])},
                },

            ],// end of column
            appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });



        this.productGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId + '-2',
            title: _lang.FlowCustomClearance.fProductDetails,
            forceFit: false,
            width: 'auto',
            height: 242,
            url: '',
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header: {cls:'x-panel-header-gray'  },
            fields: [
                'id', 'sku', 'barcode', 'factoryCode', 'color', 'size', 'style','productId',
                'orderQty', 'cartons','packingQty', 'packingCartons','pcsPerCarton','rateAudToRmb','rateAudToUsd','currency',
                'unitCbm', 'totalCbm', 'totalNw', 'totalGw','priceAud','priceRmb','priceUsd'
            ],
            columns: [

                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: 'ProductId', dataIndex: 'productId', width: 40, hidden: true },
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200, scope:this, },
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'barcode', width: 200, },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },

                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd', null, {defaultRate: {
                    audToRmb: '',
                    audToUsd: '',
                }}), },

                {
                    header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud', 'priceRmb', 'priceUsd', null, {edit: false})
                },
                { header: _lang.FlowCustomClearance.fOrderQty, dataIndex: 'orderQty', width: 80,},
                { header: _lang.FlowCustomClearance.fCartons, dataIndex: 'cartons', width: 80, },
                { header: _lang.FlowCustomClearance.fPackingCartons, dataIndex: 'packingCartons', width: 80, },
                { header: _lang.ProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', width: 80, hidden:true, },
                { header: _lang.FlowCustomClearance.fColor, dataIndex: 'color', width: 120, },
                { header: _lang.FlowCustomClearance.fSize, dataIndex: 'size', width: 120, },
                { header: _lang.FlowCustomClearance.fStyle, dataIndex: 'style', width: 120, },
               // { header: _lang.FlowCustomClearance.fUnitCbm, dataIndex: 'unitCbm', width: 70, },
                { header: _lang.FlowCustomClearance.fTotalCbm, dataIndex: 'totalCbm', width: 70, },
                //{ header: _lang.FlowCustomClearance.fTotalNw, dataIndex: 'totalNw', width: 70, },
                { header: _lang.FlowCustomClearance.fTotalGw, dataIndex: 'totalGw', width: 70, },
            ]// end of columns
        });

        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        this.imagesGridPanel  = new HP.GridPanel({
            id : conf.mainTabPanelId+'-4',
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
                { header: _lang.TText.rowAction, xtype: 'rowactions', width:55, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }, {
                        iconCls: 'btnRowDownload', btnCls: 'fa-download', tooltip: _lang.MyDocument.mFileDownload,
                        callback: function (grid, record, action, idx, col, e, target) {
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

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.attachmentsGridPanel);
        viewTabs.add(this.imagesGridPanel);

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

        var attaList = Ext.getCmp(conf.mainTabPanelId).items.items[2];
        attaList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                var productList = Ext.getCmp(conf.subGridPanelId + '-2');
                productList.getStore().removeAll();
                if(!!json.data && !!json.data.details){
                   for(var index in json.data.details){
                       var packing = json.data.details[index];
                       for(var i in packing.packingList){
                           productList.getStore().add(packing.packingList[i]);
                       }
                   }
                }

                //attachment init
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                //photos init
                var imagesList = Ext.getCmp(conf.mainTabPanelId + '-4');
                imagesList.getStore().removeAll();
                if(!!json.data && !!json.data.imagesDoc &&  json.data.imagesDoc.length>0){
                    for(index in json.data.imagesDoc){
                        var images = {};
                        images = json.data.imagesDoc[index];
                        Ext.applyIf(images, json.data.imagesDoc[index].document);
                        images.id= json.data.imagesDoc[index].documentId;
                        imagesList.getStore().add(images);
                    }
                }

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
        }
    },

    editRow : function(conf){
        App.clickTopTab('FlowCustomClearanceForm', conf);
    },

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                console.log(grid);
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
            case 'btnRowDownload':
                window.open(__ctxPath + 'mydoc/download?fileId=' + record.data.id);
                break;
        }
    }
});