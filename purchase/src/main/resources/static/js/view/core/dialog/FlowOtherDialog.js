Ext.define('App.FlowOtherDialog', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.FlowOtherDialog',

    constructor: function (conf) {
        this.onlyRead = conf.readOnly || false;
        this.listeners = { afterrender: function (e, eOpts) { e.setReadOnly(conf.readOnly);}};

        this.displayType = conf.displayType || 1;
        App.FlowOtherDialog.superclass.constructor.call(this, conf);
    },

    setDisplayType: function(value){
        this.displayType = value || 1;
    },

    onTriggerWrapClick: function (conf) {
        if (this.onlyRead) {
            return;
        }
        var selectedId = '';
        if (Ext.getCmp(this.frameId).getCmpByName(this.hiddenName)) {
            selectedId = Ext.getCmp(this.frameId).getCmpByName(this.hiddenName).getValue();
        }

        new FlowOtherDialogWin({
            scope: this,
            single: this.single ? this.single : false,
            fieldValueName: this.hiddenName,
            fieldTitleName: this.name,
            selectedId: selectedId,
            initValue: this.initValue || true,
            isFormField: true,
            displayType: this.displayType,
            filterVendor: this.filterVendor || false,
            fieldVendorIdName: this.fieldVendorIdName || '',
            meForm: Ext.getCmp(this.frameId),
            subcallback: this.subcallback || '',
            callback: function (ids, titles, rows) {
                this.meForm.getCmpByName(this.fieldTitleName).setValue(titles);
                this.meForm.getCmpByName(this.fieldValueName).setValue(ids);
                this.eventClose ? this.eventClose.call(this, ids, titles) : '';
                this.subcallback ? this.subcallback.call(this, ids, titles, rows) : '';
            }
        }, false).show();

    }
});



