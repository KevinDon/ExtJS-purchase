FlowWarehousePlanningForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        var conf = {
            title : _lang.FlowWarehousePlanning.mTitle + $getTitleSuffix(this.action),

            moduleName: 'FlowWarehousePlanning',
            winId : 'FlowWarehousePlanningForm',
            frameId : 'FlowWarehousePlanningView',
            mainGridPanelId : 'FlowWarehousePlanningGridPanelID',
            mainFormPanelId : 'FlowWarehousePlanningFormPanelID',
            processFormPanelId: 'FlowWarehousePlanningProcessFormPanelID',
            searchFormPanelId: 'FlowWarehousePlanningSearchFormPanelID',
            mainTabPanelId: 'FlowWarehousePlanningMainTabsPanelID',
            subOrderGridPanelId : 'FlowWarehousePlanningPlanSubOrderGridPanelID',
            formGridPanelId : 'FlowWarehousePlanningFormGridPanelID',

			urlList: __ctxPath + 'flow/shipping/warehousePlan/list',
			urlSave: __ctxPath + 'flow/shipping/warehousePlan/save',
			urlDelete: __ctxPath + 'flow/shipping/warehousePlan/delete',
			urlGet: __ctxPath + 'flow/shipping/warehousePlan/get',
			urlFlow: __ctxPath + 'flow/shipping/warehousePlan/approve',
			urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowWarehousePlan&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        FlowWarehousePlanningForm.superclass.constructor.call(this, {
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
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                { field: 'main.products', xtype: 'FlowWarehousePlanningFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly},

                //仓库信息
                { xtype: 'section', title:_lang.FlowWarehousePlanning.tabWarehouseInformation},
                {
                    xtype: 'container', cls: 'row', items: [
                        { field: 'main.originPlace',  xtype:'textfield',fieldLabel:_lang.FlowWarehousePlanning.fOriginAddress, cls:'col-2', readOnly: this.readOnly,},
                        { field: 'main.dispatchDate',  xtype:'datetimefield',fieldLabel: _lang.FlowWarehousePlanning.fDispatchDate, cls:'col-2', format: curUserInfo.dateFormat,readOnly: this.readOnly,}


                ]
                },
                { xtype: 'container',cls:'row', items:  [
                    {  field: 'section', xtype: 'container', cls: 'row', items: [
                        { field: 'main.warehouseId', xtype: 'hidden'},
                        { field: 'main.destinationPlace', xtype: 'hidden'},
                        { field: 'warehouse', xtype: 'WarehouseDialog', fieldLabel: _lang.FlowWarehousePlanning.fWarehouse, cls:'col-2', allowBlank: false,
                            formId: conf.mainFormPanelId, formGridPanelId: conf.formGridPanelId, hiddenName: 'main.warehouseId', single:true, readOnly: this.readOnly,
                            subcallback: function(rows){
                                if(!!rows && rows.length > 0){
                                    scope.editFormPanel.getCmpByName('warehouse').setValue(rows[0].raw.warehouse);
                                    scope.editFormPanel.getCmpByName('main.warehouseId').setValue(rows[0].raw.warehouse);
                                    scope.editFormPanel.getCmpByName('destinationPlace').setValue(rows[0].raw.address);
                                    scope.editFormPanel.getCmpByName('main.destinationPlace').setValue(rows[0].raw.address);
                                }
                            }
                        },
                        { field: 'destinationPlace',  xtype:'displayfield',fieldLabel:_lang.FlowWarehousePlanning.fDestAddress, cls:'col-2',},
                    ]},

                    { field: 'main.receiveStartDate',  xtype:'datetimefield',fieldLabel:_lang.FlowWarehousePlanning.fReceiveStartDate, cls:'col-2', format: curUserInfo.dateFormat,readOnly: this.readOnly, },
                    { field: 'main.receiveEndDate',  xtype:'datetimefield',fieldLabel:_lang.FlowWarehousePlanning.fReceiveEndDate, cls:'col-2', format: curUserInfo.dateFormat, readOnly: this.readOnly,},
                    // { field: 'main.dispatchDate', xtype: 'hidden', fieldLabel: _lang.FlowWarehousePlanning.fDispatchDate, cls:'col-2', format: curUserInfo.dateFormat },
                    { field: 'main.remark', xtype: 'hidden'},
                ] },
            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });
        
        // 加载表单对应的数据
		if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
			Ext.getCmp(conf.mainFormPanelId).loadData({
				url : conf.urlGet + '?id=' + this.record.id,
				preName : 'main', loadMask:true, success:function(response){
					var json = Ext.JSON.decode(response.responseText);
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('warehouse').setValue(json.data.warehouseId);
					if(!!json.data && !!json.data.details && json.data.details.length > 0 ){
						for(index in json.data.details){
							var order = {};
							Ext.applyIf(order, json.data.details[index]);
							Ext.apply(order, json.data.details[index].order);
							Ext.getCmp(conf.formGridPanelId).getStore().add(order);
						}
					}
				}
			});	
		}
        //if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
		this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount',]);
    },  // end of the init

    saveData: function(conf){
        console.log('save data');
        var formGrid = Ext.getCmp(conf.formGridPanelId);
        var viewGrid = Ext.getCmp(conf.mainGridPanelId);
        var form = this.editFormPanel;
        var dispatchDate = form.getCmpByName('main.dispatchDate').getRawValue();
        var originAddress = form.getCmpByName('main.originPlace').getValue();
        var destAddress = form.getCmpByName('main.destinationPlace').getValue();
        var receiveStartDate = form.getCmpByName('main.receiveStartDate').getRawValue();
        var receiveEndDate = form.getCmpByName('main.receiveEndDate').getRawValue();
        var time1 = null, time2 = null;
        if(!!receiveStartDate) {
            time1 = receiveStartDate.split(' ');
            if(time1.length > 1) time1 = time1[1];
        }

        if(!!receiveEndDate) {
            time2 = receiveEndDate.split(' ');
            if(time2.length > 1) time2 = time2[1];
        }

        //update main view grid
        var data = formGrid.getSelectionModel().selected.items;
        var tmp = [];
        for(var i = 0; i < data.length; i++) tmp.push(data[i].data);
        data = tmp;
        for(var i = 0; i < data.length; i++) {
            var item = data[i];
            item.group = '' + dispatchDate + ' delivery from ' + originAddress + ' to ' + destAddress
                + ', between ' + time1 + ' and ' + time2;
            item.dispatchDate = dispatchDate;
            item.originAddress = originAddress;
            item.destAddress = destAddress;
            item.receiveStartDate = receiveStartDate;
            item.receiveEndDate = receiveEndDate;
        }

        // console.log(data);
        viewGrid.getStore().add(data);
    },

    loadData: function(conf){
         console.log('load data');
        var formGrid = Ext.getCmp(conf.formGridPanelId);
        var viewGrid = Ext.getCmp(conf.mainGridPanelId);
        var form = Ext.getCmp(conf.mainFormPanelId);
        console.log(viewGrid);
        var selectedItems = viewGrid.getSelectionModel().selected.items;
        if(!selectedItems || selectedItems.length == 0) return false;

        var item = selectedItems[0].data;
        // console.log(item);
        form.getCmpByName('main.dispatchDate').setValue(item.dispatchDate);
        form.getCmpByName('main.originAddress').setValue(item.originAddress);
        form.getCmpByName('main.destAddress').setValue(item.destAddress);
        form.getCmpByName('main.receiveStartDate').setValue(item.receiveStartDate);
        form.getCmpByName('main.receiveEndDate').setValue(item.receiveEndDate);

        //select items in the group
        // console.log('select');
        var viewStoreData = viewGrid.getStore().getRange();
        var selectedContainers = [];
        for(var i = 0; i < viewStoreData.length; i++){
            if(viewStoreData[i].data.groupId == item.groupId) {
                selectedContainers.push(viewStoreData[i].data.containerNumber);
            }
        }

        formGrid.selectedContainers = selectedContainers;
        formGrid.getStore().add(data);
    },

    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },

    saveFun: function(action, isFlow){
        // console.log('save data');
        var formGrid = Ext.getCmp(this.formGridPanelId);
        var viewGrid = Ext.getCmp(this.mainGridPanelId);

        var form = Ext.getCmp(this.mainFormPanelId);
        var dispatchDate = form.getCmpByName('main.dispatchDate').getRawValue();
        var originAddress = form.getCmpByName('main.originPlace').getValue();
        var destAddress = form.getCmpByName('main.destinationPlace').getValue();
        var receiveStartDate = form.getCmpByName('main.receiveStartDate').getRawValue();
        var receiveEndDate = form.getCmpByName('main.receiveEndDate').getRawValue();
        var remark = 'Delivery from ' + originAddress + ' to ' + destAddress
            + ', between ' + receiveStartDate + ' and ' + receiveEndDate;
        form.getCmpByName('main.remark').setValue(remark);

        //update main view grid
        var data = formGrid.getSelectionModel().selected.items;
        var tmp = [];
        for(var i = 0; i < data.length; i++) tmp.push(data[i].data);
        data = tmp;

        // var groupId = Ext.id();
        for(var i = 0; i < data.length; i++) {
            var item = data[i];
            // item.groupId = groupId;
            item.remark = remark;
            item.dispatchDate = dispatchDate;
            item.originAddress = originAddress;
            item.destAddress = destAddress;
            item.receiveStartDate = receiveStartDate;
            item.receiveEndDate = receiveEndDate;
        }


        //post
    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});

        if(rows.length>0){
            for(index in rows){
                params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                for(key in rows[index].data){
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