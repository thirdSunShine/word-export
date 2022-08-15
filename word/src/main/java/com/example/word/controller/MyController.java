package com.example.word.controller;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.policy.reference.MultiSeriesChartTemplateRenderPolicy;
import com.deepoove.poi.util.PoitlIOUtils;
import com.example.word.common.mergeCell2.DetailData2;
import com.example.word.common.mergeCell2.DetailTablePolicy3;
import com.example.word.common.mergeCell2.MyTmpData;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: kevin.fang
 * @date: 2022/8/10 17:31
 **/
@RequestMapping("/auth/exportWord/")
@RestController
public class MyController {

    private static String getBasePath(String fileName) {
        String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/template/";
        return basePath + fileName;//word模板地址
    }

    @GetMapping("/my11")
    public void myExport1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<DataEntity> entities = generateList();
            Map<String, List<DataEntity>> collect = entities.stream().collect(Collectors.groupingBy(DataEntity::getDataIp));

            Map<String,Long> auditTotal =
                    entities.stream().collect(Collectors.groupingBy(DataEntity::getDataIp
                            ,Collectors.summingLong(DataEntity::getSubAuditTotal)));

            List<DataEntity> handlerEntity = new ArrayList<>();
            for (String key : collect.keySet()){
                DataEntity sumEntity = new DataEntity();
                sumEntity.setDataIp(key);
                sumEntity.setClientName(String.format("合计(共%d项)",collect.get(key).size()));
                sumEntity.setSubAuditTotal(auditTotal.get(key));
                handlerEntity.add(sumEntity);
                handlerEntity.addAll(collect.get(key));
            }
            MyTmpData datas = new MyTmpData();
            TableStyle rowStyle = new TableStyle();
            rowStyle = new TableStyle();
//            rowStyle.setAlign(STJc.CENTER);
            DetailData2 detailTable = new DetailData2();
            List<RowRenderData> plists = new ArrayList<>();
            for (DataEntity entity : handlerEntity) {
                RowRenderData data = Rows.of(entity.getDataIp(),
                        entity.getClientName(), String.valueOf(entity.getClientIpTotal())
                        , String.valueOf(entity.getSubAuditTotal())).create();
//                plist.setRowStyle(rowStyle);
                plists.add(data);
            }
            List<Map<String, Object>> tlists = new ArrayList<>();
            for (String key : collect.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("typeName", key);
                map.put("listSize", collect.get(key).size() + 1);
                tlists.add(map);
            }
            detailTable.setPlists(plists);
            detailTable.setTlists(tlists);
            datas.setDetailTable(detailTable);

            ChartMultiSeriesRenderData chart = Charts
                    .ofMultiSeries("", new String[]{""})
                    .addSeries("high", new Double[] { 15.0})
                    .addSeries("middle", new Double[] { 11.0})
                    .addSeries("low", new Double[] { 20.0})

                    .create();

            datas.setRenderData(chart);

//            List<ChartMultiSeriesRenderData> dataList = new ArrayList<>();
//            dataList.add(chart);
            Configure config = Configure.builder()
                    .bind("detail_table", new DetailTablePolicy3())
//                    .bind("barChart", datas.getRenderData().getCategories().length > 1)
                    .bind("inner", new MultiSeriesChartTemplateRenderPolicy())
                    .build();

            String filePath = "D:\\java\\project\\word-export1\\tmp\\tmp.docx";
            String filePath1 = "D:\\java\\project\\tmp\\tmp3.docx";
            XWPFTemplate template =
                    XWPFTemplate.compile(filePath1,config)
                            .render(
                                    new HashMap<String,Object>(){
                                        {
                                            put("detail_table",datas.getDetailTable());
                                            put("flag",true);
//                                            boolean flag = datas.getRenderData().getSeriesDatas().size() >= 1;
//                                            put("barChart",flag);
//                                            if (flag){
//                                                put("inner",datas.getRenderData());
//                                            }else {
//                                                put("text","无数据");
//                                            }
                                        }
                                    }
                            );
            //=================生成文件保存在本地D盘某目录下=================
            String temDir = filePath1;
            Long time = new Date().getTime();
            // 生成的word格式
            String formatSuffix = ".docx";
            // 拼接后的文件名
            String fileName = time + formatSuffix;//文件名  带后缀
            String downloadPath = temDir + fileName;
            FileOutputStream fos = new FileOutputStream(downloadPath);
            //=================生成word到设置浏览默认下载地址=================
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf-8"));

            OutputStream out = response.getOutputStream();
            template.write(out);
//            bos.flush();
            out.flush();
            out.close();
            fos.close();
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String filePath = "D:\\java\\project\\tmp";

    private List<DataEntity> generateList(){
        DataEntity entity1 = new DataEntity();
        entity1.setDataIp("1.1.1.1");
        entity1.setSubAuditTotal(50L);
        entity1.setClientName("mysql");
        entity1.setClientIpTotal(2L);
        DataEntity entity2 = new DataEntity();
        entity2.setDataIp("1.1.1.1");
        entity2.setSubAuditTotal(70L);
        entity2.setClientIpTotal(3L);
        entity2.setClientName("mysql1111");
        DataEntity entity3 = new DataEntity();
        entity3.setDataIp("2.1.1.1");
        entity3.setSubAuditTotal(50L);
        entity3.setClientIpTotal(8L);
        entity3.setClientName("redis");

        DataEntity entity4 = new DataEntity();
        entity4.setDataIp("2.1.1.1");
        entity4.setSubAuditTotal(90L);
        entity4.setClientIpTotal(5L);
        entity4.setClientName("mysql222");

        List<DataEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        return entities;
    }
}
