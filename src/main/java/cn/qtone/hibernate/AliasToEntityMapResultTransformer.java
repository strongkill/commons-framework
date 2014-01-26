package cn.qtone.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import org.hibernate.transform.BasicTransformerAdapter;


/**
 * ResultTransformer
 * 
 * @author 卢俊生, 2012-8-28
 */
@SuppressWarnings("serial")
public class AliasToEntityMapResultTransformer extends BasicTransformerAdapter implements Serializable {

	public static final AliasToEntityMapResultTransformer INSTANCE = new AliasToEntityMapResultTransformer();

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		ResultMap result = new ResultMap(tuple.length);
		for (int i = 0; i < tuple.length; ++i) {
			String alias = aliases[i];
			if (alias != null) {
				// 转换类型
				if (tuple[i] != null) {
					// BigDecimal 类型处理
					if (tuple[i] instanceof BigDecimal) {
						BigDecimal v = (BigDecimal) tuple[i];

						boolean flag = v.scale() == 0 && v.abs().min(BigDecimal.valueOf(Long.MAX_VALUE)).longValue() <= Long.MAX_VALUE;

						if (flag) {
							tuple[i] = v.longValue();
						} else {
							tuple[i] = v.doubleValue();
						}
					} else if (tuple[i] instanceof Date) {
						Date v = (Date) tuple[i];

						java.util.Date newdate = new java.util.Date(v.getTime());
						tuple[i] = newdate;
					}
				}
				result.put(alias, tuple[i]);
			}
		}
		return result;
	}

	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public boolean equals(Object other) {
		return ((other != null) && (AliasToEntityMapResultTransformer.class.isInstance(other)));
	}

	@Override
	public int hashCode() {
		return super.getClass().getName().hashCode();
	}
}
