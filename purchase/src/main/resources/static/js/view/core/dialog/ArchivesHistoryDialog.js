Ext.define('App.ArchivesHistoryTabGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ArchivesHistoryTabGrid',

    constructor: function (conf) {

        Ext.applyIf(this, conf);

        var conf = {
            moduleName: 'ArchivesHistory',
            historyListUrl: this.historyListUrl || 'archiveshistory/list',
            historyGetUrl: this.historyGetUrl || 'archiveshistory/get',
            historyConfirmUrl: this.historyConfirmUrl ||  'archiveshistory/confirm',
            historyDelUrl: this.historyDelUrl ||  'archiveshistory/delete',
        };
        conf.mainGridPanelId = this.farmeId || this.mainGridPanelId + '-ArchivesHistoryTabGrid';
        conf.subGridPanelId = conf.mainGridPanelId + '-SubGridPanelId';
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 200;
        conf.noTitle = this.noTitle || false;
        conf.title = this.title || '';
        conf.winForm = this.winForm || VendorDocumentForm;

        this.initUIComponents(conf);

        // App.ArchivesHistoryTabGrid.superclass.constructor.call(this, this.subGridPanel);
        App.ArchivesHistoryTabGrid.superclass.constructor.call(this, {
            id: conf.mainGridPanelId,
            minHeight: conf.defHeight,
            width: conf.defWidth,
            bodyCls:'x-panel-body-gray',
            items: [this.subGridPanel]
        });
    },

    setBusinessId: function(businessId){
        this.subGridPanel.getStore().removeAll();
        if(!! businessId)
            this.subGridPanel.getStore().proxy.url = this.subGridPanel.url + '?bid='+ businessId;
            this.subGridPanel.getStore().load({
                url : this.subGridPanel.url,
                params:{'bid': businessId},
                callback: function (response) {}
            });
    },

    refresh: function(){
        this.subGridPanel.getStore().reload();
    },

    initUIComponents: function (conf) {
         this.subGridPanel = new HP.GridPanel({
             region: 'center',
             id: conf.subGridPanelId,
             title: conf.noTitle ? '' : _lang.ArchivesHistory.mTitle,
             width: conf.defWidth,
             url: conf.historyListUrl,
             bbar: false,
             scope: this,
             header: conf.noTitle ? false : {cls: 'x-panel-header-gray'},
             tools: conf.noTitle ? false : tools,
             autoLoad: false,
             forceFit: true,
             sorters: [{property: 'ver', direction: 'DESC'}],
             fields: ['id', 'businessId', 'assigneeAt', 'assigneeId', 'assigneeCnName', 'assigneeEnName', 'assigneeAt', 'createdAt', 'ver', 'isApplied', 'status',
                'createdAt', 'updatedAt', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
             columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: this.readOnly ? 55: 120, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit, hide: this.readOnly,
                        callback: function (grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowConfirm', btnCls: 'fa-check', tooltip: _lang.TButton.confirm, hide: this.readOnly,
                        callback: function (grid, record, action, idx, col, e, target) {
                             if(record.data.isApplied !=1)
                                 this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                             else
                                 Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noOperation);
                        }
                    }, {
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del, hide: this.readOnly,
                        callback: function (grid, record, action, idx, col, e, target) {
                            if(record.data.isApplied !=1)
                                this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                            else
                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noOperation);

                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true},
                {header: 'businessId', dataIndex: 'businessId', width: 180, hidden: true},
                {header: _lang.ArchivesHistory.fIsApplied, dataIndex: 'isApplied', width: 50,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        else if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                {header: _lang.ArchivesHistory.fVer, dataIndex: 'ver', width: 50},
                {header: _lang.ArchivesHistory.fAssigneeId, dataIndex: 'assigneeId', width: 180, hidden:true},
                {header: _lang.ArchivesHistory.fAssigneeName, dataIndex: 'assigneeCnName', width: 100, hidden: curUserInfo.lang == 'zh_CN'? false: true},
                {header: _lang.ArchivesHistory.fAssigneeName, dataIndex: 'assigneeEnName', width: 120, hidden: curUserInfo.lang != 'zh_CN'? false: true},
                {header: _lang.ArchivesHistory.fAssigneeAt, dataIndex: 'assigneeAt', width: 140},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false, status:false, assignee:false}),
        });
    },

    btnRowPreview: function () {},
    btnRowEdit: function () {},
    btnRowApply: function () {},
    btnRowRemove: function () {},
    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview':
                new conf.winForm({
                    mainFormPanelId: conf.mainFormPanelId,
                    mainGridPanelId: conf.mainGridPanelId,
                    urlGet: conf.historyGetUrl,
                    isApprove: true,
                    action: 'preview',
                    record: {ahid: record.data.id, id: record.data.businessId}
                }, false).show();
                break;

            case 'btnRowEdit':
                var selectedId = record.data.id;
                new conf.winForm({
                    mainFormPanelId: conf.mainFormPanelId,
                    mainGridPanelId: conf.mainGridPanelId,
                    urlGet: conf.historyGetUrl,
                    action: 'edit',
                    record: {ahid: record.data.id, id: record.data.businessId}
                }, false).show();
                break;

            case 'btnRowConfirm':
                new conf.winForm({
                    mainFormPanelId: conf.mainFormPanelId,
                    mainGridPanelId: conf.mainGridPanelId,
                    urlGet: conf.historyGetUrl,
                    urlSave: conf.historyConfirmUrl,
                    isApprove: true,
                    isConfirm: true,
                    action: 'confirm',
                    record: {ahid: record.data.id, id: record.data.businessId}
                }, false).show();
                break;

            case 'btnRowRemove':
                $postDel({
                    url: conf.historyDelUrl,
                    ids: record.data.id,
                    callback: function () {
                        Ext.getCmp(conf.subGridPanelId).store.remove(record);
                    }
                })

                break;
        }
    }
});