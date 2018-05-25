package cn.itcast.bookstore.user.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.user.service.UserService;

public class AsyncServlet extends HttpServlet {
	private UserService userService = new UserService();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		String username = request.getParameter("username");
		
		if(username == null || username.trim().isEmpty()){
			return;
		}else if(username.length() < 3 || username.length() > 10){
			return;
		}
		
		boolean flag = userService.validate(username);
		
		if(flag){
			response.getWriter().write("该用户名已存在");
		}else{
			response.getWriter().write("该用户名可用");
		}
		
	}

}
