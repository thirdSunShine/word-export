package com.example.word.common.mergeCell2;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.springframework.util.CollectionUtils;

/**
 * 表格动态行插入、渲染、合并单元格处理
 *
 * @author Administrator
 */
public class DetailTablePolicy2 extends DynamicTableRenderPolicy {

    // 填充数据所在行数
    int listsStartRow = 1;

    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;
        DetailData2 detailData = (DetailData2) data;

        //需要写入的数据
        List<RowRenderData> datas = detailData.getPlists();
        //需要合并的单元格
        List<Map<String, Object>> tlists = detailData.getTlists();

        if (!CollectionUtils.isEmpty(datas)) {
            table.removeRow(listsStartRow);
            //总共列
            int column = datas.get(0).size();
            // 循环插入行
            for (int i = datas.size() - 1; i >= 0; i--) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(listsStartRow);
                insertNewTableRow.setHeight(620);//设置行高
                // 循环插入列
                for (int j = 0; j < column; j++){
                    insertNewTableRow.createCell();
                }
                // 渲染单行商品订单详情数据
                MiniTableRenderPolicy.Helper.renderRow(table, listsStartRow, datas.get(i));
            }
            //处理合并
            for (int i = 0; i < datas.size(); i++) {
                Object v = datas.get(i).getCells().get(0).getCellText();
                String type_name = String.valueOf(v);
                for (int j = 0; j < tlists.size(); j++) {
                    String typeName = String.valueOf(tlists.get(j).get("typeName"));
                    Integer listSize = Integer.parseInt(String.valueOf(tlists.get(j).get("listSize")));
                    if (type_name.equals(typeName)) {
                        // 合并第1列的第i+1行到第i+listSize行的单元格
                        TableTools.mergeCellsVertically(table, 0, i + 1, i + listSize);
                        tlists.remove(j);
                        break;
                    }
                }
                //处理垂直居中(自定义行高时，设置垂直居中)
                for (int y = 0; y < column; y++) {
                    XWPFTableCell cell = table.getRow(i + 1).getCell(y);
                    if (StringUtils.equals("审计总数",cell.getText())){
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH); //垂直居中
                    }
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); //垂直居中
                }
            }
        }
    }
}