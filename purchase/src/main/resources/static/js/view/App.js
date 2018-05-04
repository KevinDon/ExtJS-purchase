Ext.ns("App");
Ext.define('App.Lang', {
	alias: 'App.Lang',
	TButton: {},
    TText: {}
});

Ext.define('App.Dict',{
    alias: 'App.Dict',
    source: new $HpDictStore({code:'vendor', codeSub:'source'}),
    currency: new $HpDictStore({code:'transaction', codeSub:'currency'}),
    riskRating: new $HpDictStore({code:'check', codeSub:'product'}),
    flowStatus: new $HpDictStore({code:'workflow', codeSub:'flow_status'}),
    flowHistoryStatus: new $HpDictStore({code:'workflow', codeSub:'history_status'}),
    //起始港
    origin: new $HpDictStore({code:'service_provider', codeSub:'origin_port'}),
    //目的港
    destination: new $HpDictStore({code:'service_provider', codeSub:'destination_port'}),
    chargeItem: new $HpDictStore({code:'service_provider', codeSub:'charge_item'}),
    feeType: new $HpDictStore({code:'service_provider', codeSub:'fee_type'}),
    feeItem: new $HpDictStore({code:'service_provider', codeSub:'fee_item'}),
    chargeUnit: new $HpDictStore({code:'service_provider', codeSub:'unit_of_operation'}),
    containerType: new $HpDictStore({code:'service_provider', codeSub:'container_type'}),
    shippingMethod: new $HpDictStore({code:'service_provider', codeSub:'shipping_method'}),
    quotationType: new $HpDictStore({code:'service_inquiry', codeSub:'quotation_type'}),
    department: new $HpDictStore({code:'contacts', codeSub:'department'}),
    position: new $HpDictStore({code:'contacts', codeSub:'position'}),
    shared: new $HpDictStore({code:'document', codeSub:'shared'}),
	refundType:new $HpDictStore({code:'purchase', codeSub:'refund_type'}),
    optionsYesNo:new $HpDictStore({code:'options', codeSub:'yesno'}),
	optionsIndoorOutdoor:new $HpDictStore({code:'options', codeSub:'indoor_outdoor'}),
	paymentStatus: new $HpDictStore({code:'payment', codeSub:'payment_status'}),
    orderQcStatus: new $HpDictStore({code:'contacts', codeSub:'order_qc_status'}),
    ruleSku: new $HpDictStore({code:'rule', codeSub:'sku'}),
    handleMode: new $HpDictStore({code:'product_problem', codeSub:'handle_mode'}),
    paymentTerms: new $HpDictStore({code:'purchase', codeSub:'payment_terms'}),
    settlementType: new $HpDictStore({code:'purchase', codeSub:'settlement_type'}),
    feeOtherItem: new $HpDictStore({code:'product', codeSub:'fee_other_item'}),
    depositType: new $HpDictStore({code:'purchase', codeSub:'deposit_rate'}),
    balancePaymentTerm: new $HpDictStore({code:'purchase', codeSub:'balance_payment_term'}),
    sync: new $HpDictStore({code:'product', codeSub:'sync'}),
    syncStatus: new $HpDictStore({code:'global', codeSub:'sync'}),
    customProcessing: new $HpDictStore({code:'product_cost', codeSub:'custom_processing'}),
    getValue: function(key, index){
        if(this[key] && this[key][0].data){
            var obj = this[key][0].data;
            for(var i=0; i< obj.options.length; i++){
                if(index == i){
                    return obj.options[i].value;
                }
            }
        }
    },

	getValueRow: function(key, value){
    	if(this[key] && this[key][0].data){
            var obj = this[key][0].data;
    		for(var i=0; i< obj.options.length; i++){
				if(obj.options[i].value == value){
					return {
						id: obj.options[i].value,
                        cnName: obj.options[i].desc[0].lang == 'zh_CN' ? obj.options[i].desc[0].text : obj.options[i].desc[1].text,
                        enName: obj.options[i].desc[0].lang != 'zh_CN' ? obj.options[i].desc[0].text : obj.options[i].desc[1].text,
                    };
				}
			}
		}
	},
	getItemsByCode: function(codeMain, codeSub){
		for(var index in this) {
			if(typeof(this[index]) == 'object' && this[index][0] !=undefined && this[index][0].data) {
                if(codeMain == this[index][0].data.codeMain && codeSub == this[index][0].data.codeSub){
                	return this[index];
				}
            }
        }
	}
});

