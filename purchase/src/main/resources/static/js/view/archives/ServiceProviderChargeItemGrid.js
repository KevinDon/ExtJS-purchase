Ext.define('App.ServiceProviderChargeItemGrid', {
    extend: 'Ext.Panel',
    alias: 'widget.ServiceProviderChargeItemGrid',

    constructor : function(conf){
        Ext.applyIf(this, conf);
        var conf = {
            title : this.fieldLabel,
            moduleName: 'ChargeItems',
            fieldValueName: this.valueName || 'charge_items',
            fieldTitleName: this.titleName || 'charge_items_name'
        };
        conf.frameId = "ServiceProviderDocumentView",
        conf.mainGridPanelId= this.mainGridPanelId || this.formId + '-ServiceProviderChargeItemGrid';
        conf.subGridPanelId= this.subGridPanelId || conf.mainGridPanelId +'-ServiceProviderChargeItemGridPanelID';
        conf.defHeight = this.height || 350;
        conf.readOnly = this.readOnly || false;

        this.initUIComponents(conf);

        App.ServiceProviderChargeItemGrid.superclass.constructor.call(this, {
            id: conf.subGridPanelId + '-f',
            minHeight: conf.defHeight,
            height: conf.defHeight,
            width: this.width || '100%',
            bodyCls:'x-panel-body-gray',
            items: [ this.subGridPanel ]
        });
    },

    initUIComponents: function(conf){
        this.subGridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.subGridPanelId,
            title: _lang.ServiceProviderDocument.fChargeItem,
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
            fields: [ 'id', 'itemCnName', 'itemEnName', 'unitId', 'unitCnName', 'unitEnName', 'active'],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden: true },
                { header: _lang.ServiceProviderDocument.fChargeItem, dataIndex: 'itemCnName', width: 200 },
                { header: _lang.ServiceProviderDocument.fChargeItem, dataIndex: 'itemEnName', width: 260 },
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitId', width: 100,
                    renderer: function(value){
                        var $chargeUnit = _dict.chargeUnit;
                        if($chargeUnit.length>0 && $chargeUnit[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $chargeUnit[0].data.options);
                        }
                    },
                    editor: {xtype:'dictcombo', code:'service_provider', codeSub:'unit_of_operation', allowBlank:false,
                        selectFun: function (records, eOpts) {
                            var unit = _dict.getValueRow('chargeUnit', records[0].data.id);
                            var row = Ext.getCmp(conf.subGridPanelId).getSelectionModel().selected.getAt(0);
                            row.set('unitCnName', unit.cnName);
                            row.set('unitEnName', unit.enName);
                        }
                    }
                },
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitCnName', width: 100, hidden:true},
                { header: _lang.ServiceProviderDocument.fChargeUnit, dataIndex: 'unitEnName', width: 100},
                { header: _lang.ProductProblem.fSelect, xtype:'checkcolumn', dataIndex: 'active', width: 50, sortable:false,
                    renderer:function(value, meta){return $renderOutputCheckColumns(value, meta)}
                }
            ],
            afterrender:function(e, eOpts){
                var $chargeItems = _dict.chargeItem;
                if($chargeItems.length > 0 && $chargeItems[0].data.options.length>0){
                    var options = $chargeItems[0].data.options;
                    var unit = _dict.getValueRow('chargeUnit', 1);
                    for(var i = 0; i < options.length; i++){
                        var row = {
                            id: options[i].value,
                            itemCnName: options[i].desc[0].lang == 'zh_CN' ? options[i].desc[0].text : options[i].desc[1].text,
                            itemEnName: options[i].desc[0].lang != 'zh_CN' ? options[i].desc[0].text : options[i].desc[1].text,
                            unitId: unit.id,
                            unitCnName: unit.cnName,
                            unitEnName: unit.enName,
                        }
                        e.getStore().add(row)
                    }
                }
            }
        });
    }
});