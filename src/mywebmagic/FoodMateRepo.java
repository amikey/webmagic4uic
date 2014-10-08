package mywebmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;

public class FoodMateRepo {
	private String title;
	private String time;
	private String sourcetitle;
	private String sourcelink;
	private String introduce;
	private String content;
	private String url;
	
	public void afterProcess(Page page) {
		// TODO Auto-generated method stub
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSourcetitle() {
		return sourcetitle;
	}

	public void setSourcetitle(String sourcetitle) {
		this.sourcetitle = sourcetitle;
	}

	public String getSourcelink() {
		return sourcelink;
	}

	public void setSourcelink(String sourcelink) {
		if(sourcelink !=null){
			this.sourcelink = sourcelink;
		}else{
			this.sourcelink = "#";
		}
		
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
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
}
