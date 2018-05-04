ContactsView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.Contacts.mTitle,
			moduleName: 'Contacts',
			frameId : 'ContactsView',
			mainGridPanelId : 'ContactsGridPanelID',
			mainFormPanelId : 'ContactsFormPanelID',
			searchFormPanelId: 'ContactsSearchPanelID',
			mainTabPanelId: 'ContactsTbsPanelId',
			subGridPanelId : 'ContactsSubGridPanelID',
			urlList: __ctxPath + 'contacts/list',
			urlSave: __ctxPath + 'contacts/save',
			urlDelete: __ctxPath + 'contacts/delete',
			urlGet: __ctxPath + 'contacts/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true
		};
		this.initUIComponents(conf);
		ContactsView.superclass.constructor.call(this, {
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
				{field:'Q-email-S-LK', xtype:'textfield', title:_lang.TText.fEmail},
				{field:'Q-mobile-S-LK', xtype:'textfield', title:_lang.Contacts.fMobile},
				{field:'Q-qq-S-LK', xtype:'textfield', title:_lang.Contacts.fQq},
				{field:'Q-wechat-S-LK', xtype:'textfield', title:_lang.Contacts.fWechat},
				{field:'Q-skype-S-LK', xtype:'textfield', title:_lang.Contacts.fSkype},
				{field:'Q-phone-S-LK', xtype:'textfield', title:_lang.Contacts.fPhone},
				{field:'Q-shared-N-EQ', xtype:'dictcombo', value:'', title:_lang.Contacts.fShared, code:'document', codeSub:'shared', addAll:true},
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'',
                    store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled],  ['3', _lang.TText.vDeleted]]
                },
			    {field:'Q-cnName-S-LK', xtype:'textfield', title:_lang.Contacts.fCnName},
			    {field:'Q-enName-S-LK', xtype:'textfield', title:_lang.Contacts.fEnName},
			    {field:'Q-gender-N-EQ', xtype:'combo', title:_lang.Contacts.fGender, value:'',
			    	store: [['', _lang.TText.vAll],['2', _lang.TText.vFemale], ['1', _lang.TText.vMale]]
			    },
				{field:'Q-title-S-EQ', xtype:'dictcombo', value:'', title:_lang.Contacts.fTitle, code:'contacts', codeSub:'position', addAll:true},
				{field:'Q-departmentId-S-EQ', xtype:'dictcombo', value:'', title:_lang.Contacts.fDepartment, code:'contacts', codeSub:'department', addAll:true},
				{field:'Q-vendorCnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorCnName},
			    {field:'Q-vendorEnName-S-LK', xtype:'textfield', title:_lang.TText.fVendorEnName},
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
			title: _lang.Contacts.tabListTitle,
			collapsible: true,
			split: true,
			scope: this,
			forceFit: false,
			url: conf.urlList,
			fields: [ 'id','vendorCnName','vendorEnName','vendorId','cnName','enName', 'gender','title', 'email','department', 'qq','skype','extension','phone','wechat','mobile','shared','remark','status',
	           'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName',
				'port', 'agentCompany'
			],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 120, hidden: true },

			    { header: _lang.Contacts.fCnName, dataIndex: 'cnName', width: 100},
			    { header: _lang.Contacts.fEnName, dataIndex: 'enName', width: 120},
			    { header: _lang.Contacts.fGender, dataIndex: 'gender', width: 40,
					renderer: function(value){
						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
					}
			    },
                { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
                { header:_lang.VendorDocument.fVendorId, dataIndex: 'vendorId', width: 80, hidden: true },
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'vendorCnName', width: 260, hidden: curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.VendorDocument.fVendorAndService, dataIndex: 'vendorEnName', width: 260, hidden: curUserInfo.lang !='zh_CN'? false: true },
			    { header: _lang.Contacts.fTitle, dataIndex: 'title', width: 60,
			    	renderer: function(value){
                    	var $position = _dict.position;
                        if($position.length>0 && $position[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $position[0].data.options);
                        }
                    }
			    },
                { header: _lang.Contacts.fPort, dataIndex: 'port', width: 40, value: 0,
                    renderer: function(value){
                        var $ports = _dict.origin;
                        if($ports.length>0 && $ports[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $ports[0].data.options);
                        }
                    }
                },
                { header: _lang.Contacts.fAgentCompany, dataIndex: 'agentCompany', width: 100 },
			    { header: _lang.Contacts.fDepartment, dataIndex: 'department', width: 100,
			    	renderer: function(value){
                    	var $department = _dict.department;
                        if($department.length>0 && $department[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $department[0].data.options);
                        }
                    }
			    },
			    { header: _lang.Contacts.fQq, dataIndex: 'qq', width: 140 },
			    { header: _lang.Contacts.fSkype, dataIndex: 'skype', width: 140 },
			    { header: _lang.Contacts.fPhone, dataIndex: 'phone', width: 100 },
			    { header: _lang.Contacts.fExtension, dataIndex: 'extension', width:100 },
			    { header: _lang.Contacts.fWechat, dataIndex: 'wechat', width: 140 },
			    { header: _lang.Contacts.fMobile, dataIndex: 'mobile', width: 100 },
			    { header: _lang.Contacts.fShared, dataIndex: 'shared', width: 80 ,
			    	renderer: function(value){
                    	var $shared = _dict.shared;
                        if($shared.length>0 && $shared[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $shared[0].data.options);
                        }
                    }
			    },
			
			    { header: _lang.Contacts.fRemark, dataIndex: 'remark', width: 200 },
				
			],// end of columns
			appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
			itemclick: function(obj, record, item, index, e, eOpts){
				this.scope.rowClick.call(this.scope, record, item, index, e, conf);
			}
		});
		
		//
		this.formPanel = new HP.FormPanel({
			id: conf.mainFormPanelId,
			title: _lang.Contacts.tabFormTitle,
			region: 'center',
			scope: this,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
                { xtype: 'section', title:_lang.Contacts.tabSectionBase},
			    { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.Contacts.fCnName, allowBlank: false},
			    { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.Contacts.fEnName, allowBlank: false},
                { field: 'main.gender', xtype: 'combo', fieldLabel: _lang.Contacts.fGender, allowBlank: false, value:'1',
                    store: [['2', _lang.TText.vFemale], ['1', _lang.TText.vMale]]
                },
                { field: 'main.title', xtype: 'dictcombo', fieldLabel: _lang.Contacts.fTitle, code:'contacts', codeSub:'position'},

                { xtype: 'section', title:_lang.Contacts.tabSectionType},
                { field: 'main.type', xtype: 'dictcombo', fieldLabel: _lang.Contacts.fType, value: '0', code:'contacts', codeSub:'type', allowBlank:false,
                    scope:this, selectFun: function(records, eOpts){
						this.scope.changeType.call(this, records[0].data.id);
					}
				},
			    //供应商基础信息
			    { field:'main.vendorId', xtype:'hidden'},
			    { field: 'main_vendorName', xtype: 'VendorDialog', fieldLabel: _lang.VendorDocument.fVendor, allowBlank:true, hidden:true,
			        formId: conf.mainFormPanelId, hiddenName:'main.vendorId', single: true, readOnly: this.isApprove,
			    },
                { field: 'main_serviceName', xtype: 'ServiceProviderDialog', fieldLabel: _lang.ServiceProviderDocument.fService, allowBlank:true,hidden:true,
                	formId: conf.mainFormPanelId, hiddenName:'main.vendorId', single: true, readOnly: this.isApprove
                },
                { field: 'main.port', xtype: 'dictcombo', fieldLabel: _lang.Contacts.fPort,code:'service_provider', allowBlank:true, codeSub:'origin_port',hidden:true },
                { field: 'main.agentCompany', xtype: 'textfield', fieldLabel: _lang.Contacts.fAgentCompany,hidden:true },

				{ xtype: 'section', title:_lang.Contacts.tabSectionContact},
			    { field: 'main.qq', xtype: 'textfield', fieldLabel: _lang.Contacts.fQq},
			    { field: 'main.department', xtype: 'dictcombo', fieldLabel: _lang.Contacts.fDepartment,  allowBlank:false, code:'contacts', codeSub:'department'},
			    { field: 'main.wechat', xtype: 'textfield', fieldLabel: _lang.Contacts.fWechat},
			    { field: 'main.skype', xtype: 'textfield', fieldLabel: _lang.Contacts.fSkype},
			    { field: 'main.email', xtype: 'textfield', fieldLabel: _lang.TText.fEmail, allowBlank: false},
			    { field: 'main.phone', xtype: 'textfield', fieldLabel: _lang.Contacts.fPhone, allowBlank: false},
			    { field: 'main.extension', xtype: 'textfield', fieldLabel: _lang.Contacts.fExtension},
			    { field: 'main.mobile', xtype: 'textfield', fieldLabel: _lang.Contacts.fMobile},
			    { field: 'main.fax', xtype: 'textfield', fieldLabel: _lang.Contacts.fFax},
			    { field: 'main.shared', xtype: 'dictcombo', fieldLabel: _lang.Contacts.fShared, value: '1', allowBlank: false, code:'document', codeSub:'shared'},
                { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1',
                    store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
                },
			    { field: 'main.remark', xtype: 'textarea', fieldLabel: _lang.Contacts.fRemark},
			    { field: 'main.creatorId', xtype:'hidden', value: curUserInfo.id}
			]
		});
		
		//布局
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
                this.scope.changeType(json.data.type, this);

                if(json.data.type ==1){
                    this.getCmpByName('main_vendorName').setValue(curUserInfo.lang == 'zh_CN' ? json.data.vendorCnName: json.data.vendorEnName);
				}else if(json.data.type == 2){
                    this.getCmpByName('main_serviceName').setValue(curUserInfo.lang == 'zh_CN' ? json.data.vendorCnName: json.data.vendorEnName);
                    this.getCmpByName('main.port').setValue(json.data.port.toString());
                    this.getCmpByName('main.agentCompany').setValue(json.data.agentCompany);
				}else{
					 this.getCmpByName('main_vendorName').setValue('');
					 this.getCmpByName('main_serviceName').setValue('');
	                    this.getCmpByName('main.port').setValue('');
	                    this.getCmpByName('main.agentCompany').setValue('');
		        }
            }
		});
	},

	changeType: function(type, eOpts){
		var obj = eOpts || this.ownerCt;
        obj.getCmpByName('main_vendorName').setValue('');
        obj.getCmpByName('main_serviceName').setValue('');
        obj.getCmpByName('main.port').setValue('');
        obj.getCmpByName('main.agentCompany').setValue('');

        if(type == 1){
            obj.getCmpByName('main_vendorName').show();
            obj.getCmpByName('main_serviceName').hide();
            obj.getCmpByName('main.port').setValue(0);
            obj.getCmpByName('main.port').hide();
            obj.getCmpByName('main.agentCompany').hide();
        }else if(type == 2){
            obj.getCmpByName('main_vendorName').hide();
            obj.getCmpByName('main_serviceName').show();
            obj.getCmpByName('main.port').show();
            obj.getCmpByName('main.agentCompany').show();
        }else{
            obj.getCmpByName('main_vendorName').hide();
            obj.getCmpByName('main_serviceName').hide();
            obj.getCmpByName('main.port').setValue(0);
            obj.getCmpByName('main.port').hide();
            obj.getCmpByName('main.agentCompany').hide();
        }
    }
});