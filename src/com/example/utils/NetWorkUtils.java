package com.example.utils;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.example.api.Api;
import com.example.data.JokeData;
import com.example.data.NewsListData;
import com.example.data.WeatherListData;

//网络操作工具类
public class NetWorkUtils {
	// 获取天气列表数据
	public WeatherListData getWeatherList(String cityName) {
		WeatherListData result = null;
		String path = Api.WEATHER + "location=" + cityName + "&output=json&ak="
				+ Api.AK;
		HttpClient client = new DefaultHttpClient();
		// 设置请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		// 设置读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
		try {
			HttpResponse response = client.execute(new HttpGet(path));
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				result = new DataParser().parserWeatherList(json);
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取笑话列表数据
	public ArrayList<JokeData> getJokeList(int count, int type) {
		ArrayList<JokeData> result = null;
		String path = "";
		if (type == 1) {
			path = Api.JOKELIST + "&size=" + count;
		} else {
			path = Api.JOKELIST2 + "&size=" + count;
		}
		HttpClient client = new DefaultHttpClient();
		// 设置请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		// 设置读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
		try {
			HttpResponse response = client.execute(new HttpGet(path));
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				result = new DataParser().parserJokeList(json);
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取新闻列表数据
	/**
	 * 
	 * @param count 要求获取的新闻数量
	 * @return  返回新闻列表
	 */
	public ArrayList<NewsListData> getNewsList(int count) {
		ArrayList<NewsListData> result = null;
		String path = Api.NEWSLIST + "&count=" + count;
		HttpClient client = new DefaultHttpClient();
		// 设置请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		// 设置读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
		try {
			HttpResponse response = client.execute(new HttpGet(path));
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				result = new DataParser().parserNewsList(json);
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
