Ext.ns('AppHome');

AppHome = Ext.extend(Ext.Panel, {
    portal : null, // portal
    constructor : function(_cfg) {
        Ext.applyIf(this, _cfg);
        this.initUIComponents();
        AppHome.superclass.constructor.call(this, {
            title : _lang.Home.myHome,
            id : 'AppHome',
            border : false,
            layout : 'fit',
            bbar : [
                {xtype : 'label', id : 'onLineMsg', title : '' }
                , '-'
                , {	text : _lang.TButton.refresh, iconCls:'fa fa-fw fa-refresh', handler : this.onRefreshClick, scope : this}
                , '-', {text : _lang.TButton.default, iconCls : 'fa fa-fw fa-reply', handler : this.onResumeClick, scope : this}
                , '-', {text :  _lang.TButton.save, iconCls : 'fa fa-fw fa-save', handler : this.onSaveClick, scope : this}
                , '-'
            ],
            items: this.portal
        });
    },

    initUIComponents : function(conf) {
        this.portal = new StartPortal();
    },
    onRenderEvent : function(panel) {

    },
    onRefreshClick : function() {
        try {
            this.portal.removeAllportlet();
            this.portal.init();
        } catch (e) {

        }
    },
    onResumeClick : function() {
        try {
            this.portal.removeAllportlet();
            this.portal.restore();
        } catch (e) {

        }
    },
    onSaveClick : function() {
        var params = {act: 'save'}, rpitems=[];
        if (!this.portal.items || this.portal.items.length == 0)
            return;
        var index=0;
        for (var i = 0; i < this.portal.items.length; i++) {
            var col = this.portal.items.items[i];
            for (var j = 0; j < col.items.length; j++) {
                var portlet = col.items.items[j];
                params['main.items['+index+'].moduleKey'] = col.items.items[j].id;
                params['main.items['+index+'].colNum'] = i;
                params['main.items['+index+'].rownum'] = col.items.indexOf(portlet);
                params['main.items['+index+'].height'] = portlet.getHeight();

                rpitems.push({
                    moduleKey:col.items.items[j].id,
                    colNum:i,
                    rownum:col.items.indexOf(portlet),
                    height:portlet.getHeight(),
                });
                index++;
            }
        }
        $postUrl({
            url : __ctxPath + 'portal/save', maskTo: 'AppHome',
            params : params, callback:function (response) {
                curUserInfo.portals = rpitems;
            }
        });
    }

});

StartPortal = Ext.extend(Ext.ux.Portal, {

    leftColumn : null,
    centerColumn : null,
    rightColumn : null,

    treePanel1 : null,
    treePanel2 : null,
    treePanel3 : null,
    treePanel4 : null,
    treePanel5 : null,
    treePanel6 : null,

    constructor : function() {
        StartPortal.superclass.constructor.call(this, {
            border : false
        });
        this.init();
    },

    restore : function() { // 恢复默认
        this.initAllComponent(true);
        this.setPortals();
        this.reDoLayout();
    },

    init : function() { // 初始化
		try {
            this.initAllComponent(false);
            this.reDoLayout();
        }catch (e){
			console.log(e)
		}
    },

    initAllComponent : function(isInit) {
        this.leftColumn = this.createColumn('left', .65);
        this.rightColumn = this.createColumn('right', .34);

        var items = curUserInfo.portals;
        if(isInit || items == undefined || items.length == undefined){
            this.treePanel1 = new TasksPortalView();
            this.treePanel2 = new PurchaseContractPortalView();
            this.treePanel3 = new PurchasePlanPortalView();
            this.treePanel4 = new RatePortalView();
            this.treePanel5 = new MessagePortalView();
            this.treePanel6 = new PermissionPortalView();
            this.setPortals();
        }else{
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if(item.colNum==0){
                    this.leftColumn.add(eval('new '+ item.moduleKey + '(' + item.height + ')'));
                }else{
                    this.rightColumn.add(eval('new '+ item.moduleKey + '(' + item.height + ')'));
                }
            }
        }

    },
    setPortals: function(){
        this.leftColumn.add(this.treePanel1);
        this.leftColumn.add(this.treePanel2);
        this.leftColumn.add(this.treePanel3);
        this.rightColumn.add(this.treePanel4);
        this.rightColumn.add(this.treePanel5);
        this.rightColumn.add(this.treePanel6);

    },
    reDoLayout : function() {
        this.add(this.leftColumn);
        this.add(this.rightColumn);
    }
});