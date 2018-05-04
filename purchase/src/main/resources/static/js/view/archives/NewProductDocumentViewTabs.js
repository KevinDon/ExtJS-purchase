Ext.define('App.NewProductDocumentViewTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.NewProductDocumentViewTabs',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        conf.items = [];
        try {
            conf.items[0] = new App.ReportProductTabGrid({
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.Reports.tabRelatedReports,
                noTitle: true
            })
        }catch (e){console.log(e)}


        // conf.items[1] = new HP.GridPanel({
        //     id : this.mainTabPanelId+'-1',
        //     title : _lang.TText.fAttachmentList,
        //     scope : this,
        //     forceFit : false,
        //     autoLoad : false,
        //     rsort: false,
        //     bbar: false,
        //     fields: ['id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'],
        //     columns: [
        //         { header: _lang.TText.rowAction, xtype: 'rowactions', width:28, locked: true,
        //             keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
        //             actions: [{
        //                 iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
        //                 callback: function(grid, record, action, idx, col, e, target) {
        //                     this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
        //                 }
        //             },{
        //                 iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit, hide: this.readOnly,
        //                 callback: function (grid, record, action, idx, col, e, target) {
        //                     this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
        //                 }
        //             },{
        //                 iconCls: 'btnRowConfirm', btnCls: 'fa-check', tooltip: _lang.TButton.confirm, hide: this.readOnly,
        //                 callback: function (grid, record, action, idx, col, e, target) {
        //                     if(record.data.isApplied !=1)
        //                         this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
        //                     else
        //                         Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noOperation);
        //                 }
        //             }, {
        //                 iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del, hide: this.readOnly,
        //                 callback: function (grid, record, action, idx, col, e, target) {
        //                     if(record.data.isApplied !=1)
        //                         this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
        //                     else
        //                         Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noOperation);
        //
        //                 }
        //             }]
        //         },
        //         { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
        //         { header: _lang.MyDocument.fName, dataIndex: 'name', width:260 },
        //         { header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
        //         { header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
        //         { header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 }
        //     ],// end of columns
        //     appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
        //     // end of columns
        // });

        conf.items[2] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-2',
            title : _lang.TText.fImageList,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: [
                'id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName',
                'departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width:35, readOnly:true, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.MyDocument.fName, dataIndex: 'name', width:260 },
                { header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
                { header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
                { header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
            // end of columns
        });

        try{
            App.NewProductDocumentViewTabs.superclass.constructor.call(this, {
                id: this.mainTabPanelId,
                title: conf.fieldLabel,
                autoWidth: true,
                autoScroll : true,
                minHeight: 300,
                cls:'tabs'
            });
            this.initUIComponents(conf);
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
        var cmpPanel = Ext.getCmp(this.mainTabPanelId);
        for(var i = 0; i < conf.items.length; i++){
            cmpPanel.add(conf.items[i]);
        }
        cmpPanel.setActiveTab(conf.items[0]);
    },

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
        }
    }
});
