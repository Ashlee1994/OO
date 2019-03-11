package cn.itcast.goods.seller.order.web.servlet;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class SellerOrderServlet extends BaseServlet {
	private SellerOrderService sellerOrderService = new SellerOrderService();
	private OrderService orderService = new OrderService();

	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch(RuntimeException e) {}
		}
		return pc;
	}
	
	/**
	 * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
	 * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，如果不存在那就不用截取。
		 */
		int index = url.lastIndexOf("&pc=");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 按状态查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取链接参数：status
		 */
		int status = Integer.parseInt(req.getParameter("status"));
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		
		PageBean<SellerOrder> pb = sellerOrderService.findByStatus(user.getUid(),status, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/order/sellerList.jsp";
	}
	
	/**
	 * 查看订单详细信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String oid = req.getParameter("oid");
		SellerOrder sellerOrder = sellerOrderService.load(oid);
		req.setAttribute("sellerOrder", sellerOrder);
		String btn = req.getParameter("btn");//btn说明了用户点击哪个超链接来访问本方法的
		req.setAttribute("btn", btn);
		return "/jsps/order/sellerDesc.jsp";
	}
	
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = sellerOrderService.findStatus(oid);
		if(status != 1) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能取消！");
			return "f:/adminjsps/msg.jsp";
		}
		sellerOrderService.updateStatus(oid, 5);//设置状态为取消！
		orderService.updateStatusBySeller(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已取消，您不后悔吗！");
		return "f:/jsps/msg.jsp";		
	}
	
	/**
	 * 我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 从当前session中获取User
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		System.out.println("1 .. Normal !");
		PageBean<SellerOrder> pb = sellerOrderService.myOrders(user.getUid(), pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/order/sellerList.jsp";
	}
	
	/**
	 * 我的书籍
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myBooks(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 从当前session中获取User
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = sellerOrderService.myBooks(user.getUid(), pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/sellerList.jsp";
	}
	
	
	/**
	 * 发货功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = sellerOrderService.findStatus(oid);
		if(status != 2) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能发货！");
			return "f:/jsps/msg.jsp";
		}
		sellerOrderService.updateStatus(oid, 3);//设置状态为发货
		orderService.updateStatusBySeller(oid, 3);//设置状态为发货
		req.setAttribute("code", "success");
		req.setAttribute("msg", "该订单已发货，可以处理下一单啦！");
		return "f:/jsps/msg.jsp";		
	}
	/**
	 * 同意退货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String agreeRefund(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = sellerOrderService.findStatus(oid);
		if(status != 6) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能同意退货申请！");
			return "f:/jsps/msg.jsp";
		}
		
		sellerOrderService.updateStatus(oid, 7);//设置状态为同意退货
		orderService.updateOrderItemStatusBySellerOrder(oid,2); //设置状态为同意退货
		
		req.setAttribute("code", "success");
		req.setAttribute("msg", "已同意该订单退货，可以处理下一单啦！");
		return "f:/jsps/msg.jsp";	
	}
	/**
	 * 拒绝退货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String refuseRefund(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = sellerOrderService.findStatus(oid);
		if(status != 6) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能拒绝退货申请！");
			return "f:/jsps/msg.jsp";
		}
		sellerOrderService.updateStatus(oid, 10);//设置状态为拒绝退货
		orderService.updateOrderItemStatusBySellerOrder(oid,5); //设置状态为拒绝退货
		
		req.setAttribute("code", "success");
		req.setAttribute("msg", "已拒绝该订单退货，可以处理下一单啦！");
		return "f:/jsps/msg.jsp";			
	}
	/**
	 * 退货收货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String receiving(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = sellerOrderService.findStatus(oid);
		if(status != 8) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能收货！");
			return "f:/jsps/msg.jsp";
		}
		sellerOrderService.updateStatus(oid, 9);//设置状态为退货成功
		orderService.updateOrderItemStatusBySellerOrder(oid,4); //设置状态为退货成功
		
		req.setAttribute("code", "success");
		req.setAttribute("msg", "已收到退货，可以处理下一单啦！");
		return "f:/jsps/msg.jsp";			
	}
}
