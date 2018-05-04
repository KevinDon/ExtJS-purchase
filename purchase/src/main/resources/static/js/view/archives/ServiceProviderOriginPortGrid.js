Ext.define('App.ServiceProviderOriginPortGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ServiceProviderOriginPortGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : this.fieldLabel,
            moduleName: 'OriginPort',
            fieldValueName: this.valueName || 'origin_ports',
            fieldTitleName: this.titleName || 'origin_ports_name'
        };
        conf.mainGridPanelId= this.mainGridPanelId || this.formId + '-ServiceProviderOriginPortGrid';
        conf.subGridPanelId= this.subGridPanelId || conf.mainGridPanelId +'-ServiceProviderOriginPortGridPanelID';
        conf.defHeight = this.height || 350;
        conf.readOnly = this.readOnly || false;

        this.initUIComponents(conf);

        App.ServiceProviderOriginPortGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight, width: '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
        });
    },

    initUIComponents: function(conf){
        var sm = Ext.create('Ext.selection.CheckboxModel', {
            showHeaderCheckbox: false
        });

        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.ServiceProviderDocument.fOriginPort,
            width: '100%',
            height: conf.defHeight-3,
            url: '',
            bbar: false,
            autoLoad: false,
            rsort: false,
            edit: !conf.readOnly,
            // multiSelect: true,
            header:{
                cls:'x-panel-header-gray'
            },
            fields: [ 'id','originPortCnName', 'originPortEnName', 'active'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ServiceProviderDocument.fOriginPort, dataIndex: 'originPortCnName', width: 90 },
                { header: _lang.ServiceProviderDocument.fOriginPort, dataIndex: 'originPortEnName', width: 120 },
                { header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false,
                    renderer:function(value, meta){return $renderOutputCheckColumns(value, meta)}
                }
            ],
            afterrender:function(e, eOpts){
                var $dictOriginPorts = _dict.origin;
                if($dictOriginPorts.length > 0 && $dictOriginPorts[0].data.options.length>0){
                    var options = $dictOriginPorts[0].data.options;
                    for(var i = 0; i < options.length; i++){
                        e.getStore().add({
                            id: options[i].value,
                            originPortCnName: options[i].desc[0].lang == 'zh_CN' ? options[i].desc[0].text : options[i].desc[1].text,
                            originPortEnName: options[i].desc[0].lang != 'zh_CN' ? options[i].desc[0].text : options[i].desc[1].text
                        })
                    }
                }
            }
        });
    }
});