package com.example.api;

//接口
public class Api {
	// 新闻列表
	/**
	 * http://ic.snssdk.com/2/article/v23/stream/?category=news_hot
	 */
	public static String NEWSLIST = "http://ic.snssdk.com/2/article/v23/stream/?category=news_hot";
	// 新闻详情
	public static String NEWSDETAILS = "http://ic.snssdk.com/2/article/information/v9/?group_id=4051714396";
	// 笑话列表1
	public static String JOKELIST = "http://c.m.163.com/recommend/getChanListNews?";
	// 笑话列表2
	public static String JOKELIST2 = "http://c.m.163.com/recommend/getChanRecomNews?";
	// 天气列表
	public static String WEATHER = "http://api.map.baidu.com/telematics/v3/weather?";
	//百度key
	public static String AK="F3ae5d2571c545556ef36e05046b0360";
	// 一个文章
	public static String ONEARTICLE = "http://wufazhuce.com/one";
	// 微语录
	public static String IMAGE = "http://www.wyl.cc/weiwen";
}
