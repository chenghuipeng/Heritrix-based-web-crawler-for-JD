<%@ page contentType="text/html; charset=gb2312" %>  
<%@page import="test.PhoneModel" %>
<%@page import="test.Model" %>
<%@page import="test.JsoupTest" %>
    <%  
    //设置输出信息的格式及字符集  
    response.setContentType("text/xml; charset=UTF-8");  
    response.setHeader("Cache-Control","no-cache");  
    out.println("<response>");  

    //PhoneModel model = JsoupTest.result_phone.poll();
    Model model = JsoupTest.result_goods.poll();
    if(model!=null)
    {
      String s = JsoupTest.drawtablebody(model);
      out.println("<str>"+s+"</str>");
    }
    out.println("</response>");  
    out.close();  
    %>