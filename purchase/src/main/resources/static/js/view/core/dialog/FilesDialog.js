/**
 * FilesDialog 字段触发
 * FilesDialogWin　　弹出窗口
 * MyDocumentFormMultiGrid　多选列表
 * MyDocumentGridTabMultiGrid　多选列表
 */
Ext.define('App.FilesDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.FilesDialog',

    constructor: function (conf) {

        this.onlyRead = conf.readOnly || false;
        this.listeners = {
            afterrender: function (e, eOpts) {
                e.setReadOnly(conf.readOnly);
            }
        };
        App.FilesDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function (conf) {
        if (this.onlyRead) {
            return;
        }
        var selectedId = '';
        if (Ext.getCmp(this.formId).getCmpByName(this.hiddenName)) {
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }

        new FilesDialogWin({
            scope: this,
            single: this.single ? this.single : false,
            fileDefType: this.fileDefType,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId: selectedId,
            initValue: this.initValue || true,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback: function (ids, titles) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                this.eventClose ? this.eventClose.call(this, ids, titles) : '';
            }
        }, false).show();

    }
});


FilesDialogWin = Ext.extend(HP.Window, {
    constructor: function (conf) {

        conf.title = _lang.MyDocument.mFileSelector;
        conf.moduleName = 'MyDocument';
        conf.winId = 'FilesDialogWinID';
        conf.mainGridPanelId = 'FilesDialogWinGridPanelID';
        conf.mainFormPanelId = 'FilesDialogWinFormPanelID';
        conf.mainViewPanelId = 'FilesDialogWinViewPanelID';
        conf.mainTabsPanelId = 'FilesDialogWinTabsPanelID';
        conf.searchFormPanelId = 'FilesDialogWinSearchPanelID';
        conf.selectGridPanelId = 'FilesDialogWinSelectGridPanelID';
        conf.urlList = __ctxPath + 'mydoc/list' + (!!conf.fileDefType ? '?type=' + conf.fileDefType : '');
        conf.urlSave = __ctxPath + 'mydoc/save';
        conf.urlDelete = __ctxPath + 'mydoc/delete';
        conf.urlGet = __ctxPath + 'mydoc/get';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField : false;
        conf.ok = true;
        conf.okFun = this.winOk;

        conf.defHeight = this.height || 650;
        console.log('file');
        Ext.applyIf(this, conf);
        this.initUI(conf);
        FilesDialogWin.superclass.constructor.call(this, {
            id: conf.winId,
            scope: this,
            title: conf.title,
            width: this.single ? 880 : 1080,
            height: conf.defHeight,
            region: 'center',
            layout: 'border',
            tbar: Ext.create("App.toolbar", conf),
            items: [this.centerPanel, this.selectGridPanel]
        });

    },

    initUI: function (conf) {

        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            onlyKeywords: true
        });// end of searchPanel


        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            scope: this,
            id: conf.mainGridPanelId,
            forceFit: false,
            url: conf.urlList,

            fields: ['id', 'name', 'path', 'extension', 'note', 'categoryId', 'category.title', 'bytes', 'status', 'shared', 'createdAt', 'updatedAt', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 80, hidden: true},
                {header: _lang.MyDocument.fName, dataIndex: 'name', width: 260},
                {header: _lang.MyDocument.fExtension, dataIndex: 'extension', width: 50},
                {header: _lang.MyDocument.fCategoryId, dataIndex: 'categoryId', width: 100, hidden: true},
                {header: _lang.MyDocument.fCategory, dataIndex: 'category.title', width: 100},
                {header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width: 70, renderer: Ext.util.Format.fileSize},
                {
                    header: _lang.MyDocument.fShared, dataIndex: 'shared', width: 60,
                    renderer: function (value) {
                        var $shared = _dict.shared;
                        if ($shared.length > 0 && $shared[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $shared[0].data.options);
                        }
                    }
                },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),

            itemclick: function (obj, record, item, index, e, eOpts) {
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            },
            itemdblclick: function (obj, record, item, index, e, eOpts) {
                if (!conf.single) {
                    var selStore = this.scope.selectGridPanel.getStore();
                    if (selStore.getCount()) {
                        for (var i = 0; i < selStore.getCount(); i++) {
                            if (selStore.getAt(i).data.id == record.data.id) {
                                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedRecord);
                                return;
                            }
                        }
                    }
                    selStore.add(record.data);
                } else {
                    this.scope.winOk.call(this.scope);
                }
            },
            callback: function (obj, records) {
                if (this.selectedId && records.length) {
                    for (var i = 0; i < records.length; i++) {
                        if (records[i].data.id == this.selectedId) {
                            Ext.getCmp(conf.mainGridPanelId).getSelectionModel().select(records[i], true);
                        }
                    }
                }
                ;
            }

        });

        this.viewPanel = new HP.FormPanel({
            id: conf.mainViewPanelId,
            title: _lang.MyDocument.fSelectFile,
            fieldItems: [
                { xtype: 'container', cls: 'row', items: [
                    { field: 'main.note', xtype: 'displayfield', fieldLabel: _lang.MyDocument.fNote, allowBlank: false,cls: 'col-1'}
                ]},{ xtype: 'container', cls: 'row', items: [
                    {field: 'main.name', xtype: 'displayfield', fieldLabel: _lang.MyDocument.fName, cls: 'col-2'},
                    {field: 'main.extension',xtype: 'displayfield',fieldLabel: _lang.MyDocument.fExtension,cls: 'col-2'},
                ]},{ xtype: 'container', cls: 'row', items:[
                        { field: 'main.shared',xtype: 'dictfield',fieldLabel: _lang.MyDocument.fShared, code: 'document', codeSub: 'shared',cls: 'col-2'},
                        { field: 'main.status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls: 'col-2',
                            renderer: function (value) {
                            if (value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                            if (value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                        }
                    },
                ]},{ xtype: 'container', cls: 'row', items: [
                    { field: 'main.category.title', xtype: 'displayfield', fieldLabel: _lang.MyDocument.mTitleCate, cls: 'col-2'},
                    {field: 'main.bytes', xtype: 'displayfield', fieldLabel: _lang.MyDocument.fBytes, cls: 'col-2'}
                ]},{ xtype: 'container', cls: 'row', items: [
                    { field: 'main.creatorCnName',xtype: 'displayfield', fieldLabel: _lang.TText.fCreatorName, hidden: curUserInfo.lang == 'zh_CN' ? false : true,cls: 'col-2'},
                    { field: 'main.creatorEnName',xtype: 'displayfield', fieldLabel: _lang.TText.fCreatorName, hidden: curUserInfo.lang != 'zh_CN' ? false : true,cls: 'col-2'},
                    { field: 'main.departmentCnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, hidden: curUserInfo.lang == 'zh_CN' ? false : true, cls: 'col-2'},
                    { field: 'main.departmentEnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, hidden: curUserInfo.lang != 'zh_CN' ? false : true, cls: 'col-2'}
                ]}
            ]
        });

        var subConf = {};
        Ext.apply(subConf, conf);
        Ext.apply(subConf, {refresh: false, clean: false, ok: false, save: true, add: true, saveAs: true, del: true, addFun:this.addFun});

        this.formPanel = new HP.FormPanel({
            id: conf.mainFormPanelId,
            title: _lang.MyDocument.mFileUpload,
            region: 'center',
            tbar: Ext.create("App.toolbar", subConf),
            scope: this,
            fieldItems: [
                { field: 'id', xtype: 'hidden'},
                { xtype: 'container', cls: 'row', items: [{
                        field: 'files',xtype: 'filefield',fieldLabel: _lang.MyDocument.fName,buttonText: _lang.MyDocument.fSelectFile,cls: 'col-1',
                        labelWidth: 130,width: 150,listeners: {
                            scope: this,
                            change: function (obj, value) {
                                jQuery('#uploadFile').html('');
                            }
                        }
                    }]
                },
                { xtype: 'container', cls: 'row', items: [
                    {field: 'main.name', xtype: 'hidden'},
                    {field: 'main.categoryId', xtype: 'comboremote', fieldLabel: _lang.MyDocument.mTitleCate, cls: 'col-2', allowBlank: false,
                        url: __ctxPath + 'mydoccate/list'
                    },
                    {field: 'main.shared', xtype: 'dictcombo',fieldLabel: _lang.MyDocument.fShared, code: 'document', codeSub: 'shared', cls: 'col-2', value: '1'},
                    {field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value: '1', triggerAction: 'all', cls: 'col-2',
                        store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
                    },
                ]},
                { xtype: 'container', cls: 'row', items: [
                    { field: 'main.note', xtype: 'htmleditor', fieldLabel: _lang.MyDocument.fNote, height: 100, allowBlank: false, cls: 'col-1'},
                    { field: 'main.creatorId', xtype: 'hidden', value: curUserInfo.id}
                ]
                }
            ]
        });

        this.selectGridPanel = new HP.GridPanel({
            region: 'east',
            title: _lang.MyDocument.tSelected,
            id: conf.selectGridPanelId,
            scope: this,
            hidden: this.single ? true : false,
            width: 150,
            minWidth: 150,
            border: false,
            autoLoad: false,
            unbbar: true,
            collapsible: true,
            split: true,
            fields: ['id', 'name'],
            columns: [
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable: false},
                {header: _lang.MyDocument.fName, dataIndex: 'name', width: 260, sortable: false,},
            ],// end of columns
            itemdblclick: function (obj, record, item, index, e, eOpts) {
                this.getStore().remove(record);
            }
        });

        this.westFormPanel = new Ext.Panel({
            id: conf.winId + '-west',
            region: 'west',
            laylout: 'fit',
            width: 230,
            border: false,
            style: 'margin:0; padding:12px;',
            html: '<div id="uploadFile" class="uploadFile"></div>'
        });

        this.tabsPanel = Ext.create('Ext.tab.Panel', {
            id: conf.mainTabsPanelId,
            region: 'center',
            layout: 'border',
            plain: true,
            scope: this,
            activeTab: 0,
            bodyCls:'x-panel-body-gray',
            // header: {cls:'x-panel-header-gray'},
            cls:'x-panel-tabs-gray',
            items: [this.viewPanel, this.formPanel]
        });

        this.southPanel = new Ext.Panel({
            id: conf.winId + '-south',
            title: _lang.MyDocument.tFilePreview,
            layout: 'border',
            region: 'south',
            collapsible: true,
            split: true,
            height: 250,
            border: false,
            scope: this,
            items: [this.tabsPanel, this.westFormPanel]
        });

        this.mainPanel = new Ext.Panel({
            id: conf.winId + '-main',
            layout: 'border',
            region: 'center',
            border: false,
            scope: this,
            items: [this.gridPanel, this.southPanel]
        });

        this.centerPanel = new Ext.Panel({
            id: conf.winId + '-center',
            layout: 'border',
            region: 'center',
            border: false,
            scope: this,
            items: [this.searchPanel, this.mainPanel]
        });

        // init value
        if (this.initValue && this.fieldValueName) {
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if (ids) {
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for (var i = 0; i < arrIds.length; i++) {
                    selStore.add({id: arrIds[i], name: arrNames[i]});
                }
            }
        }
    },

    rowClick: function (record, item, index, e, conf) {
        var me = this;
        this.formPanel.loadData({
            url: conf.urlGet + '?id=' + record.data.id,
            root: 'data', preName: 'main', scope: this,
            success: function (response, options, fp) {
                if (response.responseText) {
                    var json = Ext.JSON.decode(response.responseText);

                    $_setByName(me.viewPanel, json.data, {preName: 'main', root: 'data'});

                    if(json.data.categoryId && Ext.getCmp(conf.mainFormPanelId)!=null){
                        Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.categoryId').setReadOnly(true);
                    }

                    var fileExt = json.data.extension.toLowerCase();
                    var strContent = '<img id="previewDiv" alt="' + _lang.TText.clickPreview + '" title="' + _lang.TText.clickPreview + '" ';
                    if (fileExt == 'jpg' || fileExt == 'gif' || fileExt == 'png' || fileExt == 'bmp') {
                        strContent += 'src="' + __ctxPath + json.data.path + '" width="290" height="200"';
                    } else if (fileExt == 'xls' || fileExt == 'xlsx') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/excel.png"';
                    } else if (fileExt == 'doc' || fileExt == 'docx') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/word.png"';
                    } else if (fileExt == 'ppt' || fileExt == 'pptx') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/ppt.png"';
                    } else if (fileExt == 'pdf' || fileExt == 'psd') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/pdf.png"';
                    } else if (fileExt == 'zip' || fileExt == 'rar') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/zip.png"';
                    } else if (fileExt == 'mp3' || fileExt == 'wma' || fileExt == 'wav') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/music.png"';
                    } else if (fileExt == 'rm' || fileExt == 'rmvb' || fileExt == 'wmv' || fileExt == 'avi'
                        || fileExt == 'ogg' || fileExt == 'mov' || fileExt == 'mp4') {
                        strContent += 'src="' + __ctxPath + 'images/filetype/video.png"';
                    } else {
                        strContent += 'src="' + __ctxPath + 'images/filetype/unknow.png"';
                    }
                    strContent += '/>';

                    jQuery('#uploadFile').html(strContent);
                    jQuery('#previewDiv').bind('click', function () {
                        options.scope.previewRs.call(options.scope);
                    });

                }
            }
        });
    },

    previewRs: function () {
        $editGridRs({
            grid: this.gridPanel,
            winForm: FilesPreviewDialog,
            scope: this
        });
    },
    addFun: function () {
        Ext.getCmp(this.mainFormPanelId).getCmpByName('main.categoryId').setReadOnly(false);
        Ext.getCmp(this.mainFormPanelId).form.reset();
    },

    winOk: function () {
        var ids = '';
        var names = '';
        var paths = '';
        var rows = {};
        if (this.single) {
            rows = Ext.getCmp(this.mainGridPanelId).getSelectionModel().selected.items;
            for (var i = 0; i < rows.length; i++) {
                if (i > 0) {
                    ids += ',';
                    names += ',';
                    paths += ',';
                }
                ids += rows[i].data.id;
                names += rows[i].data.name;
                paths += rows[i].data.path;
            }
        } else {
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {
                    ids += ',';
                    names += ',';
                    paths += ',';
                }
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.name;
                paths += rows.getAt(i).data.path;
            }
        }

        if (this.callback) {
            this.callback.call(this, ids, names, rows, paths);
        }
        Ext.getCmp(this.winId).close();
    },

    winClean: function () {
        var ids = '';
        var names = '';
        if (this.callback) {
            this.callback.call(this, ids, names);
        }
        Ext.getCmp(this.winId).close();
    }
});


