package com.sd.pos.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络请求基础模块,支持post和get,数据格式:json
 * 
 * @author yi.zhe
 * 
 */
public class UtilNetBase {

	private static Integer timeoutConnection = 10000; // 链接超时时间,10秒
	private static Integer timeoutSocket = 30000; // 传输超时时间,30秒
	// 传输超时时间: 需要很长时间的操作的适合,修改这个参数再调用,每次修改只生效一次,使用后将修改为默认值
	// timeoutSocketLong > timeoutSocket 时使用 timeoutSocketLong,使用后重置
	public static Integer timeoutSocketLong = 0; // 想要设置更长的读取时间,设置这个字段,再调用读取就可以,一次生效

	public static String post(String url, String params) {
		return httpPost(url, params);
	}

	/**
	 * 发送HttpPost请求
	 * 
	 * @param url
	 * @param params
	 *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
	 *            不带参数的也可以调用,params随便传
	 * @return 成功:返回json字符串<br/>
	 */
	public static String httpPost(String url, String params) {
		System.out.println("--post----" + url);
		System.out.println("--params--" + params);
		String result;
		try {
			HttpPost request = new HttpPost(url);
			request.setHeader("charset", HTTP.UTF_8);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");

			if (params != null) {
				StringEntity se = new StringEntity(params, "UTF-8");
				request.setEntity(se);
			}
			DefaultHttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			int state = response.getStatusLine().getStatusCode();

			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println("--result--" + result);
			if (state == 200) {
			} else {
				if (state == 500) {
					// String reason = Util.getXmlNode(result, "Text"); //
					// 服务器内部错误原因
					String reason = result.replaceAll("\"", "");
					result = createFalseResult("操作失败(500):\r\n" + reason, "500");
				} else {
					result = createFalseResult("发生未知错误(700):" + state + "",
							"700");
				}
			}
		} catch (ConnectTimeoutException e) {
			result = createFalseResult("连接服务超时! (001)", "001");
		} catch (SocketTimeoutException e) {
			result = createFalseResult("接收数据超时! (002)", "002");
		} catch (HttpHostConnectException e) {
			result = createFalseResult("网络不通或服务没有开启! (003)", "003");
		} catch (Exception e) {
			result = createFalseResult("出现未知异常(006):httpPost:" + e.toString(),
					"006");// 出现未知异常
		}

		result = result.replaceAll(":null", ":\"\"");
		return result;
	}

	public static String falseResultStr = "{\"IsOK\":\"false\",\"Code\":\"%s\",\"Msg\":\"%s\"}";

	// 创建一个json字符串,用于返回错误信息
	private static String createFalseResult(String msg, String code) {
		System.out.println("*****(" + code + ")" + msg);
		return String.format(falseResultStr, code, msg);
	}

	public static String get(String url) {
		return httpGet(url);
	}

	/**
	 * 发送HttpGet请求
	 * 
	 * @return 返回json字符串<br/>
	 */
	public static String httpGet(String url) {
		String result;
		HttpGet request = new HttpGet(url);
		request.setHeader("charset", HTTP.UTF_8);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-Type", "application/json");
		try {
			DefaultHttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			int state = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity());
			if (state == 200) {
			} else {
				if (state == 500) {
					// String reason = Util.getXmlNode(result, "Text"); //
					// 服务器内部错误原因
					String reason = result.replaceAll("\"", "");
					result = createFalseResult("操作失败(500):\r\n" + reason, "500");
				} else {
					result = createFalseResult("发生未知错误(700):" + state + "",
							"700");
				}
			}
		} catch (ConnectTimeoutException e) {
			result = createFalseResult("连接服务超时!(-101)", "-101");
		} catch (SocketTimeoutException e) {
			result = createFalseResult("接收数据超时!(-102)", "-102");
		} catch (HttpHostConnectException e) {
			result = createFalseResult("网络不通或者服务不存在!(-103)", "-103");
		} catch (Exception e) {
			result = createFalseResult("出现未知异常(-191):" + e.toString(), "-191");// 出现未知异常
		}
		result = result.replaceAll(":null", ":\"\"");
		return result;

	}

	// private static HttpParams httpParameters;
	/**
	 * 创建新的httpclient
	 */
	protected static DefaultHttpClient getHttpClient() {
		// 设置超时时间
		// if (null == httpParameters) {
		// httpParameters = new BasicHttpParams();
		//
		// HttpConnectionParams.setConnectionTimeout(httpParameters,
		// timeoutConnection);
		// if (timeoutSocketLong > timeoutSocket) {
		// // 临时增加读取数据的时间限制,一次有效
		// HttpConnectionParams.setSoTimeout(httpParameters,
		// timeoutSocketLong);
		// timeoutSocketLong = 0; // 使用后立即重置
		// } else {
		// HttpConnectionParams
		// .setSoTimeout(httpParameters, timeoutSocket);
		// }
		// }
		DefaultHttpClient client = new DefaultHttpClient();

		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutConnection);
		// 设置超时时间
		if (timeoutSocketLong > timeoutSocket) {
			// 临时增加读取数据的时间限制,一次有效
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					timeoutSocketLong);
		} else {
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					timeoutSocket);
		}
		return client;
	}

	// 转码
	public static String urlEncoder(String str, String charset) {
		try {
			URL url = new URL(str);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
					url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			url = uri.toURL();
			String urlStr2 = encodeZH(url.toString(), "utf-8");
			return urlStr2;
		} catch (Exception e) {
			return "";
		}
	}

	// 只转码中文
	public static String encodeZH(String str, String charset) {
		String zhPattern = "[\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			try {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			} catch (UnsupportedEncodingException e) {
			}
		}
		m.appendTail(b);
		return b.toString();
	}

	/**
	 * 判断地址是否存在
	 * 
	 * @param url
	 * @return
	 */
	public static String isConnect(String url) {
		String result;
		HttpGet request = new HttpGet(url);
		try {
			DefaultHttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			int state = response.getStatusLine().getStatusCode();
			if (state == 200) {
				result = "{\"IsOK\":\"true\",\"Code\":\"200\",\"Msg\":\"地址正常(200)\"}";
			} else {
				result = createFalseResult("地址错误:" + state, state + "");
			}
		} catch (ConnectTimeoutException e) {
			result = createFalseResult("连接服务超时!(-101)", "-101");
		} catch (SocketTimeoutException e) {
			result = createFalseResult("接收数据超时!(-102)", "-102");
		} catch (HttpHostConnectException e) {
			result = createFalseResult("网络不通或者服务不存在!(-103)", "-103");
		} catch (Exception e) {
			result = createFalseResult("出现未知异常(-191):" + e.toString(), "-191");// 出现未知异常
		}
		result = result.replaceAll(":null", ":\"\"");
		return result;
	}
}
