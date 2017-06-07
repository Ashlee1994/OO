package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.domain.CopyBook;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.goods.seller.order.web.servlet.SellerOrder;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 按卖家查询书籍
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByUser(String uid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("seller", "=", uid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 管理员查询书籍
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findAllBook(int pc) throws SQLException {
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
		String sql = "select count(*) from t_book" + whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();//得到了总记录数
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_book" + whereSql + " order by orderBy limit ?,?";
		params.add((pc-1)*ps); //当前首页行记录的下标
		params.add(ps); //一共查询几行，就是每页记录数
		
		/*List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class), 
				params.toArray());*/
		List<CopyBook> copyBookList = qr.query(sql, new BeanListHandler<CopyBook>(CopyBook.class), 
				params.toArray());
		
		List<Book> beanList = new ArrayList<Book>();
		
		String sql2 = "select * from t_user where uid=?";
		for(CopyBook copyBook : copyBookList) {	
			User seller = qr.query(sql2, new BeanHandler<User>(User.class),copyBook.getSeller());
			Book book = new Book();
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
			
			beanList.add(book);
		}
		
		
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<Book> pb = new PageBean<Book>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}
	
	
	/**
	 * 删除图书
	 * @param bid
	 * @throws SQLException 
	 */
	public void delete(String bid) throws SQLException {
		String sql = "delete from t_book where bid=?";
		//同时删除关于这个图书的所有订单
		qr.update(sql, bid);
	}
	
	/**
	 * 删除图书
	 * @param bid
	 * @throws SQLException 
	 */
	public void sellerDelete(String bid) throws SQLException {
		//将书籍存在的状态改变
		String sql = "update t_book set exist=0 where bid=?";
		qr.update(sql, bid);
	}
	
	/**
	 * 修改图书
	 * @param book
	 * 		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,seller,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
	 * @throws SQLException 
	 */
	public void edit(Book book) throws SQLException {
		String sql = "update t_book set exist=?,bname=?,author=?,price=?,currPrice=?," +
				"discount=?,press=?,publishtime=?,seller=?,edition=?,pageNum=?,wordNum=?," +
				"printtime=?,booksize=?,paper=?,cid=? where bid=?";
		
		Object[] params = {book.getExist(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getSeller().getUid(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), 
				book.getCategory().getCid(),book.getBid()};
		qr.update(sql, params);
	}
	
	/**
	 * 按bid查询
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book findByBid(String bid) throws SQLException {
		String sql = "SELECT * FROM t_book b, t_category c WHERE b.cid=c.cid AND b.bid=?";
		// 一行记录中，包含了很多的book的属性，还有一个cid属性
		Map<String,Object> map = qr.query(sql, new MapHandler(), bid);
		// 把Map中除了cid以外的其他属性映射到Book对象中
		//Book book = CommonUtils.toBean(map, Book.class);
		
		CopyBook copyBook =  CommonUtils.toBean(map, CopyBook.class);		
		String sql2 = "select * from t_user where uid=?";		
		User seller = qr.query(sql2, new BeanHandler<User>(User.class),copyBook.getSeller());
		Book book = new Book();
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
	
		
		// 把Map中cid属性映射到Category中，即这个Category只有cid
		Category category = CommonUtils.toBean(map, Category.class);
		// 两者建立关系
		book.setCategory(category);
		
		// 把pid获取出来，创建一个Category parnet，把pid赋给它，然后再把parent赋给category
		if(map.get("pid") != null) {
			Category parent = new Category();
			parent.setCid((String)map.get("pid"));
			category.setParent(parent);
		}
		return book;
	}
	
	/**
	 * 查询指定分类下图书的个数
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int findBookCountByCategory(String cid) throws SQLException {
		String sql = "select count(*) from t_book where cid=?";
		Number cnt = (Number)qr.query(sql, new ScalarHandler(), cid);
		return cnt == null ? 0 : cnt.intValue();
	}
	
	/**
	 * 按分类查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PageBean<Book> findByCategory(String cid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + bname + "%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按作者查
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%" + author + "%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按出版社查
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 多条件组合查询
	 * @param combination
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCombination(Book criteria, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + criteria.getBname() + "%"));
		exprList.add(new Expression("author", "like", "%" + criteria.getAuthor() + "%"));
		exprList.add(new Expression("press", "like", "%" + criteria.getPress() + "%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws SQLException {
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
		String sql = "select count(*) from t_book" + whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();//得到了总记录数
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_book" + whereSql + " order by orderBy limit ?,?";
		params.add((pc-1)*ps); //当前首页行记录的下标
		params.add(ps); //一共查询几行，就是每页记录数
		
		/*List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class), 
				params.toArray());*/
		List<CopyBook> copyBookList = qr.query(sql, new BeanListHandler<CopyBook>(CopyBook.class), 
				params.toArray());
		
		List<Book> beanList = new ArrayList<Book>();
		
		String sql2 = "select * from t_user where uid=?";
		for(CopyBook copyBook : copyBookList) {	
			User seller = qr.query(sql2, new BeanHandler<User>(User.class),copyBook.getSeller());
			Book book = new Book();
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
			
			beanList.add(book);
		}
		
		
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<Book> pb = new PageBean<Book>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}
	

	/**
	 * 添加图书
	 * @param book
	 * @throws SQLException 
	 */
	public void add(Book book) throws SQLException {
		String sql = "insert into t_book(bid,exist,bname,author,price,currPrice," +
				"discount,press,publishtime,seller,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getExist(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getSeller().getUid(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
	}
}
