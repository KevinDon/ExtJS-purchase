VendorDocumentForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {

        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;

        var conf = {
            winId : 'VendorDocumentFormWinID',
            moduleName: 'Vendor',
            title : _lang.VendorDocument.mTitle,
            mainGridPanelId : 'VendorDocumentGridPanelID',
            mainFormPanelId : 'VendorDocumentFormPanelID',
            searchFormPanelId: 'VendorDocumentSubFormPanlID',
            mainTabPanelId: 'VendorDocumentTbsPanelId',
            subGridPanelId : 'VendorDocumentSubGridPanelID',
            mainFormGridPanelId : 'VendorDocumentFormCategoryGridPanelID',
            urlList: __ctxPath + 'archives/vendor/list',
            urlSave: _cfg.urlSave || __ctxPath + 'archives/vendor/save',
            urlDelete: _cfg.urlDelete || __ctxPath + 'archives/vendor/delete',
            urlGet: _cfg.urlGet ||  __ctxPath + 'archives/vendor/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            actionName: this.action,
            refresh: true,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            confirm: this.isConfirm,
            saveFun: this.saveRow
        };

        this.initUIComponents(conf);
        VendorDocumentForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.VendorDocument.mTitle + $getTitleSuffix(this.action),
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
                //供应商基础资料
                {xtype: 'section', title:_lang.VendorDocument.tabInitialValue},
                {field: 'id',	xtype: 'hidden', value: this.action == 'add' ? '': this.record.id },
                {field: 'main.rateAudToRmb', xtype: 'hidden', value: curUserInfo.audToRmb},
                {field: 'main.rateAudToUsd', xtype: 'hidden', value: curUserInfo.audToUsd},
                
                { field:'main.categoryId', xtype:'hidden'},
                { xtype: 'container', cls:'row',  items:[
                        { field: 'main.code', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fVendorCode, cls:'col-2' ,allowBlank:false, maxLengthText: 50, },
                        { xtype:'button', id:'main_button_barcode', text: _lang.TText.fGenerate, width:80, formId: conf.mainFormPanelId, scope:this, hidden: this.isReadOnly,
                            handler: function(e) { this.scope.getAutoVendorCode.call(e);}
                        },
                ] },
                { xtype: 'container', cls:'row',  items:[
                    {field:'main.categoryName', xtype:'VendorCategoryDialog', title:_lang.VendorDocument.fCategoryName, fieldLabel: _lang.VendorDocument.fCategoryName,
                        formId:conf.mainFormPanelId, hiddenName:'main.categoryId', single: true, cls:'col-2', allowBlank: false, readOnly: this.isApprove || this.isConfirm,
                    },
                    { field: 'main.buyerName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fBuyerName, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2', allowBlank:false,  },
                    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2' , allowBlank: false, }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.director', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fDirector, cls:'col-2'  },
                    { field: 'main.address', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fAddress, cls:'col-2'}
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.website', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fWebsite, cls:'col-2'},
                    { field: 'main.abn', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fAbn, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    {field: 'main.source', xtype: 'dictcombo', fieldLabel: _lang.VendorDocument.fSource, value: '1', allowBlank: false, cls:'col-2',code:'vendor', codeSub:'source'},
                    {field: 'main.sellerEmail', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerEmail, cls:'col-2'  },
                    {field: 'main.sellerPhone', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerPhone, cls:'col-2'  },
                    {field: 'main.sellerFax', xtype: 'textfield', fieldLabel: _lang.VendorDocument.fSellerFax, cls:'col-2'  },
                    {field: 'main.balancePaymentTerm', xtype: 'dictcombo', fieldLabel: _lang.VendorDocument.fBalancePaymentTerm, cls:'col-2', code:'purchase', codeSub:'balance_payment_term'},
                    {field: 'main.balanceCreditTerm', xtype: 'numberfield', fieldLabel: _lang.VendorDocument.fBalanceCreditTerm, cls:'col-2',value:0},
                    {field: 'main.tradeTerm', xtype: 'textfield', fieldLabel: _lang.FlowCustomClearance.fTradeTerm, cls: 'col-2' ,allowBlank: false, value:'FOB', },
                ] },

                //  供应商图片
                { xtype: 'section', title:_lang.VendorDocument.tabImages},
                { field: 'main.images', xtype: 'hidden'},
                { field: 'main.imagesName', xtype: 'hidden'},
                { field: 'main_images', xtype: 'ImagesDialog', fieldLabel: _lang.VendorDocument.tabImages,
                    formId: conf.mainFormPanelId, hiddenName: 'main.images', titleName:'main.imagesName', fileDefType: 1, readOnly: this.isApprove,
                },
                //  供应商分类信息
                { xtype: 'section', title:_lang.VendorDocument.tabProductCategory },
                { field: 'main_productCategory', xtype:'hidden' },
                { field: 'main_productCategoryName', xtype:'hidden'},
                { field: 'main.productCategory', xtype: 'ProductCategoryMultiGrid', fieldLabel: _lang.VendorDocument.tabProductCategory,
                    formId:conf.mainFormPanelId, mainGridPanelId: conf.mainGridPanelId, scope:this, readOnly: this.isApprove
                },

                //  取得相关的认证文件
//              { xtype: 'section', title:_lang.VendorDocument.tabCertificate},
//              { field: 'main_documentsCertificate', xtype:'hidden', value:this.record.attachment },
//              { field: 'main_documentNameCertificate', xtype:'hidden'},
//              { field: 'main.attachmentsCertificate', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
//            	  mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
//            	  subGridPanelId: conf.mainGridPanelId+'-subGridPanelId-Certificate', subFormPanelId:conf.mainFormPanelId + 'subFormPanelId-Certificate',
//            	  scope:this, readOnly: this.isApprove
//              },


                //  其它附件
                { xtype: 'section', title:_lang.VendorDocument.tabOtherAttachment},
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, width:'100%', height: 150,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.isApprove
                },

                //付款条款
                { xtype: 'section', title:_lang.VendorDocument.tabPaymentTerms},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.paymentProvision', xtype: 'htmleditor', width:'100%',height:200}
                ] },

                //供应商联系人信息
                { xtype: 'section', title:_lang.VendorDocument.tabContactsInformation},
                { field: 'main_contactss', xtype:'hidden'},
                { field: 'main_contactssName', xtype:'hidden'},
                { field: 'main.contactss', xtype: 'ContactsFormMultiGrid', fieldLabel: _lang.VendorDocument.fContactsName,
                    formId:conf.mainFormPanelId, mainGridPanelId: conf.mainGridPanelId, type:1, readOnly: this.isApprove
                },

                //相关报告
                { xtype: 'section', title: _lang.Reports.tabRelatedReports},
                { field: 'main.reports', xtype: 'ReportsFormMultiGrid',fieldLabel: _lang.Reports.tabRelatedReports,
                    farmeId: conf.mainFormPanelId, scope:this, readOnly: true,
                },

                //供应商收款信息
                { xtype: 'section', title:_lang.VendorDocument.tabBankInformation},

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
                    { field: 'main.depositType', xtype: 'dictfield', fieldLabel: _lang.FlowBankAccount.fDepositType, cls:'col-2', code:'purchase', codeSub:'deposit_rate', readOnly:true,   },
                    { field: 'main.depositRate', xtype: 'displayfield',  fieldLabel: _lang.FlowBankAccount.fDepositRate, cls:'col-2', readOnly:true,  },
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.beneficiaryBank', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBank, cls:'col-2' },
                    { field: 'main.bank.beneficiaryBankAddress', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryBankAddress, cls:'col-2'  }
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.bank.beneficiaryCnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryCnName, cls:'col-2', },
                    { field: 'main.bank.beneficiaryEnName', xtype: 'displayfield', fieldLabel: _lang.BankAccount.fBeneficiaryEnName, cls:'col-2', },
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
            ].concat($creatorInfo(this.isApprove))

        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url: conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName: 'main', loadMask:true,
                success: function(response){
                    var json = Ext.JSON.decode(response.responseText);

                    //productCategory init
                    if(!!json.data && !!json.data.productCategory && json.data.productCategory.length>0){
                        for(index in json.data.productCategory){
                            var productCategory = {};
                            productCategory = json.data.productCategory[index];
                            Ext.applyIf(productCategory, json.data.productCategory[index].productCategory);
                            productCategory.id = json.data.productCategory[index].productCategoryId;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.productCategory').subGridPanel.getStore().add(productCategory);
                        }
                    }
                    //attachment init
                    if(!!json.data && !!json.data.attachments)
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);

                    //contacts init
                    if(!!json.data && !! json.data.contacts && json.data.contacts.length>0){
                        for(index in json.data.contacts){
                            var contacts = {};
                            Ext.applyIf(contacts, json.data.contacts[index]);
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.contactss').subGridPanel.getStore().add(contacts);
                        }
                    }

                    //images init
                    if(!!json.data && !!json.data.imagesDoc && json.data.imagesDoc.length>0){
                        for(index in json.data.imagesDoc){
                            var images = {};
                            Ext.applyIf(images, json.data.imagesDoc[index].document);
                            images.id= json.data.imagesDoc[index].documentId;
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_images').dataview.getStore().add(images);
                        }
                    }

                    //reports init
                    Ext.getCmp(conf.mainFormPanelId + '-ReportsMultiGrid').setValue(json.data.reports);
                }
            });
        }

        this.editFormPanel.setReadOnly(this.isApprove || this.isConfirm, []);

    },// end of the init

    saveRow: function(action){

        var params = {act: action? action: this.actionName ? this.actionName: 'save'};
        var vendorId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();
        var flag = false;
        if(action == 'confirm') params.ahid = this.ahid;

        //productCategory
        var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.productCategory').subGridPanel});
        if(rows !=undefined && rows.length>0){
            flag = true;
            for(index in rows){
                params['productCategory['+index+'].vendorId'] = vendorId;
                for(key in rows[index].data){
                    if(key == 'id')
                        params['productCategory['+index+'].productCategoryId'] = rows[index].data.id;
                    else
                        params['productCategory['+index+'].'+key ] = rows[index].data[key];
                }
            }
        }
        //TODO 后期调整
        //contact
//    	var rows = $getGdItems({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.contactss').subGridPanel});
//    	if(rows !=undefined && rows.length>0){
//    		for(index in rows){
//    			params['contactss['+index+'].vendorId'] = vendorId;
//    			for(key in rows[index].data){
//    				if(key == 'id')
//    					params['contactss['+index+'].contactsId'] = rows[index].data.id;
//    				else
//    					params['contactss['+index+'].'+key ] = rows[index].data[key];
//    			}
//    		}
//    	}

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(!!rows && rows.length>0){
            for(index in rows){
                params['attachments['+index+'].businessId'] = vendorId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }
        var code = Ext.getCmp(this.mainFormPanelId).getCmpByName('main.code').getValue();
        if(!flag) {
            //销售产品分类信息不能为空
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsVendorDocumentError);
        }else{
            Ext.Ajax.request({
                url: __ctxPath + 'archives/vendor/existsVendorCode?code=' + code + '&id=' + vendorId,
                scope: this, method: 'post',
                success: function (response, options) {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success == true) {
                        if (obj.data == true) {
                            $postForm({
                                formPanel: Ext.getCmp(this.mainFormPanelId),
                                grid: Ext.getCmp(this.mainGridPanelId),
                                scope: this,
                                url: this.urlSave,
                                params: params,
                                callback: function (fp, action, status, grid) {
                                    Ext.getCmp(this.mainGridPanelId + '-ArchivesHistoryTabGrid').refresh();
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
    },

    getAutoVendorCode: function () {
        var me = this;
        Ext.Ajax.request({
            url: __ctxPath + 'admin/autocode/generate?code=vendor_code',
            scope: this, method: 'post', success: function (response, options) {
                var obj = Ext.decode(response.responseText);
                if (obj.success == true) {
                    if (!!obj.data) {
                        me.scope.editFormPanel.getCmpByName('main.code').setValue(obj.data);
                        me.setDisabled(true);
                    } else {
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
                    }
                }
            }
        });
    },
});