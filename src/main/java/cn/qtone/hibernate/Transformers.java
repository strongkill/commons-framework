package cn.qtone.hibernate;

import org.hibernate.transform.ResultTransformer;

/**
 * ZTransformers
 * 
 * @author 卢俊生, 2012-8-28
 */
public class Transformers {

	public static final AliasToEntityMapResultTransformer ALIAS_TO_ENTITY_MAP = AliasToEntityMapResultTransformer.INSTANCE;

	public static ResultTransformer aliasToBean(Class<?> target) {
		return new AliasToBeanResultTransformer(target);
	}
}
