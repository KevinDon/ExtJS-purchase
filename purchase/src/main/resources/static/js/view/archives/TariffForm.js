TariffForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.isApprove = this.isApprove ? true: false;

        var conf = {
            title : _lang.Duty.mTitle,
            winId : 'TariffFormWinID',
            moduleName: 'Tariff',
            frameId : 'TariffView',
            viewPanelId : 'TariffViewPanelID',
            mainGridPanelId : 'TariffMainGridPanelID',
            mainFormPanelId : 'TariffFormPanelID',
            mainTabPanelId: 'TariffTbsPanelID',
            formGridPanelId : 'TariffFormGridPanelID',
            urlList: __ctxPath + 'archives/tariff/list',
            urlSave:_cfg.urlSave ||  __ctxPath + 'archives/tariff/save',
            urlDelete:_cfg.urlDelete ||  __ctxPath + 'archives/tariff/delete',
            urlGet:_cfg.urlGet ||  __ctxPath + 'archives/tariff/get',
            ahid: !!_cfg.record ?_cfg.record.ahid :'',
            actionName: this.action,
            refresh: true,
            save: !this.isConfirm && !this.isApprove,
            cancel: true,
            confirm: this.isConfirm,
            saveFun: this.saveFun
        };

        this.initUIComponents(conf);
        TariffForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
            tbar: Ext.create("App.toolbar", conf),
            minWidth: 500, width : 600, height : 350,
            items : [this.formPanel]
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;

        var $status = [['0', _lang.TText.vDraft], ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]];
        if(this.action == 'add'){
            $status = [['0', _lang.TText.vDraft]];
        }else if(this.record.status>0){
            $status = [['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled], ['3', _lang.TText.vDeleted]];
        }


        this.formPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            fieldItems : [
                { field: 'id',	xtype: 'hidden', value : this.action == 'add' ? '': this.record.id },
                { field: 'main.itemCode', xtype: 'textfield', fieldLabel: _lang.Duty.fHsCode, allowBlank: false},
                { field: 'main.dutyRate', xtype: 'numberfield', decimalPrecision: 2, minValue: 0, fieldLabel: _lang.Duty.fRate, allowBlank: false},
                { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, store: $status, value:'0' },
                { field: 'main.description', xtype: 'htmleditor', fieldLabel:_lang.Duty.fDescription,  height:300 },
            ]
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
        	this.formPanel.loadData({
                url : conf.urlGet + '?id=' + this.record.id + (this.record.ahid ? '&hid='+ this.record.ahid: ''),
                preName : 'main', loadMask:true, scope: this
            });
        }
        this.formPanel.setReadOnly(this.isApprove || this.isConfirm, []);
    },

    saveFun: function(action){
        var params = {act: action? action: this.actionName ? this.actionName: 'save'};
        if(action == 'confirm') params.ahid = this.ahid;

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.mainGridPanelId).getStore().reload();
                Ext.getCmp(this.mainGridPanelId + '-ArchivesHistoryTabGrid').refresh();
                Ext.getCmp(this.winId).close();
            }
        });
    }
});