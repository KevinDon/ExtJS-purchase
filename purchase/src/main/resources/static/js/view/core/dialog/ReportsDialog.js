ReportsDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.Reports.mTitle;
        conf.winId = 'ReportsDialogWinID';
        conf.mainGridPanelId = 'ReportsDialogWinGridPanelID';
        conf.searchFormPanelId= 'ReportsDialogWinSearchPanelID';
        conf.selectGridPanelId = 'ReportsDialogWinSelectGridPanelID';
        conf.treePanelId = 'ReportsDialogWinTreePanelId';
        conf.status = 1;
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;

        if(conf.type == '1'){
            conf.moduleName = 'Reports';
            conf.urlList = __ctxPath + 'archives/newproduct/list?status='+ conf.status;
        }else if(conf.productType == '2'){
            //all product
            conf.urlList = __ctxPath + 'archives/product/list?status=' + conf.status;
        }else if(conf.productType == '3') {
            //采购计划下的产品
            conf.urlList = __ctxPath + 'archives/flow/purchase/plandetail/list?isview=' + conf.isview;
        }else if(conf.productType == '4') {
            //样品申请通过的产品
            conf.urlList = __ctxPath + 'archives/flow/purchase/sample/listfordialog';
        }else{
            //产品
            conf.urlList = __ctxPath + 'archives/product/listProduct?status=' + conf.status;
        }

        //filter product by vendorId
        if(!!conf.filterVendor){
            var cmp = conf.meForm.getCmpByName(conf.fieldVendorIdName);

            if(!!cmp){
                if(conf.urlList.indexOf("?")>0){
                    conf.urlList += '&vendorId=' + cmp.getValue();
                }else {
                    conf.urlList += '?vendorId=' + cmp.getValue();
                }
            }
        }

        Ext.applyIf(this, conf);
        this.initUI(conf);

        ReportsDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.Reports.tSelector,
            width: this.single ? 980 : 1080,
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
        this.selectedData = [];
        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title: _lang.Reports.tabListTitle,
            scope: this,
            forceFit: false,
            border: false,
            width: '85%',
            minWidth: 1080,
            autoScroll: true,
            url : conf.urlList,
            fields: ['id', 'title', 'businessId', 'reportTime', 'vendorId', 'vendorCnName', 'vendorEnName', 'file', 'result',
                'confirmedAt', 'confirmedCnName', 'confirmedEnName', 'confirmedId','createdAt','updatedAt','moduleName',
                'status', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: this.readOnly ? 45: 45, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.Reports.fPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true},
                {header: _lang.Reports.fTitle, dataIndex: 'title', width: 260},
                {header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 140},
                {header: _lang.Reports.fConfirmedId, dataIndex: 'confirmedId', width: 180, hidden: true},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedCnName', width: 120, hidden: curUserInfo.lang != 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedEnName', width: 140, hidden: curUserInfo.lang == 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmedAt, dataIndex: 'confirmedAt', width: 140},
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
                    selStore.add(record.data);
                }else{
                    this.scope.winOk.call(this.scope);
                }
                if(!!this.scope.selectedData) {
                    this.scope.selectedData.push(record);
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
            title: _lang.Reports.tSelected,
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
            fields : ['id','sku'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.Reports.fSku, dataIndex: 'sku', width: 200, sortable:false,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
                var selectedData = [];
                for(var i = 0; i < this.scope.selectedData.length; i++){
                    var data = this.scope.selectedData[i];
                    if(data.id != record.data.id) {
                        selectedData.push(data);
                    }
                }

                this.scope.selectedData = selectedData;
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
            var win = Ext.getCmp(this.winId);
            this.callback.call(this, ids, names, rows, win.selectedData);
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


Ext.define('App.ReportsFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ReportsFormMultiGrid',

    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title: this.fieldLabel,
            moduleName: 'Reports',
            fieldValueName: this.valueName || 'main_reports',
            fieldTitleName: this.titleName || 'main_reportsName'
        };
        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-ReportsMultiGrid';
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-ReportsMultiForm';
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 200;
        conf.noTitle = this.noTitle || false;

        this.initUIComponents(conf);

        App.ReportsFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId,
            minHeight: conf.defHeight,
            height: conf.defHeight, width: conf.defWidth,
            bodyCls:'x-panel-body-gray',
            items: [this.subGridPanel]
        });
    },

    setValue:function (values) {
        var cmpAtta = this.subGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(index in values){
                var reports = {};
                reports = values[index];
                Ext.applyIf(reports, values[index].reports);
                cmpAtta.add(reports);
            }
        }
    },

    initUIComponents: function (conf) {
        var tools = [{
            type: 'plus', tooltip: _lang.TButton.insert, scope: this, hidden: this.readOnly,
            handler: function (event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }
        }, {
            type: 'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.subGridPanel.setHeight(conf.defHeight - 3);
            }
        }, {
            type: 'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function (event, toolEl, panelHeader) {
                this.setHeight(400);
                this.subGridPanel.setHeight(397);
            }
        }];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId + '-l',
            title: conf.noTitle ? '' : _lang.Reports.fReportsList,
            width: conf.defWidth,
            height: conf.defHeight - 3,
            url: '',
            bbar: false,
            header: conf.noTitle ? false : {
                cls: 'x-panel-header-gray',
            },

            tools: conf.noTitle ? false : tools,
            autoLoad: false,
            rsort: false,
            forceFit: true,
            edit: !this.readOnly,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            fields: ['id', 'title', 'businessId', 'reportTime', 'vendorId', 'vendorCnName', 'vendorEnName', 'file', 'result',
                'confirmedAt', 'confirmedCnName', 'confirmedEnName', 'confirmedId','createdAt','updatedAt','moduleName',
                'status', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: this.readOnly ? 45: 120, locked: true, hideMode:'display',
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.Reports.fPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        },

                    },{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit, hide: this.readOnly,
                        callback: function (grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }, {
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del, hide: this.readOnly,
                        callback: function (grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true},
                {header: _lang.Reports.fTitle, dataIndex: 'title', width: 260},
                {header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 140},
                {header: _lang.Reports.fConfirmedId, dataIndex: 'confirmedId', width: 180, hidden: true},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedCnName', width: 120, hidden: curUserInfo.lang != 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedEnName', width: 140, hidden: curUserInfo.lang == 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmedAt, dataIndex: 'confirmedAt', width: 140},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false, assignee:false}),
        });
    },

    btnRowPreview: function () {},
    btnRowDownload: function () {},
    btnRowEdit: function () {},
    btnRowRemove: function () {},
    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                var winForm = '';
                if(!conf.winForm){
                    winForm = eval(record.data.moduleName + 'Form');
                }

                new winForm({
                    readOnly: true,
                    action: 'preview',
                    record: {id: record.data.id, status: record.data.status}
                }, false).show();
                break;
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new ReportsDialogWin({
                    single: true,
                    fieldValueName: conf.fieldValueName,
                    fieldTitleName: conf.fieldTitleName,
                    selectedId: selectedId,
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subGridPanelId),
                    callback: function (ids, titles, result) {
                        if (result.length > 0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)) {
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx + 1);
                        }
                    }
                }, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.subGridPanelId).store.remove(record);
                break;

            default :
                new ReportsDialogWin({
                    single: false,
                    fieldValueName: this.conf.fieldValueName,
                    fieldTitleName: this.conf.fieldTitleName,
                    selectedId: '',
                    meForm: Ext.getCmp(this.conf.mainFormPanelId),
                    meGrid: Ext.getCmp(this.conf.subGridPanelId),
                    callback: function (ids, titles, result) {
                        if (result.data.items.length > 0) {
                            var items = result.data.items;
                            for (var index = 0; index < items.length; index++) {
                                if (items[index] != undefined && !$checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)) {
                                    this.meGrid.getStore().add(items[index].raw);
                                }
                            }
                        }
                    }
                }, false).show();

                break;
        }
    }
});


