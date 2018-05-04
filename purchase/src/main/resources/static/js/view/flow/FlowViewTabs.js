Ext.define('App.FlowViewTabs', {
	extend: 'Ext.tab.Panel',
    alias: 'widget.FlowViewTabs',
    
    constructor : function(conf){
    	Ext.applyIf(this, conf);
    	conf.items = [];
    	conf.items[1] = new HP.GridPanel({
    		title: _lang.TButton.processHistory,
			id: conf.mainTabPanelId + '-view-history',
			scope: this,
			autoLoad: false,
			url: conf.urlHistoryList,
            fields: ['id', 'operatorName', 'createdAt', 'type', 'remark'],
            bbar: false,
            border:false,
            loadMask:false,
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
    	
    	conf.items[0] = new Ext.ux.IFrame({
    		title: _lang.TButton.processDirection, id: conf.mainTabPanelId + '-view-direction', loadMask:false,
			frameName: conf.mainTabPanelId + '-view-direction', autoScroll: false ,scrollFlags:{y:true}
    	});
    	
    	App.FlowViewTabs.superclass.constructor.call(this, {
    		id: conf.mainTabPanelId,
    		title: conf.fieldLabel,
    		region: 'south',
    		plain: this.plain != undefined ? this.plain: false,
//    		collapsible: this.collapsible !=undefined? this.collapsible: true,
    		split: this.split != undefined? this.split: true,
    		scope: this,
    		height: this.height !=undefined? this.height: '32%',
    		minHeight: 150,
    	    autoHeight: true,
    		autoScroll : false,
    		autoRender:true,
    		border:false,
    		cls:'view-tabs'
    	});
    	
    	this.initUIComponents(conf);

    },
    
    initUIComponents: function(conf) {
    	var cmpPanel = Ext.getCmp(conf.mainTabPanelId);
    	for(var i=0; i< conf.items.length; i++){
			cmpPanel.add(conf.items[i]);
    	}
		cmpPanel.setActiveTab(conf.items[0]);
		cmpPanel.doLayout();
	}

});
