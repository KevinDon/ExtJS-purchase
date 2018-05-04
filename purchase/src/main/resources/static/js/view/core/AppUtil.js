Ext.ns("Ext.ux");
Ext.ns("AppUtil");
Ext.ns("Ext.ux.Toast");
Ext.ns("HP");

// 用于标记已经加载过的js库
var jsCache = new Array();


function setCookie(name, value, expires, path, domain, secure) {
	document.cookie = name + "=" + escape(value)
		+ ((expires) ? "; expires=" + expires.toGMTString() : "")
		+ ((path) ? "; path=" + path : "")
		+ ((domain) ? "; domain=" + domain : "")
		+ ((secure) ? "; secure" : "");
};

function $ImportJs(viewName,callback,params) {
	var b = jsCache[viewName];

	if (b != null) {
		var view =newView(viewName,params);
		callback.call(this, view);
	} else {
		var jsArr = eval('App.importJs.' + viewName);

		if(jsArr==undefined || jsArr.length==0){
			try{
				var view = newView(viewName,params);
				callback.call(this, view);
			}catch(e){
			}
			return ;
		}
		ScriptMgr.load({
			scripts : jsArr,
			callback : function() {
				jsCache[viewName]=0;
				var view = newView(viewName,params);
				callback.call(this, view);
			}
		});
	}
};

/**
 * 加载的js,并调用回调函数
 * @param {} jsArr
 * @param {} callback
 */
function $ImportSimpleJs(jsArr,callback,scope){
	ScriptMgr.load({
		scripts : jsArr,
		scope:scope,
		callback : function() { if(callback){ callback.call(this);}}
	});
};

/**
 * This function is used to get cookies
 */
function getCookie(name) {
	var prefix = name + "=";
	var start = document.cookie.indexOf(prefix);

	if (start == -1) return null;

	var end = document.cookie.indexOf(";", start + prefix.length);
	if (end == -1) 	end=document.cookie.length;

	var value = document.cookie.substring(start + prefix.length, end);
	return unescape(value);
};

function deleteCookie(name, path, domain) {
	if (getCookie(name)) {
		document.cookie = name + "=" + ((path) ? "; path=" + path : "")	+ ((domain) ? "; domain=" + domain : "") + "; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
};


function setHpInterval(fn,timer){
	try{clearInterval(interval);}catch(e){}
	interval=window.setInterval(fn,timer);
};

// json store
var $HpStore = function(conf) {
	var filterParam = '';
	if(conf.fields){
		for (var i=0; i<conf.fields.length; i++){
			var tempStr = (typeof(conf.fields[i]) == 'object')? conf.fields[i].name : conf.fields[i];
			filterParam += "," + ((tempStr.indexOf('_')>0) ? tempStr.replace('_', '.') : tempStr);
		}
		filterParam = filterParam.substr(1);
		
		if (! conf.baseParams) conf.baseParams={};
		conf.baseParams.filter = filterParam;

	}
	conf.store = new HP.JsonStore({
		storeId: conf.storeId,
		proxy: {
			url : conf.url,
			type: 'ajax',
			actionMethods: {create:'POST', read:'POST', update:'POST', destroy:'POST'},
			reader: {
	            type: 'json',
	            root : conf.root ? conf.root : 'data',
	            totalProperty: 'tc'
	        },
	        extraParams: conf.baseParams
		},
		pageSize: conf.pageSize,
		defaultProxyType: 'ajax',
		fields : conf.fields,
        sorters: !!conf.sorters? conf.sorters: '',
		groupField: conf.groupField,
        remoteGroup: conf.groupField? true :false,
		remoteSort : conf.rsort === undefined ? true : conf.rsort ,
		remoteFilter: conf.remoteFilter === undefined ? true : conf.remoteFilter ,
		autoLoad: conf.autoLoad === undefined ? true : conf.autoLoad,
	});

    var myMask = null;
    if (conf.maskTo || false) {
    	myMask = new  Ext.LoadMask ( conf.maskTo || this, { msg: _lang.TText.loading } );
        myMask.show();
    }
	if (conf.url && (conf.autoLoad || conf.autoLoad === undefined)) {
		conf.store.on('load',function(e, records, successful, eOpts, ex){
			if( !successful) {
				e.removeAll();
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorSystem);
			}else{
				if (typeof conf.callback == 'function'){
					conf.callback.call(conf.scope, conf, records, eOpts, this);
				}
			}
			if(myMask) myMask.hide();
		}); 
	}
	return conf.store;
};

// tree store
var $HpTreeStore = function(conf) {
	var filterParam = '';
	if(conf.fields){
		for (var i=0; i<conf.fields.length; i++){
			var tempStr = (typeof(conf.fields[i]) == 'object')? conf.fields[i].name : conf.fields[i];
			filterParam += "," + ((tempStr.indexOf('_')>0) ? tempStr.replace('_', '.') : tempStr);
		}
		filterParam = filterParam.substr(1);
		
		if(! conf.baseParams) conf.baseParams={};
		conf.baseParams.filter = filterParam;
	}
	conf.store = new HP.TreeStore({
		storeId: conf.storeId,
		proxy: {
			url : conf.url,
			type: 'ajax',
			actionMethods:'POST',
			reader: {
	            type: 'json',
	            root : conf.root ? conf.root : 'data',
	            totalProperty: 'tc'
	        },
	        extraParams: conf.baseParams
		},
		defaultProxyType: 'ajax',
		fields : conf.fields,
		groupField: conf.groupField,
        remoteGroup: conf.groupField? true :false,
		remoteSort : conf.rsort === undefined ? true : conf.rsort ,
		remoteFilter: conf.remoteFilter === undefined ? true : conf.remoteFilter ,
		autoLoad: conf.autoLoad === undefined ? true : conf.autoLoad
	});

    var myMask = null;
    if (conf.maskTo || false) {
        myMask = new  Ext.LoadMask ( conf.maskTo || this, { msg: _lang.TText.loading } );
        myMask.show();
    }
	if (conf.url && (conf.autoLoad || conf.autoLoad === undefined)) {
		conf.store.on('load',function(tempstore, records, successful, eOpts){
			if( !successful) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorSystem);
			}else{
				if (typeof conf.callback == 'function'){
					conf.callback.call(conf.scope, conf, records, eOpts, this);
				}
			}
            if(myMask) myMask.hide();
		}); 
	}
	return conf.store;
};

var $HpDictStore = function(conf){
	return $HpStore({
		fields: ['id','title','codeMain','codeSub','options'],
		url:__ctxPath + 'dict/getkey?code='+conf.code+'&codeSub='+conf.codeSub+''
	}).data.items;
};

//search form
var $HpSearch = function(conf) {
	var baseParams = [];
	
	if(conf.searchPanel){
		baseParams = $_HpSearchFromSerializeForm(conf.searchPanel, true);
	}

	if(conf.searchQuery){
		if(baseParams){
			for(var key in conf.searchQuery){
				baseParams[key] = conf.searchQuery[key].toString();
			}
		}else{
			var tempParams = '';
			for(var key in conf.searchQuery){
				tempParams += ',"'+key.toString() + '":"' + conf.searchQuery[key]+ '"';
			}
			tempParams = tempParams.substring(1);
			baseParams = Ext.JSON.decode("{" + tempParams + "}");
		}
	}

	var filter = conf.gridPanel.store.proxy.extraParams.filter;
	
	if(baseParams){
		conf.gridPanel.store.proxy.extraParams=baseParams;
	}else{
		conf.gridPanel.store.proxy.extraParams={};
	}
	conf.gridPanel.store.proxy.extraParams.filter = filter;
	conf.gridPanel.store.loadPage(1);

	if (typeof conf.callback == 'function'){
		conf.callback.call(conf.scope, conf);
	}
};


//提交表单
var $postForm = function(conf) {
	var form = conf.formPanel.getForm();

	if (!form.isValid()) {
		Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
		return;
	}

    var myMask = new  Ext.LoadMask ( conf.formPanel.id, { msg: _lang.TText.loading } );
    myMask.show();
	var scope = conf.scope ? conf.scope : this;

    //init params
    $_initValueForSubmit(form.getFields());

	form.submit({
		scope : scope,
		url : conf.url,
		method : 'post',
		params : conf.params,
		success : function(fp, action) {
			if(typeof(action.result) == 'string'){
				result = Ext.JSON.decode(action.result);
			}else{
				result = action.result;
			}
			if(!Ext.isEmpty(result.msg))
				Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
            else
                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSuccess);
			if (conf.callback) conf.callback.call(scope, fp, action, true, conf.grid||null);
			if (conf.grid) conf.grid.getStore().reload();
			if (conf.topGrid)	conf.topGrid.getStore().reload();
			if (conf.success) conf.success.call(scope, fp, action);
            myMask.hide();
		},
		failure : function(fp, action) {
			result = action.result;
			if (!Ext.isEmpty(result) && !Ext.isEmpty(result.msg))
				Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
			else
                Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorFailure);
            if (conf.failure) conf.failure.call(scope, fp, action);
			if (conf.callback) conf.callback.call(scope, fp, action, false, conf.grid||null);
            myMask.hide();
		}
	});
};


var $_initValueForSubmit = function (fields) {
    var items = fields.items;
    if (!!items  && items.length>0) {
        for(var index in items){
            if(items[index].emptyText != ''){
                items[index].emptyText = '';
            }
        }
    }
};

//编辑记录
var $editGridRs = function(conf){
	var ids = $getGdSelectedIds(conf);
	if (ids.length == 0) {
		Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		return;
	}if(ids.length >1){
		Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.onlyOneRecord);
		return;
	}else{
		var def={
			scope : conf.scope,
			fileId : ids[0]
		};
		Ext.applyIf(def, conf);
		if(conf.winForm) new conf.winForm(def).show();
	}
	
	return ids;
};


//删除记录
var $delGridRs = function(conf) {
	var ids = $getGdSelectedIds(conf);
	if (ids.length == 0) {
		Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.noRecord);
		return;
	}
	var params = {
		msg : conf.msg,
		url : conf.url,
		ids : ids,
		// ids : ids.join(','),
		scope : conf.scope,
		grid : conf.grid,
		topGrid : conf.topGrid,
		callback : conf.callback
	};
	// 删除所选记录
	$postDel(params);
};

var $postDel = function(conf) {
	var msg = conf.msg == null ? _lang.TText.sureDeleteRecord : conf.msg;
	Ext.Msg.confirm(_lang.TText.titleConfirm, msg, function(btn) {
		if (btn == 'yes') {
			Ext.Ajax.request({
				url : conf.url,
				params : { ids : conf.ids},
				method : 'POST',
                loadMask: conf.maskTo? true: conf.loadMask? conf.loadMask: false,
                maskTo: conf.maskTo? conf.maskTo: null,
				scope : conf.scope,
				success : function(fp, options) {
					var result = Ext.JSON.decode(fp.responseText);
					if (result.success) {
						Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.deleteSuccess);
						if (conf.callback) conf.callback.call(conf.scope, fp, options);
						if (conf.grid) conf.grid.getStore().reload();
						if (conf.topGrid) conf.topGrid.getStore().reload();
					} else {
						Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
					}

				},
				failure : function(fp, options) {
					Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorSystem);
				}
			});
		}
	});
};

