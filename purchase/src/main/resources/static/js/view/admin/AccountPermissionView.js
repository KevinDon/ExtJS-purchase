AccountPermissionView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AccountPermission.mTitle,
			moduleName: 'RolePermission',
			frameId : 'AccountPermissionView',
			mainGridPanelId : 'AccountPermissionGridPanelID',
			mainFormPanelId : 'AccountPermissionFormPanelID',
			searchFormPanelId: 'AccountPermissionSearchPanelID',
			mainTabPanelId: 'AccountPermissionTbsPanelId',
			subGridPanelId : 'AccountPermissionSubGridPanelID',
			urlList: __ctxPath + 'admin/role/list',
			urlSave: __ctxPath + 'admin/rolepermission/save',
			urlGet: __ctxPath + 'admin/rolepermission/list',
			urlSubList: __ctxPath + 'modules/list',
			urlSubGet: __ctxPath + 'dict/getkey?code=module_action',
			refresh: true,
			save: true,
			treeExpand: true,
			saveFun:this.saveFun,
			refreshFun:this.refreshFun,
			expandAllFun:function(){Ext.getCmp(this.mainFormPanelId).expandAll();},
			collapseAllFun:function(){Ext.getCmp(this.mainFormPanelId).collapseAll();}
		};
		this.initUIComponents(conf);
		
		this.initModules(conf);
		AccountPermissionView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel, this.centerPanel ]
		});
	},
    
	initUIComponents: function(conf) {
		//@TODO 加载模块功能项  
		Ext.Ajax.request({
			url : conf.urlSubGet,
			params : { ids : conf.ids},
			method : 'POST',
			scope : conf.scope,
			success : function(fp, options) {
				var result = Ext.JSON.decode(fp.responseText);
				if (result.success) {
					var frs = result.data;
					var functionsOrg = [];
					for(var i in frs){
						for(var j in frs[i].options){
							functionsOrg.push({key:frs[i].codeSub, value:frs[i].options[j].value, title:frs[i].options[j].title, group:frs[i].title});
						}
					}
					Ext.getCmp(conf.frameId).functionRows = functionsOrg;
				} else {
					Ext.ux.Toast.msg(_lang.TText.titleOperation, result.message);
				}
			},
			failure : function(fp, options) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsErrorSystem);
			}
		});
		
		//
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.AccountResource.fCnName},
			    {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.AccountResource.fEnName},
			    {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    }
			]
		});// end of searchPanel
		
		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.AccountPermission.tabRoleTitle,
			collapsible: true,
			split: true,
			scope: this,
			url: conf.urlList,
			fields: [ 'id','cnName','enName', 'createdAt', 'status', 'sort'],
			width: '20%',
			minWidth: 200,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
			    { header: _lang.AccountResource.fCnName, dataIndex: 'cnName', hidden:curUserInfo.lang =='zh_CN'? false: true},
			    { header: _lang.AccountResource.fEnName, dataIndex: 'enName', hidden:curUserInfo.lang !='zh_CN'? false: true},
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.TText.fSort, dataIndex: 'sort', width: 40, hidden:true }
				
			],// end of columns
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			},
			itemcontextmenu: function(view, record, node, index, e){
				
			}
		});
		
		this.subGridPanel = new Ext.tree.Panel({
			id: conf.mainFormPanelId,
			title: _lang.AccountPermission.tabFormTitle,
			region: 'center',
			scope: this,
			url: conf.urlSubList,
			forceFit: false,
			rootVisible:false,
			remoteFilter:false,
			sorters: [{property: 'sort', direction: 'ASC'}],
			fields: [ 'id','parentId','cnName','enName','code','functions'],
			columns: [
				{ header: _lang.AccountResource.fCnName, dataIndex: 'cnName', width:'18%', xtype:'treecolumn', hidden:curUserInfo.lang!='zh_CN' ? true: false, sortable: false},
				{ header: _lang.AccountResource.fEnName, dataIndex: 'enName', width:'18%', xtype:'treecolumn', hidden:curUserInfo.lang!='en_AU' ? true: false, sortable: false},
				{ header: 'ID', dataIndex: 'id', width:'5%', hidden : true, sortable: false },
				{ header: _lang.AccountResource.fCode, dataIndex: 'code', width:150, hidden : true, sortable: false},
                { header: _lang.AccountPermission.fFunctionsData, dataIndex: 'functions', width:280, scope:this, sortable: false,
                    renderer : function(value, eOpts, record){
                        if(record.childNodes.length<1){
                            var functionRows = Ext.getCmp(conf.frameId).functionRows;
                            var resStr = '';
                            var x = 1;
                            var curCode = "";
                            if(value){
                                for(var i in value){
                                    for(var j in value[i]){
                                        var strs = value[i][j].split('|');
                                        if(strs[0] != strs[1] && strs[0] == 'data'){
                                            var item = this.getMyItem.call(this, strs, functionRows);
                                            resStr += ' | ';
                                            if(curCode != strs[0]){
                                                resStr += ''
                                                    +'<input id="'+record.data.id+'-function-'+item.key+'-'+item.key+'" name="main.functions['+record.data.id+'][0]" type="checkbox" '
                                                    +' value="'+item.key+'" data_key="'+item.key+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)"/>'
                                                    +'<label for="'+record.data.id+'-function-'+item.key+'-'+item.key+'">'+_lang.TText.vAll+'</label></span>';
                                                curCode = strs[0];
                                            }

                                            resStr += '<span class="item">'
                                                +'<input id="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'" name="main.functions['+record.data.id+']['+x+']" type="checkbox" '
                                                //												+ this.isChecked.call(this,functionRows[i].options[j], value)
                                                +' value="'+strs[1]+'" data_key="'+strs[0]+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)" />'
                                                +'<label for="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'">'+item.title+'</label></span>';

                                            x++;
                                        }
                                    }
                                }
                                resStr = resStr.substring(3);
                                resStr = '<span class="checkbox-options">' + resStr +'</span>';
                                var tpl = new Ext.XTemplate(resStr,{disableFormats: true,compile:true});
                                tpl.overwrite(this, {});
                                return this.innerHTML;
                            }
                        }
                        else return $renderOutputColor('gray', '-');
                    }
                },
				{ header: _lang.AccountPermission.fFunctionsNormal, dataIndex: 'functions', width:350, scope:this, sortable: false,
					renderer : function(value, eOpts, record){
						if(record.childNodes.length<1){
							var functionRows = Ext.getCmp(conf.frameId).functionRows;
							var resStr = ''; 
							var x = 1;
							var curCode = "";
							if(value){
								for(var i in value){
									for(var j in value[i]){
										var strs = value[i][j].split('|');
										if(strs[0] != strs[1] && strs[0] == 'normal'){
											var item = this.getMyItem.call(this, strs, functionRows);
											resStr += ' | '; 
											if(curCode != strs[0]){
												// resStr += '<span class="label"><label>'+item.group+'</label>'
                                                resStr += ''
													+'<input id="'+record.data.id+'-function-'+item.key+'-'+item.key+'" name="main.functions['+record.data.id+'][0]" type="checkbox" '
													+' value="'+item.key+'" data_key="'+item.key+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)"/>'
													+'<label for="'+record.data.id+'-function-'+item.key+'-'+item.key+'">'+_lang.TText.vAll+'</label></span>';
												curCode = strs[0];
											}
											
											resStr += '<span class="item">'
												+'<input id="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'" name="main.functions['+record.data.id+']['+x+']" type="checkbox" '
//												+ this.isChecked.call(this,functionRows[i].options[j], value)
												+' value="'+strs[1]+'" data_key="'+strs[0]+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)" />'
												+'<label for="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'">'+item.title+'</label></span>';
											
											x++;
										}
									}
								}
								resStr = resStr.substring(3);
								resStr = '<span class="checkbox-options">' + resStr +'</span>'; 
								var tpl = new Ext.XTemplate(resStr,{disableFormats: true,compile:true});
						    	tpl.overwrite(this, {});
						    	return this.innerHTML;
							}
						}
						else return $renderOutputColor('gray', '-');
					}
				},
                { header: _lang.AccountPermission.fFunctionsFlow, dataIndex: 'functions', width:460, scope:this, sortable: false,
                    renderer : function(value, eOpts, record){
                        if(record.childNodes.length<1){
                            var functionRows = Ext.getCmp(conf.frameId).functionRows;
                            var resStr = '';
                            var x = 1;
                            var curCode = "";
                            if(value){
                                for(var i in value){
                                    for(var j in value[i]){
                                        var strs = value[i][j].split('|');
                                        if(strs[0] != strs[1] && strs[0] == 'flow'){
                                            var item = this.getMyItem.call(this, strs, functionRows);
                                            resStr += ' | ';
                                            if(curCode != strs[0]){
                                                // resStr += '<span class="label"><label>'+item.group+'</label>'
                                                resStr += ''
                                                    +'<input id="'+record.data.id+'-function-'+item.key+'-'+item.key+'" name="main.functions['+record.data.id+'][0]" type="checkbox" '
                                                    +' value="'+item.key+'" data_key="'+item.key+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)"/>'
                                                    +'<label for="'+record.data.id+'-function-'+item.key+'-'+item.key+'">'+_lang.TText.vAll+'</label></span>';
                                                curCode = strs[0];
                                            }

                                            resStr += '<span class="item">'
                                                +'<input id="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'" name="main.functions['+record.data.id+']['+x+']" type="checkbox" '
                                                //												+ this.isChecked.call(this,functionRows[i].options[j], value)
                                                +' value="'+strs[1]+'" data_key="'+strs[0]+'" data_id="'+record.data.id+'" data_code="'+record.data.code+'" onchange="javascript:AccountPermissionViewCheckBoxClick(this)" />'
                                                +'<label for="'+record.data.id+'-function-'+strs[0]+'-'+strs[1]+'">'+item.title+'</label></span>';

                                            x++;
                                        }
                                    }
                                }
                                resStr = resStr.substring(3);
                                resStr = '<span class="checkbox-options">' + resStr +'</span>';
                                var tpl = new Ext.XTemplate(resStr,{disableFormats: true,compile:true});
                                tpl.overwrite(this, {});
                                return this.innerHTML;
                            }
                        }
                        else return $renderOutputColor('gray', '-');
                    }
				}
				
			],// end of columns
			afterrender: function(eOpts){
				Ext.getCmp(conf.mainFormPanelId).expandAll();
			},
			beforerender: function(eOpts, obj){
				
			}
		});
		
		
		//布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.subGridPanel]
		});
		
		this.getMyItem = function(curValue, selectedValues){
			for(var i in selectedValues){
				if(selectedValues[i]['key'] == curValue[0] && selectedValues[i]['value']== curValue[1]){
					return selectedValues[i];
				}
			}
		};
		
		this.isChecked = function(curValue, selectedValues){
			var result = '';
			for(var i in selectedValues){
				for(var j in selectedValues[i]){
					var strs = selectedValues[i][j].split('|');
					if(strs.length>0 && curValue.value == strs[1]){
						result = ' checked="checked"';
						break;
					}
				}
			}
			return result;
		};
	},// end of the init
	
	initModules: function(conf, callback){
		var cmpPanel = Ext.getCmp(conf.mainFormPanelId);
		Ext.Ajax.request({
			url : conf.urlSubList, scope: this, method: 'post',
            loadMask:true, maskTo: this.mainFormPanelId,
			success : function(response, options) {
    			var obj = Ext.JSON.decode(response.responseText);

    			if(obj.success == true){
    				var arr = obj.data;
    				cmpPanel.setRootNode({ text: 'Root', expanded: true, children: arr });
    				cmpPanel.expandAll();
    				if(callback) callback.call(this, conf);
    			}else{
    				Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg);
    			}    			
    		}
		});
	},

	rowClick: function(record, item, index, e, conf) {
		this.initModules.call(this, conf, function(conf){
			$HpStore({
				url : conf.urlGet,
				fields: ['id', 'roleId', 'resourceId', 'action', 'data','model'],
				baseParams : { roleId : record.data.id, limit:0},
				callback: function(pt, records, eOpts){
					for(var i=0; i< records.length; i++){
						
						if(records[i].data.action){
							strs = records[i].data.action.split(':');
							jQuery('#'+records[i].data.resourceId+'-function-'+strs[0]+'-'+strs[1]).attr('checked', true);
						}else if(records[i].data.data){
							jQuery('#'+records[i].data.resourceId+'-function-data-'+records[i].data.data).attr('checked', true);
						}
					}
					
				}
			});
		});
		
	},
	refreshFun:function(){
		Ext.getCmp(this.mainGridPanelId).getStore().reload();
		var selrow = Ext.getCmp(this.mainGridPanelId).getSelectionModel().getLastSelected();
		if(selrow){
			var cmpPanel = Ext.getCmp(this.mainFormPanelId);
			Ext.Ajax.request({
				url : this.urlSubList + '?id=' + selrow.data.id, scope: this, method: 'post',
                loadMask:true, maskTo: this.mainFormPanelId,
				success : function(response, options) {
	    			var obj = Ext.JSON.decode(response.responseText);

	    			if(obj.success == true){
	    				var arr = obj.data;
	    				cmpPanel.setRootNode({ text: 'Root', expanded: true, children: arr });
	    				cmpPanel.expandAll();
	    			}else{
	    				Ext.ux.Toast.msg(_lang.TText.titleOperation, obj.msg);
	    			}    			
	    		}
			});
		}
	},
	saveFun:function(){
		Ext.getCmp(this.mainGridPanelId);
		var v = [];
		jQuery('#AccountPermissionFormPanelID-body input[type="checkbox"]:checked').each(function(e){
			v.push(jQuery(this).attr('data_id') + '|' + jQuery(this).attr('data_key') + '|' + this.value + '|' + jQuery(this).attr('data_code'));
		});
		var ids = $getGdSelectedIds({grid: Ext.getCmp(this.mainGridPanelId)});
		
		if(ids.length<1) {
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
			return;
		};
		
		$postUrl({
			url : this.urlSave,
			params: {'roleId': ids[0], 'functions':v},
			scope: this, grid: Ext.getCmp(this.mainFormPanelId),
			maskTo: this.frameId,
			callback: function(){ 
//				this.reloadCenterTopicGridPanel();
//				this.reloadCenterQuestionGridPanel();
			}
		});
	}
});

var AccountPermissionViewCheckBoxClick = function(e){
	var $curId = jQuery(e).attr('data_id');
	var $curKey = jQuery(e).attr('data_key');
	var $curValue = jQuery(e).val();
	var $curChecked = e.checked;
	var needAll = true;
	jQuery('#AccountPermissionFormPanelID-body input[data_id="'+$curId+'"][data_key="'+$curKey+'"]').each(function(e1){
		if($curKey == $curValue && e1>0){
			if($curChecked){
				this.checked = true;
			}else{
				this.checked = false;
				needAll = false;
			}
		}else if(e1 > 0 && !this.checked){
			needAll = false;
		}
	});
	jQuery('#AccountPermissionFormPanelID-body input[data_id="'+$curId+'"][value="'+$curKey+'"][data_key="'+$curKey+'"]').get(0).checked= needAll;
	
};
