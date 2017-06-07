package cn.itcast.goods.seller.order.web.servlet;

import java.sql.SQLException;

import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.order.dao.OrderDao;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.jdbc.JdbcUtils;

public class SellerOrderService {
	private SellerOrderDao sellerOrderDao = new SellerOrderDao();
	private BookDao bookDao = new BookDao();
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid, int status) {
		try {
			sellerOrderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void updateStatusByBuyer(String oid, int status) {
		try {
			sellerOrderDao.updateStatusByBuyer(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void updateStatusByOrderItemId(String oid, int status) {
		try {
			sellerOrderDao.updateStatusByOrderItemId(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid) {
		try {
			return sellerOrderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public SellerOrder load(String oid) {
		try {
			JdbcUtils.beginTransaction();
			SellerOrder sellerOrder = sellerOrderDao.load(oid);
			JdbcUtils.commitTransaction();
			return sellerOrder;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 生成订单
	 * @param order
	 */
	public void createOrder(SellerOrder sellerOrder) {
		try {
			JdbcUtils.beginTransaction();
			sellerOrderDao.add(sellerOrder);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<SellerOrder> myOrders(String seller, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<SellerOrder> pb = sellerOrderDao.findByUser(seller, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 我的书籍
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> myBooks(String seller, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Book> pb = bookDao.findByUser(seller, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<SellerOrder> findByStatus(String uid,int status, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<SellerOrder> pb = sellerOrderDao.findByStatus(uid,status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 */
	public PageBean<SellerOrder> findAll(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<SellerOrder> pb = sellerOrderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
}
