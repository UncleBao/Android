package com.sd.pos.ex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sd.pos.R;

import java.util.List;

/**
 * Created by yi.zhe on 2016/4/30.
 * 通用的适配器
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> list;
    protected final int mItemLayoutId;
    //该属性使用于设置当前的listview是否需要实现点击改变背景颜色,可在convert方法里面设置其属性
    protected boolean IsNeedSetBackGround = false;
    private int focusIndex = 0;

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
        setBackGround(position,viewHolder.mConvertView);
        return viewHolder.getConvertView();
    }

    //根据当前点击的item,设置item的背景颜色
    public void setBackGround(int position, View convertView) {
        if (IsNeedSetBackGround) {
            if (position == focusIndex) {
                convertView.setBackgroundResource(R.color.list_bg_focus);
            } else {
                convertView.setBackgroundResource(R.color.transparent);
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
