FlowSampleQcView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowSampleQc.mTitle,
			moduleName: 'FlowSampleQc',
			winId : 'FlowSampleQcForm',
			frameId : 'FlowSampleQcView',
			mainGridPanelId : 'FlowSampleQcGridPanelID',
			mainFormPanelId : 'FlowSampleQcFormPanelID',
			processFormPanelId: 'FlowSampleQcProcessFormPanelID',
			searchFormPanelId: 'FlowSampleQcSearchFormPanelID',
			mainTabPanelId: 'FlowSampleQcMainTbsPanelID',
			subGridPanelId : 'FlowSampleQcSubGridPanelID',
			formGridPanelId : 'FlowSampleQcFormGridPanelID',

			urlList: __ctxPath + 'flow/inspection/flowsampleqc/list',
			urlSave: __ctxPath + 'flow/inspection/flowsampleqc/save',
			urlDelete: __ctxPath + 'flow/inspection/flowsampleqc/delete',
			urlGet: __ctxPath + 'flow/inspection/flowsampleqc/get',
			urlExport: __ctxPath + 'flow/inspection/flowsampleqc/export',
			urlListProduct: __ctxPath + 'flow/inspection/flowsampleqc/listproduct',
			urlFlow: __ctxPath + 'flow/inspection/flowsampleqc/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowSampleQc&processInstanceId=',
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
        FlowSampleQcView.superclass.constructor.call(this, {
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
			 	{field:'Q-sku-S-LK', xtype:'textfield', title:_lang.FlowNewProduct.fSku},
			 	{field:'Q-productName-S-LK', xtype:'textfield', title:_lang.FlowNewProduct.fProductName},
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
			title: _lang.FlowSampleQc.mTitle,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [
				'id', 'vendorId','vendorCnName','vendorEnName',
                'place','details',
                'creatorId','creatorCnName','creatorEnName','assigneeId','assigneeCnName','assigneeEnName','departmentId','departmentCnName','departmentEnName',
                'startTime','endTime', 'createdAt','updatedAt','status','flowStatus','processInstanceId','hold'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.FlowNewProduct.fId, dataIndex: 'id', width: 180  },
//                { header: _lang.FlowNewProduct.fVendorName, dataIndex: 'vendorName', width: 160},
                { header: _lang.TText.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
			],// end of column
            appendColumns: $groupGridCreatedColumnsForFlow(),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
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
        try {
            viewTabs.add(new App.FlowSampleQcFormGrid({
                noTitle: true,
                formGridPanelId: conf.formGridPanelId+ '-product',
            }));
            viewTabs.add(this.reportGridPanel);
            viewTabs.add(this.attachmentsGridPanel);
        }catch (e){console.log(e)}

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

        var productList = Ext.getCmp(conf.mainTabPanelId).items.items[2].formGridPanel;
        productList.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true, maskTo: conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);
                productList.getStore().removeAll();
                if(!!json.data ) {
                    if (!!json.data.details && json.data.details.length > 0) {
                        for (index in json.data.details) {
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            Ext.apply(product, json.data.details[index].product);
                            Ext.applyIf(product, json.data.details[index].product.prop);
                            productList.getStore().add(product);
                        }
                    }

                    //相关报告
                    Ext.getCmp(conf.mainTabPanelId).items.items[3].setValue(json.data.reports);

                    //attachment init
                    Ext.getCmp(conf.mainTabPanelId).items.items[4].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
		};

	},
	editRow : function(conf){
		App.clickTopTab('FlowSampleQcForm', conf);
	},

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
        }
    }
});