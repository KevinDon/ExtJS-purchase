StaOrderView = Ext.extend(Ext.Panel, {

    constructor: function(conf) {
        Ext.applyIf(this, conf);
        var conf = {
            title : _lang.Sta.mOrderTitle,
            frameId : 'StaOrderView',
            moduleName:'StaOrder',
            mainGridPanelId : 'StaOrderGridPanelID',
            searchFormPanelId: 'StaOrderSubFormPanelID',
            urlList: __ctxPath + 'desktop/sta/list',
            urlExport: __ctxPath + 'desktop/sta/export',
            refresh: true,
            export:true
        };

        this.initUIComponents(conf);
        StaOrderView.superclass.constructor.call(this, {
            id: conf.frameId, title: conf.title,
            region: 'center', layout: 'border',
            closable: true, autoScroll: false,
            tbar: Ext.create("App.toolbar", conf),
            items: [this.searchPanel, this.gridPanel ]
        });
    },

    initUIComponents: function(conf) {
        this.searchPanel = new HP.SearchPanel({
            region: 'north',
            id: conf.searchFormPanelId,
            scope: this,
            parentConf: conf,
            fieldItems:[
            	{field:'Q-id-S-LK', xtype:'textfield', title:_lang.TText.fId},
				{field:'Q-orderNumber-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fOrderNumber},
				{field:'Q-vendorCnName-S-LK', xtype:'textfield', title: _lang.Sta.fVendorCnName, },
				{field:'Q-vendorEnName-S-LK', xtype:'textfield', title: _lang.Sta.fVendorEnName, },
				{field:'Q-receiveLocation-S-LK', xtype:'textfield', title:_lang.FlowOrderReceivingNotice.fReceiveLocation},
			    {field:'Q-creatorCnName-S-LK', xtype:'textfield', title:_lang.TText.fBuyerCnName, },
				{field:'Q-creatorEnName-S-LK', xtype:'textfield', title:_lang.TText.fBuyerEnName},
			    {field:'Q-departmentId-S-EQ', xtype:'hidden'},
			    {field:'departmentName', xtype:'DepDialog', title: _lang.TText.fAppDepartmentName,
			    	formId:conf.searchFormPanelId, hiddenName:'Q-departmentId-S-EQ', single: true
			    },

			    { field: 'Q-agentNotificationDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fAgentNotificationDate, format: curUserInfo.dateFormat},
			    { field: 'Q-readyDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fReadyDate, format: curUserInfo.dateFormat},
			    { field: 'Q-etd-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fEtd, format: curUserInfo.dateFormat},
			    { field: 'Q-eta-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fEta, format: curUserInfo.dateFormat},
			    { field: 'Q-deliveryTime-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fDelivery, format: curUserInfo.dateFormat},
			    { field: 'Q-shippingDocReceivedDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fShippingDocReceived, format: curUserInfo.dateFormat},
			    { field: 'Q-shippingDocForwardedDate-D', xtype: 'DateO2TField', fieldLabel: _lang.Sta.fShippingDocForwarded, format: curUserInfo.dateFormat},
            ]
        });// end of searchPanel

		//grid panel
		this.gridPanel = new HP.GridPanel({
			region: 'center',
			id: conf.mainGridPanelId,
			title: _lang.Sta.tabOrderTitle,
			scope: this,
			forceFit: false,
            rsort: false,
            url: conf.urlList,
            sorters: [{property: 'orderNumber', direction: 'DESC'}],
			fields: [
				'id','orderNumber','productType','orderIndex','vendorCnName','vendorEnName','originPortId','containerType','containerQty','orderTitle',
				'totalCbm','totalPackingCbm','totalShippingCbm','totalPriceAud', 'totalPriceRmb', 'totalPriceUsd', 'writeOffAud','writeOffRmb','writeOffUsd','adjustments',
                'finalInvoiceValue',
				'depositRate','deposit','balance','finalTotalPriceAud','finalTotalPriceRmb','finalTotalPriceUsd','originPortCnName','originPortEnName',
				'serviceProviderCnName','serviceProviderCnName','agentNotificationDate','readyDate','costRateAudToRmb', 'costRateAudToUsd',
				'depositAud','depositRmb','depositUsd','balanceAud','balanceRmb','balanceUsd', 'rateAudToRmb', 'rateAudToUsd',
				'totalFreightAud','totalFreightRmb','totalFreightUsd','tariffAud','tariffRmb','tariffUsd',
                'gstAud','gstRmb','gstUsd','rateAudToUsd','electronicProcessingFeeAud','electronicProcessingFeeRmb','electronicProcessingFeeUsd',
                'etd','eta','deliveryTime','arrivalDays','shippingDocReceivedDate','shippingDocForwardedDate','chargeItemFeeAud','chargeItemFeeRmb','chargeItemFeeUsd',
				'portFeeAud','portFeeRmb','portFeeUsd','freightGST','fXRate', 'depositRateAudToRmb', 'depositRateAudToUsd',
				'totalFreight','duty','gst','electronicProcessingFee','leadTime',' arrivalDays','telexReleased','balanceRateAudToRmb', 'balanceRateAudToUsd',
				'status', 'createdAt','creatorId','creatorCnName','creatorEnName','departmentCnName','exWorkAud','exWorkRmb','exWorkUsd',
                'contractRateAudToRmb', 'contractRateAudToUsd', 'serviceRateAudToRmb', 'serviceRateAudToUsd',
                'departmentEnName','departmentId','updatedAt','currency','creatorCnName','creatorEnName','vendorProductCategoryId','vendorProductCategoryAlias',
                'flagOrderShippingApplyTime','creditTerms','creditTerms','exWork','monthEta','yearEta','departmentCnName','departmentEnName',
                'costRateAudToRmb', 'costRateAudToUsd','depositType', 'totalSalesPriceAud','totalSalesPriceRmb','totalSalesPriceUsd','depositDate',
                'freightInsuranceAud','freightInsuranceRmb','freightInsuranceUsd','totalPackingCbm','estimatedBalance', 'freightGstAud','freightGstRmb','freightGstUsd',
            ],
			width: '55%',
			minWidth: 400,
			columns: [
				{ header: _lang.TText.fId, dataIndex: 'id', width: 180 ,hidden: true, },
				{ header: _lang.Sta.fOrderId, dataIndex: 'orderNumber', width: 80, },
				{ header: _lang.Sta.fOrderTitle, dataIndex: 'orderTitle', width: 230  },
				//{ header: _lang.Sta.fProductType, dataIndex: 'productType', width: 90  },
				{ header: _lang.Sta.fOrderIndex, dataIndex: 'orderIndex', width: 50  },
                { header: _lang.FlowPurchaseContract.fVendorProductCategory, dataIndex: 'vendorProductCategoryId', width: 150, hidden:true,   },
                { header: _lang.FlowPurchaseContract.fVendorProductCategory, dataIndex: 'vendorProductCategoryAlias', width: 150, hidden:true,   },

				{ header: _lang.Sta.fVendorCnName, dataIndex: 'vendorCnName', width: 260,hidden:curUserInfo.lang =='zh_CN'? false: true },
				{ header: _lang.Sta.fVendorEnName, dataIndex: 'vendorEnName', width: 260,hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Sta.fOrigin, dataIndex: 'originPortId', width: 60,
                    renderer: function(value){
                        var $loadingPort = _dict.getValueRow('origin', value);
                        if($loadingPort == undefined) return '';
                        if(curUserInfo.lang == 'zh_CN'){
                            return $loadingPort.cnName;
                        }else{
                            return $loadingPort.enName;
                        }
                    }
                },
				// { header: _lang.Sta.fOrigin, dataIndex: 'originPortCnName', width: 90, hidden:curUserInfo.lang =='zh_CN'? false: true},
				// { header: _lang.Sta.fOrigin, dataIndex: 'originPortEnName', width: 90, hidden:curUserInfo.lang !='zh_CN'? false: true },
				{ header: _lang.Sta.fContainerType, dataIndex: 'containerType', width: 90  ,
                    renderer: function(value){
                        var $containerType = _dict.containerType;
                        if(value  && $containerType.length > 0 && $containerType[0].data.options.length>0){
                            return $containerType[0].data.options[parseInt(value) - 1].title;
                        }
                    },
                },
				{ header: _lang.Sta.fContainerQty, dataIndex: 'containerQty', width: 90  },
                { header: _lang.Sta.fTotalCbm, dataIndex: 'totalCbm', width: 90  },
                { header: _lang.Sta.fTotalPackingCbm, dataIndex: 'totalPackingCbm', width: 90  },
                { header: _lang.Sta.fTotalShippingCbm, dataIndex: 'totalShippingCbm', width: 90  },
                { header: _lang.TText.fCurrency, dataIndex: 'currency', width: 60,
                    renderer: function(value){
                        var $currency =  _dict.currency;
                        if($currency.length>0 && $currency[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $currency[0].data.options);
                        }
                    }
                },
                {
                    header: _lang.Sta.fTotalPrice,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'contractRateAudToRmb', 'contractRateAudToUsd', null, { defaultRate: {
                                audToRmb: '',
                                audToUsd : '',
                            }})
                        },
                        {
                            header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this,'totalSalesPriceAud','totalSalesPriceRmb','totalSalesPriceUsd', null, {
                                edit: false,
                                width: 80,
                            })
                        },
                    ]
                },
                { header: _lang.Sta.fContractTotalPrice,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'contractRateAudToRmb', 'contractRateAudToUsd', null, {defaultRate: {
                                audToRmb: '',
                                audToUsd : '',
                            }})
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'totalPriceAud','totalPriceRmb','totalPriceUsd', null, {
                                edit:false,
                                width: 80,
                            })
                        },
                    ]
                },
                { header: _lang.Sta.fWriteOff,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,'contractRateAudToRmb', 'contractRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'writeOffAud','writeOffRmb','writeOffUsd', null, {edit:false, width: 80})
                        },
                    ]
                },
                { header: _lang.Sta.fFinalTotalPrice,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'contractRateAudToRmb', 'contractRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'finalTotalPriceAud','finalTotalPriceRmb','finalTotalPriceUsd', null, {edit:false, width: 80})
                        },
                    ]
                },
                { header: _lang.Sta.fDepositType, dataIndex: 'depositType', width: 60,
                    renderer: function(value){
                        var $depositType =  _dict.depositType;
                        if($depositType.length>0 && $depositType[0].data.options.length>0){
                            return $dictRenderOutputColor(value, $depositType[0].data.options);
                        }
                    }
                },
				{ header: _lang.Sta.fDepositRate, dataIndex: 'depositRate', width: 50  },


                { header: _lang.Sta.fDeposit,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'depositRateAudToRmb', 'depositRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'depositAud','depositRmb','depositUsd', null, {edit:false})
                        },
                    ]
                },
                { header: _lang.Sta.fDepositDate, dataIndex: 'depositDate', width: 140  },

                { header: _lang.Sta.fBalance,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'balanceRateAudToRmb', 'balanceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'balanceAud','balanceRmb','balanceUsd', null, {edit:false, width: 85})
                        },
                    ]
                },

                { header: _lang.Sta.fBalanceDate, dataIndex: 'estimatedBalance', width: 140  },
                { header: _lang.VendorDocument.fBalancePaymentTerm, dataIndex: 'creditTerms', width: 110,
                    renderer: function(value){
                        if(value) {
                            var $balancePaymentTerm = _dict.getValueRow('balancePaymentTerm', value);
                            if($balancePaymentTerm == undefined) return '';
                            if (curUserInfo.lang == 'zh_CN') {
                                return $balancePaymentTerm.cnName;
                            } else {
                                return $balancePaymentTerm.enName;
                            }
                        }
                    }
                },

                { header: _lang.VendorDocument.fExWork,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,'contractRateAudToRmb', 'contractRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'exWorkAud','exWorkRmb','exWorkUsd', null, {edit:false, width: 85})
                        },
                    ]
                },


                { header: _lang.Sta.fShippingAgent, dataIndex: 'serviceProviderCnName', width: 90 ,hidden:curUserInfo.lang =='zh_CN'? false: true  },
				{ header: _lang.Sta.fShippingAgent, dataIndex: 'serviceProviderEnName', width: 90 ,hidden:curUserInfo.lang !='zh_CN'? false: true  },
                { header: _lang.Sta.fAgentNotificationDate, dataIndex: 'agentNotificationDate', width: 140  },
                { header: _lang.Sta.fReadyDate, dataIndex: 'readyDate', width: 140  },
                { header: _lang.Sta.fEtd, dataIndex: 'etd', width: 140  },

                { header: _lang.Sta.fEta,
                    columns: [
                        { header: _lang.Sta.fEtaTime, dataIndex: 'eta', width: 140  },
                        { header: _lang.VendorDocument.fMonthEta, dataIndex: 'monthEta', width: 50  },
                        { header: _lang.VendorDocument.fYearEta, dataIndex: 'yearEta', width: 50  },
                    ]
                },
                { header: _lang.Sta.fLeadTime, dataIndex: 'leadTime', width: 60  },

                { header: _lang.Sta.fDaysToArrival, dataIndex: 'arrivalDays', width: 60  },
                { header: _lang.Sta.fDelivery, dataIndex: 'deliveryTime', width: 140  },
				{ header: _lang.Sta.fShippingDocReceived, dataIndex: 'shippingDocReceivedDate', width: 140  },
				{ header: _lang.Sta.fShippingDocForwarded, dataIndex: 'shippingDocForwardedDate', width: 140  },


                { header: _lang.Sta.fDomesticFreight,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this, 'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'chargeItemFeeAud','chargeItemFeeRmb','chargeItemFeeUsd', null, {edit:false})
                        },
                    ]
                },

                { header: _lang.Sta.fSeaFreight,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'portFeeAud','portFeeRmb','portFeeUsd', null, {edit:false, width: 80})
                        },
                    ]
                },
                { header: _lang.Sta.fSeaFreightInsurance,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'freightInsuranceAud','freightInsuranceRmb','freightInsuranceUsd', null, {edit:false, width: 80})
                        },
                    ]
                },
                { header: _lang.Sta.fFreightGST,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'freightGstAud','freightGstRmb','freightGstUsd', null, {edit:false, width: 80})
                        },
                    ]
                },
				// { header: _lang.Sta.fFXRate, dataIndex: 'rateAudToUsd', width: 140  },

                { header: _lang.Sta.fTotalFreight,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'totalFreightAud','totalFreightRmb','totalFreightUsd', null, {edit:false, width: 90})
                        },
                    ]
                },
                { header: _lang.Sta.fDuty,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'serviceRateAudToRmb', 'serviceRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'tariffAud','tariffRmb','tariffUsd', null, {edit:false})
                        },
                    ]
                },

                { header: _lang.Sta.fGst,
                    columns: [

                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'costRateAudToRmb', 'costRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'gstAud','gstRmb','gstUsd', null, {edit:false, width: 80})
                        },
                    ]
                },

                { header: _lang.Sta.fElectronicProcessingFee,
                    columns: [
                        {
                            header: _lang.NewProductDocument.fExchangeRate,
                            columns: $groupExchangeColumns(this,  'contractRateAudToRmb', 'contractRateAudToUsd')
                        },
                        { header: _lang.Sta.fPrice,
                            columns: new $groupPriceColumns(this, 'electronicProcessingFeeAud','electronicProcessingFeeRmb','electronicProcessingFeeUsd', null, {edit:false})
                        },
                    ]
                },
                { header: _lang.Sta.fTelexReleased, dataIndex: 'telexReleased', width: 60,
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                        if(value == '0') return $renderOutputColor('gray', _lang.TText.vNo);
                    }
                },
                { header: _lang.Sta.fShipmentTime, dataIndex: 'arrivalDays', width: 60  },
                { header: _lang.Sta.fBuyerCnName, dataIndex: 'creatorCnName', width: 90 ,hidden:curUserInfo.lang =='zh_CN'? false: true  },
                { header: _lang.Sta.fBuyerCnName, dataIndex: 'creatorEnName', width: 90 ,hidden:curUserInfo.lang !='zh_CN'? false: true  },
                { header: _lang.TText.fDepartmentName, dataIndex: 'departmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true },
                { header: _lang.TText.fDepartmentName, dataIndex: 'departmentEnName', width: 160, hidden:curUserInfo.lang !='zh_CN'? false: true }
            ],// end of column
    		//appendColumns: $groupGridCreatedColumns({sort:false,assignee:false}),
            // itemclick: function(obj, record, item, index, e, eOpts){
				// this.scope.rowClick.call(this.scope, record, item, index, e, conf);
            // }
		});

    },// end of the init

});