Ext.define('App.ReportProductTabGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.ReportProductTabGrid',

    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            moduleName: 'Reports',
        };

        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-ReportProductGridTabGrid';
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-ReportProductFormTabGrid';
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || '100%';
        conf.noTitle = this.noTitle || false;
        conf.title = this.title;
        conf.winForm = this.winForm || '';
        this.initUIComponents(conf);

        App.ReportProductTabGrid.superclass.constructor.call(this, this.subGridPanel);
    },

    setValue:function (values) {
        var cmpAtta = this.subGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(index in values){
                var reports = {};
                reports = values[index];
                Ext.applyIf(reports, values[index].reports);
                cmpAtta.add(reports);
            }
        }
    },

    refresh: function(){
        this.subGridPanel.getStore().reload();
    },

    initUIComponents: function (conf) {
        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: conf.noTitle ?  conf.title : _lang.Reports.fReportsList,
            width: conf.defWidth,
            height: conf.defHeight,
            url: conf.historyListUrl,
            bbar: false,
            scope: this,
            rsort:false,
            bodyCls:'x-panel-body-gray',
            header: conf.noTitle ? false : {cls: 'x-panel-header-gray'},
            tools: conf.noTitle ? false : tools,
            autoLoad: false,
            forceFit: true,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            fields: ['id', 'title', 'businessId', 'reportTime', 'vendorId', 'vendorCnName', 'vendorEnName', 'file', 'result',
                'confirmedAt', 'confirmedCnName', 'confirmedEnName', 'confirmedId','createdAt','updatedAt','moduleName',
                'status', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: this.readOnly ? 45: 45, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.Reports.fPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                {header: 'ID', dataIndex: 'id', width: 180, hidden: true},
                {header: _lang.Reports.fTitle, dataIndex: 'title', width: 260},
                {header: _lang.Reports.fReportTime, dataIndex: 'reportTime', width: 140},
                {header: _lang.Reports.fConfirmedId, dataIndex: 'confirmedId', width: 180, hidden: true},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedCnName', width: 120, hidden: curUserInfo.lang != 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmed, dataIndex: 'confirmedEnName', width: 140, hidden: curUserInfo.lang == 'zh_CN' ? true: false},
                {header: _lang.Reports.fConfirmedAt, dataIndex: 'confirmedAt', width: 140},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false, assignee:false}),
        });
    },

    btnRowPreview: function () {},
    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview':
                var winForm = '';
                if(!conf.winForm){
                    winForm = eval(record.data.moduleName + 'Form');
                }

                new winForm({
                    action: 'preview',
                    readOnly: true,
                    record: {id: record.data.id, status: record.data.status}
                }, false).show();
                break;
        }
    }

});


