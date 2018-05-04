EmailSendDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.Email.tSendWin + ' [' +( _lang.TButton[conf.action]) + ']';
        conf.moduleName = 'Email';
        conf.winId = 'EmailSendDialogWinID';
        conf.mainFormPanelId = 'EmailSendDialogWinGridPanelID';
        conf.selectGridPanelId = 'EmailSendDialogWinSelectGridPanelID';
        conf.urlSave = __ctxPath + 'desktop/email/save';
        conf.urlSend = __ctxPath + 'desktop/email/send';
        conf.urlGet = __ctxPath + 'desktop/email/get';
        // conf.action = this.action,
        conf.save = true;
        // conf.preview = true;
        conf.send = true;
        conf.close = true;
        conf.sendFun= this.sendFun;
        conf.saveFun= this.saveFun;

        this.record = conf.record || {};

        Ext.applyIf(this, conf);
        this.initUI(conf);

        EmailSendDialogWin.superclass.constructor.call(this, {
            id: conf.winId,
            title: conf.title,
            width: 980,
            region: 'center',
            layout : 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.dialogEditFormPanel]
        });
    },

    initUI : function(conf) {
        this.dialogEditFormPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            region: 'center',
            closeWin: conf.winId,
            fieldItems : [
                { field: 'id',	xtype: 'hidden'},
                { xtype: 'container',cls:'row', items: [
                    { field: 'main.recipientName', xtype: 'hidden', allowBlank: false},
                    { field: 'main.recipientEmail', xtype : 'ContactsDialog', fieldLabel : _lang.Email.fRecipientPerson,
                        formId : conf.mainFormPanelId, hiddenName : 'main.recipientName', allowBlank : false, cls: 'col-1'
                    },

                    { field: 'main.ccName', xtype: 'hidden', allowBlank: false},
                    { field: 'main.ccEmail', xtype : 'ContactsDialog', fieldLabel : _lang.Email.fCopyPerson, cls: 'col-1',
                        formId : conf.mainFormPanelId, hiddenName : 'main.ccName'
                    },

                    { field: 'main.bccName', xtype: 'hidden', allowBlank: false},
                    { field: 'main.bccEmail', xtype : 'ContactsDialog', fieldLabel : _lang.Email.fBccPerson, cls: 'col-1',
                        formId : conf.mainFormPanelId, hiddenName : 'main.bccName'
                    },

                    { field: 'main.emailTemplateId', xtype: 'hidden', allowBlank: false},

                    { field: 'main.emailSettingId', id:'main_emailSettingId', xtype: 'comboremote', fieldLabel: _lang.Email.fEmailSettingId,
                        url:__ctxPath + 'desktop/emailsetting/list?mini=1', useDefault: true, cls: 'col-2',
                        subcallback: function (records) {
                            if(this.action == 'add'){
                                var content = Ext.getCmp('main_content');
                                content.editor.insertHtml('<br/><br/>'+ records[0].raw.signature);
                            }
                        }
                    },
                    { field: 'main.emailTemplateName', xtype: 'MyTemplateDialog', fieldLabel: _lang.Email.fEmailTemplate,
                        formId : conf.mainFormPanelId, hiddenName : 'main.emailTemplateId', cls: 'col-2',
                        subcallback: function (row) {
                            if(!!row && row.length>0){
                                this.meForm.up().loadTemplate.call(this.meForm.up(), row);
                            }
                        }
                    },
                    { xtype: 'button', text: _lang.Email.fHasSignature, width:100,
                        handler: function() {
                            try {
                                var signature = Ext.getCmp('main_emailSettingId').valueModels[0].raw.signature;
                                Ext.getCmp('main_content').editor.insertHtml('<br/><br/>' + signature);
                            }catch (e){}
                        }
                    },
                    { field: 'main.title', xtype: 'textfield', fieldLabel: _lang.Email.fTitle, cls: 'col-1', allowBlank : false},
                ]},

                //内容
                { field: 'main.content', id: 'main_content', xtype: 'ckeditor', width:'100%', allowBlank: false, height: 420,},

                //附件信息
                { field: 'main_documents', xtype:'hidden', value:this.record.attachment },
                { field: 'main_documentName', xtype:'hidden'},
                { field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList,
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                    scope:this, readOnly: this.isApprove
                },
            ]
        });

        // 加载表单对应的数据
        if (this.action != 'add' && this.action != 'tomail' && !Ext.isEmpty(this.record.id)) {

            var me = this;
            this.dialogEditFormPanel.loadData({ url: this.urlGet + '?id=' + this.record.id, preName: 'main1',
                success: function (response) {
                    var json = Ext.JSON.decode(response.responseText);
                    var cmp = Ext.getCmp(conf.mainFormPanelId);

                    if(me.action != 'copy'){
                        var content ='';
                        content += '<br><br><hr/>';
                        content += '<b>'+_lang.Email.fSendEmail + ': </b>' + json.data.sendEmail + '<br/>';
                        content += '<b>'+_lang.Email.fSendTime + ': </b>' + json.data.sendTime + '<br/>';
                        content += '<b>'+_lang.Email.fRecipientPerson + ': </b>' + json.data.recipientEmail + '<br/>';
                        content += '<b>'+_lang.Email.fTitle + ': </b>' + json.data.title + '<br/>';
                        content += json.data.content;
                        cmp.getCmpByName('main.content').setValue(content);
                    }

                    if(me.action == 'reply' || me.action == 'replyall'){
                        cmp.getCmpByName('main.title').setValue(_lang.TButton.reply + ': '+ json.data.title);
                        cmp.getCmpByName('main.recipientName').setValue(json.data.sendName);
                        cmp.getCmpByName('main.recipientEmail').setValue(json.data.sendEmail);
                        if(me.action == 'reply'){
                            cmp.getCmpByName('main.ccName').setValue('');
                            cmp.getCmpByName('main.ccEmail').setValue('');
                            cmp.getCmpByName('main.bccName').setValue('');
                            cmp.getCmpByName('main.bccEmail').setValue('');
                        }
                    }else if(me.action == 'forward' || me.action == 'copy') {
                        cmp.getCmpByName('main.recipientName').setValue('');
                        cmp.getCmpByName('main.recipientEmail').setValue('');

                        if(me.action == 'forward') {
                            cmp.getCmpByName('main.ccName').setValue('');
                            cmp.getCmpByName('main.ccEmail').setValue('');
                            cmp.getCmpByName('main.bccName').setValue('');
                            cmp.getCmpByName('main.bccEmail').setValue('');
                            cmp.getCmpByName('main.title').setValue(_lang.TButton.forward + ': ' + json.data.title);
                        }else{
                            cmp.getCmpByName('main.content').setValue(json.data.content);
                        }

                        //attachment init
                        cmp.getCmpByName('main.attachments').setValue(json.data.attachments);
                    }

                }
            })
        }else if(this.action == 'tomail' && this.fileId ){
            //attachment init
            var cmp = Ext.getCmp(this.mainFormPanelId);
            $HpStore({
                fields: ['id','title','bytes','category','categoryId','createdAt','creatorCnName','creatorEnName','creatorId'
                    ,'departmentCnName','departmentEnName','departmentId','extension','name','note','path','shared','status','type','updatedAt'],
                url:__ctxPath + 'mydoc/get?id='+ this.fileId, maskTo: this.id,
                callback: function(eOpts, records){
                    if(records  && records[0].data ){
                        var attachments = [{
                                document:records[0].data,
                                documentId:records[0].data.id,
                                category: records[0].data.category
                        }]
                        cmp.getCmpByName('main.attachments').setValue(attachments);
                    }
                }
            });

        }
    }// end of the initcomponents
    ,
    loadTemplate: function (row) {
        var me = this;
        Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureReplace, function(btn) {
            if (btn == 'yes') {
                var tpData = $HpStore({
                    fields: ['id','templateName','templateContent','attachments'],
                    url:__ctxPath + 'mytemplate/get?id='+ row[0].data.id, maskTo: me.dialogEditFormPanel.id,
                    callback: function(eOpts, records){
                        if(records  && records[0].data ){
                            var $templateData = {
                                MyName: curUserInfo.lang == 'zh_CN' ? curUserInfo.cnName: curUserInfo.enName,
                                MyCnName: curUserInfo.cnName,
                                MyEnName:curUserInfo.enName,
                                MyDepartment: curUserInfo.depName,
                                MyEmail: curUserInfo.email,
                                MyPhone: curUserInfo.phone
                            };

                            var tplName = new Ext.XTemplate(records[0].data.templateName).apply($templateData);
                            var tplContent = new Ext.XTemplate(records[0].data.templateContent).apply($templateData);

                            me.dialogEditFormPanel.getCmpByName('main.title').setValue(tplName);
                            me.dialogEditFormPanel.getCmpByName('main.content').setValue(tplContent);

                            //attachments
                            var cmpAtta =me.dialogEditFormPanel.getCmpByName('main.attachments').subGridPanel.getStore();
                            cmpAtta.removeAll();
                            if(!!records[0].data.attachments && records[0].data.attachments.length>0){
                                for(index in records[0].data.attachments){
                                    var attachments = {};
                                    attachments = records[0].data.attachments[index];
                                    Ext.applyIf(attachments, records[0].data.attachments[index].document);
                                    attachments.id= records[0].data.attachments[index].documentId;
                                    cmpAtta.add(attachments);
                                }
                            }
                        }
                    }
                });
            }
        });
    },
    saveFun: function(action){
        var params = {act: !! action ? action: this.actionName ? this.actionName: 'save'};
        var $businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(rows.length>0){
            for(var index in rows){
                params['attachments['+index+'].businessId'] = $businessId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                this.grid.getStore().reload();
                Ext.getCmp(this.winId).close();
            }
        });
    },
    sendFun: function () {
        this.saveFun('send');
    }
});