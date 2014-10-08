package mywebmagic;

import java.awt.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class FoodMatePageProcessor implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {
		/*
		 * page.addTargetRequests(page.getHtml().links()
		 * .regex("(http://news.foodmate.net/\\d{4}/\\d{2}/\\d{6}.html)")
		 * .all());
		 */
		// page.addTargetRequests(page.getHtml().xpath("//div[@class='title2']//li/a[1]/text()").all());
		page.addTargetRequests(page.getHtml()
				.xpath("//div[@class='title2']//li/a[1]/@href").all());
		// java.util.List<String> list =
		// page.getHtml().xpath("//div[@class='title2']//li/a[1]/@href").all();
		// page.addTargetRequests(page.getHtml().links()
		// .regex("(https://github\\.com/\\w+)").all());
		FoodMateRepo foodMateRepo = new FoodMateRepo();
		foodMateRepo.setTitle(page.getHtml()
				.xpath("//div[@class='bt111']/text()").toString());
		foodMateRepo.setTime(page.getHtml()
				.xpath("//div[@class='info']/a[1]/text()").toString());
		foodMateRepo.setSourcetitle(page.getHtml()
				.xpath("//div[@class='info']/a[2]/text()").toString());
		/*
		 * foodMateRepo.setSourcelink(page.getHtml()
		 * .xpath("//div[@class='info']/a[3]/@href").toString());
		 */
		foodMateRepo.setIntroduce(page.getHtml()
				.xpath("//div[@class='introduce']/text()").toString());
		foodMateRepo.setContent(page.getHtml()
				.xpath("//div[@class='content']/allText()").toString());
		foodMateRepo.setUrl(page.getUrl().toString());
		/*
		 * foodMateRepo.setAuthor(page.getUrl()
		 * .regex("https://github\\.com/(\\w+)/.*").toString());
		 * foodMateRepo.setName(page.getHtml()
		 * .xpath("//h1[@class='entry-title public']/strong/a/text()")
		 * .toString()); foodMateRepo.setReadme(page.getHtml()
		 * .xpath("//div[@id='readme']/tidyText()").toString());
		 */
		if (foodMateRepo.getTitle() == null) { // skip this page
			page.setSkip(true);
		} else {
			page.putField("repo", foodMateRepo);
			System.out.println(foodMateRepo.getTitle());
			System.out.println(foodMateRepo.getTime());
			System.out.println(foodMateRepo.getContent());
			System.out.println(foodMateRepo.getUrl());
		}

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new FoodMatePageProcessor())
				.addPipeline(new FoodMatePipeline())
				.addUrl("http://news.foodmate.net").thread(3).run();
	}
}
