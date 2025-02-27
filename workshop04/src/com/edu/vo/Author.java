package com.edu.vo;

public class Author {
	private int authorno;
	private String name;
	private String phone;
	public Author(int authorno, String name, String phone) {
		super();
		this.authorno = authorno;
		this.name = name;
		this.phone = phone;
	}
	public int getAuthorno() {
		return authorno;
	}
	public void setAuthorno(int authorno) {
		this.authorno = authorno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "Author [authorno=" + authorno + ", name=" + name + ", phone=" + phone + "]";
	}

	
}