var _lang = Ext.create('App.Lang');
var _dict = Ext.create('App.Dict');

//用户信息
var UserInfo=function(user){
	this.id=user.id;
	this.loginname=user.account;
	this.cnName=user.cnName;
	this.enName=user.enName;
	this.email=user.email;
	this.gender=user.gender; 
	this.depId=user.departmentId;
	this.depName=user.department.title;
	this.department=user.department;
	this.qq=user.qq;
	this.wechat=user.wechat;
	this.skype=user.skype;
	this.phone=user.phone;
	this.pageSize = user.setListRows;
	this.dateFormat=user.dateFormat;
	this.timezone=user.timezone;
	this.lang=user.lang;
	this.permission=user.roles;
	this.portals=user.portals;
	this.topModules=user.resource;
	this.sysSupport=user.sysSupport;
	this.audToRmb = user.audToRmb || 6;
	this.audToUsd = user.audToUsd || 0.9;
	this.countMessageNew = user.countMessageNew;
	this.countEmailNew = user.countEmailNew;
	this.countTaskNew = user.countTaskNew;
};


App.init = function() {
	var publicTimer=null;
	
	Ext.QuickTips.init();
	Ext.tip.QuickTipManager.init();
    Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
        maxWidth: 300,
        minWidth: 60,
		width: 'auto',
        showDelay: 50      // Show 50ms after entering target
    });

	Ext.KeyNav.enable;
	
	Ext.BLANK_IMAGE_URL=__ctxPath+'images/s.gif';
	
	//ajax加载提示
	Ext.Ajax.on('beforerequest', function(conn, options, eOpts ){
		if (options.loadMask && !this.loadMask) {
			if(options.maskTo){
				this.loadMask = new Ext.LoadMask(Ext.getCmp(options.maskTo), {msg:_lang.TText.loading});
			}else{
				this.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:_lang.TText.loading});
			}
			this.loadMask.show();
		}
	}, this);
	
	//ajax加载完成时删除提示
	Ext.Ajax.on('requestcomplete', function(conn, response, options, eOpts ){
		if (options.loadMask && this.loadMask) {
			this.loadMask.hide();
			this.loadMask = null;
		}		
		if (response && response.getResponseHeader){
		    if(response.getResponseHeader('__timeout')) {
		    	Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noSession);
	        	window.location.href=__ctxPath+'?randId=' + parseInt(1000*Math.random());
	    	}
		}
	}, this);
	
	Ext.Ajax.on('requestexception', function( conn, response, options, eOpts ){
		if (options.loadMask && this.loadMask) {
			this.loadMask.hide();
			this.loadMask = null;
		}
		if (response && response.getResponseHeader){
			if(response.status == '500'){
				if(!options.url.indexOf("logout"))
					Ext.ux.Toast.msg(_lang.TText.titleClew, _lang.TText.rsErrorPage, options.url);
	    	}else if(response.status == '404'){
	    		Ext.ux.Toast.msg(_lang.TText.titleClew, _lang.TText.rsErrorNoPage, options.url);
	    	}else if(response.status == '403'){
	    		Ext.ux.Toast.msg(_lang.TText.titleClew,_lang.TText.rsErrorNoPermission,options.url);
	    	}
		}
	}, this);
	
	try{
		var object=Ext.JSON.decode(userInfo);
		curUserInfo=new UserInfo(object);
		new IndexPage();
        App.iniDesktop.call(this, curUserInfo, true);
		setTimeout(function(){
			App.clickTopTab('AppHome');
			Ext.QuickTips.init();
		}, 300);
		
		publicTimer = setInterval(App.timerRun, 60000, this);
		
	}catch(e){
		console.log(e);
	}
	
};

App.clickNode = function(node, record, item, index, e, eOpts) {
	var nodeid = node.id;
	if(nodeid==null || nodeid==''){ return; }

	if(record.raw.type== 1){
		App.clickTopTabUrl(record.raw);
	}else if(record.raw.type == 2){
		App.clickTopTab(record.raw.url);
	}else{
		
	}
};

/**
 * 开启tab
 * @param jsId
 * @param {} params
 * @param {} precall 执行前函数
 * @param {} callback 回调函数
 */
