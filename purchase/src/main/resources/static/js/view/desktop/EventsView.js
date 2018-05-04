EventsView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Events.tabListTitle,
			moduleName: 'Events',
			frameId : 'EventsView',
			mainGridPanelId : 'EventsGridPanelID',
			mainFormPanelId : 'EventsFormPanelID',
			subGridPanelId : 'EventsSubGridPanelID',
			subFormPanelId : 'EventsSubFormPanelID',
			searchFormPanelId: 'EventsSearchFormPanelID',
			mainTabPanelId: 'EventsTabsPanelId',
			urlList: __ctxPath + 'desktop/events/list',
            urlGet: __ctxPath + 'desktop/events/get',
            urlHistoryList: __ctxPath + 'flow/workflow/history/list',
			urlDirection: __ctxPath + 'flow/workflow/viewimage?processInstanceId=',
            refresh: true
		};
		this.initUIComponents(conf);
		
		EventsView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.centerPanel ]
		});
	},
	
	initUIComponents: function(conf) {
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.Events.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: ['id', 'businessId','taskName','startUserName', 'startTime' , 'createTime'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
				{ header: _lang.Events.fBusinessId, dataIndex: 'businessId', width: 180 },
			    { header: _lang.Events.fTaskName, dataIndex: 'taskName', width: 300 },
			    { header: _lang.Events.fStartUser, dataIndex: 'startUserName', width: 100 },
			    { header: _lang.Events.fStartTime, dataIndex: 'startTime', width: 140 },
                { header: _lang.Events.fArrivedTime, dataIndex: 'createTime', width: 140 }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//内容form
		this.centerFormPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			region: 'center',
			title: _lang.Events.tabContentTitle,
			scope: this,
			fieldItems: [
			    { field: 'main.id',	xtype: 'hidden'},
			    { field: 'main.taskName', xtype: 'displayfield', fieldLabel: _lang.Events.fTaskName},
			    { field: 'main.businessId', xtype: 'displayfield', fieldLabel: _lang.Events.fBusinessId},
			    { field: 'main.startTime', xtype: 'displayfield', fieldLabel: _lang.Events.fStartTime},
			    { field: 'main.createTime', xtype: 'displayfield', fieldLabel: _lang.Events.fArrivedTime},
			    { field: 'main.startUserName', xtype: 'displayfield', fieldLabel: _lang.Events.fStartUser},
			    { field: 'taskLink', id:'taskLink', xtype: 'button',  text: _lang.Events.fGo, align: 'right', hidden:true, width:'100%'},
			]
		});
		
		conf.height = 500;
		conf.plain = false;
		conf.collapsible = false;		
		var viewTabs = Ext.create('widget.FlowViewTabs', conf);
		
		this.eastPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-east',
			region: 'center',
			layout: 'border',
			border: false,
			items: [ this.centerFormPanel, viewTabs]
		});
		
		//主体内容
		this.centerPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-top',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.eastPanel]
		});
				
	},// end of the init
		
	rowClick: function(record, item, index, e, conf) {
        this.centerFormPanel.form.reset();
		this.centerFormPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main', 
			success:function(response, options){
				if(response.responseText){
					var obj = Ext.JSON.decode(response.responseText);
	    			if(obj.success == true){

	    				var cmpHistory = Ext.getCmp(conf.mainTabPanelId + '-view-history');
	    				cmpHistory.getStore().removeAll();
	    				if(obj.data.processInstanceId){
	    					cmpHistory.getStore().url = conf.urlHistoryList;
	    					$HpSearch({
	    						searchQuery: {"Q-businessId-S-EQ": obj.data.businessId},
	    						gridPanel: cmpHistory
	    					});
	    				};
	    				
	    				var cmpDirection = Ext.getCmp(conf.mainTabPanelId + '-view-direction');
	    				if(obj.data.processInstanceId){
	    					cmpDirection.load(conf.urlDirection + obj.data.processInstanceId);
	    				}else{
	    					cmpDirection.clean();
	    				}
	    				
	    				jQuery('#taskLink').show();
	    				jQuery('#taskLink').unbind();
	    				jQuery('#taskLink').bind('click', function(e){
	    					try{
	    						conf.record = {
	    							id: obj.data.businessId,
	    							flowStatus: 1,
	    							status: 1,
	    						};
		    					App.clickTopTab(obj.data.formKey, conf);
		    					jQuery('#taskLink').hide();
	    					}catch(e){}
	    				});
	    			}
				
    			}
			}
		});
	}
});