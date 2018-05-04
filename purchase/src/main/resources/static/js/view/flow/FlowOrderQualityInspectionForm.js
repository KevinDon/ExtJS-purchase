FlowOrderQualityInspectionForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;


        var conf = {
            title : _lang.FlowOrderQualityInspection.mTitle + $getTitleSuffix(this.action),
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
			urlFlow: __ctxPath + 'flow/inspection/flowOrderQC/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowOrderQc&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
			actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy' && !this.isCanceled,
            flowAllow: (!this.isAdd) && this.isApprove && !this.isCanceled,
            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && !this.isCanceled,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1 && !this.isCanceled,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1 && !this.isCanceled,
            //flowCancel: (!this.isAdd) && (this.record.flowStatus==1 || this.record.flowStatus==2) && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowOrderQualityInspectionForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			cls: 'gb-blank',
			tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });
    },

    initUIComponents: function(conf) {
    	
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                
                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf, {
                    hideDetails: this.action == 'add',
                    callback : function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).vendorId = record.id;
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderId').setValue('');
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderNumber').setValue('');
                        Ext.getCmp(conf.mainFormPanelId + '-order').hide();
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').setValue('');

                    }
                }),},
                
                 //订单
                { xtype:'container', cls: 'row', items: $groupFormOrderFields(this, conf, {
                    callback : function(eOpts, record){
                        if(record && record.details){
                            this.meForm.getCmpByName('main.products').setValue(record.details);
                        }else{
                            this.meForm.getCmpByName('main.products').setValue('');
                        }
                    },allowBlank:false
                })},

                //products
                { field: 'main.products', xtype: 'OrderProductFormMultiGrid',fieldLabel:_lang.FlowNewProduct.fProductList, height: 150, productType:5,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: true, allowBlank: false
                },

                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, 
                	mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, 
                	scope:this, readOnly: this.readOnly
                },

                //报告列表
                { xtype: 'section', title: _lang.Reports.tabRelatedReports},
                { field: 'main.reports', xtype: 'ReportsFormMultiGrid',fieldLabel: _lang.Reports.tabRelatedReports,
                    farmeId: conf.mainFormPanelId, scope:this, readOnly: true,
                },


            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
		    var me = this;
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);
                    Ext.getCmp(conf.mainFormPanelId).vendorId =json.data.vendorId;

					if(!!json.data && !!json.data.details && json.data.details.length>0){
                        if(json.data.details){
                            var params = {id:json.data.details[0].orderId};
                            $postUrl({
                                url: __ctxPath + 'purchase/purchasecontract/get', maskTo: this.frameId, params: params, autoMessage:false,
                                callback: function (response, eOpts) {
                                    var row = Ext.JSON.decode(response.responseText);
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').setValue(row.data.details);

                                    var cmpOrder = Ext.getCmp(conf.mainFormPanelId + '-order');
                                    $_setByName(cmpOrder, row.data, {preName:'order', root:'data'});
                                    cmpOrder.show();
                                }
                            });

                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderId').setValue(json.data.details[0].orderId);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderNumber').setValue(json.data.details[0].orderNumber);

                        }else{
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderId').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderNumber').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').setValue('');
                        }

                        //init vendor
                        var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                        $_setByName(cmpVendor, json.data, {preName:'vendor', root:'data'});
                        cmpVendor.show();

                        //相关报告
                        !!json.data && !!json.data.reports && me.action != 'copy' ? Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(json.data.reports) : '';

                        //attachment init
                        !!json.data && !!json.data.attachments ? Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments): '';
					}
				}
			});	
		}

		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount']);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};

        params['details[0].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        params['details[0].orderId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.orderId').getValue();

    	//attachments
    	var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
    	if(rows.length>0){
			for(index in rows){
				params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				params['attachments['+index+'].documentId'] = rows[index];
			}
		}

        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();
        if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == ''){
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        }else {
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url: isFlow ? this.urlFlow : this.urlSave,
                params: params,
                callback: function (fp, action, status, grid) {
                    Ext.getCmp(this.winId).close();
                    if(!!grid) {
                        grid.getSelectionModel().clearSelections();
                        grid.getView().refresh();
                    }
                }
            });
        }
    }
});