var $postUrl = function(conf) {
    var autoMessage = conf.autoMessage != undefined ? conf.autoMessage : true;

	Ext.Ajax.request({
		url : conf.url,
		params : conf.params,
		method : conf.method ? conf.method : 'POST',
        loadMask: conf.maskTo? true: conf.loadMask? conf.loadMask: false,
        maskTo: conf.maskTo? conf.maskTo: null,
		success : function(response, options) {
			var result = Ext.JSON.decode(response.responseText);
			if(autoMessage){
				if(result.msg){
					Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
				}else{
					if (result.success){ Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSuccess);}
					else{Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsError);}
				}
			}
			if (result.success) { if (conf.grid) conf.grid.getStore().reload();}
			if (conf.callback) {conf.callback.call(conf.scope, response, options);}
		},
		failure : function(response, options) {
            if (conf.callback) {conf.callback.call(conf.scope, response, options);}
			Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorSystem);
		}
	});
};

var $postList = function(conf) {
	var records = conf.grid.getStore().data.items;
	var req = [];
	if(records.length){
		for (var i=0 ; i<records.length; i++){
			req[i] = [];
			if(conf.fields){
				var line = {};
				for(var key in conf.fields){
					line[conf.fields[key]] = records[i].data[conf.fields[key]];
				}
				req[i]=line;
			}else{
				req[i]=records[i].data;
			}
		}
		var params = {};
		
		if(conf.params) params = conf.params;
		params['items'] = JSON.stringify(req);
		
		Ext.Ajax.request({
			url : conf.url,
			params : params,
			method : 'POST',
            loadMask: conf.maskTo? true: conf.loadMask? conf.loadMask: false,
            maskTo: conf.maskTo? conf.maskTo: null,
			success : function(response, options) {
				var result = Ext.JSON.decode(response.responseText);
				if(result.msg){
					Ext.ux.Toast.msg(_lang.TText.titleOperation, result.msg);
				}else{
					if (result.success){ Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsSuccess);}
					else{Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsError);}
				}
				if (result.success) { if (conf.grid) conf.grid.getStore().reload();}
				if (conf.callback) {conf.callback.call(conf.scope, response, options);}
			},
			failure : function(response, options) {
				Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsError);
			}
		});
	}else{
		Ext.ux.Toast.msg(_lang.TText.titleOperation, _lang.TText.rsErrorData);
	}
};


//serialize form fields 
var $_HpSearchFromSerializeForm = function(sPanel, outJson){
	var resultParams = "";
	var resultParamsJson = {};
	
	var els = sPanel.query();
	for(var item=0; item<els.length; item++){
		if(els[item].xtype != undefined) {
			if(outJson == true){
				//json format
				if(els[item].xtype == 'combo' && els[item].getValue() != undefined && els[item].getValue() != ''){
					resultParamsJson[els[item].hiddenName] = els[item].getValue();
				}else if(els[item].value != undefined && els[item].getValue() != ''){				
					resultParamsJson[els[item].name] = els[item].getValue();
				}
			}else{
				//get url format
				if(els[item].xtype == 'combo' && els[item].getValue() != undefined && els[item].getValue() != ''){
					resultParams += els[item].hiddenName + "=" + els[item].getValue() + "&";
				}else if(els[item].value != undefined && els[item].getValue() != ''){				
					resultParams +=  els[item].name + "=" + els[item].value + "&";
				}
			}
		}
	}
	els = null;
	
	if(outJson == true) 
		return resultParamsJson;
	else{
		if (resultParams.length > 1) {
			return resultParams.substring(0, resultParams.length-1);
		}
	}
};


var $_setByName = function(container, data, conf) {
	var items = container.items;
	if (!!items  && items.getCount) {
		for (var i = 0; i < items.getCount(); i++) {
			var comp = items.get(i);
			if (comp.items) {
				$_setByName(comp, data, conf);
				continue;
			}
			var xtype = comp.getXType();
			try {
				if (xtype == 'radiofield') {
					var val = $_getValueByName(comp.getName(), data, conf);
					comp.setValue(val == comp.inputValue);
				}else if (xtype == 'combo' || xtype=='combobox') {
					var val = $_getValueByName(comp.hiddenName, data, conf);
					comp.valueNotFoundText = val;
					if(typeof(val) == 'object'){
						comp.setLocalSource(val);
					}else{
						// console.log('A ' + xtype + ' ' + comp.getName() + ' ' + val)
						comp.setValue(val.toString());
					}
				}else if(xtype == 'component'){
					var val = $_getValueByName(comp.getRefOwner().getName(), data, conf);
					if (val == '') val = comp.getValue();
					comp.getRefOwner().setValue(val);
				}else if(xtype == 'dictcombo'){
                    var val = $_getValueByName(comp.getName(), data, conf);
                    if (val == '') val = comp.getValue();
                    comp.setValue(val.toString());
				}else{
					var val = $_getValueByName(comp.getName(), data, conf);
					if (val == '') val = comp.getValue();
					comp.setValue(val);
				}
			} catch (e) {
//				 alert(e);
			}
		}
	}
};

var $_eventAfterrenderForm = function(conf){
	for (var i = 0; i < conf.items.length; i++) {
		var comp = conf.items.get(i);
		if (comp.getXType() == 'radiogroup'){
			comp.setValue(comp.getValue());
		}
	}
};

//合并两个json对象为一个对象
var $_mergeJsonObject = function(jsonbject1, jsonbject2)
{
	var resultJsonObject={};
	for(var attr in jsonbject1){
		resultJsonObject[attr]=jsonbject1[attr];
	}
	for(var attr in jsonbject2){
		resultJsonObject[attr]=jsonbject2[attr];
	}

    return resultJsonObject;
};

var $permissionCheck = function(key){
	for(var item in curUserInfo.permission){
		if(key == curUserInfo.permission[item]) return true;
	}
};

//渲染状态
var $renderOutputStatus = function(value){
    if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
    else if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
    else if(value == '3') return $renderOutputColor('blue', _lang.TText.vDeleted);
    else if(value == '4') return $renderOutputColor('orange', _lang.TText.vCancel);
    else if(value == '0') return $renderOutputColor('red', _lang.TText.vDraft);
}
//渲染状态列
var $renderOutputStatusColumns = function(){
	return { header: _lang.TText.fStatus, dataIndex: 'status', width: 40,
        renderer: function(value){return $renderOutputStatus(value);}
    }
}

/**
 * 渲染checkcolumn 列
 * @param value
 * @param meta
 * @returns {string}
 */
var $renderOutputCheckColumns =  function(value, meta) {
    var cssPrefix = Ext.baseCSSPrefix, cls = [cssPrefix + 'grid-checkcolumn'];
    if (this.disabled) { meta.tdCls += ' ' + this.disabledCls; }
    if (value) { cls.push(cssPrefix + 'grid-checkcolumn-checked');}
    return '<img class="' + cls.join(' ') + '" src="' + Ext.BLANK_IMAGE_URL + '"/>';
}

//渲染 列显示的颜色
var $renderOutputColor = function(colorName, string){
	if(colorName){
		return '<span class="c-'+colorName+'">'+string+'</span>';
	}else{
		return '<span>'+string+'</span>';
	}
};

var $dictRenderOutputColor = function(label, labels, defaultColors){
	var colors = defaultColors !=undefined ? defaultColors : ['black', 'green', 'blue', 'red', 'gray', 'orange', 'pink', 'purple'];
	if(labels.length>0){
		for(var j=0; j<labels.length; j++){
			if(labels[j].value == label){
				return $renderOutputColor(colors[j], labels[j].title);
			}
		}
	}
};

var $renderGridDictColumn = function(value, dict, defaultColors){
    if(dict.length>0 && dict[0].data.options.length>0){
        return $dictRenderOutputColor(value, dict[0].data.options, defaultColors);
    }
};

var $fileSizeRenderOutput = function(value){
	if(value){
		if(value/1024/1024 >1){
			return (value/1024/1024).toFixed(2) + ' Mb';
		}else if(value/1024 >1){
			return (value/1024).toFixed(2) + ' Kb';
		}else{
			return value + ' Bytes';
		}
	}
};


var $setBtnDisabled = function(btnNames, value){
	if(btnNames.length>0){
		try{
			for(var j=0; j< btnNames.length; j++){
				if(this.action[btnNames[j]] && this.action[btnNames[j]].items.length >0){
					for(var i=0; i<this.action[btnNames[j]].items.length; i++){
						this.action[btnNames[j]].items[i].setDisabled(value);
					}
				}
			}
		}catch(e){}
	}
};

var $btnIsEnabled = function(btnName){
	if(this.action[btnName] && this.action[btnName].items.length >0){
		for(var i=0; i<this.action[btnName].items.length; i++){
			if(! this.action[btnName].items[i].isDisabled()){
				return true;
			}
		}
	}
};

/**
 *
 * @param scope
 * @param aud
 * @param rmb
 * @param usd
 * @param callback
 * @param options
 * @returns {[null,null,null]}
 */
