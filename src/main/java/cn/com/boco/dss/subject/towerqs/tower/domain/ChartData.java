package cn.com.boco.dss.subject.towerqs.tower.domain;

import java.util.List;
import java.util.Map;

/**
 * @program: TowerService
 * @description: 折线图数据结构
 * @author: lyj
 * @create: 2019-08-22 16:23
 **/
public class ChartData {

    private List<String> columns;


    private List<Map<String, String>> rows;

    private Object result;


    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
