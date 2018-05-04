Ext.define('App.MyDocumentCategoryTabs', {
	extend: 'Ext.tab.Panel',
    alias: 'widget.MyDocumentCategoryTabs',
    
    constructor : function(conf){
    	Ext.applyIf(this, conf);
    	
    	this.conf.items = [];
    	this.conf.items[0] = new HP.FormPanel({
    		title : _lang.TText.zh_CN,
			id: this.conf.mainTabPanelId + '-0',
			scope: this,
			fieldItems:[
			   { field: 'main.desc[0].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fCnCateTitle, allowBlank: false},
			   { field: 'main.desc[0].context', xtype: 'htmleditor', fieldLabel: _lang.Dictionary.fCnText, allowBlank: false},
			   { field: 'main.desc[0].lang', xtype: 'hidden', value: 'zh_CN'}
			]
		});
    	
    	this.conf.items[1] = new HP.FormPanel({
    		title : _lang.TText.en_AU,
    		id : this.conf.mainTabPanelId + '-1',
			scope : this,
			fieldItems : [
				{ field: 'main.desc[1].title', xtype: 'textfield', fieldLabel: _lang.Dictionary.fEnCateTitle, allowBlank: false},
				{ field: 'main.desc[1].context', xtype: 'htmleditor', fieldLabel: _lang.Dictionary.fEnText, allowBlank: false},
				{ field: 'main.desc[1].lang', xtype: 'hidden', value: 'en_AU'}
			]
		});
		
    	try{
	    	App.MyDocumentCategoryTabs.superclass.constructor.call(this, {
	    		id: this.conf.mainTabPanelId,
	    		title: conf.fieldLabel,
	    	    autoHeight: true,
	    		autoScroll : true,
                bodyCls:'x-panel-body-gray',
                cls:'sub-tabs x-panel-tabs-gray'
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
