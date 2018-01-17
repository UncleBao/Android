package com.sd.pos.pages;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.pos.BaseFragment;
import com.sd.pos.R;
import com.sd.pos.comm.Config;
import com.sd.pos.dbhelp.BillHelper;
import com.sd.pos.ex.DialogInputText;
import com.sd.pos.ex.SelectWithTable;
import com.sd.pos.task.TaskSaveLsdMST;
import com.yihujiu.util.Util;
import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;
import com.sd.pos.task.TaskReadBar;
import com.yihujiu.util.view.CommonTableAdapter;
import com.yihujiu.util.view.TextWatcher4Enter;
import com.sd.pos.task.TaskGetSTKList;
import com.yihujiu.util.view.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * POS
 */
public class PagePos extends BaseFragment implements OnClickListener, AdapterView.OnItemLongClickListener {

    TextView vStockName;
    EditText vUserCode, vBarcode, vManualBillNo;
    ListView vList;
    CommonTableAdapter adapter;
    TextView vQtyALL, vAmountALL;
    Button vCreate, vGetBill, vSaveBill, vSubmitBill;

    DataTable StockList;
    DataTable dtDetail;

    String StockCode;

    final String LastStockCode = "LastStockCode";
    final String LastStockName = "LastStockName";

    BillHelper billHelper;

    @Override
    protected int getLayout() {
        return R.layout.page_pos;
    }

    @Override
    protected void ini() {
        vStockName = (TextView) findViewById(R.id.page_pos_stock);
        vUserCode = (EditText) findViewById(R.id.page_pos_user);
        vBarcode = (EditText) findViewById(R.id.page_pos_barcode);
        vManualBillNo = (EditText) findViewById(R.id.page_pos_ManualBill);
        vList = (ListView) findViewById(R.id.page_pos_list);
        vQtyALL = (TextView) findViewById(R.id.page_pos_foot_qty);
        vAmountALL = (TextView) findViewById(R.id.page_pos_foot_amount);

        vCreate = (Button) findViewById(R.id.page_pos_CreateBill);
        vGetBill = (Button) findViewById(R.id.page_pos_GetBill);
        vSaveBill = (Button) findViewById(R.id.page_pos_SaveBill);
        vSubmitBill = (Button) findViewById(R.id.page_pos_SubmitBill);

        vList.setOnItemLongClickListener(this);
        vStockName.setOnClickListener(this);
        vCreate.setOnClickListener(this);
        vGetBill.setOnClickListener(this);
        vSaveBill.setOnClickListener(this);
        vSubmitBill.setOnClickListener(this);
        vBarcode.addTextChangedListener(new TextWatcher4Enter(vBarcode) {
            @Override
            public void onScanEnter(String str) {
                System.out.println(str);
                getDetail(str);
            }
        });

        vUserCode.setText(Config.UserCode);
        vBarcode.requestFocus();

        StockCode = enumHelper.getPreferences(LastStockCode);
        vStockName.setText(enumHelper.getPreferences(LastStockName));

        billHelper = new BillHelper(activity);

        newBill();
    }

    //获取仓库列表
    public void getStockList() {
        TaskGetSTKList taskGetSTKList = new TaskGetSTKList(activity, getStr(vUserCode)) {

            @Override
            protected void onTaskSuccessAndHaveData(DataTable table, boolean isAsk, String msg, ArrayList<String> list) {
                StockList = table;
                showStockSelect();
            }
        };
        taskGetSTKList.execute();
    }

    //显示仓库列表
    private void showStockSelect() {
        if (DataTable.isNull(StockList)) {
            getStockList();
            return;
        }
        SelectWithTable select = new SelectWithTable(activity, StockList, "usercode", "username") {
            @Override
            public void onItemClick(int menuIndex, DataRow dr, String id, String name) {
                enumHelper.savePreferences(LastStockCode, id);
                enumHelper.savePreferences(LastStockName, name);
                StockCode = id;
                vStockName.setText(name);
            }

            @Override
            protected void onRefresh() {

            }
        };
        select.show();
    }

    private void getDetail(String barcode) {
        //根据条码获取数据
        TaskReadBar task = new TaskReadBar(activity, barcode, StockCode) {
            @Override
            protected void onTaskSuccessAndHaveData(DataTable table, boolean isAsk, String msg, ArrayList<String> list) {
                //["fgdcode","fgdname","fyscode","fysname","fccode","fcname","saleprice"]
                //补充没有的列
                table.addColumn("discount");
                table.addColumn("qty");
                table.addColumn("amount");
                table.addColumn("barcode");
                DataRow dr = table.rows.get(0);
                dr.set("discount", "1");
                dr.set("qty", "1");
                dr.set("amount", "1");
                dr.set("barcode", barcode);
                if (DataTable.isNull(dtDetail)) {
                    dtDetail = table;
                    reBuildListWithDetail();
                } else {
                    dtDetail.addRow(dr);
                }
                calcDetail();
            }
        };
        task.execute();
    }

