FlowSampleForm = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;

        var conf = {
            title : _lang.FlowSample.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowSample',
            winId : 'FlowSampleForm',
            frameId : 'FlowSampleForm',
            mainGridPanelId : 'FlowSampleViewGridPanelID',
            mainFormPanelId : 'FlowSampleViewFormPanelID',
            processFormPanelId: 'FlowSampleViewProcessFormPanelID',
            searchFormPanelId: 'FlowSampleViewSearchFormPanelID',
            mainTabPanelId: 'FlowSampleViewMainTbsPanelID',
            subGridPanelId : 'FlowSampleViewSubGridPanelID',
            formGridPanelId: 'FlowSampleFormGridPanelID',
            subFormGridPanelId:'FlowSampleFormOtherGridPanelID',
            urlList: __ctxPath + 'flow/purchase/flowsample/list',
            urlSave: __ctxPath + 'flow/purchase/flowsample/save',
            urlDelete: __ctxPath + 'flow/purchase/flowsample/delete',
            urlGet: __ctxPath + 'flow/purchase/flowsample/get',
            urlFlow: __ctxPath + 'flow/purchase/flowsample/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowSample&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        FlowSampleForm.superclass.constructor.call(this, {
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

                //   供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf, {allowBlank: false,
                    hideDetails: this.action == 'add',
                    callback: function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.currency').setValue(record.currency.toString())
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    }
                })},
                // { xtype: 'container',cls:'row', items:  [
                //     {field: 'mian.sampleOutboundDate', xtype: 'datetimefield', fieldLabel: _lang.FlowSample.fSampleOutboundDate, format: curUserInfo.dateFormat,  cls:'col-2',},
                //     {field: 'main.sampleWarehouseDate', xtype: 'datetimefield', fieldLabel: _lang.FlowSample.fSampleWarehouseDate, format: curUserInfo.dateFormat,  cls:'col-2',},
                // ] },


                { xtype: 'section', title:_lang.ProductDocument.tabListTitle},
                { xtype: 'container',cls:'col-1', items:  [
                    { field: 'main.currency', xtype: 'dictcombo', code:'transaction', codeSub:'currency', fieldLabel: _lang.FlowSamplePayment.fCurrency, cls:'col-2'},

                ] },
                { xtype: 'container',cls:'col-1', items:  [
                    {field: 'main.rateAudToRmb', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToRmb, cls: 'col-2', value: curUserInfo.audToRmb, readOnly: true,  },
                    {field: 'main.rateAudToUsd', xtype:'textfield', fieldLabel: _lang.ExchangeRate.fRateAudToUsd, cls: 'col-2', value: curUserInfo.audToUsd, readOnly: true,  },
                ] },

                { xtype: 'container', cls:'col-1', items:
                    [
                        { xtype: 'container', cls:'col-2', items:
                            $groupPriceFields(this.editFormPanel, {
                                decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                                aud: {field: 'main.totalSampleFeeAud', xtype: 'textfield', fieldLabel: _lang.FlowSamplePayment.fTotalSampleFeeAud,},
                                rmb:  { field: 'main.totalSampleFeeRmb', xtype: 'textfield', fieldLabel: _lang.FlowSamplePayment.fTotalSampleFeeRmb, },
                                usd: { field: 'main.totalSampleFeeUsd', xtype: 'textfield', fieldLabel: _lang.FlowSamplePayment.fTotalSampleFeeUsd, },
                            }),
                        },
                        { xtype: 'container', cls:'col-2', items: $groupPriceFields(this.editFormPanel, {
                            decimalPrecision: 2, cls: 'col-1', readOnly: this.readOnly, allowBlank: false, formId: conf.mainFormPanelId, updatePriceFlag: this.readOnly ? 1 : null,
                            aud:{field:'main.totalOtherAud', fieldLabel:_lang.FlowPurchaseContract.fOtherItemAud, value:0},
                            rmb:{field:'main.totalOtherRmb', fieldLabel:_lang.FlowPurchaseContract.fOtherItemRmb, value:0},
                            usd:{field:'main.totalOtherUsd', fieldLabel:_lang.FlowPurchaseContract.fOtherItemUsd, value:0}
                        })},
                    ]
                },

                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowSampleFormGrid', fieldLabel: _lang.FlowNewProduct.fProductList, scope:this, readOnly: this.readOnly,
                    dataChangeCallback: this.onGridDataChange,

                },

                //其他费用
                { xtype: 'section', title: _lang.FlowFeeRegistration.tabOtherItem},
                { field: 'main.otherContainer', xtype: 'FlowSampleOtherFormGrid',  scope: this,  fieldLabel: _lang.ProductDocument.fTotalPriceAud, allowBlank:true,
                    hidden:true,readOnly: this.readOnly,
                    dataChangeCallback: this.onGridDataChange,
                    formGridPanelId:conf.subFormGridPanelId,
                    mainFormPanelId: conf.mainFormPanelId
                },
                // 备注
                {xtype: 'section', title: _lang.FlowCustomClearance.tabRemark},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 300,  allowBlank:true,  readOnly: this.readOnly},
                ] },
                //   附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, 
                	mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, 
                	scope:this, readOnly:this.readOnly
                },

            ].concat($flowCreatorInfo({isApprove : this.isApprove, isComplated : this.isComplated}))
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
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
                            product.orderValueAud = (json.data.details[index].sampleFeeAud *json.data.details[index].qty).toFixed(3);
                            product.orderValueRmb = (json.data.details[index].sampleFeeRmb *json.data.details[index].qty).toFixed(3);
                            product.orderValueUsd = (json.data.details[index].sampleFeeUsd *json.data.details[index].qty).toFixed(3);
                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                        }
                    }
                        //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);

                    if(!!json.data && !!json.data.otherDetails && json.data.otherDetails.length>0) {
                        for (var index in json.data.otherDetails) {
                            var otherDetails = {};
                            Ext.applyIf(otherDetails, json.data.otherDetails[index]);
                            otherDetails.subtotalAud =  (parseFloat(json.data.otherDetails[index].priceAud) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            otherDetails.subtotalRmb=   (parseFloat(json.data.otherDetails[index].priceRmb) *  parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            otherDetails.subtotalUsd =   (parseFloat(json.data.otherDetails[index].priceUsd) * parseFloat(json.data.otherDetails[index].qty)).toFixed(2);
                            Ext.getCmp(conf.subFormGridPanelId).getStore().add(otherDetails);
                        }
                    }
                }
            });
        }
