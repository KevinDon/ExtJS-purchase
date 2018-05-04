Ext.define('App.WarehouseDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.WarehouseDialog',

    constructor : function(conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
        App.WarehouseDialog.superclass.constructor.call(this, conf);
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

        new WarehouseDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId : selectedId,
            valueType: this.valueType || 'form',
            subcallback: this.subcallback ? this.subcallback: '',
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            meGrid: Ext.getCmp(this.formGridPanelId),
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


WarehouseDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.WarehouseDialog.tSelector;
        conf.winId = 'WarehouseDialogWinID';
        conf.mainGridPanelId = 'WarehouseDialogWinGridPanelID';
        conf.searchFormPanelId= 'WarehouseDialogWinSearchPanelID';
        conf.selectGridPanelId = 'WarehouseDialogWinSelectGridPanelID';
        conf.treePanelId = 'WarehouseDialogWinTreePanelId';
        conf.urlList = __ctxPath + 'archives/service_provider/list';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        Ext.applyIf(this, conf);
        this.initUI(conf);

        WarehouseDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title,
            width: this.single ? 880 : 1080,
            height: 250,
            region: 'center',
            layout : 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.centerPanel, this.selectGridPanel]
        });
    },

    initUI : function(conf) {
        var containerNumbers = [];
        if(!!this.meGrid){
            var store = this.meGrid.getStore().getRange();
            for(var i = 0; i < store.length; i++){
                containerNumbers.push(store[i].data.containerNumber);
            }
        }

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title: _lang.WarehouseDialog.tabListTitle,
            scope : this,
            border : false,
            forceFit: false,
            url: __ctxPath + 'api/transfer/wms?method=enquireInboundWarehouse',
            baseParams: {
                containerNumbers: containerNumbers,
            },
            fields: [
				'warehouseId', 'totalPickLocation', 'address', 'warehouse', 'empty_locations',
                {name: 'id', mapping: 'warehouseId'}
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.WarehouseDialog.fWarehouseId, dataIndex: 'warehouse', width: 100},
                { header: _lang.WarehouseDialog.fTotalPickLocation, dataIndex: 'empty_locations', width: 100},
                { header: _lang.WarehouseDialog.fAddress, dataIndex: 'address', width: 300},
                // { header: _lang.WarehouseDialog.fTotalPickLocation, dataIndex: 'totalPickLocation', width: 80},
            ],// end of columns
            // appendColumns: $groupGridCreatedColumns({sort:false}),
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
            },

        });
        this.gridPanel.store.on('load', function(record, options, success){
                if(!success){
                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorWarehouse);
                }
        });
        // this.loadTestData();

        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
			title: _lang.WarehouseDialog.tSelected,
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
                { header: _lang.WarehouseDialog.fWarehouseId, dataIndex: 'warehouseId', width: 200},
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

    loadTestData: function(){
        var data = [
            {
                warehouseId: 'WH01',
                address: 'Kensington',
                totalPickLocation: 100,
            },
            {
                warehouseId: 'WH02',
                address: 'Braybrook 1',
                totalPickLocation: 80,
            },
            {
                warehouseId: 'WH03',
                address: 'Braybrook 2',
                totalPickLocation: 70
            }
        ];

        this.gridPanel.getStore().add(data);
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