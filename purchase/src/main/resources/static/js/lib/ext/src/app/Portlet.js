/**
 * @class Ext.app.Portlet
 * @extends Ext.panel.Panel
 * A {@link Ext.panel.Panel Panel} class that is managed by {@link Ext.app.PortalPanel}.
 * closable: true,
 * collapsible: true,
 */
Ext.define('Ext.app.Portlet', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.portlet',
    
    constructor : function(conf) {
		Ext.applyIf(this, conf);
		autoLoadObj = null;
		if(this.url){
			var me = this;
			autoLoadObj = {	
				url: this.url, scripts: true, loadMask:true, 
				success: function(response, options){
					if(me.callback) me.callback.call(me, response, options);
				}	
			}
		}
		
		this.closable=true;
		
		Ext.app.Portlet.superclass.constructor.call(this, {
			layout: this.layout? this.layout : 'fit',
			height: this.height? this.height : 180,
			autoLoad: autoLoadObj,
			listeners :{
				afterrender: function(layout, eOpts){
					if(this.afterrender) this.afterrender.call(this, layout, eOpts);
				}
			}
		});
	},

    anchor: '100%',
    frame: true,
    animCollapse: true,
    draggable: {
        moveOnDrag: false    
    },
    cls: 'x-portlet',

    // Override Panel's default doClose to provide a custom fade out effect
    // when a portlet is removed from the portal
    doClose: function() {
        if (!this.closing) {
            this.closing = true;
            this.el.animate({
                opacity: 0,
                callback: function(){
                    var closeAction = this.closeAction;
                    this.closing = false;
                    this.fireEvent('close', this);
                    this[closeAction]();
                    if (closeAction == 'hide') {
                        this.el.setOpacity(1);
                    }
                },
                scope: this
            });
        }
    }
});