ReportSupplierInvestigationView = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ReportSupplierInvestigation.mTitle,
            moduleName: 'ReportSupplierInvestigation',
            frameId : 'ReportSupplierInvestigationView',
            mainGridPanelId : 'ReportSupplierInvestigationGridPanelID',
            mainFormPanelId : 'ReportSupplierInvestigationFormPanelID',
            mainViewPanelId : 'ReportSupplierInvestigationViewPanelID',
            searchFormPanelId: 'ReportSupplierInvestigationSearchPanelID',
            mainTabPanelId: 'ReportSupplierInvestigationTbsPanelId',
            urlList: __ctxPath + 'reportsupplierinvestigation/list',
            urlSave: __ctxPath + 'reportsupplierinvestigation/save',
            urlDelete: __ctxPath + 'reportsupplierinvestigation/delete',
            urlGet: __ctxPath + 'reportsupplierinvestigation/get',
            refresh: true,
            add: true,
            copy: true,
            edit: true,
            del: true,
            editFun: this.editRow
        };

        this.initUIComponents(conf);
        ReportSupplierInvestigationView.superclass.constructor.call(this, {
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
            	{field:'Q-title-S-LK', xtype:'textfield', title:_lang.Reports.fTitle},
            	  {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
			    },
			    {field:'Q-serialNumber-S-LK', xtype:'textfield', title:_lang.Reports.fSerialNumber},
            	{field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
				{field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
            	{field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-reportTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Reports.fReportTime, format: curUserInfo.dateFormat},
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			  
			  
            ]
        });// end of searchPanel

        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'west',
            id: conf.mainGridPanelId,
            title: _lang.ReportSupplierInvestigation.tabListTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [ 'id','title','vendorId','vendorCnName','vendorEnName','vendorId','businessId','serialNumber', 'reportTime',
                'status','confirmedAt','createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
            ],
            width: '40%',
            minWidth: 350,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 120, hidden: true },
                { header: _lang.Reports.fTitle, dataIndex: 'title', width: 200},
                { header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 140},
                { header: _lang.Reports.fSerialNumber, dataIndex: 'serialNumber', width: 120},
                { header: _lang.VendorDocument.fVendor, dataIndex: 'vendorCnName', width: 260, hidden: curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.VendorDocument.fVendor, dataIndex: 'vendorEnName', width: 260, hidden: curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.VendorDocument.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true },
                { header: _lang.Reports.fBusinessId, dataIndex: 'businessId', width: 180},
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
                ]},

                //供应商基础信息
                { xtype: 'container',cls:'row', readOnly:true, items: $groupFormVendorFields(this, conf, {
                    mainFormPanelId: conf.mainViewPanelId, readOnly:true, allowBlank: true, cls:'col-1'
                })},

                { xtype: 'container', cls:'row',  items:[
                    { xtype: 'FilesPreviewView', frameId: conf.mainViewPanelId, field: 'main.reportFile', cls:'col-1', width: '100%'},
                ]},

                { xtype: 'section', title:_lang.TText.fRemark},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.remark', xtype: 'displayfield', cls:'col-1'},
                ]},

                //相关图片
                { xtype: 'section', title:_lang.VendorDocument.tabImages},
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
                json.data = json.data || {};

                // init vendor
                Ext.getCmp(conf.mainViewPanelId + '-vendor').show();

                //report file
                Ext.getCmp(conf.mainViewPanelId+ '-view').setValue(json.data.reportFile);

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

            }
        });
    },

    editRow: function(conf){
        new ReportSupplierInvestigationForm(conf).show();
    }
});