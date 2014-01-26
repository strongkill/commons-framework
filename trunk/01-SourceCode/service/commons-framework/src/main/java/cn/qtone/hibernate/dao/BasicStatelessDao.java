package cn.qtone.hibernate.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

/**
 * 公用Dao接口(无状态)
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
public interface BasicStatelessDao extends BaseDao {

	public StatelessSession getSession();

	public Connection getConnection();

	public Transaction beginTransaction();

	public void commit();

	public void rollback();

	public void closeSession();

	/**
	 * 返回ResultSet（注意需要手动关闭数据库链接）
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet getResultSet(String sql);

	/**
	 * 批量插入/更新
	 * 
	 * @param sql SQL语句
	 * @param list 参数集合(数组集合)
	 * @param commitSize 分段提交数(每多少个提交一次)
	 * @return boolean
	 */
	public boolean execute(String sql, List<?> list, int commitSize);

}
