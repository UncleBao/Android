package com.sd.pos.table;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * 列不区分大小写
 */
public class DataColumn extends ArrayList<String> {
    private static final long serialVersionUID = -7806675152861839553L;

    /**
     * 列类型
     */
    public static enum ColType {
        String, Numeric
    }

    // 默认值
    private HashMap<String, String> mapDefaultValue = new HashMap<String, String>();
    // 列类型
    private HashMap<String, ColType> mapColType = new HashMap<String, ColType>();
    // 列位置
    private HashMap<String, Integer> mapColIndex = new HashMap<String, Integer>();

    public DataColumn() {
        super();
    }

    public DataColumn(JSONArray arrObj) throws JSONException {
        int len = arrObj.length();
        for (int i = 0; i < len; i++) {
            add(arrObj.optString(i));
        }
    }

    public DataColumn(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            add(list.get(i));
        }
    }

    public int getIndex(String columnName) {
        return mapColIndex.get(toLowerCase(columnName));
    }

    //重写新增元素方法
    @Override
    public boolean add(String columnName) {
        boolean result = super.add(toLowerCase(columnName));
        if (result) {
            refreshColIndex();
        }
        return result;
    }

    @Override
    public void add(int index, String columnName) {
        //TODO 这里需要判断table内是否有row, 如果有row就不能新增列
        super.add(index, toLowerCase(columnName));
        refreshColIndex();
    }

    public void add(String columnName, String defaultValue) {
        //TODO 这里需要判断table内是否有row, 如果有row就不能新增列
        add(toLowerCase(columnName));
        mapDefaultValue.put(toLowerCase(columnName), defaultValue);
    }

    private void refreshColIndex() {
        //重新获取列索引
        mapColIndex.clear();
        ListIterator<String> it = super.listIterator();
        int i = 0;
        while (it.hasNext()) {
            mapColIndex.put(((String) it.next()).toLowerCase(), i);
            i++;
        }
    }

    //获取默认值
    public String getDefaultValue(int index) {
        return mapDefaultValue.get(get(index));
    }

    public String getDefaultValue(String columnName) {
        String defaultValue = mapDefaultValue.get(toLowerCase(columnName));
        if (null == defaultValue) {
            defaultValue = "";
        }
        return defaultValue;
    }

    //设置列类型,方便排序等操作
    public ColType getColType(String columnName) {
        return mapColType.get(toLowerCase(columnName));
    }

    public void setColType(String columnName, ColType type) {
        mapColType.put(toLowerCase(columnName), type);
    }

    public void setColType(int index, ColType type) {
        mapColType.put(get(index), type);
    }

    private String toLowerCase(String columnName) {
        if (null == columnName) {
            columnName = "";
        }
        return columnName.toLowerCase();
    }

}
