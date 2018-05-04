ProductCostView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.ProductCost.mTitle,
            moduleName: 'ProductCost',
            frameId : 'ProductCostView',
            mainGridPanelId : 'ProductCostViewGridPanelID',
            mainFormPanelId : 'ProductCostViewFormPanelID',
            searchFormPanelId: 'ProductCostViewSubFormPanelID',
            mainTabPanelId: 'ProductCostViewTabsPanelId',
            subGridPanelId : 'ProductCostViewSubGridPanelID',
            urlList: __ctxPath + 'archives/cost/list',
            urlSave: __ctxPath + 'archives/cost/save',
            urlDelete: __ctxPath + 'archives/cost/delete',
            urlGet: __ctxPath + 'archives/cost/get',
            refresh: true,
            edit: true,
            add: true,
            copy: true,
            del: true,
            editFun:this.editRow
        };

        this.initUIComponents(conf);
        ProductCostView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.centerPanel ]
        });
    },

    initUIComponents: function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            fieldItems:[
                {field:'Q-orderShippingPlanBusinessId-S-LK', xtype:'textfield', title:_lang.ProductCost.fShippingPlanId},
                { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel


        //grid panel
        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.ServiceProviderDocument.tabListTitle,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id', 'orderShippingPlanId', 'orderShippingPlanBusinessId', 'orderNumbers', 'creatorId','creatorCnName','creatorEnName', 'departmentId','departmentCnName','departmentEnName',
                'status', 'createdAt','updatedAt'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductCost.fShippingPlanId, dataIndex: 'orderShippingPlanBusinessId', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumbers', width: 180 },
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.ProductCostViewTabsPanel = new App.ProductCostViewTabs({
            region: 'south',
            mainTabPanelId: conf.mainTabPanelId,
            mainGridPanelId: conf.mainGridPanelId,
            split: true,
            height: '40%',
            items: []
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.ProductCostViewTabsPanel]
        });
    },// end of the init

    rowClick: function(record, item, index, e, conf) {
        var list = Ext.getCmp(conf.mainTabPanelId + '-0');
        list.loadData({
            url : conf.urlGet + '?id=' + record.data.id,
            loadMask:true,maskTo:conf.mainTabPanelId, success:function(response){
                var json = Ext.JSON.decode(response.responseText);

                //purchase order
                var purchaseOrders = Ext.getCmp(conf.mainTabPanelId + '-0');
                purchaseOrders.getStore().removeAll();
                if(!!json.data.costProducts){
                    purchaseOrders.getStore().add(json.data.costProducts);
                }

                //freight
                var freight = Ext.getCmp(conf.mainTabPanelId + '-1');
                freight.getStore().removeAll();
                if(!!json.data.costPorts){
                    freight.getStore().add(json.data.costPorts);
                }

                //destination
                var destination = Ext.getCmp(conf.mainTabPanelId + '-2');
                destination.getStore().removeAll();
                if(!!json.data.costChargeItems){
                    destination.getStore().add(json.data.costChargeItems);
                }

                //duty
                var duty = Ext.getCmp(conf.mainTabPanelId + '-3');
                duty.getStore().removeAll();
                if(!!json.data.costTariffs){
                    duty.getStore().add(json.data.costTariffs);
                }

                //cost
                var productCost = Ext.getCmp(conf.mainTabPanelId + '-4');
                productCost.getStore().removeAll();
                if(!!json.data.costProductCosts){
                    productCost.getStore().add(json.data.costProductCosts);
                }

                //attachment init
                Ext.getCmp(conf.mainTabPanelId).items.items[5].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

            }
        });

    },
    editRow : function(conf){
        // new ServiceProviderDocumentForm(conf).show();
        App.clickTopTab('ProductCostForm', conf);
        // new ProductCostForm(conf).show();
    }
});