Ext.ns("App");
Ext.ns("App.LoginWin");

App.LoginWin = Ext.extend(Ext.window.Window, {
	constructor : function(conf) {
		Ext.applyIf(this, conf);
		this.initUI();
		
		App.LoginWin.superclass.constructor.call(this, {
			id : 'LoginWin',
			title : '用户登录',
			bodyStyle : 'background-color: white',
			border : true,
			closable : false, resizable : false,
			buttonAlign : 'center',
			height : 275, width : 460,
			layout : { type : 'vbox', align : 'stretch'	},
			items : [ {
				xtype : 'panel', border : false, width : 200, height : 114,	layout : 'column',
				items : [ this.formPanel]
			}, this.sloganPanel
			],
			buttonAlign : 'center',
			buttons : [
			  { text : '登录', scope : this, handler : this.loginHandler },
			  '-',
			  { text : '重置', scope : this, handler : this.loginReset}
			]
		});
		
	},// end of the constructor
	// 初始化组件
	initUI : function() {
		
		// 登陆表单
		this.formPanel = new Ext.FormPanel({
			id : 'LoginFormPanel',
			region: 'center',
			bodyStyle : 'padding-top:6px; padding-left: 20px;',
			defaultType : 'textfield',
			columnWidth : 0.75,
			labelWidth : 55,
			id : 'AppLoginWindowForm',
			border : false,
			layout : 'form',
			scope : this,
			defaults : { style : 'margin:0 0 0 0', anchor : '90%,120%'
//				,	selectOnFocus : true
				},
			items : [
			    { id : 'account', name : 'account', fieldLabel : '账      号', cls : 'text-user',
			      allowBlank : false, blankText : '账号不能为空', value : this.account == null ? '' : this.account
			    },
			    { id : 'password', name : 'password', fieldLabel : '密      码', allowBlank : false,
			      blankText : '密码不能为空', cls : 'text-lock',	inputType : 'password'
			    },
			    { xtype : 'container', style : 'padding-left:104px', layout : 'column',
			    	items : [ {
					    xtype : 'checkbox',
					    name : '_spring_security_remember_me',
					    boxLabel : '记住我 ',
					    checked : this.falg == null ? false : this.falg
				    }]
			    }
			],
			listeners :{
				scope: this,
				afterrender: function(layout, eOpts){
					new Ext.KeyNav({
						target: 'AppLoginWindowForm',
						scope: this,
						enter: function(e){ this.loginHandler.call(this); }
					});
					
					var fls = this.flashChecker.call(this);
					if (!fls.f){
						jQuery('body').append('<div class="clew-flashplayer"><span>您在使用前需要安装 Adobe FlashPlayer, 请直接访问Adobe官网</br>下载并安装 <a href="https://get.adobe.com/cn/flashplayer/">https://get.adobe.com/cn/flashplayer</a></span></div>');
					}
				}
			}
		});
		
		
		this.flashChecker = function () {
			var hasFlash = 0; 
			var flashVersion = 0;

			if (document.all) {
				var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
				if (swf) {
					hasFlash = 1;
					VSwf = swf.GetVariable("$version");
					flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
				}
			} else {
				if (navigator.plugins && navigator.plugins.length > 0) {
					var swf = navigator.plugins["Shockwave Flash"];
					if (swf) {
						hasFlash = 1;
						var words = swf.description.split(" ");
						for (var i = 0; i < words.length; ++i) {
							if (isNaN(parseInt(words[i])))
								continue;
							flashVersion = parseInt(words[i]);
						}
					}
				}
			}
			return {
				f : hasFlash,
				v : flashVersion
			};
		};
	},

	//登录操作
	loginHandler : function() {
		if (this.formPanel.form.isValid()) {
			this.formPanel.form.submit({
				waitTitle : "请稍候",
				waitMsg : '正在登录......',
				url : __ctxPath + 'login',
				scope : this,
				success : function(form, action) {
					this.handleLoginResult(action);
				},
				failure : function(form, action) {
					this.handleLoginResult(action);
					form.findField("password").setRawValue("");
					form.findField("account").focus(true);
				}
			});
		}
	},

	handleLoginResult : function(result) {
		if (result.success) {
//			Ext.getCmp('LoginWin').hide();
			var statusBar = new Ext.ProgressBar({
				text : '正在登录...'
			});
			statusBar.show();
			window.location.href = __ctxPath + '';
		} else {
			Ext.MessageBox.show({
				title : '操作信息',
				msg : result.msg,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	},

	loginReset: function() {
		this.formPanel.getForm().reset();
	}
});
