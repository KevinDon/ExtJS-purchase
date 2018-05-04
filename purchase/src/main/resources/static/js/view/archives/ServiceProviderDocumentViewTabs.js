Ext.define('App.ServiceProviderDocumentViewTabs', {
    extend : 'Ext.tab.Panel',
    alias : 'widget.ServiceProviderDocumentViewTabs',

    constructor : function(conf) {
        Ext.applyIf(this, conf);
        conf.items = [];

        conf.items[0] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-0',
            title: _lang.Contacts.tabListTitle,
            scope : this,
            forceFit : false,
            rsort: false,
            autoLoad : false,
            bbar: false,
            fields: [ 'id','vendor.cnName','vendor.enName','vendorId','cnName','enName', 'gender','title', 'email','department', 'qq','skype','extension','phone','wechat','mobile','shared','remark','status',
                'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 60, hidden: true },
                { header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendor.cnName', width: 260, hidden: true },
                { header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendor.enName', width: 260, hidden: true },
                { header:_lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 60, hidden: true },
                { header: _lang.Contacts.fCnName, dataIndex: 'cnName', width: 100},
                { header: _lang.Contacts.fEnName, dataIndex: 'enName', width: 120},
                { header: _lang.Contacts.fGender, dataIndex: 'gender', width: 40,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
                    }
                },
                { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
                { header: _lang.Contacts.fQq, dataIndex: 'qq', width: 140 },
                { header: _lang.Contacts.fSkype, dataIndex: 'skype', width: 140 },
                { header: _lang.Contacts.fPhone, dataIndex: 'phone', width: 100 },
                { header: _lang.Contacts.fExtension, dataIndex: 'extension', width: 100 },
                { header: _lang.Contacts.fWechat, dataIndex: 'wechat', width: 140 },
                { header: _lang.Contacts.fMobile, dataIndex: 'mobile', width: 100 },
                { header: _lang.Contacts.fRemark, dataIndex: 'remark', width: 200 },

            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
        });

        conf.items[1] = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

        conf.items[2] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-2',
            title : _lang.ServiceProviderDocument.fOriginPort,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: ['id','originPortCnName', 'originPortEnName'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.ServiceProviderDocument.fOriginPort, dataIndex: 'originPortCnName', width:200 },
                { header: _lang.ServiceProviderDocument.fOriginPort, dataIndex: 'originPortEnName', width:260 },
            ],// end of columns
        });

        conf.items[3] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-3',
            title : _lang.ServiceProviderDocument.fChargeItem,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: ['id','itemId','itemCnName','itemEnName','unitId','unitCnName','unitEnName'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.ServiceProviderDocument.fChargeItem, dataIndex: 'itemId', width:120, hidden:true},
                { header: _lang.ServiceProviderDocument.fChargeItem, dataIndex: 'itemCnName', width:200},
                { header: _lang.ServiceProviderDocument.fChargeItem, dataIndex: 'itemEnName', width:260},
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitId', width:100,
                    renderer: function(value){
                        if(_dict.chargeUnit.length>0 && _dict.chargeUnit[0].data.options.length>0){
                            return $dictRenderOutputColor(value, _dict.chargeUnit[0].data.options);
                        }
                    }
                },
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitCnName', width:100, hidden: curUserInfo.lang == 'zh_CN'? true: false },
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitEnName', width:100, hidden: curUserInfo.lang != 'zh_CN'? true: false },
            ],// end of columns
        });

        // conf.items[4] = new HP.GridPanel({
        //     id : conf.mainTabPanelId+'-4',
        //     title: _lang.ProductDocument.tabOrderListTitle,
        //     forceFit: false,
        //     autoLoad: false,
        //     bbar: false,
        //     rsort: false,
        //     fields: [
        //         'id','orderNumber','orderTitle','currency','totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb','rateAudToUsd',
        //         'depositAud','depositRmb','depositUsd'
        //     ],
        //     columns: [
        //         { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
        //         { header:_lang.ProductDocument.fBusinessId, dataIndex: 'orderNumber', width: 90, },
        //         { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },
        //
        //         { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
        //             renderer: function(value){
        //                 var $currency = _dict.currency;
        //                 if($currency.length>0 && $currency[0].data.options.length>0){
        //                     return $dictRenderOutputColor(value, $currency[0].data.options);
        //                 }
        //             }
        //         },
        //         { header: _lang.NewProductDocument.fPrice,
        //             columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
        //         },
        //         { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
        //         { header: _lang.ProductDocument.fDeposit,
        //             columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
        //         },
        //     ],// end of columns
        // });

        conf.items[4] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-4',
            title : _lang.TText.fImageList,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: [
                'id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName',
                'departmentId','departmentCnName','departmentEnName'
            ],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width: 35, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
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
            // end of columns
        });

        conf.items[5] = Ext.create("Ext.Panel",{
            id : conf.mainTabPanelId+'-5',
            title : _lang.ServiceProviderCategory.tabPaymentTerms,
            layout: 'fit',
            bodyPadding: 5,
            html: ''
        });
        conf.items[5].doLayout();
        
        try {
            conf.items[6] = new App.ArchivesHistoryTabGrid({
                historyListUrl: 'archives/service_provider/history',
                historyGetUrl: 'archives/service_provider/historyget',
                historyConfirmUrl: 'archives/service_provider/historyconfirm',
                historyDelUrl: 'archives/service_provider/historydelete',
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.ArchivesHistory.mTitle,
                winForm: ServiceProviderDocumentForm,
                noTitle: true
            });
        }catch (e){
            console.log(e);
        }
        
       

        try {
            App.ServiceProviderDocumentViewTabs.superclass.constructor.call(this, {
                id : this.mainTabPanelId,
                title : conf.fieldLabel,
                autoWidth : true,
                autoHeight : true,
                autoScroll : true,
                cls:'tabs'
            });

            this.initUIComponents(conf);
        } catch (e) {
            console.log(e);
        }
    },

    initUIComponents : function(conf) {
        var cmpPanel = Ext.getCmp(this.mainTabPanelId);
        for (var i = 0; i < conf.items.length; i++) {
            cmpPanel.add(conf.items[i]);
        }
        cmpPanel.setActiveTab(conf.items[0]);
    },

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
        }
    }
});
