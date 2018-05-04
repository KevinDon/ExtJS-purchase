ProductProblemFormRemarkForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);

        var conf = {
            title : _lang.ProductProblem.fRemarkEdit,
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormRemarkFormWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormRemarkFormPanelID',
            formRemarkPanelId : 'ProductProblemFormRemarkFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            urlList: __ctxPath + 'archives/troubleTicket/remark/list',
            urlSave: __ctxPath + 'archives/troubleTicket/remark/update',
            urlDelete: __ctxPath + 'archives/troubleTicket/remark/delete',
            urlGet: __ctxPath + 'archives/troubleTicket/remark/get',
            actionName: this.action,
            save: true,
            close: true,
            // saveAs: true,
            saveFun: this.saveFun,
        };

        this.initUIComponents(conf);
        ProductProblemFormRemarkForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductProblem.fRemarkEdit,
            tbar: Ext.create("App.toolbar", conf),
            width : 500, height : 300,
            items : this.formPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;
        this.formPanel = new HP.FormPanel({
            id : conf.formRemarkPanelId,
            scope: this,
            autoScroll: true,
            fieldItems : [
                //订单信息
                {field: 'id', xtype: 'hidden', value: this.id == null ? '' : this.id},
                //   备注
                { xtype: 'section', title:_lang.TText.fRemark },
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.remark', xtype: 'textarea', width: '100%', height: 200,  allowBlank:true, },
                ] },
            ]
        });// end of the initcomponents
        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.selectedId)) {
            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.selectedId,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                }
            });
        };
    },

    saveFun: function(){
        var params = { act: this.actionName ? this.actionName: 'save' };
        var id = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.winId).close();
            }
        });
    }
});