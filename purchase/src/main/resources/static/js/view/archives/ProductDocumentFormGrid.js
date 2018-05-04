Ext.define('App.ProductDocumentFormGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ProductDocumentFormGrid',

    constructor : function(conf){
        //console.log('ProductDocumentViewGrid');
        Ext.applyIf(this, conf);
        //conf.items = [];
        /**
         * 测试数据 Start
         */
        /**
         * 测试数据 End
         */
        try{
            this.initUIComponents({
                id: 'ProductDocumentFormGridCenterParentId',
                width: 'auto',
                minHeight: 200,
                height: 300,
                items: [
                    new HP.GridPanel({
                        id: 'ProductDocumentFormGridId',
                        title: _lang.ProductDocument.fProductCert,
                        //collapsible: true,
                        //split: true,
                        scope: this,
                        forceFit: false,
                        autoLoad: false,
                        width: 'auto',
                        minHeight: 200,
                        height: 300,
                        url:  __ctxPath + 'admin/user/list',
                        fields: [ 'id','account','departmentName', 'departmentId','cnName','enName', 'email', 'qq','skype','extension','phone', 'gender','joinusAt', 'createdAt', 'setListRows', 'lang', 'timezone','dateFormat', 'status'],
                        columns: [
                            { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                            { header: _lang.TText.fAccount, dataIndex: 'account', width: 100},
                            { header: _lang.AccountUser.fDepartmentName, dataIndex: 'departmentName', width: 100 },
                            { header: _lang.AccountUser.fDepartmentId, dataIndex: 'departmentId', width: 80, hidden: true },
                            { header: _lang.AccountUser.fCnName, dataIndex: 'cnName', width: 120},
                            { header: _lang.AccountUser.fEnName, dataIndex: 'enName', width: 120},
                            { header: _lang.AccountUser.fGender, dataIndex: 'gender', width: 40,
                                renderer: function(value){
                                    if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
                                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
                                }
                            },
                            { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
                            { header: _lang.AccountUser.fQq, dataIndex: 'qq', width: 140 },
                            { header: _lang.AccountUser.fSkype, dataIndex: 'skype', width: 140 },
                            { header: _lang.AccountUser.fPhone, dataIndex: 'phone', width: 100 },
                            { header: _lang.AccountUser.fExtension, dataIndex: 'extension', width: 100 },
                            { header: _lang.AccountUser.fSetListRows, dataIndex: 'setListRows', width: 40 },
                            { header: _lang.AccountUser.fJoinusAt, dataIndex: 'joinusAt', width: 140 },
                            { header: _lang.AccountUser.fCreatedAt, dataIndex: 'createdAt', width: 140 },
                            { header: _lang.TText.fLang, dataIndex: 'lang', width: 60 },
                            { header: _lang.TText.fTimezone, dataIndex: 'timezone', width: 60 },
                            { header: _lang.TText.fDateFormat, dataIndex: 'dateFormat', width: 80 },
                            { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
                                renderer: function(value){
                                    if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                                }
                            }
                        ],//end of columns
                    })
                ]
            });
        }catch(e){
            console.log(e);
        }
    },

    initUIComponents: function(conf) {
            App.ProductDocumentFormGrid.superclass.constructor.call(this, conf);
        }
});
