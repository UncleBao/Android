package com.sd.pos.pages;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.pos.BaseFragment;
import com.sd.pos.R;
import com.sd.pos.comm.Config;
import com.sd.pos.ex.SelectWithTable;
import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;
import com.sd.pos.task.TaskReadBar;
import com.yihujiu.util.view.CommonTableAdapter;
import com.yihujiu.util.view.TextWatcher4Enter;
import com.sd.pos.task.TaskGetSTKList;
import com.yihujiu.util.view.ViewHolder;

import java.util.ArrayList;

/**
 * POS
 */
public class PagePos extends BaseFragment implements OnClickListener {

    TextView vStock;
    EditText vUser, vBarcode;
    ListView vList;
    CommonTableAdapter adapter;

    DataTable StockList;
    DataTable dtDetail;

    String StockCode;

    final String LastStockCode = "LastStockCode";
    final String LastStockName = "LastStockName";

    @Override
    protected int getLayout() {
        return R.layout.page_pos;
    }

    @Override
    protected void ini() {
        vStock = (TextView) findViewById(R.id.page_pos_stock);
        vUser = (EditText) findViewById(R.id.page_pos_user);
        vBarcode = (EditText) findViewById(R.id.page_pos_barcode);
        vList = (ListView) findViewById(R.id.page_pos_list);

        vStock.setOnClickListener(this);
        vBarcode.addTextChangedListener(new TextWatcher4Enter(vBarcode) {
            @Override
            public void onScanEnter(String str) {
                System.out.println(str);
                //根据条码获取数据
                TaskReadBar task = new TaskReadBar(activity, str) {
                    @Override
                    protected void onTaskSuccessAndHaveData(DataTable table, boolean isAsk, String msg, ArrayList<String> list) {
                        //["fgdcode","fgdname","fyscode","fysname","fccode","fcname","saleprice"]
                        //补充没有的列
                        table.addColumn("discount");
                        table.addColumn("qty");
                        table.addColumn("amount");
                        DataRow dr = table.rows.get(0);
                        dr.set("discount", "1");
                        dr.set("qty", "1");
                        dr.set("amount", "1");
                        if (DataTable.isNull(dtDetail)) {
                            dtDetail = table;
                            adapter = new CommonTableAdapter(activity, dtDetail, R.layout.page_pos_i) {
                                @Override
                                public void convert(ViewHolder holder, int position, DataRow item) {
                                    holder.setText(R.id.page_pos_i_xh, position + "");
                                    holder.setText(R.id.page_pos_i_fgdcode, item.get("fgdcode"));
                                    holder.setText(R.id.page_pos_i_fgdname, item.get("fgdname"));
                                    holder.setText(R.id.page_pos_i_fyscode, item.get("fyscode"));
                                    holder.setText(R.id.page_pos_i_fysname, item.get("fysname"));
                                    holder.setText(R.id.page_pos_i_fccode, item.get("fccode"));
                                    holder.setText(R.id.page_pos_i_fcname, item.get("fcname"));
                                    holder.setText(R.id.page_pos_i_saleprice, item.get("saleprice"));
                                    holder.setText(R.id.page_pos_i_discount, item.get("discount"));
                                    holder.setText(R.id.page_pos_i_qty, item.get("qty"));
                                    holder.setText(R.id.page_pos_i_amount, item.get("amount"));
                                }
                            };
                            vList.setAdapter(adapter);
                        } else {
                            dtDetail.addRow(dr);
                        }
                        calcDetail();
                    }
                };
                task.execute();
            }
        });

        vUser.setText(Config.UserCode);
        vBarcode.requestFocus();

        StockCode = enumHelper.getPreferences(LastStockCode);
        vStock.setText(enumHelper.getPreferences(LastStockName));
    }

    public void calcDetail() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //获取仓库列表
    public void GetStockList() {
        TaskGetSTKList taskGetSTKList = new TaskGetSTKList(activity, Config.UserCode) {

            @Override
            protected void onTaskSuccessAndHaveData(DataTable table, boolean isAsk, String msg, ArrayList<String> list) {
                StockList = table;
                ShowStockSelect();
            }
        };
        taskGetSTKList.execute();
    }

    //显示仓库列表
    private void ShowStockSelect() {
        if (DataTable.isNull(StockList)) {
            GetStockList();
            return;
        }
        SelectWithTable select = new SelectWithTable(activity, StockList, "usercode", "username") {
            @Override
            public void onItemClick(int menuIndex, DataRow dr, String id, String name) {
                enumHelper.savePreferences(LastStockCode, id);
                enumHelper.savePreferences(LastStockName, name);
                StockCode = id;
                vStock.setText(name);
            }

            @Override
            protected void onRefresh() {

            }
        };
        select.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_pos_stock:
                ShowStockSelect();
                break;
            case R.id.page_pos_CreateBill:
                break;
            case R.id.page_pos_GetBill:
                break;
            case R.id.page_pos_SaveBill:
                break;
            case R.id.page_pos_CommitBill:
                break;
        }
    }
}
