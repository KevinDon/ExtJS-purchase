TariffView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Duty.mTitle,
            moduleName: 'Tariff',
			frameId : 'TariffView',
            mainGridPanelId : 'TariffMainGridPanelID',
            mainFormPanelId : 'ProductCombinationFormGridFormPanelID',
            searchFormPanelId: 'TariffSearchFormPanelID',
            mainFormPanelId : 'TariffFormPanelID',
            mainTabPanelId: 'TariffMainTbsPanelID',
            subGridPanelId : 'TariffSubGridPanelID',
            formGridPanelId : 'TariffFormGridPanelID',
			urlList: __ctxPath + 'archives/tariff/list',
			urlSave: __ctxPath + 'archives/tariff/save',
			urlDelete: __ctxPath + 'archives/tariff/delete',
			urlGet: __ctxPath + 'archives/tariff/get',
            refresh: true,
            edit: true,
            add: true,
            del: true,
            editFun: this.editRow
		};
		
		this.initUIComponents(conf);
		TariffView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel ,this.centerPanel ]
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
                {field:'Q-creatorId-S-EQ', xtype:'hidden'},
                {field:'creatorName', xtype:'UserDialog', title: _lang.TText.fCreatorName,
                    formId:conf.searchFormPanelId, hiddenName:'Q-creatorId-S-EQ', single: true
                },
                {field:'Q-description-S-LK', xtype:'textfield', title:_lang.Duty.fDescription},
                {field:'Q-itemCode-S-LK', xtype:'textfield', title:_lang.Duty.fHsCode},
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                    store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft], ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                },
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			]
		});// end of searchPanel
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Duty.fDuty,
			//collapsible: true,
			//split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id','itemCode','description', 'dutyRate',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status'
            ],
			width: 'auto',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.Duty.fHsCode, dataIndex: 'itemCode', width: 200},
                { header: _lang.Duty.fRate, dataIndex: 'dutyRate', width: 80, decimalPrecision: 2, allowDecimals: true},
                { header: _lang.Duty.fDescription, dataIndex: 'description', width: 250 },
			],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),

			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
			}
		});
		
		 this.TariffViewTabsPanel = new App.TariffViewTabs({
	            region: 'south',
	            //collapsible: true,
	            split: true,
				 maxHeight:'300',
				 mainTabPanelId: conf.mainTabPanelId,
	    		mainGridPanelId: conf.mainGridPanelId,
	            gridPanelId: conf.subProductGridPanelId,
	            TariffGridPanelId: conf.TariffList,
	            items: [],
	        });

	        //布局
	        this.centerPanel = new Ext.Panel({
	            id: conf.mainGridPanelId + '-center',
	            region: 'center',
	            layout: 'border',
	            width: 'auto',
	            border: false,
	            items: [ this.gridPanel, this.TariffViewTabsPanel ]
	        });
	},// end of the init

	rowClick: function(record, item, index, e, conf) {

        if (record.data.id) {
            //console.log(conf.mainGridPanelId);
            Ext.getCmp(conf.mainGridPanelId + '-ArchivesHistoryTabGrid').setBusinessId(record.data.id);

            var productList = Ext.getCmp(conf.mainTabPanelId + '-0');
            productList.loadData({
                url: conf.urlGet + '?id=' + record.data.id + '&product=1',
                loadMask: true, maskTo: conf.mainTabPanelId, success: function (response) {
                    var json = Ext.JSON.decode(response.responseText);
                    //sub product list init
                    productList.getStore().removeAll();
                    if (json.data.products != undefined && json.data.products.length > 0) {
                        for (index in json.data.products) {
                            var product = {};
                            Ext.applyIf(product, json.data.products[index]);
                            Ext.apply(product, json.data.products[index].base);
                            productList.getStore().add(product);
                        }
                    }
                }
            })
        }
    },

    editRow : function(conf){
        new TariffForm(conf).show();
    }

});