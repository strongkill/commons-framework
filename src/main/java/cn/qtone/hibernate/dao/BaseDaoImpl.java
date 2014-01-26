package cn.qtone.hibernate.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SharedSessionContract;
import org.springframework.util.Assert;

import cn.qtone.hibernate.Pagination;
import cn.qtone.hibernate.Pagination.OrderBy;
import cn.qtone.hibernate.QueryTool;
import cn.qtone.hibernate.ResultMap;
import cn.qtone.hibernate.Transformers;
import cn.qtone.util.MapUtil;
import cn.qtone.util.NumberUtil;

/**
 * 公用Dao接口
 * 
 * @author 卢俊生
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T extends SharedSessionContract> extends Base<T> implements BaseDao {

	@Override
	public int execute(String sql, Object... values) {
		Assert.hasText(sql, "sql is required");
		Query query = getSession().createSQLQuery(sql);
		for (Integer index = 0; index < values.length; index++) {
			Object obj = values[index];
			if (obj.getClass().isArray()) { // 非基础类型数组
				query.setParameterList(index.toString(), (Object[]) obj);
			} else if (obj instanceof Collection) { // 集合类型
				query.setParameterList(index.toString(), (Collection<Object>) obj);
			} else {
				query.setParameter(index.toString(), obj);
			}
		}

		return query.executeUpdate();
	}

	@Override
	public int executeUpdate(String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		return createSQLQuery(sql, args).executeUpdate();
	}

	@Override
	public int executeUpdate(String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		return createSQLQuery(sql, map).executeUpdate();
	}

	@Override
	public <E> E uniqueResult(String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		ResultMap resultMap = (ResultMap) query.uniqueResult();
		return QueryTool.convert(resultMap);
	}

	@Override
	public <E> E uniqueResult(String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		ResultMap resultMap = (ResultMap) query.uniqueResult();
		return QueryTool.convert(resultMap);
	}

	@Override
	public <E> E uniqueResult(Class<E> clazz, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return (E) query.uniqueResult();
	}

	@Override
	public <E> E uniqueResult(Class<E> clazz, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return (E) query.uniqueResult();
	}

	@Override
	public <E> List<E> list(String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return query.list();
	}

	@Override
	public <E> List<E> list(Class<E> clazz, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return query.list();
	}

	@Override
	public <E> List<E> list(Pagination pagin, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, false), args);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		setPaginParameters(query, pagin);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(Pagination pagin, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, false), map);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		setPaginParameters(query, pagin);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, false), args);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		setPaginParameters(query, pagin);

		return query.list();
	}

	@Override
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, false), map);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		setPaginParameters(query, pagin);

		return query.list();
	}

	@Override
	public <E> List<E> list(List<OrderBy> orderBies, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, false), args);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(List<OrderBy> orderBies, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, false), map);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, List<OrderBy> orderBies, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, false), args);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return query.list();
	}

	@Override
	public <E> List<E> list(Class<E> clazz, List<OrderBy> orderBies, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, false), map);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		return query.list();
	}

	@Override
	public <E> List<E> list(int first, int size, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		setPaginParameters(query, first, size);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(int first, int size, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		setPaginParameters(query, first, size);

		List<ResultMap> resultList = query.list();
		return QueryTool.convert(resultList);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		setPaginParameters(query, first, size);

		return query.list();
	}

	@Override
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		query.setResultTransformer(Transformers.aliasToBean(clazz));

		setPaginParameters(query, first, size);

		return query.list();
	}

	@Override
	public Pagination pages(Pagination pagin, String sql, Object... args) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(sql, "sql is required!");

		long count = uniqueResult(long.class, QueryTool.toCountSQL(sql), args);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list(pagin, sql, args));
		}

		return pagin;
	}

	@Override
	public Pagination pages(Pagination pagin, String sql, Map<?, ?> map) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(sql, "sql is required!");

		long count = uniqueResult(long.class, QueryTool.toCountSQL(sql), map);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list(pagin, sql, map));
		}

		return pagin;
	}

	@Override
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Object... args) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(sql, "sql is required!");

		long count = uniqueResult(long.class, QueryTool.toCountSQL(sql), args);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list(clazz, pagin, sql, args));
		}

		return pagin;
	}

	@Override
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Map<?, ?> map) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(sql, "sql is required!");

		long count = uniqueResult(long.class, QueryTool.toCountSQL(sql), map);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list(clazz, pagin, sql, map));
		}

		return pagin;
	}

	@Override
	public List<Object[]> arrays(String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		return query.list();
	}

	@Override
	public List<Object[]> arrays(String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		return query.list();
	}

	@Override
	public List<Object[]> arrays(int first, int size, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, args);
		setPaginParameters(query, first, size);

		return query.list();
	}

	@Override
	public List<Object[]> arrays(int first, int size, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(sql, map);
		setPaginParameters(query, first, size);

		return query.list();
	}

	@Override
	public List<Object[]> arrays(List<OrderBy> orderBies, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, true), args);
		return query.list();
	}

	@Override
	public List<Object[]> arrays(List<OrderBy> orderBies, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, orderBies, true), map);
		return query.list();
	}

	@Override
	public List<Object[]> arrays(Pagination pagin, String sql, Object... args) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, true), args);
		return query.list();
	}

	@Override
	public List<Object[]> arrays(Pagination pagin, String sql, Map<?, ?> map) {
		Assert.hasText(sql, "sql is required!");

		SQLQuery query = createSQLQuery(QueryTool.appendOrderBy(sql, pagin, true), map);
		return query.list();
	}

	/*
	@Override
	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Object... args) {
		Assert.hasText(hql, "hql is required!");
		Query query = createQuery(hql, args);
		Object obj = query.uniqueResult();
		return (E) obj;
	}

	@Override
	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Map<?, ?> map) {
		Assert.hasText(hql, "hql is required!");
		Query query = createQuery(hql, map);
		Object obj = query.uniqueResult();
		return (E) obj;
	}

	@Override
	public <E> List<E> list4Hql(String hql, Object... args) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(hql, args);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public <E> List<E> list4Hql(String hql, Map<?, ?> map) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(hql, map);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public <E> List<E> list4Hql(List<OrderBy> orderBies, String hql, Object... args) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(QueryUtil.appendOrderBy(hql, orderBies, true), args);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public <E> List<E> list4Hql(List<OrderBy> orderBies, String hql, Map<?, ?> map) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(QueryUtil.appendOrderBy(hql, orderBies, true), map);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public <E> List<E> list4Hql(Pagination pagin, String hql, Object... args) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(QueryUtil.appendOrderBy(hql, pagin, true), args);

		setPageParameter(query, pagin);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public <E> List<E> list4Hql(Pagination pagin, String hql, Map<?, ?> map) {
		Assert.hasText(hql, "hql is required!");

		Query query = createQuery(QueryUtil.appendOrderBy(hql, pagin, true), map);

		setPageParameter(query, pagin);

		List<Object> list = query.list();
		return (List<E>) list;
	}

	@Override
	public Pagination pages4Hql(Pagination pagin, String hql, Object... args) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(hql, "hql is required!");

		long count = uniqueResult4Hql(long.class, QueryUtil.toCountHQL(hql), args);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list4Hql(pagin, hql, args));
		}

		return pagin;
	}

	@Override
	public Pagination pages4Hql(Pagination pagin, String hql, Map<?, ?> map) {
		Assert.notNull(pagin, "pagin is required!");
		Assert.hasText(hql, "hql is required!");

		long count = uniqueResult4Hql(long.class, QueryUtil.toCountHQL(hql), map);
		pagin.setTotal(count);

		if (count == 0) {
			pagin.setList(new ArrayList<Object>());
		} else {
			pagin.setList(list4Hql(pagin, hql, map));
		}

		return pagin;
	}
	*/

	@Override
	public boolean unique(String table, String colunm, String value, Long pkValue, String pkColunm) {
		Assert.hasText(table, "table is required!");
		Assert.hasText(colunm, "colunm is required!");
		Assert.hasText(value, "value is required!");

		StringBuilder sql = new StringBuilder();
		sql.append(" select count(0) from " + table);
		sql.append(" where " + colunm + " = :value ");

		if (pkValue != null) {
			if (pkColunm == null) {
				pkColunm = "id";
			}
			sql.append(" and " + pkColunm + " <> :pk ");
		}

		long rows = uniqueResult(long.class, sql.toString(), MapUtil.toMap("value,pk", value, pkValue));

		return rows == 0;
	}

	@Override
	public String generateTreeCode(String table, String column, String pk, String parent, Object parentVal, int length, String firstCode) {
		String code = uniqueResult(String.class, "select max(" + column + ") from " + table + " where " + parent + "=?", parentVal);

		if (code == null) {
			code = uniqueResult(String.class, "select " + column + " from " + table + " where " + pk + "=?", parentVal);
			if (code == null) {
				code = firstCode;
			} else {
				code += firstCode;
			}
		} else {
			int subIndex = code.length() - length;
			code = code.substring(0, subIndex) + (NumberUtil.parseInt(code.substring(subIndex)) + 1);
		}

		return code;
	}
}