var $groupPriceColumns = function(scope, aud, rmb, usd, callback, options){
	var options = options || {};
	var decimalPrecision = options.decimalPrecision || 2;
	var numberFormat = decimalPrecision == 3 ? '0.000': (decimalPrecision == 4 ? '0.0000' : '0.00');
    var defaultRate = options.defaultRate || {
        audToRmb: curUserInfo.audToRmb,
        audToUsd: curUserInfo.audToUsd,
    }
	
	if(options.edit == undefined && options.edit!=false){
		return [
			{ header: _lang.TText.fAUD, dataIndex: aud, sortable: true, scope:scope, width: options.width || 70, align: 'right',
				editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
					listeners:{
						change:function(pt, newValue, oldValue, eOpts ){
                            var cmp = Ext.getCmp(options.gridId);
                            var row = cmp.getSelectionModel().selected.getAt(0);
                            var audToRmb = cmp.audToRmb;
                            var audToUsd = cmp.audToUsd;
                            audToRmb = audToRmb || defaultRate.audToRmb;
                            audToUsd = audToUsd || defaultRate.audToUsd;

							row.set(aud, newValue.toFixed(decimalPrecision));
							row.set(rmb, (audToRmb * newValue).toFixed(decimalPrecision));
							row.set(usd, (audToUsd * newValue).toFixed(decimalPrecision));
							if(typeof callback == 'function') callback.call(this, row, newValue);
						}
					},
				},
                renderer: function (value, meta, recode) {
                    meta.tdCls = 'grid-input';
                    return Ext.util.Format.number(value, numberFormat);
                }
			},
			{ header: _lang.TText.fRMB, dataIndex: rmb, sortable: true, scope:scope, width: options.width || 70, align: 'right',
	        	editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
	        		listeners:{
		        		change:function(pt, newValue, oldValue, eOpts ){
                            var cmp = Ext.getCmp(options.gridId);
                            var row = cmp.getSelectionModel().selected.getAt(0);
                            var audToRmb = cmp.audToRmb;
                            var audToUsd = cmp.audToUsd;
                            audToRmb = audToRmb || defaultRate.audToRmb;
                            audToUsd = audToUsd || defaultRate.audToUsd;

		        			row.set(rmb, newValue.toFixed(decimalPrecision));
		        			row.set(aud, (newValue / audToRmb).toFixed(decimalPrecision));
		        			row.set(usd, (newValue / audToRmb * audToUsd).toFixed(decimalPrecision));
		        			if(typeof callback == 'function') callback.call(this, row, newValue);
		        		}
		        	}
	        	},
                renderer: function (value, meta, recode) {
                    meta.tdCls = 'grid-input';
                    return Ext.util.Format.number(value, numberFormat);
                }
			},
	        { header: _lang.TText.fUSD, dataIndex: usd, sortable: true, scope:scope, width: options.width || 70, align: 'right',
	        	editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
	        		listeners:{
		        		change:function(pt, newValue, oldValue, eOpts ){
                            var cmp = Ext.getCmp(options.gridId);
                            var row = cmp.getSelectionModel().selected.getAt(0);
                            var audToRmb = cmp.audToRmb;
                            var audToUsd = cmp.audToUsd;
                            audToRmb = audToRmb || defaultRate.audToRmb;
                            audToUsd = audToUsd || defaultRate.audToUsd;

		        			row.set(usd, newValue.toFixed(decimalPrecision));
		        			row.set(aud, (newValue / audToUsd).toFixed(decimalPrecision));
		        			row.set(rmb, (audToRmb * newValue /audToUsd).toFixed(decimalPrecision));
		        			if(typeof callback == 'function') callback.call(this, row, newValue);
		        		}
		        	}
	        	},
                renderer: function (value, meta, recode) {
                    meta.tdCls = 'grid-input';
                    return Ext.util.Format.number(value, numberFormat);
                }
			}
    	];
	}else{
		return [
			{ header: _lang.TText.fAUD, dataIndex: aud, sortable: true, width: options.width || 70, align: 'right', value: 0, hidden: !!options.audHidden? options.audHidden: false,
				renderer: function(v) {
                    var value = v == null || v < 0 ? 0.00 : v;
                    return Ext.util.Format.number(value, numberFormat);
                }
			},
			{ header: _lang.TText.fRMB, dataIndex: rmb, sortable: true, width: options.width || 70, align: 'right',  value: 0, hidden: !!options.rmbHidden? options.rmbHidden: false,
				renderer: function(v) {
					var value =v == null || v < 0 ? 0.00 : v;
                    return Ext.util.Format.number(value, numberFormat);
                }
			},
	        { header: _lang.TText.fUSD, dataIndex: usd, sortable: true, width: options.width || 70, align: 'right',  value: 0, hidden: !!options.usdHidden? options.usdHidden: false,
				renderer: function(v) {
                    var value = v == null || v < 0 ? 0.00 : v;
                    return Ext.util.Format.number(value, numberFormat);
                }
	        }
		];
	}
};

var $groupPriceFields = function(scope, params, callback){
    var updatePriceFlag = 'updatePriceFlag-' + Ext.id();
    params = params || {};
	return [
		{field: updatePriceFlag, xtype: 'hidden', id: updatePriceFlag, value: params.updatePriceFlag ? params.updatePriceFlag : 0},
        {
            field: params.aud.field, xtype: params.type|| 'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.aud.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false, fieldLabel: params.aud.fieldLabel, cls: params.cls || 'col-2', allowNegative : params.allowNegative,
            listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
                    var flag = Ext.getCmp(updatePriceFlag);
                    if (parseInt(flag.getValue()) == 1) return;

                    var audToRmb = !!params.audToRmb ? params.audToRmb : (!!form.rateAudToRmb &&  form.rateAudToRmb > 0 ? form.rateAudToRmb : curUserInfo.audToRmb);
                    var audToUsd = !!params.audToUsd ? params.audToUsd :  (!!form.rateAudToUsd &&  form.rateAudToUsd > 0 ? form.rateAudToUsd : curUserInfo.audToUsd);
                    flag.setValue(1);
                    form.getCmpByName(params.rmb.field).setValue((newValue * audToRmb).toFixed(params.decimalPrecision || 2));
                    form.getCmpByName(params.usd.field).setValue((newValue * audToUsd).toFixed(params.decimalPrecision || 2));
                    flag.setValue(0);
                }
            }
        },{
            field: params.rmb.field, xtype: params.type||  'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.aud.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false, fieldLabel: params.rmb.fieldLabel, cls: params.cls || 'col-2', allowNegative : params.allowNegative,
            listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
                    var flag = Ext.getCmp(updatePriceFlag);
                    if (parseInt(flag.getValue()) == 1) return;

                    var audToRmb = !!params.audToRmb ? params.audToRmb : (!!form.rateAudToRmb &&  form.rateAudToRmb > 0 ? form.rateAudToRmb : curUserInfo.audToRmb);
                    var audToUsd = !!params.audToUsd ? params.audToUsd :  (!!form.rateAudToUsd &&  form.rateAudToUsd > 0 ? form.rateAudToUsd : curUserInfo.audToUsd);
                    flag.setValue(1);
                    form.getCmpByName(params.aud.field).setValue((newValue / audToRmb).toFixed(params.decimalPrecision || 2));
                    form.getCmpByName(params.usd.field).setValue((newValue / audToRmb * audToUsd).toFixed(params.decimalPrecision || 2));
                    flag.setValue(0);
                }
            }
        },{
            field: params.usd.field, xtype: params.type||  'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.aud.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false,fieldLabel: params.usd.fieldLabel, cls: params.cls || 'col-2', allowNegative : params.allowNegative,
			listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
                    var flag = Ext.getCmp(updatePriceFlag);
                    if (parseInt(flag.getValue()) == 1) return;

                    var audToRmb = !!params.audToRmb ? params.audToRmb : (!!form.rateAudToRmb &&  form.rateAudToRmb > 0 ? form.rateAudToRmb : curUserInfo.audToRmb);
                    var audToUsd = !!params.audToUsd ? params.audToUsd :  (!!form.rateAudToUsd &&  form.rateAudToUsd > 0 ? form.rateAudToUsd : curUserInfo.audToUsd);
                    flag.setValue(1);
					form.getCmpByName(params.aud.field).setValue((newValue / audToUsd).toFixed(params.decimalPrecision || 2));
					form.getCmpByName(params.rmb.field).setValue((newValue * audToRmb / audToUsd).toFixed(params.decimalPrecision || 2));
                    flag.setValue(0);
                }
            }
        }
	]
};

var $groupvVlumeFields = function(scope, params, callback){
    params = params || {};
    return [
        {
            field: params.length.field, xtype: params.type|| 'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.length.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false, fieldLabel: params.length.fieldLabel, cls: params.cls || 'col-2',
            listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
					if(!form.getCmpByName(params.width.field).getValue() ||  !form.getCmpByName(params.height.field).getValue()){
						return;
					}
					var cbm = newValue * form.getCmpByName(params.width.field).getValue() * form.getCmpByName(params.height.field).getValue();
                    form.getCmpByName(params.cmb.field).setValue((cbm/1000000).toFixed(4));
                    form.getCmpByName(params.cubicWeight.field).setValue((cbm/1000000).toFixed(2) * 250);
                }
            }
        },{
            field: params.width.field, xtype: params.type||  'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.width.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false, fieldLabel: params.width.fieldLabel, cls: params.cls || 'col-2',
            listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
                    if(!form.getCmpByName(params.length.field).getValue() ||  !form.getCmpByName(params.height.field).getValue()){
                        return;
                    }
                    var cbm = newValue * form.getCmpByName(params.length.field).getValue() * form.getCmpByName(params.height.field).getValue();
                    form.getCmpByName(params.cmb.field).setValue((cbm/1000000).toFixed(4));
                    form.getCmpByName(params.cubicWeight.field).setValue((cbm/1000000).toFixed(2) * 250);
                }
            }
        },{
            field: params.height.field, xtype: params.type||  'numberfield', decimalPrecision: params.decimalPrecision || 2, minValue: 0, value: params.height.value,
            readOnly: params.readOnly || false, allowBlank: params.allowBlank || false,fieldLabel: params.height.fieldLabel, cls: params.cls || 'col-2',
            listeners: {
                change: function (field, newValue, oldValue) {
                    var form = field.up('form');
                    if(!form.getCmpByName(params.length.field).getValue() ||  !form.getCmpByName(params.width.field).getValue()){
                        return;
                    }
                    var cbm = newValue * form.getCmpByName(params.width.field).getValue() * form.getCmpByName(params.length.field).getValue();
                    form.getCmpByName(params.cmb.field).setValue((cbm/1000000).toFixed(4));
                    form.getCmpByName(params.cubicWeight.field).setValue((cbm/1000000).toFixed(2) * 250);
                }
            }
        }
    ]
};



/**
 * 带旧价格的价格显示列
 * @param scope
 * @param aud
 * @param rmb
 * @param usd
 * @param callback
 * @param options
 * @returns {[null,null,null]}
 */
