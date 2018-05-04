FlowOrderQualityInspectionView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowOrderQualityInspection.mTitle,
			moduleName: 'FlowOrderQualityInspection',
			winId : 'FlowOrderQualityInspectionForm',
			frameId : 'FlowOrderQualityInspectionView',
			mainGridPanelId : 'FlowOrderQualityInspectionGridPanelID',
			mainFormPanelId : 'FlowOrderQualityInspectionFormPanelID',
			processFormPanelId: 'FlowOrderQualityInspectionProcessFormPanelID',
			searchFormPanelId: 'FlowOrderQualityInspectionSearchFormPanelID',
			mainTabPanelId: 'FlowOrderQualityInspectionMainTbsPanelID',
            subOrderGridPanelId : 'FlowOrderQualityInspectionSubOrderGridPanelID',
			formGridPanelId : 'FlowOrderQualityInspectionFormGridPanelID',

			urlList: __ctxPath + 'flow/inspection/flowOrderQC/list',
			urlSave: __ctxPath + 'flow/inspection/flowOrderQC/save',
			urlDelete: __ctxPath + 'flow/inspection/flowOrderQC/delete',
			urlGet: __ctxPath + 'flow/inspection/flowOrderQC/get',
			urlExport: __ctxPath + 'flow/inspection/flowOrderQC/export',
			urlListProduct: __ctxPath + 'flow/inspection/flowOrderQC/listproduct',
			urlFlow: __ctxPath + 'flow/inspection/flowOrderQC/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowOrderQc&processInstanceId=',
			refresh: true,
			add: true,
			// copy: true,
			edit: true,
			del: true,
			flowMine:true,
			flowInvolved:true,
			export:true,
			editFun: this.editRow,
		};

		this.initUIComponents(conf);
		FlowOrderQualityInspectionView.superclass.constructor.call(this, {
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
			 	{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowOrderQualityInspection.fOrderNumber, },
			 	{field:'Q-orderTitle-S-LK', xtype:'textfield', title:_lang.FlowOrderQualityInspection.fOrderTitle, },
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['3', _lang.TText.vDeleted]]
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
			title: _lang.FlowOrderQualityInspection.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
            fields: [
                'id','vendorCnName','vendorEnName','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName','orderNumber',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold','orderNumber','orderTitle',
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 80, hidden: true },
                { header:_lang.ProductDocument.fBusinessId, dataIndex: 'id', width: 180,  },
                { header: _lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 80,  },
                { header: _lang.FlowOrderQualityInspection.fOrderTitle, dataIndex: 'orderTitle', width: 80,  },
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
            ],// end of columns
			appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
		});

        this.orderGridPanel = new HP.GridPanel({
            id: conf.subOrderGridPanelId,
            title: _lang.ProductDocument.tabOrderListTitle,
            forceFit: false,
            autoLoad: false,
            bbar: false,
            rsort: false,
            // url: '',
            fields: [
				'id','orderNumber','orderTitle','currency','totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb','rateAudToUsd',
				'depositAud','depositRmb','depositUsd'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header:_lang.ProductDocument.fBusinessId, dataIndex: 'orderNumber', width:200, },
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },

                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},

                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },

            ],// end of columns
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
        viewTabs.add(this.orderGridPanel);
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

		var productList = Ext.getCmp(conf.subOrderGridPanelId);
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,
            maskTo: conf.mainTabPanelId,
			success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
	            if(json.data.details.length>0){
	                for(index in json.data.details){
	                    var order = {};
	                    Ext.applyIf(order, json.data.details[index]);
	                    Ext.apply(order, json.data.details[index].order);
	                    productList.getStore().add(order);
	                }
	            }

                //相关报告
                Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');

                //附件
                Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.reports ? json.data.reports: '');
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
		App.clickTopTab('FlowOrderQualityInspectionForm', conf);
	}
});