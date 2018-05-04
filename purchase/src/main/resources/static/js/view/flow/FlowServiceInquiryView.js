FlowServiceInquiryView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowServiceInquiry.mTitle,
			moduleName: 'FlowServiceInquiry',
			formName:'FlowServiceInquiryForm',
			winId : 'FlowServiceInquiryViewForm',
			frameId : 'FlowServiceInquiryView',
			mainGridPanelId : 'FlowServiceInquiryViewGridPanelID',
			mainFormPanelId : 'FlowServiceInquiryViewFormPanelID',
			processFormPanelId: 'FlowServiceInquiryProcessFormPanelID',
			searchFormPanelId: 'FlowServiceInquiryViewSearchFormPanelID',
			mainTabPanelId: 'FlowServiceInquiryViewMainTbsPanelID',
			subGridPanelId : 'FlowServiceInquiryViewSubGridPanelID',
			formGridPanelId : 'FlowServiceInquiryFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/serviceinquiry/list',
			urlSave: __ctxPath + 'flow/shipping/serviceinquiry/save',
			urlDelete: __ctxPath + 'flow/shipping/serviceinquiry/delete',
			urlGet: __ctxPath + 'flow/shipping/serviceinquiry/get',
			urlExport: __ctxPath + 'flow/shipping/serviceinquiry/export',
			urlFlow: __ctxPath + 'flow/shipping/serviceinquiry/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowServiceProviderQuotation&processInstanceId=',
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
		FlowServiceInquiryView.superclass.constructor.call(this, {
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
				{field:'Q-type-S-EQ', xtype:'dictcombo', value:'', title:_lang.FlowServiceInquiry.fQuotationType, code:'service_inquiry', codeSub:'quotation_type', addAll:true},
            	{field:'Q-serviceProviderCnName-S-LK', xtype:'textfield', title:_lang.TText.fServiceProviderCnName},
            	{field:'Q-serviceProviderEnName-S-LK', xtype:'textfield', title:_lang.TText.fServiceProviderEnName},
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled],  ['4',  _lang.TText.vCancel]]
			    },
			    {field:'Q-flowStatus-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fFlowStatus, code:'workflow', codeSub:'flow_status', addAll:true},
	                { field: 'Q-effectiveDate-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowServiceInquiry.fEffectiveDate, format: curUserInfo.dateFormat},
				    { field: 'Q-validUntil-D', xtype: 'DateO2TField', fieldLabel: _lang.FlowServiceInquiry.fValidUntil, format: curUserInfo.dateFormat},
			    { field: 'Q-startTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fStartTime, format: curUserInfo.dateFormat},
			    { field: 'Q-endTime-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fEndTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			    
			]
		});// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowServiceInquiry.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'type', 'serviceProviderName','serviceProviderCnName','serviceProviderEnName','currencyAdjustment', 'effectiveDate','name',
				'validUntil', 'rateAudToRmb', 'rateAudToUsd','totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', 'currency','totalPriceGp20Aud','totalPriceGp20Rmb',
				'totalPriceGp20Usd','totalPriceGp40Aud','totalPriceGp40Rmb','totalPriceGp40Usd','totalPriceHq40Aud','totalPriceHq40Rmb','totalPriceHq40Usd',
				'totalPriceLclAud','totalPriceLclRmb','totalPriceLclUsd', 'totalGp20Qty', 'totalGp40Qty','totalHq40Qty','hold',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowServiceInquiry.fId, dataIndex: 'id', width: 180  },
                { header:_lang.FlowServiceInquiry.fQuotationType, dataIndex: 'type', width: 80 ,
                    renderer: function(value){
                        var $quotationType = _dict.quotationType;
                        if($quotationType.length>0 && $quotationType[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $quotationType[0].data.options);
                        }
                    }
				},
                { header: _lang.FlowServiceInquiry.fName, dataIndex: 'name', width: 80, },
