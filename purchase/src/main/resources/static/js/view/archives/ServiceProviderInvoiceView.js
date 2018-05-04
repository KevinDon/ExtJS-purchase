ServiceProviderInvoiceView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ServiceProviderInvoice.mTitle,
			moduleName: 'ServiceInvoice',
			frameId : 'ServiceProviderInvoiceView',
			mainGridPanelId : 'ServiceProviderInvoiceGridPanelID',
			mainFormPanelId : 'ServiceProviderInvoiceFormPanelID',
			searchFormPanelId: 'ServiceProviderInvoiceSubFormPanelID',
			mainTabPanelId: 'ServiceProviderInvoiceTabsPanelId',
			subGridPanelId : 'ServiceProviderInvoiceSubGridPanelID',
			urlList: __ctxPath + 'archives/service_invoice/list',
			urlSave: __ctxPath + 'archives/service_invoice/save',
			urlDelete: __ctxPath + 'archives/service_invoice/delete',
			urlGet: __ctxPath + 'archives/service_invoice/get',
			refresh: true,
			edit: true,
			add: true,
			copy: true,
			del: true,
			editFun:this.editRow
		};
		
		this.initUIComponents(conf);
		ServiceProviderInvoiceView.superclass.constructor.call(this, {
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
			    {field:'Q-categoryId-S-EQ', xtype:'hidden'},
			    {field:'categoryName', xtype:'ServiceProviderCategoryDialog', title:_lang.ServiceProviderDocument.fCategoryName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
			    },
			    {field:'Q-source-N-EQ', xtype:'dictcombo', value:'', title:_lang.ServiceProviderDocument.fSource, code:'vendor', codeSub:'source', addAll:true},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus,value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-website-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fWebsite},
			    {field:'Q-abn-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fAbn},
			    {field:'Q-director-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fDirector},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
			    {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fcreatorEnName},
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-address-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fAddress},
			    ]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.ServiceProviderDocument.tabListTitle,
			//collapsible: true,
			//split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
                'id','categoryName','cnName','enName', 'buyerName','director',
				'address','abn','website','rating','files','creatorId','creatorCnName','creatorEnName',
				'source', 'currency', 'paymentProvision','departmentId','departmentCnName','departmentEnName',
				'status', 'createdAt','updatedAt'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ServiceProviderInvoice.fServiceProvider, dataIndex: 'serviceProviderName', width: 200},
                { header: _lang.ServiceProviderInvoice.fInvoiceDate, dataIndex: 'invoiceDate', width: 140},
                { header: _lang.ServiceProviderInvoice.fTotal, dataIndex: 'total', width: 60},

			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
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
                'gp20Qty','gp40Qty','hq40Qty','lclQty'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 120, locked:true,
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

                { header: _lang.FlowServiceInquiry.fSailingDays,  dataIndex: 'sailingDays', width: 100, },
                { header: _lang.FlowServiceInquiry.fFrequency,dataIndex: 'frequency', width: 100,},
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
                        { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'lclQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')
                        }
                    ]
                },
                { header: _lang.FlowServiceInquiry.fPriceTotal, columns:[
                    { header: _lang.FlowServiceInquiry.fPrice, columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', null, {gridId: conf.subGridPanelId, edit:false})},
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'rowTotalQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
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
                'gp20Qty','gp40Qty','hq40Qty','lclQty'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.FlowServiceInquiry.fChargeItem, dataIndex: 'itemId', width: 120, locked:true,
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
                    { header: _lang.FlowServiceInquiry.fPriceQty, dataIndex: 'lclQty',align: 'right', width: 60,renderer: Ext.util.Format.numberRenderer('0')}
                ]},
                { header: _lang.FlowServiceInquiry.fPriceTotal,
                    columns: new $groupPriceColumns(this, 'priceTotalAud','priceTotalRmb','priceTotalUsd', null, {gridId: conf.mainTabPanelId + '-3', edit:false})
                },
            ]// end of columns
        });

        var viewTabs = new Ext.tab.Panel({
            region: 'south',
            mainTabPanelId: conf.mainTabPanelId,
            split: true,
            height: '40%',
            items: []
        });

        // var viewTabs = Ext.create('widget.FlowViewTabs', conf);
        viewTabs.add(this.freightChargesGridPanel);
        viewTabs.add(this.destinationChargesGridPanel);

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
		var list = Ext.getCmp(conf.mainTabPanelId + '-0');
        list.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,maskTo:conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                //contacts
                var contactsList = Ext.getCmp(conf.mainTabPanelId + '-0');
                contactsList.getStore().removeAll();
                if(!!json.data.contacts){
                    contactsList.getStore().add(json.data.contacts);
                }

                //files
                var attachmentList = Ext.getCmp(conf.mainTabPanelId + '-1');
                attachmentList.getStore().removeAll();
                if(json.data.attachments != undefined && json.data.attachments.length>0){
                    for(index in json.data.attachments){
                        var attachments = {};
                        attachments = json.data.attachments[index];
                        Ext.applyIf(attachments, json.data.attachments[index].document);
                        attachments.id= json.data.attachments[index].documentId;
                        attachmentList.getStore().add(attachments);
                    }
                }

                //origin ports
                var portsList = Ext.getCmp(conf.mainTabPanelId + '-2');
                portsList.getStore().removeAll();
                if(!!json.data.ports){
                    portsList.getStore().add(json.data.ports);
                }

                //service charge details
                var chargeItemList = Ext.getCmp(conf.mainTabPanelId + '-3');
                chargeItemList.getStore().removeAll();
                if(!!json.data.chargeItems){
                    chargeItemList.getStore().add(json.data.chargeItems);
                }

                //orders
                var orderList = Ext.getCmp(conf.mainTabPanelId + '-4');
                orderList.getStore().removeAll();
                if(!!json.data.orders){
                    orderList.getStore().add(json.data.orders);
                }

                //payment terms
                var paymentProvision = Ext.getCmp(conf.mainTabPanelId + '-5');
                paymentProvision.update(json.data.paymentProvision || '-');
            }
        });
        
	},	
	editRow : function(conf){
		// new ServiceProviderDocumentForm(conf).show();
        App.clickTopTab('ServiceProviderInvoiceForm', conf);
	}
});