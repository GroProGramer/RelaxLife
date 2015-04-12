package com.example.catmine;

import com.example.api.Api;
import com.example.utils.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

//文章
@SuppressLint("SetJavaScriptEnabled")
public class OneFragment extends Fragment {
	private View oneFragment;
	// 控件相关
	private WebView webView;
	// 参数相关
	private ProgressDialog dialog;// 加载框

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		oneFragment = inflater.inflate(R.layout.one_fragment, container, false);
		// 查找控件
		webView = (WebView) oneFragment.findViewById(R.id.one_webview);
		// 判断网络状态
		if (new Utils().getNetState(getActivity()) != null) {// 网络已连接
			initView();
		} else {// 网络未连接
			Toast.makeText(getActivity(), "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
		return oneFragment;
	}

	// 显示视图
	public void initView() {
		WebSettings webSettings = webView.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 设置隐藏缩放工具
		webSettings.setBuiltInZoomControls(false);
		// 加载需要显示的网页
		webView.loadUrl(Api.ONEARTICLE);
		// 设置Web视图
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				dialog = ProgressDialog.show(getActivity(), null,
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
		webView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 返回键
						webView.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
	}
}
