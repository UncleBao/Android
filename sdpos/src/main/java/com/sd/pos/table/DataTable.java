package com.sd.pos.table;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

/**
 * DataTable数据结构,模仿C#的DataTable
 * 列不区分大小写
 */
public class DataTable implements Serializable {
    /**
     * 列:构建Table的时候会初始化,且不能外部修改,不会为null,可以放心使用<br>
     * 继承自ArrayList<String>
     */
    protected final DataColumn columns; // 列,
    /**
     * 行:构建Table的时候会初始化,且不能外部修改,不会为null,可以放心使用<br>
     * 继承自ArrayList<DataRow>,DataRow继承自ArrayList<String>
     */
    public final DataRowCollection rows; //行
    /**
     * 表名
     */
    protected String tableName = ""; // 表名

    public DataTable() {
        this.columns = new DataColumn();
        this.rows = new DataRowCollection(this);
    }

    public DataRowCollection getRows() {
        return this.rows;
    }

    public DataTable(JSONArray jsonArr) throws JSONException {
        this();

        if (null == jsonArr) {
            return;
        }
        int length = jsonArr.length();
        if (length > 0) {
            // 列
            setColumns(jsonArr.getJSONArray(0));

            // 行
            for (int i = 1; i < length; i++) {
                addRow(jsonArr.getJSONArray(i));
            }
        }
    }

    public DataTable(DataColumn col) {
        this.columns = col;
        this.rows = new DataRowCollection(this);
    }

    //清空当前表并用另外一个表初始化
    public void iniTable(DataTable dt) {
        columns.clear();
        columns.addAll(dt.columns);
        rows.clear();
        for (DataRow dr : dt.rows) {
            rows.add(dr);
        }
    }

    /**
     * 获取记录数
     *
     * @return
     */
    public int getRowsCount() {
        return rows.size();
    }

    /**
     * 将一个数组生成为表格头<br>
     * 参数样式: <br>
     * ["rownum","CompanyID","BillNo"],<br>
     */
    public void setColumns(JSONArray jsonArr) throws JSONException {
        DataColumn tmp = new DataColumn(jsonArr);
        this.columns.clear();
        for (String str : tmp) {
            this.columns.add(str);
        }
    }

    /**
     * 将一个数组生成为表格头<br>
     * 参数样式: <br>
     * {"rownum","CompanyID","BillNo"},<br>
     */
    public void setColumns(String[] arr) {
        this.columns.clear();
        for (String str : arr) {
            this.columns.add(str);
        }
    }

    /**
     * 将一个数组生成为表格头<br>
     * 参数样式: <br>
     * ["rownum","CompanyID","BillNo"],<br>
     */
    public void setColumns(DataColumn columns) {
        this.columns.clear();
        for (String str : columns) {
            this.columns.add(str);
        }
    }

    /**
     * 添加列
     *
     * @param colName 列名,默认值为""
     */
    public void addColumn(String colName) {
        addColumn(colName, "");
    }

    /**
     * 添加列
     *
     * @param colName      列名
     * @param defaultValue 默认值
     */
    public void addColumn(String colName, String defaultValue) {
        if (this.columns.add(colName)) {
            for (DataRow dr : this.rows) {
                dr.add(defaultValue);
            }
        }
    }

    /**
     * 移除列
     *
     * @param name
     * @return true:移除成功,false:没有找到列或者移除失败
     */
    public boolean remove(String name) {
        int index = columns.getIndex(name);
        if (index < 0) {
            return false;
        }
        columns.remove(index);
        for (DataRow row : rows) {
            row.remove(index);
        }
        return true;
    }

    /**
     * 添加行到表格,复制数据的模式<br>
     * 添加的行没有做列名检查,务必保证来源DataRow的列与目标table中的列完全相同,<br>
     * 否则取数会发生错误,来源DataRow中多出的列也为无法按列名获取到
     */
    public void addRow(DataRow row) {
        this.rows.add(row.copy(this));
    }

