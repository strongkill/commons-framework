package cn.qtone.spring.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import cn.qtone.spring.WebException;
import cn.qtone.spring.Constant;
import cn.qtone.spring.Headers;
import cn.qtone.spring.SignHelper;
import cn.qtone.spring.URICoder;

/**
 * 客户端核心,实现客户端的初始化及参数配置等
 * 
 * @author 卢俊生
 */
public abstract class _Client {

	private static boolean isInit;
	private static final String PROP_NAME_HOST = "host";
	private static final String PROP_NAME_PORT = "port";
	private static final String PROP_NAME_CONTEXT = "context";
	private static final String PROP_NAME_APPID = "appid";
	private static final String PROP_NAME_APPKEY = "appkey";
	private static final String PROP_NAME_CONN_TIMEOUT = "connectionTimeout";
	private static final String PROP_NAME_SO_TIMEOUT = "soTimeout";
	private static final String PROP_NAME_MAX_TOTAL = "maxTotal";

	protected static final String PATH_PARAM_SEPARATOR = "?";
	protected static final String PARAMETER_SEPARATOR = "&";
	protected static final String NAME_VALUE_SEPARATOR = "=";

	protected static HttpHost httpHost;
	protected static String host;
	protected static int port = 80;
	protected static String context = "";
	protected static String appid;
	protected static String appkey;

	protected static int connectionTimeout = 1000 * 10; // 连接超时时间
	protected static int soTimeout = 1000 * 60; // 等待数据超时时间
	protected static int maxTotal = 100; // 客户端最大并行连接数

	private _Client() {
	}

	/**
	 * 自动初始化,调用时判断是否已初始化,若为初始化则读取classpath根目录下的yzt-service.properties自动进行初始化
	 */
	private static void autoInit() {
		if (!isInit) {
			try {
				InputStream inputStream = _Client.class.getResourceAsStream("/service.properties");

				Properties prop = new Properties();
				prop.load(inputStream);

				setHost(prop.getProperty(PROP_NAME_HOST));
				setContext(prop.getProperty(PROP_NAME_CONTEXT));
				setAppid(prop.getProperty(PROP_NAME_APPID));
				setAppkey(prop.getProperty(PROP_NAME_APPKEY));

				String port = prop.getProperty(PROP_NAME_PORT);
				if (port != null && !"".equals(port.trim())) {
					setPort(Integer.valueOf(port).intValue());
				}

				String connTimeout = prop.getProperty(PROP_NAME_CONN_TIMEOUT);
				String soTimeout = prop.getProperty(PROP_NAME_SO_TIMEOUT);
				String maxTotal = prop.getProperty(PROP_NAME_MAX_TOTAL);

				if (connTimeout != null && !"".equals(connTimeout.trim())) {
					setConnectionTimeout(Integer.valueOf(connTimeout).intValue());
				}
				if (soTimeout != null && !"".equals(soTimeout.trim())) {
					setSoTimeout(Integer.valueOf(soTimeout).intValue());
				}
				if (maxTotal != null && !"".equals(maxTotal.trim())) {
					setMaxTotal(Integer.valueOf(maxTotal).intValue());
				}

				_Client.httpHost = new HttpHost(_Client.host, _Client.port);

				_Client.isInit = true;
			} catch (Exception e) {
				throw new WebException("autoInit Client error!", e);
			}
		}
	}

	/**
	 * 初始化客户端
	 * 
	 * @param host 服务器主机
	 * @param port 服务器端口
	 * @param context 公共服务接口目录
	 * @param appid 客户端应用唯一标识
	 * @param appkey 客户端应用密钥,用户生成签名,不直接传输
	 */
	protected static final void init(String host, int port, String context, String appid, String appkey) {
		try {
			setHost(host);
			setPort(port);
			setContext(context);
			setAppid(appid);
			setAppkey(appkey);

			_Client.httpHost = new HttpHost(_Client.host, _Client.port);

			_Client.isInit = true;
		} catch (Exception e) {
			throw new WebException("autoInit Client error!", e);
		}
	}

	/**
	 * 初始化客户端
	 * 
	 * @param host 服务器主机
	 * @param port 服务器端口
	 * @param context 公共服务接口目录
	 * @param appid 客户端应用唯一标识
	 * @param appkey 客户端应用密钥,用户生成签名,不直接传输
	 * @param connectionTimeout 连接超时时间(毫秒),默认 10秒
	 * @param soTimeout 等待响应时间(毫秒),默认 60秒
	 * @param maxTotal 最大并行连接数
	 */
	protected static final void init(String host, int port, String context, String appid, String appkey, int connectionTimeout, int soTimeout, int maxTotal) {
		setConnectionTimeout(connectionTimeout);
		setSoTimeout(soTimeout);
		setMaxTotal(maxTotal);

		init(host, port, context, appid, appkey);
	}

	/**
	 * 获取DefaultHttpClient实例
	 */
	protected static DefaultHttpClient getHttpClient() {
		_Client.autoInit(); // 自动初始化

		HttpParams params = new BasicHttpParams();

		// 设置超时时间
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);

