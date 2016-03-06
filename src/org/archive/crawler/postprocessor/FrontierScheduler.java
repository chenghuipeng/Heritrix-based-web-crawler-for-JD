/* FrontierScheduler
 * 
 * $Id: FrontierScheduler.java 4671 2006-09-26 23:47:15Z paul_jack $
 *
 * Created on June 6, 2005
 * 
 * Copyright (C) 2005 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.archive.crawler.postprocessor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;

import test.JsoupTest;
import test.Model;

/**
 * 'Schedule' with the Frontier CandidateURIs being carried by the passed
 * CrawlURI. Adds either prerequisites or whatever is in CrawlURI outlinks to
 * the Frontier. Run a Scoper ahead of this processor so only links that are
 * in-scope get scheduled.
 * 
 * @author stack
 */
public class FrontierScheduler extends Processor implements FetchStatusCodes
{

	private static final long serialVersionUID = -5178775477602250542L;

	private static Logger LOGGER = Logger.getLogger(FrontierScheduler.class
			.getName());

	private static String PATTERN_URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

	/**
	 * @param name
	 *            Name of this filter.
	 */
	public FrontierScheduler(String name)
	{
		super(
				name,
				"FrontierScheduler. 'Schedule' with the Frontier "
						+ "any CandidateURIs carried by the passed CrawlURI. "
						+ "Run a Scoper before this "
						+ "processor so links that are not in-scope get bumped from the "
						+ "list of links (And so those in scope get promoted from Link "
						+ "to CandidateURI).");
	}

	protected void innerProcess(final CrawlURI curi)
	{
		if (LOGGER.isLoggable(Level.FINEST))
		{
			LOGGER.finest(getName() + " processing " + curi);
		}

		// Handle any prerequisites when S_DEFERRED for prereqs
		if (curi.hasPrerequisiteUri() && curi.getFetchStatus() == S_DEFERRED)
		{
			handlePrerequisites(curi);
			return;
		}

		synchronized (this)
		{
			for (CandidateURI cauri : curi.getOutCandidates())
			{
				schedule(cauri);

				// String content = "";
				String url = cauri.toString();
				// Document doc = null;
				Pattern pattern = Pattern
						.compile("http://item.jd.com/\\d+.html");
				Matcher matcher = pattern.matcher(url);
				Boolean b = matcher.find();
				if (b)
				{
					if (JsoupTest.hist.contains(url))
						continue;
					JsoupTest.hist.addElement(url);
					System.out.println(url);
					try
					{
						Model Goods = JsoupTest.extract(url);
						JsoupTest.result_goods.add(Goods);
					} catch (IOException e)
					{
						System.out.println("link error");
						continue;
					}
					/*
					 * if (!doc.title().equals("") &&
					 * !doc.title().contains("404")) {
					 * 
					 * /* if (Frame.url.indexOf(url) == -1) { String outerhtml =
					 * doc.outerHtml(); try { content =
					 * TextExtract.parse(outerhtml); } catch (Exception e) { //
					 * TODO Auto-generated catch block e.printStackTrace(); } if
					 * (content.equals("") || content.length() < 180) continue;
					 * Frame.bayes.test(content); // if
					 * (Frame.bayes.getLog_prob() > 0) // continue;
					 * 
					 * DownLoadFile dowloader = new DownLoadFile();
					 * dowloader.downloadFile(url, outerhtml);
					 * 
					 * CrawlResult result = new CrawlResult();
					 * result.setCategory(Frame.bayes.getLabelName());
					 * result.setContent(content);
					 * result.setProbility(Frame.bayes.getLog_prob());
					 * result.setTitle(doc.title()); result.setUrl(url);
					 * 
					 * Frame.dBhelper.Connection();
					 * Frame.dBhelper.insert(result); Frame.dBhelper.close();
					 * 
					 * Frame.url.add(url);
					 * Frame.textseed.setText(Frame.url.size() + "");
					 * Vector<String> v = new Vector<String>();
					 * v.add(result.getTitle()); v.add(result.getUrl());
					 * v.add(result.getCategory()); v.add(result.getProbility()
					 * + ""); v.add(result.getContent());
					 * Frame.tmodel.addRow(v); } else System.out.println("不相关" +
					 * url);
					 * 
					 * }
					 */
				} else
					continue;

			}
		}
	}

	protected void handlePrerequisites(CrawlURI curi)
	{
		schedule((CandidateURI) curi.getPrerequisiteUri());
	}

	/**
	 * Schedule the given {@link CandidateURI CandidateURI} with the Frontier.
	 * 
	 * @param caUri
	 *            The CandidateURI to be scheduled.
	 */
	protected void schedule(CandidateURI caUri)
	{
		getController().getFrontier().schedule(caUri);
	}
}
