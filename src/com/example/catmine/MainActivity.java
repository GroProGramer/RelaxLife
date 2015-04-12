package com.example.catmine;

import java.util.ArrayList;

import com.example.adapter.MenuListViewAdapter;
import com.example.data.MenuData;
import com.example.utils.Utils;
import com.meg7.widget.CircleImageView;

import android.app.AlertDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MainActivity extends FragmentActivity implements OnClickListener,
		OnItemClickListener {
	// 控件相关
	private DrawerLayout drawer;// drawerlayout布局
	private View left;// 左侧抽屉
	private ListView listView;// 侧滑抽屉菜单listview
	private CircleImageView menuimg;// 导航按钮图片
	// 参数相关
	private MenuData data;// 菜单数据
	private ArrayList<MenuData> list = new ArrayList<MenuData>();// 菜单数据集合
	private FragmentManager manager;// 获取fragment管理器
	private TextView title;// 导航title
	private NewsFragment newsFragment;// 新闻
	private WeatherFragment weatherFragment;// 天气
	private JokeFragment jokeFragment;// 笑话
	private OneFragment oneFragment;// 一个
	private ImageFragment imageFragment;// 图片
	private PayRecord payRecord;// 记账薄

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 查找控件
		drawer = (DrawerLayout) findViewById(R.id.drawerlayout);// drawerlayout布局
		left = findViewById(R.id.drawer_layout);// 左侧抽屉
		title = (TextView) findViewById(R.id.menu_title);// 导航title
		menuimg = (CircleImageView) findViewById(R.id.menu_img);// 导航按钮图片
		listView = (ListView) findViewById(R.id.menu_listview);// 侧滑抽屉菜单listview
		// 菜单数据
		data = new MenuData("新闻热点", R.drawable.item1);
		list.add(data);
		data = new MenuData("天气情况", R.drawable.item2);
		list.add(data);
		data = new MenuData("轻松一下", R.drawable.item3);
		list.add(data);
		data = new MenuData("One is All", R.drawable.item4);
		list.add(data);
		data = new MenuData("微语录", R.drawable.item5);
		list.add(data);
		data = new MenuData("记账薄", R.drawable.item6);
		list.add(data);
		// 设置menu适配器
		manager = getSupportFragmentManager();// 获取fragment管理器
		MenuListViewAdapter adapter = new MenuListViewAdapter(
				MainActivity.this, manager, list);
		listView.setAdapter(adapter);
		// 设置监听
		menuimg.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		// 打开抽屉
		//drawer.openDrawer(left);
		// 设置标题动画
		Animation ani = new AlphaAnimation(0.1f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		title.startAnimation(ani);
		FragmentTransaction transaction = manager.beginTransaction();
		// 设置fragment进入退出动画
		transaction
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
						R.anim.push_left_in, R.anim.push_left_out);
		// 隐藏全部fragment
		HideAllFragment(transaction);
		NetworkInfo netState = new Utils().getNetState(MainActivity.this);
		if (netState == null) {
			Toast.makeText(MainActivity.this, "请检查网络>.<",
					Toast.LENGTH_SHORT).show();
		} else {
			if (newsFragment == null) {
				newsFragment = new NewsFragment();
				transaction.add(R.id.main_layout, newsFragment);
			} else {
				transaction.show(newsFragment);
			}
			transaction.commit();
		}
		title.setText("新闻热点");
	}

	// 监听事件
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.menu_img:// 导航菜单按钮
			// 判断当前菜单是否打开
			if (drawer.isDrawerOpen(left)) {// 是,关闭菜单
				drawer.closeDrawer(left);
			} else {// 否,打开菜单
				drawer.openDrawer(left);
			}
			break;
		}
	}

	// listview item点击事件
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// 关闭左侧菜单
		drawer.closeDrawer(left);
		// 开启fragment事务
		FragmentTransaction transaction = manager.beginTransaction();
		// 设置fragment进入退出动画
		transaction
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
						R.anim.push_left_in, R.anim.push_left_out);
		// 隐藏全部fragment
		HideAllFragment(transaction);
		// 获取item相关控件
		TextView text = (TextView) arg1.findViewById(R.id.item_text);
		// 设置导航显示信息
		title.setText(text.getText());
		// 跳转相关页面
		NetworkInfo netState = new Utils().getNetState(MainActivity.this);
		switch (arg2) {
		case 0:// 新闻
			if (netState == null) {
				Toast.makeText(MainActivity.this, "请检查网络>.<",
						Toast.LENGTH_SHORT).show();
			} else {
				if (newsFragment == null) {
					newsFragment = new NewsFragment();
					transaction.add(R.id.main_layout, newsFragment);
				} else {
					transaction.show(newsFragment);
				}
				transaction.commit();
			}
			break;
		case 1:// 天气
			if (netState == null) {
				Toast.makeText(MainActivity.this, "请检查网络>.<",
						Toast.LENGTH_SHORT).show();
			} else {
				if (weatherFragment == null) {
					weatherFragment = new WeatherFragment();
					transaction.add(R.id.main_layout, weatherFragment);
				} else {
					transaction.show(weatherFragment);
				}
				transaction.commit();
			}
			break;
		case 2:// 笑话
			if (netState == null) {
				Toast.makeText(MainActivity.this, "请检查网络>.<",
						Toast.LENGTH_SHORT).show();
			} else {
				if (jokeFragment == null) {
					jokeFragment = new JokeFragment();
					transaction.add(R.id.main_layout, jokeFragment);
				} else {
					transaction.show(jokeFragment);
				}
				transaction.commit();
			}
			break;
		case 3:// 一个
			if (netState == null) {
				Toast.makeText(MainActivity.this, "请检查网络>.<",
						Toast.LENGTH_SHORT).show();
			} else {
				if (oneFragment == null) {
					oneFragment = new OneFragment();
					transaction.add(R.id.main_layout, oneFragment);
				} else {
					transaction.show(oneFragment);
				}
				transaction.commit();
			}
			break;
		case 4:// 图片
			if (netState == null) {
				Toast.makeText(MainActivity.this, "请检查网络>.<",
						Toast.LENGTH_SHORT).show();
			} else {
				if (imageFragment == null) {
					imageFragment = new ImageFragment();
					transaction.add(R.id.main_layout, imageFragment);
				} else {
					transaction.show(imageFragment);
				}
				transaction.commit();
			}
			break;
		case 5:// 记账薄
			if (payRecord == null) {
				payRecord = new PayRecord();
				transaction.add(R.id.main_layout, payRecord);
			} else {
				transaction.show(payRecord);
			}
			transaction.commit();
		}
	}

	// 隐藏全部fragment
	private void HideAllFragment(FragmentTransaction transaction) {
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (weatherFragment != null) {
			transaction.hide(weatherFragment);
		}
		if (jokeFragment != null) {
			transaction.hide(jokeFragment);
		}
		if (oneFragment != null) {
			transaction.hide(oneFragment);
		}
		if (imageFragment != null) {
			transaction.hide(imageFragment);
		}
		if (payRecord != null) {
			transaction.hide(payRecord);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final AlertDialog dialog = new AlertDialog.Builder(this).show();
			View exit_view = getLayoutInflater().inflate(R.layout.exit_dialog,
					null);
			Window window = dialog.getWindow();
			window.setContentView(exit_view);
			DisplayMetrics metric = new DisplayMetrics(); // 定义DisplayMetrics 对象
			getWindowManager().getDefaultDisplay().getMetrics(metric);// 取得窗口属性
			window.setLayout((int) (metric.widthPixels * 0.8),
					(int) (metric.heightPixels * 0.4));
			Button cancel = (Button) window.findViewById(R.id.exit_cancel);
			Button sure = (Button) window.findViewById(R.id.exit_sure);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			sure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
		return false;
	}
}
