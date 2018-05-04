FlowOrderShippingPlanForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowOrderShippingPlan.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowOrderShippingPlan',
            winId : 'FlowOrderShippingPlanForm',
            frameId : 'FlowOrderShippingPlanView',
            mainGridPanelId : 'FlowOrderShippingPlanGridPanelID',
            mainFormPanelId : 'FlowOrderShippingPlanFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'FlowOrderShippingPlanSearchFormPanelID',
            mainTabPanelId: 'FlowOrderShippingPlaMainTabsPanelID',
            subOrderGridPanelId : 'FlowOrderShippingPlanSubOrderGridPanelID',
            formGridPanelId : 'FlowOrderShippingPlanFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/orderShippingPlan/list',
			urlSave: __ctxPath + 'flow/shipping/orderShippingPlan/save',
			urlDelete: __ctxPath + 'flow/shipping/orderShippingPlan/delete',
			urlGet: __ctxPath + 'flow/shipping/orderShippingPlan/get',
			urlFlow: __ctxPath + 'flow/shipping/orderShippingPlan/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowOrderShippingPlan&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
            flowCancel: (!this.isAdd) && (this.record.flowStatus==1 || this.record.flowStatus==2) && !this.isCanceled,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowOrderShippingPlanForm.superclass.constructor.call(this, {
            id: conf.winId, title: conf.title,
            region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			cls: 'gb-blank',
			tbar: Ext.create("App.toolbar", conf),
            items: [ this.editFormPanel ]
        });
    },

    initUIComponents: function(conf) {
        var scope = this;
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '' : this.record.id },

                 //计划明细
                { xtype: 'section', title:_lang.FlowOrderShippingPlan.tabPlanDetail},
                // { field: 'main.totalContainerQty', xtype: 'textfield', fieldLabel: _lang.FlowOrderShippingPlan.fTotalContainerQty,  cls: 'col-2', allowBlank: false },
                { field: 'main_orders', xtype:'hidden', value: this.record.products },
                { field: 'main_orderName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowOrderShippingPlanFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly, height:400,
                    clearServiceProviderQuotation: this.clearServiceProviderQuotation, scope: this,
                },

                { xtype: 'section', title:_lang.FlowOrderShippingPlan.fServiceProviderQuote},
                {  field: 'section', xtype: 'container', cls: 'row', items: [
                    { field: 'main.serviceProviderQuotationId', xtype: 'hidden'},
                    { field: 'main.serviceProviderId', xtype: 'hidden'},
                    { field: 'main.serviceProviderCnName', xtype: 'hidden'},
                    { field: 'main.serviceProviderEnName', xtype: 'hidden'},
                    { field: 'serviceProviderName', xtype: 'ServiceProviderQuotationDialog', fieldLabel: _lang.ServiceProviderDocument.fService, cls:'col-2',
                        formId: conf.mainFormPanelId, formGridPanelId: conf.formGridPanelId, hiddenName: 'main.serviceProviderId', single:true, readOnly: this.readOnly,
                        allowBlank:false,
                        subcallback: function(rows){
                            if(!!rows && rows.length > 0){
                                var data = rows[0].data;
                                var form = scope.editFormPanel;
                                form.getCmpByName('main.serviceProviderId').setValue(data.serviceProviderId);
                                form.getCmpByName('serviceProviderName').setValue(!!data.serviceProviderCnName ? data.serviceProviderCnName : data.serviceProviderEnName);
                                form.getCmpByName('main.serviceProviderCnName').setValue(data.serviceProviderCnName);
                                form.getCmpByName('main.serviceProviderEnName').setValue(data.serviceProviderEnName);
                                form.getCmpByName('main.serviceProviderQuotationId').setValue(data.serviceProviderQuotationId);
                                form.getCmpByName('quotationId').setValue(data.serviceProviderQuotationId);
                            }
                        }
                    },
                    { field: 'quotationId',  xtype:'displayfield',fieldLabel:_lang.FlowServiceInquiry.fQuotationId, cls:'col-2',},
                ]},

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);
                    // console.log(json.data);
                    scope.editFormPanel.getForm().setValues(json.data);

                    var serviceProvideName = !!json.data.serviceProviderCnName ? json.data.serviceProviderCnName : json.data.serviceProviderEnName;
                    scope.editFormPanel.getCmpByName('serviceProviderName').setValue(serviceProvideName);
                    scope.editFormPanel.getCmpByName('quotationId').setValue(json.data.serviceProviderQuotationId);

					if(!!json.data && !!json.data.details && json.data.details.length > 0){
                        Ext.getCmp(conf.formGridPanelId).getStore().add(json.data.details);
					}
				}
			});	
		}
//		if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount',]);
		
    },// end of the init

    clearServiceProviderQuotation: function(){
        var form = this.editFormPanel;
        form.getCmpByName('main.serviceProviderId').setValue('');
        form.getCmpByName('serviceProviderName').setValue('');
        form.getCmpByName('main.serviceProviderCnName').setValue('');
        form.getCmpByName('main.serviceProviderEnName').setValue('');
        form.getCmpByName('main.serviceProviderQuotationId').setValue('');
        form.getCmpByName('quotationId').setValue('');
        },

    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
    	var form = Ext.getCmp(this.mainFormPanelId);
        var serviceProviderId = form.getCmpByName('main.serviceProviderId').getValue();
        var serviceProviderCnName = form.getCmpByName('main.serviceProviderCnName').getValue();
        var serviceProviderEnName = form.getCmpByName('main.serviceProviderEnName').getValue();
        var quotationId = form.getCmpByName('main.serviceProviderQuotationId').getValue();

        if(rows != undefined && rows.length>0){
            for(index in rows){
                params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                for(key in rows[index].data){
                    if(key == 'id')
                        params['details['+index+'].orderId'] = rows[index].data.id;
                    else if(key == 'readyDate' && typeof(rows[index].data.readyDate) != "string")
                        params['details['+index+'].readyDate'] = Ext.Date.format(rows[index].data.readyDate, curUserInfo.dateFormat);
                    else if(key == 'etd' && typeof(rows[index].data.etd) != "string")
                        params['details['+index+'].etd'] = Ext.Date.format(rows[index].data.etd, curUserInfo.dateFormat);
                    else
                        params['details['+index+'].'+key ] = rows[index].data[key];

                    params['details['+index+'].'+ 'serviceProviderId' ] = serviceProviderId;
                    params['details['+index+'].'+ 'serviceProviderCnName' ] = serviceProviderCnName;
                    params['details['+index+'].'+ 'serviceProviderEnName' ] = serviceProviderEnName;
                    params['details['+index+'].'+ 'serviceProviderQuotationId' ] = quotationId;
                }
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