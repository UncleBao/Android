package com.sd.pos.task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/13.
 */

public class NetBase {

    /**
     * 字符串常量
     *
     * @author yi.zhe
     * @time 2014-7-1 下午2:13:31
     */
    public class Str {
        /*
         * 公共调用参数
         */
        public final static String CompanyID = "CompanyID"; // 公司id
        public final static String UserID = "UserID"; //
        public final static String APIKey = "APIKey"; //
        public final static String SessionKey = "SessionKey";

        /*
         * 公共返回参数
         */
        public final static String IsOK = "IsOK";
        public final static String IsAsk = "IsAsk";
        public final static String IsCut = "IsCut";
        public final static String Msg = "Msg";
        public final static String MsgList = "MsgList";//
        public final static String List = "list";//
        public final static String ListDetail = "listDetail";//
        public final static String Table = "table";//
        public final static String Table0 = "table0";//
        public final static String Table1 = "table1";//
        public final static String Table2 = "table2";

    }

    // -------------------------------------------基础方法--------------------------//

    /**
     * 创建一个基础参数表,包含公共的参数
     * <p/>
     * paramList
     */
    public static JSONObject createBasParam() throws JSONException {
        JSONObject params = new JSONObject();
//        params.put(Config.Str.CompanyID, Config.CompanyID);
//        params.put(Config.Str.UserID, Config.UserID);
//        params.put(Config.Str.APIKey, Config.APIKey);
//        params.put(Config.Str.SessionKey, Config.SessionKey);
        return params;
    }

    /**
     * 判断网络任务是否成功
     */
    public static boolean isOK(JSONObject jsonObject) {
        String resultCode = jsonObject.optString(Str.IsOK);
        if (resultCode.equalsIgnoreCase("true")
                || resultCode.equalsIgnoreCase("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否询问
     */
    public static boolean getIsAsk(JSONObject jsonObject) {
        return jsonObject.optBoolean(Str.IsAsk);
    }

    /**
     * 解析网络任务错误原因
     */
    public static String getMsg(JSONObject jsonObject) {
        return jsonObject.optString(Str.Msg);
    }

    /**
     * 获取消息列表
     */
    public static ArrayList<String> getMsgList(JSONObject jsonObject) {
        ArrayList<String> msgList = null;
        try {
            // 获取返回的一些数据
            JSONArray ja = jsonObject.getJSONArray(Str.MsgList);
            if (null != ja && ja.length() > 0) {
                msgList = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    msgList.add(ja.getString(i));
                }
            }
        } catch (JSONException e) {
            // 防止list节点为空的情况
        }
        return msgList;
    }

    /**
     * 创建标准的调用参数
     */
    public static String createParam(String label, String[] arr)
            throws JSONException {
        JSONObject params = createBasParam();
        if (null != arr) {
            JSONArray jsonArray = new JSONArray();
            for (String str : arr) {
                jsonArray.put(str);
            }
            params.put(Str.List, jsonArray);
        } else {
            params.put(Str.List, null);
        }
        return params.toString();
    }

    /**
     * 创建标准的调用参数
     */
    public static String createParam(String label, String[] arr, ArrayList<String> listDetail)
            throws JSONException {
        JSONObject params = createBasParam();
//        params.put(Config.Str.Label, label);
        if (null != arr) {
            JSONArray jsonArray = new JSONArray();
            for (String str : arr) {
                jsonArray.put(str);
            }
            params.put(Str.List, jsonArray);
        } else {
            params.put(Str.List, null);
        }
        if (null != listDetail && listDetail.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (String str : listDetail) {
                jsonArray.put(str);
            }
            params.put(Str.ListDetail, jsonArray);
        } else {
            params.put(Str.ListDetail, null);
        }
        return params.toString();
    }

    /**
     * 解析网络任务错误原因
     */
    public static String createError(Exception e) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"");
        sb.append(Str.IsOK);
        sb.append("\":false,\"");
        sb.append(Str.Msg);
        sb.append("\":\"");
        sb.append(e.toString());
        sb.append("\"}");
        return sb.toString();
    }


}
