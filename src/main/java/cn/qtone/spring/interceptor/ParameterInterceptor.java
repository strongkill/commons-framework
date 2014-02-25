package cn.qtone.spring.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.qtone.spring.URICoder;
import cn.qtone.util.ArrayUtil;

/**
 * 请求参数过滤器(转码&去空格)
 * 
 * @author 卢俊生
 */
public class ParameterInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ParameterInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (!request.getServletPath().contains(".")) {
			Map<String, String[]> params = request.getParameterMap();

			for (Map.Entry<String, String[]> entry : params.entrySet()) {
				for (int i = 0; i < entry.getValue().length; i++) {
					entry.getValue()[i] = URICoder.decode(entry.getValue()[i]).trim();
				}
			}

			if (logger.isDebugEnabled()) {
				StringBuilder formatter = new StringBuilder();
				List<String> values = new ArrayList<String>();

				formatter.append("\n	=> {}");
				values.add(request.getServletPath());

				String value = null;
				for (Map.Entry<String, String[]> entry : params.entrySet()) {
					if (ArrayUtil.length(entry.getValue()) == 1) {
						value = entry.getValue()[0];
					} else {
						value = "[" + ArrayUtil.toString(entry.getValue()) + "]";
					}

					formatter.append("\n	=> {} : {}");
					values.add(entry.getKey());
					values.add(value);
				}
				logger.debug(formatter.toString(), values.toArray());
			}
		}

		return true;
	}

}
