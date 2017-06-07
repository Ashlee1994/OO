package cn.itcast.goods.admin.admin.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.domain.CopyBook;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.goods.seller.order.web.servlet.CopySellerOrder;
import cn.itcast.goods.seller.order.web.servlet.SellerOrder;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class AdminDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过管理员登录名和登录密码查询
	 * @param adminname
	 * @param adminpwd
	 * @return
	 * @throws SQLException
	 */
	public Admin find(String adminname, String adminpwd) throws SQLException {
		String sql = "select * from t_admin where adminname=? and adminpwd=?";
		return qr.query(sql, new BeanHandler<Admin>(Admin.class), adminname, adminpwd);
	}
	
	public PageBean<User> allUser(int pc) throws SQLException {
		/*
		 * 1. 得到ps
		 * 2. 得到tr
		 * 3. 得到beanList
		 * 4. 创建PageBean，返回
		 */
		/*
		 * 1. 得到ps
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;//每页记录数
		/*
		 * 2. 通过exprList来生成where子句
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1"); 
		List<Object> params = new ArrayList<Object>();//SQL中有问号，它是对应问号的值

		/*
		 * 3. 总记录数 
		 */
		String sql = "select count(*) from t_user" + whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();//得到了总记录数
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_user";
		
		/*List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class), 
				params.toArray());*/
		
		List<User> beanList = qr.query(sql, new BeanListHandler<User>(User.class));
		
		
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<User> pb = new PageBean<User>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}

	public PageBean<SellerOrder> allArbitration(int pc) throws SQLException {
		/*
		 * 1. 得到ps
		 * 2. 得到tr
		 * 3. 得到beanList
		 * 4. 创建PageBean，返回
		 */
		/*
		 * 1. 得到ps
		 */
		
		int ps = PageConstants.ORDER_PAGE_SIZE;//每页记录数
		

		/*
		 * 3. 总记录数 
		 */
		String sql = "select count(*) from t_seller_order where status=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), 11);
		int tr = number.intValue();//得到了总记录数
		
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_seller_order where status=? order by ordertime";
		
		/*List<SellerOrder> beanList = qr.query(sql, new BeanListHandler<SellerOrder>(SellerOrder.class), 
				params.toArray());*/
		
		List<CopySellerOrder> copySellerList = qr.query(sql, new BeanListHandler<CopySellerOrder>(CopySellerOrder.class), 
				11);
		
		List<SellerOrder> beanList = new ArrayList<SellerOrder>();
		
		String sql2 = "select * from t_user where uid=?";
		String sql3 = "select * from t_book where bid=?";
		for(CopySellerOrder copySellerOrder : copySellerList) {	
			User seller = qr.query(sql2, new BeanHandler<User>(User.class),copySellerOrder.getSeller());
			User buyer = qr.query(sql2, new BeanHandler<User>(User.class),copySellerOrder.getBuyer());
			System.out.println("Normal !");
			CopyBook copyBook= qr.query(sql3, new BeanHandler<CopyBook>(CopyBook.class),copySellerOrder.getBid());
			Book book = new Book();
			SellerOrder sellerOrder = new SellerOrder();
			sellerOrder.setOid(copySellerOrder.getOid());
			sellerOrder.setOrdertime(copySellerOrder.getOrdertime());
			sellerOrder.setQuantity(copySellerOrder.getQuantity());
			sellerOrder.setCurrPrice(copySellerOrder.getCurrPrice());
			sellerOrder.setTotal(copySellerOrder.getTotal());
			sellerOrder.setStatus(copySellerOrder.getStatus());
			sellerOrder.setAddress(copySellerOrder.getAddress());
			sellerOrder.setSeller(seller);
			sellerOrder.setBuyer(buyer);
			
			System.out.println("图书编号："+copyBook.getBid());
			book.setBid(copyBook.getBid());
			book.setExist(copyBook.getExist());
			book.setBname(copyBook.getBname());
			book.setAuthor(copyBook.getAuthor());
			book.setPrice(copyBook.getPrice());
			book.setCurrPrice(copyBook.getCurrPrice());
			book.setDiscount(copyBook.getDiscount());
			book.setPress(copyBook.getPress());
			book.setPublishtime(copyBook.getPublishtime());
			book.setSeller(seller);//设置图书所有者
			book.setEdition(copyBook.getEdition());
			book.setPageNum(copyBook.getPageNum());
			book.setWordNum(copyBook.getWordNum());
			book.setPrinttime(copyBook.getPrinttime());
			book.setBooksize(copyBook.getBooksize());
			book.setPaper(copyBook.getPaper());
			book.setCategory(copyBook.getCategory());
			book.setImage_w(copyBook.getImage_w());
			book.setImage_b(copyBook.getImage_b());
			
			sellerOrder.setBook(book);
			
			beanList.add(sellerOrder);
		}
		
		
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<SellerOrder> pb = new PageBean<SellerOrder>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}
	
	public void reduceSellerCredit(String sellerOid) throws SQLException {
		String sql = "select seller from t_seller_order where oid=?";
		String seller = (String)qr.query(sql, new ScalarHandler(), sellerOid);
		
		sql = "update t_user set credit=credit-20 where uid=?";
		qr.update(sql, seller);
	}
	
	public void reduceBuyerCredit(String sellerOid) throws SQLException {
		String sql = "select buyer from t_seller_order where oid=?";
		String buyer = (String)qr.query(sql, new ScalarHandler(), sellerOid);
		
		sql = "update t_user set credit=credit-10 where uid=?";
		qr.update(sql, buyer);
	}
}
