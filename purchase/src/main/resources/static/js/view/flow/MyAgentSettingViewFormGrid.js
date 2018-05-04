Ext.define('App.MyAgentSettingViewFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.MyAgentSettingViewFormGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        
        var conf = {
            title : _lang.MyAgentSetting.fFlow,
            moduleName: 'MyAgentSetting',
            frameId : 'MyAgentSettingView',
            mainGridPanelId : 'MyAgentSettingGridPanelID',
            mainFormPanelId : 'MyAgentSettingFormPanelID',
            searchFormPanelId: 'MyAgentSettingSearchPanelID',
            mainTabPanelId: 'MyAgentSettingTbsPanelId',
            subGridPanelId : 'MyAgentSettingSubGridPanelID',
        };

        this.initUIComponents(conf);

        App.MyAgentSettingViewFormGrid.superclass.constructor.call(this, {
            id: conf.formGridPanelId + '-f',
			minHeight: 200,
            height: 460, width: '100%',
            items: [ this.formGridPanel ],
            bodyCls:'x-panel-body-gray',
        });
    },
    
    initUIComponents: function(conf){
        var sm = Ext.create('Ext.selection.CheckboxModel', {
            showHeaderCheckbox: false
        });
        var tools = [{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(460);
                this.formGridPanel.setHeight(460);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(460);
                this.formGridPanel.setHeight(460);
            }}
        ];
        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            title:_lang.MyAgentSetting.fFlow,
            id: conf.subGridPanelId,
            forceFit: false,
            width: 'auto',
            height:460,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            multiSelect:  true,
            selModel: {
                selType : 'checkboxmodel'//复选框选择模式Ext.selection.CheckboxModel
            },
            rsort: false,
            //selModel: sm,
            multiSelect: true,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id','code','name','context'
            ],
            columns: [
                /*{header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false, scope:this,
                    listeners: {
                        checkchange: function(me, recordIndex, checked, eOpts){
                            var rows = $getGdItems({grid: Ext.getCmp(conf.subGridPanelId)});
                            if(!!rows && rows.length>0){
                                if(recordIndex == 0){
                                    for(var index = 0; index< rows.length; index++){
                                        if(index == 0) continue;
                                        rows[index].set('active', false);
                                    }
                                }else{
                                    rows[0].set('active', false);
                                }
                            }
                        },
                    },
                    renderer:function(value, meta, rec) {
                        try {
                            if (this.callback) this.callback.call(this.scope);
                        }catch (e){console.log(e)}
                        return $renderOutputCheckColumns(value, meta);
                    }
                },*/
                { header: 'ID', dataIndex: 'id', width: 40, hidden:true },
                { header: _lang.FlowProcesses.fName, dataIndex: curUserInfo.lang =='zh_CN'? 'context':'name',width: 150},
                { header: _lang.FlowProcesses.fCode, dataIndex: 'code', width: 200},
                //{ header: _lang.FlowProcesses.fPublishAt, dataIndex: 'publishAt', width: 140 },
            ]// end of columns
        });
    },
    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;
        var rows = $getGdItems({grid: Ext.getCmp(conf.subGridPanelId)});
        if(!!rows && rows.length>0){
            for(index in rows){
                rows[index].set('active', true);
            }
        }
        //console.log(rows);
    },
});
