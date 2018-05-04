
/**
 * @class Ext.ux.Toast
 * Passive popup box (a toast) singleton
 * @singleton
 */
Ext.ux.Toast = function() {
    var msgCt='';

    function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }

    function createBoxWin(t, s){
        return ['<div class="msg">',
            '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
            '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><div><h3 style="float:left; ">', t, '</h3>',
            '<div class="x-tool-img x-tool-close" style="float:right; cursor: pointer;" onclick="jQuery(\'#msgwin-div div.msg\').remove()" title="'+ _lang.TButton.close +'"></div></div>',
            '<div style="clear:both; height:250px;">',s, '</div></div></div></div>',
            '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
            '</div>'].join('');
    }

    return {
		/**
		 * Shows popup
		 * @member Ext.ux.Toast
		 * @param {String} title
		 * @param {String} format
		 */
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div',style:'position:absolute;button:10px;right:10px;width:25%;z-index:100000;'}, true);
            }
            try{
	            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
	            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
	            msgCt.alignTo(document, 'b-b');
	            m.slideIn('b').pause(2500).ghost("b", {remove:true});
            }catch(e){}
        },

        /**
         * Shows popup
         * @member Ext.ux.Toast
         * @param {String} title
         * @param {String} format
         */
        msgwin : function(title, format){
            jQuery('#msgwin-div div.msg').remove();

            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msgwin-div',style:'width:400px;bottom:30px !important;z-index:99999;'}, true);
            }
            try{
                var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
                var m = Ext.DomHelper.append(msgCt, {html:createBoxWin(title, s)}, true);
                msgCt.alignTo(document, 'br-br');
                m.slideIn('b').pause(30000).ghost("b", {remove:true});
            }catch(e){}
        }
	};

}();
