package cn.qtone.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 错误模型
 * 
 * @author 卢俊生
 */
public class Error {

	// 错误类型
	private ResponseType errorType = ResponseType.server_error;

	// 错误信息集合
	private List<String> messages;

	public Error() {
	}

	public Error(ResponseType errorType, List<String> messages) {
		this.setErrorType(errorType);
		this.messages = messages;
	}

	public Error(ResponseType errorType, String... messages) {
		this.setErrorType(errorType);
		if (messages.length > 0) {
			this.messages = new ArrayList<String>();
			for (String message : messages) {
				this.messages.add(message);
			}
		}
	}

	public void addMessages(String... messages) {
		if (messages.length > 0) {
			if (this.messages == null) {
				this.messages = new ArrayList<String>();
			}
			for (String message : messages) {
				this.messages.add(message);
			}
		}
	}

	public String errMsg() {
		String msg = "";
		if (messages != null && !messages.isEmpty()) {
			msg = subStartTag(messages.get(0));
			for (int i = 1; i < messages.size(); i++) {
				msg += "; " + subStartTag(messages.get(i));
			}
			msg += ".";
		}
		return msg;
	}

	public String multilineErrMsg() {
		String msg = "";
		if (messages != null && !messages.isEmpty()) {
			msg = subStartTag(messages.get(0));
			for (int i = 1; i < messages.size(); i++) {
				msg += ";</br>" + subStartTag(messages.get(i));
			}
			msg += ".";
		}
		return msg;
	}

	private static final String subStartTag(String src) {
		if (src != null && !"".equals(src.trim())) {
			src = src.trim().replaceFirst("^<[^/<>]*>", "");
		}
		return src;
	}

	public ResponseType getErrorType() {
		return errorType;
	}

	public void setErrorType(ResponseType errorType) {
		if (errorType != null) {
			this.errorType = errorType;
		}
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
