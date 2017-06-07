package cn.itcast.goods.admin.admin.service;

import java.sql.SQLException;

import cn.itcast.goods.admin.admin.dao.AdminDao;
import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.seller.order.web.servlet.SellerOrder;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.JdbcUtils;

public class AdminService {
	private AdminDao adminDao = new AdminDao();
	private BookDao bookDao = new BookDao();
	/**
	 * 登录功能
	 * @param admin
	 * @return
	 */
	public Admin login(Admin admin) {
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 我的书籍
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> myBooks(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Book> pb = bookDao.findAllBook(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	public PageBean<User> allUser(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<User> pb = adminDao.allUser(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	public PageBean<SellerOrder> allArbitration(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<SellerOrder> pb = adminDao.allArbitration(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	public void reduceSellerCredit(String sellerOid) {
		try {
			adminDao.reduceSellerCredit(sellerOid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void reduceBuyerCredit(String sellerOid) {
		try {
			adminDao.reduceBuyerCredit(sellerOid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
