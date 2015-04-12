package com.example.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.data.JokeData;
import com.example.data.NewsListData;
import com.example.data.WeatherData;
import com.example.data.WeatherListData;

//解析工具类
public class DataParser {
	// 解析天气列表数据
	public WeatherListData parserWeatherList(String json) {
		WeatherListData weatherListData = null;
		ArrayList<WeatherData> list = new ArrayList<WeatherData>();
		WeatherData weatherData = null;
		String data1 = "";
		String currentCity = "";
		String pm25 = "";
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("results");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				currentCity = object2.getString("currentCity");
				pm25 = object2.getString("pm25");
				JSONArray array2 = object2.getJSONArray("weather_data");
				for (int j = 0; j < array2.length(); j++) {
					JSONObject object3 = array2.getJSONObject(j);
					String date = object3.getString("date");
					String dayPictureUrl = object3.getString("dayPictureUrl");
					String nightPictureUrl = object3
							.getString("nightPictureUrl");
					String weather = object3.getString("weather");
					String wind = object3.getString("wind");
					String temperature = object3.getString("temperature");
					weatherData = new WeatherData(date, dayPictureUrl,
							nightPictureUrl, weather, wind, temperature);
					list.add(weatherData);
				}
			}
			data1 = object.getString("date");
			weatherListData = new WeatherListData(data1, currentCity, pm25,
					list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weatherListData;
	}

	// 解析笑话列表数据
	public ArrayList<JokeData> parserJokeList(String json) {
		ArrayList<JokeData> list = new ArrayList<JokeData>();
		JokeData data = null;
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("段子");
			for (int i = 0; i < array.length(); i++) {
				String digest = "";// 内容
				String source = "";// 来源
				String img = "";// 图片地址
				JSONObject object2 = array.getJSONObject(i);
				if (object2.has("digest")) {
					digest = object2.getString("digest");
				}
				if (object2.has("source")) {
					source = object2.getString("source");
				}
				if (object2.has("img")) {
					img = object2.getString("img");
				}
				if (img.endsWith(".jpg") || img.endsWith(".png")
						|| img.equals("")) {
					data = new JokeData(digest, source, img);
					list.add(data);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 解析新闻列表数据
	public ArrayList<NewsListData> parserNewsList(String json) {
		ArrayList<NewsListData> list = new ArrayList<NewsListData>();
		NewsListData data = null;
		try {
			JSONObject object = new JSONObject(json);
			if (object.has("data")) {
				JSONArray array = object.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					String title = "";// 标题
					String source = "";// 来源
					String url = "";// 图片
					String article_alt_url = "";// 跳转详情页地址
					JSONObject object2 = array.getJSONObject(i);
					if (object2.has("title")) {
						title = object2.getString("title");
					}
					if (object2.has("source")) {
						source = object2.getString("source");
					}
					if (object2.has("article_url")) {
						article_alt_url = object2.getString("article_url");
					}
					if (object2.has("middle_image")) {
						JSONObject object3 = object2
								.getJSONObject("middle_image");
						if (object3.has("url")) {
							url = object3.getString("url");
						}
					}
					data = new NewsListData(title, source, url, article_alt_url);
					list.add(data);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
