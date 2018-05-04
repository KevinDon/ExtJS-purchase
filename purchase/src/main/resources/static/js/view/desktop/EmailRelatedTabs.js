Ext.define('App.EmailRelatedTabs', {
	extend: 'Ext.tab.Panel',
    alias: 'widget.EmailRelatedTabs',
    
    constructor : function(conf){
    	Ext.applyIf(this, conf);
    	this.conf.items = [];
    	this.conf.items[0] = new App.MyDocumentFormMultiGrid({
            title: _lang.TText.fAttachmentList,
            subGridPanelId : this.conf.mainTabPanelId + '-attachments',
            scope:this,
            noTitle:true,
            defHeight: 150,
            readOnly: true
		});
    	
        // this.conf.items[1] = new HP.FormPanel({
    		// title : _lang.Email.tabOrder,
    		// id : this.conf.mainTabPanelId + '-order',
		// 	scope : this,
		// 	fieldItems : [
		// 		{ field: 'main.desc[1].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fEnCateTitle, allowBlank: false},
		// 		{ field: 'main.desc[1].lang', xtype: 'hidden', value: 'en_AU'}
		// 	]
		// });
        // this.conf.items[2] = new HP.FormPanel({
         //    title : _lang.Email.tabSku,
         //    id : this.conf.mainTabPanelId + '-product',
         //    scope : this,
         //    fieldItems : [
         //        { field: 'main.desc[1].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fEnCateTitle, allowBlank: false},
         //        { field: 'main.desc[1].lang', xtype: 'hidden', value: 'en_AU'}
         //    ]
        // });
        // this.conf.items[3] = new HP.FormPanel({
    		// title : _lang.Email.tabVendor,
    		// id : this.conf.mainTabPanelId + '-vendor',
		// 	scope : this,
		// 	fieldItems : [
		// 		{ field: 'main.desc[1].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fEnCateTitle, allowBlank: false},
		// 		{ field: 'main.desc[1].lang', xtype: 'hidden', value: 'en_AU'}
		// 	]
		// });
        //
        // this.conf.items[4] = new HP.FormPanel({
    		// title : _lang.Email.tabServiceProvider,
    		// id : this.conf.mainTabPanelId + '-service',
		// 	scope : this,
		// 	fieldItems : [
		// 		{ field: 'main.desc[1].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fEnCateTitle, allowBlank: false},
		// 		{ field: 'main.desc[1].lang', xtype: 'hidden', value: 'en_AU'}
		// 	]
		// });

        this.conf.items[5] = new HP.FormPanel({
            title : _lang.Email.tabSubjection,
            id : this.conf.mainTabPanelId + '-subjection',
            scope : this,
            fieldItems : $groupFormCreatedColumns({sort:false, status: false}),
        });
    	
    	try{
            var tools = [{
                type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
                handler: function(event, toolEl, panelHeader) {
                    this.setHeight(200);
                }},{
                type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
                handler: function(event, toolEl, panelHeader) {
                    this.setHeight(550);
                }}
            ];

	    	App.EmailRelatedTabs.superclass.constructor.call(this, {
	    		id: this.conf.mainTabPanelId,
	    		title: _lang.Email.tabRelatedTabs,
                region: 'south',
				tools: tools,
                plain: !!this.plain ? this.plain: false,
   				collapsible: !!this.collapsible? this.collapsible: true,
                split: !!this.split ? this.split: true,
                height: !!this.height ? this.height: '20%',
                minHeight: !!this.minHeight ? this.minHeight: 200,
                autoHeight: true,
                autoScroll : false,
                cls:'x-panel-tabs-gray'
	    	});
	    	
	    	this.initUIComponents(this.conf);
    	
    	}catch(e){
    		console.log(e);
    	}
    },
    
    initUIComponents: function(conf) {
    	var cmpPanel = Ext.getCmp(conf.mainTabPanelId);
    	for(var i=0; i< conf.items.length; i++){
			cmpPanel.add(conf.items[i]);
    	}
		cmpPanel.setActiveTab(conf.items[0]);
	}

});
