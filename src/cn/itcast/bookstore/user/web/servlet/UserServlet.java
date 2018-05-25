package cn.itcast.bookstore.user.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.bookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService service = new UserService();

	/*
	 * 激活邮件
	 */
	public String active(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");

		try {
			service.active(code);
			request.setAttribute("msg", "恭喜，激活成功请登录");
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	/*
	 * 注册
	 */
	public String regist(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 补全
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		/*
		 * 校验表单数据
		 */
		String username = form.getUsername();
		// 用于装载错误信息
		Map<String, String> errors = new HashMap<String, String>();
		// 校验用户名
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名的长度必须为3到10");
		}
		// 校验密码
		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码的长度必须为3到10");
		}
		// 校验邮箱
		String email = form.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "email不能为空");
		} else if (!email.matches("\\w+@\\w+\\.(com|org|cn)")) {
			errors.put("email", "email格式错误");
		}
		// 校验验证码
		String vcode = (String) request.getSession().getAttribute(
				"session_vcode");
		String code = request.getParameter("vcode");

		if (code == null || code.trim().isEmpty()) {
			errors.put("verify", "验证码不能为空");
		} else if (!vcode.equalsIgnoreCase(code)) {
			errors.put("verify", "验证码错误");
		}

		// 如果存在错误就转发回去
		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 调用service层的方法
		 */
		try {
			service.regist(form);
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 发邮件
		 */
		try {
			// 准备配置文件
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader()
					.getResourceAsStream("email.properties"));
			// 得到与服务器的会话
			Session session = Session.getInstance(props);
			// 创建邮件
			MimeMessage message = new MimeMessage(session);
			// 设置发件人
			message.setFrom(new InternetAddress(props.getProperty("from")));
			message.setSubject(props.getProperty("subject"));
			message.setContent(
					"<a href='http://localhost:8080" + request.getContextPath()
							+ "/UserServlet?method=active&code="
							+ form.getCode() + "'>点击激活</a>",
					"text/html;charset=utf-8");
			// 得到发送器
			Transport transport = session.getTransport();
			// 打开连接
			transport.connect(props.getProperty("username"),
					props.getProperty("password"));
			// 发送邮件
			transport.sendMessage(message,
					InternetAddress.parse(form.getEmail()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		request.setAttribute("msg", "恭喜注册成功请到邮箱激活！");
		return "f:/jsps/msg.jsp";

	}

	/*
	 * 登录
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		/*
		 * 校验用户名和密码
		 */
		Map<String, String> errors = new HashMap<String, String>();
		// 校验用户名
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名的长度必须为3到10");
		}
		// 校验密码
		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码的长度必须为3到10");
		}

		// 校验验证码
		String vcode = (String) request.getSession().getAttribute(
				"session_vcode");
		String code = request.getParameter("vcode");

		if (code == null || code.trim().isEmpty()) {
			errors.put("verify", "验证码不能为空");
		} else if (!vcode.equalsIgnoreCase(code)) {
			errors.put("verify", "验证码错误");
		}

		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}

		/*
		 * 调用service层的方法
		 */
		try {
			User user = service.login(form);
			// 为了安全
			request.getSession().invalidate();
			// 再获取session
			request.getSession().setAttribute("session_user", user);
			// 加入购物车
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
	}

	/*
	 * 退出
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
}
