<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>注册</title>
    
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
		// 创建异步对象
		function createXMLHttpRequest() {
			try {
				return new XMLHttpRequest();//大多数浏览器
			} catch (e) {
				try {
					return ActvieXObject("Msxml2.XMLHTTP");//IE6.0
				} catch (e) {
					try {
						return ActvieXObject("Microsoft.XMLHTTP");//IE5.5及更早版本	
					} catch (e) {
						alert("哥们儿，您用的是什么浏览器啊？");
						throw e;
					}
				}
			}
		}
		window.onload = function() {//文档加载完毕后执行
			var username = document.getElementById("uid");
			username.onblur = function() {//给按钮的点击事件注册监听
				/*
				ajax四步操作，得到服务器的响应
				把响应结果显示到h1元素中
				*/
				/*
				1. 得到异步对象 
				*/
				var xmlHttp = createXMLHttpRequest();
				/*
				2. 打开与服务器的连接
				  * 指定请求方式
				  * 指定请求的URL
				  * 指定是否为异步请求
				*/
				/************修改open方法，指定请求方式为POST**************/
				xmlHttp.open("POSt", "<c:url value='/AsyncServlet'/>", true);
				/************设置请求头：Content-Type************/
				xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				/*
				3. 发送请求
				*/
				/**********发送时指定请求体***********/
				xmlHttp.send("username=" + username.value);//GET请求没有请求体，但也要给出null，不然FireFox可能会不能发送！
				/*
				4. 给异步对象的onreadystatechange事件注册监听器
				*/
				xmlHttp.onreadystatechange = function() {//当xmlHttp的状态发生变化时执行
					// 双重判断：xmlHttp的状态为4（服务器响应结束），以及服务器响应的状态码为200（响应成功）
					if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
						// 获取服务器的响应结束
						var text = xmlHttp.responseText;
						var span = document.getElementById("sid");
						span.innerHTML = "<font>" + text + "</font>";
					}
				};
			};
		};
	</script>
  </head>
  
  <body>
  <h1>注册</h1>
<p style="color: red; font-weight: 900">${msg }</p>
<form action="<c:url value='/UserServlet'/>" method="post">
	<input type="hidden" name="method" value="regist"/>
	用户名：<input id="uid" type="text" name="username" value="${form.username }"/>
	<span id="sid" style="color: red; font-weight: 900">${errors.username }</span>
	<br/>
	密　码：<input type="password" name="password" value="${form.password }"/>
	<span style="color: red; font-weight: 900">${errors.password }</span>
	<br/>
	邮　箱：<input type="text" name="email" value="${form.email }"/>
	<span style="color: red; font-weight: 900">${errors.email }</span>
	<br/>
	验证码：<input type="text" name="vcode"/>
	<img id="vid" src="<c:url value="/VerifyCodeServlet"/>"/>
	<a href="javascript:_change()">看不清,换一张</a>
	<span style="color: red; font-weight: 900">${errors.verify }</span>
	<br/>
	<input type="submit" value="注册"/>
</form>
  </body>
</html>