    /**
     * 将一个Json数组生成为一行<br>
     * 只传数据,不用传列名<br>
     * 按json数组中字符的顺序添加到新行的单元格中<br>
     * 参数样式: <br>
     * ["1","HK","IM130816HKH01-001"]
     *
     * @param jsonArr
     * @throws JSONException
     */
    public void addRow(JSONArray jsonArr) throws JSONException {
        this.rows.add(new DataRow(this, jsonArr));
    }

    /**
     * 从一个Json二维数组添加多行 <br>
     * 参数样式: <br>
     * ["1","HK" ,"IM130816HKH01-001"],<br>
     * ["2","HK","IM130806HKH01-001"]
     *
     * @param jsonArr
     * @throws JSONException
     */
    public void addRowsFromJson(JSONArray jsonArr) throws JSONException {
        int length = jsonArr.length();
        for (int i = 0; i < length; i++) {
            addRow(jsonArr.getJSONArray(i));
        }
    }

    /**
     * 从一个Json二维数组添加多行,Json数组的第一行是列名,第二行开始为数据,<br>
     * 注意: 这里直接忽略第一行,直接按Json数组的顺序添加数据到表格(不匹配列名) <br>
     * 参数样式: <br>
     * ["rownum","CompanyID","BillNo"],<br>
     * ["1","HK" ,"IM130816HKH01-001"],<br>
     * ["2","HK","IM130806HKH01-001"]
     *
     * @param jsonArr
     * @throws JSONException
     */
    public void addRowsIgnoreColumns(JSONArray jsonArr) throws JSONException {
        int length = jsonArr.length();
        // 从1开始,过滤掉表格头
        for (int i = 1; i < length; i++) {
            addRow(jsonArr.getJSONArray(i));
        }
    }

    /**
     * 新增一行到表里面,并返回句柄
     *
     * @return
     */
    public DataRow newRow2Table() {
        DataRow dr = new DataRow(this);
        this.rows.add(dr);
        return dr;
    }

    public String get(int row, String colName) {
        return this.rows.get(row).get(colName);
    }

    public String get(int row, int col) {
        return this.rows.get(row).get(col);
    }

    public void setValue(int row, int col, String value) {
        this.rows.get(row).set(col, value);
    }

    public void setValue(int row, String colName, String value) {
        this.rows.get(row).set(colName, value);
    }

    // 转换成json格式的二维数组
    public JSONArray toJson() {
        JSONArray jsonTable = new JSONArray();
        jsonTable.put(new JSONArray(columns));
        for (DataRow dr : rows) {
            jsonTable.put(new JSONArray(dr));
        }
        return jsonTable;
    }

    // ---------------------------------工具方法-----------------------

