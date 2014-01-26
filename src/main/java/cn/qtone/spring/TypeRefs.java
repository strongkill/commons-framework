package cn.qtone.spring;

import java.util.List;

import cn.qtone.spring.response.BasicResp;
import cn.qtone.spring.response.Resp;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 预定义引用类型
 * 
 * @author 卢俊生
 * @version 1.0, 2013-6-24
 */
public class TypeRefs {

	/**
	 * String引用类型
	 */
	public static final TypeReference<String> string = new TypeReference<String>() {};

	/**
	 * BasicResp引用类型
	 */
	public static final TypeReference<BasicResp> basicResp = new TypeReference<BasicResp>() {};

	/**
	 * Resp引用类型,data类型为String
	 */
	public static final TypeReference<Resp<String>> stringResp = new TypeReference<Resp<String>>() {};

	/**
	 * Resp引用类型,data类型为String集合
	 */
	public static final TypeReference<Resp<List<String>>> stringListResp = new TypeReference<Resp<List<String>>>() {};

}