//    if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
        this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount','main.rateAudToRmb', 'main.rateAudToUsd']);

    },// end of the init

    onGridDataChange: function(store, conf){
        var totalPriceAud = 0, totalPriceRmb = 0, totalPriceUsd = 0;

        var totalOtherAud = 0, totalOtherRmb = 0,  totalOtherUsd = 0;
        var totalValueAud = 0, totalValueRmb = 0, totalValueUsd = 0;
        var $currency = this.editFormPanel.getCmpByName('main.currency').getValue();;

        // var data = store.getRange();
        var data = this.editFormPanel.getCmpByName('main.products').formGridPanel.getStore().data.items;
        var otherData = this.editFormPanel.getCmpByName('main.otherContainer').formGridPanel.getStore().data.items;

        //总体积、货值总金额
        if(!!data && data.length >0) {
            for (var i = 0; i < data.length; i++) {
                totalValueAud = totalValueAud + parseFloat(data[i].data.orderValueAud);
                totalValueRmb = totalValueRmb + parseFloat(data[i].data.orderValueRmb);
                totalValueUsd = totalValueUsd + parseFloat(data[i].data.orderValueUsd);
            }
        }

        //其他费用的总金额
        if(!!otherData && otherData.length >0){
            for (var i = 0; i < otherData.length; i++) {
                totalOtherAud = totalOtherAud +  parseFloat(otherData[i].data.subtotalAud || 0.00);
                totalOtherRmb = totalOtherRmb + parseFloat(otherData[i].data.subtotalRmb || 0.00);
                totalOtherUsd = totalOtherUsd + parseFloat(otherData[i].data.subtotalUsd|| 0.00);
            }
        }

        //合同总金额
        totalPriceAud = totalValueAud + totalOtherAud;
        totalPriceRmb = totalValueRmb + totalOtherRmb;
        totalPriceUsd = totalValueUsd +  totalOtherUsd;


        if($currency == '1'){
            // this.editFormPanel.getCmpByName('main.totalPriceAud').setValue(totalPriceAud.toFixed(2));
            //计算总金额
            // totalPriceAud = totalPriceAud.toFixed(2);
            // totalPriceRmb = (totalPriceAud * audToRmb).toFixed(2);
            // totalPriceUsd = (totalPriceAud * audToUsd).toFixed(2);


            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherAud').setValue(totalOtherAud.toFixed(2));
            this.editFormPanel.getCmpByName('main.totalSampleFeeAud').setValue(totalPriceAud);

        }else if($currency == '2'){
            //计算总金额
            // totalPriceAud = (totalPriceRmb / audToRmb).toFixed(2);
            // totalPriceRmb = totalPriceRmb.toFixed(2);
            // totalPriceUsd = (totalPriceRmb / audToRmb * audToUsd).toFixed(2);
            // this.editFormPanel.getCmpByName('main.totalPriceRmb').setValue(totalPriceRmb.toFixed(2));

            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherRmb').setValue(totalOtherRmb.toFixed(2));
            this.editFormPanel.getCmpByName('main.totalSampleFeeRmb').setValue(totalPriceRmb);

        }else if($currency == '3'){
            //计算总金额
            // totalPriceAud = (totalPriceUsd / audToUsd).toFixed(2);
            // totalPriceRmb = (totalPriceUsd * audToRmb / audToUsd).toFixed(2);
            // totalPriceUsd = totalPriceUsd.toFixed(2);
            // this.editFormPanel.getCmpByName('main.totalPriceUsd').setValue(totalPriceUsd.toFixed(2))

            //设置其他费用总金额
            this.editFormPanel.getCmpByName('main.totalOtherUsd').setValue(totalOtherUsd.toFixed(2));
            this.editFormPanel.getCmpByName('main.totalSampleFeeUsd').setValue(totalPriceUsd);
        }
    },

    flowSaveFun: function(action){
        this.saveFun(action, 1);
    },
    saveFun: function(action, isFlow){

    	var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var flag = true;
        var productsRows = false;
        var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
        if(!rows) productsRows = true;
    	if(!!rows && rows.length>0){
			for(index in rows){
                if(!rows[index].data['qty'] || rows[index].data['qty']  < 0 || rows[index].data['qty'] == ''){
                    //数量为空时不能保存
                    flag = false;
                }
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
        if(!!rows && rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

        var rows = $getGdItems({grid: Ext.getCmp(this.subFormGridPanelId)});
        if(!!rows && rows.length>0){
            for(index in rows){
                if(!rows[index].data['priceAud'] || rows[index].data['priceAud']  < 0 || rows[index].data['priceAud'] == ''){
                    //价格时不能保存
                    flag = false;
                }
                for(key in rows[index].data){
                    params['otherDetails['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if((params.act == 'refuse' || params.act == 'redo' || params.act == 'back') && flowRemark == ''){
            //退回、返审、拒绝时FlowRemark为空时提示不给过
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowRemarkError);
        } else if(!flag) {
            //价格为空或采购数量为空时不能保存
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSamplePriceError);
        }else if(productsRows){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSampleProductError);
        }else{
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url:isFlow ? this.urlFlow: this.urlSave,
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