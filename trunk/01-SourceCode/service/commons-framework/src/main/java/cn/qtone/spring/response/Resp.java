package cn.qtone.spring.response;

/**
 * 默认响应对象(基础自{@linkplain BasicResp})
 * 
 * @param <T> 响应数据类型
 * @author 卢俊生
 */
public class Resp<T> extends BasicResp {

	// 响应数据
	private T data;

	public Resp() {
	}

	public Resp(T data) {
		this.data = data;
	}

	public static final <T> Resp<T> from(T data) {
		return new Resp<T>(data);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