//            { header: _lang.FlowServiceInquiry.fServiceProvider, dataIndex: 'serviceProviderName', width: 160},
                { header: _lang.TText.fServiceProviderCnName, dataIndex: 'serviceProviderCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fServiceProviderEnName, dataIndex: 'serviceProviderEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.FlowServiceInquiry.fEffectiveDate, dataIndex: 'effectiveDate', width: 140},
                { header: _lang.FlowServiceInquiry.fValidUntil, dataIndex: 'validUntil', width: 140},

				{header: _lang.NewProductDocument.fExchangeRate,
                    columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')
                },

                { header: _lang.FlowServiceInquiry.fCurrencyAdjustment, dataIndex: 'currencyAdjustment', width: 80,  align: 'right',
					renderer: Ext.util.Format.numberRenderer('00.0000')
				},
			],// end of column
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});
        this.freightChargesGridPanel = new HP.GridPanel({
				id: conf.mainTabPanelId + '-2',
				title: _lang.FlowServiceInquiry.fFreightCharges,
				forceFit: false,
				scope: this,
				width: 'auto',
				url: '',
				autoLoad: false,
				rsort: false,
				edit: !this.readOnly,
				displayAllPrice: true,
				fields: [
					'id', 'originPortId','originPortCnName','originPortEnName', 'destinationPortId', 'destinationPortCnName','destinationPortEnName',
					'sailingDays','frequency','priceBaseOceanUsd','priceInsuranceAud','priceInsuranceRmb','priceInsuranceUsd',
					'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd' ,'priceGp20InsuranceAud', 'priceGp20InsuranceRmb', 'priceGp20InsuranceUsd',
					'prevPriceGp20Aud', 'prevPriceGp20Rmb', 'prevPriceGp20Usd' ,'prevPriceGp20InsuranceAud', 'prevPriceGp20InsuranceRmb', 'prevPriceGp20InsuranceUsd',
					'priceGp40Aud','priceGp40Rmb','priceGp40Usd', 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd',
					'prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd', 'prevPriceGp40InsuranceAud','prevPriceGp40InsuranceRmb','prevPriceGp40InsuranceUsd',
					'priceHq40Aud','priceHq40Rmb','priceHq40Usd', 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd',
					'prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd', 'prevPriceHq40InsuranceAud','prevPriceHq40InsuranceRmb','prevPriceHq40InsuranceUsd',
					'priceLclAud','priceLclRmb','priceLclUsd', 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',
					'prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd', 'prevPriceLclInsuranceAud','prevPriceLclInsuranceRmb','prevPriceLclInsuranceUsd',
					'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd',
					'gp20Qty','gp40Qty','hq40Qty','lclCbm'
				],
				columns: [
					{ header: 'ID', dataIndex: 'id', width: 180, hidden: true },
					{ header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 100, locked:true,
						renderer: function(value){
							var $origin = _dict.origin;
							if($origin.length>0 && $origin[0].data.options.length>0){
								return $dictRenderOutputColor(value, $origin[0].data.options, []);
							}
						}
					},
					//汇率
                    { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd',null, {gridId: conf.mainTabPanelId + '-2'})
                    },

					{ header: _lang.FlowServiceInquiry.fSailingDays,  dataIndex: 'sailingDays', width: 80, },
					{ header: _lang.FlowServiceInquiry.fFrequency,dataIndex: 'frequency', width: 80,},
					{ header: _lang.FlowServiceInquiry.fPriceGp20,
						columns: [
							{ header: _lang.FlowServiceInquiry.fPriceBaseOcean,
								columns: new $groupPriceColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd', null, {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceInsurance,
								columns: new $groupPriceColumns(this, 'priceGp20InsuranceAud','priceGp20InsuranceRmb','priceGp20InsuranceUsd',null, {edit:false, gridId: conf.subGridPanelId})
							},
							{ header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty', align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')
							}
						]
					},
					{ header: _lang.FlowServiceInquiry.fPriceGp40,
						columns: [
							{ header: _lang.FlowServiceInquiry.fPriceBaseOcean,
								columns: new $groupPriceColumns(this,  'priceGp40Aud','priceGp40Rmb','priceGp40Usd',  null, {edit:false,  gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceInsurance,
								columns: new $groupPriceColumns(this, 'priceGp40InsuranceAud','priceGp40InsuranceRmb','priceGp40InsuranceUsd', null, {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty',align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')
							}
						]
					},
					{ header: _lang.FlowServiceInquiry.fPriceHq40,
						columns: [
							{ header: _lang.FlowServiceInquiry.fPriceBaseOcean,
								columns: new $groupPriceColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd', null, {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceInsurance,
								columns: new $groupPriceColumns(this, 'priceHq40InsuranceAud','priceHq40InsuranceRmb','priceHq40InsuranceUsd', null , {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty',align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')
							}
						]
					},
					{ header: _lang.FlowServiceInquiry.fPriceLcl,
						columns: [
							{ header: _lang.FlowServiceInquiry.fPriceBaseOcean,
								columns: new $groupPriceColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd', null, {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceInsurance,
								columns: new $groupPriceColumns(this, 'priceLclInsuranceAud','priceLclInsuranceRmb','priceLclInsuranceUsd',null , {edit:false, gridId: conf.mainTabPanelId + '-2'})
							},
							{ header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0.00')
							}
						]
					},
					// { header: _lang.FlowServiceInquiry.fPriceTotal, columns:[
					// 	{ header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', null, {gridId: conf.subGridPanelId, edit:false})},
					// 	{ header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'rowTotalQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
					// ]},
				]// end of columns
			});

        this.destinationChargesGridPanel = new HP.GridPanel({
            id: conf.mainTabPanelId + '-3',
            title: _lang.FlowServiceInquiry.fDestinationCharges,
            forceFit: false,
            scope: this,
            width: 'auto',
            height: conf.defHeight-3,
            url: '',
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            displayAllPrice: true,
            fields: [
                'id', 'itemId','itemCnName','itemEnName','unitId','unitCnName','unitEnName',
                'priceGp20Aud', 'priceGp20Rmb', 'priceGp20Usd' ,'prevPriceGp20Aud', 'prevPriceGp20Rmb', 'prevPriceGp20Usd' ,
                'priceGp40Aud','priceGp40Rmb','priceGp40Usd','prevPriceGp40Aud','prevPriceGp40Rmb','prevPriceGp40Usd',
                'priceHq40Aud','priceHq40Rmb','priceHq40Usd','prevPriceHq40Aud','prevPriceHq40Rmb','prevPriceHq40Usd',
                'priceLclAud','priceLclRmb','priceLclUsd','prevPriceLclAud','prevPriceLclRmb','prevPriceLclUsd',
                'rateAudToRmb','rateAudToUsd','prevRateAudToRmb','prevRateAudToUsd',
                'gp20Qty','gp40Qty','hq40Qty','lclCbm'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fChargeItem, dataIndex: 'itemId', width: 100, locked:true,
                    renderer: function(value){
                        var $items = _dict.chargeItem;
                        if($items.length>0 && $items[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $items[0].data.options,[]);
                        }
                    }
                },
                { header: _lang.FlowServiceInquiry.fUnit, dataIndex: 'unitId', width: 100, locked:true,
                    renderer: function(value){
                        var $items = _dict.chargeUnit;
                        if($items.length>0 && $items[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $items[0].data.options,[]);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd',null, {gridId: conf.mainTabPanelId + '-3'})
                },

                //价格
                { header: _lang.FlowServiceInquiry.fPriceGp20, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: $groupPriceColumns(this, 'priceGp20Aud','priceGp20Rmb','priceGp20Usd', null , {edit:false, gridId: conf.mainTabPanelId + '-3'})
                    },
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp20Qty',align: 'right', width: 60, renderer: Ext.util.Format.numberRenderer('0')}
                ]
                },
                { header: _lang.FlowServiceInquiry.fPriceGp40, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this,  'priceGp40Aud','priceGp40Rmb','priceGp40Usd',null , {edit:false, gridId: conf.mainTabPanelId + '-3'})
                    },
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'gp40Qty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},

                { header: _lang.FlowServiceInquiry.fPriceHq40, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this, 'priceHq40Aud','priceHq40Rmb','priceHq40Usd',null, {gridId: conf.mainTabPanelId + '-3'})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'hq40Qty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
                { header: _lang.FlowServiceInquiry.fPriceLcl, columns: [
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this, 'priceLclAud','priceLclRmb','priceLclUsd',null, {edit:false, gridId: conf.mainTabPanelId + '-3'})},
                    { header: _lang.FlowServiceInquiry.fPriceCbm, dataIndex: 'lclCbm',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0.00')}
                ]},
                // { header: _lang.FlowServiceInquiry.fPriceTotal,
                //     columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', null, {gridId: conf.mainTabPanelId + '-3', edit:false})
                // },
            ]// end of columns
        });
        this.attachmentsGridPanel = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.freightChargesGridPanel);
        viewTabs.add(this.destinationChargesGridPanel);
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

        var freightChargesList = Ext.getCmp(conf.mainTabPanelId + '-2');
        freightChargesList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                if (json.data.ports.length > 0) {
                    var gridStore = freightChargesList.getStore();
                    var ports = json.data.ports;
                    gridStore.removeAll();
                    for(var index in ports){
                        gridStore.add(ports[index] || {});
                    }
                }

                if (json.data.chargeItems.length > 0) {
                    var destinationChargesGridPanel = Ext.getCmp(conf.mainTabPanelId + '-3');
                    var gridStore = destinationChargesGridPanel.getStore();
                    var chargeItems = json.data.chargeItems;
                    gridStore.removeAll();
                    for(var index in chargeItems){
                        gridStore.add(chargeItems[index] || {});
                    }
                }

                //attachment init
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
		}
	},

	editRow : function(conf){
		App.clickTopTab('FlowServiceInquiryForm', conf);
	}
});