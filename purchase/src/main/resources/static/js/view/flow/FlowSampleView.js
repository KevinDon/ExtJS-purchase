FlowSampleView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.FlowSample.mTitle,
            moduleName: 'FlowSample',
            winId : 'FlowSampleForm',
            frameId : 'FlowSampleView',
            mainGridPanelId : 'FlowSampleViewGridPanelID',
            mainFormPanelId : 'FlowSampleViewFormPanelID',
            processFormPanelId: 'FlowSampleViewProcessFormPanelID',
            searchFormPanelId: 'FlowSampleViewSearchFormPanelID',
            mainTabPanelId: 'FlowSampleViewMainTbsPanelID',
            subGridPanelId : 'FlowSampleViewSubGridPanelID',
            formGridPanelId : 'FlowSampleViewFormGridPanelID',

            urlList: __ctxPath + 'flow/purchase/flowsample/list',
            urlSave: __ctxPath + 'flow/purchase/flowsample/save',
            urlDelete: __ctxPath + 'flow/purchase/flowsample/delete',
            urlGet: __ctxPath + 'flow/purchase/flowsample/get',
            urlExport: __ctxPath + 'flow/purchase/flowsample/export',
            urlListProduct: __ctxPath + 'flow/purchase/flowsample/listproduct',
            // urlSubList: __ctxPath + 'flow/purchase/flowsample/SubList',
            urlFlow: __ctxPath + 'flow/purchase/flowsample/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowSample&processInstanceId=',
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
        FlowSampleView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.centerPanel ]
        });
    },

    initUIComponents: function(conf) {

        var $sampleFeeRefund = new $HpDictStore({code:'samplefee', codeSub:'samplefee_return'});
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
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},

            ]
        });// end of searchPanel

        var $currency = new $HpDictStore({code:'transaction', codeSub:'currency'});
        var $flowStatus = new $HpDictStore({code:'workflow', codeSub:'flow_status'});
        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.FlowSample.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id','vendorCnName','vendorEnName','skus','totalSampleFeeAud', 'totalSampleFeeRmb','totalSampleFeeUsd',
                'sampleReceiver','currency','sampleFeeRefund','processInstanceId','rateAudToRmb','rateAudToUsd',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName'
                ,'startTime','endTime','flowStatus','updatedAt', 'createdAt', 'hold',
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: _lang.FlowSample.fId, dataIndex: 'id', width: 185 },
            	{ header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.FlowSample.fSampleFee,
                    columns: new $groupPriceColumns(this, 'totalSampleFeeAud','totalSampleFeeRmb','totalSampleFeeUsd', null, {edit:false})
                },


                // { header: _lang.FlowSample.fSampleFeeRefund, dataIndex: 'sampleFeeRefund', width: 90 ,
                //     renderer: function(value){
                //         if($sampleFeeRefund.length>0 && $sampleFeeRefund[0].data.options.length>0){
                //             return $dictRenderOutputColor(value, $sampleFeeRefund[0].data.options);
                //         }
                //     }
                // },
            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow(),

            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.productGridPanel = new HP.GridPanel({
            region: 'south',
            id: conf.subGridPanelId,
            title: _lang.ProductDocument.tabListTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            // url: conf.urlListProduct,
            fields: [
                'id','sku','vendorCnName','vendorEnName','productCategoryId', 'categoryName','name',
                'sampleName','sampleFeeAud','sampleFeeRmb','sampleFeeUsd','currency',
                'sampleFeeRefund','sampleReceiver','rateAudToRmb','rateAudToUsd','orderValueAud','orderValueRmb','orderValueUsd',
                'qty',
                'masterCartonL','innerCartonW','innerCartonH','innerCartonCbm','masterCartonWeight',
                'innerCartonL','masterCartonCbm','masterCartonW','masterCartonH', 'pcsPerCarton',
                'length','width','height','netWeight','cubicWeight','cbm'
            ],
            height: 300,
            minHeight: 200,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
                { header: _lang.NewProductDocument.fCategoryId, dataIndex: 'categoryName', width: 160, },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 40,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},

                { header: _lang.FlowSample.fSampleFee,
                    columns: new $groupPriceColumns(this, 'sampleFeeAud','sampleFeeRmb','sampleFeeUsd', function(row, value){
                        row.set('orderValueAud', (row.get('sampleFeeAud') * row.get('qty')).toFixed(3));
                        row.set('orderValueRmb', (row.get('sampleFeeRmb') * row.get('qty')).toFixed(3));
                        row.set('orderValueUsd', (row.get('sampleFeeUsd') * row.get('qty')).toFixed(3));
                    },{edit:false, gridId: conf.formGridPanelId})
                },
                //采购数量
                { header: _lang.FlowSample.fSampleQty, dataIndex: 'qty', scope:this, width: 80,},
                { header: _lang.FlowFeeRegistration.fSubtotal,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', function(row, value){
                    },{edit:false, gridId: conf.formGridPanelId})
                },
                { header: _lang.FlowSample.fSampleFeeRefund, dataIndex: 'sampleFeeRefund', width: 100,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    },
                },
                {header: _lang.ProductDocument.tabProductSize,
                    columns: [
                        {header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 80,},
                        {header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 80,},
                        {header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 80,},
                        { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight'  },
                        { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight'  },
                        { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabInnerCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonL', width: 80,},
                        { header: _lang.ProductDocument.fInnerCartonW, dataIndex: 'innerCartonW' },
                        { header: _lang.ProductDocument.fInnerCartonH, dataIndex: 'innerCartonH' },
                        { header: _lang.ProductDocument.fInnerCartonCbm, dataIndex: 'innerCartonCbm'  },
                    ],
                },
                {header: _lang.ProductDocument.tabMasterCartonSize,
                    columns: [
                        {header: _lang.ProductDocument.fMasterCartonL, dataIndex: 'masterCartonL', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonW, dataIndex: 'masterCartonW', width: 80,},
                        {header: _lang.ProductDocument.fMasterCartonH, dataIndex: 'masterCartonH', width: 80,},
                        { header: _lang.ProductDocument.fMasterCartonCbm, dataIndex: 'masterCartonCbm'  },
                    ],
                },

            ],

        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.productGridPanel);
        viewTabs.add(this.attachmentsGridPanel);

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [this.gridPanel, viewTabs]
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
                if(json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.applyIf(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product);
                        Ext.applyIf(product, json.data.details[index].product.prop);
                        product.orderValueAud = (json.data.details[index].sampleFeeAud *json.data.details[index].qty).toFixed(3);
                        product.orderValueRmb = (json.data.details[index].sampleFeeRmb *json.data.details[index].qty).toFixed(3);
                        product.orderValueUsd = (json.data.details[index].sampleFeeUsd *json.data.details[index].qty).toFixed(3);

                        productList.getStore().add(product);
                    }
                }

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
            }
        });


        var cmpDirection = Ext.getCmp(conf.mainTabPanelId + '-view-direction');
        cmpDirection.clean();
        cmpDirection.load(conf.urlDirection + record.data.processInstanceId);

        var cmpHistory = Ext.getCmp(conf.mainTabPanelId + '-view-history');
        cmpHistory.getStore().removeAll();
        if(record.data.processInstanceId){
            cmpHistory.getStore().url = conf.urslHistoryList;
            $HpSearch({
                searchQuery: {"Q-businessId-S-EQ": record.data.id},
                gridPanel: cmpHistory
            });
        };

    },
    editRow : function(conf){
        App.clickTopTab('FlowSampleForm', conf);
    }
});
