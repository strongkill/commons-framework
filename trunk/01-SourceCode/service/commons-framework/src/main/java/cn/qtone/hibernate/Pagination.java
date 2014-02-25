package cn.qtone.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 分页对象
 * 
 * @author 卢俊生
 */
public class Pagination {

	private int index = 0;// 首记录索引
	private int limit = 10;// 每页记录数
	private int count = 0;// 总记录数

	private int page = 1;
	private int totalPage = 0;

	private OrderBy orderBy;

	@SuppressWarnings("rawtypes")
	private List list = new ArrayList();// 数据集合

	public Pagination() {
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index > 0) {
			this.index = index;
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if (limit > 0) {
			this.limit = limit;
			this.index = this.limit * (this.page - 1);
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		this.totalPage = (this.count + this.limit - 1) / this.limit;
		if (this.page > this.totalPage) {
			this.setPage(this.totalPage);
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page > 0) {
			this.page = page;
		} else {
			this.page = 1;
		}

		this.index = this.limit * (this.page - 1);
	}

	public long getTotalPage() {
		return totalPage;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
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