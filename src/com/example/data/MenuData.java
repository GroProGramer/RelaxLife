package com.example.data;

//菜单数据
public class MenuData {
	private String name;
	private int img;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public MenuData(String name, int img) {
		super();
		this.name = name;
		this.img = img;
	}

}
