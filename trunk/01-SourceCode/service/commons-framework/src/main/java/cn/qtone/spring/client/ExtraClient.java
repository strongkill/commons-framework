package cn.qtone.spring.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;

import cn.qtone.spring.WebException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 扩展客户端
 * 
 * <pre>
 * 	依赖基础客户端及jackson2.+
 * 	jackson-databind,jackson-core,jackson-annotations
 * </pre>
 * 
 * @author 卢俊生
 */
@SuppressWarnings("unchecked")
public class ExtraClient {

	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();

		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
	}

	/**
	 * Get
	 * 
	 * <pre>
	 * eg : 
	 * DefaultResp&lt;List&lt;String&gt;&gt; resp = ExtraClient.get(new TypeReference&lt;DefaultResp&lt;List&lt;String&gt;&gt;&gt;() {}, &quot;/quartz/all&quot;, params);
	 * </pre>
	 * 
	 * @param ref 返回值引用类型,支持泛型
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final <T> T get(TypeReference<T> ref, String path, List<NameValuePair> params) throws IOException {
		String json = BasicClient.get(path, params);
		return toObj(ref, json);
	}

	/**
	 * Get
	 * 
	 * <pre>
	 * eg : 
	 * DefaultResp&lt;List&lt;String&gt;&gt; resp = ExtraClient.get(new TypeReference&lt;DefaultResp&lt;List&lt;String&gt;&gt;&gt;() {}, &quot;/quartz/all&quot;, params);
	 * </pre>
	 * 
	 * @param ref 返回值引用类型,支持泛型
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final <T> T get(TypeReference<T> ref, String path, NameValuePair... params) throws IOException {
		String json = BasicClient.get(path, params);
		return toObj(ref, json);
	}

	/**
	 * Post
	 * 
	 * <pre>
	 * eg : 
	 * DefaultResp&lt;List&lt;String&gt;&gt; resp = ExtraClient.get(new TypeReference&lt;DefaultResp&lt;List&lt;String&gt;&gt;&gt;() {}, &quot;/quartz/all&quot;, params);
	 * </pre>
	 * 
	 * @param ref 返回值引用类型,支持泛型
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final <T> T post(TypeReference<T> ref, String path, List<NameValuePair> params) throws IOException {
		String json = BasicClient.post(path, params);
		return toObj(ref, json);
	}

	/**
	 * Post
	 * 
	 * <pre>
	 * eg : 
	 * DefaultResp&lt;List&lt;String&gt;&gt; resp = ExtraClient.get(new TypeReference&lt;DefaultResp&lt;List&lt;String&gt;&gt;&gt;() {}, &quot;/quartz/all&quot;, params);
	 * </pre>
	 * 
	 * @param ref 返回值引用类型,支持泛型
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final <T> T post(TypeReference<T> ref, String path, NameValuePair... params) throws IOException {
		String json = BasicClient.post(path, params);
		return toObj(ref, json);
	}

	private static <T> T toObj(TypeReference<T> ref, String json) {
		try {
			if (String.class.equals(ref.getType())) {
				return (T) json;
			} else {
				return (T)objectMapper.readValue(json, ref);
			}
		} catch (IOException e) {
			throw new WebException("deserialize JSON string to Object error!", e);
		}
	}

}
