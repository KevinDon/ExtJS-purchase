MyDocumentView = Ext.extend(Ext.Panel, {
	constructor: function(conf) {
		Ext.applyIf(this, conf);
		var conf = {
			title : _lang.MyDocument.mTitle,
			moduleName: 'MyDocument',
			frameId : 'MyDocumentView',
			mainGridPanelId : 'MyDocumentViewGridPanelID',
			mainFormPanelId : 'MyDocumentViewFormPanelID',
			searchFormPanelId: 'MyDocumentViewSearchFormPanelID',
			mainTabPanelId: 'MyDocumentViewTbsPanelId',
			urlList: __ctxPath + 'mydoc/list',
			urlSave: __ctxPath + 'mydoc/save',
			urlDelete: __ctxPath + 'mydoc/delete',
			urlGet: __ctxPath + 'mydoc/get',
			refresh: true,
			save: true,
			add: true,
			saveAs: true,
			del: true,
            addFun: this.addFun,
		};
		this.initUIComponents(conf);
		
		MyDocumentView.superclass.constructor.call(this, {
			id: conf.frameId, title: conf.title,
			region: 'center', layout: 'border',
			closable: true, autoScroll: false,
			tbar: Ext.create("App.toolbar", conf),
			items: [ this.searchPanel, this.centerPanel ]
		});
	},
	
	initUIComponents: function(conf) {
		var $shared = new $HpDictStore({code:'document', codeSub:'shared'});
		
		this.searchPanel = new HP.SearchPanel({
			region: 'north',
			id: conf.searchFormPanelId,
			scope: this,
			parentConf: conf,
			fieldItems:[
			    {field:'Q-name-S-LK', xtype:'textfield', title:_lang.MyDocument.fName},
				{field:'Q-shared-N-EQ', xtype:'dictcombo', value:'', title:_lang.TText.fShared, code:'document', codeSub:'shared', addAll:true},
				{field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value:'', 
			    	store: [['', _lang.TText.vAll], ['2', _lang.TText.vDisabled], ['1', _lang.TText.vEnabled]]
			    },
			    {field:'Q-extension-S-LK', xtype:'textfield', title:_lang.MyDocument.fExtension},
			    {title:_lang.Dictionary.mCateTitle, field:'Q-categoryId-S-EQ', xtype:'comboremote', addAll: true,
			    	url:__ctxPath + 'mydoccate/list'
			    },
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
			title: _lang.MyDocument.tabDictCateTitle,
			collapsible: true,
			forceFit: false,
			split: true,
			scope: this,
			url: conf.urlList,
			fields: ['id','name','path','extension','note','categoryId','category.title','bytes','status','shared','createdAt','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'],
			width: '65%',
			minWidth: 400,
			columns: [
				{ header: 'ID', dataIndex: 'id', width: 80, hidden: true },
				{ header: _lang.MyDocument.fName, dataIndex: 'name', width:240 },
				{ header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
				{ header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 },
				{ header: _lang.MyDocument.fCategoryId, dataIndex: 'categoryId', width:100, hidden:true },
				{ header: _lang.MyDocument.fCategory, dataIndex: 'category.title', width:120},
				{ header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
				{ header: _lang.MyDocument.fShared, dataIndex: 'shared', width:60,
					renderer: function(value){
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
			title: _lang.MyDocument.tabDictCateDescTitle,
			scope: this,
			fileUpload:true,
			fieldItems: [
			    { field: 'id',	xtype: 'hidden'},
			    { field: 'files', xtype: 'filefield', fieldLabel: _lang.MyDocument.fName, buttonText: _lang.MyDocument.fSelectFile, 
			    	labelWidth: 60,
			    	listeners: {
			    		scope: this,
			    		change: function(obj, value ){
			    			jQuery('#uploadFile').html('');
			    		}
			    	}
			    },
			    { field: 'main.name', xtype:'hidden'},
			    { field: 'main.categoryId', xtype: 'comboremote', fieldLabel: _lang.MyDocument.mTitleCate, allowBlank:false, useDefault: true,
			    	url:__ctxPath + 'mydoccate/list'
			    },
			    { field: 'main.shared', xtype: 'dictcombo', fieldLabel: _lang.MyDocument.fShared, code:'document', codeSub:'shared', value:'1', allowBlank:false},
			    { field: 'main.note', xtype: 'htmleditor', fieldLabel: _lang.MyDocument.fNote, allowBlank: false},
			    { field: 'main.extension', xtype: 'displayfield', fieldLabel: _lang.MyDocument.fExtension},
			    { field: 'main.bytes', xtype: 'displayfield', fieldLabel: _lang.MyDocument.fBytes, renderer: Ext.util.Format.fileSize },
			],
			appendItems: $groupFormCreatedColumns({sort:false})
		});
		
		this.northFormPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-north',
			region: 'north',
			laylout: 'fit',
			height: 250,
			border: false,
			style: 'margin:0; padding:12px;',
			html: '<div id="uploadFile" class="uploadFile"></div>'
		});
		
		this.eastPanel = new Ext.Panel({
			id: conf.mainFormPanelId + '-east',
			layout: 'border',
			region: 'center',
			bbar:false,
			width:315,
			scope: this,
			items: [ this.northFormPanel, this.centerFormPanel]
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
		this.centerFormPanel.loadData({
			url: conf.urlGet + '?id=' + record.data.id,
			root: 'data', preName : 'main', viewPanel: this.centerFormPanel,
			success:function(response, options){
				if(response.responseText){
                    var json = Ext.JSON.decode(response.responseText);
	    			if(json.success == true){
	    			    if(json.data.categoryId){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.categoryId').setReadOnly(true);
                        }
	    				var fileExt = json.data.extension.toLowerCase();
	    				var strContent ='<img id="previewDiv" alt="'+ _lang.TText.clickPreview +'" title="'+ _lang.TText.clickPreview +'" ';
	    				if( fileExt== 'jpg' ||fileExt== 'gif' || fileExt== 'png' || fileExt== 'bmp' ){
	    					strContent += 'src="' + __ctxPath + json.data.path + '" width="290" height="200"';
	    				}else if(fileExt == 'xls' || fileExt== 'xlsx'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/excel.png"';
	    				}else if(fileExt == 'doc' || fileExt== 'docx'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/word.png"';
	    				}else if(fileExt == 'ppt' || fileExt== 'pptx'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/ppt.png"';
	    				}else if(fileExt == 'pdf' || fileExt== 'psd'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/pdf.png"';
	    				}else if(fileExt == 'zip' || fileExt== 'rar'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/zip.png"';
	    				}else if(fileExt == 'mp3' || fileExt == 'wma' || fileExt == 'wav'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/music.png"';
	    				}else if(fileExt == 'rm' || fileExt == 'rmvb' || fileExt == 'wmv' || fileExt == 'avi'
	    					|| fileExt == 'ogg' || fileExt == 'mov' || fileExt == 'mp4'){
	    					strContent += 'src="' + __ctxPath + 'images/filetype/video.png"';
	    				}else{
	    					strContent += 'src="' + __ctxPath + 'images/filetype/unknow.png"';
	    				}
	    				strContent += '/>';
	    				jQuery('#uploadFile').html(strContent);
	    				jQuery('#previewDiv').bind('click', function(){
                            options.scope.scope.previewRs.call(options.scope.scope);
	    				});
	    			}
    			}
			}
		});
	},
    //预览文件
    previewRs: function(){
        $editGridRs({
            grid: this.gridPanel,
            winForm : FilesPreviewDialog,
            scope : this
        });
    },
    addFun: function () {
        Ext.getCmp(this.mainFormPanelId).getCmpByName('main.categoryId').setReadOnly(false);
        Ext.getCmp(this.mainFormPanelId).form.reset();
    }
});