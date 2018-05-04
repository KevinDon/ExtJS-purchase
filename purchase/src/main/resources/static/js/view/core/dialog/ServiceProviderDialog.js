Ext.define('App.ServiceProviderDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.ServiceProviderDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.ServiceProviderDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
        if (this.onlyRead) {return;}
        var me = this;
        var selectedId = '';
        if(this.valueType != 'grid' && Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }else{
            selectedId = me.up().grid.getSelectionModel().selected.getAt(0).get(me.name);
        }

        new ServiceProviderDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            valueType: this.valueType || 'form',
            subcallback: this.subcallback ? this.subcallback: '',
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback:function(ids, titles, rows) {
                if(this.valueType == 'form') {
                    this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                    this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                }else{
                    me.up().grid.getSelectionModel().selected.getAt(0).set(me.name, ids);
                }

                if(this.subcallback){
                    this.subcallback.call(this, rows, me);
                }

            }}, false).show();
    }
});


ServiceProviderDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.ServiceProviderDocument.tSelector;
        conf.winId = 'ServiceProviderDialogWinID';
        conf.mainGridPanelId = 'ServiceProviderDialogWinGridPanelID';
        conf.searchFormPanelId= 'ServiceProviderDialogWinSearchPanelID',
        conf.selectGridPanelId = 'ServiceProviderDialogWinSelectGridPanelID';
        conf.treePanelId = 'ServiceProviderDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'archives/service_provider/list',
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        ServiceProviderDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title,
            width: this.single ? 650 : 750,
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
            title: _lang.ServiceProviderDocument.tabListTitle,
            scope : this,
            border : false,
            forceFit: false,
            url : conf.urlList,
            fields: [
				'id','categoryName','name','cnName','enName', 'buyerName','director',
				'address','abn','website','rating','files',
				'source', 'currency', 'paymentProvision',
				'dynContent', 'status', 'createdAt'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
				{ header: _lang.ServiceProviderDocument.fCategoryName, dataIndex: 'categoryName', width: 120, sortable: false},
                { header: _lang.VendorDocument.fCnName, dataIndex: 'cnName', width: 260 },
                { header: _lang.VendorDocument.fEnName, dataIndex: 'enName', width: 260 },
			    { header: _lang.ServiceProviderDocument.fDirector, dataIndex: 'director', width: 60},
			    { header: _lang.ServiceProviderDocument.fAddress, dataIndex: 'address', width: 390 },
			    { header: _lang.ServiceProviderDocument.fAbn, dataIndex: 'abn', width: 200 },
			    { header: _lang.ServiceProviderDocument.fWebsite, dataIndex: 'website', width: 200 },
                { header: _lang.VendorDocument.fSource, dataIndex: 'source', width: 80 ,
                    renderer: function(value){
                        var $source = _dict.source;
                        if($source.length>0 && $source[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $source[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 40 ,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
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
			title: _lang.ServiceProviderDocument.tSelected,
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
			fields : ['id','cnName','enName'],
			columns : [
				{ header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
				{ header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 260, sortable:false, hidden: curUserInfo.lang =='zh_CN' ? false: true},
			    { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 260, sortable:false, hidden: curUserInfo.lang !='zh_CN' ? false: true},
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
        if(this.valueType != 'grid' && this.fieldValueName){
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if(ids){
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for(var i=0; i<arrIds.length; i++){
                    if(curUserInfo.lang =='zh_CN'){
                        selStore.add({id: arrIds[i], cnName: arrNames[i]});
                    }else{
                        selStore.add({id: arrIds[i], enName: arrNames[i]});
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
                names += rows[i].data.name;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.name;
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