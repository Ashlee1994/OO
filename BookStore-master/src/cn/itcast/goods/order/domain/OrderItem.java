package cn.itcast.goods.order.domain;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;
public class OrderItem {
	private String orderItemId;//主键
	private String sellerOrderId;//卖家订单表主键
	private int quantity;//数量
	private double subtotal;//小计
	private Book book;//所关联的Book
	private int status; //0表示订单正常，1表示买家发起退货申请，2表示卖家同意退货申请，3表示买家退货发货中，4表示卖家收到退货并退款，5表示卖家拒绝退货申请，6申请仲裁，7仲裁失败
	private Order order;//所属的订单
	private User seller;  //所关联的卖家 
	private String sellerOid;//所关联的卖家订单表
	
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getSellerOrderId() {
		return sellerOrderId;
	}
	public void setSellerOrderId(String sellerOrderId) {
		this.sellerOrderId = sellerOrderId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	public String getSellerOid() {
		return sellerOid;
	}
	public void setSellerOid(String sellerOid) {
		this.sellerOid = sellerOid;
	}
}
