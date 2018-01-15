package com.yihujiu.util.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;

/**
 * 通用DataTable的适配器
 */
public abstract class CommonTableAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected DataTable table;
    private int focusIndex = -1; //焦点行,focusIndex>0时,焦点行突出显示,其他行背景为透明
    private int mItemLayoutId;

    //itemLayoutId: item的布局
    public CommonTableAdapter(Context context, DataTable table, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        if (DataTable.isNull(table)) {
            this.table = new DataTable();
        } else {
            this.table = table;
        }
        this.mItemLayoutId = itemLayoutId;
    }

    public void setFocusIndex(int focusIndex) {
        this.focusIndex = focusIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return table.rows.size();
    }

    @Override
    public DataRow getItem(int position) {
        return table.rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, position, getItem(position));
        setBackGround(position, viewHolder.mConvertView);
        return viewHolder.getConvertView();
    }

    //根据当前点击的item,设置item的背景颜色
    public void setBackGround(int position, View convertView) {
        if (focusIndex >= 0) {
            if (position == focusIndex) {
                convertView.setBackgroundColor(Color.parseColor("#ADD8E6"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
    }

    public abstract void convert(ViewHolder holder, int position, DataRow item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }


/*
使用例子
//activity中使用adapter可以这样写
        CommonAdapter mAdapter = new CommonAdapter<String>(getApplicationContext(),
				R.layout.item_single_str, mDatas)
		{
			@Override
			protected void convert(ViewHolder viewHolder, String item)
			{
				viewHolder.setText(R.id.id_tv_title, item);
			}
		};
*/


}
