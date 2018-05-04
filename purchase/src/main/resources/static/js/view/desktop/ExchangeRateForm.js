ExchangeRateForm = Ext.extend(HP.Window, {
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);

        this.isApprove = this.action != 'add' && this.action != 'copy' ? true: false;

        var conf = {
            title : _lang.ExchangeRate.mFormTitle + ' - '+ (this.action == 'add' || this.action == 'copy' ? _lang.TButton.add: _lang.TButton.edit),
            winId : 'ExchangeRateFormWinID',
            moduleName: 'ExchangeRate',
            frameId : 'ExchangeRateView',
            mainGridPanelId : 'ExchangeRateGridPanelID',
            mainFormPanelId : 'ExchangeRateFormPanelID',
            mainViewPanelId : 'ExchangeRateViewPanelID',
            searchFormPanelId: 'ExchangeRateSearchPanelID',
            subFormPanelId : 'ExchangeRateViewSubFormPanelID',
            urlSave: __ctxPath + 'desktop/rateControl/save',
            urlGet: __ctxPath + 'desktop/rateControl/get',
            actionName: this.action,
            refresh: true,
            save: true,
            reset: true,
            close: true,
            saveFun: this.saveRow
        };

        this.initUIComponents(conf);
        ExchangeRateForm.superclass.constructor.call(this, {
            id : conf.winId,
            scope : this,
            title : conf.title,
            tbar: Ext.create("App.toolbar", conf),
            cls: 'gb-blank',
            width : 800, height : 600,
            items : this.editFormPanel
        });
    },// end of the constructor

    initUIComponents : function(conf) {
        var scope = this;
        this.editFormPanel = new HP.FormPanel({
            id : conf.mainFormPanelId,
            region: 'center',
            closeWin: conf.winId,
            fieldItems : [
                { xtype: 'section', title:_lang.ExchangeRate.tabInitialValue},
                { field: 'id',	xtype: 'hidden', value: this.action == 'add' ? '': this.record.id },
                { field: 'aud',	xtype: 'hidden', value: 1 },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.rateAudToRmb', xtype: 'numberfield', decimalPrecision: 4, minValue: 0, fieldLabel: _lang.ExchangeRate.fRateAudToRmb, cls:'col-2',
                        allowBlank:false,
                        listeners: {
                            change: function (field, newValue, oldValue) {
                                var form = field.up('form');
                                form.getCmpByName('dispalyRateAudToRmb').setValue(' 1 AUD = ' + newValue + ' RMB');
                            }
                        }
                    },
                    { field: 'dispalyRateAudToRmb', xtype: 'displayfield', cls:'col-2',msgTarget:'side', margin: '0 0 0 5', value: '1 AUD = ' + (curUserInfo.audToRmb || '') + ' RMB'  },
                ] },
                { xtype: 'container', cls:'row', items:  [
                    { field: 'main.rateAudToUsd', xtype: 'numberfield', decimalPrecision: 4, minValue: 0, fieldLabel: _lang.ExchangeRate.fRateAudToUsd, cls:'col-2',
                        allowBlank:false, msgTarget:'side',
                        listeners: {
                            change: function (field, newValue, oldValue) {
                                var form = field.up('form');
                                form.getCmpByName('displayRateAudToUsd').setValue(' 1 AUD = ' + newValue + ' USD');
                            }
                        }
                    },
                    { field: 'displayRateAudToUsd', xtype: 'displayfield', cls:'col-2',msgTarget:'side', margin: '0 0 0 5', value: '1 AUD = ' + (curUserInfo.audToUsd || '') + ' USD'  },
                ] },
                { xtype: 'container', cls:'row', items:  [
                    {field: 'main.effectiveDate', xtype: 'datetimefield', fieldLabel: _lang.ExchangeRate.fEffectiveDate, format: curUserInfo.dateFormat,  cls:'col-2',
                        allowBlank:false, },
                ] },
                { xtype: 'container', cls:'row', margin: '0 0 300 0', items:  [
                    { xtype: 'button', text: _lang.ExchangeRate.fGetRate, width: 80, cls: 'col-2',
                        listeners: {
                            click: function(){
                                scope.getRate.call(scope, conf);
                            }
                        }
                    },
                ] },

                //创建人信息
                { xtype: 'section', title:_lang.NewProductDocument.tabCreatorInformation},
                { xtype: 'container',cls:'row', items:  [
                    { field: 'main.applicantName', xtype: 'displayfield', value:curUserInfo.loginname, fieldLabel: _lang.TText.fApplicantName, cls:'col-2', readOnly:true },
                    { field: 'main.departmentName', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.TText.fAppDepartmentName, cls:'col-2', readOnly:true}
                ] },

                { xtype: 'container',cls:'row', items: [
                    { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', readOnly:true},
                    { field: 'main.status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls:'col-2', readOnly:true,
                        renderer: function(value){
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                            if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                        }
                    }
                ], hidden: !this.isApprove },
            ]
        });


        // 加载表单对应的数据
        if (this.action != 'add' && !Ext.isEmpty(this.record.id)) {
            this.editFormPanel.loadData({
                url : conf.urlGet + '?id=' + this.record.id,
                preName : 'main', loadMask:true
            })
        } else {
            var date = Ext.Date.format(new Date(), curUserInfo.dateFormat);
            var datePart = date.split(' ')[0];
            this.editFormPanel.getCmpByName('main.effectiveDate').setValue(datePart + ' 08:00:00');
        }
    },

    getRate: function(conf){
        var scope = this;
        // console.log('get rate');
        var url = __ctxPath + 'api/transfer/fxrates';
        Ext.Ajax.request({
            url: url,
            method: 'POST',
            scope: this,
            loadMask:true, maskTo: conf.mainFormPanelId,
            success: function (response, options) {
                var json = Ext.JSON.decode(response.responseText);
                if(!json || !json.status || json.status.code != 'API-200') {
                    console.log('failed to get fx rate');
                } else {
                    for(var i = 0; i < json.fxRatesResponse.fxRates.length; i++){
                        var rate = json.fxRatesResponse.fxRates[i];
                        if(rate.buyCurrency == 'USD') {
                            // console.log("USD " + rate.currentSellRate);
                            scope.editFormPanel.getCmpByName('main.rateAudToUsd').setValue(rate.currentSellRate);
                        } else if(rate.buyCurrency == 'CNY') {
                            // console.log("CNY " + rate.currentSellRate);
                            scope.editFormPanel.getCmpByName('main.rateAudToRmb').setValue(rate.currentSellRate);
                        }
                    }
                }
            }
        });
    }
});