Ext.define('App.ReportProductTabs', {
	extend: 'Ext.tab.Panel',
    alias: 'widget.ReportProductTabs',
    
    constructor : function(conf){
    	Ext.applyIf(this, conf);

    	conf.mainFormPanelId= this.mainFormPanelId || '';

    	App.ReportProductTabs.superclass.constructor.call(this, {
    		id: this.mainTabPanelId,
            title: _lang.ReportProductCompliance.fCheckDetails,
    		region: 'south',
    		plain: this.plain != undefined ? this.plain: false,
    		split: this.split != undefined? this.split: true,
            retaleGridName: this.retaleGridName || '',
            mainFormPanelId: conf.mainFormPanelId,
            scope: this,
            height: this.height !=undefined? this.height: '32%',
            minHeight: 50,
    	    autoHeight: true,
    		autoScroll : false,
    		autoRender:true,
			width: 'auto',
            realOnly: this.readOnly,
            bodyCls:'x-panel-body-gray',
            cls:'sub-tabs x-panel-tabs-gray',
            listeners : {
                'tabchange' : function(tab, newc, oldc) {
                    if(!tab.mainFormPanelId || !tab.retaleGridName) return;
                    var cmp = Ext.getCmp(tab.mainFormPanelId).getCmpByName(tab.retaleGridName).formGridPanel;
                    if(cmp.getStore().data.getCount()>0){
                        var records = cmp.getStore().data.items;
                        for(var i=0; i< cmp.getStore().data.getCount(); i++){
                            if(records[i].data.id == newc.relateItemId){
                                cmp.getSelectionModel().select(records[i], true);
                            }
                        }
                    }
                }
            }
    	});
    },

	addTab: function(sku, productId, initData, index){

        var riskRating = initData.cplRiskRating ? initData.cplRiskRating.toString() : initData.prevRiskRating ? initData.prevRiskRating.toString() : '';
        var grid = new HP.GridPanel({
            id: sku,
            url: __ctxPath + 'dict/getkey?code=' + this.code,
            bbar: false,
            autoLoad: false,
            rsort: false,
            height: 500,
            edit: true,
            groupField: 'title',
            forceFit : true,
            features: [{ftype:'grouping'}],
            fields: ['id', 'codeMain', 'codeSub', 'troubleTicketId', 'troubleDetailId', 'active', 'options', 'title', 'subTitle','param1','qty'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fTroubleTicketId, dataIndex: 'troubleTicketId', width: 80, hidden: true, sortable:false},
                { header: _lang.ProductProblem.fTroubleTicketId, dataIndex: 'troubleDetailId', width: 80, hidden: true, sortable:false,},
                { header: _lang.Dictionary.fCodeMain, dataIndex: 'codeMain', width: 120, hidden: true, sortable:false },
                { header: _lang.Dictionary.fCodeSub, dataIndex: 'codeSub', width: 140, hidden: true, sortable:false },
                { header: _lang.ProductProblem.fTroubleCategoryId, dataIndex: 'title', width: 100, sortable:false},
                { header: _lang.ProductProblem.fTroubleDetailId, dataIndex: 'subTitle', width: 300, flex : 3, sortable:false,},
                { header: _lang.ReportProductCompliance.fParam1, dataIndex: 'param1', width: 75, align: 'right', sortable:false,},
                { header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false, scope : this,
                    realOnly: this.readOnly, disabled: this.readOnly,
                    renderer:function(value, meta){
                       return $renderOutputCheckColumns(value, meta)
                    },
                }
            ],// end of columns
            cellclick: function(obj, td, cellIndex, record, tr, rowIndex, e, eOpts){
                var rows = this.getStore().data.items;
                this.up().up().count.call(this, rows);
            },
            afterrender: function (obj) {
                // var rows = this.getStore().data.items;
                // this.up().up().count.call(this, rows);
            }
        });

        $HpStore({
            fields: ['id', 'codeMain', 'codeSub', 'troubleTicketId', 'troubleDetailId', 'troubleQty', 'active', 'options', 'title', 'subTitle'],
            url: __ctxPath + 'dict/getkey?code=' + this.code, loadMask: true, scope: this,
            callback: function (conf, records, eOpts) {
                grid.getStore().removeAll();
                for (var index in records) {
                    var row = {};
                    row.codeMain = records[index].data.codeMain;
                    row.codeSub = records[index].data.codeSub;
                    row.title = records[index].data.title;
                    if (records[index].data.options.length > 0) {
                        var options = records[index].data.options;
                        for (var i in options) {
                            row.troubleDetailId = options[i].value;
                            row.subTitle = options[i].title;
                            row.param1 = options[i].param1;
                            row.active = false;
                            if(!!initData){
                                for(var k in initData.troubleDetails){
                                    if(initData.troubleDetails[k].sku == grid.up().title && initData.troubleDetails[k].troubleDetailId == row.troubleDetailId){
                                        row.active = true;
                                    }
                                }
                            }
                            grid.getStore().add(row);
                        }
                    }
                }
            }
        });

        var tabsIndex = Ext.getCmp(this.mainTabPanelId).items.length;
        //add tab
        var panel = Ext.create('Ext.Panel',{
            id: productId + '-' + this.tabGridPanelId,
            height: 500,
            title: sku,
            relateItemId: productId,
            tabsIndex: tabsIndex,
            items:[
                new HP.FormPanel({
                    id: productId + '-' + this.tabGridPanelId + '-f',
                    readOnly: this.readOnly,
                    fieldItems:[
                        { field: 'products['+tabsIndex+'].cplHasAuthentication',  xtype: 'combo', fieldLabel: _lang.ProductDocument.fMandatory ,
                            readOnly: this.readOnly, value: initData.cplHasAuthentication || '',
                            cls:'col-2', value:'2',
                            store: [['2', _lang.TText.vNo], ['1', _lang.TText.vYes]],
                            onChange: function () {
                                var rows = this.up().up().items.items[1].getStore().data.items;
                                this.up().up().up().count.call(this.up(), rows);
                            }
                        },
                        {field: 'products['+tabsIndex+'].cplReference', xtype: 'textfield', fieldLabel: _lang.ReportProductCompliance.fCplReference, value: initData.cplReference|| '', readOnly: this.readOnly, cls:'col-2'},
                        {field: 'products['+tabsIndex+'].cplAuthenticationNames', xtype: 'textfield', fieldLabel: _lang.ReportProductCompliance.fCplAuthenticationNames, value: initData.cplAuthenticationNames|| '', readOnly: this.readOnly, cls:'col-1'},
                        {field: 'products['+tabsIndex+'].cplScore', xtype: 'textfield', fieldLabel: _lang.ReportProductCompliance.fCplScore, cls:'col-2', value: initData.cplScore|| '', readOnly:true},
                        {field: 'products['+tabsIndex+'].cplRiskRating', xtype: 'dictcombo', fieldLabel: _lang.ReportProductCompliance.fCplRiskRating,
                            value: riskRating,
                            cls:'col-2',code:'check', codeSub:'product', readOnly: this.readOnly, allowBlank:false,
                        },
                    ]
                }),
                grid
            ]
        });
        Ext.getCmp(this.mainTabPanelId).add(panel);
	},

    updateTabs: function(skus, productIds, data){
        var cmpPanel = Ext.getCmp(this.mainTabPanelId );
        var tabsToDelete = [];
        var tabs = cmpPanel.items.items;
        for(var i = 0; i < tabs.length; i++){
        	var tab = tabs[i];
        	var index = skus.indexOf(tab.title);
        	if(index < 0) tabsToDelete.push(tab);
        	else{
                skus.splice(index, 1);
                productIds.splice(index, 1);
            }
		}

		for(var i = 0; i < tabsToDelete.length; i++) cmpPanel.remove(tabsToDelete[i], true);
		for(var i = 0; i < skus.length; i++){
            this.addTab(skus[i], productIds[i], !!data ? data[tabs.length] : []);
		}
        this.setActiveTab(0);
    },

	setActiveTabBySku: function(sku){
        var cmpPanel = Ext.getCmp(this.mainTabPanelId);
        var tabsToDelete = [];
        var tabs = cmpPanel.items.items;
        for(var i = 0; i < tabs.length; i++){
            var tab = tabs[i];
            if(tab.title == sku) {
                cmpPanel.setActiveTab(tab);
                break;
            }
        }
    },

    count: function(rows){
        var cplScore = 0;
        var tabsIndex = this.up().tabsIndex;
        for(var index in rows){
            if(rows[index].data.active){
                cplScore +=  parseInt(rows[index].data.param1);
            }
        }
        this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplScore').setValue( cplScore );
        if(cplScore>=100){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('6');
        }else if(cplScore>=80){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('5');
        }else if(cplScore>=60){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('4');
        }else if(cplScore>=40){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('3');
        }else if(cplScore>=20){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('2');
        }else if(cplScore<20){
            this.up().items.items[0].getCmpByName('products['+tabsIndex+'].cplRiskRating').setValue('1');
        }
    }

});
