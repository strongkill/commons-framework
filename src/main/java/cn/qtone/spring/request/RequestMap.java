package cn.qtone.spring.request;

import java.util.HashMap;

/**
 * <pre>
 * HashMap<String, Object>子类, 作为服务接口接受POST入参用
 * 在HashMap的基础上添加泛型方法:
 * public <T> T get(String key, Class<T> clazz);
 * 
 * eg:
 * 
 * @RequestMapping("post")
 * public @ResponseBody UserResponse getUser(@RequestBody RequestMap map) {
 * 	User user = new User();
 * 	user.setName(map.get("name", String.class));
 * 	//...
 * 	return new UserResponse(user);
 * }
 * </pre>
 * 
 * @author 卢俊生
 */
public class RequestMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 7084356203341980499L;

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		return (T) get(key);
	}
}
