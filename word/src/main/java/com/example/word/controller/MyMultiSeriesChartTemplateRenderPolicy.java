package com.example.word.controller;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.DocumentRenderData;
import com.deepoove.poi.data.Documents;
import com.deepoove.poi.data.Paragraphs;
import com.deepoove.poi.policy.reference.MultiSeriesChartTemplateRenderPolicy;
import com.deepoove.poi.template.ChartTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: kevin.fang
 * @date: 2022/8/11 16:10
 **/
public class MyMultiSeriesChartTemplateRenderPolicy extends MultiSeriesChartTemplateRenderPolicy {

    private String labName;

    public MyMultiSeriesChartTemplateRenderPolicy(String labName) {
        this.labName = labName;
    }

    private static String text = "无数据";
    @Override
    public void doRender(ChartTemplate eleTemplate, ChartMultiSeriesRenderData data, XWPFTemplate template) throws Exception {
        XWPFChart chart = eleTemplate.getChart();
        List<XDDFChartData> chartSeries = chart.getChartSeries();
        if (chartSeries.size() == 1){
            XSSFSheet sheet = chart.getWorkbook().getSheetAt(0);
//            updateCTTable(sheet, new ArrayList<>());
            removeExtraSheetCell(sheet, data.getCategories().length, 1, 0);
            chart.deleteLegend();
            chart.deleteShapeProperties();
//            chart.setTitleText(text);
            List<MetaTemplate> elementTemplates = template.getElementTemplates();
            for (MetaTemplate metaTemplate : elementTemplates){
                if (metaTemplate instanceof ChartTemplate){
                    ChartTemplate chartTemplate = (ChartTemplate) metaTemplate;
                    if (this.labName.equals(chartTemplate.getTagName())){
                        XWPFRun run = eleTemplate.getRun();
                        // String thing = String.valueOf(data);
//                        String thing = "Hello, world";
                        run.setText(text, 0);
                    }
                }
            }
        }else {
            super.doRender(eleTemplate, data, template);
        }
    }
}