FlowOtherDialogWin = Ext.extend(HP.Window,{
    constructor : function(conf) {
        conf.moduleName = 'ProductCombination';
        conf.winId = 'FlowOtherDialogWinID';
        conf.mainGridPanelId = 'FlowOtherDialogWinGridPanelID';
        conf.searchFormPanelId= 'FlowOtherDialogWinSearchPanelID';
        conf.selectGridPanelId = 'FlowOtherDialogWinSelectGridPanelID';
        conf.treePanelId = 'FlowOtherDialogWinTreePanelId';
        conf.refresh = true;
        conf.clean = !!conf.isFormField ? conf.isFormField: false;
        conf.ok = true;
        conf.okFun = this.winOk;


        if(conf.displayType == 1){
            //新品申请
            conf.urlList = __ctxPath + 'flow/purchase/newproduct/list?type=1';
            conf.urlGet = __ctxPath + 'flow/purchase/newproduct/get';
            conf.title = _lang.FlowNewProduct.mTitle;

        }else if(conf.displayType==2) {
            //安检申请
            conf.urlList = __ctxPath + 'flow/inspection/complianceApply/list?type=1';
            conf.urlGet = __ctxPath + 'flow/inspection/complianceApply/get';
            conf.title = _lang.FlowComplianceArrangement.mTitle;
        }else if(conf.displayType==3) {
            //样品质检申请
            conf.urlList = __ctxPath + 'flow/inspection/flowsampleqc/list?type=1';
            conf.urlGet = __ctxPath + 'flow/inspection/flowsampleqc/get';
            conf.title = _lang.FlowSampleQc.mTitle;
        }else if(conf.displayType==4) {
            //产品证书申请
            conf.urlList = __ctxPath + 'flow/inspection/productCertification/list?type=1';
            conf.urlGet = __ctxPath + 'flow/inspection/productCertification/get';
            conf.title = _lang.FlowProductCertification.mTitle;
        }else if(conf.displayType==5) {
            //采购计划质检申请
            conf.urlList = __ctxPath + 'flow/purchase/plan/list?type=1';
            conf.urlGet = __ctxPath + 'flow/purchase/plan/get';
            conf.title = _lang.FlowPurchasePlan.mTitle;
        }else if(conf.displayType==6) {
            //采购计划质检申请
            conf.urlList = __ctxPath + 'flow/finance/flowSamplePayment/list?type=1';
            conf.urlGet = __ctxPath + 'flow/finance/flowSamplePayment/get';
            conf.title = _lang.FlowSamplePayment.mTitle;
        }else{
            //订单质检
            conf.urlList = __ctxPath + 'flow/inspection/flowOrderQC/list?type=1';
            conf.urlGet = __ctxPath + 'flow/inspection/flowOrderQC/get';
            conf.title = _lang.FlowOrderQualityInspection.mTitle;
        }

        //filter product by vendorId
        if(!!conf.filterVendor){
            var cmp = conf.meForm.getCmpByName(conf.fieldVendorIdName);
            if(conf.urlList.indexOf("?")>0){
                conf.urlList += '&vendorId=' + cmp.getValue();
            }else {
                conf.urlList += '?vendorId=' + cmp.getValue();
            }
        }

        Ext.applyIf(this, conf);
        this.initUI(conf);
        FlowOtherDialogWin.superclass.constructor.call(this, {
            id: conf.winId, scope: this,
            title: _lang.Flow.tSelector + ' ['+ conf.title +']',
            width: this.single ? 880 : 1080,
            region: 'center', layout: 'border',
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
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.ExchangeRate.tabListTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            fields: [
                'id','vendorId','vendorCnName','vendorEnName', 'status', 'flowStatus',
                'creatorId', 'creatorCnName', 'creatorEnName','departmentCnName','departmentEnName','createdAt','updatedAt','publishStatus'
            ],

            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180 },
                $renderOutputStatusColumns(),
                { header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
                    renderer: function (value) {
                        if (value == null) return $renderOutputColor('gray', _lang.TText.vNotSubmit);
                        var $flowStatus = _dict.flowStatus;
                        if ($flowStatus.length > 0 && $flowStatus[0].data.options.length > 0) {
                            return $dictRenderOutputColor(value, $flowStatus[0].data.options);
                        }
                    }
                },
                { header: _lang.TText.fCreatorId, dataIndex: 'creatorId', width: 60, hidden:true },
                { header: _lang.TText.fCreatorName, dataIndex: 'creatorCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fCreatorName, dataIndex: 'creatorEnName', width: 100, hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.TText.fDepartmentId, dataIndex: 'departmentId', width: 60, hidden:true },
                { header: _lang.TText.fDepartmentName, dataIndex: 'departmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fDepartmentName, dataIndex: 'departmentEnName', width: 160, hidden:curUserInfo.lang !='zh_CN'? false: true },
                { header: _lang.VendorDocument.fCnName, dataIndex: 'vendorCnName', width: 260},
                { header: _lang.VendorDocument.fEnName, dataIndex: 'vendorEnName', width: 260},
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, assignee: false}),

            itemdblclick:function(view, record, item, index, eventobj, obj){
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
            }
        });
        this.selectGridPanel = new HP.GridPanel({
            region : 'east',
            title: _lang.ProductDocument.tSelected,
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
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, sortable:false,},
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
            var ids = this.meForm.getCmpByName(this.fieldValueName).getValue();
            var names = this.meForm.getCmpByName(this.fieldTitleName).getValue();
            if(ids){
                var arrIds = ids.split(',');
                var arrNames = names.split(',');
                var selStore = this.selectGridPanel.getStore();
                for(var i=0; i<arrIds.length; i++){
                    if(curUserInfo.lang =='zh_CN'){
                        selStore.add({id: arrIds[i], name: arrNames[i]});
                    }else{
                        selStore.add({id: arrIds[i], name: arrNames[i]});
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
                names += rows[i].data.id;
            }
        }else{
            rows = Ext.getCmp(this.selectGridPanelId).getStore();
            for (var i = 0; i < rows.getCount(); i++) {
                if (i > 0) {ids += ',';names += ',';}
                ids += rows.getAt(i).data.id;
                names += rows.getAt(i).data.id;
            }
        }

        if (this.callback) {
            var me = this;
            var params = {id: ids, noReport: true};
            $postUrl({
                url: this.urlGet, maskTo: this.frameId, params: params, autoMessage:false,
                callback: function (response, eOpts) {
                    var json = Ext.JSON.decode(response.responseText);
                    me.callback.call(me, ids, names, json);
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