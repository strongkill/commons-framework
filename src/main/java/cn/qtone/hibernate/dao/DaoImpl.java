package cn.qtone.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

import cn.qtone.util.MapUtil;

/**
 * Dao实现类 - Dao实现类基类
 * 
 * @author 卢俊生
 */
@SuppressWarnings("unchecked")
public class DaoImpl<T, K extends Serializable> extends Base<Session> implements Dao<T, K> {

	private Class<T> entityClass;
	private Class<K> pkClass;

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 构造方法-获取接口或超类泛型参数的实际类型
	 */
	public DaoImpl() {
		Class<?> c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.entityClass = (Class<T>) parameterizedType[0];
			this.pkClass = (Class<K>) parameterizedType[1];
		}
	}

	public DaoImpl(SessionFactory sessionFactory, Class<T> entityClass, Class<K> pkClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
		this.pkClass = pkClass;
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public void evict(Object object) {
		Assert.notNull(object, "object is required");
		getSession().evict(object);
	}

	@Override
	public T get(K id) {
		Assert.notNull(id, "id is required");
		return (T) getSession().get(entityClass, id);
	}

	@Override
	public T load(K id) {
		Assert.notNull(id, "id is required");
		return (T) getSession().load(entityClass, id);
	}

	@Override
	public List<T> get(K[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		String hql = "from " + entityClass.getName() + " as model where model.id in(:ids)";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	@Override
	public T get(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		return (T) getSession().createQuery(hql).setParameter(0, value).uniqueResult();
	}

	@Override
	public List<T> getList(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		return getSession().createQuery(hql).setParameter(0, value).list();
	}

	@Override
	public List<T> getAll() {
		String hql = "from " + entityClass.getName() + " as model";
		return getSession().createQuery(hql).list();
	}

	@Override
	public Long getTotalCount() {
		String hql = "select count(*) from " + entityClass.getName();
		return (Long) getSession().createQuery(hql).uniqueResult();
	}

	@Override
	public boolean unique(String propertyName, Object oldValue, Object newValue) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		T object = get(propertyName, newValue);
		return (object == null);
	}

	@Override
	public boolean uniqueWithPK(String propertyName, Object value, K pk) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "select count(*) from " + entityClass.getName() + " where " + propertyName + "=:value";

		if (pk != null) {
			hql += " and " + getPKField(entityClass) + "<>:pk";
		}
		long size = (Long) createQuery(hql, MapUtil.toMap("value,pk", value, pk)).uniqueResult();

		return size == 0;
	}

	@Override
	public boolean exists(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		T object = get(propertyName, value);
		return (object != null);
	}

	@Override
	public K save(T entity) {
		Assert.notNull(entity, "entity is required");
		return (K) getSession().save(entity);
	}

	@Override
	public void update(T entity) {
		Assert.notNull(entity, "entity is required");
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		Assert.notNull(entity, "entity is required");
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(T entity) {
		Assert.notNull(entity, "entity is required");
		getSession().delete(entity);
	}

	@Override
	public void delete(K... ids) {
		delete(Arrays.asList(ids));
	}

	@Override
	public void delete(List<K> ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		String hql = "delete from " + entityClass.getName() + " m where m." + getPKField(entityClass) + " in(:ids)";
		createQuery(hql, MapUtil.toMap("ids", ids)).executeUpdate();
	}

	@Override
	public int update(String updateProperty, Object updateValue, K... ids) {
		Assert.hasText(updateProperty, "updateProperty is required");
		Assert.notNull(updateValue, "updateValue is required");
		Assert.notEmpty(ids, "ids is required");

		String sql = "update " + entityClass.getName() + " set " + updateProperty + "=:updateValue where " + getPKField(entityClass) + " in(:ids)";
		return createQuery(sql, MapUtil.toMap("updateValue,ids", updateValue, ids)).executeUpdate();
	}

	@Override
	public String toString() {
		return getClass().getName() + "<" + entityClass.getName() + "," + pkClass.getSimpleName() + ">#" + hashCode();
	}
}