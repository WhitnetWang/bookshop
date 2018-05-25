package cn.itcast.bookstore.order.web.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.order.service.OrderService;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService service = new OrderService();
	/*
	 * 查看所有订单
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("orderList", service.findAll());
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/*
	 * 按状态查找
	 */
	public String findByState(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int state = Integer.parseInt(request.getParameter("state"));
		request.setAttribute("orderList", service.findByState(state));
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/*
	 * 发货
	 */
	public String faHuo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid = request.getParameter("oid");
		service.faHuo(oid);
		request.setAttribute("msg", "发货成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	
}
