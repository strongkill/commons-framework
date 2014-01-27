package cn.qtone.spring.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 基础响应对象
 * 
 * @author 卢俊生
 */
public class BasicResp {

	// 请求结果
	private boolean success = true;

	// 错误信息
	private Object message;

	public static final BasicResp SUCCESS = new BasicResp(true);
	public static final BasicResp FAILURE = new BasicResp(false);

	public BasicResp() {
	}

	public BasicResp(boolean success) {
		this.success = success;
	}

	public BasicResp(boolean success, Object message) {
		this.success = success;
		this.message = message;
	}

	public static BasicResp success(Object message) {
		return new BasicResp(true, message);
	}

	public static BasicResp failure(Object message) {
		return new BasicResp(false, message);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
