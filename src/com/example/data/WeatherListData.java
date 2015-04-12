package com.example.data;

import java.util.ArrayList;

//天气信息列表数据
public class WeatherListData {
	private String date;// 日期
	private String currentCity;// 城市
	private String pm25;// pm2.5值
	private ArrayList<WeatherData> list;// 列表集合

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public ArrayList<WeatherData> getList() {
		return list;
	}

	public void setList(ArrayList<WeatherData> list) {
		this.list = list;
	}

	public WeatherListData(String date, String currentCity, String pm25,
			ArrayList<WeatherData> list) {
		super();
		this.date = date;
		this.currentCity = currentCity;
		this.pm25 = pm25;
		this.list = list;
	}

	@Override
	public String toString() {
		return "WeatherListData [date=" + date + ", currentCity=" + currentCity
				+ ", pm25=" + pm25 + ", list=" + list + "]";
	}

}
