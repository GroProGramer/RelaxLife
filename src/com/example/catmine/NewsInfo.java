package com.example.catmine;

import com.example.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

//新闻详情页
@SuppressLint("SetJavaScriptEnabled")
public class NewsInfo extends Activity {
	// 控件相关
	private WebView webView;
	// 参数相关
	private String newsPath = "";// 新闻详情地址
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_info);
		// 查找控件
		webView = (WebView) findViewById(R.id.newsinfo_webview);
		// 判断网络状态
		if (new Utils().getNetState(NewsInfo.this) != null) {// 网络已连接
			initView();
		} else {// 网络未连接
			Toast.makeText(NewsInfo.this, "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 显示视图
	public void initView() {
		// 获取传值
		newsPath = getIntent().getStringExtra("path");
		WebSettings webSettings = webView.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 设置隐藏缩放工具
		webSettings.setBuiltInZoomControls(false);
		// 加载需要显示的网页
		webView.loadUrl(newsPath);
		// 设置Web视图
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				dialog = ProgressDialog.show(NewsInfo.this, null,
						"页面加载中，请稍后...");
				dialog.setCancelable(true);
				super.onPageStarted(view, url, favicon);
			}

			// 网页结束加载
			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
				super.onPageFinished(view, url);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 点击返回键若webview可以返回则返回
		/*if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && !webView.canGoBack()
				&& !dialog.isShowing()) {
			finish();
		}*/
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(webView.canGoBack()){
				webView.goBack();
				return true;
			}
			else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
