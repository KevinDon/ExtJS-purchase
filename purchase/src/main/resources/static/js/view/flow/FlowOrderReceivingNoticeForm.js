FlowOrderReceivingNoticeForm = Ext.extend(Ext.Panel, {
	
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        var conf = {
            scope: this,
            title : _lang.FlowOrderReceivingNotice.mTitle +  $getTitleSuffix(this.action),

            moduleName: 'FlowOrderReceivingNotice',
            winId : 'FlowOrderReceivingNoticeForm',
            frameId : 'FlowOrderReceivingNoticeView',
            mainGridPanelId : 'FlowOrderReceivingNoticeGridPanelID',
            mainFormPanelId : 'FlowOrderReceivingNoticeFormPanelID',
            processFormPanelId: 'FlowOrderReceivingNoticeProcessFormPanelID',
            searchFormPanelId: 'FlowOrderReceivingNoticeSearchFormPanelID',
            mainTabPanelId: 'FlowOrderReceivingNoticeMainTabsPanelID',
            subGridPanelId : 'FlowOrderReceivingNoticeSubGridPanelID',
            formGridPanelId : 'FlowOrderReceivingNoticeFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/asn/list',
			urlSave: __ctxPath + 'flow/shipping/asn/save',
			urlDelete: __ctxPath + 'flow/shipping/asn/delete',
			urlGet: __ctxPath + 'flow/shipping/asn/get',
			urlFlow: __ctxPath + 'flow/shipping/asn/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowAsn&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
			actionName: this.action,
            readOnly: this.readOnly,
            close: true,
            save: this.isAdd || this.isStart || this.record.flowStatus === null ,
            flowStart: (this.isAdd || this.record.flowStatus === null ) && this.action != 'copy',
            flowAllow: (!this.isAdd) && this.isApprove,

            flowRefuse: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==2 &&  this.record.flagCompleteStatus != 1,

            flowRedo: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowBack: (!this.isAdd) && this.isApprove && this.record.flowStatus==1,
            flowHold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold!=1,
            flowUnhold: (!this.isAdd) && this.isApprove && this.record.flowStatus==1 && this.record.hold==1,
            processRemark: this.isApprove && (!this.isAdd || this.record.flowStatus >1),
            processHistory: this.isApprove && !this.isAdd,
            processDirection: true,
            saveFun: this.saveFun,
            flowSaveFun: this.flowSaveFun,
            loadContainer: this.loadContainer,
        };

        Ext.applyIf(this, conf);
        this.initUIComponents(conf);
        FlowOrderReceivingNoticeForm.superclass.constructor.call(this, {
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
        var formal = true;
        this.editFormPanel = new HP.FormPanel({
            region: 'center',
            //mainFormPanelId: conf.mainFormPanelId,
            insertPackingListPanel: this.insertPackingListPanel,
            deletePackingListPanel: this.deletePackingListPanel,
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },

                //通知明细
                { xtype: 'section', title:_lang.FlowOrderReceivingNotice.mTitle},
                { field:'main.warehousePlanId', xtype:'hidden'},
                { field: 'main.orderId', xtype: 'hidden',  },

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.orderNumber', xtype:'WarehousePlanningDialog', fieldLabel: _lang.FlowOrderReceivingNotice.fWarehousePlanningId,
                        cls: 'col-2', allowBlank: false, type:1,
                        formId:conf.mainFormPanelId, hiddenName:'main.orderId', single: true, readOnly: this.readOnly,
                        subcallback: function(rows){
                            if(!rows && rows.length == 0) return;
                            var row = rows[0];
                            var warehousePlan = row.raw.warehousePlanDetail;
                            var cmp = scope.editFormPanel;
                            cmp.getCmpByName('main.accessories').setValue( row.data.accessories.toString());
                            cmp.getCmpByName('main.orderId').setValue(row.raw.orderId);
                            cmp.getCmpByName('main.warehousePlanId').setValue(warehousePlan.warehousePlanId);
                            cmp.getCmpByName('main.receiveStartDate').setValue(warehousePlan.receiveStartDate);
                            cmp.getCmpByName('main.receiveDate').setValue(warehousePlan.receiveDate);
                            cmp.getCmpByName('main.receiveEndDate').setValue(warehousePlan.receiveEndDate);
                            cmp.getCmpByName('main.originPlace').setValue(warehousePlan.originPlace);
                            cmp.getCmpByName('main.warehouseId').setValue(warehousePlan.warehouseId);
                            cmp.getCmpByName('main.receiveLocation').setValue(warehousePlan.destinationPlace);

                            //cmp.getCmpByName('main.asnNumber').setValue(asnData.asnNo);

                            if(!!row.raw && !!row.raw.details){
                                for(var index in row.raw.details){
                                    row.raw.details[index].expectedQty = row.raw.details[index].orderQty;
                                    row.raw.details[index].expectedCartons = row.raw.details[index].cartons;
                                }
                            }

                            scope.insertPackingListPanel.call(scope, row.raw, row.raw.details);
                        }
                    },
                    { field: 'main.originPlace',  xtype:'textfield',fieldLabel:_lang.FlowWarehousePlanning.fOriginAddress, cls:'col-2',},
                ] },

                { xtype: 'container',cls:'row', items:  [
                    {  field: 'section', xtype: 'container', cls: 'row', items: [
                        { field: 'main.warehouseId',  xtype:'textfield',fieldLabel:_lang.FlowWarehousePlanning.fWarehouse, cls:'col-2', readOnly: true},
                        { field: 'main.receiveLocation',  xtype:'textfield',fieldLabel:_lang.FlowWarehousePlanning.fDestAddress, cls:'col-2', readOnly: true},
                    ]},

                    { field: 'main.receiveStartDate',  xtype:'datetimefield',fieldLabel:_lang.FlowWarehousePlanning.fReceiveStartDate, cls:'col-2', format: curUserInfo.dateFormat ,readOnly: this.readOnly,},
                    { field: 'main.receiveEndDate',  xtype:'datetimefield',fieldLabel:_lang.FlowWarehousePlanning.fReceiveEndDate, cls:'col-2', format: curUserInfo.dateFormat, readOnly: this.readOnly,},
                    { field: 'main.receiveDate',  xtype:'datetimefield',fieldLabel:_lang.FlowWarehousePlanning.fReceiveDate, cls:'col-2', format: curUserInfo.dateFormat, readOnly: this.readOnly,},

                    { field: 'main.asnNumber', xtype: 'textfield', fieldLabel: _lang.FlowOrderReceivingNotice.fAsnNumber,  cls: 'col-2',readOnly: this.readOnly, },
                    { field: 'main.accessories', xtype: 'dictcombo', fieldLabel:_lang.FlowCustomClearance.fHasAccessory,  cls: 'col-2', value:'1',
                        allowBlank: false, readOnly:true,  code:'options', codeSub:'yesno'
                    },
                ] },

                //通知单明细
                { xtype: 'section', title:_lang.FlowCustomClearance.tabPackingList},
                { xtype: 'container', cls:'row', id:'packingListContainer', items: []},

                //创建人信息
                { xtype: 'section', title:_lang.ProductDocument.fBusinessIdInfo},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.id', xtype: 'displayfield',  fieldLabel: _lang.FlowDepositContract.fId, cls:'col-2', readOnly:true },
                    { field: 'main.flagCompleteStatus',  xtype: 'dictfield',  fieldLabel:  _lang.FlowOrderReceivingNotice.fFlagCompleteStatus, cls:'col-2', readOnly:true,
                        code:'global', codeSub:'sync',  value:2,
                    }
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
					    if(json.data.details[0].asnPackingDetails.length>0){
					        var items = [];
					        for (var index in json.data.details[0].asnPackingDetails) {
                                var product = {}
                                Ext.apply(product, json.data.details[0].asnPackingDetails[index]);
                                Ext.applyIf(product, json.data.details[0].asnPackingDetails[index].ccPackingDetail);
                                items.push(product)
                            }
                            scope.insertPackingListPanel(json.data.details[0], items);
                        }
					}
				}
			});	
		}
		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount', 'main.asnNumber']);
    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var panels = Ext.getCmp(this.mainFormPanelId).query('AsnPackingListPanel');
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        if(!!panels && panels.length > 0){
            for(var index in panels){
                var editFormPanel = panels[index].editFormPanel;
                var packingDetails = [];
                var store = panels[index].formGridPanel.getStore().getRange();
                for(var i = 0; i < store.length; i++) {
                    packingDetails.push(store[i].data);
                }

                params['details['+index+'].businessId'] = businessId;
                // params['details['+index+'].packingDetails'] = packingDetails;
                params['details['+index+'].containerNumber'] = editFormPanel.getCmpByName('containerNumber').getValue();
                params['details['+index+'].ccPackingId'] = editFormPanel.getCmpByName('ccPackingId').getValue();
                params['details['+index+'].sealsNumber'] = editFormPanel.getCmpByName('sealsNumber').getValue();
                params['details['+index+'].containerType'] = editFormPanel.getCmpByName('containerType').getValue();
                console.log(1);
                for(var j = 0; j < packingDetails.length; j++){
                    for(var key in packingDetails[j]){
                        if(key == 'id') continue;
                        var attr = 'details['+ index + '].packingDetails[' + j + '].' + key;
                        params[attr] = packingDetails[j][key];
                    }
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
    },

    loadContainer: function(containerIds){
         //if(!containerIds || !Array.isArray(containerIds) || containerIds.length == 0) return;
        var data = containerIds[0].raw.packingDetails;
        Ext.getCmp('packingListContainer').removeAll();
        this.insertPackingListPanel(containerIds[0].raw, data);
    },
    insertPackingListPanel : function(containerDetail, orders){
        var form = Ext.getCmp(this.mainFormPanelId);
        var section = Ext.getCmp('packingListContainer');
        var packingListPanelId = Math.round(Math.random() * 100000);
        section.removeAll();
        section.add(
            { xtype: 'container',cls:'row', items:  [
                {
                    xtype: 'AsnPackingListPanel',
                    title: _lang.FlowCustomClearance.fPackingSheet,
                    formId: this.mainFormPanelId,
                    mainForm: form,
                    insertPackingListPanel: this.insertPackingListPanel,
                    deletePackingListPanel: this.deletePackingListPanel,
                    panelId: packingListPanelId,
                    data: containerDetail,
                    orders: orders,
                    bodyCls:'x-panel-body-gray',
                    header:{
                        cls:'x-panel-header-gray'
                    },
                }
            ]}
        );
    },

    deletePackingListPanel: function(panel){
        var parentContainer = panel.up();
        var panels = parentContainer.up().query('AsnPackingListPanel');
        if(panels.length <= 1){
            Ext.Msg.alert(_lang.TText.titleClew, _lang.FlowOrderReceivingNotice.msgLastPanelList);
            return false;
        } else {
            parentContainer.up().remove(parentContainer, true);
        }
    },
});