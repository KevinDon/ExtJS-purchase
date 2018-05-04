FlowProcessesView = Ext.extend(Ext.Panel, {

	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.FlowProcesses.mTitle,
			moduleName: 'FlowProcesses',
			frameId : 'FlowProcessesView',
			mainGridPanelId : 'FlowProcessesGridPanelID',
			mainFormPanelId : 'FlowProcessesFormPanelID',
			searchFormPanelId: 'FlowProcessesSubFormPanlID',
			mainTabPanelId: 'FlowProcessesTabsPanelID',
			subGridPanelId : 'FlowProcessesSubGridPanelID',
			urlList: __ctxPath + 'workfolw/processes/list',
			urlSave: __ctxPath + 'workfolw/processes/save',
			urlDelete: __ctxPath + 'workfolw/processes/delete',
			urlGet: __ctxPath + 'workfolw/processes/get',
			refresh: true,
			edit: true,
			add: true,
			copy: true,
			del: true,
			editFun:this.editRow
		};
		
		this.initUIComponents(conf);
		FlowProcessesView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel, this.gridPanel ]
		});
	},
    
	initUIComponents: function(conf) {
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			    {field:'Q-name-S-LK', xtype:'textfield', title:_lang.FlowProcesses.fName},
			    {field:'Q-code-S-LK', xtype:'textfield', title:_lang.FlowProcesses.fCode},
			    {field:'Q-deploymentId-S-LK', xtype:'textfield', title:_lang.FlowProcesses.fDeploymentId},
                {field:'Q-isPublish-N-EQ', xtype:'combo', title:_lang.FlowProcesses.fIsPublish, value: '',
                    store: [['', _lang.TText.vAll], ['1', _lang.TText.vYes], ['2',  _lang.TText.vNo]]
                },
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll], ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
                },
			]
		});// end of searchPanel

        //grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.FlowProcesses.tabListTitle,
			scope: this,
			url: conf.urlList,
			fields: [
				'id','name','deploymentId','categoryId','code','ver',
				'image','isPublish', 'status', 'createdAt','updatedAt','publishAt'
			],
			width: '55%',
			minWidth: 400,
			columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:36, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnPublish', btnCls: 'fa-hourglass-end"', tooltip: _lang.TButton.publish,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true},
				{ header: _lang.FlowProcesses.fName, dataIndex: 'name', width: 140},
				{ header: _lang.FlowProcesses.fCode, dataIndex: 'code', width: 140},
				{ header: _lang.FlowProcesses.fDeploymentId, dataIndex: 'deploymentId', width: 120, hidden:true},
				{ header: _lang.FlowProcesses.fVer, dataIndex: 'ver', width: 75 },
			    { header: _lang.FlowProcesses.fImage, dataIndex: 'image', width: 150},
                { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
                    renderer: function(value){return $renderOutputStatus(value);}
                },
			    { header: _lang.FlowProcesses.fIsPublish, dataIndex: 'isPublish', width: 65,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        else if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
			        }
                },
                { header: _lang.FlowProcesses.fPublishAt, dataIndex: 'publishAt', width: 140 },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 },
                { header: _lang.TText.fUpdatedAt, dataIndex: 'updatedAt', width: 140 }
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){

			}
		});

	},// end of the init

	editRow : function(conf){
		new FlowProcessesForm(conf).show();
	},

    btnPublish: function(){},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;

        switch (action) {
            default :
                if(record.data.isPublish == '1'){
                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsFlowIsPublised);
                }else {
                    $postUrl({
                        url: __ctxPath + 'workfolw/processes/deploy',
                        params: {id: record.data.id},
                        method: 'post',
                        maskTo: conf.mainViewPanelId,
                        scope: this,
                        // autoMessage: false,
                        callback: function (req) {
                            this.gridPanel.getView().refresh();
                            // console.log(this.gridPanel.getView());
                        }
                    })
                }


                break;
        }
    }

});