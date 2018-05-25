package cn.itcast.bookstore.user.web.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.vcode.utils.VerifyCode;

public class VerifyCodeServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		VerifyCode verifyCode = new VerifyCode();
		
		BufferedImage image = verifyCode.getImage();
		
		request.getSession().setAttribute("session_vcode", verifyCode.getText());
		
		VerifyCode.output(image, response.getOutputStream());
	}

}
