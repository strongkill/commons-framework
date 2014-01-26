package cn.qtone.spring.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import cn.qtone.spring.Error;
import cn.qtone.spring.ResponseType;

/**
 * 基础响应对象
 * 
 * @author 卢俊生
 */
public class BasicResp {

	// 请求结果
	private boolean success = true;

	// 错误信息
	private Error error;

	public static final BasicResp SUCCESS = new BasicResp(true);
	public static final BasicResp FAILURE = new BasicResp(false);

	public BasicResp() {
	}

	public BasicResp(boolean success) {
		this.success = success;
	}

	public BasicResp(Error error) {
		this.success = false;
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public String errMsg() {
		String msg = "";
		if (error != null && error.getMessages() != null && !error.getMessages().isEmpty()) {
			msg = subStartTag(error.getMessages().get(0));
			for (int i = 1; i < error.getMessages().size(); i++) {
				msg += "; " + subStartTag(error.getMessages().get(i));
			}
			msg += ".";
		}
		return msg;
	}

	public String multilineErrMsg() {
		String msg = "";
		if (error != null && error.getMessages() != null && !error.getMessages().isEmpty()) {
			msg = subStartTag(error.getMessages().get(0));
			for (int i = 1; i < error.getMessages().size(); i++) {
				msg += ";</br>" + subStartTag(error.getMessages().get(i));
			}
			msg += ".";
		}
		return msg;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	private static final String subStartTag(String src) {
		if (src != null && !"".equals(src.trim())) {
			src = src.trim().replaceFirst("^<[^/<>]*>", "");
		}
		return src;
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("<code1>message 1");
		list.add("<code2>message 2");

		BasicResp resp = new BasicResp(new Error(ResponseType.invalid_parameter, list));
		System.out.println(resp);
		System.out.println(resp.errMsg());
		System.out.println(resp.multilineErrMsg());
	}

}
