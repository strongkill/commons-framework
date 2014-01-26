package cn.qtone.hibernate.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Dao接口 - Dao基接口
 * 
 * @author 卢俊生
 */
public interface Dao<T, K extends Serializable> {

	/**
	 * 刷新session.
	 */
	public void flush();

	/**
	 * 清除Session.
	 */
	public void clear();

	/**
	 * 清除某一对象.
	 * 
	 * @param object
	 */
	public void evict(Object object);

	/**
	 * 根据ID获取实体对象
	 * 
	 * @param id 主键
	 * @return T
	 */
	public T get(K id);

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id 主键
	 * @return T
	 */
	public T load(K id);

	/**
	 * 根据ID数组获取实体对象集合.
	 * 
	 * @param ids 主键数组
	 * @return T集合
	 */
	public List<T> get(K[] ids);

	/**
	 * 根据属性名和属性值获取实体对象.
	 * 
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return T
	 */
	public T get(String propertyName, Object value);

	/**
	 * 根据属性名和属性值获取实体对象集合.
	 * 
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return T集合
	 */
	public List<T> getList(String propertyName, Object value);

	/**
	 * 获取所有实体对象集合.
	 * 
	 * @return T集合
	 */
	public List<T> getAll();

	/**
	 * 获取所有实体对象总数.
	 * 
	 * @return Long
	 */
	public Long getTotalCount();

	/**
	 * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
	 * 
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @param oldValue 旧值
	 * @return boolean
	 */
	public boolean unique(String propertyName, Object value, Object oldValue);

	/**
	 * 判断字段值是否唯一
	 * 
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @param pk 主键
	 * @return boolean
	 */
	public boolean uniqueWithPK(String propertyName, Object value, K pk);

	/**
	 * 根据属性名判断数据是否已存在.
	 * 
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return boolean
	 */
	public boolean exists(String propertyName, Object value);

	/**
	 * 保存实体对象.
	 * 
	 * @param entity 实体对象
	 * @return 主键
	 */
	public K save(T entity);

	/**
	 * 更新实体对象.
	 * 
	 * @param entity 实体对象
	 */
	public void update(T entity);

	public void saveOrUpdate(T entity);

	/**
	 * 删除实体对象.
	 * 
	 * @param entity 实体对象
	 */
	public void delete(T entity);

	/**
	 * 根据ID数组删除实体对象.
	 * 
	 * @param ids 主键数组
	 */
	public void delete(K... ids);

	/**
	 * 根据ID字符串删除
	 * @param ids
	 */
	public void delete(List<K> ids);

	/**
	 * 根据主键值批量更新某个字段值<BR>
	 * 
	 * @param updateProperty
	 *            待更新字段
	 * @param updateValue
	 *            更新的值
	 * @param ids
	 *            主键值(Long)数组
	 * @return 受影响记录数
	 * @author 卢俊生
	 */
	public int update(String updateProperty, Object updateValue, K... ids);

}