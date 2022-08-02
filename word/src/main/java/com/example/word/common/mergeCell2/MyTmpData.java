package com.example.word.common.mergeCell2;

import com.deepoove.poi.el.Name;

/**
 * @Description:
 * @author: kevin.fang
 * @date: 2022/8/2 14:04
 **/
public class MyTmpData {

    @Name("detail_table")
    private DetailData2 detailTable;

    public DetailData2 getDetailTable() {
        return detailTable;
    }

    public void setDetailTable(DetailData2 detailTable) {
        this.detailTable = detailTable;
    }
}
