FlowCustomClearanceForm = Ext.extend(Ext.Panel, {
    constructor: function(conf) {
        Ext.applyIf(this, conf);
        
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;

        var conf = {
            scope: this,
            title : _lang.FlowCustomClearance.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowCustomClearance',
            winId : 'FlowCustomClearanceForm',
            frameId : 'FlowCustomClearanceView',
            mainGridPanelId : 'FlowCustomClearanceViewGridPanelID',
            mainFormPanelId : 'FlowCustomClearanceViewFormPanelID',
            processFormPanelId: 'FlowCustomClearanceProcessFormPanelID',
            searchFormPanelId: 'FlowCustomClearanceViewSearchFormPanelID',
            mainTabPanelId: 'FlowCustomClearanceViewMainTbsPanelID',
            subGridPanelId : 'FlowCustomClearanceViewSubGridPanelID',
            formGridPanelId: 'FlowCustomClearanceFormGridPanelID',

            urlList: __ctxPath + 'flow/purchase/customC learance/list',
            urlSave: __ctxPath + 'flow/purchase/customClearance/save',
            urlDelete: __ctxPath + 'flow/purchase/customClearance/delete',
            urlGet: __ctxPath + 'flow/purchase/customClearance/get',
            urlFlow: __ctxPath + 'flow/purchase/customClearance/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowCustomClearance&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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

        Ext.applyIf(this, conf);
        this.initUIComponents(conf);
        FlowCustomClearanceForm.superclass.constructor.call(this, {
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
            mainFormPanelId: conf.mainFormPanelId,
            insertPackingListPanel: this.insertPackingListPanel,
            deletePackingListPanel: this.deletePackingListPanel,
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
            	{field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                { xtype: 'section', title:_lang.FlowCustomClearance.fShippingInfo},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.orderId', xtype: 'hidden'},
                    { field: 'main.vendorId', xtype: 'hidden'},
                    { field: 'main.orderNumber', xtype: 'hidden'},
                    { field: 'main.orderTitle', xtype: 'hidden'},
                    { field: 'nextHandlerId', xtype: 'hidden'},
                    { field: 'orderNumber', xtype: 'OrderDialog', fieldLabel: _lang.FlowCustomClearance.fPurchaseOrderId, cls:'col-2',
                        formId: conf.mainFormPanelId, hiddenName: 'main.orderId', readOnly: this.readOnly, vender:'', allowBlank: false,
                        single:true, type:1, formal:formal, filter: { flagOrderShippingApplyStatus: 1, flagCustomClearanceStatus: 2},
                        subcallback: function(records){
                            //scope.editFormPanel.vendorId = records.data.vendorId;
                            scope.editFormPanel.getCmpByName('main.vendorId').setValue(records.data.vendorId);
                            scope.editFormPanel.getCmpByName('main.orderNumber').setValue(records.data.orderNumber);
                            scope.editFormPanel.getCmpByName('main.orderTitle').setValue(records.data.orderTitle);
                            scope.editFormPanel.getCmpByName('main.ciNumber').setValue(records.data.orderTitle);
                            scope.editFormPanel.getCmpByName('main.containerQty').setValue(records.data.containerQty);
                            scope.editFormPanel.containerType = records.data.containerType;
                            scope.editFormPanel.getCmpByName('main.tradeTerm').setValue(records.data.tradeTerm);
                            scope.editFormPanel.getCmpByName('main.readyDate').setValue(records.data.readyDate);
                            scope.editFormPanel.getCmpByName('main.etd').setValue(records.data.etd);
                            scope.editFormPanel.getCmpByName('main.eta').setValue(records.data.eta);
                            scope.clearPackingListPanel();
                            scope.loadOrderDetails(records.data.id, formal, true);
                        }
                    },
                    { field: 'main.ciNumber', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fCiNo, cls: 'col-2', allowBlank: false},

                ]},

                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.readyDate', xtype: 'datetimefield', fieldLabel: _lang.FlowCustomClearance.fReadyDate, format: curUserInfo.dateFormat, cls:'col-2', allowBlank: false},
                    { field: 'main.etd', xtype: 'datetimefield', fieldLabel: _lang.FlowCustomClearance.fEtd, format: curUserInfo.dateFormat, cls:'col-2', allowBlank: false},
                    { field: 'main.eta', xtype: 'datetimefield', fieldLabel: _lang.FlowCustomClearance.fEta, format: curUserInfo.dateFormat, cls:'col-2',allowBlank: false},
                    // { field: 'main.packingDate', xtype: 'datetimefield', fieldLabel: _lang.FlowCustomClearance.fPackingDate, format: curUserInfo.dateFormat, cls:'col-2', allowBlank: false},
                    { field: 'main.tradeTerm', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fTradeTerm, cls: 'col-2' ,allowBlank: false, value:'FOB', },
                    { field: 'main.vessel', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fVessel, cls: 'col-2', allowBlank: false},
                    { field: 'main.voy', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fVoy, cls: 'col-2', allowBlank: false},
                    { field: 'main.accessories', xtype:'dictcombo', code:'options', codeSub:'yesno', fieldLabel: _lang.FlowCustomClearance.fHasAccessory, cls:'col-2', allowBlank: false,value:'1'},
                    { field: 'main.containerQty', xtype: 'numberfield', fieldLabel: _lang.FlowCustomClearance.fContainerQty, cls: 'col-2', allowBlank: false, value:1, readOnly:true,},
                    { field: 'main.totalPackingCbm', xtype: 'numberfield', fieldLabel: _lang.FlowCustomClearance.fTotalPackingCbm, cls: 'col-2', allowBlank: false, value:1, readOnly:true,},
                ]},

                //装柜明细
                { xtype: 'section', title:_lang.FlowCustomClearance.tabPackingList},
                { xtype: 'container', cls:'row', id:'packingListContainer', items: []},

                //attachment
                { xtype: 'section', title:_lang.FlowCustomClearance.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.readOnly
                },
                //产品图片
                {xtype: 'section', title: _lang.FlowCustomClearance.tabImagesInformation},
                { field: 'main.images', xtype: 'hidden'},
                { field: 'main.imagesName', xtype: 'hidden'},
                { field: 'main_images', xtype: 'ImagesDialog', fieldLabel: _lang.VendorDocument.tabImages,
                    formId: conf.mainFormPanelId, hiddenName: 'main.images', titleName:'main.imagesName', fileDefType: 1, readOnly: this.readOnly,
                },

                //remark
                { xtype: 'section', title:_lang.FlowCustomClearance.tabRemark},
                { field: 'main.remark', xtype: 'textarea', fieldLabel: '', width:'100%', height:100},

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    var cmp = Ext.getCmp(conf.mainFormPanelId);
                    var rate = {};
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        for(var index in json.data.details){
                            for(var i in json.data.details[index].packingList){
                                json.data.details[index].packingList[i].srcPackingQty =  json.data.details[index].packingList[i].packingQty;
                                json.data.details[index].packingList[i].srcPackingCartons =  json.data.details[index].packingList[i].packingCartons;
                            }
                        }
                        rate = { rateAudToRmb: json.data.details[0].packingList[0].rateAudToRmb, rateAudToUsd: json.data.details[0].packingList[0].rateAudToUsd };
                        scope.loadPackingListPanel(conf, json.data.details);
                    } else {
                        scope.insertPackingListPanel();
                    }
                    if(!!json.data && !!json.data.orderId){
                        //增加汇率
                        Ext.getCmp(conf.mainFormPanelId).rate = rate;
                        //下面这句会导致计算有问题
                        // scope.loadOrderDetails(json.data.orderId, true, false);
                    }

                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(!!json.data && !!json.data.attachments ? json.data.attachments : null);

                    //images init
                    if(!!json.data && !!json.data.imagesDoc && json.data.imagesDoc.length>0){
                        for(index in json.data.imagesDoc){
                            var images = {};
                            Ext.applyIf(images, json.data.imagesDoc[index].document);
                            images.id= json.data.imagesDoc[index].documentId;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_images').dataview.getStore().add(images);
                        }
                    }

                    if(!!json.data && !!json.data.orderId) {
                        var tpData = $HpStore({
                            fields: ['vendorId'],
                            url: __ctxPath + 'purchase/purchasecontract/get?id=' + json.data.orderId,
                            callback: function (conf, records, eOpts) {
                                //console.log(records);
                                cmp.getCmpByName('main.vendorId').setValue(records[0].data.vendorId);
                            }
                        })
                    }
                }
            });


        }

        this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount', 'main.containerQty']);
        if(this.action == 'add'){
            this.insertPackingListPanel();
        }
    },// end of the init
    onGridDataChange: function(store, conf){
        var totalCbm = 0;
        var panels = this.getPackingListPanels();
        for(var i = 0; i < panels.length; i++){
            var parentContainer = panels[i].up();

            for(var j = 0; j < panels[i].items.items[1].getStore().count(); j ++){
                totalCbm = parseFloat(totalCbm) + parseFloat(panels[i].items.items[1].getStore().getAt(j).data.totalCbm);
            }
            //parentContainer.up().remove(parentContainer, true);
        }
        this.editFormPanel.getCmpByName('main.totalPackingCbm').setValue(totalCbm.toFixed(2));

    },

    loadPackingListPanel: function(conf, records){
        this.clearPackingListPanel();
        for(var i = 0; i < records.length; i++){
            this.insertPackingListPanel(records[i]);
        }
    },

    getPackingListPanels: function(){
        return Ext.getCmp(this.mainFormPanelId).query('PackingListPanel');
    },

    clearPackingListPanel: function(){
        var panels = this.getPackingListPanels();
        for(var i = 0; i < panels.length; i++){
            var parentContainer = panels[i].up();
            panels[i].items.items[0].form.reset();
            panels[i].items.items[1].getStore().removeAll();
            parentContainer.up().remove(parentContainer, true);
        }
    },

    insertPackingListPanel : function(data){
        var form = Ext.getCmp(this.mainFormPanelId);
        var newData = form.orderDetails;
        var section = Ext.getCmp('packingListContainer');
        var packingListPanelId = Math.round(Math.random() * 100000);


        //判断是否新建
        if(!data) {
            data = form.orderDetails;
        }
        section.add(
            { xtype: 'container',cls:'row', items:  [
                { xtype: 'PackingListPanel',
                  scope:this,
                  title: _lang.FlowCustomClearance.fPackingSheet,
                  formId: this.mainFormPanelId,
                  mainFormPanelId:this.mainFormPanelId,
                  mainForm: form,
                  dataChangeCallback: this.onGridDataChange,
                  insertPackingListPanel: this.insertPackingListPanel,
                  deletePackingListPanel: this.deletePackingListPanel,
                  panelId: packingListPanelId,
                  data: data,
                  readOnly: this.readOnly
                }
            ]}
        );
    },

    deletePackingListPanel: function(panel){
        var parentContainer = panel.up();
        var panels = parentContainer.up().query('PackingListPanel');
        if(panels.length <= 1){
            Ext.Msg.alert(_lang.TText.titleClew, _lang.FlowCustomClearance.msgLastPanelList);
            return false;
        } else {
            var value =  parseInt(Ext.getCmp(this.mainFormPanelId).getCmpByName('main.containerQty').getValue()) - 1;
            Ext.getCmp(this.mainFormPanelId).getCmpByName('main.containerQty').setValue(value);
            parentContainer.up().remove(parentContainer, true);
        }
    },

    loadOrderDetails: function(orderId, formal, add){
        if(!orderId) return false;
        var scope = this;
        var url = null;
        if(formal){
            url = __ctxPath + 'purchase/purchasecontract/getForMerge?id=' + orderId;
        }else{
            url = __ctxPath + 'flow/purchase/purchasecontract/getForMerge?id=' + orderId;
        }

        Ext.Ajax.request({
            url : url,
            method : 'post',
            success : function(response, options) {
                var json = Ext.JSON.decode(response.responseText);
                var totalPackingCbm = 0;
                var detailsProducts = {
                    containerType: json.data.containerType,
                    packingList: []
                };
                if(!!json.data && !!json.data.details) {
                    // var packingList = [];
                    for(var i = 0; i < json.data.details.length; i++){
                        var x = json.data.details[i];
                        var packingCartons = Math.ceil(parseInt(x.orderQty)/parseInt(x.product.prop.pcsPerCarton));
                        var totalCbm = parseFloat(x.product.prop.masterCartonCbm || 0) * parseFloat(x.cartons);
                        var totalGw = (parseFloat(x.product.prop.masterCartonGrossWeight) || 0) * packingCartons;
                        detailsProducts.packingList.push({
                            productId: x.productId,
                            sku: x.product.sku,
                            priceAud:x.priceAud,
                            priceRmb:x.priceRmb,
                            priceUsd:x.priceUsd,
                            barcode: x.product.barcode,
                            factoryCode: x.product.prop.factoryCode,
                            color: x.product.color,
                            style: x.product.style,
                            size: x.product.model,
                            orderQty: x.orderQty,
                            packingQty: x.orderQty,
                            masterCartonGrossWeight: x.product.prop.masterCartonGrossWeight || 0,
                            unitCbm: x.product.prop.masterCartonCbm || 0,
                            unitGw: x.product.prop.masterCartonGrossWeight || 0,
                            cartons: x.cartons,
                            packingCartons: packingCartons,
                            pcsPerCarton: x.product.prop.pcsPerCarton,
                            totalCbm: totalCbm.toFixed(2),
                            totalGw: totalGw.toFixed(2),
                            rateAudToRmb: x.rateAudToRmb,
                            rateAudToUsd: x.rateAudToUsd,
                            currency: x.currency,
                        });

                        totalPackingCbm = parseFloat(totalPackingCbm) + parseFloat(totalCbm.toFixed(2))
                    }
                    scope.editFormPanel.getCmpByName('main.totalPackingCbm').setValue(totalPackingCbm.toFixed(2));

                    var details= [];
                    if(json.data.containerQty>0){
                        for (var i=0; i<json.data.containerQty ; i++) details.push(detailsProducts);
                    }

                    scope.editFormPanel.orderDetails = details;
                    scope.editFormPanel.rate = { rateAudToRmb: x.rateAudToRmb, rateAudToUsd: x.rateAudToUsd, };

                    if(add){
                        scope.insertOrderSkuToPanel.call(scope);
                    }
                }
            },
            failure : function() {
            }
        });
    },

    insertOrderSkuToPanel: function(){
        var details = this.editFormPanel.orderDetails;
        if(!details || !details[0].packingList) return;

        this.clearPackingListPanel();
        for(var index in details) {
            this.insertPackingListPanel(details[index]);
        }
    },

    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){
        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'} ;
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        var panels =Ext.getCmp(this.mainFormPanelId).query('PackingListPanel');
        var packingDetailsIncomplete = false;

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
                params['details['+index+'].sealsNumber'] = editFormPanel.getCmpByName('sealsNumber').getValue();
                params['details['+index+'].containerType'] = editFormPanel.getCmpByName('containerType').getValue();

                for(var j = 0; j < packingDetails.length; j++){
                    for(var key in packingDetails[j]){
                        var attr = 'details['+ index + '].packingDetails[' + j + '].' + key;
                        params[attr] = packingDetails[j][key];
                        if(key == 'packingCartons' && (packingDetails[j][key] == null || packingDetails[j][key] == '' ) || key == 'packingQty' && (packingDetails[j][key] == null || packingDetails[j][key] == '')){
                            packingDetailsIncomplete = true;
                            break;
                        }
                    }
                }
            }
        }

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = businessId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == '') {
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        }else if( packingDetailsIncomplete){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsClearanceDetailsError);
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
