package com.yihujiu.util.table;

import org.json.JSONArray;

import java.util.ArrayList;

public class DataRow extends ArrayList<String> {
    private static final long serialVersionUID = 1246271586128482124L;
    // table的一个引用
    private DataTable table;

    /**
     * 根据参数table的列创建一个带默认值的空行
     *
     * @param table : 行所属的表
     */
    public DataRow(DataTable table) {
        super();
        this.table = table;
        for (String columnName : table.columns) {
            String defaultValue = table.columns.getDefaultValue(columnName);
            this.add(defaultValue);//有添加一个默认值,没有添加一个"";
        }
    }

    /**
     * 根据json数组创建一个行,仅按顺序复制行,不检查表结构
     *
     * @param table     : 行所属的表
     * @param jsonArray : json数组,纯字符串数组例如 ["a","b","c"]
     */
    public DataRow(DataTable table, JSONArray jsonArray) {
        super();
        this.table = table;
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            add(jsonArray.optString(i));
        }
    }

    /**
     * 根据list创建一个行,仅按顺序复制行,不检查表结构
     *
     * @param table : 行所属的表
     * @param list
     */
    public DataRow(DataTable table, ArrayList<String> list) {
        super();
        this.table = table;
        for (String str : list) {
            add(str);
        }
    }

    /**
     * 从现有的行dr创建新行,按顺序复制数据,不检查表结构
     *
     * @param table
     * @param dr
     */
    public DataRow(DataTable table, DataRow dr) {
        this.table = table;
        for (String str : dr) {
            add(str);
        }
    }

    /**
     * 返回一个新的行,按顺序复制数据,不检查表结构
     *
     * @param dt : 新行所属的表格,
     * @return
     */
    public DataRow copy(DataTable dt) {
        return new DataRow(dt, this);
    }

    public DataColumn getColumns() {
        return table.columns;
    }

    public String set(int index, String value) {
        return super.set(index, value);
    }

    public String set(String columnName, String value) {
        try {
            return set(getColumnIndex(columnName), value);
        } catch (Exception e) {
            // 一般是没有找到列,导致报错
            return "";
        }
    }

    public String get(String columnName) {
        try {
            return get(getColumnIndex(columnName));
        } catch (Exception e) {
            // 一般是没有找到列,导致报错
            return "";
        }
    }

    public String showAll() {
        String str = "";
        for (int i = 0; i < size(); i++) {
            str += get(i) + ";";
        }
        return str;
    }

    public int getInt(int index) {
        return toInt(get(index));
    }

    public int getInt(String columnName) {
        return toInt(get(columnName));
    }

    public double getDouble(String columnName) {
        return toDouble(get(columnName));
    }

    /**
     * 格式化为整数,输出整数型字符串
     *
     * @param columnName
     */
    public String getIntStr(String columnName) {
        return toInt(get(columnName)) + "";
    }

    // 获取列的序号
    private int getColumnIndex(String columnName) {
        return table.columns.getIndex(columnName);
    }

    // ---------------------工具---------------------------

    /**
     * 取数值字符串的整数部分
     */
    public static int toInt(String strNumber) {
        return (int) Math.floor(toDouble(strNumber));
    }

    public static double toDouble(String strNumber) {
        try {
            return Double.parseDouble(strNumber);
        } catch (Exception e) {
            return 0;
        }
    }
}