var $groupPriceWithOldColumns = function(scope, aud, rmb, usd, prevAud, prevRmb, prevUsd, callback, options){
    var options = options || {};
    var decimalPrecision = options.decimalPrecision || 3;
    var numberFormat =  options.numberFormat || '0.000';
    var defaultRate = options.defaultRate || {
        audToRmb: curUserInfo.audToRmb,
        audToUsd: curUserInfo.audToUsd,
    }

    if(options.edit == undefined || options.edit == true){
        return [
            { header: _lang.TText.fAUD, columns: [
				{ header: _lang.TText.fNew, dataIndex: aud, sortable: true, scope:scope, width: 68, align: 'right',
					editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
						listeners:{
							change:function(pt, newValue, oldValue, eOpts ){
								var cmp = Ext.getCmp(options.gridId);
								var row = cmp.getSelectionModel().selected.getAt(0);
								var audToRmb = cmp.audToRmb;
								var audToUsd = cmp.audToUsd;
                                audToRmb = audToRmb || defaultRate.audToRmb;
                                audToUsd = audToUsd || defaultRate.audToUsd;

								row.set(aud, newValue.toFixed(decimalPrecision));
								row.set(rmb, (audToRmb * newValue).toFixed(decimalPrecision));
								row.set(usd, (audToUsd * newValue).toFixed(decimalPrecision));
								if(typeof callback == 'function') callback.call(this, row, newValue);
							}
						},
					},
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        if(value) return Ext.util.Format.number(value, numberFormat);
                    }
				},
                { header: _lang.TText.fOld, dataIndex: prevAud, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
			]},
            { header: _lang.TText.fRMB, columns: [
				{ header: _lang.TText.fNew, dataIndex: rmb, sortable: true, scope:scope, width: 68, align: 'right',
					editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
						listeners:{
							change:function(pt, newValue, oldValue, eOpts ){
                                var cmp = Ext.getCmp(options.gridId);
                                var row = cmp.getSelectionModel().selected.getAt(0);
                                var audToRmb = cmp.audToRmb;
                                var audToUsd = cmp.audToUsd;
                                audToRmb = audToRmb || defaultRate.audToRmb;
                                audToUsd = audToUsd || defaultRate.audToUsd;

								row.set(rmb, newValue.toFixed(decimalPrecision));
								row.set(aud, (newValue / audToRmb).toFixed(decimalPrecision));
								row.set(usd, (newValue / audToRmb * audToUsd).toFixed(decimalPrecision));
								if(typeof callback == 'function') callback.call(this, row, newValue);
							}
						}
					},
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        if(value) return Ext.util.Format.number(value, numberFormat);
                    }
				},
				{ header: _lang.TText.fOld, dataIndex: prevRmb, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
            ]},
            { header: _lang.TText.fUSD, columns: [
				{ header: _lang.TText.fNew, dataIndex: usd, sortable: true, scope:scope, width: 68, align: 'right',
					editor: {xtype: 'numberfield', minValue: 0, decimalPrecision:decimalPrecision,
						listeners:{
							change:function(pt, newValue, oldValue, eOpts ){
                                var cmp = Ext.getCmp(options.gridId);
                                var row = cmp.getSelectionModel().selected.getAt(0);
                                var audToRmb = cmp.audToRmb;
                                var audToUsd = cmp.audToUsd;
                                audToRmb = audToRmb || defaultRate.audToRmb;
                                audToUsd = audToUsd || defaultRate.audToUsd;

								row.set(usd, newValue.toFixed(decimalPrecision));
								row.set(aud, (newValue / audToUsd).toFixed(decimalPrecision));
								row.set(rmb, (audToRmb * newValue /audToUsd).toFixed(decimalPrecision));
								if(typeof callback == 'function') callback.call(this, row, newValue);
							}
						}
					},
                    renderer: function (value, meta, recode) {
                        meta.tdCls = 'grid-input';
                        if(value) return Ext.util.Format.number(value, numberFormat);
                    }
				},
                { header: _lang.TText.fOld, dataIndex: prevUsd, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
            ]},
        ];
    }else{
        return [
            { header: _lang.TText.fAUD, columns: [
            	{ header: _lang.TText.fNew, dataIndex: aud, sortable: true, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)},
                { header: _lang.TText.fOld, dataIndex: prevAud, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
            ]},
            { header: _lang.TText.fRMB, columns: [
            	{ header: _lang.TText.fNew, dataIndex: rmb, sortable: true, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)},
                { header: _lang.TText.fOld, dataIndex: prevRmb, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
            ]},
			{ header: _lang.TText.fUSD, columns: [
				{ header: _lang.TText.fNew, dataIndex: usd, sortable: true, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)},
				{ header: _lang.TText.fOld, dataIndex: prevUsd, width: 68, align: 'right', renderer: Ext.util.Format.numberRenderer(numberFormat)}
			]},
        ];
    }
};

/**
 * 创建带汇率列
 * @param scope
 * @param aud2rmb
 * @param aud2usa
 * @param options
 * @returns {[null,null]}
 */
var $groupExchangeColumns = function(scope, aud2rmb, aud2usd, callback, options){
    var options = options || {};
    var numberFormat = options.numberFormat || '0.0000';
    var decimalPrecision = options.decimalPrecision || 4;
    var columnWidth = options.columnWidth || 60;
    var defaultRate = options.defaultRate || {
        audToRmb: curUserInfo.audToRmb,
        audToUsd: curUserInfo.audToUsd,
    }

    return [
        { header: _lang.TText.fRateAudToRmb, dataIndex: aud2rmb, sortable: false, align: 'right', width: columnWidth,
            renderer: function(value, rowObj, row, rowIndex, colIndex){
                if(value == undefined || value == '') {
                    var cmp = Ext.getCmp(options.gridId);
                    if(!!cmp && !!cmp.audToRmb){
                        value = parseFloat(cmp.audToRmb || 0);
                    }else{
                        value = defaultRate.audToRmb;
                    }
                    row.data[aud2rmb] = value;
                }
                this.up().audToRmb = value;
                if(typeof callback == 'function') callback.call(this, row, value, rowObj);
                return Ext.util.Format.number(value, numberFormat);
            }
        },
        { header: _lang.TText.fRateAudToUsd, dataIndex: aud2usd, sortable: false, align: 'right', width: columnWidth,
            renderer: function(value, rowObj, row, rowIndex, colIndex){
                if(value == undefined || value == '') {
                    var cmp = Ext.getCmp(options.gridId);
                    if(!!cmp && !!cmp.audToUsd){
                        value = parseFloat(cmp.audToUsd || 0);
                    }else{
                        value =defaultRate.audToUsd;
                    }
                    row.data[aud2usd] = value;
                }
                this.up().audToUsd = value;
                if(typeof callback == 'function') callback.call(this, row, value, rowObj);
                return Ext.util.Format.number(value, numberFormat);
                // return value;
            }
        }
    ];
};
/**
 * 创建带汇率列并且带有“旧汇率”列
 * @param scope
 * @param aud2rmb
 * @param aud2usa
 * @param prevAud2rmb
 * @param prevAud2usa
 * @param options
 * @returns {[null,null]}
 */
var $groupExchangeWithOldColumns = function(scope, aud2rmb, aud2usd, prevAud2rmb, prevAud2usd, callback, options){
    var options = options || {};
    var numberFormat = options.numberFormat || '0.0000';
    var decimalPrecision = options.decimalPrecision || 4;
    var columnWidth = options.columnWidth || 60;
    var defaultRate = options.defaultRate || {
        audToRmb: curUserInfo.audToRmb,
        audToUsd: curUserInfo.audToUsd,
	}

    return [
        { header: _lang.TText.fRateAudToRmb, columns: [
            { header: _lang.TText.fNew, dataIndex: aud2rmb, sortable: true, align: 'right', width: columnWidth,
				renderer: function(value, rowObj, row, rowIndex, colIndex){
					if(value == undefined || value == '') {
                        value = defaultRate.audToRmb;
						row.data[aud2rmb] = value;
                    }
                    this.up().audToRmb = value;
					if(typeof callback == 'function') callback.call(this, row, value, rowObj);
					return Number(value).toFixed(decimalPrecision);
				}
            },
            { header: _lang.TText.fOld, dataIndex: prevAud2rmb, sortable: true, align: 'right', width: columnWidth, renderer: Ext.util.Format.numberRenderer(numberFormat)}
        ]},
        { header: _lang.TText.fRateAudToUsd, columns: [
            { header: _lang.TText.fNew, dataIndex: aud2usd, sortable: true, align: 'right', width: columnWidth,
                renderer: function(value, rowObj, row, rowIndex, colIndex){
                    if(value == undefined || value == '') {
                    	value = defaultRate.audToUsd;
                        row.data[aud2usd] = value;
                    }

                    this.up().audToUsd = value;
                    if(typeof callback == 'function') callback.call(this, row, value, rowObj);
                    return Number(value).toFixed(decimalPrecision);
                }
            },
            { header: _lang.TText.fOld, dataIndex: prevAud2usd, sortable: true, align: 'right', width: columnWidth, renderer: Ext.util.Format.numberRenderer(numberFormat)}
        ]}
    ];
};

/**
 * 创建常规流程列
 * @param options
 * @returns {Array}
 */
var $groupGridCreatedColumnsForFlow = function(options){
	var conf = {
        flowStatus:true,
        flowHold:true,
		startTime:true,
		endTime:true,
		sort:false
	}; 
	Ext.apply(conf, options);
	return $groupGridCreatedColumns(conf);
};

/**
 * 创建常规列项
 * @param options
 * @returns {Array}
 */
