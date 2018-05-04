/**
 * ProductCertificationDialog 字段触发
 * ProductCertificationDialogWin　　弹出窗口
 */

Ext.define('App.ProductCertificationDialog', {
	extend: 'Ext.form.field.Trigger',
    alias: 'widget.ProductCertificationDialog',

    constructor : function(conf) {
    	this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.ProductCertificationDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}

    	var selectedId = '';

    	if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
    		selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
    	}
        new ProductCertificationDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            selectedId : selectedId,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback:function(ids, titles,rows) {
                 this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                 this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
                    this.subcallback.call(this, rows);
                }
            }}, false).show();
    }
});


ProductCertificationDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.ProductCertificateDocument.mTitle;
        conf.moduleName = 'ProductCombination';
        conf.winId = 'ProductCertificationDialogWinID';
        conf.mainGridPanelId = 'ProductCertificationDialogWinGridPanelID';
        conf.searchFormPanelId= 'ProductCertificationDialogWinSearchPanelID',
        conf.selectGridPanelId = 'ProductCertificationDialogWinSelectGridPanelID';
        conf.treePanelId = 'ProductCertificationDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'archives/productCertificate/list',
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);
        ProductCertificationDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductCertificateDocument.mTitle,
            width: this.single ? 880 : 1080,
            region: 'center',
            layout : 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.centerPanel, this.selectGridPanel]
        });
    },

    initUI : function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            onlyKeywords: true
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title: _lang.ProductCertificateDocument.mTitle,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'id','vendorId','vendorName','productId', 'relevantStandard','description','certificateNumber','effectiveDate','validUntil',
                'certificateFile','status','creatorId','creatorName','createdAt','updatedAt','departmentId','departmentName','productCertificateId'
            ],
            columns: [
                { header: _lang.FlowProductCertification.fProductCertificateId, dataIndex: 'id', width: 180, },
                { header: _lang.ProductCertificate.fProductId, dataIndex: 'productId', width: 100, hidden:true},
                { header: _lang.ProductCertificate.fVendorId, dataIndex: 'vendorId', width: 100, hidden:true },
                { header: _lang.ProductCertificate.fVendorName, dataIndex: 'vendorName', width: 200, hidden:true},
                { header: _lang.ProductCertificate.fRelevantStandard, dataIndex: 'relevantStandard', width: 80 , },
                { header: _lang.ProductCertificate.fDesc, dataIndex: 'description', width: 200 , },
                { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200 , },
                { header: _lang.ProductCertificate.fEffectiveDate, dataIndex: 'effectiveDate', width: 140 , },
                { header: _lang.ProductCertificate.fValidUntil, dataIndex: 'validUntil', width: 140 , },
                // { header: _lang.ProductCertificate.fCertificateFile, dataIndex: 'certificateFile', width: 90 , },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({assignee:false}),

            itemdblclick : function(obj, record, item, index, e, eOpts){
                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    if(selStore.getCount()){
                        for (var i = 0; i < selStore.getCount(); i++) {
                            if (selStore.getAt(i).data.id == record.data.id) {
                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedRecord);
                                return;
                            }
                        }
                    }
                    //console.log(record.data);
                    selStore.add(record.data);
                }else{
                    this.scope.winOk.call(this.scope);
                }
            },
            callback : function(obj, records){
                //初始化选择
                if(this.selectedId && records.length){
                    for(var i=0; i<records.length; i++){
                        if(records[i].data.id == this.selectedId){
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                };
            }
        });

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.ProductCertificateDocument.tSelected,
            id : conf.selectGridPanelId,
            scope : this,
            hidden : this.single ? true : false,
            width: 150,
            minWidth: 150,
            border: false,
            autoLoad: false,
            unbbar : true,
            collapsible : true,
            split : true,
            fields : ['id','certificateNumber'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductCertificate.fCertificateNumber, dataIndex: 'certificateNumber', width: 200, sortable:false,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
            }
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,

            items: [ this.searchPanel, this.gridPanel]
        });

        // init value
        if(this.fieldValueName){
            console.log()
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if(ids){
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for(var i=0; i<arrIds.length; i++){
                    if(curUserInfo.lang =='zh_CN'){
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
                    }else{
                        selStore.add({id: arrIds[i], sku: arrNames[i]});
                    }
                }
            }
        }
    },

    winOk : function(){
        var ids = '';
        var names = '';
        var rows = {};
        if(this.single){
            rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows[i].data.id;
                names += rows[i].data.sku;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.sku;
            }
        }

        if (this.callback) {
            this.callback.call(this, ids, names, rows);
        }
        Ext.getCmp(this.winId).close();
    },

    winClean:function(){
        var ids = '';
        var names = '';
        if (this.callback) {
            this.callback.call(this, ids, names);
        }
        Ext.getCmp(this.winId).close();
    }

});