package cn.itcast.goods.admin.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.admin.admin.service.AdminService;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.seller.order.web.servlet.SellerOrder;
import cn.itcast.goods.seller.order.web.servlet.SellerOrderService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	private AdminService adminService = new AdminService();
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
	 * 登录功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到Admin
		 */
		Admin form = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(form);
		if(admin == null) {
			req.setAttribute("msg", "用户名或密码错误！");
			return "/adminjsps/login.jsp";
		}
		req.getSession().setAttribute("admin", admin);
		return "r:/adminjsps/admin/index.jsp";
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
		
		PageBean<Book> pb = adminService.myBooks(pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/sellerList.jsp";
	}
	
	public String allUser(HttpServletRequest req, HttpServletResponse resp)
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
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		
		PageBean<User> pb = adminService.allUser(pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb); 
		return "f:/adminjsps/admin/user/userList.jsp";
	}
	//仲裁管理
	public String arbitration(HttpServletRequest req, HttpServletResponse resp)
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
		PageBean<SellerOrder> pb = adminService.allArbitration(pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/arbitration/arbitrationList.jsp";
	}
	//仲裁：同意退货
	public String agreeRefund(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String oid = req.getParameter("oid");
		
		sellerOrderService.updateStatus(oid, 7);//设置状态为同意退货
		orderService.updateOrderItemStatusBySellerOrder(oid,2); //设置状态为同意退货
		adminService.reduceSellerCredit(oid);
		
		req.setAttribute("code", "success");
		req.setAttribute("msg", "仲裁结果：同意退货！");
		return "f:/jsps/msg.jsp";
	} 
	//仲裁：拒绝退货
		public String refuseRefund(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
			String oid = req.getParameter("oid");
			
			sellerOrderService.updateStatus(oid, 12);//设置状态为仲裁失败
			orderService.updateOrderItemStatusBySellerOrder(oid,7); //设置状态为仲裁失败
			adminService.reduceBuyerCredit(oid);
			
			req.setAttribute("code", "success");
			req.setAttribute("msg", "仲裁结果：退货失败！");
			return "f:/jsps/msg.jsp";
		}
}