Ext.define('App.MyDocumentFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.MyDocumentFormMultiGrid',

    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title: this.fieldLabel,
            moduleName: 'MyDocument',
            fieldValueName: this.valueName || 'main_documents',
            fieldTitleName: this.titleName || 'main_documentName'
        };
        conf.mainGridPanelId = this.mainGridPanelId || this.formId + '-MyDocumentMultiGrid';
        conf.mainFormPanelId = this.mainFormPanelId || this.formId + '-MyDocumentMultiForm';
        conf.subGridPanelId = this.subGridPanelId || this.mainGridPanelId + '-MyDocumentMultiGridPanelID';
        conf.subFormPanelId = this.subFormPanelId || this.mainGridPanelId + '-MyDocumentMultiFormPanelID';
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 200;
        conf.noTitle = this.noTitle || false,

            this.initUIComponents(conf);

        App.MyDocumentFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: conf.defWidth,
            bodyCls:'x-panel-body-gray',
            items: [this.subGridPanel]
        });
    },

    setValue:function (values) {
        //attachment init
        var cmpAtta = this.subGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(index in values){
                var attachments = {};
                attachments = values[index];
                Ext.applyIf(attachments, values[index].document);
                attachments.id= values[index].documentId;
                if(!!attachments.category) attachments['category.title']= attachments.category.title;
                cmpAtta.add(attachments);
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
        }
        ];

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: conf.noTitle ? '' : _lang.TText.fAttachmentList,
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
            fields: ['id', 'name', 'path', 'extension', 'note', 'category.title', 'categoryId', 'bytes', 'updatedAt', 'creatorId', 'creatorCnName', 'creatorEnName', 'departmentId', 'departmentCnName', 'departmentEnName'],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: this.readOnly ? 65: 120, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this, hideMode:'display',
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.MyDocument.mFilePreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowDownload', btnCls: 'fa-download', tooltip: _lang.MyDocument.mFileDownload,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
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
                {header: 'ID', dataIndex: 'id', width: 40, hidden: true},
                {header: _lang.MyDocument.fName, dataIndex: 'name', width: 260},
                {header: _lang.MyDocument.fCategory, dataIndex: 'category.title', width: 120},
                {header: _lang.MyDocument.fExtension, dataIndex: 'extension', width: 50},
                {header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width: 70, renderer: Ext.util.Format.fileSize},
                {header: _lang.MyDocument.fNote, dataIndex: 'note', width: 200}
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort: false, createdAt: false, status: false,assignee:false}),
        });
    },

    btnRowPreview: function () {},
    btnRowDownload: function () {},
    btnRowEdit: function () {},
    btnRowRemove: function () {},
    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: this.subGridPanel,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
            case 'btnRowDownload':
                window.open(__ctxPath+'mydoc/download?fileId=' + record.data.id);
                break;
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new FilesDialogWin({
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
                new FilesDialogWin({
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


Ext.define('App.MyDocumentGridTabGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.MyDocumentGridTabGrid',

    constructor: function (conf) {
        Ext.applyIf(this, conf);
        var conf = {
            moduleName: 'MyDocument',
        };

        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-MyDocumentGridTabGrid';
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-MyDocumentFormTabGrid';
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || '100%';
        conf.noTitle = this.noTitle || false;
        conf.title = this.title;
        conf.winForm = this.winForm || '';
        this.initUIComponents(conf);

        App.MyDocumentGridTabGrid.superclass.constructor.call(this, this.subGridPanel);
    },

    setValue:function (values) {
        var cmpAtta = this.subGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(index in values){
                var attachments = {};
                attachments = values[index];
                Ext.applyIf(attachments, values[index].document);
                attachments.id= values[index].documentId;
                cmpAtta.add(attachments);
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
            title: conf.noTitle ?  conf.title : _lang.TText.fAttachmentList,
            width: conf.defWidth,
            height: conf.defHeight,
            url: conf.historyListUrl,
            bbar: false,
            scope: this,
            bodyCls:'x-panel-body-gray',
            header: conf.noTitle ? false : {cls: 'x-panel-header-gray'},
            tools: conf.noTitle ? false : tools,
            autoLoad: false,
            forceFit: true,
            rsort: false,
            sorters: [{property: 'createdAt', direction: 'DESC'}],
            fields: ['id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width:55, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }, {
                        iconCls: 'btnRowDownload', btnCls: 'fa-download', tooltip: _lang.MyDocument.mFileDownload,
                        callback: function (grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.MyDocument.fName, dataIndex: 'name', width:260 },
                { header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
                { header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
                { header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
        });
    },

    btnRowPreview: function () {},
    btnRowDownload: function () {},
    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
            case 'btnRowDownload':
                window.open(__ctxPath+'mydoc/download?fileId=' + record.data.id);
                break;
        }
    }

});