    /**
     * 判断数据集的第一个表格是否有数据
     *
     * @param ds
     * @return true:对象为空,或者没有数据<br>
     * false:知识有一行数据
     */
    public static boolean isNull(DataTable[] ds) {
        if (null == ds || ds.length < 1 || isNull(ds[0])) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断表格是否有数据
     *
     * @param table
     * @return true:对象为空,或者没有数据<br>
     * false:知识有一行数据
     */
    public static boolean isNull(DataTable table) {
        if (null == table || null == table.rows || table.rows.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查 是否有数据
     *
     * @return
     */
    public boolean isNull() {
        if (isNull(this)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查 是否有数据
     *
     * @return
     */
    public boolean haveData() {
        return !isNull();
    }

    /**
     * 根据某列顺序排序
     *
     * @param colName 列名
     * @param isDesc  是否倒叙: false:正序; true:倒叙
     */
    public void sortByColumn(String colName, boolean isDesc) {
        if (isNull(this)) {
            return;
        }
        if (DataColumn.ColType.Numeric == columns.getColType(colName)) {
            sortByNumericColumn(colName, isDesc);
        } else {
            sortByStringColumn(colName, isDesc);
        }
    }

    private void sortByStringColumn(String colName, boolean isDesc) {
        int size = rows.size();
        for (int i = 0; i < size - 1; i++) {
            DataRow currentDR = rows.get(i);
            int currentIndex = i;

            String value = currentDR.get(colName);
            if (null == value) {
                continue;
            }
            for (int j = i + 1; j < size; j++) {
                String value2 = rows.get(j).get(colName);
                if (isDesc) {
                    if (value.compareToIgnoreCase(value2) < 0) {
                        currentDR = rows.get(j);
                        currentIndex = j;
                        value = currentDR.get(colName);
                    }
                } else {
                    if (value.compareToIgnoreCase(value2) > 0) {
                        currentDR = rows.get(j);
                        currentIndex = j;
                        value = currentDR.get(colName);
                    }
                }
            }

            if (i != currentIndex) {
                rows.set(currentIndex, rows.get(i));
                rows.set(i, currentDR);
            }
        }
    }

    private void sortByNumericColumn(String colName, boolean isDesc) {
        int size = rows.size();
        for (int i = 0; i < size - 1; i++) {
            DataRow currentDR = rows.get(i);
            int currentIndex = i;

            double value = currentDR.getDouble(colName);
            for (int j = i + 1; j < size; j++) {
                double value2 = rows.get(j).getDouble(colName);
                if (isDesc) {
                    if (value < value2) {
                        currentDR = rows.get(j);
                        currentIndex = j;
                        value = currentDR.getDouble(colName);
                    }
                } else {
                    if (value > value2) {
                        currentDR = rows.get(j);
                        currentIndex = j;
                        value = currentDR.getDouble(colName);
                    }
                }
            }

            if (i != currentIndex) {
                rows.set(currentIndex, rows.get(i));
                rows.set(i, currentDR);
            }
        }
    }

    /**
     * 简单查询查询过滤,相当于 Top 1 * From ...
     *
     * @param colName     要比较的列
     * @param columnValue 要匹配的列值
     * @return
     */
    public DataRow selectFrist(String colName, String columnValue) {
        if (isNull(this) || null == colName || null == columnValue) {
            return null;
        }
        for (DataRow dr : rows) {
            if (columnValue.equalsIgnoreCase(dr.get(colName))) {
                return dr;
            }
        }
        return null;
    }

    /**
     * 简单查询,相当于 Top 1 * From ...,<br>
     * colNames 和 colValues 的数量必须相等
     *
     * @param colNames  要比较的列名称
     * @param colValues 要匹配的列值
     * @return
     */
    public DataRow selectFrist(String[] colNames, String[] colValues) {
        if (isNull(this) || null == colNames || null == colValues) {
            return null;
        }
        if (colNames.length != colValues.length) {
            return null;
        }

        if (colNames.length <= 0) {
            return null;
        }
        for (DataRow dr : rows) {
            boolean isFind = true;
            for (int i = 0; i < colNames.length; i++) {
                String colName = colNames[i];
                String columnValue = colValues[i];
                if (columnValue.equalsIgnoreCase(dr.get(colName))) {
                    continue;
                } else {
                    isFind = false;
                }
            }
            if (isFind) {
                return dr;
            }
        }
        return null;
    }

    // ------------------------------以下未实现--------------------------------//

    /**
     * 功能未实现<br>
     * 查询过滤
     *
     * @param tableName
     * @param selectField
     * @param filterString
     * @param groupField
     * @return
     */
    public DataTable select(String tableName, String selectField,
                            String filterString, String groupField) {
        return null;
    }

    /**
     * 功能未实现<br>
     */
    public Object sum(String colName, String filter) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @return
     */
    public Object max(String columns, String filter) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @return
     */
    public Object min(String columns, String filter) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @return
     */
    public Object avg(String columns, String filter) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @param groupBy
     * @return
     */
    public Object max(String columns, String filter, String groupBy) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @param groupBy
     * @return
     */
    public Object min(String columns, String filter, String groupBy) {
        return null;
    }

    /**
     * 功能未实现<br>
     *
     * @param columns
     * @param filter
     * @param groupBy
     * @return
     */
    public Object avg(String columns, String filter, String groupBy) {
        return null;
    }
}