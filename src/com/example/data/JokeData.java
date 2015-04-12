package com.example.data;

//笑话数据
public class JokeData {
	private String digest;// 内容
	private String source;// 来源
	private String img;// 图片

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public JokeData(String digest, String source, String img) {
		super();
		this.digest = digest;
		this.source = source;
		this.img = img;
	}

	@Override
	public String toString() {
		return "JokeData [digest=" + digest + ", source=" + source + ", img="
				+ img + "]";
	}

}
