package cn.qtone.hibernate;

import java.util.ArrayList;
import java.util.List;

import cn.qtone.hibernate.Pagination.OrderBy;
import cn.qtone.util.ColumnAndAliasConverter;
import cn.qtone.util.StringUtil;

/**
 * 数据库操作辅助工具栏
 * 
 * @author 卢俊生, 2012-8-28
 */
public class QueryTool {

	/**
	 * Map类型查询结果转换
	 * 
	 * <pre>
	 * 如结果集仅包含一列,则将结果转换为单个简单对象
	 * </pre>
	 * 
	 * @param map Map类型结果集
	 * @return 泛型;如结果集仅包含一列,则返回单个简单对象,否则直接返回map
	 * @author 卢俊生, 2012-8-28
	 */
	@SuppressWarnings("unchecked")
	public static final <E> E convert(ResultMap map) {
		if (map == null) {
			return (E) map;
		}
		if (map.size() == 1) {
			Object val = map.values().toArray()[0];
			return (E) val;
		} else {
			return (E) map;
		}
	}

	/**
	 * Map类型查询结果转换
	 * 
	 * <pre>
	 * 如结果集仅包含一列,则将结果转换为单个简单对象集合
	 * </pre>
	 * 
	 * @param mapList Map类型结果集
	 * @return 泛型;如结果集仅包含一列,则返回单个简单对象集合,否则直接返回mapList
	 * @author 卢俊生, 2012-8-28
	 */
	@SuppressWarnings("unchecked")
	public static final <E> List<E> convert(List<ResultMap> mapList) {
		if (!mapList.isEmpty() && mapList.get(0).size() == 1) {
			List<E> list = new ArrayList<E>();
			for (int i = 0; i < mapList.size(); i++) {
				list.add((E) convert(mapList.get(i)));
			}
			return list;
		} else {
			return (List<E>) mapList;
		}
	}

	/**
	 * 将一般查询语句拼装成对应查询总记录数的语句
	 * 
	 * @param sql 一般查询语句
	 * @return 查询总记录数语句
	 * @author 卢俊生, 2012-8-28
	 */
	public static final String toCountSQL(String sql) {
		// 处理空白字符,去除","和"="两端的空白字符,其它空白字符替换为单个空格
		// String countQL = sql.replaceAll("\\s*,\\s*", ",").replaceAll("\\s*=\\s*", "=").replaceAll("\\s+",
		// " ").trim();

		String countQL = sql;
		String lowerCaseQL = countQL.toLowerCase(); // 转换小写,获取关键字索引用

		// 处理排序子句
		int index = lowerCaseQL.indexOf(" order by ");
		if (index != -1 && lowerCaseQL.indexOf("partition by") == -1) {
			countQL = countQL.substring(0, index);
		}

		countQL = "select count(0) from (" + countQL + ")";

		return countQL;
	}

	/**
	 * 将HQL语句拼装成对应查询总记录数的语句
	 * 
	 * @param hql HQL语句
	 * @return 查询总记录数语句
	 * @author 卢俊生, 2012-8-28
	 */
	public static final String toCountHQL(String hql) {
		String orderBy = " order by ";
		String groupBy = " group by ";
		String from = " from ";

		// 处理空白字符,去除","和"="两端的空白字符,其它空白字符替换为单个空格
		String countQL = hql.replaceAll("\\s*,\\s*", ",").replaceAll("\\s*=\\s*", "=").replaceAll("\\s+", " ").trim();

		String lowerCaseSQL = countQL.toLowerCase(); // 转换小写,获取关键字索引用

		int index = -1;

		// 从后往前的处理顺序处理关键字,以避免sql截取后,用lowerCaseSQL取得关键字索引与sql不一致的情况
		// 处理排序子句
		index = lowerCaseSQL.indexOf(orderBy);
		if (index != -1) {
			countQL = countQL.substring(0, index);
		}

		// 判断是否分组查询(group by)
		index = lowerCaseSQL.indexOf(groupBy);
		if (index != -1) { // 分组查询(存在group by 子句)
			countQL = "select count(*) from (" + countQL + ")";
		} else {
			index = lowerCaseSQL.indexOf(from); // 查找字符串位置
			countQL = countQL.substring(index, countQL.length()); // 去掉字段
			countQL = "select count(*)" + countQL;
		}

		return countQL;
	}

	/**
	 * 拼接order by 子句
	 * 
	 * @param ql 查询语句
	 * @param pagin Pagination
	 * @param isHql 是否HQL语句
	 * @return 拼接后的查询语句
	 */
	public static final String appendOrderBy(String ql, Pagination pagin, boolean isHql) {
		return appendOrderBy(ql, pagin.orderBy(), isHql);
	}

	/**
	 * 拼接order by 子句
	 * 
	 * @param ql 查询语句
	 * @param orderBies OrderBy集合
	 * @param isHql 是否HQL语句
	 * @return 拼接后的查询语句
	 */
	public static final String appendOrderBy(String ql, OrderBy orderBy, boolean isHql) {
		if (StringUtil.hasText(ql) && orderBy != null) {
			String alias = null;
			StringBuilder ob = new StringBuilder(" order by ");

			alias = isHql ? ColumnAndAliasConverter.columnToAlias(orderBy.getProperty()) : ColumnAndAliasConverter.aliasToColumn(orderBy.getProperty());
			ob.append(alias + " " + orderBy.getOrderType());

			ql += ob;
		}
		return ql;
	}

//	public static final String appendOrderBy(String ql, Pagination pagin, String... alias) {
//		List<OrderBy> orderBies = pagin.getOrderByList();
//		if (StringUtil.hasText(ql) && ListUtil.hasItem(orderBies)) {
//			alias = Arrays.copyOf(alias, orderBies.size());
//
//			StringBuilder ob = new StringBuilder(" order by ");
//
//			OrderBy orderBy = orderBies.get(0);
//			ob.append(toAlias(alias[0]) + orderBy.getProperty() + " " + orderBy.getOrderType());
//
//			for (int i = 1; i < orderBies.size(); i++) {
//				orderBy = orderBies.get(i);
//				ob.append("," + toAlias(alias[i]) + orderBy.getProperty() + " " + orderBy.getOrderType());
//			}
//
//			ql += ob;
//		}
//		return ql;
//	}

//	private static final String toAlias(String alias) {
//		if (StringUtil.hasText(alias)) {
//			return alias + ".";
//		} else {
//			return "";
//		}
//	}

}
