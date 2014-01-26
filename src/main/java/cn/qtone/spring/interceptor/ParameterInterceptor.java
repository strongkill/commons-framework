package cn.qtone.spring.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.qtone.spring.URICoder;

/**
 * 请求参数过滤器(转码&去空格)
 * 
 * @author 卢俊生
 */
public class ParameterInterceptor extends HandlerInterceptorAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		Map<String, String[]> params = request.getParameterMap();

		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			for (int i = 0; i < entry.getValue().length; i++) {
				entry.getValue()[i] = URICoder.decode(entry.getValue()[i]).trim();
			}
		}

		return true;
	}

}
