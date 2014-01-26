package cn.qtone.spring;

import java.util.List;



/**
 * 自定义异常
 * 
 * @author 卢俊生
 */
public class WebException extends RuntimeException {

	private static final long serialVersionUID = 7304217617718174733L;

	private Error error;

	public WebException(ResponseType errorType) {
		error = new Error(errorType);
	}

	public WebException(ResponseType errorType, String... messages) {
		error = new Error(errorType, messages);
	}

	public WebException(ResponseType errorType, List<String> messages) {
		error = new Error(errorType, messages);
	}

	public Error getError() {
		return error;
	}

	public ResponseType getErrorType() {
		return error.getErrorType();
	}

	public void setErrorType(ResponseType errorType) {
		error.setErrorType(errorType);
	}

	/**
	 * 获取错误消息
	 */
	public List<String> getMessages() {
		return error.getMessages();
	}

	/**
	 * 设置错误消息
	 */
	public void setMessages(List<String> messages) {
		error.setMessages(messages);
	}

	/**
	 * 追加错误消息
	 */
	public void addMessages(String... messages) {
		error.addMessages(messages);
	}

	/**
	 * 抛出ApiException异常
	 * 
	 * @param errorType 错误类型
	 * @param messages 错误消息
	 */
	public static void throwEx(ResponseType errorType, String... messages) {
		throw new WebException(errorType, messages);
	}

	/**
	 * 抛出ApiException异常
	 * 
	 * @param errorType 错误类型
	 * @param messages 错误消息
	 */
	public static void throwEx(ResponseType errorType, List<String> messages) {
		throw new WebException(errorType, messages);
	}

	/**
	 * 当错误消息不为空时抛出参数校验失败异常(参数校验专用)
	 * 
	 * @param messages 错误消息
	 */
	public static void throwWhenHasMsg(String... messages) {
		if (messages.length > 0) {
			throw new WebException(ResponseType.invalid_parameter, messages);
		}
	}

	/**
	 * 当错误消息不为空时抛出参数校验失败异常(参数校验专用)
	 * 
	 * @param messages 错误消息
	 */
	public static void throwWhenHasMsg(List<String> messages) {
		if (messages != null && !messages.isEmpty()) {
			throw new WebException(ResponseType.invalid_parameter, messages);
		}
	}

	public WebException(Throwable throwable) {
		super(throwable);
	}

	public WebException(String message, Throwable throwable) {
		super(message, throwable);
	}

	@Override
	public String getMessage() {
		if (error == null) {
			return super.getMessage();
		} else {
			return error.toString();
		}
	}

	@Override
	public String toString() {
		if (error == null) {
			return super.toString();
		} else {
			return getClass().getName() + ": " + error.toString();
		}
	}

}
