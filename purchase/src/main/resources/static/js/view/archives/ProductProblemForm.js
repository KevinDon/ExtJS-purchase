ProductProblemForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);

        this.isApprove = this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;
        var conf = {
            title : _lang.ProductProblem.mTitle,
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            urlList: __ctxPath + 'archives/troubleTicket/list',
            urlSave: __ctxPath + 'archives/troubleTicket/save',
            urlDelete: __ctxPath + 'archives/troubleTicket/delete',
            urlGet: __ctxPath + 'archives/troubleTicket/get',
            actionName: this.action,
            save: true,
            cancel: true,
            reset: true,
            // saveAs: true,
            saveFun: this.saveFun,
        };

        this.initUIComponents(conf);
        ProductProblemForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductProblem.mTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
            tbar: Ext.create("App.toolbar", conf),
            width : 1024, height : 800,
            items : this.formPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;
        this.formPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                //订单信息
                {xtype: 'section', title: _lang.ProductProblem.tabOmsOrder},
                {field: 'id', xtype: 'hidden', value: this.id == null ? '' : this.id},
                {xtype: 'container', cls: 'row', items: [
                    { field:'main.omsOrderId', xtype:'hidden'},
                    {field: 'omsOrderIdName', xtype: 'OmsOrderDialog', fieldLabel: _lang.ProductProblem.fOmsOrderId,
                        formId: conf.mainFormPanelId, cls: 'col-2', allowBlank: false, single: true, hiddenName: 'main.omsOrderId',
                        subcallback: function(rows){
                            scope.getCmpByName('main.product').formGridPanel.getStore().removeAll();
                            if(!rows) return;
                            var data = rows[0].raw;
                            scope.getCmpByName('main.sellChannel').setValue(data.shop_code);
                            scope.getCmpByName('main.memberNickname').setValue(data.user_nick);
                            scope.getCmpByName('main.transactionNumber').setValue(data.tid);
                            scope.getCmpByName('main.email').setValue(data.email);
                            scope.getCmpByName('main.orderAt').setValue(data.order_time);
                            scope.getCmpByName('main.remark').setValue(data.buyer_remark);

                            //scope.loadOmsOrder(data.order_no);
                            if(!!data && !!data.order_items) {
                                scope.initSku.call(scope, data.order_items);
                            }

                        }
                    },
                    {field: 'main.transactionNumber', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fTransactionNumber, cls: 'col-2'},
                ]},

                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.sellChannel', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fSellChannel, cls: 'col-2'},
                    {field: 'main.memberNickname', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fMemberNickname, cls: 'col-2'},
                    {field: 'main.email', xtype: 'textfield', fieldLabel: _lang.TText.fEmail, cls: 'col-2'},
                    {field: 'main.orderAt', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fOrderAt, cls: 'col-2', format: curUserInfo.dateFormat}
                ]},
                //  客户备注
                {xtype: 'section', title: _lang.ProductProblem.fRemark},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 35,  allowBlank:true, },
                ] },

                //处理人信息
                {xtype: 'section', title: _lang.FlowComplianceArrangement.fHandlerId},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.priority', xtype: 'dictcombo', fieldLabel: _lang.ProductProblem.fPriority, cls: 'col-2',
                        code:'product_problem', codeSub:'priority', allowBlank:false, value:'1'
                    },
                    {field: 'main.handleStatus', xtype: 'dictcombo', fieldLabel: _lang.ProductProblem.fHandleStatus, cls: 'col-2',
                        code:'product_problem', codeSub:'handle_status', allowBlank:false, value:'1'
                    }
                ]},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.handlerId',  xtype: 'hidden' },
                    {field: curUserInfo.lang =='zh_CN'? 'main.handlerCnName':'main.handlerEnName', xtype: 'UserDialog',
                        fieldLabel: _lang.FlowComplianceArrangement.fHandlerName, cls: 'col-2', single: true,
                        formId : conf.mainFormPanelId, hiddenName : 'main.handlerId', depId:'main.handlerDepartmentId',
                        depName:curUserInfo.lang =='zh_CN'? 'main.handlerDepartmentCnName': 'main.handlerDepartmentEnName', allowBlank : false, single:true,
                        value: curUserInfo.loginname
                    },
                    { field: 'main.handlerDepartmentId', xtype: 'hidden'},
                    {field: curUserInfo.lang =='zh_CN'? 'main.handlerDepartmentCnName':'main.handlerDepartmentEnName', xtype: 'textfield', fieldLabel: _lang.FlowComplianceArrangement.fHandlerDepartmentName, cls: 'col-2',
                        readOnly: true,
                        value: curUserInfo.lang =='zh_CN'? curUserInfo.department.cnName : curUserInfo.department.enName
                     }
                ]},
                //  产品信息
                {xtype: 'section', title: _lang.ProductCertificateDocument.tabProductListTitle},
                {field: 'main.product', xtype: 'ProductProblemFormProductGird', fieldLabel: _lang.ProductDocument.fLength, cls: 'row',
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly:true,
                    dataChangeCallback: this.onGridDataChange,
                    itemClickCallback: this.onItemClick
                },

                //tabs
                { xtype: 'container', cls:'row',  items: [
                    {field: 'productTabs', xtype:'ProductProblemFormTicketTabs', mainTabPanelId: conf.mainTabPanelId, retaleGridName: 'main.product',
                        mainFormPanelId: conf.mainFormPanelId, subGridPanelId: conf.subGridPanelId, defHeight: 300, cls:'col-1', code: 'ticket'
                    },
                ]},

                //  问题类型
                // {xtype: 'section', title: _lang.ProductProblem.tabProblemType},
                // {field: 'productTabs', xtype: 'ProductProblemFormPTicketGird', fieldLabel: _lang.ProductDocument.fLength, cls: 'row', readOnly:true,
                //     mainTabPanelId: conf.mainTabPanelId,
                //     subGridPanelId: conf.subGridPanelId, defHeight: 300, cls:'col-1', code: 'ticket'
                // },


                // 退货信息
                {xtype: 'section', title: _lang.ProductProblem.tabReturnInformation},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.returnMethod', xtype: 'dictcombo',  fieldLabel: _lang.ProductProblem.fReturnMethod, cls: 'col-2', value:'1',
                        code:'product_problem', codeSub:'return_method', allowBlank:false,
                        selectFun: function(records){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.inspectionOutcome').setValue('1');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.returnTrackingNumber').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.returnInitiated').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.returnReceived').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.returnCostAud').setValue(0);

                            if(records[0].data.id == 1){
                                Ext.getCmp('returnInformation').hide();
                            }else{
                                Ext.getCmp('returnInformation').show();
                            }
                        }
                    },

                ]},
                {xtype: 'container', cls: 'row', id:'returnInformation', hidden: true,  items:
                    [
                        {xtype: 'container', cls: 'col-2', items:
                            [
                                {field: 'main.inspectionOutcome', xtype: 'dictcombo',  fieldLabel: _lang.ProductProblem.fInspectionOutcome, cls: 'col-1', value: '1',
                                    code:'product_problem', codeSub:'inspection_outcome', allowBlank:false,
                                },
                                {field: 'main.returnTrackingNumber', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fReturnTraceCode, cls: 'col-1', },
                                {field: 'main.returnInitiated', xtype: 'datetimefield', fieldLabel: _lang.ProductProblem.fReturnInitiated, cls: 'col-1', format: curUserInfo.dateFormat,},
                                {field: 'main.returnReceived', xtype: 'datetimefield', fieldLabel: _lang.ProductProblem.fReturnReceived, cls: 'col-1', format: curUserInfo.dateFormat,},
                            ]
                        },
                        {xtype: 'container', cls: 'col-2', items:
                            $groupPriceFields(scope, {
                                allowBlank: true, cls: 'col-1',
                                aud: { field: 'main.returnCostAud', fieldLabel: _lang.ProductProblem.fReturnCostAud,},
                                rmb: { field: 'main.returnCostRmb', fieldLabel:_lang.ProductProblem.fReturnCostRmb, },
                                usd: { field: 'main.returnCostUsd', fieldLabel: _lang.ProductProblem.fReturnCostUsd, }
                            })
                        },
                    ]
                },



                //    处理方式
                {xtype: 'section', title: _lang.ProductProblem.tabHandleMode},
                {xtype: 'container', cls: 'row', items: [
                    {field: 'main.handleMode', xtype: 'dictcombo',  fieldLabel: _lang.ProductProblem.fHandleMode, cls: 'col-2', value: '5',
                        code:'product_problem', codeSub:'handle_mode', allowBlank:false,
                        selectFun: function(records){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.refundAt').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.accountName').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.refundPaymentMode').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.isShipped').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.refundAmountAud').setValue(0);

                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.reDeliveryAt').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.newAddress').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.reDeliveryOrderId').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.reDeliveryWeight').setValue('');
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.reDeliveryValueAud').setValue(0);

                            if(records[0].data.id == 5){
                                Ext.getCmp('fullRefundContainer').hide();
                                Ext.getCmp('partsContainer').hide();
                            }else if(records[0].data.id == 1 || records[0].data.id == 2){
                                Ext.getCmp('fullRefundContainer').show();
                                Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.isShipped').setValue('1');
                                Ext.getCmp('partsContainer').hide();
                            }else{
                                Ext.getCmp('fullRefundContainer').hide();
                                Ext.getCmp('partsContainer').show();
                            }
                        }
                    },
                ]},
                {xtype: 'container', cls: 'row', id:'fullRefundContainer', hidden: true,  items:
                    [
                        {xtype: 'container', cls: 'col-2', items:
                            [
                                {field: 'main.reDeliveryAt', xtype: 'datetimefield', fieldLabel: _lang.ProductProblem.fReDeliveryAt, cls: 'col-1', format: curUserInfo.dateFormat,},
                                {field: 'main.accountName', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fAccountName, cls: 'col-1',},
                                {field: 'main.refundPaymentMode', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fRefundPaymentMode, cls: 'col-1',},
                                {field: 'main.isShipped', xtype: 'combo', fieldLabel: _lang.ProductProblem.fIsShipped, cls: 'col-1', allowBlank:true, value:'1',
                                    store: [ ['1', _lang.TText.vYes], ['2',  _lang.TText.vNo]]
                                },
                            ]
                        },

                        {xtype: 'container', cls: 'col-2', items:
                            $groupPriceFields(scope, {
                                allowBlank: true, cls: 'col-1',
                                aud: { field: 'main.refundAmountAud', fieldLabel: _lang.ProductProblem.fRefundAmountAud,},
                                rmb: { field: 'main.refundAmountRmb', fieldLabel:_lang.ProductProblem.fRefundAmountRmb, },
                                usd: { field: 'main.refundAmountUsd', fieldLabel: _lang.ProductProblem.fRefundAmountUsd, }
                            })
                        },
                    ]
                },

                {xtype: 'container', cls: 'row', id:'partsContainer', hidden: true,   items:
                    [
                        {xtype: 'container', cls: 'col-2', items:
                            [
                                {field: 'main.refundAt', xtype: 'datetimefield', fieldLabel: _lang.ProductProblem.fRefundAt, cls: 'col-1', format: curUserInfo.dateFormat,},
                                {field: 'main.newAddress', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fNewAddress, cls: 'col-1',},
                                {field: 'main.reDeliveryOrderId', xtype: 'textfield', fieldLabel: _lang.ProductProblem.fReDeliveryOrderId, cls: 'col-1',},
                                {field: 'main.reDeliveryWeight', xtype: 'numberfield', fieldLabel: _lang.ProductProblem.fReDeliveryWeight, cls: 'col-1'},
                            ]
                        },
                        {xtype: 'container', cls: 'col-2', items:
                            $groupPriceFields(scope, {
                                allowBlank: true, cls: 'col-1',
                                aud: { field: 'main.reDeliveryValueAud', fieldLabel: _lang.ProductProblem.fReDeliveryValueAud,},
                                rmb: { field: 'main.reDeliveryValueRmb', fieldLabel:_lang.ProductProblem.fReDeliveryValueRmb, },
                                usd: { field: 'main.reDeliveryValueUsd', fieldLabel: _lang.ProductProblem.fReDeliveryValueUsd, }
                            })
                        },
                    ]
                },





                //   ASN信息
                // {xtype: 'section', title: _lang.ProductProblem.tabASNInfomation},
                // {field: 'main.product2', xtype: 'ProductProblemFormASNGird', fieldLabel: _lang.ProductDocument.fLength, cls: 'row', readOnly:true,},



                //附件信息
                { xtype: 'section', title:_lang.NewProductDocument.tabAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment ,allowBlank:true, },
                { field: 'main_documentName', xtype:'hidden', allowBlank:true, },
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.isApprove,allowBlank:true,
                },


                { xtype: 'section', title:_lang.ProductProblem.fComment },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.comment', xtype: 'textarea', width: '100%', height: 200,  allowBlank:true, },
                ] },
                //   备注
                {xtype: 'section', title: _lang.ProductProblem.fCommentList},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'comments', xtype: 'ProductProblemFormRemarkGird', width: '100%', height: 200,  allowBlank:true, },
                ] },

            ].concat($creatorInfo(this.isApprove, {notArchives: true}))
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    //console.log(Ext.getCmp(conf.mainFormPanelId).getCmpByName('productTabs').formGridPanel.setValue(this.records));

                    //product
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('omsOrderIdName').setValue(json.data.omsOrderId);
                    Ext.getCmp(conf.mainFormPanelId).troubleDetails = []
                    Ext.getCmp(conf.mainFormPanelId).sellQty = [];
                    if(!!json.data && !!json.data.details && json.data.details.length>0){
                        for(index in json.data.details){
                            var product = {};
                            //Ext.applyIf(product, json.data.details[index]);
                            Ext.apply(product, json.data.details[index]);
                            Ext.apply(product, json.data.details[index].product.prop);
                            Ext.apply(product, json.data.details[index].product);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.product').formGridPanel.getStore().add(product);

                            //product ticket
                            //json.data.details[index].troubleDetails.sku = json.data.details[index].sku;
                            for(var key in  json.data.details[index].troubleDetails){
                                json.data.details[index].troubleDetails[key].sku = json.data.details[index].sku;
                            }
                            Ext.getCmp(conf.mainFormPanelId).troubleDetails.push(json.data.details[index].troubleDetails);
                            Ext.getCmp(conf.mainFormPanelId).sellQty[json.data.details[index].sku] = json.data.details[index].sellQty;
                        }
                    }

                    if(json.data.handleMode == 5){
                        Ext.getCmp('fullRefundContainer').hide();
                        Ext.getCmp('partsContainer').hide();
                    }else if(json.data.handleMode == 1 || json.data.handleMode == 2){
                        Ext.getCmp('fullRefundContainer').show();
                        Ext.getCmp('partsContainer').hide();
                    }else{
                        Ext.getCmp('fullRefundContainer').hide();
                        Ext.getCmp('partsContainer').show();
                    }

                    if(json.data.returnMethod == 1){
                        Ext.getCmp('returnInformation').hide();
                    }else{
                        Ext.getCmp('returnInformation').show();
                    }
                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);

                    if(!!json.data && !!json.data.comments && json.data.comments.length>0){
                        for(var key in json.data.comments){
                            var remark = {};
                            Ext.apply(remark, json.data.comments[key]);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('comments').formGridPanel.getStore().add(remark);

                        }
                    }
                }
            });
        };

        //this.loadRemarkData();

    },// end of the initcomponents

    loadOmsOrder: function(orderId){
        var grid = this.formPanel.getCmpByName('main.product').formGridPanel;
        grid.getStore().removeAll();

        var scope = this;
        var url = __ctxPath + 'api/transfer?method=oms.OrderQueryInfo&order_no=' + orderId;
        Ext.Ajax.request({
            url: url,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var data = response.data;
                if(!!data && !!data.detail) {
                    scope.initSku.call(scope, data.detail);
                }
            }
        });
    },

    initSku: function(skus){
        if(!skus) return;
        var grid = this.formPanel.getCmpByName('main.product').formGridPanel;
        var gridData = [];
        this.formPanel.sellQty = [];
        for(var i = 0; i < skus.length; i++){
            var product = { sku: skus[i].sku, sellQty: skus[i].num , jobNo: skus[i].job_no}
            Ext.apply(product, skus[i].product);
            Ext.apply(product, skus[i].product.prop);
            gridData.push(product);
            this.formPanel.sellQty[skus[i].sku] = skus[i].num;
        }
        grid.getStore().add(gridData);
    },


    loadRemarkData: function(){
        var data = [
            {
                id: 'rm001',
                remark: 'Kensington',
            },
            {
                warehouseId: 'rm002',
                remark: 'Braybrook 1',
            },
            {
                warehouseId: 'rm003',
                remark: 'Braybrook 2',
            }
        ];

        var scope = this;
        var url = __ctxPath + 'archives/troubleTicket/remark/list';
        Ext.Ajax.request({
            url: url,
            method: 'post',
            success: function (response, options) {
                var response = Ext.JSON.decode(response.responseText);
                var data = response.data;
                console.log(data);
            }
        });
        this.formPanel.getCmpByName('remarks').formGridPanel.getStore().add(data);
    },

    onGridDataChange: function(store, conf){
        var skus = [];
        var productIds = [];
        var data = store.getRange();
        for(var i = 0; i < data.length; i++){
            productIds.push(data[i].data.id);
            skus.push(data[i].data.sku);
        }
        var tabs = this.formPanel.getCmpByName('productTabs');
        tabs.updateTabs(skus, productIds, this.formPanel.troubleDetails);
    },

    onItemClick: function(grid, rowIndex, columnIndex){
        var rows = grid.getSelectionModel().selected.items;
        var sku = rows[0].data.sku;
        if(!sku) return;
        var tabs = this.formPanel.getCmpByName('productTabs');
        tabs.setActiveTabBySku(sku);
    },

    saveFun: function(){
        var params = {act: this.actionName ? this.actionName: 'save'};
        // Ext.getCmp(this.mainFormPanelId).getCmpByName('productTabs').getValue(params);
        //save tabs data
        var tabItems = Ext.getCmp(this.mainFormPanelId).getCmpByName('productTabs').items.items;
        var productGrid = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.product').formGridPanel.getStore().data.items;
        var index = 0;
        var flag = true;
        var hasCheck = [];
        var hasCheckStr;
        for(var i = 0; i < tabItems.length; i++){
            var sku = tabItems[i].title;
            var id = tabItems[i].id;
            var grid = tabItems[i].items.first();
            var store = grid.getStore().getRange();
            var key = 0;
            hasCheck[i] = false;
            for(var j = 0; j < store.length; j++){
                var data = store[j].data;
                if(data.active != true) continue;
                hasCheck[i] = true;
                if(typeof data.qty != 'number' || data.qty > productGrid[index].data.sellQty){
                    flag = false;
                }
                if(!! productGrid && productGrid.length>0){
                    params['products['+index+'].jobNo'] =  productGrid[index].data.jobNo;
                }
                params['products[' + index + '].sku'] = sku;
                params['products[' + index + '].sellQty'] = Ext.getCmp(this.mainFormPanelId).sellQty[sku];
                params['products[' + index + '].troubleDetails[' + key + '].codeMain'] = data.codeMain;
                params['products[' + index + '].troubleDetails[' + key + '].codeSub'] = data.codeSub;
                params['products[' + index + '].troubleDetails[' + key + '].troubleDetailId'] = data.troubleDetailId;
                params['products[' + index + '].troubleDetails[' + key + '].qty'] = data.qty;
                // params['products[' + index + '].troubleDetails[' + key + '].checked'] = true;
                // if(data.active != true) params['products[' + index + '].troubleDetails[' + key + '].checked'] = false;
                key++;
            }
            index++;
        }
        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

        for (var index in hasCheck){
            if(!hasCheck[index]){
                hasCheckStr = false;
                break;
            }
            hasCheckStr = true;
        }
        if(!hasCheckStr){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorDataProblemQty);
        }else if(!flag){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorDataQty);
        }else{
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url: this.urlSave,
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