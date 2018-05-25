package cn.itcast.bookstore.user.web.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class AdminUserServlet extends BaseServlet {
	/*
	 * 管理员登录
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String adminName = request.getParameter("adminName");
		String adminPass = request.getParameter("adminPass");
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 校验帐号
		 */
		if (adminName == null || adminName.trim().isEmpty()) {
			errors.put("adminName", "帐号不能为空");
		} else if (!adminName.equals("admin")) {
			request.setAttribute("msg", "用户名或密码错误");
			return "f:/adminjsps/login.jsp";
		}
		/*
		 * 校验密码
		 */
		if (adminPass == null || adminPass.trim().isEmpty()) {
			errors.put("adminPass", "密码不能为空");
		} else if (!adminPass.equals("123456")) {
			request.setAttribute("msg", "用户名或密码错误");
			return "f:/adminjsps/login.jsp";
		}

		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			return "f:/adminjsps/login.jsp";
		}
		
		request.getSession().setAttribute("admin", "admin");
		
		return "f:/adminjsps/admin/index.jsp";
	}
}
