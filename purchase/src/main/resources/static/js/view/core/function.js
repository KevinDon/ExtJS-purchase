function newView(viewName,params){
    var c='new ' + viewName ;
    if(params!=null){ c+='(params);'; }else{ c+='();'; }
    try{
        return eval(c);
    }catch(e){}
};

var $_getValueByName = function(name, data, conf) {
    if (name) {
        if (conf.preName) {
            if (name.indexOf(conf.preName) != -1) {
                name = name.substring(conf.preName.length + 1);
            }
        }
        // var d = eval(conf.root + '.' + name);
        // var d = !!conf.root ? eval(conf.root + '.' + name) : '';
        var d = !!conf.root ? eval(conf.root + '.' + name) : eval(data + '.' + name);
        if (!Ext.isEmpty(d)) return d;
        else return '';
    }
    return '';
};

var $getGdSelectedIds = function(conf) {
    var ids = [];
    var sel = conf.grid.getSelectionModel().selected.items;

    if(! conf.idName) conf.idName ='id';

    for (var d = 0; d< sel.length; d++) {
        ids.push(eval('sel[d].data.' + conf.idName));
    }
    return ids;
};

var $getGdSelectedItems = function(conf) {
    var sel = conf.grid.getSelectionModel().selected.items;
    if(sel.length>0){
        return sel;
    }
};

var $getGdItemsIds = function(conf) {
    var ids = [];
    var sel = conf.grid.getStore().data.items;

    if(! conf.idName) conf.idName ='id';

    for (var d = 0; d< sel.length; d++) {
        ids.push(eval('sel[d].data.' + conf.idName));
    }
    return ids;
};

var $getGdItems = function(conf) {
    var sel = conf.grid.getStore().data.items;
    if(sel.length>0){
    	return sel;
    }
};

var $setColumnValue = function(grid, field, value){
    var cmpRowsData = $getGdItems({grid: grid});
    if(cmpRowsData){
        for(index in cmpRowsData){
            cmpRowsData[index].set(field, value)
        }
    }
}

var $ucFirst = function (str) {
    var str = str.toLowerCase();
    str = str.replace(/\b\w+\b/g, function (word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    });
    return str;
}

var $getExportFieldsFromGrid = function(cmp, params){
    if(cmp.columns){
        for(var index in cmp.columns){
            if(index == 0) continue;
            params['columns['+(index-1)+'].key']= cmp.columns[index].dataIndex;
            params['columns['+(index-1)+'].text']= cmp.columns[index].text || '';
        }
    }
    return params || '';
}

var $getExportFieldsFromForm = function(cmp, params){
    var fields = cmp.form.getFields().items;
    if(fields){
        for(var index in fields){
            params['columns['+(index-1)+'].key']= fields[index].name;
            params['columns['+(index-1)+'].text']= fields[index].fieldLabel || '';
        }
    }
    return params || '';
}

/**
 * 动态控制显示/隐藏价格列
 * @param grid
 * @param mainCurrency
 */
var $displayColumnsForMainCurrency = function(grid, mainCurrency){
    if(mainCurrency == 'Usd'){
        diaplayCurrencies = ['Aud','Rmb'];
    }else if(mainCurrency == 'Aud'){
        diaplayCurrencies = ['Rmb','Usd'];
    }else{
        diaplayCurrencies = ['Aud','Usd'];
    }

    for(index in grid.columns){
        if(grid.columns[index].dataIndex.indexOf(diaplayCurrencies[0])>0 || grid.columns[index].dataIndex.indexOf(diaplayCurrencies[1])>0){
           grid.displayAllPrice? grid.columns[index].show(): grid.columns[index].hide();
        }
    }
}
/**
 * 组合Panel标题
 * @param actionName
 * @returns {*}
 */
var $getTitleSuffix = function(actionName){
    if(actionName){
       return  ' - '+ _lang.TButton[actionName];
    }else{
        return ''
    }
}
/**
 * 判断是否为审批中的流程
 * @param actionName
 * @param record
 * @returns {boolean}
 */
var $getIsApprove = function(actionName, record){
    if(!!record && record.flowStatus <2 && record.flowStatus >-1 && record.flowStatus !==null) {
        return true;
    }else{
        return false;
    }
}

var $getIsComplated = function(actionName, record){
    if(!!record && record.flowStatus>1){
        return true;
    }else{
        return false;
    }
}

var $getIsAdd = function(actionName, record){
    if((actionName == 'add' || actionName=='copy') && (!record || record.status <2 || record.status ==4)){
        return true;
    }else{
        return false;
    }
}

var $getIsStart = function(actionName, record){
    if($getIsApprove(actionName, record) && record.flowStatus ===0){
        return true;
    }else{
        return false;
    }
}