    private void reBuildListWithDetail() {
        adapter = new CommonTableAdapter(activity, dtDetail, R.layout.page_pos_i) {
            @Override
            public void convert(ViewHolder holder, int position, DataRow item) {
                holder.setText(R.id.page_pos_i_xh, (position + 1) + "");
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
        calcDetail();
    }

    public void calcDetail() {
        int qtyALL = 0;
        double amountALL = 0;
        if (DataTable.isNull(dtDetail)) {
        } else {
            for (DataRow dr : dtDetail.rows) {
                double price = dr.getDouble("saleprice");
                double discount = dr.getDouble("discount");
                int qty = dr.getInt("qty");
                double amount = price * discount * qty;
                dr.set("amount", amount + "");
                qtyALL += qty;
                amountALL += amount;
            }
            adapter.notifyDataSetChanged();
        }
        vQtyALL.setText(qtyALL + "");
        vAmountALL.setText(Util.round(amountALL, 2) + "");
    }

    private void submitBill() {
        TaskSaveLsdMST task = new TaskSaveLsdMST(activity, StockCode, getStr(vUserCode), dtDetail) {
            @Override
            public void onTaskSuccess(JSONObject jsonObj) {
                PagePos.this.activity.showDialogOK("提交成功");
                newBill();
            }
        };
        task.execute();
    }

    //初始化单据信息
    private void newBill() {
        String ManualBillNo = getStr(vManualBillNo);
        if (!isNull(ManualBillNo)) {
            billHelper.delBillDetail(ManualBillNo);
        }
        dtDetail = null;
        vList.setAdapter(null);
        calcDetail();
        vBarcode.setText("");
        vBarcode.requestFocus();
        vManualBillNo.setText("LSD" + Util.timeFormat("yyMMddHHmmss", new Date()));
    }

    private void saveBill() {
        if (DataTable.isNull(dtDetail)) {
            return;
        }
        String ManualBillNo = getStr(vManualBillNo);
        if (isNull(ManualBillNo)) {
            toast("手工单号不能为空!");
            return;
        }
        if (billHelper.saveBill(ManualBillNo, getStr(vUserCode), StockCode, getStr(vStockName), dtDetail)) {
            toast("挂单成功!");
            newBill();
        } else {
            toast("挂单失败!");
        }
    }

    private void getBill() {
        DataTable dt = billHelper.getBill4Select();
        System.out.println(dt.rows.size());
        SelectWithTable select = new SelectWithTable(activity, dt, "ManualBillNo", "ManualBillNo") {
            @Override
            public void onItemClick(int menuIndex, DataRow dr, String id, String name) {
                resumeWithManualBillNo(id);
            }

            @Override
            protected void onRefresh() {

            }
        };
        select.show();
    }

    //跟进手工单号从数据库加载订单
    private void resumeWithManualBillNo(String ManualBillNo) {
        DataRow master = billHelper.getBillMaster(ManualBillNo);
        if (null == master) {
            return;
        }
        newBill();
        vUserCode.setText(master.get("UserCode"));
        StockCode = master.get("StockCode");
        vStockName.setText(master.get("StockName"));
        vManualBillNo.setText(master.get("ManualBillNo"));

        dtDetail = billHelper.getBillDetail(ManualBillNo);
        reBuildListWithDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_pos_stock:
                showStockSelect();
                break;
            case R.id.page_pos_CreateBill:
                newBill();
                break;
            case R.id.page_pos_GetBill:
                getBill();
                break;
            case R.id.page_pos_SaveBill:
                saveBill();
                break;
            case R.id.page_pos_SubmitBill:
                submitBill();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final DataRow dr = dtDetail.rows.get(position);
        String discount = dr.get("discount");
        DialogInputText dialog = new DialogInputText(activity, "修改折扣", discount) {
            @Override
            protected void onBtnOKClick(String val) {
                double tmp = Util.toDouble(val);
                if (tmp > 0 && tmp <= 1) {
                    dr.set("discount", tmp + "");
                    calcDetail();
                } else {
                    toast("折扣应该在0和1之间!");
                }
            }

            @Override
            protected void ini() {
                super.ini();
                vEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        };
        dialog.show();
        return true;
    }
}