var $groupGridCreatedColumns = function(options){
	var result = [];
	options = options || {};

    options.flowStatus = options.flowStatus == undefined ? false : options.flowStatus;
    options.flowHold = options.flowHold == undefined ? false : options.flowHold;
	options.startTime = options.startTime == undefined ? false : options.startTime;
	options.endTime = options.endTime == undefined ? false : options.endTime;
	options.status = options.status == undefined ? true : options.status;
	options.sort = options.sort == undefined ? true : options.sort;
	options.assignee = options.assignee == undefined ? true : options.assignee;
	options.creator = options.creator == undefined ? true : options.creator;
	options.department = options.department == undefined ? true : options.department;
	options.createdAt = options.createdAt == undefined ? true : options.createdAt;
	options.updatedAt = options.updatedAt == undefined ? true : options.updatedAt;

    options.flowStatus?
        result.push({ header: _lang.TText.fFlowStatus, dataIndex: 'flowStatus', width: 60,
			renderer: function(value){
                if(value == null || value === '') return $renderOutputColor('gray', _lang.TText.vNotSubmit);
				var $flowStatus = _dict.flowStatus;
				if($flowStatus.length>0 && $flowStatus[0].data.options.length>0){
					return $dictRenderOutputColor(value, $flowStatus[0].data.options);
				}
			}
    	}): '';
    options.flowHold?
        result.push({ header: _lang.TText.fHold, dataIndex: 'hold', width: 40,
			renderer: function(value){
                if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                if(value == '2' || value == '' || value==null) return $renderOutputColor('gray', _lang.TText.vNo);
			}
    	}): '';
    options.assignee? result.push({ header: _lang.TText.fAssigneeId, dataIndex: 'assigneeId', width: 60, hidden:true }): '';
    options.assignee? result.push({ header: _lang.TText.fAssigneeName, dataIndex: 'assigneeCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true }): '';
    options.assignee? result.push({ header: _lang.TText.fAssigneeName, dataIndex: 'assigneeEnName', width: 100, hidden:curUserInfo.lang !='zh_CN'? false: true }): '';
	options.startTime? result.push({ header: _lang.TText.fStartTime, dataIndex: 'startTime', width: 140 }): '';
	options.endTime? result.push({ header: _lang.TText.fEndTime, dataIndex: 'endTime', width: 140 }): '';
	options.status? result.push($renderOutputStatusColumns()) : '';
	options.sort? result.push({ header: _lang.TText.fSort, dataIndex: 'sort', width : 40}) : '';
	options.creator? result.push({ header: _lang.TText.fCreatorId, dataIndex: 'creatorId', width: 60, hidden:true }): '';
	options.creator? result.push({ header: _lang.TText.fCreatorName, dataIndex: 'creatorCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true }): '';
	options.creator? result.push({ header: _lang.TText.fCreatorName, dataIndex: 'creatorEnName', width: 100, hidden:curUserInfo.lang !='zh_CN'? false: true }): '';
	options.department? result.push({ header: _lang.TText.fDepartmentId, dataIndex: 'departmentId', width: 60, hidden:true }): '';
	options.department? result.push({ header: _lang.TText.fDepartmentName, dataIndex: 'departmentCnName', width: 100, hidden:curUserInfo.lang =='zh_CN'? false: true }): '';
	options.department? result.push({ header: _lang.TText.fDepartmentName, dataIndex: 'departmentEnName', width: 160, hidden:curUserInfo.lang !='zh_CN'? false: true }): '';
	options.createdAt? result.push({ header: _lang.TText.fCreatedAt, dataIndex: 'createdAt', width: 140 }): '';
	options.updatedAt? result.push({ header: _lang.TText.fUpdatedAt, dataIndex: 'updatedAt', width: 140 }): '';
	
	return result;
};

/**
 * 创建常规表单字段项
 * @param options
 * @returns {Array}
 */
var $groupFormCreatedColumns = function(options){
	var result = [];
	options = options || {};
	options.sort = options.sort == undefined ? true : options.sort;
	options.status = options.status == undefined ? true : options.status;
	options.createdAt = options.createdAt == undefined ? true : options.createdAt;
	options.updatedAt = options.updatedAt == undefined ? true : options.updatedAt;
	options.creator = options.creator == undefined ? true : options.creator;
	options.department = options.department == undefined ? true : options.department;

    result.push({ xtype: 'section', title:_lang.TText.tCreatorInformation});

    if (options.status) result.push({ field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, allowBlank: false, value:'1', triggerAction: 'all',
    				store: [['1', _lang.TText.vEnabled], ['2', _lang.TText.vDisabled]]
    			});
	options.sort ? result.push({ field: 'main.sort', xtype: 'textfield', fieldLabel: _lang.TText.fSort}) :'';
	options.creator? result.push({ field: 'main.creatorId', xtype: 'hidden', value:curUserInfo.id}) :'';
	options.creator? result.push({ field: 'main.creatorCnName', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatorName, value:curUserInfo.cnName, hidden:curUserInfo.lang =='zh_CN'? false: true}) :'';
	options.creator? result.push({ field: 'main.creatorEnName', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatorName, value:curUserInfo.enName, hidden:curUserInfo.lang !='zh_CN'? false: true}) :'';
	options.department? result.push({ field: 'main.departmentId', xtype: 'hidden', value:curUserInfo.depId}) :'';
	options.department? result.push({ field: 'main.departmentCnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, value:curUserInfo.department.cnName, hidden:curUserInfo.lang =='zh_CN'? false: true}) :'';
	options.department? result.push({ field: 'main.departmentEnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, value:curUserInfo.department.enName, hidden:curUserInfo.lang !='zh_CN'? false: true}) :'';
	
	return result;
};

//供应商字段
var $groupFormVendorFields = function(scope,conf,options){
    options = options || {};
	return [
	    {xtype: 'section', title: options.selectType==2? _lang.ServiceProviderCategory.tabServiceInformation: _lang.VendorDocument.tabVendorInformation},
		{field:'main.vendorId', xtype:'hidden', },
		{xtype: 'container', cls:'row', items: [
		    { field: 'main.vendorName', xtype: options.selectType==2? 'ServiceProviderDialog':'VendorDialog',
				fieldLabel: options.selectType==2? _lang.ServiceProviderDocument.fService: _lang.VendorDocument.fVendorName, cls: options.cls || 'col-2',
		        formId: options.mainFormPanelId || conf.mainFormPanelId, hiddenName:'main.vendorId', single: true, readOnly: options.readOnly || scope.readOnly,
                allowBlank: options.allowBlank || false,
		        subcallback: function(result, vendorBank){
                    var cmp = Ext.getCmp(options.mainFormPanelId || conf.mainFormPanelId);
		        	if(result == undefined || result.length<1){
                        cmp.getCmpByName('vendor.cnName').setValue('');
                        cmp.getCmpByName('vendor.enName').setValue('');
                        cmp.getCmpByName('vendor.address').setValue('');
                        cmp.getCmpByName('vendor.director').setValue('');
                        cmp.getCmpByName('vendor.abn').setValue('');
                        cmp.getCmpByName('vendor.currency').setValue('');
                        cmp.getCmpByName('vendor.website').setValue('');
                        cmp.getCmpByName('vendor.source').setValue('');
                        Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-vendor' :conf.mainFormPanelId + '-vendor').hide();
                        if(options.callback) options.callback.call(this, cmp, null, vendorBank);
		        		return;
		        	}
                    var row = result[0].data;
		    		cmp.getCmpByName('vendor.cnName').setValue(row.cnName);
		    		cmp.getCmpByName('vendor.enName').setValue(row.enName);
		    		cmp.getCmpByName('vendor.address').setValue(row.address);
		    		cmp.getCmpByName('vendor.director').setValue(row.director);
		    		cmp.getCmpByName('vendor.abn').setValue(row.abn);
		    		cmp.getCmpByName('vendor.currency').setValue(row.currency);
		    		cmp.getCmpByName('vendor.website').setValue(row.website);
		    		cmp.getCmpByName('vendor.source').setValue(row.source);
                    Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-vendor' :conf.mainFormPanelId + '-vendor').show();

                    if(options.callback) options.callback.call(this, cmp, row, result, vendorBank);
		        }
		    },
		] },
		{ xtype: 'container',cls:'row', id:options.mainFormPanelId ? options.mainFormPanelId + '-vendor' :conf.mainFormPanelId + '-vendor', hidden: options.hideDetails != false, items:  [

            { field: 'vendor.cnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2'},
		    { field: 'vendor.address', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAddress, cls:'col-2'},
		    { field: 'vendor.enName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2'},
		    { field: 'vendor.director', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fDirector, cls:'col-2', },
		    { field: 'vendor.abn', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fAbn, cls:'col-2', },
		    { field: 'vendor.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
		    { field: 'vendor.website', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fWebsite, cls:'col-2', },
		    { field: 'vendor.source', xtype: 'dictfield', code:'vendor', codeSub:'source', fieldLabel: _lang.VendorDocument.fSource, cls:'col-2'}
		] }
	];
};

//服务商
var $groupFormServiceProviderFields = function(scope,conf,options){
    options = options || {};
	return [
	    {xtype: 'section', title:_lang.FlowFeeRegistration.tabServiceProvider},
        // { xtype: 'fieldset',width:'100%',checkboxToggle: true, collapsed: false, title:_lang.NewProductDocument.tabVendorInformation, items:  [
		{field:'main.serviceProviderId', xtype:'hidden', allowBlank: true,},
		{xtype: 'container', cls:'row', items: [
		    { field: 'main.serviceProviderName', xtype:'ServiceProviderDialog',
				fieldLabel: _lang.ServiceProviderDocument.fServiceId,
				cls:'col-2',
		        formId: options.mainFormPanelId || conf.mainFormPanelId, hiddenName:'main.serviceProviderId', single: true, readOnly: options.readOnly || scope.readOnly,
		        subcallback: function(result){
                    var cmp = Ext.getCmp(options.mainFormPanelId || conf.mainFormPanelId);
		        	if(result == undefined || result.length<1){
                        cmp.getCmpByName('serviceProvider.cnName').setValue('');
                        cmp.getCmpByName('serviceProvider.enName').setValue('');
                        cmp.getCmpByName('serviceProvider.address').setValue('');
                        cmp.getCmpByName('serviceProvider.director').setValue('');
                        cmp.getCmpByName('serviceProvider.abn').setValue('');
                        cmp.getCmpByName('serviceProvider.currency').setValue('');
                        cmp.getCmpByName('serviceProvider.website').setValue('');
                        cmp.getCmpByName('serviceProvider.source').setValue('');
                        Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-serviceProvider' :conf.mainFormPanelId + '-serviceProvider').hide();

                        if(options.callback) options.callback.call(this, cmp, null);
		        		return;
		        	}
		    		var row = result[0].data;
		    		cmp.getCmpByName('serviceProvider.cnName').setValue(row.cnName);
		    		cmp.getCmpByName('serviceProvider.enName').setValue(row.enName);
		    		cmp.getCmpByName('serviceProvider.address').setValue(row.address);
		    		cmp.getCmpByName('serviceProvider.director').setValue(row.director);
		    		cmp.getCmpByName('serviceProvider.abn').setValue(row.abn);
		    		cmp.getCmpByName('serviceProvider.currency').setValue(row.currency);
		    		cmp.getCmpByName('serviceProvider.website').setValue(row.website);
		    		cmp.getCmpByName('serviceProvider.source').setValue(row.source);
                    Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-serviceProvider' : conf.mainFormPanelId + '-serviceProvider').show();
                    if(options.callback) options.callback.call(this, cmp, row);
		        }
		    },
		] },
		{ xtype: 'container',cls:'row', id:options.mainFormPanelId ? options.mainFormPanelId + '-serviceProvider' :conf.mainFormPanelId + '-serviceProvider', hidden: options.hideDetails != false, items:  [
		    { field: 'serviceProvider.cnName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fCnName, cls:'col-2'},
		    { field: 'serviceProvider.address', xtype: 'displayfield', fieldLabel: _lang.ServiceProviderDocument.fAddress, cls:'col-2'},
		    { field: 'serviceProvider.enName', xtype: 'displayfield', fieldLabel: _lang.VendorDocument.fEnName, cls:'col-2'},
		    { field: 'serviceProvider.director', xtype: 'displayfield', fieldLabel: _lang.ServiceProviderDocument.fDirector, cls:'col-2', },
		    { field: 'serviceProvider.abn', xtype: 'displayfield', fieldLabel: _lang.ServiceProviderDocument.fAbn, cls:'col-2', },
		    { field: 'serviceProvider.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
		    { field: 'serviceProvider.website', xtype: 'displayfield', fieldLabel: _lang.ServiceProviderDocument.fWebsite, cls:'col-2', },
		    { field: 'serviceProvider.source', xtype: 'dictfield', code:'vendor', codeSub:'source', fieldLabel: _lang.ServiceProviderDocument.fSource, cls:'col-2' ,}
		] }
		// ]}
	];
};

//采购订单信息
var $groupFormOrderFields = function(scope,conf,options) {
    options = options || {};
    try {
        return [
            {xtype: 'section', title: _lang.OrderDocument.tabOrderDetail},
            {field: 'main.orderId', xtype: 'hidden'},
            {field: 'main.orderTitle', xtype: 'hidden'},
            {xtype: 'container', cls: 'row', items: [
                { field: "main.orderNumber", xtype: 'OrderDialog', formal: true,
                    fieldLabel: _lang.FlowOrderQualityInspection.tabOrderDetail, cls: options.cls || 'col-2',
                    formId: options.mainFormPanelId? options.mainFormPanelId: conf.mainFormPanelId, hiddenName: 'main.orderId',
                    single: options.single ? options.single : conf.single ? conf.single : true,
                    readOnly: options.readOnly ? options.readOnly : conf.readOnly ? conf.readOnly : false,
                    allowBlank: options.allowBlank || false,
                    type: options.type ? options.type : 5,
                    subcallback: function (result) {
                        var cmp = Ext.getCmp(options.mainFormPanelId || conf.mainFormPanelId);
                        if(result == undefined || result.length<1){
                            cmp.getCmpByName('order.orderTitle').setValue('');
                            cmp.getCmpByName('main.orderTitle').setValue('');
                            cmp.getCmpByName('order.currency').setValue('');
                            cmp.getCmpByName('order.totalPriceAud').setValue('');
                            cmp.getCmpByName('order.totalPriceRmb').setValue('');
                            cmp.getCmpByName('order.totalPriceUsd').setValue('');
                            cmp.getCmpByName('order.totalOrderQty').setValue('');
                            cmp.getCmpByName('order.rateAudToRmb').setValue('');
                            cmp.getCmpByName('order.rateAudToUsd').setValue('');
                            cmp.getCmpByName('order.depositAud').setValue('');
                            cmp.getCmpByName('order.depositRmb').setValue('');
                            cmp.getCmpByName('order.depositUsd').setValue('');
                            Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-order' :conf.mainFormPanelId + '-order').hide();
                            if(options.callback) options.callback.call(this, cmp, null);
                            return;
                        }

                        var row = result.data;
                        cmp.getCmpByName('order.orderTitle').setValue(row.orderTitle);
                        cmp.getCmpByName('main.orderTitle').setValue(row.orderTitle);
                        cmp.getCmpByName('order.currency').setValue(row.currency);
                        cmp.getCmpByName('order.totalPriceAud').setValue(row.totalPriceAud);
                        cmp.getCmpByName('order.totalPriceRmb').setValue(row.totalPriceRmb);
                        cmp.getCmpByName('order.totalPriceUsd').setValue(row.totalPriceUsd);
                        cmp.getCmpByName('order.totalOrderQty').setValue(row.totalOrderQty);
                        cmp.getCmpByName('order.rateAudToRmb').setValue(row.rateAudToRmb);
                        cmp.getCmpByName('order.rateAudToUsd').setValue(row.rateAudToUsd);
                        cmp.getCmpByName('order.depositAud').setValue(row.depositAud);
                        cmp.getCmpByName('order.depositRmb').setValue(row.depositRmb);
                        cmp.getCmpByName('order.depositUsd').setValue(row.depositUsd);
                        Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-order' :conf.mainFormPanelId + '-order').show();
                        if(options.callback) options.callback.call(this, cmp, row);
                    }
                },
                { xtype: 'container',cls: 'row', id: options.mainFormPanelId ? options.mainFormPanelId + '-order' : conf.mainFormPanelId + '-order',
                    hidden: options.hideDetails != false,
                    items: [
                        {field: 'order.orderTitle',xtype: 'displayfield', fieldLabel: _lang.OrderDocument.fOrderTitle,cls: 'col-2'},
                        {field: 'order.currency', xtype: 'dictfield',code: 'transaction',codeSub: 'currency',fieldLabel: _lang.OrderDocument.fCurrency,cls: 'col-2'},
                        {field: 'order.rateAudToRmb',xtype: 'displayfield',fieldLabel: _lang.ExchangeRate.fRateAudToRmb,cls: 'col-2'},
                        {field: 'order.rateAudToUsd',xtype: 'displayfield',fieldLabel: _lang.ExchangeRate.fRateAudToUsd,cls: 'col-2',},
                        {field: 'order.totalPriceAud',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fTotalPriceAud,cls: 'col-2'},
                        {field: 'order.depositAud',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fDepositAud,cls: 'col-2'},
                        {field: 'order.totalPriceRmb',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fTotalPriceRmb,cls: 'col-2'},
                        {field: 'order.depositRmb',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fDepositRmb,cls: 'col-2'},
                        {field: 'order.totalPriceUsd',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fTotalPriceUsd,cls: 'col-2',},
                        {field: 'order.depositUsd',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fDepositUsd,cls: 'col-2'},
                        {field: 'order.totalOrderQty',xtype: 'displayfield',fieldLabel: _lang.OrderDocument.fTotalOrderQty,cls: 'col-2',},
                    ]
                }
            ]},
        ]
    }catch (e){
        console.log(e);
    }
}

//费用登记
var $groupFormFeeRegistrationFields = function(scope,conf,options){
    options = options || {};
	return [
        // { xtype: 'fieldset',width:'100%',checkboxToggle: true, collapsed: false, title:_lang.NewProductDocument.tabVendorInformation, items:  [
		{field:'main.Id', xtype:'hidden' },
		{xtype: 'container', cls:'row', items: [
		    { field: 'main.FlowFeeRegistration', xtype:'FlowFeeRegistrationDialog',
				fieldLabel: _lang.FlowOrderShippingPlan.fOrderId,
				cls:'col-2',
		        formId: options.mainFormPanelId || conf.mainFormPanelId, hiddenName:'main.Id', single: true, readOnly: options.readOnly || scope.isApprove,
		        subcallback: function(result){
                    var cmp = Ext.getCmp(options.mainFormPanelId || conf.mainFormPanelId);
		        	if(result == undefined || result.length<1){
                        cmp.getCmpByName('feeRegistration.id').setValue('');
                        cmp.getCmpByName('feeRegistration.feeType').setValue('');
                        cmp.getCmpByName('feeRegistration.venderName').setValue('');
                        cmp.getCmpByName('feeRegistration.remark').setValue('');
                        cmp.getCmpByName('feeRegistration.currency').setValue('');
                        cmp.getCmpByName('feeRegistration.totalPriceAud').setValue('');
                        cmp.getCmpByName('feeRegistration.totalPriceRmb').setValue('');
                        cmp.getCmpByName('feeRegistration.totalPriceUsd').setValue('');
                        Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-feeRegistration' :conf.mainFormPanelId + '-feeRegistration').hide();

                        if(options.callback) options.callback.call(this, cmp, null);
		        		return;
		        	}
		    		var row = result[0].data;
		    		cmp.getCmpByName('feeRegistration.id').setValue(row.id);
		    		cmp.getCmpByName('feeRegistration.feeType').setValue(row.feeType);
		    		cmp.getCmpByName('feeRegistration.venderName').setValue(row.venderName);
		    		cmp.getCmpByName('feeRegistration.remark').setValue(row.abn);
		    		cmp.getCmpByName('feeRegistration.currency').setValue(row.currency);
		    		cmp.getCmpByName('feeRegistration.totalPriceAud').setValue(row.totalPriceAud);
		    		cmp.getCmpByName('feeRegistration.totalPriceRmb').setValue(row.totalPriceRmb);
		    		cmp.getCmpByName('feeRegistration.totalPriceUsd').setValue(row.totalPriceUsd);
                    Ext.getCmp(options.mainFormPanelId ? options.mainFormPanelId + '-feeRegistration' :conf.mainFormPanelId + '-feeRegistration').show();

                    if(options.callback) options.callback.call(this, cmp, row);
		        }
		    },
		] },
		{ xtype: 'container',cls:'row', id:options.mainFormPanelId ? options.mainFormPanelId + '-feeRegistration' :conf.mainFormPanelId + '-feeRegistration', hidden:true, items:  [
		    { field: 'feeRegistration.id', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fId, cls:'col-2'},
		    { field: 'feeRegistration.feeType', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fFeeType, cls:'col-2'},
		    { field: 'feeRegistration.venderName', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fVendorAndService, cls:'col-2', },
		    { field: 'feeRegistration.remark', xtype: 'displayfield', fieldLabel: _lang.TText.fRemark, cls:'col-2', },
		    { field: 'feeRegistration.currency', xtype: 'dictfield', code:'transaction', codeSub:'currency', fieldLabel: _lang.NewProductDocument.fCurrency, cls:'col-2'},
		    { field: 'feeRegistration.totalPriceAud', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fReceivedAud, cls:'col-2', },
		    { field: 'feeRegistration.totalPriceRmb', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fReceivedRmb, cls:'col-2', },
		    { field: 'feeRegistration.totalPriceUsd', xtype: 'displayfield', fieldLabel: _lang.FlowFeeRegistration.fReceivedUsd, cls:'col-2', },
		] }
		// ]}
	];
};
var $toolsPlusPriceColumnsDisplay = function (options){
    options = options || {};
	options.gridId || null;
	if(! options.gridId) return ;

    return { type: 'search', tooltip: _lang.TText.toolsDisplayMainPrice, scope: this,
        handler: function (event, toolEl, panelHeader) {
            $postUrl({
                url: __ctxPath + 'pub/getnull', method: 'get', maskTo: options.gridId, autoMessage: false,
                callback: function (req) {
                    var grid = Ext.getCmp(options.gridId);
                    if (grid.displayAllPrice == undefined) {
                        grid.displayAllPrice = true;
                    } else {
                        grid.displayAllPrice = !grid.displayAllPrice;
                    }

                    $displayColumnsForMainCurrency(grid, grid.mainCurrency);
                }
            });
		}
    }
}

var $checkGridRowExist= function(store,  keyId){
	if(store.data.length > 0){
		for(oix in store.data.items){
			if(keyId == store.data.items[oix].data.id){
				return true;
			}
		}
	}
};



var $productGrid = function(scope, conf){
    return new HP.GridPanel({
        region: 'center',
        id: conf.mainGridPanelId,
        title: _lang.ProductDocument.tabListTitle,
        scope: scope, //主界面的this
        conf: conf,   //主界面的conf        
        forceFit: false,
        url: conf.urlList,
        fields: [
            'id','sku','combined','newProduct', 'name',
            'barcode','categoryName',
            'packageName','color','model','style','length',
            'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
            'seasonal', 'indoorOutdoor','electricalProduct','moq',
            'powerRequirements','mandatory','operatedAt','flagFirst',
            'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
            'departmentCnName','departmentEnName','updatedAt', 'ageLimit',
            'prop.masterCartonL','prop.masterCartonW','prop.masterCartonH','prop.masterCartonCbm',
            'prop.masterCartonGrossWeight','prop.masterCartonCubicWeight','prop.innerCartonL',
            'prop.innerCartonW','prop.innerCartonH','prop.innerCartonCbm','prop.innerCartonGrossWeight',
            'prop.innerCartonCubicWeight','prop.vendorName','prop.riskRating','prop.productLink','prop.keywords',
            'prop.pcsPerCarton','prop.pcsPerPallets','prop.estimatedAvgPostageAud','prop.estimatedAvgPostageRmb',
            'prop.estimatedAvgPostageUsd','prop.moq','prop.originPortId','prop.originPortCnName','prop.originPortEnName',

            'prop.leadTime','prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd','prop.hsCode', 'isSync','syncTime',
            'prop.quotationPriceAud','prop.quotationPriceRmb','prop.quotationPriceUsd','prop.quotationRateAudToRmb','prop.quotationRateAudToUsd',
			'prop.vendorId','ean','newProduct'
        ],
        width: '55%',
        minWidth: 400,
        columns: [
            { header: 'ID', dataIndex: 'id', width: 40, hidden: true, locked: true },
            { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
            { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200},
            { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 100 },
            { header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 100 },
            { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120,sortable: false},
            { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80 ,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 ,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.FlowComplianceArrangement.fRiskRating, dataIndex: 'prop.riskRating',align: 'center', width: 100,
                renderer: function(value){
                    var $riskRating = _dict.riskRating;
                    if($riskRating.length>0 && $riskRating[0].data.options.length>0){
                        return $dictRenderOutputColor(value, $riskRating[0].data.options, ['green', 'yellow','blue','orange','red','black']);
                    }
                }
            },
            { header: _lang.ProductDocument.fMoq, dataIndex: 'moq', width: 60 , sortable: false,},

            { header: _lang.NewProductDocument.fExchangeRate, columns: $groupExchangeColumns(this,'prop.quotationRateAudToRmb','prop.quotationRateAudToUsd')},

            { header: _lang.ProductDocument.fQuotationPrice,
                columns: new $groupPriceColumns(scope,  'prop.quotationPriceAud','prop.quotationPriceRmb','prop.quotationPriceUsd', null, {edit:false})
            },

            { header: _lang.ProductDocument.fTargetBin,
                columns: new $groupPriceColumns(scope, 'prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd', null, {edit:false})
            },
            { header: _lang.ProductDocument.fEstimatedAvgPostage,
                columns: new $groupPriceColumns(scope, 'prop.estimatedAvgPostageAud','prop.estimatedAvgPostageRmb','prop.estimatedAvgPostageUsd', null, {edit:false})
            },

            //规格
            { header: _lang.ProductDocument.fProductSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
            ]},

            //外箱
            { header: _lang.ProductDocument.fMasterCartonSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'prop.masterCartonL', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'prop.masterCartonW', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'prop.masterCartonH', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'prop.masterCartonCbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'prop.masterCartonCubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'prop.masterCartonGrossWeight', width: 60,sortable: true, },
            ]},

            //内箱
            { header: _lang.ProductDocument.fInnerCartonSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'prop.innerCartonL', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'prop.innerCartonW', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'prop.innerCartonH', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'prop.innerCartonCbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'prop.innerCartonCubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'prop.innerCartonGrossWeight', width: 60,sortable: true, },
            ]},

            //包装
            { header: _lang.ProductDocument.fPackage, columns:[
                { header: _lang.ProductDocument.fPackageCode, dataIndex: 'style', width: 60 ,sortable: true, },
                { header: _lang.ProductDocument.fPcsPerCarton, dataIndex: 'prop.pcsPerCarton', width: 60 ,sortable: true, },
                { header: _lang.ProductDocument.fPcsPerPallets, dataIndex: 'prop.pcsPerPallets', width: 60 ,sortable: true, }
            ]},

            { header: _lang.ProductDocument.fColor, dataIndex: 'color', width: 50 },
            { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }

            },
            { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 80,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fAgeRestriction, dataIndex: 'ageLimit', width: 60},
            { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 60},
            { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fHsCode, dataIndex: 'prop.hsCode', width: 60},
            { header:_lang.VendorDocument.fVendorId,  dataIndex: 'prop.vendorId', width: 200,sortable: false, hidden:true, },
            { header: _lang.ProductDocument.fVendorName, dataIndex: 'prop.vendorName', width: 200,sortable: false},

            { header: _lang.ProductDocument.fMoq, dataIndex: 'prop.moq', width: 60},
            { header: _lang.ProductDocument.fOriginPort, dataIndex: 'prop.originPortId', width: 60},
            { header: _lang.ProductDocument.fLeadTime, dataIndex: 'prop.leadTime', width: 90 ,sortable: true,},

            { header: _lang.ProductDocument.fProductLink, dataIndex: 'prop.productLink', width: 90 ,sortable: true,
                renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                    var productLink = record.data['prop.productLink'];
                    if(productLink){
                        return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
					}
                }
			},
            { header: _lang.ProductDocument.fKeywords, dataIndex: 'prop.keywords', width: 90 ,sortable: true,},
            { header:  _lang.ProductDocument.fIsSync, dataIndex: 'isSync', width: 50,
                renderer: function(value){
                    var $sync = _dict.sync;
                    if($sync.length>0 && $sync[0].data.options.length>0){
                        return $dictRenderOutputColor(value, $sync[0].data.options);
                    }
                }
            },
            { header:_lang.ProductDocument.fIsSyncDate, dataIndex: 'syncTime', width: 140}
        ],// end of columns
        appendColumns: $groupGridCreatedColumns({sort: false,assignee:false})
    });
};

var $miniProductGrid = function(scope, conf, fields, columns){
    return new HP.GridPanel({
        region: 'center',
        id: conf.mainGridPanelId,
        title: _lang.ProductDocument.tabListTitle,
        scope: scope, //主界面的this
        conf: conf,   //主界面的conf
        forceFit: false,
        url: conf.urlList,
        fields: [
            'id','sku','combined','newProduct', 'name',
            'barcode','categoryName',
            'packageName','color','model','style','length',
            'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
            'seasonal', 'indoorOutdoor','electricalProduct',
            'powerRequirements','mandatory','operatedAt',
            'status', 'sort', 'creatorId','creatorCnName','creatorEnName', 'createdAt', 'departmentId',
            'departmentCnName','departmentEnName','updatedAt', 'ageLimit', 'prop',
            {name: 'prop.masterCartonL', mapping: 'prop.masterCartonL'},
            {name: 'prop.masterCartonW', mapping: 'prop.masterCartonW'},
            {name: 'prop.masterCartonH', mapping: 'prop.masterCartonH'},
            {name: 'prop.masterCartonCbm', mapping: 'prop.masterCartonCbm'},
            {name: 'prop.masterCartonGrossWeight', mapping: 'prop.masterCartonGrossWeight'},
            {name: 'prop.masterCartonCubicWeight', mapping: 'prop.masterCartonCubicWeight'},
            {name: 'prop.estimatedAvgPostageAud', mapping: 'prop.estimatedAvgPostageAud'},
            {name: 'prop.estimatedAvgPostageRmb', mapping: 'prop.estimatedAvgPostageRmb'},
            {name: 'prop.estimatedAvgPostageUsd', mapping: 'prop.estimatedAvgPostageUsd'},
            {name: 'prop.targetBinAud', mapping: 'prop.targetBinAud'},
            {name: 'prop.targetBinRmb', mapping: 'prop.targetBinRmb'},
            {name: 'prop.targetBinUsd', mapping: 'prop.targetBinUsd'}
        ].concat(fields),
        width: '55%',
        minWidth: 400,
        columns: [
            { header: 'ID', dataIndex: 'id', width: 40, hidden: true },
            { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
            { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200, locked: true },
            { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 180 },
            { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120,sortable: false},
            { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80 ,sortable: true,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fNewProduct, dataIndex: 'newProduct', width: 80 ,sortable: true,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            },
            { header: _lang.ProductDocument.fTargetBin,
                columns: new $groupPriceColumns(scope, 'prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd', null, {edit:false})
            },

            { header: _lang.ProductDocument.fEstimatedAvgPostage,
                columns: new $groupPriceColumns(scope, 'prop.estimatedAvgPostageAud','prop.estimatedAvgPostageRmb','prop.estimatedAvgPostageUsd', null, {edit:false})
            },

            //规格
            { header: _lang.ProductDocument.fProductSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
            ]},

            //外箱
            { header: _lang.ProductDocument.fMasterCartonSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'prop.masterCartonL', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'prop.masterCartonW', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'prop.masterCartonH', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'prop.masterCartonCbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'prop.masterCartonCubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'prop.masterCartonGrossWeight', width: 60,sortable: true, },
            ]},

            { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 80,
                renderer: function(value){
                    if(value == '1') return $renderOutputColor('green', _lang.TText.vYes);
                    if(value == '2') return $renderOutputColor('gray', _lang.TText.vNo);
                }
            }
        ].concat(columns),
        appendColumns: $groupGridCreatedColumns({sort: false,assignee:false})
    });
};

var $newProductGrid = function(scope, conf, fields, columns){
    return new HP.GridPanel({
        region: 'center',
        id: conf.mainGridPanelId,
        title: _lang.ProductDocument.tabListTitle,
        scope: scope, //主界面的this
        conf: conf,   //主界面的conf
        forceFit: false,
        url: conf.urlList,
        fields: [
            'id','sku','combined', 'name',
            'barcode','categoryName',
            'packageName','color','model','style','length',
            'width', 'height', 'cbm', 'cubicWeight', 'netWeight',
            'seasonal', 'indoorOutdoor','electricalProduct',
            'powerRequirements','mandatory','operatedAt',
			'prop.flagQcTime','prop.flagQcStatus','prop.flagQcFlowId','prop.flagQcId',
			'prop.flagDevStatus','prop.flagDevFlowId','prop.flagDevTime','prop.flagDevId',
            'status', 'sort', 'creatorId', 'creatorName', 'creatorCnName','creatorEnName', 'createdAt',
			'departmentId', 'departmentName', 'departmentCnName', 'departmentEnName','updatedAt', 'ageLimit',
			'prop.masterCartonL','prop.masterCartonW','prop.masterCartonH','prop.masterCartonCbm','prop.masterCartonGrossWeight',
			'prop.masterCartonCubicWeight','prop.estimatedAvgPostageAud','prop.estimatedAvgPostageRmb','prop.estimatedAvgPostageUsd',
			'prop.competitorPriceAud','prop.competitorPriceRmb','prop.competitorPriceUsd','prop.competitorSaleRecord','prop.ebayMonthlySalesAud',
			'prop.ebayMonthlySalesRmb','prop.ebayMonthlySalesUsd','prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd','prop.rateAudToRmb',
			'prop.rateAudToUsd','prop.vendorName','prop.factoryCode','prop.riskRating','prop.productParameter','prop.productDetail','prop.productLink',
			'prop.keywords','prop.vendorName','prop.moq','prop.originPortId','prop.leadTime','prop.currency',
			'prop.flagComplianceStatus','prop.flagComplianceId','prop.flagComplianceTime','flagComplianceFlowId','ean','prop.vendorId'
        ],
        width: '55%',
        minWidth: 400,
        columns: [
            { header : _lang.TText.rowAction, xtype: 'rowactions', width:45, locked: true, hidden:this.readOnly,
                keepSelection: false, autoWidth:false, fixed:true, sortable:false, scope:this,
                actions: [{
                    iconCls: 'btnRowEdit', btnCls: 'fa-pencil', tooltip: _lang.TButton.check,
                    callback: function(grid, record, action, idx, col, e, target) {
                        scope.onRowAction.call(this.scope, grid, record, action, idx, col, conf);
                    }
                }]
            },
            { header: 'ID', dataIndex: 'id', width: 40, hidden: true,locked: true },
            { header: _lang.ProductDocument.fSku, dataIndex: 'sku', width: 200, locked: true},
            { header: _lang.ProductDocument.fName, dataIndex: 'name', width: 200},
            { header: _lang.ProductDocument.fBarcode, dataIndex: 'barcode', width: 100 },
            { header: _lang.ProductDocument.fEan, dataIndex: 'ean', width: 100 },
            { header: _lang.ProductDocument.fCategoryId, dataIndex: 'categoryName', width: 120,sortable:false},

            { header: _lang.ProductDocument.fDev, columns:
                [
                    { header: _lang.TText.fStatus, dataIndex: 'prop.flagDevStatus', width: 50,
                        renderer: function(value) {
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                            else return $renderOutputColor('gray', _lang.TText.vToCheck);
                        }
                    },
                    { header: _lang.ProductDocument.fDevStatusDate, dataIndex: 'prop.flagDevTime', width: 90},
                    { header: _lang.ProductDocument.fID, dataIndex: 'prop.flagDevId', width: 80, hidden:true,},
                    //{ header: _lang.ProductDocument.fFlowId, dataIndex: 'prop.flagDevFlowId', width: 80, hidden:true,},

                ]
            },
            { header: _lang.ProductDocument.fCompliance, columns:
                [
                    { header: _lang.TText.fStatus, dataIndex: 'prop.flagComplianceStatus', width: 50,
                        renderer: function(value) {
                            if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                            else return $renderOutputColor('gray', _lang.TText.vToCheck);
                        }
                    },
                    { header: _lang.ProductDocument.fComplianceStatusDate, dataIndex: 'prop.flagComplianceTime', width: 90},
                    { header: _lang.ProductDocument.fID, dataIndex: 'prop.flagComplianceId', width: 80, hidden:true,},
                    //{ header: _lang.ProductDocument.fFlowId, dataIndex: 'prop.flagComplianceFlowId', width: 80, hidden:true,},
                ]
            },
            { header: _lang.ProductDocument.fQc, columns:
                    [
                        { header: _lang.TText.fStatus, dataIndex: 'prop.flagQcStatus', width: 50,
                            renderer: function(value) {
                                if(value == '1') return $renderOutputColor('green', _lang.TText.vAlreadyPassed);
                                else return $renderOutputColor('gray', _lang.TText.vToCheck);
                            }
                        },
                        { header: _lang.ProductDocument.fQcStatusDate, dataIndex: 'prop.flagQcTime', width: 90},
                        { header: _lang.ProductDocument.fID, dataIndex: 'prop.flagQcId', width: 80, hidden:true,},
                       // { header: _lang.ProductDocument.fFlowId, dataIndex: 'prop.flagQcFlowId', width: 80, hidden:true,},

                    ]
            },


            { header: _lang.ProductDocument.fCombined, dataIndex: 'combined', width: 80 ,sortable: true,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo);}
            },
            { header: _lang.NewProductDocument.fTargetBin,
                columns: new $groupPriceColumns(this, 'prop.targetBinAud','prop.targetBinRmb','prop.targetBinUsd', null, {edit:false})
            },


            { header: _lang.ProductDocument.fMoq, dataIndex: 'prop.moq', width: 60},
            { header: _lang.NewProductDocument.fOriginPort, dataIndex: 'prop.originPortId', width: 60,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.origin, []);}
            },
            { header: _lang.ProductDocument.fLeadTime, dataIndex: 'prop.leadTime', width: 100},
            { header:_lang.VendorDocument.fVendorId,  dataIndex: 'prop.vendorId', width: 200,sortable: false, hidden:true, },

            { header: _lang.ProductDocument.fVendorName, dataIndex: 'prop.vendorName', width: 260,sortable:false},
            { header: _lang.ProductDocument.fFactoryCode, dataIndex: 'prop.factoryCode', width: 260},

            { header: _lang.NewProductDocument.fCompetitorPriceAndSales, columns:
                $groupPriceColumns(this, 'prop.competitorPriceAud','prop.competitorPriceRmb','prop.competitorPriceUsd', null, {edit:false}).concat(
                    [
                        { header: _lang.NewProductDocument.fSaleRecord, dataIndex: 'prop.competitorSaleRecord', width: 60},
                    ]
                ),
            },
            { header: _lang.NewProductDocument.fEbayMonthlySales,
                columns: new $groupPriceColumns(this, 'prop.ebayMonthlySalesAud','prop.ebayMonthlySalesRmb','prop.ebayMonthlySalesUsd', null, {edit:false})
            },
            { header: _lang.NewProductDocument.fExchangeRate, columns: new $groupExchangeColumns(this, 'rateAudToRmb','rateAudToUsd')},
            { header: _lang.ProductDocument.fMandatory, dataIndex: 'mandatory', width: 60,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo);}
            },
            { header: _lang.ProductDocument.fSeasonal, dataIndex: 'seasonal', width: 80,sortable: true,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo);}
            },
            { header: _lang.ProductDocument.findoorOutdoor, dataIndex: 'indoorOutdoor', width: 60,sortable: true,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo);}
            },
            { header: _lang.ProductDocument. fElectricalProduct, dataIndex: 'electricalProduct', width: 80,sortable: true,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.optionsYesNo);}
            },
            { header: _lang.ProductDocument.fPowerRequirements, dataIndex: 'powerRequirements', width: 60,sortable: true},
            { header: _lang.ProductDocument.fPackageName, dataIndex: 'packageName', width: 60},
            { header: _lang.ProductDocument.fRiskRating, dataIndex: 'prop.riskRating', width: 60,
                renderer: function(value) { return $renderGridDictColumn(value, _dict.riskRating);}
            },
            { header: _lang.ProductDocument.fProductLink, dataIndex: 'prop.productLink', width: 200,
                renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
                    var productLink = record.data['prop.productLink'];
                    if(productLink){
                        return '<a target ="_blank" href="' + productLink + '">'+ productLink + ' </a>';
                    }
            }
			},
            { header: _lang.ProductDocument.fKeywords, dataIndex: 'prop.keywords', width: 100 },

            //规格
            { header: _lang.ProductDocument.fProductSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'length', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'width', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'height', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'cbm', width: 60,sortable: true, },
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'cubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'netWeight', width: 60,sortable: true, },
            ]},

            //外箱
            { header: _lang.ProductDocument.fMasterCartonSpecification, columns:[
                { header: _lang.ProductDocument.fLength, dataIndex: 'prop.masterCartonL', width: 60 ,sortable:true,},
                { header: _lang.ProductDocument.fWidth, dataIndex: 'prop.masterCartonW', width: 60 ,sortable:true,},
                { header: _lang.ProductDocument.fHeight, dataIndex: 'prop.masterCartonH', width: 60 ,sortable:true,},
                { header: _lang.ProductDocument.fCbm, dataIndex: 'prop.masterCartonCbm', width: 60,sortable:true,},
                { header: _lang.ProductDocument.fCubicWeight, dataIndex: 'prop.masterCartonCubicWeight', width: 60 ,sortable: true,},
                { header: _lang.ProductDocument.fNetWeight, dataIndex: 'prop.masterCartonGrossWeight', width: 60,sortable: true, },
            ]},

            { header: _lang.NewProductDocument.fCreator, dataIndex: 'creatorName', width: 100, hidden: true  },
            { header: _lang.NewProductDocument.fDepartmentId, dataIndex: 'departmentName', width: 100, hidden: true }
        ],
        appendColumns: $groupGridCreatedColumns({sort: false,assignee:false}),
    });
};

