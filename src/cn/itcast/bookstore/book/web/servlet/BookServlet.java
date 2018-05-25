package cn.itcast.bookstore.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService service = new BookService();
	/*
	 * 查询所有图书
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("bookList", service.findAll());
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按分类查找
	 */
	public String findByCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("bookList", service.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按bid加载
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取bid
		String bid = request.getParameter("bid");
		request.setAttribute("book", service.load(bid));
		return "f:/jsps/book/desc.jsp";
	}
	
}	
