package cn.itcast.bookstore.order.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.bookstore.order.domain.OrderItem;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	/*
	 * 添加订单
	 */
	public void addOrder(Order order) {
		try {
			String sql = "insert into orders values (?,?,?,?,?,?)";
			Object[] params = { order.getOid(),
					new Timestamp(order.getOrdertime().getTime()),
					order.getTotal(), order.getState(),
					order.getOwner().getUid(), order.getAddress()};
			qr.update(sql, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 添加订单条目
	 */
	public void addOrderItemList(List<OrderItem> orderItemList) {
		try {
			String sql = "insert into orderitem values (?,?,?,?,?)";
			Object[][] params = new Object[orderItemList.size()][];

			for (int i = 0; i < orderItemList.size(); i++) {
				OrderItem item = orderItemList.get(i);
				params[i] = new Object[] { item.getIid(), item.getCount(),
						item.getSubtotal(), item.getOrder().getOid(),
						item.getBook().getBid() };
			}
			qr.batch(sql, params);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 按uid查询订单
	 */
	public List<Order> findByUid(String uid) {
		try {
			String sql = "select * from orders where uid = ?";
			/*
			 * 得到用户的所有订单
			 */
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class), uid);
			/*
			 * 为每个订单加载条目
			 */
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void loadOrderItems(Order order) {
		try {
			/*
			 * 多表查询
			 */
			String sql = "select * from orderitem i join book b on i.bid = b.bid and i.oid = ?";
			/*
			 *  
			 */
			List<Map<String, Object>> mapList = qr.query(sql,
					new MapListHandler(), order.getOid());
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 得到List<OrderItem>
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();

		for (Map<String, Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}

		return orderItemList;
	}

	/*
	 * 得到OrderItem
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/*
	 * 加载订单
	 */
	public Order load(String oid) {
		try {
			try {
				String sql = "select * from orders where oid = ?";
				/*
				 * 得到用户的订单
				 */
				Order order = qr.query(sql,
						new BeanHandler<Order>(Order.class), oid);
				/*
				 * 为订单加载条目
				 */
				loadOrderItems(order);

				return order;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 获取订单状态
	 */
	public int getStateByOid(String oid) {
		try {
			String sql = "select state from orders where oid = ?";
			return (Integer) qr.query(sql, new ScalarHandler(), oid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 修改订单状态
	 */
	public void updateState(String oid, int state) {
		try {
			String sql = "update orders set state = ? where oid = ?";
			qr.update(sql, state, oid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 查看所有订单
	 */
	public List<Order> findAll() {
		try {
			String sql = "select * from orders";
			/*
			 * 得到所有订单
			 */
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class));
			/*
			 * 为每个订单加载条目
			 */
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/*
	 * 按订单状态查找
	 */
	public List<Order> findByState(int state) {
		try {
			String sql = "select * from orders where state = ?";
			/*
			 * 得到该状态下所有订单
			 */
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
					Order.class), state);
			/*
			 * 为每个订单加载条目
			 */
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
