FlowComplianceArrangementForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        
        var conf = {
            title : _lang.FlowComplianceArrangement.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowComplianceArrangement',
            winId : 'FlowComplianceArrangementForm',
			frameId : 'FlowComplianceArrangementForm',
            mainGridPanelId : 'FlowComplianceArrangementViewGridPanelID',
            mainFormPanelId : 'FlowComplianceArrangementViewFormPanelID',
            processFormPanelId: 'FlowComplianceArrangementViewProcessFormPanelID',
            searchFormPanelId: 'FlowComplianceArrangementViewSearchFormPanelID',
            mainTabPanelId: 'FlowComplianceArrangementViewMainTbsPanelID',
            subGridPanelId : 'FlowComplianceArrangementViewSubGridPanelID',
            formGridPanelId : 'FlowComplianceArrangementFormGridPanelID',

			urlList: __ctxPath + 'flow/inspection/complianceApply/list',
			urlSave: __ctxPath + 'flow/inspection/complianceApply/save',
			urlDelete: __ctxPath + 'flow/inspection/complianceApply/delete',
			urlGet: __ctxPath + 'flow/inspection/complianceApply/get',
			urlFlow: __ctxPath + 'flow/inspection/complianceApply/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowComplianceApply&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
			actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy',
            flowAllow: (!this.isAdd) && this.isApprove,
            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowComplianceArrangementForm.superclass.constructor.call(this, {
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
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    hideDetails: this.action == 'add',
                    readOnly: this.readOnly,
                    callback: function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    }
                })},
                
                 //新品列表
                { xtype: 'section', title:_lang.ProductDocument.tabListTitle},
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowComplianceArrangementFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly},

                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, 
                	mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, 
                	scope:this, readOnly: this.readOnly
                },

                //相关报告
                { xtype: 'section', title: _lang.Reports.tabRelatedReports},
                { field: 'main.reports', xtype: 'ReportsFormMultiGrid',fieldLabel: _lang.Reports.tabRelatedReports,
                    farmeId: conf.mainFormPanelId, scope:this, readOnly: true,
                },

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            var me= this;
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        for(index in json.data.details){
                            var product = {};
                            Ext.applyIf(product, json.data.details[index]);
                            Ext.apply(product, json.data.details[index].product);
                            Ext.applyIf(product, json.data.details[index].product.prop);
                            product.prevRiskRating = json.data.details[index].product.prop.riskRating;
                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                        }
                    }
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                    //相关报告
                    !!json.data && !!json.data.reports && me.action != 'copy' ? Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(json.data.reports): '';
                }
            });
        }

		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandler','flowNextHandlerAccount']);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});

    	if(!!rows && !!rows.length && rows.length>0){
			for(index in rows){
				params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				for(key in rows[index].data){
					if(key == 'id') 
						params['details['+index+'].productId'] = rows[index].data.id;
					else
						params['details['+index+'].'+key ] = rows[index].data[key];
				}
			}
		}

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(!!rows && !!rows.length && rows.length>0){
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