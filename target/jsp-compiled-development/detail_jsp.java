package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import test.PhoneModel;
import test.JsoupTest;
import test.PhoneModel;

public class detail_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

  public java.util.List getIncludes() {
    return _jspx_includes;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    javax.servlet.jsp.PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("<title>ShowDetails");
      out.write("</title>\r\n");
      out.write("<style>  \r\n    #tbNew {\r\n       border: #000000 1px solid;}\r\n    #tbNew td{border: #0092d2 1px solid;\r\n     text-align:center;\r\n     font-size:14px;}\r\n    table,tr,th{  \r\n        border:1px solid #ccd;  \r\n        border-collapse:collapse;  \r\n    }  \r\n    table{  \r\n        width:100%;  \r\n    }  \r\n    th{  \r\n        height:24px;  \r\n        width:50px;/** 不管是固定像素或是百分比，应与对应数据列的宽度一致 */  \r\n        line-height:24px;  \r\n        background-color:#cfc;  \r\n    }  \r\n");
      out.write("</style>  \r\n");
      out.write("<script language=\"javascript\">  \r\n      \r\n    var XMLHttpReq;  \r\n        //创建XMLHttpRequest对象         \r\n        function createXMLHttpRequest() {  \r\n            if(window.XMLHttpRequest) { //Mozilla 浏览器  \r\n                XMLHttpReq = new XMLHttpRequest();  \r\n            }  \r\n            else if (window.ActiveXObject) { // IE浏览器  \r\n                try {  \r\n                    XMLHttpReq = new ActiveXObject(\"Msxml2.XMLHTTP\");  \r\n                } catch (e) {  \r\n                    try {  \r\n                        XMLHttpReq = new ActiveXObject(\"Microsoft.XMLHTTP\");  \r\n                    } catch (e) {}  \r\n                }  \r\n            }  \r\n        }  \r\n        //发送请求函数  \r\n        function sendRequest() {  \r\n            createXMLHttpRequest();  \r\n            var url = \"ajax.jsp\";  \r\n            XMLHttpReq.open(\"GET\", url, true);  \r\n            XMLHttpReq.onreadystatechange = processResponse;//指定响应函数  \r\n            XMLHttpReq.send(null);  // 发送请求  \r\n        }  \r\n        // 处理返回信息函数  \r\n        function processResponse() {  \r\n");
      out.write("            if (XMLHttpReq.readyState == 4) { // 判断对象状态  \r\n                if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息  \r\n                \taddRow(tbNew);  \r\n                    setTimeout(\"sendRequest()\", 3000);  \r\n                } else { //页面不正常  \r\n                    window.alert(\"您所请求的页面有异常。\");  \r\n                }  \r\n            }  \r\n        }  \r\n        function addRow(tableid){\r\n        \tvar element = XMLHttpReq.responseXML.getElementsByTagName(\"str\");\r\n        \tif(element.length==0)\r\n        \t\treturn;\r\n        \tvar name = XMLHttpReq.responseXML.getElementsByTagName(\"name\")[0].firstChild.nodeValue;  \r\n            var id = XMLHttpReq.responseXML.getElementsByTagName(\"id\")[0].firstChild.nodeValue; \r\n            var brand = XMLHttpReq.responseXML.getElementsByTagName(\"brand\")[0].firstChild.nodeValue;  \r\n           // var color = XMLHttpReq.responseXML.getElementsByTagName(\"color\")[0].firstChild.nodeValue; \r\n            var orign = XMLHttpReq.responseXML.getElementsByTagName(\"orign\")[0].firstChild.nodeValue;  \r\n");
      out.write("           // var system = XMLHttpReq.responseXML.getElementsByTagName(\"system\")[0].firstChild.nodeValue; \r\n            var time = XMLHttpReq.responseXML.getElementsByTagName(\"time\")[0].firstChild.nodeValue; \r\n            var price = XMLHttpReq.responseXML.getElementsByTagName(\"price\")[0].firstChild.nodeValue; \r\n            var url = XMLHttpReq.responseXML.getElementsByTagName(\"url\")[0].firstChild.nodeValue; \r\n        \t//添加行\r\n        \tvar newTr = tableid.insertRow();\r\n        \t//添加列\r\n        \tvar newTd0 = newTr.insertCell();\r\n        \tvar newTd1 = newTr.insertCell();\r\n       \t\tvar newTd2 = newTr.insertCell();\r\n       \t\tvar newTd3 = newTr.insertCell();\r\n        \tvar newTd4 = newTr.insertCell();\r\n       \t\tvar newTd5 = newTr.insertCell();\r\n       \t\tvar newTd6 = newTr.insertCell();\r\n       \t\t//var newTd7 = newTr.insertCell();\r\n        \t//设置列内容和属性\r\n        \tnewTd0.innerHTML = id;\r\n        \tnewTd1.innerHTML = name;\r\n        \tnewTd2.innerHTML = brand;\r\n        \tnewTd3.innerHTML = time;\r\n        \tnewTd4.innerHTML = orign;\r\n");
      out.write("        \t//newTd5.innerHTML = system;\r\n        \t//newTd6.innerHTML = color;\r\n        \tnewTd5.innerHTML = price;    \r\n        \tnewTd6.innerHTML = url;\r\n        \t}\r\n    ");
      out.write("</script>  \r\n");
      out.write("</head>\r\n");
      out.write("<body onload=sendRequest()>\r\n");
      out.write("<h1 align=\"center\">京东百货");
      out.write("</h1>\r\n        ");
      out.write("<TABLE id=\"tbNew\">\r\n         ");
      out.write("<thead>");
      out.write("<th>商品编号");
      out.write("</th>");
      out.write("<th>商品名称");
      out.write("</th>");
      out.write("<th>品牌");
      out.write("</th>");
      out.write("<th>上架时间");
      out.write("</th>");
      out.write("<th>产地");
      out.write("</th>");
      out.write("<th>价格");
      out.write("</th>");
      out.write("<th>链接");
      out.write("</th>");
      out.write("</thead>  \r\n        ");
      out.write("</TABLE> \r\n        \r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      out = _jspx_out;
      if (out != null && out.getBufferSize() != 0)
        out.clearBuffer();
      if (pageContext != null) pageContext.handlePageException(t);
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(pageContext);
    }
  }
}
