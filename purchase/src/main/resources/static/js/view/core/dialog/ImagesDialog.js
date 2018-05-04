/**
 * ImagesDialog 字段触发
 */

Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux.DataView', 'js/lib/ext/ux/DataView');
Ext.Loader.setPath('Ext.ux.DataView.DragSelector', 'js/lib/ext/ux/DataView/DragSelector.js');
Ext.Loader.setPath('Ext.ux.DataView.Draggable', 'js/lib/ext/ux/DataView/Draggable.js');
Ext.Loader.setPath('Ext.ux.DataView.LabelEditor', 'js/lib/ext/ux/DataView/LabelEditor.js');

Ext.define('App.ImagesDialog', {
    extend: 'Ext.Panel',
    alias: 'widget.ImagesDialog',

    constructor : function(conf){
        Ext.applyIf(this, conf);

        var conf = {
    		title : this.fieldLabel,
            moduleName: 'MyDocument',
            fieldValueName: this.hiddenName || this.valueName || 'main_images',
            fieldTitleName: this.titleName || 'main_imagesName',
            fileDefType: this.fileDefType || 1,
            readOnly: this.readOnly || false,
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.formId + '-ImagesGrid';
        conf.mainFormPanelId = this.formId || conf.mainGridPanelId + 'Form';
        conf.subGridPanelId = this.subGridPanelId ||  conf.mainGridPanelId ? conf.mainGridPanelId +'-ImagesMultiGridPanelID' : this.formId  +'-ImagesMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId ||  conf.mainGridPanelId ? conf.mainGridPanelId +'-ImagesMultiFormPanelID' : this.formId  +'-ImagesMultiFormPanelID';
        
        this.initUIComponents(conf);
        
        App.ImagesDialog.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
			minHeight: 200,
            height: '100%', width: '100%',
            border: false,
            items: [ this.subCenterPanel ]
        });
        
    },
    
    initUIComponents: function(conf){
    	var bnts = [{
            xtype: 'buttongroup',
            columns: 5,
            hidden: conf.readOnly,
            readOnly: conf.readOnly,
            items: [
               {xtype:'button', text: _lang.TButton.insert, iconCls:'fa fa-fw fa-plus', scope: this, 
            	   handler:function(event, toolEl, panelHeader) {
            		   this.conf = conf;
            		   this.onRowAction.call(this, null, null, null, conf);
	               }
               },
               {xtype:'button', text: _lang.TButton.edit, iconCls:'fa fa-fw fa-pencil-square-o', scope: this, 
            	   handler:function(event, toolEl, panelHeader) {
            		   var me = this;
            		   
            		   var row = me.dataview.getSelectionModel().selected.getAt(0);
            		   if (row == undefined) return false;
            		   
            		   me.conf = conf;
            		   this.onRowAction.call(me, null, row, 'btnRowEdit', conf);
	               }
               },
               {xtype:'button', text: _lang.TButton.clean, iconCls:'fa fa-fw fa-eraser', scope: this, 
            	   handler:function(event, toolEl, panelHeader) {
            		   var me = this;
            		   me.conf = conf;
            		   if (me.dataview.getStore().data.length <1) return false;
            		   Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureDeleteAll, function(btn) {
            			   if (btn == 'yes') {
            				   me.dataview.getStore().removeAll();
            				   me.dataview.refresh();
            				   me.setValueToForm.call(me, conf);
            			   }
            		   });
            	   }
               }
           ]
        }];
    	
    	var store = $HpStore({
    		url: '',
    		autoLaod: false,
    		fields: ['id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName']
    	});
    	
    	this.dataview = Ext.create('Ext.view.View', {
            id: conf.subGridPanelId,
            store: store,
            scope: this,
            loadMask: false,
            cls: 'image-list',
            itemCls: 'col-5 item',
            itemTpl: Ext.create('Ext.XTemplate',
            		'<div class="data-row" data="{[values.id]}" alt="{[_lang.TText.dblClickDelete]}" title="{[_lang.TText.dblClickDelete]}">',
	            		'<img width="120" height="120" src="{[values.path.replace(/ /g, "-")]}" />',
	            		'<div>',
		            		'<strong>{[values.name]}</strong>',
	                    '</div>',
                    '</div>'
            ),
            itemSelector: 'div.item',
            overItemCls : 'hover',
            trackOver: true, 
            autoScroll: true
        });

    	this.dataview.on('itemdblclick', function (view, record, item, index, e ) {
    		Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureDeleteSelected, function(btn) {
    			if (btn == 'yes') {
		    		view.getStore().removeAt(index);
		    		view.refresh();
		    		view.scope.setValueToForm.call(view.scope, conf);
    			}
    		});
    	});
    	
    	this.subCenterPanel =  Ext.create('Ext.panel.Panel', {
    		id: conf.subGridPanelId + '-form',
	        layout: 'fit',
	        items : [this.dataview, new HP.FormPanel({
	        	id: conf.subFormPanelId + '-hiddenForm',
	        	hidden:true,
	        	fieldItems:[
	        	     {field:'imageIds', xtype:'hidden'},
	        	     {field:'imageNames', xtype:'hidden'},
	        	]
	        })],
	        border: false,
	        tbar: conf.readOnly ? false: bnts
	    });

    },
    
    onRowAction: function(grid, record, action, conf) {
    	var me = this;
    	
		switch (action) {
			case 'btnRowEdit' :
				var selectedId = record.data.id;
				new FilesDialogWin({
		            single:true,
		            fieldValueName: this.conf.fieldValueName? this.conf.fieldValueName: 'imageIds',
		            fieldTitleName: this.conf.fieldTitleName? this.conf.fieldTitleName: 'imageNames',
		            fileDefType: this.conf.fileDefType,
		            selectedId : selectedId,
		            meForm: Ext.getCmp(this.conf.mainFormPanelId ? this.conf.mainFormPanelId :this.conf.subFormPanelId + '-hiddenForm'),
		            meGrid: Ext.getCmp(this.conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.length>0){
		            		for(var i=0; i<this.meGrid.getStore().data.length; i++){
		            			if(this.meGrid.getStore().data.items[i].data.id == selectedId && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id) ){
		            				this.meGrid.getStore().insert(i, result[0].data);
			            			this.meGrid.getStore().removeAt(i+1);
			            			
			            			break;
		            			}
		            		}
	            			
	            			this.meGrid.refresh();
	            			me.setValueToForm.call(me, conf);
		            	}
		            }}, false).show();
				break;
				
			case 'btnRowRemove' :
				Ext.getCmp(conf.subGridPanelId).store.remove(record);
				break;
				
			default:
		        new FilesDialogWin({
		            single:false,
		            fieldValueName: this.conf.fieldValueName? this.conf.fieldValueName: 'imageIds',
		            fieldTitleName: this.conf.fieldTitleName? this.conf.fieldTitleName: 'imageNames',
		            fileDefType: this.conf.fileDefType,
		            selectedId : '',
		            initValue: false,
		            meForm: Ext.getCmp(this.conf.mainFormPanelId ? this.conf.mainFormPanelId :this.conf.subFormPanelId + '-hiddenForm'),
		            meGrid: Ext.getCmp(this.conf.subGridPanelId),
		            callback:function(ids, titles, result) {
		            	if(result.data.items.length>0){
		            		var items = result.data.items;
		            		for(var index=0; index<items.length; index++){
		            			if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
		            				this.meGrid.getStore().add(items[index].raw);
		            			}
		            		}
		            	}
		            	this.meGrid.refresh();
		            	me.setValueToForm.call(me, conf);
		            }}, false).show();
		        
				break;
		}
	},
	
    setValueToForm: function(conf){
		var ids = '', names = '', rows=[];

        rows = this.dataview.getStore().data;
		for (var i = 0; i < rows.length; i++) {
		    if (i > 0) {ids += ',';names += ',';}
		    ids += rows.getAt(i).data.id;
		    names += rows.getAt(i).data.name;
		}

		//赋值
		var meForm = Ext.getCmp(conf.mainFormPanelId);
		meForm.getCmpByName(conf.fieldValueName).setValue(ids);
		meForm.getCmpByName(conf.fieldTitleName).setValue(names);

    }
});

