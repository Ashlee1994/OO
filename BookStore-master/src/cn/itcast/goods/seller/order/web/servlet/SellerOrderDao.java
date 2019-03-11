package cn.itcast.goods.seller.order.web.servlet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.domain.CopyBook;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class SellerOrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public int findStatus(String oid) throws SQLException {
		String sql = "select status from t_seller_order where oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	
	public void updateStatus(String oid, int status) throws SQLException {
		String sql = "update t_seller_order set status=? where oid=?";
		qr.update(sql, status, oid);
		System.out.println("修改数据库状态成功！oid = "+oid+ ",status = "+status);
	}
	
	public void updateStatusByOrderItemId(String orderItemId, int status) throws SQLException {

		String sql = "select sellerOrderId from t_orderitem where orderItemId=?";
		String sellerOrderId = (String)qr.query(sql,new ScalarHandler(),orderItemId);
		
		sql = "update t_seller_order set status=? where oid=?";
		qr.update(sql, status, sellerOrderId);
		
	}
	public void updateStatusByBuyer(String oid, int status) throws SQLException {
		
		String sql = "select * from t_orderitem where oid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), oid);
		
		for(Map<String,Object> map : mapList) {
			OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
			sql = "update t_seller_order set status=? where oid=?";
			qr.update(sql, status, orderItem.getSellerOid());
		}
		
		
		System.out.println("修改数据库状态成功！");
	}
	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public SellerOrder load(String oid) throws SQLException {
		String sql = "select * from t_seller_order where oid=?";
		//SellerOrder sellerOrder = qr.query(sql, new BeanHandler<SellerOrder>(SellerOrder.class), oid);
		
		CopySellerOrder copySellerOrder = qr.query(sql, new BeanHandler<CopySellerOrder>(CopySellerOrder.class), oid);
		
	
		
		String sql2 = "select * from t_user where uid=?";
		String sql3 = "select * from t_book where bid=?";
		User seller = qr.query(sql2, new BeanHandler<User>(User.class),copySellerOrder.getSeller());
		User buyer = qr.query(sql2, new BeanHandler<User>(User.class),copySellerOrder.getBuyer());
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
			
		
		return sellerOrder;
	}
	
	/**
	 * 生成订单
	 * @param order
	 * @throws SQLException 
	 */
	public void add(SellerOrder sellerOrder) throws SQLException {
		/*
		 * 1. 插入订单
		 */
		String sql = "insert into t_seller_order values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {sellerOrder.getOid(), sellerOrder.getOrdertime(),
				sellerOrder.getQuantity(),sellerOrder.getCurrPrice(),sellerOrder.getTotal(),sellerOrder.getStatus(),sellerOrder.getAddress(),
				sellerOrder.getSeller().getUid(),sellerOrder.getBuyer().getUid(),sellerOrder.getBook().getBid(),sellerOrder.getBook().getBname(),sellerOrder.getBook().getImage_b()
	    };
		qr.update(sql, params);
	}
	
	/**
	 * 按用户查询订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<SellerOrder> findByUser(String seller, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("seller", "=", seller));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 查询所有
	 */
	public PageBean<SellerOrder> findAll(int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<SellerOrder> findByStatus(String uid,int status, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status", "=", status + ""));
		exprList.add(new Expression("seller", "=", uid + ""));
		return findByCriteria(exprList, pc);
	}
	
	private PageBean<SellerOrder> findByCriteria(List<Expression> exprList, int pc) throws SQLException {
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
		 * 2. 通过exprList来生成where子句
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1"); 
		List<Object> params = new ArrayList<Object>();//SQL中有问号，它是对应问号的值
		for(Expression expr : exprList) {
			/*
			 * 添加一个条件上，
			 * 1) 以and开头
			 * 2) 条件的名称
			 * 3) 条件的运算符，可以是=、!=、>、< ... is null，is null没有值
			 * 4) 如果条件不是is null，再追加问号，然后再向params中添加一与问号对应的值
			 */
			whereSql.append(" and ").append(expr.getName())
				.append(" ").append(expr.getOperator()).append(" ");
			// where 1=1 and bid = ?
			if(!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}

		/*
		 * 3. 总记录数 
		 */
		String sql = "select count(*) from t_seller_order" + whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();//得到了总记录数
		
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_seller_order" + whereSql + " order by ordertime desc limit ?,?";
		params.add((pc-1) * ps);//当前页首行记录的下标
		params.add(ps);//一共查询几行，就是每页记录数
		
		/*List<SellerOrder> beanList = qr.query(sql, new BeanListHandler<SellerOrder>(SellerOrder.class), 
				params.toArray());*/
		
		List<CopySellerOrder> copySellerList = qr.query(sql, new BeanListHandler<CopySellerOrder>(CopySellerOrder.class), 
				params.toArray());
		
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
}
