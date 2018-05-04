FlowProductQuotationForm = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isApprove =$getIsApprove(this.action, this.record);
        this.isComplated =$getIsComplated(this.action, this.record);
        this.isAdd = $getIsAdd(this.action, this.record);
        this.isStart = $getIsStart(this.action, this.record);
        this.readOnly = (this.isAdd || this.isStart) ? false : this.isApprove || this.isComplated;
        this.isCanceled = this.record.status == 4 ? true : false;

        var conf = {
            title : _lang.FlowProductQuotation.mTitle + $getTitleSuffix(this.action),
            moduleName: 'FlowProductQuotation',
            winId : 'FlowProductQuotationForm',
            frameId : 'FlowProductQuotationView',
            mainGridPanelId : 'FlowProductQuotationViewGridPanelID',
            mainFormPanelId : 'FlowProductQuotationViewFormPanelID',
            processFormPanelId: 'FlowProductQuotationProcessFormPanelID',
            searchFormPanelId: 'FlowProductQuotationViewSearchFormPanelID',
            mainTabPanelId: 'FlowProductQuotationViewMainTbsPanelID',
            subGridPanelId : 'FlowProductQuotationViewSubGridPanelID',
            formGridPanelId : 'FlowProductQuotationViewFormGridPanelID',

            urlList: __ctxPath + 'flow/purchase/quotation/list',
            urlSave: __ctxPath + 'flow/purchase/quotation/save',
            urlDelete: __ctxPath + 'flow/purchase/quotation/delete',
            urlGet: __ctxPath + 'flow/purchase/quotation/get',
            urlFlow: __ctxPath + 'flow/purchase/quotation/approve',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list?Q-businessId-S-EQ=' + this.record.id,
            urlDirection: __ctxPath + 'flow/workflow/viewimage?processDefinitionKey=FlowProductQuotation&processInstanceId=' + (this.isApprove? this.record.processInstanceId: ''),
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
        console.log((!this.isAdd) && this.isApprove && this.record.flowStatus==1);
        this.initUIComponents(conf);
        FlowProductQuotationForm.superclass.constructor.call(this, {
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

                // { field: 'main_products', xtype:'hidden', value:this.record.products },
                // { field: 'main_productsName', xtype:'hidden'},
                //供应商基础信息
                { xtype: 'container',cls:'row', items: $groupFormVendorFields(this, conf,{
                    hideDetails: this.action == 'add',
                    callback: function(eOpts, record){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.products').formGridPanel.getStore().removeAll();
                    }

                })},
                //报价明细
                { xtype: 'section', title:_lang.FlowProductQuotation.fQuoteDetail},
                { xtype: 'container',cls:'row', items:  [
                    {field: 'effectiveDate', xtype: 'datetimefield', fieldLabel: _lang.FlowProductQuotation.fEffectiveDate, format: curUserInfo.dateFormat, readOnly:this.readOnly, cls:'col-2',
                        allowBlank:false,value:new Date(),
                    },
                    {field: 'validUntil', xtype: 'datetimefield', fieldLabel: _lang.FlowProductQuotation.fValidUntil, format: curUserInfo.dateFormat, readOnly:this.readOnly, cls:'col-2',
                        allowBlank:false,value:Ext.Date.add(new Date(), Ext.Date.MONTH, 1),
                    },
                ] },

                //新品列表
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { field: 'main.products', xtype: 'FlowProductQuotationFormGrid', fieldLabel: _lang.FlowProductQuotation.fQuoteDetail, scope:this, readOnly: this.readOnly},

                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: this.isApprove},

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
                            Ext.applyIf(product, json.data.details[index].product.prop);
                            Ext.apply(product, json.data.details[index].product);
                            Ext.getCmp(conf.formGridPanelId).getStore().add(product);
                        }
                    }
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);
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
        var flag = true;
        var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
        if(!!rows && rows.length>0){
            for(index in rows){
                if(!rows[index].data['priceAud'] || !rows[index].data['moq']){
                    //报价为空时不能保存或采购数量
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
        if(rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
        var flowRemark = Ext.getCmp(this.mainFormPanelId).getCmpByName('flowRemark').getValue();

        if(!flag){
            //报价为空时不能保存或采购数量为空时提示
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsPriceError);
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
    }
});