ReportProductComplianceForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.apply(this, _cfg);

        this.isReadOnly = _cfg.readOnly || this.action != 'add' && this.action != 'copy' && this.record.status>0? true: false;
        
		var conf = {
			title : _lang.ReportProductCompliance.mTitle + $getTitleSuffix(this.action),
            moduleName: 'ReportProductCompliance',
            winId : 'ReportProductComplianceFormWinID',
            frameId : 'ReportProductComplianceView',
            mainGridPanelId : 'ReportProductComplianceGridPanelID',
            mainFormPanelId : 'ReportProductComplianceFormPanelID',
            mainViewPanelId : 'ReportProductComplianceViewPanelID',
            formGridPanelId:'ReportProductComplianceFormGridPanelID',
            searchFormPanelId: 'ReportProductComplianceSearchPanelID',
            mainTabPanelId: 'ReportProductComplianceTbsPanelId'+ Ext.id(),
            mainFormGridPanelId : 'ReportProductComplianceFormGridPanelID',
            subGridPanelId:'ReportProductComplianceSubGridPanelID',
            urlSave: __ctxPath + 'reportproductcompliance/save',
            urlGet: __ctxPath + 'reportproductcompliance/get',
            refresh: true,
			actionName: this.action,
			save: !this.isReadOnly,
            close: true,
            saveFun: this.saveRow
		};
		
		this.initUIComponents(conf);
        Ext.applyIf(this, conf);
		ReportProductComplianceForm.superclass.constructor.call(this, {
			id : conf.winId,
			scope : this,
			title : conf.title,
			tbar: Ext.create("App.toolbar", conf),
			cls: 'gb-blank',
			width : '80%', height : '80%',
			items : this.editFormPanel
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.editFormPanel = new HP.FormPanel({
			id : conf.mainFormPanelId,
			region: 'center',
			closeWin: conf.winId,
            fieldItems: [
                { xtype: 'section', title:_lang.VendorDocument.tabInitialValue},
                { field: 'id',	xtype: 'hidden', value: this.action == 'add' ? '': this.record.id },
                { xtype: 'container', cls:'row',  items: [
                    { xtype: 'container', cls:'row',  items: [

                        {field: 'main.businessId', xtype: 'hidden'},
                        {field: 'main_businessId', xtype: 'FlowOtherDialog', fieldLabel: _lang.Reports.fBusinessId, hiddenName:'main.businessId',
                            cls:'col-2', allowBlank: false, frameId:conf.mainFormPanelId, single:true, readOnly: this.isReadOnly, displayType: 2,
                            subcallback: function (ids, titles, redords) {
                                //申请单选择并且初始化
                                if(this.selectedId != ids){
                                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                                    $_setByName(cmpVendor, redords.data, {preName:'main', root:'data'});
                                    this.meForm.getCmpByName('main.vendorId').setValue(redords.data.vendorId);
                                    this.meForm.getCmpByName('main.vendorName').setValue(redords.data.vendorName);
                                    cmpVendor.show();
                                    this.meForm.troubleDetails = redords.data.details;
                                    this.meForm.getCmpByName('main.products').setValue(redords.data.details);
                                    //初始化编码生成
                                    if(this.meForm.appDepId == undefined || this.meForm.appDepId != redords.data.departmentId) {
                                        this.meForm.appDepId = redords.data.departmentId;
                                        this.meForm.getCmpByName('main.title').setValue('');
                                        this.meForm.getCmpByName('main.serialNumber').setValue('');
                                        Ext.getCmp('main_button_gen').setDisabled(false);
                                    }
                                }
                            }
                        },
                        { xtype:'button', id:'main_button_gen', text: _lang.TText.fGenerateTitle, width:80, formId: conf.mainFormPanelId, scope:this, hidden: this.isReadOnly,
                            handler: function(e) { this.scope.getAutoCode.call(e);}
                        }
                    ]},
                    {field: 'main.title', xtype: 'textfield', id:'main_title', fieldLabel: _lang.Reports.fTitle, cls:'col-1', allowBlank: false},
                    {field: 'main.reportTime', xtype:'datetimefield', fieldLabel:_lang.Reports.fReportTime, format: curUserInfo.dateFormat, cls:'col-2', allowBlank: false},
                    {field: 'main.serialNumber', xtype: 'textfield', id:'main_serialNumber', fieldLabel: _lang.Reports.fSerialNumber, cls:'col-2', allowBlank: false, maxLength: 30},
                ]},

                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    callback:function(cmp, row){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('productTabs').removeAll();
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    },
                    readOnly: true
                })},

                { field: 'main.products', xtype: 'ProductFormMultiGrid',fieldLabel:_lang.FlowNewProduct.fProductList, height: 150, productType:2,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: this.isReadOnly, allowBlank: false,
                    dataChangeCallback: this.onGridDataChange,
                    itemClickCallback: this.onItemClick
                },

                //tabs
                { xtype: 'container', cls:'row',  items: [
                    {field: 'productTabs', xtype:'ReportProductTabs', mainTabPanelId: conf.mainTabPanelId, retaleGridName: 'main.products',
                        mainFormPanelId: conf.mainFormPanelId, defHeight: 300, readOnly: this.isReadOnly, cls:'col-1', code: 'compliance_options',height:700,
                    }
                ]},

                //报告文件
                { xtype: 'section', title:_lang.Reports.fFile},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.reportFile', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'reportFileDocumentName', xtype: 'FilesDialog', fieldLabel: _lang.Reports.fFile, cls:'col-13', allowBlank:  true,
                            formId: conf.mainFormPanelId, hiddenName: 'main.reportFile', readOnly: this.isReadOnly, single:true
                        },
                        { field: 'preview', xtype: 'button',  tooltip: _lang.ArchivesHistory.mPreview,  width:20, height: 23,  hiddenName: 'main.reportFile',
                            scope: this, cls:'col', iconCls: 'fa fa-fw fa-eye', listeners: {
                            click: function(){
                                var id = Ext.getCmp(conf.mainFormPanelId).getCmpByName(this.hiddenName).getValue()
                                if(!id){
                                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
                                    return;
                                }
                                var def = {
                                    fileId : id,
                                    scope: this.scope
                                };
                                new FilesPreviewDialog(def).show();
                            }
                        }
                        }
                    ]}
                ]},

                { xtype: 'section', title: _lang.Reports.fProductProblem},
                { xtype: 'container', cls:'row',  items:[
                    {field: 'compliance.productProblem', xtype: 'htmleditor', width:'100%', readOnly: this.isReadOnly, cls:'col-1'},
                ]},

                { xtype: 'section', title: _lang.Reports.fStandard},
                { xtype: 'container', cls:'row',  items:[
                    {field: 'compliance.standard', xtype: 'htmleditor', width:'100%', readOnly: this.isReadOnly, cls:'col-1'},
                ]},

                { xtype: 'section', title: _lang.Reports.fEssentialStandard},
                { xtype: 'container', cls:'row',  items:[
                    {field: 'compliance.essentialStandard', xtype: 'htmleditor', width:'100%', readOnly: this.isReadOnly, cls:'col-1'},
                ]},

                { xtype: 'section', title: _lang.Reports.fEvaluationSafetyRecommendations},
                { xtype: 'container', cls:'row',  items:[
                    {field: 'compliance.evaluationSafetyRecommendations', xtype: 'htmleditor', width:'100%', readOnly: this.isReadOnly, cls:'col-1'},
                ]},

                { xtype: 'section', title: _lang.TText.fRemark},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.remark', xtype: 'htmleditor',  width:'100%', readOnly: this.isReadOnly, cls:'col-1'},
                ]},

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
                ], hidden: !this.isReadOnly },

            ]
		});
		
		// 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
		    var me = this;
			this.editFormPanel.loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);

                    //report file
                    if(json.data.reportFile) {
                        this.getCmpByName('reportFileDocumentName').setValue(json.data.reportFile.document.name);
                        this.getCmpByName('main.reportFile').setValue(json.data.reportFile.documentId);
                    }

                    //init vendor
                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                    $_setByName(cmpVendor, json.data, {preName:'vendor', root:'data'});
                    cmpVendor.show();



                    //初始化备选择
                    if(json.data.details.length>0){
                        me.editFormPanel.troubleDetails = json.data.details;
                        me.getCmpByName('main.products').setValue(json.data.details);
                    }
                    //product

                    me.setReadOnly(me.isReadOnly,['main.vendorName']);
                }
			});
        }

        this.editFormPanel.setReadOnly(this.isReadOnly,[]);

	},// end of the initcomponents

    onGridDataChange: function(store){
	    var skus = [];
        var productIds = [];
        var data = store.getRange();
	    for(var i = 0; i < data.length; i++){
            productIds.push(data[i].data.id);
            skus.push(data[i].data.sku);
        }
        var tabs = this.editFormPanel.getCmpByName('productTabs');
        tabs.updateTabs(skus, productIds, this.editFormPanel.troubleDetails);
    },

    onItemClick: function(grid, rowIndex, columnIndex){
        var rows = grid.getSelectionModel().selected.items;
        var sku = rows[0].data.sku;

        if(!sku) return;
        var tabs = this.editFormPanel.getCmpByName('productTabs');
        tabs.setActiveTabBySku(sku);
    },

    saveRow: function(){
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        var params = {act: this.actionName ? this.actionName: 'save'};

        //save products
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.products').formGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['products['+index+'].id'] = businessId;
                for(key in rows[index].data){
                    if(key == 'id')
                        params['products['+index+'].productId'] = rows[index].data.id;
                    else
                        params['products['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }else{
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
            return;
        }

        //save tabs data
        var tabItems = Ext.getCmp(this.mainFormPanelId).getCmpByName('productTabs').items.items;
        var index = 0;
        for(var i = 0; i < tabItems.length; i++){
            var sku = tabItems[i].title;
            var id = tabItems[i].id;
            var grid = tabItems[i].items.items[1];
            var meForm = tabItems[i].items.items[0];
            var store = grid.getStore().getRange();
            var key = 0;
            for(var j = 0; j < store.length; j++){
                var data = store[j].data;
                if(data.active != true) continue;
                params['products[' + index + '].troubleDetails[' + key + '].sku'] = sku;
                params['products[' + index + '].troubleDetails[' + key + '].codeMain'] = data.codeMain;
                params['products[' + index + '].troubleDetails[' + key + '].codeSub'] = data.codeSub;
                params['products[' + index + '].troubleDetails[' + key + '].troubleDetailId'] = data.troubleDetailId;
                key++;
            }
            index++;
        }

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.mainGridPanelId).getStore().reload();
                Ext.getCmp(this.winId).close();
            }
        });
    },

    getAutoCode: function () {
        var curInuptTitle = Ext.getCmp('main_title');
        var curInuptSerial = Ext.getCmp('main_serialNumber');
        var appDepId = this.scope.editFormPanel.appDepId;
        var me = this;
        console.log( this);

        if(!!appDepId) {
            Ext.Ajax.request({
                url: __ctxPath + 'admin/autocode/generate?code=product_compliance&departmentId=' + appDepId,
                scope: this, method: 'post', success: function (response, options) {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success == true) {
                        if (!!obj.data) {
                            curInuptTitle.setValue(obj.data);
                            curInuptSerial.setValue(obj.data);
                            me.setDisabled(true);
                        } else {
                            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
                        }
                    }
                }
            });
        }else{
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorAppIdEmpty);
        }
    }
});