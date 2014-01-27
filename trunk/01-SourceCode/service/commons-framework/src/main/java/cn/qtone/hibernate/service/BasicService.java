package cn.qtone.hibernate.service;

import java.util.List;
import java.util.Map;

import cn.qtone.hibernate.Pagination;
import cn.qtone.hibernate.ResultMap;
import cn.qtone.hibernate.Pagination.OrderBy;

/**
 * 公用Service接口
 * 
 * <pre>
 * 注意:
 * 不可直接继承或者重新实现该接口;
 * 如需用到该接口中的方法,可将该接口注入到业务接口中使用.
 * 
 * 1.返回值中的项为Bean时,字段名的映射如NAME=>name(setName), aaa_bbb=>aaaBbb(setAaaBbb);
 * 
 * 2.关于查询返回值中字段类型:
 *   返回值中的项为Map时,统一将java.math.BigDecimal转为java.lang.Long;
 *   返回值中的项为Bean时,java.math.BigDecimal类型可转换为如下类型(根据其setter参数类型而定):
 *   	1.java.math.BigDecimal => java.math.BigDecimal(不转换);
 *   	2.java.math.BigDecimal => java.lang.Long;
 *   	3.java.math.BigDecimal => java.lang.Integer;
 *   	4.java.math.BigDecimal => java.lang.Short;
 *   	5.java.math.BigDecimal => java.lang.Byte;
 *   	6.java.math.BigDecimal => java.lang.Boolean(1-true,其它-false);
 * </pre>
 * 
 * @author 卢俊生
 */
public interface BasicService {

	/**
	 * 执行SQL语句(原生sql实现,支持传入集合类参数)<BR>
	 * <strong>适应于批量更新,删除等</strong>
	 * 
	 * @param sql
	 *            更新语句,语句中参数设置为"<strong>:index</strong>"的形式,第一个参数为0,以此类推.<BR>
	 *            如"update t_table set name=:0 where id in(:1)"
	 * @param values
	 *            sql中参数对应的值,支持集合类型(Collection子类)和非基础类型数组.<BR>
	 *            参数值与SQL语句中的参数一一对应
	 * @return 受影响记录数
	 * @author 卢俊生
	 */
	public int execute(String sql, Object... values);

	/**
	 * 执行sql(insert,update,delete等) <b>适合位置占位符["?"]设参方式, 不支持集合</b> eg :
	 * <b>select * from table where id=? and code=? (√)</b> <i>select * from
	 * table where id=:id and code in(:codes) (×)</i> </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 影响记录数
	 * @author 卢俊生, 2012-8-24
	 */
	public int executeUpdate(String sql, Object... args);

	/**
	 * 执行sql(insert,update,delete等)
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 影响记录数
	 * @author 卢俊生, 2012-8-24
	 */
	public int executeUpdate(String sql, Map<?, ?> map);

