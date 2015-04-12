package com.example.data;

//记账簿数据信息
public class PayRecordData {
	private int id;// id
	private String type;// 类型
	private String price;// 金额
	private String tag;// 类别
	private String date;// 日期
	private String time;// 时间
	private String ps;// 备注

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public PayRecordData(int id, String type, String price, String tag,
			String date, String time, String ps) {
		super();
		this.id = id;
		this.type = type;
		this.price = price;
		this.tag = tag;
		this.date = date;
		this.time = time;
		this.ps = ps;
	}

	@Override
	public String toString() {
		return "PayRecordData [id=" + id + ", type=" + type + ", price="
				+ price + ", tag=" + tag + ", date=" + date + ", time=" + time
				+ ", ps=" + ps + "]";
	}

}
