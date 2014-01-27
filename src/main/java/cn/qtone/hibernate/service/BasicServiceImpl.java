package cn.qtone.hibernate.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.qtone.hibernate.Pagination;
import cn.qtone.hibernate.Pagination.OrderBy;
import cn.qtone.hibernate.dao.BasicDao;

/**
 * 公用Service接口
 * 
 * @author 卢俊生
 */
@Service
public class BasicServiceImpl implements BasicService {

	@Resource
	private BasicDao dao;

	@Override
	public int execute(String sql, Object... values) {
		return dao.execute(sql, values);
	}

	@Override
	public int executeUpdate(String sql, Object... args) {
		return dao.executeUpdate(sql, args);
	}

	@Override
	public int executeUpdate(String sql, Map<?, ?> map) {
		return dao.executeUpdate(sql, map);
	}

	@Override
	public <E> E uniqueResult(String sql, Object... args) {
		return dao.uniqueResult(sql, args);
	}

	@Override
	public <E> E uniqueResult(String sql, Map<?, ?> map) {
		return dao.uniqueResult(sql, map);
	}

	@Override
	public <E> E uniqueResult(Class<E> clazz, String sql, Object... args) {
		return dao.uniqueResult(clazz, sql, args);
	}

	@Override
	public <E> E uniqueResult(Class<E> clazz, String sql, Map<?, ?> map) {
		return dao.uniqueResult(clazz, sql, map);
	}

	@Override
	public <E> List<E> list(String sql, Object... args) {
		return dao.list(sql, args);
	}

	@Override
	public <E> List<E> list(String sql, Map<?, ?> map) {
		return dao.list(sql, map);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, String sql, Object... args) {
		return dao.list(clazz, sql, args);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, String sql, Map<?, ?> map) {
		return dao.list(clazz, sql, map);
	}

	@Override
	public <E> List<E> list(Pagination pagin, String sql, Object... args) {
		return dao.list(pagin, sql, args);
	}

	@Override
	public <E> List<E> list(Pagination pagin, String sql, Map<?, ?> map) {
		return dao.list(pagin, sql, map);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Object... args) {
		return dao.list(clazz, pagin, sql, args);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Map<?, ?> map) {
		return dao.list(clazz, pagin, sql, map);
	}

	@Override
	public <E> List<E> list(OrderBy orderBy, String sql, Object... args) {
		return dao.list(orderBy, sql, args);
	}

	@Override
	public <E> List<E> list(OrderBy orderBy, String sql, Map<?, ?> map) {
		return dao.list(orderBy, sql, map);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, OrderBy orderBy, String sql, Object... args) {
		return dao.list(clazz, orderBy, sql, args);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, OrderBy orderBy, String sql, Map<?, ?> map) {
		return dao.list(clazz, orderBy, sql, map);
	}

	@Override
	public <E> List<E> list(int first, int size, String sql, Object... args) {
		return dao.list(first, size, sql, args);
	}

	@Override
	public <E> List<E> list(int first, int size, String sql, Map<?, ?> map) {
		return dao.list(first, size, sql, map);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Object... args) {
		return dao.list(clazz, first, size, sql, args);
	}

	@Override
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Map<?, ?> map) {
		return dao.list(clazz, first, size, sql, map);
	}

	@Override
	public Pagination pages(Pagination pagin, String sql, Object... args) {
		return dao.pages(pagin, sql, args);
	}

	@Override
	public Pagination pages(Pagination pagin, String sql, Map<?, ?> map) {
		return dao.pages(pagin, sql, map);
	}

	@Override
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Object... args) {
		return dao.pages(clazz, pagin, sql, args);
	}

	@Override
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Map<?, ?> map) {
		return dao.pages(clazz, pagin, sql, map);
	}

	@Override
	public List<Object[]> arrays(String sql, Object... args) {
		return dao.arrays(sql, args);
	}

	@Override
	public List<Object[]> arrays(String sql, Map<?, ?> map) {
		return dao.arrays(sql, map);
	}

	@Override
	public List<Object[]> arrays(int first, int size, String sql, Object... args) {
		return dao.arrays(first, size, sql, args);
	}

	@Override
	public List<Object[]> arrays(int first, int size, String sql, Map<?, ?> map) {
		return dao.arrays(first, size, sql, map);
	}

	@Override
	public List<Object[]> arrays(OrderBy orderBy, String sql, Object... args) {
		return dao.arrays(orderBy, sql, args);
	}

	@Override
	public List<Object[]> arrays(OrderBy orderBy, String sql, Map<?, ?> map) {
		return dao.arrays(orderBy, sql, map);
	}

	@Override
	public List<Object[]> arrays(Pagination pagin, String sql, Object... args) {
		return dao.arrays(pagin, sql, args);
	}

	@Override
	public List<Object[]> arrays(Pagination pagin, String sql, Map<?, ?> map) {
		return dao.arrays(pagin, sql, map);
	}

	/*
	@Override
	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Object... args) {
		return dao.uniqueResult4Hql(clazz, hql, args);
	}

	@Override
	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Map<?, ?> map) {
		return dao.uniqueResult4Hql(clazz, hql, map);
	}

	@Override
	public <E> List<E> list4Hql(String hql, Object... args) {
		return dao.list4Hql(hql, args);
	}

	@Override
	public <E> List<E> list4Hql(String hql, Map<?, ?> map) {
		return dao.list4Hql(hql, map);
	}

	@Override
	public Pagination pages4Hql(Pagination pagin, String hql, Object... args) {
		return dao.pages4Hql(pagin, hql, args);
	}

	@Override
	public Pagination pages4Hql(Pagination pagin, String hql, Map<?, ?> map) {
		return dao.pages4Hql(pagin, hql, map);
	}
	*/
	@Override
	public boolean unique(String table, String colunm, String value, Long pkValue, String pkColunm) {
		return dao.unique(table, colunm, value, pkValue, pkColunm);
	}

	@Override
	public String generateTreeCode(String table, String column, String pk, String parent, Object parentVal, int length, String firstCode) {
		return dao.generateTreeCode(table, column, pk, parent, parentVal, length, firstCode);
	}
}
