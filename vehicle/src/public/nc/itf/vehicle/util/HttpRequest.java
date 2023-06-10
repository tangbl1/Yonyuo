package nc.itf.vehicle.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import nc.bs.logging.Logger;
import org.apache.http.util.TextUtils;
import com.alibaba.fastjson.JSONObject;

public class HttpRequest {

	/**
	 * 发送POST请求[JSON Param]
	 * 
	 * @param urlPath
	 *            地址
	 * @param param
	 *            参数
	 * @return JSONObject
	 */
	public static JSONObject sendPost(String urlPath,
			List<Map<String, Object>> param) {
		String json = JSONObject.toJSONString((param != null) ? param : "");
		String result = "";
		BufferedReader reader = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			conn.setRequestProperty("accept", "application/json");
			if (json != null && !TextUtils.isEmpty(json)) {
				// 去掉前后[]
				String subJson = json.substring(1, json.length() - 1);
				OutputStreamWriter outwritestream = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
				outwritestream.write(subJson);
				outwritestream.flush();
				outwritestream.close();
				conn.getResponseCode();
			}
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = "";
				while ((line = reader.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return JSONObject.parseObject(result);
	}

	/**
	 * 发送POST请求[JSON Param]
	 * 
	 * @param urlPath
	 *            地址
	 * @param list
	 *            参数
	 * @return JSONObject
	 */
	public static JSONObject sendGet(String urlPath, List<String> list) {
		String result = "";
		// 将传入的参数拼接成想要的格式
		String param = (list != null) ? param(list) : "";
		BufferedReader in = null;
		try {
			String urlNameString = urlPath + param;
			URL realUrl = new URL(urlNameString);
//			System.out.println(realUrl);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return JSONObject.parseObject(result);
	}

	/**
	 * 拼接成url后面需要拼接的格式 参数以逗号分隔
	 * 
	 * @param list
	 * @return
	 */
	public static String param(List<String> list) {
		if (list == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			for (int i = 0, len = list.size(); i < len; i++) {
				sb.append(list.get(i));
				if (i < len - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 向指定URL发送GET方法的请求[String Param]
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			Logger.info("发送GET请求出现异常！"+e);
			// TODO
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求[String Param]
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
//			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

		URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");

			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			Logger.info("发送 POST 请求出现异常！"+e);
			// TODO 
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}