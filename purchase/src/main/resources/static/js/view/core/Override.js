Ext.ns("Ext");

//Override Ext Container, add getCmpByName
Ext.override(Ext.Container, {
    getCmpByName: function (name) {
        var getByName = function (container, name) {
            var items = container.items;
            if (items && items.getCount != undefined) {
                for (var i = 0; i < items.getCount(); i++) {
                    var comp = items.get(i);
                    if (name == comp.name || (comp.getName && name == comp.getName())) {
                        return comp;
                        break;
                    }
                    var cp = getByName(comp, name);
                    if (cp != null) return cp;
                }
            }
            return null;
        };
        return getByName(this, name);
    }
});

//重构 Panel
Ext.override(Ext.Panel, {
    //width: '100%',
    loadData: function (conf) {
        var me = this;
        var scope = conf.scope ? conf.scope : this;
        var params = conf.params ? conf.params : {};

        if (!conf.root) conf.root = 'data';
        Ext.Ajax.request({
            method: 'POST', url: conf.url, scope: scope, params: params,
            loadMask: conf.maskTo ? true : conf.loadMask ? conf.loadMask : false,
            maskTo: conf.maskTo ? conf.maskTo : null,
            success: function (response, options) {
                var json = Ext.JSON.decode(response.responseText);
                var data = null;

                if (!!this.scope && !!this.scope.centerFormPanel && !!this.scope.centerFormPanel.form) this.scope.centerFormPanel.form.reset();
                if (conf.before) {
                    conf.before.call(scope, response, options);
                }
                if (conf.root) {
                    data = eval('json.' + conf.root);
                } else {
                    data = json.data;
                }
                $_setByName(me, data, conf);

                if (conf.success) {
                    conf.success.call(scope, response, options);
                }
            },// end of success
            failure: function (response, options) {
                if (conf.failure) {
                    conf.failure.call(scope, response, options);
                }
            }
        });
    },

    loadHtml: function (conf) {
        var scope = conf.scope ? conf.scope : this;
        var params = conf.params ? conf.params : {};

        if (!conf.root) conf.root = 'data';
        Ext.Ajax.request({
            method: 'POST',
            url: conf.url,
            scope: scope,
            params: params,
            loadMask: conf.loadMask ? conf.loadMask : false,
            maskTo: conf.maskTo ? conf.maskTo : null,
            success: function (response, options) {
                Ext.getCmp('PermissionPortalViewHtml').setValue(response.responseText);
                if (conf.success) {
                    conf.success.call(scope, response, options);
                }
            },// end of success
            failure: function (response, options) {
                if (conf.failure) {
                    conf.failure.call(scope, response, options);
                }
            }
        });
    },
    dictData: {},
    // {[{key:'', code:'', subCode:''}]}
    dictLoad: function (conf) {
        var def = conf || [];
        if (def.length > 0) {
            for (var i = 0; i < def.length; i++) {
                this.dictData[def[i].key] = new $HpDictStore({code: def[i].code, codeSub: def[i].codeSub});
            }
            this.updateLayout();
        }
    },

    setReadOnly: function (boolean, excludeFields) {
        this.readOnly = boolean;
        if (!!this.form && this.getForm().getFields().length > 0) {
            var items = this.getForm().getFields().items;
            for (var i = 0; i < items.length; i++) {
                var next = true;
                if (excludeFields != undefined && excludeFields.length > 0) {
                    next = true;
                    for (var j = 0; j < excludeFields.length; j++) {
                        if (excludeFields[j] == items[i].name) {
                            next = false;
                            continue;
                        }
                    }
                }
                if (next && items[i].xtype != 'hidden' && items[i].xtype != 'displayfield') {
                    var me = items[i];
                    me.setReadOnly(boolean);
                }
            }
        }
    }
});

Ext.override(Ext.form.field.ComboBox, {
    labelWidth: this.labelWidth ? this.labelWidth : 130,
    width: this.width ? this.width : 150,
    blankText:this.blankText? this.blankText: '',
    emptyText:this.emptyText? this.emptyText: '',
    setLocalSource: function (value) {
        if (value.length) {
            var $valueType = this.valueType || 'user';
            var $data = [];
            for (var index in value) {
                // console.log(value);
                if ($valueType == 'user') {
                    $data.push([value[index].account, value[index].account + ' / ' + value[index].cnName + ' / ' + value[index].enName]);
                } else if ($valueType == 'dict') {
                    //@TODO
                } else {
                    $data.push([value[index].id.toString(), value[index].title]);
                }
            }

            this.store = new Ext.data.SimpleStore({mode:'local', fields: ['id', 'text'], data: $data});
            this.valueField= 'id',
            this.displayField= 'text',
            this.show();
        }
    }
});

// Ext.override(Ext.form.field.HtmlEditor, {
//     plugins: [
//         Ext.create('Ext.app.form.HtmlEditor.Image', {}),
//         Ext.create('Ext.app.form.HtmlEditor.Table', {}),
//     ]
// });

Ext.override(Ext.form.field.Base, {
    initComponent: function () {
        if (this.allowBlank !== undefined && !this.allowBlank) {
            if (this.fieldLabel) {
                this.fieldLabel += ' <font color=red>*</font>';
            }
        }
        this.callParent(arguments);
    }
});
Ext.override(Ext.container.Container, {
    initComponent: function () {
        if (this.allowBlank !== undefined && !this.allowBlank) {
            if (this.fieldLabel) {
                this.fieldLabel += ' <font color=red>*</font>';
            }
        }
        this.callParent(arguments);
    }
});

Ext.override(Ext.form.field.Text, {
    width: 0
});
Ext.override(Ext.form.field.File, {
    width: 0
});
Ext.override(Ext.form.field.TextArea, {
    width: 0
});
Ext.override(Ext.form.field.Display, {
//	width: '100%',
    labelWidth: this.labelWidth ? this.labelWidth : 130
});

Ext.override(Ext.window.MessageBox, {
    width: 250
});

//重构 radiogroup
Ext.override(Ext.form.RadioGroup, {
    width: 0,
    getValue: function () {
        var v = '';
        if (this.rendered) {
            this.items.each(function (item) {
                if (!item.getValue())
                    return true;
                v = item.getRawValue();
                return false;
            });
        } else {
            var items = this.items.items;
            for (var k in items) {
                if (items[k].checked) {
                    v = items[k].inputValue;
                    break;
                }
            }
        }
        return v;
    },
    setValue: function (v) {
        if (this.rendered)
            this.items.each(function (item) {
                item.setValue(item.getRawValue() == v);
            });
        else {
            var items = this.items.items;
            for (var k in items) {
                items[k].checked = items[k].inputValue == v;
            }
        }
    }
});

//url预载进度条
Ext.override(Ext.Component, {
    listeners: {
        afterrender: function (conn, eOpts) {
            if (this.xtype == 'uxiframe' && this.url) {
                this.load(this.url);
            }
        }
    }
});