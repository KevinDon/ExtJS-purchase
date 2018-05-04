ReportProductForecastForm = Ext.extend(HP.Window, {
	constructor : function(_cfg) {
		Ext.apply(this, _cfg);

        this.isReadOnly = _cfg.readOnly || this.action != 'add' && this.action != 'copy' && this.record.status>0? true: false;
        
		var conf = {
			title : _lang.ReportProductForecast.mTitle + $getTitleSuffix(this.action),
			winId : 'ReportProductForecastFormWinID',
            moduleName: 'ReportProductForecast',
            frameId : 'ReportProductForecastView',
            mainGridPanelId : 'ReportProductForecastGridPanelID',
            mainFormPanelId : 'ReportProductForecastFormPanelID',
            mainViewPanelId : 'ReportProductForecastViewPanelID',
            searchFormPanelId: 'ReportProductForecastSearchPanelID',
            mainTabPanelId: 'ReportProductForecastTbsPanelId',
            urlSave: __ctxPath + 'reportproductforecast/save',
            urlGet: __ctxPath + 'reportproductforecast/get',
			actionName: this.action,
			save: !this.isReadOnly,
            close: true,
            saveFun: this.saveRow
		};
		
		this.initUIComponents(conf);
		ReportProductForecastForm.superclass.constructor.call(this, {
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
                { xtype: 'container', cls:'row',  items:[
                    {field: 'main.title', xtype: 'textfield', fieldLabel: _lang.Reports.fTitle, cls:'col-1', allowBlank: false},
                    {field: 'main.reportTime', xtype:'datetimefield', fieldLabel:_lang.Reports.fReportTime, format: curUserInfo.dateFormat, cls:'col-2', allowBlank: false},
                    {field: 'main.serialNumber', xtype: 'textfield', fieldLabel: _lang.Reports.fSerialNumber, cls:'col-2', allowBlank: false, maxLength: 30,},
                    {field: 'main.businessType', xtype: 'combo', fieldLabel: _lang.Reports.fBusinessType, cls:'col-2', allowBlank: false, readOnly: this.isReadOnly, value: '1',
                        store:[['1', _lang.Reports.vFlowNewProduct], ['2', _lang.Reports.vFlowPurchasePlan]],
                        onChange: function(value){
                            var cmp = Ext.getCmp(conf.mainFormPanelId);
                            cmp.getCmpByName('main.businessId').setValue('');
                            cmp.getCmpByName('main_businessId').setValue('');
                            cmp.getCmpByName('main_businessId').setDisplayType(value != '1'? 5: 1);

                            cmp.getCmpByName('main.vendorId').setValue('');
                            cmp.getCmpByName('main.vendorName').setValue('');
                            Ext.getCmp(conf.mainFormPanelId + '-vendor').hide();

                            cmp.getCmpByName('main.products').setValue('');
                        }
                    },
                    {field: 'main.businessId', xtype: 'hidden'},
                    {field: 'main_businessId', xtype: 'FlowOtherDialog', fieldLabel: _lang.Reports.fBusinessId, hiddenName:'main.businessId',
                        cls:'col-2', allowBlank: false, frameId:conf.mainFormPanelId, single:true, readOnly: this.isReadOnly,
                        subcallback: function (ids, titles, redords) {
                            //申请单选择并且初始化
                            if(this.selectedId != ids){
                                var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                                $_setByName(cmpVendor, redords.data, {preName:'main', root:'data'});
                                this.meForm.getCmpByName('main.vendorId').setValue(redords.data.vendorId);
                                this.meForm.getCmpByName('main.vendorName').setValue(redords.data.vendorName);
                                cmpVendor.show();

                                this.meForm.getCmpByName('main.products').setValue(redords.data.details);
                            }
                        }
                    },
                ]},

                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    callback: function (eOpts) {
                        //产品选择器关联设置
                        var cmp = eOpts.getCmpByName('main.products');
                        cmp.vendorId = eOpts.getCmpByName('main.vendorId').getValue();
                        if(this.selectedId != eOpts.getCmpByName('main.vendorId').getValue()){
                            eOpts.getCmpByName('main.businessId').setValue('');
                            eOpts.getCmpByName('main_businessId').setValue('');
                            cmp.setValue([]);
                        }
                    },
                    readOnly: true
                })},

                //products
                { field: 'main.products', xtype: 'ProductFormMultiGrid',fieldLabel:_lang.FlowNewProduct.fProductList, height: 150, productType:2,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: this.isReadOnly, allowBlank: false
                },

				//报告文件
                { xtype: 'section', title:_lang.Reports.fFile},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.reportFile', xtype: 'hidden'},
                    { xtype: 'container', cls:'col-2', items:  [
                        { field: 'reportFileDocumentName', xtype: 'FilesDialog', fieldLabel: _lang.Reports.fFile, cls:'col-13', allowBlank: false,
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

                { xtype: 'section', title:_lang.TText.fRemark},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.remark', xtype: 'htmleditor', cls:'col-1', width:'100%', readOnly: this.isReadOnly},
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
                    var cmp = this;

                    //report file
                    if(json.data.reportFile) {
                        this.getCmpByName('reportFileDocumentName').setValue(json.data.reportFile.document.name);
                        this.getCmpByName('main.reportFile').setValue(json.data.reportFile.documentId);
                    }

                    //init vendor
                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                    $_setByName(cmpVendor, json.data, {preName:'vendor', root:'data'});
                    cmpVendor.show();

                    //init porducts
                    cmp.getCmpByName('main.products').vendorId = cmp.getCmpByName('main.vendorId').getValue();
                    cmp.getCmpByName('main.products').setValue(json.data.details);

                    cmp.setReadOnly(me.isReadOnly,['main.vendorName']);
                }
			});
        }

        this.editFormPanel.setReadOnly(this.isReadOnly,[]);

	},// end of the initcomponents
    saveRow: function(){
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        var params = {act: this.actionName ? this.actionName: 'save'};
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.products').formGridPanel});
        if(rows.length>0){
            for(index in rows){
                params['products['+index+'].businessId'] = businessId;
                params['products['+index+'].productId'] = rows[index];
            }
        }else{
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
            return;
        }

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.mainGridPanelId).getStore().reload();
                Ext.getCmp(this.winId).close();
            }
        });

    }
});