FlowNewProductForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;

        var conf = {
            title : _lang.FlowNewProduct.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowNewProduct',
            winId : 'FlowNewProductForm',
			frameId : 'FlowNewProductView',
			mainGridPanelId : 'FlowNewProductViewGridPanelID',
			mainFormPanelId : 'FlowNewProductViewFormPanelID',
			processFormPanelId: 'FlowNewProductProcessFormPanelID',
			searchFormPanelId: 'FlowNewProductViewSearchFormPanelID',
			mainTabPanelId: 'FlowNewProductViewMainTabsPanelID',
			subGridPanelId : 'FlowNewProductViewSubGridPanelID',
			formGridPanelId: 'FlowNewProductFormGridPanelID',
			urlList: __ctxPath + 'flow/purchase/newproduct/list',
			urlSave: __ctxPath + 'flow/purchase/newproduct/save',
			urlDelete: __ctxPath + 'flow/purchase/newproduct/delete',
			urlGet: __ctxPath + 'flow/purchase/newproduct/get',
            urlExport: __ctxPath + 'flow/purchase/newproduct/export',
			urlFlow: __ctxPath + 'flow/purchase/newproduct/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowNewProduct&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
            expdoc: this.isApprove && !this.isAdd || this.isComplated && (this.action!= 'add' && this.action!='copy'),
            tomail: this.isApprove && !this.isAdd || this.isComplated && (this.action!= 'add' && this.action!='copy')
        };

        this.initUIComponents(conf);
        FlowNewProductForm.superclass.constructor.call(this, {
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
                {field: 'main.rateAudToRmb', xtype: 'hidden', value: curUserInfo.audToRmb},
                {field: 'main.rateAudToUsd', xtype: 'hidden', value: curUserInfo.audToUsd},
                
                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf, {allowBlank: false,
					callback: function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    }
                })},
                
                 //新品列表
                { xtype: 'section', title:_lang.FlowNewProduct.tabNewProducts},
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowNewProductFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly},

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
					//products
					if(!!json.data && !!json.data.details && json.data.details.length>0){
						for(index in json.data.details){
							var product = {};
							Ext.applyIf(product, json.data.details[index]);
                            Ext.applyIf(product, json.data.details[index].product.prop);
							Ext.apply(product, json.data.details[index].product);
							Ext.getCmp(conf.formGridPanelId).getStore().add(product);
						}
					}

                    //init vendor
                    var cmpVendor = Ext.getCmp(conf.mainFormPanelId + '-vendor');
                    $_setByName(cmpVendor, json.data, {preName:'vendor', root:'data'});
                    cmpVendor.show();

                    //相关报告
                    !!json.data && !!json.data.reports && me.action != 'copy' ? Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(json.data.reports) : '';
   
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');
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
    	
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
        var flagPrice = true;
    	if(rows != undefined && rows.length>0){
			for(index in rows){
                if(!rows[index].data['priceAud']){
                    //报价为空时不能保存
                    flagPrice = false;
                }
				params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				for(key in rows[index].data){
					if(key == 'id')
						params['details['+index+'].productId'] = rows[index].data.id;
					else
						params['details['+index+'].'+key ] = rows[index].data[key];
				}
			}
        }else{
            //报价为空时不能保存
            flagPrice = false;
        }

    	//attachments
    	var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
    	if(rows.length>0){
			for(index in rows){
				params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
				params['attachments['+index+'].documentId'] = rows[index];
			}
         }

        var reportsCount = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.reports').subGridPanel});
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if(!flagPrice){
            //报价为空时不能保存
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsPriceError);
        }else if((params.act == 'start' || params.act == 'allow') && (reportsCount==undefined || reportsCount<3)){
            //报告为空时不能发启
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsReportError);
        }else if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == ''){
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
    },

    checkFlowRemarkFun: function(conf){

    },
});