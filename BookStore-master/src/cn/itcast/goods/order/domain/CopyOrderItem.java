package cn.itcast.goods.order.domain;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;
public class CopyOrderItem {
	private String orderItemId;//主键
	private int quantity;//数量
	private double subtotal;//小计
	private String bid;//所关联的Book
	private String bname;
	private double currPrice;
	private String image_b;
	private String oid;//所属的订单
	private String sellerOid;//所关联的卖家订单表
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public String getSellerOid() {
		return sellerOid;
	}
	public void setSellerOid(String sellerOid) {
		this.sellerOid = sellerOid;
	}
}
