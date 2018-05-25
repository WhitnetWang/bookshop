package cn.itcast.bookstore.category.domain;

import java.io.Serializable;

public class Category implements Serializable{
	private String cid;
	private String cname;

	public Category() {
		super();
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	
	
}
