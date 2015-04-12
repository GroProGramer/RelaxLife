package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.catmine.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

//工具类
public class Utils {
	// 判断网络状态
	public NetworkInfo getNetState(Activity activity) {
		NetworkInfo result = null;
		ConnectivityManager mConnectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		result = mConnectivity.getActiveNetworkInfo();
		return result;
	}

	// 图片缓存
	public DisplayImageOptions getOptions() {
		// 图片缓存相关
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.wait)
				// 正在加载
				.showImageForEmptyUri(R.drawable.wait)
				// 空图片
				.showImageOnFail(R.drawable.wait)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
}
