package cn.itcast.goods.message.domain;

import cn.itcast.goods.user.domain.User;

public class message {
		private String mid;//主键
		private User sender;//消息发送者
		private User receiver;//消息接收者
		private String msg;// 消息
		private int status;//0消息还未读，1表示消息已读但未回复，2表示消息已经回复
		public String getMid() {
			return mid;
		}
		public void setMid(String mid) {
			this.mid = mid;
		}
		public User getSender() {
			return sender;
		}

		public void setSender(User sender) {
			this.sender = sender;
		}
		public User getReceiver() {
			return receiver;
		}

		public void setReceiver(User receiver) {
			this.receiver = receiver;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public int getStatus() {
			return status;
		}
		public void setMid(int status) {
			this.status = status;
		}
}
