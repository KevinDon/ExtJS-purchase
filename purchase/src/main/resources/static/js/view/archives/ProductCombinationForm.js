ProductCombinationForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;
        var conf = {
            winId : 'ProductCombinationFormWinID',
            frameId : 'ProductCombinationView',
            title : _lang.AccountDepartment.mTitle,
            moduleName: 'ProductCombination',
            mainGridPanelId : 'ProductCombinationFormGridGridPanelID',
            mainFormPanelId : 'ProductCombinationFormGridFormPanelID',
            processFormPanelId: 'FlowNewProductProcessFormPanelID',
            searchFormPanelId: 'ProductCombinationFormGridSearchFormPanelID',
            mainTabPanelId: 'ProductCombinationFormGridMainTbsPanelID',
            subGridPanelId : 'ProductCombinationFormGridSubGridPanelID',
            formGridPanelId : 'ProductCombinationFormGridPanelID',
            actionName: this.action,
            urlList: __ctxPath + 'archives/productCombined/list',
            urlSave:_cfg.urlSave ||  __ctxPath + 'archives/productCombined/save',
            urlDelete:_cfg.urlDelete ||  __ctxPath + 'archives/productCombined/delete',
            urlGet:_cfg.urlGet ||  __ctxPath + 'archives/productCombined/get',
	        ahid: !!_cfg.record ?_cfg.record.ahid :'',
            refresh: true,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            confirm: this.isConfirm,
            saveFun: this.saveFun,
        };

        this.initUIComponents(conf);
        ProductCombinationForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductCombination.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
            tbar: Ext.create("App.toolbar", conf),
            width : 1024, height : 800,
            items : [this.formPanel]
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;
        this.formPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            fieldItems : [
                //基础资料
                { xtype: 'section', title:_lang.ProductDocument.tabInitialValue},
                {field:'id', xtype:'hidden', value: this.action == 'add' ? '': this.record.id },
                { field: 'main_products', xtype:'hidden', value:this.record.products },
                { field: 'main_productsName', xtype:'hidden'},
                { xtype: 'container', cls: 'row',  items:  [
                        { field: 'main.combinedSku', xtype: 'textfield',  fieldLabel: _lang.ProductCombination.fCombinedSku, allowBlank: false, cls:'col-2', },
                        { field: 'main.combinedName', xtype: 'textfield', fieldLabel: _lang.ProductCombination.fCombinedName, allowBlank: false,  cls:'col-2', },
                        { field: 'main.comboType', xtype: 'combo', fieldLabel: _lang.ProductCombination.fComboType, cls:'col-2', allowBlank: false,
                            store: [['1', _lang.ProductCombination.vCombo], ['2',  _lang.ProductCombination.vVariation],['3',  _lang.ProductCombination.vParent]], emptyText: '', value: '1',
                            rerender: function (value) {
                                if(value == '1') return $renderOutputColor('green', _lang.ProductCombination.vCombo);
                                if(value == '2') return $renderOutputColor('blue', _lang.ProductCombination.vVariation);
                                if(value == '3') return $renderOutputColor('rad', _lang.ProductCombination.vParent);
                            },
                            listeners:{
                                select: function(combo, records, eOpts){
                                    if(combo.getValue() == '3'){
                                        combo.up().up().getCmpByName('combinedDetail').hide();
                                    }else{
                                        combo.up().up().getCmpByName('combinedDetail').show();
                                    }
                                }
                            }
                        },
                        {field: 'main.categoryId', xtype: 'hidden'},
                        {field: 'main.categoryName', xtype: 'ProductCategoryDialog', title: _lang.ProductDocument.fCategoryId, fieldLabel: _lang.ProductDocument.fCategoryId,
                            formId: conf.mainFormPanelId, hiddenName: 'main.categoryId', single: true, cls: 'col-2', readOnly: this.isReadOnly
                        },
                    ]
                },
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.ean', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fEan, cls:'col-2', allowBlank: false, scope: this,  },
                    { xtype:'button', id:'main_button_ean', text: _lang.TText.fGenerate, width:80, formId: conf.mainFormPanelId, scope:this, hidden: this.isReadOnly,
                        handler: function(e) { this.scope.getAutoEanCode.call(e);}
                    }
                ]},

                { xtype: 'container', cls: 'row',  items: [
                    { xtype: 'container', cls: 'col-2',  items:  $groupPriceFields(scope, {
                            allowBlank: true,
                            decimalPrecision: 2, cls: 'col-1',
                            aud: { field: 'main.priceAud',fieldLabel: _lang.ProductDocument.fTargetBinAud, },
                            rmb: { field: 'main.priceRmb', fieldLabel: _lang.ProductDocument.fTargetBinRmb, },
                            usd: { field: 'main.priceUsd', fieldLabel: _lang.ProductDocument.fTargetBinUsd,}
                        })
                    },
                    //汇率
                    { xtype: 'container',cls:'col-2', items:  [
                        { field: 'main.rateAudToRmb', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fRateAudToRmb, cls:'col-1',allowBlank: false ,
                            readOnly: true, value: curUserInfo.audToRmb
                        },
                        { field: 'main.paymentRateAudToRmb', xtype: 'textfield',  fieldLabel: _lang.FlowDepositContract.fRateAudToUsd, cls:'col-1',
                            allowBlank: false, readOnly: true, value: curUserInfo.audToUsd
                        }
                    ]},
                ]
                },
               
                //   组合产品明细
                { xtype: 'section', title:_lang.ProductCombination.tabCombinedDetail},
                { field: 'combinedDetail', xtype: 'ProductCombinationFormGrid', fieldLabel: _lang.ProductDocument.fProductCert,  cls: 'row', readOnly: this.isApprove },
                { xtype: 'section', title: _lang.ProductDocument.tabIsSync},
                { xtype: 'container', cls: 'row',  items:  [
                    { field: 'main.flagSyncStatus ', xtype: 'dictfield',fieldLabel: _lang.ProductDocument.fIsSync,cls:'col-2', code:'product', codeSub:'sync' ,  },
                    { field: 'main.flagSyncDate', xtype: 'displayfield',fieldLabel: _lang.ProductDocument.fIsSyncDate,cls:'col-2',  },
                ]}
            ].concat($creatorInfo(this.isApprove,{notArchives: false}))
        });

        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            this.formPanel.loadData({
                url : conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName : 'main', loadMask:true, scope: this, success:function(response) {
                    var json = Ext.JSON.decode(response.responseText);
                    if(!!json.data && !!json.data.details){
                        Ext.getCmp(conf.formGridPanelId).getStore().add(json.data.details);
                    }
                    //barcode ean
                    if (json.data.ean != null) {
                        Ext.getCmp('main_button_ean').setDisabled(true);
                    }
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.comboType').setReadOnly(true);
                    if (!!json.data.comboType && json.data.comboType==3) {
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('combinedDetail').hide();
                    }else{
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('combinedDetail').show();
                    }

                }
            });
        }
        this.formPanel.setReadOnly(this.isApprove || this.isConfirm, []);
    },// end of the initcomponents

    saveFun: function(action){
        var params = {act: action? action: this.actionName ? this.actionName: 'save'};
        var flag = false;
        var comboType = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.comboType').getValue();

        if(action == 'confirm') params.ahid = this.ahid;
        var rows = $getGdItems({grid: Ext.getCmp(this.formGridPanelId)});
        if(comboType != 3 && rows && rows.length>0){
            flag = true;
            for(index in rows){
                params['details['+index+'].businessId'] = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
                for(key in rows[index].data){
                    params['details['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        if(!flag && comboType != 3 ){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsProductCombinationError);
        }else{
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url: this.urlSave,
                params: params,
                callback: function (fp, action, status, grid) {
                    Ext.getCmp(this.mainGridPanelId + '-ArchivesHistoryTabGrid').refresh();
                    Ext.getCmp(this.winId).close();
                    if(!!grid) {
                        grid.getSelectionModel().clearSelections();
                        grid.getView().refresh();
                    }
                }
            });
        }
    },

    getAutoEanCode: function () {
        var me = this;
        console.log(this);
        Ext.Ajax.request({
            url: __ctxPath + 'admin/autocode/generate?code=ean',
            scope: this, method: 'post', success: function (response, options) {
                var obj = Ext.decode(response.responseText);
                if (obj.success == true) {
                    if (!!obj.data) {
                        me.scope.formPanel.getCmpByName('main.ean').setValue(obj.data);
                        me.setDisabled(true);
                    } else {
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
                    }
                }
            }
        });
    },
});