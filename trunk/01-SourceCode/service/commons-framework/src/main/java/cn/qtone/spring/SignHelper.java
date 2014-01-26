package cn.qtone.spring;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 签名工具类
 * 
 * @author 卢俊生
 */
public class SignHelper {

	/**
	 * 生成签名字符串
	 * 
	 * @param uri uri
	 * @param params 请求参数
	 * @param appkey 客户端应用密钥
	 * @return 签名字符串
	 */
	public static String sign(String uri, List<NameValuePair> params, String appkey) {
		StringBuilder sb = new StringBuilder();
		sb.append(appkey);
		sb.append(uri);

		if (params != null && !params.isEmpty()) {
			// 去空格,去空值
			List<NameValuePair> list = SignHelper.trim(params);

			// 排序(name自然排序,如同名则按值自然排序)
			Collections.sort(list, new Comparator<NameValuePair>() {
				@Override
				public int compare(NameValuePair p1, NameValuePair p2) {
					int x = p1.getName().compareTo(p2.getName());
					if (x == 0) {
						x = p1.getValue().compareTo(p2.getValue());
					}
					return x < 0 ? -1 : 1;
				}
			});

			for (Iterator<NameValuePair> iterator = list.iterator(); iterator.hasNext();) {
				NameValuePair pair = iterator.next();
				sb.append(pair.getName()).append(pair.getValue());
			}
		}
		sb.append(appkey);

		return SignHelper.signature(sb.toString());
	}

	/**
	 * 生成签名字符串
	 * 
	 * @param uri uri
	 * @param params 请求参数
	 * @param appkey 客户端应用密钥
	 * @return 签名字符串
	 */
	public static String sign(String uri, Map<String, String[]> params, String appkey) {
		StringBuilder sb = new StringBuilder();
		sb.append(appkey);
		sb.append(uri);

		if (params != null && !params.isEmpty()) {
			// 去空值
			Map<String, String[]> map = SignHelper.trim(params);

			// Key排序
			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);

			String key = null;
			String[] value = null;
			for (int i = 0; i < keys.size(); i++) {
				key = keys.get(i);
				value = map.get(key);
				// Value排序
				Arrays.sort(value);
				for (String v : value) {
					sb.append(key).append(v);
				}
			}
		}

		sb.append(appkey);

		return SignHelper.signature(sb.toString());
	}

	/**
	 * 生成签名字符串
	 * 
	 * @param data 输入数据
	 * @return 签名字符串
	 * @throws WebException
	 */
	public static String signature(String data) throws WebException {
		String signature = byte2hex(getSHA1Digest(data));
		return signature;
	}

	/**
	 * SHA-1
	 * 
	 * @param data 输入数据
	 * @return 摘要字节数组
	 * @throws WebException
	 */
	public static byte[] getSHA1Digest(String data) throws WebException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] bytes = md.digest(data.getBytes(Constant.CHARSET));
			return bytes;
		} catch (Exception e) {
			throw new WebException("签名失败!", e);
		}
	}

	/**
	 * 将字节数组转为16进制字符串
	 * 
	 * @param bytes 字节数组
	 * @return 16进制字符串
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	public static List<NameValuePair> trim(List<NameValuePair> params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(params);

		// 去空格,去空值
		int size = list.size();
		for (int i = size - 1; i >= 0; i--) {
			NameValuePair pair = list.get(i);
			if (pair.getName() == null || "".equals(pair.getName().trim()) || pair.getValue() == null || "".equals(pair.getValue().trim())) {
				list.remove(i);
			} else {
				pair = new BasicNameValuePair(pair.getName().trim(), pair.getValue().trim());
				list.set(i, pair);
			}
		}

		return list;
	}

	public static Map<String, String[]> trim(Map<String, String[]> params) {
		String key = null;
		String[] value = null;
		Map<String, String[]> map = new HashMap<String, String[]>();
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			if (key != null && !"".equals(key.trim()) && value != null && value.length > 0) {
				for (String v : value) {
					if (v != null && !"".equals(v.trim())) {
						list.add(v.trim());
					}
				}
				if (!list.isEmpty()) {
					map.put(key.trim(), list.toArray(new String[0]));
				}

				list.clear();
			}
		}

		return map;
	}

	public static void main(String[] args) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("f", "3"));
		list.add(new BasicNameValuePair("f", "2"));
		list.add(new BasicNameValuePair("f", "1"));
		list.add(new BasicNameValuePair("a", "5"));
		list.add(new BasicNameValuePair("_", "6"));
		list.add(new BasicNameValuePair("c", ""));
		list.add(new BasicNameValuePair("b", "8"));

		long s1 = System.currentTimeMillis();
		String sign1 = sign("/path", list, "appkey");
		long e1 = System.currentTimeMillis();
		System.err.println(sign1);
		System.err.println(e1 - s1);

		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("f", new String[] { "3", "2", "1" });
		map.put("a", new String[] { "5" });
		map.put("_", new String[] { "6" });
		map.put("c", new String[] { "" });
		map.put("b", new String[] { "8" });
		long s2 = System.currentTimeMillis();
		String sign2 = sign("/path", map, "appkey");
		long e2 = System.currentTimeMillis();
		System.err.println(sign2);
		System.err.println(e2 - s2);
	}

}
