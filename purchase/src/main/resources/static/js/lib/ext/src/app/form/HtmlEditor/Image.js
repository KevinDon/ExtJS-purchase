Ext.define('Ext.app.form.HtmlEditor.Image', {
    extend: 'Ext.util.Observable',

    langTitle: '插入图片',
    overflowText: '',
    urlSizeVars: ['width','height'],
    init: function (cmp) {
        this.cmp = cmp;
        this.cmp.on('render', this.onRender, this);
        this.cmp.on('initialize', this.onInit, this, {delay: 100, single: true});
    },
    onEditorMouseUp: function (e) {
        Ext.get(e.getTarget()).select('img').each(function (el) {
            var w = el.getAttribute('width'), h = el.getAttribute('height'), src = el.getAttribute('src') + ' ';
            src = src.replace(new RegExp(this.urlSizeVars[0] + '=[0-9]{1,5}([&| ])'), this.urlSizeVars[0] + '=' + w + '$1');
            src = src.replace(new RegExp(this.urlSizeVars[1] + '=[0-9]{1,5}([&| ])'), this.urlSizeVars[1] + '=' + h + '$1');
            el.set({src: src.replace(/\s+$/, "")});
        }, this);

    },
    onInit: function () {
        Ext.EventManager.on(this.cmp.getDoc(), {
            'mouseup': this.onEditorMouseUp,
            buffer: 100,
            scope: this
        });
    },
    onRender: function () {
        var place = this.cmp.getToolbar().items.length;
        var me = this;

        var btn = this.cmp.getToolbar().add(place-1, {
            iconCls:'fa fa-fw fa-picture-o',
            handler: function () {
                new FilesDialogWin({
                    scope: me,
                    single: true,
                    fileDefType: this.fileDefType,
                    fieldValueName: this.hiddenName,
                    fieldTitleName: this.name,
                    initValue: this.initValue || true,
                    cmp: me.cmp,
                    callback: function (ids, names, rows, paths) {
                        me.insertImg.call(this.cmp,{
                            url: paths,
                            content: '',
                            width: 100,
                            height: 120
                        });

                    }
                }, false).show();
            },
            tooltip: { title: this.langTitle},
            overflowText: this.overflowText,
        });
    },

    insertImg: function (data) {
        var url = data.url || '';
        var content = data.content;
        var width = data.width;
        var height = data.height;
        // this.urlSizeVars[0]
        // '<img src="'+this.basePath+'?'+this.urlSizeVars[0]+'='+img.Width+'&'+this.urlSizeVars[1]+'='+img.Height+'&id='+img.ID+'" title="'+img.Name+'" alt="'+img.Name+'">'

        var html = '<img src="' + url + '" border="0" ';
        if (content != undefined && content != null && content != '') {
            html += ' title="' + content + '" ';
        }
        if (width != undefined && width != null && width != 0) {
            html += ' width="' + width + '" ';
        }
        if (height != undefined && height != null && height != 0) {
            html += ' height="' + height + '" ';
        }
        html += ' />';

        this.insertAtCursor(html);
    }
})