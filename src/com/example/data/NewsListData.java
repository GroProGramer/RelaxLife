package com.example.data;

//新闻列表数据
public class NewsListData {
	private String title;// 标题
	private String source;// 来源
	private String url;// 图片
	private String article_alt_url;// 跳转详情页地址

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArticle_alt_url() {
		return article_alt_url;
	}

	public void setArticle_alt_url(String article_alt_url) {
		this.article_alt_url = article_alt_url;
	}

	public NewsListData(String title, String source, String url,
			String article_alt_url) {
		super();
		this.title = title;
		this.source = source;
		this.url = url;
		this.article_alt_url = article_alt_url;
	}

	@Override
	public String toString() {
		return "NewsListData [title=" + title + ", source=" + source + ", url="
				+ url + ", article_alt_url=" + article_alt_url + "]";
	}
}
