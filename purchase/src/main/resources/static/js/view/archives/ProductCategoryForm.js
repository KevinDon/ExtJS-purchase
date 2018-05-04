ProductCategoryForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        var conf = {
            winId : 'ProductCategoryFormWinID',
            title : _lang.ProductCategory.mTitle,
            moduleName: 'ProductCategory',
            viewPanelId : 'ProductCategoryViewPanelID',
            treePanelId : 'ProductCategoryViewTreePanelId',
            formPanelId : 'ProductCategoryViewFormPanelId',
            urlList: __ctxPath + 'archives/product-category/list',
            urlSave: __ctxPath + 'archives/product-category/save',
            urlDelete: __ctxPath + 'archives/product-category/delete',
            urlGet: __ctxPath + 'archives/product-category/get',
            actionName: this.action,
            treeRefresh: true,
            treeSave: true,
            cancel: true,
            saveFun: this.saveFun,
        };

        this.initUIComponents(conf);
        ProductCategoryForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : _lang.ProductCategory.mTitle + ' - '+ (!Ext.isEmpty(this.record.id) ? _lang.TButton.edit: _lang.TButton.add),
            tbar: Ext.create("App.toolbar", conf),
            width : 500, height : 350,
            items : this.formPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;
        this.formPanel = new HP.FormPanel({
            id : conf.formPanelId,
            fieldItems : [
                { field: 'id',	xtype: 'hidden', value : this.id == null ? '' : this.id},
                { field: 'main.cnName', xtype: 'textfield', fieldLabel: _lang.TText.fCnName, allowBlank: false},
                { field: 'main.enName', xtype: 'textfield', fieldLabel: _lang.TText.fEnName, allowBlank: false},
                { field:'main.omsId', xtype:'hidden'},
                { field:'main.omsCode', xtype:'hidden'},
                { field: 'main.omsName', xtype: 'OMSCategoryDialog', fieldLabel : _lang.ProductCategory.fOmsCategory,
                    formId: conf.formPanelId, hiddenName: 'main.omsId', single: true,
                    subcallback: function (rows) {
                        if(!rows) return;
                        var data = rows[0].raw;
                        scope.formPanel.getCmpByName('main.omsCode').setValue(data.category_code);
                    }
                },
                { field:'main.creatorId', xtype:'hidden'},
                { field: 'main.creatorName', xtype: 'UserDialog', fieldLabel : _lang.VendorDocument.fBuyerName,
                    formId: conf.formPanelId, hiddenName: 'main.creatorId', single: true
                },
                { field: 'main.status', xtype: 'radiogroup', fieldLabel: _lang.TText.fStatus, value: '2',
                    data: [['1',  _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
                },
                { field: 'main.leaf', xtype: 'radiogroup', fieldLabel : _lang.TText.fLeaf, value: '1',
                    data: [['0', _lang.TText.vNo], ['1',  _lang.TText.vYes]]
                }

            ]
        });
        // 加载表单对应的数据
        if (!Ext.isEmpty(this.record.id)) {
            this.formPanel.loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, scope: this
            });
        }

    },

    saveFun: function(){
        var params = {act: this.actionName ? this.actionName: 'save'};

        $postForm({
            formPanel: Ext.getCmp(this.mainFormPanelId),
            scope: this,
            url: this.urlSave,
            params: params,
            callback: function(fp, action) {
                Ext.getCmp(this.treePanelId).getStore().reload();
                Ext.getCmp(this.winId).close();
            }
        });
    }
});