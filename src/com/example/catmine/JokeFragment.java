package com.example.catmine;

import java.util.ArrayList;

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

import com.example.adapter.JokeListAdapter;
import com.example.data.JokeData;
import com.example.utils.NetWorkUtils;
import com.example.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

//笑话
@SuppressLint("HandlerLeak")
public class JokeFragment extends Fragment {
	private View jokeFragment;
	// 控件相关
	private PullToRefreshListView listView;
	// 工具类相关
	private NetWorkUtils netWorkUtils;// 网络操作工具类
	// 参数相关
	private int size = 20;
	private ProgressDialog dialog;// 等待框
	private ArrayList<JokeData> jokeList;// 服务器返回的笑话列表数据
	private JokeListAdapter adapter;// 笑话列表listview适配器
	// 消息相关
	protected static final int GETJOKEDATA = 0;// 获取笑话列表数据
	protected static final int GETJOKEDATA_REFRESH = 1;// 获取下拉刷新笑话列表数据
	protected static final int GETJOKEDATA_ADD = 2;// 获取上拉加载笑话列表数据(不为空)
	protected static final int GETJOKEDATA_ADD_NULL = 3;// 获取上拉加载笑话列表数据(为空)
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETJOKEDATA_ADD_NULL:// 获取上拉加载笑话列表数据(为空)
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "数据君找不到了>.<", Toast.LENGTH_SHORT)
						.show();
				break;
			case GETJOKEDATA_ADD:// // 获取上拉加载笑话列表数据(不为空)
			case GETJOKEDATA_REFRESH:// 获取下拉刷新笑话列表数据
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				break;
			case GETJOKEDATA:// 获取笑话列表数据
				if (jokeList != null) {
					// 设置适配器
					adapter = new JokeListAdapter(getActivity(),
							new Utils().getOptions(), jokeList);
					listView.setAdapter(adapter);
					// 设置listview上拉下拉监听
					listView.setOnRefreshListener(new JokeListRefresh());
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
		jokeFragment = inflater.inflate(R.layout.joke_fragment, container,
				false);
		// 查找控件
		listView = (PullToRefreshListView) jokeFragment
				.findViewById(R.id.joke_listview);// 笑话列表listview
		// 设置listview可以下拉刷新上拉加载更多
		listView.setMode(Mode.BOTH);
		// 判断网络状态
		if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
			dialog = ProgressDialog.show(getActivity(), null, "页面加载中，请稍后..");
			dialog.setCancelable(true);
			new Thread(new Runnable() {
				@Override
				public void run() {
					netWorkUtils = new NetWorkUtils();// 创建网络操作工具类
					// 获取笑话列表
					jokeList = netWorkUtils.getJokeList(size, 1);
					// 发送消息
					Message message = handler.obtainMessage();
					message.what = GETJOKEDATA;
					handler.sendMessage(message);
				}
			}).start();
		} else {// 网络未连接
			Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
		return jokeFragment;
	}

	public class JokeListRefresh implements OnRefreshListener2<ListView> {
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {// 下拉刷新
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 获取最新笑话列表数据
						ArrayList<JokeData> jokeList2 = netWorkUtils
								.getJokeList(size, 1);
						jokeList.clear();
						for (int i = 0; i < jokeList2.size(); i++) {
							jokeList.add(jokeList2.get(i));
						}
						// 发送消息
						Message message = handler.obtainMessage();
						message.what = GETJOKEDATA_REFRESH;
						handler.sendMessage(message);
					}
				}).start();
			} else {// 网络未连接
				Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {// 上拉加载更多
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 获取更多笑话列表数据
						ArrayList<JokeData> joke = netWorkUtils.getJokeList(
								size, 2);
						// 发送消息
						Message message = handler.obtainMessage();
						if (joke.isEmpty()) {// 没有更多数据
							message.what = GETJOKEDATA_ADD_NULL;
						} else {// 有更多数据
							for (int i = 0; i < joke.size(); i++) {
								jokeList.add(joke.get(i));
							}
							message.what = GETJOKEDATA_ADD;
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
