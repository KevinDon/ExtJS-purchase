ServiceProviderDocumentForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;
        var conf = {
            winId : 'ServiceProviderDocumentFormWinID',
            moduleName: 'Service',
            frameId : 'ServiceProviderDocumentView',
            title : _lang.ServiceProviderDocument.mTitle,
            mainGridPanelId : 'ServiceProviderDocumentGridPanelID',
            mainFormPanelId : 'ServiceProviderDocumentFormPanelID',
            searchFormPanelId: 'ServiceProviderDocumentSubFormPanelID',
            mainTabPanelId: 'ServiceProviderDocumentTbsPanelId',
            subGridPanelId : 'ServiceProviderDocumentSubGridPanelID',
            mainFormGridPanelId : 'ServiceProviderDocumentFormCategoryGridPanelID',
            urlList: __ctxPath + 'archives/service_provider/list',
            urlSave:  _cfg.urlSave || __ctxPath + 'archives/service_provider/save',
            urlDelete:  _cfg.urlDelete || __ctxPath + 'archives/service_provider/delete',
            urlGet: _cfg.urlGet ||  __ctxPath + 'archives/service_provider/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            actionName: this.action,
            refresh: true,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            confirm: this.isConfirm,
            saveFun: this.saveRow
        };

        this.initUIComponents(conf);
        ServiceProviderDocumentForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ServiceProviderDocument.mTitle + $getTitleSuffix(this.action),
            tbar: Ext.create("App.toolbar", conf),
            width : 1024, height : 800,
            items : this.editFormPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        this.editFormPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            region: 'center',
            scope: this,
            fieldItems : [
                //服务商基础资料
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabInitialValue},
                { field: 'id',	xtype: 'hidden', value: this.action == 'add' ? '': this.record.id },
                { field:'main.categoryId', xtype:'hidden'},
                { xtype: 'container', cls:'row',  items:[
                    { field: 'main.code', xtype: 'textfield', fieldLabel: _lang.ServiceProviderDocument.fCode, cls:'col-2' ,allowBlank:false, maxLengthText: 50, },
                    {field:'main.categoryName', xtype:'ServiceProviderCategoryDialog', title:_lang.ServiceProviderDocument.fCategoryId, fieldLabel: _lang.ServiceProviderDocument.fCategoryId,
                        formId:conf.mainFormPanelId, hiddenName:'main.categoryId', single: true, cls:'col-2',
                        allowBlank: false,readOnly: this.isApprove || this.isConfirm
                    }
                ] },



                { xtype: 'container', cls:'row',  items:  [
                    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2', allowBlank:false },
                    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2' , maxLength:100, allowBlank: false}
                ] },

                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.director', xtype: 'textfield', fieldLabel: _lang.ServiceProviderDocument.fDirector, cls:'col-2'  },
                    { field: 'main.address', xtype: 'textfield', fieldLabel: _lang.ServiceProviderDocument.fAddress, cls:'col-2',allowBlank: false}
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.website', xtype: 'textfield', fieldLabel: _lang.ServiceProviderDocument.fWebsite, cls:'col-2'},
                    { field: 'main.abn', xtype: 'textfield', fieldLabel: _lang.ServiceProviderDocument.fAbn, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.source', xtype: 'dictcombo', fieldLabel: _lang.ServiceProviderDocument.fSource, allowBlank: false, cls:'col-2',code:'vendor', codeSub:'source'},
                    { field: 'main.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.ServiceProviderDocument.fCurrency, cls:'col-2'},
                ] },

                //图片
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabImages},
                { field: 'main.images', xtype: 'hidden'},
                { field: 'main.imagesName', xtype: 'hidden'},
                { field: 'main_images', xtype: 'ImagesDialog', fieldLabel: _lang.ServiceProviderDocument.tabImages,
                    formId: conf.mainFormPanelId, hiddenName: 'main.images', titleName:'main.imagesName', fileDefType: 1, readOnly: this.isApprove,
                },

                //起始港和收费项目
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabQuotationTemplate},
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.originPorts', xtype: 'ServiceProviderOriginPortGrid', fieldLabel: _lang.ServiceProviderDocument.fOriginPort, cls:'col-3'},
                    { field: 'main.chargeItems', xtype: 'ServiceProviderChargeItemGrid', fieldLabel: _lang.ServiceProviderDocument.fChargeItem, cls:'col', width: '66%'}
                ] },

                //  服务商收款附件信息
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabBankAttachmentInformation},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    subGridPanelId: conf.mainGridPanelId+'-subGridPanelId-atta', subFormPanelId:conf.mainFormPanelId + 'subFormPanelId-atta',
                    scope:this, readOnly: this.isApprove
                },
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabPaymentTerms},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.paymentProvision', xtype: 'htmleditor', width:'100%',height:200}
                ] },

                // 服务商联系人信息
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabContactsInformation},
                { field: 'main_contacts', xtype:'hidden'},
                { field: 'main_contactsName', xtype:'hidden'},
                { field: 'main.contacts', xtype: 'ContactsFormMultiGrid', fieldLabel: _lang.ServiceProviderDocument.fContactsName,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId, scope:this, readOnly: this.isApprove
                },

                //  服务商收款信息
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabBankInformation},
                { field:'main.bank.id', xtype:'hidden'},
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.companyCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyName, cls:'col-2'},
                    { field: 'main.bank.companyEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyName, cls:'col-2'}
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.companyCnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnCompanyAddress, cls:'col-2'},
                    { field: 'main.bank.companyEnAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fEnCompanyAddress, cls:'col-2'}
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.beneficiaryBank', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' },
                    { field: 'main.bank.beneficiaryBankAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.bankAccount', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBankAccount, cls:'col-2'  },
                    { field: 'main.bank.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.swiftCode', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fSwiftCode, cls:'col-2'  },
                    { field: 'main.bank.cnaps', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fCnaps, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.guaranteeLetterFile.document.name', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fGuaranteeLetter, cls:'col-2'  },
                    { field: 'main.bank.contractGuaranteeLetterFile.document.name', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fContractGuaranteeLetter, cls:'col-2'  }
                ] },


                //创建人信息
                { xtype: 'section', title:_lang.ServiceProviderDocument.tabCreatorInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.applicantName', xtype: 'displayfield', value:curUserInfo.loginname, fieldLabel: _lang.TText.fApplicantName, cls:'col-2', readOnly:true },
                    { field: 'main.departmentName', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.TText.fAppDepartmentName, cls:'col-2', readOnly:true}
                ] },

                { xtype: 'container',cls:'row', items: [
                    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', readOnly:true},
                    { field: 'main.status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls:'col-2', readOnly:true,
                        renderer: function(value){return $renderOutputStatus(value);}
                    }
                ], hidden: !this.isApprove },


            ],// end of columns

        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            //附件
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    //origin ports
                    var cmpPorts = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.originPorts').subGridPanel;
                    if(json.data.ports != undefined && json.data.ports.length>0){
                        for(index in json.data.ports){
                            for(var j=0; j<cmpPorts.getStore().data.length; j++){
                                if(cmpPorts.getStore().getAt(j).data.id == json.data.ports[index].originPortId){
                                    cmpPorts.getStore().getAt(j).set('active', true);
                                    break;
                                }
                            }
                        }
                    }

                    //charge items
                    var cmpChargeItems = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.chargeItems').subGridPanel;
                    if(json.data.chargeItems != undefined && json.data.chargeItems.length>0){
                        for(index in json.data.chargeItems){
                            for(var j=0; j<cmpChargeItems.getStore().data.length; j++){
                                if(cmpChargeItems.getStore().getAt(j).data.id == json.data.chargeItems[index].itemId){
                                    cmpChargeItems.getStore().getAt(j).data.unitId = json.data.chargeItems[index].unitId;
                                    cmpChargeItems.getStore().getAt(j).data.unitCnName = json.data.chargeItems[index].unitCnName;
                                    cmpChargeItems.getStore().getAt(j).data.unitEnName = json.data.chargeItems[index].unitEnName;
                                    cmpChargeItems.getStore().getAt(j).set('active', true);
                                    break;
                                }
                            }
                        }
                    }

                    //images init
                    if(json.data.imagesDoc != undefined && json.data.imagesDoc.length>0){
                        for(index in json.data.imagesDoc){
                            var images = {};
                            Ext.applyIf(images, json.data.imagesDoc[index].document);
                            images.id= json.data.imagesDoc[index].documentId;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_images').dataview.getStore().add(images);
                        }
                    }

                    //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);

                    //contacts
                    var contactsGrid = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.contacts').subGridPanel;
                    if(!!json.data.contacts) contactsGrid.getStore().add(json.data.contacts);
                }
            });
        }
        this.editFormPanel.setReadOnly(this.isApprove || this.isConfirm, []);

    },// end of the init

    saveRow: function(action){
        var params = {act: action? action: this.actionName ? this.actionName: 'save'};
        var businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        if(action == 'confirm') params.ahid = this.ahid;
        //联系人
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.contacts').subGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['main.details['+index+'].businessId'] = businessId;
                for(key in rows[index].data){
                    if(key == 'id')
                        params['main.details['+index+'].productId'] = rows[index].data.id;
                    else
                        params['main.details['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }

        //origin ports
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.originPorts').subGridPanel});
        var count = 0;
        if(!!rows && rows.length>0){
            for(var index in rows){
                if(!rows[index].data.active) continue;
                params['ports['+count+'].serviceProviderId'] = businessId;
                params['ports['+count+'].originPortCnName'] = rows[index].data.originPortCnName;
                params['ports['+count+'].originPortEnName'] = rows[index].data.originPortEnName;
                params['ports['+count+'].originPortId'] = rows[index].data.id;
                count++;
            }
        }

        //charge items
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.chargeItems').subGridPanel});
        count = 0;
        if(!!rows && rows.length>0){
            for(var index in rows){
                if(!rows[index].data.active) continue;
                params['chargeItems['+count+'].serviceProviderId'] = businessId;
                params['chargeItems['+count+'].itemId'] = rows[index].data.id;
                params['chargeItems['+count+'].itemCnName'] = rows[index].data.itemCnName;
                params['chargeItems['+count+'].itemEnName'] = rows[index].data.itemEnName;
                params['chargeItems['+count+'].unitId'] = rows[index].data.unitId;
                params['chargeItems['+count+'].unitCnName'] = rows[index].data.unitCnName;
                params['chargeItems['+count+'].unitEnName'] = rows[index].data.unitEnName;
                count++;
            }
        }

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = businessId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

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
});