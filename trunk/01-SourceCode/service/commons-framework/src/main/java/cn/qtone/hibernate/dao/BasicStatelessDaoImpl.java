package cn.qtone.hibernate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 公用Dao接口
 * 
 * @author 卢俊生
 */
@Repository
public class BasicStatelessDaoImpl extends BaseDaoImpl<StatelessSession> implements BasicStatelessDao {
	private static final Logger logger = LoggerFactory.getLogger(BasicStatelessDaoImpl.class);

	private static ThreadLocal<StatelessSession> threadStatelessSession = new ThreadLocal<StatelessSession>();

	private Transaction transaction;

	@Resource
	private SessionFactory sessionFactory;

	@Override
	public StatelessSession getSession() {
		StatelessSession session = threadStatelessSession.get();
		if (session == null) {
			synchronized (this) {
				session = sessionFactory.openStatelessSession();
				threadStatelessSession.set(session);
			}
		}
		return session;
	}

	@Override
	@SuppressWarnings("deprecation")
	public Connection getConnection() {
		return getSession().connection();
	}

	@Override
	public Transaction beginTransaction() {
		commit();
		transaction = getSession().beginTransaction();
		return transaction;
	}

	@Override
	public void commit() {
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
		}
	}

	@Override
	public void rollback() {
		if (transaction != null && transaction.isActive()) {
			transaction.rollback();
		}
	}

	@Override
	public void closeSession() {
		StatelessSession session = threadStatelessSession.get();
		if (session != null) {
			session.close();
			session = null;
			threadStatelessSession.remove();
		}
	}

	@Override
	public ResultSet getResultSet(String sql) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public boolean execute(String sql, List<?> list, int commitSize) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			commitSize = commitSize > 0 ? commitSize : 500;
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				for (int j = 0; j < obj.length; j++) {
					pstmt.setObject(j + 1, obj[j]);
				}
				pstmt.addBatch();

				if ((i + 1) % commitSize == 0) {
					pstmt.executeBatch();
				}
			}
			if (list.size() % commitSize != 0) {
				pstmt.executeBatch();
			}
			conn.commit();

			if (logger.isDebugEnabled()) {
				logger.debug("影响记录数:{}, SQL:{}", list.size(), sql);
			}

			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				logger.error(sql, sqle);
			}
			logger.error("execute failed,sql:" + sql, e);
			return false;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(sql, e);
			}
		}
	}

}
