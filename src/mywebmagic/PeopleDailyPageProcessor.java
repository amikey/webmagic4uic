package mywebmagic;

import java.util.List;
import java.util.ListIterator;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class PeopleDailyPageProcessor implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {
		List<String> list = page.getHtml()
				.xpath("//div[@class='d2_left']//a/@href")
				.regex("/n/\\d{4}/\\d{4}/c\\d{5}-\\d{8}.html").all();
		ListIterator<String> iter = list.listIterator();
		while (iter.hasNext()) {
			iter.set("shipin.people.com.cn" + iter.next());
		}

		page.addTargetRequests(list);

		FoodMateRepo foodMateRepo = new FoodMateRepo();
		foodMateRepo.setTitle(page.getHtml()
				.xpath("//h1[@id='p_title']/text()").toString());
		String tempTimeString = page.getHtml()
				.xpath("//span[@id='p_publishtime']/text()").toString();

		tempTimeString = tempTimeString.replace("Äê", "-");
		tempTimeString = tempTimeString.replace("ÔÂ", "-");
		tempTimeString = tempTimeString.replace("ÈÕ", " ");
		foodMateRepo.setTime(tempTimeString + ":00");
		foodMateRepo.setSourcetitle(page.getHtml()
				.xpath("//span[@id='p_origin']/a[1]/text()").toString());
		/*
		 * foodMateRepo.setSourcelink(page.getHtml()
		 * .xpath("//div[@class='info']/a[3]/@href").toString());
		 */
		foodMateRepo.setIntroduce("");
		foodMateRepo.setContent(page.getHtml()
				.xpath("//div[@id='p_content']/allText()").toString());
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
		Spider.create(new PeopleDailyPageProcessor())
				.addPipeline(new FoodMatePipeline())
				.addUrl("http://shipin.people.com.cn/GB/86117/index.html")
				.thread(3).run();
	}
}
