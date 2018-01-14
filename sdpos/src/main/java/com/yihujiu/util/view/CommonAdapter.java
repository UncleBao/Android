package com.yihujiu.util.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的适配器
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> list;
    protected final int mItemLayoutId;
    private int focusIndex = -1; //焦点行,focusIndex>0时,焦点行突出显示,其他行背景为透明

    //itemLayoutId: item的布局
    public CommonAdapter(Context context, List<T> datas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = datas;
        this.mItemLayoutId = itemLayoutId;
    }

    public void setFocusIndex(int focusIndex) {
        this.focusIndex = focusIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position));
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

    public abstract void convert(ViewHolder holder, T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }


/*
使用例子
//activity中使用adapter可以这样写
        CommonAdapter mAdapter = new CommonAdapter<String>(getApplicationContext(),
				R.layout.item_single_str, list)
		{
			@Override
			protected void convert(ViewHolder viewHolder, String item)
			{
				viewHolder.setText(R.id.id_tv_title, item);
			}
		};
*/


}
