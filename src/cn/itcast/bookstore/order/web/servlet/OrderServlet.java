package cn.itcast.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.cart.domain.CartItem;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.bookstore.order.domain.OrderItem;
import cn.itcast.bookstore.order.service.OrderService;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService service = new OrderService();

	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		// 创建order对象
		Order order = new Order();
		// 设置订单oid
		order.setOid(CommonUtils.uuid());
		// 设置订单日期
		order.setOrdertime(new Date());
		User user = (User) request.getSession().getAttribute("session_user");
		// 设置下定者
		order.setOwner(user);
		// 设置订单状态
		order.setState(1);
		// 设置订单的合计
		order.setTotal(cart.getTotal());

		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		// 遍历所有的购物车条目
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem item = new OrderItem();
			// 设置订单条目
			item.setIid(CommonUtils.uuid());
			// 设置订单条目的数目
			item.setCount(cartItem.getCount());
			// 设置订单条目的小计
			item.setSubtotal(cartItem.getSubtotal());
			// 设置订单条目的书
			item.setBook(cartItem.getBook());
			// 设置所属订单
			item.setOrder(order);

			orderItemList.add(item);
		}
		// 把所有订单条目放到订单中
		order.setOrderItemList(orderItemList);
		/*
		 * 清空购物车
		 */
		cart.clear();
		// 调用service层的方法
		service.add(order);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}

	/*
	 * 查询我的订单
	 */
	public String myOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> OrderList = service.myOrders(user.getUid());
		request.setAttribute("orderList", OrderList);
		return "f:/jsps/order/list.jsp";
	}

	/*
	 * 加载订单
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("order", service.load(request.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}

	/*
	 * 确认收货
	 */
	public String confirm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String oid = request.getParameter("oid");
			service.confirm(oid);
			request.setAttribute("msg", "恭喜,确认收货,交易完成");
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
		}

		return "f:/jsps/msg.jsp";
	}

	/*
	 * 修改订单状态为2表示已付款
	 */
	public String back(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		/*
		 * 校验访问者是否为易宝
		 */
		String hmac = request.getParameter("hmac");
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);
		String keyValue = props.getProperty("keyValue");
		// 校验
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
				r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
				keyValue);
		if(!bool){
			request.setAttribute("msg", "非法访问");
			return "f:/jsps/msg.jsp";
		}
		//有可能对数据库进行操作也有可能不操作
		service.zhiFu(r6_Order);
		
		//判断回调方式
		if(r9_BType.equals("2")){
			response.getWriter().write("success");
		}
		
		request.setAttribute("msg", "支付成功,等待发货");
		return "f:/jsps/msg.jsp";
	}

	/*
	 * 支付去银行
	 */
	public String zhiFu(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 准备13个参数
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);

		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		String p3_Amt = "0.01"; // 支付金额
		String p4_Cur = "CNY";// 人名币
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";
		/*
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&p0_Cmd=").append(p0_Cmd);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		response.sendRedirect(url.toString());
		return null;
	}

}
