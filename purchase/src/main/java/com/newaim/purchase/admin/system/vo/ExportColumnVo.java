package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;

public class ExportColumnVo implements Serializable {

    public String key;
    public String text;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
