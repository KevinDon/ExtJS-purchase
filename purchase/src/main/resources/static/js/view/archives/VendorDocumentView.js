VendorDocumentView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.VendorDocument.mTitle,
			moduleName: 'Vendor',
			frameId : 'VendorDocumentView',
			mainGridPanelId : 'VendorDocumentGridPanelID',
			mainFormPanelId : 'VendorDocumentFormPanelID',
			searchFormPanelId: 'VendorDocumentSubFormPanlID',
			mainTabPanelId: 'VendorDocumentTabsPanelID',
			subGridPanelId : 'VendorDocumentSubGridPanelID',
			urlList: __ctxPath + 'archives/vendor/list',
			urlSave: __ctxPath + 'archives/vendor/save',
			urlDelete: __ctxPath + 'archives/vendor/delete',
			urlGet: __ctxPath + 'archives/vendor/get',
			refresh: true,
			edit: true,
			add: true,
			copy: true,
			del: true,
			editFun:this.editRow
		};
		
		this.initUIComponents(conf);
		VendorDocumentView.superclass.constructor.call(this, {
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
			    {field:'categoryName', xtype:'VendorCategoryDialog', title:_lang.VendorDocument.fCategoryName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-categoryId-S-EQ', single: true
			    },
                {field:'Q-id-S-LK', xtype:'textfield', title:_lang.VendorDocument.fSystemId, },
                {field:'Q-code-S-LK', xtype:'textfield', title:_lang.VendorDocument.fVendorCode},

                {field:'Q-source-N-EQ', xtype:'dictcombo', value:'', title:_lang.VendorDocument.fSource, code:'vendor', codeSub:'source', addAll:true},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus,value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-director-S-LK', xtype:'textfield', title:_lang.VendorDocument.fDirector},
			    {field:'Q-website-S-LK', xtype:'textfield', title:_lang.VendorDocument.fWebsite},
			    {field:'Q-abn-S-LK', xtype:'textfield', title:_lang.VendorDocument.fAbn},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
			    {field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyCnName},
			    {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.TText.fCompanyEnName},
			    {field:'Q-address-S-LK', xtype:'textfield', title:_lang.VendorDocument.fAddress},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.VendorDocument.tabListTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','categoryName','cnName','enName', 'buyerName','director','code',
				'address','abn','website','rating','files',
				'source', 'currency', 'orderSerialNumber', 'paymentProvision',
				'dynContent', 'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName',
				'departmentEnName','departmentId','updatedAt',
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true},
				{ header: _lang.VendorDocument.fCategoryName, dataIndex: 'categoryName', width: 120,sortable:false},
				{ header: _lang.VendorDocument.fVendorCode, dataIndex: 'code', width: 120,sortable:false},
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
				{ header: _lang.VendorDocument.fCnName, dataIndex: 'cnName', width: 260 },
				{ header: _lang.VendorDocument.fEnName, dataIndex: 'enName', width: 260 },
			    /*{ header: _lang.TText.fBuyerCnName, dataIndex: 'buyerCnName', width: 100,hidden:curUserInfo.lang =='zh_CN'? false: true },
			    { header: _lang.TText.fBuyerEnName, dataIndex: 'buyerEnName', width: 100,hidden:curUserInfo.lang !='zh_CN'? false: true},*/
			    { header: _lang.VendorDocument.fDirector, dataIndex: 'director', width: 150},
			    { header: _lang.VendorDocument.fAddress, dataIndex: 'address', width: 390 },
			    { header: _lang.VendorDocument.fAbn, dataIndex: 'abn', width: 200 },
			    { header: _lang.VendorDocument.fWebsite, dataIndex: 'website', width:200 },
			    { header: _lang.VendorDocument.fSource, dataIndex: 'source', width: 80 ,
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

		this.VendorDocumentViewTabsPanel = new App.VendorDocumentViewTabs({
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
			items: [ this.gridPanel, this.VendorDocumentViewTabsPanel]
		});
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
		var productList = Ext.getCmp(conf.mainTabPanelId + '-0');
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id + '&product=1',
            loadMask:true, maskTo:conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                //sub product list init
                productList.getStore().removeAll();
	            if(!!json.data && !!json.data.products && json.data.products.length>0){
	                for(index in json.data.products){
	                    var product = {};
	                    Ext.applyIf(product, json.data.products[index]);
	                    Ext.apply(product, json.data.products[index].base);
	                    productList.getStore().add(product);
	                }
	            }
	            
	            //contacts init
	            var contactsList = Ext.getCmp(conf.mainTabPanelId + '-1');
	            contactsList.getStore().removeAll();
				if(!!json.data && !!json.data.contacts && json.data.contacts.length>0){
					for(index in json.data.contacts){
						var contacts = {};
						contacts = json.data.contacts[index];
						Ext.applyIf(contacts, json.data.contacts[index].document);
						contacts.id= json.data.contacts[index].documentId;
						contactsList.getStore().add(contacts);
					}
				}
				
				//attachment init
                Ext.getCmp(conf.mainTabPanelId).items.items[2].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

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

                //Orders
                var ordersList = Ext.getCmp(conf.mainTabPanelId + '-5');
                ordersList.getStore().removeAll();
                if(!!json.data && !!json.data.orders &&  json.data.orders.length>0){
                    for(index in json.data.orders){
                        ordersList.getStore().add( json.data.orders[index]);
                    }
                }

                //PaymentTerms
				var paymentProvision = Ext.getCmp(conf.mainTabPanelId + '-3');
				paymentProvision.update(!!json.data && !!json.data.paymentProvision  ? json.data.paymentProvision : '-');

                //相关报告
                Ext.getCmp(conf.mainTabPanelId).items.items[6].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');

				//archivesHistory
                if(record.data.id) {
                    Ext.getCmp(conf.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);
                }
            }
        });
        
	},
	editRow : function(conf){
		new VendorDocumentForm(conf).show();
	},

});