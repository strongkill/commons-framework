package cn.qtone.spring;

/**
 * 错误类型
 * 
 * @author 卢俊生
 */
public enum ResponseType {
	/** 无效身份 **/
	invalid_identity,

	/** 权限不足 **/
	invalid_permission,

	/** 非法业务参数 **/
	invalid_parameter,

	/** 不存在 **/
	not_exist,

	/** 不唯一 **/
	not_unique,

	/** 不可修改 **/
	not_allow_modify,

	/** 系统错误(异常) **/
	server_error,

	/** 拒绝连接/连接失败 **/
	aborted_request,

	/** 未知异常 **/
	undefinition,

	/**正常**/
	normal
}
