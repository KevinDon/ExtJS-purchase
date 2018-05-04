FlowOrderShippingConfirmationForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowOrderShippingConfirmation.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowOrderShippingConfirmation',
            winId : 'FlowOrderShippingConfirmationForm',
            frameId : 'FlowOrderShippingConfirmationView',
            mainGridPanelId : 'FlowOrderShippingConfirmationGridPanelID',
            mainFormPanelId : 'FlowOrderShippingConfirmationFormPanelID',
            processFormPanelId: 'FlowOrderShippingConfirmationProcessFormPanelID',
            searchFormPanelId: 'FlowOrderShippingConfirmationSearchFormPanelID',
            mainTabPanelId: 'FlowOrderShippingConfirmationMainTabsPanelID',
            subOrderGridPanelId : 'FlowOrderShippingConfirmationSubOrderGridPanelID',
            formGridPanelId : 'FlowOrderShippingConfirmationFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/orderShippingApply/list',
			urlSave: __ctxPath + 'flow/shipping/orderShippingApply/save',
			urlDelete: __ctxPath + 'flow/shipping/orderShippingApply/delete',
			urlGet: __ctxPath + 'flow/shipping/orderShippingApply/get',
			urlFlow: __ctxPath + 'flow/shipping/orderShippingApply/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowOrderShippingApply&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        FlowOrderShippingConfirmationForm.superclass.constructor.call(this, {
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


                 //订单信息
                { xtype: 'section', title:_lang.FlowOrderShippingConfirmation.tabOrderDetail},

                { field: 'main_orders', xtype:'hidden', value: this.record.products },
                { field: 'main_orderName', xtype:'hidden'},
                { field: 'main_serviceProviderId', xtype:'hidden'},
                { field: 'serviceProviderName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowOrderShippingConfirmationFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly},

                
                //创建人信息
                { xtype: 'section', title:_lang.ProductDocument.fBusinessIdInfo},

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.id', xtype: 'displayfield', fieldLabel: _lang.FlowSamplePayment.fBusinessId, cls:'col-2', readOnly:true },
                    { field: 'main.startTime', xtype: 'displayfield',  fieldLabel: _lang.TText.fAppTime, cls:'col-2', readOnly:true}
                ] },
            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    if(!!json.data && !!json.data.details && json.data.details.length > 0){
                        for(index in json.data.details){
                            var order = {};
                            Ext.applyIf(order, json.data.details[index]);
                            Ext.apply(order, json.data.details[index]);
                            Ext.getCmp(conf.formGridPanelId).getStore().add(order);
                        }
                    }
                }
            });
        }
//		if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount',]);
		
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
    	var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});

    	//目的港口必须相同
        var destinationPortId = null;
        for(var i = 0; i < rows.length; i++){
            if(!destinationPortId) destinationPortId = rows[i].data.destinationPortId;
            else if(rows[i].data.destinationPortId != destinationPortId) {
                Ext.Msg.alert(_lang.TText.titleClew, _lang.FlowOrderShippingConfirmation.msgDestinaionPort);
                return false;
            }
        }

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
                    else if(key == 'eta' && typeof(rows[index].data.eta) != "string")
                        params['details['+index+'].eta'] = Ext.Date.format(rows[index].data.eta, curUserInfo.dateFormat);
                    else
                        params['details['+index+'].'+key ] = rows[index].data[key];
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