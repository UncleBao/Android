package com.sd.pos.table;

import java.util.ArrayList;

public class DataRowCollection extends ArrayList<DataRow> {

	private static final long serialVersionUID = -4052603768143682140L;
	private DataTable table;

	public DataRowCollection(DataTable table) {
		this.table = table;
	}

	public DataTable getTable() {
		return table;
	}

	/**
	 * 添加行到表格,复制数据的模式<br>
	 * 添加的行没有做列名检查,务必保证来源DataRow的列跟目标对象中的列完全相同,<br>
	 * 否则取数会发生错误,来源DataRow中多出的列以为无法按列名获取到
	 * 
	 * @return
	 */
	@Override
	public boolean add(DataRow dr) {
		return super.add(dr.copy(table));
	}

	/**
	 * 返回当前表中有多少行
	 * 
	 * @return
	 */
	public int getSize() {
		return size();
	}
}
