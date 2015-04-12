package com.example.catmine;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//微语录详情
public class ImageInfo extends Activity {
	// 控件相关
	private TextView title_text;// 标题textview
	private TextView content_text;// 内容textview
	private ImageView img;// 图片imageview
	private TextView return_text;// 返回文字
	private ImageView return_img;// 返回图片
	private View layout;// 内容所在布局
	// 参数相关
	private String title;// 标题
	private String content;// 内容
	private String image;// 图片
	private String url = "";// 获取详情地址
	private Dialog dialog;// 等待框
	// 消息相关
	protected static final int GETIMAGEINFO = 0;// 获取微语录详情
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETIMAGEINFO:// 获取微语录详情
				// 设置显示内容
				title_text.setText(title);
				content_text.setText(content);
				ImageLoader.getInstance().displayImage(image, img,
						new Utils().getOptions());
				// 设置返回文字图片动画效果
				Animation ani = new AlphaAnimation(0.1f, 1f);
				ani.setDuration(1500);
				ani.setRepeatMode(Animation.REVERSE);
				ani.setRepeatCount(Animation.INFINITE);
				return_text.startAnimation(ani);
				Animation anim = AnimationUtils.loadAnimation(ImageInfo.this,
						R.anim.tutorail_left);
				return_img.startAnimation(anim);
				return_text.setVisibility(View.VISIBLE);
				return_img.setVisibility(View.VISIBLE);
				layout.setVisibility(View.VISIBLE);
				return_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
				dialog.dismiss();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_info);
		// 获取传值
		url = getIntent().getStringExtra("href");
		// 判断网络状态
		if (new Utils().getNetState(ImageInfo.this) != null) {// 网络已连接
			// 显示等待框
			dialog = ProgressDialog.show(ImageInfo.this, null, "页面加载中，请稍后..");
			dialog.setCancelable(true);
			dialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 显示视图
					GetInfo(url);
				}
			}).start();
		} else {// 网络未连接
			Toast.makeText(ImageInfo.this, "请检查网络>.<", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 显示视图
	private void GetInfo(String url) {
		try {
			// 查找控件
			title_text = (TextView) findViewById(R.id.image_info_title);// 标题
			content_text = (TextView) findViewById(R.id.image_info_content);// 内容
			img = (ImageView) findViewById(R.id.image_info_image);// 图片
			return_text = (TextView) findViewById(R.id.image_info_return_text);// 返回文字
			return_img = (ImageView) findViewById(R.id.image_info_return_img);// 返回图片
			layout = findViewById(R.id.image_info_content_layout);// 内容所在布局
			// 取值
			Document doc = Jsoup.connect(url).get();
			Element head = doc.head();
			title = head.select("meta[property=og:title]").attr("content");
			content = head.select("meta[property=og:description]").attr(
					"content");
			Elements elements = doc.getElementsByTag("main");
			Document parse = Jsoup.parse(elements.toString());
			Elements elementsByTag = parse.getElementsByTag("article");
			for (Element element : elementsByTag) {
				Elements select = element.select("img[src]");
				for (Element element2 : select) {
					image = element2.attr("src");
				}
			}
			// 发送消息
			Message message = handler.obtainMessage();
			message.what = GETIMAGEINFO;
			handler.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
