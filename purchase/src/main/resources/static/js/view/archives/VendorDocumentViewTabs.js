Ext.define('App.VendorDocumentViewTabs', {
	extend : 'Ext.tab.Panel',
	alias : 'widget.VendorDocumentViewTabs',

	constructor : function(conf) {
		Ext.applyIf(this, conf);
		conf.items = [];

		conf.items[0] = new HP.GridPanel({
			id : conf.mainTabPanelId+'-0',
			title : _lang.ProductDocument.tabListTitle,
			scope : this,
			forceFit : false,
			rsort: false,
			autoLoad : false,
            bbar: false,
			fields : [
				'id','sku','combined', 'name', 'barcode','categoryName','packageName','color','model','style','length',
				'width', 'height', 'cbm', 'cubicWeight', 'netWeight', 'seasonal', 'indoorOutdoor','electricalProduct',
				'powerRequirements','mandatory','operatedAt','status', 'sort','ean',
				'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId', 'departmentCnName','departmentEnName','updatedAt',
                {name: 'newProduct', mapping: 'base.newProduct'}
			],
			columns : [ 
	            { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200},
                { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 60 ,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200, hidden: true },
                { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 80 },
                { header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 100,  },
                { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120},
                { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 60,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 200 },
                { header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 40 },
                { header: _lang.ProductDocument.fModel, dataIndex: 'model', width: 40 },
                { header: _lang.ProductDocument.fStyle, dataIndex: 'style', width: 40},
                { header: _lang.ProductDocument.fProductInformation, columns:[
                    { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true},
                    { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true},
                    { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true },
                ]},
                { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 80},
                { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 80,
                    renderer: function(value){
                        var $item = _dict.optionsYesNo;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                }
			]
		});

		conf.items[1] = new HP.GridPanel({
			id : conf.mainTabPanelId+'-1',
			title: _lang.Contacts.tabListTitle,
			scope : this,
			forceFit : false,
			autoLoad : false,
			rsort: false,
			bbar: false,
			region: 'west',
			fields: [ 'id','vendor.cnName','vendor.enName','vendorId','cnName','enName', 'gender','title', 'email','department', 'qq','skype','extension','phone','wechat','mobile','shared','remark','status',
 	           'createdAt', 'updatedAt','creatorId','creatorCnName','creatorEnName','departmentId','departmentCnName','departmentEnName'
 			],
 			columns: [
  				{ header: 'ID', dataIndex: 'id', width: 120, hidden: true },
 				{ header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendor.cnName', width: 260, hidden: true },
 				{ header: _lang.NewProductDocument.fVendorId, dataIndex: 'vendor.enName', width: 260, hidden: true },
 				{ header:_lang.NewProductDocument.fVendorId, dataIndex: 'vendorId', width: 60, hidden: true },
 			    { header: _lang.Contacts.fCnName, dataIndex: 'cnName', width: 100},
 			    { header: _lang.Contacts.fEnName, dataIndex: 'enName', width: 120},
 			    { header: _lang.Contacts.fGender, dataIndex: 'gender', width: 40,
 					renderer: function(value){
 						if(value == '1') return $renderOutputColor('green', _lang.TText.vMale);
 						if(value == '2') return $renderOutputColor('gray', _lang.TText.vFemale);
 					}
 			    },
 			    { header: _lang.TText.fEmail, dataIndex: 'email', width: 200 },
 			    { header: _lang.Contacts.fQq, dataIndex: 'qq', width: 140 },
 			    { header: _lang.Contacts.fSkype, dataIndex: 'skype', width: 140 },
 			    { header: _lang.Contacts.fPhone, dataIndex: 'phone', width: 100 },
 			    { header: _lang.Contacts.fExtension, dataIndex: 'extension', width: 100 },
 			    { header: _lang.Contacts.fWechat, dataIndex: 'wechat', width: 140 },
 			    { header: _lang.Contacts.fMobile, dataIndex: 'mobile', width: 100 },
 			    { header: _lang.Contacts.fRemark, dataIndex: 'remark', width: 200 },
 				
 			],// end of columns
        	appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
        });

		conf.items[2] = new App.MyDocumentGridTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.TText.fAttachmentList,
            scope: this,
            noTitle: true
        });

		conf.items[3] = Ext.create("Ext.Panel",{
			id : conf.mainTabPanelId+'-3',
			title : _lang.VendorDocument.tabPaymentTerms,
			layout: 'fit',
			bodyPadding: 5,
	        html: ''
		});
		conf.items[3].doLayout();

        conf.items[4] = new HP.GridPanel({
            id : conf.mainTabPanelId+'-4',
            title : _lang.TText.fImageList,
            scope : this,
            forceFit : false,
            autoLoad : false,
            rsort: false,
            bbar: false,
            fields: [
            	'id','name','path','extension','note','categoryId','bytes','updatedAt','creatorId','creatorCnName','creatorEnName',
				'departmentId','departmentCnName','departmentEnName'
			],
            columns: [
                { header: _lang.TText.rowAction, xtype: 'rowactions', width:55, locked: true,
                    keepSelection: false, autoWidth: false, fixed: true, sortable: false, scope: this,
                    actions: [{
                        iconCls: 'btnRowPreview', btnCls: 'fa-eye', tooltip: _lang.ArchivesHistory.mPreview,
                        callback: function(grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }, {
                        iconCls: 'btnRowDownload', btnCls: 'fa-download', tooltip: _lang.MyDocument.mFileDownload,
                        callback: function (grid, record, action, idx, col, e, target) {
                            this.scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                        }
                    }]
                },
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.MyDocument.fName, dataIndex: 'name', width:260 },
                { header: _lang.MyDocument.fExtension, dataIndex: 'extension', width:50 },
                { header: _lang.MyDocument.fBytes, dataIndex: 'bytes', width:70, renderer: Ext.util.Format.fileSize },
                { header: _lang.MyDocument.fNote, dataIndex: 'note', width:260 }
            ],// end of columns
            appendColumns: $groupGridCreatedColumns({sort:false, createdAt:false, status:false,assignee:false}),
            // end of columns
        });
        conf.items[5] = new HP.GridPanel({
            id: conf.mainTabPanelId+'-5',
            title: _lang.ProductDocument.tabOrderListTitle,
            collapsible: true,
            split: true,
            scope: this,
            forceFit: false,
            autoLoad: false,
            rsort: false,
            fields: [
                'id','businessId','jobNumber', 'sellerName','sellerAddress','vendorCnName','vendorEnName',
                'sellerPhone', 'sellerFax', 'sellerContactName','sellerEmail','buyerName','buyerAddress', 'buyerPhone','fBuyerContactName','buyerFax',
                'buyerEmail', 'currency', 'totalPriceAud', 'totalPriceRmb','totalPriceUsd', 'totalOrderQty','shippingDate',
                'originPortId','originPortCnName','originPortEnName', 'destinationPortId',
                'depositAud','depositRmb','depositUsd','shippingMethod','paymentTerms','otherTerms','orderDate','startTime','flowStatus',
                'orderTitle', 'orderNumber', 'vendorName', 'createdAt'
            ],
            width: '55%',
            minWidth: 400,
            columns: [
                { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
                { header: _lang.ProductDocument.fBusinessId, dataIndex: 'id', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderNumber, dataIndex: 'orderNumber', width: 180 },
                { header: _lang.FlowPurchaseContract.fOrderTitle, dataIndex: 'orderTitle', width: 180 },
                { header: _lang.ProductDocument.fVendorName, dataIndex: 'vendorCnName', width: 200 ,hidden:curUserInfo.lang == 'zh_CN' ? false: true, },
                { header: _lang.ProductDocument.fVendorName, dataIndex: 'vendorEnName', width: 200, hidden:curUserInfo.lang == 'zh_CN' ? true: false,  },
                { header: _lang.NewProductDocument.fCurrency, dataIndex: 'currency', width: 80,
                    renderer: function(value){
                        var $currency = _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fTotalPrice,
                    columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fTotalOrderQty, dataIndex: 'totalOrderQty', width: 60 },
                { header: _lang.ProductDocument.fShippingDate, dataIndex: 'shippingDate', width: 140 },
                { header: _lang.FlowPurchaseContract.fOriginPort, dataIndex: 'originPortId', width: 100,
                    renderer: function(value){
                        var $item = _dict.origin;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options, []);
                        }
                    }
                },
                { header: _lang.FlowPurchaseContract.fDestinationPort, dataIndex: 'destinationPortId', width: 100,
                    renderer: function(value){
                        var $item = _dict.destination;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options, []);
                        }
                    }
                },
                { header: _lang.ProductDocument.fDeposit,
                    columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                },
                { header: _lang.ProductDocument.fShippingMethod, dataIndex: 'shippingMethod', width: 60,
                    renderer: function(value){
                        var $item = _dict.shippingMethod;
                        if($item.length>0 && $item[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $item[0].data.options);
                        }
                    }
                },
                { header: _lang.ProductDocument.fPaymentTerms, dataIndex: 'paymentTerms', width: 60,  },
                { header: _lang.ProductDocument.fOtherTerms, dataIndex: 'otherTerms', width: 60,  },
                { header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140,  },
                { header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
                    renderer: function(value){
                        var $flowStatus = _dict.flowStatus;
                        if($flowStatus.length>0 && $flowStatus[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $flowStatus[0].data.options);
                        }
                    }
                },
            ],//end of columns
        });

        conf.items[6] = new App.ReportProductTabGrid({
            mainGridPanelId: conf.mainGridPanelId,
            title: _lang.Reports.tabRelatedReports,
            scope: this,
            noTitle: true
        });

        try {
            conf.items[7] = new App.ArchivesHistoryTabGrid({
                historyListUrl: 'archives/vendor/history',
                historyGetUrl: 'archives/vendor/historyget',
                historyConfirmUrl: 'archives/vendor/historyconfirm',
                historyDelUrl: 'archives/vendor/historydelete',
                mainGridPanelId: conf.mainGridPanelId,
                title: _lang.ArchivesHistory.mTitle,
                winForm: VendorDocumentForm,
                noTitle: true
            });
        }catch (e){
            console.log(e);
        }

		
		try {
			App.VendorDocumentViewTabs.superclass.constructor.call(this, {
				id : this.mainTabPanelId,
				title : conf.fieldLabel,
				autoWidth : true,
				autoHeight : true,
				autoScroll : true,
				cls:'tabs'
			});

			this.initUIComponents(conf);
		} catch (e) {
			console.log(e);
		}


	},

	initUIComponents : function(conf) {
		var cmpPanel = Ext.getCmp(this.mainTabPanelId);
		for (var i = 0; i < conf.items.length; i++) {
			cmpPanel.add(conf.items[i]);
		}
		cmpPanel.setActiveTab(conf.items[0]);
	},

    onRowAction: function (grid, record, action, idx, col, conf) {
        switch (action) {
            case 'btnRowPreview' :
                console.log(grid);
                $editGridRs({
                    grid: grid,
                    winForm: FilesPreviewDialog,
                    scope: this
                });
                break;
            case 'btnRowDownload':
                window.open(__ctxPath + 'mydoc/download?fileId=' + record.data.id);
                break;
        }
    }

});
