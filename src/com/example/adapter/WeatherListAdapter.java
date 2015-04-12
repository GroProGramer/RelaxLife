package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.catmine.R;
import com.example.data.WeatherData;
import com.example.data.WeatherListData;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//天气列表接口
@SuppressWarnings("unused")
public class WeatherListAdapter extends BaseAdapter {
	private Context context;
	private WeatherListData weatherListData;
	private DisplayImageOptions options;
	private LayoutInflater layoutInflater;
	private boolean type;

	public WeatherListAdapter(Context context, WeatherListData weatherListData,
			DisplayImageOptions options, boolean type) {
		super();
		this.context = context;
		this.weatherListData = weatherListData;
		this.options = options;
		this.type = type;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return weatherListData.getList().size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		WeatherHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.weather_list_item, null);
			holder = new WeatherHolder();
			holder.date = (TextView) arg1
					.findViewById(R.id.weather_list_item_date);
			holder.weather = (TextView) arg1
					.findViewById(R.id.weather_list_item_weather);
			holder.wind = (TextView) arg1
					.findViewById(R.id.weather_list_item_wind);
			holder.temperature = (TextView) arg1
					.findViewById(R.id.weather_list_item_temperature);
			holder.imageView = (CircleImageView) arg1
					.findViewById(R.id.weather_list_item_img);
			arg1.setTag(holder);
		} else {
			holder = (WeatherHolder) arg1.getTag();
		}
		// 获取item数据
		WeatherData data = weatherListData.getList().get(position);
		// 赋值
		holder.date.setText(data.getDate());
		holder.weather.setText(data.getWeather());
		holder.wind.setText(data.getWind());
		holder.temperature.setText(data.getTemperature());
		if (type == true) {// 白天
			ImageLoader.getInstance().displayImage(data.getDayPictureUrl(),
					holder.imageView, options);
		} else {// 夜晚
			ImageLoader.getInstance().displayImage(data.getNightPictureUrl(),
					holder.imageView, options);
		}
		return arg1;
	}

	class WeatherHolder {
		TextView date;// 日期
		TextView weather;// 天气
		TextView wind;// 风力
		TextView temperature;// 温度
		CircleImageView imageView;// 图片(分白天夜晚)
	}
}
