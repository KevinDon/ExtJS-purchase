NewProductDocumentFormCheck = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        var conf = {
            winId : 'NewProductDocumentFormCheck',
            title : _lang.NewProductDocument.mCheckTitle,
            moduleName:'NewProduct',
            mainGridPanelId : 'NewProductDocumentGridPanelID',
            mainFormCheckPanelId : 'NewProductDocumentFormCheckPanelID',
            searchFormPanelId: 'NewProductDocumentSubFormPanelID',
            mainTabPanelId: 'NewProductDocumentTbsPanelId',
            subGridPanelId : 'NewProductDocumentSubGridPanelID',
            urlList: __ctxPath + 'archives/newproduct/list',
            urlSave: __ctxPath + 'archives/newproduct/convertToProduct',
            urlDelete: __ctxPath + 'archives/newproduct/delete',
            urlGet: __ctxPath + 'archives/newproduct/get',
            actionName: this.action,
            save: true,
            close: true,
            // saveAs: true,
            saveFun: this.saveFun,
        };
        this.initUIComponents(conf);
        NewProductDocumentFormCheck.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.NewProductDocument.mCheckTitle,
            tbar: Ext.create("App.toolbar", conf),
            width : 720, height : 150,
            items : this.formPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
            var scope = this;
            var updatePriceFlag = false;
            this.formPanel = new HP.FormPanel({
                id: conf.mainFormCheckPanelId,
                scope: this,
                autoScroll: true,
                fieldItems : [
                    //基础资料
                    {xtype: 'section', title: _lang.ProductDocument.tabInitialValue},
                    {field: 'id', xtype: 'hidden', value: this.id == null ? '' : this.id},
                    {field: 'barcode', xtype: 'hidden',  value: this.barcode},
                    {field: 'ean', xtype: 'hidden', value: this.ean},
                    { xtype: 'container',cls:'row', items:  [
                        {field: 'sku', xtype: 'textfield', fieldLabel: _lang.ProductDocument.fSku, formId: conf.mainFormPanelId,  cls:'col-2',allowBlank: false, value: this.sku,
                            listeners: {
                                change: function(field, newValue, oldValue){
                                    this.setValue(newValue.toUpperCase());
                                }
                            }
                        },
                        {field: 'updatedAt', xtype: 'displayfield', fieldLabel:  _lang.TText.fUpdatedAt, formId: conf.mainFormPanelId,  cls:'col-2', value: this.prop['updatedAt']},
                    ] },


                    { xtype: 'section', title: _lang.ProductDocument.fComplianceStatus},
                    { xtype: 'container',cls:'row', items:  [
                        { field: 'main.flagComplianceId', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fID, cls:'col-2', readOnly:true,
                            value:this.prop['flagComplianceId'],
                        },
                        {field: 'main.flagComplianceStatus', xtype: 'displayfield', fieldLabel:   _lang.ProductDocument.fComplianceStatus, formId: conf.mainFormPanelId, cls: 'col-2',
                            value: this.prop['flagComplianceStatus'],
                            renderer: function(value) {
                                if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                                else return $renderOutputColor('gray', _lang.TText.vToCheck);
                            }
                        },
                        { field: 'main.flagComplianceTime', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fQcStatusDate, cls:'col-2', readOnly:true,
                            value: this.prop['flagComplianceTime'],
                        },
                        { field: 'main.riskRating', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel:  _lang.ProductDocument.fRiskRating, cls:'col-2', readOnly:true,
                            value: this.prop['riskRating'],
                            renderer: function(value){
                                var $riskRating = _dict.riskRating;
                                if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                                    return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                                }
                            }
                        }

                    ] },

                    { xtype: 'section', title:_lang.ProductDocument.fDevStatus},

                    { xtype: 'container',cls:'row', items:  [
                        { field: 'main.flagDevId', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fID, cls:'col-2', readOnly:true,
                            value:this.prop['flagDevId'],
                        },

                        {field: 'main.flagDevStatus', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fDevStatus, formId: conf.mainFormPanelId, cls: 'col-2',
                            value: this.prop['flagDevStatus'],
                            renderer: function(value) {
                                if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                                else return $renderOutputColor('gray', _lang.TText.vToCheck);
                            }
                        },
                        { field: 'main.flagDevTime', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fQcStatusDate, cls:'col-2', readOnly:true,
                            value:this.prop['flagDevTime'],
                        }

                    ] },

                    { xtype: 'section', title: _lang.ProductDocument.fQcStatus},

                    { xtype: 'container',cls:'row', items:  [
                        { field: 'main.flagQcId', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fID, cls:'col-2', readOnly:true,
                            value:this.prop['flagQcId'],
                        },

                        {field: 'main.flagQcStatus', xtype: 'displayfield', fieldLabel:  _lang.ProductDocument.fQcStatus, formId: conf.mainFormPanelId, cls: 'col-2', value:this.prop['flagQcStatus'],
                            renderer: function(value) {
                                if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                                else return $renderOutputColor('gray', _lang.TText.vToCheck);
                            }
                        },
                        { field: 'main.flagQcTime', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.ProductDocument.fQcStatusDate, cls:'col-2', readOnly:true,
                            value:this.prop['flagQcTime'],
                        }

                    ] },


                    //创建人信息
                    {xtype: 'section', title: _lang.ProductDocument.tabCreatorInformation},
                    {xtype: 'container', cls: 'row', items: [
                        {field: 'main.creatorCnName', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fCreator, cls: 'col-2', value:this.creatorCnName},
                        {field: 'main.departmentCnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, cls: 'col-2',  value:this.departmentCnName},
                    ]},
                    {xtype: 'container', cls: 'row', items: [
                        { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', value: this.createdAt},
                    ],},

                ]
            });
    },// end of the initcomponents

    saveFun: function(){
        var params = {act: this.actionName ? this.actionName: 'save'};
        var riskRating = Ext.getCmp(this.mainFormCheckPanelId).getCmpByName('main.riskRating').getValue();
        var sku = Ext.getCmp(this.mainFormCheckPanelId).getCmpByName('sku').getValue();
        var id = Ext.getCmp(this.mainFormCheckPanelId).getCmpByName('id').getValue();
        params['barcode'] = Ext.getCmp(this.mainFormCheckPanelId).getCmpByName('barcode').getValue();
        params['ean'] = Ext.getCmp(this.mainFormCheckPanelId).getCmpByName('ean').getValue();
        if(riskRating >= 5){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorRiskRating)
        }else if(params['barcode'] == '' ||  params['ean'] == '' ){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorEanOrBarcode)
        }else{
            Ext.Ajax.request({
                url: __ctxPath + 'archives/product/existsSku?sku=' + sku + '&id=' + id,
                scope: this, method: 'post',
                success: function (response, options) {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success == true) {
                        if (obj.data == true) {
                            $postForm({
                                formPanel: Ext.getCmp(this.mainFormCheckPanelId),
                                grid: Ext.getCmp(this.mainGridPanelId),
                                scope: this,
                                url: this.urlSave,
                                params: params,
                                callback: function (fp, action, status, grid) {
                                    Ext.getCmp(this.winId).close();
                                    if (!!grid) {
                                        grid.getSelectionModel().clearSelections();
                                        grid.getView().refresh();
                                    }
                                }
                            });
                        } else {
                            Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg);
                        }
                    }
                }
            });
        }
    }
});