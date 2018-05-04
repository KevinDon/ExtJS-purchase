Ext.define('App.ReportProductComplianceFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ReportProductComplianceFormGrid',
    width: '100%',
    //height: 300,
    bodyCls:'x-panel-body-gray',

    initComponent: function(){
        var conf = {
            frameId : 'ReportProductComplianceView',
            mainFormPanelId : 'ReportProductComplianceViewFormPanelID',
            formId: 'ReportProductComplianceForm',
            formGridPanelId : 'ReportProductComplianceFormGridPanelID' + Ext.id(),
        };

        // conf.mainGridPanelId = this.mainGridPanelId || this.formId + '-PackingListMultiGrid';
        conf.panelId = this.panelId || 'ReportProductComplianceFormGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.formId + '-ReportProductComplianceForm';
        conf.formGridPanelId = this.gridPanelId ? this.gridPanelId + Ext.id() : 'ReportOrderInspectionFormGridPanelID';
        conf.subGridPanelId = this.subGridPanelId || conf.panelId +'-GridPanelID';
        conf.defHeight = this.height || 200;

        //this.initEditFormPanel(conf);
        this.initGridPanel(conf);
        this.loadData();
        Ext.apply(this, {
            items: [this.formGridPanel]
        });

        this.superclass.initComponent.call(this);
    },

    initGridPanel: function(conf){
        var scope = this;
        var tools = [{
            type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                var grid = this.formGridPanel;
                grid.getStore().insert(grid.getStore().count(), {});
            }
        }, {
            type: 'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(260);
                this.formGridPanel.setHeight(258);
            }
        }, {
            type: 'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(698);
            }
        }];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.formGridPanelId,
            title: _lang.FlowCustomClearance.fDetails,
            forceFit: false,
            width: '100%',
            height: 242,
            url: '',
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id', 'sku', 'categoryName', 'name'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [
                        {
                            iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                            callback: function(grid, record, action, idx, col, e, target) {
                                this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                            }
                        }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.FlowCustomClearance.fSku, dataIndex: 'sku', width: 200,
                    editor: {
                        xtype: 'ProductDialog',
                        single: true,
                        subcallback: function (rows) {
                            if(rows && rows.length > 0) {
                                var data = rows[0].raw;
                                var gridRow = scope.formGridPanel.getSelectionModel().selected.getAt(0);
                                gridRow.set('sku', data.sku);
                                gridRow.set('categoryName', data.categoryName);
                                gridRow.set('name', data.name);
                            }
                        }
                    }
                },
                { header: _lang.FlowCustomClearance.fBarcode, dataIndex: 'categoryName', width: 200, editor: {xtype: 'textfield'}},
                { header: _lang.FlowCustomClearance.fProductName, dataIndex: 'name', width: 200, editor: {xtype: 'textfield'}},
            ]// end of columns
        });
    },
    onRowAction: function(grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowRemove' :
                Ext.getCmp(conf.subGridPanelId).store.remove(record);
                break;
            default :
                break;
        }
    },

    loadData: function(){
        var data = this.data;
        if(!data) return;
        if(!!data.packingList){
            this.formGridPanel.getStore().add(data.packingList);
        }

        this.editFormPanel.getForm().setValues(data);
    }
});