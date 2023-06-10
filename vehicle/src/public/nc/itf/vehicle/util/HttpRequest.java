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
	 * ����POST����[JSON Param]
	 * 
	 * @param urlPath
	 *            ��ַ
	 * @param param
	 *            ����
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
				// ȥ��ǰ��[]
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
	 * ����POST����[JSON Param]
	 * 
	 * @param urlPath
	 *            ��ַ
	 * @param list
	 *            ����
	 * @return JSONObject
	 */
	public static JSONObject sendGet(String urlPath, List<String> list) {
		String result = "";
		// ������Ĳ���ƴ�ӳ���Ҫ�ĸ�ʽ
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
	 * ƴ�ӳ�url������Ҫƴ�ӵĸ�ʽ �����Զ��ŷָ�
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
	 * ��ָ��URL����GET����������[String Param]
	 * 
	 * @param url
	 *            ���������URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * @return URL ������Զ����Դ����Ӧ���
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = connection.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// ���� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			Logger.info("����GET��������쳣��"+e);
			// TODO
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
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
	 * ��ָ�� URL ����POST����������[String Param]
	 * 
	 * @param url
	 *            ��������� URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * @return ������Զ����Դ����Ӧ���
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
//			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

		URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");

			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			// �����������
			out.write(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			Logger.info("���� POST ��������쳣��"+e);
			// TODO 
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
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