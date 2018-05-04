ProductCombinationView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ProductCombination.mTitle,
            moduleName: 'ProductCombination',
			frameId : 'ProductCombinationView',
            mainGridPanelId : 'ProductCombinationFormGridGridPanelID',
            mainFormPanelId : 'ProductCombinationFormGridFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'ProductCombinationFormGridSearchFormPanelID',
            mainTabPanelId: 'ProductCombinationFormGridMainTbsPanelID',
            subGridPanelId : 'ProductCombinationFormGridSubGridPanelID',
            formGridPanelId : 'ProductCombinationFormGridPanelID',
			urlList: __ctxPath + 'archives/productCombined/list',
			urlSave: __ctxPath + 'archives/productCombined/save',
			urlDelete: __ctxPath + 'archives/productCombined/delete',
			urlGet: __ctxPath + 'archives/productCombined/get',
            refresh: true,
            edit: true,
            add: true,
            copy: true,
            del: true,
            editFun: this.editRow
		};
		
		this.initUIComponents(conf);
		ProductCombinationView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel ,this.centerPanel]
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
			    {field:'departmentName', xtype:'DepDialog', title:_lang.ProductCombination.fDepartmentId, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	 store: [['', _lang.TText.vAll],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled],['0', _lang.TText.vDraft]]
			    },
                {field:'Q-comboType-N-EQ', xtype:'combo', title:_lang.ProductCombination.fComboType, value:'',
                    store: [['', _lang.TText.vAll],  ['1', _lang.ProductCombination.vCombo], ['2',  _lang.ProductCombination.vVariation],['3',  _lang.ProductCombination.vParent]]
                },
			    {field:'Q-combinedSku-S-LK', xtype:'textfield', title:_lang.ProductCombination.fCombinedSku},
			    {field:'Q-combinedName-S-LK', xtype:'textfield', title:_lang.ProductCombination.fCombinedName},
			    {field:'Q-ean-S-LK', xtype:'textfield', title:_lang.ProductDocument.fEan},
			    {field:'Q-barcode-S-LK', xtype:'textfield', title:_lang.ProductDocument.fBarcode},
			    //{field: 'Q-createdAt-S-EQ', xtype: 'datetimefield', fieldLabel: _lang.ProductCombination.fCreatedAt, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.ProductCombination.mTitle,
			//collapsible: true,
			//split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','combinedSku','combinedName', 'comboType', 'ean','barcode', 'productId','priceAud','priceRmb','priceUsd',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flagSyncStatus','flagSyncDate'
            ],
			width: 'auto',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.ProductCombination.fCombinedSku, dataIndex: 'combinedSku', width: 200},
				{ header: _lang.ProductCombination.fCombinedName, dataIndex: 'combinedName', width: 200 },
				{ header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 150,},
                { header:  _lang.ProductCombination.fComboType, dataIndex: 'comboType', width: 100,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.ProductCombination.vCombo);
                        if(value == '2') return $renderOutputColor('blue', _lang.ProductCombination.vVariation);
                        if(value == '3') return $renderOutputColor('red', _lang.ProductCombination.vParent);
                    }
                },
                { header: _lang.ProductDocument.fTargetBin,
                 columns: [
                    { header: _lang.TText.fAUD, dataIndex: 'priceAud', sortable: true, width: 68, align: 'right', },
                    { header: _lang.TText.fRMB, dataIndex: 'priceRmb', sortable: true, width: 68, align: 'right',},
                    { header: _lang.TText.fUSD, dataIndex: 'priceUsd', sortable: true, width: 68, align: 'right', },
                 ]
                },
                { header:  _lang.ProductDocument.fIsSync, dataIndex: 'flagSyncStatus', width: 50,
                    renderer: function(value){
                        var $sync = _dict.sync;
                        if($sync.length>0 && $sync[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $sync[0].data.options);
                        }
                    }
                },
                { header:_lang.ProductDocument.fIsSyncDate, dataIndex: 'flagSyncDate', width: 140},

                // { header: _lang.ProductDocument.fTargetBin,
                //     columns: new $groupPriceColumns(scope, 'prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd', null, {edit:false})
                // },

			],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),

			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});

	    this.ProductCombinationViewTabsPanel = new App.ProductCombinationViewTabs({
            region: 'south',
            //collapsible: true,
            split: true,
            mainTabPanelId: conf.mainTabPanelId,
    		mainGridPanelId: conf.mainGridPanelId,
            gridPanelId: conf.subProductGridPanelId,
            ProductCombinationGridPanelId: conf.ProductCombinationList,
            items: [],
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.ProductCombinationViewTabsPanel ]
        });
	},// end of the init

	 rowClick: function(record, item, index, e, conf) {
         if(record.data.id) {
             Ext.getCmp(conf.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);
             var productList = Ext.getCmp(conf.mainTabPanelId + '-0');
             productList.loadData({
                 url : conf.urlGet + '?id=' + record.data.id + '&product=1',
                 loadMask:true, maskTo:conf.mainTabPanelId, success:function(response){
                     var json = Ext.JSON.decode(response.responseText);
                     //sub product list init
                     productList.getStore().removeAll();
                     if(json.data.details != undefined && json.data.details.length>0){
                         for(index in json.data.details){
                             var product = {};
                             Ext.applyIf(product, json.data.details[index]);
                             Ext.apply(product, json.data.details[index].base);
                             productList.getStore().add(product);
                         }
                     }
                 }
             });
         }
	},

    editRow : function(conf){
        new ProductCombinationForm(conf).show();
    }

});