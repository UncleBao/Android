package com.yihujiu.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 工具类:包括最常用的字符串为空的检验,时间的格式化,数字的格式化,取精度,sql语句拼接等
 */
public class Util {

	/**
	 * 检查字符串是否是空对象或空字符串
	 * 
	 * @param str
	 * @return 为空返回true,不为空返回false
	 */
	public static boolean isNull(String str) {
		if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equalsIgnoreCaseIgnoreNull(String str1, String str2) {
		if (null == str1) {
			str1 = "";
		}
		if (null == str2) {
			str2 = "";
		}
		return str1.equalsIgnoreCase(str2);
	}

	/*
	 * px：像素。 in：英寸。 mm：毫米。 pt：磅。 dp：与密度无关的像素，基于160dpi（每英寸的像素数）屏幕（尺寸适应屏幕密度）。
	 * sp：与比例无关的像素（这种尺寸支持用户调整大小，适合在字体中使用）。
	 */

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// --------------------------------------time--------------------------------------//
	/**
	 * 获取当前时间, 格式"yyyy-MM-dd"
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormatDateShort(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String timeFormatTime() {
		Date d = new Date();
		return timeFormat(d);
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd 00:00:00"
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormatDateBegin(Date d) {
		String str = new SimpleDateFormat("yyyy-MM-dd").format(d);
		return str + " 00:00:00";
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd 23:59:59"
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormatDateEnd(Date d) {
		String str = new SimpleDateFormat("yyyy-MM-dd").format(d);
		return str + " 23:59:59";
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormat(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param fomatStr
	 *            格式化字符串
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormat(String fomatStr) {
		return new SimpleDateFormat(fomatStr).format(new Date());
	}

	/**
	 * 获取当前时间, 格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param fomatStr
	 *            格式化字符串
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormat(String fomatStr, Date d) {
		return new SimpleDateFormat(fomatStr).format(d);
	}

	/**
	 * 将时间字符串格式化为只有年月日部分的短日期字符串 <br>
	 * 取空格前办部分
	 * 
	 * @param timeStr
	 *            时间字符串
	 * @return 短日期
	 */
	public static String timeFormatStr2Short(String timeStr) {
		return timeStr.split(" ")[0];
	}

	/**
	 * 将时间字符串格式化为只有年月日部分的短日期字符串 <br>
	 * 取空格前办部分
	 * 
	 * @param timeStr
	 *            时间字符串
	 * @return 短日期
	 */
	public static String timeFormatStr2NoSecond(String timeStr) {
		try {
			return timeStr.substring(0, timeStr.lastIndexOf(":"));
		} catch (Exception ex) {
			return timeStr;
		}
	}

	/**
	 * 计算两个日期相差多少小时
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long hoursBetween(Date d1, Date d2) {
		return hoursBetween(d1.getTime(), d2.getTime());
	}

	/**
	 * 计算两个日期相差多少小时
	 * 
	 * @param d1
	 *            : Date.getTime()
	 * @param d2
	 *            : Date.getTime()
	 * @return
	 */
	public static long hoursBetween(long d1, long d2) {
		return (d1 - d2) / (3600 * 1000);
	}

	// -----------------------------------转换---------------------------------//
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

	/**
	 * @param str
	 * @return 传入字符串(不区分大小写): true ,yes ,1 返回true,否则返回false
	 */
	public static boolean toBoolean(String str) {
		if ("true".equalsIgnoreCase(str) || "1".equals(str)
				|| "yes".equalsIgnoreCase(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param Str
	 *            等待被分割的字符串,例如: a,b,c
	 * @param splitStr
	 *            : 字符串里面包含的分隔符
	 * @return
	 */
	public static ArrayList<String> str2list(String Str, String splitStr) {
		ArrayList<String> list = new ArrayList<String>();
		if (isNull(Str)) {
			return list;
		}
		String[] _arr = Str.split(splitStr);
		for (String str : _arr) {
			list.add(str);
		}
		return list;
	}

	/**
	 * @param list
	 * @param splitStr
	 *            : 分隔符
	 * @return 含分隔符的字符串,例如: a,b,c
	 */
	public static String list2str(ArrayList<String> list, String splitStr) {
		if (null == list || list.size() == 0) {
			return null;
		}
		String _strArray = "";
		for (String str : list) {
			_strArray += str + ",";
		}
		_strArray = _strArray.substring(0, _strArray.length() - 1);
		return _strArray;
	}

	// ------------------------------------------Math-------------------------------------//
	/**
	 * 将数值型字符串,格式化到只有两位小数
	 * 
	 */
	public static String numericStringFormat2Digits2(String numStr) {
		int length = numStr.length();
		int dotIndex = numStr.indexOf('.');
		if (dotIndex + 2 < length) {
			return numStr.substring(0, dotIndex);
		} else {
			return numStr;
		}
	}

	/**
	 * 金钱格式化,取到小数点后两位,例如:0.00
	 * 
	 * @param money
	 * @return
	 */
	public static String moneyFormat(double money) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(money);
	}

	/**
	 * 去掉多余的.与0
	 * 
	 * @param s
	 *            数字型字符串
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 去掉多余的.与0
	 * 
	 * @param d
	 * @return
	 */
	public static String subZeroAndDot(double d) {
		return subZeroAndDot(d + "");
	}

	/**
	 * 精确到两位小数位,四舍五入
	 * 
	 * @param strNumber
	 * @return
	 */
	public static String getRound2(String strNumber) {
		try {
			double number = Double.parseDouble(strNumber);
			return getRound2(number) + "";
		} catch (Exception e) {
			return "0.00";
		}
	}

	/**
	 * 精确到两位小数位,四舍五入
	 * 
	 */
	public static double getRound2(double number) {
		return round(number, 2);
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param v
	 * @param scale
	 *            保留几位小数 保留几位小数
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 精确到小数点后N,四舍五入
	 * 
	 * @param number
	 * @param n
	 * @return
	 */
	public static double getRound(double number, int n) {
		return round(number, n);
	}

	// ----------------------------------------sql--------------------------------------//
	/**
	 * 对字符串类型的参数进行格式化, 将会自动给值添加引号, "abc"-->"'abc'" <br>
	 * 本方法会将字符串内的 [单引号] 格式化为 [双引号] 以防止数据库报错, "a'b'c"-->"'a''b''c'"
	 * 
	 * @param str
	 *            "abc"
	 * @return "'abc'"
	 */
	public static String sqlParamFomat(String str) {
		if (null != str) {
			String tmp = str.replace("'", "''");
			return "'" + tmp + "'";
		} else {
			return "null";
		}
	}

	public static String sqlPF(String str) {
		return sqlParamFomat(str);
	}

	/**
	 * 传入指定格式的sql字符串和参数列表,返回拼接好的sql语句 例如: <br>
	 * sql: select * from stock where companyid={0}<br>
	 * 其中{0}将被参数列表中的第0个参数替换<br>
	 * 本函数会对参数进行参数化处理,请优先使用本函数拼接sql <br>
	 * 目前支持的参数类型: String,Integer,Long,Float,Double
	 * 
	 * @param sqlStr
	 * @param arr
	 * @return
	 */
	public static String sqlCreate(String sqlStr, Object[] arr) {
		String newSql = sqlStr;
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			Object obj = arr[i];
			if (obj instanceof String) {
				// 字符串要进行格式化
				String param = (String) obj;
				newSql = newSql.replace("{" + i + "}", sqlParamFomat(param));
			}
			if ((obj instanceof Integer) || (obj instanceof Long)
					|| (obj instanceof Float) || (obj instanceof Double)) {
				newSql = newSql.replace("{" + i + "}", obj + "");
			}
			if ((obj instanceof Calendar)) {
				Calendar param = (Calendar) obj;
				newSql = newSql.replace("{" + i + "}",
						timeFormat(param.getTime()));
			}
			if ((obj instanceof Date)) {
				Date param = (Date) obj;
				newSql = newSql.replace("{" + i + "}", timeFormat(param));
			}
		}
		return newSql;
	}

	public static String sqlCreate(String sqlStr, String[] arr) {
		String newSql = sqlStr;
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			newSql = newSql.replace("{" + i + "}", sqlParamFomat(arr[i]));
		}
		return newSql;
	}

	// ----------------------------------------other--------------------------------------//
	/**
	 * 检测网络连接是否可用
	 * 
	 * @param ctx
	 * @return true 可用; false 不可用
	 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			if (activeNetInfo.isAvailable()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 获取XML下面某个节点的值,忽略错误
	 * 
	 * @param xml
	 * @param nodeName
	 * @return
	 */
	public static String getXmlNode(String xml, String nodeName) {
		ByteArrayInputStream tInputStringStream = null;
		if (xml != null && !xml.trim().equals("")) {
			tInputStringStream = new ByteArrayInputStream(xml.getBytes());
		}
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(tInputStringStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					// persons = new ArrayList<Person>();
					break;
				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase(nodeName)) {
						return parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:// 结束元素事件
					break;
				}
				eventType = parser.next();
			}
			tInputStringStream.close();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// --------------------------------------------加密----------------------------
	/**
	 * encrypt 按某种格式加密
	 * 
	 * @param str
	 * @param algorithm
	 *            : 支持 MD5 或 SHA 两种
	 */
	private static String encryption(String str, String algorithm) {
		try {
			MessageDigest message = MessageDigest.getInstance(algorithm);
			message.reset();
			message.update(str.getBytes("utf-8"));
			byte[] md = message.digest();

			StringBuffer sbd = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				String temp = Integer.toHexString(0xFF & md[i]);
				if (temp.length() == 1)
					sbd.append("0");
				sbd.append(temp);
			}
			return sbd.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 用md5加密 ,默认1次,默认128位,(32位字符) , <br>
	 * 消息摘要算法 为保证只在发送数据前加密,这个方法设置为私有<br>
	 * 需要使用的地方:用户登录,用户注册,密码修改
	 */
	public static String toMD5(String str) {
		return encryption(str, "MD5");
	}

}
