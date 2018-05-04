FilesPreviewDialog = Ext.extend(HP.Window,{
	constructor : function(conf) {
		Ext.applyIf(this, conf);

		this.initUI(conf);
		FilesPreviewDialog.superclass.constructor.call(this, {
			id : 'FilesPreviewDialogWinId',
			scope : this,
			title : _lang.TText.mFilePreviewSelector,
			region: 'center', layout : 'border',
			width: conf.width || 900,
			items: [this.centerPanel]
		});
		
	},
	
	initUI : function() {

        this.tools = [{
            type: 'down', tooltip: _lang.MyDocument.mFileDownload, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                window.open(__ctxPath+'mydoc/download?fileId=' + this.fileId, '_blank');
            }
        }];

		this.centerPanel = new Ext.Panel({
			id: 'FilesPreviewDialogPanelId',
			layout: 'fit',
			region: 'center',
			scope: this,
			border: false,
			items: [
			    { xtype: 'uxiframe', loadMask: _lang.TText.loading,
			      src: __ctxPath + "mydoc/preview?fileId=" + this.fileId
			    }
			]
		});
	},
	
	winCancel : function() {
		this.close();
	}
});

Ext.define('App.FilesPreviewView', {
    extend: 'Ext.Panel',
    alias: 'widget.FilesPreviewView',

	fileId: '',
	fileInformation: {},
	options: {},
    constructor : function(conf) {
        this.fileId= conf.fileId || '';
        this.options = conf || {};

        this.options.frameId = conf.frameId || 'FilesPreviewViewFrameId';
        this.options.id = conf.frameId + '-view';
        this.options.previewForm = conf.frameId + '-frame';
        this.options.defHeight = conf.height || 300;
        this.options.defMinHeight = conf.minHeight || 300;

        var tools = [
            {
                type: 'restore', tooltip: _lang.TButton.openAs, scope: this,
                handler: function (event, toolEl, panelHeader) {
                	if(this.fileId)
                		new FilesPreviewDialog({fileId: this.fileId}).show();
                	else
                        Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
                }
            },{
                type: 'minimize', tooltip: _lang.TButton.minimize, scope: this,
                handler: function (event, toolEl, panelHeader) {
                    this.setHeight(this.options.defHeight);
                }
            }, {
                type: 'maximize', tooltip: _lang.TButton.maximize, scope: this,
                handler: function (event, toolEl, panelHeader) {
                    this.setHeight(700);
                }
            }];


        App.FilesPreviewView.superclass.constructor.call(this, {
            layout: 'fit',
			id: this.options.id,
            title:_lang.MyDocument.tFilePreview,
            region: 'center',
            tools: tools,
			cls: 'file-preview-win',
			width: this.options.width || '100%',
            border: this.options.border || false,
            height: this.options.defHeight,
            minHeight: this.options.minHeight,
            items: [
                { xtype: 'uxiframe', id:this.options.previewForm, loadMask: _lang.TText.loading,
                    src: __ctxPath + "mydoc/preview?fileId=" + this.fileId
                }
            ]
        });
    },
    getValue: function() {
        return this.fileId;
    },
    setValue: function(value) {
    	if( typeof (value) == 'object'){
    		this.fileInformation = value;
    		this.fileId = !!value && !!value.documentId ? value.documentId || '' : '';
		}else {
            this.fileId = value || '';
        }
        Ext.getCmp(this.options.previewForm).load(__ctxPath + "mydoc/preview?fileId=" + this.fileId);
    },

});