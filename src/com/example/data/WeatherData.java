package com.example.data;

//天气数据
public class WeatherData {
	private String date;// 日期
	private String dayPictureUrl;// 白天图片
	private String nightPictureUrl;// 夜晚图片
	private String weather;// 天气
	private String wind;// 风力
	private String temperature;// 温度

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayPictureUrl() {
		return dayPictureUrl;
	}

	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}

	public String getNightPictureUrl() {
		return nightPictureUrl;
	}

	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public WeatherData(String date, String dayPictureUrl,
			String nightPictureUrl, String weather, String wind,
			String temperature) {
		super();
		this.date = date;
		this.dayPictureUrl = dayPictureUrl;
		this.nightPictureUrl = nightPictureUrl;
		this.weather = weather;
		this.wind = wind;
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "WeatherData [date=" + date + ", dayPictureUrl=" + dayPictureUrl
				+ ", nightPictureUrl=" + nightPictureUrl + ", weather="
				+ weather + ", wind=" + wind + ", temperature=" + temperature
				+ "]";
	}

}
