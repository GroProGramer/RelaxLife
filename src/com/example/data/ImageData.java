package com.example.data;

//微语录信息
public class ImageData {
	private String title;// 标题
	private String date;// 日期
	private String content;// 内容
	private String url;// 图片地址
	private String href;// 详情地址

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public ImageData(String title, String date, String content, String url,
			String href) {
		super();
		this.title = title;
		this.date = date;
		this.content = content;
		this.url = url;
		this.href = href;
	}

	@Override
	public String toString() {
		return "ImageData [title=" + title + ", date=" + date + ", content="
				+ content + ", url=" + url + ", href=" + href + "]";
	}

}
