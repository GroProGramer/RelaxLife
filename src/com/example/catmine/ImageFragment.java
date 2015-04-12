package com.example.catmine;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.adapter.ImageListAdapter;
import com.example.data.ImageData;
import com.example.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

//微语录
@SuppressLint("HandlerLeak")
public class ImageFragment extends Fragment {
	private View imageFragment;
	// 控件相关
	private PullToRefreshListView listView;// 微语录listview
	// 参数相关
	private Dialog dialog;// 等待框
	private ArrayList<ImageData> list;// 服务器返回的微语录数据
	private ImageListAdapter adapter;// 设置微语录listview适配器
	private String url = "http://www.wyl.cc/weiwen";// 数据来源地址
	private int page = 1;// 页码
	// 消息相关
	protected static final int GETIMAGEDATA = 0;// 获取微语录数据
	protected static final int GETIMAGEDATA_DOWN_REFRESH = 1;// 获取微语录下拉刷新数据
	protected static final int GETIMAGEDATA_UP_REFRESH = 2;// 获取微语录上拉加载更多数据(不为空)
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETIMAGEDATA_UP_REFRESH:// 获取微语录上拉加载更多数据(不为空)
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			case GETIMAGEDATA_DOWN_REFRESH:// 获取微语录下拉刷新数据
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				break;
			case GETIMAGEDATA:// 获取微语录数据
				if (list != null) {
					// 设置微语录listview适配器
					adapter = new ImageListAdapter(getActivity(), list,
							new Utils().getOptions());
					listView.setAdapter(adapter);
					dialog.dismiss();
					// 设置listview上拉下拉监听
					listView.setOnRefreshListener(new ImageRefresh());
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
		imageFragment = inflater.inflate(R.layout.img_fragment, container,
				false);
		// 判断网络状态
		if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
			initView();
		} else {// 网络未连接
			Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
		return imageFragment;
	}

	// 显示视图
	public void initView() {
		// 查找控件
		listView = (PullToRefreshListView) imageFragment
				.findViewById(R.id.image_listview);// 微语录listview
		// 设置listview允许上拉加载更多和下拉刷新
		listView.setMode(Mode.BOTH);
		dialog = ProgressDialog.show(getActivity(), null, "页面加载中，请稍后...");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = new ArrayList<ImageData>();
				// 获取微语录数据
				GetImageData(list, url);
				// 发送消息
				Message message = handler.obtainMessage();
				message.what = GETIMAGEDATA;
				handler.sendMessage(message);
			}
		}).start();
	}

	// 获取微语录数据
	private void GetImageData(ArrayList<ImageData> list, String path) {
		ImageData data = null;
		try {
			Document doc = Jsoup.connect(path).get();
			Elements elements = doc.getElementsByTag("main");
			Document parse = Jsoup.parse(elements.toString());
			Elements elementsByTag = parse.getElementsByTag("article");
			for (Element element : elementsByTag) {
				boolean result = false;
				String href = "";// 详情地址
				String src = "";// 图片地址
				String title = "";// 标题
				String content = "";// 简介内容
				String date = "";// 日期
				Element first = element.select("a").first();
				href = first.attr("href");
				Elements select = element.select("img[src]");
				for (Element element2 : select) {
					src = element2.attr("src");
				}
				title = element.getElementsByClass("entry-title").text();
				content = element.getElementsByClass("archive-content").text();
				date = element.getElementsByClass("date").text();
				data = new ImageData(title, date, content, src, href);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getTitle().equals(title)) {
						result = true;
					}
				}
				if (result == false) {
					list.add(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 刷新
	private class ImageRefresh implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {// 下拉刷新
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 获取微语录数据
						GetImageData(list, url);
						// 发送消息
						Message message = handler.obtainMessage();
						message.what = GETIMAGEDATA_DOWN_REFRESH;
						handler.sendMessage(message);
					}
				}).start();
			} else {// 网络未连接
				Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {// 上拉加载
			if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
				new Thread(new Runnable() {
					@Override
					public void run() {
						page++;
						// 获取微语录数据
						GetImageData(list, url + "/page/" + page);
						// 发送消息
						Message message = handler.obtainMessage();
						message.what = GETIMAGEDATA_UP_REFRESH;
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
