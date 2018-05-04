package com.newaim.purchase.admin.system.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class ExportColumnsVo implements Serializable {

    List<ExportColumnVo> columns;

    public List<ExportColumnVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ExportColumnVo> columns) {
        this.columns = columns;
    }

    public String[] getKeys(){
        List<String> temp = Lists.newArrayList();
        if(columns.size()>0){
            for(ExportColumnVo ecv: columns){
                temp.add(ecv.getKey());
            }
        }
        return temp.toArray(new String[temp.size()]);
    }

    public String[] getTexts(){
        List<String> temp = Lists.newArrayList();
        if(columns.size()>0){
            for(ExportColumnVo ecv: columns){
                temp.add(ecv.getText());
            }
        }
        return temp.toArray(new String[temp.size()]);
    }
}
