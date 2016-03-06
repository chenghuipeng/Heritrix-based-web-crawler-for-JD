package test;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class segment
{
	public static String show(Analyzer a, String s) throws Exception
	{

		StringReader reader = new StringReader(s);
		TokenStream ts = a.tokenStream(s, reader);
		String s1 = "", s2 = "";
		boolean hasnext = ts.incrementToken();
		// Token t = ts.next();
		while (hasnext)
		{
			// AttributeImpl ta = new AttributeImpl();
			CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);
			// TermAttribute ta = ts.getAttribute(TermAttribute.class);

			s2 = ta.toString() + " ";
			s1 += s2;
			hasnext = ts.incrementToken();
		}
		return s1;
	}

	public String segment(String s) throws Exception
	{
		Analyzer a = new IKAnalyzer();
		return show(a, s);
	}

	public static void main(String args[])
	{
		String name = "我是俊杰，我爱编程，我的测试用例";
		segment s = new segment();
		String test = "";
		try
		{
			System.out.println(test + s.segment(name));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}