package com.example.word.common.mergeCell2;

import java.util.List;
import java.util.Map;

import com.deepoove.poi.data.RowRenderData;

public class DetailData2 {
	
    
    // 商品订单详情列表数据
    private List<RowRenderData> plists;
    
    //
    private List<Map<String,Object>> tlists;
    

	public List<RowRenderData> getPlists() {
		return plists;
	}

	public void setPlists(List<RowRenderData> plists) {
		this.plists = plists;
	}

	public List<Map<String,Object>> getTlists() {
		return tlists;
	}

	public void setTlists(List<Map<String,Object>> tlists) {
		this.tlists = tlists;
	}


	
}