var $creatorInfo = function(isApprove, options){
    var op = options || {}
	return [
        //创建人信息
        {xtype: 'section', title: _lang.ProductDocument.tabCreatorInformation},
        {xtype: 'container', cls: 'row', items: [
            {field: 'main.creatorCnName', xtype: 'displayfield', fieldLabel: _lang.ProductDocument.fCreator, cls: 'col-2'},
            {field: 'main.departmentCnName', xtype: 'displayfield', fieldLabel: _lang.TText.fDepartmentName, cls: 'col-2'},
        ]},
        {xtype: 'container', cls: 'row', items: [
            { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', readOnly:true},
            { field: 'main.status', xtype: 'combo', fieldLabel: _lang.TText.fStatus, cls:'col-2', readOnly:op.notArchives != undefined? op.notArchives : true , allowBlank:true,  value:'1',
                store: [['1', _lang.TText.vEnabled], ['2',  _lang.TText.vDisabled]]
            }
		], hidden: op.notArchives != undefined ? op.notArchives : !isApprove },
	]
};

var $flowCreatorInfo = function(options){
		var op = options || {};
		return [
            //创建人信息
            { xtype: 'section', title:_lang.NewProductDocument.tabCreatorInformation},
            { xtype: 'container',cls:'row', items:  [
                { field: 'main.creatorCnName', xtype: 'displayfield', value:curUserInfo.loginname, fieldLabel: _lang.TText.fApplicantName, cls:'col-2', readOnly:true },
                { field: 'main.departmentCnName', xtype: 'displayfield', value:curUserInfo.depName, fieldLabel: _lang.TText.fAppDepartmentName, cls:'col-2', readOnly:true}
            ] },
            { xtype: 'container',cls:'row', items: [
                { field: 'main.createdAt', xtype: 'displayfield', fieldLabel: _lang.TText.fCreatedAt, cls:'col-2', readOnly:true},
                { field: 'main.status', xtype: 'displayfield', fieldLabel: _lang.TText.fStatus, cls:'col-2', readOnly:true, allowBlank:true, value:'1',
                    renderer: function(value){
                        if(value == '1') return $renderOutputColor('green', _lang.TText.vEnabled);
                        if(value == '2') return $renderOutputColor('gray', _lang.TText.vDisabled);
                        if(value == '4') return $renderOutputColor('gray', _lang.TText.vCancel);
                    }
                }
            ], hidden: !op.isApprove && !op.isComplated },
		]
}