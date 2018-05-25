package cn.itcast.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.bookstore.order.dao.OrderDao;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao dao = new OrderDao();

	/*
	 * 添加订单 处理事务
	 */
	public void add(Order order) {
		try {
			JdbcUtils.beginTransaction();

			dao.addOrder(order);
			dao.addOrderItemList(order.getOrderItemList());

			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}

			throw new RuntimeException(e);
		}
	}

	/*
	 * 我的订单
	 */
	public List<Order> myOrders(String uid) {
		return dao.findByUid(uid);
	}

	/*
	 * 加载订单
	 */
	public Order load(String oid) {
		return dao.load(oid);
	}

	/*
	 * 确认收货
	 */
	public void confirm(String oid) throws OrderException {
		int state = dao.getStateByOid(oid);

		if (state != 3) {
			throw new OrderException("您还没有付款");
		}
		// 修改订单状态
		dao.updateState(oid, 4);
	}

	/*
	 * 支付
	 */
	public void zhiFu(String oid) {
		int state = dao.getStateByOid(oid);

		if (state == 1) {
			dao.updateState(oid, 2);
			/*
			 * 如果有加积分业务可以在这儿加
			 */
		}
	}

	/*
	 * 查看所有订单
	 */
	public List<Order> findAll() {
		return dao.findAll();
	}

	/*
	 * 按订单状态查找
	 */
	public List<Order> findByState(int state) {
		return dao.findByState(state);
	}

	/*
	 * 发货
	 */
	public void faHuo(String oid) {
		int state = dao.getStateByOid(oid);

		if (state == 2) {
			dao.updateState(oid, 3);
		}
	}
}
