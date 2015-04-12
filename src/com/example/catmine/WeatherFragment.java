package com.example.catmine;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.WeatherListAdapter;
import com.example.data.WeatherListData;
import com.example.utils.NetWorkUtils;
import com.example.utils.Utils;

//天气
@SuppressLint("HandlerLeak")
public class WeatherFragment extends Fragment {
	private View weatherFragment;
	// 控件相关
	private ListView listView;// 天气列表listview
	private View headview;// 头视图
	private Button changecity;// 切换城市
	private TextView currentCity;// 所选城市
	private TextView date;// 当前日期
	private TextView pm25;// pm2.5
	private TextView pmtitle;// pm提示文字
	// 工具类相关
	private NetWorkUtils netWorkUtils;// 网络操作工具类
	// 参数相关
	private WeatherListData weatherList;// 服务器返回的天气数据
	private SharedPreferences sp;// 声明sharedpreferences
	private Dialog dialog;// 等待框
	// 消息相关
	protected static final int GETWEATHERDATA = 0;// 获取天气数据
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETWEATHERDATA:// 获取天气数据
				if (weatherList != null) {
					// 设置listview头视图
					if (headview == null) {
						headview = getActivity().getLayoutInflater().inflate(
								R.layout.weather_headview, null);
					}
					currentCity = (TextView) headview
							.findViewById(R.id.weather_headview_currentCity);
					date = (TextView) headview
							.findViewById(R.id.weather_headview_date);
					pm25 = (TextView) headview
							.findViewById(R.id.weather_headview_pm25);
					changecity = (Button) headview
							.findViewById(R.id.weather_headview_changecity);// 切换城市
					pmtitle = (TextView) headview
							.findViewById(R.id.weather_headview_pmtitle);//
					currentCity.setText(weatherList.getCurrentCity());
					date.setText(weatherList.getDate());
					if (weatherList.getPm25().equals("")) {
						pmtitle.setVisibility(View.INVISIBLE);
						pm25.setVisibility(View.INVISIBLE);
					} else {
						pmtitle.setVisibility(View.VISIBLE);
						pm25.setVisibility(View.VISIBLE);
						pm25.setText(weatherList.getPm25());
					}
					// 设置切换城市监听
					if (listView.getHeaderViewsCount() == 0) {
						listView.addHeaderView(headview);
					}
					changecity.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startActivity(new Intent().setClass(getActivity(),
									CitySelect.class));
						}
					});
					// 判断时间
					boolean day = getDay();
					// 设置天气列表适配器
					WeatherListAdapter adapter = new WeatherListAdapter(
							getActivity(), weatherList,
							new Utils().getOptions(), day);
					listView.setAdapter(adapter);
					dialog.dismiss();
				} else {
					dialog.dismiss();
					Toast.makeText(getActivity(), "暂无数据>.<", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		weatherFragment = inflater.inflate(R.layout.weather_fragment,
				container, false);
		return weatherFragment;
	}

	// 加载视图
	public void initView() {
		// 实例化SharedPreferences对象
		sp = getActivity().getSharedPreferences("Fate", Context.MODE_PRIVATE);
		if (sp.getString("cityname", "").equals("")) {// 为选择城市
			getActivity().startActivity(
					new Intent().setClass(getActivity(), CitySelect.class));
		} else {// 已选城市
			// 查找控件
			listView = (ListView) weatherFragment
					.findViewById(R.id.weather_listview);// 天气列表listview
			// 判断网络状态
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				dialog = ProgressDialog
						.show(getActivity(), null, "页面加载中，请稍后..");
				dialog.setCancelable(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						netWorkUtils = new NetWorkUtils();// 创建网络操作工具类
						// 获取天气数据
						weatherList = netWorkUtils.getWeatherList(sp.getString(
								"cityname", ""));
						// 发送消息
						Message message = handler.obtainMessage();
						message.what = GETWEATHERDATA;
						handler.sendMessage(message);
					}
				}).start();
			} else {// 网络未连接
				Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	// 判断白天黑夜
	public boolean getDay() {
		boolean result = false;// 黑夜
		ContentResolver cv = getActivity().getContentResolver();
		String strTimeFormat = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.TIME_12_24);
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.HOUR_OF_DAY);
		if (strTimeFormat != null && strTimeFormat.equals("24")) {
			if (i < 6 || i >= 18) {
				result = false;
			} else if (i >= 6 && i < 18) {
				result = true;
			}
		} else {
			if (c.get(Calendar.AM_PM) == 0) {// 上午
				if (i < 6) {
					result = false;
				} else {
					result = true;
				}
			} else {// 下午
				if (i >= 6) {
					result = false;
				} else {
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public void onStart() {
		initView();
		super.onStart();
	};
}