	/**
	 * 查询单值/单行
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 单值返回查询字段对应类型;单行返回Map<String, Object>,key为查询字段别名
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> E uniqueResult(String sql, Object... args);

	/**
	 * 查询单值/单行
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 单值返回查询字段对应类型;单行返回Map<String, Object>实例,key为查询字段别名
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> E uniqueResult(String sql, Map<?, ?> map);

	/**
	 * 查询单行
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return clazz实例
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> E uniqueResult(Class<E> clazz, String sql, Object... args);

	/**
	 * 查询单行
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return clazz实例
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> E uniqueResult(Class<E> clazz, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Pagination pagin, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Pagination pagin, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, Pagination pagin, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param orderBy
	 *            排序信息
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-11-1
	 */
	public <E> List<E> list(OrderBy orderBy, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param orderBy
	 *            排序信息
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-11-1
	 */
	public <E> List<E> list(OrderBy orderBy, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param orderBy
	 *            排序信息
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-11-1
	 */
	public <E> List<E> list(Class<E> clazz, OrderBy orderBy, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param orderBy
	 *            排序信息
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-11-1
	 */
	public <E> List<E> list(Class<E> clazz, OrderBy orderBy, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param first
	 *            首记录索引
	 * @param size
	 *            最大记录数
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回Map<String, Object>集合,map中为查询字段别名
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(int first, int size, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param first
	 *            首记录索引
	 * @param size
	 *            最大记录数
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return 单列返回查询列对应类型的集合;多列返回{@link ResultMap}集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(int first, int size, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param first
	 *            首记录索引
	 * @param size
	 *            最大记录数
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param first
	 *            首记录索引
	 * @param size
	 *            最大记录数
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return clazz集合
	 * @author 卢俊生, 2012-8-24
	 */
	public <E> List<E> list(Class<E> clazz, int first, int size, String sql, Map<?, ?> map);

	/**
	 * 分页查询
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Pagination实例(Pagination中list为{@link ResultMap}集合)
	 * @author 卢俊生, 2012-8-24
	 */
	public Pagination pages(Pagination pagin, String sql, Object... args);

	/**
	 * 分页查询
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Pagination实例(Pagination中list为{@link ResultMap}集合)
	 * @author 卢俊生, 2012-8-24
	 */
	public Pagination pages(Pagination pagin, String sql, Map<?, ?> map);

	/**
	 * 分页查询
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Pagination实例(Pagination中list为clazz集合)
	 * @author 卢俊生, 2012-8-24
	 */
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Object... args);

	/**
	 * 分页查询
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param clazz
	 *            Javabean(clazz中必须有所有查询字段别名的setter)
	 * @param pagin
	 *            分页对象
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Pagination实例(Pagination中list为clazz集合)
	 * @author 卢俊生, 2012-8-24
	 */
	public Pagination pages(Class<?> clazz, Pagination pagin, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param size
	 *            最大记录数(默认100)
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(int first, int size, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param size
	 *            最大记录数(默认100)
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(int first, int size, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(OrderBy orderBy, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(OrderBy orderBy, String sql, Map<?, ?> map);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select * from table where id=? and code=? (√)</b>
	 * 	<i>select * from table where id=:id and code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(Pagination pagin, String sql, Object... args);

	/**
	 * 查询列表
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            查询参数
	 * @return Object[]集合
	 * @author 卢俊生, 2012-8-28
	 */
	public List<Object[]> arrays(Pagination pagin, String sql, Map<?, ?> map);

	/**
	 * 查询单个实体(HQL)
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select m from Entity m where m.id=? and m.code=?(√)</b>
	 * 	<i>select m from Entity m where m.id=:id and m.code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param hql
	 *            hql语句
	 * @param args
	 *            查询参数
	 * @return Entity/Object[]/单值(需自行判断)
	 * @author 卢俊生, 2012-9-10
	 */
//	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Object... args);

	/**
	 * 查询单个实体(HQL)
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select m from Entity m where m.id=:id and m.code in(:codes) (√)</b>
	 * 	<i>select m from Entity m where m.id=? and m.code=? (×)</i>
	 * </pre>
	 * 
	 * @param hql
	 *            hql语句
	 * @param map
	 *            查询参数
	 * @return Entity/Object[]/单值(需自行判断)
	 * @author 卢俊生, 2012-9-10
	 */
//	public <E> E uniqueResult4Hql(Class<E> clazz, String hql, Map<?, ?> map);

	/**
	 * 查询列表(HQL)
	 * 
	 * <pre>
	 * <b>适合位置占位符["?"]设参方式, 不支持集合</b>
	 * eg : 
	 * 	<b>select m from Entity m where m.id=? and m.code=?(√)</b>
	 * 	<i>select m from Entity m where m.id=:id and m.code in(:codes) (×)</i>
	 * </pre>
	 * 
	 * @param hql
	 *            hql语句
	 * @param args
	 *            查询参数
	 * @return Entity/Object[]/单值 集合(需自行判断)
	 * @author 卢俊生, 2012-9-10
	 */
//	public <E> List<E> list4Hql(String hql, Object... args);

	/**
	 * 查询列表(HQL)
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select m from Entity m where m.id=:id and m.code in(:codes) (√)</b>
	 * 	<i>select m from Entity m where m.id=? and m.code=? (×)</i>
	 * </pre>
	 * 
	 * @param hql
	 *            hql语句
	 * @param map
	 *            查询参数
	 * @return Entity/Object[]/单值 集合(需自行判断)
	 * @author 卢俊生, 2012-9-10
	 */
//	public <E> List<E> list4Hql(String hql, Map<?, ?> map);

	/**
	 * 分页查询(HQL)
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select m from Entity m where m.id=:id and m.code in(:codes) (√)</b>
	 * 	<i>select m from Entity m where m.id=? and m.code=? (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param hql
	 *            hql语句
	 * @param args
	 *            查询参数
	 * @return Pagination实例
	 * @author 卢俊生, 2012-9-10
	 */
//	public Pagination pages4Hql(Pagination pagin, String hql, Object... args);

	/**
	 * 分页查询(HQL)
	 * 
	 * <pre>
	 * <b>适合名称占位符[":name"]设参方式, 支持集合Collection,Object[]</b>
	 * Map中的键对应Sql语句中的参数名称,即":name"中的"name"
	 * eg : 
	 * 	<b>select * from table where id=:id and code in(:codes) (√)</b>
	 * 	<i>select * from table where id=? and code=? (×)</i>
	 * </pre>
	 * 
	 * @param pagin
	 *            分页对象
	 * @param hql
	 *            hql语句
	 * @param map
	 *            查询参数
	 * @return Pagination实例
	 * @author 卢俊生, 2012-9-10
	 */
//	public Pagination pages4Hql(Pagination pagin, String hql, Map<?, ?> map);

	/**
	 * 唯一性校验
	 * 
	 * @param table 表名(必须)
	 * @param colunm 字段名(必须)
	 * @param value 校验值(必须)
	 * @param pkValue 主键(非必须,不为空时忽略该行数据)
	 * @param pkColunm 主键字段名(非必须,默认为"id")
	 * @return boolean
	 */
	public boolean unique(String table, String colunm, String value, Long pkValue, String pkColunm);

	/**
	 * 生成树形编码
	 * 
	 * @param table 表名
	 * @param column 字段名
	 * @param pk 主键字段名
	 * @param parent 父级字段名
	 * @param parentVal 父级主键值
	 * @param length 各级长度
	 * @param firstCode 各级首值
	 * @return 树形编码
	 */
	public String generateTreeCode(String table, String column, String pk, String parent, Object parentVal, int length, String firstCode);
}
