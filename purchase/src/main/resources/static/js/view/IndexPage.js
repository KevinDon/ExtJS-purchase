var IndexPage = Ext.extend(Ext.Viewport, {
	top : new Ext.panel.Panel({
		region : 'north',
		id : '__nortPanel',
		contentEl : 'app-header',
		height : 58
	}),
	center : null,
	west : new Ext.Panel({
		region : 'west',
		id : 'west-panel',
		title : _lang.Help.fNavigationMenu,
		iconCls : 'menu-navigation',
		split : true,
		width : 230,
		layout : 'accordion',
		collapsible : true,
		items : []
	}),
	south : new Ext.Panel({
		border : false,
		region : 'south',
		tbar : [{
			text : _lang.Help.fExit,
			iconCls : 'btn-logout',
			handler : function() {
				App.Logout();
			}
		},'-',{
			id : 'messageTip',
			xtype : 'button',
			hidden : true,
			width : 50,
			height : 20,
			handler : function() {
				var megBtn = Ext.getCmp('messageTip');
				var megWin = Ext.getCmp('win');
				if (megWin == null) {
					new MessageWin().show();
				}
				megBtn.hide();
			}
		},{
			xtype : 'tbtext',
			width: 280,
			text : 'Copyright@2018',
			id : 'toolbarCompanyName'
		},'->',{
			xtype : "tbfill"		
		},{
			xtype : 'tbseparator'
		}, {
			pressed : false,
			text : _lang.Help.fTechnicalSupport,
			scope: this,
			handler : function() {
				Ext.ux.Toast.msg(_lang.Help.fPopMessageTitle,
                    "1) Function Problem: <br/>&nbsp;&nbsp; Josie.Duan's SKYPE:597158897@qq.com <a href=\"skype:live:597158897?chat\" target=\"blank\">Chat</a><br /> "+
                    "2) Data Problem: <br/>&nbsp;&nbsp; Bryan Zhao's SKYPE:47ef2b5122d0aa80 <a href=\"skype:live:47ef2b5122d0aa80?chat\" target=\"blank\">Chat</a>"
                );
			}
		}, '-', {
			text : _lang.Help.fShrink,
			iconCls : 'btn-expand',
			handler : function() {
				var panel = Ext.getCmp("__nortPanel");
				if (panel.collapsed) {
					panel.title= '';
					panel.expand();
				} else {
					panel.title= _lang.Help.fTopMenu;
					panel.collapse();
				}
			}
		}, '-', {
			xtype : 'combo',
			mode : 'local',
			editable : false,
			value : _lang.Help.fSkin,
			width : 80,
			triggerAction : 'all',
			store : [
			    ['ext-all', _lang.Help.vSkinDefault],
		        ['ext-all-classic', _lang.Help.vSkinBlue],
		        ['ext-all-gray', _lang.Help.vSkinGray]
			],
			listeners : {
				scope : this,
				'select' : function(combo, record, index) {
					if (combo.value != '') {
						var expires = new Date();
						expires.setDate(expires.getDate() + 300);
						setCookie("theme", combo.value, expires, __ctxPath);
						Ext.util.CSS.swapStyleSheet("theme", __ctxPath + "js/lib/ext/resources/css/"	+ combo.value + ".css");
					}
				}
			}
		}]
	}),
	
	//构造函数
	constructor : function() {
		this.center = new Ext.TabPanel({
			id : 'centerTabPanel',
			region : 'center',
			deferredRender : true,
			enableTabScroll : true,
			activeTab : 0,
			defaults : {
				autoScroll : true,
				closable : true
			},
			items : [],
			plugins:new Ext.ux.TabCloseMenu({closeTabText:_lang.TButton.close, closeOthersTabsText:_lang.Help.fCloseOther, closeAllTabsText:_lang.Help.fCloseAll}),
			listeners : {
				'add' : function(tabPanel, comp, index) {
					if (tabPanel.items.length > 10) {
//						tabPanel.remove(tabPanel.items.get(1));
						tabPanel.doLayout();
					}
				}
			}
		});
		
		IndexPage.superclass.constructor.call(this, {
			border : false,
			layout : "border",
			scope: this,
			items : [this.top, this.west, this.center, this.south]
		});

		// 加上首页的导航菜单
		var activeTab = getCookie('_topNavId');
		var table_width=0;
		if (activeTab == null || activeTab == undefined) activeTab = 0;
		table_width=(document.body.clientWidth)*5.5;

		this.navTab = new Ext.TabPanel({
			width : table_width,
			id : 'appNavTabPanel',
			deferredRender : true,
			enableTabScroll : true,
			activeTab : activeTab,
			frame : false,
			border : false,
			plain : true,
			height : 21,
			renderTo : 'header-topnav',
			tabMargin : 20,
			defaults : {
				autoScroll : false,
				closable : false,
				bodyStyle : 'padding-bottom: 12px;'
			},
			listeners : {
				scope : this,
				'tabchange' : function(tabPanel, tab) {
					var expires = new Date();
					expires.setDate(expires.getDate() + 300);
					setCookie("_topNavId", tab.getId(), expires, __ctxPath);
					loadWestMenu();
				}
			},
			items: []
		});

		this.initData();
	},
	
	initData : function() {
		var navTab = this.navTab;
		navTab.add(curUserInfo.topModules);
		
		setTimeout(function() {
			var actTabId = getCookie('_topNavId');
			if (actTabId) { navTab.setActiveTab(actTabId);	}
			if (navTab.getActiveTab() == null) { navTab.setActiveTab(this, navTab.items.get(0)); }
		}, 500);
	}
});

	
function loadWestMenu(isReload) {
	var alias = Ext.getCmp('appNavTabPanel').getActiveTab().id;
	
	if(!isReload && curUserInfo.topModules){
		var allMenu = curUserInfo.topModules;
		for (var i = 0; i < allMenu.length; i++) {
			if(alias == allMenu[i].id){
				_setMenuList(allMenu[i].children);
				break;
			}
		}
	}else{
		Ext.Ajax.request({
			url : __ctxPath + 'modules/menu',
			params : {alias : alias, isReload: isReload},
			scope : this,
			method: 'post',
			success : function(response, options) {
				var obj = Ext.decode(response.responseText);
				
				if(obj.success == true){
					_setMenuList(obj.data);
				}else{
					Ext.ux.Toast.msg('System Message', obj.msg);
				}
			}
		});
	}
}	

function _setMenuList(arrMenu){
	var westPanel = Ext.getCmp('west-panel');
	var __activedPanelId = getCookie("__activedPanelId");
	westPanel.removeAll();
	westPanel.doLayout();
	
	for (var i = 0; i < arrMenu.length; i++) {
		var panel = new Ext.tree.TreePanel({
			id : arrMenu[i].id,
			title : arrMenu[i].title,
			text : arrMenu[i].title,
			iconCls : arrMenu[i].iconCls,
			layout : 'fit',
			animate : true,
			border : false,
			autoScroll : true,
			allowDeselect: false,
			store : Ext.create('Ext.data.TreeStore', {
			    root: { expanded: true, children: arrMenu[i].children }
			}),
			listeners: {
				'itemclick' : App.clickNode
			},
			rootVisible : false
		});
		westPanel.add(panel);
		panel.on('expand', function(p) {
			var expires = new Date();
			expires.setDate(expires.getDate() + 30);
			setCookie("__activedPanelId", p.id,	expires, __ctxPath);
		});
		if (arrMenu[i].id == __activedPanelId) {
			westPanel.layout.activeItem = panel;
		}
	}
}
