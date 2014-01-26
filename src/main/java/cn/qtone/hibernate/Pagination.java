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

	private int first = 0;// 首记录索引
	private int pageSize = 10;// 每页记录数
	private long total = 0L;// 总记录数

	@SuppressWarnings("rawtypes")
	private List list = new ArrayList();// 数据集合

	public Pagination() {
	}

	public Pagination(Integer first, Integer size, String sort, String order) {
		setFirst(first);
		setPageSize(size);

		setOrderBy(sort, order);
	}

	/**
	 * 排序
	 */
	private List<OrderBy> orderByList = new ArrayList<OrderBy>();

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		if (first > 0) {
			this.first = first;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

//	public Integer getTotalPage() {
//		return Long.valueOf((total + size - 1) / size).intValue();
//	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public void setOrderBy(String property, OrderType orderType) {
		orderByList.clear();
		if (property != null && !"".equals(property)) {
			orderByList.add(this.new OrderBy(property, orderType));
		}
	}

	public void addOrderBy(String property, OrderType orderType) {
		if (property != null && !"".equals(property)) {
			orderByList.add(this.new OrderBy(property, orderType));
		}
	}

	public void setOrderBy(String props, String types) {
		orderByList.clear();
		if (props != null && !"".equals(props) && types != null && !"".equals(types)) {
			String[] prop = props.split(",");
			String[] type = types.split(",");
			for (int i = 0; i < prop.length; i++) {
				orderByList.add(this.new OrderBy(prop[i], OrderType.valueOf(type[i].toLowerCase())));
			}
		}
	}

	public void clearOrderBy() {
		orderByList.clear();
	}

	public List<OrderBy> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<OrderBy> orderByList) {
		if (orderByList != null) {
			this.orderByList = orderByList;
		}
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