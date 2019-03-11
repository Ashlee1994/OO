<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单详细</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/order/desc.css'/>">
  </head>
  
<body>
	<div class="divOrder">
		<span>订单号：${order.oid }
<c:choose>
	<c:when test="${order.status eq 1 }">(等待付款)</c:when>
	<c:when test="${order.status eq 2 }">(准备发货)</c:when>
	<c:when test="${order.status eq 3 }">(等待确认)</c:when>
	<c:when test="${order.status eq 4 }">(交易成功)</c:when>
	<c:when test="${order.status eq 5 }">(已取消)</c:when>
</c:choose>	
		　　　下单时间：${order.ordertime }</span>
	</div>
	<div class="divContent">
		<div class="div2">
			<dl>
				<dt>收货人信息</dt>
				<dd>${order.address }</dd>
			</dl>
		</div>
		<div class="div2">
			<dl>
				<dt>商品清单</dt>
				<dd>
					<table cellpadding="0" cellspacing="0">
						<tr>
							<th class="tt">商品名称</th>
							<th class="tt" align="left">单价</th>
							<th class="tt" align="left">数量</th>
							<th class="tt" align="left">小计</th>
							<th class="tt" align="left">状态</th>
							<th class="tt" align="left">操作</th>
						</tr>


<c:forEach items="${order.orderItemList }" var="item">
						<tr style="padding-top: 20px; padding-bottom: 20px;">
							<td class="td" width="400px">
								<div class="bookname">
								  <img align="middle" width="70" src="<c:url value='/${item.book.image_b }'/>"/>
								  <a href="<c:url value='/BookServlet?method=load&bid=${item.book.bid }'/>">${item.book.bname }</a>
								</div>
							</td>
							<td class="td" >
								<span>&yen;${item.book.currPrice }</span>
							</td>
							<td class="td">
								<span>${item.quantity }</span>
							</td>
							<td class="td">
								<span>&yen;${item.subtotal }</span>
							</td>		
							<c:if test="${item.status eq 0}">
   		 						<td class="td">
									<span>正常</span>
								</td>	
								<td class="td">
									<a href="<c:url value='/OrderServlet?method=refund&orderItemId=${item.orderItemId }'/>">退货</a>
								</td>	
							</c:if>
							<c:if test="${item.status eq 1}">
   		 						<td class="td">
									<span>申请退货中</span>
								</td>	
								<td class="td">
									<span>无</span>
								</td>	
							</c:if>
							<c:if test="${item.status eq 2}">
   		 						<td class="td">
									<span>同意退货</span>
								</td>	
								<td class="td">
									<a href="<c:url value='/OrderServlet?method=delivering&orderItemId=${item.orderItemId }'/>">发货</a>
								</td>	
							</c:if>
							<c:if test="${item.status eq 3}">
   		 						<td class="td">
									<span>发货中</span>
								</td>	
								<td class="td">
									<span>无</span>
								</td>	
							</c:if>
							<c:if test="${item.status eq 4}">
   		 						<td class="td">
									<span>退货成功</span>
								</td>	
								<td class="td">
									<span>无</span>
								</td>	
							</c:if>
							<c:if test="${item.status eq 5}">
   		 						<td class="td">
									<span>卖家拒绝退货</span>
								</td>	
								<td class="td">
									<a href="<c:url value='/OrderServlet?method=arbitration&orderItemId=${item.orderItemId }'/>">申请仲裁</a>
								</td>	
							</c:if>
							<c:if test="${item.status eq 6}">
   		 						<td class="td">
									<span>申请仲裁</span>
								</td>	
								<td class="td">
									<span>无</span>
								</td>	
							</c:if>
							<c:if test="${item.status eq 7}">
   		 						<td class="td">
									<span>仲裁失败</span>
								</td>	
								<td class="td">
									<span>无</span>
								</td>	
							</c:if>
						</tr>
</c:forEach>


					</table>
				</dd>
			</dl>
		</div>
		<div style="margin: 10px 10px 10px 550px;">
			<span style="font-weight: 900; font-size: 15px;">合计金额：</span>
			<span class="price_t">&yen;${order.total }</span><br/>
<c:if test="${order.status eq 1 }">
	<a href="<c:url value='/OrderServlet?method=paymentPre&oid=${order.oid }'/>" class="pay"></a><br/>
</c:if>
<c:if test="${order.status eq 1 and btn eq 'cancel'}">
    <a id="cancel" href="<c:url value='/OrderServlet?method=cancel&oid=${order.oid }'/>">取消订单</a><br/>
</c:if>
<c:if test="${order.status eq 3 and btn eq 'confirm'}">
	<a id="confirm" href="<c:url value='/OrderServlet?method=confirm&oid=${order.oid }'/>">确认收货</a><br/>
</c:if>	
		</div>
	</div>
</body>
</html>

