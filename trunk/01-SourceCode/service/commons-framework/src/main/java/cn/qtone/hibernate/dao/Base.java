package cn.qtone.hibernate.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import cn.qtone.hibernate.Pagination;
import cn.qtone.util.MapUtil;
import cn.qtone.util.StringUtil;

/**
 * Dao接口 - Dao基接口
 * 
 * @author 卢俊生
 */
public abstract class Base<S extends SharedSessionContract> {
	private static final Logger logger = LoggerFactory.getLogger(Base.class);

	@Resource
	protected SessionFactory sessionFactory;

	public abstract S getSession();

	public Query setPaginParameters(Query query, int index, int limit) {
		Assert.notNull(query, "query is required!");

		query.setFirstResult(index > 0 ? index : 0);
		query.setMaxResults(limit > 0 ? limit : 10);
		return query;
	}

	public Query setPaginParameters(Query query, Pagination pagin) {
		Assert.notNull(query, "query is required!");
		Assert.notNull(pagin, "pagin is required!");

		query.setFirstResult(pagin.getIndex());
		query.setMaxResults(pagin.getLimit());
		return query;
	}

	public Query setParameters(Query query, Object... args) {
		Assert.notNull(query, "query is required!");

		for (int i = 0; i < args.length; i++) {
			Object val = args[i];
			query.setParameter(i, val);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	public Query setParameters(Query query, Map<?, ?> map) {
		Assert.notNull(query, "query is required!");

		if (MapUtil.hasItem(map)) {
			Set<Object> keySet = (Set<Object>) map.keySet();
			for (Object key : keySet) {
				Object val = map.get(key);
				if (val instanceof Collection<?>) {
					query.setParameterList(StringUtil.trim(key), (Collection<Object>) val);
				} else if (val.getClass().isArray()) {
					query.setParameterList(StringUtil.trim(key), (Object[]) val);
				} else {
					query.setParameter(StringUtil.trim(key), val);
				}
			}
		}

		return query;
	}

	public Query createQuery(String hql, Object... args) {
		Assert.hasText(hql, "hql is required!");

		Query query = getSession().createQuery(hql);
		setParameters(query, args);
		return query;
	}

	public Query createQuery(String hql, Map<?, ?> map) {
		Assert.hasText(hql, "hql is required!");

		Query query = getSession().createQuery(hql);
		setParameters(query, map);
		return query;
	}

	public SQLQuery createSQLQuery(String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, args);
		return query;
	}

	public SQLQuery createSQLQuery(String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, map);
		return query;
	}

	public String getPKByTable(String table) {
		Assert.hasText(table, "table is required!");
		String pk = null;

		Class<?> entity = getEntityByTable(table);
		if (entity != null) {
			pk = getPKCol(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("TABLE:" + table + ", PK:" + pk);
		}

		return pk;
	}

	public Class<?> getEntityByTable(String table) {
		Class<?> entity = null;
		EntityMetamodel entityMetamodel = null;
		int index = -1;
		try {
			Map<String, ClassMetadata> metaMap = sessionFactory.getAllClassMetadata();
			for (String key : metaMap.keySet()) {
				AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap.get(key);
				String tableName = classMetadata.getTableName().toLowerCase();
				index = tableName.lastIndexOf(".");
				if (index != -1 && table.equalsIgnoreCase(tableName.substring(index + 1))) {
					entityMetamodel = classMetadata.getEntityMetamodel();
					break;
				}
			}
			if (entityMetamodel != null) {
				entity = Class.forName(entityMetamodel.getName());
			}
		} catch (Exception e) {
			throw new RuntimeException("获取实体异常:" + table, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(table + " <==> " + entity == null ? null : entity.getName());
		}

		return entity;
	}

	public String getPKField(Class<?> entity) {
		String pk = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entity);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getReadMethod().isAnnotationPresent(Id.class)) {
					pk = pd.getName();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("获取实体主键属性异常:" + entity.getName(), e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(entity.getName() + " PK_Field: " + pk);
		}

		return pk;
	}

	public String getPKCol(Class<?> entity) {
		String pk = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entity);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getReadMethod().isAnnotationPresent(Id.class)) {
					Column column = pd.getReadMethod().getAnnotation(Column.class);
					pk = column.name();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("获取表主键字段异常:" + entity.getName(), e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(entity.getName() + " PK_Col: " + pk);
		}

		return pk;
	}
}
