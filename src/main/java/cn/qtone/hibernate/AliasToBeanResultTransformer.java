package cn.qtone.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

import cn.qtone.util.ColumnAndAliasConverter;

/**
 * ResultTransformer
 * 
 * @author 卢俊生, 2012-8-27
 */
@SuppressWarnings("serial")
public class AliasToBeanResultTransformer implements ResultTransformer {

	private final Class<?> resultClass;
	private final PropertyAccessor propertyAccessor;
	private Setter[] setters;

	public AliasToBeanResultTransformer(Class<?> resultClass) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		this.resultClass = resultClass;
		this.propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] { PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
				PropertyAccessorFactory.getPropertyAccessor("field") });
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List transformList(List paramList) {
		return paramList;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result = null;
		try {
			if (this.setters == null) {
				this.setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; ++i) {
					String alias = aliases[i];
					if (alias != null) {
						try {
							this.setters[i] = this.propertyAccessor.getSetter(this.resultClass, ColumnAndAliasConverter.columnToAlias(alias));
						} catch (PropertyNotFoundException e) {
						}
					}
				}
			}
			result = this.resultClass.newInstance();

			Setter setter = null;
			Object value = null;
			for (int i = 0; i < tuple.length; ++i) {
				setter = this.setters[i];
				if (setter != null) {
					value = tuple[i];
					// 转换类型
					if (value != null && value instanceof BigDecimal) {
						Class<?> clazz = setter.getMethod().getParameterTypes()[0];
						if (!clazz.equals(BigDecimal.class)) {
							BigDecimal v = (BigDecimal) value;

							if (clazz.equals(Long.class) || clazz.equals(long.class)) { // Long|long
								value = v.longValueExact();
							} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) { // Integer|int
								value = v.intValueExact();
							} else if (clazz.equals(Double.class) || clazz.equals(double.class)) { // Double|double
								value = v.doubleValue();
							} else if (clazz.equals(Float.class) || clazz.equals(float.class)) { // Float|float
								value = v.floatValue();
							} else if (clazz.equals(Short.class) || clazz.equals(short.class)) { // Short|short
								value = v.shortValueExact();
							} else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) { // Byte|byte
								value = v.byteValueExact();
							} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) { // Boolean|boolean
								value = value.toString().equals("1");
							}
						}
					}
					setter.set(result, value, null);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName());
		}
		return result;
	}

}