App.clickTopTab = function(jsId, params, precall, callback){
	if(precall != null) { precall.call(this); }
	
	var tabs = Ext.getCmp('centerTabPanel');
	try{
		var tabItem = null;
		tabs.items.each(function(item){
			if(item.getId()==jsId){tabItem=item;}
		});
		
		if (tabItem == null) {
			$ImportJs(jsId, function(view) {
				tabItem = tabs.add(view);
				tabs.setActiveTab(tabItem);
			},params);
		}else {
			if(precall!=null){precall.call(this);}
			tabs.setActiveTab(tabItem);
            !!tabItem.gridPanel? tabItem.gridPanel.getStore().reload(): '';
            if(callback!=null){callback.call(this);}
		}
	
	}catch(e){
		console.log(e);
    }
};


App.clickTopTabUrl = function(node){
	if (node.id == null || node.id == '') {	return;	}

	var tabs = Ext.getCmp('centerTabPanel');
	var _url = node.url;
	if(!(_url.substring(0,5)=="http:")){
		_url = __ctxPath + _url;
	}
	
	if(node.params){
		_url += '?';
		for(var key in node.params){
			_url += key + '=' + node.params[key] + "&";
		}
	}
	
	try{
		var tabItem = null;
		tabs.items.each(function(item){
			if(item.getId()==node.path + '-'+ node.id){
				tabItem=item;
			}
		});
		
		if (tabItem == null) {
			tabItem = tabs.add( {
				xtype : 'uxiframe',
				title : node.text,
				id : node.path + '-'+ node.id,
				iconCls : node.iconCls,
				layout: 'fit',
				scope: this,
				border: false,
				url: _url,
				autoScroll: false,
				loadMask: _lang.TText.loading
			});
		}
		tabs.setActiveTab(tabItem);
	}catch(e){
		console.log(e);
	}
	
	
};

/**
 * 注销系统
 */
App.Logout = function(){
	var url = __ctxPath + 'logout';
	var me = this;
	me.__ctxPath = __ctxPath.replace('//', '/');
	Ext.Msg.confirm(_lang.TText.titleConfirm, _lang.TText.sureLogout, function(btn) {
		if (btn == 'yes') {
			Ext.Ajax.request({
				url : url,
				success : function(){ window.location.href = me.__ctxPath; },
				failure: function(){ window.location.href = me.__ctxPath; }
			});
		}
	});
};

/**
 * 退出系统
 */
App.runOnUnload = function(){
	Ext.Ajax.request({
		url : __ctxPath + 'logout',
		success : function(){
			window.location.href = __ctxPath + '/';
		}
	});
};

/**
 * 定时执行
 */
App.timerRun = function(notPop){
	Ext.Ajax.request({
		url : __ctxPath + 'pub/timerrun',
		success : function(response, options){
			var result = Ext.JSON.decode(response.responseText);
            App.iniDesktop.call(this, result.data, result.success, notPop);
		}
	});
};

var _checkobj = null;
App.iniDesktop=function(data, result, notPop){
    var $_myCountMessageNew = jQuery('#myCountMessageNew');
    var $_myCountEmailNew = jQuery('#myCountEmailNew');
    if(result == true){
        $_myCountMessageNew.html(data.countMessageNew);
        $_myCountEmailNew.html(data.countEmailNew);
        data.countMessageNew > 0 ? $_myCountMessageNew.addClass('new') : $_myCountMessageNew.removeClass('new');
        data.countEmailNew > 0 ? $_myCountEmailNew.addClass('new') : $_myCountEmailNew.removeClass('new');
        curUserInfo.audToRmb = data.audToRmb;
        curUserInfo.audToUsd = data.audToUsd;

        var cmp =Ext.getCmp('RatePortalViewTodayForm');
        if(!! cmp) {
            cmp.getCmpByName('rate_audToRmb').setValue(curUserInfo.audToRmb);
            cmp.getCmpByName('rate_audToUsd').setValue(curUserInfo.audToUsd);
        }

        var systemTitle = 'Newaim\'s Purchase';

        if(!notPop && (data.countMessageNew > 0 || data.countTaskNew>0)){
            Ext.ux.Toast.msgwin(_lang.TText.titleClew, _lang.TText.hasMessageBox , data.countMessageNew||0, data.countTaskNew||0);

            if(_checkobj == null)
                _checkobj = setInterval(function() {
                    if (/NEW/.test(document.title) == false) {
                        document.title = '【 NEW 】 '+ systemTitle;
                    } else {
                        document.title = '【　  　】 '+ systemTitle;
                    }
                }, 500);
        }else{
            clearInterval(_checkobj);
            _checkobj= null;
            document.title = systemTitle;
        }

    }else{
        $_myCountMessageNew.html('0').removeClass('new');
        $_myCountEmailNew.html('0').removeClass('new');
    }
}
Ext.onReady(App.init);
