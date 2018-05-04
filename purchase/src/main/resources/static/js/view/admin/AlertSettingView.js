AlertSettingView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.AlertSetting.mTitle,
			moduleName: 'AlertSetting',
			frameId : 'AlertSettingView',
			mainGridPanelId : 'AlertSettingGridPanelID',
			mainFormPanelId : 'AlertSettingFormPanelID',
			searchFormPanelId: 'AlertSettingSearchPanelID',
			mainTabPanelId: 'AlertSettingTbsPanelId',
			subGridPanelId : 'AlertSettingSubGridPanelID',
			urlList: __ctxPath + 'cronjob/list',
			urlSave: __ctxPath + 'cronjob/save',
			urlDelete: __ctxPath + 'cronjob/delete',
			urlGet: __ctxPath + 'cronjob/get',
			refresh: true,
			save: true,
		
		};
		this.initUIComponents(conf);
		AlertSettingView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [this.searchPanel, this.centerPanel ]
		});
	},
    
	initUIComponents: function(conf) {		
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
				{field:'Q-code-S-LK', xtype:'textfield', title:_lang.AlertSetting.fCode},
				{field:'Q-name-S-LK', xtype:'textfield', title:_lang.AlertSetting.fName},
				{field:'Q-type-S-LK', xtype:'textfield', title:_lang.AlertSetting.fType},
				{field:'Q-remindType-S-LK', xtype:'combo', title:_lang.AlertSetting.fRemindType, value:'',
                    store: [['', _lang.TText.vAll],['1', _lang.AlertSetting.vAduance], ['2', _lang.AlertSetting.vLag]]
                },
				{field:'Q-isSystem-S-LK', xtype:'combo', title:_lang.AlertSetting.fIsSystem, value:'',
			    	store: [['', _lang.TText.vAll],['1', _lang.AlertSetting.vSystem], ['2', _lang.AlertSetting.vUser]]
                },
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
                }
			]
		});// end of searchPanel
		
		// grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'west',
			id: conf.mainGridPanelId,
			title: _lang.AlertSetting.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			//'dayOfWeek','dayOfWeekUnit','month','monthUnit','minute','minuteUnit',
			fields: [ 'id','code','name','isStarted','type','dayOfMonth', 'dayOfMonthUnit','hour','hourUnit','remindType', 'status','isSystem','updatedAt'],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.AlertSetting.fCode, dataIndex: 'code', width: 200},
				{ header: _lang.AlertSetting.fName, dataIndex: 'name', width: 200},
				{ header: _lang.AlertSetting.fIsStarted, dataIndex: 'isStarted', width: 65,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2' || value == null) return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.AlertSetting.fType, dataIndex: 'type', width: 90,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.AlertSetting.vNoPeriodic);
                        if(value == '2') return $renderOutputColor('gray', _lang.AlertSetting.vPeriodic);
                    }
                },
				{ header: _lang.AlertSetting.fDayOfMonth, dataIndex: 'dayOfMonth', width: 40 },
			    { header: _lang.AlertSetting.fHour, dataIndex: 'hour', width: 40},
			    { header: _lang.AlertSetting.fRemindType, dataIndex: 'remindType', width: 60,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.AlertSetting.vAduance);
						if(value == '2') return $renderOutputColor('gray', _lang.AlertSetting.vLag);
					}
			    },
			    { header: _lang.AlertSetting.fIsSystem, dataIndex: 'isSystem', width:60,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.AlertSetting.vSystem);
						if(value == '2') return $renderOutputColor('gray', _lang.AlertSetting.vUser);
					}
			    },
				{ header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
					}
			    },
			    { header: _lang.AlertSetting.fUpdatedAt, dataIndex: 'updatedAt', width: 140 },

			],// end of columns
			// appendColumns:
			 //$groupGridCreatedColumns({sort:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.AlertSetting.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
                { field: 'main.code', xtype: 'displayfield', fieldLabel: _lang.AlertSetting.fCode},
			    { field: 'main.name', xtype: 'displayfield', fieldLabel: _lang.AlertSetting.fName},
			    { xtype: 'container', cls:'row', items:  [
			    	{ field: 'main.dayOfWeek', xtype: 'textfield', fieldLabel: _lang.AlertSetting.fDayOfWeek,allowDecimals:false,minValue:'0', maxValue:'6',cls:'col-2'},
                    { field: 'main.dayOfWeekUnit', xtype: 'dictcombo', fieldLabel: _lang.AlertSetting.fDayOfWeekUnit, code:'cronjob', codeSub:'unit',hidden:true,cls:'col-2'},
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.month', xtype: 'textfield', fieldLabel: _lang.AlertSetting.fMonth,allowDecimals:false,minValue:'0',maxValue:'11',cls:'col-2'},
                    { field: 'main.monthUnit', xtype: 'dictcombo', fieldLabel: _lang.AlertSetting.fMonthUnit, code:'cronjob', codeSub:'unit',hidden:true,cls:'col-2'},
			    ] },
			    { xtype: 'container', cls:'row', items:  [
                    { field: 'main.dayOfMonth', xtype: 'textfield', fieldLabel: _lang.AlertSetting.fDayOfMonth,allowDecimals:false,minValue:'0', maxValue:'31',cls:'col-2'},
                    { field: 'main.dayOfMonthUnit', xtype: 'dictcombo', fieldLabel: _lang.AlertSetting.fDayOfMonthUnit, code:'cronjob', codeSub:'unit',hidden:true,cls:'col-2'},
			    ] },
			    { xtype: 'container', cls:'row', items:  [
                    { field: 'main.hour', xtype: 'textfield', fieldLabel: _lang.AlertSetting.fHour,allowDecimals:false,minValue:'0',maxValue:'23',cls:'col-2'},
                    { field: 'main.hourUnit', xtype: 'dictcombo', fieldLabel: _lang.AlertSetting.fHourUnit, code:'cronjob', codeSub:'unit',hidden:true,cls:'col-2'},
			    ] },
			    { xtype: 'container', cls:'row', items:  [
                    { field: 'main.minute', xtype: 'textfield', fieldLabel: _lang.AlertSetting.fMinute,allowDecimals:false,minValue:'0', maxValue:'59',cls:'col-2'},
                    { field: 'main.minuteUnit', xtype: 'dictcombo', fieldLabel: _lang.AlertSetting.fMinuteUnit, code:'cronjob', codeSub:'unit',hidden:true,cls:'col-2'},
			    ] },
			    { field: 'main.remindType', xtype: 'combo', fieldLabel: _lang.AlertSetting.fRemindType,value:'1',hidden:true,
                    store:[['1', _lang.AlertSetting.vAduance], ['2', _lang.AlertSetting.vLag]]
                },
                { field: 'main.type', xtype: 'combo', fieldLabel: _lang.AlertSetting.fType, value: '1', store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]],allowBlank:false,hidden:true,
                    scope:this, selectFun: function(records, eOpts){
						this.scope.changeType.call(this, records[0].data.id);
					}
				},
			    { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
                    store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
			    }, 
			    { field: 'main.isSystem', xtype: 'displayfield', fieldLabel: _lang.AlertSetting.fIsSystem,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.AlertSetting.vSystem);
                        if(value == '2') return $renderOutputColor('gray', _lang.AlertSetting.vUser);
                        if(value == '') return '';
                    }
                },
                { field: 'main.isStarted', xtype: 'displayfield', fieldLabel: _lang.AlertSetting.fIsStarted,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == null || value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                        if(value == '') return '';
                    }
                },
                { field: 'runLink', id:'runLink', xtype:'button', text: _lang.AlertSetting.fRun, cls: 'col-1',
                    handler: function() {
                        var id = this.up().getCmpByName('id').getValue();
                        var code = this.up().getCmpByName('main.code').getValue();
                        if(id) {
                            var params = {id: id,code:code};
                            $postUrl({
                                scope: this,
                                url: __ctxPath + 'cronjob/runJob',
                                params: params,
                                callback: function (response, options) {
                                    console.log(response);
                                }
                            });
                        }
                    }
                },
			]
		});
		
		// 布局
		this.centerPanel = new Ext.Panel({
			id: conf.mainGridPanelId + '-center',
			region: 'center',
			layout: 'border',
			width: 'auto',
			border: false,
			items: [ this.gridPanel, this.formPanel]
		});
				
	},// end of the init

	rowClick: function(record, item, index, e, conf) {
		this.formPanel.loadData({
			url : conf.urlGet + '?id=' + record.data.id,
			root : 'data', preName : 'main', loadMask:true, maskTo:conf.mainFormPanelId,
			success:function (response) {
                var json = Ext.JSON.decode(response.responseText);
                this.scope.changeType(json.data.type, json.data,this);
                if(json.data.status == 2) Ext.getCmp('runLink').hide();
                else Ext.getCmp('runLink').show();
                if(json.data.type ==1){
                    this.getCmpByName('main.remindType').setValue(json.data.remindType);
				}else if(json.data.type == 2){
                    this.getCmpByName('main.month').setValue(json.data.month);
                    this.getCmpByName('main.monthUnit').setValue(json.data.monthUnit);
                    this.getCmpByName('main.dayOfWeek').setValue(json.data.dayOfWeek);
                    this.getCmpByName('main.dayOfWeekUnit').setValue(json.data.dayOfWeekUnit);
                    this.getCmpByName('main.dayOfMonthUnit').setValue(json.data.dayOfMonthUnit);
                    this.getCmpByName('main.hourUnit').setValue(json.data.hourUnit);
                    this.getCmpByName('main.minute').setValue(json.data.minute);
                    this.getCmpByName('main.minuteUnit').setValue(json.data.minuteUnit);
				}
            }
		});
	},

	changeType: function(type, data,eOpts){
		var obj = eOpts || this.ownerCt;
	    if (type == 1) {
			obj.getCmpByName('main.remindType').show();
			
			obj.getCmpByName('main.dayOfMonthUnit').hide();
			obj.getCmpByName('main.dayOfMonthUnit').setValue('1');
			
			obj.getCmpByName('main.hourUnit').hide();
			obj.getCmpByName('main.hourUnit').setValue('1');
			
			obj.getCmpByName('main.month').hide();
			
			obj.getCmpByName('main.monthUnit').hide();
			obj.getCmpByName('main.monthUnit').setValue('1');
			
		    obj.getCmpByName('main.dayOfWeek').hide();
		    obj.getCmpByName('main.dayOfWeek').setValue('1');
		    
			obj.getCmpByName('main.dayOfWeekUnit').hide();
			obj.getCmpByName('main.dayOfWeekUnit').setValue('1');
			
		    obj.getCmpByName('main.minute').hide();
		   
			obj.getCmpByName('main.minuteUnit').hide();
			 obj.getCmpByName('main.minuteUnit').setValue('1');

		} else if (type == 2) {
			obj.getCmpByName('main.remindType').hide();
			obj.getCmpByName('main.dayOfMonthUnit').show();
			obj.getCmpByName('main.hourUnit').show();
			obj.getCmpByName('main.month').show();
	        obj.getCmpByName('main.monthUnit').show();
			obj.getCmpByName('main.dayOfWeek').show();
			obj.getCmpByName('main.dayOfWeekUnit').show();
			 obj.getCmpByName('main.minute').show();
			obj.getCmpByName('main.minuteUnit').show();

		}
	}
});