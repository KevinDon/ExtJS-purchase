FlowProcessesForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {

        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove || this.record.flowStatus>0 && this.action != 'add' && this.action != 'copy' ? true: false;

        var conf = {
            winId : 'FlowProcessesFormWinID',
            moduleName: 'Vendor',
            title : _lang.FlowProcesses.mTitle,
            mainGridPanelId : 'FlowProcessesGridPanelID',
            mainFormPanelId : 'FlowProcessesFormPanelID',
            searchFormPanelId: 'FlowProcessesSubFormPanlID',
            mainTabPanelId: 'FlowProcessesTbsPanelId',
            subGridPanelId : 'FlowProcessesSubGridPanelID',
            mainFormGridPanelId : 'FlowProcessesFormCategoryGridPanelID',
            urlList: __ctxPath + 'workfolw/processes/list',
            urlSave: _cfg.urlSave || __ctxPath + 'workfolw/processes/save',
            urlDelete: _cfg.urlDelete || __ctxPath + 'workfolw/processes/delete',
            urlGet: _cfg.urlGet ||  __ctxPath + 'workfolw/processes/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            actionName: this.action,
            refresh: true,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            confirm: this.isConfirm,
            saveFun: this.saveRow
        };

        this.initUIComponents(conf);
        FlowProcessesForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.FlowProcesses.mTitle + $getTitleSuffix(this.action),
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
                {field: 'id',	xtype: 'hidden', value: this.action == 'add' ? '': this.record.id },
                { xtype: 'container', cls:'row', items: [
                    { field: 'main.name', xtype: 'textfield', fieldLabel: _lang.FlowProcesses.fName, cls:'col-2', allowBlank:false,  },
                    { field: 'main.code', xtype: 'textfield', fieldLabel: _lang.FlowProcesses.fCode, cls:'col-2', allowBlank: false, },
                    { field: 'main.model', xtype: 'textfield', fieldLabel: _lang.FlowProcesses.fModel, cls:'col-2', allowBlank: false, },
                    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, cls:'col-2', allowBlank:true,  value:'1',
                        store: [['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                    },
                    { field: 'main.isPublish', xtype: 'displayfield', fieldLabel: _lang.FlowProcesses.fIsPublish, cls:'col-2',
                        renderer: function(value){
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                            if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                        }
                    },
                    { field: 'main.context', xtype: 'textareafield', fieldLabel: _lang.FlowProcesses.fContext, cls:'col-1' },
                    { field: 'main.content', xtype: 'textareafield', fieldLabel: _lang.FlowProcesses.fContent, cls:'col-1', allowBlank:false, height:450,
                    },
                    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2'},
                    { field: 'main.updatedAt', xtype: 'displayfield', fieldLabel: _lang.TText.fUpdatedAt, cls:'col-2'},
                    { field: 'main.publishAt', xtype: 'displayfield', fieldLabel: _lang.FlowProcesses.fPublishAt, cls:'col-2'},
                ] },
            ]

        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url: conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName: 'main', loadMask:true,
                success: function(response){
                    var json = Ext.JSON.decode(response.responseText);
                }
            });
        }

        this.editFormPanel.setReadOnly(this.isApprove || this.isConfirm, []);

    },// end of the init

    saveRow: function(action){
        var params = {act: !! action ? action: this.actionName ? this.actionName: 'save'};

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function (fp, action, status, grid) {
                Ext.getCmp(this.winId).close();
                if (!!grid) {
                    grid.getView().refresh();
                }
            }
        });
    }

});