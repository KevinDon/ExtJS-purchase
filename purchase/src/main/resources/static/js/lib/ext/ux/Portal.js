/**
 * @class Ext.app.Portal
 * @extends Object
 * A sample portal layout application class.
 */

Ext.define('Ext.ux.Portal', {
    extend: 'Ext.Panel',
    requires: ['Ext.app.Portlet', 'Ext.app.PortalPanel', 'Ext.app.PortalColumn'],
    
    layout: 'column',
    autoScroll : true,
    cls : 'x-portal',
    defaultType : 'portalcolumn',
    
    initComponent : function(){
        Ext.ux.Portal.superclass.initComponent.call(this);
        this.addEvents({
            validatedrop:true,
            beforedragover:true,
            dragover:true,
            beforedrop:true,
            drop:true
        });
    },
    
	initEvents : function(){
        Ext.ux.Portal.superclass.initEvents.call(this);
        this.dd = new Ext.app.PortalDropZone(this, this.dropConfig);
    },
    
    beforeDestroy : function() {
        if(this.dd){
            this.dd.unreg();
        }
        Ext.ux.Portal.superclass.beforeDestroy.call(this);
    },
    
    createPortlet : function(id, title, items) {
		return new Ext.app.Portlet({
			id : id,
			title : title,
			style : 'padding:0px 0 5px 0px; margin-bottom:5px',
			items : items
		});
	},
	createColumn : function(id, columnWidth) {
		return new Ext.app.PortalColumn({
			id : id,
			columnWidth : columnWidth,
			style : 'padding:10px 5px 10px 10px'
		});
	},
	
    removeAllportlet : function() { // 移除所有的portlet
		if (this.items && this.items.length > 0) {
			for (var i = 0; i < this.items.length; i++) {
				var c = this.items.getAt(i);
				if (c.items) {
					for (var j = c.items.length - 1; j >= 0; j--) {
						c.remove(c.items.getAt(j), true);
					}
				}

			}
		}
	}
});
