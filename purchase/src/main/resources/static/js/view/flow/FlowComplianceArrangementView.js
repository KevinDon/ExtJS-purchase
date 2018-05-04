FlowComplianceArrangementView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.FlowComplianceArrangement.mTitle,
            moduleName: 'FlowComplianceArrangement',
            winId : 'FlowComplianceArrangementForm',
            frameId : 'FlowComplianceArrangementView',
            mainGridPanelId : 'FlowComplianceArrangementViewGridPanelID',
            mainFormPanelId : 'FlowComplianceArrangementViewFormPanelID',
            processFormPanelId: 'FlowComplianceArrangementViewProcessFormPanelID',
            searchFormPanelId: 'FlowComplianceArrangementViewSearchFormPanelID',
            mainTabPanelId: 'FlowComplianceArrangementViewMainTbsPanelID',
            subGridPanelId : 'FlowComplianceArrangementViewSubGridPanelID',
            formGridPanelId : 'FlowComplianceArrangementViewFormGridPanelID',

            urlList: __ctxPath + 'flow/inspection/complianceApply/list',
            urlSave: __ctxPath + 'flow/inspection/complianceApply/save',
            urlDelete: __ctxPath + 'flow/inspection/complianceApply/delete',
            urlGet: __ctxPath + 'flow/inspection/complianceApply/get',
            urlExport: __ctxPath + 'flow/inspection/complianceApply/export',
            urlListProduct: __ctxPath + 'flow/inspection/complianceApply/listproduct',
            urlFlow: __ctxPath + 'flow/inspection/complianceApply/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowComplianceApply&processInstanceId=',
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
        FlowComplianceArrangementView.superclass.constructor.call(this, {
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
            title: _lang.FlowComplianceArrangement.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id', 'vendorId','vendorCnName', 'vendorEnName','creatorId','creatorName','handlerId','startTime',
                'handlerDepartmentId','handlerDepartmentName',
                'applicantId', 'applicantName','departmentId','applyTime', 'createdAt','status','flowStatus','details',
                'processInstanceId','updatedAt','hold',
                'startTime', 'endTime', 'creatorId', 'creatorCnName', 'creatorEnName','assigneeId','assigneeCnName','assigneeEnName',
                'departmentName', 'departmentCnName', 'departmentEnName'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 180 },
//                { header: _lang.FlowNewProduct.fVendorName, dataIndex: 'vendorName', width: 160},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.ProductDocument.fVendorId, dataIndex: 'vendorId', width: 160,hidden: true},
            ],// end of column
            appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.productGridPanel = new HP.GridPanel({
            id: conf.subGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            // url: conf.urlListProduct,
            fields: [
                'id','sku','name','barcode',
                'seasonal','indoorOutdoor','electricalProduct','powerRequirements','vendorId',
                'productParameter', 'productDetail', 'productLink', 'keywords',
                'mandatory', 'creatorName','departmentName',  'vendorName','productLink','newPrevRiskRating','prevRiskRating'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.NewProductDocument.fSku, dataIndex: 'sku', width: 200},

                { header: _lang.NewProductDocument.fName, dataIndex: 'name', width: 200, },


                { header: _lang.FlowComplianceArrangement.fRiskRating,
                    columns: [
                        { header: _lang.FlowComplianceArrangement.fNewPrevRiskRating, dataIndex: 'newPrevRiskRating', sortable: true, width: 120,
                            renderer: function(value){
                                var $riskRating = _dict.riskRating;
                                if(value  && $riskRating.length > 0 && $riskRating[0].data.options.length>0){
                                    return !! $riskRating[0].data.options[parseInt(value) - 1] ? $riskRating[0].data.options[parseInt(value) - 1].title: '';
                                }
                            }
                        },
                        { header: _lang.FlowComplianceArrangement.fPrevRiskRating, dataIndex: 'prevRiskRating', sortable: true, width: 120,
                            renderer: function(value){
                                var $riskRating = _dict.riskRating;
                                if(value  && $riskRating.length > 0 && $riskRating[0].data.options.length>0){
                                    return !! $riskRating[0].data.options[parseInt(value) - 1] ? $riskRating[0].data.options[parseInt(value) - 1].title: '';
                                }
                            }
                        }
                    ]

                },


                { header: _lang.NewProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fIndoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 80},
                { header: _lang.NewProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },

                // { header: _lang.NewProductDocument.fProductParameter, dataIndex: 'productParameter', width: 80 },
                // { header: _lang.NewProductDocument.fProductDetail, dataIndex: 'productDetail', width: 200 },
                { header: _lang.NewProductDocument.fProductLink, dataIndex: 'productLink', width: 200,
                    renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                        var productLink = record.data.productLink;
                        if(productLink){
                            return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                        }
                    }
                },
                { header: _lang.NewProductDocument.fKeywords, dataIndex: 'keywords', width: 100 },
            ]// end of columns
        });

        this.reportGridPanel = new App.ReportProductTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.Reports.tabRelatedReports,
            scope: this,
            noTitle: true
        });

        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.reportGridPanel);
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

        var productList = Ext.getCmp(conf.subGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
            success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(!!json.data && !!json.data.details && json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
                        product.prevRiskRating = json.data.details[index].product.prop.riskRating;
                        productList.getStore().add(product);
                    }
                }

                //相关报告
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
        App.clickTopTab('FlowComplianceArrangementForm', conf);
    }
});