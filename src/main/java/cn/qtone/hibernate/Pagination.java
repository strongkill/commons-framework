package cn.qtone.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 分页对象
 * 
 * @author 卢俊生
 */
public class Pagination {

	private int index = 0;// 首记录索引
	private int limit = 10;// 每页记录数
	private long count = 0L;// 总记录数
	private OrderBy orderBy;

	@SuppressWarnings("rawtypes")
	private List list = new ArrayList();// 数据集合

	public Pagination() {
	}

	public Pagination(Integer first, Integer size, OrderBy orderBy) {
		index(first);
		limit(size);
		orderBy(orderBy);
	}

	public int index() {
		return index;
	}

	public void index(int index) {
		if (index > 0) {
			this.index = index;
		}
	}

	public int limit() {
		return limit;
	}

	public void limit(int limit) {
		if (limit > 0) {
			this.limit = limit;
		}
	}

	public long count() {
		return count;
	}

	public void count(long count) {
		this.count = count;
	}

	public OrderBy orderBy() {
		return orderBy;
	}

	public void orderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@SuppressWarnings("rawtypes")
	public List list() {
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void list(List list) {
		this.list = list;
	}

	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "list");
	}

	public enum OrderType {
		asc, desc
	}

	public class OrderBy {

		private String property;
		private OrderType orderType;

		public OrderBy(String property) {
			this.property = property;
			this.orderType = OrderType.asc;
		}

		public OrderBy(String property, OrderType orderType) {
			this.property = property;
			this.orderType = orderType;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public OrderType getOrderType() {
			return orderType;
		}

		public void setOrderType(OrderType orderType) {
			this.orderType = orderType;
		}
	}

}