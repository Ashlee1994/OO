<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>书籍列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />
    <script type="text/javascript" src="<c:url value='/jsps/pager/pager.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/book/sellerList.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/css.css'/>" />

  </head>
  
  <body>

<div class="divMain">
	<div class="title">
		<div style="margin-top:7px;">
			<span style="margin-left: 150px;margin-right: 280px;">用户信息&nbsp;&nbsp;</span>
			<span style="margin-left: 40px;margin-right: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<span style="margin-left: 50px;margin-right: 53px;">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<span style="margin-left: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;操作</span>
		</div>
	</div>
	<br/>
	<table align="center" border="0" width="100%" cellpadding="0" cellspacing="0">
<c:forEach items="${pb.beanList }" var="user" >	
		<tr class="tt">
			<td width="400px">用户编号：<a  href="<c:url value='/BookServlet?method=load&bid=${user.uid}'/>">${user.uid }</a></td>
			<td width="200px"> 用户名：${user.loginname }</td>
			<td width="100px"> &nbsp;</td>
			<td width="205px">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>	

		<tr style="padding-top: 10px; padding-bottom: 10px;">
			<td colspan="2">
				 
	        </td>
			<td style="padding-left: 0">
				
			</td>
			<td>
			
			</td>
						
			<td>
			<a href="<c:url value='/BookServlet?method=load'/>">查看</a><br/>
<c>
				<a href="<c:url value='/SellerBookServlet?method=sellerDelete&btn=delete'/>">删除用户</a><br/>						
</c>		

			</td>
		</tr>
</c:forEach>

	</table>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>
  </body>
</html>
