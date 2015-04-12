package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catmine.R;
import com.example.data.JokeData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
//笑话适配器
@SuppressWarnings("unused")
public class JokeListAdapter extends BaseAdapter {
	private Context context;
	private DisplayImageOptions options;
	private ArrayList<JokeData> list;
	private LayoutInflater layoutInflater;

	public JokeListAdapter(Context context, DisplayImageOptions options,
			ArrayList<JokeData> list) {
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
		JokeHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.joke_list_item, null);
			holder = new JokeHolder();
			holder.img = (ImageView) arg1.findViewById(R.id.joke_item_img);
			holder.source = (TextView) arg1.findViewById(R.id.joke_item_source);
			holder.content = (TextView) arg1
					.findViewById(R.id.joke_item_content);
			arg1.setTag(holder);
		} else {
			holder = (JokeHolder) arg1.getTag();
		}
		// 获取item的数据
		JokeData data = list.get(position);
		// 赋值
		if (data.getImg().equals("")) {
			holder.img.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage(data.getImg(), holder.img,
					options);// 设置图片
			holder.img.setVisibility(View.VISIBLE);
		}
		holder.source.setText(data.getSource());// 设置来源
		holder.content.setText(data.getDigest());// 设置内容
		arg1.setOnClickListener(null);
		return arg1;
	}

	class JokeHolder {
		ImageView img;
		TextView source;
		TextView content;
	}
}
