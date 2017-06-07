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
<p class="pLink">
  <a href="<c:url value='/SellerBookServlet?method=addPre'/>">添加图书</a>  
</p>
<div class="divMain">
	<div class="title">
		<div style="margin-top:7px;">
			<span style="margin-left: 150px;margin-right: 280px;">图书信息</span>
			<span style="margin-left: 40px;margin-right: 100px;">库存</span>
			<span style="margin-left: 50px;margin-right: 53px;">库存状态</span>
			<span style="margin-left: 100px;">操作</span>
		</div>
	</div>
	<br/>
	<table align="center" border="0" width="100%" cellpadding="0" cellspacing="0">
<c:forEach items="${pb.beanList }" var="book" >	
	<c:if test="${book.exist == true}">
		<tr class="tt">
			<td width="320px">图书编号：<a  href="<c:url value='/BookServlet?method=load&bid=${book.bid }'/>">${book.bid }</a></td>
			<td width="180px"> 书名：${book.bname }</td>
			<td width="178px">作者：${book.author} &nbsp;</td>
			<td width="205px">&nbsp;</td>
			<td  width="65px">&nbsp;</td>
		</tr>	

		<tr style="padding-top: 10px; padding-bottom: 10px;">
			<td colspan="2">
				  <img border="0" width="70" src="<c:url value='/${book.image_b }'/>"/>
	        </td>
			<td style="padding-left: 0">
				<span class="price_t">100</span>
			</td>
			<td>
			库存充足
			</td>
						
			<td colspan="2">
			  <a href="<c:url value='/BookServlet?method=load&bid=${book.bid }'/>">查看</a><br/> 
 <c:if test="${book.exist== true }">
		      <a href="<c:url value='/SellerBookServlet?method=sellerDelete&bid=${book.bid }&btn=delete'/>">删除图书</a>	<br/>	
</c:if>
<c:if test="${book.exist== true }" >	
			  <a href="<c:url value='/SellerBookServlet?method=edit&bid=${book.bid }&btn=edit'/>">修改图书</a><br/>			
</c:if>	
			</td>
		</tr>
		</c:if>
</c:forEach>

	</table>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>
  </body>
</html>
