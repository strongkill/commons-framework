package cn.qtone.hibernate;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StringType;

public class OracleDialect extends Oracle10gDialect {

	public OracleDialect() {
		super();
		// very important, mapping char(n) to String
		registerHibernateType(Types.CHAR, StringType.class.getName());
	}

//	@Override
//	public String getSelectSequenceNextValString(String sequenceName) {
//		return sequenceName + ".nextval + 1";
//	}
}
