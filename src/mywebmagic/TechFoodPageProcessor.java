package mywebmagic;

import java.util.List;
import java.util.ListIterator;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class TechFoodPageProcessor implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {
		List<String> list = page.getHtml()
				.xpath("//div[@class='news_content3']//a/@href").all();
		/*
		 * ListIterator<String> iter = list.listIterator(); while
		 * (iter.hasNext()) { iter.set("http://www.tech-food.com/news" +
		 * iter.next()); }
		 * 
		 * page.addTargetRequests(list);
		 */

		page.addTargetRequests(page.getHtml()
				.xpath("//div[@class='news_content3']//a/@href").all());

		FoodMateRepo foodMateRepo = new FoodMateRepo();
		foodMateRepo
				.setTitle(page.getHtml().xpath("//title/text()").toString());
		String tempTimeString = page.getHtml()
				.xpath("//div[@class='biaoti1x']/text()").toString();
		if (tempTimeString==null) {
			return;
		}
		String[] strArray = tempTimeString.split(" ");
		if (strArray.length >= 3) {
			tempTimeString = strArray[0] + " " + strArray[1];
		} else {
			return;
		}
		/*
		 * foodMateRepo .setTime(page .getHtml()
		 * .xpath("//div[@class='biaoti1x']/text()") .regex(
		 * "(\\d{2}|\\d{4})(?:\\-)?([0]{1}\\d{1}|[1]{1}[0-2]{1})(?:\\-)?([0-2]{1}\\d{1}|[3]{1}[0-1]{1})(?:\\s)?([0-1]{1}\\d{1}|[2]{1}[0-3]{1})(?::)?([0-5]{1}\\d{1})(?::)?([0-5]{1}\\d{1})"
		 * ) .toString());
		 */
		foodMateRepo.setTime(tempTimeString);
		foodMateRepo.setSourcetitle(strArray[2]);
		/*
		 * foodMateRepo.setSourcelink(page.getHtml()
		 * .xpath("//div[@class='info']/a[3]/@href").toString());
		 */
		foodMateRepo.setIntroduce("");
		foodMateRepo.setContent(page.getHtml()
				.xpath("//div[@id='zoom']/allText()").toString());
		foodMateRepo.setUrl(page.getUrl().toString());
		/*
		 * foodMateRepo.setAuthor(page.getUrl()
		 * .regex("https://github\\.com/(\\w+)/.*").toString());
		 * foodMateRepo.setName(page.getHtml()
		 * .xpath("//h1[@class='entry-title public']/strong/a/text()")
		 * .toString()); foodMateRepo.setReadme(page.getHtml()
		 * .xpath("//div[@id='readme']/tidyText()").toString());
		 */

		page.putField("repo", foodMateRepo);
		System.out.println(foodMateRepo.getTitle());
		System.out.println(foodMateRepo.getTime());
		System.out.println(foodMateRepo.getContent());
		System.out.println(foodMateRepo.getUrl());

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new TechFoodPageProcessor())
				.addPipeline(new FoodMatePipeline())
				.addUrl("http://www.tech-food.com/news/").thread(3).run();
	}

}
