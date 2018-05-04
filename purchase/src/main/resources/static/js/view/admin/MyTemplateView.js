MyTemplateView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.MyTemplate.mTitle,
			moduleName: 'MyTemplate',
			frameId : 'MyTemplateView',
			mainGridPanelId : 'MyTemplateViewGridPanelID',
			mainFormPanelId : 'MyTemplateViewFormPanelID',
			searchFormPanelId: 'MyTemplateViewSearchFormPanelID',
			mainTabPanelId: 'MyTemplateViewTbsPanelId',
			urlList: __ctxPath + 'mytemplate/list',
			urlSave: __ctxPath + 'mytemplate/save',
			urlDelete: __ctxPath + 'mytemplate/delete',
			urlGet: __ctxPath + 'mytemplate/get',
            actionName: this.action,
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true,
            saveFun: this.saveFun,
            copyFun: this.copyFun,
		};
		this.initUIComponents(conf);
		
		MyTemplateView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.searchPanel, this.centerPanel ]
		});
	},
	
	initUIComponents: function(conf) {
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			    {field:'Q-name-S-LK', xtype:'textfield', title:_lang.MyTemplate.fName},
				{field:'Q-shared-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fShared, code:'document', codeSub:'shared', addAll:true},
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    },
			    {field:'Q-type-N-EQ', xtype:'combo', title: _lang.MyTemplate.fType, value:'', 
			    	store: [['', _lang.TText.vAll], ['2',  _lang.MyTemplate.vTypeEmail], ['1',  _lang.MyTemplate.vTypeOnlyFile], ['3',  _lang.MyTemplate.vTypeExportWord]]
			    },
			    {field:'Q-context-S-LK', xtype:'textfield', title:_lang.MyTemplate.fContext},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
			    
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.MyTemplate.tabDictCateTitle,
			collapsible: true,
			forceFit: false,
			split: true,
			scope: this,
			url: conf.urlList,
			fields: ['id','name','type','context','status','shared','createdAt','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'],
			width: '45%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
				{ header: _lang.MyTemplate.fName, dataIndex: 'name', width:200 },
				{ header: _lang.MyTemplate.fContext, dataIndex: 'context', width:200 },
                { header: _lang.MyTemplate.fType, dataIndex: 'type', width:50,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.MyTemplate.vTypeOnlyFile);
                        else if(value == '2') return $renderOutputColor('blue', _lang.MyTemplate.vTypeEmail);
                        else if(value == '3') return $renderOutputColor('yellow', _lang.MyTemplate.vTypeExportWord);
                    }
                },
				{ header: _lang.TText.fShared, dataIndex: 'shared', width:60,
					renderer: function(value){
						$shared = _dict.shared;
			    		if($shared.length>0 && $shared[0].data.options.length>0){
			    		    return $dictRenderOutputColor(value, $shared[0].data.options);
			    		}
			    	}
				},
			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
		});
		
		//内容form
		this.centerFormPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			region: 'center',
			title: _lang.MyTemplate.mTitle,
			scope: this,
			fileUpload:true,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
				{ field: 'main.name', xtype:'textfield', fieldLabel: _lang.MyTemplate.fName},
				{ field: 'main.context', xtype: 'htmleditor', fieldLabel: _lang.MyTemplate.fContext, allowBlank: false, width: '100%', height: 100},
                { field: 'main.shared', xtype: 'dictcombo', fieldLabel: _lang.TText.fShared, code:'document', codeSub:'shared', value:'1'},
                { field: 'main.type',xtype: 'combo', fieldLabel: _lang.TText.fType, allowBlank: false, value:'1', triggerAction: 'all', width: '100%',
                    store: [['1', _lang.MyTemplate.vTypeOnlyFile], ['2', _lang.MyTemplate.vTypeEmail], ['3',  _lang.MyTemplate.vTypeExportWord]],
                    listeners:{
                        select: function(combo, records, eOpts){
                            combo.up().scope.selectFun.call(combo, combo.getValue());
                        }
                    }
                },
                { xtype: 'container',cls:'row', id: 'container-email-template', hidden:'1', items: [
					{ xtype: 'section', title:_lang.MyTemplate.tabTemplateInformation},
					{ field: 'main.templateName', xtype:'textfield', fieldLabel: _lang.MyTemplate.fTemplateName, cls: 'col-1'},
					{ xtype: 'section', title: _lang.MyTemplate.fTemplateContent},
					{ field: 'main.templateContent', xtype: 'htmleditor', cls: 'col-1', width:'100%', height:300},
				]},
                { xtype: 'container',cls:'row', items: [
					//附件信息
					{ field: 'main_documents', xtype:'hidden'},
					{ field: 'main_documentName', xtype:'hidden'},
					{ field: 'main.attachments', xtype: 'MyDocumentFormMultiGrid',fieldLabel: _lang.TText.fAttachmentList, width:'100%', height: 150,
						mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
						scope:this, readOnly: this.isApprove
					}
				]}
			],
			appendItems: $groupFormCreatedColumns({sort:false})
		});

		//主体内容
		this.centerPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-top',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.centerFormPanel]
		});
				
	},// end of the init

    selectFun: function(value){
		var cmp = Ext.getCmp('container-email-template');
		if(value == '1' || value == '3'){
            cmp.hide();
		}else{
			cmp.show();
		}
	},
	rowClick: function(record, item, index, e, conf) {
		this.centerFormPanel.loadData({
			url: conf.urlGet + '?id=' + record.data.id,
			root: 'data', preName : 'main', maskTo: conf.mainFormPanelId,
			success:function(response, options){
				if(response.responseText){
					var json = Ext.JSON.decode(response.responseText);
	    			if(!json.success){ return; }

	    			this.scope.selectFun(json.data.type);

                  //attachment init
                    Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.attachments').setValue(json.data.attachments);
                    
    			}
			}
		});
	},
    saveFun: function(action){
        var params = {act: !! action ? action: this.actionName ? this.actionName: 'save'};
        var $businessId = Ext.getCmp(this.mainFormPanelId).getCmpByName('id').getValue();

        //attachments
        var rows = $getGdItemsIds({grid: Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel});
        if(rows.length>0){
            for(var index in rows){
                params['attachments['+index+'].businessId'] = $businessId;
                params['attachments['+index+'].documentId'] = rows[index];
            }
        }

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            grid: Ext.getCmp(this.mainGridPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                this.refreshRs.call(this);
                Ext.getCmp(this.mainFormPanelId).getCmpByName('main.attachments').subGridPanel.getStore().removeAll();
                Ext.getCmp(this.mainFormPanelId).form.reset();
            }
        });
    },
	copyFun: function () {
		this.saveFun('copy');
    }
});