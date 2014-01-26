package cn.qtone.hibernate.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class BasicDaoImpl extends BaseDaoImpl<Session> implements BasicDao {

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

}
