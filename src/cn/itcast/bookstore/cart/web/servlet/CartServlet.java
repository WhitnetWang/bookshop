package cn.itcast.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet {
	/*
	 * 添加购物条目
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart == null){
			return "f:/jsps/cart/list.jsp";
		}
		// 得到书
		String bid = request.getParameter("bid");
		Book book = new BookService().load(bid);
		// 获取数目
		int count = Integer.parseInt(request.getParameter("count"));
		// 创建一个条目
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}

	/*
	 * 清空购物车
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}

	/*
	 * 删除条目
	 */
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String bid = request.getParameter("bid");
		// 得到车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
