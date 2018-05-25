<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function _change(){
			var vcode =  document.getElementById("vid");
			vcode.src = "<c:url value='/VerifyCodeServlet'/>?time=" + new Date().getTime();
		};
	</script>
  </head>
  
  <body>
  <h1>登录</h1>
<p style="color: red; font-weight: 900">${msg }</p>
<form action="<c:url value='/UserServlet'/>" method="post" target="_top">
	<input type="hidden" name="method" value="login"/>
	用户名：<input type="text" name="username" value="${form.username }"/>
	<span style="color: red; font-weight: 900">${errors.username }</span>
	<br/>
	密　码：<input type="password" name="password" value="${form.password }"/>
	<span style="color: red; font-weight: 900">${errors.password }</span>
	<br/>
	验证码：<input type="text" name="vcode"/>
	<img id="vid" src="<c:url value="/VerifyCodeServlet"/>"/>
	<a href="javascript:_change()">看不清,换一张</a>
	<span style="color: red; font-weight: 900">${errors.verify }</span>
	<br/>
	<input type="submit" value="登录"/>
</form>
  </body>
</html>
