CommonConfigurationItemsView = Ext.extend(Ext.Panel, {
	constructor : function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			winId : 'CommonConfigurationItemsView',
			moduleName: 'CommonConfiguration',
			title : _lang.CommonConfiguration.mTitle,
			mainGridPanelId : 'CommonConfigurationItemsViewGridPanelID',
			searchFormPanelId : 'CommonConfigurationItemsViewSearchFormPanelId',
			urlList: __ctxPath + 'common/list',
			urlSave: __ctxPath + 'common/save',
			refresh: true,
			save: true,
			close: true,
			saveFun: this.saveFun
		};
	
		this.initUIComponents(conf);
		CommonConfigurationItemsView.superclass.constructor.call(this, {
			id: conf.winId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.gridPanel]
		});
	},// end of the constructor

	initUIComponents : function(conf) {
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			    {title:_lang.Dictionary.fCodeMain, field:'Q-codeMain-S-LK', xtype:'textfield'},
			    {title:_lang.Dictionary.fCodeSub, field:'Q-codeSub-S-LK', xtype:'textfield'},
			    {title:_lang.TText.fType, field:'Q-type-N-EQ', xtype:'combo', value:'',
			    	store: [['', _lang.TText.vAll], ['1', _lang.Dictionary.vSingleOption], ['2', _lang.Dictionary.vMultipleOption]]
			    },
			    {title:_lang.Dictionary.fCustom,field:'Q-custom-N-EQ', xtype:'combo', value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vNo], ['1', _lang.TText.vYes]]
			    }
			]
		});// end of searchPanel
		
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.CommonConfiguration.tabListTitle,
			// scope: this,
			forceFit: true,
			url: conf.urlList,
			fields: [ 'id','categoryId','codeMain','codeSub','title','type', 'options', 'desc'],
			groupField: 'codeMain',
			features: [{ftype:'grouping', }],
			edit:true,
			selType: 'cellmodel',
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
				{ header: _lang.Dictionary.fCateId, dataIndex: 'categoryId', width: 100, hidden: true},
				{ header: _lang.Dictionary.fCodeMain, dataIndex: 'codeMain', width: 90, hidden: true },
				{ header: _lang.Dictionary.fCodeSub, dataIndex: 'codeSub', width: 90, hidden: true },
				{ header: _lang.Dictionary.fTitle, dataIndex: 'title', width: 100},
				{ header: _lang.Dictionary.fValue, dataIndex: 'value', width: 200, cls:'input-edit-cell', 
		            editor: {xtype: 'textfield', allowBlank: true },
		            renderer: function(value, data, record, index, total,opt) {
                        //var row = Ext.getCmp(conf.mainGridPanelId).store.data.items[index].data;
                        var rd = record.data;

		            	if(value==undefined){
			            	if(record.data.options.length>0 && record.data.options[0].value){
                                rd.value = record.data.options[0].value;
                                if (rd.type == 1) {

                                    if(value == 1) return $renderOutputColor('green', _lang.TText.vYes);
                                    if(value == 2) return $renderOutputColor('gray', _lang.TText.vNo);
                                }
                                return record.data.options[0].value;

			            	}else{
			            		return '';
			            	}
		            	}else{
		            		rd.value= value;
		            		return value;
		            	}
		            }
				},
			]// end of columns
		});
	
	},// end of the initcomponents

	saveFun : function() {
		var records = Ext.getCmp(this.mainGridPanelId).getStore().data.items;
		var req = [];
		if(records.length>0){
			for(var i=0; i<records.length; i++){
				req[i] = {vid:records[i].data.id, value:records[i].data.value};
			}
		}		
		Ext.Ajax.request({
			url : this.urlSave,
			params: {rows: JSON.stringify(req)},
			method : 'POST',
			success : function(response, options) {
				var result = Ext.JSON.decode(response.responseText);
				if(result.msg){
					Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
				}else{
					if (result.success){ Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsSuccess);}
					else{Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsError);}
				}
			},
			failure : function(response, options) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, lang.TText.rsError);
			}
		});
	}// end of save
    
});