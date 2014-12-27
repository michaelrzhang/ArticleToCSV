import java.util.*;

public class ArticleScraperTest{
	public static void main(String[] args){
		String url1 = "http://www.thedailybeast.com/articles/2014/08/21/swat-lobby-takes-a-shot-at-congress.html";
		String url2 = "http://www.thedailybeast.com/articles/2014/08/12/russia-s-suspicious-humanitarian-aid-for-ukraine-separatists.html";
		List<String> urls = new ArrayList<String>();
		urls.add(url1);
		urls.add(url2);
		ArticleScraper scraper_singleURL = new ArticleScraper("test1.csv", url1);
		ArticleScraper.scrapeArticles(scraper_singleURL); // outputs file test1.csv
		ArticleScraper scraper_singleURL2 = new ArticleScraper("test2.csv", url2);
		ArticleScraper.scrapeArticles(scraper_singleURL2); // outputs file test2.csv
		ArticleScraper scraper_multipleURL = new ArticleScraper("test3.csv", urls);
		ArticleScraper.scrapeArticles(scraper_multipleURL); // outputs file test3.csv
	}
}