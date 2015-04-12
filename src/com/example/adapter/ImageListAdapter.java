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

import com.example.catmine.ImageInfo;
import com.example.catmine.R;
import com.example.data.ImageData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//微语录适配器
public class ImageListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ImageData> list;
	private LayoutInflater layoutInflater;
	private DisplayImageOptions options;

	public ImageListAdapter(Context context, ArrayList<ImageData> list,
			DisplayImageOptions options) {
		super();
		this.context = context;
		this.list = list;
		this.options = options;
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
		ImageViewHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.image_list_item, null);
			holder = new ImageViewHolder();
			holder.title = (TextView) arg1
					.findViewById(R.id.image_list_item_title);
			holder.date = (TextView) arg1
					.findViewById(R.id.image_list_item_date);
			holder.img = (ImageView) arg1
					.findViewById(R.id.image_list_item_img);
			holder.content = (TextView) arg1
					.findViewById(R.id.image_list_item_content);
			arg1.setTag(holder);
		} else {
			holder = (ImageViewHolder) arg1.getTag();
		}
		// 获取item的数据
		final ImageData data = list.get(position);
		// 赋值
		holder.title.setText(data.getTitle());
		holder.date.setText(data.getDate());
		ImageLoader.getInstance().displayImage(data.getUrl(), holder.img,
				options);
		holder.content.setText("    " + data.getContent());
		arg1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("href", data.getHref());
				intent.setClass(context, ImageInfo.class);
				context.startActivity(intent);
			}
		});
		return arg1;
	}

	class ImageViewHolder {
		TextView title;
		TextView date;
		ImageView img;
		TextView content;
	}
}
