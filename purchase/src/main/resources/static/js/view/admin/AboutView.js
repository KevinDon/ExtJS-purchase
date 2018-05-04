AboutView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		this.initUIComponents();
		AboutView.superclass.constructor.call(this, {
			id: 'AboutView', title: '关于系统',
			region: 'center', layout : 'border',
			closable: true,
			items: [ this.gridPanel ]
		});
	},
	
	initUIComponents: function() {
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: 'AboutViewGridPanel',
			scope: this,
			edit: true,
			url: __ctxPath + "/admin/about",
			fields: [ 'id', 'title', 'info'],
			columns: [
			   { header: 'ID', dataIndex: 'id', hidden: true},
			   { header: '项目名称', dataIndex: 'title', width: 100},
			   { header: '相关信息', dataIndex: 'info', width: 400, editor: 'textfield'}
			],// end of columns
		});
	}// end of the init
	
});