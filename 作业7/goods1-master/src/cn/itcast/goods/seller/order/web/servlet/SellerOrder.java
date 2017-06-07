package cn.itcast.goods.seller.order.web.servlet;

import java.util.List;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;
public class SellerOrder {
	private String oid;//主键
	private String orderItemId;//买家订单表主键
	private String ordertime;//下单时间
	private int quantity;//数量
	private double currPrice;//当前价格
	private double total;//总计
	private int status;//订单状态：1未付款, 2已付款但未发货, 3已发货未确认收货, 4确认收货了交易成功, 5已取消(只有未付款才能取消)
	//6买家发起退货申请，7卖家同意退款申请，8买家退货发货，9卖家收到退货并退款，10卖家拒绝退货申请，11申请仲裁，12仲裁失败
	private String address;//收货地址
	private User seller;//店铺id
	private User buyer;//
	private Book book;//图书主键
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getCurrPrice() {
		return currPrice;
	}
	public void setCurrPrice(double currPrice) {
		this.currPrice = currPrice;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
}
