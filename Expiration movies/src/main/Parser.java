package main;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser 
{
	private Elements letterboxd_element = null, amazon_element = null, netflix_element = null;
	private ArrayList<String> amazonExpiringMovies = new ArrayList<String>();
	private ArrayList<String> netflixExpiringMovies = new ArrayList<String>();
	
	
	//Crawls Letterboxd's owned watch list.
	public Elements parseLetterboxd(String url)
	{
		try
		{
			Document doc = Jsoup.connect(url).get();
			
			if (letterboxd_element == null)
			{
				letterboxd_element = doc.select("img[width=125]");
			}
			else 
				letterboxd_element.addAll(doc.select("img[width=125]"));
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return letterboxd_element;
	}
	
	//Initializes the parseLetterboxd function to crawl specific pages.
	public void parseLetterboxdInit ()
	{
		parseLetterboxd("https://letterboxd.com/eys/list/owned-watchlist/");
		parseLetterboxd("https://letterboxd.com/eys/list/owned-watchlist/page/2/");
		parseLetterboxd("https://letterboxd.com/eys/list/owned-watchlist/page/3/");
		parseLetterboxd("https://letterboxd.com/eys/list/owned-watchlist/page/4/");
	}
	
	//Crawls whatever website to grab expiring movies.
	public Elements parseBoth(String url)
	{
		Elements elt = new Elements();
		try
		{
			Document doc = Jsoup.connect(url).get();
			elt = doc.select(".post-body a");
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return elt;
	}
	
	public void parseBothInit() 
	{
		amazon_element = parseBoth("http://expiringtitlesonamazonprime.blogspot.com/");
		netflix_element = parseBoth("http://expiringonnetflix.blogspot.com/");
	}
	
	//Compares movies on Letterboxd and movies expiring on the blogspot page.
	//Prints movie names that match.
	public void compareBoth(Elements elt, ArrayList<String> movieList, String service) 
	{
		for (int i = 0; i < letterboxd_element.size(); ++i)
		{
			if (elt.text().contains(letterboxd_element.get(i).attr("alt")))
			{
				movieList.add(letterboxd_element.get(i).attr("alt"));
			}
		}
		
		if (!movieList.isEmpty())
		{
			for (int i = 0; i < movieList.size(); ++i)
			{
				System.out.println(service +  " expiring movie: " + movieList.get(i));
			}
		} 
	}
	
	public void compareBothInit()
	{
		compareBoth(amazon_element, amazonExpiringMovies, "Amazon");
		compareBoth(netflix_element, netflixExpiringMovies, "Netflix");
	}
	

}
