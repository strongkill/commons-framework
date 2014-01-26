package cn.qtone.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;

import cn.qtone.hibernate.dao.Dao;
import cn.qtone.hibernate.dao.DaoImpl;
import cn.qtone.util.SpringUtil;

public class DAOUtil {

	@SuppressWarnings("rawtypes")
	private static Map<Class, Dao> daos = new HashMap<Class, Dao>();
	private static SessionFactory sessionFactory = SpringUtil.getBean(SessionFactory.class);

	public static <T> Dao<T, Long> get(Class<T> entityClass) {
		return get(entityClass, Long.class);
	}

	@SuppressWarnings("unchecked")
	public static <T, K extends Serializable> Dao<T, K> get(Class<T> entityClass, Class<K> pkClass) {
		Dao<T, K> dao = (Dao<T, K>) daos.get(entityClass);
		if (dao == null) {
			synchronized (DAOUtil.class) {
				dao = new DaoImpl<T, K>(sessionFactory, entityClass, pkClass);
				daos.put(entityClass, dao);
			}
		}
		return dao;
	}

}
