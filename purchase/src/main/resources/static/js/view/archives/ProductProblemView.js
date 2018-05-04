ProductProblemView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);

        var conf = {
            title : _lang.ProductProblem.mTitle,
            frameId : 'ProductProblemView',
            moduleName:'ProductProblem',
            winId : 'ProductProblemFormWinID',
            mainGridPanelId : 'ProductProblemGridPanelID',
            mainFormPanelId : 'ProductProblemFormPanelID',
            searchFormPanelId: 'ProductProblemSubFormPanelID',
            mainTabPanelId: 'ProductProblemTbsPanelId',
            subGridPanelId : 'ProductProblemSubGridPanelID',
            urlList: __ctxPath + 'archives/troubleTicket/list',
            urlSave: __ctxPath + 'archives/troubleTicket/save',
            urlDelete: __ctxPath + 'archives/troubleTicket/delete',
            urlGet: __ctxPath + 'archives/troubleTicket/get',
            actionName: this.action,
            refresh: true,
            edit: true,
            add: true,
            copy: true,
            del: true,
            editFun: this.editRow,
            //previewFun: this.previewRow
        };

        this.initUIComponents(conf);
        ProductProblemView.superclass.constructor.call(this, {
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
            	   
                {field:'Q-omsOrderId-S-LK', xtype:'textfield', title:_lang.ProductProblem.fOmsOrderId},
                {field:'Q-transactionNumber-S-LK', xtype:'textfield', title:_lang.ProductProblem.fTransactionNumber},
                {field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowDepositContract.fOrderNumber},
                {field:'Q-sku-S-LK', xtype:'textfield', title:_lang.ProductDocument.fSku},
                {field:'Q-handleMode-S-LK', xtype:'textfield', title:_lang.ProductProblem.fHandleMode , xtype: 'dictcombo',  code:'product_problem', codeSub:'handle_mode',},
                {field:'Q-memberNickname-S-LK', xtype:'textfield', title:_lang.ProductProblem.fMemberNickname},
                {field:'Q-email-S-LK', xtype:'textfield', title:_lang.TText.fEmail},
                {field:'Q-status-N-EQ', xtype:'combo', title:_lang.TText.fStatus, value: '',
                    store: [['', _lang.TText.vAll],['0', _lang.TText.vDraft],  ['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
               },
                {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorCnName},
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fCreatorEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title:_lang.TText.fDepartmentName, 
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },
			    { field: 'Q-createdAt-D', xtype: 'DateO2TField', fieldLabel: _lang.TText.fCreatedAt, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

        this.gridPanel = new HP.GridPanel({
            region: 'center',
            id: conf.mainGridPanelId,
            title: _lang.ProductProblem.mTitle,
            scope: this,
            forceFit: false,
            url: conf.urlList,
            width: '55%',
            minWidth: 400,
            fields: [
                'id','omsOrderId','sellChannel','transactionNumber', 'orderNumber','memberNickname','qty',
                'email','orderAt','handleMode','troubleDetailId','troubleCategoryId',
                'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
                'departmentCnName','departmentEnName','updatedAt', 'ageLimit',
            ],
            columns: [
                { header: 'ID', dataIndex: 'id', width: 180, hidden:true},
                { header: _lang.ProductProblem.fOmsOrderId, dataIndex: 'omsOrderId', width: 200, },
                { header: _lang.ProductProblem.fSellChannel, dataIndex: 'sellChannel', width: 200, },
                { header: _lang.ProductProblem.fTransactionNumber, dataIndex: 'transactionNumber', width: 200, },
                { header: _lang.ProductProblem.fMemberNickname, dataIndex: 'memberNickname', width: 120, },
                // { header: _lang.ProductProblem.fQty, dataIndex: 'qty', width: 40, },
                { header: _lang.TText.fEmail, dataIndex: 'email', width: 200, },
                { header: _lang.ProductProblem.fOrderAt, dataIndex: 'orderAt', width: 140 },
                { header: _lang.ProductProblem.fHandleMode, dataIndex: 'handleMode', width: 100,
                	 renderer: function(value){
                         var $source = _dict.handleMode;
                         if($source.length>0 && $source[0].data.options.length>0){
                             return $dictRenderOutputColor(value, $source[0].data.options);
                         }
                     }},
            ],
            appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),
            itemclick: function(obj, record, item, index, e, eOpts){
                this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            }
        });

        this.ProductProblemViewTabsPanel = new App.ProductProblemViewTabs({
            region: 'south',
            //collapsible: true,
            split: true,
            maxHeight:'300',
            mainTabPanelId: conf.mainTabPanelId,
            mainGridPanelId: conf.mainGridPanelId,
            gridPanelId: conf.subProductGridPanelId,
            ProductCombinationGridPanelId: conf.ProductCombinationList,
            items: [],
        });

        //布局
        this.centerPanel = new Ext.Panel({
            id: conf.mainGridPanelId + '-center',
            region: 'center',
            layout: 'border',
            width: 'auto',
            border: false,
            items: [ this.gridPanel, this.ProductProblemViewTabsPanel ]
        });
    },// end of the init

    rowClick: function(record, item, index, e, conf) {

        $postUrl({ url:conf.urlGet + '?id=' + record.data.id + '&product=1', maskTo:conf.mainTabPanelId, autoMessage: false,
            callback: function (response) {
                var json = Ext.JSON.decode(response.responseText);

                //sub product list init
                Ext.getCmp(conf.mainTabPanelId).items.items[2].setValue(!!json.data && !!json.data.attachments ? json.data.attachments: '');

                var commentsList = Ext.getCmp(conf.mainTabPanelId + '-1');
                commentsList.getStore().removeAll();
                if(!!json.data && !!json.data.comments && json.data.comments.length>0){
                    for(index in json.data.comments){
                        var comments = {};
                        Ext.apply(comments, json.data.comments[index]);
                        commentsList.getStore().add(comments);
                    }
                }
                var productsList = Ext.getCmp(conf.mainTabPanelId + '-0');
                productsList.getStore().removeAll();

                if(!!json.data && !!json.data.details &&  json.data.details.length>0){
                    for(index in json.data.details){
                        var product = {};
                        Ext.apply(product, json.data.details[index]);
                        Ext.apply(product, json.data.details[index].product.prop);
                        Ext.apply(product, json.data.details[index].product);
                        productsList.getStore().add(product);
                    }
                }
            }
        });
    },
    editRow : function(conf){
        new ProductProblemForm(conf).show();
    },


});