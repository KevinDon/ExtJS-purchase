Ext.define('App.OrderDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.OrderDialog',

    constructor : function(conf) {

        this.onlyRead = conf.readOnly || false;
    	this.listeners = {afterrender: function( e, eOpts ){e.setReadOnly(conf.readOnly);}};
    	App.OrderDialog.superclass.constructor.call(this, conf);
    },
    onTriggerWrapClick: function(conf) {
    	if (this.onlyRead) {return;}

        var selectedId = '';
        if(Ext.getCmp(this.formId).getCmpByName(this.hiddenName)){
            selectedId = Ext.getCmp(this.formId).getCmpByName(this.hiddenName).getValue();
        }
        new OrderDialogWin({
            scope:this,
            single:this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            subcallback: this.subcallback ? this.subcallback: '',
            type: !! this.type ? this.type : '',
            selectedId : selectedId,
            formal : this.formal ? this.formal: false,
            vendorId : Ext.getCmp(this.formId).vendorId ?  Ext.getCmp(this.formId).vendorId : '',
            filter: this.filter,
            isFormField: true,
            meForm: Ext.getCmp(this.formId),
            callback: function(ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                if(this.subcallback){
					this.subcallback.call(this, rows);
				}
            }}, false).show();
    }
});


OrderDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.title = _lang.FlowOrderQualityInspection.tabOrderDetail;
        conf.moduleName = 'FlowOrderQualityInspection';
        conf.winId = 'OrderDialogWinID';
        conf.mainGridPanelId = 'OrderDialogWinGridPanelID';
        conf.searchFormPanelId= 'OrderDialogWinSearchPanelID';
        conf.selectGridPanelId = 'OrderDialogWinSelectGridPanelID';
        conf.treePanelId = 'OrderDialogWinTreePanelId';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;
        conf.urlGet =  __ctxPath + 'purchase/purchasecontract/get';

        Ext.applyIf(this, conf);
        if(conf.type==1){
            //清关申请调用，过滤条件采购合同、合同定金通过且没做过清关申请
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=1';
        } else if(conf.type==2){
            //差额退款调用，过滤条件采购合同、ASN通过，未做过差额退款
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=2';
        }else if(conf.type==3){
            //发货计划调用，过滤条件是合同定金通过且没发货过的记录
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=3';
        }else if(conf.type==4){
            //费用登记调用，费用支付没有通过的正式合同订单
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=4';
            this.urlGet = __ctxPath + 'purchase/purchasecontract/getForFeeRegister';
        }else if(conf.type==5){
            //订单质检调用，没做过订单质检、没有做过清关正式合同订单
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=5';
        }else if(conf.type==6){
            //合同定金调用，没做过合同定金的正式合同订单
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=6';
        }else if(conf.type==7){
            //采购合同所有正式数据
            conf.urlList = __ctxPath + 'purchase/purchasecontract/list?type=7';
        }else{
            //流程中的所有数据，过滤条件为VendorID，没有venerID默认所有数据
            conf.urlList = __ctxPath + 'flow/purchase/purchasecontract/list'
        }


        //filter product by vendorId
        if(!!conf.vendorId){
                if(conf.urlList.indexOf("?")>0){
                    conf.urlList += '&vendorId=' + conf.vendorId;
                }else {
                    conf.urlList += '?vendorId=' + conf.vendorId;
                }
        }
        this.initUI(conf);

        OrderDialogWin.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :_lang.FlowOrderQualityInspection.tabOrderDetail,
            width: this.single ? 1080 : 1280,
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

        //filter
        var url = conf.urlList;
        if(!!this.filter){
            url += "?" + Ext.urlEncode(this.filter);
        }
        this.selectedData = [];

        this.gridPanel = new HP.GridPanel({
            region : 'center',
            id : conf.mainGridPanelId,
            title:_lang.FlowOrderQualityInspection.tabOrderDetail,
            //collapsible: true,
            //split: true,
            scope: this,
            forceFit: false,
            border: false,
            //width: '85%',
            minWidth: 980,
            autoScroll: true,
            url : conf.urlList,
            fields: [
                'id','orderNumber','orderTitle',
                'originPortId', 'destinationPortId', 'readyDate',
                'currency','totalPriceAud','totalPriceRmb','totalPriceUsd','rateAudToRmb','rateAudToUsd',
                'depositAud','depositRmb','depositUsd',
                'creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','createdAt',
                'departmentEnName',
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 100, hidden: true },
                { header:_lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 200, },
                { header: _lang.FlowOrderQualityInspection.fOrderName, dataIndex: 'orderTitle', width: 200, },
                { header: _lang.FlowServiceInquiry.fOrigin, dataIndex: 'originPortId', width: 100,
                    renderer: function(value){
                        var $port = _dict.origin;
                        if($port.length>0 && $port[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $port[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowServiceInquiry.fDestination, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $port = _dict.destination;
                        if($port.length>0 && $port[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $port[0].data.options);
                        }
                    }
                },
                { header: _lang.FlowPurchaseContract.fReadyDate, dataIndex: 'readyDate', width: 140, format: curUserInfo.dateFormat},
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.NewProductDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'rateAudToRmb','rateAudToUsd')},
                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },
            ],// end of columns
            appendColumns: $groupGridCreatedColumnsForFlow({
                status:false,
                updatedAt:false,
                flowStatus:false,
                flowHold:false,
                startTime:false,
                endTime:false,
                assignee:false,
            }),

            itemdblclick : function(obj, record, item, index, e, eOpts){
                if(! conf.single){
                    var selStore = this.scope.selectGridPanel.getStore();
                    if(conf.checkDestinationPort){
                        conf.checkDestinationPort.call(conf.scope, record.data.destinationPortId);
                    }
                    if(selStore.getCount()){
                        for (var i = 0; i < selStore.getCount(); i++) {
                            //发货计划下的判断
                            if(conf.type == 3){
                                if (selStore.getAt(i).data.destinationPortId != record.data.destinationPortId) {
                                    Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.selectedDestinationPortRecord);
                                    return;
                                }
                            }
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
                };
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
            title: _lang.FlowOrderQualityInspection.tSelected,
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
            fields : ['id','orderNumber','destinationPortId'],
            columns : [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true, sortable:false },
                { header: _lang.FlowOrderQualityInspection.fOrderNumber, dataIndex: 'orderNumber', width: 90, sortable:false,},
                { header: _lang.FlowServiceInquiry.fDestination, dataIndex: 'destinationPortId', width: 90, sortable:false,hidden: true,},
            ],// end of columns
            itemdblclick : function(obj, record, item, index, e, eOpts){
                this.getStore().remove(record);
                var selectedData = [];
                for(var i = 0; i < this.scope.selectedData.length; i++){
                    var data = this.scope.selectedData[i];
                    if(data.raw.id != record.data.id) {
                        selectedData.push(data);
                    }
                }
                console.log(selectedData);
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
                names += rows[i].data.orderNumber;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.orderNumber;
            }
        }

        if (this.callback) {
            var win = Ext.getCmp(this.winId);
            var me = this;
            var params = {id: ids};
            $postUrl({
                url: this.urlGet, maskTo: this.frameId, params: params, autoMessage:false,
                callback: function (response, eOpts) {
                    var json = Ext.JSON.decode(response.responseText);
                    me.callback.call(me, ids, names, json, win.selectedData);
                }
            });
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


Ext.define('App.OrderProductFormMultiGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.OrderProductFormMultiGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title: this.fieldLabel,
            moduleName: 'Order',
            fieldValueName: this.valueName || 'main_order',
            fieldTitleName: this.titleName || 'main_orderName'
        };

        conf.mainGridPanelId= this.mainGridPanelId;
        conf.mainFormPanelId= this.mainFormPanelId;
        conf.subGridPanelId = (!!this.mainGridPanelId? this.mainGridPanelId : this.farmeId) + '-OrderProductFormMultiGrid' + Ext.id();
        conf.subFormPanelId = (!!this.mainFormPanelId? this.mainFormPanelId : this.farmeId) + '-OrderProductFormMultiForm' + Ext.id();
        conf.defWidth = this.defWidth || '100%';
        conf.defHeight = this.defHeight || 260;
        conf.noTitle = this.noTitle || false;
        conf.productType = this.productType || 1;

        this.initUIComponents(conf);

        App.ProductFormMultiGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.formGridPanel ],
        });
    },

    vendorId: '',

    setValue:function (values) {
        var cmpAtta = this.formGridPanel.getStore();
        cmpAtta.removeAll();
        if(!!values && values.length>0){
            for(var i = 0; i < values.length; i++){
                var product = {};
                Ext.applyIf(product, values[i].product);
                Ext.apply(product, values[i].product.prop);

                for(var index in values[i]){
                    if(typeof values[i][index] != 'object'){
                        product[index] = values[i][index];
                    }
                }
                product.sku = values[i].product.sku;
                cmpAtta.add(product);
            }
        }
    },

    initUIComponents: function(conf){
        var tools = [{
            type:'plus', tooltip: _lang.TButton.insert, scope: this, hidden:this.readOnly,
            handler: function(event, toolEl, panelHeader) {
                this.conf = conf;
                this.onRowAction.call(this);
            }},{
            type:'minimize', tooltip: _lang.TButton.minimize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(conf.defHeight);
                this.formGridPanel.setHeight(conf.defHeight-3);
            }},{
            type:'maximize', tooltip: _lang.TButton.maximize, scope: this,
            handler: function(event, toolEl, panelHeader) {
                this.setHeight(700);
                this.formGridPanel.setHeight(697);
            }}
        ];

        this.formGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.NewProductDocument.tabListTitle,
            forceFit: false,
            width: 'auto',
            height:conf.defHeight-3,
            url: '',
            bbar: false,
            tools: tools,
            autoLoad: false,
            rsort: false,
            edit: !this.readOnly,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [
                'id','cartons','currency','factoryCode','orderQty','orderValueAud','orderValueRmb','orderValueUsd',
                'priceAud','priceRmb','priceUsd','rateAudToRmb','rateAudToUsd',
                'sku','name','barcode','categoryName','categoryId','combined','ean','newProduct','model',
                'masterCartonW','masterCartonH','masterCartonCbm','masterCartonGrossWeight','masterCartonCubicWeight',
                'innerCartonL','innerCartonW','innerCartonH','innerCartonCbm','innerCartonGrossWeight','innerCartonCubicWeight',
                'riskRating','productLink','keywords','style','pcsPerCarton','pcsPerPallets','moq','leadTime','length', 'width','height', 'cbm','cubicWeight',
                'netWeight', 'masterCartonL',
                'creatorName','departmentName'
            ],
            columns: [
                { header : _lang.TText.rowAction, xtype: 'rowactions', width:60, locked: true, hidden:this.readOnly,
                    keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                    actions: [{
                        iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.edit,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    },{
                        iconCls: 'btnRowRemove', btnCls: 'fa-trash', tooltip: _lang.TButton.del,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 180, locked: true},
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200 },
                { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 180 },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120,sortable: false},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 70 ,sortable: true,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 70 ,sortable: true,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                //汇率
                { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this, 'rateAudToRmb', 'rateAudToUsd')},
                { header: _lang.OrderDocument.fPrice,
                    columns: new $groupPriceColumns(this, 'priceAud','priceRmb','priceUsd', null, {edit:false})
                },
                { header: _lang.OrderDocument.fOrderQty, dataIndex: 'orderQty', width: 60},
                { header: _lang.OrderDocument.fOrderValue,
                    columns: new $groupPriceColumns(this, 'orderValueAud','orderValueRmb','orderValueUsd', null, {edit:false})
                },

                //规格
                { header: _lang.ProductDocument.fProductSpecification, columns:[
                    { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
                ]},

                //外箱
                { header: _lang.ProductDocument.fMasterCartonSpecification, columns:[
                    { header: _lang.ProductDocument.fLength, dataIndex: 'masterCartonL', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'masterCartonW', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'masterCartonH', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'masterCartonCbm', width: 60,sortable: true, },
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'masterCartonCubicWeight', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'masterCartonGrossWeight', width: 60,sortable: true, },
                ]},

                //内箱
                { header: _lang.ProductDocument.fInnerCartonSpecification, columns:[
                    { header: _lang.ProductDocument.fLength, dataIndex: 'innerCartonL', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'innerCartonW', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'innerCartonH', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'innerCartonCbm', width: 60,sortable: true, },
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'innerCartonCubicWeight', width: 60 ,sortable: true,},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'innerCartonGrossWeight', width: 60,sortable: true, },
                ]},

                //包装
                { header: _lang.ProductDocument.fPackage, columns:[
                    { header: _lang.ProductDocument.fPackageCode, dataIndex: 'style', width: 120 ,sortable: true, },
                    { header: _lang.ProductDocument.fPcsPerCarton, dataIndex: 'pcsPerCarton', width: 60 ,sortable: true, },
                    { header: _lang.ProductDocument.fPcsPerPallets, dataIndex: 'pcsPerPallets', width: 60 ,sortable: true, }
                ]},

                { header: _lang.NewProductDocument.fCreator, dataIndex: 'creatorName', width: 100 },
                { header: _lang.NewProductDocument.fDepartmentId, dataIndex: 'departmentName', width: 100 }
            ]// end of columns
        });
    },

    btnRowEdit: function(){},
    btnRowRemove: function() {},
    onRowAction: function(grid, record, action, idx, col, conf) {
        conf = conf || this.conf;

        switch (action) {
            case 'btnRowEdit' :
                var selectedId = record.data.id;
                new PurchaseQuotationProductDialogWin({
                    single:true,
                    selectedId : selectedId,
                    vendorId: this.vendorId,
                    productType: conf.productType || '1',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subGridPanelId),
                    callback:function(ids, titles, result) {
                        if(result.length>0 && !$checkGridRowExist(this.meGrid.getStore(), result[0].data.id)){
                            this.meGrid.getStore().insert(idx, result[0].data);
                            this.meGrid.getStore().removeAt(idx+1);
                        }
                    }}, false).show();
                break;

            case 'btnRowRemove' :
                Ext.getCmp(conf.formGridPanelId).store.remove(record);
                break;

            default :
                new PurchaseQuotationProductDialogWin({
                    single:false,
                    selectedId : '',
                    vendorId: this.vendorId,
                    productType: conf.productType || '1',
                    meForm: Ext.getCmp(conf.mainFormPanelId),
                    meGrid: Ext.getCmp(conf.subGridPanelId),
                    callback:function(ids, titles, result, selectedData) {
                        if(result.data.items.length>0){
                            var items = selectedData;
                            for(index in items){
                                if(items[index] != undefined && ! $checkGridRowExist(this.meGrid.getStore(), items[index].raw.id)){
                                    var product = {};
                                    Ext.apply(product, items[index].raw.prop);
                                    Ext.applyIf(product, items[index].raw);
                                    product.sku =  items[index].raw.sku;
                                    product.id =  items[index].raw.id;
                                    this.meGrid.getStore().add(product);
                                }
                            }
                        }
                    }}, false).show();

                break;
        }
    }
});