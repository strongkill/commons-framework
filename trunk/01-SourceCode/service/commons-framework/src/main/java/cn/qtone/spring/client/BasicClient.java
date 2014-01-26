package cn.qtone.spring.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import cn.qtone.spring.Constant;
import cn.qtone.spring.SignHelper;

/**
 * 基础客户端,实现最基本的GET,POST请求,返回值类型均为JSON字符串
 * 
 * @author 卢俊生
 */
public class BasicClient {

	/**
	 * Get
	 * 
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final String get(String path, List<NameValuePair> params) throws IOException {
		params = SignHelper.trim(params); // 去空值

		String uri = _Client.uri(path);
		uri = _Client.appendParams(uri, params);// 设参
		HttpGet httpGet = new HttpGet(uri);
		_Client.setHeaders(httpGet);// 设置头信息
		_Client.sign(httpGet, params);// 设置签名

		String response = _Client.execute(httpGet);// 提交请求

		httpGet.abort();// 释放资源

		return response;
	}

	/**
	 * Get
	 * 
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final String get(String path, NameValuePair... params) throws IOException {
		return BasicClient.get(path, Arrays.asList(params));
	}

	/**
	 * Post
	 * 
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final String post(String path, List<NameValuePair> params) throws IOException {
		params = SignHelper.trim(params); // 去空值

		HttpPost httpPost = new HttpPost(_Client.uri(path));
		httpPost.setEntity(new UrlEncodedFormEntity(params, Constant.CHARSET));// 设参
		_Client.setHeaders(httpPost);// 设置头信息
		_Client.sign(httpPost, params);// 设置签名

		String response = _Client.execute(httpPost);

		httpPost.abort();// 释放资源

		return response;
	}

	/**
	 * Post
	 * 
	 * @param path servlet路径,如/quartz/all
	 * @param params 请求参数
	 * @return JSON字符串
	 * @throws IOException 连接/请求失败
	 */
	public static final String post(String path, NameValuePair... params) throws IOException {
		return BasicClient.post(path, Arrays.asList(params));
	}

	public static void main(String[] args) throws IOException {
		String msg = get("/quartz/all");
		System.err.println(msg);
	}
}
