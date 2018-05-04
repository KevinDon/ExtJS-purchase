MyAgentSettingForm = Ext.extend(HP.Window, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        this.isReadOnly = this.action != 'add' && this.action != 'copy' && this.record.status>0 || this.isConfirm ? true: false;
        var conf = {
            winId : 'MyAgentSettingForm',
            title : _lang.MyAgentSetting.mTitle,
            moduleName: 'MyAgentSetting',
            frameId : 'MyAgentSettingView',
            mainGridPanelId : 'MyAgentSettingGridPanelID',
            mainFormPanelId : 'MyAgentSettingFormPanelID',
            searchFormPanelId: 'MyAgentSettingSearchPanelID',
            mainTabPanelId: 'MyAgentSettingTbsPanelId',
            subGridPanelId : 'MyAgentSettingSubGridPanelID',
            urlList: __ctxPath + 'flow/myagentsetting/list',
            urlSave: __ctxPath + 'flow/myagentsetting/save',
            urlDelete: __ctxPath + 'flow/myagentsetting/delete',
            urlGet: __ctxPath + 'flow/myagentsetting/get',
            //refresh: true,
            close: true,
            save: true,
            //add: true,
            //saveAs: true,
            //del: true,
            //addFun: this.addRow,
            saveFun: this.saveFun,
        };

        this.initUIComponents(conf);
        MyAgentSettingForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title :  conf.title,
            tbar:  Ext.create("App.toolbar", conf),
            width : 780, height : 780,
            items : this.formPanel
        });


    },

    initUIComponents: function(conf) {

        this.formPanel = new HP.FormPanel({
            id: conf.mainFormPanelId,
            title: _lang.MyAgentSetting.tabFormTitle,
            region: 'center',
            scope: this,
            fieldItems: [
                { field: 'id',	xtype: 'hidden'},
                { field: 'main.toUserId', xtype: 'hidden'},
                { field : curUserInfo.lang =='zh_CN'? 'main.toUserCnName':'main.toUserEnName', xtype : 'UserDialog', fieldLabel : _lang.MyAgentSetting.fToUserName,
                    formId : conf.mainFormPanelId, hiddenName : 'main.toUserId', depId:'main.toDepartmentId', inDep: true,
                    depName:curUserInfo.lang =='zh_CN'? 'main.toDepartmentCnName': 'main.toDepartmentEnName', allowBlank : false, single:true
                },
                { field: 'main.toDepartmentId', xtype: 'hidden'},
                { field: curUserInfo.lang =='zh_CN'? 'main.toDepartmentCnName': 'main.toDepartmentEnName', xtype: 'displayfield', fieldLabel : _lang.MyAgentSetting.fToDepartmentName},

                { field: 'main.fromUserId', xtype: 'hidden', value: curUserInfo.id},
                { field : curUserInfo.lang =='zh_CN'? 'main.fromUserCnName':'main.fromUserEnName', xtype : 'UserDialog', fieldLabel : _lang.MyAgentSetting.fFromUserName,
                    formId : conf.mainFormPanelId, hiddenName : 'main.fromUserId', depId:'main.fromDepartmentId', depName:'main.fromDepartmentName',
                    single:true, hidden: true
                },
                { field: 'main.fromDepartmentId', xtype: 'hidden'},
                { field: 'main.fromDepartmentName', xtype: 'displayfield', fieldLabel : _lang.MyAgentSetting.fFromDepartmentName, hidden: true},
                { field: 'main.fromTime', xtype: 'datetimefield', fieldLabel : _lang.MyAgentSetting.fFromTime, format: curUserInfo.dateFormat, scope: this, allowBlank : false,
                    onChange: function(value){
                        var totime = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.toTime').getValue();
                        if(totime && Ext.Date.format(value, 'Y-m-d H:i:s') > Ext.Date.format(totime, 'Y-m-d H:i:s')){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.toTime').setValue('');
                        }else if(totime){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_days').setValue(
                                this.scope.dateDiff(Ext.Date.format(value, 'Y-m-d H:i:s'), Ext.Date.format(totime, 'Y-m-d H:i:s'))
                            );
                        }
                    }
                },
                { field: 'main.toTime', xtype: 'datetimefield', fieldLabel : _lang.MyAgentSetting.fToTime, format: curUserInfo.dateFormat, scope: this, allowBlank : false,
                    onChange: function(value){
                        var fromTime = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.fromTime').getValue();
                        if(fromTime && Ext.Date.format(value, 'Y-m-d H:i:s') <= Ext.Date.format(fromTime, 'Y-m-d H:i:s')){
                            this.setValue('');
                        }else if(fromTime){
                            Ext.getCmp(conf.mainFormPanelId).getCmpByName('main_days').setValue(
                                this.scope.dateDiff(Ext.Date.format(fromTime, 'Y-m-d H:i:s'), Ext.Date.format(value, 'Y-m-d H:i:s'))
                            );
                        }
                    }
                },
                { field: 'main_days', xtype: 'displayfield', fieldLabel : _lang.MyAgentSetting.fDays },

                { field: 'main.creatorId', xtype: 'hidden', value: curUserInfo.id},
                { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus,
                    store: [['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled] ,]
                },

                {field: 'main.flow', xtype: 'MyAgentSettingViewFormGrid', fieldLabel: _lang.ProductDocument.fLength, cls: 'row',
                    mainGridPanelId: conf.mainGridPanelId, mainFormPanelId:conf.mainFormPanelId,
                },
            ]
        });

        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            var me= this;

            Ext.getCmp(conf.mainFormPanelId).loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true, success:function(response){
                    var json = Ext.JSON.decode(response.responseText);
                    var cmp = me.formPanel.getCmpByName('main.flow').formGridPanel;
                    cmp.getStore().removeAll();
                    var active = [];
                    //cmp.getStore().add(all);
                    $HpStore({
                        fields: ['id','code'],
                        url: __ctxPath + 'flow/myagentsetting/processeslist?id=' + json.data.id, loadMask: true, scope: this,
                        callback: function (conf, records, eOpts) {
                            for (var index = 0; index < records.length; index++) {
                                var flow = {};
                                if(records[index].raw.active == true) active.push(index);
                                Ext.apply(flow, records[index].raw);
                                cmp.getStore().add(flow);
                            }
                            console.log(active);
                            for(var i = 0; i < active.length; i ++){
                                cmp.getSelectionModel().select(active[i], true);
                            }
                        }
                    });

                }
            });
        }else if(this.action == 'add'){
            var cmp = Ext.getCmp(conf.mainFormPanelId).getCmpByName('main.flow').formGridPanel;
            cmp.getStore().removeAll();

            //cmp.getStore().add(all);
            $HpStore({
                fields: ['id','code'],
                url: __ctxPath + 'flow/myagentsetting/processeslist', loadMask: true, scope: this,
                callback: function (conf, records, eOpts) {
                    for (var index = 0; index < records.length; index++) {
                        var flow = {};
                        Ext.apply(flow, records[index].raw);
                        cmp.getStore().add(flow);
                    }
                }
            });
        }
        //if(this.isApprove) this.editFormPanel.setFieldsEditable(['flowRemark']);
        //this.editFormPanel.setReadOnly(this.readOnly, ['flowRemark','flowNextHandlerAccount',]);

    },// end of the init
    dateDiff: function(from, to){
        var start = new Date(from), end = new Date(to);
        var result = ((end.getTime() - start.getTime())/(1000*3600*24)).toFixed(1);
        return result;
    },

    saveFun: function(action, isFlow){
        var params = {act: !!action? action :this.actionName ? this.actionName: 'save'};
        var flag = false;
        //var rows = $getGdItems({grid: Ext.getCmp(this.subGridPanelId)});
        var rows = Ext.getCmp(this.subGridPanelId).getSelectionModel().selected.items;

        var ids = '';
        console.log(rows);
        if(!!rows && rows.length>0){
            for(var j=0;j<rows.length;j++){
                ids += rows[j].data.id;
                //alert(ids);
                rows[j].data['active']=true;
            };

            var i = 0;

            for(var index in rows){
                    flag = true;
                    params['details['+ i +']' ]= rows[index].data['code'];
                    i++;
            }
        }
        if(!flag){
            Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsAgentSettingError);
        }else{
            $postForm({
                formPanel: Ext.getCmp(this.mainFormPanelId),
                grid: Ext.getCmp(this.mainGridPanelId),
                scope: this,
                url: this.urlSave,
                maskTo: this.frameId,
                params: params,
                callback: function (fp, action, status, grid) {
                    Ext.getCmp(this.winId).close();
                    if(!!grid) {
                        grid.getSelectionModel().clearSelections();
                        grid.getView().refresh();
                    }
                }
            });
        }

    }
});