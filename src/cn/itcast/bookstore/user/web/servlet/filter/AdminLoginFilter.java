package cn.itcast.bookstore.user.web.servlet.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AdminLoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String admin = (String) httpServletRequest.getSession().getAttribute(
				"admin");
		if (admin != null && admin.equals("admin")) {
			chain.doFilter(httpServletRequest, response);
		}else{
			httpServletRequest.setAttribute("msg", "请登录");
			httpServletRequest.getRequestDispatcher("/adminjsps/login.jsp").forward(httpServletRequest, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void destroy() {

	}

}
