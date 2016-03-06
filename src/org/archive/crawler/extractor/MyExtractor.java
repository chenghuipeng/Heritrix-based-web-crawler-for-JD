package org.archive.crawler.extractor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.io.ReplayCharSequence;
import org.archive.util.HttpRecorder;
import org.archive.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyExtractor extends Extractor
{
	public static final String ATTR_IGNORE_UNEXPECTED_HTML = "ignore-unexpected-html";

	static final String NON_HTML_PATH_EXTENSION = "(?i)(gif)|(jp(e)?g)|(png)|(tif(f)?)|(bmp)|(avi)|(mov)|(mp(e)?g)"
			+ "|(mp3)|(mp4)|(swf)|(wav)|(au)|(aiff)|(mid)";
	private static Logger logger = Logger
			.getLogger(MyExtractor.class.getName());
	private static String PATTERN_URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

	public MyExtractor(String name, String description)
	{
		super(name, description);
	}

	public MyExtractor(String name)
	{
		this(name, "My Extractor");
	}

	protected boolean isHtmlExpectedHere(CrawlURI curi) throws URIException
	{
		String path = curi.getUURI().getPath();
		if (path == null)
		{
			// no path extension, HTML is fine
			return true;
		}
		int dot = path.lastIndexOf('.');
		if (dot < 0)
		{
			// no path extension, HTML is fine
			return true;
		}
		if (dot < (path.length() - 5))
		{
			// extension too long to recognize, HTML is fine
			return true;
		}
		String ext = path.substring(dot + 1);
		return !TextUtils.matches(NON_HTML_PATH_EXTENSION, ext);
	}

	protected void extract(CrawlURI curi)
	{
		if (!isHttpTransactionContentToProcess(curi)
				|| !(isExpectedMimeType(curi.getContentType(), "text/html")
						|| isExpectedMimeType(curi.getContentType(),
								"application/xhtml")
						|| isExpectedMimeType(curi.getContentType(),
								"text/vnd.wap.wml")
						|| isExpectedMimeType(curi.getContentType(),
								"application/vnd.wap.wml") || isExpectedMimeType(
							curi.getContentType(), "application/vnd.wap.xhtml")))
		{
			return;
		}

		final boolean ignoreUnexpectedHTML = ((Boolean) getUncheckedAttribute(
				curi, ATTR_IGNORE_UNEXPECTED_HTML)).booleanValue();

		if (ignoreUnexpectedHTML)
		{
			try
			{
				if (!isHtmlExpectedHere(curi))
				{
					// HTML was not expected (eg a GIF was expected) so ignore
					// (as if a soft 404)
					return;
				}
			} catch (URIException e)
			{
				logger.severe("Failed expectedHTML test: " + e.getMessage());
			}
		}

		ReplayCharSequence cs = null;

		try
		{
			HttpRecorder hr = curi.getHttpRecorder();
			if (hr == null)
			{
				throw new IOException("Why is recorder null here?");
			}
			cs = hr.getReplayCharSequence();
		} catch (IOException e)
		{
			curi.addLocalizedError(this.getName(), e,
					"Failed get of replay char sequence " + curi.toString()
							+ " " + e.getMessage());
			logger.log(Level.SEVERE, "Failed get of replay char sequence in "
					+ Thread.currentThread().getName(), e);
		}

		if (cs == null)
		{
			return;
		}

		// We have a ReplayCharSequence open. Wrap all in finally so we
		// for sure close it before we leave.
		try
		{
			// Extract all links from the charsequence
			String content = cs.toString();
			Document doc = Jsoup.parse(content);
			Elements links = doc.select("a[href]");
			for (Element element : links)
			{
				if (element == null)
					continue;
				String newurl = element.attr("href");
				Pattern p = Pattern.compile(PATTERN_URL);
				Matcher matcher = p.matcher(newurl);
				while (matcher.find())
				{
					addLinkFromString(curi, newurl, "", Link.NAVLINK_HOP);
				}

				// System.out.println("链接源代码：" + element.outerHtml());
				// System.out.println("链接地址：" + element.attr("href") +
				// "  链接文本："+ element.text());
			}
			// Set flag to indicate that link extraction is completed.
			curi.linkExtractorFinished();
		} finally
		{
			if (cs != null)
			{
				try
				{
					cs.close();
				} catch (IOException ioe)
				{
					logger.warning(TextUtils.exceptionToString(
							"Failed close of ReplayCharSequence.", ioe));
				}
			}
		}

	}

	protected void addLinkFromString(CrawlURI curi, CharSequence uri,
			CharSequence context, char hopType)
	{
		try
		{

			curi.createAndAddLinkRelativeToBase(uri.toString(),
					context.toString(), hopType);
		} catch (URIException e)
		{
			if (getController() != null)
			{
				getController().logUriError(e, curi.getUURI(), uri);
			} else
			{
				logger.info("Failed createAndAddLinkRelativeToBase " + curi
						+ ", " + uri + ", " + context + ", " + hopType + ": "
						+ e);
			}
		}
	}

}
