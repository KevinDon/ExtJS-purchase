ReportOrderInspectionView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.ReportOrderInspection.mTitle,
			moduleName: 'ReportOrderInspection',
			frameId : 'ReportOrderInspectionView',
			mainGridPanelId : 'ReportOrderInspectionGridPanelID',
			mainFormPanelId : 'ReportOrderInspectionFormPanelID',
			mainViewPanelId : 'ReportOrderInspectionViewPanelID',
			searchFormPanelId: 'ReportOrderInspectionSearchPanelID',
			mainTabPanelId: 'ReportOrderInspectionTbsPanelId'  + Ext.id() ,
			urlList: __ctxPath + 'reportorderinspection/list',
			urlSave: __ctxPath + 'reportorderinspection/save',
			urlDelete: __ctxPath + 'reportorderinspection/delete',
			urlGet: __ctxPath + 'reportorderinspection/get',
			refresh: true,
			add: true,
			copy: true,
			edit: true,
			del: true,
            editFun: this.editRow
		};
		
		this.initUIComponents(conf);
		ReportOrderInspectionView.superclass.constructor.call(this, {
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
            fieldItems: [
                {field: 'Q-title-S-LK', xtype: 'textfield', title: _lang.Reports.fTitle},
                {field: 'Q-status-N-EQ', xtype: 'combo', title: _lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft], ['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
                },
                {field: 'Q-orderNumber-S-LK', xtype: 'textfield', title:_lang.TText.fOrderNumber},
                {field: 'Q-orderTitle-S-LK', xtype: 'textfield', title: _lang.TText.fOrderTitle},
                {field: 'Q-serialNumber-S-LK', xtype: 'textfield', title: _lang.Reports.fSerialNumber},
                {field: 'Q-vendorCnName-S-LK', xtype: 'textfield', title: _lang.TText.fVendorCnName},
                {field: 'Q-vendorEnName-S-LK', xtype: 'textfield', title: _lang.TText.fVendorEnName},
                {field: 'Q-creatorCnName-S-LK', xtype: 'textfield', title: _lang.TText.fCreatorCnName},
                {field: 'Q-creatorEnName-S-LK', xtype: 'textfield', title: _lang.TText.fCreatorEnName},
                {field: 'Q-departmentId-S-EQ', xtype: 'hidden'},
                {field: 'departmentName', xtype: 'DepDialog', title: _lang.TText.fAppDepartmentName,
                    formId: conf.searchFormPanelId, hiddenName: 'Q-departmentId-S-EQ', single: true
                },
            	{ field: 'Q-reportTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Reports.fReportTime, format: curUserInfo.dateFormat},
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.ReportOrderInspection.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','title','vendorId','vendorCnName','vendorEnName','vendorId','businessId','serialNumber', 'reportTime','confirmedEnName','confirmedCnName','confirmedId',
	          'status','confirmedAt','createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName',
                'orderNumber','orderTitle'
			],
			width: '40%',
			minWidth: 350,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 120, hidden: true },
				{ header: _lang.Reports.fTitle, dataIndex: 'title', width: 120},
				{ header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 140},
				{ header: _lang.Reports.fSerialNumber, dataIndex: 'serialNumber', width: 120},
				{ header: _lang.TText.fOrderNumber, dataIndex: 'orderNumber', width: 100},
				{ header: _lang.TText.fOrderTitle, dataIndex: 'orderTitle', width: 120},
				{ header: _lang.VendorDocument.fVendor, dataIndex: 'vendorCnName', width: 260, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.VendorDocument.fVendor, dataIndex: 'vendorEnName', width: 260, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.VendorDocument.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true },
			    { header: _lang.Reports.fBusinessId, dataIndex: 'businessId', width: 200},
			    { header: _lang.Reports.fConfirmedAt, dataIndex: 'confirmedAt', width: 140 },
			    { header: _lang.Reports.fConfirmed, dataIndex: 'confirmedCnName', width: 100, hidden: curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Reports.fConfirmed, dataIndex: 'confirmedEnName', width: 120, hidden: curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Reports.fConfirmedId, dataIndex: 'confirmedId', width: 80, hidden: true },
			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});

		this.viewPanel = new HP.FormPanel({
			id: conf.mainViewPanelId,
			title: _lang.Reports.tabFormTitle,
			region: 'center',
//			scope: this,
			fieldItems: [

			    { xtype: 'section', title:_lang.VendorDocument.tabInitialValue},
			    { xtype: 'container', cls:'row',  items:[
                    {field: 'main.title', xtype: 'displayfield', fieldLabel: _lang.Reports.fTitle, cls:'col-1'},

					{field: 'main.reportTime', xtype:'displayfield', fieldLabel:_lang.Reports.fReportTime, cls:'col-2'},
                    {field: 'main.serialNumber', xtype: 'displayfield', fieldLabel: _lang.Reports.fSerialNumber, cls:'col-2'},

				    {field: curUserInfo.lang =='zh_CN'? 'main.creatorCnName': 'main.creatorEnName', xtype: 'displayfield', fieldLabel: _lang.Reports.fCreator, cls:'col-2'},
                    {field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2'},

                    {field: curUserInfo.lang =='zh_CN'? 'main.confirmedCnName': 'main.confirmedEnName', xtype:'displayfield', fieldLabel:_lang.Reports.fConfirmed, cls:'col-2'},
                    {field: 'main.confirmedAt', xtype: 'displayfield', fieldLabel: _lang.Reports.fConfirmedAt, cls:'col-2'},

                    {field: 'main.businessId', xtype: 'displayfield', fieldLabel: _lang.Reports.fBusinessId, cls:'col-2'},
                ]},

                //供应商基础信息
                { xtype: 'container',cls:'row', readOnly:true, items: $groupFormVendorFields(this, conf, {
                    mainFormPanelId: conf.mainViewPanelId, readOnly:true, allowBlank: true, cls:'col-1'
                })},

                //订单
                { xtype:'container', cls: 'row', items: $groupFormOrderFields(this, conf, {
                    callback : function(eOpts, record){
                        if(record && record.details){
                            this.meForm.getCmpByName('main.products').setValue(record.details);
                        }else{
                            this.meForm.getCmpByName('main.products').setValue('');
                        }
                    },
                    mainFormPanelId:conf.mainViewPanelId,
                    readOnly:true, cls:'col-1'
                })},

                //products
                { field: 'main.products', xtype: 'OrderProductFormMultiGrid',fieldLabel:_lang.FlowNewProduct.fProductList, height: 150, productType:5,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainViewPanelId, scope:this, readOnly: true, allowBlank: false
                },

                { xtype: 'container', cls:'row',  items:[
                    { xtype: 'FilesPreviewView', frameId: conf.mainViewPanelId, field: 'main.reportFile', cls:'col-1', width: '100%'},
                ]},

                { xtype: 'section', title:_lang.TText.fRemark},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.remark', xtype: 'displayfield', cls:'col-1'},
                ]},

                //相关图片
                { xtype: 'section', title:_lang.Reports.tabImages},
                { field: 'main.images', xtype: 'hidden'},
                { field: 'main.imagesName', xtype: 'hidden'},
                { field: 'main_images', xtype: 'ImagesDialog', fieldLabel: _lang.Reports.tabImages,
                    formId: conf.mainViewPanelId, hiddenName: 'main.images', titleName:'main.imagesName', fileDefType: 1, readOnly: true,
                },

                //创建人信息
                { xtype: 'section', title:_lang.Reports.tabCreatorInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: curUserInfo.lang =='zh_CN'? 'main.creatorCnName' : 'main.creatorEnName', xtype: 'displayfield', fieldLabel: _lang.Reports.fCreator, cls:'col-2' },
                    { field: curUserInfo.lang =='zh_CN'? 'main.departmentCnName' : 'main.departmentEnName', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.Reports.fDepartment, cls:'col-2'},
                    { field: curUserInfo.lang =='zh_CN'? 'main.confirmedCnName': 'main.confirmedEnName', xtype:'displayfield', fieldLabel:_lang.Reports.fConfirmed, cls:'col-2'},
                    { field: 'main.confirmedAt', xtype: 'displayfield', fieldLabel: _lang.Reports.fConfirmedAt, cls:'col-2'}
                ] },

                { xtype: 'container',cls:'row', items: [
                    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2'},
                    { field: 'main.status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls:'col-2',
                        renderer: function(value){
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                            if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                        }
                    }
                ], hidden: !this.isApprove },
			]
		});
		
		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.viewPanel]
		});
				
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
        this.viewPanel.form.reset();
		this.viewPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id,
            preName: 'main', loadMask:true, maskTo: conf.mainViewPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);

                //订单号及产品
                if(json.data){
                    var params = {id:json.data.orderId};
                    $postUrl({
                        url: __ctxPath + 'purchase/purchasecontract/get', maskTo: this.frameId, params: params, autoMessage:false,
                        callback: function (response, eOpts) {
                            var row = Ext.JSON.decode(response.responseText);
                            Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.products').setValue(row.data.details);

                            var cmpOrder = Ext.getCmp(conf.mainViewPanelId + '-order');
                            $_setByName(cmpOrder, row.data, {preName:'order', root:'data'});
                            cmpOrder.show();
                        }
                    });

                    Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.orderId').setValue(json.data.orderId);
                    Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.orderNumber').setValue(json.data.orderNumber);

                }else{
                    Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.orderId').setValue('');
                    Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.orderNumber').setValue('');
                    Ext.getCmp(conf.mainViewPanelId).getCmpByName('main.products').setValue('');
                }

                //report file
                var cmpReportFile = Ext.getCmp(conf.mainViewPanelId+ '-view');
                cmpReportFile.setValue(json.data.reportFile);

                //images init
                var cmpImages = Ext.getCmp(conf.mainViewPanelId).getCmpByName('main_images').dataview.getStore();
                cmpImages.removeAll();
                if(json.data.imagesDoc != undefined && json.data.imagesDoc.length>0){
                    for(index in json.data.imagesDoc){
                        var images = {};
                        Ext.applyIf(images, json.data.imagesDoc[index].document);
                        images.id= json.data.imagesDoc[index].documentId;
                        cmpImages.add(images);
                    }
                }

                // init vendor
                Ext.getCmp(conf.mainViewPanelId + '-vendor').show();
			}
		});
	},

    editRow: function(conf){
        new ReportOrderInspectionForm(conf).show();
    }
});