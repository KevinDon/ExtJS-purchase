Ext.define("App.FlowWindow", {
    extend: 'Ext.tab.Panel',
    alias: 'widget.FlowWindow',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        this.conf.hidden = conf.hidden;
        this.conf.items = [];
        this.conf.mainTabPanelId += '-win';

        this.conf.items[0] = new HP.FormPanel({
            title : _lang.TButton.processRemark,
            id: this.conf.mainTabPanelId + '-0',
            scope: this,
            hidden:conf.hidden,
            autoScroll : true,
            header:{
                cls:'x-panel-header-gray'
            },
            fieldItems:[
                { field: 'flowNextHandler', xtype:'hidden'},
                { field: 'flowNextHandlerAccount', xtype: 'combo', hiddenName:'flowNextHandler', onlyRead:false, allowBlank:true, fieldLabel: _lang.Flow.fNextHandler, width:'100%', hidden:true},
                { field: 'flowRemark', xtype: 'ckeditor', width:'100%', height:190},
            ]
        });
        this.conf.items[1] = new HP.GridPanel({
            title : _lang.TButton.processHistory,
            id: this.conf.mainTabPanelId + '-1',
            scope: this,
            hidden:conf.hidden,
            url: this.conf.urlHistoryList,
            fields: ['id', 'operatorName', 'createdAt', 'type', 'remark'],
            bbar: false,
            header:{
                cls:'x-panel-header-gray'
            },
            columns:[
                { header: 'ID', dataIndex: 'id', width: 80, hidden: true, sortable:false},
                { header: _lang.Flow.fOperator, dataIndex: 'operatorName', width: 100, sortable:false},
                { header: _lang.Flow.fCreatedAt, dataIndex: 'createdAt', width: 140, sortable:false},
                { header: _lang.Flow.fType, dataIndex: 'type', width: 40, sortable:false,
                    renderer: function(value) { return $renderGridDictColumn(value, _dict.flowHistoryStatus, [])}
                },
                { header: _lang.Flow.fRemark, dataIndex: 'remark', width: 200, sortable:false},
            ]
        });
        this.conf.items[2] = Ext.create('Ext.panel.Panel', {
            title : _lang.TButton.processDirection,
            id: this.conf.mainTabPanelId + '-2',
            height: '100%',
            header:{
                cls:'x-panel-header-gray'
            },
            items:[
                {xtype:'uxiframe', url: this.conf.urlDirection, frameName: this.conf.mainTabPanelId + '-direction-frame', height:'100%'}
            ]
        });

        App.FlowWindow.superclass.constructor.call(this, {
            id: this.conf.mainTabPanelId,
            title: _lang.Flow.flowWindow,
            tabPosition : 'bottom',
            scrollFlags: 'scroll',
            resizable: true,
            resizeHandles: 's',
            collapsible: true,
            split: true,
            height: conf.height,
            autoWidth: true,
            autoHeight: true,
            autoScroll : true,
            hidden: this.conf.hidden,
            bodyCls:'x-panel-body-gray',
            header: {cls:'x-panel-header-gray'},
            cls:'flow-tabs x-panel-tabs-gray'
        });

        this.initUIComponents(this.conf);
        this.updateLayout();
    },

    initUIComponents: function(conf) {
        var cmpPanel = Ext.getCmp(conf.mainTabPanelId);
        for(var i=0; i< conf.items.length; i++){
            cmpPanel.add(conf.items[i]);
        }
        if(conf.hidden){
            cmpPanel.setActiveTab(conf.items[2]);
        }else{
            cmpPanel.setActiveTab(conf.items[0]);
        }
    }
});