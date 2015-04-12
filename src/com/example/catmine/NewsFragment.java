package com.example.catmine;

import java.util.ArrayList;

import com.example.adapter.NewsListAdapter;
import com.example.data.NewsListData;
import com.example.utils.NetWorkUtils;
import com.example.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

//新闻
@SuppressLint("HandlerLeak")
public class NewsFragment extends Fragment {
	private View newsFragment;
	// 控件相关
	private PullToRefreshListView listView;
	// 工具类相关
	private NetWorkUtils netWorkUtils;// 网络操作工具类
	// 参数相关
	private ProgressDialog dialog;// 等待框
	private int count = 20;// 新闻列表显示新闻数
	private NewsListAdapter adapter;// 新闻列表listview适配器
	private ArrayList<NewsListData> newsList;// 服务器返回的新闻列表数据
	// 消息相关
	protected static final int GETNEWSLISTDATA = 0;// 获取新闻列表数据
	protected static final int GETNEWSLISTDATA_REFRESH = 1;// 刷新新闻列表数据
	protected static final int GETNEWSLISTDATA_ADD_NULL = 2;// 加载新闻列表数据
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETNEWSLISTDATA_ADD_NULL:// 加载新闻列表数据
				listView.onRefreshComplete();
				break;
			case GETNEWSLISTDATA_REFRESH:// 刷新(加载)新闻列表数据
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				break;
			case GETNEWSLISTDATA:// 获取新闻列表数据
				if (newsList != null) {
					// 设置新闻列表listview适配器
					adapter = new NewsListAdapter(getActivity(),
							new Utils().getOptions(), newsList);
					listView.setAdapter(adapter);
					dialog.dismiss();
					// 设置新闻列表listview下拉刷新上拉加载更多监听
					listView.setOnRefreshListener(new NewsRefresh());
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
		newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		// 查找控件
		listView = (PullToRefreshListView) newsFragment
				.findViewById(R.id.news_listview);// 新闻列表listview
		// 设置listview允许下拉刷新上拉加载更多
		listView.setMode(Mode.BOTH);
		// 判断网络状态
		if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
			dialog = ProgressDialog.show(getActivity(), null, "页面加载中，请稍后..");
			dialog.setCancelable(true);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 创建网络操作工具类
					netWorkUtils = new NetWorkUtils();
					// 获取新闻列表数据
					newsList = netWorkUtils.getNewsList(count);
					// 发送消息
					Message message = handler.obtainMessage();
					message.what = GETNEWSLISTDATA;
					handler.sendMessage(message);
				}
			}).start();
		} else {// 网络未连接
			Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
		return newsFragment;
	}

	public class NewsRefresh implements OnRefreshListener2<ListView> {

		/**
		 * 下拉刷新
		 */
		 
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {// 下拉刷新
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 创建网络操作工具类
						netWorkUtils = new NetWorkUtils();
						// 获取新闻列表数据
						ArrayList<NewsListData> newsList2 = netWorkUtils
								.getNewsList(20);
						newsList.clear();
						for (int i = 0; i < newsList2.size(); i++) {
							newsList.add(newsList2.get(i));
						}
						// 发送消息
						Message message = handler.obtainMessage();
						message.what = GETNEWSLISTDATA_REFRESH;
						handler.sendMessage(message);
					}
				}).start();
			} else {// 网络未连接
				Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
						.show();
			}

		}

		/**
		 * 上拉刷新
		 */
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {// 上拉加载
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						count = count + 20;
						// 创建网络操作工具类
						netWorkUtils = new NetWorkUtils();
						// 获取新闻列表数据
						ArrayList<NewsListData> newsList2 = netWorkUtils
								.getNewsList(count);
						// 发送消息
						Message message = handler.obtainMessage();
						if (newsList2.size() <= count - 19) {
							message.what = GETNEWSLISTDATA_ADD_NULL;
						} else {
							for (int i = 0; i < newsList2.size(); i++) {
								if (i > count - 19) {
									newsList.add(newsList2.get(i));
								}
							}
							message.what = GETNEWSLISTDATA_REFRESH;
						}
						handler.sendMessage(message);
					}
				}).start();
			} else {// 网络未连接
				Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
