package com.vista;

public class CrawlResult
{
	private String title;
	private String url;
	private double probility;
	private String content;
	private String category;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public double getProbility()
	{
		return probility;
	}

	public void setProbility(double probility)
	{
		this.probility = probility;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
}