		// 编码
		params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Constant.CHARSET);
		params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, Constant.CHARSET);

		// 设置组件参数, HTTP协议的版本,1.1/1.0/0.9
//		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//		HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
		HttpProtocolParams.setUseExpectContinue(params, true);

		// 设置访问协议
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		// 多连接的线程安全的管理器
		PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);
		pccm.setDefaultMaxPerRoute(maxTotal); // 每个主机的最大并行链接数
		pccm.setMaxTotal(maxTotal);          // 客户端总并行链接最大数

		DefaultHttpClient httpClient = new DefaultHttpClient(pccm, params);
		return httpClient;
	}

	/**
	 * 执行http请求
	 * 
	 * @param request http请求(HttpGet/HttpPost)
	 * @return JSON格式的响应数据
	 * @throws IOException 连接/请求错误
	 */
	protected static final String execute(HttpRequestBase request) throws IOException {
		_Client.autoInit(); // 自动初始化

		HttpResponse response = _Client.getHttpClient().execute(_Client.httpHost, request);
		String resp = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			resp = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} else {
			resp = "{\"success\":false,\"error\":{\"errorType\":\"aborted_request\",\"messages\":[\"请求失败! Http_Status_Code:" + response.getStatusLine().getStatusCode() + "\"]}}";
		}
		return resp;
	}

	/**
	 * 对http请求进行签名
	 * 
	 * @param request http请求(HttpGet/HttpPost)
	 * @param params 请求参数
	 * @return 添加签名后的HttpRequestBase实例
	 */
	protected static final <T extends HttpRequestBase> T sign(T request, List<NameValuePair> params) {
		_Client.autoInit(); // 自动初始化

		request.addHeader(Headers.sign, SignHelper.sign(request.getURI().getPath(), params, _Client.appkey));
		return request;
	}

	/**
	 * 设置请求头(编码/appid等)
	 * 
	 * @param request http请求(HttpGet/HttpPost)
	 */
	protected static final void setHeaders(HttpRequestBase request) {
		_Client.autoInit(); // 自动初始化

		request.addHeader("Content-Type", "application/x-www-form-urlencoded");
		request.addHeader("Accept-Language", "zh-cn");
		request.addHeader("Accept-Encoding", "gzip, deflate");

		request.addHeader(Headers.appid, _Client.appid);
	}

	/**
	 * 拼接请求uri
	 * 
	 * @param path servlet路径
	 * @return uri(目录+servlet路径)
	 */
	protected static final String uri(String path) {
		_Client.autoInit(); // 自动初始化

		String uri = _Client.context;
		if (path != null && !"".equals(path)) {
			if (path.startsWith("/")) {
				uri += path;
			} else {
				uri += "/" + path;
			}
		}
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}
		return uri;
	}

	/**
	 * 拼接请求参数(编码),Get请求用
	 * 
	 * @param uri 请求uri
	 * @param params 请求参数
	 * @return 拼接参数后的uri
	 */
	protected static String appendParams(String uri, List<NameValuePair> params) {
		if (params != null && !params.isEmpty()) {
			// 将参数拼接到uri中
			StringBuilder sb = new StringBuilder();
			NameValuePair nameValuePair = params.get(0);
			sb.append(nameValuePair.getName()).append(NAME_VALUE_SEPARATOR).append(URICoder.encode(nameValuePair.getValue()));
			for (int i = 1; i < params.size(); i++) {
				nameValuePair = params.get(i);
				sb.append(PARAMETER_SEPARATOR).append(nameValuePair.getName()).append(NAME_VALUE_SEPARATOR).append(URICoder.encode(nameValuePair.getValue()));
			}
			uri += PATH_PARAM_SEPARATOR + sb.toString();
		}
		return uri;
	}

	private static void setHost(String host) {
		if (host != null && !"".equals(host.trim())) {
			_Client.host = host.trim();
		} else {
			throw new RuntimeException("host can not be null!");
		}
	}

	private static void setPort(int port) {
		if (port > 0) {
			_Client.port = port;
		}
	}

	private static void setContext(String context) {
		if (context != null && !"".equals(context.trim())) {
			if (context.startsWith("/")) {
				_Client.context = context;
			} else {
				_Client.context = "/" + context;
			}
		}
	}

	private static void setAppid(String appid) {
		if (appid != null && !"".equals(appid.trim())) {
			_Client.appid = appid.trim();
		} else {
			throw new RuntimeException("appid can not be null!");
		}
	}

	private static void setAppkey(String appkey) {
		if (appkey != null && !"".equals(appkey.trim())) {
			_Client.appkey = appkey.trim();
		} else {
			throw new RuntimeException("appkey can not be null!");
		}
	}

	private static void setConnectionTimeout(int connectionTimeout) {
		if (connectionTimeout > 0) {
			_Client.connectionTimeout = connectionTimeout;
		}
	}

	private static void setSoTimeout(int soTimeout) {
		if (soTimeout > 0) {
			_Client.soTimeout = soTimeout;
		}
	}

	private static void setMaxTotal(int maxTotal) {
		if (maxTotal > 0) {
			_Client.maxTotal = maxTotal;
		}
	}

}
