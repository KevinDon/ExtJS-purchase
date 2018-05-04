ServiceProviderDocumentView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ServiceProviderDocument.mTitle,
			moduleName: 'Service',
			frameId : 'ServiceProviderDocumentView',
			mainGridPanelId : 'ServiceProviderDocumentGridPanelID',
			mainFormPanelId : 'ServiceProviderDocumentFormPanelID',
			searchFormPanelId: 'ServiceProviderDocumentSubFormPanelID',
			mainTabPanelId: 'ServiceProviderDocumentTabsPanelId',
			subGridPanelId : 'ServiceProviderDocumentSubGridPanelID',
			//mainFormGridPanelId : 'VendorDocumentFormCategoryGridPanelID',
			urlList: __ctxPath + 'archives/service_provider/list',
			urlSave: __ctxPath + 'archives/service_provider/save',
			urlDelete: __ctxPath + 'archives/service_provider/delete',
			urlGet: __ctxPath + 'archives/service_provider/get',
			refresh: true,
			edit: true,
			add: true,
			copy: true,
			del: true,
			editFun:this.editRow
		};
		
		this.initUIComponents(conf);
		ServiceProviderDocumentView.superclass.constructor.call(this, {
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
                {field:'Q-id-S-LK', xtype:'textfield', title:_lang.VendorDocument.fSystemId, },
                {field:'Q-code-N-EQ', xtype:'textfield', value:'', title:_lang.ServiceProviderDocument.fCode, },
			    {field:'Q-source-N-EQ', xtype:'dictcombo', value:'', title:_lang.ServiceProviderDocument.fSource, code:'vendor', codeSub:'source', addAll:true},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus,value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-website-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fWebsite},
			    {field:'Q-abn-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fAbn},
			    {field:'Q-director-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fDirector},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
			    {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-address-S-LK', xtype:'textfield', title:_lang.ServiceProviderDocument.fAddress},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
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
                'id','categoryName','cnName','enName', 'buyerName','director','code',
				'address','abn','website','rating','files','creatorId','creatorCnName','creatorEnName',
				'source', 'currency', 'paymentProvision','departmentId','departmentCnName','departmentEnName',
				'status', 'createdAt','updatedAt'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.ServiceProviderDocument.fCategoryName, dataIndex: 'categoryName', width: 120,sortable:false},
				{ header: _lang.ServiceProviderDocument.fCode, dataIndex: 'code', width: 120,sortable:false},

                { header: _lang.ServiceProviderDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.VendorDocument.fCnName, dataIndex: 'cnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.VendorDocument.fEnName, dataIndex: 'enName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.ServiceProviderDocument.fDirector, dataIndex: 'director', width: 60},
			    { header: _lang.ServiceProviderDocument.fAddress, dataIndex: 'address', width: 390 },
			    { header: _lang.ServiceProviderDocument.fAbn, dataIndex: 'abn', width: 200 },
			    { header: _lang.ServiceProviderDocument.fWebsite, dataIndex: 'website', width: 200 },
			    { header: _lang.ServiceProviderDocument.fSource, dataIndex: 'source', width: 80,
			    	 renderer: function(value){
						 var $source = _dict.source;
						 if($source.length>0 && $source[0].data.options.length>0){
							return $dictRenderOutputColor(value, $source[0].data.options);
						 }
					 }
				},

			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});

		this.ServiceProviderDocumentViewTabsPanel = new App.ServiceProviderDocumentViewTabs({
			region: 'south',
			mainTabPanelId: conf.mainTabPanelId,
			mainGridPanelId: conf.mainGridPanelId,
			split: true,
			height: '40%',
			items: []
		});

		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.ServiceProviderDocumentViewTabsPanel]
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

                //attachment init
                Ext.getCmp(conf.mainTabPanelId).items.items[1].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

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

                //相关图片
                var imagesList = Ext.getCmp(conf.mainTabPanelId + '-4');
                imagesList.getStore().removeAll();
                if(json.data.imagesDoc != undefined && json.data.imagesDoc.length>0){
                    for(index in json.data.imagesDoc){
                        var images = {};
                        images = json.data.imagesDoc[index];
                        Ext.applyIf(images, json.data.imagesDoc[index].document);
                        images.id= json.data.imagesDoc[index].documentId;
                        imagesList.getStore().add(images);
                    }
                }

              //archivesHistory
                if(record.data.id) {
                	//console.log(conf.mainGridPanelId);
                    Ext.getCmp(conf.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);
                }
            }
        });
        
	},	
	editRow : function(conf){
		new ServiceProviderDocumentForm(conf).show();
	}
});