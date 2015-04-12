package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catmine.NewsInfo;
import com.example.catmine.R;
import com.example.data.NewsListData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//新闻列表适配器
public class NewsListAdapter extends BaseAdapter {
	private Context context;
	private DisplayImageOptions options;
	private ArrayList<NewsListData> list;
	private LayoutInflater layoutInflater;

	public NewsListAdapter(Context context, DisplayImageOptions options,
			ArrayList<NewsListData> list) {
		super();
		this.context = context;
		this.options = options;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
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
		NewsListHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.news_list_item, null);
			holder = new NewsListHolder();
			holder.img = (ImageView) arg1.findViewById(R.id.news_list_item_img);
			holder.title = (TextView) arg1
					.findViewById(R.id.news_list_item_title);
			holder.source = (TextView) arg1
					.findViewById(R.id.news_list_item_source);
			holder.imglayout = arg1.findViewById(R.id.news_list_item_imglayout);
			arg1.setTag(holder);
		} else {
			holder = (NewsListHolder) arg1.getTag();
		}
		// 获取item值
		final NewsListData data = list.get(position);
		// 赋值
		holder.title.setText(data.getTitle());
		holder.source.setText(data.getSource());
		if (data.getUrl().equals("")) {
			holder.imglayout.setVisibility(View.GONE);
		} else {
			holder.imglayout.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(data.getUrl(), holder.img,
					options);
		}
		// 设置item点击监听
		arg1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("path", data.getArticle_alt_url());
				intent.setClass(context, NewsInfo.class);
				context.startActivity(intent);
			}
		});
		return arg1;
	}

	class NewsListHolder {
		View imglayout;
		ImageView img;
		TextView title;
		TextView source;
	}
}
