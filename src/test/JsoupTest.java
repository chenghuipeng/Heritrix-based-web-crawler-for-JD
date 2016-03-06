package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JsoupTest
{
	public static Queue<PhoneModel> result_phone = new LinkedList<PhoneModel>();
	public static Queue<Model> result_goods = new LinkedList<Model>();
	public static Vector<String> hist = new Vector<String>();

	public static Model extract(String url)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException
	{
		WebClient wc = new WebClient();
		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
		wc.getOptions().setCssEnabled(false); // 禁用css支持
		wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
		wc.getOptions().setTimeout(5000); // 设置连接超时时间 ，这里是5S。如果为0，则无限期等待
		HtmlPage page = wc.getPage(url);
		wc.close();
		String pageXml = page.asXml(); // 以xml的形式获取响应文本
		Document doc = Jsoup.parse(pageXml);
		Elements attrs = doc.select("ul#parameter2");
		Element p = doc.select("strong#jd-price.p-price").first();
		String price = p.text();
		Element e_ID = attrs.select("li:contains(编号)").first();
		String id = e_ID.attr("title");

		Element e_Name = attrs.select("li:contains(名称)").first();
		String name = e_Name.attr("title");

		Element e_Brand = attrs.select("li:contains(品牌)").first();
		String brand = e_Brand.attr("title");

		Element e_Time = attrs.select("li:contains(上架时间)").first();
		String time = e_Time.attr("title");

		Element e_Origin = attrs.select("li:contains(产地)").first();
		String orign = e_Origin.attr("title");
		//
		// Element e_System = attrs.select("li:contains(系统)").first();
		// String system = e_System.attr("title");
		//
		// Element e_Color = attrs.select("li:contains(颜色)").first();
		// String color = e_Color.attr("title");

		// PhoneModel phone = new PhoneModel(id, name, brand, time, orign,
		// system,
		// color, price);
		// return phone;

		Model goods = new Model(id, name, brand, time, orign, price, url);
		return goods;
	}

	public static String drawtablebody(Model model)
	{
		String tablebody = "";
		tablebody += "<id>" + model.ID + "</id><name>" + model.Name
				+ "</name><brand>" + model.Brand + "</brand><time>"
				+ model.Time + "</time><orign>" + model.Orign
				+ "</orign><price>" + model.Price + "</price>" + "<url>"
				+ model.Url + "</url>";
		return tablebody;
	}

	public static void main(String[] args) throws Exception
	{
		Pattern pattern = Pattern.compile("http://item.jd.com/\\d+.html");
		Matcher matcher = pattern.matcher("http://item.jd.com/1.html");
		Boolean b = matcher.find();
		System.out.println(b);

	}
}
