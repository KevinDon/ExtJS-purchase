FlowBalanceRefundForm = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;

        var conf = {
            title : _lang.FlowBalanceRefund.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowBalanceRefund',
            winId : 'FlowBalanceRefundForm',
            frameId : 'FlowBalanceRefundView',
            mainGridPanelId : 'FlowBalanceRefundGridPanelID',
            mainFormPanelId : 'FlowBalanceRefundFormPanelID',
            processFormPanelId: 'FlowBalanceRefundProcessFormPanelID',
            searchFormPanelId: 'FlowBalanceRefundSearchFormPanelID',
            mainTabPanelId: 'FlowBalanceRefundMainTabsPanelID',
            subFormGridPanelId : 'FlowBalanceRefundSubGridPanelID',
            formGridPanelId : 'FlowBalanceRefundFormGridPanelID',

            urlList: __ctxPath + 'flow/finance/balanceRefund/list',
            urlSave: __ctxPath + 'flow/finance/balanceRefund/save',
            urlDelete: __ctxPath + 'flow/finance/balanceRefund/delete',
            urlGet: __ctxPath + 'flow/finance/balanceRefund/get',
            urlFlow: __ctxPath + 'flow/finance/balanceRefund/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowBalanceRefund&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
            saveFun: this.saveRow,
            flowSaveFun: this.flowSaveFun,
        };

        this.initUIComponents(conf);
        FlowBalanceRefundForm.superclass.constructor.call(this, {
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
                {field:'main.FlowWindow', xtype:'FlowWindow', width:'100%', height:400, conf: conf, hidden: !this.isApprove || this.isAdd, allowBlank:true, },

                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id, allowBlank:true,  },
                
                //登记信息
                { xtype: 'section', title:_lang.FlowBalanceRefund.tabRefundInfomation},
                { xtype: 'container',cls:'row', scope: this, items: [
                    { field: 'main.type', xtype: 'dictcombo', fieldLabel:_lang.FlowBalanceRefund.fRefundType, value:'1', cls:'col-2', code:'purchase', codeSub:'refund_type',
                        allowBlank: false, scope: this, selectFun: function(records, eOpts){
                            //修改显示的费用类型
                            this.scope.changeType.call(this, records[0].data.id, null, conf.mainFormPanelId);
                        },
                    },
                    { field: 'main.chargebackReason', xtype: 'textarea', fieldLabel: _lang.FlowBalanceRefund.fChargebackReason,cls:'col-1'},
                    { field: 'main.remark', xtype: 'textarea', fieldLabel: _lang.TText.fRemark,cls:'col-1'},
                ],
                },
                // 基础信息
                {xtype: 'container',cls:'row', items:  [
                    { xtype: 'container', id: conf.mainFormPanelId + '-vendorContainer', allowBlank:true,  cls:'row', items: $groupFormVendorFields(this, conf, {
                        allowBlank:true, readOnly:this.readOnly, hideDetails: this.action == 'add', formId: conf.mainFormPanelId,
                            callback: function (eOpts, record) {
                                var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                cmpFormPlanId.vendorId = record.id;
                                cmpFormPlanId.currency = record.currency;
                                cmpFormPlanId.getCmpByName('main.currency').setValue(record.currency);
                                cmpFormPlanId.orderId = '';
                                cmpFormPlanId.getCmpByName('main.orderId').setValue('');
                                cmpFormPlanId.getCmpByName('main.orderNumber').setValue('');
                                cmpFormPlanId.getCmpByName('main.orders').setValue('');
                                cmpFormPlanId.getCmpByName('main.orderName').setValue('');
                                cmpFormPlanId.getCmpByName('main.product').formGridPanel.getStore().removeAll();

                                cmpFormPlanId.getCmpByName('main.samplePaymentId').setValue('');
                                cmpFormPlanId.getCmpByName('main.samplePaymentName').setValue('');
                                cmpFormPlanId.getCmpByName('main.sampleName').setValue('');
                                cmpFormPlanId.getCmpByName('main.samples').setValue('');
                                cmpFormPlanId.getCmpByName('main.sample').setValue('');

                                cmpFormPlanId.getCmpByName('main.totalFeeAud').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeRmb').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeUsd').setValue(0);
                            },
                        }),
                    },
                    { xtype: 'container',id: conf.mainFormPanelId + '-serviceProviderContainer', cls:'row', allowBlank:true,  hidden:true, items: $groupFormServiceProviderFields(this, conf, {
                        hideDetails: this.action == 'add', readOnly: this.readOnly,
                            callback: function (eOpts, record) {
                                var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                cmpFormPlanId.getCmpByName('main.vendorId').setValue(record.id);
                                cmpFormPlanId.vendorId = record.id;
                                cmpFormPlanId.currency = record.currency;
                                cmpFormPlanId.getCmpByName('main.currency').setValue(record.currency);
                                cmpFormPlanId.getCmpByName('main.totalFeeAud').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeRmb').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeUsd').setValue(0);
                            },
                        }),
                    },

                    { xtype: 'container',id: conf.mainFormPanelId + '-orderContainer', allowBlank:true, cls:'row', items:[
                        { xtype: 'section', title:_lang.FlowBalanceRefund.tabOrderOrProduct},
                        { field:'main.orderId', xtype:'hidden'},
                        { field: 'main.orderNumber', xtype: 'OrderDialog', fieldLabel: _lang.FlowOrderQualityInspection.fOrderNumber, cls:'col-2',
                            hiddenName:'main.orderId', formId: conf.mainFormPanelId, single: true,type:2, readOnly: this.readOnly,
                            subcallback: function(rows){
                                var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                if(rows) {
                                    cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(rows.data.rateAudToRmb);
                                    cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(rows.data.rateAudToUsd);
                                }
                                cmpFormPlanId.getCmpByName('main.product').setValue('');
                                cmpFormPlanId.getCmpByName('main.samplePaymentId').setValue('');
                                cmpFormPlanId.getCmpByName('main.sample').setValue('');

                                cmpFormPlanId.getCmpByName('main.totalFeeAud').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeRmb').setValue(0);
                                cmpFormPlanId.getCmpByName('main.totalFeeUsd').setValue(0);
                            }
                        },
                        { field:'main.samplePaymentId', xtype:'hidden'},
                        { field: 'main.samplePaymentName', xtype: 'FlowOtherDialog', fieldLabel: _lang.FlowSamplePayment.tabBusinessId,
                            hiddenName:'main.samplePaymentId', cls:'col-2', frameId:conf.mainFormPanelId, single:true, displayType:6, filterVendor: true, fieldVendorIdName: 'main.vendorId',
                            hidden:true, readOnly: this.readOnly, scope:this,
                            subcallback: function(id, title, rows){
                                var cmpAtta = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.sample').formGridPanel.getStore();
                                cmpAtta.removeAll();
                                if(!!rows  && !!rows.data && !!rows.data.details && rows.data.details.length>0){
                                    var totalFeeAuds =0, totalFeeRmbs =0, totalFeeUsds=0;
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.sample').vendorId  = rows.data.vendorId;
                                    for(var i = 0; i < rows.data.details.length; i++){
                                        if(rows.data.details[i].sampleFeeRefund == 1) {
                                            var product = {};
                                            Ext.applyIf(product, rows.data.details[i]);
                                            product.id = rows.data.details[i].product.id;
                                            product.priceAud = rows.data.details[i].sampleFeeAud;
                                            product.priceRmb = rows.data.details[i].sampleFeeRmb;
                                            product.priceUsd = rows.data.details[i].sampleFeeUsd;
                                            product.diffAud = rows.data.details[i].sampleFeeAud * rows.data.details[i].qty;
                                            product.diffRmb = rows.data.details[i].sampleFeeRmb * rows.data.details[i].qty;
                                            product.diffUsd = rows.data.details[i].sampleFeeUsd * rows.data.details[i].qty;
                                            product.diffQty = rows.data.details[i].qty;

                                            totalFeeAuds += product.diffAud;
                                            totalFeeRmbs += product.diffRmb;
                                            totalFeeUsds += product.diffUsd;

                                            cmpAtta.add(product);
                                        }
                                    }
                                    var cmpFormPlanId = Ext.getCmp(conf.mainFormPanelId);
                                    cmpFormPlanId.getCmpByName('main.totalFeeAud').setValue(totalFeeAuds);
                                    cmpFormPlanId.getCmpByName('main.totalFeeRmb').setValue(totalFeeRmbs);
                                    cmpFormPlanId.getCmpByName('main.totalFeeUsd').setValue(totalFeeUsds);
                                    cmpFormPlanId.getCmpByName('main.rateAudToRmb').setValue(rows.data.rateAudToRmb);
                                    cmpFormPlanId.getCmpByName('main.rateAudToUsd').setValue(rows.data.rateAudToUsd);
                                }else{
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.sample').vendorId  = '___';
                                }


                            }
                        }
                    ]} ,
                ] },

                { xtype: 'hidden', field: 'main.currency',  },
                { xtype: 'section', title:_lang.FlowBalanceRefund.tabRefundInfomationDetail},
                { xtype: 'container', id: conf.mainFormPanelId + '-asnContainer', cls:'row',  items:  [
                    { field: 'main.orders', xtype:'hidden', value: this.record.orders },
                    { field: 'main.orderName', xtype:'hidden'},
                    { field: 'main.product', xtype: 'FlowBalanceRefundFormGrid', fieldLabel: _lang.FlowBalanceRefund.tabRefundInfomation, readOnly:this.readOnly },
                ]},

                { xtype: 'container',cls:'row', id: conf.mainFormPanelId + '-projectContainer', hidden:true,items:  [
                    { field: 'main.project', xtype: 'FlowBalanceRefundProjectFormGrid',  scope: this, fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true, readOnly:this.readOnly, },
                ] },

                { xtype: 'container', id: conf.mainFormPanelId + '-sampleContainer', hidden: true, cls:'row',  items:  [
                    { field: 'main.samples', xtype:'hidden', value: this.record.samples },
                    { field: 'main.sampleName', xtype:'hidden'},
                    { field: 'main.sample', xtype: 'FlowBalanceRefundSampleFormGrid', fieldLabel: _lang.FlowBalanceRefund.tabRefundInfomation, readOnly:true},
                ]},

                //总金额
                { xtype: 'container',cls:'row', items:  [
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.totalFeeAud', xtype: 'numberfield', fieldLabel: _lang.ProductDocument.fTotalPriceAud, cls:'col-1', readOnly:true, editor:{xtype:'textfield'}, allowBlank:true,  },
                        { field: 'main.totalFeeRmb', xtype: 'numberfield', fieldLabel: _lang.ProductDocument.fTotalPriceRmb, cls:'col-1', readOnly:true, allowBlank:true,  },
                        { field: 'main.totalFeeUsd', xtype: 'numberfield', fieldLabel: _lang.ProductDocument.fTotalPriceUsd, cls:'col-1',readOnly:true,  allowBlank:true, },
                    ]},
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.rateAudToRmb',fieldLabel:_lang.FlowDepositContract.fRateAudToRmb,  xtype: 'numberfield', decimalPrecision:4,
                            value: curUserInfo.audToRmb, allowBlank:true, readOnly:true,  cls:'col-1' },
                        { field: 'main.rateAudToUsd', fieldLabel: _lang.FlowDepositContract.fRateAudToUsd, xtype: 'numberfield', decimalPrecision:4,
                            value: curUserInfo.audToUsd, allowBlank:true, readOnly:true, cls:'col-1' },
                    ] },
                ] },


            ].concat($flowCreatorInfo( {isApprove : this.isApprove, isComplated : this.isComplated}))
        });


        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){

                    var json = Ext.JSON.decode(response.responseText);
                    var bank = json.data.vendor ? json.data.vendor.bank : '';
                    var type = json.data.type;

                    Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').hide();
                    Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').hide();
                    Ext.getCmp(conf.mainFormPanelId + '-projectContainer').hide();
                    Ext.getCmp(conf.mainFormPanelId + '-asnContainer').hide();
                    Ext.getCmp(conf.mainFormPanelId + '-sampleContainer').hide();
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderNumber').hide();
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.samplePaymentName').hide();

                    if(type == '1' || type == '3'){
                        Ext.getCmp(conf.mainFormPanelId + '-vendorContainer').show();

                        if(type == '1'){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.orderNumber').show();
                            Ext.getCmp(conf.mainFormPanelId + '-asnContainer').show();

                            //details
                            if(!!json.data && !!json.data.details && json.data.details.length>0){
                                Ext.getCmp(conf.mainFormPanelId).vendorId = json.data.vendorId;
                                Ext.getCmp(conf.mainFormPanelId).orderId = json.data.orderId;
                                for(index in json.data.details){
                                    var product = {};
                                    Ext.applyIf(product, json.data.details[index]);
                                    product.subRemark = product.remark;
                                    Ext.getCmp(conf.subFormGridPanelId + '-product').getStore().add(product);
                                }
                            }

                        }else{
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.samplePaymentName').show();
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.samplePaymentName').setValue(json.data.samplePaymentBusinessId);
                            Ext.getCmp(conf.mainFormPanelId + '-sampleContainer').show();
                            //details
                            if(!!json.data && !!json.data.details && json.data.details.length>0){
                                for(index in json.data.details){
                                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.sample').formGridPanel.getStore().add(json.data.details[index]);
                                }
                            }
                        }
                    }else if(type == '2'){
                        var cmp = Ext.getCmp(conf.mainFormPanelId);

                        cmp.getCmpByName('serviceProvider.cnName').setValue(json.data.vendor.cnName);
                        cmp.getCmpByName('serviceProvider.enName').setValue(json.data.vendor.enName);
                        cmp.getCmpByName('serviceProvider.address').setValue(json.data.vendor.address);
                        cmp.getCmpByName('serviceProvider.director').setValue(json.data.vendor.director);
                        cmp.getCmpByName('serviceProvider.abn').setValue(json.data.vendor.abn);
                        cmp.getCmpByName('serviceProvider.currency').setValue(json.data.vendor.currency);
                        cmp.getCmpByName('serviceProvider.website').setValue(json.data.vendor.website);
                        cmp.getCmpByName('serviceProvider.source').setValue(json.data.vendor.source);

                        Ext.getCmp(conf.mainFormPanelId + '-serviceProviderContainer').show();
                        Ext.getCmp(conf.mainFormPanelId + '-projectContainer').show();

                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceProviderId').setValue(json.data.vendorId);
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.serviceProviderName').setValue(json.data.vendorName);

                        //details
                        if(!!json.data && !!json.data.details && json.data.details.length>0){
                            for(index in json.data.details){
                                var project = {};
                                Ext.applyIf(project, json.data.details[index]);
                                Ext.apply(project, json.data.details[index].product);
                                project.subtotalAud =  (parseFloat(json.data.details[index].priceAud) * parseFloat(json.data.details[index].qty)).toFixed(3);
                                project.subtotalRmb=   (parseFloat(json.data.details[index].priceRmb) *  parseFloat(json.data.details[index].qty)).toFixed(3);
                                project.subtotalUsd =   (parseFloat(json.data.details[index].priceUsd) * parseFloat(json.data.details[index].qty)).toFixed(3);
                                Ext.getCmp(conf.subFormGridPanelId + '-project').getStore().add(project);
                            }
                        }
                    }



                }
            });
        }

        this.editFormPanel.setReadOnly(this.readOnly, [
            'flowRemark','flowNextHandlerAccount',
            ,'main.bankAccount','main.totalPriceRmb','main.totalPriceAud','main.totalPriceUsd',
            'main.companyCnName', 'main.companyEnName','main.companyCnAddress','main.companyEnAddress','main.swiftCode','main.cnaps','main.currency',
            'main_contractGuaranteeLetter', 'main_guaranteeLetter','main.rateAudToRmb','main.rateAudToUsd' ,'main.totalFeeAud','main.totalFeeRmb','main.totalFeeUsd'
        ]);

    },// end of the init
    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },

    saveRow: function(action, isFlow){
        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};

        var type = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.type').getValue();

        if(type == '1') {
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-product')});
            if (!!rows && rows.length > 0) {
                for (index in rows) {
                    params['details[' + index + '].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for (key in rows[index].data) {
                        if(key == 'subRemark') params['details[' + index + '].remark'] = rows[index].data[key];
                        params['details[' + index + '].' + key] = rows[index].data[key];
                    }
                }
            }
        }else if(type == '2'){
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-project')});
            if(!!rows && rows.length>0){
                for(index in rows){
                    params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for(key in rows[index].data){
                        params['details['+index+'].'+key ] = rows[index].data[key];
                    }
                }
            }
        }else if(type == '3'){
            var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId + '-sample')});
            if (!!rows && rows.length > 0) {
                for (index in rows) {
                    params['details[' + index + '].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                    for (key in rows[index].data) {
                        params['details[' + index + '].' + key] = rows[index].data[key];
                    }
                }
            }
        }


        //查看
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

    changeType: function(type, eOpts, form){
        var obj = eOpts || this.ownerCt;
        var cmp = Ext.getCmp(form);

        Ext.getCmp(form + '-projectContainer').show();

        cmp.getCmpByName('main.totalFeeAud').setValue('');
        cmp.getCmpByName('main.totalFeeRmb').setValue('');
        cmp.getCmpByName('main.totalFeeUsd').setValue('');
        Ext.getCmp(form).getCmpByName('main.project').formGridPanel.getStore().removeAll();
        //供应商信息为空
        cmp.getCmpByName('main.vendorName').setValue('');
        cmp.getCmpByName('vendor.cnName').setValue('');
        cmp.getCmpByName('vendor.enName').setValue('');
        cmp.getCmpByName('vendor.address').setValue('');
        cmp.getCmpByName('vendor.director').setValue('');
        cmp.getCmpByName('vendor.abn').setValue('');
        cmp.getCmpByName('vendor.currency').setValue('');
        cmp.getCmpByName('vendor.website').setValue('');
        cmp.getCmpByName('vendor.source').setValue('');
        Ext.getCmp(form + '-vendor').hide();

        //服务商信息
        cmp.getCmpByName('main.serviceProviderName').setValue('');
        cmp.getCmpByName('serviceProvider.cnName').setValue('');
        cmp.getCmpByName('serviceProvider.enName').setValue('');
        cmp.getCmpByName('serviceProvider.address').setValue('');
        cmp.getCmpByName('serviceProvider.director').setValue('');
        cmp.getCmpByName('serviceProvider.abn').setValue('');
        cmp.getCmpByName('serviceProvider.currency').setValue('');
        cmp.getCmpByName('serviceProvider.website').setValue('');
        cmp.getCmpByName('serviceProvider.source').setValue('');
        Ext.getCmp(form + '-serviceProvider').hide();

        Ext.getCmp(form + '-vendorContainer').hide();
        Ext.getCmp(form + '-serviceProviderContainer').hide();
        Ext.getCmp(form + '-projectContainer').hide();
        Ext.getCmp(form + '-asnContainer').hide();
        Ext.getCmp(form + '-sampleContainer').hide();
        Ext.getCmp(form + '-orderContainer').hide();
        Ext.getCmp(form).getCmpByName('main.orderNumber').hide();
        Ext.getCmp(form).getCmpByName('main.samplePaymentName').hide();

        if(type == 1 || type == 3){
            Ext.getCmp(form + '-vendorContainer').show();

            if(type == 1){
                Ext.getCmp(form + '-orderContainer').show();
                Ext.getCmp(form).getCmpByName('main.orderNumber').show();
                Ext.getCmp(form + '-asnContainer').show();

            }else{
                Ext.getCmp(form).getCmpByName('main.samplePaymentName').show();
                Ext.getCmp(form + '-orderContainer').show();
                Ext.getCmp(form + '-sampleContainer').show();
            }

        }else if(type == 2){
            Ext.getCmp(form + '-serviceProviderContainer').show();
            Ext.getCmp(form + '-projectContainer').show();
        }
    }
});