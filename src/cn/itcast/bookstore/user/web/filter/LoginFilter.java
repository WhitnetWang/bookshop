package cn.itcast.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.bookstore.user.domain.User;

public class LoginFilter implements Filter {

	public void destroy() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		User user = (User) httpServletRequest.getSession().getAttribute(
				"session_user");
		if (user != null) {
			chain.doFilter(request, response);
		} else {
			httpServletRequest.setAttribute("msg", "您还没有登录");
			httpServletRequest.getRequestDispatcher("/jsps/user/login.jsp")
					.forward(httpServletRequest, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
