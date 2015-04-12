package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.catmine.R;
import com.example.data.MenuData;
import com.meg7.widget.CircleImageView;

//侧滑抽屉菜单listview适配器
@SuppressWarnings("unused")
public class MenuListViewAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater layoutInflater;
	private FragmentManager manager;
	private ArrayList<MenuData> list;

	public MenuListViewAdapter(Context context, FragmentManager manager,
			ArrayList<MenuData> list) {
		super();
		this.context = context;
		this.manager = manager;
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
		MenuHolder holder;
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.menu_list_item, null);
			holder = new MenuHolder();
			holder.img = (CircleImageView) arg1.findViewById(R.id.item_img);
			holder.name = (TextView) arg1.findViewById(R.id.item_text);
			arg1.setTag(holder);
		} else {
			holder = (MenuHolder) arg1.getTag();
		}
		// 获取item数据
		final MenuData data = list.get(position);
		// 设置item图片
		holder.img.setImageResource(data.getImg());
		// 设置item文字
		holder.name.setText(data.getName());
		// 设置item动画效果
		Animation ani = new AlphaAnimation(0.1f, 1f);
		ani.setDuration(1500);
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.tutorail_left);
		holder.name.startAnimation(anim);
		return arg1;
	}

	class MenuHolder {
		CircleImageView img;
		TextView name;
	}
}
